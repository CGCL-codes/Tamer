package org.plazmaforge.bsolution.project.client.swing.forms;

import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import org.plazmaforge.bsolution.project.client.swing.GUIProjectEnvironment;
import org.plazmaforge.bsolution.project.common.beans.ProjectType;
import org.plazmaforge.bsolution.project.common.services.ProjectTypeService;
import org.plazmaforge.framework.client.swing.forms.EXTListForm;
import org.plazmaforge.framework.client.swing.gui.table.ColumnProperty;
import org.plazmaforge.framework.core.exception.ApplicationException;

public class ProjectTypeList extends EXTListForm {

    public ProjectTypeList() throws ApplicationException {
        super(GUIProjectEnvironment.getResources());
        initialize();
    }

    public ProjectTypeList(Window window) throws ApplicationException {
        super(window, GUIProjectEnvironment.getResources());
        initialize();
    }

    private void initialize() {
        this.setEntityClass(ProjectType.class);
        this.setEntityServiceClass(ProjectTypeService.class);
        this.setEntityEditFormClass(ProjectTypeEdit.class);
    }

    protected void initComponents() throws ApplicationException {
        super.initComponents();
        setTitle(getString("title"));
    }

    protected void initShell() throws ApplicationException {
        super.initShell();
        getShell().setSize(900, DEFAULT_HEIGHT);
    }

    protected List<ColumnProperty> createTableColumnProperties() throws ApplicationException {
        List<ColumnProperty> columns = new ArrayList<ColumnProperty>();
        ColumnProperty d = new ColumnProperty();
        d.setName(getString("table.column-name.name"));
        d.setFieldName("name");
        d.setColumnClass(String.class);
        d.setSize(60);
        columns.add(d);
        d = new ColumnProperty();
        d.setName(getString("table.column-note.name"));
        d.setFieldName("note");
        d.setColumnClass(String.class);
        d.setSize(60);
        columns.add(d);
        d = new ColumnProperty();
        d.setName(getString("table.column-code.name"));
        d.setFieldName("code");
        d.setColumnClass(String.class);
        d.setSize(20);
        columns.add(d);
        return columns;
    }
}
