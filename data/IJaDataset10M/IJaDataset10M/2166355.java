package com.tieland.xunda.system.dao;

import org.picocontainer.annotations.Inject;
import com.tieland.xunda.common.jdbc.DBConnection;

/**
 * User: qiukx
 * Date: 2008-7-30
 * Company: Freshpower
 * Description:
 */
public class TestDaoImpl implements TestDao {

    @Inject
    private DBConnection dbc;

    public void testDBC() {
        dbc.getArr("select * from t_user");
    }
}
