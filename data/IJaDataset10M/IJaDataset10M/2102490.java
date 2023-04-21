package com.sksdpt.kioskjui.control.config;

import java.util.Map;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import com.sksdpt.kioskjui.exception.KioskException;

/**
 * DSess
 * 
 * @author "Luis Alfonso Vega Garcia" <vegacom@gmail.com>
 */
public class DSess extends MVKioskInfo {

    public static final String MD_YML = "mobile-devices.yml";

    public static final String BG_DEFAULT = "bg-default.png";

    private static DSess instance;

    private static Logger logger = Logger.getLogger(DSess.class);

    public static synchronized DSess sngltn() {
        if (instance == null) {
            try {
                instance = new DSess();
            } catch (KioskException e) {
                logger.fatal("oops: ", e);
                System.exit(2);
            }
        }
        return instance;
    }

    /** with or without border */
    private int border;

    private int separatorSpace;

    private boolean isSheared;

    /**
     * Constructor.
     * 
     * @throws KioskException
     */
    private DSess() throws KioskException {
        super();
        boolean flag = (Boolean) settings.get("isDesignMode");
        border = flag ? SWT.BORDER : SWT.NONE;
        separatorSpace = 25;
        isSheared = ((String) settings.get("theme")).indexOf("shear") != -1;
    }

    /**
     * @return the border
     */
    public int brdr() {
        return border;
    }

    /**
     * @return the contents dir path.
     */
    public String getContentsDirPath() {
        String dir = (String) settings.get("contentsDir");
        return getAppPath(dir);
    }

    /**
     * @return the mobile devices yaml configuration file path.
     */
    public String getMobileDevicesConfigPath() {
        String dir = (String) settings.get("mobileDevicesDir");
        return getAppPath(dir, MD_YML);
    }

    public Map getScr10StartCfg() {
        return (Map) settings.get("Scr10Start");
    }

    /**
     * @return the screenSaverTimeoutMs
     */
    public long getScreenSaverTimeoutMs() {
        long t = (Long) settings.get("screenSaverTimeout");
        return t * 1000;
    }

    /**
     * @return the separatorSpace
     */
    public int getSeparatorSpace() {
        return separatorSpace;
    }

    public String getTheme() {
        return (String) settings.get("theme");
    }

    /**
     * @return the isSheared
     */
    public boolean isSheared() {
        return isSheared;
    }
}
