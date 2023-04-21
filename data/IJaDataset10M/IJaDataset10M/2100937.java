package org.skins.web.struts.plugin;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import org.skins.core.config.Form;
import org.skins.core.config.XmlBeanElement;
import org.skins.core.config.XmlEntryElement;
import org.skins.core.config.XmlPropertyElement;
import org.skins.logger.CrpArchLogger;
import org.xml.sax.InputSource;

public class CRPStrutsPlugin implements PlugIn {

    public static final String PLUGIN_NAME = CRPStrutsPlugin.class.getName();

    private static Map formsData = new Hashtable();

    private static Map actionData = new Hashtable();

    private String castorMapping = "/it/crp/crparch/web/config/etc/castor/config.cm.xml";

    private String formsXmlConfigurator;

    private Logger log = CrpArchLogger.getLogger();

    public String getFormsXmlConfigurator() {
        return formsXmlConfigurator;
    }

    public void setFormsXmlConfigurator(String formsXmlConfigurator) {
        this.formsXmlConfigurator = formsXmlConfigurator;
    }

    public void destroy() {
    }

    public void init(ActionServlet actionServlet, ModuleConfig moduleConfig) throws ServletException {
        log.info("[CRPStrutsPlugin::init] BEGIN");
        InputStream input = CRPStrutsPlugin.class.getResourceAsStream(getCastorMapping());
        InputStream inXmlConfig = CRPStrutsPlugin.class.getResourceAsStream(getFormsXmlConfigurator());
        Mapping mapping = new Mapping();
        mapping.loadMapping(new InputSource(input));
        Unmarshaller unmarshaller = new Unmarshaller(ArrayList.class);
        try {
            unmarshaller.setMapping(mapping);
            List forms = (List) unmarshaller.unmarshal(new InputSource(inXmlConfig));
            for (Iterator iter = forms.iterator(); iter.hasNext(); ) {
                Object obj = iter.next();
                if (obj instanceof Form) {
                    Form form = (Form) obj;
                    if (form.getName() == null && form.getPath() == null) {
                        throw new ServletException("Obbligatorio uno fra gli attibuti path e name");
                    }
                    formsData.put(form.getName() == null ? form.getPath() : form.getName(), form);
                }
                if (obj instanceof XmlBeanElement) {
                    XmlBeanElement action = (XmlBeanElement) obj;
                    actionData.put(action.getPath(), action);
                }
            }
        } catch (Exception e) {
            log.error("[CRPStrutsPlugin::init] " + e.getMessage(), e);
            throw new ServletException("Errore nella lettura del file di configurazione. " + e.getMessage());
        }
        actionServlet.getServletContext().setAttribute(CRPStrutsPlugin.PLUGIN_NAME, this);
        log.info("[CRPStrutsPlugin::init] END");
    }

    public Object getService(String name) {
        try {
            Form form = (Form) formsData.get(name);
            Object service = Class.forName(form.getService().getClassName()).newInstance();
            initProperties(service, form.getService());
            return service;
        } catch (Exception e) {
            return null;
        }
    }

    public Object getValidator(String name) {
        try {
            Form form = (Form) formsData.get(name);
            Object validator = Class.forName(form.getValidator().getClassName()).newInstance();
            return validator;
        } catch (Exception e) {
            return null;
        }
    }

    static Map getActionData() {
        return actionData;
    }

    static Map getFormsData() {
        return formsData;
    }

    public void initBeanProperties(String path, Object bean) throws SecurityException, NoSuchMethodException, RuntimeException, IllegalAccessException, InvocationTargetException {
        log.info("[CRPStrutsPlugin::initBeanProperties()] BEGIN");
        XmlBeanElement beanAction = (XmlBeanElement) actionData.get(path);
        log.debug("[CRPStrutsPlugin::initBeanProperties] Init delle properties.");
        log.debug("[CRPStrutsPlugin::initBeanProperties] Path: " + path);
        log.debug("[CRPStrutsPlugin::initBeanProperties] Bean: " + bean);
        initProperties(bean, beanAction);
        log.info("[CRPStrutsPlugin::initBeanProperties()] END");
    }

    /**
	 * Inizializza le property dell'oggetto bean sulla base del file xml di configurazione
	 * @param bean il bean che va valorizzato
	 * @param config oggetto cone le informazioni di inizializzazione
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
    private void initProperties(Object bean, Object config) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Collection props = (Collection) config.getClass().getMethod("getProperties", null).invoke(config, null);
        for (Iterator iter = props.iterator(); iter.hasNext(); ) {
            XmlPropertyElement property = (XmlPropertyElement) iter.next();
            String propertyName = StringUtils.capitalize(property.getName());
            Collection entryMap = property.getEntryMap();
            if (entryMap != null && entryMap.size() != 0) {
                Map map = new HashMap();
                for (Iterator iterator = entryMap.iterator(); iterator.hasNext(); ) {
                    XmlEntryElement entry = (XmlEntryElement) iterator.next();
                    map.put(entry.getKey(), entry.getValue());
                }
                try {
                    Method m = bean.getClass().getMethod("set" + propertyName, new Class[] { Map.class });
                    m.invoke(bean, new Object[] { map });
                } catch (Exception e) {
                }
            }
            String value = property.getValue();
            if (value != null) {
                try {
                    Method m = bean.getClass().getMethod("set" + propertyName, new Class[] { String.class });
                    m.invoke(bean, new String[] { value });
                } catch (Exception e) {
                }
            }
        }
    }

    public Object getInitializer(String name) {
        try {
            Form form = (Form) formsData.get(name);
            Object initActionForm = Class.forName(form.getInitForm().getClassName()).newInstance();
            return initActionForm;
        } catch (Exception e) {
            return null;
        }
    }

    public String getCastorMapping() {
        return castorMapping;
    }
}
