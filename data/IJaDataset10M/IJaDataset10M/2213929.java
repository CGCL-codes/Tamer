package org.offseam.beans;

import java.util.Map;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.offseam.conversation.ConversationContext;
import org.offseam.conversation.ConversationManager;
import org.offseam.ioc.InjectException;
import org.offseam.util.BeanUtil;
import org.offseam.util.ComponentUtils;
import org.offseam.util.ContextManager;
import com.sun.faces.spi.InjectionProviderException;

public class ManagedBeanFactory {

    private static Log logger = LogFactory.getLog("ManagedBeanFactory");

    private static final Object[] EMPTYPARAMS = new Object[0];

    private ManagedBean managedBean;

    public ManagedBeanFactory(ManagedBean managedBean) {
        this.managedBean = managedBean;
    }

    public ManagedBean getManagedBean() {
        return managedBean;
    }

    public void setManagedBean(ManagedBean managedBean) {
        this.managedBean = managedBean;
    }

    public Object newInstance(FacesContext context) throws FacesException {
        Object result = null;
        ConversationContext cc = ConversationManager.getConversationContext();
        String cid = ContextManager.getRequestParameter(ConversationManager.conversationParamName);
        if (cid == null) {
            cid = ComponentUtils.getCoversationId();
        } else {
            if (ComponentUtils.getCoversationId() == null) {
                ComponentUtils.saveConversationId(cid);
            }
        }
        String bijectionId = ContextManager.getRequestParameter(ConversationManager.bijectionParamName);
        if (bijectionId == null) bijectionId = ComponentUtils.getBijectionId();
        if (bijectionId != null && ComponentUtils.getBijectionId() == null) {
            ComponentUtils.saveBijectionId(bijectionId);
        }
        boolean isConversation = false;
        boolean outjectionCheckNeeded = false;
        String key = null;
        if (cid != null && !ConversationManager.INVALID_ID.equals(cid)) {
            isConversation = true;
            if (managedBean.isConversationScope()) {
                key = ConversationManager.conversationPrefix + cid + "." + managedBean.getBeanAlias();
                result = cc.get(key);
                if (result == null) {
                    outjectionCheckNeeded = true;
                }
            }
        } else if (!ConversationManager.INVALID_ID.equals(bijectionId)) {
            outjectionCheckNeeded = true;
            if (managedBean.isInjectionNeeded()) {
                if (bijectionId != null) {
                    key = ConversationManager.bijectionPrefix + bijectionId + "." + managedBean.getBeanAlias();
                    result = cc.get(key);
                }
                if (result != null) {
                    cc.remove(key);
                }
            }
        }
        if (result == null) {
            result = newInstance();
        }
        if (!ConversationManager.INVALID_ID.equals(bijectionId) && managedBean.isFieldInjectionNeeded()) {
            doInjection(result, bijectionId);
        }
        if (outjectionCheckNeeded && !ConversationManager.INVALID_ID.equals(cid)) {
            doOutjection(cid, result);
        }
        return result;
    }

    /**
	 * try to inject value into fields of managedBean
	 * 
	 * @param instance :
	 *            bean to do injection
	 * @param passedId :
	 *            current conversation id or injection id <br>
	 *            passed in query parameter <br>
	 *            passedId is null when inject from page
	 * @param isConversation :
	 *            in existing conversation
	 */
    private void doInjection(Object instance, String passedId) {
        ConversationContext cc = ConversationManager.getConversationContext();
        try {
            for (BijectionProperty bj : managedBean.getInjectingProperties()) {
                String propertyName = bj.getPropertyName();
                BeanProperty mbp = managedBean.getProperties().get(propertyName);
                Object fieldValue = BeanUtil.getFieldValue(instance, mbp.getName());
                if (!bj.isRequired()) {
                    if (fieldValue != null) {
                        continue;
                    }
                }
                String name = bj.getBijectionName();
                if (name == null || name.trim().length() == 0) {
                    name = propertyName;
                }
                String conversationKey = name;
                Object passedValue = ContextManager.getManagedBeanInScopes(name);
                if (passedValue != null) {
                    if (fieldValue != null && fieldValue.equals(passedValue)) {
                        continue;
                    }
                    BeanUtil.setFieldValue(instance, propertyName, passedValue);
                    continue;
                }
                if (passedId != null) {
                    conversationKey = ConversationManager.bijectionPrefix + passedId + "." + name;
                    passedValue = cc.get(conversationKey);
                }
                if (passedValue != null) {
                    if (fieldValue != null && fieldValue.equals(passedValue)) {
                        continue;
                    }
                    BeanUtil.setFieldValue(instance, propertyName, passedValue);
                    ContextManager.putRequestBean(name, passedValue);
                    continue;
                }
                if (passedValue == null) {
                    if (bj.isRequired()) {
                        logger.warn("Cann't inject null: to " + propertyName + " of class " + managedBean.getBeanClass());
                        throw new InjectionProviderException(new InjectException(managedBean));
                    }
                    if (fieldValue == null && !mbp.isPrimitive()) {
                        ClassLoader loader = Thread.currentThread().getContextClassLoader();
                        if (loader == null) {
                            loader = FacesContext.getCurrentInstance().getClass().getClassLoader();
                        }
                        Class mbpClass = Class.forName(mbp.getClassName(), true, loader);
                        BeanUtil.setFieldValue(instance, propertyName, mbpClass.newInstance());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * outject the conversation bean at the first time it's initialized
	 * 
	 * @param managedBean
	 */
    private void doOutjection(String cidParam, Object managedBean1) {
        if (!managedBean.isConversationScope()) {
            return;
        }
        ConversationContext cc = ConversationManager.getConversationContext();
        String cid = cidParam;
        if (cid == null || cid.trim().length() == 0) {
            cid = String.valueOf(cc.generateConversationId());
            ComponentUtils.saveConversationId(cid);
        }
        String prefix = ConversationManager.conversationPrefix + cid + ".";
        String name = this.managedBean.getBeanAlias();
        cc.put(prefix + name, managedBean1);
    }

    private Object newInstance() {
        Object result = null;
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader == null) {
                loader = FacesContext.getCurrentInstance().getClass().getClassLoader();
            }
            Class mbClass = Class.forName(managedBean.getBeanClass(), true, loader);
            result = mbClass.newInstance();
        } catch (Exception ex) {
            throw new FacesException(("ManagedBean new Instance error!Bean:" + managedBean.getBeanAlias() + ",BeanClass:" + managedBean.getBeanClass()), ex);
        }
        return result;
    }
}
