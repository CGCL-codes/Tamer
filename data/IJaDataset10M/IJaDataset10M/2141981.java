package cn.ekuma.epos.remote.hessian.service;

import java.sql.SQLException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import cn.ekuma.data.dao.I_DataLogic;
import cn.ekuma.epos.datalogic.I_DataLogicCustomers;
import cn.ekuma.epos.datalogic.define.DataLogicCustomers;

public class DataLogicCustomerServlet extends AbstractDataLogicServlet {

    @Override
    protected I_DataLogic createService(ServletConfig config) throws ServletException {
        DataLogicCustomers dataLogic = new DataLogicCustomers();
        try {
            dataLogic.init(getSession(config), pmf);
        } catch (SQLException e) {
            throw new ServletException("Error configuring service ");
        }
        return dataLogic;
    }

    @Override
    protected Class getDataLogicClass() {
        return I_DataLogicCustomers.class;
    }
}
