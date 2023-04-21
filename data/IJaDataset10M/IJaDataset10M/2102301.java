package com.mscg.theme;

import java.net.URL;

public abstract class StandardAbstractThemeElementsLoader implements ThemeElementsLoader {

    protected abstract String getThemeFolder();

    protected String getDefaultExtension() {
        return "png";
    }

    public URL getUrlForImage(IconType iconType) {
        String imageName = iconType.getFileName();
        int index = imageName.lastIndexOf('.');
        if (index < 0) imageName += "." + getDefaultExtension();
        return this.getClass().getClassLoader().getResource(getThemeFolder() + imageName);
    }
}
