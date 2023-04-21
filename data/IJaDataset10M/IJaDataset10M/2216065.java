package net.sourceforge.mazix.persistence.dao.impl.xml.dom.levels.squares.keydoor;

import static net.sourceforge.mazix.components.constants.CommonFileConstants.XML_EXTENSION;
import static net.sourceforge.mazix.components.constants.CommonFileConstants.XSD_EXTENSION;
import static net.sourceforge.mazix.components.utils.file.FileUtils.readFileIntoString;
import static net.sourceforge.mazix.components.utils.file.FileUtils.writeIntoFile;
import static net.sourceforge.mazix.persistence.constants.FileConstants.LEVELS_PATH;
import static net.sourceforge.mazix.persistence.constants.FileConstants.LEVELS_SERIES_FILE;
import static net.sourceforge.mazix.persistence.constants.LevelTestConstants.LEVEL_DTO_1;
import static net.sourceforge.mazix.persistence.constants.LevelTestConstants.LEVEL_DTO_4;
import static net.sourceforge.mazix.persistence.constants.LevelTestConstants.LEVEL_DTO_5;
import static net.sourceforge.mazix.persistence.constants.SquareTestConstants.KEY_DOOR_SQUARE_DTO_1;
import static net.sourceforge.mazix.persistence.constants.SquareTestConstants.KEY_DOOR_SQUARE_DTO_2;
import static net.sourceforge.mazix.persistence.constants.SquareTestConstants.KEY_DOOR_SQUARE_DTO_3;
import static net.sourceforge.mazix.persistence.constants.SquareTestConstants.KEY_DOOR_SQUARE_DTO_4;
import static net.sourceforge.mazix.persistence.constants.SquareTestConstants.KEY_DOOR_SQUARE_DTO_5;
import static net.sourceforge.mazix.persistence.constants.SquareTestConstants.KEY_DOOR_SQUARE_DTO_6;
import static net.sourceforge.mazix.persistence.constants.SquareTestConstants.KEY_DOOR_SQUARE_DTO_7;
import static net.sourceforge.mazix.persistence.constants.SquareTestConstants.SQUARE_TEST_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.util.Set;
import net.sourceforge.mazix.components.exception.PersistenceException;
import net.sourceforge.mazix.persistence.dao.levels.squares.keydoor.KeyDoorSquareDAOTest;
import net.sourceforge.mazix.persistence.dto.levels.squares.keydoor.KeyDoorSquareDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test classes for {@link net.sourceforge.mazix.persistence.dao.impl.xml.dom.levels.squares.arrival.XMLDOMArrivalSquareDAO}.
 *
 * @author Benjamin Croizet (<a href="mailto:graffity2199@yahoo.fr>graffity2199@yahoo.fr</a>)
 *
 * @since 0.8
 * @version 0.8
 */
public class XMLDOMKeyDoorSquareDAOTest implements KeyDoorSquareDAOTest {

    /** The {@link XMLDOMKeyDoorSquareDAO} test instance. */
    private static final XMLDOMKeyDoorSquareDAO XML_DOM_KEY_DOOR_SQUARE_DAO = new XMLDOMKeyDoorSquareDAO(LEVELS_PATH, LEVELS_SERIES_FILE + XSD_EXTENSION);

    /**
     * This method is called before each test to reset the level series temporary XML file content.
     *
     * @since 0.8
     */
    @Before
    public void initializeXMLFile() {
        writeIntoFile("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<series xmlns:ns0=\"http://www.w3.org/2001/XMLSchema-instance\" ns0:noNamespaceSchemaLocation=\"levelSeries.xsd\">\r\n" + "\t<language lang=\"en\" name=\"Tutorial\" comment=\"\" />\r\n" + "\t<language lang=\"fr\" name=\"Didacticiel\" comment=\"\" />\r\n" + "\t<level lines=\"8\" author=\"Mazix\" level_type=\"simple\" id=\"0\" time=\"0\" columns=\"8\">\r\n" + "\t\t<language lang=\"en\" name=\"Tutorial 0\" comment=\"Lighting, camera, and controls\" />\r\n" + "\t\t<language lang=\"fr\" name=\"Didacticiel 0\" comment=\"Eclairage, manipulation camÃ©ra, et contrÃ´les\" />\r\n" + "\t\t<empty x=\"0\" y=\"0\" />\r\n" + "\t\t<empty x=\"1\" y=\"0\" />\r\n" + "\t\t<empty x=\"2\" y=\"0\" />\r\n" + "\t\t<empty x=\"3\" y=\"0\" />\r\n" + "\t\t<empty x=\"4\" y=\"0\" />\r\n" + "\t\t<wall x=\"5\" y=\"0\" />\r\n" + "\t\t<wall x=\"6\" y=\"0\" />\r\n" + "\t\t<arrival x=\"7\" y=\"0\" />\r\n" + "\r\n" + "\t\t<empty x=\"0\" y=\"1\" />\r\n" + "\t\t<wall x=\"1\" y=\"1\" />\r\n" + "\t\t<empty x=\"2\" y=\"1\" />\r\n" + "\t\t<wall x=\"3\" y=\"1\" />\r\n" + "\t\t<wall x=\"4\" y=\"1\" />\r\n" + "\t\t<empty x=\"5\" y=\"1\" />\r\n" + "\t\t<empty x=\"6\" y=\"1\" />\r\n" + "\t\t<empty x=\"7\" y=\"1\" />\r\n" + "\r\n" + "\t\t<empty x=\"0\" y=\"2\" />\r\n" + "\t\t<wall x=\"1\" y=\"2\" />\r\n" + "\t\t<empty x=\"2\" y=\"2\" />\r\n" + "\t\t<empty x=\"3\" y=\"2\" />\r\n" + "\t\t<empty x=\"4\" y=\"2\" />\r\n" + "\t\t<empty x=\"5\" y=\"2\" />\r\n" + "\t\t<wall x=\"6\" y=\"2\" />\r\n" + "\t\t<empty x=\"7\" y=\"2\" />\r\n" + "\r\n" + "\t\t<empty x=\"0\" y=\"3\" />\r\n" + "\t\t<wall x=\"1\" y=\"3\" />\r\n" + "\t\t<empty x=\"2\" y=\"3\" />\r\n" + "\t\t<wall x=\"3\" y=\"3\" />\r\n" + "\t\t<wall x=\"4\" y=\"3\" />\r\n" + "\t\t<empty x=\"5\" y=\"3\" />\r\n" + "\t\t<wall x=\"6\" y=\"3\" />\r\n" + "\t\t<empty x=\"7\" y=\"3\" />\r\n" + "\r\n" + "\t\t<empty x=\"0\" y=\"4\" />\r\n" + "\t\t<wall x=\"1\" y=\"4\" />\r\n" + "\t\t<empty x=\"2\" y=\"4\" />\r\n" + "\t\t<empty x=\"3\" y=\"4\" />\r\n" + "\t\t<empty x=\"4\" y=\"4\" />\r\n" + "\t\t<empty x=\"5\" y=\"4\" />\r\n" + "\t\t<wall x=\"6\" y=\"4\" />\r\n" + "\t\t<empty x=\"7\" y=\"4\" />\r\n" + "\r\n" + "\t\t<empty x=\"0\" y=\"5\" />\r\n" + "\t\t<wall x=\"1\" y=\"5\" />\r\n" + "\t\t<empty x=\"2\" y=\"5\" />\r\n" + "\t\t<wall x=\"3\" y=\"5\" />\r\n" + "\t\t<wall x=\"4\" y=\"5\" />\r\n" + "\t\t<empty x=\"5\" y=\"5\" />\r\n" + "\t\t<wall x=\"6\" y=\"5\" />\r\n" + "\t\t<empty x=\"7\" y=\"5\" />\r\n" + "\r\n" + "\t\t<empty x=\"0\" y=\"6\" />\r\n" + "\t\t<wall x=\"1\" y=\"6\" />\r\n" + "\t\t<empty x=\"2\" y=\"6\" />\r\n" + "\t\t<empty x=\"3\" y=\"6\" />\r\n" + "\t\t<empty x=\"4\" y=\"6\" />\r\n" + "\t\t<empty x=\"5\" y=\"6\" />\r\n" + "\t\t<wall x=\"6\" y=\"6\" />\r\n" + "\t\t<empty x=\"7\" y=\"6\" />\r\n" + "\r\n" + "\t\t<departure x=\"0\" y=\"7\">\r\n" + "\t\t\t<lantern lighting=\"-1\" />\r\n" + "\t\t</departure>\r\n" + "\t\t<empty x=\"1\" y=\"7\" />\r\n" + "\t\t<empty x=\"2\" y=\"7\" />\r\n" + "\t\t<wall x=\"3\" y=\"7\" />\r\n" + "\t\t<wall x=\"4\" y=\"7\" />\r\n" + "\t\t<empty x=\"5\" y=\"7\" />\r\n" + "\t\t<empty x=\"6\" y=\"7\" />\r\n" + "\t\t<empty x=\"7\" y=\"7\" />\r\n" + "\t</level>\r\n" + "\t<level lines=\"8\" author=\"Mazix\" level_type=\"simple\" id=\"1\" time=\"0\" columns=\"8\">\r\n" + "\t\t<language lang=\"en\" name=\"Tutorial 8\" comment=\"Summary\" />\r\n" + "\t\t<language lang=\"fr\" name=\"Didacticiel 8\" comment=\"RÃ©capitulatif\" />\r\n" + "\r\n" + "\t\t<arrival x=\"0\" y=\"0\" />\r\n" + "\t\t<wall x=\"1\" y=\"0\" />\r\n" + "\t\t<empty x=\"2\" y=\"0\" />\r\n" + "\t\t<teleport x=\"3\" y=\"0\" color=\"PINK\" link_x=\"5\" link_y=\"0\" />\r\n" + "\t\t<wall x=\"4\" y=\"0\" />\r\n" + "\t\t<empty x=\"5\" y=\"0\" />\r\n" + "\t\t<wall x=\"6\" y=\"0\" />\r\n" + "\t\t<empty x=\"7\" y=\"0\">\r\n" + "\t\t\t<key color=\"SEMI_RED\" />\r\n" + "\t\t</empty>\r\n" + "\r\n" + "\t\t<key_door x=\"0\" y=\"1\" color=\"SEMI_RED\" />\r\n" + "\t\t<wall x=\"1\" y=\"1\" />\r\n" + "\t\t<empty x=\"2\" y=\"1\" />\r\n" + "\t\t<empty x=\"3\" y=\"1\" />\r\n" + "\t\t<wall x=\"4\" y=\"1\" />\r\n" + "\t\t<empty x=\"5\" y=\"1\" />\r\n" + "\t\t<wall x=\"6\" y=\"1\" />\r\n" + "\t\t<empty x=\"7\" y=\"1\" />\r\n" + "\r\n" + "\t\t<empty x=\"0\" y=\"2\" />\r\n" + "\t\t<switch_door x=\"1\" y=\"2\" color=\"SEMI_BLUE\" />\r\n" + "\t\t<empty x=\"2\" y=\"2\" />\r\n" + "\t\t<empty x=\"3\" y=\"2\" />\r\n" + "\t\t<wall x=\"4\" y=\"2\" />\r\n" + "\t\t<empty x=\"5\" y=\"2\" />\r\n" + "\t\t<empty x=\"6\" y=\"2\" />\r\n" + "\t\t<empty x=\"7\" y=\"2\" />\r\n" + "\r\n" + "\t\t<wall x=\"0\" y=\"3\" />\r\n" + "\t\t<wall x=\"1\" y=\"3\" />\r\n" + "\t\t<empty x=\"2\" y=\"3\" />\r\n" + "\t\t<wall x=\"3\" y=\"3\" />\r\n" + "\t\t<wall x=\"4\" y=\"3\" />\r\n" + "\t\t<empty x=\"5\" y=\"3\" />\r\n" + "\t\t<empty x=\"6\" y=\"3\" />\r\n" + "\t\t<switch_button x=\"7\" y=\"3\" color=\"SEMI_BLUE\" link_x=\"1\" link_y=\"2\" />\r\n" + "\r\n" + "\t\t<empty x=\"0\" y=\"4\" />\r\n" + "\t\t<empty x=\"1\" y=\"4\">\r\n" + "\t\t\t<ball />\r\n" + "\t\t</empty>\r\n" + "\t\t<empty x=\"2\" y=\"4\" />\r\n" + "\t\t<wall x=\"3\" y=\"4\" />\r\n" + "\t\t<empty x=\"4\" y=\"4\" />\r\n" + "\t\t<empty x=\"5\" y=\"4\" />\r\n" + "\t\t<wall x=\"6\" y=\"4\" />\r\n" + "\t\t<wall x=\"7\" y=\"4\" />\r\n" + "\r\n" + "\t\t<empty x=\"0\" y=\"5\" />\r\n" + "\t\t<wall x=\"1\" y=\"5\" />\r\n" + "\t\t<empty x=\"2\" y=\"5\" />\r\n" + "\t\t<wall x=\"3\" y=\"5\" />\r\n" + "\t\t<switch_door x=\"4\" y=\"5\" color=\"SEMI_GREEN\" />\r\n" + "\t\t<wall x=\"5\" y=\"5\" />\r\n" + "\t\t<empty x=\"6\" y=\"5\">\r\n" + "\t\t\t<key color=\"SEMI_YELLOW\" />\r\n" + "\t\t</empty>\r\n" + "\t\t<empty x=\"7\" y=\"5\" />\r\n" + "\r\n" + "\t\t<empty x=\"0\" y=\"6\" />\r\n" + "\t\t<wall x=\"1\" y=\"6\" />\r\n" + "\t\t<empty x=\"2\" y=\"6\">\r\n" + "\t\t\t<ball />\r\n" + "\t\t</empty>\r\n" + "\t\t<wall x=\"3\" y=\"6\" />\r\n" + "\t\t<empty x=\"4\" y=\"6\" />\r\n" + "\t\t<empty x=\"5\" y=\"6\" />\r\n" + "\t\t<wall x=\"6\" y=\"6\" />\r\n" + "\t\t<empty x=\"7\" y=\"6\" />\r\n" + "\r\n" + "\t\t<departure x=\"0\" y=\"7\">\r\n" + "\t\t\t<lantern lighting=\"-1\" />\r\n" + "\t\t</departure>\r\n" + "\t\t<empty x=\"1\" y=\"7\" />\r\n" + "\t\t<key_door x=\"2\" y=\"7\" color=\"SEMI_YELLOW\" />\r\n" + "\t\t<switch_button x=\"3\" y=\"7\" color=\"SEMI_GREEN\" link_x=\"4\" link_y=\"5\" />\r\n" + "\t\t<wall x=\"4\" y=\"7\" />\r\n" + "\t\t<empty x=\"5\" y=\"7\" />\r\n" + "\t\t<empty x=\"6\" y=\"7\" />\r\n" + "\t\t<departure x=\"7\" y=\"7\">\r\n" + "\t\t\t<lantern lighting=\"-1\" />\r\n" + "\t\t</departure>\r\n" + "\t</level>\r\n" + "</series>", XML_DOM_KEY_DOOR_SQUARE_DAO.getFilePath() + SQUARE_TEST_FILE + XML_EXTENSION);
    }

    /**
     * This method is called after all tests, it is used to remove the temporary file used for the
     * tests.
     *
     * @since 0.8
     */
    @After
    public void removeLevelSeriesTestFile() {
        final File levelSeriesTestFile = new File(XML_DOM_KEY_DOOR_SQUARE_DAO.getFilePath() + KEY_DOOR_SQUARE_DTO_1.getLevelSeriesFileName() + XML_EXTENSION);
        levelSeriesTestFile.delete();
    }

    /**
     * {@inheritDoc}
     * @since 0.8
     */
    @Override
    @Test
    public void testCreateOrUpdateKeyDoorSquareWithExistingSeriesWithExistingLevelWithExistingSquare() throws PersistenceException {
        XML_DOM_KEY_DOOR_SQUARE_DAO.createOrUpdateKeyDoorSquare(KEY_DOOR_SQUARE_DTO_1);
        final String xmlLevelSeriesContent = readFileIntoString(XML_DOM_KEY_DOOR_SQUARE_DAO.getFilePath() + KEY_DOOR_SQUARE_DTO_1.getLevelSeriesFileName() + XML_EXTENSION);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" + "<series xmlns:ns0=\"http://www.w3.org/2001/XMLSchema-instance\" ns0:noNamespaceSchemaLocation=\"levelSeries.xsd\">\n" + "\t<language comment=\"\" lang=\"en\" name=\"Tutorial\"/>\n" + "\t<language comment=\"\" lang=\"fr\" name=\"Didacticiel\"/>\n" + "\t<level author=\"Mazix\" columns=\"8\" id=\"0\" level_type=\"simple\" lines=\"8\" time=\"0\">\n" + "\t\t<language comment=\"Lighting, camera, and controls\" lang=\"en\" name=\"Tutorial 0\"/>\n" + "\t\t<language comment=\"Eclairage, manipulation camÃÂ©ra, et contrÃÂ´les\" lang=\"fr\" name=\"Didacticiel 0\"/>\n" + "\t\t<empty x=\"0\" y=\"0\"/>\n" + "\t\t<empty x=\"1\" y=\"0\"/>\n" + "\t\t<empty x=\"2\" y=\"0\"/>\n" + "\t\t<empty x=\"3\" y=\"0\"/>\n" + "\t\t<empty x=\"4\" y=\"0\"/>\n" + "\t\t<wall x=\"5\" y=\"0\"/>\n" + "\t\t<wall x=\"6\" y=\"0\"/>\n" + "\t\t<arrival x=\"7\" y=\"0\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"1\"/>\n" + "\t\t<wall x=\"1\" y=\"1\"/>\n" + "\t\t<empty x=\"2\" y=\"1\"/>\n" + "\t\t<wall x=\"3\" y=\"1\"/>\n" + "\t\t<wall x=\"4\" y=\"1\"/>\n" + "\t\t<empty x=\"5\" y=\"1\"/>\n" + "\t\t<empty x=\"6\" y=\"1\"/>\n" + "\t\t<empty x=\"7\" y=\"1\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"2\"/>\n" + "\t\t<wall x=\"1\" y=\"2\"/>\n" + "\t\t<empty x=\"2\" y=\"2\"/>\n" + "\t\t<empty x=\"3\" y=\"2\"/>\n" + "\t\t<empty x=\"4\" y=\"2\"/>\n" + "\t\t<empty x=\"5\" y=\"2\"/>\n" + "\t\t<wall x=\"6\" y=\"2\"/>\n" + "\t\t<empty x=\"7\" y=\"2\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"3\"/>\n" + "\t\t<wall x=\"1\" y=\"3\"/>\n" + "\t\t<empty x=\"2\" y=\"3\"/>\n" + "\t\t<wall x=\"3\" y=\"3\"/>\n" + "\t\t<wall x=\"4\" y=\"3\"/>\n" + "\t\t<empty x=\"5\" y=\"3\"/>\n" + "\t\t<wall x=\"6\" y=\"3\"/>\n" + "\t\t<empty x=\"7\" y=\"3\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"4\"/>\n" + "\t\t<wall x=\"1\" y=\"4\"/>\n" + "\t\t<empty x=\"2\" y=\"4\"/>\n" + "\t\t<empty x=\"3\" y=\"4\"/>\n" + "\t\t<empty x=\"4\" y=\"4\"/>\n" + "\t\t<empty x=\"5\" y=\"4\"/>\n" + "\t\t<wall x=\"6\" y=\"4\"/>\n" + "\t\t<empty x=\"7\" y=\"4\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"5\"/>\n" + "\t\t<wall x=\"1\" y=\"5\"/>\n" + "\t\t<empty x=\"2\" y=\"5\"/>\n" + "\t\t<wall x=\"3\" y=\"5\"/>\n" + "\t\t<wall x=\"4\" y=\"5\"/>\n" + "\t\t<empty x=\"5\" y=\"5\"/>\n" + "\t\t<wall x=\"6\" y=\"5\"/>\n" + "\t\t<empty x=\"7\" y=\"5\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"6\"/>\n" + "\t\t<wall x=\"1\" y=\"6\"/>\n" + "\t\t<empty x=\"2\" y=\"6\"/>\n" + "\t\t<empty x=\"3\" y=\"6\"/>\n" + "\t\t<empty x=\"4\" y=\"6\"/>\n" + "\t\t<empty x=\"5\" y=\"6\"/>\n" + "\t\t<wall x=\"6\" y=\"6\"/>\n" + "\t\t<empty x=\"7\" y=\"6\"/>\n" + "\n" + "\t\t<departure x=\"0\" y=\"7\">\n" + "\t\t\t<lantern lighting=\"-1\"/>\n" + "\t\t</departure>\n" + "\t\t<empty x=\"1\" y=\"7\"/>\n" + "\t\t<empty x=\"2\" y=\"7\"/>\n" + "\t\t<wall x=\"3\" y=\"7\"/>\n" + "\t\t<wall x=\"4\" y=\"7\"/>\n" + "\t\t<empty x=\"5\" y=\"7\"/>\n" + "\t\t<empty x=\"6\" y=\"7\"/>\n" + "\t\t<empty x=\"7\" y=\"7\"/>\n" + "\t</level>\n" + "\t<level author=\"Mazix\" columns=\"8\" id=\"1\" level_type=\"simple\" lines=\"8\" time=\"0\">\n" + "\t\t<language comment=\"Summary\" lang=\"en\" name=\"Tutorial 8\"/>\n" + "\t\t<language comment=\"RÃÂ©capitulatif\" lang=\"fr\" name=\"Didacticiel 8\"/>\n" + "\n" + "\t\t<arrival x=\"0\" y=\"0\"/>\n" + "\t\t<wall x=\"1\" y=\"0\"/>\n" + "\t\t<empty x=\"2\" y=\"0\"/>\n" + "\t\t<teleport color=\"PINK\" link_x=\"5\" link_y=\"0\" x=\"3\" y=\"0\"/>\n" + "\t\t<wall x=\"4\" y=\"0\"/>\n" + "\t\t<empty x=\"5\" y=\"0\"/>\n" + "\t\t<wall x=\"6\" y=\"0\"/>\n" + "\t\t<empty x=\"7\" y=\"0\">\n" + "\t\t\t<key color=\"SEMI_RED\"/>\n" + "\t\t</empty>\n" + "\n" + "\t\t<key_door color=\"SEMI_RED\" x=\"0\" y=\"1\"/>\n" + "\t\t<wall x=\"1\" y=\"1\"/>\n" + "\t\t<empty x=\"2\" y=\"1\"/>\n" + "\t\t<empty x=\"3\" y=\"1\"/>\n" + "\t\t<wall x=\"4\" y=\"1\"/>\n" + "\t\t<empty x=\"5\" y=\"1\"/>\n" + "\t\t<wall x=\"6\" y=\"1\"/>\n" + "\t\t<empty x=\"7\" y=\"1\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"2\"/>\n" + "\t\t<switch_door color=\"SEMI_BLUE\" x=\"1\" y=\"2\"/>\n" + "\t\t<key_door color=\"GAINS_BORO\" x=\"2\" y=\"2\"/>\n" + "\t\t<empty x=\"3\" y=\"2\"/>\n" + "\t\t<wall x=\"4\" y=\"2\"/>\n" + "\t\t<empty x=\"5\" y=\"2\"/>\n" + "\t\t<empty x=\"6\" y=\"2\"/>\n" + "\t\t<empty x=\"7\" y=\"2\"/>\n" + "\n" + "\t\t<wall x=\"0\" y=\"3\"/>\n" + "\t\t<wall x=\"1\" y=\"3\"/>\n" + "\t\t<empty x=\"2\" y=\"3\"/>\n" + "\t\t<wall x=\"3\" y=\"3\"/>\n" + "\t\t<wall x=\"4\" y=\"3\"/>\n" + "\t\t<empty x=\"5\" y=\"3\"/>\n" + "\t\t<empty x=\"6\" y=\"3\"/>\n" + "\t\t<switch_button color=\"SEMI_BLUE\" link_x=\"1\" link_y=\"2\" x=\"7\" y=\"3\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"4\"/>\n" + "\t\t<empty x=\"1\" y=\"4\">\n" + "\t\t\t<ball/>\n" + "\t\t</empty>\n" + "\t\t<empty x=\"2\" y=\"4\"/>\n" + "\t\t<wall x=\"3\" y=\"4\"/>\n" + "\t\t<empty x=\"4\" y=\"4\"/>\n" + "\t\t<empty x=\"5\" y=\"4\"/>\n" + "\t\t<wall x=\"6\" y=\"4\"/>\n" + "\t\t<wall x=\"7\" y=\"4\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"5\"/>\n" + "\t\t<wall x=\"1\" y=\"5\"/>\n" + "\t\t<empty x=\"2\" y=\"5\"/>\n" + "\t\t<wall x=\"3\" y=\"5\"/>\n" + "\t\t<switch_door color=\"SEMI_GREEN\" x=\"4\" y=\"5\"/>\n" + "\t\t<wall x=\"5\" y=\"5\"/>\n" + "\t\t<empty x=\"6\" y=\"5\">\n" + "\t\t\t<key color=\"SEMI_YELLOW\"/>\n" + "\t\t</empty>\n" + "\t\t<empty x=\"7\" y=\"5\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"6\"/>\n" + "\t\t<wall x=\"1\" y=\"6\"/>\n" + "\t\t<empty x=\"2\" y=\"6\">\n" + "\t\t\t<ball/>\n" + "\t\t</empty>\n" + "\t\t<wall x=\"3\" y=\"6\"/>\n" + "\t\t<empty x=\"4\" y=\"6\"/>\n" + "\t\t<empty x=\"5\" y=\"6\"/>\n" + "\t\t<wall x=\"6\" y=\"6\"/>\n" + "\t\t<empty x=\"7\" y=\"6\"/>\n" + "\n" + "\t\t<departure x=\"0\" y=\"7\">\n" + "\t\t\t<lantern lighting=\"-1\"/>\n" + "\t\t</departure>\n" + "\t\t<empty x=\"1\" y=\"7\"/>\n" + "\t\t<key_door color=\"SEMI_YELLOW\" x=\"2\" y=\"7\"/>\n" + "\t\t<switch_button color=\"SEMI_GREEN\" link_x=\"4\" link_y=\"5\" x=\"3\" y=\"7\"/>\n" + "\t\t<wall x=\"4\" y=\"7\"/>\n" + "\t\t<empty x=\"5\" y=\"7\"/>\n" + "\t\t<empty x=\"6\" y=\"7\"/>\n" + "\t\t<departure x=\"7\" y=\"7\">\n" + "\t\t\t<lantern lighting=\"-1\"/>\n" + "\t\t</departure>\n" + "\t</level>\n" + "</series>\n", xmlLevelSeriesContent);
    }

    /**
     * {@inheritDoc}
     * @since 0.8
     */
    @Override
    @Test
    public void testCreateOrUpdateKeyDoorSquareWithExistingSeriesWithExistingLevelWithNotExistingSquare() throws PersistenceException {
        XML_DOM_KEY_DOOR_SQUARE_DAO.createOrUpdateKeyDoorSquare(KEY_DOOR_SQUARE_DTO_4);
        final String xmlLevelSeriesContent = readFileIntoString(XML_DOM_KEY_DOOR_SQUARE_DAO.getFilePath() + KEY_DOOR_SQUARE_DTO_4.getLevelSeriesFileName() + XML_EXTENSION);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" + "<series xmlns:ns0=\"http://www.w3.org/2001/XMLSchema-instance\" ns0:noNamespaceSchemaLocation=\"levelSeries.xsd\">\n" + "\t<language comment=\"\" lang=\"en\" name=\"Tutorial\"/>\n" + "\t<language comment=\"\" lang=\"fr\" name=\"Didacticiel\"/>\n" + "\t<level author=\"Mazix\" columns=\"8\" id=\"0\" level_type=\"simple\" lines=\"8\" time=\"0\">\n" + "\t\t<language comment=\"Lighting, camera, and controls\" lang=\"en\" name=\"Tutorial 0\"/>\n" + "\t\t<language comment=\"Eclairage, manipulation camÃÂ©ra, et contrÃÂ´les\" lang=\"fr\" name=\"Didacticiel 0\"/>\n" + "\t\t<empty x=\"0\" y=\"0\"/>\n" + "\t\t<empty x=\"1\" y=\"0\"/>\n" + "\t\t<empty x=\"2\" y=\"0\"/>\n" + "\t\t<empty x=\"3\" y=\"0\"/>\n" + "\t\t<empty x=\"4\" y=\"0\"/>\n" + "\t\t<wall x=\"5\" y=\"0\"/>\n" + "\t\t<wall x=\"6\" y=\"0\"/>\n" + "\t\t<arrival x=\"7\" y=\"0\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"1\"/>\n" + "\t\t<wall x=\"1\" y=\"1\"/>\n" + "\t\t<empty x=\"2\" y=\"1\"/>\n" + "\t\t<wall x=\"3\" y=\"1\"/>\n" + "\t\t<wall x=\"4\" y=\"1\"/>\n" + "\t\t<empty x=\"5\" y=\"1\"/>\n" + "\t\t<empty x=\"6\" y=\"1\"/>\n" + "\t\t<empty x=\"7\" y=\"1\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"2\"/>\n" + "\t\t<wall x=\"1\" y=\"2\"/>\n" + "\t\t<empty x=\"2\" y=\"2\"/>\n" + "\t\t<empty x=\"3\" y=\"2\"/>\n" + "\t\t<empty x=\"4\" y=\"2\"/>\n" + "\t\t<empty x=\"5\" y=\"2\"/>\n" + "\t\t<wall x=\"6\" y=\"2\"/>\n" + "\t\t<empty x=\"7\" y=\"2\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"3\"/>\n" + "\t\t<wall x=\"1\" y=\"3\"/>\n" + "\t\t<empty x=\"2\" y=\"3\"/>\n" + "\t\t<wall x=\"3\" y=\"3\"/>\n" + "\t\t<wall x=\"4\" y=\"3\"/>\n" + "\t\t<empty x=\"5\" y=\"3\"/>\n" + "\t\t<wall x=\"6\" y=\"3\"/>\n" + "\t\t<empty x=\"7\" y=\"3\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"4\"/>\n" + "\t\t<wall x=\"1\" y=\"4\"/>\n" + "\t\t<empty x=\"2\" y=\"4\"/>\n" + "\t\t<empty x=\"3\" y=\"4\"/>\n" + "\t\t<empty x=\"4\" y=\"4\"/>\n" + "\t\t<empty x=\"5\" y=\"4\"/>\n" + "\t\t<wall x=\"6\" y=\"4\"/>\n" + "\t\t<empty x=\"7\" y=\"4\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"5\"/>\n" + "\t\t<wall x=\"1\" y=\"5\"/>\n" + "\t\t<empty x=\"2\" y=\"5\"/>\n" + "\t\t<wall x=\"3\" y=\"5\"/>\n" + "\t\t<wall x=\"4\" y=\"5\"/>\n" + "\t\t<empty x=\"5\" y=\"5\"/>\n" + "\t\t<wall x=\"6\" y=\"5\"/>\n" + "\t\t<empty x=\"7\" y=\"5\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"6\"/>\n" + "\t\t<wall x=\"1\" y=\"6\"/>\n" + "\t\t<empty x=\"2\" y=\"6\"/>\n" + "\t\t<empty x=\"3\" y=\"6\"/>\n" + "\t\t<empty x=\"4\" y=\"6\"/>\n" + "\t\t<empty x=\"5\" y=\"6\"/>\n" + "\t\t<wall x=\"6\" y=\"6\"/>\n" + "\t\t<empty x=\"7\" y=\"6\"/>\n" + "\n" + "\t\t<departure x=\"0\" y=\"7\">\n" + "\t\t\t<lantern lighting=\"-1\"/>\n" + "\t\t</departure>\n" + "\t\t<empty x=\"1\" y=\"7\"/>\n" + "\t\t<empty x=\"2\" y=\"7\"/>\n" + "\t\t<wall x=\"3\" y=\"7\"/>\n" + "\t\t<wall x=\"4\" y=\"7\"/>\n" + "\t\t<empty x=\"5\" y=\"7\"/>\n" + "\t\t<empty x=\"6\" y=\"7\"/>\n" + "\t\t<empty x=\"7\" y=\"7\"/>\n" + "\t</level>\n" + "\t<level author=\"Mazix\" columns=\"8\" id=\"1\" level_type=\"simple\" lines=\"8\" time=\"0\">\n" + "\t\t<language comment=\"Summary\" lang=\"en\" name=\"Tutorial 8\"/>\n" + "\t\t<language comment=\"RÃÂ©capitulatif\" lang=\"fr\" name=\"Didacticiel 8\"/>\n" + "\n" + "\t\t<arrival x=\"0\" y=\"0\"/>\n" + "\t\t<wall x=\"1\" y=\"0\"/>\n" + "\t\t<empty x=\"2\" y=\"0\"/>\n" + "\t\t<teleport color=\"PINK\" link_x=\"5\" link_y=\"0\" x=\"3\" y=\"0\"/>\n" + "\t\t<wall x=\"4\" y=\"0\"/>\n" + "\t\t<empty x=\"5\" y=\"0\"/>\n" + "\t\t<wall x=\"6\" y=\"0\"/>\n" + "\t\t<empty x=\"7\" y=\"0\">\n" + "\t\t\t<key color=\"SEMI_RED\"/>\n" + "\t\t</empty>\n" + "\n" + "\t\t<key_door color=\"SEMI_RED\" x=\"0\" y=\"1\"/>\n" + "\t\t<wall x=\"1\" y=\"1\"/>\n" + "\t\t<empty x=\"2\" y=\"1\"/>\n" + "\t\t<empty x=\"3\" y=\"1\"/>\n" + "\t\t<wall x=\"4\" y=\"1\"/>\n" + "\t\t<empty x=\"5\" y=\"1\"/>\n" + "\t\t<wall x=\"6\" y=\"1\"/>\n" + "\t\t<empty x=\"7\" y=\"1\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"2\"/>\n" + "\t\t<switch_door color=\"SEMI_BLUE\" x=\"1\" y=\"2\"/>\n" + "\t\t<empty x=\"2\" y=\"2\"/>\n" + "\t\t<empty x=\"3\" y=\"2\"/>\n" + "\t\t<wall x=\"4\" y=\"2\"/>\n" + "\t\t<empty x=\"5\" y=\"2\"/>\n" + "\t\t<empty x=\"6\" y=\"2\"/>\n" + "\t\t<empty x=\"7\" y=\"2\"/>\n" + "\n" + "\t\t<wall x=\"0\" y=\"3\"/>\n" + "\t\t<wall x=\"1\" y=\"3\"/>\n" + "\t\t<empty x=\"2\" y=\"3\"/>\n" + "\t\t<wall x=\"3\" y=\"3\"/>\n" + "\t\t<wall x=\"4\" y=\"3\"/>\n" + "\t\t<empty x=\"5\" y=\"3\"/>\n" + "\t\t<empty x=\"6\" y=\"3\"/>\n" + "\t\t<switch_button color=\"SEMI_BLUE\" link_x=\"1\" link_y=\"2\" x=\"7\" y=\"3\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"4\"/>\n" + "\t\t<empty x=\"1\" y=\"4\">\n" + "\t\t\t<ball/>\n" + "\t\t</empty>\n" + "\t\t<empty x=\"2\" y=\"4\"/>\n" + "\t\t<wall x=\"3\" y=\"4\"/>\n" + "\t\t<empty x=\"4\" y=\"4\"/>\n" + "\t\t<empty x=\"5\" y=\"4\"/>\n" + "\t\t<wall x=\"6\" y=\"4\"/>\n" + "\t\t<wall x=\"7\" y=\"4\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"5\"/>\n" + "\t\t<wall x=\"1\" y=\"5\"/>\n" + "\t\t<empty x=\"2\" y=\"5\"/>\n" + "\t\t<wall x=\"3\" y=\"5\"/>\n" + "\t\t<switch_door color=\"SEMI_GREEN\" x=\"4\" y=\"5\"/>\n" + "\t\t<wall x=\"5\" y=\"5\"/>\n" + "\t\t<empty x=\"6\" y=\"5\">\n" + "\t\t\t<key color=\"SEMI_YELLOW\"/>\n" + "\t\t</empty>\n" + "\t\t<empty x=\"7\" y=\"5\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"6\"/>\n" + "\t\t<wall x=\"1\" y=\"6\"/>\n" + "\t\t<empty x=\"2\" y=\"6\">\n" + "\t\t\t<ball/>\n" + "\t\t</empty>\n" + "\t\t<wall x=\"3\" y=\"6\"/>\n" + "\t\t<empty x=\"4\" y=\"6\"/>\n" + "\t\t<empty x=\"5\" y=\"6\"/>\n" + "\t\t<wall x=\"6\" y=\"6\"/>\n" + "\t\t<empty x=\"7\" y=\"6\"/>\n" + "\n" + "\t\t<departure x=\"0\" y=\"7\">\n" + "\t\t\t<lantern lighting=\"-1\"/>\n" + "\t\t</departure>\n" + "\t\t<empty x=\"1\" y=\"7\"/>\n" + "\t\t<key_door color=\"SEMI_YELLOW\" x=\"2\" y=\"7\"/>\n" + "\t\t<switch_button color=\"SEMI_GREEN\" link_x=\"4\" link_y=\"5\" x=\"3\" y=\"7\"/>\n" + "\t\t<wall x=\"4\" y=\"7\"/>\n" + "\t\t<empty x=\"5\" y=\"7\"/>\n" + "\t\t<empty x=\"6\" y=\"7\"/>\n" + "\t\t<departure x=\"7\" y=\"7\">\n" + "\t\t\t<lantern lighting=\"-1\"/>\n" + "\t\t</departure>\n" + "\t<key_door color=\"GAINS_BORO\" x=\"50\" y=\"50\"/>\n" + "</level>\n" + "</series>\n", xmlLevelSeriesContent);
    }

    /**
     * {@inheritDoc}
     * @since 0.8
     */
    @Override
    @Test(expected = PersistenceException.class)
    public void testCreateOrUpdateKeyDoorSquareWithExistingSeriesWithNotExistingLevel() throws PersistenceException {
        XML_DOM_KEY_DOOR_SQUARE_DAO.createOrUpdateKeyDoorSquare(KEY_DOOR_SQUARE_DTO_3);
    }

    /**
     * {@inheritDoc}
     * @since 0.8
     */
    @Override
    @Test(expected = PersistenceException.class)
    public void testCreateOrUpdateKeyDoorSquareWithNotExistingSeries() throws PersistenceException {
        XML_DOM_KEY_DOOR_SQUARE_DAO.createOrUpdateKeyDoorSquare(KEY_DOOR_SQUARE_DTO_2);
    }

    /**
     * {@inheritDoc}
     * @since 0.8
     */
    @Override
    @Test
    public void testDeleteAllKeyDoorSquaresWithExistingSeriesWithExistingLevel() throws PersistenceException {
        XML_DOM_KEY_DOOR_SQUARE_DAO.deleteAllKeyDoorSquares(LEVEL_DTO_5);
        final String xmlLevelSeriesContent = readFileIntoString(XML_DOM_KEY_DOOR_SQUARE_DAO.getFilePath() + LEVEL_DTO_5.getLevelSeriesFileName() + XML_EXTENSION);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" + "<series xmlns:ns0=\"http://www.w3.org/2001/XMLSchema-instance\" ns0:noNamespaceSchemaLocation=\"levelSeries.xsd\">\n" + "\t<language comment=\"\" lang=\"en\" name=\"Tutorial\"/>\n" + "\t<language comment=\"\" lang=\"fr\" name=\"Didacticiel\"/>\n" + "\t<level author=\"Mazix\" columns=\"8\" id=\"0\" level_type=\"simple\" lines=\"8\" time=\"0\">\n" + "\t\t<language comment=\"Lighting, camera, and controls\" lang=\"en\" name=\"Tutorial 0\"/>\n" + "\t\t<language comment=\"Eclairage, manipulation camÃÂ©ra, et contrÃÂ´les\" lang=\"fr\" name=\"Didacticiel 0\"/>\n" + "\t\t<empty x=\"0\" y=\"0\"/>\n" + "\t\t<empty x=\"1\" y=\"0\"/>\n" + "\t\t<empty x=\"2\" y=\"0\"/>\n" + "\t\t<empty x=\"3\" y=\"0\"/>\n" + "\t\t<empty x=\"4\" y=\"0\"/>\n" + "\t\t<wall x=\"5\" y=\"0\"/>\n" + "\t\t<wall x=\"6\" y=\"0\"/>\n" + "\t\t<arrival x=\"7\" y=\"0\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"1\"/>\n" + "\t\t<wall x=\"1\" y=\"1\"/>\n" + "\t\t<empty x=\"2\" y=\"1\"/>\n" + "\t\t<wall x=\"3\" y=\"1\"/>\n" + "\t\t<wall x=\"4\" y=\"1\"/>\n" + "\t\t<empty x=\"5\" y=\"1\"/>\n" + "\t\t<empty x=\"6\" y=\"1\"/>\n" + "\t\t<empty x=\"7\" y=\"1\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"2\"/>\n" + "\t\t<wall x=\"1\" y=\"2\"/>\n" + "\t\t<empty x=\"2\" y=\"2\"/>\n" + "\t\t<empty x=\"3\" y=\"2\"/>\n" + "\t\t<empty x=\"4\" y=\"2\"/>\n" + "\t\t<empty x=\"5\" y=\"2\"/>\n" + "\t\t<wall x=\"6\" y=\"2\"/>\n" + "\t\t<empty x=\"7\" y=\"2\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"3\"/>\n" + "\t\t<wall x=\"1\" y=\"3\"/>\n" + "\t\t<empty x=\"2\" y=\"3\"/>\n" + "\t\t<wall x=\"3\" y=\"3\"/>\n" + "\t\t<wall x=\"4\" y=\"3\"/>\n" + "\t\t<empty x=\"5\" y=\"3\"/>\n" + "\t\t<wall x=\"6\" y=\"3\"/>\n" + "\t\t<empty x=\"7\" y=\"3\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"4\"/>\n" + "\t\t<wall x=\"1\" y=\"4\"/>\n" + "\t\t<empty x=\"2\" y=\"4\"/>\n" + "\t\t<empty x=\"3\" y=\"4\"/>\n" + "\t\t<empty x=\"4\" y=\"4\"/>\n" + "\t\t<empty x=\"5\" y=\"4\"/>\n" + "\t\t<wall x=\"6\" y=\"4\"/>\n" + "\t\t<empty x=\"7\" y=\"4\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"5\"/>\n" + "\t\t<wall x=\"1\" y=\"5\"/>\n" + "\t\t<empty x=\"2\" y=\"5\"/>\n" + "\t\t<wall x=\"3\" y=\"5\"/>\n" + "\t\t<wall x=\"4\" y=\"5\"/>\n" + "\t\t<empty x=\"5\" y=\"5\"/>\n" + "\t\t<wall x=\"6\" y=\"5\"/>\n" + "\t\t<empty x=\"7\" y=\"5\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"6\"/>\n" + "\t\t<wall x=\"1\" y=\"6\"/>\n" + "\t\t<empty x=\"2\" y=\"6\"/>\n" + "\t\t<empty x=\"3\" y=\"6\"/>\n" + "\t\t<empty x=\"4\" y=\"6\"/>\n" + "\t\t<empty x=\"5\" y=\"6\"/>\n" + "\t\t<wall x=\"6\" y=\"6\"/>\n" + "\t\t<empty x=\"7\" y=\"6\"/>\n" + "\n" + "\t\t<departure x=\"0\" y=\"7\">\n" + "\t\t\t<lantern lighting=\"-1\"/>\n" + "\t\t</departure>\n" + "\t\t<empty x=\"1\" y=\"7\"/>\n" + "\t\t<empty x=\"2\" y=\"7\"/>\n" + "\t\t<wall x=\"3\" y=\"7\"/>\n" + "\t\t<wall x=\"4\" y=\"7\"/>\n" + "\t\t<empty x=\"5\" y=\"7\"/>\n" + "\t\t<empty x=\"6\" y=\"7\"/>\n" + "\t\t<empty x=\"7\" y=\"7\"/>\n" + "\t</level>\n" + "\t<level author=\"Mazix\" columns=\"8\" id=\"1\" level_type=\"simple\" lines=\"8\" time=\"0\">\n" + "\t\t<language comment=\"Summary\" lang=\"en\" name=\"Tutorial 8\"/>\n" + "\t\t<language comment=\"RÃÂ©capitulatif\" lang=\"fr\" name=\"Didacticiel 8\"/>\n" + "\n" + "\t\t<arrival x=\"0\" y=\"0\"/>\n" + "\t\t<wall x=\"1\" y=\"0\"/>\n" + "\t\t<empty x=\"2\" y=\"0\"/>\n" + "\t\t<teleport color=\"PINK\" link_x=\"5\" link_y=\"0\" x=\"3\" y=\"0\"/>\n" + "\t\t<wall x=\"4\" y=\"0\"/>\n" + "\t\t<empty x=\"5\" y=\"0\"/>\n" + "\t\t<wall x=\"6\" y=\"0\"/>\n" + "\t\t<empty x=\"7\" y=\"0\">\n" + "\t\t\t<key color=\"SEMI_RED\"/>\n" + "\t\t</empty>\n" + "\n" + "\t\t\n" + "\t\t<wall x=\"1\" y=\"1\"/>\n" + "\t\t<empty x=\"2\" y=\"1\"/>\n" + "\t\t<empty x=\"3\" y=\"1\"/>\n" + "\t\t<wall x=\"4\" y=\"1\"/>\n" + "\t\t<empty x=\"5\" y=\"1\"/>\n" + "\t\t<wall x=\"6\" y=\"1\"/>\n" + "\t\t<empty x=\"7\" y=\"1\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"2\"/>\n" + "\t\t<switch_door color=\"SEMI_BLUE\" x=\"1\" y=\"2\"/>\n" + "\t\t<empty x=\"2\" y=\"2\"/>\n" + "\t\t<empty x=\"3\" y=\"2\"/>\n" + "\t\t<wall x=\"4\" y=\"2\"/>\n" + "\t\t<empty x=\"5\" y=\"2\"/>\n" + "\t\t<empty x=\"6\" y=\"2\"/>\n" + "\t\t<empty x=\"7\" y=\"2\"/>\n" + "\n" + "\t\t<wall x=\"0\" y=\"3\"/>\n" + "\t\t<wall x=\"1\" y=\"3\"/>\n" + "\t\t<empty x=\"2\" y=\"3\"/>\n" + "\t\t<wall x=\"3\" y=\"3\"/>\n" + "\t\t<wall x=\"4\" y=\"3\"/>\n" + "\t\t<empty x=\"5\" y=\"3\"/>\n" + "\t\t<empty x=\"6\" y=\"3\"/>\n" + "\t\t<switch_button color=\"SEMI_BLUE\" link_x=\"1\" link_y=\"2\" x=\"7\" y=\"3\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"4\"/>\n" + "\t\t<empty x=\"1\" y=\"4\">\n" + "\t\t\t<ball/>\n" + "\t\t</empty>\n" + "\t\t<empty x=\"2\" y=\"4\"/>\n" + "\t\t<wall x=\"3\" y=\"4\"/>\n" + "\t\t<empty x=\"4\" y=\"4\"/>\n" + "\t\t<empty x=\"5\" y=\"4\"/>\n" + "\t\t<wall x=\"6\" y=\"4\"/>\n" + "\t\t<wall x=\"7\" y=\"4\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"5\"/>\n" + "\t\t<wall x=\"1\" y=\"5\"/>\n" + "\t\t<empty x=\"2\" y=\"5\"/>\n" + "\t\t<wall x=\"3\" y=\"5\"/>\n" + "\t\t<switch_door color=\"SEMI_GREEN\" x=\"4\" y=\"5\"/>\n" + "\t\t<wall x=\"5\" y=\"5\"/>\n" + "\t\t<empty x=\"6\" y=\"5\">\n" + "\t\t\t<key color=\"SEMI_YELLOW\"/>\n" + "\t\t</empty>\n" + "\t\t<empty x=\"7\" y=\"5\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"6\"/>\n" + "\t\t<wall x=\"1\" y=\"6\"/>\n" + "\t\t<empty x=\"2\" y=\"6\">\n" + "\t\t\t<ball/>\n" + "\t\t</empty>\n" + "\t\t<wall x=\"3\" y=\"6\"/>\n" + "\t\t<empty x=\"4\" y=\"6\"/>\n" + "\t\t<empty x=\"5\" y=\"6\"/>\n" + "\t\t<wall x=\"6\" y=\"6\"/>\n" + "\t\t<empty x=\"7\" y=\"6\"/>\n" + "\n" + "\t\t<departure x=\"0\" y=\"7\">\n" + "\t\t\t<lantern lighting=\"-1\"/>\n" + "\t\t</departure>\n" + "\t\t<empty x=\"1\" y=\"7\"/>\n" + "\t\t\n" + "\t\t<switch_button color=\"SEMI_GREEN\" link_x=\"4\" link_y=\"5\" x=\"3\" y=\"7\"/>\n" + "\t\t<wall x=\"4\" y=\"7\"/>\n" + "\t\t<empty x=\"5\" y=\"7\"/>\n" + "\t\t<empty x=\"6\" y=\"7\"/>\n" + "\t\t<departure x=\"7\" y=\"7\">\n" + "\t\t\t<lantern lighting=\"-1\"/>\n" + "\t\t</departure>\n" + "\t</level>\n" + "</series>\n", xmlLevelSeriesContent);
    }

    /**
     * {@inheritDoc}
     * @since 0.8
     */
    @Override
    @Test(expected = PersistenceException.class)
    public void testDeleteAllKeyDoorSquaresWithExistingSeriesWithNotExistingLevel() throws PersistenceException {
        XML_DOM_KEY_DOOR_SQUARE_DAO.deleteAllKeyDoorSquares(LEVEL_DTO_4);
    }

    /**
     * {@inheritDoc}
     * @since 0.8
     */
    @Override
    @Test(expected = PersistenceException.class)
    public void testDeleteAllKeyDoorSquaresWithNotExistingSeries() throws PersistenceException {
        XML_DOM_KEY_DOOR_SQUARE_DAO.deleteAllKeyDoorSquares(LEVEL_DTO_1);
    }

    /**
     * {@inheritDoc}
     * @since 0.8
     */
    @Override
    @Test(expected = PersistenceException.class)
    public void testDeleteKeyDoorSquareWithExistingSeriesWithExistingLevelWithExistingEmptySquare() throws PersistenceException {
        XML_DOM_KEY_DOOR_SQUARE_DAO.deleteKeyDoorSquare(KEY_DOOR_SQUARE_DTO_6);
    }

    /**
     * {@inheritDoc}
     * @since 0.8
     */
    @Override
    @Test
    public void testDeleteKeyDoorSquareWithExistingSeriesWithExistingLevelWithExistingKeyDoorSquare() throws PersistenceException {
        XML_DOM_KEY_DOOR_SQUARE_DAO.deleteKeyDoorSquare(KEY_DOOR_SQUARE_DTO_5);
        final String xmlLevelSeriesContent = readFileIntoString(XML_DOM_KEY_DOOR_SQUARE_DAO.getFilePath() + KEY_DOOR_SQUARE_DTO_5.getLevelSeriesFileName() + XML_EXTENSION);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" + "<series xmlns:ns0=\"http://www.w3.org/2001/XMLSchema-instance\" ns0:noNamespaceSchemaLocation=\"levelSeries.xsd\">\n" + "\t<language comment=\"\" lang=\"en\" name=\"Tutorial\"/>\n" + "\t<language comment=\"\" lang=\"fr\" name=\"Didacticiel\"/>\n" + "\t<level author=\"Mazix\" columns=\"8\" id=\"0\" level_type=\"simple\" lines=\"8\" time=\"0\">\n" + "\t\t<language comment=\"Lighting, camera, and controls\" lang=\"en\" name=\"Tutorial 0\"/>\n" + "\t\t<language comment=\"Eclairage, manipulation camÃÂ©ra, et contrÃÂ´les\" lang=\"fr\" name=\"Didacticiel 0\"/>\n" + "\t\t<empty x=\"0\" y=\"0\"/>\n" + "\t\t<empty x=\"1\" y=\"0\"/>\n" + "\t\t<empty x=\"2\" y=\"0\"/>\n" + "\t\t<empty x=\"3\" y=\"0\"/>\n" + "\t\t<empty x=\"4\" y=\"0\"/>\n" + "\t\t<wall x=\"5\" y=\"0\"/>\n" + "\t\t<wall x=\"6\" y=\"0\"/>\n" + "\t\t<arrival x=\"7\" y=\"0\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"1\"/>\n" + "\t\t<wall x=\"1\" y=\"1\"/>\n" + "\t\t<empty x=\"2\" y=\"1\"/>\n" + "\t\t<wall x=\"3\" y=\"1\"/>\n" + "\t\t<wall x=\"4\" y=\"1\"/>\n" + "\t\t<empty x=\"5\" y=\"1\"/>\n" + "\t\t<empty x=\"6\" y=\"1\"/>\n" + "\t\t<empty x=\"7\" y=\"1\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"2\"/>\n" + "\t\t<wall x=\"1\" y=\"2\"/>\n" + "\t\t<empty x=\"2\" y=\"2\"/>\n" + "\t\t<empty x=\"3\" y=\"2\"/>\n" + "\t\t<empty x=\"4\" y=\"2\"/>\n" + "\t\t<empty x=\"5\" y=\"2\"/>\n" + "\t\t<wall x=\"6\" y=\"2\"/>\n" + "\t\t<empty x=\"7\" y=\"2\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"3\"/>\n" + "\t\t<wall x=\"1\" y=\"3\"/>\n" + "\t\t<empty x=\"2\" y=\"3\"/>\n" + "\t\t<wall x=\"3\" y=\"3\"/>\n" + "\t\t<wall x=\"4\" y=\"3\"/>\n" + "\t\t<empty x=\"5\" y=\"3\"/>\n" + "\t\t<wall x=\"6\" y=\"3\"/>\n" + "\t\t<empty x=\"7\" y=\"3\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"4\"/>\n" + "\t\t<wall x=\"1\" y=\"4\"/>\n" + "\t\t<empty x=\"2\" y=\"4\"/>\n" + "\t\t<empty x=\"3\" y=\"4\"/>\n" + "\t\t<empty x=\"4\" y=\"4\"/>\n" + "\t\t<empty x=\"5\" y=\"4\"/>\n" + "\t\t<wall x=\"6\" y=\"4\"/>\n" + "\t\t<empty x=\"7\" y=\"4\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"5\"/>\n" + "\t\t<wall x=\"1\" y=\"5\"/>\n" + "\t\t<empty x=\"2\" y=\"5\"/>\n" + "\t\t<wall x=\"3\" y=\"5\"/>\n" + "\t\t<wall x=\"4\" y=\"5\"/>\n" + "\t\t<empty x=\"5\" y=\"5\"/>\n" + "\t\t<wall x=\"6\" y=\"5\"/>\n" + "\t\t<empty x=\"7\" y=\"5\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"6\"/>\n" + "\t\t<wall x=\"1\" y=\"6\"/>\n" + "\t\t<empty x=\"2\" y=\"6\"/>\n" + "\t\t<empty x=\"3\" y=\"6\"/>\n" + "\t\t<empty x=\"4\" y=\"6\"/>\n" + "\t\t<empty x=\"5\" y=\"6\"/>\n" + "\t\t<wall x=\"6\" y=\"6\"/>\n" + "\t\t<empty x=\"7\" y=\"6\"/>\n" + "\n" + "\t\t<departure x=\"0\" y=\"7\">\n" + "\t\t\t<lantern lighting=\"-1\"/>\n" + "\t\t</departure>\n" + "\t\t<empty x=\"1\" y=\"7\"/>\n" + "\t\t<empty x=\"2\" y=\"7\"/>\n" + "\t\t<wall x=\"3\" y=\"7\"/>\n" + "\t\t<wall x=\"4\" y=\"7\"/>\n" + "\t\t<empty x=\"5\" y=\"7\"/>\n" + "\t\t<empty x=\"6\" y=\"7\"/>\n" + "\t\t<empty x=\"7\" y=\"7\"/>\n" + "\t</level>\n" + "\t<level author=\"Mazix\" columns=\"8\" id=\"1\" level_type=\"simple\" lines=\"8\" time=\"0\">\n" + "\t\t<language comment=\"Summary\" lang=\"en\" name=\"Tutorial 8\"/>\n" + "\t\t<language comment=\"RÃÂ©capitulatif\" lang=\"fr\" name=\"Didacticiel 8\"/>\n" + "\n" + "\t\t<arrival x=\"0\" y=\"0\"/>\n" + "\t\t<wall x=\"1\" y=\"0\"/>\n" + "\t\t<empty x=\"2\" y=\"0\"/>\n" + "\t\t<teleport color=\"PINK\" link_x=\"5\" link_y=\"0\" x=\"3\" y=\"0\"/>\n" + "\t\t<wall x=\"4\" y=\"0\"/>\n" + "\t\t<empty x=\"5\" y=\"0\"/>\n" + "\t\t<wall x=\"6\" y=\"0\"/>\n" + "\t\t<empty x=\"7\" y=\"0\">\n" + "\t\t\t<key color=\"SEMI_RED\"/>\n" + "\t\t</empty>\n" + "\n" + "\t\t<key_door color=\"SEMI_RED\" x=\"0\" y=\"1\"/>\n" + "\t\t<wall x=\"1\" y=\"1\"/>\n" + "\t\t<empty x=\"2\" y=\"1\"/>\n" + "\t\t<empty x=\"3\" y=\"1\"/>\n" + "\t\t<wall x=\"4\" y=\"1\"/>\n" + "\t\t<empty x=\"5\" y=\"1\"/>\n" + "\t\t<wall x=\"6\" y=\"1\"/>\n" + "\t\t<empty x=\"7\" y=\"1\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"2\"/>\n" + "\t\t<switch_door color=\"SEMI_BLUE\" x=\"1\" y=\"2\"/>\n" + "\t\t<empty x=\"2\" y=\"2\"/>\n" + "\t\t<empty x=\"3\" y=\"2\"/>\n" + "\t\t<wall x=\"4\" y=\"2\"/>\n" + "\t\t<empty x=\"5\" y=\"2\"/>\n" + "\t\t<empty x=\"6\" y=\"2\"/>\n" + "\t\t<empty x=\"7\" y=\"2\"/>\n" + "\n" + "\t\t<wall x=\"0\" y=\"3\"/>\n" + "\t\t<wall x=\"1\" y=\"3\"/>\n" + "\t\t<empty x=\"2\" y=\"3\"/>\n" + "\t\t<wall x=\"3\" y=\"3\"/>\n" + "\t\t<wall x=\"4\" y=\"3\"/>\n" + "\t\t<empty x=\"5\" y=\"3\"/>\n" + "\t\t<empty x=\"6\" y=\"3\"/>\n" + "\t\t<switch_button color=\"SEMI_BLUE\" link_x=\"1\" link_y=\"2\" x=\"7\" y=\"3\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"4\"/>\n" + "\t\t<empty x=\"1\" y=\"4\">\n" + "\t\t\t<ball/>\n" + "\t\t</empty>\n" + "\t\t<empty x=\"2\" y=\"4\"/>\n" + "\t\t<wall x=\"3\" y=\"4\"/>\n" + "\t\t<empty x=\"4\" y=\"4\"/>\n" + "\t\t<empty x=\"5\" y=\"4\"/>\n" + "\t\t<wall x=\"6\" y=\"4\"/>\n" + "\t\t<wall x=\"7\" y=\"4\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"5\"/>\n" + "\t\t<wall x=\"1\" y=\"5\"/>\n" + "\t\t<empty x=\"2\" y=\"5\"/>\n" + "\t\t<wall x=\"3\" y=\"5\"/>\n" + "\t\t<switch_door color=\"SEMI_GREEN\" x=\"4\" y=\"5\"/>\n" + "\t\t<wall x=\"5\" y=\"5\"/>\n" + "\t\t<empty x=\"6\" y=\"5\">\n" + "\t\t\t<key color=\"SEMI_YELLOW\"/>\n" + "\t\t</empty>\n" + "\t\t<empty x=\"7\" y=\"5\"/>\n" + "\n" + "\t\t<empty x=\"0\" y=\"6\"/>\n" + "\t\t<wall x=\"1\" y=\"6\"/>\n" + "\t\t<empty x=\"2\" y=\"6\">\n" + "\t\t\t<ball/>\n" + "\t\t</empty>\n" + "\t\t<wall x=\"3\" y=\"6\"/>\n" + "\t\t<empty x=\"4\" y=\"6\"/>\n" + "\t\t<empty x=\"5\" y=\"6\"/>\n" + "\t\t<wall x=\"6\" y=\"6\"/>\n" + "\t\t<empty x=\"7\" y=\"6\"/>\n" + "\n" + "\t\t<departure x=\"0\" y=\"7\">\n" + "\t\t\t<lantern lighting=\"-1\"/>\n" + "\t\t</departure>\n" + "\t\t<empty x=\"1\" y=\"7\"/>\n" + "\t\t\n" + "\t\t<switch_button color=\"SEMI_GREEN\" link_x=\"4\" link_y=\"5\" x=\"3\" y=\"7\"/>\n" + "\t\t<wall x=\"4\" y=\"7\"/>\n" + "\t\t<empty x=\"5\" y=\"7\"/>\n" + "\t\t<empty x=\"6\" y=\"7\"/>\n" + "\t\t<departure x=\"7\" y=\"7\">\n" + "\t\t\t<lantern lighting=\"-1\"/>\n" + "\t\t</departure>\n" + "\t</level>\n" + "</series>\n", xmlLevelSeriesContent);
    }

    /**
     * {@inheritDoc}
     * @since 0.8
     */
    @Override
    @Test
    public void testFindAllKeyDoorSquaresWithExistingSeriesWithExistingLevel() throws PersistenceException {
        final Set<KeyDoorSquareDTO> squaresSet = XML_DOM_KEY_DOOR_SQUARE_DAO.findAllKeyDoorSquares(LEVEL_DTO_5);
        assertEquals(2, squaresSet.size());
        assertTrue(squaresSet.contains(KEY_DOOR_SQUARE_DTO_5));
        assertTrue(squaresSet.contains(KEY_DOOR_SQUARE_DTO_7));
    }

    /**
     * {@inheritDoc}
     * @since 0.8
     */
    @Override
    @Test(expected = PersistenceException.class)
    public void testFindAllKeyDoorSquaresWithExistingSeriesWithNotExistingLevel() throws PersistenceException {
        XML_DOM_KEY_DOOR_SQUARE_DAO.findAllKeyDoorSquares(LEVEL_DTO_4);
    }

    /**
     * {@inheritDoc}
     * @since 0.8
     */
    @Override
    @Test(expected = PersistenceException.class)
    public void testFindAllKeyDoorSquaresWithNotExistingSeries() throws PersistenceException {
        XML_DOM_KEY_DOOR_SQUARE_DAO.findAllKeyDoorSquares(LEVEL_DTO_1);
    }

    /**
     * {@inheritDoc}
     * @since 0.8
     */
    @Override
    @Test
    public void testFindKeyDoorSquareWithExistingSeriesWithExistingLevelWithExistingSquare() throws PersistenceException {
        final KeyDoorSquareDTO squareDTO = XML_DOM_KEY_DOOR_SQUARE_DAO.findKeyDoorSquare(LEVEL_DTO_5, 2, 7);
        assertEquals(KEY_DOOR_SQUARE_DTO_5, squareDTO);
    }

    /**
     * {@inheritDoc}
     * @since 0.8
     */
    @Override
    @Test
    public void testFindKeyDoorSquareWithExistingSeriesWithExistingLevelWithNotExistingSquare() throws PersistenceException {
        final KeyDoorSquareDTO squareDTO = XML_DOM_KEY_DOOR_SQUARE_DAO.findKeyDoorSquare(LEVEL_DTO_5, 5, 5);
        assertNull(squareDTO);
    }

    /**
     * {@inheritDoc}
     * @since 0.8
     */
    @Override
    @Test(expected = PersistenceException.class)
    public void testFindKeyDoorSquareWithExistingSeriesWithNotExistingLevel() throws PersistenceException {
        XML_DOM_KEY_DOOR_SQUARE_DAO.findKeyDoorSquare(LEVEL_DTO_4, 0, 0);
    }

    /**
     * {@inheritDoc}
     * @since 0.8
     */
    @Override
    @Test(expected = PersistenceException.class)
    public void testFindKeyDoorSquareWithNotExistingSeries() throws PersistenceException {
        XML_DOM_KEY_DOOR_SQUARE_DAO.findKeyDoorSquare(LEVEL_DTO_1, 0, 0);
    }
}
