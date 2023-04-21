package biz.subjectMager;

import java.util.Collection;
import models.Subject;
import utils.TransactionTemplate;
import dao.SubjectDao;
import dao.SubjectDaoHbnImpl;

public class SubjectMagerBizImpl implements SubjectMagerBiz {

    private SubjectDao subDao = new SubjectDaoHbnImpl();

    @Override
    public void addSubject(Subject sub) {
        try {
            TransactionTemplate.beginTransaction();
            subDao.addSubject(sub);
            TransactionTemplate.commit();
        } catch (Exception e) {
            e.printStackTrace();
            TransactionTemplate.rollback();
        }
    }

    @Override
    public void deleteSubject(Long subId) {
        try {
            TransactionTemplate.beginTransaction();
            subDao.delSubject(subId);
            TransactionTemplate.commit();
        } catch (Exception e) {
            e.printStackTrace();
            TransactionTemplate.rollback();
        }
    }

    @Override
    public Collection<Subject> getAllSubjects() {
        Collection<Subject> subs = null;
        try {
            TransactionTemplate.beginTransaction();
            subs = subDao.selAllSubject();
            TransactionTemplate.commit();
        } catch (Exception e) {
            e.printStackTrace();
            TransactionTemplate.rollback();
        }
        return subs;
    }

    @Override
    public Subject getSubjectById(Long subId) {
        Subject sub = null;
        try {
            TransactionTemplate.beginTransaction();
            sub = subDao.selSubjectById(subId);
            TransactionTemplate.commit();
        } catch (Exception e) {
            e.printStackTrace();
            TransactionTemplate.rollback();
        }
        return sub;
    }

    @Override
    public void modifySubject(Subject sub) {
        try {
            TransactionTemplate.beginTransaction();
            subDao.updateSubject(sub);
            TransactionTemplate.commit();
        } catch (Exception e) {
            e.printStackTrace();
            TransactionTemplate.rollback();
        }
    }
}
