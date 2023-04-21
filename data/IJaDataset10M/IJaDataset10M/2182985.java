package com.pk.platform.business.stock.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.pk.platform.business.core.dao.impl.GenericDaoImpl;
import com.pk.platform.business.stock.dao.IStockDao;
import com.pk.platform.business.stock.vo.FetchRecordVO;
import com.pk.platform.business.stock.vo.ImportRecordVO;
import com.pk.platform.business.stock.vo.StockObjVO;
import com.pk.platform.util.StringConverter;
import com.pk.platform.util.constant.Constant;
import com.pk.platform.util.page.ListPage;
import com.pk.platform.util.page.Pager;

public class StockDaoImpl extends GenericDaoImpl implements IStockDao {

    public ListPage queryFetchRecord(FetchRecordVO frvo, Pager pager) {
        StringBuffer sql = new StringBuffer();
        List<Object> param = new ArrayList<Object>();
        sql.append("select fr.*, so.objName, so.unit from fetch_record fr, stock_obj so where fr.objId = so.id");
        if (StringConverter.isNotNull(frvo.getKindergartenId())) {
            sql.append(" and fr.kgId = ?");
            param.add(frvo.getKindergartenId());
        }
        if (StringConverter.isNotNull(frvo.getObjId())) {
            sql.append(" and fr.objId = ?");
            param.add(frvo.getObjId());
        }
        if (StringConverter.isNotNull(frvo.getFr().getRemark())) {
            sql.append(" and fr.remark like ?");
            param.add("%" + frvo.getFr().getRemark() + "%");
        }
        if (StringConverter.isNotNull(frvo.getFetchTimeStart())) {
            sql.append(" and fr.fetchTime >= ?");
            param.add(frvo.getFetchTimeStart());
        }
        if (StringConverter.isNotNull(frvo.getFetchTimeEnd())) {
            sql.append(" and fr.fetchTime <= ?");
            param.add(frvo.getFetchTimeEnd() + Constant.LATEST_TIME_OF_DAY);
        }
        sql.append(this.getKgData("fr", Constant.SQL));
        sql.append(" order by fr.fetchTime desc");
        return this.getListPage(sql.toString(), param.toArray(), pager);
    }

    public ListPage queryImportRecord(ImportRecordVO irvo, Pager pager) {
        StringBuffer sql = new StringBuffer();
        List<Object> param = new ArrayList<Object>();
        sql.append("select ir.*, so.objName, so.unit from import_record ir, stock_obj so where ir.objId = so.id");
        if (StringConverter.isNotNull(irvo.getKindergartenId())) {
            sql.append(" and ir.kgId = ?");
            param.add(irvo.getKindergartenId());
        }
        if (StringConverter.isNotNull(irvo.getObjId())) {
            sql.append(" and ir.objId = ?");
            param.add(irvo.getObjId());
        }
        if (StringConverter.isNotNull(irvo.getIr().getRemark())) {
            sql.append(" and ir.remark like ?");
            param.add("%" + irvo.getIr().getRemark() + "%");
        }
        if (StringConverter.isNotNull(irvo.getImportTimeStart())) {
            sql.append(" and ir.importTime >= ?");
            param.add(irvo.getImportTimeStart());
        }
        if (StringConverter.isNotNull(irvo.getImportTimeEnd())) {
            sql.append(" and ir.importTime <= ?");
            param.add(irvo.getImportTimeEnd() + Constant.LATEST_TIME_OF_DAY);
        }
        sql.append(this.getKgData("ir", Constant.SQL));
        sql.append(" order by ir.importTime desc");
        return this.getListPage(sql.toString(), param.toArray(), pager);
    }

    public ListPage queryStockObj(StockObjVO objvo, Pager pager) {
        StringBuffer sql = new StringBuffer();
        List<Object> param = new ArrayList<Object>();
        sql.append("select so.* from stock_obj so where 1 = 1 and so.isDel = 0");
        if (StringConverter.isNotNull(objvo.getKindergartenId())) {
            sql.append(" and so.kgId = ?");
            param.add(objvo.getKindergartenId());
        }
        if (StringConverter.isNotNull(objvo.getStockObj().getObjName())) {
            sql.append(" and so.objName like ?");
            param.add("%" + objvo.getStockObj().getObjName() + "%");
        }
        if (objvo.getPriceStart() != null) {
            sql.append(" and so.price >= ?");
            param.add(objvo.getPriceStart());
        }
        if (objvo.getPriceEnd() != null) {
            sql.append(" and so.price <= ?");
            param.add(objvo.getPriceEnd());
        }
        if (objvo.getStockCountStart() != null) {
            sql.append(" and so.stockCount >= ?");
            param.add(objvo.getStockCountStart());
        }
        if (objvo.getStockCountEnd() != null) {
            sql.append(" and so.stockCount <= ?");
            param.add(objvo.getStockCountEnd());
        }
        sql.append(this.getKgData("so", Constant.SQL));
        return this.getListPage(sql.toString(), param.toArray(), pager);
    }

    public List<Map<String, Object>> queryStockObjList(String kgId) {
        String sql = "select id, objName from stock_obj where kgId = ? and isDel = 0";
        return this.getSjt().queryForList(sql, kgId);
    }
}
