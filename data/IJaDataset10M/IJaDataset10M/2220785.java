package sist.erp.db.Insa;

import java.util.List;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class EnddayManager extends SqlMapClientDaoSupport {

    public List enddaySearch(EnddayVO vo) throws Exception {
        return getSqlMapClientTemplate().queryForList("enddaySearch", vo);
    }

    public EnddayVO enddayInfo(int emp_no) throws Exception {
        return (EnddayVO) getSqlMapClientTemplate().queryForObject("enddayInfo", emp_no);
    }

    public void enddayUpdate(int emp_no) throws Exception {
        getSqlMapClientTemplate().update("enddayUpdate", emp_no);
    }
}
