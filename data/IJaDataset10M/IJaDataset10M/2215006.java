package net.sourceforge.interpay.gateway;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

/**
 * Process credit card payment, use DOM to build XML output.
 * 
 * @author Kong Wang
 * @see IPayment
 */
public class CreditCardPayment implements IPayment {

    private String mid, cardNum, expMonth, expYear, amount, type, cvv, refid, fname, lname, address1, city, state, zip, output, tid;

    private Document document = null;

    private String avsCode, cvvCode, statusCode;

    /**
     * constructor
     */
    public CreditCardPayment(String mid, String refid, String type, String cardNum, String expMonth, String expYear, String cvv, String amount, String fname, String lname, String address1, String city, String state, String zip) {
        this.mid = mid;
        this.cardNum = cardNum;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.amount = amount;
        this.type = type;
        this.cvv = cvv;
        this.refid = refid;
        this.fname = fname;
        this.lname = lname;
        this.address1 = address1;
        this.city = city;
        this.state = state;
        this.zip = zip;
        Random myRandom = new Random();
        tid = String.valueOf(Math.abs(myRandom.nextLong()));
        if (type.equals("A")) {
            if (cardNum.equals("4444333322221111")) {
                statusCode = "0";
            } else {
                statusCode = "1";
            }
            if (zip.equals("11361")) {
                avsCode = "Y";
            } else {
                avsCode = "N";
            }
            if (cvv.equals("123")) {
                cvvCode = "Y";
            } else {
                cvvCode = "N";
            }
            buildOutput(1);
        } else {
            if (refid == "87654321") {
                statusCode = "0";
            } else {
                statusCode = "4";
            }
            buildOutput(2);
        }
    }

    public String getResult() {
        return output;
    }

    public void buildOutput(int flag) {
        DocumentBuilder builder = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
            document = builder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        if (flag == 1) buildAuthorizationXML(); else buildSettlementXML();
        try {
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(document);
            trans.transform(source, result);
            output = sw.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * build DOM tree using JAXP
     */
    private void buildAuthorizationXML() {
        Element root = document.createElement("response");
        document.appendChild(root);
        insertNode(root, "mid", mid);
        insertNode(root, "statusCode", statusCode);
        insertNode(root, "tid", tid);
        insertNode(root, "avsCode", avsCode);
        insertNode(root, "cvvCode", cvvCode);
        insertNode(root, "type", type);
        insertNode(root, "refid", refid);
        Element account = document.createElement("account");
        root.appendChild(account);
        insertNode(account, "cardNum", cardNum);
        Element expDate = document.createElement("expDate");
        account.appendChild(expDate);
        insertNode(expDate, "expMonth", expMonth);
        insertNode(expDate, "expYear", expYear);
        insertNode(account, "amount", amount);
        insertNode(account, "cvv2", cvv);
        Element customer = document.createElement("customer");
        account.appendChild(customer);
        Element name = document.createElement("name");
        customer.appendChild(name);
        insertNode(name, "fname", fname);
        insertNode(name, "lname", lname);
        Element address = document.createElement("address");
        customer.appendChild(address);
        insertNode(address, "address1", address1);
        insertNode(address, "city", city);
        insertNode(address, "state", state);
        insertNode(address, "zip", zip);
    }

    /**
     * @param element
     */
    private void insertNode(Element element, String nodeName, String nodeValue) {
        Node item, value;
        item = document.createElement(nodeName);
        value = document.createTextNode(nodeValue);
        item.appendChild(value);
        element.appendChild(item);
    }

    /**
     * build DOM tree using JAXP
     */
    private void buildSettlementXML() {
        Element root = document.createElement("response");
        document.appendChild(root);
        insertNode(root, "mid", mid);
        insertNode(root, "statusCode", statusCode);
        insertNode(root, "tid", tid);
        insertNode(root, "type", type);
        insertNode(root, "refid", refid);
        insertNode(root, "amount", amount);
    }
}
