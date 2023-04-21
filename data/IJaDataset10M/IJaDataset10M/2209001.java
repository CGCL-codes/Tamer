package ants.p2p.filesharing;

import java.io.*;
import java.beans.*;
import ants.p2p.messages.*;
import org.apache.log4j.*;
import kerjodando.casper.util.ConstantUtil;

public class PartialFile {

    static Logger _logger = Logger.getLogger(PartialFile.class.getName());

    File f = null;

    File downloadInfos = null;

    String name;

    String source;

    long fileSize;

    byte[] hash;

    ByteWrapper[] fileParts;

    FilePushMessage fpm;

    public long lastId = 0;

    int groupFactor;

    long lastActivityTime = System.currentTimeMillis();

    PropertyChangeSupport pcs;

    public static int computeGroupFactor(int blockSize) {
        return 1;
    }

    public long getLastId() {
        return this.lastId;
    }

    public String getName() {
        return this.name;
    }

    public byte[] getHash() {
        return this.hash;
    }

    public long getLastActivityTime() {
        return this.lastActivityTime;
    }

    public PartialFile(String name, String source, byte[] hash, boolean resume, long offset, FilePushMessage fpm, PropertyChangeSupport pcs) throws Exception {
        name = WarriorAnt.chunksHome + name;
        downloadInfos = new File(name + ".tmp");
        if (downloadInfos.exists() && resume == false) {
            ObjectInputStream fis = new ObjectInputStream(new FileInputStream(downloadInfos));
            String oldName = (String) fis.readObject();
            fis.close();
            f = new File(oldName);
            if (f.exists()) f.delete();
            f = new File(name);
            if (f.exists()) f.delete();
            ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(downloadInfos, false));
            fos.writeObject(name);
            fos.close();
        } else if (!downloadInfos.exists() && resume == true) {
            throw new Exception("Resuming error");
        } else if (downloadInfos.exists() && resume == true) {
            ObjectInputStream fis = new ObjectInputStream(new FileInputStream(downloadInfos));
            String oldName = (String) fis.readObject();
            fis.close();
            f = new File(oldName);
            if (f.length() != offset) throw new Exception("Resuming error");
        } else {
            f = new File(name);
            if (f.exists()) f.delete();
            ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(downloadInfos, false));
            fos.writeObject(name);
            fos.close();
        }
        this.pcs = pcs;
        this.fpm = fpm;
        this.name = name;
        this.source = source;
        this.hash = hash;
        this.fileSize = fpm.getFileSize().longValue();
        this.groupFactor = computeGroupFactor(fpm.getBlockSize().intValue());
        fileParts = new ByteWrapper[groupFactor];
    }

    public FilePushMessage getFilePushMessage() {
        return this.fpm;
    }

    public synchronized void appendBytes(byte[] b, long id) {
        try {
            if (id < (lastId * groupFactor)) {
                return;
            }
            if (fileParts[(int) (id - (lastId * groupFactor))] != null) {
                return;
            }
            _logger.debug("Writing to memory part " + id);
            this.pcs.firePropertyChange("bytesGroupAppendedToFilePart", null, this);
            fileParts[(int) (id - (lastId * groupFactor))] = new ByteWrapper(b);
            if (receivedBlock()) {
                finalizeFile();
                this.pcs.firePropertyChange("byteBlockFinalizedInFilePart", null, this);
                lastId++;
            }
            this.lastActivityTime = System.currentTimeMillis();
        } catch (Exception e) {
            _logger.error("", e);
        }
    }

    public boolean receivedBlock() {
        for (int x = 0; x < fileParts.length; x++) {
            if (fileParts[x] == null) return false;
        }
        return true;
    }

    public synchronized void finalizeFile() {
        try {
            _logger.debug("Finalizing");
            FileOutputStream fos = new FileOutputStream(f, true);
            for (int x = 0; x < fileParts.length; x++) {
                if (fileParts[x] != null) {
                    fos.write(((ByteWrapper) fileParts[x]).content);
                } else break;
            }
            for (int x = 0; x < fileParts.length; x++) {
                fileParts[x] = null;
            }
            fos.close();
            this.downloadInfos.delete();
            System.gc();
        } catch (Exception e) {
            _logger.error("", e);
        }
    }

    public boolean equals(Object o) {
        if (o instanceof PartialFile) return compareHash(((PartialFile) o).hash, this.hash) && ((PartialFile) o).getFilePushMessage().equals(this.getFilePushMessage()); else return o == this;
    }

    public long getReceivedSize() {
        return f.length();
    }

    public static boolean compareHash(byte[] h1, byte[] h2) {
        if (h1.length != h2.length) return false;
        for (int x = 0; x < h1.length; x++) {
            if (h1[x] != h2[x]) return false;
        }
        return true;
    }
}

class ByteWrapper {

    byte[] content;

    public ByteWrapper(byte[] content) {
        this.content = content;
    }
}
