package welo.dao;

import org.apache.cayenne.ObjectContext;
import welo.ioc.guice.CmsInjector;

public class CmsDaoFactory {

    public static KeyValueDao getKeyValueDao() {
        return new CmsInjector().get(KeyValueDao.class);
    }

    public static CmsPageDao getCmsPageDao() {
        return new CmsInjector().get(CmsPageDao.class);
    }

    public static CmsPanelDao getCmsPanelDao() {
        return new CmsInjector().get(CmsPanelDao.class);
    }

    public static CmsPanelDao getCmsPanelDao(ObjectContext ctx) {
        return new CmsPanelDao(ctx);
    }

    public static CmsFolderDao getCmsFolderDao() {
        return new CmsInjector().get(CmsFolderDao.class);
    }

    public static WorkItemDao getWorkItemDao() {
        return new CmsInjector().get(WorkItemDao.class);
    }
}
