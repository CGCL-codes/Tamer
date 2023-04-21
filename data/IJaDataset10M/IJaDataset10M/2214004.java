package com.dcivision.workflow.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionMapping;
import com.dcivision.framework.ApplicationException;
import com.dcivision.framework.SystemFunctionConstant;
import com.dcivision.framework.web.AbstractActionForm;
import com.dcivision.framework.web.AbstractMaintAction;

/**
  MaintWorkflowProgressReasonAction.java

  This class is for maint workflow record.

    @author          Angus Shiu
    @company         DCIVision Limited
    @creation date   29/07/2003
    @version         $Revision: 1.4 $
*/
public class MaintWorkflowProgressReasonAction extends AbstractMaintAction {

    public static final String REVISION = "$Revision: 1.4 $";

    /** Creates a new instance of MaintWorkflowProgressReasonAction */
    public MaintWorkflowProgressReasonAction() {
        super();
    }

    /** getFunctionCode
   *
   * Abstract function which sub-class should implement to return the corresponding
   * function code.
   *
   * @return   The function code
   */
    public String getFunctionCode() {
        return (SystemFunctionConstant.WORKFLOW_TASK);
    }

    /** getMajorDAOClassName
   *
   * Abstract function which sub-class should implement to return the corresponding
   * major DAO class name used in this class.
   *
   * @return   The DAO class name
   */
    public String getMajorDAOClassName() {
        return ("com.dcivision.workflow.dao.MtmWorkflowProgressUserRecordDAObject");
    }

    public void clearUniqueFields(ActionMapping mapping, AbstractActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        MaintWorkflowProgressForm workflowProgressForm = (MaintWorkflowProgressForm) form;
        workflowProgressForm.setID(null);
    }

    public void selectRecord(ActionMapping mapping, AbstractActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        super.selectRecord(mapping, form, request, response);
    }
}
