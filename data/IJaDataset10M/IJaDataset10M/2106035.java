package nacaLib.exceptions;

import jlib.misc.LogicalFileDescriptor;

public class FileDescriptorNofFoundException extends NacaBatchFileException {

    private static final long serialVersionUID = 1L;

    public FileDescriptorNofFoundException(String csFileName, LogicalFileDescriptor logicalFileDescriptor) {
        super("FileDescriptorNofFoundException", csFileName, logicalFileDescriptor);
    }
}
