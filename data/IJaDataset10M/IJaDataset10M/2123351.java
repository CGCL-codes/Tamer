package com.shine.sourceflow.dao.allocation;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import com.shine.DBUtil.DBUtil;
import com.shine.DBUtil.model.DBModel;
import com.shine.sourceflow.dao.GenericDao;
import com.shine.sourceflow.model.GenericDto;
import com.shine.sourceflow.utils.Pagination;
import com.shine.sourceflow.web.GenericAction;

public abstract class ConfigGenericDao extends GenericDao {

    public abstract String getField();

    public abstract String getTableName();

    /**
	 * 查询配置信息
	 */
    @Override
    public Map<String, DBModel> list(GenericDto dto) {
        String perPage = (String) dto.getExtraParams("perPage");
        String curPage = (String) dto.getExtraParams("curPage");
        this.pagination = new Pagination(Integer.parseInt(perPage), Integer.parseInt(curPage), this.count(dto));
        Map<String, DBModel> dbModels = new HashMap<String, DBModel>();
        String sql = this.createQuerySql();
        DBModel dbModel = DBUtil.getInstance().executeQuery(GenericDao.JNDI_DEFAULT, sql);
        try {
            dbModel.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbModel.close();
        }
        dbModels.put(GenericAction.DATA_DEFAULT, dbModel);
        return dbModels;
    }

    /**
	 * 创建查询语句
	 * 
	 * @return
	 */
    protected abstract String createQuerySql();

    /**
	 * 根据ID进行查询
	 * 
	 * @param dto
	 * @return
	 */
    @Override
    public Map<String, DBModel> listById(GenericDto dto) {
        String sql = this.createQuerySql(dto);
        Map<String, DBModel> dbModels = new HashMap<String, DBModel>();
        DBModel dbModel = DBUtil.getInstance().executeQuery(GenericDao.JNDI_DEFAULT, sql);
        try {
            dbModel.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbModel.close();
        }
        dbModels.put(GenericAction.DATA_DEFAULT, dbModel);
        return dbModels;
    }

    /**
	 * 创建条件查询语句
	 * 
	 * @return
	 */
    protected abstract String createQuerySql(GenericDto dto);

    /**
	 * 添加数据
	 * 
	 * @param dto
	 */
    @Override
    public void add(GenericDto dto) {
        String sql = this.createInsertSql(dto);
        DBUtil.getInstance().executeClusterUpdate(JNDI_CLUSTER, sql);
    }

    /**
	 * 创建SQL插入语句
	 * 
	 * @param dto
	 * @return
	 */
    protected abstract String createInsertSql(GenericDto dto);

    /**
	 * 更新数据
	 */
    @Override
    public void edit(GenericDto dto) {
        String sql = this.createUpdateSql(dto);
        DBUtil.getInstance().executeClusterUpdate(JNDI_CLUSTER, sql);
    }

    /**
	 * 创建更新语句 
	 * 
	 * @param dto
	 * @return
	 */
    protected abstract String createUpdateSql(GenericDto dto);

    /**
	 * 删除数据
	 */
    @Override
    public void delete(GenericDto dto) {
        String sql = createDeleteSql(dto);
        DBUtil.getInstance().executeClusterUpdate(JNDI_CLUSTER, sql);
    }

    /**
	 * 创建删除语句
	 * 
	 * @param dto
	 * @return
	 */
    protected abstract String createDeleteSql(GenericDto dto);

    /**
	 * 获取总记录数
	 */
    public int count(GenericDto dto) {
        String sql = "select count(1) as total from " + this.getTableName();
        DBModel dbModel = DBUtil.getInstance().executeQuery(JNDI_DEFAULT, sql);
        try {
            dbModel.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbModel.close();
        }
        return Integer.parseInt(dbModel.get(0).get("total"));
    }
}
