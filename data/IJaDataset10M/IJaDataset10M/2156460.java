package net.yura.android.lcdui;

import java.util.HashMap;
import javax.microedition.lcdui.Font;
import net.yura.android.AndroidMeApp;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Typeface;
import android.util.DisplayMetrics;

public class FontManager {

    private static int SIZE_SMALL = 14;

    private static int SIZE_MEDIUM = 16;

    private static int SIZE_LARGE = 20;

    private static HashMap<Font, FontManager> fonts = new HashMap<Font, FontManager>();

    Paint paint;

    FontMetricsInt fontMetrics;

    public FontManager(Typeface typeface, int size, boolean underlined) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTypeface(typeface);
        paint.setTextSize(size);
        paint.setUnderlineText(underlined);
        fontMetrics = paint.getFontMetricsInt();
    }

    public static FontManager getFont(Font meFont) {
        FontManager result = fonts.get(meFont);
        if (result == null) {
            Typeface family = Typeface.SANS_SERIF;
            if (meFont.getFace() == Font.FACE_SYSTEM) {
                family = Typeface.SANS_SERIF;
            } else if (meFont.getFace() == Font.FACE_MONOSPACE) {
                family = Typeface.MONOSPACE;
            } else if (meFont.getFace() == Font.FACE_PROPORTIONAL) {
                family = Typeface.SANS_SERIF;
            }
            int style = 0;
            if ((meFont.getStyle() & Font.STYLE_PLAIN) != 0) {
                style |= Typeface.NORMAL;
            }
            if ((meFont.getStyle() & Font.STYLE_BOLD) != 0) {
                style |= Typeface.BOLD;
            }
            if ((meFont.getStyle() & Font.STYLE_ITALIC) != 0) {
                style |= Typeface.ITALIC;
            }
            boolean underlined = false;
            if ((meFont.getStyle() & Font.STYLE_UNDERLINED) != 0) {
                underlined = true;
            }
            int meFontSize = meFont.getSize();
            int size;
            if (meFontSize < 0) {
                size = -meFontSize;
            } else {
                int textAppearance;
                if (meFontSize == Font.SIZE_SMALL) {
                    size = SIZE_SMALL;
                    textAppearance = android.R.style.TextAppearance_Small;
                } else if (meFontSize == Font.SIZE_MEDIUM) {
                    size = SIZE_MEDIUM;
                    textAppearance = android.R.style.TextAppearance_Medium;
                } else {
                    size = SIZE_LARGE;
                    textAppearance = android.R.style.TextAppearance_Large;
                }
            }
            Context ctx = AndroidMeApp.getContext();
            DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
            size = (int) (size * metrics.scaledDensity);
            result = new FontManager(Typeface.create(family, style), size, underlined);
            fonts.put(meFont, result);
        }
        return result;
    }

    public int charWidth(Font f, char ch) {
        return getFont(f).charWidth(ch);
    }

    public int charsWidth(Font f, char[] ch, int offset, int length) {
        return getFont(f).charsWidth(ch, offset, length);
    }

    public int getBaselinePosition(Font f) {
        return getFont(f).getBaselinePosition();
    }

    public int getHeight(Font f) {
        return getFont(f).getHeight();
    }

    public int stringWidth(Font f, String str) {
        return getFont(f).stringWidth(str);
    }

    public Paint getPaint() {
        return paint;
    }

    public FontMetricsInt getFontMetricsInt() {
        return fontMetrics;
    }

    public int charWidth(char ch) {
        return (int) paint.measureText(new char[] { ch }, 0, 1);
    }

    public int charsWidth(char[] ch, int offset, int length) {
        return (int) paint.measureText(ch, offset, length);
    }

    public int getBaselinePosition() {
        return -fontMetrics.ascent;
    }

    public int getHeight() {
        return paint.getFontMetricsInt(fontMetrics);
    }

    public int stringWidth(String str) {
        return (int) paint.measureText(str);
    }
}
