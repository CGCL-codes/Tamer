package com.pk.platform.business.charge.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.pk.platform.business.charge.dao.IRefundDao;
import com.pk.platform.business.charge.vo.RefundVO;
import com.pk.platform.business.charge.vo.TuitionDetailVO;
import com.pk.platform.business.core.dao.impl.GenericDaoImpl;
import com.pk.platform.util.StringConverter;
import com.pk.platform.util.constant.Constant;
import com.pk.platform.util.page.ListPage;
import com.pk.platform.util.page.Pager;

public class RefundDaoImpl extends GenericDaoImpl implements IRefundDao {

    public ListPage queryRefund(RefundVO rvo, Pager pager) {
        StringBuffer sql = new StringBuffer();
        sql.append("select r.*, c.childNum, c.childName from refund r left join child c on r.childId = c.id where 1 = 1");
        List<Object> param = new ArrayList<Object>();
        if (StringConverter.isNotNull(rvo.getKindergartenId())) {
            sql.append(" and r.kgId = ?");
            param.add(rvo.getKindergartenId());
        }
        if (StringConverter.isNotNull(rvo.getChildNum())) {
            sql.append(" and c.childNum like ?");
            param.add("%" + rvo.getChildNum() + "%");
        }
        if (StringConverter.isNotNull(rvo.getChildName())) {
            sql.append(" and c.childName like ?");
            param.add("%" + rvo.getChildName() + "%");
        }
        if (StringConverter.isNotNull(rvo.getRefund().getRemark())) {
            sql.append(" and r.remark like ?");
            param.add("%" + rvo.getRefund().getRemark() + "%");
        }
        if (StringConverter.isNotNull(rvo.getRefundDateStart())) {
            sql.append(" and r.refundDate >= ?");
            param.add(rvo.getRefundDateStart());
        }
        if (StringConverter.isNotNull(rvo.getRefundDateEnd())) {
            sql.append(" and r.refundDate <= ?");
            param.add(rvo.getRefundDateEnd() + Constant.LATEST_TIME_OF_DAY);
        }
        sql.append(" order by r.refundDate");
        return this.getListPage(sql.toString(), param.toArray(), pager);
    }

    public ListPage queryRefundDetail(RefundVO rvo, Pager pager) {
        StringBuffer sql = new StringBuffer();
        sql.append("select rd.*, round(rd.amount, 0) as intAmount, ci.indexName from refund_detail rd left join charge_index ci on rd.ciId = ci.id where rd.rfId = ? order by rd.chargeDate");
        return this.getListPage(sql.toString(), new Object[] { rvo.getRefund().getId() }, pager);
    }

    public ListPage queryTuitionAndDetail(TuitionDetailVO tdvo, Pager pager) {
        StringBuffer sql = new StringBuffer();
        sql.append("select td.*, round(td.amount, 0) as intAmount, ci.indexName, t.payDate from tuition_detail td left join charge_index ci on td.chargeIndexId = ci.id, tuition t where td.tuitionId = t.id and t.isDel = 0");
        sql.append(" and t.childId = ?");
        List<Object> param = new ArrayList<Object>();
        param.add(tdvo.getChildId());
        if (StringConverter.isNotNull(tdvo.getIndexId())) {
            sql.append(" and td.chargeIndexId = ?");
            param.add(tdvo.getIndexId());
        }
        if (StringConverter.isNotNull(tdvo.getYearMonth())) {
            sql.append(" and date_format(td.tuitionDateStart, '%Y-%m') <= ? and date_format(td.tuitionDateEnd, '%Y-%m') >= ?");
            param.add(tdvo.getYearMonth());
            param.add(tdvo.getYearMonth());
        }
        if (StringConverter.isNotNull(tdvo.getPayDateStart())) {
            sql.append(" and t.payDate >= ?");
            param.add(tdvo.getPayDateStart());
        }
        if (StringConverter.isNotNull(tdvo.getPayDateEnd())) {
            sql.append(" and t.payDate <= ?");
            param.add(tdvo.getPayDateEnd());
        }
        sql.append(" order by td.tuitionDateStart");
        return this.getListPage(sql.toString(), param.toArray(), pager);
    }

    public List<Map<String, Object>> queryRefundDetailList(String refundId) {
        String sql = "select rd.*, round(rd.amount, 0) as intAmount, ci.indexName from refund_detail rd, charge_index ci where rd.ciId = ci.id and rd.rfId = ? order by rd.chargeDate";
        return this.getSjt().queryForList(sql, new Object[] { refundId });
    }
}
