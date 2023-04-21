package net.sourceforge.mazix.persistence.dao.impl.xml.jaxb.profiles;

import static net.sourceforge.mazix.components.constants.CommonFileConstants.XSD_EXTENSION;
import static net.sourceforge.mazix.components.utils.file.FileUtils.readFileIntoString;
import static net.sourceforge.mazix.components.utils.file.FileUtils.writeIntoFile;
import static net.sourceforge.mazix.persistence.constants.FileConstants.PROFILES_FILE;
import static net.sourceforge.mazix.persistence.constants.FileConstants.PROFILES_PATH;
import static net.sourceforge.mazix.persistence.constants.LevelTestConstants.LEVEL_SERIES_DTO_3;
import static net.sourceforge.mazix.persistence.constants.LevelTestConstants.LEVEL_SERIES_DTO_4;
import static net.sourceforge.mazix.persistence.constants.ProfileTestConstants.PROFILES_TEST_FILE;
import static net.sourceforge.mazix.persistence.constants.ProfileTestConstants.PROFILE_DTO_1;
import static net.sourceforge.mazix.persistence.constants.ProfileTestConstants.PROFILE_DTO_3;
import static net.sourceforge.mazix.persistence.constants.ProfileTestConstants.PROFILE_DTO_4;
import static net.sourceforge.mazix.persistence.constants.ProfileTestConstants.PROGRESSION_DTO_1;
import static net.sourceforge.mazix.persistence.constants.ProfileTestConstants.PROGRESSION_DTO_2;
import static net.sourceforge.mazix.persistence.constants.ProfileTestConstants.PROGRESSION_DTO_3;
import static net.sourceforge.mazix.persistence.constants.ProfileTestConstants.PROGRESSION_DTO_4;
import static net.sourceforge.mazix.persistence.constants.ProfileTestConstants.PROGRESSION_DTO_5;
import static net.sourceforge.mazix.persistence.constants.ProfileTestConstants.PROGRESSION_DTO_6;
import static net.sourceforge.mazix.persistence.constants.ProfileTestConstants.PROGRESSION_DTO_7;
import static net.sourceforge.mazix.persistence.constants.ProfileTestConstants.PROGRESSION_DTO_8;
import static net.sourceforge.mazix.persistence.constants.ProfileTestConstants.PROGRESSION_DTO_9;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.util.Set;
import net.sourceforge.mazix.components.exception.PersistenceException;
import net.sourceforge.mazix.persistence.dao.profiles.ProgressionDAOTest;
import net.sourceforge.mazix.persistence.dto.profiles.ProgressionDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test classes for {@link XMLJAXBProgressionDAO}.
 * 
 * @author Benjamin Croizet (<a href="mailto:graffity2199@yahoo.fr>graffity2199@yahoo.fr</a>)
 * 
 * @since 0.8
 * @version 0.8
 */
public class XMLJAXBProgressionDAOTest implements ProgressionDAOTest {

    /** The {@link XMLJAXBProgressionDAO} test instance. */
    private static final XMLJAXBProgressionDAO XML_JAXB_PROGRESSION_DAO = new XMLJAXBProgressionDAO(PROFILES_PATH, PROFILES_TEST_FILE, PROFILES_FILE + XSD_EXTENSION);

    /**
     * This method is called before each test to reset the profiles temporary XML file content.
     * 
     * @since 0.8
     */
    @Before
    public void initializeXMLFile() {
        writeIntoFile("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" + "<profiles xmlns:ns0=\"http://www.w3.org/2001/XMLSchema-instance\" ns0:noNamespaceSchemaLocation=\"profiles.xsd\">\n" + "\t<profile name=\"Default\">\n" + "\t\t<series level_number=\"0\" name=\"official-levels-medium\"/>\n" + "\t\t<series level_number=\"0\" name=\"official-levels-hard\"/>\n" + "\t\t<series level_number=\"0\" name=\"official-levels-expert\"/>\n" + "\t\t<series level_number=\"0\" name=\"official-levels-easy\"/>\n" + "\t\t<series level_number=\"0\" name=\"official-levels-tutorial\"/>\n" + "\t</profile>\n" + "\t<profile name=\"Mazix\">\n" + "\t\t<series level_number=\"2\" name=\"official-levels-medium\"/>\n" + "\t\t<series level_number=\"1\" name=\"official-levels-hard\"/>\n" + "\t</profile>\n" + "\t<profile name=\"Test\">\n" + "\t\t<series level_number=\"0\" name=\"official-levels-medium\"/>\n" + "\t\t<series level_number=\"5\" name=\"official-levels-hard\"/>\n" + "\t\t<series level_number=\"4\" name=\"official-levels-expert\"/>\n" + "\t\t<series level_number=\"1\" name=\"official-levels-easy\"/>\n" + "\t\t<series level_number=\"0\" name=\"official-levels-tutorial\"/>\n" + "\t\t<series level_number=\"0\" name=\"contrib-levels-fun\"/>\n" + "\t</profile>\n" + "</profiles>", XML_JAXB_PROGRESSION_DAO.getFilePath() + XML_JAXB_PROGRESSION_DAO.getFileName());
    }

    /**
     * This method is called after all tests, it is used to remove the temporary file used for the
     * tests.
     * 
     * @since 0.8
     */
    @After
    public void removeProfilesTestFile() {
        final File profileTestFile = new File(XML_JAXB_PROGRESSION_DAO.getFilePath() + XML_JAXB_PROGRESSION_DAO.getFileName());
        profileTestFile.delete();
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    @Test
    public void testCreateOrUpdateProgressionWithExistingProfileNameWithExistingLevelSeriesName() throws PersistenceException {
        XML_JAXB_PROGRESSION_DAO.createOrUpdateProgression(PROGRESSION_DTO_3);
        final String xmlLevelSeriesContent = readFileIntoString(XML_JAXB_PROGRESSION_DAO.getFilePath() + XML_JAXB_PROGRESSION_DAO.getFileName());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" + "<profiles xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"profiles.xsd\">\n" + "    <profile name=\"Default\">\n" + "        <series name=\"official-levels-medium\" level_number=\"0\"/>\n" + "        <series name=\"official-levels-hard\" level_number=\"0\"/>\n" + "        <series name=\"official-levels-expert\" level_number=\"0\"/>\n" + "        <series name=\"official-levels-easy\" level_number=\"0\"/>\n" + "        <series name=\"official-levels-tutorial\" level_number=\"0\"/>\n" + "    </profile>\n" + "    <profile name=\"Mazix\">\n" + "        <series name=\"official-levels-medium\" level_number=\"9\"/>\n" + "        <series name=\"official-levels-hard\" level_number=\"1\"/>\n" + "    </profile>\n" + "    <profile name=\"Test\">\n" + "        <series name=\"official-levels-medium\" level_number=\"0\"/>\n" + "        <series name=\"official-levels-hard\" level_number=\"5\"/>\n" + "        <series name=\"official-levels-expert\" level_number=\"4\"/>\n" + "        <series name=\"official-levels-easy\" level_number=\"1\"/>\n" + "        <series name=\"official-levels-tutorial\" level_number=\"0\"/>\n" + "        <series name=\"contrib-levels-fun\" level_number=\"0\"/>\n" + "    </profile>\n" + "</profiles>\n", xmlLevelSeriesContent);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    @Test
    public void testCreateOrUpdateProgressionWithExistingProfileNameWithNotExistingLevelSeriesName() throws PersistenceException {
        XML_JAXB_PROGRESSION_DAO.createOrUpdateProgression(PROGRESSION_DTO_2);
        final String xmlLevelSeriesContent = readFileIntoString(XML_JAXB_PROGRESSION_DAO.getFilePath() + XML_JAXB_PROGRESSION_DAO.getFileName());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" + "<profiles xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"profiles.xsd\">\n" + "    <profile name=\"Default\">\n" + "        <series name=\"official-levels-medium\" level_number=\"0\"/>\n" + "        <series name=\"official-levels-hard\" level_number=\"0\"/>\n" + "        <series name=\"official-levels-expert\" level_number=\"0\"/>\n" + "        <series name=\"official-levels-easy\" level_number=\"0\"/>\n" + "        <series name=\"official-levels-tutorial\" level_number=\"0\"/>\n" + "        <series name=\"contrib-levels-fun\" level_number=\"2\"/>\n" + "    </profile>\n" + "    <profile name=\"Mazix\">\n" + "        <series name=\"official-levels-medium\" level_number=\"2\"/>\n" + "        <series name=\"official-levels-hard\" level_number=\"1\"/>\n" + "    </profile>\n" + "    <profile name=\"Test\">\n" + "        <series name=\"official-levels-medium\" level_number=\"0\"/>\n" + "        <series name=\"official-levels-hard\" level_number=\"5\"/>\n" + "        <series name=\"official-levels-expert\" level_number=\"4\"/>\n" + "        <series name=\"official-levels-easy\" level_number=\"1\"/>\n" + "        <series name=\"official-levels-tutorial\" level_number=\"0\"/>\n" + "        <series name=\"contrib-levels-fun\" level_number=\"0\"/>\n" + "    </profile>\n" + "</profiles>\n", xmlLevelSeriesContent);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    @Test(expected = PersistenceException.class)
    public void testCreateOrUpdateProgressionWithNotExistingProfileName() throws PersistenceException {
        XML_JAXB_PROGRESSION_DAO.createOrUpdateProgression(PROGRESSION_DTO_1);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    @Test(expected = PersistenceException.class)
    public void testDeleteAllProgressionsWithExistingProfileName() throws PersistenceException {
        XML_JAXB_PROGRESSION_DAO.deleteAllProgressions(PROFILE_DTO_3);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    @Test
    public void testDeleteAllProgressionsWithoutExistingProfileName() throws PersistenceException {
        XML_JAXB_PROGRESSION_DAO.deleteAllProgressions(PROFILE_DTO_1);
        final String xmlLevelSeriesContent = readFileIntoString(XML_JAXB_PROGRESSION_DAO.getFilePath() + XML_JAXB_PROGRESSION_DAO.getFileName());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" + "<profiles xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"profiles.xsd\">\n" + "    <profile name=\"Default\"/>\n" + "    <profile name=\"Mazix\">\n" + "        <series name=\"official-levels-medium\" level_number=\"2\"/>\n" + "        <series name=\"official-levels-hard\" level_number=\"1\"/>\n" + "    </profile>\n" + "    <profile name=\"Test\">\n" + "        <series name=\"official-levels-medium\" level_number=\"0\"/>\n" + "        <series name=\"official-levels-hard\" level_number=\"5\"/>\n" + "        <series name=\"official-levels-expert\" level_number=\"4\"/>\n" + "        <series name=\"official-levels-easy\" level_number=\"1\"/>\n" + "        <series name=\"official-levels-tutorial\" level_number=\"0\"/>\n" + "        <series name=\"contrib-levels-fun\" level_number=\"0\"/>\n" + "    </profile>\n" + "</profiles>\n", xmlLevelSeriesContent);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    @Test
    public void testDeleteProgressionWithExistingProfileNameWithLevelSeriesName() throws PersistenceException {
        XML_JAXB_PROGRESSION_DAO.deleteProgression(PROGRESSION_DTO_3);
        final String xmlLevelSeriesContent = readFileIntoString(XML_JAXB_PROGRESSION_DAO.getFilePath() + XML_JAXB_PROGRESSION_DAO.getFileName());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" + "<profiles xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"profiles.xsd\">\n" + "    <profile name=\"Default\">\n" + "        <series name=\"official-levels-medium\" level_number=\"0\"/>\n" + "        <series name=\"official-levels-hard\" level_number=\"0\"/>\n" + "        <series name=\"official-levels-expert\" level_number=\"0\"/>\n" + "        <series name=\"official-levels-easy\" level_number=\"0\"/>\n" + "        <series name=\"official-levels-tutorial\" level_number=\"0\"/>\n" + "    </profile>\n" + "    <profile name=\"Mazix\">\n" + "        <series name=\"official-levels-hard\" level_number=\"1\"/>\n" + "    </profile>\n" + "    <profile name=\"Test\">\n" + "        <series name=\"official-levels-medium\" level_number=\"0\"/>\n" + "        <series name=\"official-levels-hard\" level_number=\"5\"/>\n" + "        <series name=\"official-levels-expert\" level_number=\"4\"/>\n" + "        <series name=\"official-levels-easy\" level_number=\"1\"/>\n" + "        <series name=\"official-levels-tutorial\" level_number=\"0\"/>\n" + "        <series name=\"contrib-levels-fun\" level_number=\"0\"/>\n" + "    </profile>\n" + "</profiles>\n", xmlLevelSeriesContent);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    @Test(expected = PersistenceException.class)
    public void testDeleteProgressionWithExistingProfileNameWithoutLevelSeriesName() throws PersistenceException {
        XML_JAXB_PROGRESSION_DAO.deleteProgression(PROGRESSION_DTO_2);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    @Test(expected = PersistenceException.class)
    public void testDeleteProgressionWithNotExistingProfileName() throws PersistenceException {
        XML_JAXB_PROGRESSION_DAO.deleteProgression(PROGRESSION_DTO_1);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    @Test
    public void testFindAllProgressionsWithExistingProfileName() throws PersistenceException {
        final Set<ProgressionDTO> progressions = XML_JAXB_PROGRESSION_DAO.findAllProgressions(PROFILE_DTO_1);
        assertEquals(5, progressions.size());
        assertTrue(progressions.contains(PROGRESSION_DTO_4));
        assertTrue(progressions.contains(PROGRESSION_DTO_5));
        assertTrue(progressions.contains(PROGRESSION_DTO_6));
        assertTrue(progressions.contains(PROGRESSION_DTO_7));
        assertTrue(progressions.contains(PROGRESSION_DTO_8));
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    @Test(expected = PersistenceException.class)
    public void testFindAllProgressionsWithNotExistingProfileName() throws PersistenceException {
        XML_JAXB_PROGRESSION_DAO.findAllProgressions(PROFILE_DTO_3);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    @Test
    public void testFindProgressionWithExistingProfileNameWithLevelSeriesName() throws PersistenceException {
        final ProgressionDTO progressionDTO = XML_JAXB_PROGRESSION_DAO.findProgression(PROFILE_DTO_4, LEVEL_SERIES_DTO_3);
        assertEquals(2, progressionDTO.getReachedLevelId());
        assertEquals(PROGRESSION_DTO_9, progressionDTO);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    @Test
    public void testFindProgressionWithExistingProfileNameWithNotLevelSeriesName() throws PersistenceException {
        final ProgressionDTO progressionDTO = XML_JAXB_PROGRESSION_DAO.findProgression(PROFILE_DTO_4, LEVEL_SERIES_DTO_4);
        assertNull(progressionDTO);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    @Test(expected = PersistenceException.class)
    public void testFindProgressionWithNotExistingProfileName() throws PersistenceException {
        XML_JAXB_PROGRESSION_DAO.findProgression(PROFILE_DTO_3, LEVEL_SERIES_DTO_3);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    @Test
    public void testResetAllProgressionWithExistingProfileName() throws PersistenceException {
        XML_JAXB_PROGRESSION_DAO.resetAllProgressions(PROFILE_DTO_4, 0);
        final String xmlLevelSeriesContent = readFileIntoString(XML_JAXB_PROGRESSION_DAO.getFilePath() + XML_JAXB_PROGRESSION_DAO.getFileName());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" + "<profiles xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"profiles.xsd\">\n" + "    <profile name=\"Default\">\n" + "        <series name=\"official-levels-medium\" level_number=\"0\"/>\n" + "        <series name=\"official-levels-hard\" level_number=\"0\"/>\n" + "        <series name=\"official-levels-expert\" level_number=\"0\"/>\n" + "        <series name=\"official-levels-easy\" level_number=\"0\"/>\n" + "        <series name=\"official-levels-tutorial\" level_number=\"0\"/>\n" + "    </profile>\n" + "    <profile name=\"Mazix\">\n" + "        <series name=\"official-levels-medium\" level_number=\"0\"/>\n" + "        <series name=\"official-levels-hard\" level_number=\"0\"/>\n" + "    </profile>\n" + "    <profile name=\"Test\">\n" + "        <series name=\"official-levels-medium\" level_number=\"0\"/>\n" + "        <series name=\"official-levels-hard\" level_number=\"5\"/>\n" + "        <series name=\"official-levels-expert\" level_number=\"4\"/>\n" + "        <series name=\"official-levels-easy\" level_number=\"1\"/>\n" + "        <series name=\"official-levels-tutorial\" level_number=\"0\"/>\n" + "        <series name=\"contrib-levels-fun\" level_number=\"0\"/>\n" + "    </profile>\n" + "</profiles>\n", xmlLevelSeriesContent);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    @Test(expected = PersistenceException.class)
    public void testResetAllProgressionWithNotExistingProfileName() throws PersistenceException {
        XML_JAXB_PROGRESSION_DAO.resetAllProgressions(PROFILE_DTO_3, 0);
    }
}
