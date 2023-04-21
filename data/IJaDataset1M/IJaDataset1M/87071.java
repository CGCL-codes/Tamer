package org.openremote.irbuilder.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author allen.wei
 */
@XStreamAlias("button")
public class IPhoneButton extends BusinessEntity {

    @XStreamAsAttribute
    String label;

    @XStreamAsAttribute
    String icon;

    @XStreamAsAttribute
    int x;

    @XStreamAsAttribute
    int y;

    @XStreamAsAttribute
    int width;

    @XStreamAsAttribute
    int height;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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
}
