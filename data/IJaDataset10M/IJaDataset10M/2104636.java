package org.openuss.paperSubmission;

import java.util.Date;
import java.util.List;
import org.openuss.TestUtility;
import org.openuss.foundation.DefaultDomainObject;
import org.openuss.security.User;

/**
 * JUnit Test for Spring Hibernate PaperSubmissionDao class.
 * @see org.openuss.paperSubmission.PaperSubmissionDao
 */
public class PaperSubmissionDaoTest extends PaperSubmissionDaoTestBase {

    private ExamDao examDao;

    private TestUtility testUtility;

    public void testPaperSubmissionDao() {
        DefaultDomainObject domain = new DefaultDomainObject(TestUtility.unique());
        Exam exam = new ExamImpl();
        exam.setName("test_exam");
        exam.setDescription("description");
        exam.setDomainId(domain.getId());
        Long futureDate = new Date().getTime() + 100000000;
        exam.setDeadline(new Date(futureDate));
        assertNull(exam.getId());
        getExamDao().create(exam);
        assertNotNull(exam.getId());
        User user = getTestUtility().createUniqueUserInDB();
        PaperSubmission paperSubmission = PaperSubmission.Factory.newInstance();
        paperSubmission.setDeliverDate(new Date());
        paperSubmission.setExam(exam);
        paperSubmission.setSender(user);
        assertNull(paperSubmission.getId());
        paperSubmissionDao.create(paperSubmission);
        assertNotNull(paperSubmission.getId());
        PaperSubmission paperSubmission2 = paperSubmissionDao.load(paperSubmission.getId());
        assertEquals(paperSubmission2.getId(), paperSubmission.getId());
        paperSubmission.setDeliverDate(new Date());
        paperSubmissionDao.update(paperSubmission);
        paperSubmission2 = paperSubmissionDao.load(paperSubmission.getId());
        List<PaperSubmission> submissionList = paperSubmissionDao.findByExam(exam);
        assertNotNull(submissionList);
        List<PaperSubmission> submissionListByUser = paperSubmissionDao.findByExamAndUser(exam, user);
        assertNotNull(submissionListByUser);
        paperSubmissionDao.remove(paperSubmission2);
        assertNull(paperSubmissionDao.load(paperSubmission2.getId()));
    }

    public ExamDao getExamDao() {
        return examDao;
    }

    public void setExamDao(ExamDao examDao) {
        this.examDao = examDao;
    }

    public TestUtility getTestUtility() {
        return testUtility;
    }

    public void setTestUtility(TestUtility testUtility) {
        this.testUtility = testUtility;
    }
}
