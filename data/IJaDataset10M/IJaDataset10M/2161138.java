package com.medcentrex.bridge.messaging;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.ejb.EJBException;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import java.io.Serializable;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import com.medcentrex.bridge.entity.Invoice_Status_TypeEntityBean;
import com.medcentrex.bridge.interfaces.*;
import com.medcentrex.bridge.messaging.util.BridgeTools;
import com.medcentrex.bridge.messaging.util.JNDITools;
import com.medcentrex.bridge.messaging.util.XMLTools;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import javax.jms.TextMessage;
import javax.jms.ObjectMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.InputSource;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import javax.xml.rpc.ParameterMode;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Properties;
import java.io.*;

public class SendClaimsQueueListener implements MessageDrivenBean, MessageListener {

    private MessageDrivenContext ctx = null;

    public SendClaimsQueueListener() {
    }

    private Logger getLogger() {
        return Logger.getLogger(getClass());
    }

    public void setMessageDrivenContext(MessageDrivenContext ctx) throws EJBException {
        this.ctx = ctx;
    }

    public void ejbCreate() {
    }

    public void ejbRemove() {
        ctx = null;
    }

    public void onMessage(Message msg) {
        try {
            this.ctx.getUserTransaction().begin();
            X12_InterchangeControlNumberEntity x12E = getNextInterchangeNumber();
            X12_InterchangeControlNumberEntityData x12 = x12E.getValueObject();
            Document doc = getClaimsXML(x12.getID());
            if (doc != null) {
                String response = sendClaims(doc);
                x12.setData(response);
                x12E.updateValueObject(x12);
                response = sendEDI(response);
            } else {
                try {
                    this.ctx.getUserTransaction().rollback();
                } catch (Exception e2) {
                    getLogger().error(e2);
                }
                getLogger().warn("No Claims to send");
            }
            this.ctx.getUserTransaction().commit();
        } catch (Exception e) {
            getLogger().error(e);
            try {
                this.ctx.getUserTransaction().rollback();
            } catch (Exception e2) {
                getLogger().error(e2);
            }
        }
    }

    /**
     * Sends edi text to the translation engine, which sends to clearinghouse or payer.
     * validates response.
     * 
     * @param edi
     * @throws NamingException
     * @throws JMSException
     * @throws Exception
     */
    protected String sendEDI(String edi) throws NamingException, JMSException, Exception {
        getLogger().info("Sending edi to translation engine");
        String response = BridgeTools.instance().bieWebService("WebMD_Batch", edi);
        getLogger().info("got response from translation engine");
        if (!response.endsWith(",OK")) {
            throw new Exception("Response was invalid or file did not process correctly:" + response);
        }
        return response;
    }

    /**
     * Send claims to the translation engine and get expected edi document
     * 
     * @param doc - xml doc of all claims getting sent to clearing house
     * @return - the EDI file in string format
     * @throws Exception if any error occurs
     */
    protected String sendClaims(Document doc) throws Exception {
        getLogger().info("Sending claims to translation engine");
        String response = BridgeTools.instance().bieWebService("CrossApps837to837", XMLTools.instance().serializeDoc(doc));
        getLogger().info("got response from translation engine");
        if (!response.startsWith("ISA")) {
            throw new Exception("Response was invalid or file did not process correctly:" + response);
        }
        return response;
    }

    private Context getInitialContext() throws Exception {
        return JNDITools.instance().getContext();
    }

    private DataSource getDataSource() throws Exception {
        return BridgeTools.instance().getDataSource();
    }

    protected Document getClaimsXML(Integer ICN) throws Exception {
        getLogger().info("Beginning search for claims");
        Context context = getInitialContext();
        DataSource ds = getDataSource();
        Connection cn = null;
        try {
            cn = ds.getConnection();
            Statement cmd = cn.createStatement();
            cmd.setFetchSize(250);
            ResultSet rs;
            String sql = "select claim_block.claim_block_id AS `claim_block claim_block_id`, " + "claim_block.customer_id AS `claim_block customer_id`, " + "claim_block.charge_ticket_id AS `claim_block charge_ticket_id`, " + "claim_block.claim_type_id AS `claim_block claim_type_id`, " + "claim_block.patient_id AS `claim_block patient_id`, " + "claim_block.upload_file_id AS `claim_block upload_file_id`, " + "claim_block.create_date AS `claim_block create_date`, " + "claim_block.date_of_illness AS `claim_block date_of_illness`, " + "claim_block.first_date_of_illness AS `claim_block first_date_of_illness`, " + "claim_block.discharge_date AS `claim_block discharge_date`, " + "claim_block.admission_date AS `claim_block admission_date`, " + "claim_block.disability_from_date AS `claim_block disability_from_date`, " + "claim_block.disability_to_date AS `claim_block disability_to_date`, " + "claim_block.disability AS `claim_block disability`, " + "claim_block.accept_assignment AS `claim_block accept_assignment`, " + "claim_block.last_visit_date AS `claim_block last_visit_date`, " + "claim_block.service_date AS `claim_block service_date`, " + "claim_block.accident_related_cause AS `claim_block accident_related_cause`, " + "claim_block.accident_state AS `claim_block accident_state`, " + "claim_block.epsdt AS `claim_block epsdt`, " + "claim_block.place_of_service AS `claim_block place_of_service`, " + "claim_block.patient_lastname AS `claim_block patient_lastname`, " + "claim_block.patient_firstname AS `claim_block patient_firstname`, " + "claim_block.patient_mi AS `claim_block patient_mi`, " + "claim_block.patient_prefix AS `claim_block patient_prefix`, " + "claim_block.patient_suffix AS `claim_block patient_suffix`, " + "claim_block.patient_address1 AS `claim_block patient_address1`, " + "claim_block.patient_address2 AS `claim_block patient_address2`, " + "claim_block.patient_city AS `claim_block patient_city`, " + "claim_block.patient_state AS `claim_block patient_state`, " + "claim_block.patient_zip AS `claim_block patient_zip`, " + "claim_block.patient_country AS `claim_block patient_country`, " + "claim_block.patient_ssn AS `claim_block patient_ssn`, " + "claim_block.patient_dob AS `claim_block patient_dob`, " + "claim_block.patient_birthplace AS `claim_block patient_birthplace`, " + "patient_gender.gender AS `claim_block patient_gender`, " + "claim_block.patient_employment_status AS `claim_block patient_employment_status`, " + "claim_block.patient_marital_status AS `claim_block patient_marital_status`, " + "claim_block.patient_employer_name AS `claim_block patient_employer_name`, " + "claim_block.patient_phone AS `claim_block patient_phone`, " + "claim_block.patient_mrn AS `claim_block patient_mrn`, " + "claim_block.ext_claim_id AS `claim_block ext_claim_id`, " + "claim_block.outside_lab AS `claim_block outside_lab`, " + "claim_block.lab_charges AS `claim_block lab_charges`, " + "claim_block.prior_auth AS `claim_block prior_auth`, " + "claim_block.patient_policy_no AS `claim_block patient_policy_no`, " + "claim_block.patient_signature AS `claim_block patient_signature`, " + "claim_block.patient_signature_date AS `claim_block patient_signature_date`, " + "claim_block.physician_signature AS `claim_block physician_signature`, " + "claim_block.physician_signature_date AS `claim_block physician_signature_date`, " + "claim_block.date_of_menstruation AS `claim_block date_of_menstruation`, " + "claim_block.date_of_accident AS `claim_block date_of_accident`, " + "claim_block.icd1 AS `claim_block icd1`, " + "claim_block.icd2 AS `claim_block icd2`, " + "claim_block.icd3 AS `claim_block icd3`, " + "claim_block.icd4 AS `claim_block icd4`, " + "claim_block.icd5 AS `claim_block icd5`, " + "claim_block.icd6 AS `claim_block icd6`, " + "claim_block.icd7 AS `claim_block icd7`, " + "claim_block.icd8 AS `claim_block icd8`, " + "claim_v2.orig_ref_no AS `claim_v2 orig_ref_no`, " + "claim_v2.resubmission_code AS `claim_v2 resubmission_code`, " + "claim_v2.claim_id AS `claim_v2 claim_id`, " + "claim_v2.claim_block_id AS `claim_v2 claim_block_id`, " + "claim_v2.customer_id AS `claim_v2 customer_id`, " + "claim_v2.priority AS `claim_v2 priority`, " + "claim_v2.invoice_status_type_id AS `claim_v2 invoice_status_type_id`, " + "claim_v2.submitted AS `claim_v2 submitted`, " + "claim_v2.last_print_date AS `claim_v2 last_print_date`, " + "claim_v2.last_submit_date AS `claim_v2 last_submit_date`, " + "claim_v2.guarantor_signature AS `claim_v2 guarantor_signature`, " + "claim_insurance.claim_insurance_id AS `claim_insurance claim_insurance_id`, " + "claim_insurance.claim_id AS `claim_insurance claim_id`, " + "claim_insurance.guarantor_insurance_id AS `claim_insurance guarantor_insurance_id`, " + "claim_insurance.priority AS `claim_insurance priority`, " + "claim_insurance.guarantor_type_id AS `claim_insurance guarantor_type_id`, " + "claim_insurance.guarantor_lastname AS `claim_insurance guarantor_lastname`, " + "claim_insurance.guarantor_firstname AS `claim_insurance guarantor_firstname`, " + "claim_insurance.guarantor_mi AS `claim_insurance guarantor_mi`, " + "claim_insurance.guarantor_address1 AS `claim_insurance guarantor_address1`, " + "claim_insurance.guarantor_address2 AS `claim_insurance guarantor_address2`, " + "claim_insurance.guarantor_city AS `claim_insurance guarantor_city`, " + "claim_insurance.guarantor_state AS `claim_insurance guarantor_state`, " + "claim_insurance.guarantor_zip AS `claim_insurance guarantor_zip`, " + "claim_insurance.guarantor_country AS `claim_insurance guarantor_country`, " + "claim_insurance.relationship_type_id AS `claim_insurance relationship_type_id`, " + "claim_insurance.guarantor_dob AS `claim_insurance guarantor_dob`, " + "guarantor_gender.gender AS `claim_insurance guarantor_gender`, " + "claim_insurance.guarantor_phone AS `claim_insurance guarantor_phone`, " + "claim_insurance.policy_no AS `claim_insurance policy_no`, " + "claim_insurance.feca_no AS `claim_insurance feca_no`, " + "claim_insurance.employer_name AS `claim_insurance employer_name`, " + "claim_insurance.eclaims_payer_id AS `claim_insurance eclaims_payer_id`, " + "claim_insurance.payer_name AS `claim_insurance payer_name`, " + "claim_insurance.payer_address1 AS `claim_insurance payer_address1`, " + "claim_insurance.payer_address2 AS `claim_insurance payer_address2`, " + "claim_insurance.payer_city AS `claim_insurance payer_city`, " + "claim_insurance.payer_state AS `claim_insurance payer_state`, " + "claim_insurance.payer_zip AS `claim_insurance payer_zip`, " + "claim_insurance.payer_country AS `claim_insurance payer_country`, " + "claim_insurance.plan_name AS `claim_insurance plan_name`, " + "claim_insurance.plan_address1 AS `claim_insurance plan_address1`, " + "claim_insurance.plan_address2 AS `claim_insurance plan_address2`, " + "claim_insurance.plan_city AS `claim_insurance plan_city`, " + "claim_insurance.plan_state AS `claim_insurance plan_state`, " + "claim_insurance.plan_zip AS `claim_insurance plan_zip`, " + "claim_insurance.plan_country AS `claim_insurance plan_country`, " + "claim_insurance.insurance_type_id AS `claim_insurance insurance_type_id`, " + "claim_insurance.plan_type_id AS `claim_insurance plan_type_id`, " + "claim_insurance.insurance_company_plan_id AS `claim_insurance insurance_company_plan_id`, " + "claim_insurance.place_id AS `claim_insurance place_id`, " + "claim_item.claim_item_id AS `claim_item claim_item_id`, " + "claim_item.claim_id AS `claim_item claim_id`, " + "claim_item.line_no AS `claim_item line_no`, " + "claim_item.service_date_to AS `claim_item service_date_to`, " + "claim_item.service_date_from AS `claim_item service_date_from`, " + "claim_item.place_of_service AS `claim_item place_of_service`, " + "claim_item.type_of_service AS `claim_item type_of_service`, " + "claim_item.procedure_code AS `claim_item procedure_code`, " + "claim_item.mod1 AS `claim_item mod1`, " + "claim_item.mod2 AS `claim_item mod2`, " + "claim_item.mod3 AS `claim_item mod3`, " + "claim_item.icd1 AS `claim_item icd1`, " + "claim_item.icd2 AS `claim_item icd2`, " + "claim_item.icd3 AS `claim_item icd3`, " + "claim_item.icd4 AS `claim_item icd4`, " + "claim_item.charge AS `claim_item charge`, " + "claim_item.charge_unit AS `claim_item charge_unit`, " + "claim_item.charge_unit_type_id AS `claim_item charge_unit_type_id`, " + "claim_item.allowed AS `claim_item allowed`, " + "claim_item.payer_paid AS `claim_item payer_paid`, " + "claim_item.patient_responsible AS `claim_item patient_responsible`, " + "claim_item.discount AS `claim_item discount`, " + "claim_item.writeoff AS `claim_item writeoff`, " + "claim_item.adjustment_code AS `claim_item adjustment_code`, " + "claim_item.extra_info AS `claim_item extra_info`, " + "claim_item.epsdt AS `claim_item epsdt`, " + "claim_item.emg AS `claim_item emg`, " + "claim_item.cob AS `claim_item cob`, " + "claim_item.family_plan AS `claim_item family_plan`, " + "claim_location.claim_location_id AS `claim_location claim_location_id`, " + "claim_location.claim_block_id AS `claim_location claim_block_id`, " + "claim_location.customer_id AS `claim_location customer_id`, " + "claim_location.claim_item_id AS `claim_location claim_item_id`, " + "claim_location.location_name AS `claim_location location_name`, " + "claim_location.location_address1 AS `claim_location location_address1`, " + "claim_location.location_address2 AS `claim_location location_address2`, " + "claim_location.location_city AS `claim_location location_city`, " + "claim_location.location_state AS `claim_location location_state`, " + "claim_location.location_zip AS `claim_location location_zip`, " + "claim_location.location_country AS `claim_location location_country`, " + "claim_location_identifier.claim_location_identifier_id AS `claim_location_identifier claim_location_identifier_id`, " + "claim_location_identifier.claim_location_id AS `claim_location_identifier claim_location_id`, " + "claim_location_identifier.identifier_code AS `claim_location_identifier identifier_code`, " + "claim_location_identifier.identifier AS `claim_location_identifier identifier`, " + "claim_physician.claim_physician_id AS `claim_physician claim_physician_id`, " + "claim_physician.claim_block_id AS `claim_physician claim_block_id`, " + "claim_physician.customer_id AS `claim_physician customer_id`, " + "claim_physician.claim_item_id AS `claim_physician claim_item_id`, " + "claim_physician.physician_lastname AS `claim_physician physician_lastname`, " + "claim_physician.physician_firstname AS `claim_physician physician_firstname`, " + "claim_physician.physician_mi AS `claim_physician physician_mi`, " + "claim_physician.physician_address1 AS `claim_physician physician_address1`, " + "claim_physician.physician_address2 AS `claim_physician physician_address2`, " + "claim_physician.physician_city AS `claim_physician physician_city`, " + "claim_physician.physician_state AS `claim_physician physician_state`, " + "claim_physician.physician_zip AS `claim_physician physician_zip`, " + "claim_physician.physician_country AS `claim_physician physician_country`, " + "claim_physician.physician_class_id AS `claim_physician physician_class_id`, " + "claim_physician.entity_type_id AS `claim_physician entity_type_id`, claim_physician.tax_id AS `claim_physician tax_id`, claim_physician.pinorgrp AS `claim_physician pinorgrp`, claim_physician_identifier.claim_physician_identifer_id AS `claim_physician_identifier claim_physician_identifer_id`, claim_physician_identifier.claim_physician_id AS `claim_physician_identifier claim_physician_id`, claim_physician_identifier.identifier_code AS `claim_physician_identifier identifier_code`, claim_physician_identifier.identifier AS `claim_physician_identifier identifier`, claim_insurance_identifier.claim_insurance_identifier_id AS `claim_insurance_identifier claim_insurance_identifier_id`, " + "claim_insurance_identifier.claim_insurance_id AS `claim_insurance_identifier claim_insurance_id`, " + "claim_insurance_identifier.identifier_code AS `claim_insurance_identifier identifier_code`, " + "claim_insurance_identifier.identifier AS `claim_insurance_identifier identifier` " + "from claim_block inner join claim_v2 on claim_block.claim_block_id=claim_v2.claim_block_id " + "and claim_block.customer_id=claim_v2.customer_id " + "left outer join claim_insurance on claim_v2.claim_id=claim_insurance.claim_id " + "left outer join claim_item on claim_v2.claim_id=claim_item.claim_id " + "left outer join claim_location on claim_block.claim_block_id=claim_location.claim_block_id " + "and claim_block.customer_id=claim_location.customer_id " + "left outer join claim_location_identifier on claim_location.claim_location_id=claim_location_identifier.claim_location_id " + "left outer join claim_physician on claim_block.claim_block_id=claim_physician.claim_block_id " + "and claim_block.customer_id=claim_physician.customer_id " + "left outer join claim_physician_identifier on claim_physician.claim_physician_id=claim_physician_identifier.claim_physician_id " + "left outer join gender patient_gender on claim_block.patient_gender=patient_gender.gender_id " + "left outer join gender guarantor_gender on claim_insurance.guarantor_gender=guarantor_gender.gender_id " + "left outer join claim_insurance_identifier on claim_insurance.claim_insurance_id=claim_insurance_identifier.claim_insurance_id " + "where claim_v2.invoice_status_type_id=" + Invoice_Status_TypeEntityBean.PENDING;
            getLogger().info(sql);
            rs = cmd.executeQuery(sql);
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = doc.createElement("root");
            root.setAttribute("interchangecontrolnumber", ICN.toString());
            doc.appendChild(root);
            HashMap claim_blocks = new HashMap();
            HashMap claim_v2s = new HashMap();
            HashMap claim_locations = new HashMap();
            HashMap claim_physicians = new HashMap();
            HashMap claim_location_identifiers = new HashMap();
            HashMap claim_physician_identifiers = new HashMap();
            HashMap claim_insurance_identifiers = new HashMap();
            HashMap claim_insurances = new HashMap();
            HashMap claim_items = new HashMap();
            Claim_V2EntityHome cv2Home = (Claim_V2EntityHome) getInitialContext().lookup(Claim_V2EntityHome.JNDI_NAME);
            Claim_StatusEntityHome csHome = (Claim_StatusEntityHome) getInitialContext().lookup(Claim_StatusEntityHome.JNDI_NAME);
            boolean _continue = rs.next();
            if (!_continue) return null;
            while (_continue) {
                String claim_block_id = rs.getString("claim_block claim_block_id");
                String customer_id = rs.getString("claim_block customer_id");
                String claim_id = rs.getString("claim_v2 claim_id");
                String claim_location_id = rs.getString("claim_location claim_location_id");
                String claim_physician_id = rs.getString("claim_physician claim_physician_id");
                String claim_location_identifier_id = rs.getString("claim_location_identifier claim_location_identifier_id");
                String claim_physician_identifer_id = rs.getString("claim_physician_identifier claim_physician_identifer_id");
                String claim_insurance_id = rs.getString("claim_insurance claim_insurance_id");
                String claim_insurance_identifier_id = rs.getString("claim_insurance_identifier claim_insurance_identifier_id");
                String claim_item_id = rs.getString("claim_item claim_item_id");
                Element claim_block = (Element) claim_blocks.get(claim_block_id + ":" + customer_id);
                if (claim_block == null) {
                    getLogger().debug("add claim block");
                    claim_block = doc.createElement("claim_block");
                    claim_blocks.put(claim_block_id + ":" + customer_id, claim_block);
                    root.appendChild(claim_block);
                    claim_block.setAttribute("claim_block_id", rs.getString("claim_block claim_block_id"));
                    claim_block.setAttribute("customer_id", rs.getString("claim_block customer_id"));
                    claim_block.setAttribute("charge_ticket_id", rs.getString("claim_block charge_ticket_id"));
                    claim_block.setAttribute("claim_type_id", rs.getString("claim_block claim_type_id"));
                    claim_block.setAttribute("patient_id", rs.getString("claim_block patient_id"));
                    claim_block.setAttribute("upload_file_id", rs.getString("claim_block upload_file_id"));
                    claim_block.setAttribute("create_date", rs.getString("claim_block create_date"));
                    claim_block.setAttribute("date_of_illness", rs.getString("claim_block date_of_illness"));
                    claim_block.setAttribute("first_date_of_illness", rs.getString("claim_block first_date_of_illness"));
                    claim_block.setAttribute("discharge_date", rs.getString("claim_block discharge_date"));
                    claim_block.setAttribute("admission_date", rs.getString("claim_block admission_date"));
                    claim_block.setAttribute("disability_from_date", rs.getString("claim_block disability_from_date"));
                    claim_block.setAttribute("disability_to_date", rs.getString("claim_block disability_to_date"));
                    claim_block.setAttribute("disability", rs.getString("claim_block disability"));
                    claim_block.setAttribute("accept_assignment", rs.getString("claim_block accept_assignment"));
                    claim_block.setAttribute("last_visit_date", rs.getString("claim_block last_visit_date"));
                    claim_block.setAttribute("service_date", rs.getString("claim_block service_date"));
                    claim_block.setAttribute("accident_related_cause", rs.getString("claim_block accident_related_cause"));
                    claim_block.setAttribute("accident_state", rs.getString("claim_block accident_state"));
                    claim_block.setAttribute("epsdt", rs.getString("claim_block epsdt"));
                    claim_block.setAttribute("place_of_service", rs.getString("claim_block place_of_service"));
                    claim_block.setAttribute("patient_lastname", rs.getString("claim_block patient_lastname"));
                    claim_block.setAttribute("patient_firstname", rs.getString("claim_block patient_firstname"));
                    claim_block.setAttribute("patient_mi", rs.getString("claim_block patient_mi"));
                    claim_block.setAttribute("patient_prefix", rs.getString("claim_block patient_prefix"));
                    claim_block.setAttribute("patient_suffix", rs.getString("claim_block patient_suffix"));
                    claim_block.setAttribute("patient_address1", rs.getString("claim_block patient_address1"));
                    claim_block.setAttribute("patient_address2", rs.getString("claim_block patient_address2"));
                    claim_block.setAttribute("patient_city", rs.getString("claim_block patient_city"));
                    claim_block.setAttribute("patient_state", rs.getString("claim_block patient_state"));
                    claim_block.setAttribute("patient_zip", rs.getString("claim_block patient_zip"));
                    claim_block.setAttribute("patient_country", rs.getString("claim_block patient_country"));
                    claim_block.setAttribute("patient_ssn", rs.getString("claim_block patient_ssn"));
                    claim_block.setAttribute("patient_dob", rs.getString("claim_block patient_dob"));
                    claim_block.setAttribute("patient_birthplace", rs.getString("claim_block patient_birthplace"));
                    claim_block.setAttribute("patient_gender", rs.getString("claim_block patient_gender"));
                    claim_block.setAttribute("patient_employment_status", rs.getString("claim_block patient_employment_status"));
                    claim_block.setAttribute("patient_marital_status", rs.getString("claim_block patient_marital_status"));
                    claim_block.setAttribute("patient_employer_name", rs.getString("claim_block patient_employer_name"));
                    claim_block.setAttribute("patient_phone", rs.getString("claim_block patient_phone"));
                    claim_block.setAttribute("patient_mrn", rs.getString("claim_block patient_mrn"));
                    claim_block.setAttribute("ext_claim_id", rs.getString("claim_block ext_claim_id"));
                    claim_block.setAttribute("outside_lab", rs.getString("claim_block outside_lab"));
                    claim_block.setAttribute("lab_charges", rs.getString("claim_block lab_charges"));
                    claim_block.setAttribute("prior_auth", rs.getString("claim_block prior_auth"));
                    claim_block.setAttribute("patient_policy_no", rs.getString("claim_block patient_policy_no"));
                    claim_block.setAttribute("patient_signature", rs.getString("claim_block patient_signature"));
                    claim_block.setAttribute("patient_signature_date", rs.getString("claim_block patient_signature_date"));
                    claim_block.setAttribute("physician_signature", rs.getString("claim_block physician_signature"));
                    claim_block.setAttribute("physician_signature_date", rs.getString("claim_block physician_signature_date"));
                    claim_block.setAttribute("date_of_menstruation", rs.getString("claim_block date_of_menstruation"));
                    claim_block.setAttribute("date_of_accident", rs.getString("claim_block date_of_accident"));
                    claim_block.setAttribute("icd1", rs.getString("claim_block icd1"));
                    claim_block.setAttribute("icd2", rs.getString("claim_block icd2"));
                    claim_block.setAttribute("icd3", rs.getString("claim_block icd3"));
                    claim_block.setAttribute("icd4", rs.getString("claim_block icd4"));
                    claim_block.setAttribute("icd5", rs.getString("claim_block icd5"));
                    claim_block.setAttribute("icd6", rs.getString("claim_block icd6"));
                    claim_block.setAttribute("icd7", rs.getString("claim_block icd7"));
                    claim_block.setAttribute("icd8", rs.getString("claim_block icd8"));
                }
                Element claim_v2 = (Element) claim_v2s.get(claim_id);
                if (claim_v2 == null && claim_id != null) {
                    getLogger().debug("add claim v2");
                    claim_v2 = doc.createElement("claim_v2");
                    claim_v2s.put(claim_id, claim_v2);
                    Claim_V2Entity cv2Entity = cv2Home.findByClaim_ID(new Integer(rs.getString("claim_v2 claim_id")));
                    Claim_V2EntityData cv2Data = cv2Entity.getValueObject();
                    cv2Data.setInvoice_Status_Type_ID(Invoice_Status_TypeEntityBean.SUBMITTED);
                    cv2Entity.updateValueObject(cv2Data);
                    Claim_StatusEntityData claimStatus = new Claim_StatusEntityData();
                    claimStatus.setChange_Date(new java.sql.Date(System.currentTimeMillis()));
                    claimStatus.setChanged_By("SYSTEM");
                    claimStatus.setClaim_ID(cv2Data.getClaim_ID());
                    claimStatus.setClaim_Status_Type_ID(Invoice_Status_TypeEntityBean.SUBMITTED);
                    claimStatus.setClaim_Status_Type("Submitted");
                    claimStatus.setX12_InterchangeControlNumber(ICN);
                    csHome.create(claimStatus);
                    claim_block.appendChild(claim_v2);
                    claim_v2.setAttribute("orig_ref_no", rs.getString("claim_v2 orig_ref_no"));
                    claim_v2.setAttribute("resubmission_code", rs.getString("claim_v2 resubmission_code"));
                    claim_v2.setAttribute("claim_id", rs.getString("claim_v2 claim_id"));
                    claim_v2.setAttribute("claim_block_id", rs.getString("claim_v2 claim_block_id"));
                    claim_v2.setAttribute("customer_id", rs.getString("claim_v2 customer_id"));
                    claim_v2.setAttribute("priority", rs.getString("claim_v2 priority"));
                    claim_v2.setAttribute("invoice_status_type_id", rs.getString("claim_v2 invoice_status_type_id"));
                    claim_v2.setAttribute("submitted", rs.getString("claim_v2 submitted"));
                    claim_v2.setAttribute("last_print_date", rs.getString("claim_v2 last_print_date"));
                    claim_v2.setAttribute("last_submit_date", rs.getString("claim_v2 last_submit_date"));
                    claim_v2.setAttribute("guarantor_signature", rs.getString("claim_v2 guarantor_signature"));
                }
                Element claim_insurance = (Element) claim_insurances.get(claim_insurance_id);
                if (claim_insurance == null && claim_insurance_id != null) {
                    getLogger().debug("add claim insurance");
                    claim_insurance = doc.createElement("claim_insurance");
                    claim_insurances.put(claim_insurance_id, claim_insurance);
                    claim_v2.appendChild(claim_insurance);
                    claim_insurance.setAttribute("claim_insurance_id", rs.getString("claim_insurance claim_insurance_id"));
                    claim_insurance.setAttribute("claim_id", rs.getString("claim_insurance claim_id"));
                    claim_insurance.setAttribute("guarantor_insurance_id", rs.getString("claim_insurance guarantor_insurance_id"));
                    claim_insurance.setAttribute("priority", rs.getString("claim_insurance priority"));
                    claim_insurance.setAttribute("guarantor_type_id", rs.getString("claim_insurance guarantor_type_id"));
                    claim_insurance.setAttribute("guarantor_lastname", rs.getString("claim_insurance guarantor_lastname"));
                    claim_insurance.setAttribute("guarantor_firstname", rs.getString("claim_insurance guarantor_firstname"));
                    claim_insurance.setAttribute("guarantor_mi", rs.getString("claim_insurance guarantor_mi"));
                    claim_insurance.setAttribute("guarantor_address1", rs.getString("claim_insurance guarantor_address1"));
                    claim_insurance.setAttribute("guarantor_address2", rs.getString("claim_insurance guarantor_address2"));
                    claim_insurance.setAttribute("guarantor_city", rs.getString("claim_insurance guarantor_city"));
                    claim_insurance.setAttribute("guarantor_state", rs.getString("claim_insurance guarantor_state"));
                    claim_insurance.setAttribute("guarantor_zip", rs.getString("claim_insurance guarantor_zip"));
                    claim_insurance.setAttribute("guarantor_country", rs.getString("claim_insurance guarantor_country"));
                    claim_insurance.setAttribute("relationship_type_id", rs.getString("claim_insurance relationship_type_id"));
                    claim_insurance.setAttribute("guarantor_dob", rs.getString("claim_insurance guarantor_dob"));
                    claim_insurance.setAttribute("guarantor_gender", rs.getString("claim_insurance guarantor_gender"));
                    claim_insurance.setAttribute("guarantor_phone", rs.getString("claim_insurance guarantor_phone"));
                    claim_insurance.setAttribute("policy_no", rs.getString("claim_insurance policy_no"));
                    claim_insurance.setAttribute("feca_no", rs.getString("claim_insurance feca_no"));
                    claim_insurance.setAttribute("employer_name", rs.getString("claim_insurance employer_name"));
                    claim_insurance.setAttribute("eclaims_payer_id", rs.getString("claim_insurance eclaims_payer_id"));
                    claim_insurance.setAttribute("payer_name", rs.getString("claim_insurance payer_name"));
                    claim_insurance.setAttribute("payer_address1", rs.getString("claim_insurance payer_address1"));
                    claim_insurance.setAttribute("payer_address2", rs.getString("claim_insurance payer_address2"));
                    claim_insurance.setAttribute("payer_city", rs.getString("claim_insurance payer_city"));
                    claim_insurance.setAttribute("payer_state", rs.getString("claim_insurance payer_state"));
                    claim_insurance.setAttribute("payer_zip", rs.getString("claim_insurance payer_zip"));
                    claim_insurance.setAttribute("payer_country", rs.getString("claim_insurance payer_country"));
                    claim_insurance.setAttribute("plan_name", rs.getString("claim_insurance plan_name"));
                    claim_insurance.setAttribute("plan_address1", rs.getString("claim_insurance plan_address1"));
                    claim_insurance.setAttribute("plan_address2", rs.getString("claim_insurance plan_address2"));
                    claim_insurance.setAttribute("plan_city", rs.getString("claim_insurance plan_city"));
                    claim_insurance.setAttribute("plan_state", rs.getString("claim_insurance plan_state"));
                    claim_insurance.setAttribute("plan_zip", rs.getString("claim_insurance plan_zip"));
                    claim_insurance.setAttribute("plan_country", rs.getString("claim_insurance plan_country"));
                    claim_insurance.setAttribute("insurance_type_id", rs.getString("claim_insurance insurance_type_id"));
                    claim_insurance.setAttribute("plan_type_id", rs.getString("claim_insurance plan_type_id"));
                    claim_insurance.setAttribute("insurance_company_plan_id", rs.getString("claim_insurance insurance_company_plan_id"));
                    claim_insurance.setAttribute("place_id", rs.getString("claim_insurance place_id"));
                }
                Element claim_insurance_identifier = (Element) claim_insurance_identifiers.get(claim_insurance_identifier_id);
                if (claim_insurance_identifier == null && claim_insurance_identifier_id != null) {
                    getLogger().debug("add claim insurance identifier");
                    claim_insurance_identifier = doc.createElement("claim_insurance_identifier");
                    claim_insurance_identifiers.put(claim_insurance_identifier_id, claim_insurance_identifier);
                    claim_insurance.appendChild(claim_insurance_identifier);
                    claim_insurance_identifier.setAttribute("claim_insurance_identifier_id", rs.getString("claim_insurance_identifier claim_insurance_identifier_id"));
                    claim_insurance_identifier.setAttribute("claim_insurance_id", rs.getString("claim_insurance_identifier claim_insurance_id"));
                    claim_insurance_identifier.setAttribute("identifier_code", rs.getString("claim_insurance_identifier identifier_code"));
                    claim_insurance_identifier.setAttribute("identifier", rs.getString("claim_insurance_identifier identifier"));
                }
                Element claim_location = (Element) claim_locations.get(claim_location_id);
                if (claim_location == null && claim_location_id != null) {
                    getLogger().debug("add claim location");
                    claim_location = doc.createElement("claim_location");
                    claim_locations.put(claim_location_id, claim_location);
                    claim_block.appendChild(claim_location);
                    claim_location.setAttribute("claim_location_id", rs.getString("claim_location claim_location_id"));
                    claim_location.setAttribute("claim_block_id", rs.getString("claim_location claim_block_id"));
                    claim_location.setAttribute("customer_id", rs.getString("claim_location customer_id"));
                    claim_location.setAttribute("claim_item_id", rs.getString("claim_location claim_item_id"));
                    claim_location.setAttribute("location_name", rs.getString("claim_location location_name"));
                    claim_location.setAttribute("location_address1", rs.getString("claim_location location_address1"));
                    claim_location.setAttribute("location_address2", rs.getString("claim_location location_address2"));
                    claim_location.setAttribute("location_city", rs.getString("claim_location location_city"));
                    claim_location.setAttribute("location_state", rs.getString("claim_location location_state"));
                    claim_location.setAttribute("location_zip", rs.getString("claim_location location_zip"));
                    claim_location.setAttribute("location_country", rs.getString("claim_location location_country"));
                }
                Element claim_location_identifier = (Element) claim_location_identifiers.get(claim_location_identifier_id);
                if (claim_location_identifier == null && claim_location_identifier_id != null) {
                    getLogger().debug("add claim location identifier");
                    claim_location_identifier = doc.createElement("claim_location_identifier");
                    claim_location_identifiers.put(claim_location_identifier_id, claim_location_identifier);
                    claim_location.appendChild(claim_location_identifier);
                    claim_location_identifier.setAttribute("claim_location_identifier_id", rs.getString("claim_location_identifier claim_location_identifier_id"));
                    claim_location_identifier.setAttribute("claim_location_id", rs.getString("claim_location_identifier claim_location_id"));
                    claim_location_identifier.setAttribute("identifier_code", rs.getString("claim_location_identifier identifier_code"));
                    claim_location_identifier.setAttribute("identifier", rs.getString("claim_location_identifier identifier"));
                }
                Element claim_physician = (Element) claim_physicians.get(claim_physician_id);
                if (claim_physician == null && claim_physician_id != null) {
                    getLogger().debug("add claim physician");
                    claim_physician = doc.createElement("claim_physician");
                    claim_physicians.put(claim_physician_id, claim_physician);
                    claim_block.appendChild(claim_physician);
                    claim_physician.setAttribute("claim_physician_id", rs.getString("claim_physician claim_physician_id"));
                    claim_physician.setAttribute("claim_block_id", rs.getString("claim_physician claim_block_id"));
                    claim_physician.setAttribute("customer_id", rs.getString("claim_physician customer_id"));
                    claim_physician.setAttribute("claim_item_id", rs.getString("claim_physician claim_item_id"));
                    claim_physician.setAttribute("physician_lastname", rs.getString("claim_physician physician_lastname"));
                    claim_physician.setAttribute("physician_firstname", rs.getString("claim_physician physician_firstname"));
                    claim_physician.setAttribute("physician_mi", rs.getString("claim_physician physician_mi"));
                    claim_physician.setAttribute("physician_address1", rs.getString("claim_physician physician_address1"));
                    claim_physician.setAttribute("physician_address2", rs.getString("claim_physician physician_address2"));
                    claim_physician.setAttribute("physician_city", rs.getString("claim_physician physician_city"));
                    claim_physician.setAttribute("physician_state", rs.getString("claim_physician physician_state"));
                    claim_physician.setAttribute("physician_zip", rs.getString("claim_physician physician_zip"));
                    claim_physician.setAttribute("physician_country", rs.getString("claim_physician physician_country"));
                    claim_physician.setAttribute("physician_class_id", rs.getString("claim_physician physician_class_id"));
                    claim_physician.setAttribute("entity_type_id", rs.getString("claim_physician entity_type_id"));
                    claim_physician.setAttribute("tax_id", rs.getString("claim_physician tax_id"));
                    claim_physician.setAttribute("pinorgrp", rs.getString("claim_physician pinorgrp"));
                }
                Element claim_physician_identifier = (Element) claim_physician_identifiers.get(claim_physician_identifer_id);
                if (claim_physician_identifier == null && claim_physician_identifer_id != null) {
                    getLogger().debug("add claim physician identifier");
                    claim_physician_identifier = doc.createElement("claim_physician_identifier");
                    claim_physician_identifiers.put(claim_physician_identifer_id, claim_physician_identifier);
                    claim_physician.appendChild(claim_physician_identifier);
                    claim_physician_identifier.setAttribute("claim_physician_identifer_id", rs.getString("claim_physician_identifier claim_physician_identifer_id"));
                    claim_physician_identifier.setAttribute("claim_physician_id", rs.getString("claim_physician_identifier claim_physician_id"));
                    claim_physician_identifier.setAttribute("identifier_code", rs.getString("claim_physician_identifier identifier_code"));
                    claim_physician_identifier.setAttribute("identifier", rs.getString("claim_physician_identifier identifier"));
                }
                Element claim_item = (Element) claim_items.get(claim_item_id);
                if (claim_item == null && claim_item_id != null) {
                    getLogger().debug("add claim item");
                    claim_item = doc.createElement("claim_item");
                    claim_items.put(claim_item_id, claim_item);
                    claim_v2.appendChild(claim_item);
                    claim_item.setAttribute("claim_item_id", rs.getString("claim_item claim_item_id"));
                    claim_item.setAttribute("claim_id", rs.getString("claim_item claim_id"));
                    claim_item.setAttribute("line_no", rs.getString("claim_item line_no"));
                    claim_item.setAttribute("service_date_to", rs.getString("claim_item service_date_to"));
                    claim_item.setAttribute("service_date_from", rs.getString("claim_item service_date_from"));
                    claim_item.setAttribute("place_of_service", rs.getString("claim_item place_of_service"));
                    claim_item.setAttribute("type_of_service", rs.getString("claim_item type_of_service"));
                    claim_item.setAttribute("procedure_code", rs.getString("claim_item procedure_code"));
                    claim_item.setAttribute("mod1", rs.getString("claim_item mod1"));
                    claim_item.setAttribute("mod2", rs.getString("claim_item mod2"));
                    claim_item.setAttribute("mod3", rs.getString("claim_item mod3"));
                    claim_item.setAttribute("icd1", rs.getString("claim_item icd1"));
                    claim_item.setAttribute("icd2", rs.getString("claim_item icd2"));
                    claim_item.setAttribute("icd3", rs.getString("claim_item icd3"));
                    claim_item.setAttribute("icd4", rs.getString("claim_item icd4"));
                    claim_item.setAttribute("charge", rs.getString("claim_item charge"));
                    claim_item.setAttribute("charge_unit", rs.getString("claim_item charge_unit"));
                    claim_item.setAttribute("charge_unit_type_id", rs.getString("claim_item charge_unit_type_id"));
                    claim_item.setAttribute("allowed", rs.getString("claim_item allowed"));
                    claim_item.setAttribute("payer_paid", rs.getString("claim_item payer_paid"));
                    claim_item.setAttribute("patient_responsible", rs.getString("claim_item patient_responsible"));
                    claim_item.setAttribute("discount", rs.getString("claim_item discount"));
                    claim_item.setAttribute("writeoff", rs.getString("claim_item writeoff"));
                    claim_item.setAttribute("adjustment_code", rs.getString("claim_item adjustment_code"));
                    claim_item.setAttribute("extra_info", rs.getString("claim_item extra_info"));
                    claim_item.setAttribute("epsdt", rs.getString("claim_item epsdt"));
                    claim_item.setAttribute("emg", rs.getString("claim_item emg"));
                    claim_item.setAttribute("cob", rs.getString("claim_item cob"));
                    claim_item.setAttribute("family_plan", rs.getString("claim_item family_plan"));
                }
                _continue = rs.next();
            }
            return doc;
        } finally {
            try {
                cn.close();
            } catch (Exception e) {
            }
        }
    }

    protected X12_InterchangeControlNumberEntity getNextInterchangeNumber() throws Exception {
        X12_InterchangeControlNumberEntityHome home = (X12_InterchangeControlNumberEntityHome) getInitialContext().lookup(X12_InterchangeControlNumberEntityHome.JNDI_NAME);
        X12_InterchangeControlNumberEntity entity = home.create(new X12_InterchangeControlNumberEntityData());
        return entity;
    }
}
