package com.liferay.mail.service;

import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.util.ClassLoaderProxy;

/**
 * <a href="AccountLocalServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author Scott Lee
 *
 */
public class AccountLocalServiceUtil {

    public static com.liferay.mail.model.Account addAccount(com.liferay.mail.model.Account account) throws com.liferay.portal.SystemException {
        return getService().addAccount(account);
    }

    public static com.liferay.mail.model.Account createAccount(long accountId) {
        return getService().createAccount(accountId);
    }

    public static void deleteAccount(long accountId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        getService().deleteAccount(accountId);
    }

    public static void deleteAccount(com.liferay.mail.model.Account account) throws com.liferay.portal.SystemException {
        getService().deleteAccount(account);
    }

    public static java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) throws com.liferay.portal.SystemException {
        return getService().dynamicQuery(dynamicQuery);
    }

    public static java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start, int end) throws com.liferay.portal.SystemException {
        return getService().dynamicQuery(dynamicQuery, start, end);
    }

    public static com.liferay.mail.model.Account getAccount(long accountId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return getService().getAccount(accountId);
    }

    public static java.util.List<com.liferay.mail.model.Account> getAccounts(int start, int end) throws com.liferay.portal.SystemException {
        return getService().getAccounts(start, end);
    }

    public static int getAccountsCount() throws com.liferay.portal.SystemException {
        return getService().getAccountsCount();
    }

    public static com.liferay.mail.model.Account updateAccount(com.liferay.mail.model.Account account) throws com.liferay.portal.SystemException {
        return getService().updateAccount(account);
    }

    public static com.liferay.mail.model.Account createMailMessageAccount(long userId, java.lang.String mailAddress, java.lang.String mailInHostName, java.lang.String mailInPort, boolean mailInSecure, java.lang.String mailOutHostName, java.lang.String mailOutPort, boolean mailOutSecure, java.lang.String password, java.lang.String username) throws com.liferay.mail.MailboxException {
        return getService().createMailMessageAccount(userId, mailAddress, mailInHostName, mailInPort, mailInSecure, mailOutHostName, mailOutPort, mailOutSecure, password, username);
    }

    public static com.liferay.mail.model.Account getAccount(long userId, java.lang.String messageAddress) throws com.liferay.mail.MailboxException {
        return getService().getAccount(userId, messageAddress);
    }

    public static com.liferay.mail.model.Account getAccount(long userId, java.lang.String type, java.lang.String messageAddress) throws com.liferay.mail.MailboxException {
        return getService().getAccount(userId, type, messageAddress);
    }

    public static java.util.List<com.liferay.mail.model.Account> getAccounts() throws com.liferay.mail.MailboxException {
        return getService().getAccounts();
    }

    public static java.util.List<com.liferay.mail.model.Account> getAccounts(long userId) throws com.liferay.mail.MailboxException {
        return getService().getAccounts(userId);
    }

    public static java.util.List<com.liferay.mail.model.Account> getAccounts(long userId, java.lang.String type) throws com.liferay.mail.MailboxException {
        return getService().getAccounts(userId, type);
    }

    public static com.liferay.mail.model.Account getPrivateMessageAccount(long companyId, java.lang.String screenName) throws com.liferay.mail.MailboxException {
        return getService().getPrivateMessageAccount(companyId, screenName);
    }

    public static com.liferay.mail.model.Account updateMailMessageAccount(long accountId, java.lang.String mailInHostName, java.lang.String mailInPort, boolean mailInSecure, java.lang.String mailOutHostName, java.lang.String mailOutPort, boolean mailOutSecure, java.lang.String password, java.lang.String username) throws com.liferay.mail.MailboxException {
        return getService().updateMailMessageAccount(accountId, mailInHostName, mailInPort, mailInSecure, mailOutHostName, mailOutPort, mailOutSecure, password, username);
    }

    public static AccountLocalService getService() {
        if (_service == null) {
            Object obj = PortletBeanLocatorUtil.locate("mail-portlet", AccountLocalServiceUtil.class.getName());
            ClassLoader portletClassLoader = (ClassLoader) PortletBeanLocatorUtil.locate("mail-portlet", "portletClassLoader");
            ClassLoaderProxy classLoaderProxy = new ClassLoaderProxy(obj, portletClassLoader);
            _service = new AccountLocalServiceClp(classLoaderProxy);
            ClpSerializer.setClassLoader(portletClassLoader);
        }
        return _service;
    }

    public void setService(AccountLocalService service) {
        _service = service;
    }

    private static AccountLocalService _service;
}
