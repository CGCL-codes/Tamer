package com.moshpit.tasmania;

import java.util.Comparator;

class TasmaniaAppSorter implements Comparator {

    public static int compareVersions(Object o1, Object o2) {
        return (new TasmaniaAppSorter()).compare(o1, o2);
    }

    public int compare(Object o1, Object o2) {
        int app1MajorVersion = 0;
        int app1MinorVersion = 0;
        int app1MicroVersion = 0;
        int app1BuildNumber = 0;
        int app2MajorVersion = 0;
        int app2MinorVersion = 0;
        int app2MicroVersion = 0;
        int app2BuildNumber = 0;
        if (o1 instanceof TasmaniaApp) {
            TasmaniaApp app1 = (TasmaniaApp) o1;
            app1MajorVersion = app1.getMajorVersion();
            app1MinorVersion = app1.getMinorVersion();
            app1MicroVersion = app1.getMicroVersion();
            app1BuildNumber = app1.getBuildNumber();
        } else {
            ConfigProvider app1 = (ConfigProvider) o1;
            app1MajorVersion = app1.getMajorVersion();
            app1MinorVersion = app1.getMinorVersion();
            app1MicroVersion = app1.getMicroVersion();
            app1BuildNumber = app1.getBuildNumber();
        }
        if (o2 instanceof TasmaniaApp) {
            TasmaniaApp app2 = (TasmaniaApp) o2;
            app2MajorVersion = app2.getMajorVersion();
            app2MinorVersion = app2.getMinorVersion();
            app2MicroVersion = app2.getMicroVersion();
            app2BuildNumber = app2.getBuildNumber();
        } else {
            ConfigProvider app2 = (ConfigProvider) o2;
            app2MajorVersion = app2.getMajorVersion();
            app2MinorVersion = app2.getMinorVersion();
            app2MicroVersion = app2.getMicroVersion();
            app2BuildNumber = app2.getBuildNumber();
        }
        if (app1MajorVersion > app2MajorVersion) {
            return 1;
        } else if (app1MajorVersion < app2MajorVersion) {
            return -1;
        } else {
            if (app1MinorVersion > app2MinorVersion) {
                return 1;
            } else if (app1MinorVersion < app2MinorVersion) {
                return -1;
            } else {
                if (app1MicroVersion > app2MicroVersion) {
                    return 1;
                } else if (app1MicroVersion < app2MicroVersion) {
                    return -1;
                } else {
                    if (app1BuildNumber > app2BuildNumber) {
                        return 1;
                    } else if (app1BuildNumber < app2BuildNumber) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            }
        }
    }

    public boolean equals(Object obj) {
        return (obj instanceof TasmaniaAppSorter);
    }
}
