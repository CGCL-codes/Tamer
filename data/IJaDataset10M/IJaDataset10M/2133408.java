package com.ezware.common;

import java.awt.Color;
import java.awt.Font;

public final class Markup {

    private static final String HTML_START = "<html>";

    private static final String HTML_END = "</html>";

    private static final String HTML_BREAK = "<br>";

    private Markup() {
    }

    public static final String toHex(Color color) {
        color = color == null ? Color.BLACK : color;
        String rgb = Integer.toHexString(color.getRGB());
        return rgb.substring(2, rgb.length());
    }

    /**
	 * Converts string to simple &lt;HTML&gt; presentation.<br>
	 * Replaces EOLs with &lt;BR&gt; tags
	 * @param s
	 * @param finalize if true wraps text with &lt;HTML&gt; tags
	 * @return
	 */
    public static final String toHTML(String s, boolean finalize) {
        s = s == null ? "" : s.replaceAll("\n", HTML_BREAK);
        String tmp = s.trim().toLowerCase();
        StringBuilder sb = new StringBuilder(s);
        if (finalize) {
            if (!tmp.startsWith(HTML_START)) sb.insert(0, HTML_START);
            if (!tmp.endsWith(HTML_END)) sb.append(HTML_END);
        }
        return sb.toString();
    }

    /**
	 * Converts string to simple &lt;HTML&gt; presentation.<br>
	 * Replaces EOLs with &lt;BR&gt; tags, wraps text with &lt;HTML&gt; tags
	 * @param s
	 * @return
	 */
    public static final String toHTML(String s) {
        return toHTML(s, true);
    }

    /**
	 * Converts font to CSS style
	 * @param font
	 * @return CSS style as string
	 */
    public static final String toCSS(Font font) {
        return String.format("font-family: \"%s\"; %s; %s;", font.getFamily(), toSizeCSS(font), toStyleCSS(font));
    }

    public static final String toSizeCSS(Font font) {
        return String.format("font-size: %fpx", font.getSize() * .75);
    }

    public static final String toStyleCSS(Font font) {
        switch(font.getStyle()) {
            case Font.ITALIC:
                return "font-style : italic";
            case Font.BOLD:
                return "font-weight: bold";
            default:
                return "font-weight: normal";
        }
    }

    /**
	 * Converts color to CSS style
	 * @param color
	 * @return CSS style as string
	 */
    public static final String toCSS(Color color) {
        return String.format("color: #%s;", toHex(color));
    }
}
