package com.asoft.examonline.exammanager.manager;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.easymock.EasyMock;
import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.asoft.examonline.exammanager.dao.ExamQuestionDAO;
import com.asoft.examonline.exammanager.model.ExamQuestion;

/**
 * 试题manager单元测试用例模板
 *
 * @author amonlei
 *
 */
@Test(groups = { "examonline.exammanager.examquestion.manager" }, enabled = true)
public class TestExamQuestionManager extends ExamManagerManagerTestCaseTemplet {

    static Logger logger = Logger.getLogger(TestExamQuestionManager.class);

    private ExamQuestionDAO examQuestionDAO;

    private ExamQuestionManager examQuestionManager;

    @BeforeClass
    public void setUp() {
        this.examQuestionManager = (ExamQuestionManager) this.getManagerInst(ExamQuestionManagerimp.class);
    }

    @BeforeMethod
    public void initBeforeMethod() {
        this.examQuestionDAO = (ExamQuestionDAO) this.getMockedDAO(ExamQuestionDAO.class);
        this.examQuestionManager.setDAO(this.examQuestionDAO);
    }
}
