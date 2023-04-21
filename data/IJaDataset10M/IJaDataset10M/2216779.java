package org.mitre.rt.client.ui.tables.comparators;

import org.mitre.rt.client.ui.cchecks.complex.ComplexCCheckListTableModel;
import org.mitre.rt.client.ui.tables.ComplexComplianceCheckTableItem;

/**
 *
 * @author JWINSTON
 */
public class ComplexComplianceCheckTableItemComparator extends AbsTableItemComparator<ComplexComplianceCheckTableItem> {

    private final int field;

    public ComplexComplianceCheckTableItemComparator(final int field) {
        this.field = field;
    }

    @Override
    public int compareItems(ComplexComplianceCheckTableItem o1, ComplexComplianceCheckTableItem o2) {
        int rtnval = 0;
        switch(this.field) {
            case ComplexCCheckListTableModel.TITLE:
                rtnval = o1.title.toLowerCase().compareTo(o2.title.toLowerCase());
                break;
            case ComplexCCheckListTableModel.MODIFIED_BY:
                rtnval = o1.modifiedBy.compareTo(o2.modifiedBy);
                break;
            case ComplexCCheckListTableModel.LAST_MODIFIED:
                rtnval = o1.modifiedDate.compareTo(o2.modifiedDate);
                break;
            case ComplexCCheckListTableModel.STATUS:
                rtnval = o1.status.compareTo(o2.status);
                break;
            case ComplexCCheckListTableModel.RULE:
                rtnval = super.compareIds(o1.ruleId, o2.ruleId);
                break;
            case ComplexCCheckListTableModel.COMPLEXCHECK:
                rtnval = o1.complexCheckTitle.compareTo(o2.complexCheckTitle);
                break;
            default:
                rtnval = 0;
        }
        return rtnval;
    }
}
