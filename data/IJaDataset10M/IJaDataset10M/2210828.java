package net.sf.mogbox.pol.ffxi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import net.sf.mogbox.pol.Region;

public class FFXI {

    public static final int NONE = 0;

    public static final int BASE = 1;

    public static final int EXP_RISE_OF_THE_ZILART = 2;

    public static final int EXP_CHAINS_OF_PROMATHIA = 3;

    public static final int EXP_TREASURES_OF_AHT_URHGAN = 4;

    public static final int EXP_WINGS_OF_THE_GODDESS = 5;

    public static final int EXP_A_CRYSTALLINE_PROPHECY = 6;

    public static final int EXP_A_MOOGLE_KUPO_DETAT = 7;

    public static final int EXP_A_SHANTOTTO_ASCENSION = 8;

    private static final Map<Region, String> VERSION_STRINGS = new EnumMap<Region, String>(Region.class);

    public static long getPatchTime() {
        return getPatchTime(Region.getDefaultFFXIRegion());
    }

    public static long getPatchTime(Region region) {
        File patch = new File(region.getFFXILocation(), "patch.ver");
        return patch.lastModified();
    }

    public static String getVersionString() {
        return getVersionString(Region.getDefaultFFXIRegion());
    }

    public static String getVersionString(Region region) {
        String version = VERSION_STRINGS.get(region);
        if (version != null) return version;
        File file = new File(region.getFFXILocation(), "patch.cfg");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("US-ASCII")));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("}")) break;
                version = line;
            }
            int index = version.indexOf(" ");
            version = version.substring(0, index);
        } catch (Throwable t) {
            version = "Unknown";
        }
        VERSION_STRINGS.put(region, version);
        return version;
    }

    public static boolean isExpansionInstalled(int expansion) {
        return isExpansionInstalled(Region.getDefaultFFXIRegion(), expansion);
    }

    public static boolean isExpansionInstalled(Region region, int expansion) {
        if (expansion < 1) throw new IllegalArgumentException("Expansion must be greater than or equal to 1.");
        if (expansion > 9) return false;
        File location = region.getFFXILocation();
        if (location == null) return false;
        File file;
        if (expansion == 1) {
            file = new File(location, "ROM");
            if (!file.exists() || !file.canRead()) return false;
            file = new File(location, "VTABLE.DAT");
            if (!file.exists() || !file.canRead()) return false;
            file = new File(location, "FTABLE.DAT");
            if (!file.exists() || !file.canRead()) return false;
            return true;
        }
        location = new File(location, "ROM" + expansion);
        if (!location.exists() || !location.canRead()) return false;
        file = new File(location, "VTABLE" + expansion + ".DAT");
        if (!file.exists() || !file.canRead()) return false;
        file = new File(location, "FTABLE" + expansion + ".DAT");
        if (!file.exists() || !file.canRead()) return false;
        return true;
    }

    public static int[] getInstalledExpansions() {
        return getInstalledExpansions(Region.getDefaultFFXIRegion());
    }

    public static int[] getInstalledExpansions(Region region) {
        int[] installed = new int[9];
        int num = 0;
        for (int i = 1; i < 9; i++) {
            if (isExpansionInstalled(region, i)) {
                installed[num] = i;
                num++;
            }
        }
        return Arrays.copyOf(installed, num);
    }

    public static String getExpansionTitle(int expansion) {
        if (expansion < 1) throw new IllegalArgumentException("Expansion must be greater than or equal to 1.");
        if (expansion > 8) return String.format(Strings.getString("ffxi.expansion.unknown") + " [%d]", expansion);
        return Strings.getString("ffxi.expansion." + expansion);
    }

    private FFXI() {
    }
}
