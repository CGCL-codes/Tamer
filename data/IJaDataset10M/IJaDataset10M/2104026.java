package edu.umich.marketplace;

import org.apache.log4j.Logger;
import com.ramsayconz.wocore.CoreApplication;

public class ColorPalette {

    private static final Logger logger = Logger.getLogger(ColorPalette.class);

    private static final String warnColor = "#FFCC00", failColor = "#FFCCCC", helpColor = "#ffffff", sideColor = "#ffffcc", mainColor = "#ffff9d", liteColor = "#3366AA", editColor = "#ffffcc", darkColor = "#003399";

    private static ColorPalette colorPalatteRef;

    public static String _mainColor, _sideColor, _editColor, _helpColor, _warnColor, _failColor, _darkColor, _liteColor, _bookColor, _backColor, _textColor;

    private ColorPalette() {
        _mainColor = CoreApplication.properties.getProperty("marketplaceApp.mainColor", mainColor);
        _sideColor = CoreApplication.properties.getProperty("marketplaceApp.sideColor", sideColor);
        _editColor = CoreApplication.properties.getProperty("marketplaceApp.editColor", editColor);
        _helpColor = CoreApplication.properties.getProperty("marketplaceApp.helpColor", helpColor);
        _warnColor = CoreApplication.properties.getProperty("marketplaceApp.warnColor", warnColor);
        _failColor = CoreApplication.properties.getProperty("marketplaceApp.failColor", failColor);
        _darkColor = CoreApplication.properties.getProperty("marketplaceApp.darkColor", darkColor);
        _liteColor = CoreApplication.properties.getProperty("marketplaceApp.liteColor", liteColor);
        _bookColor = CoreApplication.properties.getProperty("marketplaceApp.bookColor", "#000066");
        _backColor = CoreApplication.properties.getProperty("bannerBgColor", "#3366cc");
        _textColor = CoreApplication.properties.getProperty("broadcastTextColor", "#9a2f27");
    }

    public static synchronized ColorPalette getColorPalette() {
        if (null == colorPalatteRef) {
            colorPalatteRef = new ColorPalette();
        }
        return colorPalatteRef;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        logger.error("*** You can't clone the singleton ColorPalette.");
        throw new CloneNotSupportedException("You can't clone the singleton ColorPalette");
    }

    public String getHelpPanelColor() {
        return _helpColor;
    }

    public String getBannerBackgroundColor() {
        return _backColor;
    }

    public String getBroadcastTextColor() {
        return _textColor;
    }

    public String getColorForObject(String objectType) {
        if (objectType.equals("edit")) {
            return _editColor;
        } else if (objectType.equals("main")) {
            return _mainColor;
        } else if (objectType.equals("side")) {
            return _sideColor;
        } else if (objectType.equals("alert")) {
            return _warnColor;
        } else if (objectType.equals("error")) {
            return _failColor;
        } else if (objectType.equals("outline")) {
            return _liteColor;
        } else if (objectType.equals("lightAccent")) {
            return _liteColor;
        } else if (objectType.equals("darkAccent")) {
            return _darkColor;
        } else if (objectType.equals("instruction")) {
            return _bookColor;
        }
        return "";
    }
}
