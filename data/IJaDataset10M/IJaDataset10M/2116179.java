package net.sf.jncu.fdil;

import java.util.zip.ZipException;

/**
 * Could not decompress data error.<br>
 * <tt>kFD_CouldNotDecompressData (kFD_ErrorBase - 5)</tt>
 * 
 * @author Moshe
 */
public class CouldNotDecompressDataException extends ZipException {

    public CouldNotDecompressDataException() {
        super();
    }

    public CouldNotDecompressDataException(String message) {
        super(message);
    }
}
