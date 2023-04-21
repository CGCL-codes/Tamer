package com.griffin.coushe;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Class description
 *     
 * @author Duncan Griffin
 * @version 1.0.0
 * 
 * <ul>Class history:
 *  <li><b>v1.0.0</b> 
 *		Class created.
 * 	</li>
 * </ul> 
 */
public class ColorScheme {

    private static ResourceBundle aresColors = null;

    public static final String CSHEADER_COLOURSCHEME = "ColourScheme";

    public static final String CSHEADER_ID = "ID";

    public static final String CSHEADER_VERSION = "Version";

    public static final String CSHEADER_LENGTH = "Length";

    public static final String CSHEADER_CRC = "CRC";

    public static final String CSHEADER_DATA = "Data";

    public static final char END_OF_LINE = 0x0A;

    private String name = "Anonymous";

    private String id = "0";

    private String version = "1.0";

    private String length = "960";

    private String CRC = "0";

    private ColorSet colorSet = new ColorSet();

    /**
	 * Parses a string cointaining 4 values to a Color object
	 * @param line
	 * @return
	 */
    public Color parseColorLine(String line) {
        if (null == line) return null;
        String[] strings = line.split(" ");
        if (strings.length == 4) {
            short[] values = new short[4];
            values[0] = Short.parseShort(strings[0]);
            values[1] = Short.parseShort(strings[1]);
            values[2] = Short.parseShort(strings[2]);
            values[3] = Short.parseShort(strings[3]);
            return new Color(values[0], values[1], values[2], values[3] == 0);
        }
        System.out.println("parseColorLine() Wrong line format");
        return null;
    }

    /**
	 * Loads color scheme from given file
	 * @param fileName
	 * @return <code>true</code> if the scheme is successfully loaded, <code>false</code> otherwise
	 */
    public boolean loadColorScheme(String fileName) {
        if (null == fileName) return false;
        try {
            BufferedReader dataIn = new BufferedReader(new FileReader(fileName));
            String str;
            while ((str = dataIn.readLine()) != null) {
                if (str.startsWith(CSHEADER_COLOURSCHEME)) {
                    str = str.substring(CSHEADER_COLOURSCHEME.length()).trim();
                    this.setName(str);
                    continue;
                }
                if (str.startsWith(CSHEADER_ID)) {
                    str = str.substring(CSHEADER_ID.length()).trim();
                    this.setId(str);
                    continue;
                }
                if (str.startsWith(CSHEADER_VERSION)) {
                    str = str.substring(CSHEADER_VERSION.length()).trim();
                    this.setVersion(str);
                    continue;
                }
                if (str.startsWith(CSHEADER_LENGTH)) {
                    str = str.substring(CSHEADER_LENGTH.length()).trim();
                    this.length = str;
                    continue;
                }
                if (str.startsWith(CSHEADER_CRC)) {
                    str = str.substring(CSHEADER_CRC.length()).trim();
                    this.CRC = str;
                    continue;
                }
                if (str.trim().equals(CSHEADER_DATA)) {
                    short index = 0;
                    while ((str = dataIn.readLine()) != null) {
                        this.getColorSet().setColor(index, parseColorLine(str.trim()));
                        index++;
                    }
                    System.out.println(index + " colors loaded from file " + fileName);
                    continue;
                }
            }
            dataIn.close();
        } catch (Exception e) {
            System.out.println("readColorScheme() Exception catched");
            e.printStackTrace();
            return false;
        }
        this.print();
        return true;
    }

    /**
	 * Writes color scheme to a file
	 * @param fileName
	 * @return
	 */
    public boolean saveColorScheme(String fileName) {
        if (null == fileName) return false;
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(fileName));
            StringBuffer sbuf = new StringBuffer(500);
            sbuf.append(CSHEADER_COLOURSCHEME);
            sbuf.append(' ');
            sbuf.append(this.getName());
            sbuf.append(END_OF_LINE);
            sbuf.append(CSHEADER_ID);
            sbuf.append("      ");
            sbuf.append(this.getId());
            sbuf.append(END_OF_LINE);
            sbuf.append(CSHEADER_VERSION);
            sbuf.append(' ');
            sbuf.append(this.getVersion());
            sbuf.append(END_OF_LINE);
            sbuf.append(CSHEADER_LENGTH);
            sbuf.append("  ");
            sbuf.append(this.getLength());
            sbuf.append(END_OF_LINE);
            sbuf.append(CSHEADER_CRC);
            sbuf.append("     ");
            sbuf.append(this.getCRC());
            sbuf.append(END_OF_LINE);
            writer.print(sbuf.toString());
            this.getColorSet().writeDataToFile(writer);
            writer.close();
        } catch (Exception e) {
            System.out.println("saveColorScheme() Exception catched");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
	 * Prints color scheme information 
	 *
	 */
    public void print() {
        System.out.println(CSHEADER_COLOURSCHEME + " " + this.getName());
        System.out.println(CSHEADER_ID + " " + this.getId());
        System.out.println(CSHEADER_VERSION + " " + this.getVersion());
        System.out.println(CSHEADER_LENGTH + " " + this.getLength());
        System.out.println(CSHEADER_CRC + " " + this.getCRC());
    }

    /**
	 * @return
	 */
    public String getCRC() {
        return CRC;
    }

    /**
	 * @return
	 */
    public String getId() {
        return id;
    }

    /**
	 * @return
	 */
    public String getLength() {
        return length;
    }

    /**
	 * @return
	 */
    public String getName() {
        return name;
    }

    /**
	 * @return
	 */
    public String getVersion() {
        return version;
    }

    /**
	 * @param string
	 */
    public void setId(String string) {
        id = string;
    }

    /**
	 * @param string
	 */
    public void setName(String string) {
        name = string;
    }

    /**
	 * @param string
	 */
    public void setVersion(String string) {
        version = string;
    }

    /**
	 * @return
	 */
    public ColorSet getColorSet() {
        return colorSet;
    }

    /**
	 * @param set
	 */
    public void setColorSet(ColorSet set) {
        colorSet = set;
    }

    public static void reloadResources() {
        try {
            ColorScheme.aresColors = ResourceBundle.getBundle("com.griffin.coushe.properties.Colors");
        } catch (MissingResourceException e) {
            ColorScheme.aresColors = null;
        }
    }

    public static boolean resourcesLoaded() {
        return ColorScheme.aresColors != null;
    }

    public static String getColorDescription(short index) {
        index++;
        if (!resourcesLoaded()) {
            return "Color " + index;
        }
        String key = "COLOR_" + Color.COLORVALUE_FORMATTER.format(index);
        return aresColors.getString(key);
    }

    static {
        ColorScheme.reloadResources();
    }

    /**
	 * For test purpouses
	 * @param args
	 */
    public static void main(String[] args) {
        ColorScheme cs = new ColorScheme();
        cs.loadColorScheme("C:\\Temp\\Siemens Original\\Color_schemes\\Titanium.col");
        cs.saveColorScheme("C:\\Temp\\Siemens Original\\Color_schemes\\Titan_m.col");
        cs.print();
        System.out.println("" + ColorScheme.getColorDescription((short) 5));
    }
}
