package org.gocha.gef.gui.tree;

import org.gocha.gef.Glyph;

/**
 * Событие дерева объектов
 * @author gocha
 */
public class GlyphTreeEvent {

    private GlyphTreeModel src = null;

    private int code = 0;

    private Glyph glyph = null;

    public static final int TREE_CHILDREN_CHANGED = 0;

    public static final int CHILD_ADDED = 1;

    public static final int CHILD_REMOVED = 2;

    public GlyphTreeEvent(GlyphTreeModel src, int code, Glyph glyph) {
        this.src = src;
        this.code = code;
        this.glyph = glyph;
    }

    public int getCode() {
        return code;
    }

    public Glyph getGlyph() {
        return glyph;
    }

    public GlyphTreeModel getSrc() {
        return src;
    }
}
