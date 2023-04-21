package phaenuhr.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

public class Texture {

    private TexturePaint texture;

    private Textures style;

    private Color color;

    public Texture(Textures style, Color color) {
        this.style = style;
        this.color = color;
        this.texture = createTexture(style, color);
    }

    private TexturePaint createTexture(Textures style, Color color) {
        TexturePaint texture = null;
        BufferedImage bi = new BufferedImage(6, 6, BufferedImage.TYPE_INT_RGB);
        Graphics2D big = bi.createGraphics();
        Rectangle r = new Rectangle(0, 0, 4, 4);
        switch(style) {
            case DIAGONAL_RECHTS:
                Rectangle r2 = new Rectangle(0, 0, 6, 6);
                big.setColor(Color.white);
                big.fillRect(0, 0, 6, 6);
                big.setColor(color);
                big.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
                big.drawLine(6, -1, -1, 6);
                texture = new TexturePaint(bi, r2);
                break;
            case VERTIKAL_ENG:
                big.setColor(Color.white);
                big.fillRect(0, 0, 8, 8);
                big.setColor(color);
                big.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
                big.drawLine(4, 0, 4, 8);
                texture = new TexturePaint(bi, r);
                break;
            case DIAGONAL_LINKS:
                Rectangle r3 = new Rectangle(0, 0, 6, 6);
                big.setColor(Color.white);
                big.fillRect(0, 0, 6, 6);
                big.setColor(color);
                big.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
                big.drawLine(8, 8, -1, -1);
                texture = new TexturePaint(bi, r3);
                break;
            case HORIZONTAL_ENG:
                big.setColor(Color.white);
                big.fillRect(0, 0, 6, 6);
                big.setColor(color);
                big.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
                big.drawLine(0, 4, 8, 4);
                texture = new TexturePaint(bi, r);
                break;
            case PUNKTE_ENG:
                big.setColor(Color.white);
                big.fillRect(0, 0, 6, 6);
                big.setColor(color);
                big.fillOval(0, 0, 4, 4);
                texture = new TexturePaint(bi, r);
                break;
            case VERTIKAL_GESTRICHELT:
                bi = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
                big = bi.createGraphics();
                big.setColor(Color.white);
                big.fillRect(0, 0, 20, 20);
                big.setColor(color);
                float dash5[] = { 8.0f, 12.0f };
                BasicStroke dashed5 = new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, dash5, 0.0f);
                big.setStroke(dashed5);
                big.drawLine(10, 0, 10, 20);
                Rectangle r5 = new Rectangle(0, 0, 8, 16);
                texture = new TexturePaint(bi, r5);
                break;
            case HORIZONTAL_GESTRICHELT:
                bi = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
                big = bi.createGraphics();
                big.setColor(Color.white);
                big.fillRect(0, 0, 20, 20);
                big.setColor(color);
                float dash6[] = { 8.0f, 12.0f };
                BasicStroke dashed6 = new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, dash6, 0.0f);
                big.setStroke(dashed6);
                big.drawLine(0, 10, 20, 10);
                Rectangle r6 = new Rectangle(0, 0, 16, 8);
                texture = new TexturePaint(bi, r6);
                break;
            case HORIZONTAL_WEIT:
                bi = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
                big = bi.createGraphics();
                big.setColor(Color.white);
                big.fillRect(0, 0, 20, 20);
                big.setColor(color);
                big.setStroke(new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
                big.drawLine(0, 10, 20, 10);
                Rectangle r7 = new Rectangle(0, 0, 16, 8);
                texture = new TexturePaint(bi, r7);
                break;
            case VERTIKAL_WEIT:
                bi = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
                big = bi.createGraphics();
                big.setColor(Color.white);
                big.fillRect(0, 0, 20, 20);
                big.setColor(color);
                big.setStroke(new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
                big.drawLine(10, 0, 10, 20);
                Rectangle r8 = new Rectangle(0, 0, 8, 16);
                texture = new TexturePaint(bi, r8);
                break;
            case PUNKTE_WEIT:
                bi = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
                big = bi.createGraphics();
                big.setColor(Color.white);
                big.fillRect(0, 0, 20, 20);
                big.setColor(color);
                big.fillOval(0, 0, 6, 6);
                Rectangle r9 = new Rectangle(0, 0, 8, 8);
                texture = new TexturePaint(bi, r9);
                break;
            default:
                bi = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
                big = bi.createGraphics();
                big.setColor(Color.white);
                big.fillRect(0, 0, 20, 20);
                big.setColor(color);
                big.fillOval(0, 0, 6, 6);
                r9 = new Rectangle(0, 0, 8, 8);
                texture = new TexturePaint(bi, r9);
                break;
        }
        return texture;
    }

    public TexturePaint getTexturePaint() {
        return texture;
    }

    public Color getColor() {
        return color;
    }

    public Textures getStyle() {
        return style;
    }

    public void setColor(Color color) {
        this.color = color;
        texture = createTexture(style, color);
    }

    public String toString() {
        return "Style=" + getStyle() + " Color=" + getColor();
    }
}
