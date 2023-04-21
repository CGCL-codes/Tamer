package org.riverock.webmill.portal.dao;

import java.util.Locale;
import java.util.List;
import org.riverock.interfaces.portal.dao.PortalCatalogDao;
import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 13:59:34
 */
public class PortalCatalogDaoImpl implements PortalCatalogDao {

    private AuthSession authSession = null;

    private ClassLoader classLoader = null;

    PortalCatalogDaoImpl(AuthSession authSession, ClassLoader classLoader) {
        this.authSession = authSession;
        this.classLoader = classLoader;
    }

    public Long getCatalogItemId(Long siteLanguageId, Long portletNameId, Long templateId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            return InternalDaoFactory.getInternalCatalogDao().getCatalogItemId(siteLanguageId, portletNameId, templateId);
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    public Long getCatalogItemId(Long siteId, Locale locale, String portletName, String templateName) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            return InternalDaoFactory.getInternalCatalogDao().getCatalogItemId(siteId, locale, portletName, templateName);
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    public Long getCatalogItemId(Long siteId, Locale locale, String pageName) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            return InternalDaoFactory.getInternalCatalogDao().getCatalogItemId(siteId, locale, pageName);
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    public Long getCatalogItemId(Long siteId, Locale locale, Long catalogId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            return InternalDaoFactory.getInternalCatalogDao().getCatalogItemId(siteId, locale, catalogId);
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    public List<CatalogItem> getCatalogItemList(Long catalogLanguageId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            return InternalDaoFactory.getInternalCatalogDao().getCatalogItemList(catalogLanguageId);
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    public CatalogItem getCatalogItem(Long catalogId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            return InternalDaoFactory.getInternalCatalogDao().getCatalogItem(catalogId);
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    public CatalogLanguageItem getCatalogLanguageItem(Long catalogLanguageId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            return InternalDaoFactory.getInternalCatalogDao().getCatalogLanguageItem(catalogLanguageId);
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    public List<CatalogLanguageItem> getCatalogLanguageItemList(Long siteLanguageId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            return InternalDaoFactory.getInternalCatalogDao().getCatalogLanguageItemList(siteLanguageId);
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    public Long createCatalogItem(CatalogItem catalogItem) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            return InternalDaoFactory.getInternalCatalogDao().createCatalogItem(catalogItem);
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    public void updateCatalogItem(CatalogItem catalogItem) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            InternalDaoFactory.getInternalCatalogDao().updateCatalogItem(catalogItem);
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    public void deleteCatalogItem(Long catalogId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            InternalDaoFactory.getInternalCatalogDao().deleteCatalogItem(catalogId);
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    public Long createCatalogLanguageItem(CatalogLanguageItem catalogLanguageItem) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            return InternalDaoFactory.getInternalCatalogDao().createCatalogLanguageItem(catalogLanguageItem);
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    public void updateCatalogLanguageItem(CatalogLanguageItem catalogLanguageItem) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            InternalDaoFactory.getInternalCatalogDao().updateCatalogLanguageItem(catalogLanguageItem);
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    public void deleteCatalogLanguageItem(Long catalogLanguageId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            InternalDaoFactory.getInternalCatalogDao().deleteCatalogLanguageItem(catalogLanguageId);
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    public CatalogLanguageItem getCatalogLanguageItem(String catalogLanguageCode, Long siteLanguageId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            return InternalDaoFactory.getInternalCatalogDao().getCatalogLanguageItem(catalogLanguageCode, siteLanguageId);
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }
}
