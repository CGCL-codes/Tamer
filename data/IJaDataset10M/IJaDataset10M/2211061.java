package dde.text;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.SwingUtilities;
import dde.core.DDComponent;
import dde.core.DDConfiguration;
import dde.core.DDGame;
import dde.util.DDUtil;

public class DDText extends DDComponent {

    private int width;

    private int height;

    private boolean centralizedOnScreen;

    private Color color;

    private String text;

    private Font font;

    private FontMetrics metrics;

    private Shape area;

    public DDText(String text, Font font) {
        setText(text);
        setColor(Color.BLACK);
        setFont(font);
        setArea(new Rectangle());
    }

    public DDText(String text) {
        this(text, DDUtil.getDefaultFont());
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void render(Graphics g) {
        g.setColor(getColor());
        g.setFont(getFont());
        metrics = g.getFontMetrics();
        setWidth(SwingUtilities.computeStringWidth(metrics, getText()));
        int ascent = metrics.getAscent();
        setHeight((ascent == 0 ? getFont().getSize() : ascent));
        int x = getLocation().x;
        int y = getLocation().y;
        if (isCentralizedOnScreen()) {
            DDConfiguration config = DDGame.getInstance().getConfig();
            x = config.getWidth() / 2;
            x -= getWidth() / 2;
        }
        int areaY = y - getHeight();
        setArea(new Rectangle(x, (areaY < 0 ? 0 : areaY), getWidth(), getHeight()));
        g.drawString(getText(), x, y);
    }

    public boolean isCentralizedOnScreen() {
        return centralizedOnScreen;
    }

    public void setCentralizedOnScreen(boolean centralizedOnScreen) {
        this.centralizedOnScreen = centralizedOnScreen;
    }

    public Shape getArea() {
        return area;
    }

    public void setArea(Shape area) {
        this.area = area;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
