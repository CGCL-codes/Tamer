package com.legstar.cixs.jbossesb;

import java.util.Map;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.format.MessageFactory;
import com.legstar.coxb.host.HostData;
import com.legstar.coxb.transform.AbstractTransformers;
import com.legstar.test.coxb.LsfileaeCases;
import com.legstar.test.coxb.lsfileae.Dfhcommarea;
import junit.framework.TestCase;

/**
 * Test AbstractHostToJavaAction.
 *
 */
public class AbstractHostToJavaActionTest extends TestCase {

    /** A LegStarMessage serialization. */
    private static final String LEGSTAR_MESSAGE_HOST_DATA = "d3e2d6d2c8c5c1c44040404040404040000000080000000100000000d3e2d6d2c3d6d4d4c1d9c5c140404040" + "0000004ff0f0f0f1f0f0e3d6e3d640404040404040404040404040404040d3c1c2c1e240e2e3d9c5c5e34040404040404040" + "f8f8f9f9f3f3f1f4f1f0f0f4f5f84040f0f0f1f0f04bf3f5c140e5d6c9d9404040";

    /**
     * test getRawHostContent().
     */
    public void testGetRawHostContent() {
        ConfigTree config = new ConfigTree("root");
        AbstractHostToJavaAction transformer;
        try {
            transformer = new AbstractHostToJavaActionImpl(config);
        } catch (ConfigurationException e1) {
            fail(e1.toString());
            return;
        }
        Message esbMessage = MessageFactory.getInstance().getMessage();
        try {
            transformer.getRawHostContent(esbMessage);
            fail();
        } catch (ActionProcessingException e) {
            assertEquals("Request content is null", e.getMessage());
        }
        esbMessage.getBody().add(new byte[2]);
        try {
            assertTrue(transformer.getRawHostContent(esbMessage) instanceof byte[]);
        } catch (ActionProcessingException e) {
            fail(e.toString());
        }
    }

    /**
     * test getLegStarMessage().
     */
    public void testDetectLegStarMessaging() {
        try {
            ConfigTree config = new ConfigTree("root");
            AbstractHostToJavaAction transformer = new AbstractHostToJavaActionImpl(config);
            Message esbMessage = MessageFactory.getInstance().getMessage();
            esbMessage.getBody().add(HostData.toByteArray(LEGSTAR_MESSAGE_HOST_DATA));
            Message esbtransformedMessage = transformer.process(esbMessage);
            assertTrue(AbstractHostTransformerAction.isLegStarMessaging(esbtransformedMessage));
            esbMessage.getBody().add(HostData.toByteArray(LsfileaeCases.getHostBytesHex()));
            esbtransformedMessage = transformer.process(esbMessage);
            assertFalse(AbstractHostTransformerAction.isLegStarMessaging(esbtransformedMessage));
            Message esbMessageNew = MessageFactory.getInstance().getMessage();
            esbMessageNew.getBody().add(HostData.toByteArray(LsfileaeCases.getHostBytesHex()));
            esbtransformedMessage = transformer.process(esbMessageNew);
            assertFalse(AbstractHostTransformerAction.isLegStarMessaging(esbtransformedMessage));
        } catch (ConfigurationException e) {
            fail(e.toString());
        } catch (ActionProcessingException e) {
            fail(e.toString());
        }
    }

    /**
     * test processRawHost().
     * @throws Exception if something goes wrong
     */
    public void testProcessRawHost() throws Exception {
        ConfigTree config = new ConfigTree("root");
        AbstractHostToJavaAction transformer = new AbstractHostToJavaActionImpl(config);
        Message esbMessage = MessageFactory.getInstance().getMessage();
        esbMessage.getBody().add(HostData.toByteArray(LsfileaeCases.getHostBytesHex()));
        Message changedEsbMessage = transformer.process(esbMessage);
        assertTrue(changedEsbMessage.getBody().get() instanceof Dfhcommarea);
        Dfhcommarea valueObject = (Dfhcommarea) changedEsbMessage.getBody().get();
        LsfileaeCases.checkJavaObject(valueObject);
    }

    /**
     * test processLegStarMessage().
     * @throws Exception if something goes wrong
     */
    public void testProcessLegStarMessage() throws Exception {
        ConfigTree config = new ConfigTree("root");
        AbstractHostToJavaAction transformer = new AbstractHostToJavaActionImpl(config);
        Message esbMessage = MessageFactory.getInstance().getMessage();
        esbMessage.getBody().add(HostData.toByteArray(LEGSTAR_MESSAGE_HOST_DATA));
        Message changedEsbMessage = transformer.process(esbMessage);
        assertTrue(changedEsbMessage.getBody().get() instanceof Dfhcommarea);
        Dfhcommarea valueObject = (Dfhcommarea) changedEsbMessage.getBody().get();
        LsfileaeCases.checkJavaObject(valueObject);
    }

    /**
     * test capability to wrap as map entry.
     * @throws Exception if something goes wrong
     */
    @SuppressWarnings("unchecked")
    public void testWrapAsMapEntry() throws Exception {
        ConfigTree config = new ConfigTree("root");
        config.setAttribute(AbstractHostTransformerAction.MAP_KEY_NAME_PROPERTY, "the Key");
        AbstractHostToJavaAction transformer = new AbstractHostToJavaActionImpl(config);
        Message esbMessage = MessageFactory.getInstance().getMessage();
        esbMessage.getBody().add(HostData.toByteArray(LsfileaeCases.getHostBytesHex()));
        Message changedEsbMessage = transformer.process(esbMessage);
        Object content = changedEsbMessage.getBody().get();
        assertTrue(content instanceof Map);
        Map<String, Object> mapContent = (Map<String, Object>) content;
        Dfhcommarea valueObject = (Dfhcommarea) mapContent.get("the Key");
        LsfileaeCases.checkJavaObject(valueObject);
    }

    /**
     * A simplistic implementation of the abstract class being tested.
     *
     */
    public static class AbstractHostToJavaActionImpl extends AbstractHostToJavaAction {

        /**
         * @param config the Jboss ESB configuration tree
         * @throws ConfigurationException if configuration parameters are invalid
         */
        public AbstractHostToJavaActionImpl(final ConfigTree config) throws ConfigurationException {
            super(config);
        }

        /** {@inheritDoc}*/
        public AbstractTransformers getTransformers() {
            return new com.legstar.test.coxb.lsfileae.bind.DfhcommareaTransformers();
        }
    }
}
