package org.objectstyle.cayenne.modeler.editor.datanode;

import java.awt.Component;
import org.objectstyle.cayenne.modeler.ProjectController;
import org.objectstyle.cayenne.swing.BindingBuilder;
import org.objectstyle.cayenne.swing.BindingDelegate;
import org.objectstyle.cayenne.swing.ObjectBinding;

/**
 * @author Andrei Adamchik
 */
public class DBCPDataSourceEditor extends DataSourceEditor {

    protected DBCPDataSourceView view;

    public DBCPDataSourceEditor(ProjectController controller, BindingDelegate nodeChangeProcessor) {
        super(controller, nodeChangeProcessor);
    }

    protected void prepareBindings(BindingBuilder builder) {
        this.view = new DBCPDataSourceView();
        fieldAdapters = new ObjectBinding[1];
        fieldAdapters[0] = builder.bindToTextField(view.getPropertiesFile(), "node.dataSourceLocation");
    }

    public Component getView() {
        return view;
    }
}
