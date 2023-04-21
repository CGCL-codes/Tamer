package de.anormalmedia.touchui.component.progress;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import de.anormalmedia.touchui.Dimension;
import de.anormalmedia.touchui.Insets;
import de.anormalmedia.touchui.Rectangle;
import de.anormalmedia.touchui.UIConstants;
import de.anormalmedia.touchui.UIFactory;
import de.anormalmedia.touchui.border.AbstractBorder;
import de.anormalmedia.touchui.border.LineBorder;
import de.anormalmedia.touchui.component.Component;
import de.anormalmedia.touchui.paint.AbstractPaint;
import de.anormalmedia.touchui.paint.Color;
import de.anormalmedia.touchui.paint.ColorPaint;

public class ProgressBar extends Component {

    private int maximum = 100;

    private int value = 0;

    private Font font = Font.getDefaultFont();

    private AbstractPaint barPaint;

    private Color foreground = new Color(255, 255, 255);

    private boolean stringPainted = false;

    public ProgressBar() {
        setBorder(new LineBorder(new Color(100, 100, 100)));
        setBarPaint(new ColorPaint(new Color(75, 75, 57)));
        UIFactory.styleProgressBar(this);
    }

    public ProgressBar(int maximum) {
        this();
        setMaximum(maximum);
    }

    public void setStringPainted(boolean stringPainted) {
        this.stringPainted = stringPainted;
    }

    public boolean isStringPainted() {
        return stringPainted;
    }

    public AbstractPaint getBarPaint() {
        return barPaint;
    }

    public void setBarPaint(AbstractPaint barPaint) {
        this.barPaint = barPaint;
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Dimension getPreferredSize() {
        int prefWidth = 0;
        int prefHeight = 0;
        Insets insets = getInsets();
        if (insets == null) {
            insets = new Insets(0, 0, 0, 0);
        }
        AbstractBorder localBorder = getBorder();
        Insets borderInsets = new Insets(0, 0, 0, 0);
        if (localBorder != null) {
            borderInsets = localBorder.getBorderInsets();
        }
        String localtext = "100%";
        prefWidth = font.stringWidth(localtext) + insets.getLeft() + insets.getRight() + borderInsets.getLeft() + borderInsets.getRight();
        prefHeight = font.getHeight() + insets.getTop() + insets.getBottom() + borderInsets.getTop() + borderInsets.getBottom();
        return new Dimension(prefWidth, prefHeight);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Rectangle b = getBounds();
        AbstractBorder localBorder = getBorder();
        Insets borderInsets = new Insets(0, 0, 0, 0);
        if (localBorder != null) {
            borderInsets = localBorder.getBorderInsets();
        }
        Insets insets = getInsets();
        if (insets == null) {
            insets = new Insets(0, 0, 0, 0);
        }
        if (getValue() > 0 && barPaint != null) {
            int maxWidth = b.getWidth() - borderInsets.getLeft() - borderInsets.getRight();
            double valueWidth = maxWidth / (double) getMaximum() * getValue();
            barPaint.paint(g, b.getX() + borderInsets.getLeft(), b.getY() + borderInsets.getTop(), (int) valueWidth, b.getHeight() - borderInsets.getTop() - borderInsets.getBottom());
        }
        if (stringPainted && foreground != null) {
            String text = ((int) ((double) getValue() / (double) getMaximum() * 100)) + "%";
            g.setColor(foreground.getRed(), foreground.getGreen(), foreground.getBlue());
            int yOffset = b.getHeight() == UIConstants.UNSET ? 0 : (b.getHeight() - insets.getTop() - insets.getBottom() - borderInsets.getTop() - borderInsets.getBottom()) / 2 - font.getHeight() / 2;
            int xOffset = b.getWidth() == UIConstants.UNSET ? 0 : (b.getWidth() - insets.getLeft() - insets.getRight() - borderInsets.getLeft() - borderInsets.getRight()) / 2 - ((text == null ? 0 : font.stringWidth(text))) / 2;
            g.drawString(text, b.getX() + xOffset + insets.getLeft() + borderInsets.getLeft(), b.getY() + yOffset + insets.getTop() + borderInsets.getTop(), Graphics.LEFT | Graphics.TOP);
        }
    }
}
