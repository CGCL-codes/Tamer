package sunsite.service;

import java.util.List;
import org.springframework.dao.DataAccessException;
import sunsite.po.Resource;

/**
 *
 * @author mg
 */
public interface ResourceService {

    /**
     * 取得下载排名前10的软件
     * @param
     * @return 下载排名前10的软件
     * @throws org.springframework.dao.DataAccessException
     */
    List<Resource> getTop10Software() throws DataAccessException;

    /**
     * 取得最新上传的10个公开资源
     * @param
     * @return 最新上传的10个公开资源
     * @throws org.springframework.dao.DataAccessException
     */
    List<Resource> getLatestResource() throws DataAccessException;
}
