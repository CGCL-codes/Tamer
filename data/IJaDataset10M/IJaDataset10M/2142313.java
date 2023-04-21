package org.gamegineer.game.internal.core.system.bindings.xml;

import static org.gamegineer.game.core.system.Assert.assertGameSystemEquals;
import java.io.Reader;
import java.io.StringReader;
import javax.xml.bind.UnmarshalException;
import org.gamegineer.game.core.system.GameSystems;
import org.gamegineer.game.core.system.IGameSystem;
import org.gamegineer.game.core.system.NonValidatingGameSystemBuilder;
import org.gamegineer.game.core.system.bindings.xml.XmlGameSystems;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A fixture for testing the
 * {@link org.gamegineer.game.internal.core.system.bindings.xml.XmlGameSystem}
 * class.
 */
public final class XmlGameSystemTest extends AbstractJaxbTestCase {

    /** The game system builder for use in the fixture. */
    private NonValidatingGameSystemBuilder m_builder;

    /**
     * Initializes a new instance of the {@code XmlGameSystemTest} class.
     */
    public XmlGameSystemTest() {
        super();
    }

    private static Reader createGameSystemReader(final IGameSystem gameSystem) {
        assert gameSystem != null;
        return new StringReader(XmlGameSystems.toXml(gameSystem));
    }

    @Override
    protected Class<?> getRootElementType() {
        return XmlGameSystem.class;
    }

    @Before
    @Override
    public void setUp() throws Exception {
        m_builder = new NonValidatingGameSystemBuilder(GameSystems.createUniqueGameSystem());
        super.setUp();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        m_builder = null;
    }

    /**
     * Ensures the {@code toGameSystem} method creates the expected game system
     * when given a well-formed game system.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Test
    public void testToGameSystem_Success() throws Exception {
        final IGameSystem expectedGameSystem = m_builder.toGameSystem();
        final XmlGameSystem xmlGameSystem = (XmlGameSystem) getUnmarshaller().unmarshal(createGameSystemReader(expectedGameSystem));
        final IGameSystem actualGameSystem = xmlGameSystem.toGameSystem();
        assertGameSystemEquals(expectedGameSystem, actualGameSystem);
    }

    /**
     * Ensures a game system fails to be unmarshalled when it does not have an
     * identifier.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Test(expected = UnmarshalException.class)
    public void testUnmarshal_Fail_NoId() throws Exception {
        m_builder.setId(null);
        getUnmarshaller().unmarshal(createGameSystemReader(m_builder.toGameSystem()));
    }

    /**
     * Ensures a game system fails to be unmarshalled when it does not have a
     * role container.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Ignore("XmlElementWrapper.required = true does not appear to be respected")
    @Test(expected = UnmarshalException.class)
    public void testUnmarshal_Fail_NoRoleContainer() throws Exception {
        m_builder.clearRoles();
        getUnmarshaller().unmarshal(createGameSystemReader(m_builder.toGameSystem()));
    }

    /**
     * Ensures a game system fails to be unmarshalled when it does not have at
     * least one role.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Test(expected = UnmarshalException.class)
    public void testUnmarshal_Fail_NoRoles() throws Exception {
        m_builder.clearRoles().addRole(null);
        getUnmarshaller().unmarshal(createGameSystemReader(m_builder.toGameSystem()));
    }

    /**
     * Ensures a game system fails to be unmarshalled when it does not have a
     * stage container.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Ignore("XmlElementWrapper.required = true does not appear to be respected")
    @Test(expected = UnmarshalException.class)
    public void testUnmarshal_Fail_NoStageContainer() throws Exception {
        m_builder.clearStages();
        getUnmarshaller().unmarshal(createGameSystemReader(m_builder.toGameSystem()));
    }

    /**
     * Ensures a game system fails to be unmarshalled when it does not have at
     * least one stage.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Test(expected = UnmarshalException.class)
    public void testUnmarshal_Fail_NoStages() throws Exception {
        m_builder.clearStages().addStage(null);
        getUnmarshaller().unmarshal(createGameSystemReader(m_builder.toGameSystem()));
    }

    /**
     * Ensures a game system is successfully unmarshalled when it is completely
     * specified.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Test
    public void testUnmarshal_Success_Complete() throws Exception {
        final IGameSystem expectedGameSystem = m_builder.toGameSystem();
        final XmlGameSystem xmlGameSystem = (XmlGameSystem) getUnmarshaller().unmarshal(createGameSystemReader(expectedGameSystem));
        final IGameSystem actualGameSystem = xmlGameSystem.toGameSystem();
        assertGameSystemEquals(expectedGameSystem, actualGameSystem);
    }
}
