package com.yeah.sql;

import com.yeah.models.Picture;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 *
 * @author kaarel
 */
public class PictureDAO implements DaoTemplate {

    private HibernateTemplate hibernateTemplate;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    public Object getItemById(int id) {
        return (Picture) hibernateTemplate.find("from Picture " + "Where picID='" + id + "'");
    }

    @Override
    public List getAllItems() {
        List<Picture> albums = null;
        albums = hibernateTemplate.find("from Picture");
        return albums;
    }

    @Override
    public List getItemByQuery(String query) {
        List<Picture> foundUsers = null;
        foundUsers = hibernateTemplate.find(query);
        return foundUsers;
    }

    @Override
    public void deleteItem(Object o) {
        Picture deleteUser = (Picture) o;
        hibernateTemplate.delete(deleteUser);
    }

    @Override
    public void saveItem(Object o) {
        Picture save = (Picture) o;
        hibernateTemplate.save(save);
    }

    @Override
    public void updateItem(Object o) {
        Picture update = (Picture) o;
        hibernateTemplate.update(update);
    }
}
