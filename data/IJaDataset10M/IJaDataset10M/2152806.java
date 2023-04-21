package com.nexirius.framework.dataviewer;

import com.nexirius.framework.datamodel.ColorModel;
import com.nexirius.framework.datamodel.DataModel;
import com.nexirius.framework.datamodel.DataModelAdaptor;
import com.nexirius.framework.datamodel.DataModelEvent;
import com.nexirius.framework.swing.SwingViewer;
import javax.swing.*;
import java.awt.*;

public class ColorViewer implements SwingViewer {

    JLabel label = new JLabel("");

    ColorListener colorListener = new ColorListener();

    ColorModel colorModel;

    public ColorViewer() {
    }

    public JComponent createJComponent(ViewerFactory factory) {
        colorModel.addSoftDataModelListener(colorListener);
        label.setPreferredSize(new Dimension(16, 16));
        update();
        label.setOpaque(true);
        return label;
    }

    public void update() {
        label.setForeground(colorModel.getTextColor());
        label.setBackground(colorModel.getColor());
        label.setText(colorModel.getText());
        label.setHorizontalAlignment(JLabel.CENTER);
    }

    public boolean isEditor() {
        return false;
    }

    public void setDataModel(DataModel model) {
        colorModel = (ColorModel) model;
    }

    public DataModel getDataModel() {
        return colorModel;
    }

    class ColorListener extends DataModelAdaptor {

        public void dataModelChangeValue(DataModelEvent ev) {
            update();
        }
    }
}
