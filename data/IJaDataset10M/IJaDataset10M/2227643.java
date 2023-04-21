package br.com.jmerg.components;

import javax.microedition.lcdui.Graphics;

public class JMeRGBevel extends JMeRGComponent {

    public JMeRGBevel() {
        super();
        this.height = 100;
        this.width = 100;
    }

    public void paintBody(Graphics graphics) {
    }

    public void paintCaption(Graphics graphics) {
    }

    public void paintBorder(Graphics graphics) {
        graphics.setColor(200, 200, 200);
        graphics.drawLine((this.xParent + this.x), (this.yParent + this.y), ((this.xParent + this.x) + this.width) - 1, (this.yParent + this.y));
        graphics.drawLine((this.xParent + this.x), (this.yParent + this.y), (this.xParent + this.x), ((this.yParent + this.y) + this.height));
        graphics.setColor(255, 255, 255);
        graphics.drawLine((this.xParent + this.x), ((this.yParent + this.y) + this.height), ((this.xParent + this.x) + this.width), (this.yParent + this.y) + this.height);
        graphics.drawLine(((this.xParent + this.x) + this.width), (this.yParent + this.y), ((this.xParent + this.x) + this.width), ((this.yParent + this.y) + this.height));
    }

    public void paintSelection(Graphics graphics) {
    }

    public void setHeight(int height) {
        super.setHeight(height);
    }

    public void setParent(JMeRGComponent component) {
        super.setParent(component);
    }

    public void setWidth(int width) {
        super.setWidth(width);
    }

    public void setX(int x) {
        super.setX(x);
    }

    public void setY(int y) {
        super.setY(y);
    }
}
