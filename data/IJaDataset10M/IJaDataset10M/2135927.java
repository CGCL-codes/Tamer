package com.kongur.network.erp.support.tc.event;

import com.kongur.network.erp.domain.tc.TradeOrderDO;
import com.kongur.network.erp.enums.tc.OrderStatus;
import com.kongur.network.erp.enums.tc.OrderType;

/**
 * ���������¼�
 * 
 * @author zhengwei
 */
public class TradeOrderSingleEvent extends TradeOrderEvent {

    public TradeOrderSingleEvent(TradeOrderDO source) {
        super(source);
    }

    /**
     * 
     */
    private static final long serialVersionUID = -807081577304048192L;

    public TradeOrderDO getTradeOrder() {
        return (TradeOrderDO) getSource();
    }

    /**
     * ����״̬
     * 
     * @return
     */
    public OrderStatus getOrderStatus() {
        return OrderStatus.getByStatusValue(getTradeOrder().getOrderStatus());
    }

    /**
     * ��������
     * 
     * @return
     */
    public OrderType getOrderType() {
        return OrderType.getByTypeValue(getTradeOrder().getOrderType());
    }
}
