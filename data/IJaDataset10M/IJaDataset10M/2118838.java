package com.webmotix.core;

import java.util.Hashtable;
import java.util.Map;
import javax.jcr.PropertyType;
import javax.jcr.Session;
import javax.jcr.Value;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.webmotix.dao.ParameterDAO;
import com.webmotix.repository.RepositoryHelper;

public final class SystemParameter {

    private static final Logger log = LoggerFactory.getLogger(SystemParameter.class);

    public static final String WEBMOTIX_AUDIT_ENABLE = "motix.audit.enable";

    public static final String WEBMOTIX_UPLOAD_MAXFILESIZE = "motix.upload.maxfilesite";

    public static final String WEBMOTIX_TEMPLATE_ROOTDIR = "motix.template.rootdir";

    public static final String WEBMOTIX_WEB_CACHE_ENABLE = "webmotix.web.cache.enable";

    public static final String WEBMOTIX_WEB_CONTEXT = "webmotix.web.context";

    public static final String WEBMOTIX_WEB_CACHE_TIMEOUT = "webmotix.web.cache.timeout";

    public static final String WEBMOTIX_WEB_CACHE_PATH = "webmotix.web.cache.path";

    public static final String WEBMOTIX_RSS_MAXSIZE = "webmotix.rss.maxsize";

    public static final String WEBMOTIX_WEB_SESSION_EXPIRE = "webmotix.web.session.expire";

    public static final String WEBMOTIX_SMTP_AUTHENTICATION = "motix.smtp.authentication";

    public static final String WEBMOTIX_SMTP_SERVER = "motix.smtp.server";

    public static final String WEBMOTIX_SMTP_SENDER = "motix.smtp.sender";

    public static final String WEBMOTIX_SMTP_USER = "motix.smtp.user";

    public static final String WEBMOTIX_SMTP_PASSWORD = "motix.smtp.password";

    public static final String WEBMOTIX_CONTENT_LIST_MODE = "motix.content.list.mode";

    public static final String WEBMOTIX_IMAGE_TOP = "motix.image.top";

    public static final String WEBMOTIX_IMAGE_LOGIN = "motix.image.login";

    public static final String WEBMOTIX_OPEN_PANEL = "motix.open.panel";

    public static final String WEBMOTIX_BOLETO = "webmotix.boleto";

    public static final String WEBMOTIX_NEWSLETTER_QUEUE_SIZE = "motix.newsletter.queue.size";

    public static final String WEBMOTIX_CAPCTHA = "motix.captcha";

    public static final String WEBMOTIX_RSS_SLEEP = "webmotix.rss.sleep";

    public static final String WEBMOTIX_WEB_CACHE_MAX_PAGES = "webmotix.web.cache.max.pages";

    public static final String WEBMOTIX_WEB_CACHE_MAX_SLEEP = "webmotix.web.cache.max.sleep";

    private static final Map<String, String> properties = new Hashtable<String, String>();

    /**
     * Classe utilit�ria, n�o instanciar.
     */
    private SystemParameter() {
    }

    /**
     * @param name
     * @param value
     */
    public static void setProperty(final String name, final String value) {
        SystemParameter.properties.put(name, value);
    }

    /**
     * @param name
     */
    public static String getProperty(final String name) {
        return SystemParameter.properties.get(name);
    }

    /**
     * @param name
     * @param defaultValue
     */
    public static String getProperty(final String name, final String defaultValue) {
        final String value = getProperty(name);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        return value;
    }

    /**
     *
     */
    public static Map<String, String> getPropertyList() {
        return SystemParameter.properties;
    }

    public static synchronized void reload(final String name, final String value) {
        properties.remove(name);
        properties.put(name, value);
    }

    public static synchronized void reload() {
        Session session = null;
        log.info("CARREGANDO PARAMETROS:");
        try {
            properties.clear();
            session = RepositoryHelper.getSession(SystemProperty.getProperty(SystemProperty.WEBMOTIX_CONNECTION_JCR_WORKSPACE));
            final ParameterDAO[] parameters = ParameterDAO.findAll(session);
            for (int i = 0; i < parameters.length; i++) {
                final ParameterDAO parameterDAO = parameters[i];
                final String name = parameterDAO.getName();
                final Value pValue = parameterDAO.getNode().getProperty(MotixNodeTypes.NS_PROPERTY + ":value").getValue();
                if (pValue != null && pValue.getType() == PropertyType.BINARY) {
                    final String value = parameterDAO.getUUID();
                    properties.put(name, value);
                    log.info(name + " = " + value);
                } else {
                    final String value = StringUtils.defaultIfEmpty(parameterDAO.getValue(), StringUtils.EMPTY);
                    properties.put(name, value);
                    log.info(name + " = " + value);
                }
            }
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (session != null) {
                session.logout();
            }
            session = null;
        }
    }
}
