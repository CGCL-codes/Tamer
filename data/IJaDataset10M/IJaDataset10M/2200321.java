package icescrum2.dao.impl;

import icescrum2.dao.ITaskDao;
import icescrum2.dao.model.IRemainingEstimationArray;
import icescrum2.dao.model.ITask;
import icescrum2.dao.model.impl.Task;
import java.io.Serializable;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TaskDao extends HibernateDaoSupport implements ITaskDao {

    public boolean checkUniqueName(ITask _pbi, Serializable idPb) {
        return true;
    }

    public boolean saveTask(ITask _task) {
        if (this.checkUniqueName(_task, _task.getParentProductBacklogItem().getIdProductBacklogItem())) {
            try {
                this.getHibernateTemplate().save(_task);
            } catch (DataIntegrityViolationException _e) {
                ExceptionManager.getInstance().manageDataIntegrityViolationException(this.getClass().getName(), "saveSprintBacklogItem", _e);
                return false;
            } catch (ConstraintViolationException _ex) {
                ExceptionManager.getInstance().manageConstraintViolationException(this.getClass().getName(), "saveSprintBacklogItem", _ex);
                return false;
            }
            return true;
        }
        return false;
    }

    public void saveTaskEstimation(IRemainingEstimationArray _re) {
        try {
            this.getHibernateTemplate().save(_re);
        } catch (DataIntegrityViolationException _e) {
            ExceptionManager.getInstance().manageDataIntegrityViolationException(this.getClass().getName(), "saveSprintBacklogItem", _e);
        } catch (ConstraintViolationException _ex) {
            ExceptionManager.getInstance().manageConstraintViolationException(this.getClass().getName(), "saveSprintBacklogItem", _ex);
        }
    }

    public void updateTask(ITask _task) {
        try {
            this.getHibernateTemplate().update(_task);
        } catch (DataIntegrityViolationException _e) {
            ExceptionManager.getInstance().manageDataIntegrityViolationException(this.getClass().getName(), "updateSprintBacklogItem", _e);
        } catch (ConstraintViolationException _ex) {
            ExceptionManager.getInstance().manageConstraintViolationException(this.getClass().getName(), "updateSprintBacklogItem", _ex);
        }
    }

    public Boolean deleteTask(ITask _task) {
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().delete(_task);
            return true;
        } catch (DataAccessException _e) {
            ExceptionManager.getInstance().manageDataAccessException(this.getClass().getName(), "deleteTask", _e);
            return false;
        }
    }

    public Boolean deleteRemainingEstimation(IRemainingEstimationArray rm) {
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().delete(rm);
            return true;
        } catch (DataAccessException _e) {
            ExceptionManager.getInstance().manageDataAccessException(this.getClass().getName(), "deleteRM", _e);
            return false;
        }
    }

    public void updateEstimationArray(IRemainingEstimationArray estimations) {
        try {
            this.getHibernateTemplate().update(estimations);
        } catch (DataIntegrityViolationException _e) {
            ExceptionManager.getInstance().manageDataIntegrityViolationException(this.getClass().getName(), "updateSprintBacklogItem", _e);
        } catch (ConstraintViolationException _ex) {
            ExceptionManager.getInstance().manageConstraintViolationException(this.getClass().getName(), "updateSprintBacklogItem", _ex);
        }
    }

    public ITask getTask(int id) {
        return (ITask) getHibernateTemplate().get(Task.class, id);
    }
}
