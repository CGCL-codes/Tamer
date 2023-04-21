package com.brekeke.hiway.ticket.dao.impl;

import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import com.brekeke.hiway.ticket.dao.DisticketListDAO;
import com.brekeke.hiway.ticket.dto.DisticketListDto;
import com.brekeke.hiway.ticket.entity.BasePojo;
import com.brekeke.hiway.ticket.entity.DisticketList;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 废票清单DAO
 * @author LEPING.LI
 * @version 1.0.0
 */
public class DisticketListDAOImpl implements DisticketListDAO {

    private Logger log = Logger.getLogger(DisticketListDAOImpl.class);

    @SuppressWarnings("unused")
    private SqlMapClientTemplate sqlMapClientTemplate;

    /**
	 * 注入DAO模板
	 */
    public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
        this.sqlMapClientTemplate = sqlMapClientTemplate;
    }

    public Integer delete(String id) {
        return null;
    }

    public Integer delete(Integer id) {
        return null;
    }

    public Integer insert(BasePojo bp) {
        return null;
    }

    public List<BasePojo> searchAllList() {
        return null;
    }

    public BasePojo searchByID(String id) {
        return null;
    }

    public BasePojo searchByID(Integer id) {
        return null;
    }

    public Integer update(BasePojo bp) {
        return null;
    }

    /**
	 * 批量保存废票清单数据
	 */
    public void insertDisticketListBatch(final List<DisticketList> dllist) {
        sqlMapClientTemplate.execute(new SqlMapClientCallback() {

            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                executor.startBatch();
                for (DisticketList dl : dllist) {
                    executor.insert("insertDisticketList", dl);
                }
                executor.executeBatch();
                return null;
            }
        });
    }

    /**
     * 判断废票表
     * 业务：先查询该报表是否存在，如果存在则删除所有数据，
     * 如果不存在则创建表
     */
    public void createDisticketTable(DisticketListDto dld) {
        String tablename = dld.getTablename();
        String table = (String) sqlMapClientTemplate.queryForObject("existsDisticketListTable", tablename);
        if (table == null) {
            sqlMapClientTemplate.update("createDisticketListTable", tablename);
        } else {
            sqlMapClientTemplate.delete("deleteDisticketListData", dld);
        }
    }

    @SuppressWarnings("unchecked")
    public List<DisticketList> searchDisticketListByDto(DisticketListDto dld) {
        log.debug("searchDisticketListByDto");
        return sqlMapClientTemplate.queryForList("searchDisticketListByDto", dld);
    }
}
