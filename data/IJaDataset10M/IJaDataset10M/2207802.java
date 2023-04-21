package cn.langhua.opencms.ofbiz.rmi.order;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javolution.util.FastMap;
import javolution.util.FastList;
import org.apache.commons.logging.Log;
import org.ofbiz.base.util.GeneralRuntimeException;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilFormatOut;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilNumber;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.base.util.collections.ResourceBundleMapWrapper;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityConditionList;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import cn.langhua.opencms.ofbiz.rmi.CmsOFBizRemoteClient;
import cn.langhua.opencms.ofbiz.rmi.base.util.UtilProperties;
import cn.langhua.opencms.ofbiz.rmi.common.DataModelConstants;
import cn.langhua.opencms.ofbiz.rmi.product.store.ProductStoreWorker;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.ModelService;
import org.ofbiz.service.ServiceUtil;
import org.opencms.main.CmsLog;

/**
 * OrderReturnServices
 */
public class OrderReturnServices {

    public static final String module = OrderReturnServices.class.getName();

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(OrderReturnServices.class);

    public static final String resource = "OrderUiLabels";

    public static final String resource_error = "OrderErrorUiLabels";

    private static BigDecimal ZERO = new BigDecimal("0");

    private static int decimals = -1;

    private static int rounding = -1;

    static {
        decimals = UtilNumber.getBigDecimalScale("invoice.decimals");
        rounding = UtilNumber.getBigDecimalRoundingMode("invoice.rounding");
        if (decimals != -1) ZERO.setScale(decimals);
    }

    public static Map getReturnItemInitialCost(CmsOFBizRemoteClient remoteClient, Map context) {
        String returnId = (String) context.get("returnId");
        String returnItemSeqId = (String) context.get("returnItemSeqId");
        Map result = ServiceUtil.returnSuccess();
        result.put("initialItemCost", getReturnItemInitialCost(remoteClient, returnId, returnItemSeqId));
        return result;
    }

    public static Map getOrderAvailableReturnedTotal(CmsOFBizRemoteClient remoteClient, Map context) {
        String orderId = (String) context.get("orderId");
        OrderReadHelper orh = null;
        try {
            orh = new OrderReadHelper(remoteClient, orderId);
        } catch (IllegalArgumentException e) {
            return ServiceUtil.returnError(e.getMessage());
        }
        Double adj = (Double) context.get("adjustment");
        if (adj == null) {
            adj = new Double(0);
        }
        Boolean countNewReturnItems = (Boolean) context.get("countNewReturnItems");
        if (countNewReturnItems == null) {
            countNewReturnItems = Boolean.FALSE;
        }
        double returnTotal = orh.getOrderReturnedTotal(remoteClient, countNewReturnItems.booleanValue());
        double orderTotal = orh.getOrderGrandTotal(remoteClient);
        double available = orderTotal - returnTotal - adj.doubleValue();
        Map result = ServiceUtil.returnSuccess();
        result.put("availableReturnTotal", new Double(available));
        result.put("orderTotal", new Double(orderTotal));
        result.put("returnTotal", new Double(returnTotal));
        return result;
    }

    public static Double getReturnItemInitialCost(CmsOFBizRemoteClient remoteClient, String returnId, String returnItemSeqId) {
        if (remoteClient == null || returnId == null || returnItemSeqId == null) {
            throw new IllegalArgumentException("Method parameters cannot contain nulls");
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("Finding the initial item cost for return item : " + returnId + " / " + returnItemSeqId);
        }
        Double itemCost = new Double(0.00);
        GenericValue returnItem = null;
        try {
            returnItem = remoteClient.findByPrimaryKey("ReturnItem", UtilMisc.toMap("returnId", returnId, "returnItemSeqId", returnItemSeqId));
        } catch (GenericEntityException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e);
            }
            throw new GeneralRuntimeException(e.getMessage());
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("Return item value object - " + returnItem);
        }
        if (returnItem != null) {
            Map fields = returnItem.getAllFields();
            String orderId = (String) fields.get("orderId");
            String orderItemSeqId = (String) fields.get("orderItemSeqId");
            if (orderItemSeqId != null && orderId != null) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Found order item reference");
                }
                List itemIssue = null;
                try {
                    itemIssue = remoteClient.findByAnd("ItemIssuance", UtilMisc.toMap("orderId", orderId, "orderItemSeqId", orderItemSeqId));
                } catch (GenericEntityException e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(e);
                    }
                    throw new GeneralRuntimeException(e.getMessage());
                }
                if (itemIssue != null && itemIssue.size() > 0) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Found item issuance referece");
                    }
                    GenericValue issue = remoteClient.getFirst(itemIssue);
                    GenericValue inventoryItem = null;
                    try {
                        inventoryItem = remoteClient.getRelatedOne("InventoryItem", issue);
                    } catch (GenericEntityException e) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(e);
                        }
                        throw new GeneralRuntimeException(e.getMessage());
                    }
                    if (inventoryItem != null) {
                        if (LOG.isInfoEnabled()) {
                            LOG.info("Located inventory item - " + ((String) inventoryItem.getAllFields().get("inventoryItemId")));
                        }
                        if (inventoryItem.getAllFields().get("unitCost") != null) {
                            itemCost = (Double) inventoryItem.getAllFields().get("unitCost");
                        } else {
                            if (LOG.isInfoEnabled()) {
                                LOG.info("Found item cost; but cost was null. Returning default amount (0.00)");
                            }
                        }
                    }
                }
            }
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("Initial item cost - " + itemCost);
        }
        return itemCost;
    }

    private static Map sendReturnNotificationScreen(CmsOFBizRemoteClient remoteClient, Map context, String emailType) {
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String returnId = (String) context.get("returnId");
        Locale locale = (Locale) context.get("locale");
        GenericValue returnHeader = null;
        try {
            returnHeader = remoteClient.findByPrimaryKey("ReturnHeader", UtilMisc.toMap("returnId", returnId));
        } catch (GenericEntityException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e);
            }
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderErrorUnableToGetReturnHeaderForID", UtilMisc.toMap("returnId", returnId), locale));
        }
        List returnItems = null;
        try {
            returnItems = remoteClient.getRelated(returnHeader, "ReturnItem");
        } catch (GenericEntityException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e);
            }
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderErrorUnableToGetReturnItemRecordsFromReturnHeader", locale));
        }
        String productStoreId = null;
        String emailAddress = null;
        if (returnItems != null && returnItems.size() > 0) {
            GenericValue firstItem = remoteClient.getFirst(returnItems);
            GenericValue orderHeader = null;
            try {
                orderHeader = remoteClient.getRelatedOne("OrderHeader", firstItem);
            } catch (GenericEntityException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(e);
                }
                return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderErrorUnableToGetOrderHeaderFromReturnItem", locale));
            }
            if (orderHeader != null && UtilValidate.isNotEmpty((String) orderHeader.getAllFields().get("productStoreId"))) {
                OrderReadHelper orh = new OrderReadHelper(remoteClient, orderHeader);
                productStoreId = orh.getProductStoreId();
                emailAddress = orh.getOrderEmailString(remoteClient);
            }
        }
        if (productStoreId != null && productStoreId.length() > 0) {
            Map sendMap = FastMap.newInstance();
            GenericValue productStoreEmail = null;
            try {
                productStoreEmail = remoteClient.findByPrimaryKey("ProductStoreEmailSetting", UtilMisc.toMap("productStoreId", productStoreId, "emailType", emailType));
            } catch (GenericEntityException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(e);
                }
            }
            if (productStoreEmail != null && emailAddress != null) {
                Map fields = productStoreEmail.getAllFields();
                String bodyScreenLocation = (String) fields.get("bodyScreenLocation");
                if (UtilValidate.isEmpty(bodyScreenLocation)) {
                    bodyScreenLocation = ProductStoreWorker.getDefaultProductStoreEmailScreenLocation(emailType);
                }
                sendMap.put("bodyScreenUri", bodyScreenLocation);
                ResourceBundleMapWrapper uiLabelMap = (ResourceBundleMapWrapper) UtilProperties.getResourceBundleMap("EcommerceUiLabels", locale);
                uiLabelMap.addBottomResourceBundle("OrderUiLabels");
                uiLabelMap.addBottomResourceBundle("CommonUiLabels");
                Map bodyParameters = UtilMisc.toMap("returnHeader", returnHeader, "returnItems", returnItems, "uiLabelMap", uiLabelMap, "locale", locale);
                sendMap.put("bodyParameters", bodyParameters);
                sendMap.put("subject", (String) fields.get("subject"));
                sendMap.put("contentType", fields.get("contentType"));
                sendMap.put("sendFrom", fields.get("fromAddress"));
                sendMap.put("sendCc", fields.get("ccAddress"));
                sendMap.put("sendBcc", fields.get("bccAddress"));
                sendMap.put("sendTo", emailAddress);
                sendMap.put("userLogin", userLogin);
                Map sendResp = null;
                try {
                    sendResp = remoteClient.runSync("sendMailFromScreen", sendMap);
                } catch (GenericServiceException e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Problem sending mail", e);
                    }
                    return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemSendingEmail", locale));
                } catch (RemoteException e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Problem sending mail", e);
                    }
                    return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemSendingEmail", locale));
                }
                if (sendResp != null && !ServiceUtil.isError(sendResp)) {
                    sendResp.put("emailType", emailType);
                    return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemSendingEmail", locale), null, null, sendResp);
                }
                return sendResp;
            }
        }
        return ServiceUtil.returnFailure("No valid email setting for store");
    }

    public static Map sendReturnAcceptNotification(CmsOFBizRemoteClient remoteClient, Map context) {
        return sendReturnNotificationScreen(remoteClient, context, "PRDS_RTN_ACCEPT");
    }

    public static Map sendReturnCompleteNotification(CmsOFBizRemoteClient remoteClient, Map context) {
        return sendReturnNotificationScreen(remoteClient, context, "PRDS_RTN_COMPLETE");
    }

    public static Map sendReturnCancelNotification(CmsOFBizRemoteClient remoteClient, Map context) {
        return sendReturnNotificationScreen(remoteClient, context, "PRDS_RTN_CANCEL");
    }

    public static Map getReturnableQuantity(CmsOFBizRemoteClient remoteClient, Map context) {
        GenericValue orderItem = (GenericValue) context.get("orderItem");
        GenericValue product = null;
        Locale locale = (Locale) context.get("locale");
        if (orderItem.getAllFields().get("productId") != null) {
            try {
                product = remoteClient.getRelatedOne("Product", orderItem);
            } catch (GenericEntityException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("ERROR: Unable to get Product from OrderItem", e);
                }
            }
        }
        boolean returnable = true;
        if (product != null && product.getAllFields().get("returnable") != null && "N".equalsIgnoreCase((String) product.getAllFields().get("returnable"))) {
            returnable = false;
        }
        if (product != null && product.getAllFields().get("supportDiscontinuationDate") != null && !UtilDateTime.nowTimestamp().before((Timestamp) product.getAllFields().get("supportDiscontinuationDate"))) {
            returnable = false;
        }
        String itemStatus = (String) orderItem.getAllFields().get("statusId");
        double orderQty = ((Double) orderItem.getAllFields().get("quantity")).doubleValue();
        if (orderItem.getAllFields().get("cancelQuantity") != null) {
            orderQty -= ((Double) orderItem.getAllFields().get("cancelQuantity")).doubleValue();
        }
        double returnableQuantity = 0.00;
        if (returnable && (itemStatus.equals("ITEM_APPROVED") || itemStatus.equals("ITEM_COMPLETED"))) {
            List returnedItems = null;
            try {
                returnedItems = remoteClient.getRelated(orderItem, "ReturnItem");
            } catch (GenericEntityException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(e);
                }
                return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderErrorUnableToGetReturnItemInformation", locale));
            }
            if (returnedItems == null || returnedItems.size() == 0) {
                returnableQuantity = orderQty;
            } else {
                double returnedQty = 0.00;
                Iterator ri = returnedItems.iterator();
                while (ri.hasNext()) {
                    GenericValue returnItem = (GenericValue) ri.next();
                    GenericValue returnHeader = null;
                    try {
                        returnHeader = remoteClient.getRelatedOne("ReturnHeader", returnItem);
                    } catch (GenericEntityException e) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(e);
                        }
                        return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderErrorUnableToGetReturnHeaderFromItem", locale));
                    }
                    String returnStatus = (String) returnHeader.getAllFields().get("statusId");
                    if (!returnStatus.equals("RETURN_CANCELLED")) {
                        returnedQty += ((Double) returnItem.getAllFields().get("returnQuantity")).doubleValue();
                    }
                }
                if (returnedQty < orderQty) {
                    returnableQuantity = orderQty - returnedQty;
                }
            }
        }
        Map result = ServiceUtil.returnSuccess();
        result.put("returnableQuantity", new Double(returnableQuantity));
        result.put("returnablePrice", orderItem.getAllFields().get("unitPrice"));
        return result;
    }

    public static Map getReturnableItems(CmsOFBizRemoteClient remoteClient, Map context) {
        String orderId = (String) context.get("orderId");
        Locale locale = (Locale) context.get("locale");
        GenericValue orderHeader = null;
        try {
            orderHeader = remoteClient.findByPrimaryKey("OrderHeader", UtilMisc.toMap("orderId", orderId));
        } catch (GenericEntityException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e);
            }
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderErrorUnableToGetReturnItemInformation", locale));
        }
        Map returnable = new HashMap();
        if (orderHeader != null) {
            EntityConditionList whereConditions = new EntityConditionList(UtilMisc.toList(new EntityExpr("orderId", EntityOperator.EQUALS, (String) orderHeader.getAllFields().get("orderId")), new EntityExpr("orderItemStatusId", EntityOperator.IN, UtilMisc.toList("ITEM_APPROVED", "ITEM_COMPLETED"))), EntityOperator.AND);
            EntityConditionList havingConditions = new EntityConditionList(UtilMisc.toList(new EntityExpr("quantityIssued", EntityOperator.GREATER_THAN, new Double(0))), EntityOperator.AND);
            List orderItemQuantitiesIssued = null;
            try {
                orderItemQuantitiesIssued = remoteClient.findByCondition("OrderItemQuantityReportGroupByItem", whereConditions, havingConditions, UtilMisc.toList("orderId", "orderItemSeqId"), UtilMisc.toList("orderItemSeqId"), null);
            } catch (GenericEntityException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(e);
                }
                return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderErrorUnableToGetReturnHeaderFromItem", locale));
            }
            if (orderItemQuantitiesIssued != null) {
                Iterator i = orderItemQuantitiesIssued.iterator();
                while (i.hasNext()) {
                    GenericValue orderItemQuantityIssued = (GenericValue) i.next();
                    GenericValue item = null;
                    try {
                        item = remoteClient.getRelatedOne("OrderItem", orderItemQuantityIssued);
                    } catch (GenericEntityException e) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(e);
                        }
                        return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderErrorUnableToGetOrderItemInformation", locale));
                    }
                    Map serviceResult = null;
                    try {
                        serviceResult = remoteClient.runSync("getReturnableQuantity", UtilMisc.toMap("orderItem", item));
                    } catch (GenericServiceException e) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(e);
                        }
                        return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderErrorUnableToGetTheItemReturnableQuantity", locale));
                    } catch (RemoteException e) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(e);
                        }
                        return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderErrorUnableToGetTheItemReturnableQuantity", locale));
                    }
                    if (serviceResult.containsKey(ModelService.ERROR_MESSAGE)) {
                        return ServiceUtil.returnError((String) serviceResult.get(ModelService.ERROR_MESSAGE));
                    } else {
                        if (((Double) serviceResult.get("returnableQuantity")).doubleValue() == 0) {
                            continue;
                        }
                        Map returnInfo = new HashMap();
                        returnInfo.put("returnableQuantity", serviceResult.get("returnableQuantity"));
                        returnInfo.put("returnablePrice", serviceResult.get("returnablePrice"));
                        String itemTypeKey = "FINISHED_GOOD";
                        GenericValue product = null;
                        if (item.getAllFields().get("productId") != null) {
                            try {
                                product = remoteClient.getRelatedOne("Product", item);
                            } catch (GenericEntityException e) {
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug(e);
                                }
                                return ServiceUtil.returnError("Unable to obtain order item information!");
                            }
                        }
                        if (product != null) {
                            itemTypeKey = (String) product.getAllFields().get("productTypeId");
                        } else if (item != null) {
                            itemTypeKey = (String) item.getAllFields().get("orderItemTypeId");
                        }
                        returnInfo.put("itemTypeKey", itemTypeKey);
                        returnable.put(item, returnInfo);
                    }
                }
            } else {
                return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderErrorNoOrderItemsFound", locale));
            }
        } else {
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderErrorUnableToFindOrderHeader", locale));
        }
        Map result = ServiceUtil.returnSuccess();
        result.put("returnableItems", returnable);
        return result;
    }

    public static Map checkReturnComplete(CmsOFBizRemoteClient remoteClient, Map context) {
        String returnId = (String) context.get("returnId");
        Locale locale = (Locale) context.get("locale");
        GenericValue returnHeader = null;
        List returnItems = null;
        try {
            returnHeader = remoteClient.findByPrimaryKey("ReturnHeader", UtilMisc.toMap("returnId", returnId));
            if (returnHeader != null) {
                returnItems = remoteClient.getRelated(returnHeader, "ReturnItem");
            }
        } catch (GenericEntityException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Problems looking up return information", e);
            }
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderErrorGettingReturnHeaderItemInformation", locale));
        }
        if (returnHeader != null && returnHeader.getAllFields().get("statusId") != null) {
            String currentStatus = (String) returnHeader.getAllFields().get("statusId");
            if ("RETURN_COMPLETED".equals(currentStatus) || "RETURN_CANCELLED".equals(currentStatus)) {
                return ServiceUtil.returnSuccess();
            }
        }
        Timestamp now = UtilDateTime.nowTimestamp();
        List completedItems = new ArrayList();
        if (returnHeader != null && returnItems != null && returnItems.size() > 0) {
            Iterator itemsIter = returnItems.iterator();
            while (itemsIter.hasNext()) {
                GenericValue item = (GenericValue) itemsIter.next();
                String itemStatus = item != null ? ((String) item.getAllFields().get("statusId")) : null;
                if (itemStatus != null) {
                    if ("RETURN_COMPLETED".equals(itemStatus) || "RETURN_CANCELLED".equals(itemStatus)) {
                        completedItems.add(item);
                    }
                }
            }
            if (completedItems.size() == returnItems.size()) {
                List toStore = new LinkedList();
                returnHeader = remoteClient.set(returnHeader, "statusId", "RETURN_COMPLETED");
                toStore.add(returnHeader);
                String returnStatusId = remoteClient.getNextSeqId("ReturnStatus");
                GenericValue returnStatus = remoteClient.makeValue("ReturnStatus", UtilMisc.toMap("returnStatusId", returnStatusId));
                returnStatus = remoteClient.set(returnStatus, "statusId", "RETURN_COMPLETED");
                returnStatus = remoteClient.set(returnStatus, "returnId", returnId);
                returnStatus = remoteClient.set(returnStatus, "statusDatetime", now);
                toStore.add(returnStatus);
                try {
                    remoteClient.storeAll(toStore);
                } catch (GenericEntityException e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(e);
                    }
                    return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderErrorUnableToCreateReturnStatusHistory", locale));
                }
            }
        }
        Map result = ServiceUtil.returnSuccess();
        result.put("statusId", returnHeader.getAllFields().get("statusId"));
        return result;
    }

    public static Map processCreditReturn(CmsOFBizRemoteClient remoteClient, Map context) {
        String returnId = (String) context.get("returnId");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Locale locale = (Locale) context.get("locale");
        GenericValue returnHeader = null;
        List returnItems = null;
        try {
            returnHeader = remoteClient.findByPrimaryKey("ReturnHeader", UtilMisc.toMap("returnId", returnId));
            if (returnHeader != null) {
                returnItems = remoteClient.getRelatedByAnd(returnHeader, "ReturnItem", UtilMisc.toMap("returnTypeId", "RTN_CREDIT"));
            }
        } catch (GenericEntityException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Problems looking up return information", e);
            }
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderErrorGettingReturnHeaderItemInformation", locale));
        }
        if (returnHeader != null && returnItems != null && returnItems.size() > 0) {
            Map fields = returnHeader.getAllFields();
            String billingAccountId = (String) fields.get("billingAccountId");
            String fromPartyId = (String) fields.get("fromPartyId");
            String toPartyId = (String) fields.get("toPartyId");
            Map serviceResult = null;
            try {
                serviceResult = remoteClient.runSync("checkPaymentAmountForRefund", UtilMisc.toMap("returnId", returnId));
            } catch (GenericServiceException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Problem running the checkPaymentAmountForRefund service", e);
                }
                return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemsWithCheckPaymentAmountForRefund", locale));
            } catch (RemoteException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Problem running the checkPaymentAmountForRefund service", e);
                }
                return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemsWithCheckPaymentAmountForRefund", locale));
            }
            if (ServiceUtil.isError(serviceResult)) {
                return ServiceUtil.returnError(ServiceUtil.getErrorMessage(serviceResult));
            }
            if (billingAccountId == null) {
                Map results = createBillingAccountFromReturn(returnHeader, returnItems, remoteClient, context);
                if (ServiceUtil.isError(results)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Error creating BillingAccount: " + results.get(ModelService.ERROR_MESSAGE));
                    }
                    return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderErrorWithCreateBillingAccount", locale) + results.get(ModelService.ERROR_MESSAGE));
                }
                billingAccountId = (String) results.get("billingAccountId");
            }
            if (billingAccountId == null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("No available billing account, none was created");
                }
                return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderNoAvailableBillingAccount", locale));
            }
            Timestamp now = UtilDateTime.nowTimestamp();
            BigDecimal creditTotal = ZERO;
            for (Iterator itemsIter = returnItems.iterator(); itemsIter.hasNext(); ) {
                GenericValue item = (GenericValue) itemsIter.next();
                BigDecimal quantity = (BigDecimal) item.getAllFields().get("returnQuantity");
                BigDecimal price = (BigDecimal) item.getAllFields().get("returnPrice");
                if (quantity == null) quantity = ZERO;
                if (price == null) price = ZERO;
                creditTotal = creditTotal.add(price.multiply(quantity).setScale(decimals, rounding));
            }
            BigDecimal adjustments = new BigDecimal(getReturnAdjustmentTotal(remoteClient, UtilMisc.toMap("returnId", returnId)));
            creditTotal = creditTotal.add(adjustments.setScale(decimals, rounding));
            String paymentId = remoteClient.getNextSeqId("Payment");
            GenericValue payment = remoteClient.makeValue("Payment", UtilMisc.toMap("paymentId", paymentId));
            payment = remoteClient.set(payment, "paymentTypeId", "CUSTOMER_REFUND");
            payment = remoteClient.set(payment, "paymentMethodTypeId", "EXT_BILLACT");
            payment = remoteClient.set(payment, "partyIdFrom", toPartyId);
            payment = remoteClient.set(payment, "partyIdTo", fromPartyId);
            payment = remoteClient.set(payment, "effectiveDate", now);
            payment = remoteClient.set(payment, "amount", creditTotal);
            payment = remoteClient.set(payment, "comments", "Return Credit");
            payment = remoteClient.set(payment, "statusId", "PMNT_CONFIRMED");
            try {
                payment = remoteClient.create(payment);
            } catch (GenericEntityException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Problem creating Payment record");
                }
                return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemCreatingPaymentRecord", locale));
            }
            Map itemResponse = UtilMisc.toMap("paymentId", paymentId);
            itemResponse.put("billingAccountId", billingAccountId);
            itemResponse.put("responseAmount", new Double(creditTotal.doubleValue()));
            itemResponse.put("responseDate", now);
            itemResponse.put("userLogin", userLogin);
            Map serviceResults = null;
            try {
                serviceResults = remoteClient.runSync("createReturnItemResponse", itemResponse);
                if (ServiceUtil.isError(serviceResults)) {
                    return ServiceUtil.returnError("Could not create ReturnItemResponse record", null, null, serviceResults);
                }
            } catch (GenericServiceException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Problem creating ReturnItemResponse record");
                }
                return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemCreatingReturnItemResponseRecord", locale));
            } catch (RemoteException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Problem creating ReturnItemResponse record");
                }
                return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemCreatingReturnItemResponseRecord", locale));
            }
            String itemResponseId = (String) serviceResults.get("returnItemResponseId");
            List toBeStored = new ArrayList();
            for (Iterator itemsIter = returnItems.iterator(); itemsIter.hasNext(); ) {
                GenericValue item = (GenericValue) itemsIter.next();
                item = remoteClient.set(item, "returnItemResponseId", itemResponseId);
                item = remoteClient.set(item, "statusId", "RETURN_COMPLETED");
                toBeStored.add(item);
                String returnStatusId = remoteClient.getNextSeqId("ReturnStatus");
                GenericValue returnStatus = remoteClient.makeValue("ReturnStatus", UtilMisc.toMap("returnStatusId", returnStatusId));
                returnStatus = remoteClient.set(returnStatus, "statusId", item.getAllFields().get("statusId"));
                returnStatus = remoteClient.set(returnStatus, "returnId", item.getAllFields().get("returnId"));
                returnStatus = remoteClient.set(returnStatus, "returnItemSeqId", item.getAllFields().get("returnItemSeqId"));
                returnStatus = remoteClient.set(returnStatus, "statusDatetime", now);
                toBeStored.add(returnStatus);
            }
            try {
                remoteClient.storeAll(toBeStored);
            } catch (GenericEntityException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Problem storing ReturnItem updates");
                }
                return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemStoringReturnItemUpdates", locale));
            }
            String paId = remoteClient.getNextSeqId("PaymentApplication");
            GenericValue pa = remoteClient.makeValue("PaymentApplication", UtilMisc.toMap("paymentApplicationId", paId));
            pa = remoteClient.set(pa, "paymentId", paymentId);
            pa = remoteClient.set(pa, "billingAccountId", billingAccountId);
            pa = remoteClient.set(pa, "amountApplied", creditTotal);
            try {
                pa = remoteClient.create(pa);
            } catch (GenericEntityException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Problem creating PaymentApplication record for billing account");
                }
                return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemCreatingPaymentApplicationRecord", locale));
            }
            try {
                serviceResults = remoteClient.runSync("createPaymentApplicationsFromReturnItemResponse", UtilMisc.toMap("returnItemResponseId", itemResponseId, "userLogin", userLogin));
                if (ServiceUtil.isError(serviceResults)) {
                    return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemCreatingPaymentApplicationRecord", locale), null, null, serviceResults);
                }
            } catch (GenericServiceException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Problem creating PaymentApplication records for return invoice");
                }
                return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemCreatingPaymentApplicationRecord", locale));
            } catch (RemoteException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Problem creating PaymentApplication records for return invoice");
                }
                return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemCreatingPaymentApplicationRecord", locale));
            }
        }
        return ServiceUtil.returnSuccess();
    }

    /**
     * Helper method to generate a BillingAccount (store credit) from a return 
     * header.  This method takes care of all business logic relating to
     * the initialization of a Billing Account from the Return data.
     *
     * The BillingAccount.thruDate will be set to (now + 
     * ProductStore.storeCreditValidDays + end of day).  The product stores 
     * are obtained via the return orders, and the minimum storeCreditValidDays
     * will be used.  The default is to set thruDate to null, which implies no 
     * expiration.
     *
     * Note that we set BillingAccount.accountLimit to 0.0 for store credits.
     * This is because the available balance of BillingAccounts is 
     * calculated as accountLimit + sum of Payments - sum of Invoices.
     */
    private static Map createBillingAccountFromReturn(GenericValue returnHeader, List returnItems, CmsOFBizRemoteClient remoteClient, Map context) {
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Locale locale = (Locale) context.get("locale");
        try {
            List orders = remoteClient.getRelated("OrderHeader", returnItems);
            List productStores = remoteClient.getRelated("ProductStore", orders);
            Long storeCreditValidDays = null;
            for (Iterator iter = productStores.iterator(); iter.hasNext(); ) {
                GenericValue productStore = (GenericValue) iter.next();
                Long thisStoreValidDays = (Long) productStore.getAllFields().get("storeCreditValidDays");
                if (thisStoreValidDays == null) continue;
                if (storeCreditValidDays == null) {
                    storeCreditValidDays = thisStoreValidDays;
                } else if (thisStoreValidDays.compareTo(storeCreditValidDays) < 0) {
                    storeCreditValidDays = thisStoreValidDays;
                }
            }
            Timestamp thruDate = null;
            if (storeCreditValidDays != null) thruDate = UtilDateTime.getDayEnd(UtilDateTime.nowTimestamp(), storeCreditValidDays.intValue());
            Map input = UtilMisc.toMap("accountLimit", new Double(0.00), "description", "Credit Account for Return #" + returnHeader.getAllFields().get("returnId"), "userLogin", userLogin);
            input.put("accountCurrencyUomId", returnHeader.getAllFields().get("currencyUomId"));
            input.put("thruDate", thruDate);
            Map results = remoteClient.runSync("createBillingAccount", input);
            if (ServiceUtil.isError(results)) return results;
            String billingAccountId = (String) results.get("billingAccountId");
            input = UtilMisc.toMap("billingAccountId", billingAccountId, "partyId", returnHeader.getAllFields().get("fromPartyId"), "roleTypeId", "BILL_TO_CUSTOMER", "userLogin", userLogin);
            Map roleResults = remoteClient.runSync("createBillingAccountRole", input);
            if (ServiceUtil.isError(roleResults)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Error with createBillingAccountRole: " + roleResults.get(ModelService.ERROR_MESSAGE));
                }
                return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderErrorWithCreateBillingAccountRole", locale) + roleResults.get(ModelService.ERROR_MESSAGE));
            }
            return results;
        } catch (GenericEntityException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Entity error when creating BillingAccount: " + e.getMessage());
            }
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemsCreatingBillingAccount", locale));
        } catch (GenericServiceException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Entity error when creating BillingAccount: " + e.getMessage());
            }
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemsCreatingBillingAccount", locale));
        } catch (RemoteException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Entity error when creating BillingAccount: " + e.getMessage());
            }
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemsCreatingBillingAccount", locale));
        }
    }

    public static Map processRefundReturn(CmsOFBizRemoteClient remoteClient, Map context) {
        String returnId = (String) context.get("returnId");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Locale locale = (Locale) context.get("locale");
        GenericValue returnHeader = null;
        List returnItems = null;
        try {
            returnHeader = remoteClient.findByPrimaryKey("ReturnHeader", UtilMisc.toMap("returnId", returnId));
            if (returnHeader != null) {
                returnItems = remoteClient.getRelatedByAnd(returnHeader, "ReturnItem", UtilMisc.toMap("returnTypeId", "RTN_REFUND"));
            }
        } catch (GenericEntityException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Problems looking up return information", e);
            }
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderErrorGettingReturnHeaderItemInformation", locale));
        }
        if (returnHeader != null && returnItems != null && returnItems.size() > 0) {
            Map itemsByOrder = new HashMap();
            Map totalByOrder = new HashMap();
            Map serviceResult = null;
            try {
                serviceResult = remoteClient.runSync("checkPaymentAmountForRefund", UtilMisc.toMap("returnId", returnId));
            } catch (GenericServiceException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Problem running the checkPaymentAmountForRefund service", e);
                }
                return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemsWithCheckPaymentAmountForRefund", locale));
            } catch (RemoteException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Problem running the checkPaymentAmountForRefund service", e);
                }
                return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemsWithCheckPaymentAmountForRefund", locale));
            }
            if (ServiceUtil.isError(serviceResult)) {
                return ServiceUtil.returnError(ServiceUtil.getErrorMessage(serviceResult));
            }
            groupReturnItemsByOrder(returnItems, itemsByOrder, totalByOrder, remoteClient, returnId);
            Set itemSet = itemsByOrder.entrySet();
            Iterator itemByOrderIt = itemSet.iterator();
            while (itemByOrderIt.hasNext()) {
                Map.Entry entry = (Map.Entry) itemByOrderIt.next();
                String orderId = (String) entry.getKey();
                List items = (List) entry.getValue();
                Double orderTotal = (Double) totalByOrder.get(orderId);
                GenericValue orderHeader = null;
                List orderPayPrefs = null;
                try {
                    orderHeader = remoteClient.findByPrimaryKey("OrderHeader", UtilMisc.toMap("orderId", orderId));
                    orderPayPrefs = remoteClient.getRelated(orderHeader, "OrderPaymentPreference", UtilMisc.toMap("statusId", "PAYMENT_SETTLED"), UtilMisc.toList("-maxAmount"));
                } catch (GenericEntityException e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Cannot get Order details for #" + orderId, e);
                    }
                    continue;
                }
                OrderReadHelper orderReadHelper = new OrderReadHelper(remoteClient, orderId);
                Timestamp now = UtilDateTime.nowTimestamp();
                Map receivedPaymentTotalsByPaymentMethod = orderReadHelper.getReceivedPaymentTotalsByPaymentMethod(remoteClient);
                Map refundedTotalsByPaymentMethod = orderReadHelper.getReturnedTotalsByPaymentMethod(remoteClient);
                Map prefSplitMap = new HashMap();
                Iterator oppit = orderPayPrefs.iterator();
                while (oppit.hasNext()) {
                    GenericValue orderPayPref = (GenericValue) oppit.next();
                    Map fields = orderPayPref.getAllFields();
                    String paymentMethodTypeId = (String) fields.get("paymentMethodTypeId");
                    String orderPayPrefKey = ((String) fields.get("paymentMethodId")) != null ? ((String) fields.get("paymentMethodId")) : ((String) fields.get("paymentMethodTypeId"));
                    BigDecimal orderPayPrefReceivedTotal = ZERO;
                    if (receivedPaymentTotalsByPaymentMethod.containsKey(orderPayPrefKey)) {
                        orderPayPrefReceivedTotal = orderPayPrefReceivedTotal.add(new BigDecimal(((Double) receivedPaymentTotalsByPaymentMethod.get(orderPayPrefKey)).doubleValue()).setScale(decimals, rounding));
                    }
                    BigDecimal orderPayPrefRefundedTotal = ZERO;
                    if (refundedTotalsByPaymentMethod.containsKey(orderPayPrefKey)) {
                        orderPayPrefRefundedTotal = orderPayPrefRefundedTotal.add(new BigDecimal(((Double) refundedTotalsByPaymentMethod.get(orderPayPrefKey)).doubleValue()).setScale(decimals, rounding));
                    }
                    BigDecimal orderPayPrefAvailableTotal = orderPayPrefReceivedTotal.subtract(orderPayPrefRefundedTotal);
                    if (orderPayPrefAvailableTotal.compareTo(ZERO) == 1) {
                        Map orderPayPrefDetails = new HashMap();
                        orderPayPrefDetails.put("orderPaymentPreference", orderPayPref);
                        orderPayPrefDetails.put("availableTotal", orderPayPrefAvailableTotal);
                        if (prefSplitMap.containsKey(paymentMethodTypeId)) {
                            ((List) prefSplitMap.get(paymentMethodTypeId)).add(orderPayPrefDetails);
                        } else {
                            prefSplitMap.put(paymentMethodTypeId, UtilMisc.toList(orderPayPrefDetails));
                        }
                    }
                }
                BigDecimal amountLeftToRefund = new BigDecimal(orderTotal.doubleValue()).setScale(decimals, rounding);
                List electronicTypes = UtilMisc.toList("CREDIT_CARD", "EFT_ACCOUNT", "GIFT_CARD");
                List orderedRefundPaymentMethodTypes = new ArrayList();
                orderedRefundPaymentMethodTypes.add("EXT_BILLACT");
                orderedRefundPaymentMethodTypes.add("GIFT_CARD");
                orderedRefundPaymentMethodTypes.add("CREDIT_CARD");
                EntityConditionList pmtConditionList = new EntityConditionList(UtilMisc.toList(new EntityExpr("paymentMethodTypeId", EntityOperator.NOT_IN, orderedRefundPaymentMethodTypes)), EntityOperator.AND);
                List otherPaymentMethodTypes = new ArrayList();
                try {
                    otherPaymentMethodTypes = remoteClient.findByConditionCache("PaymentMethodType", pmtConditionList, null, null);
                } catch (GenericEntityException e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Cannot get PaymentMethodTypes", e);
                    }
                    return ServiceUtil.returnError("Problems getting PaymentMethodTypes: " + e.toString());
                }
                orderedRefundPaymentMethodTypes.addAll(remoteClient.getFieldListFromEntityList(otherPaymentMethodTypes, "paymentMethodTypeId", true));
                Iterator orpmtit = orderedRefundPaymentMethodTypes.iterator();
                while (orpmtit.hasNext() && amountLeftToRefund.compareTo(ZERO) == 1) {
                    String paymentMethodTypeId = (String) orpmtit.next();
                    if (prefSplitMap.containsKey(paymentMethodTypeId)) {
                        List paymentMethodDetails = (List) prefSplitMap.get(paymentMethodTypeId);
                        Iterator pmtppit = paymentMethodDetails.iterator();
                        while (pmtppit.hasNext() && amountLeftToRefund.compareTo(ZERO) == 1) {
                            Map orderPaymentPrefDetails = (Map) pmtppit.next();
                            GenericValue orderPaymentPreference = (GenericValue) orderPaymentPrefDetails.get("orderPaymentPreference");
                            BigDecimal orderPaymentPreferenceAvailable = (BigDecimal) orderPaymentPrefDetails.get("availableTotal");
                            BigDecimal amountToRefund = orderPaymentPreferenceAvailable.min(amountLeftToRefund);
                            String paymentId = null;
                            if (electronicTypes.contains(paymentMethodTypeId)) {
                                try {
                                    serviceResult = remoteClient.runSync("refundPayment", UtilMisc.toMap("orderPaymentPreference", orderPaymentPreference, "refundAmount", new Double(amountToRefund.setScale(decimals, rounding).doubleValue()), "userLogin", userLogin));
                                    if (ServiceUtil.isError(serviceResult)) {
                                        return ServiceUtil.returnError("Error in refund payment", null, null, serviceResult);
                                    }
                                    paymentId = (String) serviceResult.get("paymentId");
                                } catch (GenericServiceException e) {
                                    if (LOG.isDebugEnabled()) {
                                        LOG.debug("Problem running the refundPayment service", e);
                                    }
                                    return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemsWithTheRefundSeeLogs", locale));
                                } catch (RemoteException e) {
                                    if (LOG.isDebugEnabled()) {
                                        LOG.debug("Problem running the refundPayment service", e);
                                    }
                                    return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemsWithTheRefundSeeLogs", locale));
                                }
                            } else if (paymentMethodTypeId.equals("EXT_BILLACT")) {
                                try {
                                    serviceResult = remoteClient.runSync("refundBillingAccountPayment", UtilMisc.toMap("orderPaymentPreference", orderPaymentPreference, "refundAmount", new Double(amountToRefund.setScale(decimals, rounding).doubleValue()), "userLogin", userLogin));
                                    if (ServiceUtil.isError(serviceResult)) {
                                        return ServiceUtil.returnError("Error in refund payment", null, null, serviceResult);
                                    }
                                    paymentId = (String) serviceResult.get("paymentId");
                                } catch (GenericServiceException e) {
                                    if (LOG.isDebugEnabled()) {
                                        LOG.debug("Problem running the refundPayment service", e);
                                    }
                                    return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemsWithTheRefundSeeLogs", locale));
                                } catch (RemoteException e) {
                                    if (LOG.isDebugEnabled()) {
                                        LOG.debug("Problem running the refundPayment service", e);
                                    }
                                    return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemsWithTheRefundSeeLogs", locale));
                                }
                            } else {
                            }
                            Map response = FastMap.newInstance();
                            response.put("orderPaymentPreferenceId", (String) orderPaymentPreference.getAllFields().get("orderPaymentPreferenceId"));
                            response.put("responseAmount", new Double(amountToRefund.setScale(decimals, rounding).doubleValue()));
                            response.put("responseDate", now);
                            response.put("userLogin", userLogin);
                            if (paymentId != null) {
                                response.put("paymentId", paymentId);
                            }
                            if (paymentMethodTypeId.equals("EXT_BILLACT")) {
                                response.put("billingAccountId", (String) orderReadHelper.getBillingAccount(remoteClient).getAllFields().get("billingAccountId"));
                            }
                            Map serviceResults = null;
                            try {
                                serviceResults = remoteClient.runSync("createReturnItemResponse", response);
                                if (ServiceUtil.isError(serviceResults)) {
                                    return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemsCreatingReturnItemResponseEntity", locale), null, null, serviceResults);
                                }
                            } catch (GenericServiceException e) {
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug("Problems creating new ReturnItemResponse entity", e);
                                }
                                return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemsCreatingReturnItemResponseEntity", locale));
                            } catch (RemoteException e) {
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug("Problems creating new ReturnItemResponse entity", e);
                                }
                                return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemsCreatingReturnItemResponseEntity", locale));
                            }
                            String responseId = (String) serviceResults.get("returnItemResponseId");
                            Iterator itemsIter = items.iterator();
                            while (itemsIter.hasNext()) {
                                GenericValue item = (GenericValue) itemsIter.next();
                                item = remoteClient.set(item, "returnItemResponseId", responseId);
                                item = remoteClient.set(item, "statusId", "RETURN_COMPLETED");
                                String returnStatusId = remoteClient.getNextSeqId("ReturnStatus");
                                GenericValue returnStatus = remoteClient.makeValue("ReturnStatus", UtilMisc.toMap("returnStatusId", returnStatusId));
                                returnStatus = remoteClient.set(returnStatus, "statusId", item.getAllFields().get("statusId"));
                                returnStatus = remoteClient.set(returnStatus, "returnId", item.getAllFields().get("returnId"));
                                returnStatus = remoteClient.set(returnStatus, "returnItemSeqId", item.getAllFields().get("returnItemSeqId"));
                                returnStatus = remoteClient.set(returnStatus, "statusDatetime", now);
                                try {
                                    remoteClient.store(item);
                                    returnStatus = remoteClient.create(returnStatus);
                                } catch (GenericEntityException e) {
                                    if (LOG.isDebugEnabled()) {
                                        LOG.debug("Problem updating the ReturnItem entity", e);
                                    }
                                    return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemUpdatingReturnItemReturnItemResponseId", locale));
                                }
                            }
                            try {
                                serviceResults = remoteClient.runSync("createPaymentApplicationsFromReturnItemResponse", UtilMisc.toMap("returnItemResponseId", responseId, "userLogin", userLogin));
                                if (ServiceUtil.isError(serviceResults)) {
                                    return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemUpdatingReturnItemReturnItemResponseId", locale), null, null, serviceResults);
                                }
                            } catch (GenericServiceException e) {
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug("Problem creating PaymentApplication records for return invoice", e);
                                }
                                return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemUpdatingReturnItemReturnItemResponseId", locale));
                            } catch (RemoteException e) {
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug("Problem creating PaymentApplication records for return invoice", e);
                                }
                                return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemUpdatingReturnItemReturnItemResponseId", locale));
                            }
                            amountLeftToRefund = amountLeftToRefund.subtract(amountToRefund);
                        }
                    }
                }
                if (amountLeftToRefund.compareTo(ZERO) == 1) {
                    try {
                        Map input = UtilMisc.toMap("userLogin", userLogin, "amount", new Double(amountLeftToRefund.doubleValue()), "statusId", "PMNT_NOT_PAID");
                        input.put("partyIdTo", returnHeader.getAllFields().get("fromPartyId"));
                        input.put("partyIdFrom", returnHeader.getAllFields().get("toPartyId"));
                        input.put("paymentTypeId", "CUSTOMER_REFUND");
                        Map results = remoteClient.runSync("createPayment", input);
                        if (ServiceUtil.isError(results)) return results;
                        input = UtilMisc.toMap("userLogin", userLogin, "paymentId", results.get("paymentId"), "responseAmount", new Double(amountLeftToRefund.doubleValue()));
                        results = remoteClient.runSync("createReturnItemResponse", input);
                        if (ServiceUtil.isError(results)) return results;
                    } catch (GenericServiceException e) {
                        return ServiceUtil.returnError(e.getMessage());
                    } catch (RemoteException e) {
                        return ServiceUtil.returnError(e.getMessage());
                    }
                }
            }
        }
        return ServiceUtil.returnSuccess();
    }

    public static Map refundBillingAccountPayment(CmsOFBizRemoteClient remoteClient, Map context) {
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        GenericValue paymentPref = (GenericValue) context.get("orderPaymentPreference");
        Double refundAmount = (Double) context.get("refundAmount");
        GenericValue orderHeader = null;
        try {
            orderHeader = remoteClient.getRelatedOne("OrderHeader", paymentPref);
        } catch (GenericEntityException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Cannot get OrderHeader from OrderPaymentPreference", e);
            }
            return ServiceUtil.returnError("Problems getting OrderHeader from OrderPaymentPreference: " + e.toString());
        }
        OrderReadHelper orh = new OrderReadHelper(remoteClient, orderHeader);
        String payFromPartyId = (String) orh.getBillFromParty(remoteClient).getAllFields().get("partyId");
        String payToPartyId = (String) orh.getBillToParty(remoteClient).getAllFields().get("partyId");
        String responseId = remoteClient.getNextSeqId("PaymentGatewayResponse");
        GenericValue response = remoteClient.makeValue("PaymentGatewayResponse", null);
        response = remoteClient.set(response, "paymentGatewayResponseId", responseId);
        response = remoteClient.set(response, "paymentServiceTypeEnumId", "PRDS_PAY_REFUND");
        response = remoteClient.set(response, "orderPaymentPreferenceId", paymentPref.getAllFields().get("orderPaymentPreferenceId"));
        response = remoteClient.set(response, "paymentMethodTypeId", paymentPref.getAllFields().get("paymentMethodTypeId"));
        response = remoteClient.set(response, "transCodeEnumId", "PGT_REFUND");
        response = remoteClient.set(response, "amount", refundAmount);
        response = remoteClient.set(response, "transactionDate", UtilDateTime.nowTimestamp());
        response = remoteClient.set(response, "currencyUomId", orh.getCurrency());
        try {
            response = remoteClient.create(response);
        } catch (GenericEntityException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e);
            }
            return ServiceUtil.returnError("Unable to create PaymentGatewayResponse record");
        }
        Map paymentCtx = UtilMisc.toMap("paymentTypeId", "CUSTOMER_REFUND");
        paymentCtx.put("paymentMethodTypeId", paymentPref.get("paymentMethodTypeId"));
        paymentCtx.put("paymentGatewayResponseId", responseId);
        paymentCtx.put("partyIdTo", payToPartyId);
        paymentCtx.put("partyIdFrom", payFromPartyId);
        paymentCtx.put("statusId", "PMNT_CONFIRMED");
        paymentCtx.put("paymentPreferenceId", paymentPref.get("orderPaymentPreferenceId"));
        paymentCtx.put("currencyUomId", orh.getCurrency());
        paymentCtx.put("amount", refundAmount);
        paymentCtx.put("userLogin", userLogin);
        paymentCtx.put("comments", "Refund");
        String paymentId = null;
        try {
            Map paymentCreationResult = remoteClient.runSync("createPayment", paymentCtx);
            if (ServiceUtil.isError(paymentCreationResult)) {
                return paymentCreationResult;
            } else {
                paymentId = (String) paymentCreationResult.get("paymentId");
            }
        } catch (GenericServiceException e) {
            return ServiceUtil.returnError("Problem creating Payment " + e.getMessage());
        } catch (RemoteException e) {
            return ServiceUtil.returnError("Problem creating Payment " + e.getMessage());
        }
        if (paymentId == null) {
            return ServiceUtil.returnError("Create payment failed");
        }
        if ("EXT_BILLACT".equals((String) paymentPref.getAllFields().get("paymentMethodTypeId"))) {
            GenericValue billingAccount = orh.getBillingAccount(remoteClient);
            Map fields = billingAccount.getAllFields();
            if (UtilValidate.isNotEmpty((String) fields.get("billingAccountId"))) {
                try {
                    Map paymentApplResult = remoteClient.runSync("createPaymentApplication", UtilMisc.toMap("paymentId", paymentId, "billingAccountId", (String) fields.get("billingAccountId"), "amountApplied", refundAmount, "userLogin", userLogin));
                    if (ServiceUtil.isError(paymentApplResult)) {
                        return paymentApplResult;
                    }
                } catch (GenericServiceException e) {
                    return ServiceUtil.returnError("Problem creating PaymentApplication: " + e.getMessage());
                } catch (RemoteException e) {
                    return ServiceUtil.returnError("Problem creating PaymentApplication: " + e.getMessage());
                }
            }
        }
        Map result = ServiceUtil.returnSuccess();
        result.put("paymentId", paymentId);
        return result;
    }

    public static Map createPaymentApplicationsFromReturnItemResponse(CmsOFBizRemoteClient remoteClient, Map context) {
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String responseId = (String) context.get("returnItemResponseId");
        String errorMsg = "Failed to create payment applications for return item response [" + responseId + "]. ";
        try {
            GenericValue response = remoteClient.findByPrimaryKey("ReturnItemResponse", UtilMisc.toMap("returnItemResponseId", responseId));
            if (response == null) {
                return ServiceUtil.returnError(errorMsg + "Return Item Response not found with ID [" + responseId + "].");
            }
            BigDecimal responseAmount = ((BigDecimal) response.getAllFields().get("responseAmount")).setScale(decimals, rounding);
            String paymentId = (String) response.getAllFields().get("paymentId");
            Map returnInvoices = FastMap.newInstance();
            List items = remoteClient.getRelated(response, "ReturnItem");
            for (Iterator itemIter = items.iterator(); itemIter.hasNext(); ) {
                GenericValue item = (GenericValue) itemIter.next();
                List billings = remoteClient.getRelated(item, "ReturnItemBilling");
                for (Iterator billIter = billings.iterator(); billIter.hasNext(); ) {
                    GenericValue billing = (GenericValue) billIter.next();
                    GenericValue invoice = remoteClient.getRelatedOne("Invoice", billing);
                    String invoiceId = (String) invoice.getAllFields().get("invoiceId");
                    if (returnInvoices.get(invoiceId) == null) {
                        returnInvoices.put(invoiceId, invoice);
                    }
                }
            }
            Map invoiceTotals = FastMap.newInstance();
            BigDecimal grandTotal = ZERO;
            for (Iterator iter = returnInvoices.values().iterator(); iter.hasNext(); ) {
                GenericValue invoice = (GenericValue) iter.next();
                List billings = remoteClient.getRelated(invoice, "ReturnItemBilling");
                BigDecimal runningTotal = ZERO;
                for (Iterator billIter = billings.iterator(); billIter.hasNext(); ) {
                    GenericValue billing = (GenericValue) billIter.next();
                    runningTotal = runningTotal.add((BigDecimal) billing.getAllFields().get("amount")).multiply(((BigDecimal) billing.getAllFields().get("quantity")).setScale(decimals, rounding));
                }
                invoiceTotals.put((String) invoice.getAllFields().get("invoiceId"), runningTotal);
                grandTotal = grandTotal.add(runningTotal);
            }
            for (Iterator iter = returnInvoices.values().iterator(); iter.hasNext(); ) {
                GenericValue invoice = (GenericValue) iter.next();
                String invoiceId = (String) invoice.getAllFields().get("invoiceId");
                BigDecimal invoiceTotal = (BigDecimal) invoiceTotals.get(invoiceId);
                BigDecimal amountApplied = responseAmount.multiply(invoiceTotal).divide(grandTotal, decimals, rounding).setScale(decimals, rounding);
                if (paymentId != null) {
                    Map input = UtilMisc.toMap("paymentId", paymentId, "invoiceId", invoiceId);
                    input.put("amountApplied", new Double(amountApplied.doubleValue()));
                    input.put("userLogin", userLogin);
                    if (response.getAllFields().get("billingAccountId") != null) {
                        GenericValue billingAccount = remoteClient.getRelatedOne("BillingAccount", response);
                        if (billingAccount != null) {
                            input.put("billingAccountId", response.getAllFields().get("billingAccountId"));
                        }
                    }
                    Map serviceResults = remoteClient.runSync("createPaymentApplication", input);
                    if (ServiceUtil.isError(serviceResults)) {
                        return ServiceUtil.returnError(errorMsg, null, null, serviceResults);
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Created PaymentApplication for response with amountApplied " + amountApplied.toString());
                    }
                }
            }
        } catch (GenericServiceException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e);
            }
            return ServiceUtil.returnError(errorMsg + e.getMessage());
        } catch (GenericEntityException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e);
            }
            return ServiceUtil.returnError(errorMsg + e.getMessage());
        } catch (RemoteException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e);
            }
            return ServiceUtil.returnError(errorMsg + e.getMessage());
        }
        return ServiceUtil.returnSuccess();
    }

    public static Map processReplacementReturn(CmsOFBizRemoteClient remoteClient, Map context) {
        String returnId = (String) context.get("returnId");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Locale locale = (Locale) context.get("locale");
        GenericValue returnHeader = null;
        List returnItems = null;
        try {
            returnHeader = remoteClient.findByPrimaryKey("ReturnHeader", UtilMisc.toMap("returnId", returnId));
            if (returnHeader != null) {
                returnItems = remoteClient.getRelatedByAnd(returnHeader, "ReturnItem", UtilMisc.toMap("returnTypeId", "RTN_REPLACE"));
            }
        } catch (GenericEntityException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Problems looking up return information", e);
            }
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderErrorGettingReturnHeaderItemInformation", locale));
        }
        List createdOrderIds = new ArrayList();
        if (returnHeader != null && returnItems != null && returnItems.size() > 0) {
            Map itemsByOrder = new HashMap();
            Map totalByOrder = new HashMap();
            groupReturnItemsByOrder(returnItems, itemsByOrder, totalByOrder, remoteClient, returnId);
            Set itemSet = itemsByOrder.entrySet();
            Iterator itemByOrderIt = itemSet.iterator();
            while (itemByOrderIt.hasNext()) {
                Map.Entry entry = (Map.Entry) itemByOrderIt.next();
                String orderId = (String) entry.getKey();
                List items = (List) entry.getValue();
                GenericValue orderHeader = null;
                try {
                    orderHeader = remoteClient.findByPrimaryKey("OrderHeader", UtilMisc.toMap("orderId", orderId));
                } catch (GenericEntityException e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Cannot get Order details for #" + orderId, e);
                    }
                    continue;
                }
                OrderReadHelper orh = new OrderReadHelper(remoteClient, orderHeader);
                Map orderMap = UtilMisc.toMap("userLogin", userLogin);
                GenericValue placingParty = orh.getPlacingParty(remoteClient);
                String placingPartyId = null;
                if (placingParty != null) {
                    placingPartyId = (String) placingParty.getAllFields().get("partyId");
                }
                orderMap.put("orderTypeId", "SALES_ORDER");
                orderMap.put("partyId", placingPartyId);
                orderMap.put("productStoreId", orderHeader.getAllFields().get("productStoreId"));
                orderMap.put("webSiteId", orderHeader.getAllFields().get("webSiteId"));
                orderMap.put("visitId", orderHeader.getAllFields().get("visitId"));
                orderMap.put("currencyUom", orderHeader.getAllFields().get("currencyUom"));
                orderMap.put("grandTotal", new Double(0.00));
                List contactMechs = new ArrayList();
                List orderCm = null;
                try {
                    orderCm = remoteClient.getRelated(orderHeader, "OrderContactMech");
                } catch (GenericEntityException e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(e);
                    }
                }
                if (orderCm != null) {
                    Iterator orderCmi = orderCm.iterator();
                    while (orderCmi.hasNext()) {
                        GenericValue v = (GenericValue) orderCmi.next();
                        contactMechs.add(remoteClient.createGenericValue(v));
                    }
                    orderMap.put("orderContactMechs", contactMechs);
                }
                List shipmentPrefs = new ArrayList();
                List orderSp = null;
                try {
                    orderSp = remoteClient.getRelated(orderHeader, "OrderShipmentPreference");
                } catch (GenericEntityException e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(e);
                    }
                }
                if (orderSp != null) {
                    Iterator orderSpi = orderSp.iterator();
                    while (orderSpi.hasNext()) {
                        GenericValue v = (GenericValue) orderSpi.next();
                        shipmentPrefs.add(remoteClient.createGenericValue(v));
                    }
                    orderMap.put("orderShipmentPreferences", shipmentPrefs);
                }
                double itemTotal = 0.00;
                List orderItems = new ArrayList();
                List orderItemShipGroupInfo = new ArrayList();
                List orderItemShipGroupIds = new ArrayList();
                List orderItemAssocs = new ArrayList();
                if (items != null) {
                    Iterator ri = items.iterator();
                    int itemCount = 1;
                    while (ri.hasNext()) {
                        GenericValue returnItem = (GenericValue) ri.next();
                        GenericValue orderItem = null;
                        try {
                            orderItem = remoteClient.getRelatedOne("OrderItem", returnItem);
                        } catch (GenericEntityException e) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug(e);
                            }
                            continue;
                        }
                        if (orderItem != null) {
                            Double quantity = (Double) returnItem.getAllFields().get("returnQuantity");
                            Double unitPrice = (Double) returnItem.getAllFields().get("returnPrice");
                            if (quantity != null && unitPrice != null) {
                                itemTotal = (quantity.doubleValue() * unitPrice.doubleValue());
                                GenericValue newItem = remoteClient.makeValue("OrderItem", UtilMisc.toMap("orderItemSeqId", UtilFormatOut.formatPaddedNumber(itemCount, 5)));
                                newItem = remoteClient.set(newItem, "orderItemTypeId", orderItem.getAllFields().get("orderItemTypeId"));
                                newItem = remoteClient.set(newItem, "productId", orderItem.getAllFields().get("productId"));
                                newItem = remoteClient.set(newItem, "productFeatureId", orderItem.getAllFields().get("productFeatureId"));
                                newItem = remoteClient.set(newItem, "prodCatalogId", orderItem.getAllFields().get("prodCatalogId"));
                                newItem = remoteClient.set(newItem, "productCategoryId", orderItem.getAllFields().get("productCategoryId"));
                                newItem = remoteClient.set(newItem, "quantity", quantity);
                                newItem = remoteClient.set(newItem, "unitPrice", unitPrice);
                                newItem = remoteClient.set(newItem, "unitListPrice", orderItem.getAllFields().get("unitListPrice"));
                                newItem = remoteClient.set(newItem, "itemDescription", orderItem.getAllFields().get("itemDescription"));
                                newItem = remoteClient.set(newItem, "comments", orderItem.getAllFields().get("comments"));
                                newItem = remoteClient.set(newItem, "correspondingPoId", orderItem.getAllFields().get("correspondingPoId"));
                                newItem = remoteClient.set(newItem, "statusId", "ITEM_CREATED");
                                orderItems.add(newItem);
                                try {
                                    GenericValue orderItemShipGroupAssoc = remoteClient.getFirst(remoteClient.getRelated(orderItem, "OrderItemShipGroupAssoc"));
                                    if (orderItemShipGroupAssoc != null) {
                                        Map fields = orderItemShipGroupAssoc.getAllFields();
                                        String shipGroupSeqId = (String) fields.get("shipGroupSeqId");
                                        if (!orderItemShipGroupIds.contains(shipGroupSeqId)) {
                                            GenericValue orderItemShipGroup = remoteClient.getRelatedOne("OrderItemShipGroup", orderItemShipGroupAssoc);
                                            GenericValue newOrderItemShipGroup = (GenericValue) orderItemShipGroup.clone();
                                            newOrderItemShipGroup = remoteClient.set(newOrderItemShipGroup, "orderId", null);
                                            orderItemShipGroupInfo.add(newOrderItemShipGroup);
                                            orderItemShipGroupIds.add(shipGroupSeqId);
                                        }
                                        GenericValue newOrderItemShipGroupAssoc = remoteClient.makeValue("OrderItemShipGroupAssoc", UtilMisc.toMap("orderItemSeqId", (String) fields.get("orderItemSeqId"), "shipGroupSeqId", shipGroupSeqId, "quantity", quantity));
                                        orderItemShipGroupInfo.add(newOrderItemShipGroupAssoc);
                                    }
                                } catch (GenericEntityException gee) {
                                    if (LOG.isDebugEnabled()) {
                                        LOG.debug(gee);
                                    }
                                }
                                GenericValue newOrderItemAssoc = remoteClient.makeValue("OrderItemAssoc", UtilMisc.toMap("orderId", (String) orderHeader.getAllFields().get("orderId"), "orderItemSeqId", (String) orderItem.getAllFields().get("orderItemSeqId"), "shipGroupSeqId", "_NA_", "toOrderItemSeqId", (String) newItem.getAllFields().get("orderItemSeqId"), "toShipGroupSeqId", "_NA_", "orderItemAssocTypeId", "REPLACEMENT"));
                                orderItemAssocs.add(newOrderItemAssoc);
                            }
                        }
                    }
                    orderMap.put("orderItems", orderItems);
                    if (orderItemShipGroupInfo.size() > 0) {
                        orderMap.put("orderItemShipGroupInfo", orderItemShipGroupInfo);
                    }
                    if (orderItemAssocs.size() > 0) {
                        orderMap.put("orderItemAssociations", orderItemAssocs);
                    }
                } else {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("No return items found??");
                    }
                    continue;
                }
                GenericValue adj = remoteClient.makeValue("OrderAdjustment", new HashMap());
                adj = remoteClient.set(adj, "orderAdjustmentTypeId", "REPLACE_ADJUSTMENT");
                adj = remoteClient.set(adj, "amount", new Double(itemTotal * -1));
                adj = remoteClient.set(adj, "comments", "Replacement Item Return #" + returnId);
                adj = remoteClient.set(adj, "createdDate", UtilDateTime.nowTimestamp());
                adj = remoteClient.set(adj, "createdByUserLogin", (String) userLogin.getAllFields().get("userLoginId"));
                orderMap.put("orderAdjustments", UtilMisc.toList(adj));
                try {
                    orderMap.put("orderTerms", remoteClient.getRelated(orderHeader, "OrderTerm"));
                } catch (GenericEntityException e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Cannot create replacement order because order terms for original order are not available");
                    }
                }
                try {
                    List orderRoles = remoteClient.getRelated(orderHeader, "OrderRole");
                    Map orderRolesMap = FastMap.newInstance();
                    if (orderRoles != null) {
                        Iterator orderRolesIt = orderRoles.iterator();
                        while (orderRolesIt.hasNext()) {
                            GenericValue orderRole = (GenericValue) orderRolesIt.next();
                            Map fields = orderRole.getAllFields();
                            String roleTypeId = (String) fields.get("roleTypeId");
                            List parties = (List) orderRolesMap.get(roleTypeId);
                            if (parties == null) {
                                parties = FastList.newInstance();
                                orderRolesMap.put(roleTypeId, parties);
                            }
                            parties.add((String) fields.get("partyId"));
                        }
                    }
                    if (orderRolesMap.size() > 0) {
                        orderMap.put("orderAdditionalPartyRoleMap", orderRolesMap);
                    }
                } catch (GenericEntityException e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Cannot create replacement order because order roles for original order are not available");
                    }
                }
                String createdOrderId = null;
                Map orderResult = null;
                try {
                    orderResult = remoteClient.runSync("storeOrder", orderMap);
                } catch (GenericServiceException e) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Problem creating the order!", e);
                    }
                } catch (RemoteException e) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Problem creating the order!", e);
                    }
                }
                if (orderResult != null) {
                    createdOrderId = (String) orderResult.get("orderId");
                    createdOrderIds.add(createdOrderId);
                }
                if (createdOrderId != null) {
                    OrderChangeHelper.approveOrder(remoteClient, userLogin, createdOrderId);
                }
            }
        }
        StringBuffer successMessage = new StringBuffer();
        if (createdOrderIds.size() > 0) {
            successMessage.append("The following new orders have been created : ");
            Iterator i = createdOrderIds.iterator();
            while (i.hasNext()) {
                successMessage.append(i.next());
                if (i.hasNext()) {
                    successMessage.append(", ");
                }
            }
        } else {
            successMessage.append("No orders were created.");
        }
        return ServiceUtil.returnSuccess(successMessage.toString());
    }

    /**
     * Takes a List of returnItems and returns a Map of orderId -> items and a Map of orderId -> orderTotal 
     * @param returnItems
     * @param itemsByOrder
     * @param totalByOrder
     * @param remoteClient
     * @param returnId
     */
    public static void groupReturnItemsByOrder(List returnItems, Map itemsByOrder, Map totalByOrder, CmsOFBizRemoteClient remoteClient, String returnId) {
        Iterator itemIt = returnItems.iterator();
        while (itemIt.hasNext()) {
            GenericValue item = (GenericValue) itemIt.next();
            String orderId = (String) item.getAllFields().get("orderId");
            if (orderId != null) {
                if (itemsByOrder != null) {
                    List orderList = (List) itemsByOrder.get(orderId);
                    Double totalForOrder = null;
                    if (totalByOrder != null) {
                        totalForOrder = (Double) totalByOrder.get(orderId);
                    }
                    if (orderList == null) {
                        orderList = new ArrayList();
                    }
                    if (totalForOrder == null) {
                        totalForOrder = new Double(0.00);
                    }
                    orderList.add(item);
                    itemsByOrder.put(orderId, orderList);
                    if (totalByOrder != null) {
                        Double quantity = (Double) item.getAllFields().get("returnQuantity");
                        Double amount = (Double) item.getAllFields().get("returnPrice");
                        if (quantity == null) {
                            quantity = new Double(0);
                        }
                        if (amount == null) {
                            amount = new Double(0.00);
                        }
                        double thisTotal = amount.doubleValue() * quantity.doubleValue();
                        double existingTotal = totalForOrder.doubleValue();
                        Map condition = UtilMisc.toMap("returnId", item.getAllFields().get("returnId"), "returnItemSeqId", item.getAllFields().get("returnItemSeqId"));
                        Double newTotal = new Double(existingTotal + thisTotal + getReturnAdjustmentTotal(remoteClient, condition));
                        totalByOrder.put(orderId, newTotal);
                    }
                }
            }
        }
        if ((totalByOrder != null) && (totalByOrder.keySet() != null)) {
            Iterator orderIterator = totalByOrder.keySet().iterator();
            while (orderIterator.hasNext()) {
                String orderId = (String) orderIterator.next();
                Map condition = UtilMisc.toMap("returnId", returnId, "returnItemSeqId", DataModelConstants.SEQ_ID_NA);
                double existingTotal = ((Double) totalByOrder.get(orderId)).doubleValue() + getReturnAdjustmentTotal(remoteClient, condition);
                totalByOrder.put(orderId, new Double(existingTotal));
            }
        }
    }

    public static Map getReturnAmountByOrder(CmsOFBizRemoteClient remoteClient, Map context) {
        String returnId = (String) context.get("returnId");
        Locale locale = (Locale) context.get("locale");
        List returnItems = null;
        Map returnAmountByOrder = new HashMap();
        try {
            returnItems = remoteClient.findByAnd("ReturnItem", UtilMisc.toMap("returnId", returnId));
        } catch (GenericEntityException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Problems looking up return information", e);
            }
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderErrorGettingReturnHeaderItemInformation", locale));
        }
        if ((returnItems != null) && (returnItems.size() > 0)) {
            Iterator returnItemIterator = returnItems.iterator();
            GenericValue returnItem = null;
            GenericValue returnItemResponse = null;
            GenericValue payment = null;
            String orderId;
            List paymentList = new ArrayList();
            while (returnItemIterator.hasNext()) {
                returnItem = (GenericValue) returnItemIterator.next();
                orderId = (String) returnItem.getAllFields().get("orderId");
                try {
                    returnItemResponse = remoteClient.getRelatedOne("ReturnItemResponse", returnItem);
                    if ((returnItemResponse != null) && (orderId != null)) {
                        payment = remoteClient.getRelatedOne("Payment", returnItemResponse);
                        if ((payment != null) && (payment.getAllFields().get("amount") != null) && !paymentList.contains(payment.getAllFields().get("paymentId"))) {
                            UtilMisc.addToDoubleInMap(returnAmountByOrder, orderId, (Double) payment.getAllFields().get("amount"));
                            paymentList.add(payment.getAllFields().get("paymentId"));
                        }
                    }
                } catch (GenericEntityException e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Problems looking up return item related information", e);
                    }
                    return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderErrorGettingReturnHeaderItemInformation", locale));
                }
            }
        }
        return UtilMisc.toMap("orderReturnAmountMap", returnAmountByOrder);
    }

    public static Map checkPaymentAmountForRefund(CmsOFBizRemoteClient remoteClient, Map context) {
        String returnId = (String) context.get("returnId");
        Locale locale = (Locale) context.get("locale");
        Map returnAmountByOrder = null;
        Map serviceResult = null;
        try {
            serviceResult = remoteClient.runSync("getReturnAmountByOrder", org.ofbiz.base.util.UtilMisc.toMap("returnId", returnId));
        } catch (GenericServiceException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Problem running the getReturnAmountByOrder service", e);
            }
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemsWithGetReturnAmountByOrder", locale));
        } catch (RemoteException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Problem running the getReturnAmountByOrder service", e);
            }
            return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderProblemsWithGetReturnAmountByOrder", locale));
        }
        if (ServiceUtil.isError(serviceResult)) {
            return ServiceUtil.returnError((String) serviceResult.get(ModelService.ERROR_MESSAGE));
        } else {
            returnAmountByOrder = (Map) serviceResult.get("orderReturnAmountMap");
        }
        if ((returnAmountByOrder != null) && (returnAmountByOrder.keySet() != null)) {
            Iterator orderIterator = returnAmountByOrder.keySet().iterator();
            while (orderIterator.hasNext()) {
                String orderId = (String) orderIterator.next();
                Double returnAmount = (Double) returnAmountByOrder.get(orderId);
                if (Math.abs(returnAmount.doubleValue()) < 0.000001) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Order [" + orderId + "] refund amount[ " + returnAmount + "] less than zero");
                    }
                    return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderReturnTotalCannotLessThanZero", locale));
                }
                OrderReadHelper helper = new OrderReadHelper(remoteClient, OrderReadHelper.getOrderHeader(remoteClient, orderId));
                double grandTotal = helper.getOrderGrandTotal(remoteClient);
                if (returnAmount == null) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("No returnAmount found for order:" + orderId);
                    }
                } else {
                    if ((returnAmount.doubleValue() - grandTotal) > 0.01) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Order [" + orderId + "] refund amount[ " + returnAmount + "] exceeds order total [" + grandTotal + "]");
                        }
                        return ServiceUtil.returnError(UtilProperties.getMessage(resource_error, "OrderRefundAmountExceedsOrderTotal", locale));
                    }
                }
            }
        }
        return ServiceUtil.returnSuccess();
    }

    public static Map createReturnAdjustment(CmsOFBizRemoteClient remoteClient, Map context) {
        String orderAdjustmentId = (String) context.get("orderAdjustmentId");
        String returnAdjustmentTypeId = (String) context.get("returnAdjustmentTypeId");
        String returnId = (String) context.get("returnId");
        String returnItemSeqId = (String) context.get("returnItemSeqId");
        String description = (String) context.get("description");
        GenericValue returnItemTypeMap = null;
        GenericValue orderAdjustment = null;
        GenericValue returnAdjustmentType = null;
        GenericValue orderItem = null;
        GenericValue returnItem = null;
        GenericValue returnHeader = null;
        Double amount;
        if (orderAdjustmentId != null) {
            try {
                orderAdjustment = remoteClient.findByPrimaryKey("OrderAdjustment", UtilMisc.toMap("orderAdjustmentId", orderAdjustmentId));
                returnHeader = remoteClient.findByPrimaryKey("ReturnHeader", UtilMisc.toMap("returnId", returnId));
                String returnHeaderTypeId = ((returnHeader != null) && (((String) returnHeader.getAllFields().get("returnHeaderTypeId")) != null)) ? ((String) returnHeader.getAllFields().get("returnHeaderTypeId")) : "CUSTOMER_RETURN";
                returnItemTypeMap = remoteClient.findByPrimaryKey("ReturnItemTypeMap", UtilMisc.toMap("returnHeaderTypeId", returnHeaderTypeId, "returnItemMapKey", orderAdjustment.getAllFields().get("orderAdjustmentTypeId")));
                returnAdjustmentType = remoteClient.getRelatedOne("ReturnAdjustmentType", returnItemTypeMap);
                if (returnAdjustmentType != null && UtilValidate.isEmpty(description)) {
                    description = (String) returnAdjustmentType.getAllFields().get("description");
                }
                if ((returnItemSeqId != null) && !("_NA_".equals(returnItemSeqId))) {
                    returnItem = remoteClient.findByPrimaryKey("ReturnItem", UtilMisc.toMap("returnId", returnId, "returnItemSeqId", returnItemSeqId));
                    if (LOG.isInfoEnabled()) {
                        LOG.info("returnId:" + returnId + ",returnItemSeqId:" + returnItemSeqId);
                    }
                    orderItem = remoteClient.getRelatedOne("OrderItem", returnItem);
                }
            } catch (GenericEntityException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(e);
                }
                throw new GeneralRuntimeException(e.getMessage());
            }
            context.putAll(orderAdjustment.getAllFields());
        }
        if (returnAdjustmentTypeId == null) {
            String mappingTypeId = returnItemTypeMap != null ? returnItemTypeMap.getAllFields().get("returnItemTypeId").toString() : null;
            returnAdjustmentTypeId = mappingTypeId != null ? mappingTypeId : "RET_MAN_ADJ";
        }
        if (returnItem != null) {
            if (needRecalculate(returnAdjustmentTypeId)) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("returnPrice:" + ((Double) returnItem.getAllFields().get("returnPrice")) + ",returnQuantity:" + ((Double) returnItem.getAllFields().get("returnQuantity")) + ",sourcePercentage:" + ((Double) orderAdjustment.getAllFields().get("sourcePercentage")));
                }
                if (orderAdjustment == null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("orderAdjustment [" + orderAdjustmentId + "] not found");
                    }
                    return ServiceUtil.returnError("orderAdjustment [" + orderAdjustmentId + "] not found");
                }
                BigDecimal returnTotal = ((BigDecimal) returnItem.getAllFields().get("returnPrice")).multiply((BigDecimal) returnItem.getAllFields().get("returnQuantity"));
                BigDecimal orderTotal = ((BigDecimal) orderItem.getAllFields().get("quantity")).multiply((BigDecimal) orderItem.getAllFields().get("unitPrice"));
                amount = getAdjustmentAmount("RET_SALES_TAX_ADJ".equals(returnAdjustmentType), returnTotal, orderTotal, (BigDecimal) orderAdjustment.getAllFields().get("amount"));
            } else {
                amount = (Double) context.get("amount");
            }
        } else {
            amount = (Double) context.get("amount");
        }
        String seqId = remoteClient.getNextSeqId("ReturnAdjustment");
        GenericValue newReturnAdjustment = remoteClient.makeValue("ReturnAdjustment", UtilMisc.toMap("returnAdjustmentId", seqId));
        try {
            newReturnAdjustment.setNonPKFields(context);
            if (orderAdjustment != null && orderAdjustment.getAllFields().get("taxAuthorityRateSeqId") != null) {
                newReturnAdjustment = remoteClient.set(newReturnAdjustment, "taxAuthorityRateSeqId", (String) orderAdjustment.getAllFields().get("taxAuthorityRateSeqId"));
            }
            newReturnAdjustment = remoteClient.set(newReturnAdjustment, "amount", amount);
            newReturnAdjustment = remoteClient.set(newReturnAdjustment, "returnAdjustmentTypeId", returnAdjustmentTypeId);
            newReturnAdjustment = remoteClient.set(newReturnAdjustment, "description", description);
            newReturnAdjustment = remoteClient.set(newReturnAdjustment, "returnItemSeqId", UtilValidate.isEmpty(returnItemSeqId) ? "_NA_" : returnItemSeqId);
            newReturnAdjustment = remoteClient.create(newReturnAdjustment);
            Map result = ServiceUtil.returnSuccess("Create ReturnAdjustment with Id:" + seqId + " successfully.");
            result.put("returnAdjustmentId", seqId);
            return result;
        } catch (GenericEntityException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Failed to store returnAdjustment", e);
            }
            return ServiceUtil.returnError("Failed to store returnAdjustment");
        }
    }

    public static Map updateReturnAdjustment(CmsOFBizRemoteClient remoteClient, Map context) {
        GenericValue returnItem = null;
        GenericValue returnAdjustment = null;
        String returnAdjustmentTypeId = null;
        Double amount;
        try {
            returnAdjustment = remoteClient.findByPrimaryKey("ReturnAdjustment", UtilMisc.toMap("returnAdjustmentId", context.get("returnAdjustmentId")));
            if (returnAdjustment != null) {
                returnItem = remoteClient.findByPrimaryKey("ReturnItem", UtilMisc.toMap("returnId", returnAdjustment.getAllFields().get("returnId"), "returnItemSeqId", returnAdjustment.getAllFields().get("returnItemSeqId")));
                returnAdjustmentTypeId = (String) returnAdjustment.getAllFields().get("returnAdjustmentTypeId");
            }
            if (returnItem != null) {
                double originalReturnPrice = (context.get("originalReturnPrice") != null) ? ((Double) context.get("originalReturnPrice")).doubleValue() : ((Double) returnItem.getAllFields().get("returnPrice")).doubleValue();
                double originalReturnQuantity = (context.get("originalReturnQuantity") != null) ? ((Double) context.get("originalReturnQuantity")).doubleValue() : ((Double) returnItem.getAllFields().get("returnQuantity")).doubleValue();
                if (needRecalculate(returnAdjustmentTypeId)) {
                    BigDecimal returnTotal = ((BigDecimal) returnItem.getAllFields().get("returnPrice")).multiply((BigDecimal) returnItem.getAllFields().get("returnQuantity"));
                    BigDecimal originalReturnTotal = new BigDecimal(originalReturnPrice).multiply(new BigDecimal(originalReturnQuantity));
                    amount = getAdjustmentAmount("RET_SALES_TAX_ADJ".equals(returnAdjustmentTypeId), returnTotal, originalReturnTotal, (BigDecimal) returnAdjustment.getAllFields().get("amount"));
                } else {
                    amount = (Double) context.get("amount");
                }
            } else {
                amount = (Double) context.get("amount");
            }
            returnAdjustment.setNonPKFields(context);
            returnAdjustment = remoteClient.set(returnAdjustment, "amount", amount);
            remoteClient.store(returnAdjustment);
            if (LOG.isInfoEnabled()) {
                LOG.info("Update ReturnAdjustment with Id:" + context.get("returnAdjustmentId") + " to amount " + amount + " successfully.");
            }
            Map result = ServiceUtil.returnSuccess("Update ReturnAdjustment with Id:" + context.get("returnAdjustmentId") + " to amount " + amount + " successfully.");
            return result;
        } catch (GenericEntityException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Failed to store returnAdjustment", e);
            }
            return ServiceUtil.returnError("Failed to store returnAdjustment");
        }
    }

    public static Map createReturnItemOrAdjustment(CmsOFBizRemoteClient remoteClient, Map context) {
        if (LOG.isInfoEnabled()) {
            LOG.info("createReturnItemOrAdjustment's context:" + context);
        }
        String orderItemSeqId = (String) context.get("orderItemSeqId");
        if (LOG.isInfoEnabled()) {
            LOG.info("orderItemSeqId:" + orderItemSeqId + "#");
        }
        String serviceName = UtilValidate.isNotEmpty(orderItemSeqId) ? "createReturnItem" : "createReturnAdjustment";
        if (LOG.isInfoEnabled()) {
            LOG.info("serviceName:" + serviceName);
        }
        try {
            return remoteClient.runSync(serviceName, context);
        } catch (org.ofbiz.service.GenericServiceException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e);
            }
            return ServiceUtil.returnError(e.getMessage());
        } catch (RemoteException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e);
            }
            return ServiceUtil.returnError(e.getMessage());
        }
    }

    public static Map updateReturnItemOrAdjustment(CmsOFBizRemoteClient remoteClient, Map context) {
        if (LOG.isInfoEnabled()) {
            LOG.info("updateReturnItemOrAdjustment's context:" + context);
        }
        String returnAdjustmentId = (String) context.get("returnAdjustmentId");
        if (LOG.isInfoEnabled()) {
            LOG.info("returnAdjustmentId:" + returnAdjustmentId + "#");
        }
        String serviceName = UtilValidate.isEmpty(returnAdjustmentId) ? "updateReturnItem" : "updateReturnAdjustment";
        if (LOG.isInfoEnabled()) {
            LOG.info("serviceName:" + serviceName);
        }
        try {
            return remoteClient.runSync(serviceName, context);
        } catch (org.ofbiz.service.GenericServiceException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e);
            }
            return ServiceUtil.returnError(e.getMessage());
        } catch (RemoteException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e);
            }
            return ServiceUtil.returnError(e.getMessage());
        }
    }

    /**
     * These return adjustment types need to be recalculated when the return item is updated
     * @param returnAdjustmentTypeId
     * @return
     */
    public static boolean needRecalculate(String returnAdjustmentTypeId) {
        return "RET_PROMOTION_ADJ".equals(returnAdjustmentTypeId) || "RET_DISCOUNT_ADJ".equals(returnAdjustmentTypeId) || "RET_SALES_TAX_ADJ".equals(returnAdjustmentTypeId);
    }

    /**
     * Get the total return adjustments for a set of key -> value condition pairs.  Done for code efficiency.
     * @param remoteClient
     * @param condition
     * @return
     */
    public static double getReturnAdjustmentTotal(CmsOFBizRemoteClient remoteClient, Map condition) {
        double total = 0.0;
        List adjustments;
        try {
            adjustments = remoteClient.findByAnd("ReturnAdjustment", condition);
            if (adjustments != null) {
                Iterator adjustmentIterator = adjustments.iterator();
                while (adjustmentIterator.hasNext()) {
                    GenericValue returnAdjustment = (GenericValue) adjustmentIterator.next();
                    total += ((Double) returnAdjustment.getAllFields().get("amount")).doubleValue();
                }
            }
        } catch (org.ofbiz.entity.GenericEntityException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e);
            }
        }
        return total;
    }

    /**
     * Calculate new returnAdjustment amount and set scale and rounding mode based on returnAdjustmentType: RET_SALES_TAX_ADJ use sales.tax._ and others use order._
     * @param isSalesTax  if returnAdjustmentType is SaleTax  
     * @param returnTotal
     * @param originalTotal
     * @param amount
     * @return  new returnAdjustment amount
     */
    public static Double getAdjustmentAmount(boolean isSalesTax, BigDecimal returnTotal, BigDecimal originalTotal, BigDecimal amount) {
        String settingPrefix = isSalesTax ? "salestax" : "order";
        String decimalsPrefix = isSalesTax ? ".calc" : "";
        int decimals = UtilNumber.getBigDecimalScale(settingPrefix + decimalsPrefix + ".decimals");
        int rounding = UtilNumber.getBigDecimalRoundingMode(settingPrefix + ".rounding");
        int finalDecimals = isSalesTax ? UtilNumber.getBigDecimalScale(settingPrefix + ".final.decimals") : decimals;
        returnTotal = returnTotal.setScale(decimals, rounding);
        originalTotal = originalTotal.setScale(decimals, rounding);
        BigDecimal newAmount = returnTotal.divide(originalTotal, decimals, rounding).multiply(amount).setScale(finalDecimals, rounding);
        return new Double(newAmount.doubleValue());
    }
}
