package org.jcryptool.core.operations.dataobject.modern.symmetric.stream;

import java.io.InputStream;
import java.io.OutputStream;
import org.jcryptool.core.operations.dataobject.modern.ModernDataObject;

/**
 * The implementation of the IDataObject interface for the Lfsr algorithm.
 *  
 * @version 0.1
 * @author Tahir Kacak
 */
public class LfsrDataObject extends ModernDataObject {

    private boolean[] seed;

    private boolean[] tapSettings;

    private InputStream keyStreamIS;

    private OutputStream keyStreamOutputStream;

    /**
	 * Returns the seed.
	 * 
	 * @return the seed
	 */
    public boolean[] getSeed() {
        return seed;
    }

    /**
	 * Sets the seed.
	 * 
	 * @param seed 
	 */
    public void setSeed(boolean[] seed) {
        this.seed = seed;
    }

    /**
	 * Returns the tap settings.
	 * 
	 * @return the tap settings
	 */
    public boolean[] getTapSettings() {
        return tapSettings;
    }

    /**
	 * Sets the tap settings.
	 * 
	 * @param tapSettings
	 */
    public void setTapSettings(boolean[] tapSettings) {
        this.tapSettings = tapSettings;
    }

    /**
	 * 
	 * @param inputStream
	 */
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
