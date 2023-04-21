package com.passionatelife.familytree.webservice.po.client;

import org.apache.log4j.Logger;
import com.passionatelife.familytree.webservice.po.PurchaseOrderRequest;
import com.passionatelife.familytree.webservice.po.PurchaseOrderResponse;
import com.passionatelife.familytree.webservice.po.PurchaseOrderVO;
import com.passionatelife.familytree.webservice.po.services.PurchaseOrderService;

public class PurchaseOrderClient {

    private static Logger logger;

    private PurchaseOrderService poService;

    public PurchaseOrderClient() {
        logger = Logger.getLogger(this.getClass());
    }

    public PurchaseOrderResponse savePO(PurchaseOrderVO purchaseOrderVO) {
        logger.info("client");
        PurchaseOrderRequest poRequest = new PurchaseOrderRequest();
        poRequest.setPurchaseOrderVO(purchaseOrderVO);
        poRequest.setRequestingSystemInstanceId("123456");
        poRequest.setRequestingSystemName("Test System");
        return poService.savePO(poRequest);
    }

    public PurchaseOrderService getPoService() {
        return poService;
    }

    public void setPoService(PurchaseOrderService poService) {
        this.poService = poService;
    }
}
