package net.sourceforge.offsprings.splitter;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import javax.net.ssl.SSLSocketFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ClassBeanMap<TYPE extends ApplicableClasses> implements ApplicationContextAware {

    ClassBeanMap() {
    }

    private Map<String, String> _classBeanMap;

    private String _description;

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    @SuppressWarnings("unchecked")
    public void init(List<String> beanNames) {
        Map<String, String> map = new HashMap<String, String>();
        Iterator<String> nameIterator = beanNames.iterator();
        while (nameIterator.hasNext()) {
            String beanName = nameIterator.next();
            TYPE item;
            if (applicationContext.containsBean(beanName)) {
                try {
                    item = (TYPE) applicationContext.getBean(beanName);
                    Class<?>[] classes = item.getApplicableClasses();
                    int index = 0;
                    while (index < classes.length) {
                        map.put(classes[index].getName(), beanName);
                        index++;
                    }
                } catch (RuntimeException e) {
                    throw new RuntimeException("Bean " + beanName + " cannot be cast to type of this BeanMapFactory Generic Parameter");
                }
            } else {
                throw new RuntimeException("Application Context does not contain bean named: " + beanName);
            }
        }
        _classBeanMap = map;
    }

    @SuppressWarnings("unchecked")
    public TYPE itemForClass(Class<?> classType, Class<?> originalClassType) {
        String className = classType.getName();
        Class<?> _originalClassType;
        if (originalClassType == null) {
            _originalClassType = classType;
        } else {
            _originalClassType = originalClassType;
        }
        if (classType.isArray() && applicationContext.containsBean(_classBeanMap.get(Array.class.getName()))) {
            return (TYPE) applicationContext.getBean(_classBeanMap.get(Array.class.getName()));
        } else if (_classBeanMap.containsKey(className) && applicationContext.containsBean(_classBeanMap.get(className))) {
            return (TYPE) applicationContext.getBean(_classBeanMap.get(className));
        } else if (classType.getSuperclass() != null) {
            TYPE answer = itemForClass(classType.getSuperclass(), _originalClassType);
            return answer;
        } else {
            throw new RuntimeException(_description + " of type " + _originalClassType.getName() + " not supported");
        }
    }

    private ApplicationContext applicationContext;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
