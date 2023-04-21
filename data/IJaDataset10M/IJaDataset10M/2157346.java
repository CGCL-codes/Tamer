package vlan.webgame.manage.dao;

import java.util.List;
import java.util.Map;
import org.jmantis.core.Order;
import org.jmantis.core.PageResult;
import org.jmantis.core.QueryParams;
import vlan.webgame.manage.entity.PetShapeLog;

public interface PetShapeLogDao {

    /**
	 * @return 插入对象id,如果失败返回类型的初始值0或者null
	 */
    public int insert(PetShapeLog petShapeLog);

    /**
	 * @return 返回插入成功个数
	 */
    public int insert(List<PetShapeLog> list);

    public boolean update(PetShapeLog petShapeLog);

    /**
	 * m为空拒绝修改！
	 */
    public boolean updatePartial(Map<String, Object> m, int id);

    /**
	 * wh和m为空拒绝修改！
	 */
    public int updatePartial(Map<String, Object> m, QueryParams wh);

    public boolean del(int id);

    public PetShapeLog get(int id);

    public long getCount();

    public long getCount(QueryParams params);

    public PageResult<PetShapeLog> find(int page, int pageSize);

    public PageResult<PetShapeLog> find(Order order, int page, int pageSize);

    public PageResult<PetShapeLog> find(QueryParams params, int page, int pageSize);

    public PageResult<PetShapeLog> find(QueryParams params, Order order, int page, int pageSize);

    public List<PetShapeLog> find(int max);

    public List<PetShapeLog> find(QueryParams params, int max);

    public List<PetShapeLog> find(QueryParams params, Order order, int max);
}
