package com.handjoys.startup;

import com.handjoys.logger.FileLogger;

public class LicenseStartup implements Startup {

    public boolean start() {
        FileLogger.info("LicenseStartup start......");
        return true;
    }

    public boolean end() {
        return true;
    }
}
