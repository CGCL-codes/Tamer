package marubinotto.piggydb.presentation.model;

import marubinotto.piggydb.model.Filter;

public class FilterFormModel extends Filter {

    private boolean dirty = false;

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}
