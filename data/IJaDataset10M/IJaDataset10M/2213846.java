package cn.ekuma.erp.server.datalogic;

import cn.ekuma.data.dao.DataLogic;
import cn.ekuma.epos.online.bean.ClientCustomer;
import cn.ekuma.erp.client.datalogic.I_DataLogicSales;
import cn.ekuma.erp.server.util.SessionUtil;
import cn.ekuma.odbo.client.I_DataLogic;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.openbravo.data.basic.BasicException;

public class I_DataLogicSalesImpl extends RemoteServiceServlet implements I_DataLogicSales {

    @Override
    public ClientCustomer findCustomerByCard(String sessionId, String clientId, String card) throws BasicException {
        return new ClientCustomer();
    }
}
