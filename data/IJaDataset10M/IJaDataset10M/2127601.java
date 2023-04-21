package org.fao.waicent.kids.giews.dao.impl;

import java.util.ArrayList;
import org.fao.waicent.kids.giews.dao.ChartsDataDAO;
import org.fao.waicent.kids.giews.dao.bean.ChartsData;
import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;

public class ChartsDataDAOImpl extends SqlMapDaoTemplate implements ChartsDataDAO {

    public ChartsDataDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }

    public ArrayList getChartsDataListByChartId(Integer chart_id) {
        return (ArrayList) super.queryForList("charts_data.getChartsDataListByChartId", chart_id);
    }

    public void insert(ChartsData record) {
        insert("charts_data.insert", record);
    }

    public Integer getNextID() {
        return (Integer) super.queryForObject("charts_data.getNextID", null);
    }

    public void update(ChartsData record) {
        insert("charts_data.update", record);
    }
}
