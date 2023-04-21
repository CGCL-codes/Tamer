package cn.langhua.opencms.ofbiz.rmi.order;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.ModelService;
import org.ofbiz.service.ServiceUtil;
import org.opencms.main.CmsLog;
import cn.langhua.opencms.ofbiz.rmi.CmsOFBizRemoteClient;

/**
 * Order Helper - Helper Methods For Non-Read Actions
 */
public class OrderChangeHelper {

    public static final String module = OrderChangeHelper.class.getName();

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(OrderChangeHelper.class);

    public static boolean approveOrder(CmsOFBizRemoteClient remoteClient, GenericValue userLogin, String orderId) {
        return approveOrder(remoteClient, userLogin, orderId, false);
    }

    public static boolean approveOrder(CmsOFBizRemoteClient remoteClient, GenericValue userLogin, String orderId, boolean holdOrder) {
        GenericValue productStore = OrderReadHelper.getProductStoreFromOrder(remoteClient, orderId);
        if (productStore == null) {
            throw new IllegalArgumentException("Could not find ProductStore for orderId [" + orderId + "], cannot approve order.");
        }
        String HEADER_STATUS = "ORDER_PROCESSING";
        String ITEM_STATUS = "ITEM_CREATED";
        String DIGITAL_ITEM_STATUS = "ITEM_APPROVED";
        Map fields = productStore.getAllFields();
        if (!holdOrder) {
            if (fields.get("headerApprovedStatus") != null) {
                HEADER_STATUS = (String) fields.get("headerApprovedStatus");
            }
            if (fields.get("itemApprovedStatus") != null) {
                ITEM_STATUS = (String) fields.get("itemApprovedStatus");
            }
            if (fields.get("digitalItemApprovedStatus") != null) {
                DIGITAL_ITEM_STATUS = (String) fields.get("digitalItemApprovedStatus");
            }
        }
        try {
            OrderChangeHelper.orderStatusChanges(remoteClient, userLogin, orderId, HEADER_STATUS, "ITEM_CREATED", ITEM_STATUS, DIGITAL_ITEM_STATUS);
            OrderChangeHelper.releaseInitialOrderHold(remoteClient, orderId);
        } catch (GenericServiceException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Service invocation error, status changes were not updated for order #" + orderId, e);
            }
            return false;
        } catch (RemoteException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Service invocation error, status changes were not updated for order #" + orderId, e);
            }
            return false;
        }
        return true;
    }

    public static boolean rejectOrder(CmsOFBizRemoteClient remoteClient, GenericValue userLogin, String orderId) {
        GenericValue productStore = OrderReadHelper.getProductStoreFromOrder(remoteClient, orderId);
        String HEADER_STATUS = "ORDER_REJECTED";
        String ITEM_STATUS = "ITEM_REJECTED";
        Map fields = productStore.getAllFields();
        if (fields.get("headerDeclinedStatus") != null) {
            HEADER_STATUS = (String) fields.get("headerDeclinedStatus");
        }
        if (fields.get("itemDeclinedStatus") != null) {
            ITEM_STATUS = (String) fields.get("itemDeclinedStatus");
        }
        try {
            OrderChangeHelper.orderStatusChanges(remoteClient, userLogin, orderId, HEADER_STATUS, null, ITEM_STATUS, null);
            OrderChangeHelper.cancelInventoryReservations(remoteClient, userLogin, orderId);
            OrderChangeHelper.releasePaymentAuthorizations(remoteClient, userLogin, orderId);
            OrderChangeHelper.releaseInitialOrderHold(remoteClient, orderId);
        } catch (GenericServiceException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Service invocation error, status changes were not updated for order #" + orderId, e);
            }
            return false;
        } catch (RemoteException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Service invocation error, status changes were not updated for order #" + orderId, e);
            }
            return false;
        }
        return true;
    }

    public static boolean completeOrder(CmsOFBizRemoteClient remoteClient, GenericValue userLogin, String orderId) {
        try {
            OrderChangeHelper.createReceivedPayments(remoteClient, userLogin, orderId);
            OrderChangeHelper.createOrderInvoice(remoteClient, userLogin, orderId);
            OrderChangeHelper.orderStatusChanges(remoteClient, userLogin, orderId, "ORDER_COMPLETED", "ITEM_APPROVED", "ITEM_COMPLETED", null);
        } catch (GenericEntityException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e);
            }
            return false;
        } catch (GenericServiceException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e);
            }
            return false;
        } catch (RemoteException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e);
            }
            return false;
        }
        return true;
    }

    public static boolean cancelOrder(CmsOFBizRemoteClient remoteClient, GenericValue userLogin, String orderId) {
        GenericValue productStore = OrderReadHelper.getProductStoreFromOrder(remoteClient, orderId);
        String HEADER_STATUS = "ORDER_CANCELLED";
        String ITEM_STATUS = "ITEM_CANCELLED";
        Map fields = productStore.getAllFields();
        if (fields.get("headerCancelStatus") != null) {
            HEADER_STATUS = (String) fields.get("headerCancelStatus");
        }
        if (fields.get("itemCancelStatus") != null) {
            ITEM_STATUS = (String) fields.get("itemCancelStatus");
        }
        try {
            OrderChangeHelper.orderStatusChanges(remoteClient, userLogin, orderId, HEADER_STATUS, null, ITEM_STATUS, null);
            OrderChangeHelper.cancelInventoryReservations(remoteClient, userLogin, orderId);
            OrderChangeHelper.releasePaymentAuthorizations(remoteClient, userLogin, orderId);
            OrderChangeHelper.releaseInitialOrderHold(remoteClient, orderId);
        } catch (GenericServiceException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Service invocation error, status changes were not updated for order #" + orderId, e);
            }
            return false;
        } catch (RemoteException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Service invocation error, status changes were not updated for order #" + orderId, e);
            }
            return false;
        }
        return true;
    }

    public static void orderStatusChanges(CmsOFBizRemoteClient remoteClient, GenericValue userLogin, String orderId, String orderStatus, String fromItemStatus, String toItemStatus, String digitalItemStatus) throws GenericServiceException, RemoteException {
        Map statusFields = UtilMisc.toMap("orderId", orderId, "statusId", orderStatus, "userLogin", userLogin);
        Map statusResult = remoteClient.runSync("changeOrderStatus", statusFields);
        if (statusResult.containsKey(ModelService.ERROR_MESSAGE)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Problems adjusting order header status for order #" + orderId);
            }
        }
        Map itemStatusFields = UtilMisc.toMap("orderId", orderId, "statusId", toItemStatus, "userLogin", userLogin);
        if (fromItemStatus != null) {
            itemStatusFields.put("fromStatusId", fromItemStatus);
        }
        Map itemStatusResult = remoteClient.runSync("changeOrderItemStatus", itemStatusFields);
        if (itemStatusResult.containsKey(ModelService.ERROR_MESSAGE)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Problems adjusting order header status for order #" + orderId);
            }
        }
        if (digitalItemStatus != null && !digitalItemStatus.equals(toItemStatus)) {
            GenericValue orderHeader = null;
            try {
                orderHeader = remoteClient.findByPrimaryKey("OrderHeader", UtilMisc.toMap("orderId", orderId));
            } catch (GenericEntityException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("ERROR: Unable to get OrderHeader for OrderID : " + orderId, e);
                }
            }
            if (orderHeader != null) {
                List orderItems = null;
                try {
                    orderItems = remoteClient.getRelated(orderHeader, "OrderItem");
                } catch (GenericEntityException e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("ERROR: Unable to get OrderItem records for OrderHeader : " + orderId, e);
                    }
                }
                if (orderItems != null && orderItems.size() > 0) {
                    Iterator oii = orderItems.iterator();
                    while (oii.hasNext()) {
                        GenericValue orderItem = (GenericValue) oii.next();
                        Map fields = orderItem.getAllFields();
                        String orderItemSeqId = (String) fields.get("orderItemSeqId");
                        GenericValue product = null;
                        try {
                            product = remoteClient.getRelatedOne("Product", orderItem);
                        } catch (GenericEntityException e) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("ERROR: Unable to get Product record for OrderItem : " + orderId + "/" + orderItemSeqId, e);
                            }
                        }
                        if (product != null) {
                            GenericValue productType = null;
                            try {
                                productType = remoteClient.getRelatedOne("ProductType", product);
                            } catch (GenericEntityException e) {
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug("ERROR: Unable to get ProductType from Product : " + product, e);
                                }
                            }
                            if (productType != null) {
                                fields = productType.getAllFields();
                                String isDigital = (String) fields.get("isDigital");
                                if (isDigital != null && "Y".equalsIgnoreCase(isDigital)) {
                                    Map digitalStatusFields = UtilMisc.toMap("orderId", orderId, "orderItemSeqId", orderItemSeqId, "statusId", digitalItemStatus, "userLogin", userLogin);
                                    Map digitalStatusChange = remoteClient.runSync("changeOrderItemStatus", digitalStatusFields);
                                    if (ModelService.RESPOND_ERROR.equals(digitalStatusChange.get(ModelService.RESPONSE_MESSAGE))) {
                                        if (LOG.isDebugEnabled()) {
                                            LOG.debug("Problems with digital product status change : " + product);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void cancelInventoryReservations(CmsOFBizRemoteClient remoteClient, GenericValue userLogin, String orderId) throws GenericServiceException, RemoteException {
        Map cancelInvFields = UtilMisc.toMap("orderId", orderId, "userLogin", userLogin);
        Map cancelInvResult = remoteClient.runSync("cancelOrderInventoryReservation", cancelInvFields);
        if (ModelService.RESPOND_ERROR.equals(cancelInvResult.get(ModelService.RESPONSE_MESSAGE))) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Problems reversing inventory reservations for order #" + orderId);
            }
        }
    }

    public static void releasePaymentAuthorizations(CmsOFBizRemoteClient remoteClient, GenericValue userLogin, String orderId) throws GenericServiceException, RemoteException {
        Map releaseFields = UtilMisc.toMap("orderId", orderId, "userLogin", userLogin);
        Map releaseResult = remoteClient.runSync("releaseOrderPayments", releaseFields);
        if (ModelService.RESPOND_ERROR.equals(releaseResult.get(ModelService.RESPONSE_MESSAGE))) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Problems reversing inventory reservations for order #" + orderId);
            }
        }
    }

    public static void createReceivedPayments(CmsOFBizRemoteClient remoteClient, GenericValue userLogin, String orderId) throws GenericEntityException, GenericServiceException, RemoteException {
        GenericValue orderHeader = null;
        try {
            orderHeader = remoteClient.findByPrimaryKey("OrderHeader", UtilMisc.toMap("orderId", orderId));
        } catch (GenericEntityException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e);
            }
        }
        if (orderHeader != null) {
            OrderReadHelper orh = new OrderReadHelper(remoteClient, orderHeader);
            GenericValue btparty = orh.getBillToParty(remoteClient);
            String partyId = "_NA_";
            if (btparty != null) {
                Map fields = btparty.getAllFields();
                partyId = (String) fields.get("partyId");
            }
            List opps = orh.getPaymentPreferences(remoteClient);
            Iterator oppi = opps.iterator();
            while (oppi.hasNext()) {
                GenericValue opp = (GenericValue) oppi.next();
                Map fields = opp.getAllFields();
                if ("PAYMENT_RECEIVED".equals((String) fields.get("statusId"))) {
                    List payments = orh.getOrderPayments(remoteClient, opp);
                    if (payments == null || payments.size() == 0) {
                        Map results = remoteClient.runSync("createPaymentFromPreference", UtilMisc.toMap("userLogin", userLogin, "orderPaymentPreferenceId", (String) fields.get("orderPaymentPreferenceId"), "paymentRefNum", UtilDateTime.nowTimestamp().toString(), "paymentFromId", partyId));
                        if (results.get(ModelService.RESPONSE_MESSAGE).equals(ModelService.RESPOND_ERROR)) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug((String) results.get(ModelService.ERROR_MESSAGE));
                            }
                        }
                    }
                }
            }
        }
    }

    public static void createOrderInvoice(CmsOFBizRemoteClient remoteClient, GenericValue userLogin, String orderId) throws GenericServiceException, RemoteException {
        GenericValue orderHeader = null;
        try {
            orderHeader = remoteClient.findByPrimaryKey("OrderHeader", UtilMisc.toMap("orderId", orderId));
        } catch (GenericEntityException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e);
            }
        }
        if (orderHeader != null) {
            OrderReadHelper orh = new OrderReadHelper(remoteClient, orderHeader);
            List items = orh.getOrderItems(remoteClient);
            Map serviceParam = UtilMisc.toMap("orderId", orderId, "billItems", items, "userLogin", userLogin);
            Map serviceRes = remoteClient.runSync("createInvoiceForOrder", serviceParam);
            if (ServiceUtil.isError(serviceRes)) {
                throw new GenericServiceException(ServiceUtil.getErrorMessage(serviceRes));
            }
        }
    }

    public static boolean releaseInitialOrderHold(CmsOFBizRemoteClient remoteClient, String orderId) {
        List workEfforts = null;
        try {
            workEfforts = remoteClient.findByAnd("WorkEffort", UtilMisc.toMap("currentStatusId", "WF_SUSPENDED", "sourceReferenceId", orderId));
        } catch (GenericEntityException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Problems getting WorkEffort with order ref number: " + orderId, e);
            }
            return false;
        }
        return false;
    }

    public static boolean abortOrderProcessing(CmsOFBizRemoteClient remoteClient, String orderId) {
        if (LOG.isInfoEnabled()) {
            LOG.info("Aborting workflow for order " + orderId);
        }
        GenericValue workEffort = null;
        try {
            List workEfforts = remoteClient.findByAnd("WorkEffort", UtilMisc.toMap("workEffortTypeId", "WORK_FLOW", "sourceReferenceId", orderId));
            if (workEfforts != null && workEfforts.size() > 1) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("More then one workflow found for defined order: " + orderId);
                }
            }
            workEffort = remoteClient.getFirst(workEfforts);
        } catch (GenericEntityException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Problems getting WorkEffort with order ref number: " + orderId);
            }
            return false;
        }
        return false;
    }
}
