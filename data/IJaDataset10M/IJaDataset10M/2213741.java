package com.javaeye.procurement.service;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.javaeye.order.dao.PurchaseOrderDAO;
import com.javaeye.procurement.dao.MaterialsRequestDAO;
import com.javaeye.order.dto.PurchaseOrder;
import com.javaeye.procurement.dto.MaterialsRequest;

public class MaterialsRequestService implements IMaterialsRequestService {

    protected static Log log = LogFactory.getLog(MaterialsRequestService.class);

    private MaterialsRequestDAO dao;

    private PurchaseOrderDAO orderDao;

    public void setDao(MaterialsRequestDAO dao) {
        this.dao = dao;
    }

    public void setOrderDao(PurchaseOrderDAO orderDao) {
        this.orderDao = orderDao;
    }

    public MaterialsRequest getMaterialsRequest(int requestId) {
        MaterialsRequest request = dao.getMaterialsRequest(requestId);
        if (request == null) {
            log.warn("数据库中未找到对应的物流需求信息，id：" + requestId);
        }
        return request;
    }

    public void saveMaterialsRequests(List<MaterialsRequest> requests, String orderId) {
        for (MaterialsRequest request : requests) {
            dao.saveMaterialsRequest(request);
        }
        PurchaseOrder order = orderDao.getPurchaseOrderInfo(orderId);
        order.setPlanMaterials(order.getPlanMaterials() + requests.size());
        orderDao.savePurchaseOrder(order);
    }

    public List<MaterialsRequest> getWaitingMaterialsRequests(MaterialsRequest condition) {
        List<MaterialsRequest> results = dao.getWaitingMaterialsRequests(condition);
        return results;
    }
}
