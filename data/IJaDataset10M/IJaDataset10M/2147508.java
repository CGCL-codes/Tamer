package org.icy.core.installation;

import org.ice.logger.Logger;
import org.ice.utils.FieldUtils;

public class InstallStrategyFactory {

    public static InstallStrategy createStrategy(String name, Object param) {
        String className = "org.icy.core.installation.strategy." + name + "Strategy";
        try {
            InstallStrategy strategy = (InstallStrategy) FieldUtils.loadClass(className);
            strategy.setParam(param);
            return strategy;
        } catch (Exception ex) {
            Logger.getLogger().log("Cannot create installation strategy: " + className, Logger.LEVEL_WARNING);
            ex.printStackTrace();
            return null;
        }
    }
}
