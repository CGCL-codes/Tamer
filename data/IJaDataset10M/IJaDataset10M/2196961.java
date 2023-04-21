package com.taobao.api.model;

import java.util.List;

/**
 * @author sulinchong.pt 2009-1-13 下午09:10:57
 *
 */
public class OrdersGetResponse extends TaobaoListResponse {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8776046162172069693L;

    private List<Order> orders;

    public OrdersGetResponse() {
        super();
    }

    public OrdersGetResponse(TaobaoResponse rsp) {
        super(rsp);
    }

    /**
	 * @return the orders
	 */
    public List<Order> getOrders() {
        return orders;
    }

    /**
	 * @param orders the orders to set
	 */
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
