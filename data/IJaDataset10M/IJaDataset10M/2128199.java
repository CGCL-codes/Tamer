package javango.contrib.admin.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javango.contrib.admin.MetaOptions;
import javango.contrib.hibernate.HibernateUtil;
import javango.db.Manager;
import javango.db.Managers;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import com.google.inject.Inject;

/**
 * Default implementation of admin options tha allows all known hibernate entities to be managed.  
 * 
 * @author johns
 *
 */
public class DefaultAdminOptions implements AdminOptions {

    List<Class> classes = new ArrayList<Class>();

    HibernateUtil hibernateUtil;

    Managers managers;

    @Inject
    public DefaultAdminOptions(Managers managers, HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
        this.managers = managers;
        Configuration cfg = hibernateUtil.getConfiguration();
        Iterator<PersistentClass> i = cfg.getClassMappings();
        while (i.hasNext()) {
            classes.add(i.next().getMappedClass());
        }
    }

    /**
	 * Return an iterator of all available class mappings
	 * @return
	 */
    public Iterator<Class> getClassMappings() {
        return classes.iterator();
    }

    /**
	 * Return the class mapping for the specified class
	 * @return
	 */
    public Class getClassMapping(String entityName) {
        Configuration cfg = hibernateUtil.getConfiguration();
        return cfg.getClassMapping(entityName).getMappedClass();
    }

    public ModelAdmin getModelAdmin(Class model) {
        BaseModelAdmin ma = new BaseModelAdmin();
        ma.setFields(getFieldList(model));
        ma.setListDisplay(ma.getFields());
        ma.setManager(getManager(model));
        return ma;
    }

    private Manager<?> getManager(Class entityClass) {
        return managers.forClass(entityClass);
    }

    private String[] getFieldList(Class clazz) {
        List<String> property_list = new ArrayList<String>();
        Iterator<Property> i = hibernateUtil.getConfiguration().getClassMapping(clazz.getName()).getPropertyIterator();
        while (i.hasNext()) {
            Property p = i.next();
            property_list.add(p.getName());
        }
        return property_list.toArray(new String[] {});
    }
}
