package net.sf.JRecord.Details.Selection;

import java.util.List;
import net.sf.JRecord.ExternalRecordSelection.ExternalGroupSelection;

public abstract class AbsGroup extends ExternalGroupSelection<RecordSel> implements RecordSel {

    public AbsGroup(int size) {
        super(size);
    }

    @Override
    public FieldSelect getFirstField() {
        FieldSelect s = null;
        for (int i = 0; i < getSize() && s == null; i++) {
            s = get(i).getFirstField();
        }
        return s;
    }

    @Override
    public void getAllFields(List<FieldSelect> fields) {
        for (int i = 0; i < getSize(); i++) {
            get(i).getAllFields(fields);
        }
    }
}
