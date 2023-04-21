package org.pojosoft.lms.web.gwt.client.learningassignment;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.pojosoft.lms.web.gwt.client.course.CourseServiceAsync;
import org.pojosoft.lms.web.gwt.client.support.ServiceLocator;
import org.pojosoft.ria.gwt.client.service.AuthenticationException;
import org.pojosoft.ria.gwt.client.service.UserMessageUtils;
import org.pojosoft.ria.gwt.client.ui.ErrorDialogBox;
import org.pojosoft.ria.gwt.client.ui.support.LayoutUtils;
import org.pojosoft.ria.gwt.client.ui.support.Logger;
import org.pojosoft.ria.gwt.client.ui.support.WidgetFactory;
import org.pojosoft.ria.gwt.client.ui.treetable.ModelRow;
import org.pojosoft.ria.gwt.client.ui.treetable.TreeTableModel;
import org.pojosoft.ria.gwt.client.ui.wizard.DefaultWizardStep;
import org.pojosoft.ria.gwt.client.ui.wizard.WizardContext;
import java.util.ArrayList;
import java.util.List;

/**
 * A step in the the Learning Assignment wizard.
 *
 * @author POJO Softare
 */
public class FinishWizardStep extends DefaultWizardStep {

    VerticalPanel pageContainer;

    public Widget createPageWidget() {
        Logger.debug(this, "createPageWidget");
        pageContainer = WidgetFactory.createHorizAlignVerticalPanel(false);
        return pageContainer;
    }

    private List getIdList(String key) {
        List ids = new ArrayList();
        TreeTableModel userModel = (TreeTableModel) context.getData(key);
        for (int i = 0; i < userModel.getRows().size(); i++) {
            ModelRow modelRow = (ModelRow) userModel.getRows().get(i);
            ids.add(modelRow.getId());
        }
        return ids;
    }

    public void onShow() {
        Logger.debug(this, "onShow");
        List userIds = getIdList("users");
        List courseIds = getIdList("courses");
        String dueDate = (String) context.getData("dueDate");
        CourseServiceAsync courseService = ServiceLocator.lookupCourseService();
        courseService.assignUserCourses(userIds, courseIds, dueDate, new AsyncCallback() {

            public void onFailure(Throwable caught) {
                if (caught instanceof AuthenticationException) ErrorDialogBox.showError(UserMessageUtils.getMessage("error.UnauthorizedAccess"), caught.getMessage()); else ErrorDialogBox.showError(UserMessageUtils.getMessage("error.LearningAssignment.AssignCoursesToUsersFailed"), caught.getMessage());
            }

            public void onSuccess(Object result) {
                pageContainer.add(WidgetFactory.createLabel(UserMessageUtils.getMessage("header.Result")));
                LayoutUtils.displayValidationMessages(pageContainer, (List) result);
            }
        });
    }

    public void navigateNextStep(Widget sender, WizardContext context) {
    }

    public void navigatePreviousStep(Widget sender, WizardContext context) {
    }
}
