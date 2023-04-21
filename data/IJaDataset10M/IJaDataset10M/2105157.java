package org.ofbiz.product.inventory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityConditionList;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.model.DynamicViewEntity;
import org.ofbiz.entity.model.ModelKeyMap;
import org.ofbiz.entity.util.EntityListIterator;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;

/**
 * Inventory Services 
 */
public class InventoryServices {

    public static final String module = InventoryServices.class.getName();

    public static Map prepareInventoryTransfer(DispatchContext dctx, Map context) {
        GenericDelegator delegator = dctx.getDelegator();
        String inventoryItemId = (String) context.get("inventoryItemId");
        Double xferQty = (Double) context.get("xferQty");
        GenericValue inventoryItem = null;
        GenericValue newItem = null;
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        try {
            inventoryItem = delegator.findByPrimaryKey("InventoryItem", UtilMisc.toMap("inventoryItemId", inventoryItemId));
        } catch (GenericEntityException e) {
            return ServiceUtil.returnError("Inventory item lookup problem [" + e.getMessage() + "]");
        }
        if (inventoryItem == null) {
            return ServiceUtil.returnError("Cannot locate inventory item.");
        }
        try {
            Map results = ServiceUtil.returnSuccess();
            String inventoryType = inventoryItem.getString("inventoryItemTypeId");
            if (inventoryType.equals("NON_SERIAL_INV_ITEM")) {
                Double atp = inventoryItem.getDouble("availableToPromiseTotal");
                Double qoh = inventoryItem.getDouble("quantityOnHandTotal");
                if (atp == null) {
                    return ServiceUtil.returnError("The request transfer amount is not available, there is no available to promise on the Inventory Item with ID " + inventoryItem.getString("inventoryItemId"));
                }
                if (qoh == null) {
                    qoh = atp;
                }
                if (xferQty.doubleValue() > atp.doubleValue()) {
                    return ServiceUtil.returnError("The request transfer amount is not available, the available to promise [" + atp + "] is not sufficient for the desired transfer quantity [" + xferQty + "] on the Inventory Item with ID " + inventoryItem.getString("inventoryItemId"));
                }
                if (xferQty.doubleValue() < atp.doubleValue() || atp.doubleValue() < qoh.doubleValue()) {
                    Double negXferQty = new Double(-xferQty.doubleValue());
                    newItem = GenericValue.create(inventoryItem);
                    newItem.set("availableToPromiseTotal", new Double(0));
                    newItem.set("quantityOnHandTotal", new Double(0));
                    String newSeqId = null;
                    try {
                        newSeqId = delegator.getNextSeqId("InventoryItem");
                    } catch (IllegalArgumentException e) {
                        return ServiceUtil.returnError("ERROR: Could not get next sequence id for InventoryItem, cannot create item.");
                    }
                    newItem.set("inventoryItemId", newSeqId);
                    newItem.create();
                    results.put("inventoryItemId", newItem.get("inventoryItemId"));
                    Map createNewDetailMap = UtilMisc.toMap("availableToPromiseDiff", xferQty, "quantityOnHandDiff", xferQty, "inventoryItemId", newItem.get("inventoryItemId"), "userLogin", userLogin);
                    Map createUpdateDetailMap = UtilMisc.toMap("availableToPromiseDiff", negXferQty, "quantityOnHandDiff", negXferQty, "inventoryItemId", inventoryItem.get("inventoryItemId"), "userLogin", userLogin);
                    try {
                        Map resultNew = dctx.getDispatcher().runSync("createInventoryItemDetail", createNewDetailMap);
                        if (ServiceUtil.isError(resultNew)) {
                            return ServiceUtil.returnError("Inventory Item Detail create problem in prepare inventory transfer", null, null, resultNew);
                        }
                        Map resultUpdate = dctx.getDispatcher().runSync("createInventoryItemDetail", createUpdateDetailMap);
                        if (ServiceUtil.isError(resultNew)) {
                            return ServiceUtil.returnError("Inventory Item Detail create problem in prepare inventory transfer", null, null, resultUpdate);
                        }
                    } catch (GenericServiceException e1) {
                        return ServiceUtil.returnError("Inventory Item Detail create problem in prepare inventory transfer: [" + e1.getMessage() + "]");
                    }
                } else {
                    results.put("inventoryItemId", inventoryItem.get("inventoryItemId"));
                }
            } else if (inventoryType.equals("SERIALIZED_INV_ITEM")) {
                if (!"INV_AVAILABLE".equals(inventoryItem.getString("statusId"))) {
                    return ServiceUtil.returnError("Serialized inventory is not available for transfer.");
                }
            }
            if (inventoryType.equals("NON_SERIAL_INV_ITEM")) {
                GenericValue inventoryItemToClear = newItem == null ? inventoryItem : newItem;
                inventoryItemToClear.refresh();
                double atp = inventoryItemToClear.get("availableToPromiseTotal") == null ? 0 : inventoryItemToClear.getDouble("availableToPromiseTotal").doubleValue();
                if (atp != 0) {
                    Map createDetailMap = UtilMisc.toMap("availableToPromiseDiff", new Double(-atp), "inventoryItemId", inventoryItemToClear.get("inventoryItemId"), "userLogin", userLogin);
                    try {
                        Map result = dctx.getDispatcher().runSync("createInventoryItemDetail", createDetailMap);
                        if (ServiceUtil.isError(result)) {
                            return ServiceUtil.returnError("Inventory Item Detail create problem in complete inventory transfer", null, null, result);
                        }
                    } catch (GenericServiceException e1) {
                        return ServiceUtil.returnError("Inventory Item Detail create problem in complete inventory transfer: [" + e1.getMessage() + "]");
                    }
                }
            } else if (inventoryType.equals("SERIALIZED_INV_ITEM")) {
                if (newItem != null) {
                    newItem.refresh();
                    newItem.set("statusId", "INV_BEING_TRANSFERED");
                    newItem.store();
                    results.put("inventoryItemId", newItem.get("inventoryItemId"));
                } else {
                    inventoryItem.refresh();
                    inventoryItem.set("statusId", "INV_BEING_TRANSFERED");
                    inventoryItem.store();
                    results.put("inventoryItemId", inventoryItem.get("inventoryItemId"));
                }
            }
            return results;
        } catch (GenericEntityException e) {
            return ServiceUtil.returnError("Inventory store/create problem [" + e.getMessage() + "]");
        }
    }

    public static Map completeInventoryTransfer(DispatchContext dctx, Map context) {
        GenericDelegator delegator = dctx.getDelegator();
        String inventoryTransferId = (String) context.get("inventoryTransferId");
        GenericValue inventoryTransfer = null;
        GenericValue inventoryItem = null;
        GenericValue destinationFacility = null;
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        try {
            inventoryTransfer = delegator.findByPrimaryKey("InventoryTransfer", UtilMisc.toMap("inventoryTransferId", inventoryTransferId));
            inventoryItem = inventoryTransfer.getRelatedOne("InventoryItem");
            destinationFacility = inventoryTransfer.getRelatedOne("ToFacility");
        } catch (GenericEntityException e) {
            return ServiceUtil.returnError("Inventory Item/Transfer lookup problem [" + e.getMessage() + "]");
        }
        if (inventoryTransfer == null || inventoryItem == null) {
            return ServiceUtil.returnError("ERROR: Lookup of InventoryTransfer and/or InventoryItem failed!");
        }
        String inventoryType = inventoryItem.getString("inventoryItemTypeId");
        if (inventoryTransfer.get("receiveDate") == null) {
            inventoryTransfer.set("receiveDate", UtilDateTime.nowTimestamp());
        }
        if (inventoryType.equals("NON_SERIAL_INV_ITEM")) {
            double atp = inventoryItem.get("availableToPromiseTotal") == null ? 0 : inventoryItem.getDouble("availableToPromiseTotal").doubleValue();
            double qoh = inventoryItem.get("quantityOnHandTotal") == null ? 0 : inventoryItem.getDouble("quantityOnHandTotal").doubleValue();
            Map createDetailMap = UtilMisc.toMap("availableToPromiseDiff", new Double(qoh - atp), "inventoryItemId", inventoryItem.get("inventoryItemId"), "userLogin", userLogin);
            try {
                Map result = dctx.getDispatcher().runSync("createInventoryItemDetail", createDetailMap);
                if (ServiceUtil.isError(result)) {
                    return ServiceUtil.returnError("Inventory Item Detail create problem in complete inventory transfer", null, null, result);
                }
            } catch (GenericServiceException e1) {
                return ServiceUtil.returnError("Inventory Item Detail create problem in complete inventory transfer: [" + e1.getMessage() + "]");
            }
            try {
                inventoryItem.refresh();
            } catch (GenericEntityException e) {
                return ServiceUtil.returnError("Inventory refresh problem [" + e.getMessage() + "]");
            }
        }
        Map updateInventoryItemMap = UtilMisc.toMap("inventoryItemId", inventoryItem.getString("inventoryItemId"), "facilityId", inventoryTransfer.get("facilityIdTo"), "containerId", inventoryTransfer.get("containerIdTo"), "locationSeqId", inventoryTransfer.get("locationSeqIdTo"), "userLogin", userLogin);
        if (inventoryType.equals("SERIALIZED_INV_ITEM")) {
            updateInventoryItemMap.put("statusId", "INV_AVAILABLE");
        }
        if (destinationFacility != null && destinationFacility.get("ownerPartyId") != null) {
            String fromPartyId = inventoryItem.getString("ownerPartyId");
            String toPartyId = destinationFacility.getString("ownerPartyId");
            if (fromPartyId == null || !fromPartyId.equals(toPartyId)) {
                updateInventoryItemMap.put("ownerPartyId", toPartyId);
            }
        }
        try {
            Map result = dctx.getDispatcher().runSync("updateInventoryItem", updateInventoryItemMap);
            if (ServiceUtil.isError(result)) {
                return ServiceUtil.returnError("Inventory item store problem", null, null, result);
            }
        } catch (GenericServiceException exc) {
            return ServiceUtil.returnError("Inventory item store problem [" + exc.getMessage() + "]");
        }
        inventoryTransfer.set("statusId", "IXF_COMPLETE");
        try {
            inventoryTransfer.store();
        } catch (GenericEntityException e) {
            return ServiceUtil.returnError("Inventory store problem [" + e.getMessage() + "]");
        }
        return ServiceUtil.returnSuccess();
    }

    public static Map cancelInventoryTransfer(DispatchContext dctx, Map context) {
        GenericDelegator delegator = dctx.getDelegator();
        String inventoryTransferId = (String) context.get("inventoryTransferId");
        GenericValue inventoryTransfer = null;
        GenericValue inventoryItem = null;
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        try {
            inventoryTransfer = delegator.findByPrimaryKey("InventoryTransfer", UtilMisc.toMap("inventoryTransferId", inventoryTransferId));
            if (UtilValidate.isEmpty(inventoryTransfer)) {
                return ServiceUtil.returnError("Inventory transfer [" + inventoryTransferId + "] not found");
            }
            inventoryItem = inventoryTransfer.getRelatedOne("InventoryItem");
        } catch (GenericEntityException e) {
            return ServiceUtil.returnError("Inventory Item/Transfer lookup problem [" + e.getMessage() + "]");
        }
        if (inventoryTransfer == null || inventoryItem == null) {
            return ServiceUtil.returnError("ERROR: Lookup of InventoryTransfer and/or InventoryItem failed!");
        }
        String inventoryType = inventoryItem.getString("inventoryItemTypeId");
        if (inventoryType.equals("NON_SERIAL_INV_ITEM")) {
            double atp = inventoryItem.get("availableToPromiseTotal") == null ? 0 : inventoryItem.getDouble("availableToPromiseTotal").doubleValue();
            double qoh = inventoryItem.get("quantityOnHandTotal") == null ? 0 : inventoryItem.getDouble("quantityOnHandTotal").doubleValue();
            Map createDetailMap = UtilMisc.toMap("availableToPromiseDiff", new Double(qoh - atp), "inventoryItemId", inventoryItem.get("inventoryItemId"), "userLogin", userLogin);
            try {
                Map result = dctx.getDispatcher().runSync("createInventoryItemDetail", createDetailMap);
                if (ServiceUtil.isError(result)) {
                    return ServiceUtil.returnError("Inventory Item Detail create problem in cancel inventory transfer", null, null, result);
                }
            } catch (GenericServiceException e1) {
                return ServiceUtil.returnError("Inventory Item Detail create problem in cancel inventory transfer: [" + e1.getMessage() + "]");
            }
        } else if (inventoryType.equals("SERIALIZED_INV_ITEM")) {
            inventoryItem.set("statusId", "INV_AVAILABLE");
            try {
                inventoryItem.store();
            } catch (GenericEntityException e) {
                return ServiceUtil.returnError("Inventory item store problem in cancel inventory transfer: [" + e.getMessage() + "]");
            }
        }
        inventoryTransfer.set("statusId", "IXF_CANCELLED");
        try {
            inventoryTransfer.store();
        } catch (GenericEntityException e) {
            return ServiceUtil.returnError("Inventory store problem [" + e.getMessage() + "]");
        }
        return ServiceUtil.returnSuccess();
    }

    /** In spite of the generic name this does the very specific task of checking availability of all back-ordered items and sends notices, etc */
    public static Map checkInventoryAvailability(DispatchContext dctx, Map context) {
        GenericDelegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map ordersToUpdate = new HashMap();
        Map ordersToCancel = new HashMap();
        List inventoryItems = null;
        try {
            List exprs = UtilMisc.toList(new EntityExpr("availableToPromiseTotal", EntityOperator.LESS_THAN, new Double(0)));
            inventoryItems = delegator.findByAnd("InventoryItem", exprs);
        } catch (GenericEntityException e) {
            Debug.logError(e, "Trouble getting inventory items", module);
            return ServiceUtil.returnError("Problem getting InventoryItem records");
        }
        if (inventoryItems == null) {
            Debug.logInfo("No items out of stock; no backorders to worry about", module);
            return ServiceUtil.returnSuccess();
        }
        Debug.log("OOS Inventory Items: " + inventoryItems.size(), module);
        Iterator itemsIter = inventoryItems.iterator();
        while (itemsIter.hasNext()) {
            GenericValue inventoryItem = (GenericValue) itemsIter.next();
            List shipmentAndItems = null;
            try {
                List exprs = new ArrayList();
                exprs.add(new EntityExpr("productId", EntityOperator.EQUALS, inventoryItem.get("productId")));
                exprs.add(new EntityExpr("destinationFacilityId", EntityOperator.EQUALS, inventoryItem.get("facilityId")));
                exprs.add(new EntityExpr("statusId", EntityOperator.NOT_EQUAL, "SHIPMENT_DELIVERED"));
                exprs.add(new EntityExpr("statusId", EntityOperator.NOT_EQUAL, "SHIPMENT_CANCELLED"));
                shipmentAndItems = delegator.findByAnd("ShipmentAndItem", exprs, UtilMisc.toList("estimatedArrivalDate"));
            } catch (GenericEntityException e) {
                Debug.logError(e, "Problem getting ShipmentAndItem records", module);
                return ServiceUtil.returnError("Problem getting ShipmentAndItem records");
            }
            List reservations = null;
            try {
                reservations = inventoryItem.getRelated("OrderItemShipGrpInvRes", null, UtilMisc.toList("-reservedDatetime"));
            } catch (GenericEntityException e) {
                Debug.logError(e, "Problem getting related reservations", module);
                return ServiceUtil.returnError("Problem getting related reservations");
            }
            if (reservations == null) {
                Debug.logWarning("No outstanding reservations for this inventory item, why is it negative then?", module);
                continue;
            }
            Debug.log("Reservations for item: " + reservations.size(), module);
            double availableBeforeReserved = inventoryItem.getDouble("availableToPromiseTotal").doubleValue();
            Iterator ri = reservations.iterator();
            while (ri.hasNext()) {
                GenericValue reservation = (GenericValue) ri.next();
                String orderId = reservation.getString("orderId");
                String orderItemSeqId = reservation.getString("orderItemSeqId");
                Timestamp promisedDate = reservation.getTimestamp("promisedDatetime");
                Timestamp currentPromiseDate = reservation.getTimestamp("currentPromisedDate");
                Timestamp actualPromiseDate = currentPromiseDate;
                if (actualPromiseDate == null) {
                    if (promisedDate != null) {
                        actualPromiseDate = promisedDate;
                    } else {
                        actualPromiseDate = reservation.getTimestamp("reservedDatetime");
                    }
                }
                Debug.log("Promised Date: " + actualPromiseDate, module);
                Timestamp nextShipDate = null;
                double availableAtTime = 0.00;
                Iterator si = shipmentAndItems.iterator();
                while (si.hasNext()) {
                    GenericValue shipmentItem = (GenericValue) si.next();
                    availableAtTime += shipmentItem.getDouble("quantity").doubleValue();
                    if (availableAtTime >= availableBeforeReserved) {
                        nextShipDate = shipmentItem.getTimestamp("estimatedArrivalDate");
                        break;
                    }
                }
                Debug.log("Next Ship Date: " + nextShipDate, module);
                Calendar pCal = Calendar.getInstance();
                pCal.setTimeInMillis(actualPromiseDate.getTime());
                pCal.add(Calendar.DAY_OF_YEAR, -1);
                Timestamp modifiedPromisedDate = new Timestamp(pCal.getTimeInMillis());
                Timestamp now = UtilDateTime.nowTimestamp();
                Debug.log("Promised Date + 1: " + modifiedPromisedDate, module);
                Debug.log("Now: " + now, module);
                if (nextShipDate == null || nextShipDate.after(actualPromiseDate)) {
                    if (nextShipDate == null && modifiedPromisedDate.after(now)) {
                        Debug.log("No ship date known yet, but promised date hasn't approached, assuming it will be here on time", module);
                    } else {
                        Debug.log("We won't ship on time, getting notification info", module);
                        Map notifyItems = (Map) ordersToUpdate.get(orderId);
                        if (notifyItems == null) {
                            notifyItems = new HashMap();
                        }
                        notifyItems.put(orderItemSeqId, nextShipDate);
                        ordersToUpdate.put(orderId, notifyItems);
                        Calendar sCal = Calendar.getInstance();
                        sCal.setTimeInMillis(actualPromiseDate.getTime());
                        sCal.add(Calendar.DAY_OF_YEAR, 30);
                        Timestamp farPastPromised = new Timestamp(sCal.getTimeInMillis());
                        boolean needToCancel = false;
                        if (nextShipDate == null || nextShipDate.after(farPastPromised)) {
                            Debug.log("Ship date is >30 past the promised date", module);
                            needToCancel = true;
                        } else if (currentPromiseDate != null && actualPromiseDate.equals(currentPromiseDate)) {
                            needToCancel = true;
                        }
                        if (needToCancel) {
                            Debug.log("Flagging the item to auto-cancel", module);
                            Map cancelItems = (Map) ordersToCancel.get(orderId);
                            if (cancelItems == null) {
                                cancelItems = new HashMap();
                            }
                            cancelItems.put(orderItemSeqId, farPastPromised);
                            ordersToCancel.put(orderId, cancelItems);
                        }
                        try {
                            reservation.set("currentPromisedDate", nextShipDate);
                            reservation.store();
                        } catch (GenericEntityException e) {
                            Debug.logError(e, "Problem storing reservation : " + reservation, module);
                        }
                    }
                }
                availableBeforeReserved -= reservation.getDouble("quantity").doubleValue();
            }
        }
        List ordersToNotify = new ArrayList();
        Set orderSet = ordersToUpdate.keySet();
        Iterator orderIter = orderSet.iterator();
        while (orderIter.hasNext()) {
            String orderId = (String) orderIter.next();
            Map backOrderedItems = (Map) ordersToUpdate.get(orderId);
            Map cancelItems = (Map) ordersToCancel.get(orderId);
            boolean cancelAll = false;
            Timestamp cancelAllTime = null;
            List orderItemShipGroups = null;
            try {
                orderItemShipGroups = delegator.findByAnd("OrderItemShipGroup", UtilMisc.toMap("orderId", orderId));
            } catch (GenericEntityException e) {
                Debug.logError(e, "Cannot get OrderItemShipGroups from orderId" + orderId, module);
            }
            Iterator orderItemShipGroupsIter = orderItemShipGroups.iterator();
            while (orderItemShipGroupsIter.hasNext()) {
                GenericValue orderItemShipGroup = (GenericValue) orderItemShipGroupsIter.next();
                List orderItems = new java.util.Vector();
                List orderItemShipGroupAssoc = null;
                try {
                    orderItemShipGroupAssoc = delegator.findByAnd("OrderItemShipGroupAssoc", UtilMisc.toMap("shipGroupSeqId", orderItemShipGroup.get("shipGroupSeqId"), "orderId", orderId));
                    Iterator assocIter = orderItemShipGroupAssoc.iterator();
                    while (assocIter.hasNext()) {
                        GenericValue assoc = (GenericValue) assocIter.next();
                        GenericValue orderItem = assoc.getRelatedOne("OrderItem");
                        if (orderItem != null) {
                            orderItems.add(orderItem);
                        }
                    }
                } catch (GenericEntityException e) {
                    Debug.logError(e, "Problem fetching OrderItemShipGroupAssoc", module);
                }
                boolean maySplit = false;
                if (orderItemShipGroup != null && orderItemShipGroup.get("maySplit") != null) {
                    maySplit = orderItemShipGroup.getBoolean("maySplit").booleanValue();
                }
                if (!maySplit && cancelItems != null) {
                    cancelAll = true;
                    Set cancelSet = cancelItems.keySet();
                    cancelAllTime = (Timestamp) cancelItems.get(cancelSet.iterator().next());
                }
                if (cancelItems == null) {
                    cancelItems = new HashMap();
                }
                if (orderItems != null) {
                    List toBeStored = new ArrayList();
                    Iterator orderItemsIter = orderItems.iterator();
                    while (orderItemsIter.hasNext()) {
                        GenericValue orderItem = (GenericValue) orderItemsIter.next();
                        String orderItemSeqId = orderItem.getString("orderItemSeqId");
                        Timestamp shipDate = (Timestamp) backOrderedItems.get(orderItemSeqId);
                        Timestamp cancelDate = (Timestamp) cancelItems.get(orderItemSeqId);
                        Timestamp currentCancelDate = orderItem.getTimestamp("autoCancelDate");
                        Debug.logError("OI: " + orderId + " SEQID: " + orderItemSeqId + " cancelAll: " + cancelAll + " cancelDate: " + cancelDate, module);
                        if (backOrderedItems.containsKey(orderItemSeqId)) {
                            orderItem.set("estimatedShipDate", shipDate);
                            if (currentCancelDate == null) {
                                if (cancelAll || cancelDate != null) {
                                    if (orderItem.get("dontCancelSetUserLogin") == null && orderItem.get("dontCancelSetDate") == null) {
                                        if (cancelAllTime != null) {
                                            orderItem.set("autoCancelDate", cancelAllTime);
                                        } else {
                                            orderItem.set("autoCancelDate", cancelDate);
                                        }
                                    }
                                }
                                ordersToNotify.add(orderId);
                            }
                            toBeStored.add(orderItem);
                        }
                    }
                    if (toBeStored.size() > 0) {
                        try {
                            delegator.storeAll(toBeStored);
                        } catch (GenericEntityException e) {
                            Debug.logError(e, "Problem storing order items", module);
                        }
                    }
                }
            }
        }
        Iterator orderNotifyIter = ordersToNotify.iterator();
        while (orderNotifyIter.hasNext()) {
            String orderId = (String) orderNotifyIter.next();
            try {
                dispatcher.runAsync("sendOrderBackorderNotification", UtilMisc.toMap("orderId", orderId, "userLogin", userLogin));
            } catch (GenericServiceException e) {
                Debug.logError(e, "Problems sending off the notification", module);
                continue;
            }
        }
        return ServiceUtil.returnSuccess();
    }

    /**
     * Get Inventory Available for a Product based on the list of associated products.  The final ATP and QOH will
     * be the minimum of all the associated products' inventory divided by their ProductAssoc.quantity 
     * */
    public static Map getProductInventoryAvailableFromAssocProducts(DispatchContext dctx, Map context) {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        List productAssocList = (List) context.get("assocProducts");
        String facilityId = (String) context.get("facilityId");
        Double availableToPromiseTotal = new Double(0);
        Double quantityOnHandTotal = new Double(0);
        if (productAssocList != null && productAssocList.size() > 0) {
            double minQuantityOnHandTotal = Double.MAX_VALUE;
            double minAvailableToPromiseTotal = Double.MAX_VALUE;
            for (int i = 0; productAssocList.size() > i; i++) {
                GenericValue productAssoc = (GenericValue) productAssocList.get(i);
                String productIdTo = productAssoc.getString("productIdTo");
                Double assocQuantity = productAssoc.getDouble("quantity");
                if (assocQuantity == null) {
                    Debug.logWarning("ProductAssoc from [" + productAssoc.getString("productId") + "] to [" + productAssoc.getString("productIdTo") + "] has no quantity, assuming 1.0", module);
                    assocQuantity = new Double(1.0);
                }
                Map resultOutput = null;
                try {
                    Map inputMap = UtilMisc.toMap("productId", productIdTo);
                    if (facilityId != null) {
                        inputMap.put("facilityId", facilityId);
                        resultOutput = dispatcher.runSync("getInventoryAvailableByFacility", inputMap);
                    } else {
                        resultOutput = dispatcher.runSync("getProductInventoryAvailable", inputMap);
                    }
                } catch (GenericServiceException e) {
                    Debug.logError(e, "Problems getting inventory available by facility", module);
                    return ServiceUtil.returnError(e.getMessage());
                }
                Double currentQuantityOnHandTotal = (Double) resultOutput.get("quantityOnHandTotal");
                Double currentAvailableToPromiseTotal = (Double) resultOutput.get("availableToPromiseTotal");
                double tmpQuantityOnHandTotal = currentQuantityOnHandTotal.doubleValue() / assocQuantity.doubleValue();
                double tmpAvailableToPromiseTotal = currentAvailableToPromiseTotal.doubleValue() / assocQuantity.doubleValue();
                if (tmpQuantityOnHandTotal < minQuantityOnHandTotal) {
                    minQuantityOnHandTotal = tmpQuantityOnHandTotal;
                }
                if (tmpAvailableToPromiseTotal < minAvailableToPromiseTotal) {
                    minAvailableToPromiseTotal = tmpAvailableToPromiseTotal;
                }
                if (Debug.verboseOn()) {
                    Debug.logVerbose("productIdTo = " + productIdTo + " assocQuantity = " + assocQuantity + "current QOH " + currentQuantityOnHandTotal + "currentATP = " + currentAvailableToPromiseTotal + " minQOH = " + minQuantityOnHandTotal + " minATP = " + minAvailableToPromiseTotal, module);
                }
            }
            quantityOnHandTotal = new Double(minQuantityOnHandTotal);
            availableToPromiseTotal = new Double(minAvailableToPromiseTotal);
        }
        Map result = ServiceUtil.returnSuccess();
        result.put("availableToPromiseTotal", availableToPromiseTotal);
        result.put("quantityOnHandTotal", quantityOnHandTotal);
        return result;
    }

    public static Map getProductInventorySummaryForItems(DispatchContext dctx, Map context) {
        GenericDelegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        List orderItems = (List) context.get("orderItems");
        String facilityId = (String) context.get("facilityId");
        Map atpMap = new HashMap();
        Map qohMap = new HashMap();
        Map mktgPkgAtpMap = new HashMap();
        Map mktgPkgQohMap = new HashMap();
        Map results = ServiceUtil.returnSuccess();
        List facilities = null;
        try {
            if (facilityId != null) {
                facilities = delegator.findByAnd("Facility", UtilMisc.toMap("facilityId", facilityId));
            } else {
                facilities = delegator.findAll("Facility");
            }
        } catch (GenericEntityException e) {
            return ServiceUtil.returnError("Unable to locate facilities." + e.getMessage());
        }
        Iterator iter = orderItems.iterator();
        while (iter.hasNext()) {
            GenericValue orderItem = (GenericValue) iter.next();
            String productId = orderItem.getString("productId");
            if ((productId == null) || productId.equals("")) continue;
            GenericValue product = null;
            try {
                product = orderItem.getRelatedOneCache("Product");
            } catch (GenericEntityException e) {
                Debug.logError(e, "Couldn't get product.", module);
                return ServiceUtil.returnError("Unable to retrive product with id [" + productId + "]");
            }
            double atp = 0.0;
            double qoh = 0.0;
            double mktgPkgAtp = 0.0;
            double mktgPkgQoh = 0.0;
            Iterator facilityIter = facilities.iterator();
            while (facilityIter.hasNext()) {
                GenericValue facility = (GenericValue) facilityIter.next();
                Map invResult = null;
                Map mktgPkgInvResult = null;
                try {
                    if ("MARKETING_PKG_AUTO".equals(product.getString("productTypeId"))) {
                        mktgPkgInvResult = dispatcher.runSync("getMktgPackagesAvailable", UtilMisc.toMap("productId", productId, "facilityId", facility.getString("facilityId")));
                    }
                    invResult = dispatcher.runSync("getInventoryAvailableByFacility", UtilMisc.toMap("productId", productId, "facilityId", facility.getString("facilityId")));
                } catch (GenericServiceException e) {
                    String msg = "Could not find inventory for facility [" + facility.getString("facilityId") + "]";
                    Debug.logError(e, msg, module);
                    return ServiceUtil.returnError(msg);
                }
                if (!ServiceUtil.isError(invResult)) {
                    Double fatp = (Double) invResult.get("availableToPromiseTotal");
                    Double fqoh = (Double) invResult.get("quantityOnHandTotal");
                    if (fatp != null) atp += fatp.doubleValue();
                    if (fqoh != null) qoh += fqoh.doubleValue();
                }
                if (("MARKETING_PKG_AUTO".equals(product.getString("productTypeId"))) && (!ServiceUtil.isError(mktgPkgInvResult))) {
                    Double fatp = (Double) mktgPkgInvResult.get("availableToPromiseTotal");
                    Double fqoh = (Double) mktgPkgInvResult.get("quantityOnHandTotal");
                    if (fatp != null) mktgPkgAtp += fatp.doubleValue();
                    if (fqoh != null) mktgPkgQoh += fqoh.doubleValue();
                }
            }
            atpMap.put(productId, new Double(atp));
            qohMap.put(productId, new Double(qoh));
            mktgPkgAtpMap.put(productId, new Double(mktgPkgAtp));
            mktgPkgQohMap.put(productId, new Double(mktgPkgQoh));
        }
        results.put("availableToPromiseMap", atpMap);
        results.put("quantityOnHandMap", qohMap);
        results.put("mktgPkgATPMap", mktgPkgAtpMap);
        results.put("mktgPkgQOHMap", mktgPkgQohMap);
        return results;
    }

    public static Map getProductInventoryAndFacilitySummary(DispatchContext dctx, Map context) {
        GenericDelegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Timestamp checkTime = (Timestamp) context.get("checkTime");
        String facilityId = (String) context.get("facilityId");
        String productId = (String) context.get("productId");
        String minimumStock = (String) context.get("minimumStock");
        Map result = new HashMap();
        Map resultOutput = new HashMap();
        Map contextInput = UtilMisc.toMap("productId", productId, "facilityId", facilityId);
        GenericValue product = null;
        try {
            product = delegator.findByPrimaryKey("Product", UtilMisc.toMap("productId", productId));
        } catch (GenericEntityException e) {
            e.printStackTrace();
        }
        if ("MARKETING_PKG_AUTO".equals(product.getString("productTypeId"))) {
            try {
                resultOutput = dispatcher.runSync("getMktgPackagesAvailable", contextInput);
            } catch (GenericServiceException e) {
                e.printStackTrace();
            }
        } else {
            try {
                resultOutput = dispatcher.runSync("getInventoryAvailableByFacility", contextInput);
            } catch (GenericServiceException e) {
                e.printStackTrace();
            }
        }
        int minimumStockInt = 0;
        if (minimumStock != null) {
            minimumStockInt = new Double(minimumStock).intValue();
        }
        int quantityOnHandTotalInt = 0;
        if (resultOutput.get("quantityOnHandTotal") != null) {
            quantityOnHandTotalInt = ((Double) resultOutput.get("quantityOnHandTotal")).intValue();
        }
        int offsetQOHQtyAvailable = quantityOnHandTotalInt - minimumStockInt;
        int availableToPromiseTotalInt = 0;
        if (resultOutput.get("availableToPromiseTotal") != null) {
            availableToPromiseTotalInt = ((Double) resultOutput.get("availableToPromiseTotal")).intValue();
        }
        int offsetATPQtyAvailable = availableToPromiseTotalInt - minimumStockInt;
        double quantityOnOrder = InventoryWorker.getOutstandingPurchasedQuantity(productId, delegator);
        result.put("totalQuantityOnHand", resultOutput.get("quantityOnHandTotal").toString());
        result.put("totalAvailableToPromise", resultOutput.get("availableToPromiseTotal").toString());
        result.put("quantityOnOrder", new Double(quantityOnOrder));
        result.put("offsetQOHQtyAvailable", new Integer(offsetQOHQtyAvailable));
        result.put("offsetATPQtyAvailable", new Integer(offsetATPQtyAvailable));
        List productPrices = null;
        try {
            productPrices = (List) delegator.findByAndCache("ProductPrice", UtilMisc.toMap("productId", productId), UtilMisc.toList("-fromDate"));
        } catch (GenericEntityException e) {
            e.printStackTrace();
        }
        Iterator pricesIt = productPrices.iterator();
        while (pricesIt.hasNext()) {
            GenericValue onePrice = (GenericValue) pricesIt.next();
            if (onePrice.getString("productPriceTypeId").equals("DEFAULT_PRICE")) {
                result.put("defultPrice", onePrice.getDouble("price"));
            } else if (onePrice.getString("productPriceTypeId").equals("WHOLESALE_PRICE")) {
                result.put("wholeSalePrice", onePrice.getDouble("price"));
            } else if (onePrice.getString("productPriceTypeId").equals("LIST_PRICE")) {
                result.put("listPrice", onePrice.getDouble("price"));
            } else {
                result.put("defultPrice", onePrice.getDouble("price"));
                result.put("listPrice", onePrice.getDouble("price"));
                result.put("wholeSalePrice", onePrice.getDouble("price"));
            }
        }
        DynamicViewEntity salesUsageViewEntity = new DynamicViewEntity();
        DynamicViewEntity productionUsageViewEntity = new DynamicViewEntity();
        if (!UtilValidate.isEmpty(checkTime)) {
            salesUsageViewEntity.addMemberEntity("OI", "OrderItem");
            salesUsageViewEntity.addMemberEntity("OH", "OrderHeader");
            salesUsageViewEntity.addMemberEntity("ItIss", "ItemIssuance");
            salesUsageViewEntity.addMemberEntity("InvIt", "InventoryItem");
            salesUsageViewEntity.addViewLink("OI", "OH", new Boolean(false), ModelKeyMap.makeKeyMapList("orderId"));
            salesUsageViewEntity.addViewLink("OI", "ItIss", new Boolean(false), ModelKeyMap.makeKeyMapList("orderId", "orderId", "orderItemSeqId", "orderItemSeqId"));
            salesUsageViewEntity.addViewLink("ItIss", "InvIt", new Boolean(false), ModelKeyMap.makeKeyMapList("inventoryItemId"));
            salesUsageViewEntity.addAlias("OI", "productId");
            salesUsageViewEntity.addAlias("OH", "statusId");
            salesUsageViewEntity.addAlias("OH", "orderTypeId");
            salesUsageViewEntity.addAlias("OH", "orderDate");
            salesUsageViewEntity.addAlias("ItIss", "inventoryItemId");
            salesUsageViewEntity.addAlias("ItIss", "quantity");
            salesUsageViewEntity.addAlias("InvIt", "facilityId");
            productionUsageViewEntity.addMemberEntity("WEIA", "WorkEffortInventoryAssign");
            productionUsageViewEntity.addMemberEntity("WE", "WorkEffort");
            productionUsageViewEntity.addMemberEntity("II", "InventoryItem");
            productionUsageViewEntity.addViewLink("WEIA", "WE", new Boolean(false), ModelKeyMap.makeKeyMapList("workEffortId"));
            productionUsageViewEntity.addViewLink("WEIA", "II", new Boolean(false), ModelKeyMap.makeKeyMapList("inventoryItemId"));
            productionUsageViewEntity.addAlias("WEIA", "quantity");
            productionUsageViewEntity.addAlias("WE", "actualCompletionDate");
            productionUsageViewEntity.addAlias("WE", "workEffortTypeId");
            productionUsageViewEntity.addAlias("II", "facilityId");
            productionUsageViewEntity.addAlias("II", "productId");
        }
        if (!UtilValidate.isEmpty(checkTime)) {
            EntityListIterator salesUsageIt = null;
            try {
                salesUsageIt = delegator.findListIteratorByCondition(salesUsageViewEntity, new EntityConditionList(UtilMisc.toList(new EntityExpr("facilityId", EntityOperator.EQUALS, facilityId), new EntityExpr("productId", EntityOperator.EQUALS, productId), new EntityExpr("statusId", EntityOperator.IN, UtilMisc.toList("ORDER_COMPLETED", "ORDER_APPROVED", "ORDER_HELD")), new EntityExpr("orderTypeId", EntityOperator.EQUALS, "SALES_ORDER"), new EntityExpr("orderDate", EntityOperator.GREATER_THAN_EQUAL_TO, checkTime)), EntityOperator.AND), null, null, null, null);
            } catch (GenericEntityException e2) {
                e2.printStackTrace();
            }
            double salesUsageQuantity = 0;
            GenericValue salesUsageItem = null;
            while ((salesUsageItem = (GenericValue) salesUsageIt.next()) != null) {
                if (salesUsageItem.get("quantity") != null) {
                    try {
                        salesUsageQuantity += salesUsageItem.getDouble("quantity").doubleValue();
                    } catch (Exception e) {
                    }
                }
            }
            try {
                salesUsageIt.close();
            } catch (GenericEntityException e2) {
                e2.printStackTrace();
            }
            EntityListIterator productionUsageIt = null;
            try {
                productionUsageIt = delegator.findListIteratorByCondition(productionUsageViewEntity, new EntityConditionList(UtilMisc.toList(new EntityExpr("facilityId", EntityOperator.EQUALS, facilityId), new EntityExpr("productId", EntityOperator.EQUALS, productId), new EntityExpr("workEffortTypeId", EntityOperator.EQUALS, "PROD_ORDER_TASK"), new EntityExpr("actualCompletionDate", EntityOperator.GREATER_THAN_EQUAL_TO, checkTime)), EntityOperator.AND), null, null, null, null);
            } catch (GenericEntityException e1) {
                e1.printStackTrace();
            }
            double productionUsageQuantity = 0;
            GenericValue productionUsageItem = null;
            while ((productionUsageItem = (GenericValue) productionUsageIt.next()) != null) {
                if (productionUsageItem.get("quantity") != null) {
                    try {
                        productionUsageQuantity += productionUsageItem.getDouble("quantity").doubleValue();
                    } catch (Exception e) {
                    }
                }
            }
            try {
                productionUsageIt.close();
            } catch (GenericEntityException e) {
                e.printStackTrace();
            }
            result.put("usageQuantity", new Double((salesUsageQuantity + productionUsageQuantity)));
        }
        return result;
    }
}
