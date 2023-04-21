package com.pk.platform.business.charge.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.pk.platform.business.charge.dao.IIncomeExpenseDao;
import com.pk.platform.business.charge.vo.IncomeExpenseVO;
import com.pk.platform.business.core.dao.impl.GenericDaoImpl;
import com.pk.platform.domain.charge.IncomeExpenseIndex;
import com.pk.platform.util.DateConverter;
import com.pk.platform.util.StringConverter;
import com.pk.platform.util.page.ListPage;
import com.pk.platform.util.page.Pager;

public class IncomeExpenseDaoImpl extends GenericDaoImpl implements IIncomeExpenseDao {

    public List<Map<String, Object>> queryYearAgo(String kgId, String thisYear) {
        String sql = "select distinct year from income_expense ie where ie.kgId = ? and year not in (?, ?) order by year desc";
        return this.getSjt().queryForList(sql, kgId, thisYear, DateConverter.getCurrentYearStr());
    }

    public List<Map<String, Object>> queryLockMonth(String year, String kgId) {
        String sql = "select * from income_expense_lock iel where iel.year = ? and iel.kgId = ?";
        return this.getSjt().queryForList(sql, year, kgId);
    }

    public void delLock(String year, String month, String kgId) {
        String sql = "delete from income_expense_lock where year = ? and month = ? and kgId = ?";
        this.getSjt().update(sql, year, month, kgId);
    }

    public boolean existLock(String year, String month, String kgId) {
        String sql = "select count(0) from income_expense_lock where year = ? and month = ? and kgId = ?";
        int count = this.getSjt().queryForInt(sql, year, month, kgId);
        if (count > 0) return true;
        return false;
    }

    public List<IncomeExpenseIndex> queryIncomeExpenseIndexList(String type) {
        List<Object> param = new ArrayList<Object>();
        String hql = "from IncomeExpenseIndex iei where iei.isDel = 0";
        if (StringConverter.isNotNull(type)) {
            hql += " and iei.type.id = ?";
            param.add(type);
        }
        return this.getHibernateTemplate().find(hql, param.toArray());
    }

    private String getReportSql(IncomeExpenseVO ivo) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ie.id, DATE_FORMAT(ie.chargeTime, '%Y-%m-%d') as chargeTime,ie.remark,iei.indexName,ie.amount,0 as amount2 FROM income_expense  ie ");
        sql.append("left join income_expense_index iei on ie.ieIndexId=iei.id ");
        sql.append("where iei.bdid='income' and ie.year='" + ivo.getYear() + "' and ie.month='" + ivo.getMonth() + "' ");
        if (StringConverter.isNotNull(ivo.getKindergartenId())) sql.append("and ie.kgid='" + ivo.getKindergartenId() + "' ");
        sql.append("union all ");
        sql.append("SELECT ie.id, DATE_FORMAT(ie.chargeTime, '%Y-%m-%d') as chargeTime,ie.remark,iei.indexName,0 as amount,ie.amount FROM income_expense  ie ");
        sql.append("left join income_expense_index iei on ie.ieIndexId=iei.id ");
        sql.append("where iei.bdid='expense' and ie.year='" + ivo.getYear() + "' and ie.month='" + ivo.getMonth() + "' ");
        if (StringConverter.isNotNull(ivo.getKindergartenId())) sql.append("and ie.kgid='" + ivo.getKindergartenId() + "' ");
        sql.append("order by amount2,chargeTime ");
        return sql.toString();
    }

    public ListPage getReportList(IncomeExpenseVO ivo, Pager pager) {
        return this.getListPage(getReportSql(ivo), null, pager);
    }

    public List<Map<String, Object>> getList(IncomeExpenseVO ivo) {
        return this.getSjt().queryForList(getReportSql(ivo), new Object[] {});
    }

    public IncomeExpenseVO getTotalAmount(IncomeExpenseVO ivo) {
        IncomeExpenseVO rtn = ivo;
        StringBuffer sql = new StringBuffer();
        sql.append("select a.amount,b.amount1 from( ");
        sql.append("SELECT sum(ie.amount) as amount FROM income_expense  ie ");
        sql.append("left join income_expense_index iei on ie.ieIndexId=iei.id ");
        sql.append("where iei.bdid='income' and ie.year='" + ivo.getYear() + "' and ie.month='" + ivo.getMonth() + "' ");
        if (StringConverter.isNotNull(ivo.getKindergartenId())) sql.append("and ie.kgid='" + ivo.getKindergartenId() + "' ");
        sql.append(") a,( ");
        sql.append("SELECT sum(ie.amount) as amount1 FROM income_expense  ie ");
        sql.append("left join income_expense_index iei on ie.ieIndexId=iei.id ");
        sql.append("where iei.bdid='expense' and ie.year='" + ivo.getYear() + "' and ie.month='" + ivo.getMonth() + "' ");
        if (StringConverter.isNotNull(ivo.getKindergartenId())) sql.append("and ie.kgid='" + ivo.getKindergartenId() + "' ");
        sql.append(") b");
        List<Map<String, Object>> result = this.getSjt().queryForList(sql.toString(), new Object[] {});
        if (result.size() > 0) {
            rtn.setAmountStart(result.get(0).get("amount") == null ? 0 : (Double.parseDouble(result.get(0).get("amount").toString())));
            rtn.setAmountEnd(result.get(0).get("amount1") == null ? 0 : (Double.parseDouble(result.get(0).get("amount1").toString())));
        }
        return rtn;
    }
}
