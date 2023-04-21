package cat.inkubator.plugin4j.updater;

import javax.swing.text.MaskFormatter;

/**
 *
 * @author gato
 */
public class VersionChecker {

    public static boolean check(String localVersion, String remoteVersion) {
        try {
            MaskFormatter mask = new MaskFormatter("##.##.##");
            mask.setPlaceholderCharacter('0');
            mask.setValueContainsLiteralCharacters(false);
            localVersion = mask.valueToString(localVersion);
            remoteVersion = mask.valueToString(remoteVersion);
        } catch (Exception e) {
            System.out.println("Version Format Incorrect.");
            return false;
        }
        int version = VersionChecker.checkHigh(localVersion, remoteVersion);
        if (version > 0) return true;
        return false;
    }

    private static int checkHigh(String local, String remote) {
        int version = 0;
        String loc[] = local.split("\\.");
        String rem[] = remote.split("\\.");
        for (int i = 0; i < loc.length; i++) {
            if (Integer.parseInt(rem[i]) > Integer.parseInt(loc[i])) {
                version++;
                break;
            } else if (Integer.parseInt(rem[i]) < Integer.parseInt(loc[i])) {
                version--;
                break;
            }
        }
        return version;
    }

    @Deprecated
    private static int checkMedium(String local, String remote) {
        int version = 0;
        String loc[] = local.split("-");
        String rem[] = remote.split("-");
        try {
            if (loc[1].equalsIgnoreCase("alpha") && rem[1].equals("beta")) {
                version++;
            } else if (loc[1].equalsIgnoreCase("beta") && rem[1].equals("alpha")) {
                version--;
            }
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
            version--;
        }
        return version;
    }

    @Deprecated
    private static int checkLow(String local, String remote) {
        int version = 0;
        String loc[] = local.split("-");
        String rem[] = remote.split("-");
        try {
            if (Integer.parseInt(loc[2]) < Integer.parseInt(rem[2])) {
                version++;
            } else if (Integer.parseInt(loc[2]) > Integer.parseInt(rem[2])) {
                version--;
            }
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
            version--;
        }
        return version;
    }
}
