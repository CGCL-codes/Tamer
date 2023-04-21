package org.jaffa.modules.printing.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.apache.log4j.Logger;
import org.jaffa.exceptions.ApplicationException;
import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.modules.printing.services.exceptions.DataNotFoundException;
import org.jaffa.modules.printing.services.exceptions.EngineProcessingException;
import org.jaffa.persistence.UOW;

/** This beam implements the IDataBean interface and is used to wrapper any
 * domain object for use as the root node of a DataBean for form printing.
 * <p>
 * It is constructed via the DomainDataBeanFactory, via the AbstractDataBeanFactory
 *
 * @author PaulE
 * @version 1.0
 */
public class DomainDataBean implements IDataBean {

    private static final Logger log = Logger.getLogger(DomainDataBean.class);

    private Class m_beanClass = null;

    private List m_keys = null;

    private Object m_domainObject = null;

    /** Creates a new instance of DomainDataBean */
    DomainDataBean(Class domainClass, List keys) {
        m_beanClass = domainClass;
        m_keys = keys;
    }

    public void populate() throws FrameworkException, ApplicationExceptions {
        log.debug("Get the findByPK method for " + m_beanClass.getName());
        try {
            for (Method method : m_beanClass.getDeclaredMethods()) {
                if (method.getName().equals("findByPK") && method.getParameterTypes()[0].equals(UOW.class) && method.getParameterTypes().length == m_keys.size() + 1) {
                    log.debug("Found Method : " + method.toString());
                    Object[] args = new Object[method.getParameterTypes().length];
                    args[0] = (UOW) null;
                    int argCount = 1;
                    for (Object arg : m_keys) {
                        log.debug("    Argument " + argCount + " = " + arg);
                        args[argCount++] = arg;
                    }
                    try {
                        m_domainObject = method.invoke(null, args);
                        log.debug("Found Domain Object : " + m_domainObject);
                    } catch (IllegalAccessException e) {
                        log.error("Can't invoke " + method.toString(), e);
                        throw new EngineProcessingException(e.getMessage(), e);
                    } catch (InvocationTargetException e) {
                        log.error("Can't invoke " + method.toString(), e);
                        throw new EngineProcessingException(e.getMessage(), e);
                    }
                    if (m_domainObject == null) {
                        throw new DataNotFoundException(new String[] { "No Method", m_beanClass.getName() });
                    }
                    return;
                }
            }
            throw new DataNotFoundException("No findByPK method");
        } catch (ApplicationException e) {
            throw new ApplicationExceptions(e);
        }
    }

    public Object getDocumentRoot() {
        return m_domainObject;
    }

    public String getFormAlternateName() {
        return null;
    }

    public DocumentPrintedListener getDocumentPrintedListener() {
        return null;
    }
}
