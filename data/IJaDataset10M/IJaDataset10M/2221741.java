package org.ini4j.spi;

import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.Profile;

public class IniBuilder extends AbstractProfileBuilder implements IniHandler {

    private Ini _ini;

    public static IniBuilder newInstance(Ini ini) {
        IniBuilder instance = newInstance();
        instance.setIni(ini);
        return instance;
    }

    public void setIni(Ini value) {
        _ini = value;
    }

    @Override
    Config getConfig() {
        return _ini.getConfig();
    }

    @Override
    Profile getProfile() {
        return _ini;
    }

    private static IniBuilder newInstance() {
        return ServiceFinder.findService(IniBuilder.class);
    }
}
