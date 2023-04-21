package com.teg.tobe.db.dataset;

import java.io.Serializable;
import com.teg.tobe.db.DataRow;
import com.teg.tobe.db.command.Command;

public class DefaultDatasetListener implements DatasetListener, Serializable {

    private static final long serialVersionUID = 1L;

    public void afterAddDataRow(Dataset dataset, DataRow dataRow) throws Exception {
    }

    public void afterClear(Dataset dataset) throws Exception {
    }

    public void afterRemoveDataRow(Dataset dataset, DataRow dataRow) throws Exception {
    }

    public void beforeAddDataRow(Dataset dataset, DataRow dataRow) throws Exception {
    }

    public void beforeClear(Dataset dataset) throws Exception {
    }

    public void beforeRemoveDataRow(Dataset dataset, DataRow dataRow) throws Exception {
    }

    public void beforeFill(Dataset dataset, Command dataRow) throws Exception {
    }

    public void afterFill(Dataset dataset, Command dataRow) throws Exception {
    }

    public void beforeUndoChanges(Dataset dataset) throws Exception {
    }

    public void afterUndoChanges(Dataset dataset) throws Exception {
    }

    public void afterSave(Dataset dataset) throws Exception {
    }

    public void beforeSave(Dataset dataset) throws Exception {
    }

    public void dataChanged(Dataset dataset) throws Exception {
    }
}
