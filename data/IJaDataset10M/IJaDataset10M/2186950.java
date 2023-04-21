package com.dcivision.form.web;

import com.dcivision.framework.web.AbstractSearchForm;

/**
  ListFormGroupRecordForm.java

  The ActionForm for ListFormGroupRecordForm

    @author          Tony Chen
    @company         DCIVision Limited
    @creation date   19/02/2003
    @version         $Revision: 1.4 $
*/
public class ListFormGroupRecordForm extends AbstractSearchForm {

    public static final String REVISION = "$Revision: 1.4 $";

    private String listType;

    /** Creates a new instance of ListFormGroupRecordForm */
    public ListFormGroupRecordForm() {
        super();
        this.setSortAttribute("GROUP_NAME");
        this.setSortOrder("ASC");
    }

    public String getListType() {
        return (this.listType);
    }

    public void setListType(String listType) {
        this.listType = listType;
    }
}
