package net.cygeek.tech.client.adapters;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import net.cygeek.tech.client.service.*;
import net.cygeek.tech.client.ui.grid.AbstractGrid;
import net.cygeek.tech.client.data.EmpUsTax;
import java.util.ArrayList;
import java.util.Date;

/**
 * Author: Thilina Hasantha
 */
public class EmpUsTaxAdapter {

    private static EmpUsTaxAdapter me = null;

    EmpUsTaxServiceAsync ms = (EmpUsTaxServiceAsync) GWT.create(EmpUsTaxService.class);

    ServiceDefTarget endpoint = (ServiceDefTarget) ms;

    String moduleRelativeURL = GWT.getModuleBaseURL() + "EmpUsTax";

    public static EmpUsTaxAdapter getInstance() {
        if (me == null) me = new EmpUsTaxAdapter();
        return me;
    }

    private EmpUsTaxAdapter() {
        endpoint.setServiceEntryPoint(moduleRelativeURL);
    }

    public void addEmpUsTax(EmpUsTax mEmpUsTax, AbstractGrid grid, boolean isNew) {
        ms.addEmpUsTax(mEmpUsTax, isNew, new AddEmpUsTaxCallBack(grid, mEmpUsTax, isNew));
    }

    public void getEmpUsTaxs(AbstractGrid grid) {
        ms.getEmpUsTaxs(new GetEmpUsTaxsCallBack(grid));
    }

    public void deleteEmpUsTax(AbstractGrid grid, int empNumber) {
        ms.deleteEmpUsTax(empNumber, new DeleteEmpUsTaxCallBack(grid, empNumber));
    }

    class GetEmpUsTaxsCallBack implements AsyncCallback {

        AbstractGrid grid;

        GetEmpUsTaxsCallBack(AbstractGrid grid) {
            this.grid = grid;
        }

        public void onFailure(Throwable throwable) {
            MessageBox.alert("Error reading EmpUsTaxs");
        }

        public void onSuccess(Object o) {
            if (o != null) {
                grid.data = (ArrayList) o;
                grid.updateGrid();
            } else {
                MessageBox.alert("Error reading EmpUsTaxs");
            }
        }
    }

    class AddEmpUsTaxCallBack implements AsyncCallback {

        AbstractGrid grid;

        EmpUsTax mEmpUsTax;

        boolean isNew = false;

        AddEmpUsTaxCallBack(AbstractGrid grid, EmpUsTax mEmpUsTax, boolean isNew) {
            this.grid = grid;
            this.mEmpUsTax = mEmpUsTax;
            this.isNew = isNew;
        }

        public void onFailure(Throwable throwable) {
            MessageBox.alert("Error saving EmpUsTax {Failed}");
        }

        public void onSuccess(Object o) {
            String val = String.valueOf(o);
            if (val.equals("true")) {
                if (isNew) {
                    grid.data.add(mEmpUsTax);
                } else {
                    Object temp = grid.window.getForm().getItemByID(String.valueOf(mEmpUsTax.getUniqueID()));
                    int i = grid.data.indexOf(temp);
                    grid.data.remove(i);
                    grid.data.add(i, mEmpUsTax);
                }
                grid.updateGrid();
            } else {
                MessageBox.alert("Error saving EmpUsTax");
            }
        }
    }

    class DeleteEmpUsTaxCallBack implements AsyncCallback {

        AbstractGrid grid;

        int empNumber;

        DeleteEmpUsTaxCallBack(AbstractGrid grid, int empNumber) {
            this.grid = grid;
            this.empNumber = empNumber;
        }

        public void onFailure(Throwable throwable) {
            MessageBox.alert("Error deleting EmpUsTax");
        }

        public void onSuccess(Object o) {
            String val = String.valueOf(o);
            if (val.equals("true")) {
                Object temp = grid.window.getForm().getItemByID(String.valueOf(empNumber));
                grid.data.remove(temp);
                grid.updateGrid();
            } else {
                MessageBox.alert("Error deleting EmpUsTax");
            }
        }
    }
}
