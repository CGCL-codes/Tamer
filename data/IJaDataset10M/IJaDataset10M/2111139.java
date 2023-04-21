package org.oclc.da.common.bytestream.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.oclc.da.exceptions.DAException;
import org.oclc.da.exceptions.DAExceptionCodes;
import org.oclc.da.logging.Logger;

/** This class implements <code>Bytestream</code> functionality.
 * It creates a <code>Bytestream</code> by reading the contents of the URL.
 * @author JCG
 */
public class URLBytestream extends BasicBytestream {

    private Logger logger = Logger.newInstance();

    /** Public constructor.
     * @param content   A url to content of the <code>Bytestream</code>.
     * @throws DAException Converted IOExcetpion.
     */
    public URLBytestream(URL content) throws DAException {
        InputStream input = null;
        try {
            input = content.openStream();
            StreamedBytestreamLoader loader = new StreamedBytestreamLoader(input, content.toString());
            setLoader(loader);
        } catch (IOException e) {
            logger.log(DAExceptionCodes.ERROR_READING, Logger.WARN, this, "constructor", "loading input stream", e);
            DAException ex = new DAException(DAExceptionCodes.ERROR_READING, new String[] { content.toString() });
            throw ex;
        }
    }
}
