package welo.dao;

import java.util.List;
import welo.dao.common.ThreadBaseDao;
import welo.model.db.CmsPage;

/**
 * 
 * @author james.yong
 *
 */
public class CmsPageDao extends ThreadBaseDao {

    public CmsPage getCmsPageBean(int id) {
        return objectForPK(CmsPage.class, id);
    }

    public List<CmsPage> getCmsPageBeans() {
        return getBeans(CmsPage.class);
    }

    public CmsPage getCmsPageBeanFromPath(String pageLink) {
        String link = pageLink;
        if (pageLink != null && pageLink.startsWith("/")) {
            link = link.substring(1);
        }
        return getBean(CmsPage.class, "pageLink='" + link + "'");
    }
}
