package org.epo.jbps.sgml2xml;

import java.io.RandomAccessFile;
import java.io.*;
import org.epo.jbps.generic.BpsException;
import org.epoline.jsf.utils.Log4jManager;

/**
 * Describe a XmlAddrSheet..
 * @author Infotel Conseil
 */
public class XmlAddrSheet extends XmlSubTag {

    private XmlOverlay xmlOverlay = null;

    /**
 * XmlAddrSheet constructor.
 */
    public XmlAddrSheet() {
        super();
    }

    /**
 * return the Type of the object
 * @return int
 */
    public int getType() {
        return ADDR_SHEET;
    }

    /**
 * xmlOverlay standard accessor
 * @return sgml2xml.XmlOverlay
 */
    public XmlOverlay getXmlOverlay() {
        return xmlOverlay;
    }

    /**
 * xmlOverlay standard accessor
 * @param newValue sgml2xml.XmlOverlay
 */
    public void setXmlOverlay(XmlOverlay newValue) {
        this.xmlOverlay = newValue;
    }

    /**
 * write in a file 
 * @param theFile RandomAccessFile
 * @param theLevel String
 * @exception BpsException
 * 
 */
    public void writeFile(RandomAccessFile theFile, String theLevel) throws BpsException {
        try {
            theFile.writeBytes(theLevel + "<SINGLESHEET STYLE=\"blank.xsl\" >\r\n");
            getXmlOverlay().writeFile(theFile, theLevel + "\t");
            theFile.writeBytes(theLevel + "</SINGLESHEET>\r\n");
        } catch (IOException e) {
            Log4jManager.getLogger(XmlAddrSheet.class).warn(e.getMessage());
            throw new BpsException(BpsException.ERR_FILE_ACCESS, e.getMessage());
        }
    }
}
