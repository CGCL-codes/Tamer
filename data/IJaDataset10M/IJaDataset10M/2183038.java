package de.psisystems.dmachinery;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Test;
import de.psisystems.dmachinery.core.Constants;
import de.psisystems.dmachinery.core.exeptions.PrintException;
import de.psisystems.dmachinery.core.types.ResourceType;
import de.psisystems.dmachinery.resources.Resource;
import de.psisystems.dmachinery.resources.SimpleResourceID;
import de.psisystems.dmachinery.resources.csv.CSVResourceRepo;
import de.psisystems.dmachinery.xml.MarshallUtil;
import de.psisystems.dmachinery.xml.Marshallable;

public class HerbstcampusTest extends BaseTest {

    private static final Log log = LogFactory.getLog(HerbstcampusTest.class);

    @Test
    public void testPrintMaster() {
        URL repositoryBase = this.getClass().getResource("/de/psisystems/dmachinery/template/samples/herbstcampustest/repo/");
        assertNotNull(repositoryBase);
        ResourceRepo resourceRepo = new CSVResourceRepo(repositoryBase);
        PrintService printService = new SimplePrintService();
        Marshallable data = createTestData();
        Resource resource = null;
        try {
            resource = resourceRepo.getResource("COMMON", "de_DE", new SimpleResourceID("MASTER", ResourceType.TEMPLATE), new Date());
        } catch (PrintException e) {
            fail(e.getMessage());
        }
        assertNotNull(resource);
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("UUID", UUID.randomUUID().toString());
        String outputFilename = "HerbstcampusTest_testPrintMaster.pdf";
        try {
            printService.print(resource, data, createStreamOutputChannel(outputFilename), attributes);
        } catch (PrintException e) {
            log.debug(e);
            fail(e.getMessage());
        }
        assertTrue(exist(new File(getOutputPath() + File.separator + outputFilename + ".pdf")));
    }

    @Test
    public void testPrintHaspaKartenReg() {
        URL repositoryBase = this.getClass().getResource("/de/psisystems/dmachinery/template/samples/herbstcampustest/repo/");
        assertNotNull(repositoryBase);
        ResourceRepo resourceRepo = new CSVResourceRepo(repositoryBase);
        PrintService printService = new SimplePrintService();
        Marshallable data = createTestData();
        Resource resource = null;
        try {
            resource = resourceRepo.getResource("HASPA", "de_DE", new SimpleResourceID("KARTENREGISTRIERUNG", ResourceType.TEMPLATE), new Date());
        } catch (PrintException e) {
            fail(e.getMessage());
        }
        assertNotNull(resource);
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("key1", "value1");
        String outputFilename = "HerbstcampusTest_testPrintHaspaKartenReg.pdf";
        try {
            printService.print(resource, data, createStreamOutputChannel(outputFilename), attributes);
        } catch (PrintException e) {
            log.debug(e);
            fail(e.getMessage());
        }
        assertTrue(exist(new File(getOutputPath() + File.separator + outputFilename + ".pdf")));
    }

    @Test
    public void testPrintCoverLetter() {
        URL repositoryBase = this.getClass().getResource("/de/psisystems/dmachinery/template/samples/herbstcampustest/repo/");
        assertNotNull(repositoryBase);
        ResourceRepo resourceRepo = new CSVResourceRepo(repositoryBase);
        PrintService printService = new SimplePrintService();
        Marshallable data = createTestData();
        Resource resource = null;
        try {
            resource = resourceRepo.getResource("COMMON", "de_DE", new SimpleResourceID("COVERLETTER", ResourceType.TEMPLATE), new Date());
        } catch (PrintException e) {
            fail(e.getMessage());
        }
        assertNotNull(resource);
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("UUID", UUID.randomUUID().toString());
        Properties metaData = new Properties();
        metaData.setProperty("UUID", UUID.randomUUID().toString());
        metaData.setProperty("NAME", "Steve Jobs");
        metaData.setProperty("STREET", "1 Infinite Loop");
        metaData.setProperty("ZIP", "CA 95014");
        metaData.setProperty("CITY", "Cupertino");
        metaData.setProperty("BEILAGEN", "RueckumschlagA6, Tankgutschein");
        attributes.put(Constants.METADATA, metaData);
        String outputFilename = "HerbstcampusTest_testPrintCoverLetter.pdf";
        try {
            printService.print(resource, data, createStreamOutputChannel(outputFilename), attributes);
        } catch (PrintException e) {
            log.debug(e);
            fail(e.getMessage());
        }
        assertTrue(exist(new File(getOutputPath() + File.separator + outputFilename + ".pdf")));
    }

    @Test
    @Ignore
    public void testPrintContract() {
        URL repositoryBase = this.getClass().getResource("/de/psisystems/dmachinery/template/samples/herbstcampustest/repo/");
        assertNotNull(repositoryBase);
        ResourceRepo resourceRepo = new CSVResourceRepo(repositoryBase);
        PrintService printService = new SimplePrintService();
        Marshallable data = createTestData();
        Resource resource = null;
        try {
            resource = resourceRepo.getResource("COMPANY", "de_DE", new SimpleResourceID("CONTRACT", ResourceType.WORKFLOW), new Date());
        } catch (PrintException e) {
            fail(e.getMessage());
        }
        assertNotNull(resource);
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("UUID", UUID.randomUUID().toString());
        Properties metaData = new Properties();
        metaData.setProperty("UUID", UUID.randomUUID().toString());
        metaData.setProperty("NAME", "Steve Jobs");
        metaData.setProperty("STREET", "1 Infinite Loop");
        metaData.setProperty("ZIP", "CA 95014");
        metaData.setProperty("CITY", "Cupertino");
        metaData.setProperty("BEILAGEN", "RueckumschlagA6, Tankgutschein");
        attributes.put(Constants.METADATA, metaData);
        String outputFilename = "HerbstcampusTest_testPrintContract";
        try {
            printService.print(resource, data, createStreamOutputChannel(outputFilename), attributes);
        } catch (PrintException e) {
            log.debug(e);
            fail(e.getMessage());
        }
        assertTrue(exist(new File(getOutputPath() + File.separator + outputFilename + ".pdf")));
    }

    @Test
    public void testMarshallTestData() {
        Marshallable data = createTestData();
        File of = new File(getOutputPath() + "/HerbstcampusTest_testMarshallTestData.xml");
        try {
            MarshallUtil.toXML(new FileOutputStream(of), data);
        } catch (FileNotFoundException e) {
            fail(e.getMessage());
        } catch (PrintException e) {
            fail(e.getMessage());
        }
        assertTrue(exist(of));
    }

    private Marshallable createTestData() {
        SimplePrintServiceTestData simplePrintServiceTestData = new SimplePrintServiceTestData();
        simplePrintServiceTestData.getContract().setApplicant1(new Applicant("Jobs", "Steve", " 1 Infinite Loop", "CA 95014", "Cupertino"));
        return simplePrintServiceTestData;
    }
}
