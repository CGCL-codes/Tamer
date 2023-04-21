package de.searchworkorange.lib.acl;

import de.searchworkorange.lib.logger.LoggerCollection;
import java.io.IOException;
import jcifs.smb.ACE;

/**
 *
 * @author Sascha Kriegesmann kriegesmann at vaxnet.de
 */
public class LocalFileACLCheck implements IACLCheck {

    private static final boolean CLASSDEBUG = false;

    private LoggerCollection loggerCol = null;

    public LocalFileACLCheck(LoggerCollection loggerCol) {
        if (loggerCol == null) {
            throw new IllegalArgumentException();
        } else {
            this.loggerCol = loggerCol;
        }
    }

    /**
     *
     * @param fileObject
     * @return StringBuilder
     * @throws IOException
     */
    @Override
    public StringBuilder getACLDataStringBuilder(Object fileObject) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @param fileObject
     * @return ACE[] 
     * @throws IOException
     */
    @Override
    public ACE[] getACLData(Object fileObject) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
