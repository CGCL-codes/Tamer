package com.brekeke.hiway.ticket.dao.impl;

import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import com.brekeke.hiway.ticket.dao.TicCheckDetailReportDAO;
import com.brekeke.hiway.ticket.dto.TicCheckDetailDto;
import com.brekeke.hiway.ticket.entity.BasePojo;
import com.brekeke.hiway.ticket.entity.TicCheckDetailReport;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 票据收发盘存明细账DAO
 * @author LEPING.LI
 * @version 1.0.0
 */
public class TicCheckDetailReportDAOImpl implements TicCheckDetailReportDAO {

    private Logger log = Logger.getLogger(TicCheckDetailReportDAOImpl.class);

    private SqlMapClientTemplate sqlMapClientTemplate;

    /**
	 * 注入 SqlMapClientTemplate 实例
	 * @param sqlMapClientTemplate
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

    /**
	 * 新增一条票据收发盘存明细账记录
	 */
    public Integer insert(BasePojo bp) {
        sqlMapClientTemplate.insert("insertTCDReport", bp);
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
	 * 批量保存票据收发盘存明细账记录
	 */
    public void insertBatchTCDReport(final List<TicCheckDetailReport> tcdlist) {
        sqlMapClientTemplate.execute(new SqlMapClientCallback() {

            public Object doInSqlMapClient(SqlMapExecutor executer) throws SQLException {
                executer.startBatch();
                for (TicCheckDetailReport tcd : tcdlist) {
                    executer.insert("insertTCDReport", tcd);
                }
                executer.executeBatch();
                return null;
            }
        });
    }

    /**
	 * 创建根据收发盘存明细表
	 * @param tablename
	 * 一个站一年一张表
	 */
    public void createTCDReportTable(TicCheckDetailDto ticketCheckDetialReportDto) {
        String tablename = ticketCheckDetialReportDto.getTablename();
        String table = (String) sqlMapClientTemplate.queryForObject("existsRCDRportTable", tablename);
        if (table == null) {
            sqlMapClientTemplate.update("createTCDReportTable", tablename);
        } else {
            sqlMapClientTemplate.delete("deleteTCDReport", tablename);
        }
    }
}
