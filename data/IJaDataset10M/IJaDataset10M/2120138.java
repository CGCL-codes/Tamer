package org.fao.waicent.kids.editor;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import javax.servlet.ServletRequest;
import org.fao.waicent.util.StringGraphics;

public class LabelEditor {

    java.util.Map lbl = null;

    public void update(ServletRequest request) {
        String setting = request.getParameter("setting");
        String whichToEdit = "";
        String strRGBColor = "";
        int r = 0, g = 0, b = 0;
        int comma_index = setting.indexOf(",");
        if (comma_index != -1) {
            whichToEdit = setting.substring(0, comma_index);
            strRGBColor = setting.substring(comma_index + 1, setting.length());
            r = Integer.parseInt(strRGBColor.substring(0, strRGBColor.indexOf(",")));
            g = Integer.parseInt(strRGBColor.substring(strRGBColor.indexOf(",") + 1, strRGBColor.lastIndexOf(",")));
            b = Integer.parseInt(strRGBColor.substring(strRGBColor.lastIndexOf(",") + 1, strRGBColor.length()));
        } else {
            whichToEdit = setting;
        }
        if (whichToEdit.equals("FONT_NAME") || whichToEdit.equals("FONT_SIZE") || whichToEdit.equals("BOLD") || whichToEdit.equals("ITALIC") || whichToEdit.equals("UNDERLINE")) {
            String familyfontname = "";
            int style = Font.PLAIN;
            if (whichToEdit.equals("FONT_NAME")) {
                familyfontname = request.getParameter("FONT_NAME");
                getFontStyle().put(TextAttribute.FAMILY, familyfontname);
            } else {
                familyfontname = (String) getFontStyle().get(TextAttribute.FAMILY);
            }
            Float textsize = new Float(0f);
            int size = 10;
            if (whichToEdit.equals("FONT_SIZE")) {
                String sel_size = request.getParameter("FONT_SIZE");
                textsize = Float.valueOf(sel_size);
                getFontStyle().put(TextAttribute.SIZE, textsize);
            } else {
                textsize = (Float) getFontStyle().get(TextAttribute.SIZE);
            }
            if (textsize != null) {
                size = textsize.intValue();
            }
            if (whichToEdit.equals("BOLD")) {
                boolean isBold = (request.getParameter("BOLD") != null) && (request.getParameter("BOLD").equals("on"));
                if (request.getParameter("BOLD") == null) {
                    System.out.println("no bold parameter");
                } else {
                    System.out.println(request.getParameter("BOLD"));
                }
                System.out.println("bold " + isBold);
                if (isBold) {
                    getFontStyle().put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
                    style = Font.BOLD;
                } else {
                    getFontStyle().put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
                }
                Float textposture = (Float) getFontStyle().get(TextAttribute.POSTURE);
                if (textposture != null && textposture.floatValue() == 0.2F) {
                    style |= Font.ITALIC;
                }
            }
            if (whichToEdit.equals("ITALIC")) {
                boolean isItalic = (request.getParameter("ITALIC") != null) && (request.getParameter("ITALIC").equals("on"));
                if (request.getParameter("ITALIC") == null) {
                    System.out.println("no ITALIC parameter");
                } else {
                    System.out.println(request.getParameter("ITALIC"));
                }
                System.out.println("ITALIC " + isItalic);
                if (isItalic) {
                    getFontStyle().put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
                    style |= Font.ITALIC;
                } else {
                    getFontStyle().put(TextAttribute.POSTURE, TextAttribute.POSTURE_REGULAR);
                }
                Float textweiht = (Float) getFontStyle().get(TextAttribute.WEIGHT);
                if (textweiht != null && textweiht.floatValue() == 2F) {
                    style |= Font.BOLD;
                }
            }
            if (whichToEdit.equals("UNDERLINE")) {
                boolean isUnderline = (request.getParameter("UNDERLINE") != null) && (request.getParameter("UNDERLINE").equals("on"));
                if (request.getParameter("UNDERLINE") == null) {
                    System.out.println("no UNDERLINE parameter");
                } else {
                    System.out.println(request.getParameter("UNDERLINE"));
                }
                System.out.println("UNDERLINE " + isUnderline);
                if (isUnderline) {
                    getFontStyle().put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                } else {
                    getFontStyle().remove(TextAttribute.UNDERLINE);
                }
                Float textweiht = (Float) getFontStyle().get(TextAttribute.WEIGHT);
                if (textweiht != null && textweiht.floatValue() == 2F) {
                    style |= Font.BOLD;
                }
                Float textposture = (Float) getFontStyle().get(TextAttribute.POSTURE);
                if (textposture != null && textposture.floatValue() == 0.2F) {
                    style |= Font.ITALIC;
                }
            }
            System.out.println("font style " + style);
            Font font = new Font(familyfontname, style, size);
            getFontStyle().put(TextAttribute.FONT, font);
        } else if (whichToEdit.equals("FEATURE_LABEL_COLOR")) {
            int a = ((Color) getFontStyle().get(TextAttribute.FOREGROUND)).getAlpha();
            Color newrgbColor = new Color(r, g, b, a);
            getFontStyle().put(TextAttribute.FOREGROUND, newrgbColor);
        } else if (whichToEdit.equals("FEATURE_LABEL_BGCOLOR")) {
            Color c = (Color) getFontStyle().get(TextAttribute.BACKGROUND);
            if (c == null) {
                c = (Color) getFontStyle().get("secondcolor");
            }
            int a = c.getAlpha();
            Color newrgbColor = new Color(r, g, b, a);
            getFontStyle().put(TextAttribute.BACKGROUND, newrgbColor);
            getFontStyle().put("secondcolor", newrgbColor);
        } else if (whichToEdit.equals("FONT_ALPHA")) {
            String previewFontAlpha = request.getParameter("FONT_ALPHA");
            int a = Integer.parseInt(previewFontAlpha);
            Color current_color = (Color) getFontStyle().get(TextAttribute.FOREGROUND);
            r = current_color.getRed();
            g = current_color.getGreen();
            b = current_color.getBlue();
            Color newrgbColor = new Color(r, g, b, a);
            getFontStyle().put(TextAttribute.FOREGROUND, newrgbColor);
        } else if (whichToEdit.equals("BACKGROUND_STYLE")) {
            String str = request.getParameter("BACKGROUND_STYLE");
            Object obj = getFontStyle().get(StringGraphics.FILTER_EFFECTS);
            Color color = (Color) getFontStyle().get("secondcolor");
            if (color == null) {
                System.out.println("LabelEditor: no secondcolor");
            } else {
                System.out.println("secondcolor " + color);
            }
            color = (Color) getFontStyle().get(TextAttribute.BACKGROUND);
            if (color == null) {
                System.out.println("LabelEditor: no background");
                color = Color.lightGray;
            } else {
                System.out.println("background " + color);
            }
            getFontStyle().put(StringGraphics.FILTER_EFFECTS, StringGraphics.FilterEffects.NONE);
            getFontStyle().remove("secondcolor");
            getFontStyle().remove(TextAttribute.BACKGROUND);
            if (str.equals("None")) {
                System.out.println(str + " oks");
                getFontStyle().put(StringGraphics.FILTER_EFFECTS, StringGraphics.FilterEffects.NONE);
                getFontStyle().remove("secondcolor");
                getFontStyle().remove(TextAttribute.BACKGROUND);
            } else if (str.equals("Outline")) {
                System.out.println(str);
                getFontStyle().put(StringGraphics.FILTER_EFFECTS, StringGraphics.FilterEffects.OUTLINE);
                getFontStyle().put("secondcolor", color);
            } else if (str.equals("Halo")) {
                System.out.println(str);
                getFontStyle().put(StringGraphics.FILTER_EFFECTS, StringGraphics.FilterEffects.HALO);
                getFontStyle().put("secondcolor", color);
            } else if (str.equals("Box")) {
                System.out.println(str + " oks");
                getFontStyle().put(StringGraphics.FILTER_EFFECTS, StringGraphics.FilterEffects.BOX);
                getFontStyle().put(TextAttribute.BACKGROUND, color);
            }
        } else if (whichToEdit.equals("BACKGROUND_ALPHA")) {
            String previewBgAlpha = request.getParameter("BACKGROUND_ALPHA");
            int a = Integer.parseInt(previewBgAlpha);
            Color current_bgcolor = (Color) getFontStyle().get(TextAttribute.BACKGROUND);
            r = current_bgcolor.getRed();
            g = current_bgcolor.getGreen();
            b = current_bgcolor.getBlue();
            Color newrgbColor = new Color(r, g, b, a);
            getFontStyle().put(TextAttribute.BACKGROUND, newrgbColor);
        }
    }

    public java.util.Map getFontStyle() {
        Object obj = lbl.get(StringGraphics.FILTER_EFFECTS);
        if (obj != null) {
            if (obj != null && obj == StringGraphics.FilterEffects.HALO) {
                System.out.println("LabelEditor.getFontStyle() halo");
            }
            if (obj != null && obj == StringGraphics.FilterEffects.OUTLINE) {
                System.out.println("LabelEditor.getFontStyle() outline");
            }
            if (obj != null && obj == StringGraphics.FilterEffects.BOX) {
                System.out.println("LabelEditor.getFontStyle() box");
            }
        } else {
            System.out.println("LabelEditor.getFontStyle() no filter effects!");
        }
        return lbl;
    }

    public void setFontStyle(java.util.Map map) {
        lbl = map;
    }
}
