package net.cygeek.tech.client.ui.grid;

import net.cygeek.tech.client.ui.grid.AbstractGrid;
import net.cygeek.tech.client.ui.form.AbstractWindow;
import net.cygeek.tech.client.data.EmpLanguage;
import net.cygeek.tech.client.adapters.EmpLanguageAdapter;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.MessageBox;

/**
 * Author: Thilina Hasantha
 */
public class EmpLanguageGrid extends AbstractGrid {

    EmpLanguageAdapter adp = EmpLanguageAdapter.getInstance();

    static RecordDef recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("competency"), new StringFieldDef("elangType") });

    static ColumnConfig[] columns = new ColumnConfig[] { new ColumnConfig("Competency", "competency", 100), new ColumnConfig("ElangType", "elangType", 100) };

    public EmpLanguageGrid() {
        super(recordDef, columns, "EmpLanguages", "");
    }

    public Object[][] getData() {
        Object[][] obj = new Object[data.size()][EmpLanguage.gridFieldCount];
        for (int i = 0; i < data.size(); i++) {
            obj[i] = ((EmpLanguage) data.get(i)).getGridFieldsX();
        }
        return obj;
    }

    public void add() {
        window.show(AbstractWindow.ADD);
    }

    public void edit(Record record) {
        if (record == null) {
            MessageBox.alert("EmpLanguage is not selected");
            return;
        }
        window.show(AbstractWindow.EDIT);
        window.getForm().setData(record);
    }

    public void view(Record record) {
        if (record == null) {
            MessageBox.alert("EmpLanguage is not selected");
            return;
        }
        window.show(AbstractWindow.VIEW);
        window.getForm().setData(record);
    }

    public void delete(Record record) {
        if (record == null) {
            MessageBox.alert("EmpLanguage is not selected");
            return;
        }
        adp.deleteEmpLanguage(this, record.getAsString("langCode"), (short) record.getAsInteger("elangType"), record.getAsInteger("empNumber"));
    }

    public void refresh() {
        adp.getEmpLanguages(this);
    }
}
