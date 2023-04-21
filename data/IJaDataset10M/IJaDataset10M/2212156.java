package prohtml.tools;

import java.util.ArrayList;
import java.util.HashMap;
import prohtml.InvalidColorNameException;
import prohtml.TextElement;

public class HtmlColors implements IHtmlTool {

    private static class ColorConts {

        /**
		 * This field holds the colornames and according hex values as Strings. The
		 * hexvalue is always ordered behind the according colorname
		 */
        private static String[] colornameshexes = { "black", "000000", "gray", "808080", "maroon", "800000", "red", "FF0000", "green", "008000", "lime", "00FF00", "olive", "808000", "yellow", "FFFF00", "navy", "000080", "blue", "0000FF", "purple", "800080", "fuchsia", "FF00FF", "teal", "008080", "aqua", "00FFFF", "silver", "C0C0C0", "white", "FFFFFF", "aliceblue", "F0F8FF", "antiquewhite", "FAEBD7", "aquamarine", "7FFFD4", "azure", "F0FFFF", "beige", "F5F5DC", "blueviolet", "8A2BE2", "brown", "A52A2A", "burlywood", "DEB887", "cadetblue", "5F9EA0", "chartreuse", "7FFF00", "chocolate", "D2691E", "coral", "FF7F50", "cornflowerblue", "6495ED", "cornsilk", "FFF8DC", "crimson", "DC143C", "darkblue", "00008B", "darkcyan", "008B8B", "darkgoldenrod", "B8860B", "darkgray", "A9A9A9", "darkgreen", "006400", "darkkhaki", "BDB76B", "darkmagenta", "8B008B", "darkolivegreen", "556B2F", "darkorange", "FF8C00", "darkorchid", "9932CC", "darkred", "8B0000", "darksalmon", "E9967A", "darkseagreen", "8FBC8F", "darkslateblue", "483D8B", "darkslategray", "2F4F4F", "darkturquoise", "00CED1", "darkviolet", "9400D3", "deeppink", "FF1493", "deepskyblue", "00BFFF", "dimgray", "696969", "dodgerblue", "1E90FF", "firebrick", "B22222", "floralwhite", "FFFAF0", "forestgreen", "228B22", "gainsboro", "DCDCDC", "ghostwhite", "F8F8FF", "gold", "FFD700", "goldenrod", "DAA520", "greenyellow", "ADFF2F", "honeydew", "F0FFF0", "hotpink", "FF69B4", "indianred", "CD5C5C", "indigo", "4B0082", "ivory", "FFFFF0", "khaki", "F0E68C", "lavender", "E6E6FA", "lavenderblush", "FFF0F5", "lawngreen", "7CFC00", "lemonchiffon", "FFFACD", "lightblue", "ADD8E6", "lightcoral", "F08080", "lightcyan", "E0FFFF", "lightgoldenrodyellow", "FAFAD2", "lightgreen", "90EE90", "lightgrey", "D3D3D3", "lightpink", "FFB6C1", "lightsalmon", "FFA07A", "lightseagreen", "20B2AA", "lightskyblue", "87CEFA", "lightslategray", "778899", "lightsteelblue", "B0C4DE", "lightyellow", "FFFFE0", "limegreen", "32CD32", "linen", "FAF0E6", "mediumaquamarine", "66CDAA", "mediumblue", "0000CD", "mediumorchid", "BA55D3", "mediumpurple", "9370DB", "mediumseagreen", "3CB371", "mediumslateblue", "7B68EE", "mediumspringgreen", "00FA9A", "mediumturquoise", "48D1CC", "mediumvioletred", "C71585", "midnightblue", "191970", "mintcream", "F5FFFA", "mistyrose", "FFE4E1", "moccasin", "FFE4B5", "navajowhite", "FFDEAD", "oldlace", "FDF5E6", "olivedrab", "6B8E23", "orange", "FFA500", "orangered", "FF4500", "orchid", "DA70D6", "palegoldenrod", "EEE8AA", "palegreen", "98FB98", "paleturquoise", "AFEEEE", "palevioletred", "DB7093", "papayawhip", "FFEFD5", "peachpuff", "FFDAB9", "peru", "CD853F", "pink", "FFC0CB", "plum", "DDA0DD", "powderblue", "B0E0E6", "rosybrown", "BC8F8F", "royalblue", "4169E1", "saddlebrown", "8B4513", "salmon", "FA8072", "sandybrown", "F4A460", "seagreen", "2E8B57", "seashell", "FFF5EE", "sienna", "A0522D", "skyblue", "87CEEB", "slateblue", "6A5ACD", "slategray", "708090", "snow", "FFFAFA", "springgreen", "00FF7F", "steelblue", "4682B4", "tan", "D2B48C", "thistle", "D8BFD8", "tomato", "FF6347", "turquoise", "40E0D0", "violet", "EE82E", "wheat", "F5DEB3", "whitesmoke", "F5F5F5", "yellowgreen", "9ACD32" };

        /**
		 * Use this method to convert a colorname into the according hexvalue. If
		 * the color name is not valid an InvalidColorNameExecption is thrown
		 * 
		 * @param colorname
		 *            String
		 * @return String
		 * @throws InvalidColorNameException
		 */
        public static String colornameTohexvalue(String colorname) {
            colorname = colorname.toLowerCase();
            for (int i = 0; i < colornameshexes.length; i += 2) {
                if (colornameshexes[i].equals(colorname)) {
                    return colornameshexes[i + 1];
                }
            }
            return "000000";
        }
    }

    /**
    * Arraylist holding the colors found in the page
    */
    public ArrayList colors;

    public HtmlColors() {
        colors = new ArrayList();
    }

    public void doAfterEndTag() {
    }

    /**
    * returns the int value of a sequence of two hexvalues.
    * @param hex String
    * @return int
    */
    private int doubleHexToInt(String hex) {
        return Integer.parseInt(hex, 16);
    }

    /**
    * Returns a Color corresponding to the given color in Hexformat
    * @param hex String
    * @return Color
    */
    private int hexToColor(String hex) {
        hex = hex.replaceAll("#", "");
        int x, y, z;
        try {
            x = doubleHexToInt(hex.substring(0, 2));
        } catch (Exception e) {
            hex = ColorConts.colornameTohexvalue(hex);
            x = doubleHexToInt(hex.substring(0, 2));
        }
        y = doubleHexToInt(hex.substring(2, 4));
        z = doubleHexToInt(hex.substring(4, 6));
        if (x > 255) x = 255; else if (x < 0) x = 0;
        if (y > 255) y = 255; else if (y < 0) y = 0;
        if (z > 255) z = 255; else if (z < 0) z = 0;
        return 0xff000000 | (x << 16) | (y << 8) | z;
    }

    public void handleAttribute(String theAttributeName, String theAttributeValue) {
        if (theAttributeName.equals("bgcolor") || theAttributeName.equals("color")) {
            Integer keep = new Integer(hexToColor(theAttributeValue));
            if (!colors.contains(keep)) {
                colors.add(keep);
            }
        }
    }

    public void handleStartTag(String theTag, HashMap theAttributes, boolean theIsStandAlone) {
    }

    public void handleText(final String theText) {
    }

    public void initBeforeParsing() {
    }

    /**
	 * GetPageColor gives you a specified color of an HtmlPage. Use getNumbOfColors to get the number of found colors. 
	 * 
	 * @shortdesc GetPageColor gives you a specified color of an HtmlPage.
	 * @param value int, the number of the color you want to be returned
	 * @return color, the color for the given value
	 * @example HtmlTree_colors
	 * @related HtmlTree
	 * @related childNumbToWeight ( ) 
	 * @related getLinks ( ) 
	 * @related getNumbOfColors ( )
	 */
    public int getPageColor(int value) {
        return ((Integer) colors.get(value)).intValue();
    }

    /**
    * Use this Method to get the number of all different colors of a HTML document.
    * 
    * @return int, the number of found colors
    * @example HtmlTree_colors
    * @related HtmlTree
    * @related childNumbToWeight ( ) 
    * @related getLinks ( ) 
    * @related getPageColor ( ) 
    */
    public int getNumbOfColors() {
        return colors.size();
    }
}
