package src.lib;

import src.lib.ioInterfaces.Log_Buffer;

public class CurrentVersion {

    private static final String VER = "4.0.17-dev";

    public static final String get_version() {
        return VER;
    }

    public CurrentVersion(final Log_Buffer LB) {
        LB.package_announce("Vancouver Short Read Analysis Package                " + VER);
    }
}
