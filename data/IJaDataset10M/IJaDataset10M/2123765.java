package org.nightlabs.jfire.fontregistry.ui;

import java.text.DateFormat;
import org.nightlabs.fontregistry.model.FontFile;

class FontFileNode implements TreeNode<FontFileNode>, Comparable<FontFileNode> {

    private FontFile fontFile;

    private final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

    public FontFileNode(FontFile fontFile) {
        this.fontFile = fontFile;
    }

    @Override
    public FontFileNode[] getChildren() {
        return new FontFileNode[0];
    }

    @Override
    public String getLabel(Column column) {
        switch(column) {
            case FONT_NAME:
                return fontFile.getFont().getName();
            case FONT_STYLE:
                return fontFile.getFontStyle().toString();
            case FONT_SIZE:
                return fontFile.fontSizesToString();
            case CHANGE_DT:
                return df.format(fontFile.getChangeDate());
            case DATA_SIZE:
                return String.valueOf(fontFile.getFontFileDataSizeKb()) + " KB";
            case FONT_FILE_TYPE:
                return fontFile.getFontFileType().getName();
        }
        return "";
    }

    public FontFile getFontFile() {
        return fontFile;
    }

    private static final Column[] compareOrder = { Column.FONT_NAME, Column.FONT_FILE_TYPE, Column.FONT_STYLE, Column.FONT_SIZE };

    @Override
    public int compareTo(FontFileNode o) {
        for (Column col : compareOrder) {
            int result = compareColumnLabel(this, o, col);
            if (result != 0) return result;
        }
        return 0;
    }

    protected static int compareColumnLabel(FontFileNode node1, FontFileNode node2, Column column) {
        return node1.getLabel(column).compareToIgnoreCase(node2.getLabel(column));
    }
}
