package net.cygeek.tech.client.ui.form;

import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.MessageBox;
import net.cygeek.tech.client.Unique;
import net.cygeek.tech.client.ui.grid.AbstractGrid;

/**
 * Created by IntelliJ IDEA.
 * User: thilinah
 * Date: Oct 4, 2008
 * Time: 9:56:34 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractForm extends FormPanel {

    AbstractGrid grid;

    protected boolean isNew = false;

    int id = -1;

    public AbstractForm() {
    }

    public int getPriId() {
        return id;
    }

    public void setPriId(int id) {
        this.id = id;
    }

    public abstract void reinitialize(int mode);

    public abstract void save();

    public abstract void cancel();

    public abstract void setData(Record record);

    public AbstractGrid getGrid() {
        return grid;
    }

    public void setGrid(AbstractGrid grid) {
        this.grid = grid;
    }

    public Object getItemByID(String id) {
        for (int i = 0; i < grid.data.size(); i++) {
            Unique u = (Unique) grid.data.get(i);
            if (u.getUniqueID().equals(id)) {
                return grid.data.get(i);
            }
        }
        return null;
    }
}
