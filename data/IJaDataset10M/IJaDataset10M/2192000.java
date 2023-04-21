package org.endeavour.mgmt.view.model;

import java.text.SimpleDateFormat;
import java.util.List;
import org.endeavour.mgmt.model.ChangeRequest;
import org.endeavour.mgmt.model.Defect;
import org.endeavour.mgmt.model.Task;
import org.endeavour.mgmt.model.UseCase;
import org.endeavour.mgmt.model.WorkProduct;
import org.endeavour.mgmt.view.IViewConstants;
import org.endeavour.mgmt.view.components.GridBoxModel;

public class WorkProductsListModel extends GridBoxModel {

    public WorkProductsListModel() {
        super();
    }

    public WorkProductsListModel(List<WorkProduct> aWorkProducts) {
        super(aWorkProducts);
    }

    public void initializeColumns() {
        String theNumber = IViewConstants.RB.getString("number.lbl");
        String theArtifactName = IViewConstants.RB.getString("artifact_name.lbl");
        String theIcon = IViewConstants.RB.getString("icon.lbl");
        String theType = IViewConstants.RB.getString("type.lbl");
        String theProject = IViewConstants.RB.getString("project.lbl");
        String theDuration = IViewConstants.RB.getString("duration.lbl");
        String theStartDate = IViewConstants.RB.getString("start_date.lbl");
        String theEndDate = IViewConstants.RB.getString("end_date.lbl");
        String thePercentComplete = IViewConstants.RB.getString("percent_complete.lbl");
        super.columns = new String[] { theNumber, theArtifactName, theIcon, theType, theProject, theDuration, theStartDate, theEndDate, thePercentComplete };
    }

    public Object getValueAt(int aRowIndex, int aColumnIndex) {
        String theValue = null;
        WorkProduct theWorkProduct = (WorkProduct) this.rows.get(aRowIndex);
        switch(aColumnIndex) {
            case 0:
                if (theWorkProduct instanceof UseCase) {
                    theValue = IViewConstants.RB.getString("use_case_initials.lbl") + theWorkProduct.getId().toString();
                } else if (theWorkProduct instanceof Task) {
                    theValue = IViewConstants.RB.getString("task_initial.lbl") + theWorkProduct.getId().toString();
                    Task theTask = (Task) theWorkProduct;
                    WorkProduct theOwner = theTask.getWorkProduct();
                    if (theOwner != null) {
                        theValue = theValue + " (" + IViewConstants.RB.getString("use_case_initials.lbl") + theOwner.getId().toString() + ")";
                    }
                } else if (theWorkProduct instanceof Defect) {
                    theValue = IViewConstants.RB.getString("defect_initial.lbl") + theWorkProduct.getId().toString();
                } else if (theWorkProduct instanceof ChangeRequest) {
                    theValue = IViewConstants.RB.getString("change_request_initials.lbl") + theWorkProduct.getId().toString();
                }
                break;
            case 1:
                theValue = theWorkProduct.getName();
                break;
            case 2:
                if (theWorkProduct instanceof UseCase) {
                    theValue = IViewConstants.USE_CASES_ICON;
                } else if (theWorkProduct instanceof Task) {
                    theValue = IViewConstants.TASKS_ICON;
                } else if (theWorkProduct instanceof Defect) {
                    theValue = IViewConstants.DEFECTS_ICON;
                } else if (theWorkProduct instanceof ChangeRequest) {
                    theValue = IViewConstants.CHANGE_REQUESTS_ICON;
                }
                theValue = "<img src=\"" + theValue + "\"/>";
                break;
            case 3:
                theValue = theWorkProduct.getElementType();
                break;
            case 4:
                theValue = theWorkProduct.getProject().getName();
                break;
            case 5:
                theValue = (theWorkProduct.getEndDate().getTime() - theWorkProduct.getStartDate().getTime()) / (24 * 60 * 60 * 1000) + 1 + " " + IViewConstants.RB.getString("days.lbl");
                break;
            case 6:
                theValue = new SimpleDateFormat(IViewConstants.DATE_MASK).format(theWorkProduct.getStartDate());
                break;
            case 7:
                theValue = new SimpleDateFormat(IViewConstants.DATE_MASK).format(theWorkProduct.getEndDate());
                break;
            case 8:
                theValue = theWorkProduct.getProgress() + IViewConstants.RB.getString("percent_sign.lbl");
                break;
            default:
                theValue = "";
        }
        return theValue;
    }

    public int getRowId(int aRowIndex) {
        WorkProduct theWorkProduct = (WorkProduct) this.rows.get(aRowIndex);
        return theWorkProduct.getId();
    }
}
