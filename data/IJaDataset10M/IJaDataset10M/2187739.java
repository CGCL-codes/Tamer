package com.kongur.network.erp.tc;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import com.kongur.network.erp.dao.tc.TradeOrderLogisticsDAO;
import com.kongur.network.erp.domain.tc.TradeOrderLogisticsDO;
import com.kongur.network.metal.demo.test.TestBase;

/**
 * 
 * transactionTemplate
 * 
 * @author zhengwei
 *
 */
public class TestTransaction extends TestBase {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private TradeOrderLogisticsDAO tradeOrderLogisticsDAO;

    @Test
    public void test() {
        System.out.println("in test()");
    }

    public void testTrans() {
        transactionTemplate.execute(new TransactionCallback<Object>() {

            @Override
            public Object doInTransaction(TransactionStatus status) {
                TradeOrderLogisticsDO logistics = createLogistics();
                try {
                    tradeOrderLogisticsDAO.insertTradeOrderLogistics(logistics);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });
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
        logistics.setTradeOrderId(9999999L);
        return logistics;
    }
}
