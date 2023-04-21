package org.jaffa.applications.mylife.admin.components.userrolelookup.ui;

import org.apache.log4j.Logger;
import org.jaffa.presentation.portlet.FormBase;
import org.jaffa.presentation.portlet.widgets.model.GridModel;
import org.jaffa.presentation.portlet.widgets.model.GridModelRow;
import org.jaffa.presentation.portlet.widgets.model.CheckBoxModel;
import org.jaffa.presentation.portlet.widgets.controller.UserGridController;
import org.jaffa.components.finder.IFinderListener;
import java.util.EventObject;
import org.jaffa.applications.mylife.admin.components.userrolelookup.dto.UserRoleLookupOutDto;
import org.jaffa.applications.mylife.admin.components.userrolelookup.dto.UserRoleLookupOutRowDto;

/** The FormBean class to support the Results jsp of the UserRoleLookup.
 */
public class UserRoleLookupResultsForm extends FormBase {

    /** The name constant used for determining the corresponding jsp through the struts-config file.
     */
    public static final String NAME = "admin_userRoleLookupResults";

    private static final Logger log = Logger.getLogger(UserRoleLookupResultsForm.class);

    private GridModel w_rows = null;

    private boolean m_moreRecordsExist = false;

    /** Getter for property rows. This is invoked by the custom tag, when the jsp is rendered, to get the current value.
     * This gets the current data from the component.
     * @return Value of property roleName.
     */
    public GridModel getRowsWM() {
        if (w_rows == null) {
            w_rows = (GridModel) getWidgetCache().getModel("rows");
            if (w_rows == null) {
                w_rows = new GridModel();
                getWidgetCache().addModel("rows", w_rows);
                populateRows();
            }
        }
        return w_rows;
    }

    /** Setter for property rows. This is invoked by the servlet, when a post is done on the Results screen.
     * It sets the selected rows on the model.
     * @param value New value of property roleName.
     */
    public void setRowsWV(String value) {
        UserGridController.updateModel(value, getRowsWM(), this);
    }

    /** Getter for property moreRecordsExist.
     * @return Value of property moreRecordsExist.
     */
    public boolean getMoreRecordsExist() {
        return m_moreRecordsExist;
    }

    /** Setter for property moreRecordsExist.
     * @param moreRecordsExist New value of property moreRecordsExist.
     */
    public void setMoreRecordsExist(boolean moreRecordsExist) {
        m_moreRecordsExist = moreRecordsExist;
    }

    private void populateRows() {
        GridModel rows = getRowsWM();
        rows.clearRows();
        UserRoleLookupOutDto outputDto = ((UserRoleLookupComponent) getComponent()).getUserRoleLookupOutDto();
        if (outputDto != null) {
            GridModelRow row;
            for (int i = 0; i < outputDto.getRowsCount(); i++) {
                UserRoleLookupOutRowDto rowDto = outputDto.getRows(i);
                row = rows.newRow();
                row.addElement("userName", rowDto.getUserName());
                row.addElement("roleName", rowDto.getRoleName());
            }
        }
    }

    private void determineMoreRecordsExist() {
        UserRoleLookupOutDto outputDto = ((UserRoleLookupComponent) getComponent()).getUserRoleLookupOutDto();
        if (outputDto != null && outputDto.getMoreRecordsExist() != null && outputDto.getMoreRecordsExist().booleanValue()) m_moreRecordsExist = true; else m_moreRecordsExist = false;
    }

    /** This registers a listener with the Component. */
    public void initForm() {
        super.initForm();
        determineMoreRecordsExist();
        ((UserRoleLookupComponent) getComponent()).setFinderListener(new IFinderListener() {

            public void inquiryDone(EventObject eventObject) {
                populateRows();
                determineMoreRecordsExist();
            }
        });
    }
}
