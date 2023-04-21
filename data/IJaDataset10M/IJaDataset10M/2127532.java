package com.gever.goa.dailyoffice.targetmng.dao.impl;

import com.gever.goa.dailyoffice.targetmng.dao.MonthSumDao;
import com.gever.goa.dailyoffice.targetmng.dao.MonthSumFactory;

/**
 * Title: �¶��ܽ�����๤����ʵ����
 * Description: �¶��ܽ�����๤����ʵ����
 * Copyright: Copyright (c) 2004
 * Company: �������
 * @author Hu.Walker
 * @version 1.0
 */
public class DefaultMonthSumFactory extends MonthSumFactory {

    public DefaultMonthSumFactory() {
    }

    public MonthSumDao createMonthSum(String dbData) {
        return new MonthSumDaoImpl(dbData);
    }
}
