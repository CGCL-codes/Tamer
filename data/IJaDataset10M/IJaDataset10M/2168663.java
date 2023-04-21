package org.fao.fenix.web.modules.birt.client.view.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BeanLegend implements Serializable {

    private boolean visible;

    private int sizeLabel;

    private String position;

    private List color;

    private List colorFromClientToServer;

    public BeanLegend() {
        super();
        this.color = new ArrayList();
        this.colorFromClientToServer = new ArrayList();
    }

    public int getSizeLabel() {
        return sizeLabel;
    }

    public void setSizeLabel(int sizeLabel) {
        this.sizeLabel = sizeLabel;
    }

    public List getColorFromClientToServer() {
        return colorFromClientToServer;
    }

    public void setColorFromClientToServer(String color) {
        this.colorFromClientToServer.add(color);
    }

    public List getColor() {
        return color;
    }

    public void setColor(List color) {
        this.color.add(color);
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
