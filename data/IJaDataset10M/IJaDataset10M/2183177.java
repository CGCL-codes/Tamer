package org.isqlviewer.ui;

import java.awt.Cursor;
import javax.swing.JTree;
import org.isqlviewer.model.JdbcSchemaTreeModel;
import org.isqlviewer.swing.Refreshable;

public class JdbcSchemaTreeRefreshable implements Refreshable {

    private JdbcSchemaTreeModel schemaModel = null;

    private JTree componentView = null;

    public JdbcSchemaTreeRefreshable(JdbcSchemaTreeModel schemaModel, JTree schemaView) {
        this.componentView = schemaView;
        this.schemaModel = schemaModel;
    }

    public void refreshView() {
        componentView.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            synchronized (schemaModel) {
                schemaModel.reload();
            }
            componentView.invalidate();
        } finally {
            componentView.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
}
