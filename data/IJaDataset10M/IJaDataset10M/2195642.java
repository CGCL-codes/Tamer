package net.cygeek.tech.client.ui.grid;

import net.cygeek.tech.client.ui.grid.AbstractGrid;
import net.cygeek.tech.client.ui.form.AbstractWindow;
import net.cygeek.tech.client.data.EmprepUsergroup;
import net.cygeek.tech.client.adapters.EmprepUsergroupAdapter;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.MessageBox;

/**
 * Author: Thilina Hasantha
 */
public class EmprepUsergroupGrid extends AbstractGrid {

    EmprepUsergroupAdapter adp = EmprepUsergroupAdapter.getInstance();

    static RecordDef recordDef = new RecordDef(new FieldDef[] {});

    static ColumnConfig[] columns = new ColumnConfig[] {};

    public EmprepUsergroupGrid() {
        super(recordDef, columns, "EmprepUsergroups", "");
    }

    public Object[][] getData() {
        Object[][] obj = new Object[data.size()][EmprepUsergroup.gridFieldCount];
        for (int i = 0; i < data.size(); i++) {
            obj[i] = ((EmprepUsergroup) data.get(i)).getGridFieldsX();
        }
        return obj;
    }

    public void add() {
        window.show(AbstractWindow.ADD);
    }

    public void edit(Record record) {
        if (record == null) {
            MessageBox.alert("EmprepUsergroup is not selected");
            return;
        }
        window.show(AbstractWindow.EDIT);
        window.getForm().setData(record);
    }

    public void view(Record record) {
        if (record == null) {
            MessageBox.alert("EmprepUsergroup is not selected");
            return;
        }
        window.show(AbstractWindow.VIEW);
        window.getForm().setData(record);
    }

    public void delete(Record record) {
        if (record == null) {
            MessageBox.alert("EmprepUsergroup is not selected");
            return;
        }
        adp.deleteEmprepUsergroup(this, record.getAsString("repCode"), record.getAsString("usergId"));
    }

    public void refresh() {
        adp.getEmprepUsergroups(this);
    }
}
