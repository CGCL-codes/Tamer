package org.jaffa.modules.printing.services;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.jaffa.exceptions.ApplicationException;
import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.modules.printing.services.exceptions.EngineProcessingException;

/**
 * The problem with this class is that we don't know what the order of
 * the keys to the domain object are as all we get from the findByPK method
 * is the classes. We have to assume then if the object has multiple key fields,
 * the setKey() method is called in the order of the keys. We in effect are ignoring
 * the name on the Key, and relying on the order they are passed.
 *
 * @author PaulE
 */
public class DomainDataBeanFactory implements IDataBeanFactory {

    /** Logger reference */
    private static final Logger log = Logger.getLogger(FormProcessor.class);

    private Class m_beanClass = null;

    private List m_keys = new ArrayList();

    private Object additionalDataObject = null;

    public void setBeanClass(Class beanClass) {
        m_beanClass = beanClass;
    }

    public void setKey(String key, String value) {
        m_keys.add(value);
    }

    public void setAdditionalDataObject(java.lang.Object additionalDataObject) {
        this.additionalDataObject = additionalDataObject;
    }

    public IDataBean getInstance() throws FrameworkException, ApplicationExceptions {
        IDataBean dataBean = null;
        log.debug("Created a DomainDataBean to wrapper " + m_beanClass);
        dataBean = (IDataBean) new DomainDataBean(m_beanClass, m_keys);
        if (dataBean == null) throw new EngineProcessingException("No DomainDataBean created");
        dataBean.populate();
        return dataBean;
    }
}
