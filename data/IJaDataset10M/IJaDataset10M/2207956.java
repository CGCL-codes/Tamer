package org.campware.cream.modules.actions;

import java.util.Date;
import java.text.ParsePosition;
import org.apache.turbine.modules.actions.VelocitySecureAction;
import org.apache.turbine.util.RunData;
import org.apache.turbine.util.security.AccessControlList;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import org.apache.velocity.context.Context;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.BasePeer;
import com.workingdogs.village.Record;
import org.campware.cream.om.PaymentPeer;
import org.campware.cream.om.CustomerPeer;
import org.campware.cream.om.ProductPeer;
import org.campware.cream.om.ProjectPeer;
import org.campware.cream.om.PaymentItemPeer;
import org.campware.cream.om.OnlineSubscriptionPeer;
import org.campware.cream.om.PrintSubscriptionPeer;
import org.campware.cream.om.NewsSubscriptionPeer;
import org.campware.cream.om.ShipmentPeer;
import org.campware.cream.om.CurrencyPeer;

/**
 * This class provides a simple set of methods to
 * insert/update/delete records in a database.
 */
public class ReportSQL extends VelocitySecureAction {

    /**
     * Show report data based on
     * filter parameters and report choosen
     */
    public void doReport(RunData data, Context context) throws Exception {
        try {
            int reportid = data.getParameters().getInt("reportid", 0);
            Criteria criteria = new Criteria();
            if (reportid == 1) {
                criteria.addJoin(PaymentPeer.PAYMENT_ID, PaymentItemPeer.PAYMENT_ID);
                criteria.addJoin(CustomerPeer.CUSTOMER_ID, PaymentItemPeer.CUSTOMER_ID);
                criteria.addJoin(ProductPeer.PRODUCT_ID, PaymentItemPeer.PRODUCT_ID);
                criteria.addJoin(ProjectPeer.PROJECT_ID, PaymentItemPeer.PROJECT_ID);
                criteria.addSelectColumn(PaymentItemPeer.PRODUCT_ID);
                criteria.addSelectColumn("SUM(" + PaymentItemPeer.ITEM_TOTAL + ")");
                criteria.addSelectColumn(ProductPeer.PRODUCT_CODE);
                criteria.addSelectColumn(ProductPeer.PRODUCT_DISPLAY);
                setPaymentCriteria(data, criteria);
                setCustomerCriteria(data, criteria);
                setProjectCriteria(data, criteria);
                setProductCriteria(data, criteria);
                criteria.addGroupByColumn(PaymentItemPeer.PRODUCT_ID);
                criteria.addGroupByColumn(ProductPeer.PRODUCT_CODE);
                criteria.addGroupByColumn(ProductPeer.PRODUCT_DISPLAY);
                criteria.addAscendingOrderByColumn(ProductPeer.PRODUCT_CODE);
                List records = BasePeer.doSelect(criteria);
                List results = new ArrayList();
                for (int i = 0; i < records.size(); i++) {
                    results.add(((Record) records.get(i)));
                }
                context.put("entries", results);
                Criteria critall = new Criteria();
                critall.addJoin(PaymentPeer.PAYMENT_ID, PaymentItemPeer.PAYMENT_ID);
                critall.addJoin(CustomerPeer.CUSTOMER_ID, PaymentItemPeer.CUSTOMER_ID);
                critall.addJoin(ProductPeer.PRODUCT_ID, PaymentItemPeer.PRODUCT_ID);
                critall.addJoin(ProjectPeer.PROJECT_ID, PaymentItemPeer.PROJECT_ID);
                critall.addSelectColumn("SUM(" + PaymentItemPeer.ITEM_TOTAL + ")");
                setPaymentCriteria(data, critall);
                setCustomerCriteria(data, critall);
                setProjectCriteria(data, critall);
                setProductCriteria(data, critall);
                List sumrecord = BasePeer.doSelect(critall);
                BigDecimal sumAll = ((Record) sumrecord.get(0)).getValue(1).asBigDecimal();
                context.put("sumall", sumAll);
            } else if (reportid == 2) {
                criteria.addJoin(PaymentPeer.PAYMENT_ID, PaymentItemPeer.PAYMENT_ID);
                criteria.addJoin(CurrencyPeer.CURRENCY_ID, PaymentItemPeer.CURRENCY_ID);
                criteria.addJoin(CustomerPeer.CUSTOMER_ID, PaymentItemPeer.CUSTOMER_ID);
                criteria.addJoin(ProductPeer.PRODUCT_ID, PaymentItemPeer.PRODUCT_ID);
                criteria.addJoin(ProjectPeer.PROJECT_ID, PaymentItemPeer.PROJECT_ID);
                criteria.addSelectColumn(PaymentItemPeer.CURRENCY_ID);
                criteria.addSelectColumn("SUM(" + PaymentItemPeer.ITEM_CURR_TOTAL + ")");
                criteria.addSelectColumn(CurrencyPeer.CURRENCY_CODE);
                criteria.addSelectColumn(CurrencyPeer.CURRENCY_NAME);
                setPaymentCriteria(data, criteria);
                setCustomerCriteria(data, criteria);
                setProjectCriteria(data, criteria);
                setProductCriteria(data, criteria);
                criteria.addGroupByColumn(PaymentItemPeer.CURRENCY_ID);
                criteria.addGroupByColumn(CurrencyPeer.CURRENCY_CODE);
                criteria.addGroupByColumn(CurrencyPeer.CURRENCY_NAME);
                criteria.addAscendingOrderByColumn(CurrencyPeer.CURRENCY_CODE);
                List records = BasePeer.doSelect(criteria);
                List results = new ArrayList();
                for (int i = 0; i < records.size(); i++) {
                    results.add(((Record) records.get(i)));
                }
                context.put("entries", results);
            } else if (reportid == 3) {
                criteria.addJoin(CustomerPeer.CUSTOMER_ID, OnlineSubscriptionPeer.CUSTOMER_ID);
                criteria.addJoin(ProductPeer.PRODUCT_ID, OnlineSubscriptionPeer.PRODUCT_ID);
                criteria.addJoin(ProjectPeer.PROJECT_ID, OnlineSubscriptionPeer.PROJECT_ID);
                criteria.addSelectColumn(OnlineSubscriptionPeer.PRODUCT_ID);
                criteria.addSelectColumn("SUM(" + OnlineSubscriptionPeer.QUANTITY + ")");
                criteria.addSelectColumn(ProductPeer.PRODUCT_CODE);
                criteria.addSelectColumn(ProductPeer.PRODUCT_DISPLAY);
                setOnlineSubscriptionCriteria(data, criteria);
                setCustomerCriteria(data, criteria);
                setProjectCriteria(data, criteria);
                setProductCriteria(data, criteria);
                criteria.addGroupByColumn(OnlineSubscriptionPeer.PRODUCT_ID);
                criteria.addGroupByColumn(ProductPeer.PRODUCT_CODE);
                criteria.addGroupByColumn(ProductPeer.PRODUCT_DISPLAY);
                criteria.addAscendingOrderByColumn(ProductPeer.PRODUCT_CODE);
                List records = BasePeer.doSelect(criteria);
                List results = new ArrayList();
                for (int i = 0; i < records.size(); i++) {
                    results.add(((Record) records.get(i)));
                }
                context.put("entries", results);
                Criteria critall = new Criteria();
                critall.addJoin(CustomerPeer.CUSTOMER_ID, OnlineSubscriptionPeer.CUSTOMER_ID);
                critall.addJoin(ProductPeer.PRODUCT_ID, OnlineSubscriptionPeer.PRODUCT_ID);
                critall.addJoin(ProjectPeer.PROJECT_ID, OnlineSubscriptionPeer.PROJECT_ID);
                critall.addSelectColumn("SUM(" + OnlineSubscriptionPeer.QUANTITY + ")");
                setOnlineSubscriptionCriteria(data, critall);
                setCustomerCriteria(data, critall);
                setProjectCriteria(data, critall);
                setProductCriteria(data, critall);
                List sumrecord = BasePeer.doSelect(critall);
                int sumAll = ((Record) sumrecord.get(0)).getValue(1).asInt();
                context.put("sumall", new Integer(sumAll));
            } else if (reportid == 4) {
                criteria.addJoin(CustomerPeer.CUSTOMER_ID, PrintSubscriptionPeer.CUSTOMER_ID);
                criteria.addJoin(ProductPeer.PRODUCT_ID, PrintSubscriptionPeer.PRODUCT_ID);
                criteria.addJoin(ProjectPeer.PROJECT_ID, PrintSubscriptionPeer.PROJECT_ID);
                criteria.addSelectColumn(PrintSubscriptionPeer.PRODUCT_ID);
                criteria.addSelectColumn("SUM(" + PrintSubscriptionPeer.QUANTITY + ")");
                criteria.addSelectColumn(ProductPeer.PRODUCT_CODE);
                criteria.addSelectColumn(ProductPeer.PRODUCT_DISPLAY);
                setPrintSubscriptionCriteria(data, criteria);
                setCustomerCriteria(data, criteria);
                setProjectCriteria(data, criteria);
                setProductCriteria(data, criteria);
                criteria.addGroupByColumn(PrintSubscriptionPeer.PRODUCT_ID);
                criteria.addGroupByColumn(ProductPeer.PRODUCT_CODE);
                criteria.addGroupByColumn(ProductPeer.PRODUCT_DISPLAY);
                criteria.addAscendingOrderByColumn(ProductPeer.PRODUCT_CODE);
                List records = BasePeer.doSelect(criteria);
                List results = new ArrayList();
                for (int i = 0; i < records.size(); i++) {
                    results.add(((Record) records.get(i)));
                }
                context.put("entries", results);
                Criteria critall = new Criteria();
                critall.addJoin(CustomerPeer.CUSTOMER_ID, PrintSubscriptionPeer.CUSTOMER_ID);
                critall.addJoin(ProductPeer.PRODUCT_ID, PrintSubscriptionPeer.PRODUCT_ID);
                critall.addJoin(ProjectPeer.PROJECT_ID, PrintSubscriptionPeer.PROJECT_ID);
                critall.addSelectColumn("SUM(" + PrintSubscriptionPeer.QUANTITY + ")");
                setPrintSubscriptionCriteria(data, critall);
                setCustomerCriteria(data, critall);
                setProjectCriteria(data, critall);
                setProductCriteria(data, critall);
                List sumrecord = BasePeer.doSelect(critall);
                int sumAll = ((Record) sumrecord.get(0)).getValue(1).asInt();
                context.put("sumall", new Integer(sumAll));
            } else if (reportid == 5) {
                criteria.addJoin(CustomerPeer.CUSTOMER_ID, PrintSubscriptionPeer.RECIPIENT_ID);
                criteria.addJoin(ProductPeer.PRODUCT_ID, PrintSubscriptionPeer.PRODUCT_ID);
                criteria.addJoin(ProjectPeer.PROJECT_ID, PrintSubscriptionPeer.PROJECT_ID);
                setPrintSubscriptionCriteria(data, criteria);
                setCustomerCriteria(data, criteria);
                setProjectCriteria(data, criteria);
                setProductCriteria(data, criteria);
                criteria.addAscendingOrderByColumn(CustomerPeer.CUSTOMER_DISPLAY);
                context.put("entries", PrintSubscriptionPeer.doSelect(criteria));
            } else if (reportid == 6) {
                criteria.addJoin(CustomerPeer.CUSTOMER_ID, ShipmentPeer.RECIPIENT_ID);
                criteria.addJoin(ProjectPeer.PROJECT_ID, ShipmentPeer.PROJECT_ID);
                setShipmentCriteria(data, criteria);
                setCustomerCriteria(data, criteria);
                setProjectCriteria(data, criteria);
                criteria.addAscendingOrderByColumn(CustomerPeer.CUSTOMER_DISPLAY);
                context.put("entries", ShipmentPeer.doSelect(criteria));
            } else if (reportid == 7) {
                criteria.addJoin(ProductPeer.PRODUCT_ID, NewsSubscriptionPeer.PRODUCT_ID);
                criteria.addJoin(ProjectPeer.PROJECT_ID, NewsSubscriptionPeer.PROJECT_ID);
                criteria.addSelectColumn(NewsSubscriptionPeer.PRODUCT_ID);
                criteria.addSelectColumn("COUNT(" + NewsSubscriptionPeer.NEWS_SUBS_ID + ")");
                criteria.addSelectColumn(ProductPeer.PRODUCT_CODE);
                criteria.addSelectColumn(ProductPeer.PRODUCT_DISPLAY);
                setNewsSubscriptionCriteria(data, criteria);
                setProjectCriteria(data, criteria);
                setProductCriteria(data, criteria);
                criteria.addGroupByColumn(NewsSubscriptionPeer.PRODUCT_ID);
                criteria.addGroupByColumn(ProductPeer.PRODUCT_CODE);
                criteria.addGroupByColumn(ProductPeer.PRODUCT_DISPLAY);
                criteria.addAscendingOrderByColumn(ProductPeer.PRODUCT_CODE);
                List records = BasePeer.doSelect(criteria);
                List results = new ArrayList();
                for (int i = 0; i < records.size(); i++) {
                    results.add(((Record) records.get(i)));
                }
                context.put("entries", results);
                Criteria critall = new Criteria();
                critall.addJoin(ProductPeer.PRODUCT_ID, NewsSubscriptionPeer.PRODUCT_ID);
                critall.addJoin(ProjectPeer.PROJECT_ID, NewsSubscriptionPeer.PROJECT_ID);
                critall.addSelectColumn("COUNT(" + NewsSubscriptionPeer.NEWS_SUBS_ID + ")");
                setNewsSubscriptionCriteria(data, critall);
                setProjectCriteria(data, critall);
                setProductCriteria(data, critall);
                List sumrecord = BasePeer.doSelect(critall);
                int sumAll = ((Record) sumrecord.get(0)).getValue(1).asInt();
                context.put("sumall", new Integer(sumAll));
            }
            context.put("reptitle", data.getParameters().getString("reptitle"));
            context.put("df", new SimpleDateFormat("dd.MM.yyyy"));
            DecimalFormatSymbols symb = new DecimalFormatSymbols();
            symb.setDecimalSeparator('.');
            context.put("af", new DecimalFormat("0.00", symb));
            context.put("rf", new DecimalFormat("0.000000", symb));
            context.put("today", new Date());
        } catch (Exception e) {
            throw (e);
        }
    }

    private void setPaymentCriteria(RunData data, Criteria criteria) throws Exception {
        Date fromDate = parseDate(data.getParameters().getString("fromdate"));
        Date toDate = parseDate(data.getParameters().getString("todate"));
        int status = data.getParameters().getInt("status", 30);
        if (status == 50) {
            Criteria.Criterion b1 = criteria.getNewCriterion(PaymentPeer.CLOSED_DATE, fromDate, Criteria.GREATER_EQUAL);
            Criteria.Criterion b2 = criteria.getNewCriterion(PaymentPeer.CLOSED_DATE, toDate, Criteria.LESS_THAN);
            criteria.add(b1.and(b2));
        } else {
            Criteria.Criterion b1 = criteria.getNewCriterion(PaymentPeer.ISSUED_DATE, fromDate, Criteria.GREATER_EQUAL);
            Criteria.Criterion b2 = criteria.getNewCriterion(PaymentPeer.ISSUED_DATE, toDate, Criteria.LESS_THAN);
            criteria.add(b1.and(b2));
        }
        criteria.add(PaymentPeer.STATUS, new Integer(status), Criteria.EQUAL);
    }

    private void setOnlineSubscriptionCriteria(RunData data, Criteria criteria) throws Exception {
        Date fromDate = parseDate(data.getParameters().getString("fromdate"));
        Date toDate = parseDate(data.getParameters().getString("todate"));
        int status = data.getParameters().getInt("status", 30);
        if (status == 50) {
            Criteria.Criterion b1 = criteria.getNewCriterion(OnlineSubscriptionPeer.CLOSED_DATE, fromDate, Criteria.GREATER_EQUAL);
            Criteria.Criterion b2 = criteria.getNewCriterion(OnlineSubscriptionPeer.CLOSED_DATE, toDate, Criteria.LESS_THAN);
            criteria.add(b1.and(b2));
        } else {
            Criteria.Criterion b1 = criteria.getNewCriterion(OnlineSubscriptionPeer.ISSUED_DATE, fromDate, Criteria.GREATER_EQUAL);
            Criteria.Criterion b2 = criteria.getNewCriterion(OnlineSubscriptionPeer.ISSUED_DATE, toDate, Criteria.LESS_THAN);
            criteria.add(b1.and(b2));
        }
        criteria.add(OnlineSubscriptionPeer.STATUS, new Integer(status), Criteria.EQUAL);
    }

    private void setPrintSubscriptionCriteria(RunData data, Criteria criteria) throws Exception {
        Date fromDate = parseDate(data.getParameters().getString("fromdate"));
        Date toDate = parseDate(data.getParameters().getString("todate"));
        int status = data.getParameters().getInt("status", 30);
        if (status == 50) {
            Criteria.Criterion b1 = criteria.getNewCriterion(PrintSubscriptionPeer.CLOSED_DATE, fromDate, Criteria.GREATER_EQUAL);
            Criteria.Criterion b2 = criteria.getNewCriterion(PrintSubscriptionPeer.CLOSED_DATE, toDate, Criteria.LESS_THAN);
            criteria.add(b1.and(b2));
        } else {
            Criteria.Criterion b1 = criteria.getNewCriterion(PrintSubscriptionPeer.ISSUED_DATE, fromDate, Criteria.GREATER_EQUAL);
            Criteria.Criterion b2 = criteria.getNewCriterion(PrintSubscriptionPeer.ISSUED_DATE, toDate, Criteria.LESS_THAN);
            criteria.add(b1.and(b2));
        }
        criteria.add(PrintSubscriptionPeer.STATUS, new Integer(status), Criteria.EQUAL);
    }

    private void setNewsSubscriptionCriteria(RunData data, Criteria criteria) throws Exception {
        Date fromDate = parseDate(data.getParameters().getString("fromdate"));
        Date toDate = parseDate(data.getParameters().getString("todate"));
        int status = data.getParameters().getInt("status", 30);
        if (status == 50) {
            Criteria.Criterion b1 = criteria.getNewCriterion(NewsSubscriptionPeer.CLOSED_DATE, fromDate, Criteria.GREATER_EQUAL);
            Criteria.Criterion b2 = criteria.getNewCriterion(NewsSubscriptionPeer.CLOSED_DATE, toDate, Criteria.LESS_THAN);
            criteria.add(b1.and(b2));
        } else {
            Criteria.Criterion b1 = criteria.getNewCriterion(NewsSubscriptionPeer.ISSUED_DATE, fromDate, Criteria.GREATER_EQUAL);
            Criteria.Criterion b2 = criteria.getNewCriterion(NewsSubscriptionPeer.ISSUED_DATE, toDate, Criteria.LESS_THAN);
            criteria.add(b1.and(b2));
        }
        criteria.add(NewsSubscriptionPeer.STATUS, new Integer(status), Criteria.EQUAL);
    }

    private void setShipmentCriteria(RunData data, Criteria criteria) throws Exception {
        Date fromDate = parseDate(data.getParameters().getString("fromdate"));
        Date toDate = parseDate(data.getParameters().getString("todate"));
        int status = data.getParameters().getInt("status", 30);
        if (status == 50) {
            Criteria.Criterion b1 = criteria.getNewCriterion(ShipmentPeer.CLOSED_DATE, fromDate, Criteria.GREATER_EQUAL);
            Criteria.Criterion b2 = criteria.getNewCriterion(ShipmentPeer.CLOSED_DATE, toDate, Criteria.LESS_THAN);
            criteria.add(b1.and(b2));
        } else {
            Criteria.Criterion b1 = criteria.getNewCriterion(ShipmentPeer.ISSUED_DATE, fromDate, Criteria.GREATER_EQUAL);
            Criteria.Criterion b2 = criteria.getNewCriterion(ShipmentPeer.ISSUED_DATE, toDate, Criteria.LESS_THAN);
            criteria.add(b1.and(b2));
        }
        criteria.add(ShipmentPeer.STATUS, new Integer(status), Criteria.EQUAL);
    }

    private void setCustomerCriteria(RunData data, Criteria criteria) throws Exception {
        int customerId = data.getParameters().getInt("customerid", 999);
        int customerCatId = data.getParameters().getInt("customercatid", 999);
        int customerType = data.getParameters().getInt("customertype", 1);
        int countryId = data.getParameters().getInt("countryid", 999);
        int regionId = data.getParameters().getInt("regionid", 999);
        int languageId = data.getParameters().getInt("languageid", 999);
        int industryId = data.getParameters().getInt("industryid", 999);
        int leadSourceId = data.getParameters().getInt("leadsourceid", 999);
        if (customerId > 999) {
            criteria.add(CustomerPeer.CUSTOMER_ID, new Integer(customerId), Criteria.EQUAL);
        } else {
            if (customerType > 1) {
                criteria.add(CustomerPeer.CUSTOMER_TYPE, new Integer(customerType), Criteria.EQUAL);
            }
            if (customerCatId > 999) {
                criteria.add(CustomerPeer.CUSTOMER_CAT_ID, new Integer(customerCatId), Criteria.EQUAL);
            }
            if (countryId > 999) {
                criteria.add(CustomerPeer.COUNTRY_ID, new Integer(countryId), Criteria.EQUAL);
            }
            if (regionId > 999) {
                criteria.add(CustomerPeer.REGION_ID, new Integer(regionId), Criteria.EQUAL);
            }
            if (languageId > 999) {
                criteria.add(CustomerPeer.LANGUAGE_ID, new Integer(languageId), Criteria.EQUAL);
            }
            if (industryId > 999) {
                criteria.add(CustomerPeer.INDUSTRY_ID, new Integer(industryId), Criteria.EQUAL);
            }
            if (leadSourceId > 999) {
                criteria.add(CustomerPeer.LEAD_SOURCE_ID, new Integer(leadSourceId), Criteria.EQUAL);
            }
        }
    }

    private void setProjectCriteria(RunData data, Criteria criteria) throws Exception {
        int projectId = data.getParameters().getInt("projectid", 999);
        int projectCatId = data.getParameters().getInt("projectcatid", 999);
        if (projectId > 999) {
            criteria.add(ProjectPeer.PROJECT_ID, new Integer(projectId), Criteria.EQUAL);
        } else {
            if (projectCatId > 999) {
                criteria.add(ProjectPeer.PROJECT_CAT_ID, new Integer(projectCatId), Criteria.EQUAL);
            }
        }
    }

    private void setProductCriteria(RunData data, Criteria criteria) throws Exception {
        int productId = data.getParameters().getInt("productid", 999);
        int productCatId = data.getParameters().getInt("productcatid", 999);
        int productType = data.getParameters().getInt("producttype", 1);
        int vendorId = data.getParameters().getInt("vendorid", 999);
        int uomId = data.getParameters().getInt("uomid", 999);
        if (productId > 999) {
            criteria.add(ProductPeer.PRODUCT_ID, new Integer(productId), Criteria.EQUAL);
        } else {
            if (productType > 1) {
                criteria.add(ProductPeer.PRODUCT_TYPE, new Integer(productType), Criteria.EQUAL);
            }
            if (productCatId > 999) {
                criteria.add(ProductPeer.PRODUCT_CAT_ID, new Integer(productCatId), Criteria.EQUAL);
            }
            if (vendorId > 999) {
                criteria.add(ProductPeer.VENDOR_ID, new Integer(vendorId), Criteria.EQUAL);
            }
            if (uomId > 999 || (uomId < 900 && uomId > 100)) {
                criteria.add(ProductPeer.UOM_ID, new Integer(uomId), Criteria.EQUAL);
            }
        }
    }

    /**
     * Implement this to add information to the context.
     *
     * @param data Turbine information.
     * @param context Context for web pages.
     * @exception Exception, a generic exception.
     */
    public void doPerform(RunData data, Context context) throws Exception {
        data.setMessage("Can't find the button!");
    }

    /**
     * This currently only checks to make sure that user is allowed to
     * view the storage area.  If you create an action that requires more
     * security then override this method.
     *
     * @param data Turbine information.
     * @return True if the user is authorized to access the screen.
     * @exception Exception, a generic exception.
     */
    protected boolean isAuthorized(RunData data) throws Exception {
        int reportid = data.getParameters().getInt("reportid", 0);
        boolean isAuthorized = false;
        AccessControlList acl = data.getACL();
        if (reportid == 1 && data.getUser().hasLoggedIn() && (acl.hasPermission("PAYMENT_VIEW") || acl.hasRole("turbine_root"))) {
            isAuthorized = true;
        } else if (reportid == 2 && data.getUser().hasLoggedIn() && (acl.hasPermission("PAYMENT_VIEW") || acl.hasRole("turbine_root"))) {
            isAuthorized = true;
        } else if (reportid == 3 && data.getUser().hasLoggedIn() && (acl.hasPermission("ONLINE_SUBSCRIPTION_VIEW") || acl.hasRole("turbine_root"))) {
            isAuthorized = true;
        } else if (reportid == 4 && data.getUser().hasLoggedIn() && (acl.hasPermission("PRINT_SUBSCRIPTION_VIEW") || acl.hasRole("turbine_root"))) {
            isAuthorized = true;
        } else if (reportid == 5 && data.getUser().hasLoggedIn() && (acl.hasPermission("PRINT_SUBSCRIPTION_VIEW") || acl.hasRole("turbine_root"))) {
            isAuthorized = true;
        } else if (reportid == 6 && data.getUser().hasLoggedIn() && (acl.hasPermission("SHIPMENT_VIEW") || acl.hasRole("turbine_root"))) {
            isAuthorized = true;
        } else if (reportid == 7 && data.getUser().hasLoggedIn() && (acl.hasPermission("NEWS_SUBSCRIPTION_VIEW") || acl.hasRole("turbine_root"))) {
            isAuthorized = true;
        } else {
            data.setMessage("Sorry, you don't have permission for this operation!");
            this.setTemplate(data, "CreamError.vm");
            isAuthorized = false;
        }
        return isAuthorized;
    }

    protected Date parseDateTime(String d) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        return formatter.parse(d, pos);
    }

    protected Date parseDate(String d) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        ParsePosition pos = new ParsePosition(0);
        try {
            Date myDate = formatter.parse(d, pos);
            return myDate;
        } catch (Exception e) {
            return null;
        }
    }
}
