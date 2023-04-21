package com.liferay.portal.service.spring;

/**
 * <a href="PhoneServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class PhoneServiceUtil {

    public static com.liferay.portal.model.Phone addPhone(java.lang.String className, java.lang.String classPK, java.lang.String number, java.lang.String extension, java.lang.String typeId, boolean primary) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            PhoneService phoneService = PhoneServiceFactory.getService();
            return phoneService.addPhone(className, classPK, number, extension, typeId, primary);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static void deletePhone(java.lang.String phoneId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            PhoneService phoneService = PhoneServiceFactory.getService();
            phoneService.deletePhone(phoneId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static com.liferay.portal.model.Phone getPhone(java.lang.String phoneId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            PhoneService phoneService = PhoneServiceFactory.getService();
            return phoneService.getPhone(phoneId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static java.util.List getPhones(java.lang.String className, java.lang.String classPK) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            PhoneService phoneService = PhoneServiceFactory.getService();
            return phoneService.getPhones(className, classPK);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static com.liferay.portal.model.Phone updatePhone(java.lang.String phoneId, java.lang.String number, java.lang.String extension, java.lang.String typeId, boolean primary) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            PhoneService phoneService = PhoneServiceFactory.getService();
            return phoneService.updatePhone(phoneId, number, extension, typeId, primary);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }
}
