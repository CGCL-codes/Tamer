package sunsite.dao;

import java.util.List;
import java.util.Map;
import sunsite.po.DownloadHistory;
import sunsite.po.Resource;
import sunsite.po.ResourceCatelog;
import sunsite.po.ResourceType;
import sunsite.tools.Pagination;

/**
 *
 * @author Administrator
 */
public interface FileDao {

    List<sunsite.po.ResourceTp> getAllResourceTp();

    List<sunsite.po.ResourceCatelog> getAllResourceCatelog();

    List<sunsite.po.ResourceType> getAllResourceType();

    ResourceCatelog getResourceCatelog(int catid);

    List<sunsite.po.ResourceCatelog> getNullResourceCatelog();

    List<sunsite.po.ResourceCatelog> getResourceCatelogList(int tpid);

    List<sunsite.po.ResourceType> getResourceTypeList(int catid);

    ResourceType getResourceType(int typeid);

    void addResourceType(ResourceType resourceType);

    void addResource(Resource resource);

    void modifyResource(Resource resource);

    void addDownloadHistory(DownloadHistory downloadHistory);

    List<sunsite.po.Resource> getAllResource();

    Resource getResource(String resid);

    void deleteResource(String resid);

    List<sunsite.po.Resource> getResourceList(Pagination pagination);

    List<Resource> getResourceList(final Pagination pagination, Map map);
}
