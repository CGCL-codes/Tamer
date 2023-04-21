package com.pk.platform.business.common.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.pk.platform.business.common.dao.IChildDao;
import com.pk.platform.business.common.vo.ChildVO;
import com.pk.platform.business.core.dao.impl.GenericDaoImpl;
import com.pk.platform.util.DateConverter;
import com.pk.platform.util.StringConverter;
import com.pk.platform.util.constant.ChargeConstant;
import com.pk.platform.util.constant.Constant;
import com.pk.platform.util.page.ListPage;
import com.pk.platform.util.page.Pager;

public class ChildDaoImpl extends GenericDaoImpl implements IChildDao {

    public ListPage queryChildren(ChildVO childvo, Pager pager) {
        List<Object> param = new ArrayList<Object>();
        String sql = this.getChildSql(childvo, param);
        return this.getListPage(sql.toString(), param.toArray(), pager);
    }

    public List<Map<String, Object>> queryChildrenList(ChildVO childvo) {
        List<Object> param = new ArrayList<Object>();
        String sql = this.getChildSql(childvo, param);
        return this.getSjt().queryForList(sql, param.toArray());
    }

    public boolean existChildNum(String childNum, String kgId, String childId) {
        StringBuffer sql = new StringBuffer();
        List<Object> param = new ArrayList<Object>();
        sql.append("select count(*) from child where childNum = ? and kgId = ?");
        param.add(childNum);
        param.add(kgId);
        if (StringConverter.isNotNull(childId)) {
            sql.append(" and id <> ?");
            param.add(childId);
        }
        int count = this.getSjt().queryForInt(sql.toString(), param.toArray());
        if (count > 0) return true;
        return false;
    }

    public ListPage queryPaySituationByThisMonth(ChildVO childvo, Pager pager) {
        String today = DateConverter.getToday();
        String year = DateConverter.getYearStrByDate(today);
        String month = DateConverter.getMonthStrByDate(today);
        String yearMonth = year + "-" + month;
        StringBuffer hasPaySql = new StringBuffer();
        hasPaySql.append(" select * from (");
        hasPaySql.append(" select t2.childId as hasPayChildId,");
        hasPaySql.append(" sum(case when indexName = '").append(ChargeConstant.SCHOOLING).append("' then amount else null end) as schooling,");
        hasPaySql.append(" sum(case when indexName = '").append(ChargeConstant.MEAL).append("' then amount else null end) as meal from");
        hasPaySql.append(" (select t1.*, date_format(t1.tuitionDateStart, '%Y-%m') as dateStart,");
        hasPaySql.append(" date_format(t1.tuitionDateEnd, '%Y-%m') as dateEnd, ci.indexName");
        hasPaySql.append(" from tuition_detail t1, charge_index ci where");
        hasPaySql.append(" t1.chargeIndexId = ci.id) t1ci, tuition t2 where t2.isDel = 0");
        hasPaySql.append(" and t1ci.tuitionId = t2.id and dateStart <= '").append(yearMonth).append("'");
        hasPaySql.append(" and t1ci.dateEnd >= '").append(yearMonth).append("'");
        hasPaySql.append(" group by t2.childId) t3 where t3.schooling is not null and t3.meal is not null");
        StringBuffer sql = new StringBuffer();
        sql.append("select ch.*, c.className from child ch left join cla c on ch.classId = c.id left join ");
        sql.append(" ( ").append(hasPaySql).append(" ) t4 on ch.id = t4.hasPayChildId");
        sql.append(" where ch.status = 1");
        if (Constant.Y.equals(childvo.getHasPay())) {
            sql.append(" and t4.hasPayChildId is not null");
        } else if (Constant.N.equals(childvo.getHasPay())) {
            sql.append(" and t4.hasPayChildId is null");
        }
        List<Object> param = new ArrayList<Object>();
        if (StringConverter.isNotNull(childvo.getKindergartenId())) {
            sql.append(" and ch.kgId = ?");
            param.add(childvo.getKindergartenId());
        }
        if (StringConverter.isNotNull(childvo.getChild().getChildName())) {
            sql.append(" and ch.childName like ?");
            param.add("%" + childvo.getChild().getChildName() + "%");
        }
        if (StringConverter.isNotNull(childvo.getChild().getChildNum())) {
            sql.append(" and ch.childNum like ?");
            param.add("%" + childvo.getChild().getChildNum() + "%");
        }
        if (StringConverter.isNotNull(childvo.getClassId())) {
            sql.append(" and ch.classId = ?");
            param.add(childvo.getClassId());
        }
        sql.append(this.getKgData("ch", Constant.SQL));
        return this.getListPage(sql.toString(), param.toArray(), pager);
    }

    public int getMaxNumByKg(String kgId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select max(childNum) as maxNum from child c where c.kgId = ?");
        List<Map<String, Object>> list = this.getSjt().queryForList(sql.toString(), kgId);
        if (list.size() > 0 && list.get(0).get("maxNum") != null) {
            return Integer.parseInt(list.get(0).get("maxNum").toString());
        }
        return 0;
    }

    private String getChildSql(ChildVO childvo, List<Object> param) {
        StringBuffer sql = new StringBuffer();
        sql.append("select ch.*, cl.className from child ch left join cla cl on ch.classId = cl.id where 1 = 1");
        if (StringConverter.isNotNull(childvo.getKindergartenId())) {
            sql.append(" and ch.kgId = ?");
            param.add(childvo.getKindergartenId());
        }
        if (StringConverter.isNotNull(childvo.getChild().getChildName())) {
            sql.append(" and ch.childName like ?");
            param.add("%" + childvo.getChild().getChildName() + "%");
        }
        if (StringConverter.isNotNull(childvo.getChild().getChildNum())) {
            sql.append(" and ch.childNum like ?");
            param.add("%" + childvo.getChild().getChildNum() + "%");
        }
        if (StringConverter.isNotNull(childvo.getClassId())) {
            sql.append(" and ch.classId = ?");
            param.add(childvo.getClassId());
        }
        if (StringConverter.isNotNull(childvo.getChild().getSex())) {
            sql.append(" and ch.sex = ?");
            param.add(childvo.getChild().getSex());
        }
        if (childvo.getChild().getStatus() != null) {
            sql.append(" and ch.status = ?");
            param.add(childvo.getChild().getStatus());
        }
        if (StringConverter.isNotNull(childvo.getEntranceTimeStart())) {
            sql.append(" and ch.entranceTime >= ?");
            param.add(childvo.getEntranceTimeStart());
        }
        if (StringConverter.isNotNull(childvo.getEntranceTimeEnd())) {
            sql.append(" and ch.entranceTime <= ?");
            param.add(childvo.getEntranceTimeEnd() + Constant.LATEST_TIME_OF_DAY);
        }
        sql.append(this.getKgData("ch", Constant.SQL));
        sql.append(" order by ch.childNum");
        return sql.toString();
    }
}
