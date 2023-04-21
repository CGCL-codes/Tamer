package net.cygeek.tech.client.ui.grid;

import net.cygeek.tech.client.ui.grid.AbstractGrid;
import net.cygeek.tech.client.ui.form.AbstractWindow;
import net.cygeek.tech.client.data.EmpBasicsalary;
import net.cygeek.tech.client.adapters.EmpBasicsalaryAdapter;
import net.cygeek.tech.client.util.TS;
import net.cygeek.tech.client.util.MessageStore;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.core.TextAlign;

/**
 * Author: Thilina Hasantha
 */
public class EmpBasicsalaryGrid extends AbstractGrid {

    EmpBasicsalaryAdapter adp = EmpBasicsalaryAdapter.getInstance();

    static RecordDef recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("salGrdCode"), new StringFieldDef("salGrdName"), new StringFieldDef("payperiodCode"), new StringFieldDef("currencyId"), new StringFieldDef("currencyName"), new StringFieldDef("ebsalBasicSalary"), new StringFieldDef("empNumber") });

    static ColumnConfig[] columns = new ColumnConfig[] { new ColumnConfig(TS.gi().get("L0271"), "salGrdName", 150), new ColumnConfig(TS.gi().get("L0272"), "payperiodCode", 150), new ColumnConfig(TS.gi().get("L0273"), "currencyName", 175), new ColumnConfig(TS.gi().get("L0274"), "ebsalBasicSalary", 120) };

    public EmpBasicsalaryGrid() {
        super(recordDef, columns, TS.gi().get("L0274"), "");
        columns[3].setAlign(TextAlign.RIGHT);
    }

    public Object[][] getData() {
        Object[][] obj = new Object[data.size()][EmpBasicsalary.gridFieldCount];
        for (int i = 0; i < data.size(); i++) {
            obj[i] = ((EmpBasicsalary) data.get(i)).getGridFieldsX();
        }
        return obj;
    }

    public void add() {
        window.show(AbstractWindow.ADD);
    }

    public void edit(Record record) {
        if (record == null) {
            MessageStore.showNotSelectedError("L0274");
            return;
        }
        window.show(AbstractWindow.EDIT);
        window.getForm().setData(record);
    }

    public void view(Record record) {
        if (record == null) {
            MessageStore.showNotSelectedError("L0274");
            return;
        }
        window.show(AbstractWindow.VIEW);
        window.getForm().setData(record);
    }

    public void delete(Record record) {
        if (record == null) {
            MessageStore.showNotSelectedError("L0274");
            return;
        }
        adp.deleteEmpBasicsalary(this, record.getAsString("currencyId"), record.getAsString("salGrdCode"), record.getAsInteger("empNumber"));
    }

    public void refresh() {
        adp.getEmpBasicsalarys(this, window.form.getPriId());
    }
}
