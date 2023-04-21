package com.kongur.network.erp.tc.service;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.kongur.network.erp.domain.tc.TradeOrderDO;
import com.kongur.network.erp.domain.tc.TradeOrderLogisticsDO;
import com.kongur.network.erp.domain.tc.TradeOrderPayDO;
import com.kongur.network.erp.exception.tc.TcException;
import com.kongur.network.erp.query.tc.OrderQuery;
import com.kongur.network.erp.query.tc.OrderQueryOption;
import com.kongur.network.erp.query.tc.RefundOrderQuery;
import com.kongur.network.erp.service.tc.TcService;
import com.kongur.network.metal.demo.test.TestBase;

public class TestTcService extends TestBase {

    @Autowired
    private TcService tcService;

    public void testSelectByTradeOrderId() throws TcException {
        TradeOrderDO tradeOrderDO = tcService.getTradeOrderDOById(84556L);
        System.out.println(tradeOrderDO);
    }

    public void testSelectByQuery() throws TcException {
        OrderQuery query = new OrderQuery();
        OrderQueryOption orderQueryOption = new OrderQueryOption();
        orderQueryOption.setIncludeLogistics(true);
        orderQueryOption.setIncludePay(true);
        query.setOrderQueryOption(orderQueryOption);
        query.setSellerNick("zhangsan");
        tcService.queryTradeOrders(query);
        System.out.println(query.getData());
    }

    @Test
    public void testCreateTradeOrder() throws TcException {
        TradeOrderDO tradeOrder = createTradeOrder();
        TradeOrderLogisticsDO logistics = createLogistics();
        TradeOrderPayDO pay = createPay();
        tradeOrder.setTradeOrderLogisticsDO(logistics);
        tradeOrder.setTradeOrderPayDO(pay);
        tcService.createTradeOrder(tradeOrder);
    }

    private TradeOrderPayDO createPay() {
        TradeOrderPayDO pay = new TradeOrderPayDO();
        pay.setActualTotalFee(10033L);
        pay.setAlipaySellerId("4444444444444444");
        pay.setOutPayId("5341444788944444111");
        pay.setPayStatus(1);
        return pay;
    }

    private TradeOrderLogisticsDO createLogistics() {
        TradeOrderLogisticsDO logistics = new TradeOrderLogisticsDO();
        logistics.setReceiverName("����");
        logistics.setReceiverMobile("13888888888");
        logistics.setReceiverState("3306");
        logistics.setReceiverCity("33061");
        logistics.setReceiverDistrict("30060101");
        logistics.setReceiverZip("310000");
        logistics.setReceiverAddress("����·����ʱ��");
        return logistics;
    }

    private TradeOrderDO createTradeOrder() {
        TradeOrderDO tradeOrder = new TradeOrderDO();
        tradeOrder.setSellerNick("zhangsan");
        tradeOrder.setOrderStructure(1);
        tradeOrder.setSubOrderList(createSubOrderList());
        return tradeOrder;
    }

    private List<TradeOrderDO> createSubOrderList() {
        List<TradeOrderDO> subOrderList = new ArrayList<TradeOrderDO>();
        for (int i = 0; i < 2; i++) {
            TradeOrderDO subOrder = new TradeOrderDO();
            subOrder.setSellerNick("zhangsan");
            subOrder.setOrderStructure(2);
            subOrderList.add(subOrder);
        }
        return subOrderList;
    }

    public void testQueryOrderRefund() {
        RefundOrderQuery query = new RefundOrderQuery();
        query.setSellerNick("zhangsan");
        query.setBuyerNick("lis");
        System.out.println(query.getData());
    }
}
