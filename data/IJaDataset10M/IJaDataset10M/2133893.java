package com.sts.webmeet.client;

public class ButtonInfo extends ControlInfo {

    public ButtonInfo(String strImage, String strCommand, String strTooltip) {
        super(strCommand, strTooltip);
        this.strImage = strImage;
    }

    public String getImage() {
        return strImage;
    }

    private String strImage;
}
