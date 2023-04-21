package org.fudaa.fudaa.crue.common;

import java.util.EnumMap;
import java.util.Map;
import javax.swing.Icon;
import org.fudaa.dodico.crue.metier.emh.EnumTypeEMH;
import org.openide.util.ImageUtilities;

/**
 *
 * @author deniger ( genesis)
 */
public class CrueIconsProvider {

    private static final Map<EnumTypeEMH, String> ICON_TYPE_EMH = new EnumMap<EnumTypeEMH, String>(EnumTypeEMH.class);

    private static final Map<EnumTypeEMH, String> BASE_BY_TYPE_EMH = new EnumMap<EnumTypeEMH, String>(EnumTypeEMH.class);

    private static final Map<EnumTypeEMH, Icon> ICON_BY_TYPE_EMH = new EnumMap<EnumTypeEMH, Icon>(EnumTypeEMH.class);

    static {
        ICON_TYPE_EMH.put(EnumTypeEMH.SCENARIO, "scenario.png");
        ICON_TYPE_EMH.put(EnumTypeEMH.MODELE, "modele.png");
        ICON_TYPE_EMH.put(EnumTypeEMH.SOUS_MODELE, "sous-modele.png");
    }

    private static String getCompleteName(String fileName) {
        return "org/fudaa/fudaa/crue/common/icons/" + fileName;
    }

    private static Icon ETU_ICON = null;

    public static Icon getEtuIcon() {
        if (ETU_ICON == null) {
            ETU_ICON = getIcon(getCompleteName("etude.gif"));
        }
        return ETU_ICON;
    }

    public static String getIconBase(EnumTypeEMH typeEMH) {
        String iconBase = BASE_BY_TYPE_EMH.get(typeEMH);
        if (iconBase == null && ICON_TYPE_EMH.containsKey(typeEMH)) {
            iconBase = getCompleteName(ICON_TYPE_EMH.get(typeEMH));
            BASE_BY_TYPE_EMH.put(typeEMH, iconBase);
        }
        return iconBase;
    }

    public static Icon getIcon(String baseName) {
        return ImageUtilities.image2Icon(ImageUtilities.loadImage(baseName));
    }

    public static Icon getIcon(EnumTypeEMH typeEMH) {
        Icon iconBase = ICON_BY_TYPE_EMH.get(typeEMH);
        if (iconBase == null && ICON_TYPE_EMH.containsKey(typeEMH)) {
            iconBase = ImageUtilities.image2Icon(ImageUtilities.loadImage(getIconBase(typeEMH)));
            ICON_BY_TYPE_EMH.put(typeEMH, iconBase);
        }
        return iconBase;
    }

    public static String getIconBaseFile() {
        return getCompleteName("file.png");
    }

    public static String getIconBaseFileBroken() {
        return getCompleteName("file-broken.png");
    }

    public static String getIconBaseRun() {
        return getCompleteName("run.png");
    }

    public static String getIconBaseRunCourant() {
        return getCompleteName("element-courant-non-charge.png");
    }

    public static String getIconBaseScenarioCourant() {
        return getCompleteName("element-courant-non-charge.png");
    }

    public static Icon getIconRun() {
        return getIcon(getCompleteName("run.png"));
    }

    public static Icon getIconRunCourant() {
        return getIcon(getIconBaseRunCourant());
    }

    public static String getFileIconBase() {
        return getCompleteName("file.png");
    }

    public static String getDirIconBase() {
        return getCompleteName("folder.png");
    }
}
