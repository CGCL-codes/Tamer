package org.endeavour.mgmt.view.model;

import java.util.List;
import org.endeavour.mgmt.model.TestPlan;
import org.endeavour.mgmt.view.IViewConstants;
import org.endeavour.mgmt.view.components.GridBoxModel;

public class TestPlansListModel extends GridBoxModel {

    public TestPlansListModel() {
        super();
    }

    public TestPlansListModel(List<TestPlan> aTestPlans) {
        super(aTestPlans);
    }

    public void initializeColumns() {
        super.columns = new String[] { IViewConstants.RB.getString("test_plan_name.lbl") };
    }

    public Object getValueAt(int aRowIndex, int aColumnIndex) {
        String theValue = null;
        TestPlan theTestPlan = (TestPlan) this.rows.get(aRowIndex);
        switch(aColumnIndex) {
            case 0:
                theValue = theTestPlan.getName();
                break;
            default:
                theValue = "";
        }
        return theValue;
    }

    public int getRowId(int aRowIndex) {
        TestPlan theTestPlan = (TestPlan) this.rows.get(aRowIndex);
        return theTestPlan.getId();
    }
}
