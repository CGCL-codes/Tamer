package org.jaffa.applications.test.modules.material.components.itemfinder.ui;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import org.apache.log4j.Logger;
import org.jaffa.metadata.*;
import org.jaffa.datatypes.*;
import org.jaffa.components.finder.*;
import org.jaffa.presentation.portlet.widgets.model.*;
import org.jaffa.presentation.portlet.widgets.controller.*;
import org.jaffa.util.StringHelper;
import org.jaffa.applications.test.modules.material.domain.ItemMeta;

/** The FormBean class to support the Criteria jsp of the ItemFinder.
 */
public class ItemFinderCriteriaForm extends FinderCriteriaForm {

    /** The name constant used for determining the corresponding jsp through the struts-config file.
     */
    public static final String NAME = "material_itemFinderCriteria";

    private static Logger log = Logger.getLogger(ItemFinderCriteriaForm.class);

    private EditBoxModel w_segregationCode = null;

    private DropDownModel w_segregationCodeDd = null;

    private EditBoxModel w_partNo = null;

    private DropDownModel w_partNoDd = null;

    private EditBoxModel w_serial = null;

    private DropDownModel w_serialDd = null;

    private EditBoxModel w_qty = null;

    private DropDownModel w_qtyDd = null;

    /** Getter for property segregationCode.
     * @return Value of property segregationCode.
     */
    public String getSegregationCode() {
        return ((ItemFinderComponent) getComponent()).getSegregationCode();
    }

    /** Setter for property segregationCode.
     * @param segregationCode New value of property segregationCode.
     */
    public void setSegregationCode(String segregationCode) {
        ((ItemFinderComponent) getComponent()).setSegregationCode(segregationCode);
    }

    /** Getter for property segregationCode. This is invoked by the custom tag, when the jsp is rendered, to get the current value.
     * @return Value of property segregationCode.
     */
    public EditBoxModel getSegregationCodeWM() {
        if (w_segregationCode == null) {
            w_segregationCode = (EditBoxModel) getWidgetCache().getModel("segregationCode");
            if (w_segregationCode == null) {
                if (getSegregationCode() != null) w_segregationCode = new EditBoxModel(getSegregationCode()); else w_segregationCode = new EditBoxModel();
                w_segregationCode.setStringCase(((StringFieldMetaData) ItemMeta.META_SC).getCaseType());
                getWidgetCache().addModel("segregationCode", w_segregationCode);
            }
        }
        return w_segregationCode;
    }

    /** Setter for property segregationCode. This is invoked by the servlet, when a post is done on the Criteria screen.
     * @param value New value of property segregationCode.
     */
    public void setSegregationCodeWV(String value) {
        EditBoxController.updateModel(value, getSegregationCodeWM());
    }

    /** Getter for DropDown property segregationCode.
     * @return Value of property segregationCodeDd.
     */
    public String getSegregationCodeDd() {
        return ((ItemFinderComponent) getComponent()).getSegregationCodeDd();
    }

    /** Setter for DropDown property segregationCode.
     * @param segregationCodeDd New value of property segregationCodeDd.
     */
    public void setSegregationCodeDd(String segregationCodeDd) {
        ((ItemFinderComponent) getComponent()).setSegregationCodeDd(segregationCodeDd);
    }

    /** Getter for DropDown property segregationCode. This is invoked by the custom tag, when the jsp is rendered, to get the current value.
     * @return Value of property segregationCodeDd.
     */
    public DropDownModel getSegregationCodeDdWM() {
        if (w_segregationCodeDd == null) {
            w_segregationCodeDd = (DropDownModel) getWidgetCache().getModel("segregationCodeDd");
            if (w_segregationCodeDd == null) {
                if (getSegregationCodeDd() != null) w_segregationCodeDd = new DropDownModel(getSegregationCodeDd()); else w_segregationCodeDd = new DropDownModel(CriteriaField.RELATIONAL_EQUALS);
                Map optionsMap = CriteriaDropDownOptions.getAllCriteriaDropDownOptions();
                Set options = optionsMap.entrySet();
                for (Iterator i = options.iterator(); i.hasNext(); ) {
                    Map.Entry option = (Map.Entry) i.next();
                    w_segregationCodeDd.addOption((String) option.getValue(), (String) option.getKey());
                }
                getWidgetCache().addModel("segregationCodeDd", w_segregationCodeDd);
            }
        }
        return w_segregationCodeDd;
    }

    /** Setter for DropDown property segregationCode. This is invoked by the servlet, when a post is done on the Criteria screen.
     * @param value New value of property segregationCodeDd.
     */
    public void setSegregationCodeDdWV(String value) {
        DropDownController.updateModel(value, getSegregationCodeDdWM());
    }

    /** Getter for property partNo.
     * @return Value of property partNo.
     */
    public String getPartNo() {
        return ((ItemFinderComponent) getComponent()).getPartNo();
    }

    /** Setter for property partNo.
     * @param partNo New value of property partNo.
     */
    public void setPartNo(String partNo) {
        ((ItemFinderComponent) getComponent()).setPartNo(partNo);
    }

    /** Getter for property partNo. This is invoked by the custom tag, when the jsp is rendered, to get the current value.
     * @return Value of property partNo.
     */
    public EditBoxModel getPartNoWM() {
        if (w_partNo == null) {
            w_partNo = (EditBoxModel) getWidgetCache().getModel("partNo");
            if (w_partNo == null) {
                if (getPartNo() != null) w_partNo = new EditBoxModel(getPartNo()); else w_partNo = new EditBoxModel();
                w_partNo.setStringCase(((StringFieldMetaData) ItemMeta.META_PART).getCaseType());
                getWidgetCache().addModel("partNo", w_partNo);
            }
        }
        return w_partNo;
    }

    /** Setter for property partNo. This is invoked by the servlet, when a post is done on the Criteria screen.
     * @param value New value of property partNo.
     */
    public void setPartNoWV(String value) {
        EditBoxController.updateModel(value, getPartNoWM());
    }

    /** Getter for DropDown property partNo.
     * @return Value of property partNoDd.
     */
    public String getPartNoDd() {
        return ((ItemFinderComponent) getComponent()).getPartNoDd();
    }

    /** Setter for DropDown property partNo.
     * @param partNoDd New value of property partNoDd.
     */
    public void setPartNoDd(String partNoDd) {
        ((ItemFinderComponent) getComponent()).setPartNoDd(partNoDd);
    }

    /** Getter for DropDown property partNo. This is invoked by the custom tag, when the jsp is rendered, to get the current value.
     * @return Value of property partNoDd.
     */
    public DropDownModel getPartNoDdWM() {
        if (w_partNoDd == null) {
            w_partNoDd = (DropDownModel) getWidgetCache().getModel("partNoDd");
            if (w_partNoDd == null) {
                if (getPartNoDd() != null) w_partNoDd = new DropDownModel(getPartNoDd()); else w_partNoDd = new DropDownModel(CriteriaField.RELATIONAL_EQUALS);
                Map optionsMap = CriteriaDropDownOptions.getAllCriteriaDropDownOptions();
                Set options = optionsMap.entrySet();
                for (Iterator i = options.iterator(); i.hasNext(); ) {
                    Map.Entry option = (Map.Entry) i.next();
                    w_partNoDd.addOption((String) option.getValue(), (String) option.getKey());
                }
                getWidgetCache().addModel("partNoDd", w_partNoDd);
            }
        }
        return w_partNoDd;
    }

    /** Setter for DropDown property partNo. This is invoked by the servlet, when a post is done on the Criteria screen.
     * @param value New value of property partNoDd.
     */
    public void setPartNoDdWV(String value) {
        DropDownController.updateModel(value, getPartNoDdWM());
    }

    /** Getter for property serial.
     * @return Value of property serial.
     */
    public String getSerial() {
        return ((ItemFinderComponent) getComponent()).getSerial();
    }

    /** Setter for property serial.
     * @param serial New value of property serial.
     */
    public void setSerial(String serial) {
        ((ItemFinderComponent) getComponent()).setSerial(serial);
    }

    /** Getter for property serial. This is invoked by the custom tag, when the jsp is rendered, to get the current value.
     * @return Value of property serial.
     */
    public EditBoxModel getSerialWM() {
        if (w_serial == null) {
            w_serial = (EditBoxModel) getWidgetCache().getModel("serial");
            if (w_serial == null) {
                if (getSerial() != null) w_serial = new EditBoxModel(getSerial()); else w_serial = new EditBoxModel();
                w_serial.setStringCase(((StringFieldMetaData) ItemMeta.META_SERIAL).getCaseType());
                getWidgetCache().addModel("serial", w_serial);
            }
        }
        return w_serial;
    }

    /** Setter for property serial. This is invoked by the servlet, when a post is done on the Criteria screen.
     * @param value New value of property serial.
     */
    public void setSerialWV(String value) {
        EditBoxController.updateModel(value, getSerialWM());
    }

    /** Getter for DropDown property serial.
     * @return Value of property serialDd.
     */
    public String getSerialDd() {
        return ((ItemFinderComponent) getComponent()).getSerialDd();
    }

    /** Setter for DropDown property serial.
     * @param serialDd New value of property serialDd.
     */
    public void setSerialDd(String serialDd) {
        ((ItemFinderComponent) getComponent()).setSerialDd(serialDd);
    }

    /** Getter for DropDown property serial. This is invoked by the custom tag, when the jsp is rendered, to get the current value.
     * @return Value of property serialDd.
     */
    public DropDownModel getSerialDdWM() {
        if (w_serialDd == null) {
            w_serialDd = (DropDownModel) getWidgetCache().getModel("serialDd");
            if (w_serialDd == null) {
                if (getSerialDd() != null) w_serialDd = new DropDownModel(getSerialDd()); else w_serialDd = new DropDownModel(CriteriaField.RELATIONAL_EQUALS);
                Map optionsMap = CriteriaDropDownOptions.getAllCriteriaDropDownOptions();
                Set options = optionsMap.entrySet();
                for (Iterator i = options.iterator(); i.hasNext(); ) {
                    Map.Entry option = (Map.Entry) i.next();
                    w_serialDd.addOption((String) option.getValue(), (String) option.getKey());
                }
                getWidgetCache().addModel("serialDd", w_serialDd);
            }
        }
        return w_serialDd;
    }

    /** Setter for DropDown property serial. This is invoked by the servlet, when a post is done on the Criteria screen.
     * @param value New value of property serialDd.
     */
    public void setSerialDdWV(String value) {
        DropDownController.updateModel(value, getSerialDdWM());
    }

    /** Getter for property qty.
     * @return Value of property qty.
     */
    public String getQty() {
        return ((ItemFinderComponent) getComponent()).getQty();
    }

    /** Setter for property qty.
     * @param qty New value of property qty.
     */
    public void setQty(String qty) {
        ((ItemFinderComponent) getComponent()).setQty(qty);
    }

    /** Getter for property qty. This is invoked by the custom tag, when the jsp is rendered, to get the current value.
     * @return Value of property qty.
     */
    public EditBoxModel getQtyWM() {
        if (w_qty == null) {
            w_qty = (EditBoxModel) getWidgetCache().getModel("qty");
            if (w_qty == null) {
                if (getQty() != null) w_qty = new EditBoxModel(getQty()); else w_qty = new EditBoxModel();
                getWidgetCache().addModel("qty", w_qty);
            }
        }
        return w_qty;
    }

    /** Setter for property qty. This is invoked by the servlet, when a post is done on the Criteria screen.
     * @param value New value of property qty.
     */
    public void setQtyWV(String value) {
        EditBoxController.updateModel(value, getQtyWM());
    }

    /** Getter for DropDown property qty.
     * @return Value of property qtyDd.
     */
    public String getQtyDd() {
        return ((ItemFinderComponent) getComponent()).getQtyDd();
    }

    /** Setter for DropDown property qty.
     * @param qtyDd New value of property qtyDd.
     */
    public void setQtyDd(String qtyDd) {
        ((ItemFinderComponent) getComponent()).setQtyDd(qtyDd);
    }

    /** Getter for DropDown property qty. This is invoked by the custom tag, when the jsp is rendered, to get the current value.
     * @return Value of property qtyDd.
     */
    public DropDownModel getQtyDdWM() {
        if (w_qtyDd == null) {
            w_qtyDd = (DropDownModel) getWidgetCache().getModel("qtyDd");
            if (w_qtyDd == null) {
                if (getQtyDd() != null) w_qtyDd = new DropDownModel(getQtyDd()); else w_qtyDd = new DropDownModel(CriteriaField.RELATIONAL_EQUALS);
                Map optionsMap = CriteriaDropDownOptions.getNumericalCriteriaDropDownOptions();
                Set options = optionsMap.entrySet();
                for (Iterator i = options.iterator(); i.hasNext(); ) {
                    Map.Entry option = (Map.Entry) i.next();
                    w_qtyDd.addOption((String) option.getValue(), (String) option.getKey());
                }
                getWidgetCache().addModel("qtyDd", w_qtyDd);
            }
        }
        return w_qtyDd;
    }

    /** Setter for DropDown property qty. This is invoked by the servlet, when a post is done on the Criteria screen.
     * @param value New value of property qtyDd.
     */
    public void setQtyDdWV(String value) {
        DropDownController.updateModel(value, getQtyDdWM());
    }

    /** This method should be invoked to ensure a valid state of the FormBean. It will validate the data in the models and set the corresponding properties.
     * Errors will be raised in the FormBean, if any validation fails.
     * @param request The request stream
     * @return A true indicates validations went through successfully. */
    public boolean doValidate(HttpServletRequest request) {
        boolean valid = super.doValidate(request);
        String value = null;
        StringBuffer buf = null;
        value = getSegregationCodeWM().getValue();
        if (value != null && value.trim().length() == 0) value = null;
        setSegregationCode(value);
        setSegregationCodeDd(getSegregationCodeDdWM().getValue());
        value = getPartNoWM().getValue();
        if (value != null && value.trim().length() == 0) value = null;
        setPartNo(value);
        setPartNoDd(getPartNoDdWM().getValue());
        value = getSerialWM().getValue();
        if (value != null && value.trim().length() == 0) value = null;
        setSerial(value);
        setSerialDd(getSerialDdWM().getValue());
        value = getQtyWM().getValue();
        if (value != null && value.trim().length() == 0) value = null;
        setQty(value);
        setQtyDd(getQtyDdWM().getValue());
        return valid;
    }
}
