package org.javanuke.news.managers;

import java.util.Collection;
import java.util.Iterator;
import net.sf.hibernate.Criteria;
import org.javanuke.core.managers.ManagerSupport;
import org.javanuke.core.model.dao.DataAccesObject;
import org.javanuke.core.model.dto.DataTransferObjectSupport;
import org.javanuke.core.model.dto.dbobjects.User;
import org.javanuke.news.dao.NewsDAO;

/**
 * @author Franklin Samir (franklin@portaljava.com)
 * Created on 22/03/2003
 */
public class NewsManager extends ManagerSupport {

    DataAccesObject dao = null;

    public NewsManager() {
        super();
        init();
    }

    /**
	 * Prepare the Manager to work properly.
	 * 
	 */
    protected void init() {
        dao = new NewsDAO();
    }

    /**
	 * Load all news from the Data Base.
	 * 
	 * @return Collection with all News
	 */
    public Collection loadAll() {
        DataAccesObject dao = new NewsDAO();
        return dao.findAll();
    }

    /**
		 * Load all news from the Data Base.
		 * 
		 * @return Collection with all News
		 */
    public Iterator loadAllAsIterator() {
        return dao.findAllAsIterator();
    }

    public Iterator findAsIterator(DataTransferObjectSupport dto) {
        Criteria criteria = null;
        return dao.findByCriteriaAsIterator(criteria);
    }

    public void save(User user) {
        dao.store(user);
    }
}
