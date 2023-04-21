package com.loribel.commons.gui.bo.table;

import java.util.List;
import com.loribel.commons.abstraction.GB_TableModelSimple;
import com.loribel.commons.business.GB_BOTools;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.commons.swing.GB_Table;
import com.loribel.commons.swing.table.GB_TableModelFilter;

/**
 * Default Table to represent a list of businessObject.
 */
public class GB_BOTableDefault extends GB_Table {

    /**
     * The list of BusinessObject to represent.
     */
    protected List bOList;

    /**
     * The BOName of the objets that this table will represent.
     */
    protected String bOName;

    /**
     * The list of property names to used.
     */
    protected String[] bOPropertyNames;

    private GB_TableModelSimple myModel;

    /**
     * Constructor of GB_BOTable with parameter(s).
     *
     * @param a_bOName String - the BOName of the objets that this table will represent
     * @param a_bOList java.util.List - the list of BusinessObject to represent
     * @param a_bOPropertyNames String[] - the list of property names to used
     */
    public GB_BOTableDefault(String a_bOName, List a_bOList, String[] a_bOPropertyNames) {
        super();
        bOName = a_bOName;
        bOList = a_bOList;
        bOPropertyNames = a_bOPropertyNames;
        init();
    }

    /**
     * Get The list of BusinessObject to represent..
     *
     * @return java.util.List - <tt>bOList</tt>
     */
    public List getBOList() {
        return bOList;
    }

    /**
     * @return Returns the myModel.
     */
    public GB_TableModelSimple getMyModel() {
        return myModel;
    }

    public GB_SimpleBusinessObject getSelectedBO() {
        int l_index = this.getSelectedRow();
        return (GB_SimpleBusinessObject) myModel.getItems().get(l_index);
    }

    /**
     * Method init.
     */
    private void init() {
        if (bOPropertyNames == null) {
            bOPropertyNames = GB_BOTools.getPropertyNamesSimple(bOName);
        }
        GB_BORowDecorator l_rowDecorator = new GB_BORowDecorator(bOName, bOPropertyNames);
        myModel = new GB_TableModelFilter(bOList, l_rowDecorator);
        this.setModel(myModel);
    }
}
