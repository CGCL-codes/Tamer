package com.dcivision.setup.web;

import com.dcivision.framework.web.AbstractSearchForm;

/**
  ListFunctionAccessForm.java

  The ActionForm for ListFunctionAccess

    @author          Phoebe Wong
    @company         DCIVision Limited
    @creation date   07/08/2003
    @version         $Revision: 1.2 $
*/
public class ListFunctionAccessForm extends AbstractSearchForm {

    public static final String REVISION = "$Revision: 1.2 $";

    public ListFunctionAccessForm() {
        super();
        this.setSortAttribute("FUNCTION_CODE");
        this.setSortOrder("ASC");
    }
}
