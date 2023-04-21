package mf.torneo.logic;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import mf.torneo.component.pager.CriteriaPager;
import mf.torneo.model.Categorie;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.PropertyValueException;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:maurofgn@tiscali.it">Mauro Fugante</a><br>
 *         Categorie persistence management
 */
public class CategorieLogic extends AbstractLogic {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5926122734747608494L;

    private java.lang.Integer idcate;

    private static final Log LOG = LogFactory.getLog(CategorieLogic.class);

    private Categorie dataDB;

    private Categorie dataForm;

    private List listDB;

    private List listForm;

    public CategorieLogic() {
    }

    /**
	 * @param dataForm
	 */
    public CategorieLogic(Categorie dataForm) {
        setForm(dataForm);
    }

    /**
	 * data from DB with primary key from idcate
	 * 
	 * @return categorie
	 * @throws HibernateException
	 */
    public Categorie getDataDB() {
        return getDataDB(getIdcate());
    }

    /**
	 * data from DB
	 * 
	 * @param id
	 *            primary key
	 * @return categorie
	 * @throws HibernateException
	 */
    public Categorie getDataDB(java.lang.Integer id) {
        if (dataDB == null && id != null) {
            try {
                dataDB = (Categorie) getSession().get(Categorie.class, id);
            } catch (HibernateException e) {
                setError(e);
            }
            idcate = (dataDB != null ? id : null);
        }
        return dataDB;
    }

    /**
	 * delete istance defined by getIdcate
	 * 
	 * @throws HibernateException
	 */
    public void delete() {
        delete(getIdcate());
    }

    /**
	 * delete istance
	 * 
	 * @param id
	 *            primary key
	 * @throws HibernateException
	 */
    public void delete(java.lang.Integer id) {
        if (id != null) delete(getDataDB(id));
    }

    /**
	 * delete istance
	 * 
	 * @param obj
	 *            Categorie istance
	 * @throws HibernateException
	 */
    public void delete(Categorie obj) {
        if (obj != null) {
            try {
                getSession().delete(obj);
                getSession().flush();
            } catch (HibernateException e) {
                setError(e);
            }
        }
    }

    /**
	 * list save
	 * 
	 * @throws HibernateException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
    public void saveListForm() {
        saveListForm(getListForm());
    }

    /**
	 * list save
	 * 
	 * @param listForm
	 *            list of categorie
	 * @throws HibernateException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
    public void saveListForm(List listForm) {
        for (Iterator iter = listForm.iterator(); iter.hasNext() && !isRollBackOnly(); ) {
            Categorie dataForm = (Categorie) iter.next();
            saveForm(dataForm);
        }
    }

    /**
	 * save CategorieForm defined from CategorieForm
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws HibernateException
	 *  
	 */
    public void saveForm() {
        Categorie dataForm = getForm();
        saveForm(dataForm);
    }

    /**
	 * save dataForm
	 * 
	 * @param dataForm
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws HibernateException
	 */
    public void saveForm(Categorie dataForm) {
        try {
            Categorie dataDB = getDataDB(dataForm.getIdcate());
            if (dataDB == null) {
                getSession().save(dataForm);
            } else {
                BeanUtils.copyProperties(dataDB, dataForm);
                getSession().saveOrUpdate(dataDB);
            }
            getSession().flush();
        } catch (PropertyValueException e) {
            setError(e);
        } catch (HibernateException e) {
            setError(e);
        } catch (IllegalAccessException e) {
            setError(e);
        } catch (InvocationTargetException e) {
            setError(e);
        }
    }

    /**
	 * Categorie list
	 * 
	 * @return Categorie list. Filter defined in form
	 * @throws HibernateException
	 */
    public List getList() throws HibernateException {
        return getList(getForm());
    }

    /**
	 * Categorie list
	 * 
	 * @param example
	 * @return Categorie list. Filter defined from example
	 * @throws HibernateException
	 */
    public List getList(Categorie example) {
        Criteria criteria = null;
        try {
            criteria = getSession().createCriteria(Categorie.class);
            if (example != null) {
                criteria.add(Example.create(example).ignoreCase().enableLike(MatchMode.START));
            }
            criteria.addOrder(Order.asc("categoria"));
            criteria.setCacheable(true);
        } catch (HibernateException e) {
            setError(e);
        }
        return (criteria != null ? getList(criteria) : null);
    }

    /**
	 * Categorie list
	 * 
	 * @param criteria
	 * @return Categorie list. Filter defined from criteria
	 * @throws HibernateException
	 */
    public List getList(Criteria criteria) {
        if (getMaxResults() > 0 && getSearchManager() != null) {
            CriteriaPager pager = getSearchManager().getCriteriaPager();
            pager.setCriteria(criteria);
            listDB = (List) pager.getPageElements();
        } else {
            try {
                listDB = criteria.list();
            } catch (HibernateException e) {
                setError(e);
            }
        }
        return listDB;
    }

    /**
	 * esegue le operazioni per la gestione dell'errore
	 * 
	 * @param e
	 *            Exception
	 */
    private void setError(Exception exception) {
        setRollBackOnly();
        LOG.error(exception.getMessage());
        addActionError(exception.getMessage());
    }

    /**
	 * getter categorie list
	 */
    public List getListForm() {
        if (listForm == null) {
            listForm = new java.util.ArrayList();
        }
        return listForm;
    }

    /**
	 * setter categorie list
	 * 
	 * @param listForm
	 *            categorie list
	 */
    public void setListForm(List listForm) {
        this.listForm = listForm;
    }

    /**
	 * getter categorie
	 */
    public Categorie getForm() {
        if (dataForm == null) {
            dataForm = new Categorie();
        }
        return dataForm;
    }

    /**
	 * setter categorie
	 */
    public void setForm(Categorie dataForm) {
        this.dataForm = dataForm;
    }

    /**
	 * getter id
	 */
    public java.lang.Integer getIdcate() {
        return idcate;
    }

    /**
	 * setter id
	 * 
	 * @param idcate
	 */
    public void setIdcate(java.lang.Integer idcate) {
        this.idcate = idcate;
    }
}
