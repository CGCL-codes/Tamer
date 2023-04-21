package riceSystem.dao.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import riceSystem.dao.DaoTemplateInterface;
import riceSystem.entity.Information;

@Component("informationDao")
public class InformationDaoImpl implements DaoTemplateInterface<Information> {

    private HibernateTemplate hibernateTemplate;

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    @Resource
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public void delete(Information entity) {
        Information imformation = (Information) hibernateTemplate.load(entity.getClass(), entity.getId());
        hibernateTemplate.delete(imformation);
    }

    public void deleteById(long id) {
        Information imformation = (Information) hibernateTemplate.load(Information.class, id);
        hibernateTemplate.delete(imformation);
    }

    @SuppressWarnings("unchecked")
    public List<Information> loadAll() {
        return hibernateTemplate.loadAll(Information.class);
    }

    public Information loadById(long id) {
        return (Information) hibernateTemplate.load(Information.class, id);
    }

    public void save(Information entity) {
        hibernateTemplate.save(entity);
    }

    public long update(Information entity) {
        hibernateTemplate.update(entity);
        return entity.getId();
    }
}
