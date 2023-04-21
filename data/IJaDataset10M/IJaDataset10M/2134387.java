package com.mscg.theme;

public class IconType {

    public static final IconType EXIT = new IconType("exit");

    private String fileName;

    protected IconType(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
