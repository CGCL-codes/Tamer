package de.ddb.conversion.converters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import org.apache.log4j.Logger;
import de.ddb.conversion.ConverterException;
import junit.framework.TestCase;

public class MarcxmlToMarcConverterTest extends TestCase {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(MarcxmlToMarcConverterTest.class);

    public void testConvert() throws IOException, ConverterException {
        File f = new File("test" + File.separator + "input" + File.separator + "jo-marc.xml");
        logger.info("testConvert()- start");
        MarcxmlToMarcConverter marcxml2marc = new MarcxmlToMarcConverter();
        logger.debug("Using resource: " + f.getName());
        OutputStream convFos = new FileOutputStream("test" + File.separator + "output" + File.separator + "out.txt");
        OutputStream reconvFos = new FileOutputStream("test" + File.separator + "output" + File.separator + "out.bak");
        try {
        } catch (Exception e) {
            logger.info(e);
        }
        FileInputStream input = null;
        ByteArrayOutputStream inbuffer = new ByteArrayOutputStream();
        try {
            input = new FileInputStream(f);
            int i;
            while ((i = input.read()) != -1) {
                inbuffer.write(i);
            }
        } finally {
            if (input != null) {
                input.close();
            }
        }
        byte[] in = inbuffer.toByteArray();
        logger.info("Beginne mit Konversion");
        logger.info("Beginne mit Konversion");
        byte[] output = marcxml2marc.convert(in, "UTF-8", "UTF-8");
        MarcToMarcxmlConverter converter2 = new MarcToMarcxmlConverter();
        byte[] output2 = converter2.convert(output, "UTF-8", "UTF-8");
        convFos.write(output);
        reconvFos.write(output2);
        convFos.close();
        reconvFos.close();
        assertFalse(Arrays.equals(in, output2));
    }

    private byte[] generateXmlFromMarc(String testFile) throws ConverterException, IOException {
        logger.info("generateXmlFromMarc()- start");
        MarcToMarcxmlConverter converter = new MarcToMarcxmlConverter();
        InputStream in = new FileInputStream(testFile);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int i;
        while ((i = in.read()) != -1) {
            out.write(i);
        }
        byte[] output = converter.convert(out.toByteArray(), "x-PICA", "UTF-8");
        logger.info("XML-OUTPUT:" + new String(output, "UTF-8"));
        logger.info("generateXmlFromMarc()- end");
        in.close();
        out.close();
        return output;
    }
}
