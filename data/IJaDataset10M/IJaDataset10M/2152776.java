package andre.grids.filesystem.client;

import java.io.*;
import andre.grids.guids.*;
import andre.grids.filesystem.common.*;

/**
 *
 * @author andre
 */
public class RemoteFileOutputStream extends OutputStream {

    private IGuid fileGuid;

    private FileOperations fileOperations;

    private long filePointer;

    public RemoteFileOutputStream(IGuid remoteFileGuid, FileOperations remoteFileOp) {
        filePointer = 0;
        fileGuid = remoteFileGuid;
        fileOperations = remoteFileOp;
    }

    @Override
    public void write(int b) throws IOException {
        WriteOperation op = fileOperations.write(fileGuid, filePointer, new byte[] { (byte) b });
        filePointer += op.getTotalWrite();
    }

    @Override
    public void close() throws IOException {
        fileGuid = null;
        fileOperations = null;
        filePointer = -1;
        super.close();
    }

    @Override
    public void flush() throws IOException {
        return;
    }

    @Override
    public void write(byte[] b) throws IOException {
        WriteOperation op = fileOperations.write(fileGuid, filePointer, b);
        filePointer += op.getTotalWrite();
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        byte buff[] = new byte[len];
        System.arraycopy(b, off, buff, 0, len);
        WriteOperation op = fileOperations.write(fileGuid, filePointer, buff);
        filePointer += op.getTotalWrite();
    }
}
