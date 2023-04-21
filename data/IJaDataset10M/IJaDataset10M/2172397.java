package com.systop.common.modules.security.acegi.acl.creator;

import java.util.ArrayList;
import java.util.List;
import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.acl.basic.AclObjectIdentity;
import org.acegisecurity.acl.basic.NamedEntityObjectIdentity;
import org.acegisecurity.acl.basic.SimpleAclEntry;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;
import com.systop.common.modules.security.acegi.acl.AclUtils;
import com.systop.common.modules.security.acegi.acl.domain.AclDomainAware;

/**
 * 简单的creator实现，给同一角色的人赋予可读权限，本人管理权限。
 * 
 * @author sshwsfc@gmail.com
 */
@SuppressWarnings("unchecked")
public class SimpleAclCreator extends AbstractBasicAclCreator {

    /**
   * 可读权限
   */
    private Integer defaultRoleMask = SimpleAclEntry.READ;

    /**
   * Creator 管理权限
   */
    private Integer defaultCreatorMask = SimpleAclEntry.ADMINISTRATION;

    /**
   * 缺省的Domain类
   */
    protected Class processDomainObjectClass = AclDomainAware.class;

    /**
   * 返回类型为null
   */
    protected Class processReturnedObjectClass = null;

    /**
   * Acl对象标识的类
   */
    protected Class defaultAclObjectIdentityClass = NamedEntityObjectIdentity.class;

    public Integer getDefaultCreatorMask() {
        return defaultCreatorMask;
    }

    public void setDefaultCreatorMask(Integer defaultCreatorMask) {
        this.defaultCreatorMask = defaultCreatorMask;
    }

    public Integer getDefaultRoleMask() {
        return defaultRoleMask;
    }

    public void setDefaultRoleMask(Integer defaultRoleMask) {
        this.defaultRoleMask = defaultRoleMask;
    }

    /**
   * @see com.systop.common.security.acegi.acl.AclCreator#
   *   creat(Authentication, Object, ConfigAttributeDefinition, Object)
   */
    public void creat(Authentication authentication, Object domainInstance, ConfigAttributeDefinition config, Object returnedObject) {
        SimpleAclEntry[] acls = getEntry(authentication, domainInstance, returnedObject);
        try {
            for (int i = 0; i < acls.length; i++) {
                aclDAO.create(acls[i]);
            }
        } catch (DataAccessException e) {
            logger.warn("parent not found");
        }
    }

    /**
   * 返回父对象的标识
   */
    protected AclObjectIdentity getParentAclIdentity(Object domainInstance) {
        return null;
    }

    /**
   * 创建一个AclEntry对象
   * @param authentication 认证信息
   * @param domainInstance Domain Object Instance
   * @param returnedObject 返回对象
   * @return
   */
    protected SimpleAclEntry[] getEntry(Authentication authentication, Object domainInstance, Object returnedObject) {
        AclObjectIdentity aclIdentity = AclUtils.obtainIdentity(domainInstance, defaultAclObjectIdentityClass);
        Assert.notNull(aclIdentity, "domainInstance is not supported by this creator");
        AclObjectIdentity parentAclIdentity = getParentAclIdentity(domainInstance);
        GrantedAuthority[] authoritys = authentication.getAuthorities();
        List<SimpleAclEntry> list = new ArrayList<SimpleAclEntry>();
        for (int i = 0; i < authoritys.length; i++) {
            GrantedAuthority auth = authoritys[i];
            if (auth.getAuthority().startsWith("ACL_")) {
                SimpleAclEntry aclEntry = new SimpleAclEntry(auth.getAuthority(), aclIdentity, parentAclIdentity, defaultRoleMask.intValue());
                list.add(aclEntry);
            }
        }
        if (authentication.getPrincipal() != null) {
            SimpleAclEntry aclEntry = new SimpleAclEntry(authentication.getPrincipal().toString(), aclIdentity, parentAclIdentity, defaultCreatorMask.intValue());
            list.add(aclEntry);
        }
        return list.toArray(new SimpleAclEntry[list.size()]);
    }

    /**
   * @see com.systop.common.security.acegi.acl.AclCreator#supports(Class)
   */
    public boolean supports(Object domainObject, Object returnedObject) {
        return ((domainObject == null && processDomainObjectClass == null) || this.processDomainObjectClass.isAssignableFrom(domainObject.getClass())) && ((returnedObject == null && processReturnedObjectClass == null) || this.processReturnedObjectClass.isAssignableFrom(returnedObject.getClass()));
    }
}
