package edu.virginia.cs.storagedesk.storageserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.rmi.ConnectException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import edu.virginia.cs.storagedesk.common.Disk;
import edu.virginia.cs.storagedesk.common.ISCSI;
import edu.virginia.cs.storagedesk.common.Config;
import edu.virginia.cs.storagedesk.common.Util;
import edu.virginia.cs.storagedesk.database.Mapping;
import edu.virginia.cs.storagedesk.database.Volume;
import edu.virginia.cs.storagedesk.storagemachine.IStorageMachine;
import edu.virginia.cs.storagedesk.volumecontroller.IVolumeController;

public class VirtualDisk extends Disk {

    private static Logger logger = Logger.getLogger(VirtualDisk.class);

    HashMap<String, Registry> machineRegistry = new HashMap<String, Registry>();

    HashMap<String, IStorageMachine> storageMachines = new HashMap<String, IStorageMachine>();

    HashMap<String, Boolean> machineStatus = new HashMap<String, Boolean>();

    ArrayList<Boolean> replicaStatus;

    private Registry controllerRegistry;

    private IVolumeController volumeController;

    private Volume volume;

    private Mapping[][] mappings;

    private String path;

    private String jdataPath;

    private Version version;

    private Version[] virtualChunkVersion;

    private HashMap<String, Version> physicalChunkVersion = new HashMap<String, Version>();

    private Journal journal;

    public VirtualDisk(int numCopies) {
        super();
        this.volume = new Volume(Config.TARGET_NAME, numCopies, this.getNumLUNs(), this.getNumBlocks(), this.getBlockSize(), (int) Math.ceil((ISCSI.DEFAULT_DISK_NUM_LUNS * ISCSI.DEFAULT_DISK_BLOCK_SIZE * ISCSI.DEFAULT_DISK_NUM_BLOCKS) / Config.VOLUME_CHUNK_SIZE), Config.VOLUME_CHUNK_SIZE);
        boolean isNewVolume = true;
        try {
            controllerRegistry = LocateRegistry.getRegistry(Config.VOLUMECONTROLLER_IP_ADDRESS, Config.SD_SOCKET_PORT);
            volumeController = (IVolumeController) controllerRegistry.lookup("VolumeController");
            isNewVolume = volumeController.isNewVolume(volume);
            if (isNewVolume) {
                logger.error("Trying to create a new volume, use admin console instead.");
                return;
            }
            volume.setId(volumeController.registerVolume(volume));
            if (volume.getId() > -1) {
                logger.info("Volume registration OK and id is " + volume.getId());
            } else {
                logger.info("Volume registration failed");
                return;
            }
            this.volume = volumeController.assignMapping(volume);
            this.mappings = volume.getMappings();
            if (mappings.length != numCopies) {
                logger.error("SHOULD HAVE " + numCopies + " COPIES");
                return;
            }
        } catch (Exception e) {
            logger.error(Util.getStackTrace(e));
        }
        path = "." + File.separator + volume.getId();
        jdataPath = path + File.separator + "jdata";
        String versionFilename = path + File.separator + "volume.ver";
        File f = new File(versionFilename);
        isNewVolume = !f.exists();
        try {
            if (isNewVolume) {
                logger.debug("set up env for new volume");
                new File(path).mkdir();
                new File(versionFilename).createNewFile();
                version = new Version(versionFilename);
                version.setVersionNumber((long) 0);
                new File(jdataPath).mkdir();
                for (int i = 0; i < volume.getNumChunks(); i++) {
                    String cpath = path + File.separator + "vchunk" + i;
                    new File(cpath).mkdir();
                    new File(cpath + File.separator + "vchunk" + i + ".ver").createNewFile();
                }
            } else {
                logger.debug("existing volume");
                version = new Version(versionFilename);
            }
        } catch (IOException e) {
            logger.error(Util.getStackTrace(e));
        }
        journal = new Journal(jdataPath);
        logger.debug("Volume version " + version.getVersionNumber());
        virtualChunkVersion = new Version[volume.getNumChunks()];
        for (int i = 0; i < volume.getNumChunks(); i++) {
            virtualChunkVersion[i] = new Version(path + File.separator + "vchunk" + i + File.separator + "vchunk" + i + ".ver");
        }
        replicaStatus = new ArrayList<Boolean>(mappings.length);
        logger.debug("replica status " + replicaStatus.size());
        for (int replica = 0; replica < mappings.length; replica++) {
            replicaStatus.add(replica, true);
            for (int i = 0; i < mappings[replica].length; i++) {
                logger.info("Init for replica " + replica + " chunk " + i);
                Mapping mapping = mappings[replica][i];
                try {
                    logger.info("Getting the registry of the machine (id " + mapping.getMachineID() + ") ip " + mapping.getIp());
                    machineRegistry.put(mapping.getMachineID(), LocateRegistry.getRegistry(mapping.getIp(), Config.SD_SOCKET_PORT));
                    logger.info("Looking up the stub of the machine (id " + mapping.getMachineID() + ") ip " + mapping.getIp());
                    IStorageMachine machineStub = (IStorageMachine) machineRegistry.get(mapping.getMachineID()).lookup(mapping.getMachineID());
                    logger.info("Got the stub of the machine (id " + mapping.getMachineID() + ") ip " + mapping.getIp());
                    storageMachines.put(mapping.getMachineID(), machineStub);
                    logger.info("Init machine (id " + mapping.getMachineID() + ")");
                    machineStub.init();
                    machineStatus.put(mapping.getMachineID(), true);
                    logger.info("Machine ready (id " + mapping.getMachineID() + ")");
                } catch (Exception e) {
                    machineStatus.put(mapping.getMachineID(), false);
                    replicaStatus.set(replica, false);
                    logger.error(Util.getStackTrace(e));
                }
                String cpath = path + File.separator + "vchunk" + i;
                String mfilename = cpath + File.separator + mapping.getMachineID() + "." + mapping.getPhyscialChunkID() + ".ver";
                File mf = new File(mfilename);
                if (mf.exists() == false) {
                    try {
                        mf.createNewFile();
                    } catch (IOException e) {
                        logger.error(Util.getStackTrace(e));
                    }
                }
                physicalChunkVersion.put(mapping.getMachineID() + "." + mapping.getPhyscialChunkID(), new Version(mfilename));
            }
        }
        new Thread(new Writer()).start();
        new Thread(new Prober()).start();
        logger.info("Virtual Disk inits");
    }

    public byte[] read(long position, int length) {
        logger.debug("Read " + length + " bytes at " + position);
        byte[] bytes = new byte[1];
        boolean[] tryReplica = new boolean[mappings.length];
        for (int i = 0; i < tryReplica.length; i++) {
            tryReplica[i] = false;
        }
        int virtualChunk = (int) Math.floor(position / this.volume.getChunkSize());
        bytes = new byte[length];
        int numBytesLeft = length;
        int count = 0;
        int cursor = (int) (position - virtualChunk * this.volume.getChunkSize());
        while (numBytesLeft > 0) {
            long numBytesAdjusted = numBytesLeft;
            if ((cursor + numBytesLeft) > this.volume.getChunkSize()) {
                numBytesAdjusted = this.volume.getChunkSize() - cursor;
            }
            byte[] result = new byte[1];
            for (int replica = 0; replica < mappings.length; replica++) {
                if (replicaStatus.get(replica)) {
                    logger.debug("Try replica " + replica);
                } else {
                    logger.debug("Replica " + replica + " has been marked down");
                    continue;
                }
                Mapping mapping = mappings[replica][virtualChunk];
                IStorageMachine machineStub = storageMachines.get(mapping.getMachineID());
                if (machineStatus.get(mapping.getMachineID()) == false) {
                    logger.debug("Machine " + mapping.getMachineID() + " has been marked down");
                    continue;
                }
                boolean readable = true;
                while (physicalChunkVersion.get(mapping.getMachineID() + "." + mapping.getPhyscialChunkID()).getVersionNumber() != virtualChunkVersion[virtualChunk].getVersionNumber()) {
                    logger.debug("waiting for writes");
                    try {
                        machineStub.isAlive();
                        Thread.sleep(3000);
                    } catch (ConnectException e) {
                        logger.error("Unable to connect to the storage machine");
                        readable = false;
                        break;
                    } catch (RemoteException e) {
                        logger.error("Unable to read from replica " + replica);
                        logger.error(Util.getStackTrace(e));
                        readable = false;
                        break;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (readable == false) {
                    replicaStatus.set(replica, false);
                    machineStatus.put(mapping.getMachineID(), false);
                    continue;
                }
                logger.debug("Replica " + replica + " has the latest version");
                logger.debug("Read for replica " + replica + " virtual chunk " + virtualChunk + " from machine (id " + mapping.getMachineID() + ") at " + cursor);
                boolean readOK = true;
                try {
                    result = machineStub.read(mapping.getPhyscialChunkID(), cursor, (int) numBytesAdjusted);
                } catch (ConnectException e) {
                    logger.error("Unable to connect to the storage machine");
                    readOK = false;
                } catch (RemoteException e) {
                    logger.error("Unable to read from replica " + replica);
                    logger.error(Util.getStackTrace(e));
                    readOK = false;
                }
                if (readOK == false) {
                    replicaStatus.set(replica, false);
                    machineStatus.put(mapping.getMachineID(), false);
                    continue;
                }
                System.arraycopy(result, 0, bytes, count, result.length);
                logger.debug("Read " + result.length + " bytes from replica " + replica + ", machine (id " + mapping.getMachineID() + ") at " + cursor);
                break;
            }
            count += result.length;
            numBytesLeft -= result.length;
            if (numBytesLeft > 0) {
                virtualChunk++;
                cursor = 0;
            }
        }
        return bytes;
    }

    public boolean write(byte[] bytes, long position) {
        logger.debug("Write to DISK " + bytes.length + " bytes at " + position);
        long ver = version.increment();
        logger.debug("Start journaling for update version " + ver);
        try {
            File datafile = new File(jdataPath + File.separator + ver + ".dat");
            File auxfile = new File(jdataPath + File.separator + ver + ".aux");
            if (datafile.exists() == true || auxfile.exists() == true) {
                logger.error("Version " + ver + " existed!");
                return false;
            }
            logger.debug("Writing data to the data file (size " + datafile.length() + ")");
            FileChannel wChannel = new FileOutputStream(datafile, false).getChannel();
            wChannel.write(ByteBuffer.wrap(bytes));
            wChannel.close();
            logger.debug("data file size is " + datafile.length());
            logger.debug("Writing data to the aux file (size " + auxfile.length() + ")");
            RandomAccessFile posfile = new RandomAccessFile(auxfile, "rw");
            posfile.writeLong(position);
            posfile.writeInt(bytes.length);
            posfile.close();
            logger.debug("Writing data to the aux file (size " + auxfile.length() + ")");
            int virtualChunk = (int) Math.floor(position / this.volume.getChunkSize());
            int numBytesLeft = bytes.length;
            int count = 0;
            long cursor = (position - virtualChunk * this.volume.getChunkSize());
            while (numBytesLeft > 0) {
                virtualChunkVersion[virtualChunk].setVersionNumber(ver);
                long numBytesAdjusted = numBytesLeft;
                if ((cursor + numBytesLeft) > this.volume.getChunkSize()) {
                    numBytesAdjusted = this.volume.getChunkSize() - cursor;
                }
                count += numBytesAdjusted;
                numBytesLeft -= numBytesAdjusted;
                if (numBytesLeft > 0) {
                    virtualChunk++;
                    cursor = 0;
                }
            }
        } catch (IOException e) {
            logger.error(Util.getStackTrace(e));
        }
        journal.add(new Long(ver));
        logger.debug("journal size " + journal.size());
        logger.debug("End journaling for update version " + ver);
        return true;
    }

    private boolean writeTask(byte[] bytes, long position, long ver) {
        logger.debug("write task begins");
        boolean success = true;
        int virtualChunk = (int) Math.floor(position / this.volume.getChunkSize());
        int numBytesLeft = bytes.length;
        int count = 0;
        long cursor = (position - virtualChunk * this.volume.getChunkSize());
        while (numBytesLeft > 0) {
            long numBytesAdjusted = numBytesLeft;
            if ((cursor + numBytesLeft) > this.volume.getChunkSize()) {
                numBytesAdjusted = this.volume.getChunkSize() - cursor;
            }
            for (int replica = 0; replica < mappings.length; replica++) {
                if (replicaStatus.get(replica)) {
                    logger.debug("Try replica " + replica);
                } else {
                    logger.debug("Replica " + replica + " has been marked down");
                    success = false;
                    continue;
                }
                Mapping mapping = mappings[replica][virtualChunk];
                IStorageMachine machineStub = storageMachines.get(mapping.getMachineID());
                if (machineStatus.get(mapping.getMachineID()) == false) {
                    logger.debug("Machine " + mapping.getMachineID() + " has been marked down");
                    success = false;
                    continue;
                }
                logger.debug("Write for replica " + replica + " virtual chunk " + virtualChunk + " from machine (id " + mapping.getMachineID() + ") at " + cursor);
                if (ver > physicalChunkVersion.get(mapping.getMachineID() + "." + mapping.getPhyscialChunkID()).getVersionNumber()) {
                    byte[] portion = new byte[(int) numBytesAdjusted];
                    System.arraycopy(bytes, count, portion, 0, (int) numBytesAdjusted);
                    boolean writeOK = true;
                    try {
                        machineStub.write(mapping.getPhyscialChunkID(), portion, cursor);
                    } catch (ConnectException e) {
                        logger.error("Unable to connect to the storage machine");
                        logger.error(Util.getStackTrace(e));
                        writeOK = false;
                    } catch (RemoteException e) {
                        logger.error("Unable to write the storage machine");
                        logger.error(Util.getStackTrace(e));
                        writeOK = false;
                    }
                    if (writeOK == false) {
                        replicaStatus.set(replica, false);
                        machineStatus.put(mapping.getMachineID(), false);
                        success = false;
                        continue;
                    }
                    physicalChunkVersion.get(mapping.getMachineID() + "." + mapping.getPhyscialChunkID()).setVersionNumber(ver);
                } else {
                    logger.debug("No need to update machine " + mapping.getMachineID());
                }
                logger.debug("Write " + numBytesAdjusted + " bytes from machine (id " + mapping.getMachineID() + ") at " + cursor);
            }
            count += numBytesAdjusted;
            numBytesLeft -= numBytesAdjusted;
            if (numBytesLeft > 0) {
                virtualChunk++;
                cursor = 0;
            }
        }
        logger.debug("Write ends");
        return success;
    }

    private class Prober implements Runnable {

        public void run() {
            for (; ; ) {
                double r = Math.random() % 2;
                if (r == 0) {
                    logger.debug("Running prober to update ");
                    for (int replica = 0; replica < mappings.length; replica++) {
                        if (replicaStatus.get(replica)) {
                            continue;
                        } else {
                            logger.debug("Replica " + replica + " has been marked down");
                        }
                        boolean status = true;
                        for (int i = 0; i < mappings[replica].length; i++) {
                            logger.info("Re-Init for replica " + replica + " chunk " + i);
                            Mapping mapping = mappings[replica][i];
                            if (machineStatus.get(mapping.getMachineID())) {
                                continue;
                            } else {
                                logger.debug("Machine " + mapping.getMachineID() + " has been marked down");
                            }
                            try {
                                logger.info("Re-Getting the registry of the machine (id " + mapping.getMachineID() + ") ip " + mapping.getIp());
                                machineRegistry.put(mapping.getMachineID(), LocateRegistry.getRegistry(mapping.getIp(), Config.SD_SOCKET_PORT));
                                logger.info("Re-Looking up the stub of the machine (id " + mapping.getMachineID() + ") ip " + mapping.getIp());
                                IStorageMachine machineStub = (IStorageMachine) machineRegistry.get(mapping.getMachineID()).lookup(mapping.getMachineID());
                                logger.info("Re-Got the stub of the machine (id " + mapping.getMachineID() + ") ip " + mapping.getIp());
                                storageMachines.put(mapping.getMachineID(), machineStub);
                                logger.info("Re-Init machine (id " + mapping.getMachineID() + ")");
                                machineStub.init();
                                machineStatus.put(mapping.getMachineID(), true);
                                logger.info("Machine ready (id " + mapping.getMachineID() + ")");
                            } catch (Exception e) {
                                machineStatus.put(mapping.getMachineID(), false);
                                replicaStatus.set(replica, false);
                                logger.error(Util.getStackTrace(e));
                                status = false;
                            }
                        }
                        replicaStatus.set(replica, status);
                    }
                } else {
                    try {
                        Thread.sleep(0, 1000);
                    } catch (InterruptedException e) {
                        logger.error(Util.getStackTrace(e));
                    }
                }
            }
        }
    }

    private class Writer implements Runnable {

        public void run() {
            for (; ; ) {
                int size = journal.size();
                if (size > 0) {
                    logger.debug("To get the journal queue size " + journal.size());
                    Long[] queue = journal.get();
                    logger.debug("Writer is running with size " + queue.length);
                    for (long ver : queue) {
                        logger.debug("Read, from the journal, entry of Version " + ver);
                        logger.debug("Writing updates to disks");
                        File datafile = new File(jdataPath + File.separator + ver + ".dat");
                        File auxfile = new File(jdataPath + File.separator + ver + ".aux");
                        if (datafile.exists() == false || auxfile.exists() == false) {
                            logger.error("Version " + ver + " does not existed!");
                            continue;
                        }
                        long position = 0;
                        int length;
                        ByteBuffer buffer = null;
                        try {
                            RandomAccessFile posfile = new RandomAccessFile(auxfile, "r");
                            position = posfile.readLong();
                            length = posfile.readInt();
                            posfile.close();
                            logger.debug("Read from aux file " + position + ", " + length);
                            FileChannel wChannel = new FileInputStream(datafile).getChannel();
                            buffer = ByteBuffer.allocate(length);
                            wChannel.read(buffer);
                            wChannel.close();
                            logger.debug("Read from data file, bytes size " + buffer.capacity());
                        } catch (IOException e) {
                            logger.error(Util.getStackTrace(e));
                        }
                        byte[] bytes;
                        if (buffer != null) {
                            bytes = buffer.array();
                            if (writeTask(bytes, position, ver)) {
                                if (datafile.delete() == false || auxfile.delete() == false) {
                                    logger.error("Version " + ver + " could not be removed!");
                                } else {
                                    logger.debug("Removed Version " + ver);
                                }
                                journal.remove(ver);
                                logger.debug("Remove, from the journal, entry of Version " + ver);
                                logger.debug("The journal (size " + journal.size() + ")");
                            } else {
                                logger.debug("Not all replicas written, keep the journal");
                            }
                        }
                    }
                } else {
                    try {
                        Thread.sleep(0, 100);
                    } catch (InterruptedException e) {
                        logger.error(Util.getStackTrace(e));
                    }
                }
            }
        }
    }
}
