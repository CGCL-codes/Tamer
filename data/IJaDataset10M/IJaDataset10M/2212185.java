package com.sksdpt.kioskjui.gui.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import com.sksdpt.kioskjui.control.config.VSess;

/**
 * WrapperButton
 * 
 * @author "Luis Alfonso Vega Garcia" <vegacom@gmail.com>
 */
public class WrapperButton implements IMyButton {

    private Button button;

    /**
     * 
     */
    public WrapperButton(Composite parent, int style) {
        button = new Button(parent, style);
    }

    public Point getSize() {
        return button.getSize();
    }

    public void setData(Object data) {
        button.setData(data);
    }

    public void setEnabled(boolean enabled) {
        button.setEnabled(enabled);
    }

    public void setImagePath(String imagePath) {
        Image img = VSess.sngltn().getGuiHelper().createImage(imagePath);
        button.setImage(img);
        button.setSize(200, img.getBounds().height);
    }

    public void setLocation(int x, int y) {
        button.setLocation(x, y);
    }

    public void setSelectListener(Listener listener) {
        button.addListener(SWT.Selection, listener);
    }

    public void setText(String text) {
        button.setText(text);
    }

    public void setVisible(boolean visible) {
        button.setVisible(visible);
    }
}
