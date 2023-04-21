package org.epo.jbpa.sgml2xml;

/**
 * SgmlRef
 */
public class SgmlRef {

    UnTag[] sgmlTag;

    /**
 * SgmlRef constructor.
 */
    public SgmlRef() {
        int myCpt;
        sgmlTag = new UnTag[28];
        for (myCpt = 0; myCpt < 28; myCpt++) sgmlTag[myCpt] = new UnTag();
        sgmlTag[0].setValues("<BPSJOB", "</BPSJOB>", true, 0, true, false);
        sgmlTag[1].setValues("<BPSREQ", "</BPSREQ>", false, 8, false, false);
        sgmlTag[2].setValues("<BIN", "", false, 0, true, false);
        sgmlTag[3].setValues("<USER", "", false, 0, true, false);
        sgmlTag[4].setValues("<JOBNAME", "", false, 0, true, false);
        sgmlTag[5].setValues("<DIST", "", false, 2, true, false);
        sgmlTag[6].setValues("<COPIES", "", false, 0, true, false);
        sgmlTag[7].setValues("<ERROR", "", false, 0, true, false);
        sgmlTag[8].setValues("<BATCH", "", false, 0, true, false);
        sgmlTag[9].setValues("<FORMAT", "", false, 0, true, false);
        sgmlTag[10].setValues("<BPSDATA", "</BPSDATA>", true, 0, false, false);
        sgmlTag[11].setValues("<OUTFILE", "", false, 0, true, false);
        sgmlTag[12].setValues("<SOFTCOPY", "</SOFTCOPY>", true, 0, false, true);
        sgmlTag[13].setValues("<BPSADDR", "</BPSADDR>", true, 5, false, true);
        sgmlTag[14].setValues("<BPSHDR", "</BPSHDR>", true, 0, false, false);
        sgmlTag[15].setValues("<RDATE", "", false, 0, true, false);
        sgmlTag[16].setValues("<RTIME", "", false, 0, true, false);
        sgmlTag[17].setValues("<H1", "", false, 0, true, false);
        sgmlTag[18].setValues("<H2", "", false, 0, true, false);
        sgmlTag[19].setValues("<TEXT", "</TEXT>", true, 1, false, false);
        sgmlTag[20].setValues("<TI", "", false, 0, true, false);
        sgmlTag[21].setValues("<TAB", "", false, 0, true, false);
        sgmlTag[22].setValues("<BNSDOC", "", false, 8, true, false);
        sgmlTag[23].setValues("<PHXDOC", "", false, 8, true, false);
        sgmlTag[24].setValues("<BPSSEP", "", false, 2, false, false);
        sgmlTag[25].setValues("<BPSSTAP", "", false, 0, true, false);
        sgmlTag[26].setValues("<APPDOC", "", false, 9, true, false);
        sgmlTag[27].setValues("<BPSOVLY", "", false, 6, true, false);
    }

    /**
 * get End Tag.
 * @return String
 * @param firstTag String
 */
    public String getEndTag(String firstTag) {
        int myCpt;
        for (myCpt = 0; myCpt < 28; myCpt++) {
            if (firstTag.equals(sgmlTag[myCpt].getStartTag())) {
                return sgmlTag[myCpt].getEndTag();
            }
        }
        return null;
    }

    /**
 * get the first Tag.
 * @return String
 * @param inString java.lang.String
 */
    public String getFirstTag(String inString) {
        int deb = 0;
        int end = 0;
        deb = inString.indexOf('<');
        if (deb > -1) {
            end = inString.indexOf('>', deb);
            if ((inString.indexOf(' ', deb) < end) && (inString.indexOf(' ', deb) > -1)) end = inString.indexOf(' ', deb);
            if (end > deb) return inString.substring(deb, end); else return null;
        }
        return null;
    }

    /**
 * get maxAttribute.
 * @return int
 * @param firstTag String
 */
    public int getMaxAttribute(String firstTag) {
        int myCpt;
        for (myCpt = 0; myCpt < 28; myCpt++) {
            if (firstTag.equals(sgmlTag[myCpt].getStartTag())) {
                return sgmlTag[myCpt].getNbMaxAttr();
            }
        }
        return 0;
    }

    /**
 * hasData
 * @return boolean
 * @param firstTag String
 */
    public boolean hasData(String firstTag) {
        int myCpt;
        for (myCpt = 0; myCpt < 28; myCpt++) {
            if (firstTag.equals(sgmlTag[myCpt].getStartTag())) {
                return sgmlTag[myCpt].getData();
            }
        }
        return false;
    }

    /**
 * has an end tag
 * @return boolean
 * @param firstTag String
 */
    public boolean hasEndTag(String firstTag) {
        int myCpt;
        for (myCpt = 0; myCpt < 28; myCpt++) {
            if (firstTag.equals(sgmlTag[myCpt].getStartTag())) {
                if (sgmlTag[myCpt].getEndTag() != "") return true;
            }
        }
        return false;
    }

    /**
 * has parameter
 * @return boolean
 * @param firstTag String
 */
    public boolean hasParameter(String firstTag) {
        int myCpt;
        for (myCpt = 0; myCpt < 28; myCpt++) {
            if (firstTag.equals(sgmlTag[myCpt].getStartTag())) {
                return sgmlTag[myCpt].getParam();
            }
        }
        return false;
    }
}
