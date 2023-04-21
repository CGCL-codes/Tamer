package org.epo.jbps.sgml2xml;

import java.io.IOException;
import org.epo.jbps.generic.BpsException;
import org.epoline.jsf.utils.Log4jManager;

/**
 * Describe the tag text.
 * @author Infotel Conseil
 */
public class XmlTagText extends XmlSubTag {

    private String freeText = "";

    private XmlSubTagList tagLineList = null;

    private String font = "Helvetica";

    private int fontSize = 12;

    private int backcolor = 1;

    /**
	 * XmlTagText constructor.
	 */
    public XmlTagText() {
        super();
        tagLineList = new XmlSubTagList();
    }

    /**
	 * XmlTagText constructor.
	 * @param theFont String
	 * @param theFontSize int
	 */
    public XmlTagText(String theFont, int theFontSize) {
        super();
        tagLineList = new XmlSubTagList();
        setFont(theFont);
        setFontSize(theFontSize);
    }

    /**
	 * backcolor standard accessor..
	 * @return int
	 */
    public int getBackcolor() {
        return backcolor;
    }

    /**
	 * font standard accessor.
	 * @return java.lang.String
	 */
    public String getFont() {
        return font;
    }

    /**
	 * fontSize standard accessor.
	 * @return int
	 */
    public int getFontSize() {
        return fontSize;
    }

    /**
	 * freeText standard accessor.
	 * @return java.lang.String
	 */
    public String getFreeText() {
        return freeText;
    }

    /**
	 * tagLineList
	 * @return sgml2xml.XmlSubTagList
	 */
    public XmlSubTagList getTagLineList() {
        return tagLineList;
    }

    /**
	 * return the type of the tag
	 * @return int
	 */
    public int getType() {
        return TAG_TEXT;
    }

    /**
	 * backcolor standard accessor.
	 * @param newValue int
	 */
    public void setBackcolor(int newValue) {
        this.backcolor = newValue;
    }

    /**
	 * font standard accessor.
	 * @param newValue java.lang.String
	 */
    public void setFont(String newValue) {
        this.font = newValue;
    }

    /**
	 * fontSize standard accessor.
	 * @param newValue int
	 */
    public void setFontSize(int newValue) {
        this.fontSize = newValue;
    }

    /**
	 * freeText standard accessor.
	 * @param newValue java.lang.String
	 */
    public void setFreeText(String newValue) {
        this.freeText = newValue;
    }

    /**
	 * tagLineList standard accessor.
	 * @param newValue sgml2xml.XmlSubTagList
	 */
    public void setTagLineList(XmlSubTagList newValue) {
        this.tagLineList = newValue;
    }

    /**
	 * writeFile method comment.
	 * @param theFile java.io.RandomAccessFile
	 * @param theLevel java.lang.String
	 * @exception BpsException
	 */
    public void writeFile(java.io.RandomAccessFile theFile, String theLevel) throws BpsException {
        try {
            theFile.writeBytes(theLevel + "<TEXT");
            if (!getFont().equals("Helvetica")) theFile.writeBytes(" FONT=\"" + getFont() + "\"");
            if (getFontSize() != 12) theFile.writeBytes(" FONTSIZE=\"" + getFontSize() + "\"");
            if (getBackcolor() != 1) theFile.writeBytes(" BACKCOLOR=\"" + getBackcolor() + "\"");
            theFile.writeBytes(">\r\n");
            if (getTagLineList().size() != 0) {
                for (int myCpt = 0; myCpt < getTagLineList().size(); myCpt++) {
                    ((XmlTagLine) (getTagLineList().get(myCpt))).writeFile(theFile, theLevel + "\t");
                }
            } else {
                theFile.writeBytes(getFreeText() + "\r\n");
            }
            theFile.writeBytes(theLevel + "</TEXT>\r\n");
        } catch (IOException e) {
            throw new BpsException(BpsException.ERR_FILE_ACCESS, e.getMessage());
        }
    }
}
