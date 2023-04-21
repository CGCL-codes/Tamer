package org.objectstyle.cayenne.modeler.dialog.pref;

import java.awt.Component;
import java.util.Map;
import javax.swing.JOptionPane;
import org.objectstyle.cayenne.modeler.pref.DBConnectionInfo;
import org.objectstyle.cayenne.modeler.util.CayenneController;
import org.objectstyle.cayenne.pref.Domain;
import org.objectstyle.cayenne.pref.PreferenceEditor;
import org.objectstyle.cayenne.swing.BindingBuilder;

/**
 * @author Andrei Adamchik
 */
public class DataSourceDuplicator extends CayenneController {

    protected DataSourceDuplicatorView view;

    protected PreferenceEditor editor;

    protected Domain domain;

    protected boolean canceled;

    protected Map dataSources;

    protected String prototypeKey;

    public DataSourceDuplicator(DataSourcePreferences parent, String prototypeKey) {
        super(parent);
        this.view = new DataSourceDuplicatorView("Create a copy of \"" + prototypeKey + "\"");
        this.editor = parent.getEditor();
        this.domain = parent.getDataSourceDomain();
        this.dataSources = parent.getDataSources();
        this.prototypeKey = prototypeKey;
        String suggestion = prototypeKey + "0";
        for (int i = 1; i <= dataSources.size(); i++) {
            suggestion = prototypeKey + i;
            if (!dataSources.containsKey(suggestion)) {
                break;
            }
        }
        this.view.getDataSourceName().setText(suggestion);
        initBindings();
    }

    public Component getView() {
        return view;
    }

    protected void initBindings() {
        BindingBuilder builder = new BindingBuilder(getApplication().getBindingFactory(), this);
        builder.bindToAction(view.getCancelButton(), "cancelAction()");
        builder.bindToAction(view.getOkButton(), "okAction()");
    }

    public void okAction() {
        if (getName() == null) {
            JOptionPane.showMessageDialog(view, "Enter DataSource Name", null, JOptionPane.WARNING_MESSAGE);
        } else if (dataSources.containsKey(getName())) {
            JOptionPane.showMessageDialog(view, "'" + getName() + "' is already in use, enter a different name", null, JOptionPane.WARNING_MESSAGE);
        } else {
            canceled = false;
            view.dispose();
        }
    }

    public void cancelAction() {
        canceled = true;
        view.dispose();
    }

    /**
     * Pops up a dialog and blocks current thread until the dialog is closed.
     */
    public DBConnectionInfo startupAction() {
        canceled = true;
        view.setModal(true);
        view.pack();
        view.setResizable(false);
        makeCloseableOnEscape();
        centerView();
        view.setVisible(true);
        return createDataSource();
    }

    public String getName() {
        String name = view.getDataSourceName().getText();
        return (name.length() > 0) ? name : null;
    }

    protected DBConnectionInfo createDataSource() {
        if (canceled) {
            return null;
        }
        DBConnectionInfo prototype = (DBConnectionInfo) dataSources.get(prototypeKey);
        DBConnectionInfo dataSource = (DBConnectionInfo) editor.createDetail(domain, getName(), DBConnectionInfo.class);
        prototype.copyTo(dataSource);
        return dataSource;
    }
}
