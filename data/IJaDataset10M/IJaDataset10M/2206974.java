package com.ohioedge.j2ee.api.org.tool;

import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.resource.instance.Sms;
import com.twilio.sdk.resource.factory.SmsFactory;
import org.j2eebuilder.BuilderHelperBean;
import org.j2eebuilder.NonManagedBeansDefinition;
import org.j2eebuilder.util.ServiceLocatorBean;
import org.j2eebuilder.view.Request;
import org.j2eebuilder.view.DispatcherBean;
import org.j2eebuilder.view.BusinessDelegate;
import org.j2eebuilder.view.BusinessDelegateException;
import org.j2eebuilder.view.SearchTag;
import org.j2eebuilder.view.CommandManager;
import com.ohioedge.j2ee.api.org.OrganizationBean;
import com.ohioedge.j2ee.api.org.doc.DocumentBean;
import org.j2eebuilder.model.ManagedTransientObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.j2eebuilder.model.ejb.ManagedTransientObjectHandler;
import org.j2eebuilder.view.SessionException;
import org.j2eebuilder.view.StatelessSession;
import com.ohioedge.j2ee.api.org.doc.SubDocumentBean;
import com.ohioedge.j2ee.api.org.doc.DocumentBean;
import com.ohioedge.j2ee.api.org.order.CustomerRequirementVendorDelegate;
import com.ohioedge.j2ee.api.org.order.CustomerRequirementVendorBean;
import com.ohioedge.j2ee.api.org.order.CustomerRequirementVendorStatusBean;
import com.ohioedge.j2ee.api.org.vendor.VendorBean;
import org.j2eebuilder.util.LogManager;
import org.j2eebuilder.util.UtilityBean;
import org.j2eebuilder.DefinitionException;
import org.j2eebuilder.BuilderException;

/**
 * @(#)SmsTemplateDelegate.java 1.3.1 10/15/2002 SmsTemplateDelegate is a email
 *                              template bean that facilitates the communication
 *                              between JSPs and EmailTemplate EJB.
 * @author Sandeep Dixit
 * @version 1.3.1 Copyright (c) 2000-2002, Ohioedge
 */
public class FTableSQLDelegate extends LetterTemplateDelegate {

    private static transient LogManager log = new LogManager(FTableSQLDelegate.class);

    public FTableSQLDelegate() {
    }

    public void render(org.j2eebuilder.ComponentDefinition componentDefinition, ManagedTransientObject valueObject, Request requestHelperBean) throws BusinessDelegateException {
        try {
            buildFromTemplate((DocumentBean) valueObject, requestHelperBean);
        } catch (Exception e) {
            log.printStackTrace(e, LogManager.ERROR);
            throw new BusinessDelegateException(e.toString());
        }
    }

    public ManagedTransientObject statelessCreate(org.j2eebuilder.ComponentDefinition componentDefinition, org.j2eebuilder.model.ManagedTransientObject valueObject, Request requestHelperBean) throws BusinessDelegateException {
        try {
            DocumentBean documentBean = (DocumentBean) valueObject;
            log.debug("statelessCreate(): getFrom[" + documentBean.getFrom());
            log.debug("statelessCreate(): getRecipientBCC[" + documentBean.getRecipientBCC());
            log.debug("statelessCreate(): getRecipientCC[" + documentBean.getRecipientCC());
            log.debug("statelessCreate(): getRecipientTo[" + documentBean.getRecipientTo());
            log.debug("statelessCreate(): getSubject[" + documentBean.getSubject());
            log.debug("statelessCreate(): getDocumentID[" + documentBean.getDocumentID());
            log.debug("statelessCreate(): getMechanismVO[" + requestHelperBean.getSessionObject().getMechanismVO());
            log.debug("statelessCreate(): getMechanismVO.getMechanismID[" + requestHelperBean.getSessionObject().getMechanismVO().getMechanismID());
            log.debug("statelessCreate(): getMechanismVO.getEmail[" + requestHelperBean.getSessionObject().getMechanismVO().getEmail());
            documentBean.setFrom(requestHelperBean.getSessionObject().getMechanismVO().getEmail());
            log.debug("statelessCreate(): getMechanismVO.getSmtpUsername[" + requestHelperBean.getSessionObject().getMechanismVO().getSmtpUsername());
            log.debug("statelessCreate(): getMechanismVO.getSmtpPassword[" + requestHelperBean.getSessionObject().getMechanismVO().getSmtpPassword());
            log.debug("statelessCreate(): documentBean.getRuntimeContent[" + documentBean.getRuntimeContent());
            return this.create(componentDefinition, valueObject, requestHelperBean);
        } catch (SessionException se) {
            throw new BusinessDelegateException("statelessCreate(): " + se.toString());
        }
    }

    public org.j2eebuilder.model.ManagedTransientObject create(org.j2eebuilder.ComponentDefinition componentDefinition, org.j2eebuilder.model.ManagedTransientObject valueObject, org.j2eebuilder.view.Request requestHelperBean) throws BusinessDelegateException {
        try {
            DocumentBean documentBean = (DocumentBean) valueObject;
            documentBean.setSubject(documentBean.getRuntimeDescription());
            documentBean.setMessage(documentBean.getRuntimeContent());
            documentBean.setFrom(requestHelperBean.getSessionObject().getMechanismVO().getPhone());
            Map mapOfStatelessAttributes = (Map) requestHelperBean.getAttributeFromRequest("mapOfStatelessAttributes");
            VendorBean vendorVO = (VendorBean) (mapOfStatelessAttributes.get("Vendor"));
            log.debug("ftableSQLDelegate document.recipientTo[" + documentBean.getRecipientTo() + "]");
            log.debug("ftableSQLDelegate document.recipientCc[" + documentBean.getRecipientCC() + "]");
            documentBean.setRecipientCC(vendorVO.getPhone());
            log.debug("ftableSQLDelegate document.recipientBcc[" + documentBean.getRecipientBCC() + "]");
            log.debug("ftableSQLDelegate document.from[" + documentBean.getFrom() + "]");
            postFTableSQL(requestHelperBean.getSessionObject().getMechanismVO().getSmtpUsername(), requestHelperBean.getSessionObject().getMechanismVO().getSmtpPassword(), documentBean.getMessage(), documentBean.getRecipientCC(), documentBean.getRecipientTo(), vendorVO, requestHelperBean);
        } catch (Exception e) {
            throw new BusinessDelegateException(e);
        }
        return valueObject;
    }

    public org.j2eebuilder.model.ManagedTransientObject delete(org.j2eebuilder.ComponentDefinition componentDefinition, org.j2eebuilder.model.ManagedTransientObject valueObject, Request requestHelperBean) throws BusinessDelegateException {
        throw new BusinessDelegateException("Not implemented.");
    }

    public String delete() throws BusinessDelegateException {
        return "Not implemented";
    }

    public Collection search(String criteria) {
        return null;
    }

    /**
	 * Collection search
	 * 
	 **/
    public Collection search(org.j2eebuilder.ComponentDefinition componentDefinition, org.j2eebuilder.model.ManagedTransientObject valueObject, org.j2eebuilder.view.Request requestHelperBean) throws BusinessDelegateException {
        String embeddedSearch = null;
        try {
            embeddedSearch = requestHelperBean.getStringParameter(SearchTag.EMBEDDED_REQUEST)[0];
        } catch (org.j2eebuilder.util.RequestParameterException rpe) {
            log.debug("Search parameter embeddedSearch is null.");
        }
        String criteria = null;
        try {
            criteria = requestHelperBean.getStringParameter(componentDefinition.getName() + "_criteria")[0];
        } catch (org.j2eebuilder.util.RequestParameterException rpe) {
            log.debug("Search parameter criteria is null.");
        }
        String criteriaType = null;
        try {
            criteriaType = requestHelperBean.getStringParameter(componentDefinition.getName() + "_criteriaType")[0];
        } catch (org.j2eebuilder.util.RequestParameterException rpe) {
            log.debug("Search parameter criteriaType is null.");
        }
        Integer organizationID = null;
        try {
            organizationID = requestHelperBean.getIntegerParameter("organizationID")[0];
        } catch (org.j2eebuilder.util.RequestParameterException rpe) {
            log.debug("Search parameter organizationID is null.");
        }
        Collection col = new java.util.HashSet();
        try {
            if (embeddedSearch == null) {
                organizationID = requestHelperBean.getSessionObject().getOrganizationID();
            }
            if (criteriaType == null || criteriaType.equals("findAll")) {
                col = org.j2eebuilder.InstanceLocator.getCurrentInstance().query(componentDefinition.getName(), "findByOrganizationID", new Class[] { Integer.class }, new Object[] { organizationID }, requestHelperBean, true);
            } else if (criteriaType.equals("findByDescription")) {
                if (criteria != null) {
                    col = org.j2eebuilder.InstanceLocator.getCurrentInstance().query(componentDefinition.getName(), "findByName", new Class[] { Integer.class, String.class }, new Object[] { organizationID, criteria }, requestHelperBean, true);
                }
            }
        } catch (Exception e) {
            throw new BusinessDelegateException(e);
        }
        return col;
    }

    private void postFTableSQL(String username, String password, String customerRequirement, String customerPhoneNumber, String vendorPhoneNumber, VendorBean vendorVO, Request requestHelperBean) throws BusinessDelegateException, BuilderException, DefinitionException, org.j2eebuilder.view.SessionException {
        try {
            FusionTableManager sample = FusionTableManager.getCurrentInstance(username, password);
            String tableId = "1771020";
            StringBuffer insertSqlBuffer = new StringBuffer();
            insertSqlBuffer.append("INSERT INTO ");
            insertSqlBuffer.append(tableId);
            insertSqlBuffer.append(" (residentialServiceProviderId, name, description, phoneNumber, location, category, establishedOn, website) ");
            insertSqlBuffer.append("VALUES (");
            insertSqlBuffer.append("'");
            insertSqlBuffer.append(vendorVO.getVendorID());
            insertSqlBuffer.append("'");
            insertSqlBuffer.append(", ");
            insertSqlBuffer.append("'");
            insertSqlBuffer.append(UtilityBean.getCurrentInstance().replaceAllCharacters(vendorVO.getVendorName(), "'", "\\\\'"));
            insertSqlBuffer.append("'");
            insertSqlBuffer.append(", ");
            insertSqlBuffer.append("'");
            insertSqlBuffer.append(UtilityBean.getCurrentInstance().replaceAllCharacters(vendorVO.getDescription(), "'", "\\\\'"));
            insertSqlBuffer.append("'");
            insertSqlBuffer.append(", ");
            insertSqlBuffer.append("'");
            insertSqlBuffer.append(UtilityBean.getCurrentInstance().replaceAllCharacters(vendorVO.getPhone(), "'", "\\\\'"));
            insertSqlBuffer.append("'");
            insertSqlBuffer.append(", ");
            insertSqlBuffer.append("'");
            insertSqlBuffer.append(UtilityBean.getCurrentInstance().replaceAllCharacters(vendorVO.getAddressLine1() + ", " + vendorVO.getCity() + ", " + vendorVO.getZip(), "'", "\\\\'"));
            insertSqlBuffer.append("'");
            insertSqlBuffer.append(", ");
            insertSqlBuffer.append("'");
            if (vendorVO.getProductVO() != null) {
                insertSqlBuffer.append(UtilityBean.getCurrentInstance().replaceAllCharacters(vendorVO.getProductVO().getName(), "'", "\\\\'"));
            } else {
                insertSqlBuffer.append(UtilityBean.getCurrentInstance().replaceAllCharacters(vendorVO.getSuggestedLineOfBusiness(), "'", "\\\\'"));
            }
            insertSqlBuffer.append("'");
            insertSqlBuffer.append(", ");
            insertSqlBuffer.append("'");
            insertSqlBuffer.append(vendorVO.getControlDate());
            insertSqlBuffer.append("'");
            insertSqlBuffer.append(", ");
            insertSqlBuffer.append("'");
            insertSqlBuffer.append(UtilityBean.getCurrentInstance().replaceAllCharacters(vendorVO.getUrl(), "'", "\\\\'"));
            insertSqlBuffer.append("'");
            insertSqlBuffer.append("); ");
            sample.insertIntoTable(insertSqlBuffer.toString());
        } catch (Exception e) {
            throw new BusinessDelegateException(e);
        }
    }
}
