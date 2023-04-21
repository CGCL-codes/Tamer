package net.jalbum.filters;

import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import se.datadosen.jalbum.Msg;

public class ResizableObjectControl extends FilterControl {

    /**
	 *
	 */
    private static final long serialVersionUID = 5710178774723654057L;

    protected ResizableObjectPanel resizableObjectPanel;

    protected ResizableObject resizableObject;

    protected int objectWidth = 100;

    protected int objectHeight = 100;

    protected double X1;

    protected double Y1;

    protected double X2;

    protected double Y2;

    protected JCheckBox showOutline;

    public ResizableObjectControl() {
    }

    public ResizableObjectControl(ResizableObjectPanel resizableObjectPane) {
        init(resizableObjectPane);
        addResizableControls();
    }

    protected void init(ResizableObjectPanel resizableObjectPanel) {
        this.resizableObjectPanel = resizableObjectPanel;
        resizableObject = resizableObjectPanel.getResizableObject();
        super.init(resizableObjectPanel);
    }

    protected void addResizableControls() {
        showOutline = new JCheckBox(Msg.getString(this, "filters.resizable.showOutlines"), true);
        showOutline.addActionListener(this);
        this.add("p", showOutline);
    }

    public void update(double x1, double y1, double x2, double y2) {
        this.X1 = x1;
        this.Y1 = y1;
        this.X2 = x2;
        this.Y2 = y2;
    }

    public void setWidth(int width) {
        this.objectWidth = width;
    }

    public void setHeight(int height) {
        this.objectHeight = height;
    }

    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (e.getSource() == showOutline) {
            resizableObjectPanel.setShowOutline(showOutline.isSelected());
            resizableObjectPanel.renderPreview();
        }
    }

    public ResizableObject getResizableObject() {
        return resizableObject;
    }

    public double getX1() {
        return X1;
    }

    public void setX1(double x1) {
        this.X1 = x1;
    }

    public double getY1() {
        return Y1;
    }

    public void setY1(double y1) {
        this.Y1 = y1;
    }

    public double getX2() {
        return X2;
    }

    public void setX2(double x2) {
        this.X2 = x2;
    }

    public double getY2() {
        return Y2;
    }

    public void setY2(double y2) {
        this.Y2 = y2;
    }

    public int getObjectWidth() {
        return objectWidth;
    }

    public int getObjectHeight() {
        return objectHeight;
    }

    public void adjustResizableObject(ResizableObject resizableObject) {
        this.resizableObject = resizableObject;
    }

    public void setResizableObject(ResizableObject resizableObject) {
        this.resizableObject = resizableObject;
    }

    public void setObjectWidth(int objectWidth) {
        this.objectWidth = objectWidth;
    }

    public void setObjectHeight(int objectHeight) {
        this.objectHeight = objectHeight;
    }
}
