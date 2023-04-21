package com.javaeye.delivery.service;

import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.javaeye.delivery.dao.OrderDAO;
import com.javaeye.order.dao.PurchaseOrderDAO;
import com.javaeye.order.dao.PurchaseOrderDetailDAO;
import com.javaeye.delivery.dao.ReasonDAO;
import com.javaeye.delivery.dto.CustomerOrder;
import com.javaeye.delivery.dto.OrderDetail;
import com.javaeye.delivery.dto.OrderDetailBatchInfo;
import com.javaeye.order.dto.PurchaseOrder;
import com.javaeye.order.dto.PurchaseOrderDetail;
import com.javaeye.store.dao.MaterialsSKUDetailDAO;
import com.javaeye.delivery.dto.Reason;
import com.javaeye.common.util.DateUtils;
import com.javaeye.common.util.ListUtil;
import com.javaeye.common.web.PageInfo;
import com.javaeye.delivery.web.OrderCondition;

public class OrderService implements IOrderService {

    private static Log log = LogFactory.getLog(OrderService.class);

    private OrderDAO dao;

    private PurchaseOrderDetailDAO purchaseOrderDetailDao;

    private PurchaseOrderDAO purchaseOrderDao;

    private ReasonDAO reasonDao;

    private MaterialsSKUDetailDAO materialsDao;

    public void setDao(OrderDAO dao) {
        this.dao = dao;
    }

    public OrderDAO getDao() {
        return dao;
    }

    public void setPurchaseOrderDetailDao(PurchaseOrderDetailDAO dao) {
        this.purchaseOrderDetailDao = dao;
    }

    public void setReasonDao(ReasonDAO reasonDao) {
        this.reasonDao = reasonDao;
    }

    public ReasonDAO getReasonDao() {
        return reasonDao;
    }

    public PurchaseOrderDetailDAO getPurchaseOrderDetailDao() {
        return purchaseOrderDetailDao;
    }

    public void setPurchaseOrderDao(PurchaseOrderDAO purchaseOrderDao) {
        this.purchaseOrderDao = purchaseOrderDao;
    }

    public PurchaseOrderDAO getPurchaseOrderDao() {
        return purchaseOrderDao;
    }

    public void setMaterialsDao(MaterialsSKUDetailDAO materialsDao) {
        this.materialsDao = materialsDao;
    }

    @SuppressWarnings("unchecked")
    public List getOrderList(OrderCondition condition, PageInfo pageInfo) {
        List<CustomerOrder> result = dao.getPageData(condition, pageInfo);
        if (result == null || result.size() == 0) {
            log.warn("数据库中未找到指定的出货单信息");
        }
        for (CustomerOrder order : result) {
            String status = String.valueOf(order.getStatus());
            order.setStatusMessage(ListUtil.orderStatusName(status));
            long count = reasonDao.getReasonCount(order.getId());
            order.setReasons(count);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<CustomerOrder> getOrderList(OrderCondition condition) {
        return getOrderList(condition, null);
    }

    public CustomerOrder getOrderBaseInfo(String orderId) {
        CustomerOrder order = dao.getOrderBaseInfo(orderId);
        if (order == null) {
            log.warn("数据库中未找到指定的出货单信息，出货单号：" + orderId);
            return new CustomerOrder();
        }
        Set<OrderDetail> details = order.getOrderDetails();
        for (OrderDetail detail : details) {
            int id = detail.getProductId();
            float acceptNumber = materialsDao.getMaterialsSKUDetail(id).getNumber();
            detail.setAcceptNumber((int) acceptNumber);
        }
        long count = reasonDao.getReasonCount(order.getId());
        order.setReasons(count);
        String status = String.valueOf(order.getStatus());
        order.setStatusMessage(ListUtil.orderStatusName(status));
        return order;
    }

    public void closeOrder(String orderId) {
        CustomerOrder order = dao.getOrderBaseInfo(orderId);
        order.setStatus(CustomerOrder.ORDER_STATES_CLOSED);
        dao.saveOrder(order);
    }

    public void saveApplyClose(String orderId, CustomerOrder order) {
        CustomerOrder dbOrder = dao.getOrderBaseInfo(orderId);
        dbOrder.setOldStatus(dbOrder.getStatus());
        dbOrder.setStatus(CustomerOrder.ORDER_STATES_WAIT_CLOSE);
        dbOrder.setCloseReason(order.getCloseReason());
        dao.saveOrder(dbOrder);
    }

    public void cancelCloseOrder(String orderId) {
        CustomerOrder dbOrder = dao.getOrderBaseInfo(orderId);
        dbOrder.setStatus(dbOrder.getOldStatus());
        dao.saveOrder(dbOrder);
    }

    public void closeApplyOrder(String orderId) {
        Reason reason = new Reason(Reason.REASON_CATEGORY_BUSINESS, orderId, "关闭了一个已经安排计划的出货单");
        getReasonDao().saveReason(reason);
        CustomerOrder dbOrder = dao.getOrderBaseInfo(orderId);
        dbOrder.setStatus(CustomerOrder.ORDER_STATES_CLOSED);
        dao.saveOrder(dbOrder);
        Set<OrderDetail> details = dbOrder.getOrderDetails();
        String purchaseOrderId = dbOrder.getPurchaseOrderId();
        int productId;
        PurchaseOrderDetail purchaseOrderdetail;
        for (OrderDetail detail : details) {
            productId = detail.getProductId();
            purchaseOrderdetail = getPurchaseOrderDetailDao().getPurchaseOrderDetail(purchaseOrderId, productId);
            purchaseOrderdetail.setPlanNumber(purchaseOrderdetail.getPlanNumber() - detail.getNumber());
            getPurchaseOrderDetailDao().savePurchaseOrderDetail(purchaseOrderdetail);
        }
        PurchaseOrder purchaseOrder = getPurchaseOrderDao().getPurchaseOrderInfo(purchaseOrderId);
        purchaseOrder.setStatus(CustomerOrder.ORDER_STATES_CREATED);
        getPurchaseOrderDao().savePurchaseOrder(purchaseOrder);
    }

    public void saveOrder(CustomerOrder order) {
        String purchaseOrderId = order.getPurchaseOrderId();
        int maxId = dao.getMaxOrderId(purchaseOrderId);
        order.setId(purchaseOrderId + "-" + String.format("%02d", maxId + 1));
        order.setStatus(CustomerOrder.ORDER_STATES_CREATED);
        order.setUpdateDate(DateUtils.now());
        dao.saveOrder(order);
    }

    @Override
    public void saveOrder(CustomerOrder order, List<OrderDetailBatchInfo> batchInfos) {
    }
}
