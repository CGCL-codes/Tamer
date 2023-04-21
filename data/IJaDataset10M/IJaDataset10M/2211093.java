package org.fao.waicent.kids.giews.dao;

import org.fao.waicent.kids.giews.dao.bean.ChartsData_4Dimension;

public interface ChartsData_4DimensionDAO {

    public Object getChartDataParameters(Integer chart_data_id);

    public void insert(ChartsData_4Dimension record);

    public void update(ChartsData_4Dimension record);
}
