package org.bpaul.rtalk.protocol;

import java.awt.Color;

public class TextMessage {

    private String fontType;

    private Color fontColor;

    private int fontSize;

    private boolean italic;

    private boolean bold;

    private boolean underLined;

    private boolean strikedOut;

    private String message;

    private int smileyCode;

    private boolean isSmiley;

    private boolean isAudible;

    private boolean isNewLine;

    public String getFontType() {
        return fontType;
    }

    public void setFontType(String fontType) {
        this.fontType = fontType;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isUnderLined() {
        return underLined;
    }

    public void setUnderLined(boolean underLined) {
        this.underLined = underLined;
    }

    public boolean isStrikedOut() {
        return strikedOut;
    }

    public void setStrikedOut(boolean strikedOut) {
        this.strikedOut = strikedOut;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSmileyCode() {
        return smileyCode;
    }

    public void setSmileyCode(int smileyCode) {
        this.smileyCode = smileyCode;
    }

    public boolean isSmiley() {
        return isSmiley;
    }

    public void setSmiley(boolean isSmiley) {
        this.isSmiley = isSmiley;
    }

    public boolean isAudible() {
        return isAudible;
    }

    public void setAudible(boolean isAudible) {
        this.isAudible = isAudible;
    }

    public boolean isNewLine() {
        return isNewLine;
    }

    public void setNewLine(boolean isNewLine) {
        this.isNewLine = isNewLine;
    }
}
