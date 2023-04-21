package org.epoline.bsi.bps.old;

import org.epoline.bsi.bps.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (16/10/00 11:24:44)
 * @author: mv02540
 */
public class BPSFreeTextOverlay implements java.io.Serializable, BPSFreeTextOverlayIF {

    private int page;

    private int posX;

    private int posY;

    private java.lang.String font;

    private int fontSize;

    private java.lang.String text;

    /**
 * BPSFreeTextOverlay constructor.
 * After creation of a BPSFreeTextOverlay using this contructor one of the two init
 * procedures must be called before any of the other procedures is called
 */
    public BPSFreeTextOverlay() {
        super();
    }

    /**
 * PrintOverlay constructor comment.
 */
    public BPSFreeTextOverlay(String aText, int aPage, int aXPos, int aYPos) {
        super();
        init(aText, aPage, aXPos, aYPos);
    }

    /**
 * PrintOverlay constructor comment.
 */
    public BPSFreeTextOverlay(String aText, int aPage, int aXPos, int aYPos, String aFont, int aFontSize) {
        super();
        init(aText, aPage, aXPos, aYPos, aFont, aFontSize);
    }

    /**
 * Insert the method's description here.
 * Creation date: (18/10/00 11:45:29)
 * @return java.lang.String
 */
    public java.lang.String getFont() {
        return font;
    }

    /**
 * Insert the method's description here.
 * Creation date: (18/10/00 11:45:43)
 * @return int
 */
    public int getFontSize() {
        return fontSize;
    }

    /**
 * Insert the method's description here.
 * Creation date: (16/10/00 11:26:29)
 * @return int
 */
    public int getPage() {
        return page;
    }

    /**
 * Insert the method's description here.
 * Creation date: (16/10/00 11:27:13)
 * @return int
 */
    public int getPosX() {
        return posX;
    }

    /**
 * Insert the method's description here.
 * Creation date: (16/10/00 11:27:24)
 * @return int
 */
    public int getPosY() {
        return posY;
    }

    /**
 * Insert the method's description here.
 * Creation date: (18/10/00 13:03:45)
 * @return java.lang.String
 */
    public java.lang.String getText() {
        return text;
    }

    /**
 * PrintOverlay constructor comment.
 */
    public void init(String aText, int aPage, int aXPos, int aYPos) {
        setText(aText);
        setPosX(aXPos);
        setPosY(aYPos);
        setPage(aPage);
        setFont("Helvetica");
        setFontSize(12);
    }

    /**
 * PrintOverlay constructor comment.
 */
    public void init(String aText, int aPage, int aXPos, int aYPos, String aFont, int aFontSize) {
        setText(aText);
        setPosX(aXPos);
        setPosY(aYPos);
        setPage(aPage);
        setFont(aFont);
        setFontSize(aFontSize);
    }

    /**
 * Insert the method's description here.
 * Creation date: (18/10/00 11:45:29)
 * @param newFont java.lang.String
 */
    public void setFont(java.lang.String newFont) {
        font = newFont;
    }

    /**
 * Insert the method's description here.
 * Creation date: (18/10/00 11:45:43)
 * @param newFontSize int
 */
    public void setFontSize(int newFontSize) {
        fontSize = newFontSize;
    }

    /**
 * Insert the method's description here.
 * Creation date: (16/10/00 11:26:29)
 * @param newPage int
 */
    public void setPage(int newPage) {
        page = newPage;
    }

    /**
 * Insert the method's description here.
 * Creation date: (16/10/00 11:27:13)
 * @param newPosX int
 */
    public void setPosX(int newPosX) {
        posX = newPosX;
    }

    /**
 * Insert the method's description here.
 * Creation date: (16/10/00 11:27:24)
 * @param newPosY int
 */
    public void setPosY(int newPosY) {
        posY = newPosY;
    }

    /**
 * Insert the method's description here.
 * Creation date: (18/10/00 13:03:45)
 * @param newText java.lang.String
 */
    public void setText(java.lang.String newText) {
        text = newText;
    }
}
