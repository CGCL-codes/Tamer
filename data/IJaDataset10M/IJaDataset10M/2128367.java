package org.restfaces.el;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.restfaces.beans.ManagedBean;
import org.restfaces.beans.ManagedBeanRegister;
import org.restfaces.beans.ManagedBeanRegister.BeanScope;
import org.restfaces.conversation.ConversationContext;
import org.restfaces.conversation.ConversationManager;
import org.restfaces.util.ComponentUtils;
import org.restfaces.util.ContextManager;
import org.restfaces.util.FacesUtil;

/**
 * Registered conversation managedBean resolver 
 * @author Administrator
 *
 */
public class ConversationBeanELResolver extends ELResolver {

    public ConversationBeanELResolver() {
    }

    public Object getValue(ELContext context, Object base, Object property) throws ELException {
        if (base != null) {
            return null;
        }
        if (property == null) {
            String message = "null property!";
            throw new PropertyNotFoundException(message);
        }
        Object result = null;
        FacesContext facesContext = (FacesContext) context.getContext(FacesContext.class);
        ExternalContext externalContext = facesContext.getExternalContext();
        if (externalContext.getRequestMap().containsKey(property) || externalContext.getSessionMap().containsKey(property) || externalContext.getApplicationMap().containsKey(property)) {
            return null;
        }
        ManagedBeanRegister manager = ManagedBeanRegister.getInstance();
        if (null != manager) {
            if (manager.isBeanRegistered((String) property)) {
                result = manager.createAndMaybeStoreManagedBeans(facesContext, ((String) property));
            } else {
                ConversationContext cc = ConversationManager.getConversationContext(facesContext);
                String cid = ContextManager.getRequestParameter(facesContext, ConversationManager.conversationParamName);
                if (cid == null) {
                    cid = ComponentUtils.getCoversationId();
                }
                String injectionId = ContextManager.getRequestParameter(ConversationManager.bijectionParamName);
                if (injectionId == null) injectionId = ComponentUtils.getBijectionId();
                String key = null;
                if (cid != null) {
                    key = ConversationManager.conversationPrefix + cid + "." + (String) property;
                    result = cc.get(key);
                }
                if (result == null) {
                    if (injectionId != null) {
                        key = ConversationManager.bijectionPrefix + injectionId + "." + (String) property;
                        result = cc.get(key);
                        if (result != null) {
                            ManagedBean mb = manager.getNormalBean((String) property);
                            BeanScope scope = null;
                            if (mb == null) {
                                externalContext.getRequestMap().put((String) property, result);
                            } else {
                                scope = mb.getBeanScope();
                                if (scope == BeanScope.APPLICATION) {
                                    externalContext.getApplicationMap().put((String) property, result);
                                } else if (scope == BeanScope.SESSION) {
                                    externalContext.getSessionMap().put((String) property, result);
                                } else if (scope == BeanScope.REQUEST) {
                                    externalContext.getRequestMap().put((String) property, result);
                                }
                            }
                        }
                    }
                }
            }
            if (result != null) {
                context.setPropertyResolved(true);
            }
        }
        return result;
    }

    public Class<?> getType(ELContext context, Object base, Object property) throws ELException {
        if (base == null && property == null) {
            String message = "null base and property";
            throw new PropertyNotFoundException(message);
        }
        return null;
    }

    public void setValue(ELContext context, Object base, Object property, Object val) throws ELException {
        if (base == null && property == null) {
            String message = "null base and property";
            throw new PropertyNotFoundException(message);
        }
    }

    public boolean isReadOnly(ELContext context, Object base, Object property) throws ELException {
        if (base != null) {
            return false;
        }
        if (property == null) {
            String message = "null property";
            throw new PropertyNotFoundException(message);
        }
        return false;
    }

    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        if (base != null) {
            return null;
        }
        ArrayList<FeatureDescriptor> list = new ArrayList<FeatureDescriptor>();
        FacesContext facesContext = (FacesContext) context.getContext(FacesContext.class);
        ManagedBeanRegister manager = ManagedBeanRegister.getInstance();
        Map mbMap = manager.getManagedBeanRegisterMap();
        if (mbMap == null) {
            return list.iterator();
        }
        for (Iterator i = mbMap.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            String managedBeanName = (String) entry.getKey();
            ManagedBean managedBean = (ManagedBean) entry.getValue();
            if (managedBean != null) {
                list.add(FacesUtil.getFeatureDescriptor(managedBeanName, managedBeanName, managedBeanName, false, false, true, managedBean.getBeanClass(), Boolean.TRUE));
            }
        }
        return list.iterator();
    }

    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        if (base != null) {
            return null;
        }
        return Object.class;
    }
}
