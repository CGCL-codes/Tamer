package org.jcryptool.core.operations.dataobject.modern.symmetric.stream;

import java.io.InputStream;
import java.io.OutputStream;
import org.jcryptool.core.operations.dataobject.modern.symmetric.SymmetricDataObject;

/**
 * The implementation of the IDataObject interface for the Dragon algorithm.
 *  
 * @version 0.1
 * @author Tahir Kacak
 */
public class DragonDataObject extends SymmetricDataObject {

    private InputStream keyStreamIS;

    private OutputStream keyStreamOutputStream;

    public void setKeyStreamIS(InputStream inputStream) {
        keyStreamIS = inputStream;
    }

    public InputStream getKeyStreamIS() {
        return keyStreamIS;
    }

    public void setKeyStreamOutputStream(OutputStream outputStream) {
        keyStreamOutputStream = outputStream;
    }

    public OutputStream getKeyStreamOutputStream() {
        return keyStreamOutputStream;
    }
}
