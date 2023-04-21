package visitpc.filetransfer.dest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import java.io.*;
import visitpc.filetransfer.messages.*;
import java.util.*;
import visitpc.FileUtil;
import visitpc.VisitPCConstants;
import java.util.zip.*;

public class FileTransferHandler extends Thread {

    private DataInputStream dis;

    private DataOutputStream dos;

    private FileTransferObjectMover objectMover;

    private DirDetailsObject dirDetails;

    private StartToRemoteFileCopyObject startToRemoteFileCopyObject;

    private File newFile;

    private File partFile;

    private BufferedOutputStream filePartBOS;

    private long fileRXByteCount;

    private ErrorObject errorObject;

    private double lastRxFilePartTime;

    private int txBlockSize = VisitPCConstants.MIN_BLOCK_SIZE;

    public FileTransferHandler(Socket socket) throws IOException {
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
        objectMover = new FileTransferObjectMover();
    }

    @Override
    public void run() {
        try {
            objectMover.sendObject(dos, new FileTransferServerGreetingObject());
            while (dis != null) {
                Object obj = objectMover.getObject(dis);
                if (obj instanceof SetDirObject) {
                    setDir((SetDirObject) obj, false);
                } else if (obj instanceof SetParentDirObject) {
                    SetParentDirObject setParentDirObject = (SetParentDirObject) obj;
                    SetDirObject setDirObject = new SetDirObject();
                    setDirObject.dir = setParentDirObject.dir;
                    setDir(setDirObject, true);
                } else if (obj instanceof AddDirObject) {
                    addDir((AddDirObject) obj);
                } else if (obj instanceof DelDirEntriesObject) {
                    delEntries((DelDirEntriesObject) obj);
                } else if (obj instanceof StartToRemoteFileCopyObject) {
                    initCopyToRemote((StartToRemoteFileCopyObject) obj);
                } else if (obj instanceof TransferFilePartObject) {
                    saveFilePart((TransferFilePartObject) obj);
                } else if (obj instanceof RemoteFileDetailsObject) {
                    sendRemoteFileDetails((RemoteFileDetailsObject) obj);
                } else if (obj instanceof StartToLocalFileCopyObject) {
                    sendFileToLocal((StartToLocalFileCopyObject) obj);
                } else if (obj instanceof FilePartReceivedObject) {
                    processPartFileReceivedObject((FilePartReceivedObject) obj);
                } else if (obj instanceof RenameRemoteObject) {
                    renameRemoteEntry((RenameRemoteObject) obj);
                } else if (obj instanceof ErrorObject) {
                    errorObject = (ErrorObject) obj;
                } else if (obj instanceof IntCommandObject) {
                    processIntCommand((IntCommandObject) obj);
                }
            }
        } catch (IOException e) {
        }
    }

    private void processIntCommand(IntCommandObject intCommandObject) {
        switch(intCommandObject.command) {
            case IntCommandObject.RESTART:
                System.exit(10);
            case IntCommandObject.SHUTDOWN:
                System.exit(0);
        }
    }

    /**
   * Set the given director, or it's parent if upToParent is true.
   * 
   * @param newDir
   *          The full path of the new directory.
   * @throws IOException
   */
    public void setDir(SetDirObject setDir, boolean upToParent) throws IOException {
        if (setDir.dir == null || setDir.dir.length() == 0) {
            setDir.dir = System.getProperty("user.dir");
        }
        File _newDir = new File(setDir.dir);
        if (upToParent) {
            File parentDir = _newDir.getParentFile();
            if (parentDir != null) {
                _newDir = parentDir;
            }
        }
        if (_newDir.isDirectory()) {
            if (_newDir.canRead()) {
                dirDetails = new DirDetailsObject();
                dirDetails.dir = _newDir.getAbsolutePath();
                File entries[] = _newDir.listFiles();
                Vector<String> dirs = new Vector<String>();
                Vector<String> files = new Vector<String>();
                for (File entry : entries) {
                    if (entry.isDirectory()) {
                        dirs.add(entry.getName());
                    } else {
                        files.add(entry.getName());
                    }
                }
                String dirArray[] = new String[dirs.size()];
                String fileArray[] = new String[files.size()];
                for (int i = 0; i < dirs.size(); i++) {
                    dirArray[i] = dirs.get(i);
                }
                for (int i = 0; i < files.size(); i++) {
                    fileArray[i] = files.get(i);
                }
                dirDetails.dirs = dirArray;
                dirDetails.files = fileArray;
                objectMover.sendObject(dos, dirDetails);
            } else {
                sendErrorMessage("No read access to " + setDir.dir);
            }
        } else {
            sendErrorMessage("Failed to set the remote path to " + setDir.dir);
        }
    }

    public void addDir(AddDirObject addDirObject) throws IOException {
        if (dirDetails != null) {
            File currentDir = new File(dirDetails.dir);
            if (currentDir.isDirectory()) {
                if (currentDir.canWrite()) {
                    File newDir = new File(dirDetails.dir, addDirObject.dir);
                    if (newDir.mkdirs()) {
                        SetDirObject setDirObject = new SetDirObject();
                        setDirObject.dir = newDir.getAbsolutePath();
                        setDir(setDirObject, false);
                    } else {
                        sendErrorMessage("Failed to create the " + newDir.getAbsolutePath() + " directory on the remote machine");
                    }
                } else {
                    sendErrorMessage("No write access to the " + currentDir + " directory on the remote machine");
                }
            } else {
                sendErrorMessage("The " + currentDir + " does not exist.");
            }
        } else {
            sendErrorMessage("Server does not know which dir to add the " + addDirObject.dir + " directory to");
        }
    }

    public void delEntries(DelDirEntriesObject delDirEntriesObject) throws IOException {
        if (dirDetails != null) {
            File currentDir = new File(dirDetails.dir);
            if (currentDir.isDirectory()) {
                if (currentDir.canWrite()) {
                    for (String entry : delDirEntriesObject.entries) {
                        File newDir = new File(dirDetails.dir, entry);
                        if (newDir.delete()) {
                            SetDirObject setDirObject = new SetDirObject();
                            setDirObject.dir = dirDetails.dir;
                            setDir(setDirObject, false);
                        } else {
                            sendErrorMessage("Failed to delete the " + newDir.getAbsolutePath() + " on the remote machine");
                        }
                    }
                } else {
                    sendErrorMessage("No write access to the " + currentDir + " directory on the remote machine");
                }
            } else {
                sendErrorMessage("The " + currentDir + " does not exist and so the selected entries cannot be deleted.");
            }
        } else {
            sendErrorMessage("Server does not know which dir to delete the entries from");
        }
    }

    public void renameRemoteEntry(RenameRemoteObject renameRemoteObject) throws IOException {
        if (dirDetails != null) {
            File currentDir = new File(dirDetails.dir);
            if (currentDir.isDirectory()) {
                if (currentDir.canWrite()) {
                    File fromF = new File(dirDetails.dir, renameRemoteObject.from);
                    File toF = new File(dirDetails.dir, renameRemoteObject.to);
                    if (fromF.renameTo(toF)) {
                        SetDirObject setDirObject = new SetDirObject();
                        setDirObject.dir = dirDetails.dir;
                        setDir(setDirObject, false);
                    } else {
                        sendErrorMessage("Failed to rename the " + renameRemoteObject.from + " to " + renameRemoteObject.to);
                    }
                } else {
                    sendErrorMessage("No write access to the " + currentDir + " directory on the remote machine");
                }
            } else {
                sendErrorMessage("The " + currentDir + " does not exist and so files/dirs cannot be renamed.");
            }
        } else {
            sendErrorMessage("Server does not know which dir to rename the files/dirs");
        }
    }

    public void initCopyToRemote(StartToRemoteFileCopyObject startToRemoteFileCopyObject) throws IOException {
        boolean error = false;
        this.startToRemoteFileCopyObject = startToRemoteFileCopyObject;
        newFile = new File(dirDetails.dir, startToRemoteFileCopyObject.file);
        File currentDir = new File(dirDetails.dir);
        if (startToRemoteFileCopyObject.length <= currentDir.getFreeSpace()) {
            if (newFile.exists()) {
                if (newFile.length() > 0) {
                    error = sendErrorMessage("The file " + startToRemoteFileCopyObject.file + " already exists on the remote machine");
                } else {
                    if (!newFile.delete()) {
                        error = sendErrorMessage("Failed to delete the zero length " + startToRemoteFileCopyObject.file + " file that already exists on the remote machine");
                    }
                }
            }
            if (!error) {
                if (newFile.createNewFile()) {
                    partFile = new File(newFile.getAbsolutePath() + ".part");
                    if (partFile.exists() && !partFile.delete()) {
                        error = sendErrorMessage("Unable to delete the " + partFile + " on the remote machine");
                    }
                    if (!error) {
                        try {
                            fileRXByteCount = 0;
                            objectMover.sendObject(dos, startToRemoteFileCopyObject);
                        } catch (Exception e) {
                            error = sendErrorMessage("Failed to open the " + partFile + " on the remote machine");
                        }
                    }
                } else {
                    error = sendErrorMessage("Unable to create the " + startToRemoteFileCopyObject.file + " on the remote machine");
                }
            }
        } else {
            error = sendErrorMessage("Not enough space on the remote machine to store " + startToRemoteFileCopyObject.file);
        }
    }

    public void saveFilePart(TransferFilePartObject transferFilePartObject) throws IOException {
        FilePartReceivedObject filePartReceivedObject;
        try {
            byte decompressed_buffer[];
            int decompressed_buffer_length;
            if (transferFilePartObject.compressed) {
                Inflater decompresser = new Inflater();
                decompresser.setInput(transferFilePartObject.buffer, 0, transferFilePartObject.buffer.length);
                decompressed_buffer = new byte[visitpc.VisitPCConstants.MAX_BLOCK_SIZE];
                decompressed_buffer_length = decompresser.inflate(decompressed_buffer);
                decompresser.end();
            } else {
                decompressed_buffer_length = transferFilePartObject.buffer.length;
                decompressed_buffer = transferFilePartObject.buffer;
            }
            if (filePartBOS == null) {
                filePartBOS = new BufferedOutputStream(new FileOutputStream(partFile.getAbsolutePath(), true));
            }
            filePartBOS.write(decompressed_buffer, 0, decompressed_buffer_length);
            fileRXByteCount += decompressed_buffer_length;
            filePartBOS.close();
            filePartBOS = null;
            filePartReceivedObject = new FilePartReceivedObject();
            filePartReceivedObject.rxByteCount = decompressed_buffer_length;
            objectMover.sendObject(dos, filePartReceivedObject);
            if (fileRXByteCount == startToRemoteFileCopyObject.length) {
                if (newFile.delete()) {
                    long partFileCRC32 = FileUtil.GetLocalFileCrc32(partFile);
                    if (partFileCRC32 == startToRemoteFileCopyObject.crc32) {
                        if (partFile.renameTo(newFile)) {
                            objectMover.sendObject(dos, new FileTransferOKObject());
                            initToRemoteVars();
                        } else {
                            sendErrorMessage("Failed to rename " + partFile.getName() + " to " + newFile.getName());
                        }
                    } else {
                        sendErrorMessage("Transfer completed but source and destination files are not the same.");
                    }
                } else {
                    sendErrorMessage("Failed to delete the " + newFile + " placeholder file on the remote machine");
                }
            }
        } catch (Exception e) {
            sendErrorMessage("Failed write to the " + partFile + " on the remote machine");
        }
    }

    public void sendRemoteFileDetails(RemoteFileDetailsObject remoteFileDetailsObject) throws IOException {
        File file = new File(dirDetails.dir, remoteFileDetailsObject.file);
        if (file.exists()) {
            remoteFileDetailsObject.length = file.length();
            remoteFileDetailsObject.crc32 = FileUtil.GetLocalFileCrc32(file);
            objectMover.sendObject(dos, remoteFileDetailsObject);
        } else {
            sendErrorMessage("The " + file.getName() + " does not exist on the remote machine");
        }
    }

    /**
   * Send an error message to the client.
   * 
   * @param message
   *          The error message.
   * @returns True
   * @throws IOException
   */
    public boolean sendErrorMessage(String message) throws IOException {
        ErrorObject error = new ErrorObject();
        error.message = message;
        objectMover.sendObject(dos, error);
        return true;
    }

    public void sendFileToLocal(StartToLocalFileCopyObject startToLocalFileCopyObject) {
        class SendFileToLocalThread extends Thread {

            private String filename;

            public SendFileToLocalThread(String filename) {
                this.filename = filename;
            }

            @Override
            public void run() {
                int bytecountReadFromFile;
                byte[] txFileBuffer = new byte[VisitPCConstants.MAX_BLOCK_SIZE];
                try {
                    File f = new File(dirDetails.dir, filename);
                    if (!f.isFile()) {
                        sendErrorMessage(filename + " file not found.");
                    }
                    if (!f.canRead()) {
                        sendErrorMessage("No read access for the " + filename + " file.");
                    } else {
                        FileInputStream fis = null;
                        try {
                            fis = new FileInputStream(f);
                            errorObject = null;
                            while (errorObject == null) {
                                bytecountReadFromFile = fis.read(txFileBuffer, 0, txBlockSize);
                                if (bytecountReadFromFile == -1) {
                                    break;
                                }
                                Deflater compresser = new Deflater();
                                compresser.setInput(txFileBuffer, 0, bytecountReadFromFile);
                                compresser.finish();
                                byte compressed_buffer[] = new byte[bytecountReadFromFile];
                                int compressedDataLength = compresser.deflate(compressed_buffer);
                                boolean compressed = false;
                                if (compressedDataLength < bytecountReadFromFile) {
                                    compressed = true;
                                } else {
                                    compressedDataLength = bytecountReadFromFile;
                                    compressed_buffer = txFileBuffer;
                                }
                                sendFilePart(f, compressed_buffer, compressedDataLength, compressed);
                            }
                            if (errorObject != null) {
                                throw new IOException(errorObject.message);
                            }
                        } finally {
                            try {
                                fis.close();
                            } catch (Exception e) {
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        SendFileToLocalThread sendFileToLocalThread = new SendFileToLocalThread(startToLocalFileCopyObject.file);
        sendFileToLocalThread.start();
    }

    private void processPartFileReceivedObject(FilePartReceivedObject filePartReceivedObject) {
        double blockTransitTime = 0;
        double currentRxFilePartTime = System.currentTimeMillis();
        if (lastRxFilePartTime != -1) {
            blockTransitTime = (currentRxFilePartTime - lastRxFilePartTime) / 1E3;
            if (blockTransitTime < visitpc.filetransfer.src.FileTransferHandler.MAX_XFER_BLOCK_DELAY / 2) {
                txBlockSize = txBlockSize * 2;
                if (txBlockSize > VisitPCConstants.MAX_BLOCK_SIZE) {
                    txBlockSize = VisitPCConstants.MAX_BLOCK_SIZE;
                }
            } else if (blockTransitTime > visitpc.filetransfer.src.FileTransferHandler.MAX_XFER_BLOCK_DELAY) {
                txBlockSize = txBlockSize / 2;
                if (txBlockSize < VisitPCConstants.MIN_BLOCK_SIZE) {
                    txBlockSize = VisitPCConstants.MIN_BLOCK_SIZE;
                }
            }
        }
        lastRxFilePartTime = currentRxFilePartTime;
    }

    /**
   * Send a part of a file to the vnct client
   * 
   * @param buffer
   *          The buffer holding the file contents
   * @param length
   *          The length (from offset 0) of the data in the buffer.
   */
    public void sendFilePart(File f, byte[] buffer, int length, boolean compressed) throws IOException {
        byte tmpBuffer[];
        if (buffer.length == length) {
            tmpBuffer = buffer;
        } else {
            tmpBuffer = new byte[length];
            System.arraycopy(buffer, 0, tmpBuffer, 0, length);
        }
        TransferFilePartObject transferFilePartObject = new TransferFilePartObject();
        transferFilePartObject.compressed = compressed;
        transferFilePartObject.buffer = tmpBuffer;
        objectMover.sendObject(dos, transferFilePartObject);
    }

    private void initToRemoteVars() {
        startToRemoteFileCopyObject = null;
        newFile = null;
        partFile = null;
        filePartBOS = null;
        fileRXByteCount = 0;
    }

    public void close() {
        if (dis != null) {
            try {
                dis.close();
            } catch (IOException e) {
            }
            dis = null;
            dos = null;
        }
    }
}
