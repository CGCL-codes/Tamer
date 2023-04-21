package de.hattrickorganizer.gui.utils;

import gui.UserParameter;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

public class FontUtil {

    private FontUtil() {
    }

    private static String checkInstalledFont(String targetFont, String sample, Font[] allfonts) {
        if (targetFont != null) {
            for (int j = 0; j < allfonts.length; j++) {
                if (targetFont.equalsIgnoreCase(allfonts[j].getFontName()) && (sample == null || allfonts[j].canDisplayUpTo(sample) == -1)) {
                    return allfonts[j].getFontName();
                }
            }
        }
        return null;
    }

    public static String getFontName(final String languageFile) {
        if ("Georgian".equalsIgnoreCase(languageFile)) {
            String georgiansample = "არჰ–";
            Font[] allfonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
            String geFont = checkInstalledFont("BPG Nino Elite Round Cond", georgiansample, allfonts);
            if (geFont == null) {
                geFont = checkInstalledFont("Arial Unicode MS", georgiansample, allfonts);
            }
            if (geFont == null) {
                geFont = checkInstalledFont("Sylfaen", georgiansample, allfonts);
            }
            if (geFont == null) {
                for (int j = 0; j < allfonts.length; j++) {
                    if (allfonts[j].canDisplayUpTo(georgiansample) == -1) {
                        geFont = allfonts[j].getFontName();
                        break;
                    }
                }
            }
            return geFont;
        } else if ("Chinese".equalsIgnoreCase(languageFile)) {
            String chinesesample = "一前未经能首";
            Font[] allfonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
            String chFont = checkInstalledFont("SimSun", chinesesample, allfonts);
            if (chFont == null) {
                chFont = checkInstalledFont("Arial Unicode MS", chinesesample, allfonts);
            }
            if (chFont == null) {
                for (int j = 0; j < allfonts.length; j++) {
                    if (allfonts[j].canDisplayUpTo(chinesesample) == -1) {
                        chFont = allfonts[j].getFontName();
                        break;
                    }
                }
            }
            return chFont;
        } else {
            return "SansSerif";
        }
    }
}
