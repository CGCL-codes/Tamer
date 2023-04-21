package org.personalsmartspace.cm.broker.impl.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.personalsmartspace.cm.broker.test.CtxBrokerTestUtils;
import org.personalsmartspace.cm.db.api.platform.ICtxDBManager;
import org.personalsmartspace.cm.db.impl.util.HibernateUtil;
import org.personalsmartspace.cm.model.api.pss3p.ICtxAttribute;
import org.personalsmartspace.cm.model.api.pss3p.ICtxEntity;
import org.personalsmartspace.cm.model.api.pss3p.ICtxEntityIdentifier;
import org.personalsmartspace.cm.model.api.pss3p.ICtxIdentifier;

/**
 * Context Broker Utilities unit tests.
 * 
 * @author      <a href="mailto:phdn@users.sourceforge.net">phdn</a>
 * @since       0.5.1
 */
public class TestCtxBrokerUtils {

    private static final String TEST_STRING = "test";

    private static final Integer TEST_INTEGER = 99;

    private static final Double TEST_DOUBLE = 99.99d;

    private static ICtxDBManager ctxDbManager = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ctxDbManager = CtxBrokerTestUtils.createCtxDbManager();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        HibernateUtil.shutdown();
        ctxDbManager = null;
    }

    @Test
    public void testAttributeValueAsStringString() {
        assertEquals(TEST_STRING, CtxBrokerUtils.attributeValueAsString(TEST_STRING));
    }

    @Test
    public void testAttributeValueAsStringInteger() {
        assertEquals(TEST_INTEGER.toString(), CtxBrokerUtils.attributeValueAsString(TEST_INTEGER));
    }

    @Test
    public void testAttributeValueAsStringDouble() {
        assertEquals(TEST_DOUBLE.toString(), CtxBrokerUtils.attributeValueAsString(TEST_DOUBLE));
    }

    @Test
    public void testAttributeValueAsStringOther() {
        final Byte testByte = new Byte(Byte.MAX_VALUE);
        assertEquals(CtxBrokerUtils.BLOB_STRING, CtxBrokerUtils.attributeValueAsString(testByte));
    }

    @Test
    public void testAttributeValueAsStringNull() {
        assertNull(CtxBrokerUtils.attributeValueAsString(null));
    }

    @Test
    public void testRemoveDuplicateIdentifiers() throws Exception {
        final ICtxEntity entity = ctxDbManager.createEntity(TEST_STRING);
        final ICtxAttribute attribute = ctxDbManager.createAttribute(entity.getCtxIdentifier(), TEST_STRING);
        final List<ICtxIdentifier> identifiers = new ArrayList<ICtxIdentifier>();
        identifiers.add(entity.getCtxIdentifier());
        identifiers.add(attribute.getCtxIdentifier());
        identifiers.add(entity.getCtxIdentifier());
        identifiers.add(attribute.getCtxIdentifier());
        identifiers.add(attribute.getCtxIdentifier());
        assertEquals(5, identifiers.size());
        CtxBrokerUtils.removeDuplicateIdentifiers(identifiers);
        assertEquals(2, identifiers.size());
        assertTrue(identifiers.contains(entity.getCtxIdentifier()));
        assertTrue(identifiers.contains(attribute.getCtxIdentifier()));
    }

    @Test
    public void testRemoveDuplicateEntities() throws Exception {
        final ICtxEntity entity1 = ctxDbManager.createEntity("entity_1");
        final ICtxEntity entity2 = ctxDbManager.createEntity("entity_2");
        final List<ICtxEntityIdentifier> identifiers = new ArrayList<ICtxEntityIdentifier>();
        identifiers.add(entity1.getCtxIdentifier());
        identifiers.add(entity2.getCtxIdentifier());
        identifiers.add(entity1.getCtxIdentifier());
        identifiers.add(entity2.getCtxIdentifier());
        identifiers.add(entity2.getCtxIdentifier());
        assertEquals(5, identifiers.size());
        CtxBrokerUtils.removeDuplicateEntities(identifiers);
        assertEquals(2, identifiers.size());
        assertTrue(identifiers.contains(entity1.getCtxIdentifier()));
        assertTrue(identifiers.contains(entity2.getCtxIdentifier()));
    }
}
