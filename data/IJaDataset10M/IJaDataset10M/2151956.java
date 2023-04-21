package websiteschema.persistence.mapper;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websiteschema.common.base.Function;
import websiteschema.common.wrapper.SQLBeanWrapper;
import websiteschema.model.domain.UrlLink;
import websiteschema.persistence.Mapper;
import websiteschema.persistence.rdbms.UrlLinkDBMapper;

/**
 *
 * @author ray
 */
@Service
public class UrlLinkMapper implements Mapper<UrlLink> {

    @Autowired
    UrlLinkDBMapper urlLinkDBMapper;

    @Override
    public boolean exists(String rowKey) {
        int count = urlLinkDBMapper.exists(rowKey);
        return count > 0;
    }

    @Override
    public UrlLink get(String rowKey) {
        Map map = urlLinkDBMapper.getByRowKey(rowKey);
        return SQLBeanWrapper.getBean(map, UrlLink.class, false);
    }

    @Override
    public List<UrlLink> getList(String start, String end) {
        return getList(start, end, null);
    }

    @Override
    public List<UrlLink> getList(String start, String end, String family) {
        return getList(start, end, family, -1);
    }

    @Override
    public List<UrlLink> getList(String start, String end, String family, int maxResults) {
        return getList(start, end, family, 0, maxResults);
    }

    public List<UrlLink> getList(String start, String end, String family, int begin, int maxResults) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void put(UrlLink obj) {
        if (null != obj) {
            Map map = SQLBeanWrapper.getMap(obj, UrlLink.class);
            if (exists(obj.getRowKey())) {
                urlLinkDBMapper.update(map);
            } else {
                urlLinkDBMapper.insert(map);
            }
        }
    }

    @Override
    public void put(List<UrlLink> lst) {
        if (null != lst) {
            for (UrlLink obj : lst) {
                put(obj);
            }
        }
    }

    @Override
    public void delete(String rowKey) {
        urlLinkDBMapper.deleteByRowKey(rowKey);
    }

    @Override
    public void scan(String start, String end, Function<UrlLink> func) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void batchScan(String start, String end, int batchSize, Function<List<UrlLink>> func) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
