package org.brainypdm.license;

import java.io.IOException;
import java.io.InputStream;

/**
 * This singleton is used for load the license text
 * 
 * @author <a href="mailto:nico@brainypdm.org">Nico Bagari</a>
 * 
 */
public class License {

    /**
	 * the file name for the short license
	 */
    private String K_SHORT_LICENSE = "/org/brainypdm/license/license-short-output.txt";

    /**
	 * the file name for the full license
	 */
    private String K_FULL_LICENSE = "/org/brainypdm/license/license-full.txt";

    /**
	 * the instance
	 */
    private static License instance;

    /**
	 * void constructor
	 *
	 */
    private License() {
    }

    /**
	 * the get instance
	 * @return the unique instance of License
	 */
    public static final synchronized License getInstance() {
        if (instance == null) {
            instance = new License();
        }
        return instance;
    }

    /**
	 * 
	 * @return the short license text
	 */
    public String getShortLicense() throws IOException {
        return getText(K_SHORT_LICENSE);
    }

    /**
	 * 
	 * @return the full license text
	 */
    public String getFullLicense() throws IOException {
        return getText(K_FULL_LICENSE);
    }

    /**
	 * return the content for the file in input
	 * @param aFileName the file name
	 * @return the content
	 * @throws IOException
	 */
    private String getText(String aFileName) throws IOException {
        InputStream reader = License.class.getResourceAsStream(aFileName);
        if (reader == null) {
            throw new IOException("It's impossible to read the license file");
        } else {
            byte[] buffer = new byte[1024];
            int nByte = 0;
            StringBuffer ret = new StringBuffer();
            while ((nByte = reader.read(buffer)) != -1) {
                String msg = new String(buffer, 0, nByte);
                ret.append(msg);
            }
            reader.close();
            return ret.toString();
        }
    }
}
