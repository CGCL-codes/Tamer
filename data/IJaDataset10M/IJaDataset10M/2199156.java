package com.brekeke.hiway.ticket.dao.impl;

import java.sql.SQLException;
import java.util.List;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import com.brekeke.hiway.ticket.dao.CommonTicketDetailDAO;
import com.brekeke.hiway.ticket.dto.TicketDetailDto;
import com.brekeke.hiway.ticket.entity.BasePojo;
import com.brekeke.hiway.ticket.entity.TicketDetail;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 管理所、管理处、高管局公共票据盘存明细账DAO
 * @author LEPING.LI
 * @version 1.0.0
 */
public class CommonTicketDetailDAOImpl implements CommonTicketDetailDAO {

    private SqlMapClientTemplate sqlMapClientTemplate;

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
	 * 创建票据盘存明细账表
	 * @param ticketDetailDto
	 */
    public void createCommonTicketDetailReport(TicketDetailDto ticketDetailDto) {
        String tablename = ticketDetailDto.getTablename();
        String table = (String) sqlMapClientTemplate.queryForObject("existsTicketDetailTable", tablename);
        if (table == null) {
            sqlMapClientTemplate.update("createTicketDetailTable", tablename);
        } else {
            sqlMapClientTemplate.delete("deleteTicketDetailForReport", ticketDetailDto);
        }
    }

    /**
	 * 批量保存票据收发盘存明细账
	 * @param ticketDetail
	 */
    public void insertTicketDetailBatch(final List<TicketDetail> ticketDetailList) {
        sqlMapClientTemplate.execute(new SqlMapClientCallback() {

            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                executor.startBatch();
                for (TicketDetail ticketDetail : ticketDetailList) {
                    executor.insert("insertTicketDetail", ticketDetail);
                }
                executor.executeBatch();
                return null;
            }
        });
    }
}
