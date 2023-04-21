package com.liferay.portal.service;

/**
 * <a href="PhoneLocalService.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This interface defines the service. The default implementation is <code>com.liferay.portal.service.impl.PhoneLocalServiceImpl</code>.
 * Modify methods in that class and rerun ServiceBuilder to populate this class
 * and all other generated classes.
 * </p>
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be accessed
 * from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portal.service.PhoneServiceFactory
 * @see com.liferay.portal.service.PhoneServiceUtil
 *
 */
public interface PhoneLocalService {

    public java.util.List dynamicQuery(com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer) throws com.liferay.portal.SystemException;

    public java.util.List dynamicQuery(com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer, int begin, int end) throws com.liferay.portal.SystemException;

    public com.liferay.portal.service.persistence.AccountPersistence getAccountPersistence();

    public void setAccountPersistence(com.liferay.portal.service.persistence.AccountPersistence accountPersistence);

    public com.liferay.portal.service.persistence.AddressPersistence getAddressPersistence();

    public void setAddressPersistence(com.liferay.portal.service.persistence.AddressPersistence addressPersistence);

    public com.liferay.portal.service.persistence.ClassNamePersistence getClassNamePersistence();

    public void setClassNamePersistence(com.liferay.portal.service.persistence.ClassNamePersistence classNamePersistence);

    public com.liferay.portal.service.persistence.CompanyPersistence getCompanyPersistence();

    public void setCompanyPersistence(com.liferay.portal.service.persistence.CompanyPersistence companyPersistence);

    public com.liferay.portal.service.persistence.ContactPersistence getContactPersistence();

    public void setContactPersistence(com.liferay.portal.service.persistence.ContactPersistence contactPersistence);

    public com.liferay.portal.service.persistence.CountryPersistence getCountryPersistence();

    public void setCountryPersistence(com.liferay.portal.service.persistence.CountryPersistence countryPersistence);

    public com.liferay.portal.service.persistence.EmailAddressPersistence getEmailAddressPersistence();

    public void setEmailAddressPersistence(com.liferay.portal.service.persistence.EmailAddressPersistence emailAddressPersistence);

    public com.liferay.portal.service.persistence.GroupPersistence getGroupPersistence();

    public void setGroupPersistence(com.liferay.portal.service.persistence.GroupPersistence groupPersistence);

    public com.liferay.portal.service.persistence.ImagePersistence getImagePersistence();

    public void setImagePersistence(com.liferay.portal.service.persistence.ImagePersistence imagePersistence);

    public com.liferay.portal.service.persistence.LayoutPersistence getLayoutPersistence();

    public void setLayoutPersistence(com.liferay.portal.service.persistence.LayoutPersistence layoutPersistence);

    public com.liferay.portal.service.persistence.LayoutSetPersistence getLayoutSetPersistence();

    public void setLayoutSetPersistence(com.liferay.portal.service.persistence.LayoutSetPersistence layoutSetPersistence);

    public com.liferay.portal.service.persistence.ListTypePersistence getListTypePersistence();

    public void setListTypePersistence(com.liferay.portal.service.persistence.ListTypePersistence listTypePersistence);

    public com.liferay.portal.service.persistence.MembershipRequestPersistence getMembershipRequestPersistence();

    public void setMembershipRequestPersistence(com.liferay.portal.service.persistence.MembershipRequestPersistence membershipRequestPersistence);

    public com.liferay.portal.service.persistence.OrganizationPersistence getOrganizationPersistence();

    public void setOrganizationPersistence(com.liferay.portal.service.persistence.OrganizationPersistence organizationPersistence);

    public com.liferay.portal.service.persistence.OrgGroupPermissionPersistence getOrgGroupPermissionPersistence();

    public void setOrgGroupPermissionPersistence(com.liferay.portal.service.persistence.OrgGroupPermissionPersistence orgGroupPermissionPersistence);

    public com.liferay.portal.service.persistence.OrgGroupRolePersistence getOrgGroupRolePersistence();

    public void setOrgGroupRolePersistence(com.liferay.portal.service.persistence.OrgGroupRolePersistence orgGroupRolePersistence);

    public com.liferay.portal.service.persistence.OrgLaborPersistence getOrgLaborPersistence();

    public void setOrgLaborPersistence(com.liferay.portal.service.persistence.OrgLaborPersistence orgLaborPersistence);

    public com.liferay.portal.service.persistence.PasswordPolicyPersistence getPasswordPolicyPersistence();

    public void setPasswordPolicyPersistence(com.liferay.portal.service.persistence.PasswordPolicyPersistence passwordPolicyPersistence);

    public com.liferay.portal.service.persistence.PasswordPolicyRelPersistence getPasswordPolicyRelPersistence();

    public void setPasswordPolicyRelPersistence(com.liferay.portal.service.persistence.PasswordPolicyRelPersistence passwordPolicyRelPersistence);

    public com.liferay.portal.service.persistence.PasswordTrackerPersistence getPasswordTrackerPersistence();

    public void setPasswordTrackerPersistence(com.liferay.portal.service.persistence.PasswordTrackerPersistence passwordTrackerPersistence);

    public com.liferay.portal.service.persistence.PermissionPersistence getPermissionPersistence();

    public void setPermissionPersistence(com.liferay.portal.service.persistence.PermissionPersistence permissionPersistence);

    public com.liferay.portal.service.persistence.PhonePersistence getPhonePersistence();

    public void setPhonePersistence(com.liferay.portal.service.persistence.PhonePersistence phonePersistence);

    public com.liferay.portal.service.persistence.PluginSettingPersistence getPluginSettingPersistence();

    public void setPluginSettingPersistence(com.liferay.portal.service.persistence.PluginSettingPersistence pluginSettingPersistence);

    public com.liferay.portal.service.persistence.PortletPersistence getPortletPersistence();

    public void setPortletPersistence(com.liferay.portal.service.persistence.PortletPersistence portletPersistence);

    public com.liferay.portal.service.persistence.PortletPreferencesPersistence getPortletPreferencesPersistence();

    public void setPortletPreferencesPersistence(com.liferay.portal.service.persistence.PortletPreferencesPersistence portletPreferencesPersistence);

    public com.liferay.portal.service.persistence.RegionPersistence getRegionPersistence();

    public void setRegionPersistence(com.liferay.portal.service.persistence.RegionPersistence regionPersistence);

    public com.liferay.portal.service.persistence.ReleasePersistence getReleasePersistence();

    public void setReleasePersistence(com.liferay.portal.service.persistence.ReleasePersistence releasePersistence);

    public com.liferay.portal.service.persistence.ResourcePersistence getResourcePersistence();

    public void setResourcePersistence(com.liferay.portal.service.persistence.ResourcePersistence resourcePersistence);

    public com.liferay.portal.service.persistence.ResourceCodePersistence getResourceCodePersistence();

    public void setResourceCodePersistence(com.liferay.portal.service.persistence.ResourceCodePersistence resourceCodePersistence);

    public com.liferay.portal.service.persistence.RolePersistence getRolePersistence();

    public void setRolePersistence(com.liferay.portal.service.persistence.RolePersistence rolePersistence);

    public com.liferay.portal.service.persistence.ServiceComponentPersistence getServiceComponentPersistence();

    public void setServiceComponentPersistence(com.liferay.portal.service.persistence.ServiceComponentPersistence serviceComponentPersistence);

    public com.liferay.portal.service.persistence.SubscriptionPersistence getSubscriptionPersistence();

    public void setSubscriptionPersistence(com.liferay.portal.service.persistence.SubscriptionPersistence subscriptionPersistence);

    public com.liferay.portal.service.persistence.UserPersistence getUserPersistence();

    public void setUserPersistence(com.liferay.portal.service.persistence.UserPersistence userPersistence);

    public com.liferay.portal.service.persistence.UserGroupPersistence getUserGroupPersistence();

    public void setUserGroupPersistence(com.liferay.portal.service.persistence.UserGroupPersistence userGroupPersistence);

    public com.liferay.portal.service.persistence.UserGroupRolePersistence getUserGroupRolePersistence();

    public void setUserGroupRolePersistence(com.liferay.portal.service.persistence.UserGroupRolePersistence userGroupRolePersistence);

    public com.liferay.portal.service.persistence.UserIdMapperPersistence getUserIdMapperPersistence();

    public void setUserIdMapperPersistence(com.liferay.portal.service.persistence.UserIdMapperPersistence userIdMapperPersistence);

    public com.liferay.portal.service.persistence.UserTrackerPersistence getUserTrackerPersistence();

    public void setUserTrackerPersistence(com.liferay.portal.service.persistence.UserTrackerPersistence userTrackerPersistence);

    public com.liferay.portal.service.persistence.UserTrackerPathPersistence getUserTrackerPathPersistence();

    public void setUserTrackerPathPersistence(com.liferay.portal.service.persistence.UserTrackerPathPersistence userTrackerPathPersistence);

    public com.liferay.portal.service.persistence.WebsitePersistence getWebsitePersistence();

    public void setWebsitePersistence(com.liferay.portal.service.persistence.WebsitePersistence websitePersistence);

    public void afterPropertiesSet();

    public com.liferay.portal.model.Phone addPhone(long userId, java.lang.String className, long classPK, java.lang.String number, java.lang.String extension, int typeId, boolean primary) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public void deletePhone(long phoneId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public void deletePhones(long companyId, java.lang.String className, long classPK) throws com.liferay.portal.SystemException;

    public com.liferay.portal.model.Phone getPhone(long phoneId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public java.util.List getPhones() throws com.liferay.portal.SystemException;

    public java.util.List getPhones(long companyId, java.lang.String className, long classPK) throws com.liferay.portal.SystemException;

    public com.liferay.portal.model.Phone updatePhone(long phoneId, java.lang.String number, java.lang.String extension, int typeId, boolean primary) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;
}
