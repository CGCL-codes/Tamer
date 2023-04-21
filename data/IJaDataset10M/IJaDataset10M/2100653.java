package biz.attemptMager;

import java.util.Collection;
import models.Attempt;
import models.ExaminationOfPaper;
import utils.TransactionTemplate;
import dao.AttemptDao;
import dao.AttemptDaoHbnImpl;
import dao.ExamOfPaprDao;
import dao.ExamOfPaprDaoHbnImpl;

public class AttemptMagerBizImpl implements AttemptMagerBiz {

    private AttemptDao attDao = new AttemptDaoHbnImpl();

    private ExamOfPaprDao eopDao = new ExamOfPaprDaoHbnImpl();

    @Override
    public void addAttempt(Attempt attempt) {
        try {
            TransactionTemplate.beginTransaction();
            attDao.addAttempt(attempt);
            TransactionTemplate.commit();
        } catch (Exception e) {
            e.printStackTrace();
            TransactionTemplate.rollback();
        }
    }

    @Override
    public void deleteAttempt(Long attId) {
        try {
            TransactionTemplate.beginTransaction();
            attDao.delAttemptById(attId);
            TransactionTemplate.commit();
        } catch (Exception e) {
            e.printStackTrace();
            TransactionTemplate.rollback();
        }
    }

    @Override
    public Collection<Attempt> getAllAttempts() {
        Collection<Attempt> attempts = null;
        try {
            TransactionTemplate.beginTransaction();
            attempts = attDao.selAttemptsByPaprId();
            TransactionTemplate.commit();
        } catch (Exception e) {
            e.printStackTrace();
            TransactionTemplate.rollback();
        }
        return attempts;
    }

    @Override
    public Attempt getAttemptById(Long attId) {
        Attempt attempt = null;
        try {
            TransactionTemplate.beginTransaction();
            attempt = attDao.selAttemptById(attId);
            TransactionTemplate.commit();
        } catch (Exception e) {
            e.printStackTrace();
            TransactionTemplate.rollback();
        }
        return attempt;
    }

    @Override
    public void modifyAttempt(Attempt attempt) {
        try {
            TransactionTemplate.beginTransaction();
            attDao.updateAttempt(attempt);
            TransactionTemplate.commit();
        } catch (Exception e) {
            e.printStackTrace();
            TransactionTemplate.rollback();
        }
    }

    @Override
    public Collection<ExaminationOfPaper> getAllPapers() {
        Collection<ExaminationOfPaper> eops = null;
        try {
            TransactionTemplate.beginTransaction();
            eops = eopDao.selAllPapers();
            TransactionTemplate.commit();
        } catch (Exception e) {
            e.printStackTrace();
            TransactionTemplate.rollback();
        }
        return eops;
    }

    @Override
    public ExaminationOfPaper getPaperById(Long paperId) {
        ExaminationOfPaper eop = null;
        try {
            TransactionTemplate.beginTransaction();
            eop = eopDao.selPaperById(paperId);
            TransactionTemplate.commit();
        } catch (Exception e) {
            e.printStackTrace();
            TransactionTemplate.rollback();
        }
        return eop;
    }
}
