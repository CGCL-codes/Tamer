package info.gdeDengi.expense;

import info.gdeDengi.common.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class ExpensesCategoryUserService extends CommonService {

    protected BasicDao basicDao;

    public BasicDao getBasicDao() {
        return basicDao;
    }

    public void setBasicDao(BasicDao basicDao) {
        this.basicDao = basicDao;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<ExpenseCategoryUser> findAll(int _userId) {
        String q = "select ec from ExpenseCategoryUser ec where ec.userid = " + String.valueOf(_userId);
        List<?> categories = this.basicDao.find(q, null);
        return (List<ExpenseCategoryUser>) categories;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeAll() {
        String q = "delete from ExpenseCategoryUser ec where ec.id.userid = " + String.valueOf(this.getSessionManager().getCurrentUser().getUserid());
        EntityManager em = this.getBasicDao().getEntityManager();
        Query qr = em.createQuery(q);
        qr.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<ExpenseCategoryUser> findChildren(int _parentId) {
        String q = "select ec from ExpenseCategoryUser ec where ec.parentid = " + String.valueOf(_parentId) + " and ec.user.userid = " + String.valueOf(this.getSessionManager().getCurrentUser().getUserid());
        List<?> categories = this.basicDao.find(q, null);
        return (List<ExpenseCategoryUser>) categories;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void persistEvent(ExpenseCategoryUser category) {
        this.basicDao.persist(category);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteEvent(ExpenseCategoryUser category) throws EntityNotFoundException {
        this.basicDao.remove(ExpenseCategoryUser.class, category.getId());
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void mergeEvent(ExpenseCategoryUser category) throws EntityNotFoundException {
        this.basicDao.merge(category);
    }

    public Integer getNextId() {
        return this.basicDao.nextNum("ecu_id");
    }

    public void generateExpenseCategoryUser(User curUser) {
        String q = "select ec from ExpenseCategoryTemplate ec";
        List<ExpenseCategoryTemplate> templCats = this.getBasicDao().find(q, null);
        Iterator it = templCats.iterator();
        while (it.hasNext()) {
            ExpenseCategoryUser ecu = new ExpenseCategoryUser((ExpenseCategoryTemplate) it.next());
            ecu.setUsername(curUser.getUsername());
            ecu.getId().setUserid(curUser.getUserid());
            this.persistEvent(ecu);
        }
    }
}
