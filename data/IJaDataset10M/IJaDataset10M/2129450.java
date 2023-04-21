package com.dcivision.calendar.web;

import com.dcivision.framework.web.AbstractSearchForm;

/**
  ListUserRoleForm.java

  The ActionForm for ListUserRole

    @author          Phoebe Wong
    @company         DCIVision Limited
    @creation date   24/07/2003
    @version         $Revision: 1.4 $
*/
public class ListCalendarRecordForm extends AbstractSearchForm {

    public static final String REVISION = "$Revision: 1.4 $";

    protected int ownerID = 0;

    public ListCalendarRecordForm() {
        super();
        this.setSortAttribute("datetime");
        this.setSortOrder("ASC");
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }
}
