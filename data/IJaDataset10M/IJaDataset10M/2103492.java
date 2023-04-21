package com.dcivision.framework.web;

/**
  ListSysUserDefinedIndexForm.java

  The ActionForm for ListSysUserDefinedIndex

    @author          Phoebe Wong
    @company         DCIVision Limited
    @creation date   01/08/2003
    @version         $Revision: 1.5 $
*/
public class ListSysUserDefinedIndexForm extends AbstractSearchForm {

    public static final String REVISION = "$Revision: 1.5 $";

    private boolean useSysDefinedIndex = true;

    public ListSysUserDefinedIndexForm() {
        super();
        this.setSortAttribute("USER_DEFINED_TYPE");
        this.setSortOrder("ASC");
    }

    public boolean getUseSysDefinedIndex() {
        return this.useSysDefinedIndex;
    }

    public void setUseSysDefinedIndex(boolean useSysDefinedIndex) {
        this.useSysDefinedIndex = useSysDefinedIndex;
    }
}
