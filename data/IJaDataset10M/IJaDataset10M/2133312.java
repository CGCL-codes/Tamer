package com.liferay.portlet.softwarecatalog.service.base;

import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.dao.DynamicQueryInitializer;
import com.liferay.portlet.softwarecatalog.service.SCFrameworkVersionLocalService;
import com.liferay.portlet.softwarecatalog.service.SCFrameworkVersionLocalServiceFactory;
import com.liferay.portlet.softwarecatalog.service.SCFrameworkVersionService;
import com.liferay.portlet.softwarecatalog.service.SCFrameworkVersionServiceFactory;
import com.liferay.portlet.softwarecatalog.service.SCLicenseLocalService;
import com.liferay.portlet.softwarecatalog.service.SCLicenseLocalServiceFactory;
import com.liferay.portlet.softwarecatalog.service.SCLicenseService;
import com.liferay.portlet.softwarecatalog.service.SCLicenseServiceFactory;
import com.liferay.portlet.softwarecatalog.service.SCProductEntryLocalService;
import com.liferay.portlet.softwarecatalog.service.SCProductEntryLocalServiceFactory;
import com.liferay.portlet.softwarecatalog.service.SCProductEntryService;
import com.liferay.portlet.softwarecatalog.service.SCProductEntryServiceFactory;
import com.liferay.portlet.softwarecatalog.service.SCProductScreenshotLocalService;
import com.liferay.portlet.softwarecatalog.service.SCProductScreenshotLocalServiceFactory;
import com.liferay.portlet.softwarecatalog.service.SCProductVersionLocalService;
import com.liferay.portlet.softwarecatalog.service.persistence.SCFrameworkVersionPersistence;
import com.liferay.portlet.softwarecatalog.service.persistence.SCFrameworkVersionUtil;
import com.liferay.portlet.softwarecatalog.service.persistence.SCLicensePersistence;
import com.liferay.portlet.softwarecatalog.service.persistence.SCLicenseUtil;
import com.liferay.portlet.softwarecatalog.service.persistence.SCProductEntryPersistence;
import com.liferay.portlet.softwarecatalog.service.persistence.SCProductEntryUtil;
import com.liferay.portlet.softwarecatalog.service.persistence.SCProductScreenshotPersistence;
import com.liferay.portlet.softwarecatalog.service.persistence.SCProductScreenshotUtil;
import com.liferay.portlet.softwarecatalog.service.persistence.SCProductVersionPersistence;
import com.liferay.portlet.softwarecatalog.service.persistence.SCProductVersionUtil;
import org.springframework.beans.factory.InitializingBean;
import java.util.List;

/**
 * <a href="SCProductVersionLocalServiceBaseImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public abstract class SCProductVersionLocalServiceBaseImpl implements SCProductVersionLocalService, InitializingBean {

    public List dynamicQuery(DynamicQueryInitializer queryInitializer) throws SystemException {
        return SCProductVersionUtil.findWithDynamicQuery(queryInitializer);
    }

    public List dynamicQuery(DynamicQueryInitializer queryInitializer, int begin, int end) throws SystemException {
        return SCProductVersionUtil.findWithDynamicQuery(queryInitializer, begin, end);
    }

    public SCLicenseLocalService getSCLicenseLocalService() {
        return scLicenseLocalService;
    }

    public void setSCLicenseLocalService(SCLicenseLocalService scLicenseLocalService) {
        this.scLicenseLocalService = scLicenseLocalService;
    }

    public SCLicenseService getSCLicenseService() {
        return scLicenseService;
    }

    public void setSCLicenseService(SCLicenseService scLicenseService) {
        this.scLicenseService = scLicenseService;
    }

    public SCLicensePersistence getSCLicensePersistence() {
        return scLicensePersistence;
    }

    public void setSCLicensePersistence(SCLicensePersistence scLicensePersistence) {
        this.scLicensePersistence = scLicensePersistence;
    }

    public SCFrameworkVersionLocalService getSCFrameworkVersionLocalService() {
        return scFrameworkVersionLocalService;
    }

    public void setSCFrameworkVersionLocalService(SCFrameworkVersionLocalService scFrameworkVersionLocalService) {
        this.scFrameworkVersionLocalService = scFrameworkVersionLocalService;
    }

    public SCFrameworkVersionService getSCFrameworkVersionService() {
        return scFrameworkVersionService;
    }

    public void setSCFrameworkVersionService(SCFrameworkVersionService scFrameworkVersionService) {
        this.scFrameworkVersionService = scFrameworkVersionService;
    }

    public SCFrameworkVersionPersistence getSCFrameworkVersionPersistence() {
        return scFrameworkVersionPersistence;
    }

    public void setSCFrameworkVersionPersistence(SCFrameworkVersionPersistence scFrameworkVersionPersistence) {
        this.scFrameworkVersionPersistence = scFrameworkVersionPersistence;
    }

    public SCProductEntryLocalService getSCProductEntryLocalService() {
        return scProductEntryLocalService;
    }

    public void setSCProductEntryLocalService(SCProductEntryLocalService scProductEntryLocalService) {
        this.scProductEntryLocalService = scProductEntryLocalService;
    }

    public SCProductEntryService getSCProductEntryService() {
        return scProductEntryService;
    }

    public void setSCProductEntryService(SCProductEntryService scProductEntryService) {
        this.scProductEntryService = scProductEntryService;
    }

    public SCProductEntryPersistence getSCProductEntryPersistence() {
        return scProductEntryPersistence;
    }

    public void setSCProductEntryPersistence(SCProductEntryPersistence scProductEntryPersistence) {
        this.scProductEntryPersistence = scProductEntryPersistence;
    }

    public SCProductScreenshotLocalService getSCProductScreenshotLocalService() {
        return scProductScreenshotLocalService;
    }

    public void setSCProductScreenshotLocalService(SCProductScreenshotLocalService scProductScreenshotLocalService) {
        this.scProductScreenshotLocalService = scProductScreenshotLocalService;
    }

    public SCProductScreenshotPersistence getSCProductScreenshotPersistence() {
        return scProductScreenshotPersistence;
    }

    public void setSCProductScreenshotPersistence(SCProductScreenshotPersistence scProductScreenshotPersistence) {
        this.scProductScreenshotPersistence = scProductScreenshotPersistence;
    }

    public SCProductVersionPersistence getSCProductVersionPersistence() {
        return scProductVersionPersistence;
    }

    public void setSCProductVersionPersistence(SCProductVersionPersistence scProductVersionPersistence) {
        this.scProductVersionPersistence = scProductVersionPersistence;
    }

    public void afterPropertiesSet() {
        if (scLicenseLocalService == null) {
            scLicenseLocalService = SCLicenseLocalServiceFactory.getImpl();
        }
        if (scLicenseService == null) {
            scLicenseService = SCLicenseServiceFactory.getImpl();
        }
        if (scLicensePersistence == null) {
            scLicensePersistence = SCLicenseUtil.getPersistence();
        }
        if (scFrameworkVersionLocalService == null) {
            scFrameworkVersionLocalService = SCFrameworkVersionLocalServiceFactory.getImpl();
        }
        if (scFrameworkVersionService == null) {
            scFrameworkVersionService = SCFrameworkVersionServiceFactory.getImpl();
        }
        if (scFrameworkVersionPersistence == null) {
            scFrameworkVersionPersistence = SCFrameworkVersionUtil.getPersistence();
        }
        if (scProductEntryLocalService == null) {
            scProductEntryLocalService = SCProductEntryLocalServiceFactory.getImpl();
        }
        if (scProductEntryService == null) {
            scProductEntryService = SCProductEntryServiceFactory.getImpl();
        }
        if (scProductEntryPersistence == null) {
            scProductEntryPersistence = SCProductEntryUtil.getPersistence();
        }
        if (scProductScreenshotLocalService == null) {
            scProductScreenshotLocalService = SCProductScreenshotLocalServiceFactory.getImpl();
        }
        if (scProductScreenshotPersistence == null) {
            scProductScreenshotPersistence = SCProductScreenshotUtil.getPersistence();
        }
        if (scProductVersionPersistence == null) {
            scProductVersionPersistence = SCProductVersionUtil.getPersistence();
        }
    }

    protected SCLicenseLocalService scLicenseLocalService;

    protected SCLicenseService scLicenseService;

    protected SCLicensePersistence scLicensePersistence;

    protected SCFrameworkVersionLocalService scFrameworkVersionLocalService;

    protected SCFrameworkVersionService scFrameworkVersionService;

    protected SCFrameworkVersionPersistence scFrameworkVersionPersistence;

    protected SCProductEntryLocalService scProductEntryLocalService;

    protected SCProductEntryService scProductEntryService;

    protected SCProductEntryPersistence scProductEntryPersistence;

    protected SCProductScreenshotLocalService scProductScreenshotLocalService;

    protected SCProductScreenshotPersistence scProductScreenshotPersistence;

    protected SCProductVersionPersistence scProductVersionPersistence;
}
