package com.asoft.examonline.exammanager.valid;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.asoft.examonline.exammanager.manager.ExamQuestionManager;

/**
 * 测试删除试题s校验类
 *
 * @author author
 *
 */
@Test(groups = { "examonline.exammanager.examquestion.valid" }, enabled = true)
public class TestDelExamQuestionsValidator extends ExamManagerValidateClassTestCaseTemplet {

    static Logger logger = Logger.getLogger(TestDelExamQuestionsValidator.class);

    private HttpServletRequest request;

    private ExamQuestionManager examQuestionManager;

    private DelExamQuestionsValidator delExamQuestionsValidator;

    @BeforeClass
    public void setUp() {
        this.delExamQuestionsValidator = new DelExamQuestionsValidator();
    }

    @BeforeMethod
    public void initBeforeMethod() {
        this.request = this.getMockedHttpServletRequest();
        this.examQuestionManager = (ExamQuestionManager) this.getMockedManager(ExamQuestionManager.class);
        this.delExamQuestionsValidator.setExamQuestionManager(this.examQuestionManager);
    }
}
