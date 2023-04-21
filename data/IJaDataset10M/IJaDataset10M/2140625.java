package com.dalonedrau.font;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.geom.Rectangle2D;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.dalonedrau.xmlparser.XMLParserUtility;

/**
 * Factory class to produce game fonts.
 * @author DaLoneDrow
 */
public final class FontFactory {

    /** the map of all fonts used in the game. */
    private static HashMap<String, HashMap<Float, Font>> fontsMap;

    /** the xml resource file. */
    private static final String XMLFILE = "com/dalonedrau/font/fonts/fontlist.xml";

    /** the directory for all fonts. */
    public static final String DIR_FONTS = "com/dalonedrau/font/fonts/";

    /** alchemist.  just as it says, nice for magic scrolls and spells. */
    public static final String FONT_ALCHEMIST = "alchemist";

    /** alphabet of the magi. */
    public static final String FONT_ALPHAMAGI = "ALPMAGI";

    /** aniron - taken from the LOTR movies, readable. */
    public static final String FONT_ANIRON = "ANIRON";

    /** carolingia. celtic-style font, upper/lower case and digits. */
    public static final String FONT_CAROLINGIA = "carolingia";

    /** 
	 * cenobyte - diablo-ish, suitably evil-looking. 
	 * roman numerals for digits.
	 */
    public static final String FONT_CENOBYTE = "CENOBYTE";

    /** 
	 * dark11 - lower-case is top-aligned with caps, 
	 * numbers the same way, kind of annoying.
	 * difficult to read in smaller fonts.
	 */
    public static final String FONT_DARK11 = "DARK11";

    /** dungeon. generic-looking font. upper/lower/digits. */
    public static final String FONT_DUNGEON = "DUNGRG__";

    /** enochian - magic type script - no numbers. */
    public static final String FONT_ENOCHIAN = "ENOCHIAN";

    /** fairy dust. use for large fonts, maybe titles only. */
    public static final String FONT_FAIRY = "FairyDustB";

    /** first order. fantasy style, nice, but not readable below 18pt. */
    public static final String FONT_FIRSTORDER = "Firstv2p";

    /** gang of three. asian style. like disneyland under martial law.  upper/digits */
    public static final String FONT_GANG_OF_3 = "go3v2";

    /** holy - like the holy roman empire. */
    public static final String FONT_HOLY = "HOLY";

    /** herakles - ancient greek-style script. */
    public static final String FONT_HERAKLES = "HERAKLES";

    /** jgrr - roman rustic, numbers not so special. */
    public static final String FONT_JGJRR = "JGJRR";

    /** jgrrb - roman rustic, numbers not so special. */
    public static final String FONT_JGJRRB = "JGJRRB";

    /** king arthur - caps only no numbers 12th century style. */
    public static final String FONT_KINGARTHUR = "KINGARTH";

    /** knight's quest - caps only medieval style. */
    public static final String FONT_KNIGHTSQUEST = "KnightsQuest";

    /** knight's quest - wood-carving style - use large font. */
    public static final String FONT_KNIGHTSQUESTCALLIG = "KnightsQuestCallig";

    /** knight's quest - each character in a shield - use sparingly. */
    public static final String FONT_KNIGHTSQUESTSHIELDS = "KnightsQuestShielded";

    /** kudasai - asian font, more japanese style. */
    public static final String FONT_KUDASAI = "KUDASAI";

    /** lovecraft's diary- alien runic, good for magic scrolls. */
    public static final String FONT_LCD = "Lovecraft's Diary";

    /** 
	 * Morris Roman. 
	 * clean, roman-style. as in holy roman empire. upper/lower/digits. 
	 */
    public static final String FONT_MORRIS_ROMAN = "MorrisRomanBlack";

    /** 
	 * Morris Roman Alternative. 
	 * clean, roman-style. as in holy roman empire. upper/lower/digits. 
	 */
    public static final String FONT_MORRIS_ROMAN_ALT = "MorrisRomanBlackAlt";

    /** Pulse Rifle. futuristic. upper/lower/digits. */
    public static final String FONT_PULSE_RIFLE = "pulserifle";

    /** Robotaur. futuristic. upper/lower/digits. */
    public static final String FONT_ROBOTAUR = "robotaur";

    /** Sands Of Fire. near eastern-looking. upper/lower/digits. */
    public static final String FONT_SANDS_OF_FIRE = "SandsOfFire";

    /** Seven Monkeys Fury. asian-style. bold. upper/digits. */
    public static final String FONT_7_MONKEYS_FURY = "SEVEMFBR";

    /** shadow - alien runic, from babylon 5, good for magic scrolls. */
    public static final String FONT_SHADOW = "SHADOW";

    /** shanghai - asian font, caps only. */
    public static final String FONT_SHANGHAI = "shanghai";

    /** 
	 * sherwood. clean, celtic-style.  
	 * you think robin hood. upper/lower, digits. 
	 */
    public static final String FONT_SHERWOOD = "SHERWOOD";

    /** 
	 * Space Age. futuristic, curved.  
	 * use '~' and '_' to create ligatures between characters.  
	 * upper/lower/digits.
	 */
    public static final String FONT_SPACE_AGE = "space_age";

    /** 
	 * SF Square Head. futuristic, square head font, no curves.  
	 * upper/lower/digits.
	 */
    public static final String FONT_SQUARE_HEAD = "SFSquareHead";

    /** UnZialish. interesting celtic font, upper/lower, digits. */
    public static final String FONT_UNZIALISH = "UnZialish";

    /** vafthrudnir - nice celtic font, caps only. */
    public static final String FONT_VAFTHRUD = "VAFTHRUD";

    /** valhalla - similar to viking font. */
    public static final String FONT_VALHALLA = "valhn";

    /** all mapped font sizes. */
    private static final float[] FONT_SIZES = { 8.0f, 10.0f, 11.0f, 12.0f, 14.0f, 16.0f, 18.0f, 24.0f, 36.0f, 48.0f };

    /** the list of all font names. */
    private static ArrayList<String> fontNames;

    /** Hidden constructor. */
    private FontFactory() {
    }

    /** Loads all fonts into the game system. */
    public static void loadFonts() {
        fontNames = new ArrayList<String>();
        fontsMap = new HashMap<String, HashMap<Float, Font>>();
        ClassLoader classLoader = FontFactory.class.getClassLoader();
        InputStream xmlIs = classLoader.getResourceAsStream(XMLFILE);
        Document fontDoc = XMLParserUtility.parseXmlFile(xmlIs);
        Element root = fontDoc.getDocumentElement();
        NodeList nodes = root.getElementsByTagName("font");
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Element node = (Element) nodes.item(i);
                String fontFile = XMLParserUtility.getTextFromElement(node, "file");
                String fontName = null;
                try {
                    StringBuffer fullPath = new StringBuffer();
                    fullPath.append(DIR_FONTS);
                    fullPath.append(fontFile);
                    InputStream is = classLoader.getResourceAsStream(fullPath.toString());
                    fontName = getPrefix(fontFile).toLowerCase();
                    System.out.println("loading font " + fontName);
                    Font newFont = Font.createFont(Font.TRUETYPE_FONT, is);
                    is.close();
                    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    ge.registerFont(newFont);
                    HashMap<Float, Font> fontMap = new HashMap<Float, Font>();
                    for (int j = 0; j < FONT_SIZES.length; j++) {
                        float size = FONT_SIZES[j];
                        fontMap.put(new Float(size), newFont.deriveFont(size));
                    }
                    fontsMap.put(fontName, fontMap);
                    fontNames.add(fontName);
                    System.out.println("complete!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(ex.getMessage());
                    System.exit(1);
                }
            }
        }
    }

    /**
	 * Gets system font in a specific size.
	 * @param name the name of the font
	 * @param size the font size
	 * @return <code>Font</code>
	 * @throws Exception if the font name or font size is invalid
	 */
    public static Font getFont(final String name, final float size) throws Exception {
        HashMap<Float, Font> fontMap = fontsMap.get(name.toLowerCase());
        if (fontMap == null) {
            throw new Exception("Font " + name + " was never loaded into system.");
        }
        Font font = fontMap.get(new Float(size));
        if (font == null) {
            throw new Exception("Font size " + size + " was never established for font " + name + ".");
        }
        return font;
    }

    /**
	 * Gets the names of all loaded fonts.
	 * @return <code>ArrayList</code>
	 */
    public static ArrayList<String> getFontNames() {
        return fontNames;
    }

    /**
	 * Gets the file's prefix.
	 * @param fileName the name of the file
	 * @return <code>String</code>
	 */
    private static String getPrefix(final String fileName) {
        String prefix = fileName;
        if (fileName != null) {
            int i = fileName.lastIndexOf('.');
            if ((i > 0) && (i < fileName.length() - 1)) {
                prefix = fileName.substring(0, i).toLowerCase();
            }
        }
        return prefix;
    }

    /**
	 * Gets the dimensions of a String.
	 * @param g2d the graphics context
	 * @param font the font being used
	 * @param text the String
	 * @return <code>Rectangle</code>
	 */
    public static Rectangle2D getStringBounds(final Graphics2D g2d, final Font font, final String text) {
        FontMetrics fm = g2d.getFontMetrics(font);
        return fm.getStringBounds(text, g2d);
    }
}
