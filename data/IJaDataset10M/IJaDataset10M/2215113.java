package test.dao;

import java.util.List;
import java.util.Map;
import org.jmantis.core.PageResult;
import org.jmantis.core.QueryParam;
import test.entity.Servers;

public interface ServersDao {

    /**
	 * @return 插入对象id
	 */
    public int insert(Servers servers);

    /**
	 * @return 返回插入成功个数
	 */
    public int insert(List<Servers> list);

    public boolean update(Servers servers);

    /**
	 * m为空拒绝修改！
	 */
    public int updatePartial(Map<String, Object> m);

    /**
	 * wh和m为空拒绝修改！
	 */
    public int updatePartial(Map<String, Object> m, List<QueryParam> wh);

    public boolean del(int sid);

    public Servers get(int sid);

    public PageResult<Servers> find(int page, int pageSize);

    public List<Servers> find(int max);

    public int getCount();

    public PageResult<Servers> findOrderByParam(List<QueryParam> order, int page, int pageSize);

    public List<Servers> findOrderByParam(List<QueryParam> order, int max);

    public int getCountByParam(List<QueryParam> param);

    public PageResult<Servers> findByParam(List<QueryParam> param, int page, int pageSize);

    public List<Servers> findByParam(List<QueryParam> param, int max);

    public PageResult<Servers> findByParamOrderByParam(List<QueryParam> param, List<QueryParam> order, int page, int pageSize);

    public List<Servers> findByParamOrderByParam(List<QueryParam> param, List<QueryParam> order, int max);
}
