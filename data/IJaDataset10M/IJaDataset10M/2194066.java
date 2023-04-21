package org.openkonnect.accessor.openbravo;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openbravo.base.HttpBaseServlet;
import org.openbravo.erpCommon.utility.Utility;
import org.openbravo.erpWindows.BusinessPartner.BusinessPartnerData;
import org.openbravo.erpWindows.DocumentSequence.SequenceData;

public class ESBMessageListener extends HttpBaseServlet {

    /**
   * 
   */
    private static final long serialVersionUID = -6601558595331603943L;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        OpenKonnectSession openKonnectSession = new OpenKonnectSession("openkonnectUser", "OpenKonnect", "OpenKontor");
        log4j.info("caught request");
        CharBuffer target = CharBuffer.allocate(request.getContentLength());
        request.getReader().read(target);
        Map<String, Map<String, Object>> eventCustomer = getCustomerFromXml(target.toString());
        createCustomer(eventCustomer, openKonnectSession);
    }

    public void createCustomer(Map<String, Map<String, Object>> eventCustomer, OpenKonnectSession okSession) throws ServletException {
        BusinessPartnerData[] bpNameArray = null;
        BusinessPartnerData bpartner = getBusinessPartnerDataFromCustomer(eventCustomer, okSession);
        int total = 0;
        try {
            bpNameArray = BusinessPartnerData.select(this, bpartner.adLanguage, bpartner.name, "", "", "", "", "", bpartner.adClientId, bpartner.adOrgId, "name", bpartner.value, "value", 1);
            total = bpNameArray.length;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
        if (0 < total) {
            throw new ServletException(" record already exists.");
        } else {
            try {
                boolean updateNext = true;
                String docno = Utility.getDocumentNo(this, bpartner.adClientId, "C_BPartner", updateNext);
                bpartner.cBpartnerId = getCurrentnextFromAdSequence("C_BPartner", okSession, bpartner);
                if (!docno.equalsIgnoreCase(bpartner.cBpartnerId)) {
                    System.out.println("C_BPartner: " + docno + " ne " + bpartner.cBpartnerId);
                    bpartner.cBpartnerId = docno;
                }
                bpartner.insert(this);
                log4j.warn(bpartner.cBpartnerId + " inserted ");
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServletException(e);
            }
        }
        System.out.println("create Customer done.");
    }

    private BusinessPartnerData getBusinessPartnerDataFromCustomer(Map<String, Map<String, Object>> eventCustomer, OpenKonnectSession okSession) {
        Map<String, Object> customer = (Map<String, Object>) eventCustomer.get("customer");
        BusinessPartnerData bpartner = new BusinessPartnerData();
        bpartner.adClientId = okSession.getAdClientId();
        bpartner.adOrgId = okSession.getAdOrgId();
        bpartner.adLanguage = "de_DE";
        bpartner.isactive = "Y";
        bpartner.createdby = okSession.getAdUserId();
        bpartner.updatedby = okSession.getAdUserId();
        bpartner.numberemployees = (String) customer.get("numEmployees");
        bpartner.value = (String) customer.get("number");
        bpartner.cBpartnerId = "0";
        bpartner.name = (String) customer.get("name");
        bpartner.name2 = "Added by OpenKonnect";
        bpartner.issummary = "N";
        bpartner.cBpGroupId = okSession.getAdGroupId();
        bpartner.isonetime = "N";
        bpartner.isprospect = "Y";
        String partnerType = (String) customer.get("partnerType");
        bpartner.isvendor = ("Vendor".equalsIgnoreCase(partnerType)) ? "Y" : "N";
        bpartner.iscustomer = ("Customer".equalsIgnoreCase(partnerType)) ? "Y" : "N";
        bpartner.istaxexempt = "N";
        bpartner.socreditstatus = "0";
        bpartner.soCreditused = "0";
        bpartner.showpriceinorder = "Y";
        bpartner.description = (String) customer.get("description");
        bpartner.url = (String) customer.get("websiteURL");
        return bpartner;
    }

    private String getCurrentnextFromAdSequence(String tableName, OpenKonnectSession okSession, BusinessPartnerData bpartner) throws Exception {
        SequenceData[] seqData = null;
        String nextEntry = null;
        String entryName = "DocumentNo_" + tableName;
        int total = 0;
        try {
            seqData = SequenceData.select(this, okSession.getLanguage(), entryName, "", okSession.getAdUserId(), okSession.getAdClientId(), okSession.getAdOrgId(), "name", entryName, "name", 1);
            total = seqData.length;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        if (0 < total) {
            nextEntry = seqData[0].startno;
            for (int iSeq = 0; iSeq < seqData.length - 1; iSeq++) {
                if (seqData[iSeq].adClientId.equalsIgnoreCase(bpartner.adClientId)) nextEntry = seqData[iSeq].currentnext;
            }
        }
        if (null == nextEntry) nextEntry = "1000000"; else {
            nextEntry = Integer.toString(Integer.parseInt(nextEntry) + 1);
        }
        return nextEntry;
    }

    private Map<String, Map<String, Object>> getCustomerFromXml(String source) {
        Map<String, Map<String, Object>> eventCustomer = new HashMap<String, Map<String, Object>>();
        eventCustomer.put("event", new HashMap<String, Object>());
        eventCustomer.put("customer", new HashMap<String, Object>());
        return eventCustomer;
    }
}
