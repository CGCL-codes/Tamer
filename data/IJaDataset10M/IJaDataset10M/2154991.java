package org.openvpnsystray.openvpn;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link OpenVPNUtils} is an utility class containing methods concerning OpenVPN.
 * 
 * @author Stefan Franke
 * @date 07.02.2008
 * @version ($Id$)
 */
public class OpenVPNUtils {

    /**
	 * The OpenVPN configuration file extension
	 */
    public static final String OPENVPN_FILE_EXTENSION = ".ovpn";

    /**
	 * Returns the list of OpenVPN configuration files in the given directory.
	 *
	 * @param openVPNConfigDirectory the OpenVPN configuration file directory
	 * @param openVPNExecutable the OpenVPN executable
	 * @return list of OpenVPN configuration files in the given directory
	 */
    public static List<OpenVPNConfig> searchOpenVPNConfigFiles(File openVPNConfigDirectory, File openVPNExecutable) {
        List<OpenVPNConfig> openVPNConfigs = new LinkedList<OpenVPNConfig>();
        FileFilter fileFilter = new FileFilter() {

            @Override
            public boolean accept(File file) {
                return file.getName().toLowerCase().endsWith(OPENVPN_FILE_EXTENSION);
            }
        };
        File files[] = openVPNConfigDirectory.listFiles(fileFilter);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            openVPNConfigs.add(new OpenVPNConfig(file, openVPNExecutable));
        }
        return openVPNConfigs;
    }
}
