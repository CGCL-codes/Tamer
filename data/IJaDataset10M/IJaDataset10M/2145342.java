package net.cygeek.tech.client.ui.grid;

import net.cygeek.tech.client.ui.grid.AbstractGrid;
import net.cygeek.tech.client.ui.form.AbstractWindow;
import net.cygeek.tech.client.data.ProjectActivity;
import net.cygeek.tech.client.adapters.ProjectActivityAdapter;
import net.cygeek.tech.client.adapters.CustomerAdapter;
import net.cygeek.tech.client.util.TS;
import net.cygeek.tech.client.util.MessageStore;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.MessageBox;

/**
 * Author: Thilina Hasantha
 */
public class ProjectActivityGrid extends AbstractGrid {

    ProjectActivityAdapter adp = ProjectActivityAdapter.getInstance();

    static RecordDef recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("deleted"), new StringFieldDef("name"), new StringFieldDef("projectId"), new StringFieldDef("activityId"), new StringFieldDef("projectName") });

    static ColumnConfig[] columns = new ColumnConfig[] { new ColumnConfig(TS.gi().get("L0235"), "activityId", 100), new ColumnConfig(TS.gi().get("L0236"), "name", 200), new ColumnConfig(TS.gi().get("L0232"), "projectName", 200) };

    public ProjectActivityGrid() {
        super(recordDef, columns, "Tasks", "");
    }

    public Object[][] getData() {
        Object[][] obj = new Object[data.size()][ProjectActivity.gridFieldCount];
        for (int i = 0; i < data.size(); i++) {
            obj[i] = ((ProjectActivity) data.get(i)).getGridFieldsX();
        }
        return obj;
    }

    public void add() {
        window.show(AbstractWindow.ADD);
    }

    public void edit(Record record) {
        if (record == null) {
            MessageStore.showNotSelectedError("L0236");
            return;
        }
        window.show(AbstractWindow.EDIT);
        window.getForm().setData(record);
    }

    public void view(Record record) {
        if (record == null) {
            MessageStore.showNotSelectedError("L0236");
            return;
        }
        window.show(AbstractWindow.VIEW);
        window.getForm().setData(record);
    }

    public void delete(Record record) {
        if (record == null) {
            MessageStore.showNotSelectedError("L0236");
            return;
        }
        MessageStore.showDeleteConfirm(new DeletePCB(this, adp, record.getAsInteger("activityId")), TS.gi().get("L0236"), record.getAsString("name"));
    }

    public void refresh() {
        adp.getProjectActivitys(this);
    }

    class DeletePCB implements MessageBox.PromptCallback {

        AbstractGrid grid;

        ProjectActivityAdapter adp;

        int tid;

        DeletePCB(AbstractGrid grid, ProjectActivityAdapter adp, int tid) {
            this.grid = grid;
            this.adp = adp;
            this.tid = tid;
        }

        public void execute(String s, String s1) {
            if (s.equals("yes")) {
                adp.deleteProjectActivity(grid, tid);
            }
        }
    }
}
