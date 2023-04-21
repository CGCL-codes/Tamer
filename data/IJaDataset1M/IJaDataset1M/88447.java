package net.cygeek.tech.client.adapters;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import net.cygeek.tech.client.service.*;
import net.cygeek.tech.client.ui.grid.AbstractGrid;
import net.cygeek.tech.client.data.Rights;
import java.util.ArrayList;
import java.util.Date;

/**
 * Author: Thilina Hasantha
 */
public class RightsAdapter {

    private static RightsAdapter me = null;

    RightsServiceAsync ms = (RightsServiceAsync) GWT.create(RightsService.class);

    ServiceDefTarget endpoint = (ServiceDefTarget) ms;

    String moduleRelativeURL = GWT.getModuleBaseURL() + "Rights";

    public static RightsAdapter getInstance() {
        if (me == null) me = new RightsAdapter();
        return me;
    }

    private RightsAdapter() {
        endpoint.setServiceEntryPoint(moduleRelativeURL);
    }

    public void addRights(Rights mRights, AbstractGrid grid, boolean isNew) {
        ms.addRights(mRights, isNew, new AddRightsCallBack(grid, mRights, isNew));
    }

    public void getRightss(AbstractGrid grid) {
        ms.getRightss(new GetRightssCallBack(grid));
    }

    public void deleteRights(AbstractGrid grid, String modId, String usergId) {
        ms.deleteRights(modId, usergId, new DeleteRightsCallBack(grid, modId, usergId));
    }

    class GetRightssCallBack implements AsyncCallback {

        AbstractGrid grid;

        GetRightssCallBack(AbstractGrid grid) {
            this.grid = grid;
        }

        public void onFailure(Throwable throwable) {
            MessageBox.alert("Error reading Rightss");
        }

        public void onSuccess(Object o) {
            if (o != null) {
                grid.data = (ArrayList) o;
                grid.updateGrid();
            } else {
                MessageBox.alert("Error reading Rightss");
            }
        }
    }

    class AddRightsCallBack implements AsyncCallback {

        AbstractGrid grid;

        Rights mRights;

        boolean isNew = false;

        AddRightsCallBack(AbstractGrid grid, Rights mRights, boolean isNew) {
            this.grid = grid;
            this.mRights = mRights;
            this.isNew = isNew;
        }

        public void onFailure(Throwable throwable) {
            MessageBox.alert("Error saving Rights {Failed}");
        }

        public void onSuccess(Object o) {
            String val = String.valueOf(o);
            if (val.equals("true")) {
                if (isNew) {
                    grid.data.add(mRights);
                } else {
                    Object temp = grid.window.getForm().getItemByID(String.valueOf(mRights.getUniqueID()));
                    int i = grid.data.indexOf(temp);
                    grid.data.remove(i);
                    grid.data.add(i, mRights);
                }
                grid.updateGrid();
            } else {
                MessageBox.alert("Error saving Rights");
            }
        }
    }

    class DeleteRightsCallBack implements AsyncCallback {

        AbstractGrid grid;

        String modId;

        String usergId;

        DeleteRightsCallBack(AbstractGrid grid, String modId, String usergId) {
            this.grid = grid;
            this.modId = modId;
            this.usergId = usergId;
        }

        public void onFailure(Throwable throwable) {
            MessageBox.alert("Error deleting Rights");
        }

        public void onSuccess(Object o) {
            String val = String.valueOf(o);
            if (val.equals("true")) {
                Object temp = grid.window.getForm().getItemByID(String.valueOf(modId) + "#" + String.valueOf(usergId));
                grid.data.remove(temp);
                grid.updateGrid();
            } else {
                MessageBox.alert("Error deleting Rights");
            }
        }
    }
}
