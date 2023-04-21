package cn.jsprun.service.forumsedit;

import java.util.List;
import cn.jsprun.dao.TypeoptionsDao;
import cn.jsprun.domain.Typeoptions;
import cn.jsprun.utils.BeanFactory;

public class TypeoptionService {

    public boolean addTypeoption(Typeoptions typeoption) {
        return ((TypeoptionsDao) BeanFactory.getBean("typeoptionsDao")).addTypeoption(typeoption);
    }

    public boolean removeTypeoption(Typeoptions typeoption) {
        return ((TypeoptionsDao) BeanFactory.getBean("typeoptionsDao")).removeTypeoption(typeoption);
    }

    public boolean updateTypeoption(Typeoptions typeoption) {
        return ((TypeoptionsDao) BeanFactory.getBean("typeoptionsDao")).updateTypeoption(typeoption);
    }

    public Typeoptions findById(Short optionid) {
        return ((TypeoptionsDao) BeanFactory.getBean("typeoptionsDao")).findById(optionid);
    }

    public List<Typeoptions> findByClassId(Short classid) {
        return ((TypeoptionsDao) BeanFactory.getBean("typeoptionsDao")).findByClassId(classid);
    }

    public Typeoptions findByIdentifier(String identifier) {
        return ((TypeoptionsDao) BeanFactory.getBean("typeoptionsDao")).findByIdentifier(identifier);
    }

    public List<Typeoptions> findByProperty(String propertyName, Object value) {
        return ((TypeoptionsDao) BeanFactory.getBean("typeoptionsDao")).findByProperty(propertyName, value);
    }

    public List<Typeoptions> findAll() {
        return ((TypeoptionsDao) BeanFactory.getBean("typeoptionsDao")).findAll();
    }
}
