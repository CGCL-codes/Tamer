package org.fao.waicent.xmap2D.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.text.NumberFormat;
import org.fao.waicent.attributes.DataLegendDefinition;
import org.fao.waicent.attributes.DataLegendDefinitionEntry;
import org.fao.waicent.util.TranslationTable;

public class defaultDataLegendRenderer implements dataLegendRendererInterface {

    public Dimension getPreferredSize(DataLegendDefinition legend, int style, FontMetrics fm, NumberFormat format, TranslationTable lang, String title, String less, String more) {
        return getPreferredSize(legend, style, fm, format, lang, title, less, more, 0);
    }

    public Dimension getPreferredSize(DataLegendDefinition legend, int style, FontMetrics fm, NumberFormat format, TranslationTable lang, String title, String less, String more, int depth) {
        int width = 0;
        int height = 0;
        int INDENT = depth * 20;
        int POST = 10;
        int count = 0;
        for (int i = 0; i < legend.size(); i++) {
            if (legend.at(i).isDisplay()) {
                count++;
            }
        }
        DataLegendDefinitionEntry entry = legend.at(DataLegendDefinition.UNKNOWN);
        if (entry != null && entry.getPatternOutline() != null && entry.isDisplay() && !entry.getPatternOutline().isTransparent()) {
            count++;
        }
        entry = legend.at(DataLegendDefinition.UNCLASSIFIED);
        if (entry != null && entry.getPatternOutline() != null && entry.isDisplay() && !entry.getPatternOutline().isTransparent()) {
            count++;
        }
        if (title != null && !title.equals("")) {
            count++;
            String lang_title = title;
            if (lang != null) {
                lang_title = lang.translate(title);
            }
            width = fm.stringWidth(lang_title) + POST;
        }
        width = Math.max(width, getMaxWidth(legend, fm, format, lang, getSpaceWidth(fm), less, more)) + INDENT;
        height = count * (fm.getHeight() + fm.getLeading());
        return new Dimension(width, height);
    }

    public void paint(DataLegendDefinition legend, Graphics g, int style, NumberFormat format, TranslationTable lang, String title, String less, String more, boolean dash) {
        paint(legend, g, style, format, lang, title, less, more, dash, 0);
    }

    public void paint(DataLegendDefinition legend, Graphics g, int style, NumberFormat format, TranslationTable lang, String title, String less, String more, boolean dash, int depth) {
        FontMetrics fm = g.getFontMetrics();
        int space_width = getSpaceWidth(fm);
        Dimension dim = getPreferredSize(legend, style, fm, format, lang, title, less, more);
        int max_text_width = getMaxTextWidth(legend, fm, format, lang);
        int max_lower_width = getMaxLowerWidth(legend, fm, format, lang, less);
        int max_upper_width = getMaxUpperWidth(legend, fm, format, lang, more);
        int max_range_width = Math.max(max_lower_width, max_upper_width);
        g.setColor(Color.white);
        g.fillRect(0, 0, dim.width, dim.height);
        g.setColor(Color.black);
        g.drawRect(0, 0, dim.width, dim.height);
        int width = dim.width - space_width;
        Point origin = new Point();
        origin.y = fm.getLeading() / 2;
        origin.x = space_width / 2;
        if (title != null && !title.equals("")) {
            String lang_title = title;
            if (lang != null) {
                lang_title = lang.translate(title);
            }
            int title_width = fm.stringWidth(lang_title);
            origin.x += Math.abs((width - title_width) / 2);
            g.drawString(lang_title, origin.x, origin.y + fm.getAscent());
            origin.y += fm.getHeight();
        }
        int shape_width = getShapeSize(fm);
        width -= shape_width;
        width -= space_width / 2;
        DataLegendDefinitionEntry entry = null;
        for (int i = 0; i < legend.size(); i++) {
            entry = legend.at(i);
            if ((entry == null) || (!entry.isDisplay())) {
                continue;
            }
            paintEntry(legend, g, style, format, lang, less, more, entry, origin, shape_width, space_width, max_lower_width, max_upper_width, dash);
            origin.y += fm.getHeight() + fm.getLeading();
        }
        entry = legend.at(legend.UNKNOWN);
        if (entry != null && entry.isDisplay() && entry.getPatternOutline() != null && !entry.getPatternOutline().isTransparent()) {
            paintEntry(legend, g, style, format, lang, less, more, entry, origin, shape_width, space_width, max_lower_width, max_upper_width, dash);
            origin.y += fm.getHeight() + fm.getLeading();
        }
        entry = legend.at(legend.UNCLASSIFIED);
        if (entry != null && entry.isDisplay() && entry.getPatternOutline() != null && !entry.getPatternOutline().isTransparent()) {
            paintEntry(legend, g, style, format, lang, less, more, entry, origin, shape_width, space_width, max_lower_width, max_upper_width, dash);
            origin.y += fm.getHeight() + fm.getLeading();
        }
    }

    public void paintEntry(DataLegendDefinition legend, Graphics g, int style, NumberFormat format, TranslationTable lang, String less, String more, DataLegendDefinitionEntry entry, Point origin, int shape_width, int space_width, int max_lower_width, int max_upper_width, boolean dash) {
        paintEntry(legend, g, style, format, lang, less, more, entry, origin, shape_width, space_width, max_lower_width, max_upper_width, dash, 0);
    }

    public void paintEntry(DataLegendDefinition legend, Graphics g, int style, NumberFormat format, TranslationTable lang, String less, String more, DataLegendDefinitionEntry entry, Point origin, int shape_width, int space_width, int max_lower_width, int max_upper_width, boolean dash, int depth) {
        int dash_spaces = 2;
        FontMetrics fm = g.getFontMetrics();
        origin.x = space_width;
        Rectangle r = new Rectangle(origin.x, origin.y + (fm.getHeight() - shape_width) / 2, shape_width, shape_width);
        if (entry.getPatternOutline() == null) {
            g.setColor(Color.black);
            g.drawRect(r.x, r.y, r.width, r.height);
            entry.setArea(r);
        } else {
            entry.getPatternOutline().paintLegendIcon((Graphics2D) g, r);
            entry.setArea(r);
        }
        g.setColor(Color.black);
        origin.x += shape_width;
        origin.x += space_width;
        if (entry.hasLabel()) {
            origin.x = shape_width;
            origin.x += space_width;
            origin.x += space_width;
            String label = entry.getLabel();
            if (lang != null) {
                label = lang.translate(label);
            }
            g.drawString(label, origin.x, origin.y + fm.getAscent());
        } else if (entry.hasSet()) {
            origin.x = shape_width;
            origin.x += space_width;
            origin.x += space_width;
            g.drawString(entry.getSetLabel(format), origin.x, origin.y + fm.getAscent());
        } else {
            String lower_label = legend.getEntryLowerLabel(format, lang, less, entry);
            int lower_width = fm.stringWidth(lower_label);
            origin.x = shape_width;
            origin.x += space_width;
            origin.x += space_width;
            origin.x += Math.abs(max_lower_width - lower_width);
            g.drawString(lower_label, origin.x, origin.y + fm.getAscent());
            String upper_label = legend.getEntryUpperLabel(format, lang, more, entry);
            int upper_width = fm.stringWidth(upper_label);
            if (dash) {
                origin.x = shape_width;
                origin.x += space_width;
                origin.x += space_width;
                origin.x += max_lower_width;
                origin.x += space_width * 2;
                g.drawString("-", origin.x, origin.y + fm.getAscent());
                dash_spaces = 4;
            }
            origin.x = space_width;
            origin.x += shape_width;
            origin.x += space_width;
            origin.x += max_lower_width;
            origin.x += space_width * dash_spaces;
            origin.x += Math.abs(max_upper_width - upper_width);
            g.drawString(upper_label, origin.x, origin.y + fm.getAscent());
        }
    }

    public int getMaxTextWidth(DataLegendDefinition legend, FontMetrics fm, NumberFormat format, TranslationTable lang) {
        int width = 0;
        for (int i = 0; i < legend.size(); i++) {
            width = Math.max(width, legend.getEntryTextWidth(fm, format, lang, legend.at(i)));
        }
        DataLegendDefinitionEntry entry = legend.at(legend.UNKNOWN);
        if (entry != null && entry.getPatternOutline() != null && !entry.getPatternOutline().isTransparent()) {
            width = Math.max(width, legend.getEntryTextWidth(fm, format, lang, entry));
        }
        entry = legend.at(legend.UNCLASSIFIED);
        if (entry != null && entry.getPatternOutline() != null && !entry.getPatternOutline().isTransparent()) {
            width = Math.max(width, legend.getEntryTextWidth(fm, format, lang, entry));
        }
        return width;
    }

    public int getMaxUpperWidth(DataLegendDefinition legend, FontMetrics fm, NumberFormat format, TranslationTable lang, String more) {
        int width = 0;
        for (int i = 0; i < legend.size(); i++) {
            width = Math.max(width, legend.getEntryUpperWidth(fm, format, lang, more, legend.at(i)));
        }
        return width;
    }

    public int getMaxLowerWidth(DataLegendDefinition legend, FontMetrics fm, NumberFormat format, TranslationTable lang, String less) {
        int width = 0;
        for (int i = 0; i < legend.size(); i++) {
            width = Math.max(width, legend.getEntryLowerWidth(fm, format, lang, less, legend.at(i)));
        }
        return width;
    }

    public int getMaxWidth(DataLegendDefinition legend, FontMetrics fm, NumberFormat format, TranslationTable lang, int space_width, String less, String more) {
        int width = getMaxTextWidth(legend, fm, format, lang);
        int max_lower_width = getMaxLowerWidth(legend, fm, format, lang, less);
        int max_upper_width = getMaxUpperWidth(legend, fm, format, lang, more);
        int max_range_width = Math.max(max_lower_width, max_upper_width);
        width = Math.max(width, max_range_width + (2 * space_width) + max_range_width);
        width += 6 * space_width + getShapeSize(fm);
        return width;
    }

    public int getSpaceWidth(FontMetrics fm) {
        return 2 * fm.charWidth(' ');
    }

    public int getShapeSize(FontMetrics fm) {
        return fm.getAscent() + fm.getDescent();
    }
}
