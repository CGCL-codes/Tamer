package org.usca.workshift.gwt.workshiftapp.client.workshiftmanagement;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.GWT;
import org.usca.workshift.database.model.House;
import org.usca.workshift.database.model.Workshift;
import org.usca.workshift.gwt.util.client.DisplayList;
import org.usca.workshift.gwt.util.client.NameChangeListener;
import org.usca.workshift.gwt.workshiftapp.client.WorkshiftApplication;
import java.util.List;

public class ListWorkshift extends DisplayList<Workshift, WorkshiftCategoryEnum> {

    House house;

    public ListWorkshift(House h) {
        house = h;
        populate();
    }

    public void addNew() {
        Workshift ws = new Workshift();
        ws.setHouse(house);
        DisplayWokshift dwc = new DisplayWokshift(this, ws, new NameChangeListener(getSize()) {

            public void delete() {
                remove(getIndex());
            }

            public void nameChange(String newName) {
            }
        });
        dwc.addStyleName("workshift-Element");
        dwc.setVisible(true);
        add(dwc, ((WorkshiftApplication) GWT.create(WorkshiftApplication.class)).workshiftcUnnamed());
    }

    public String getNewButtonText() {
        return ((WorkshiftApplication) GWT.create(WorkshiftApplication.class)).workshiftCreate();
    }

    public void getList() {
        GetWorkshiftInfo.App.getInstance().getWorkshift(house, new AsyncCallback<List<Workshift>>() {

            public void onFailure(Throwable caught) {
                clear();
            }

            public void onSuccess(List<Workshift> result) {
                clear();
                for (Workshift ws : result) {
                    DisplayWokshift dwc = new DisplayWokshift(ListWorkshift.this, ws, new NameChangeListener(getSize()) {

                        public void delete() {
                            remove(getIndex());
                        }

                        public void nameChange(String newName) {
                        }
                    });
                    add(dwc, ws.getTitle());
                }
            }
        });
    }
}
