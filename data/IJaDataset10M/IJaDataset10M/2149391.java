package de.iteratec.visio.model;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.TestCase;
import org.xml.sax.SAXException;
import de.iteratec.visio.model.exceptions.MasterNotFoundException;
import de.iteratec.visio.model.exceptions.NoSuchElementException;

public class MasterplanShapeGenerationTest extends TestCase {

    private static final String VISIO_TEMPLATE_NAME = "VisioMasterplanTemplate.vdx";

    private Document createDocument() throws IOException, ParserConfigurationException, SAXException {
        InputStream input = ClassLoader.getSystemResourceAsStream(VISIO_TEMPLATE_NAME);
        Document document = DocumentFactory.getInstance().loadDocument(input);
        input.close();
        return document;
    }

    public void testShapeGeneration() throws NoSuchElementException, IOException, MasterNotFoundException, ParserConfigurationException, SAXException {
        Document document = createDocument();
        Page page = document.getPage(0);
        Master master = document.getMaster("Legend");
        assertEquals(43, master.getID().intValue());
        Shape[] shapes = master.getShapes();
        assertEquals(1, shapes.length);
        page.setSize(10.0, 15.0);
        double width = page.getWidth();
        double height = page.getHeight();
        assertEquals("Width incorrect", 10.0, width, 0.01);
        assertEquals("Height incorrect", 15.0, height, 0.01);
        Shape shape = page.createNewShape("Legend");
        shape.setLineWeight(2.0);
        shape.setPosition(2.0, 3.0);
        double pinX = shape.getPinX();
        double pinY = shape.getPinY();
        assertEquals("PinX incorrect", 2.0, pinX, 0.01);
        assertEquals("PinY incorrect", 3.0, pinY, 0.01);
    }

    public void testShapeGenerationAndWrite() throws IOException, NoSuchElementException, MasterNotFoundException, ParserConfigurationException, SAXException {
        Document document = createDocument();
        Page page = document.getPage(0);
        Master master = document.getMaster("Legend");
        assertEquals(43, master.getID().intValue());
        Shape[] shapes = master.getShapes();
        assertEquals(1, shapes.length);
        Shape shape = page.createNewShape("Legend");
        shape.setPosition(2.0, 3.0);
        assertEquals("PinX incorrect", 2.0, shape.getPinX(), 0.01);
        assertEquals("PinY incorrect", 3.0, shape.getPinY(), 0.01);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        document.write(byteOut);
        byteOut.close();
        String fileName = "testresult/MasterplanShapeGenerationTest_testShapeGenerationAndWrite.vdx";
        writeToFileAndOpen(document, fileName);
    }

    private void writeToFileAndOpen(Document document, String fileName) throws IOException {
        File outFile = new File(fileName);
        outFile.getParentFile().mkdirs();
        document.save(outFile);
        Desktop.getDesktop().open(outFile);
    }
}
