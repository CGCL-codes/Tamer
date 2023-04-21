package org.mozilla.browser.impl.prompt;

/**
 * Answer to confirm dialogs with checkbox
 * and custom button titles
 */
public class ConfirmExAnswer extends DialogAnswer {

    public ConfirmExAnswer(String inDialogTitle, boolean isInDialogTitleRegexp, String inDialogText, boolean isInDialogTextRegexp, String inCheckMsg, boolean inCheckMsgRegexp, boolean inCheckValue, long inButtonFlags, String inButton1Title, boolean isInButton1TitleRegexp, String inButton2Title, boolean isInButton2TitleRegexp, String inButton3Title, boolean isInButton3TitleRegexp, boolean outCheckValue, int outButtonIndex) {
        super(inDialogTitle, isInDialogTitleRegexp, inDialogText, isInDialogTextRegexp);
        this.inCheckMsg = inCheckMsg;
        this.inCheckMsgRegexp = inCheckMsgRegexp;
        this.inCheckValue = inCheckValue;
        this.inButtonFlags = inButtonFlags;
        this.inButton1Title = inButton1Title;
        this.inButton1TitleRegexp = isInButton1TitleRegexp;
        this.inButton2Title = inButton2Title;
        this.inButton2TitleRegexp = isInButton2TitleRegexp;
        this.inButton3Title = inButton3Title;
        this.inButton3TitleRegexp = isInButton3TitleRegexp;
        this.outCheckValue = outCheckValue;
        this.outButtonIndex = outButtonIndex;
    }

    private long inButtonFlags;

    public long getInButtonFlags() {
        return inButtonFlags;
    }

    public void setInButtonFlags(long value) {
        inButtonFlags = value;
    }

    private String inButton1Title;

    public String getInButton1Title() {
        return inButton1Title;
    }

    public void setInButton1Title(String value) {
        inButton1Title = value;
    }

    private boolean inButton1TitleRegexp;

    public boolean isRegexpInButton1Title() {
        return inButton1TitleRegexp;
    }

    public void setRegexpInButton1Title(boolean value) {
        inButton1TitleRegexp = value;
    }

    private String inButton2Title;

    public String getInButton2Title() {
        return inButton2Title;
    }

    public void setInButton2Title(String value) {
        inButton2Title = value;
    }

    private boolean inButton2TitleRegexp;

    public boolean isRegexpInButton2Title() {
        return inButton2TitleRegexp;
    }

    public void setRegexpInButton2Title(boolean value) {
        inButton2TitleRegexp = value;
    }

    private String inButton3Title;

    public String getInButton3Title() {
        return inButton3Title;
    }

    public void setInButton3Title(String value) {
        inButton3Title = value;
    }

    private boolean inButton3TitleRegexp;

    public boolean isRegexpInButton3Title() {
        return inButton3TitleRegexp;
    }

    public void setRegexpInButton3Title(boolean value) {
        inButton3TitleRegexp = value;
    }

    private String inCheckMsg;

    public String getInCheckMsg() {
        return inCheckMsg;
    }

    public void setInCheckMsg(String value) {
        inCheckMsg = value;
    }

    private boolean inCheckMsgRegexp;

    public boolean isRegexpInCheckMsg() {
        return inCheckMsgRegexp;
    }

    public void setRegexpInCheckMsg(boolean value) {
        inCheckMsgRegexp = value;
    }

    private boolean inCheckValue;

    public boolean isInCheckValue() {
        return inCheckValue;
    }

    public void setInCheckValue(boolean value) {
        inCheckValue = value;
    }

    private boolean outCheckValue;

    public boolean isOutCheckValue() {
        return outCheckValue;
    }

    public void setOutCheckValue(boolean value) {
        outCheckValue = value;
    }

    private int outButtonIndex;

    public int getOutButtonIndex() {
        return outButtonIndex;
    }

    public void setOutButtonIndex(int value) {
        outButtonIndex = value;
    }
}
