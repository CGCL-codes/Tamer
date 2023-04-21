package net.cygeek.tech.client.ui.grid;

import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.grid.ColumnConfig;
import net.cygeek.tech.client.adapters.EmpreportAdapter;
import net.cygeek.tech.client.data.Empreport;
import net.cygeek.tech.client.ui.form.AbstractWindow;

/**
 * Author: Thilina Hasantha
 */
public class EmpreportGrid extends AbstractGrid {

    EmpreportAdapter adp = EmpreportAdapter.getInstance();

    static RecordDef recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("repName"), new StringFieldDef("repFlddefStr"), new StringFieldDef("repCode"), new StringFieldDef("repCridefStr") });

    static ColumnConfig[] columns = new ColumnConfig[] { new ColumnConfig("RepName", "repName", 100), new ColumnConfig("RepFlddefStr", "repFlddefStr", 100), new ColumnConfig("RepCode", "repCode", 100), new ColumnConfig("RepCridefStr", "repCridefStr", 100) };

    public EmpreportGrid() {
        super(recordDef, columns, "Empreports", "");
    }

    public Object[][] getData() {
        Object[][] obj = new Object[data.size()][Empreport.gridFieldCount];
        for (int i = 0; i < data.size(); i++) {
            obj[i] = ((Empreport) data.get(i)).getGridFieldsX();
        }
        return obj;
    }

    public void add() {
        window.show(AbstractWindow.ADD);
    }

    public void edit(Record record) {
        if (record == null) {
            MessageBox.alert("Empreport is not selected");
            return;
        }
        window.show(AbstractWindow.EDIT);
        window.getForm().setData(record);
    }

    public void view(Record record) {
        if (record == null) {
            MessageBox.alert("Empreport is not selected");
            return;
        }
        window.show(AbstractWindow.VIEW);
        window.getForm().setData(record);
    }

    public void delete(Record record) {
        if (record == null) {
            MessageBox.alert("Empreport is not selected");
            return;
        }
        adp.deleteEmpreport(this, record.getAsString("repCode"));
    }

    public void refresh() {
        adp.getEmpreports(this);
    }
}
