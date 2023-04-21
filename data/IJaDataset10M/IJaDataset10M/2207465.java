package org.dcm4chee.docstore.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import org.dcm4chee.docstore.Availability;
import org.jboss.mx.util.MBeanServerLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSystemInfo {

    private static Logger log = LoggerFactory.getLogger(FileSystemInfo.class);

    private static ObjectName dfCmdName;

    private static MBeanServer server;

    /** JDK6 File.getUsableSpace(), if available. */
    private static Method jdk6getUsableSpace = null;

    static {
        try {
            jdk6getUsableSpace = File.class.getMethod("getUsableSpace", (Class[]) null);
        } catch (Exception ignore) {
        }
    }

    public static final void disableJDK6Support() {
        jdk6getUsableSpace = null;
    }

    public static ObjectName getFilesystemMgtName() {
        return dfCmdName;
    }

    public static void setDFCmdServiceName(String name) {
        try {
            dfCmdName = new ObjectName(name);
        } catch (Exception e) {
            log.error("Cant set FilesystemMgtName! name:" + name);
        }
    }

    public static long freeSpace(String path) throws IOException, InstanceNotFoundException, MBeanException, ReflectionException {
        if (jdk6getUsableSpace != null) {
            try {
                long l = ((Long) jdk6getUsableSpace.invoke(new File(path), (Object[]) null)).longValue();
                if (l != 0) return l;
            } catch (Exception ignore) {
                log.warn("freeSpace using JDK6 getUsableSpace throws exception! try to get free space via DFCommand service!");
            }
        }
        if (getServer().isRegistered(dfCmdName)) {
            return ((Long) getServer().invoke(dfCmdName, "freeSpace", new Object[] { path }, new String[] { String.class.getName() })).longValue();
        } else {
            return -1l;
        }
    }

    public static Availability getFileSystemAvailability(File baseDir, long minFree) {
        if (!baseDir.isDirectory()) {
            log.warn(baseDir + " is not a directory! Set Availability to UNAVAILABLE!");
            return Availability.UNAVAILABLE;
        } else {
            try {
                long free = freeSpace(baseDir.getPath());
                log.debug("check Filesystem availability for doc store! path:" + baseDir.getPath() + " free:" + free);
                if (free == -1) {
                    log.warn("Availability can't be checked! Set to ONLINE anyway!");
                    return Availability.ONLINE;
                }
                return free < minFree ? Availability.UNAVAILABLE : Availability.ONLINE;
            } catch (Exception x) {
                log.error("Can not get free space for " + baseDir + " ! Set Availability to UNAVAILABLE!", x);
                return Availability.UNAVAILABLE;
            }
        }
    }

    private static MBeanServer getServer() {
        if (server == null) {
            server = MBeanServerLocator.locate();
        }
        return server;
    }
}
