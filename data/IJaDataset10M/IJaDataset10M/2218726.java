package com.systop.common.modules.security.acegi.acl.afterinvocation;

import java.util.Iterator;
import org.acegisecurity.AcegiMessageSource;
import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.acl.basic.AclObjectIdentity;
import org.acegisecurity.acl.basic.BasicAclExtendedDao;
import org.acegisecurity.afterinvocation.AfterInvocationProvider;
import org.acegisecurity.afterinvocation.BasicAclEntryAfterInvocationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;
import com.systop.common.modules.security.acegi.acl.AclUtils;
import com.systop.common.modules.security.acegi.acl.domain.AclDomainBuilder;

/**
 * 当有acl对象被删除的时候，需要删除该对象对应的acl信息。
 * 
 * @author sshwsfc@gmail.com
 * @see AfterInvocationProvider
 */
@SuppressWarnings("unchecked")
public class DeleteAclEntryAfterInvocationProvider implements AfterInvocationProvider, InitializingBean, MessageSourceAware {

    /**
   * Log for the class
   */
    private static Logger logger = LoggerFactory.getLogger(BasicAclEntryAfterInvocationProvider.class);

    /**
   * Message
   */
    protected MessageSourceAccessor messages = AcegiMessageSource.getAccessor();

    /**
   * 权限
   */
    private String processConfigAttribute = "AFTER_ACL_DELETE";

    /**
   * ACL DAO
   */
    private BasicAclExtendedDao aclDAO;

    /**
   * 域对象的class
   */
    private Class domainClass;

    /**
   * 方法名称
   */
    private String methodDomainObjectClass;

    public Class getDomainClass() {
        return domainClass;
    }

    public void setDomainClass(Class domainClass) {
        this.domainClass = domainClass;
    }

    public String getMethodDomainObjectClass() {
        return methodDomainObjectClass;
    }

    public void setMethodDomainObjectClass(String methodDomainObjectClass) {
        this.methodDomainObjectClass = methodDomainObjectClass;
    }

    public BasicAclExtendedDao getAclDAO() {
        return aclDAO;
    }

    public void setAclDAO(BasicAclExtendedDao aclDAO) {
        this.aclDAO = aclDAO;
    }

    /**
   * @see InitializingBean#afterPropertiesSet()
   */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(processConfigAttribute, "A processConfigAttribute is mandatory");
        Assert.notNull(messages, "A message source must be set");
        Assert.notNull(aclDAO, "AclExtendedDao is mandatory");
    }

    public String getProcessConfigAttribute() {
        return processConfigAttribute;
    }

    public void setProcessConfigAttribute(String processConfigAttribute) {
        this.processConfigAttribute = processConfigAttribute;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    /**
   * @see AfterInvocationProvider
   */
    public Object decide(Authentication authentication, Object object, ConfigAttributeDefinition config, Object returnedObject) {
        Iterator iter = config.getConfigAttributes();
        while (iter.hasNext()) {
            ConfigAttribute attr = (ConfigAttribute) iter.next();
            if (this.supports(attr)) {
                AclDomainBuilder domainObject = AclUtils.getDomainObjectInstance(object, methodDomainObjectClass, domainClass);
                AclObjectIdentity aclId = domainObject.getAclObjectIdentity();
                if (aclId != null) {
                    try {
                        aclDAO.delete(aclId);
                        logger.debug("Delete AclObject[{}]", aclId);
                    } catch (DataAccessException e) {
                        logger.debug("Try to Delete null AclObject[{}], Ignore It!", aclId);
                    }
                }
            }
        }
        return returnedObject;
    }

    /**
   * @see AfterInvocationProvider#supports(ConfigAttribute)
   */
    public boolean supports(ConfigAttribute attribute) {
        return (attribute.getAttribute() != null) && attribute.getAttribute().equals(getProcessConfigAttribute());
    }

    /**
   * This implementation supports any type of class, because it does not query
   * the presented secure object.
   * 
   * @param clazz the secure object
   * @return always <code>true</code>
   */
    public boolean supports(Class clazz) {
        return true;
    }
}
