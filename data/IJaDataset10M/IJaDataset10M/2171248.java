package com.ipolyglot.webapp.action;

import com.ipolyglot.Constants;
import com.ipolyglot.model.User;
import com.ipolyglot.webapp.form.LessonUserGradeForm;

/**
 * @author mishag
 */
public class LessonUserGradeActionTest extends MyLazyStrutsTestCase {

    public LessonUserGradeActionTest(String name) {
        super(name);
    }

    public void testEdit() throws Exception {
        setRequestPathInfo("/editLessonUserGrade");
        addRequestParameter("method", "Edit");
        addRequestParameter("id", "1");
        actionPerform();
        verifyForward("edit");
        assertTrue(request.getAttribute(Constants.LESSON_USER_GRADE_KEY) != null);
        verifyNoActionErrors();
    }

    public void testSave() throws Exception {
        setRequestPathInfo("/editLessonUserGrade");
        addRequestParameter("method", "Edit");
        addRequestParameter("id", "1");
        actionPerform();
        LessonUserGradeForm lessonUserGradeForm = (LessonUserGradeForm) request.getAttribute(Constants.LESSON_USER_GRADE_KEY);
        assertTrue(lessonUserGradeForm != null);
        setRequestPathInfo("/saveLessonUserGrade");
        addRequestParameter("method", "Save");
        lessonUserGradeForm.setComment("Updated from LessonUserGradeActionTest");
        request.setAttribute(Constants.LESSON_USER_GRADE_KEY, lessonUserGradeForm);
        actionPerform();
        verifyForward("edit");
        verifyNoActionErrors();
    }

    public void testSearch() {
        setRequestPathInfo("/editLessonUserGrade");
        addRequestParameter("method", "Search");
        User user = new User();
        user.setUsername("kitty");
        request.setAttribute(Constants.USER_KEY, user);
        actionPerform();
        verifyForward("list");
        assertNotNull(getRequest().getAttribute(Constants.LESSON_USER_GRADE_LIST));
        verifyNoActionErrors();
    }
}
