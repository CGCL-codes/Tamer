package com.technoetic.xplanner.actions;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.xplanner.dao.UserStoryDao;
import net.sf.xplanner.dao.impl.CommonDao;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.UserStory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import com.technoetic.xplanner.domain.repository.ObjectRepository;
import com.technoetic.xplanner.forms.AbstractEditorForm;
import com.technoetic.xplanner.forms.UserStoryEditorForm;

public class EditStoryAction extends EditObjectAction<UserStory> {

    public static final String CONTINUED = "continued";

    public static final String MOVED = "moved";

    public static final String OPERATION_PARAM_KEY = "operation";

    public static final String ACTION_KEY = "action";

    @Override
    public void beforeObjectCommit(UserStory object, ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse reply) throws Exception {
        UserStory story = (UserStory) object;
        UserStoryEditorForm userStoryEditorForm = (UserStoryEditorForm) actionForm;
        int iterationId = userStoryEditorForm.getIterationId();
        Iteration iteration = (Iteration) getCommonDao().getById(Iteration.class, iterationId);
        story.setIteration(iteration);
        getCommonDao().flush();
        List<UserStory> userStories = iteration.getUserStories();
        if (!userStories.contains(story)) {
            userStories.add(story);
        }
        iteration.modifyStoryOrder(DomainOrderer.buildStoryIdNewOrderArray(userStories));
        String action = request.getParameter(ACTION_KEY);
    }

    @Override
    protected void populateForm(AbstractEditorForm form, ActionMapping actionMapping, HttpServletRequest request) throws Exception {
        super.populateForm(form, actionMapping, request);
        if (form.getOid() == null) {
            UserStoryEditorForm storyForm = (UserStoryEditorForm) form;
            int iterationId = Integer.parseInt(request.getParameter("fkey"));
            Iteration iteration = (Iteration) getCommonDao().getById(Iteration.class, iterationId);
            storyForm.setDispositionName(iteration.determineNewStoryDisposition().getName());
            storyForm.setOrderNo(iteration.getUserStories().size() + 1);
        }
    }
}
