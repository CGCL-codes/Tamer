package org.deft.vextoolkit.layout;

import net.sf.vex.core.ColorResource;
import net.sf.vex.core.FontResource;
import net.sf.vex.core.Graphics;
import net.sf.vex.css.Styles;
import net.sf.vex.dom.Element;
import net.sf.vex.layout.LayoutContext;
import net.sf.vex.swt.SwtColor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class SelectedCodeTextBox extends AbstractCodeStyleBox {

    public SelectedCodeTextBox(String text, int startOffset, int endOffset, Element element) {
        super(text, startOffset, endOffset, element);
    }

    public void paint(LayoutContext context, int x, int y) {
        Styles styles = context.getStyleSheet().getStyles(getElement());
        Graphics g = context.getGraphics();
        FontResource font = g.createFont(styles.getFont());
        ColorResource color = g.createColor(styles.getColor());
        FontResource oldFont = g.setFont(font);
        ColorResource oldColor = g.setColor(g.getSystemColor(ColorResource.SELECTION_FOREGROUND));
        g.fillRect(x, y + 1, g.stringWidth(getText()), styles.getLineHeight() - 2);
        g.setColor(new SwtColor(new Color(Display.getCurrent(), 120, 13, 80)));
        g.drawText(this.getText(), x, y);
        g.setFont(oldFont);
        g.setColor(oldColor);
        font.dispose();
        color.dispose();
    }

    public CodeBoxPair splitAt(int offset, String indent) {
        String leftText = getText().substring(0, offset - getStartOffset());
        String rightText = getText().substring(offset - getStartOffset());
        rightText = indent + rightText;
        return new CodeBoxPair(new SelectedCodeTextBox(leftText, getStartOffset(), offset, getElement()), new SelectedCodeTextBox(rightText, offset, getEndOffset() + indent.length(), getElement()));
    }
}
