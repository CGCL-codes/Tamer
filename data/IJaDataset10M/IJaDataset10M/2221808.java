package org.orangegears.school.order;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javolution.util.FastList;
import javolution.util.FastMap;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.order.shoppingcart.CartItemModifyException;
import org.ofbiz.order.shoppingcart.CheckOutHelper;
import org.ofbiz.order.shoppingcart.ShoppingCart;
import org.ofbiz.order.shoppingcart.ShoppingCartEvents;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;
import org.orangegears.school.util.SchoolUtil;

public class OrderEvents {

    public static final String module = OrderEvents.class.getName();

    /**
	 * check term event 
	 * @param request
	 * @param response
	 * @return
	 */
    public static String checkTermDate(HttpServletRequest request, HttpServletResponse response) {
        GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        Locale locale = UtilHttp.getLocale(request);
        HttpSession session = request.getSession();
        List errMsgList = new LinkedList();
        String partyId = request.getParameter("partyId");
        if (UtilValidate.isEmpty(partyId)) {
            errMsgList.add("กรุณากรอกรหัสบุคคล");
        }
        String date = request.getParameter("date");
        String creditDate = request.getParameter("creditDate");
        if (UtilValidate.isEmpty(creditDate)) {
            errMsgList.add("กรุณากรอกเครดิตวัน");
        }
        String currencyUom = (String) UtilProperties.getPropertyValue("school_general.properties", "currency.uom.id");
        String productStoreId = (String) UtilProperties.getPropertyValue("school_general.properties", "product.store.id");
        System.out.println("========================date ========" + date);
        System.out.println("========================creditDate ========" + creditDate);
        if (UtilValidate.isNotEmpty(partyId) && UtilValidate.isNotEmpty(creditDate)) {
            Timestamp firstDate = SchoolUtil.getStartTimestamp(SchoolUtil.toSqlDate(date));
            Timestamp lastDate = SchoolUtil.getEndTimestamp(SchoolUtil.toSqlDate(creditDate));
            com.ibm.icu.util.Calendar cal1 = UtilDateTime.toCalendar(firstDate);
            com.ibm.icu.util.Calendar cal2 = UtilDateTime.toCalendar(lastDate);
            long milliseconds1 = cal1.getTimeInMillis();
            long milliseconds2 = cal2.getTimeInMillis();
            long resultDate = milliseconds2 - milliseconds1;
            Long numDate = resultDate / (24 * 60 * 60 * 1000);
            String termDays = String.valueOf(numDate);
            System.out.println("========================termDays ========" + termDays);
            try {
                GenericValue fact = delegator.findOne("Facility", UtilMisc.toMap("facilityId", "SCHOOL_FACILITY"), false);
                List<GenericValue> contactMech = fact.getRelated("FacilityContactMech", UtilMisc.toList("contactMechId"));
                if (UtilValidate.isNotEmpty(contactMech) && contactMech.size() > 0) {
                    GenericValue contactMechs = EntityUtil.getFirst(contactMech);
                    session.setAttribute("0_shipping_contact_mech_id", contactMechs);
                }
            } catch (GenericEntityException e) {
                Debug.logError(e, module);
                errMsgList.add(e.getMessage());
            }
            String forwardUrl = "/control/finalizeOrder?";
            String forwardQueryString = "0_shipping_method=NO_SHIPPING@_NA_&0_may_split=false&0_is_gift=false&additionalPartyType=None&partyId=" + partyId + "&currencyUomId=" + currencyUom + "&productStoreId=" + productStoreId + "&termDays=" + termDays;
            RequestDispatcher requestDispatcher = request.getSession().getServletContext().getRequestDispatcher(forwardUrl + forwardQueryString);
            try {
                requestDispatcher.forward(request, response);
            } catch (Exception e) {
                Debug.logError(e, module);
                errMsgList.add(e.getMessage());
            }
        }
        if (errMsgList.size() > 0) {
            request.setAttribute("_ERROR_MESSAGE_LIST_", errMsgList);
            return "error";
        } else {
            return "success";
        }
    }

    /**
	 * change Order Status
	 * @param request
	 * @param response
	 * @return
	 */
    public static String changeOrderStatus(HttpServletRequest request, HttpServletResponse response) {
        GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        ShoppingCart cart = ShoppingCartEvents.getCartObject(request);
        Locale locale = UtilHttp.getLocale(request);
        HttpSession session = request.getSession();
        List errMsgList = new LinkedList();
        String orderId = cart.getOrderId();
        System.out.println("================orderId========" + orderId);
        String comment = request.getParameter("comment");
        String referenceNumber = request.getParameter("referenceNumber");
        String partyId = request.getParameter("partyId");
        System.out.println("================comment========" + comment);
        System.out.println("================referenceNumber========" + referenceNumber);
        System.out.println("================partyId========" + partyId);
        Map serviceMap = FastMap.newInstance();
        if (UtilValidate.isNotEmpty(orderId)) {
            try {
                serviceMap.put("orderId", orderId);
                serviceMap.put("statusId", "ORDER_APPROVED");
                serviceMap.put("setItemStatus", "Y");
                serviceMap.put("locale", locale);
                serviceMap.put("userLogin", userLogin);
                dispatcher.runSync("changeOrderStatus", serviceMap);
                List<GenericValue> orderMap = delegator.findByAnd("OrderItemBilling", UtilMisc.toMap("orderId", orderId), UtilMisc.toList("orderItemSeqId"));
                GenericValue orderIdMap;
                if ((UtilValidate.isNotEmpty(comment) || UtilValidate.isNotEmpty(referenceNumber)) && (orderMap.size() > 0)) {
                    Map invoiceMap = FastMap.newInstance();
                    orderIdMap = EntityUtil.getFirst(orderMap);
                    System.out.println("================orderIdMap========: " + orderIdMap.getString("invoiceId"));
                    String invoiceId = orderIdMap.getString("invoiceId");
                    invoiceMap.put("invoiceId", invoiceId);
                    invoiceMap.put("description", comment);
                    invoiceMap.put("referenceNumber", referenceNumber);
                    invoiceMap.put("userLogin", userLogin);
                    dispatcher.runSync("updateInvoice", invoiceMap);
                } else {
                    String forwardUrl = "/control/clearpocart?";
                    String forwardQueryString = "orderId=" + orderId + "&partyId=" + partyId;
                    RequestDispatcher requestDispatcher = request.getSession().getServletContext().getRequestDispatcher(forwardUrl + forwardQueryString);
                    requestDispatcher.forward(request, response);
                }
            } catch (GenericEntityException e) {
                e.printStackTrace();
                Debug.logError(e, module);
                errMsgList.add(e.getMessage());
            } catch (GenericServiceException e) {
                e.printStackTrace();
                Debug.logError(e, module);
                errMsgList.add(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Debug.logError(e, module);
                errMsgList.add(e.getMessage());
            } catch (ServletException e) {
                e.printStackTrace();
                Debug.logError(e, module);
                errMsgList.add(e.getMessage());
            }
        }
        if (errMsgList.size() > 0) {
            request.setAttribute("_ERROR_MESSAGE_LIST_", errMsgList);
            return "error";
        } else {
            return "success";
        }
    }

    /**
	  * สร้าง order ด้านขาย แบบรายคน
	 * @param request
	 * @param response
	 * @return
	 */
    public static String checkBeforeCreateOrder(HttpServletRequest request, HttpServletResponse response) {
        GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        String partyId = request.getParameter("partyId");
        String classPartyId = request.getParameter("classPartyId");
        String roomLevelPartyId = request.getParameter("roomLevelPartyId");
        String orderDate = request.getParameter("orderDate");
        String yearPartyId = request.getParameter("yearPartyId");
        String termPartyId = request.getParameter("termPartyId");
        List errMsgList = new LinkedList();
        if (UtilValidate.isNotEmpty(partyId)) {
            try {
                String contactMechId = null;
                GenericValue party = delegator.findByPrimaryKey("Party", UtilMisc.toMap("partyId", partyId));
                List<GenericValue> partyContactMechLists = party.getRelated("PartyContactMech");
                for (Iterator<GenericValue> it = partyContactMechLists.iterator(); it.hasNext(); ) {
                    GenericValue partyContactMech = it.next();
                    if (partyContactMech.get("thruDate") == null) {
                        GenericValue contactMech = partyContactMech.getRelatedOne("ContactMech");
                        System.out.println("4========================= contactMechDefault : " + contactMech.get("contactMechId"));
                        if ("POSTAL_ADDRESS".equals(contactMech.get("contactMechTypeId"))) {
                            System.out.println("3========================= contactMechId : " + contactMech.get("contactMechId"));
                            List<GenericValue> telAndFaxList = contactMech.getRelated("PartyContactMechPurpose");
                            for (Iterator<GenericValue> nj = telAndFaxList.iterator(); nj.hasNext(); ) {
                                GenericValue datain = nj.next();
                                if (datain.getString("contactMechPurposeTypeId").equals("SHIPPING_LOCATION")) {
                                    System.out.println("2========================= contactMechId : " + datain.get("contactMechId"));
                                    contactMechId = datain.getString("contactMechId");
                                }
                            }
                        }
                    }
                }
                if (UtilValidate.isNotEmpty(contactMechId)) {
                    System.out.println("1========================= contactMechId : " + contactMechId);
                    System.out.println("1========================= response : " + response);
                    String forwardUrl = "/control/studentFinalizeOrder?";
                    String forwardQueryString = "&finalizeMode=init&finalizeMode=ship&0_supplierPartyId=KOWITTAMRONG&0_shipGroupFacilityId=SCHOOL_FACILITY&0_shipping_contact_mech_id=" + contactMechId + "&finalizeMode=options&0_shipping_method=NO_SHIPPING@_NA_&0_may_split=false&0_is_gift=false&finalizeMode=payment&checkOutPaymentId=EXT_OFFLINE&additionalPartyType=None";
                    RequestDispatcher requestDispatcher = request.getSession().getServletContext().getRequestDispatcher(forwardUrl + forwardQueryString);
                    requestDispatcher.forward(request, response);
                } else {
                    String forwardUrl = "/control/checkout?";
                    String forwardQueryString = "checkoutpage=quick&BACK_PAGE=quickcheckout&shipToCustomerPartyId=" + partyId + "&shipping_method=NO_SHIPPING@_NA_&may_split=false&is_gift=false&checkOutPaymentId=EXT_OFFLINE";
                    RequestDispatcher requestDispatcher = request.getSession().getServletContext().getRequestDispatcher(forwardUrl + forwardQueryString);
                    requestDispatcher.forward(request, response);
                }
            } catch (GenericEntityException e) {
                e.printStackTrace();
                Debug.logError(e, module);
                errMsgList.add(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Debug.logError(e, module);
                errMsgList.add(e.getMessage());
            } catch (ServletException e) {
                e.printStackTrace();
                Debug.logError(e, module);
                errMsgList.add(e.getMessage());
            }
        }
        if (errMsgList.size() > 0) {
            request.setAttribute("_ERROR_MESSAGE_LIST_", errMsgList);
            return "error";
        } else {
            return "success";
        }
    }

    /**
	 *  สร้าง order ด้านขาย แบบชั้น
	 * @param request
	 * @param response
	 * @return
	 */
    public static String checkBeforeCreateOrderByClass(HttpServletRequest request, HttpServletResponse response) {
        GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        Locale locale = request.getLocale();
        String yearPartyId = request.getParameter("yearPartyId");
        String termPartyId = request.getParameter("termPartyId");
        String classPartyId = request.getParameter("classPartyId");
        String roomLevelPartyId = request.getParameter("roomLevelPartyId");
        String orderDateIn = request.getParameter("orderDate");
        Date orderDates = SchoolUtil.toSqlDate(orderDateIn);
        Timestamp orderDate = SchoolUtil.getStartTimestamp(orderDates);
        List errMsgList = new LinkedList();
        ShoppingCart cartOut = ShoppingCartEvents.getCartObject(request);
        String orderName = "";
        final String schoolPartyId = (String) UtilProperties.getPropertyValue("school_party.properties", "party.school.id");
        try {
            if (UtilValidate.isNotEmpty(classPartyId)) {
                GenericValue classPartyGroup = delegator.findByPrimaryKey("PartyGroup", UtilMisc.toMap("partyId", classPartyId));
                orderName += " " + classPartyGroup.getString("groupName");
            }
            if (UtilValidate.isNotEmpty(roomLevelPartyId)) {
                GenericValue roomLevelPartyGroup = delegator.findByPrimaryKey("PartyGroup", UtilMisc.toMap("partyId", roomLevelPartyId));
                orderName += " " + roomLevelPartyGroup.getString("groupName");
            }
            if (UtilValidate.isNotEmpty(yearPartyId)) {
                GenericValue yearPartyGroup = delegator.findByPrimaryKey("Enumeration", UtilMisc.toMap("enumId", yearPartyId));
                orderName += " " + yearPartyGroup.getString("description");
            }
            if (UtilValidate.isNotEmpty(termPartyId)) {
                GenericValue termPartyGroup = delegator.findByPrimaryKey("Enumeration", UtilMisc.toMap("enumId", termPartyId));
                orderName += " " + termPartyGroup.getString("description");
            }
        } catch (GenericEntityException e) {
            e.printStackTrace();
            Debug.logError(e, module);
            errMsgList.add(e.getMessage());
        }
        orderName = orderName.trim();
        if (UtilValidate.isNotEmpty(classPartyId)) {
            try {
                List<GenericValue> partyList;
                List<Map<String, Object>> returnOrderMap = FastList.newInstance();
                if (UtilValidate.isNotEmpty(roomLevelPartyId)) {
                    List exprs = FastList.newInstance();
                    List<EntityExpr> exp1 = FastList.newInstance();
                    exp1.add(EntityCondition.makeCondition("partyIdFrom", EntityOperator.EQUALS, roomLevelPartyId));
                    exp1.add(EntityCondition.makeCondition("partyIdFrom", EntityOperator.EQUALS, classPartyId));
                    exprs.add(EntityCondition.makeCondition(exp1, EntityOperator.AND));
                    List<EntityExpr> exp2 = FastList.newInstance();
                    exp2.add(EntityCondition.makeCondition("roleTypeIdFrom", EntityOperator.EQUALS, "CLASS_ROLE"));
                    exp2.add(EntityCondition.makeCondition("roleTypeIdFrom", EntityOperator.EQUALS, "ROOM_ROLE"));
                    exprs.add(EntityCondition.makeCondition(exp2, EntityOperator.AND));
                    exprs.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "PTREL_ACTIVE"));
                    exprs.add(EntityCondition.makeCondition("partyRelationshipTypeId", EntityOperator.EQUALS, "GROUP_ROLLUP"));
                    exprs.add(EntityCondition.makeCondition("roleTypeIdTo", EntityOperator.EQUALS, "STUDENT_ROLE"));
                    EntityCondition cond = null;
                    if (exprs.size() > 0) {
                        cond = EntityCondition.makeCondition(exprs, EntityOperator.AND);
                    }
                    partyList = delegator.findList("PartyRelationship", cond, null, null, null, false);
                    System.out.println("checkBeforeCreateOrderByClass partyList TERM_0  =========" + partyList);
                } else {
                    List<EntityExpr> exprs = FastList.newInstance();
                    exprs.add(EntityCondition.makeCondition("partyIdFrom", EntityOperator.EQUALS, classPartyId));
                    exprs.add(EntityCondition.makeCondition("roleTypeIdFrom", EntityOperator.EQUALS, "CLASS_ROLE"));
                    exprs.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "PTREL_ACTIVE"));
                    exprs.add(EntityCondition.makeCondition("partyRelationshipTypeId", EntityOperator.EQUALS, "GROUP_ROLLUP"));
                    exprs.add(EntityCondition.makeCondition("roleTypeIdTo", EntityOperator.EQUALS, "STUDENT_ROLE"));
                    EntityCondition cond = null;
                    if (exprs.size() > 0) {
                        cond = EntityCondition.makeCondition(exprs, EntityOperator.AND);
                    }
                    partyList = delegator.findList("PartyRelationship", cond, null, null, null, false);
                    System.out.println("checkBeforeCreateOrderByClass partyList TERM_0 != null =========" + partyList);
                }
                if (UtilValidate.isNotEmpty(partyList) && partyList.size() > 0) {
                    for (GenericValue result : partyList) {
                        String partyId = result.getString("partyIdTo");
                        String orderId = null;
                        String contactMechId = null;
                        System.out.println("checkBeforeCreateOrderByClass partyId=========" + partyId);
                        GenericValue party = delegator.findByPrimaryKey("Party", UtilMisc.toMap("partyId", partyId));
                        List<GenericValue> partyContactMechLists = party.getRelated("PartyContactMech");
                        for (Iterator<GenericValue> it = partyContactMechLists.iterator(); it.hasNext(); ) {
                            GenericValue partyContactMech = it.next();
                            if (partyContactMech.get("thruDate") == null) {
                                GenericValue contactMech = partyContactMech.getRelatedOne("ContactMech");
                                System.out.println("4========================= contactMechDefault : " + contactMech.get("contactMechId"));
                                if ("POSTAL_ADDRESS".equals(contactMech.get("contactMechTypeId"))) {
                                    System.out.println("3========================= contactMechId : " + contactMech.get("contactMechId"));
                                    List<GenericValue> telAndFaxList = contactMech.getRelated("PartyContactMechPurpose");
                                    for (Iterator<GenericValue> nj = telAndFaxList.iterator(); nj.hasNext(); ) {
                                        GenericValue datain = nj.next();
                                        if (datain.getString("contactMechPurposeTypeId").equals("SHIPPING_LOCATION")) {
                                            System.out.println("2========================= contactMechId : " + datain.get("contactMechId"));
                                            contactMechId = datain.getString("contactMechId");
                                        }
                                    }
                                }
                            }
                        }
                        if (UtilValidate.isNotEmpty(contactMechId)) {
                            ShoppingCart cart = new ShoppingCart(cartOut);
                            cart.setOrderType("SALES_ORDER");
                            cart.setBillFromVendorPartyId(schoolPartyId);
                            cart.setBillToCustomerPartyId(partyId);
                            cart.setOrderPartyId(partyId);
                            cart.setOrderName("ใบรายการตั้งหนี้ของ :" + partyId + " " + orderName);
                            cart.setShippingContactMechId(contactMechId);
                            cart.setShipmentMethodTypeId("NO_SHIPPING");
                            cart.setCarrierPartyId("_NA_");
                            cart.setMaySplit(false);
                            cart.setIsGift(false);
                            cart.setLocale(locale);
                            cart.setUserLogin(userLogin, dispatcher);
                            Map orderIdMap = FastMap.newInstance();
                            new CheckOutHelper(dispatcher, delegator, cart);
                            orderIdMap = dispatcher.runSync("createOrderFromShoppingCart", UtilMisc.toMap("shoppingCart", cart), 90, true);
                            if (!ServiceUtil.isError(orderIdMap)) {
                                orderId = orderIdMap.get("orderId").toString();
                            }
                        } else {
                            ShoppingCart cart = new ShoppingCart(cartOut);
                            System.out.println("cart =================" + cart.items());
                            cart.setOrderType("SALES_ORDER");
                            cart.setBillFromVendorPartyId(schoolPartyId);
                            cart.setBillToCustomerPartyId(partyId);
                            cart.setOrderPartyId(partyId);
                            cart.setOrderName("ใบรายการตั้งหนี้ของ :" + partyId + " " + orderName);
                            cart.setShipmentMethodTypeId("NO_SHIPPING");
                            cart.setCarrierPartyId("_NA_");
                            cart.setMaySplit(false);
                            cart.setIsGift(false);
                            cart.setLocale(locale);
                            cart.setUserLogin(userLogin, dispatcher);
                            Map orderIdMap = FastMap.newInstance();
                            new CheckOutHelper(dispatcher, delegator, cart);
                            orderIdMap = dispatcher.runSync("createOrderFromShoppingCart", UtilMisc.toMap("shoppingCart", cart), 90, true);
                            if (!ServiceUtil.isError(orderIdMap)) {
                                orderId = orderIdMap.get("orderId").toString();
                            }
                        }
                        if (UtilValidate.isNotEmpty(orderId)) {
                            try {
                                Map<String, Object> partyOrderMap = FastMap.newInstance();
                                partyOrderMap.put("partyId", partyId);
                                partyOrderMap.put("orderId", orderId);
                                returnOrderMap.add(partyOrderMap);
                                Map serviceMap = FastMap.newInstance();
                                serviceMap.put("orderId", orderId);
                                serviceMap.put("statusId", "ORDER_APPROVED");
                                serviceMap.put("setItemStatus", "Y");
                                serviceMap.put("locale", locale);
                                serviceMap.put("userLogin", userLogin);
                                dispatcher.runSync("changeOrderStatus", serviceMap);
                                List<GenericValue> orderMap = delegator.findByAnd("OrderItemBilling", UtilMisc.toMap("orderId", orderId), UtilMisc.toList("orderItemSeqId"));
                                GenericValue orderIdMap;
                                if (orderMap.size() > 0) {
                                    Map invoiceAndClassMap = FastMap.newInstance();
                                    orderIdMap = EntityUtil.getFirst(orderMap);
                                    System.out.println("================orderIdMap to invoice========: " + orderIdMap.getString("invoiceId"));
                                    System.out.println("================orderId========: " + orderId);
                                    String invoiceId = orderIdMap.getString("invoiceId");
                                    invoiceAndClassMap.put("invoiceId", invoiceId);
                                    invoiceAndClassMap.put("partyId", partyId);
                                    invoiceAndClassMap.put("classId", classPartyId);
                                    invoiceAndClassMap.put("roomId", roomLevelPartyId);
                                    invoiceAndClassMap.put("yearId", yearPartyId);
                                    invoiceAndClassMap.put("termId", termPartyId);
                                    invoiceAndClassMap.put("description", "ใบกำกับรายการของ :" + partyId);
                                    invoiceAndClassMap.put("fromDate", orderDate);
                                    invoiceAndClassMap.put("userLogin", userLogin);
                                    dispatcher.runSync("createInvoiceAndClass", invoiceAndClassMap);
                                } else {
                                    Debug.logInfo("ค่าของ orderIdMap ไม่มีค่าจึงไม่สามารถดึงค่า invoice ได้:" + orderId, module);
                                }
                            } catch (GenericEntityException e) {
                                e.printStackTrace();
                                Debug.logError(e, module);
                                errMsgList.add(e.getMessage());
                            } catch (GenericServiceException e) {
                                e.printStackTrace();
                                Debug.logError(e, module);
                                errMsgList.add(e.getMessage());
                            }
                        } else {
                            Debug.logInfo("ค่าของ orderId ไม่มีค่า:" + orderId, module);
                        }
                    }
                    request.setAttribute("returnOrderMap", returnOrderMap);
                } else {
                    errMsgList.add("ค่าของ partyList ไม่มีค่า เนื่องจากไม่มีความสัมพันธ์ชั้นกับนักเรียน:" + partyList);
                }
            } catch (GenericEntityException e) {
                e.printStackTrace();
                Debug.logError(e, module);
                errMsgList.add(e.getMessage());
            } catch (CartItemModifyException e2) {
                e2.printStackTrace();
                Debug.logError(e2, module);
                errMsgList.add(e2.getMessage());
            } catch (GenericServiceException e3) {
                e3.printStackTrace();
                Debug.logError(e3, module);
                errMsgList.add(e3.getMessage());
            }
        }
        if (errMsgList.size() > 0) {
            request.setAttribute("_ERROR_MESSAGE_LIST_", errMsgList);
            return "error";
        } else {
            return "success";
        }
    }

    /**
	  * เปลี่ยนสถานะสำหรับ order ของ นักเรียน
	 * @param request
	 * @param response
	 * @return
	 */
    public static String changeStudentOrderStatus(HttpServletRequest request, HttpServletResponse response) {
        GenericDelegator delegator = (GenericDelegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        ShoppingCart cart = ShoppingCartEvents.getCartObject(request);
        Locale locale = UtilHttp.getLocale(request);
        HttpSession session = request.getSession();
        List errMsgList = new LinkedList();
        String orderId = cart.getOrderId();
        System.out.println("================orderId========" + orderId);
        String partyId = request.getParameter("partyId");
        System.out.println("================partyId========" + partyId);
        String yearPartyId = request.getParameter("yearPartyId");
        System.out.println("================yearPartyId========" + yearPartyId);
        String termPartyId = request.getParameter("termPartyId");
        System.out.println("================termPartyId========" + termPartyId);
        String classPartyId = request.getParameter("classPartyId");
        System.out.println("================classPartyId========" + classPartyId);
        String roomLevelPartyId = request.getParameter("roomLevelPartyId");
        System.out.println("================roomLevelPartyId========" + roomLevelPartyId);
        String orderDateIn = request.getParameter("orderDate");
        System.out.println("================orderDate========" + orderDateIn);
        Date orderDates = SchoolUtil.toSqlDate(orderDateIn);
        Timestamp orderDate = SchoolUtil.getStartTimestamp(orderDates);
        String orderName = "";
        Map serviceMap = FastMap.newInstance();
        if (UtilValidate.isNotEmpty(orderId) && UtilValidate.isNotEmpty(partyId)) {
            try {
                if (UtilValidate.isNotEmpty(classPartyId)) {
                    GenericValue classPartyGroup = delegator.findByPrimaryKey("PartyGroup", UtilMisc.toMap("partyId", classPartyId));
                    orderName += " " + classPartyGroup.getString("groupName");
                }
                if (UtilValidate.isNotEmpty(roomLevelPartyId)) {
                    GenericValue roomLevelPartyGroup = delegator.findByPrimaryKey("PartyGroup", UtilMisc.toMap("partyId", roomLevelPartyId));
                    orderName += " " + roomLevelPartyGroup.getString("groupName");
                }
                if (UtilValidate.isNotEmpty(yearPartyId)) {
                    GenericValue yearPartyGroup = delegator.findByPrimaryKey("Enumeration", UtilMisc.toMap("enumId", yearPartyId));
                    orderName += " " + yearPartyGroup.getString("description");
                }
                if (UtilValidate.isNotEmpty(termPartyId)) {
                    GenericValue termPartyGroup = delegator.findByPrimaryKey("Enumeration", UtilMisc.toMap("enumId", termPartyId));
                    orderName += " " + termPartyGroup.getString("description");
                }
                orderName = orderName.trim();
                serviceMap.put("orderId", orderId);
                serviceMap.put("statusId", "ORDER_APPROVED");
                serviceMap.put("setItemStatus", "Y");
                serviceMap.put("locale", locale);
                serviceMap.put("userLogin", userLogin);
                dispatcher.runSync("changeOrderStatus", serviceMap);
                Map orderHeaderMap = FastMap.newInstance();
                orderHeaderMap.put("orderId", orderId);
                orderHeaderMap.put("orderName", "ใบรายการตั้งหนี้ของ :" + partyId + " " + orderName);
                orderHeaderMap.put("userLogin", userLogin);
                dispatcher.runSync("updateOrderHeader", orderHeaderMap);
                List<GenericValue> orderMap = delegator.findByAnd("OrderItemBilling", UtilMisc.toMap("orderId", orderId), UtilMisc.toList("orderItemSeqId"));
                GenericValue orderIdMap;
                if (orderMap.size() > 0) {
                    Map invoiceAndClassMap = FastMap.newInstance();
                    orderIdMap = EntityUtil.getFirst(orderMap);
                    System.out.println("================orderIdMap to invoice========: " + orderIdMap.getString("invoiceId"));
                    String invoiceId = orderIdMap.getString("invoiceId");
                    invoiceAndClassMap.put("invoiceId", invoiceId);
                    invoiceAndClassMap.put("partyId", partyId);
                    invoiceAndClassMap.put("classId", classPartyId);
                    invoiceAndClassMap.put("roomId", roomLevelPartyId);
                    invoiceAndClassMap.put("yearId", yearPartyId);
                    invoiceAndClassMap.put("termId", termPartyId);
                    invoiceAndClassMap.put("description", "ใบกำกับรายการของ :" + partyId);
                    invoiceAndClassMap.put("fromDate", orderDate);
                    invoiceAndClassMap.put("userLogin", userLogin);
                    dispatcher.runSync("createInvoiceAndClass", invoiceAndClassMap);
                }
                String forwardUrl = "/control/clearcart?";
                String forwardQueryString = "orderId=" + orderId;
                RequestDispatcher requestDispatcher = request.getSession().getServletContext().getRequestDispatcher(forwardUrl + forwardQueryString);
                requestDispatcher.forward(request, response);
            } catch (GenericEntityException e) {
                e.printStackTrace();
                Debug.logError(e, module);
                errMsgList.add(e.getMessage());
            } catch (GenericServiceException e) {
                e.printStackTrace();
                Debug.logError(e, module);
                errMsgList.add(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Debug.logError(e, module);
                errMsgList.add(e.getMessage());
            } catch (ServletException e) {
                e.printStackTrace();
                Debug.logError(e, module);
                errMsgList.add(e.getMessage());
            }
        }
        if (errMsgList.size() > 0) {
            request.setAttribute("_ERROR_MESSAGE_LIST_", errMsgList);
            return "error";
        } else {
            return "success";
        }
    }
}
