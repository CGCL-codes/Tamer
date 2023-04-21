package com.javaeye.delivery.web;

import java.util.List;
import com.javaeye.delivery.dto.CustomerOrder;
import com.javaeye.common.util.ListItem;
import com.javaeye.common.util.ListUtil;

public class BusinessAction extends OrderAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7408989332825417843L;

    private List<ListItem> logisticsTypeList;

    /**
	 * 查询需要业务确认的出货单列表
	 * @return
	 * @throws Exception
	 */
    public String queryBusinessList() throws Exception {
        getCondition().setStatus(new Integer[] { CustomerOrder.ORDER_STATES_PLAN });
        return queryOrderList();
    }

    /**
	 * 查询用于业务确认
	 * @return
	 * @throws Exception
	 */
    public String queryOrderForBusiness() throws Exception {
        logisticsTypeList = ListUtil.logisticsTypeList();
        return queryOrderBaseInfo();
    }

    /**
	 * 保存业务确认
	 * @return
	 * @throws Exception
	 */
    public String saveOrderBusiness() throws Exception {
        CustomerOrder order = getService().getOrderBaseInfo(getOrderId());
        order.setOutDate(getOrder().getOutDate());
        order.setLogisticsType(getOrder().getLogisticsType());
        order.setArriveDate(getOrder().getArriveDate());
        getService().saveOrder(order);
        return SUCCESS;
    }

    public void setLogisticsTypeList(List<ListItem> logisticsTypeList) {
        this.logisticsTypeList = logisticsTypeList;
    }

    public List<ListItem> getLogisticsTypeList() {
        return logisticsTypeList;
    }
}
