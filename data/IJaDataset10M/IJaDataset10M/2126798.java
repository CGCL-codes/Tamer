package org.microemu.cldc.file;

import java.io.IOException;
import org.microemu.microedition.io.ConnectionImplementation;

/**
 * This is default Connection when no initialization has been made.
 * 
 * @author vlads
 * 
 */
public class Connection implements ConnectionImplementation {

    public static final String PROTOCOL = "file://";

    public static final int CONNECTIONTYPE_SYSTEM_FS = 0;

    private static int connectionType = CONNECTIONTYPE_SYSTEM_FS;

    public javax.microedition.io.Connection openConnection(String name, int mode, boolean timeouts) throws IOException {
        if (!name.startsWith(PROTOCOL)) {
            throw new IOException("Invalid Protocol " + name);
        }
        switch(connectionType) {
            case CONNECTIONTYPE_SYSTEM_FS:
                return new FileSystemFileConnection(null, name.substring(PROTOCOL.length()), null);
            default:
                throw new IOException("Invalid connectionType configuration");
        }
    }

    static int getConnectionType() {
        return connectionType;
    }

    static void setConnectionType(int connectionType) {
        Connection.connectionType = connectionType;
    }
}
