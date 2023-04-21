package de.beas.explicanto.distribution.web.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import de.beas.explicanto.distribution.config.Constants;
import de.beas.explicanto.distribution.util.PagedList;
import de.beas.explicanto.distribution.web.forms.BaseForm;
import de.beas.explicanto.distribution.model.Course;

/**
 * The WizardBaseAction class.
 *
 */
public abstract class WizardBaseAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(WizardBaseAction.class);

    protected void processList(BaseForm baseFrm, HttpServletRequest request, EntitiesCallback callback, String sessionLstKey) {
        ActionMessages errors = new ActionMessages();
        try {
            int start = (baseFrm.getPage().intValue() - 1) <= 0 ? 0 : (baseFrm.getPage().intValue() - 1) * baseFrm.getOffset().intValue();
            int stop = baseFrm.getOffset().intValue();
            PagedList lst = callback.getEntities(start, stop, request);
            int pagesNo = (lst.getTotalParentSize() % baseFrm.getOffset().intValue() == 0) ? lst.getTotalParentSize() / baseFrm.getOffset().intValue() : (lst.getTotalParentSize() / baseFrm.getOffset().intValue()) + 1;
            if (pagesNo < baseFrm.getPage().intValue()) {
                baseFrm.setPage(new Integer(pagesNo));
                start = (baseFrm.getPage().intValue() - 1) <= 0 ? 0 : (baseFrm.getPage().intValue() - 1) * baseFrm.getOffset().intValue();
                lst = callback.getEntities(start, stop, request);
            }
            request.setAttribute(sessionLstKey, lst);
        } catch (Exception e) {
            logger.error("At WizardBaseAction !!!", e);
            errors.add(Constants.ERROR_MESSAGE, new ActionMessage("global.error.message", e.getMessage()));
        }
        if (!errors.isEmpty()) {
            saveMessages(request, errors);
        }
    }

    protected List processSelectedIds(String selectedIds) {
        List selIdsLst = new ArrayList();
        if (selectedIds != null) {
            String[] selectedIdsArr = selectedIds.split(",");
            for (int i = 0; i < selectedIdsArr.length; i++) {
                String id = selectedIdsArr[i];
                if (!"".equals(id)) {
                    selIdsLst.add(id);
                }
            }
        }
        return selIdsLst;
    }

    protected Course getCourse(Map changedCourses, String courseId) {
        Course course = (Course) changedCourses.get(courseId);
        if (course == null) {
            course = getCourseAdministrationService().getCourse(courseId);
            changedCourses.put(courseId, course);
        }
        return course;
    }
}
