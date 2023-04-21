package org.epo.jbpa.sgml2xml;

import java.io.*;
import org.apache.log4j.Logger;

public class Sgml2Xml {

    public SgmlTagList sgmlTree = null;

    public XmlTag xmlData = null;

    private String ctlfileDtdUrl = null;

    /**
 * sgml2xml constructor comment.
 */
    public Sgml2Xml(String theCtlfileDtdUrl) {
        super();
        setCtlfileDtdUrl(theCtlfileDtdUrl);
    }

    /**
 * format String
 * @return String
 * @param inString String
 */
    public String formatString(String inString) {
        String myBuffer = inString;
        String outBuffer = "";
        int myDep = 0;
        while (myBuffer != null) {
            myDep = myBuffer.indexOf('>');
            while ((myDep < myBuffer.length()) && ((myBuffer.charAt(myDep) != 0x0A) || (myBuffer.charAt(myDep) == 0x0D))) myDep++;
            outBuffer += myBuffer.substring(0, myDep);
            while ((myDep < myBuffer.length()) && ((myBuffer.charAt(myDep) == 0x0A) || (myBuffer.charAt(myDep) == 0x0D))) myDep++;
            if (myDep < myBuffer.length()) myBuffer = myBuffer.substring(myDep); else myBuffer = null;
        }
        return outBuffer;
    }

    /**
 * ctlfileDtdUrl accessor
 * Creation date: (06/11/01 10:44:57)
 * @return String
 */
    public String getCtlfileDtdUrl() {
        return ctlfileDtdUrl;
    }

    /**
 * process Sgml to Xml
 * @param theSgmlFile String
 * @param theXmlFile String
 */
    public void processSgml2Xml(String theSgmlFile, String theXmlFile) {
        String strTotal = null;
        try {
            byte[] myBuffer = null;
            RandomAccessFile mySgmlFile = new RandomAccessFile(theSgmlFile, "r");
            myBuffer = new byte[(int) mySgmlFile.length()];
            mySgmlFile.read(myBuffer);
            mySgmlFile.close();
            strTotal = new String(myBuffer);
            strTotal = formatString(strTotal);
            sgmlTree = new SgmlTagList(strTotal);
            xmlData = new XmlTag(sgmlTree, getCtlfileDtdUrl());
            xmlData.genXmlFile(theXmlFile);
        } catch (IOException e) {
        }
    }

    /**
 * ctlfileDtdUrl setter
 * Creation date: (06/11/01 10:44:57)
 * @param newCtlfileDtdUrl String
 */
    private void setCtlfileDtdUrl(String newCtlfileDtdUrl) {
        ctlfileDtdUrl = newCtlfileDtdUrl;
    }
}
