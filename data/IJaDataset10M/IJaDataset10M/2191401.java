package com.apelonTests.dts.client;

import com.apelon.apelonserver.client.ServerConnection;
import com.apelon.dts.client.attribute.*;
import com.apelon.dts.client.common.DTSHeader;
import com.apelon.dts.client.namespace.Namespace;
import com.apelon.dts.client.term.*;
import com.apelon.dts.client.association.*;
import com.apelon.dts.client.concept.*;
import com.apelonTests.ConfiguredTest;
import com.apelonTests.dts.client.config.DTSClientFactory;
import junit.framework.Test;
import junit.framework.TestSuite;
import java.util.Properties;

/**
 * TestTerm is a junit test case for NavQuery API. <P>
 * Copyright:     Copyright (c) 2001<P>
 * Company:       Apelon <P>
 *
 * @see           com.apelon.dts.client.namespace.Namespace
 *
 *

 */
public class TestNavQuery extends ConfiguredTest {

    private static ServerConnection oracleConn;

    private static ServerConnection sql2kConn;

    private DTSConcept[] dtsconcepts;

    private DTSSearchOptions searchOption;

    private static String[] testNames = { "testGetNavChildContext", "testGetNavParentContext" };

    private NavQuery navQuery;

    private AssociationQuery assocQuery;

    public TestNavQuery(String s) {
        super(s);
    }

    public TestNavQuery(ServerConnection s, String testName) {
        super(testName);
        try {
            s.setQueryServer(Class.forName("com.apelon.dts.server.NamespaceServer"), DTSHeader.NAMESPACESERVER_HEADER);
            s.setQueryServer(Class.forName("com.apelon.dts.server.AssociationServer"), DTSHeader.ASSOCIATIONSERVER_HEADER);
            s.setQueryServer(Class.forName("com.apelon.dts.server.NavQueryServer"), DTSHeader.NAVSERVER_HEADER);
            navQuery = NavQuery.createInstance(s);
            assocQuery = AssociationQuery.createInstance(s);
        } catch (Exception e) {
            navQuery = null;
            assocQuery = null;
        }
    }

    public TestNavQuery() {
        this(TestNavQuery.class.getName());
    }

    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite();
        Properties props = new TestNavQuery().props();
        oracleConn = TestNamespace.getServerConnection("oracle", props);
        if (oracleConn != null) {
            for (int i = 0; i < testNames.length; i++) {
                suite.addTest(new TestNavQuery(oracleConn, testNames[i] + TestNamespace.ORACLE));
            }
        }
        sql2kConn = TestNamespace.getServerConnection("sql2k", props);
        if (sql2kConn != null) {
            for (int i = 0; i < testNames.length; i++) {
                suite.addTest(new TestNavQuery(sql2kConn, testNames[i] + TestNamespace.SQL2K));
            }
        }
        return suite;
    }

    public void setUp() throws Exception {
        dtsconcepts = DTSClientFactory.getDTSConcepts("dtsconcepts.xml");
        searchOption = new DTSSearchOptions();
        searchOption.setLimit(1);
        searchOption.setNamespaceId(DTSSearchOptions.ALL_NAMESPACES);
        searchOption.setAttributeSetDescriptor(ConceptAttributeSetDescriptor.NO_ATTRIBUTES);
    }

    protected String propertiesFile() {
        return "TestConnect.properties";
    }

    public void testGetNavChildContextORACLE() throws Exception {
        testGetNavChildContext();
    }

    public void testGetNavChildContextSQL2K() throws Exception {
        testGetNavChildContext();
    }

    public void testGetNavParentContextORACLE() throws Exception {
        testGetNavParentContext();
    }

    public void testGetNavParentContextSQL2K() throws Exception {
        testGetNavParentContext();
    }

    private void testGetNavChildContext() throws Exception {
        NavChildContext context = navQuery.getNavChildContext((OntylogConcept) dtsconcepts[0], ConceptAttributeSetDescriptor.NO_ATTRIBUTES);
        if (context == null) {
            return;
        }
        ConceptChild[] contextChildren = context.getChildren();
        assertTrue("testGetNavChildContext ", (contextChildren.length > 0));
    }

    private void testGetNavParentContext() throws Exception {
        ConceptAssociation[] assocs = dtsconcepts[0].getFetchedConceptAssociations();
        NavParentContext context = navQuery.getNavParentContext((OntylogConcept) assocs[0].getToConcept(), ConceptAttributeSetDescriptor.NO_ATTRIBUTES);
        if (context == null) {
            return;
        }
        ConceptParent[] contextParent = context.getParents();
        assertTrue("testGetNavParentContext ", (contextParent.length > 0));
        assertTrue("testGetNavParentContext ", (contextParent[0].getHasParents() == false));
    }
}
