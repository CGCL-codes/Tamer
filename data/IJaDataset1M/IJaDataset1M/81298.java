package lebah.portal.action;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import lebah.db.PersistenceManager;
import lebah.portal.annotation.HttpRequestParameter;
import lebah.portal.annotation.HttpSessionAttribute;
import lebah.portal.annotation.MappedCommand;
import lebah.portal.annotation.VelocityContextParameter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractBaseModule extends AjaxModule {

    private static final long serialVersionUID = 3702721438781401565L;

    private static final String CMD_PARAM = "command";

    private static final String CTX_ERROR_MESSAGES = "errorMessages";

    private static final String CTX_INFO_MESSAGES = "infoMessages";

    private static Log logger = LogFactory.getLog(AbstractBaseModule.class);

    protected PersistenceManager persistenceManager;

    protected HttpSession httpSession;

    protected String command;

    private List<String> errorMessages = null;

    private List<String> infoMessages = null;

    @Override
    public String doAction() throws Exception {
        preProcess();
        processMethodToInvoke();
        postProcess();
        processMessages();
        return getVmTemplate();
    }

    public abstract String getVmTemplate() throws Exception;

    private void preProcess() throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("processRequest()");
        }
        errorMessages = new ArrayList<String>();
        infoMessages = new ArrayList<String>();
        command = getParam(CMD_PARAM);
        Field[] fields = this.getClass().getDeclaredFields();
        if (fields != null) {
            for (Field field : fields) {
                HttpRequestParameter httpReqAnno = field.getAnnotation(HttpRequestParameter.class);
                HttpSessionAttribute httpSesAnno = field.getAnnotation(HttpSessionAttribute.class);
                if (httpReqAnno != null) {
                    field.setAccessible(true);
                    String paramKey = null;
                    if (StringUtils.isBlank(httpReqAnno.paramName())) {
                        paramKey = field.getName();
                    } else {
                        paramKey = httpReqAnno.paramName();
                    }
                    Class classType = field.getType();
                    if (logger.isDebugEnabled()) {
                        logger.debug("setting field[" + field + "] with value[" + getParam(paramKey) + "] from HttpRequest.");
                    }
                    if (classType == String.class) {
                        field.set(this, getParam(paramKey));
                    } else if (classType == Boolean.class || classType == boolean.class) {
                        field.setBoolean(this, new Boolean(getParam(paramKey)));
                    }
                }
                if (httpSesAnno != null) {
                    field.setAccessible(true);
                    Object obj = getHttpSession().getAttribute(getId() + "_" + httpSesAnno.attributeKey());
                    if (logger.isDebugEnabled()) {
                        logger.debug("setting field[" + field + "] with value[" + obj + "] from HttpSession.");
                    }
                    field.set(this, obj);
                }
            }
        }
    }

    private void processMethodToInvoke() throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("processMethodToInvoke()");
            logger.debug("command : " + command);
        }
        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method mtd : methods) {
            MappedCommand mappedCommand = mtd.getAnnotation(MappedCommand.class);
            if (mappedCommand != null && mappedCommand.command().equals(command)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("selected mtd : " + mtd);
                    logger.debug("selected mappedCommand : " + mappedCommand);
                }
                Object[] params = null;
                mtd.invoke(this, params);
                break;
            }
        }
    }

    private void postProcess() throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("postProcess()");
        }
        Field[] fields = this.getClass().getDeclaredFields();
        if (fields != null) {
            for (Field field : fields) {
                HttpSessionAttribute httpSesAnno = field.getAnnotation(HttpSessionAttribute.class);
                VelocityContextParameter velCtxAnno = field.getAnnotation(VelocityContextParameter.class);
                if (httpSesAnno != null) {
                    field.setAccessible(true);
                    if (logger.isDebugEnabled()) {
                        logger.debug("setting HttpSession with: " + httpSesAnno.attributeKey() + ", " + field.get(this));
                    }
                    getHttpSession().setAttribute(getId() + "_" + httpSesAnno.attributeKey(), field.get(this));
                }
                if (velCtxAnno != null) {
                    field.setAccessible(true);
                    if (logger.isDebugEnabled()) {
                        logger.debug("setting context with: " + velCtxAnno.paramName() + ", " + field.get(this));
                    }
                    context.remove(velCtxAnno.paramName());
                    context.put(velCtxAnno.paramName(), field.get(this));
                }
            }
        }
    }

    private void processMessages() {
        context.remove(CTX_INFO_MESSAGES);
        context.put(CTX_INFO_MESSAGES, infoMessages);
        context.remove(CTX_ERROR_MESSAGES);
        context.put(CTX_ERROR_MESSAGES, errorMessages);
    }

    private HttpSession getHttpSession() {
        if (httpSession == null) {
            httpSession = request.getSession();
        }
        return httpSession;
    }

    public PersistenceManager getPersistenceManager() {
        if (persistenceManager == null) {
            persistenceManager = new PersistenceManager();
        }
        return persistenceManager;
    }

    public void addErrorMessage(String message) {
        errorMessages.add(message);
    }

    public void addInfoMessage(String message) {
        infoMessages.add(message);
    }

    public void addErrorMessage(String message, Object[] params) {
        errorMessages.add(MessageFormat.format(message, params));
    }

    public void addInfoMessage(String message, Object[] params) {
        infoMessages.add(MessageFormat.format(message, params));
    }
}
