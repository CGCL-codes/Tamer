package org.ofbiz.product.product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javolution.util.FastMap;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.base.util.UtilXml;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.ServiceUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ProductsExportToEbay {

    private static final String resource = "ProductUiLabels";

    private static final String module = ProductsExportToEbay.class.getName();

    public static Map exportToEbay(DispatchContext dctx, Map context) {
        Locale locale = (Locale) context.get("locale");
        try {
            String configString = "productsExport.properties";
            String devID = UtilProperties.getPropertyValue(configString, "productsExport.eBay.devID");
            String appID = UtilProperties.getPropertyValue(configString, "productsExport.eBay.appID");
            String certID = UtilProperties.getPropertyValue(configString, "productsExport.eBay.certID");
            String token = UtilProperties.getPropertyValue(configString, "productsExport.eBay.token");
            String compatibilityLevel = UtilProperties.getPropertyValue(configString, "productsExport.eBay.compatibilityLevel");
            String siteID = UtilProperties.getPropertyValue(configString, "productsExport.eBay.siteID");
            String xmlGatewayUri = UtilProperties.getPropertyValue(configString, "productsExport.eBay.xmlGatewayUri");
            StringBuffer dataItemsXml = new StringBuffer();
            if (!ServiceUtil.isFailure(buildDataItemsXml(dctx, context, dataItemsXml, token))) {
                Map result = postItem(xmlGatewayUri, dataItemsXml, devID, appID, certID, "AddItem", compatibilityLevel, siteID);
                if (ServiceUtil.isFailure(result)) {
                    return ServiceUtil.returnFailure(ServiceUtil.getErrorMessage(result));
                }
            }
        } catch (Exception e) {
            Debug.logError("Exception in exportToEbay " + e, module);
            return ServiceUtil.returnFailure(UtilProperties.getMessage(resource, "productsExportToEbay.exceptionInExportToEbay", locale));
        }
        return ServiceUtil.returnSuccess(UtilProperties.getMessage(resource, "productsExportToEbay.productItemsSentCorrecltyToEbay", locale));
    }

    private static void appendRequesterCredentials(Element elem, Document doc, String token) {
        Element requesterCredentialsElem = UtilXml.addChildElement(elem, "RequesterCredentials", doc);
        UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", token, doc);
    }

    private static String toString(InputStream inputStream) throws IOException {
        String string;
        StringBuilder outputBuilder = new StringBuilder();
        if (inputStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while (null != (string = reader.readLine())) {
                outputBuilder.append(string).append('\n');
            }
        }
        return outputBuilder.toString();
    }

    private static Map postItem(String postItemsUrl, StringBuffer dataItems, String devID, String appID, String certID, String callName, String compatibilityLevel, String siteID) throws IOException {
        if (Debug.verboseOn()) {
            Debug.logVerbose("Request of " + callName + " To eBay:\n" + dataItems.toString(), module);
        }
        HttpURLConnection connection = (HttpURLConnection) (new URL(postItemsUrl)).openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("X-EBAY-API-COMPATIBILITY-LEVEL", compatibilityLevel);
        connection.setRequestProperty("X-EBAY-API-DEV-NAME", devID);
        connection.setRequestProperty("X-EBAY-API-APP-NAME", appID);
        connection.setRequestProperty("X-EBAY-API-CERT-NAME", certID);
        connection.setRequestProperty("X-EBAY-API-CALL-NAME", callName);
        connection.setRequestProperty("X-EBAY-API-SITEID", siteID);
        connection.setRequestProperty("Content-Type", "text/xml");
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(dataItems.toString().getBytes());
        outputStream.close();
        int responseCode = connection.getResponseCode();
        InputStream inputStream;
        Map result = FastMap.newInstance();
        String response = null;
        if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
            inputStream = connection.getInputStream();
            response = toString(inputStream);
            result = ServiceUtil.returnSuccess(response);
        } else {
            inputStream = connection.getErrorStream();
            response = toString(inputStream);
            result = ServiceUtil.returnFailure(response);
        }
        if (Debug.verboseOn()) {
            Debug.logVerbose("Response of " + callName + " From eBay:\n" + response, module);
        }
        return result;
    }

    private static Map buildDataItemsXml(DispatchContext dctx, Map context, StringBuffer dataItemsXml, String token) {
        Locale locale = (Locale) context.get("locale");
        try {
            GenericDelegator delegator = dctx.getDelegator();
            List selectResult = (List) context.get("selectResult");
            List productsList = delegator.findByCondition("Product", new EntityExpr("productId", EntityOperator.IN, selectResult), null, null);
            try {
                Document itemDocument = UtilXml.makeEmptyXmlDocument("AddItemRequest");
                Element itemRequestElem = itemDocument.getDocumentElement();
                itemRequestElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                appendRequesterCredentials(itemRequestElem, itemDocument, token);
                Iterator productsListItr = productsList.iterator();
                while (productsListItr.hasNext()) {
                    GenericValue prod = (GenericValue) productsListItr.next();
                    String title = parseText(prod.getString("internalName"));
                    String description = parseText(prod.getString("internalName"));
                    Element itemElem = UtilXml.addChildElement(itemRequestElem, "Item", itemDocument);
                    UtilXml.addChildElementValue(itemElem, "Country", (String) context.get("country"), itemDocument);
                    UtilXml.addChildElementValue(itemElem, "Location", (String) context.get("location"), itemDocument);
                    UtilXml.addChildElementValue(itemElem, "Currency", "USD", itemDocument);
                    UtilXml.addChildElementValue(itemElem, "SKU", prod.getString("productId"), itemDocument);
                    UtilXml.addChildElementValue(itemElem, "Title", title, itemDocument);
                    UtilXml.addChildElementValue(itemElem, "Description", description, itemDocument);
                    UtilXml.addChildElementValue(itemElem, "ListingDuration", (String) context.get("listingDuration"), itemDocument);
                    UtilXml.addChildElementValue(itemElem, "Quantity", (String) context.get("quantity"), itemDocument);
                    setPaymentMethodAccepted(itemDocument, itemElem, context);
                    Element primaryCatElem = UtilXml.addChildElement(itemElem, "PrimaryCategory", itemDocument);
                    UtilXml.addChildElementValue(primaryCatElem, "CategoryID", (String) context.get("ebayCategory"), itemDocument);
                    Element startPriceElem = UtilXml.addChildElementValue(itemElem, "StartPrice", (String) context.get("startPrice"), itemDocument);
                    startPriceElem.setAttribute("currencyID", "USD");
                }
                dataItemsXml.append(UtilXml.writeXmlDocument(itemDocument));
            } catch (Exception e) {
                Debug.logError("Exception during building data items to eBay", module);
                return ServiceUtil.returnFailure(UtilProperties.getMessage(resource, "productsExportToEbay.exceptionDuringBuildingDataItemsToEbay", locale));
            }
        } catch (Exception e) {
            Debug.logError("Exception during building data items to eBay", module);
            return ServiceUtil.returnFailure(UtilProperties.getMessage(resource, "productsExportToEbay.exceptionDuringBuildingDataItemsToEbay", locale));
        }
        return ServiceUtil.returnSuccess();
    }

    private static Map buildCategoriesXml(Map context, StringBuffer dataItemsXml, String token) {
        Locale locale = (Locale) context.get("locale");
        try {
            Document itemRequest = UtilXml.makeEmptyXmlDocument("GetCategoriesRequest");
            Element itemRequestElem = itemRequest.getDocumentElement();
            itemRequestElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
            appendRequesterCredentials(itemRequestElem, itemRequest, token);
            UtilXml.addChildElementValue(itemRequestElem, "DetailLevel", "ReturnAll", itemRequest);
            UtilXml.addChildElementValue(itemRequestElem, "CategorySiteID", "0", itemRequest);
            UtilXml.addChildElementValue(itemRequestElem, "LevelLimit", "2", itemRequest);
            UtilXml.addChildElementValue(itemRequestElem, "ViewAllNodes", "false", itemRequest);
            dataItemsXml.append(UtilXml.writeXmlDocument(itemRequest));
        } catch (Exception e) {
            Debug.logError("Exception during building data items to eBay", module);
            return ServiceUtil.returnFailure(UtilProperties.getMessage(resource, "productsExportToEbay.exceptionDuringBuildingDataItemsToEbay", locale));
        }
        return ServiceUtil.returnSuccess();
    }

    private static Map buildAddTransactionConfirmationItemRequest(Map context, StringBuffer dataItemsXml, String token, String itemId) {
        Locale locale = (Locale) context.get("locale");
        try {
            Document transDoc = UtilXml.makeEmptyXmlDocument("AddTransactionConfirmationItemRequest");
            Element transElem = transDoc.getDocumentElement();
            transElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
            appendRequesterCredentials(transElem, transDoc, token);
            UtilXml.addChildElementValue(transElem, "ItemID", itemId, transDoc);
            UtilXml.addChildElementValue(transElem, "ListingDuration", "Days_1", transDoc);
            Element negotiatePriceElem = UtilXml.addChildElementValue(transElem, "NegotiatedPrice", "50.00", transDoc);
            negotiatePriceElem.setAttribute("currencyID", "USD");
            UtilXml.addChildElementValue(transElem, "RecipientRelationType", "1", transDoc);
            UtilXml.addChildElementValue(transElem, "RecipientUserID", "buyer_anytime", transDoc);
            dataItemsXml.append(UtilXml.writeXmlDocument(transDoc));
            Debug.logInfo(dataItemsXml.toString(), module);
        } catch (Exception e) {
            Debug.logError("Exception during building AddTransactionConfirmationItemRequest eBay", module);
            return ServiceUtil.returnFailure(UtilProperties.getMessage(resource, "productsExportToEbay.exceptionDuringBuildingAddTransactionConfirmationItemRequestToEbay", locale));
        }
        return ServiceUtil.returnSuccess();
    }

    private static void setPaymentMethodAccepted(Document itemDocument, Element itemElem, Map context) {
        String payPal = (String) context.get("paymentPayPal");
        String visaMC = (String) context.get("paymentVisaMC");
        String amEx = (String) context.get("paymentAmEx");
        String discover = (String) context.get("paymentDiscover");
        String ccAccepted = (String) context.get("paymentCCAccepted");
        String cashInPerson = (String) context.get("paymentCashInPerson");
        String cashOnPickup = (String) context.get("paymentCashOnPickup");
        String cod = (String) context.get("paymentCOD");
        String codPrePayDelivery = (String) context.get("paymentCODPrePayDelivery");
        String mocc = (String) context.get("paymentMOCC");
        String moneyXferAccepted = (String) context.get("paymentMoneyXferAccepted");
        String personalCheck = (String) context.get("paymentPersonalCheck");
        if (UtilValidate.isNotEmpty(payPal) && "Y".equals(payPal)) {
            UtilXml.addChildElementValue(itemElem, "PaymentMethods", "PayPal", itemDocument);
            UtilXml.addChildElementValue(itemElem, "PayPalEmailAddress", (String) context.get("payPalEmail"), itemDocument);
        }
        if (UtilValidate.isNotEmpty(visaMC) && "Y".equals(visaMC)) {
            UtilXml.addChildElementValue(itemElem, "PaymentMethods", "VisaMC", itemDocument);
        }
        if (UtilValidate.isNotEmpty(amEx) && "Y".equals(amEx)) {
            UtilXml.addChildElementValue(itemElem, "PaymentMethods", "AmEx", itemDocument);
        }
        if (UtilValidate.isNotEmpty(discover) && "Y".equals(discover)) {
            UtilXml.addChildElementValue(itemElem, "PaymentMethods", "Discover", itemDocument);
        }
        if (UtilValidate.isNotEmpty(ccAccepted) && "Y".equals(ccAccepted)) {
            UtilXml.addChildElementValue(itemElem, "PaymentMethods", "CCAccepted", itemDocument);
        }
        if (UtilValidate.isNotEmpty(cashInPerson) && "Y".equals(cashInPerson)) {
            UtilXml.addChildElementValue(itemElem, "PaymentMethods", "CashInPerson", itemDocument);
        }
        if (UtilValidate.isNotEmpty(cashOnPickup) && "Y".equals(cashOnPickup)) {
            UtilXml.addChildElementValue(itemElem, "PaymentMethods", "CashOnPickup", itemDocument);
        }
        if (UtilValidate.isNotEmpty(cod) && "Y".equals(cod)) {
            UtilXml.addChildElementValue(itemElem, "PaymentMethods", "COD", itemDocument);
        }
        if (UtilValidate.isNotEmpty(codPrePayDelivery) && "Y".equals(codPrePayDelivery)) {
            UtilXml.addChildElementValue(itemElem, "PaymentMethods", "CODPrePayDelivery", itemDocument);
        }
        if (UtilValidate.isNotEmpty(mocc) && "Y".equals(mocc)) {
            UtilXml.addChildElementValue(itemElem, "PaymentMethods", "MOCC", itemDocument);
        }
        if (UtilValidate.isNotEmpty(moneyXferAccepted) && "Y".equals(moneyXferAccepted)) {
            UtilXml.addChildElementValue(itemElem, "PaymentMethods", "MoneyXferAccepted", itemDocument);
        }
        if (UtilValidate.isNotEmpty(personalCheck) && "Y".equals(personalCheck)) {
            UtilXml.addChildElementValue(itemElem, "PaymentMethods", "PersonalCheck", itemDocument);
        }
    }

    private static String parseText(String text) {
        Pattern htmlPattern = Pattern.compile("[<](.+?)[>]");
        Pattern tabPattern = Pattern.compile("\\s");
        if (null != text && text.length() > 0) {
            Matcher matcher = htmlPattern.matcher(text);
            text = matcher.replaceAll("");
            matcher = tabPattern.matcher(text);
            text = matcher.replaceAll(" ");
        } else {
            text = "";
        }
        return text;
    }
}
