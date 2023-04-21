package com.nexirius.tools.properties.dataviewer;

import com.nexirius.framework.dataeditor.StructEditor;
import com.nexirius.framework.datamodel.DataModel;
import com.nexirius.framework.datamodel.DataModelCommandVector;
import com.nexirius.framework.dataviewer.ViewerFactory;
import com.nexirius.framework.swing.SwingViewer;
import com.nexirius.tools.properties.model.EntryArrayModel;
import com.nexirius.tools.properties.model.MainModel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainViewer implements SwingViewer {

    protected MainModel mainModel;

    public JComponent createJComponent(ViewerFactory factory) {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JComponent table = null;
        try {
            table = factory.createDefaultViewer(mainModel.getChild(EntryArrayModel.FIELD_NAME)).getJComponent();
            panel.add(table, BorderLayout.CENTER);
            DataModelCommandVector v = new DataModelCommandVector();
            v.add(mainModel.getMethod(MainModel.COMMAND_ADD));
            v.add(mainModel.getMethod(MainModel.COMMAND_EDIT));
            v.add(mainModel.getMethod(MainModel.COMMAND_DELETE));
            v.add(mainModel.getMethod(MainModel.COMMAND_DUPLICATE));
            panel.add(StructEditor.createButtonPanel(factory, v, true), BorderLayout.SOUTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return panel;
    }

    public boolean isEditor() {
        return true;
    }

    public void setDataModel(DataModel model) {
        mainModel = (MainModel) model;
    }

    public DataModel getDataModel() {
        return mainModel;
    }
}
