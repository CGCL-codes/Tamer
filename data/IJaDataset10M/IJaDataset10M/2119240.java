package com.apelon.dts.client.concept;

import com.apelon.apelonserver.client.ApelonException;
import com.apelon.apelonserver.client.ServerConnection;
import com.apelon.common.xml.XML;
import com.apelon.common.xml.XMLException;
import com.apelon.dts.client.DTSException;
import com.apelon.dts.client.common.DTSHeader;
import com.apelon.dts.transfer.CommonResultHandler;
import com.apelon.dts.transfer.OntylogConceptHandler;
import com.apelon.dts.util.DTSXMLFactory;

/**
 * <code>OntylogClassQueryServer</code> supports queries on subconcept
 * relationships within an Ontylog concept hierarchy.  Ontylog concepts reside
 * in a namespace provided by subscription from Apelon, or loaded from a TDE
 * database.
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Apelon, Inc.</p>
 * @author     Apelon, Inc.
 * @version    DTS 3.0.0
 *
 * @since      DTS 3.0.0
 */
public abstract class OntylogClassQuery extends BaseOntylogConceptQuery {

    OntylogClassQuery(ServerConnection sc, boolean isValidating) {
        super(sc, isValidating);
    }

    /**
   * Creates an instance of OntylogClassQuery.
   * Depending on the type of ServerConnection object passed in the
   * argument, the server may be local(JDBC), socket, or another implementation.
   *
   * @param      sc               the type of server connection
   * @return     the created OntylogClassQueryServer
   * @throws     IllegalArgumentException  if a null server connection is passed
   *
   * @see        com.apelon.apelonserver.client.ServerConnectionJDBC
   * @see        com.apelon.apelonserver.client.ServerConnectionSocket
   */
    public static OntylogClassQuery createInstance(ServerConnection sc) throws IllegalArgumentException {
        OntylogClassQuery newServer = null;
        if (sc == null) {
            throw new IllegalArgumentException("OntylogClassQueryServer.createInstance() called with null argument");
        }
        newServer = new OntylogClassQueryImpl(sc);
        newServer.setVersion(DTSHeader.CLASSQUERYSERVER_HEADER);
        return newServer;
    }

    /**
   * Creates an instance of OntylogClassQueryServer.
   * Depending on the type of ServerConnection object passed in the
   * argument, the server may be local(JDBC), socket, or another implementation.
   * The second argument is a boolean value for XML validation.
   *
   * @param      sc               the type of server connection
   * @param      isValidating     a boolean value.  It is true if XML needs to
   *                              be validated, otherwise it is false.
   * @return     the created OntylogClassQueryServer
   * @throws     IllegalArgumentException  if a null server connection is passed
   *
   * @see        com.apelon.apelonserver.client.ServerConnectionJDBC
   * @see        com.apelon.apelonserver.client.ServerConnectionSocket
   */
    public static OntylogClassQuery createInstance(ServerConnection sc, boolean isValidating) throws IllegalArgumentException {
        OntylogClassQuery newServer = null;
        if (sc == null) {
            throw new IllegalArgumentException("OntylogClassQueryServer.createInstance() called with null argument");
        }
        newServer = new OntylogClassQueryImpl(sc, isValidating);
        newServer.setVersion(DTSHeader.CLASSQUERYSERVER_HEADER);
        return newServer;
    }

    /**
   * Determines if concept1 is a subconcept of concept2.
   *
   * @param      concept1         an Ontylog concept
   * @param      concept2         an Ontylog concept
   * @return     <code>true</code> if concept1 is a subconcept of concept2,
   *             <code>false</code> if concept1 isn't a subconcept of concept2
   * @throws     DTSException     if any error occurs in the method
   */
    public boolean isSubConcept(OntylogConcept concept1, OntylogConcept concept2) throws DTSException {
        if (concept1 == null) {
            throw new DTSException(new IllegalArgumentException("OntylogConcept concept1 argument is null."));
        }
        if (concept2 == null) {
            throw new DTSException(new IllegalArgumentException("OntylogConcept concept2 argument is null."));
        }
        String query = "";
        query += XML.createProlog("isSubConcept", com.apelon.dts.dtd.query.DTD.CLASSQUERY, false);
        query += "<isSubConcept>";
        query += DTSXMLFactory.createConceptParamXML("id", String.valueOf(concept1.getId()), concept1.getNamespaceId());
        query += DTSXMLFactory.createConceptParamXML("id", String.valueOf(concept2.getId()), concept2.getNamespaceId());
        query += "</isSubConcept>";
        return processQuery(query);
    }

    /**
   * Determines if concept1 is a subconcept of an array of concepts.
   *
   * @param      concept1         an Ontylog concept
   * @param      concepts         an array of Ontylog concepts
   * @return     an array of booleans indicating if concept1 is a subconcept of
   *             the corresponding concept in the array of concepts
   * @throws     DTSException     if any error occurs in the method
   */
    public boolean[] isSubConcept(OntylogConcept concept1, OntylogConcept[] concepts) throws DTSException {
        if (concept1 == null) {
            throw new DTSException(new IllegalArgumentException("OntylogConcept concept1 argument is null."));
        }
        if (concepts == null) {
            throw new DTSException(new IllegalArgumentException("OntylogConcept[] concepts argument is null."));
        }
        if (concepts.length == 0) {
            throw new DTSException(new IllegalArgumentException("OntylogConcept[] concepts argument array length is 0."));
        }
        String query = "";
        query += XML.createProlog("isSubConcept", com.apelon.dts.dtd.query.DTD.CLASSQUERY, false);
        query += "<isSubConcept>";
        query += DTSXMLFactory.createConceptParamXML("id", String.valueOf(concept1.getId()), concept1.getNamespaceId());
        query += "<conceptParams>";
        for (int i = 0; i < concepts.length; i++) {
            query += DTSXMLFactory.createConceptParamXML("id", String.valueOf(concepts[i].getId()), concepts[i].getNamespaceId());
        }
        query += "</conceptParams>";
        query += "</isSubConcept>";
        return buildBooleans(query);
    }

    /**
   * Determines if concept1 is a subconcept of concept2 in the specified
   * namespace.  Concept1 and concept2 are names of Ontylog concepts.
   *
   * @param      concept1         a concept name
   * @param      concept2         a concept name
   * @param      namespaceId      namespace ID of the two Ontylog concepts
   * @return     <code>true</code> if concept1 is a subconcept of concept2,
   *             <code>false</code> if concept1 isn't a subconcept of concept2
   * @throws     DTSException     if any error occurs in the method
   */
    public boolean isSubConceptByName(String concept1, String concept2, int namespaceId) throws DTSException {
        if (concept1 == null) {
            throw new DTSException(new IllegalArgumentException("String concept1 argument is null."));
        }
        if (concept2 == null) {
            throw new DTSException(new IllegalArgumentException("String concept2 argument is null."));
        }
        String query = "";
        query += XML.createProlog("isSubConcept", com.apelon.dts.dtd.query.DTD.CLASSQUERY, false);
        query += "<isSubConcept>";
        query += DTSXMLFactory.createConceptParamXML("name", concept1, namespaceId);
        query += DTSXMLFactory.createConceptParamXML("name", concept2, namespaceId);
        query += "</isSubConcept>";
        return processQuery(query);
    }

    /**
   * Determines if concept1 is a subconcept of concept2.  Concept1 and
   * concept2 are IDs of concepts.
   *
   * @param      conceptId1       a concept ID
   * @param      conceptId2       a concept ID
   * @param      namespaceId      namespace ID of the two Ontylog concepts
   * @return     <code>true</code> if concept1 is a subconcept of concept2,
   *             <code>false</code> if concept1 isn't a subconcept of concept2
   * @throws     DTSException     if any error occurs in the method
   */
    public boolean isSubConceptById(int conceptId1, int conceptId2, int namespaceId) throws DTSException {
        String query = "";
        query += XML.createProlog("isSubConcept", com.apelon.dts.dtd.query.DTD.CLASSQUERY, false);
        query += "<isSubConcept>";
        query += DTSXMLFactory.createConceptParamXML("id", Integer.toString(conceptId1), namespaceId);
        query += DTSXMLFactory.createConceptParamXML("id", Integer.toString(conceptId2), namespaceId);
        query += "</isSubConcept>";
        return processQuery(query);
    }

    /**
   * Determines if concept1 is a subconcept of concept2.  Concept1 and
   * concept2 are codes of concepts.
   *
   * @param      concept1         a concept code
   * @param      concept2         a concept code
   * @param      namespaceId      namespace ID of the two Ontylog concepts
   * @return     <code>true</code> if concept1 is a subconcept of concept2,
   *             <code>false</code> if concept1 isn't a subconcept of concept2
   * @throws     DTSException     if any error occurs in the method
   */
    public boolean isSubConceptByCode(String concept1, String concept2, int namespaceId) throws DTSException {
        if (concept1 == null) {
            throw new DTSException(new IllegalArgumentException("String concept1 argument is null."));
        }
        if (concept2 == null) {
            throw new DTSException(new IllegalArgumentException("String concept2 argument is null."));
        }
        String query = "";
        query += XML.createProlog("isSubConcept", com.apelon.dts.dtd.query.DTD.CLASSQUERY, false);
        query += "<isSubConcept>";
        query += DTSXMLFactory.createConceptParamXML("code", concept1, namespaceId);
        query += DTSXMLFactory.createConceptParamXML("code", concept2, namespaceId);
        query += "</isSubConcept>";
        return processQuery(query);
    }

    /**
   * Determines if concept1 is a subconcept of concepts.  Concepts is an
   * array of concept names.  Concept1 is a concept name.
   *
   * @param      concept1         a concept name
   * @param      concepts         an array of concept names
   * @param      namespaceId      namespace ID of the Ontylog concepts
   * @return     an array of booleans indicating if concept1 is a subconcept of
   *             the corresponding concept in the array of concepts
   * @throws     DTSException     if any error occurs in the method
   */
    public boolean[] isSubConceptByName(String concept1, String[] concepts, int namespaceId) throws DTSException {
        if (concept1 == null) {
            throw new DTSException(new IllegalArgumentException("String concept1 argument is null."));
        }
        if (concepts == null) {
            throw new DTSException(new IllegalArgumentException("String[] concepts argument is null."));
        }
        if (concepts.length == 0) {
            throw new DTSException(new IllegalArgumentException("String[] concepts argument array length is 0."));
        }
        String query = "";
        query += XML.createProlog("isSubConcept", com.apelon.dts.dtd.query.DTD.CLASSQUERY, false);
        query += "<isSubConcept>";
        query += DTSXMLFactory.createConceptParamXML("name", concept1, namespaceId);
        query += "<conceptParams>";
        for (int i = 0; i < concepts.length; i++) {
            query += DTSXMLFactory.createConceptParamXML("name", concepts[i], namespaceId);
        }
        query += "</conceptParams>";
        query += "</isSubConcept>";
        return buildBooleans(query);
    }

    /**
   * Gets all the subconcepts of the concept argument with attributes filled
   * in according to the ConceptAttributeSetDescriptor.
   *
   * @param      concept          a concept
   * @param      dtsAsd           a ConceptAttributeSetDescriptor
   * @return     an array of concepts which are subconcepts of a concept with
   *             attributes filled in according to the ConceptAttributeSetDescriptor.
   * @throws     DTSException     if any error occurs in the method
   */
    public OntylogConcept[] getSubConcepts(OntylogConcept concept, ConceptAttributeSetDescriptor dtsAsd) throws DTSException {
        if (concept == null) {
            throw new DTSException(new IllegalArgumentException("OntylogConcept concept argument is null."));
        }
        if (dtsAsd == null) {
            throw new DTSException(new IllegalArgumentException("ConceptAttributeSetDescriptor dtsAsd argument is null."));
        }
        String query = "";
        query += XML.createProlog("getSubConcepts", com.apelon.dts.dtd.query.DTD.CLASSQUERY, false);
        query += "<getSubConcepts>";
        query += DTSXMLFactory.createConceptParamXML("name", concept.getName(), concept.getNamespaceId());
        query += ConceptAttributeSetDescriptor.asXML(dtsAsd);
        query += "</getSubConcepts>";
        try {
            String response = executeQueryNoParse(query);
            return OntylogConceptHandler.getOntylogConcepts(response);
        } catch (XMLException ex) {
            throw new DTSException("Unable to create OntylogConcepts from XML result document.", ex);
        } catch (ApelonException ex) {
            throw new DTSException("Error executing XML query.", ex);
        }
    }

    private boolean processQuery(String query) throws DTSException {
        boolean bool = false;
        try {
            String response = executeQueryNoParse(query);
            bool = ((Boolean) CommonResultHandler.getObject(response)).booleanValue();
        } catch (XMLException ex) {
            throw new DTSException("Unable to create boolean from XML result document.", ex);
        } catch (ApelonException ex) {
            throw new DTSException("Error executing XML query.", ex);
        }
        return bool;
    }

    private boolean[] buildBooleans(String query) throws DTSException {
        boolean bool = false;
        try {
            String response = executeQueryNoParse(query);
            Boolean[] booleans = (Boolean[]) CommonResultHandler.getBooleans(response);
            boolean[] values = new boolean[booleans.length];
            for (int i = 0; i < booleans.length; i++) {
                values[i] = booleans[i].booleanValue();
            }
            return values;
        } catch (XMLException ex) {
            throw new DTSException("Unable to create boolean from XML result document.", ex);
        } catch (ApelonException ex) {
            throw new DTSException("Error executing XML query.", ex);
        }
    }
}
