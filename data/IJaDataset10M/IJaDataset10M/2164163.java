package com.brekeke.hiway.ticket.dao.impl;

import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import com.brekeke.hiway.ticket.dao.TendaysCollectReportDAO;
import com.brekeke.hiway.ticket.dto.TendaysCollectReportDto;
import com.brekeke.hiway.ticket.entity.TendaysCollectReport;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 旬报表统计DAO
 * @author LEPING LI
 * @version 1.0.0
 */
public class TendaysCollectReportDAOImpl implements TendaysCollectReportDAO {

    private Logger log = Logger.getLogger(TendaysCollectReportDAOImpl.class);

    private SqlMapClientTemplate sqlMapClientTemplate;

    /**
	 * 注入DAO模板
	 */
    public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
        this.sqlMapClientTemplate = sqlMapClientTemplate;
    }

    /**
	 * 批量插入旬报表数据
	 * @param tcrlist
	 */
    public void insertTenDayRpoertBatch(final List<TendaysCollectReport> tcrlist) {
        sqlMapClientTemplate.execute(new SqlMapClientCallback() {

            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                executor.startBatch();
                for (TendaysCollectReport tcr : tcrlist) {
                    executor.insert("insertTenDayReport", tcr);
                }
                executor.executeBatch();
                return null;
            }
        });
    }

    /**
	 * 创建旬报表
	 * 如果表存在,则删除该表中符合条件的记录,如果不存在
	 * 则创建表
	 */
    public void createTdayCollectTable(TendaysCollectReportDto tcrd) {
        String tablename = tcrd.getTablename();
        String table = (String) sqlMapClientTemplate.queryForObject("existsTendayCollectRportTable", tablename);
        if (table == null) {
            sqlMapClientTemplate.update("createTendayCollectTable", tablename);
        } else {
            sqlMapClientTemplate.delete("deleteTenDayReport", tcrd);
        }
    }
}
