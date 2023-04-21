package org.deft.vextoolkit.layout;

import net.sf.vex.core.FontResource;
import net.sf.vex.core.Graphics;
import net.sf.vex.css.Styles;
import net.sf.vex.dom.Element;
import net.sf.vex.layout.LayoutContext;

public abstract class AbstractCodeStyleBox {

    private String text;

    private Element element;

    private int startOffset;

    private int endOffset;

    /**
	 * Is true if the box was indented, which means in this case that there are additional leading whitespaces.
	 */
    private boolean indented;

    public AbstractCodeStyleBox(String text, int startOffset, int endOffset, Element e) {
        this.text = text;
        this.element = e;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.indented = false;
    }

    public String getText() {
        return this.text;
    }

    public void setIndented(boolean b) {
        this.indented = b;
    }

    public boolean isIndented() {
        return this.indented;
    }

    public int getTextWidth(LayoutContext context) {
        Graphics g = context.getGraphics();
        Styles styles = context.getStyleSheet().getStyles(element);
        FontResource font = g.createFont(styles.getFont());
        FontResource oldFont = g.setFont(font);
        int width = g.stringWidth(text);
        g.setFont(oldFont);
        font.dispose();
        return width;
    }

    public int getEndOffset() {
        return endOffset;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public Element getElement() {
        return element;
    }

    public void adjustOffsets(int change) {
        this.startOffset += change;
        this.endOffset += change;
    }

    public abstract void paint(LayoutContext context, int x, int y);

    public abstract CodeBoxPair splitAt(int offset, String indent);

    public class CodeBoxPair {

        private AbstractCodeStyleBox left;

        private AbstractCodeStyleBox right;

        public AbstractCodeStyleBox getLeft() {
            return left;
        }

        public AbstractCodeStyleBox getRight() {
            return right;
        }

        public CodeBoxPair(AbstractCodeStyleBox left, AbstractCodeStyleBox right) {
            this.left = left;
            this.right = right;
        }
    }
}
