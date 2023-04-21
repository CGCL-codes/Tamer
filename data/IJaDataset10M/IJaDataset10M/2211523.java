package br.com.jmerg.components;

import br.com.jmerg.interfaces.IJMeRGAction;
import br.com.jmerg.interfaces.IJMeRGClick;
import br.com.jmerg.utils.JMeRGColor;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class JMeRGPanel extends JMeRGComponent implements IJMeRGClick {

    private String caption;

    private boolean visibleBorder;

    public JMeRGPanel(String caption) {
        super();
        this.caption = caption;
        this.height = 100;
        this.width = 100;
        this.visibleBorder = true;
    }

    public JMeRGPanel() {
        this("Panel");
    }

    public void addComponent(JMeRGComponent component) {
        super.addComponent(component);
    }

    public JMeRGColor getBackgroudColor() {
        return super.getBackgroudColor();
    }

    public String getCaption() {
        return this.caption;
    }

    public JMeRGColor getCaptionColor() {
        return super.getCaptionColor();
    }

    public Font getCaptionFont() {
        return super.getCaptionFont();
    }

    public boolean isDown() {
        return false;
    }

    public boolean isVisibleBorder() {
        return visibleBorder;
    }

    public boolean isTransparent() {
        return super.isTransparent();
    }

    public void onClick() {
        if (this.onClick != null) {
            this.onClick.execute();
        }
    }

    public void paintBody(Graphics graphics) {
        graphics.setColor(this.getBackgroudColor().getR(), this.getBackgroudColor().getG(), this.getBackgroudColor().getB());
        graphics.fillRect((this.xParent + this.x), (this.yParent + this.y), this.width - 1, this.height - 1);
    }

    public void paintCaption(Graphics graphics) {
        graphics.setFont(this.captionFont);
        graphics.setColor(this.getCaptionColor().getR(), this.getCaptionColor().getG(), this.getCaptionColor().getB());
        graphics.drawString(this.caption, this.xParent + x + (this.width / 2), (this.yParent + y + (this.height / 2)) + 5, Graphics.BASELINE | Graphics.HCENTER);
    }

    public void paintBorder(Graphics graphics) {
        if (this.visibleBorder) {
            graphics.setColor(255, 255, 255);
            graphics.drawLine((this.xParent + this.x), (this.yParent + this.y), (this.xParent + this.x + this.width - 1), (this.yParent + this.y));
            graphics.drawLine((this.xParent + this.x), (this.yParent + this.y), (this.xParent + this.x), ((this.yParent + this.y) + this.height - 1));
            graphics.setColor(200, 200, 200);
            graphics.drawLine((this.xParent + this.x), ((this.yParent + this.y) + this.height - 1), ((this.xParent + this.x) + this.width - 1), ((this.yParent + this.y) + this.height - 1));
            graphics.drawLine(((this.xParent + this.x) + this.width - 1), (this.yParent + this.y), ((this.xParent + this.x) + this.width - 1), ((this.yParent + this.y) + this.height - 1));
        }
    }

    public void paintSelection(Graphics graphics) {
    }

    public void setBackgroudColor(JMeRGColor backgroudColor) {
        super.setBackgroudColor(backgroudColor);
    }

    public void setCaption(String caption) {
        this.caption = caption;
        this.repaintComponent();
    }

    public void setCaptionColor(JMeRGColor captionColor) {
        super.setCaptionColor(captionColor);
    }

    public void setCaptionFont(Font captionFont) {
        super.setCaptionFont(captionFont);
    }

    public void setDown(boolean down) {
    }

    public void setHeight(int height) {
        super.setHeight(height);
    }

    public void setOnClick(IJMeRGAction action) {
        this.onClick = action;
    }

    public void setParent(JMeRGComponent component) {
        super.setParent(component);
    }

    public void setTransparent(boolean transparent) {
        super.setTransparent(transparent);
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

    public void setVisibleBorder(boolean visibleBorder) {
        this.visibleBorder = visibleBorder;
    }
}
