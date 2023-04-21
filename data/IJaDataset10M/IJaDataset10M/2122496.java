package mrstk;

import FileFormats.Base64;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.xml.sax.SAXException;

/**
 * This class loads in the input XML file and reads the fid elements present in
 * it. Once this is done, it sets the Fid array of a SpectrumArray Object, which
 * in turn calls the fft function and creates the spec array for the same object.
 * @author avani
 */
public class MrstkXmlFileReader extends SpectrumArrayPipelineElement {

    /**
     * The file that it reads from
     */
    protected File inFile;

    /**
     * The double arrays that hold the traces temporarily
     */
    protected double[][] realArray, imagArray;

    /**
     * Constructor, specifing 0 inputs and 1 output port
     */
    public MrstkXmlFileReader() {
        super();
        this.setNumberOfInputPorts(0);
        this.setNumberOfOutputPorts(1);
    }

    /**
     * Specifies the file to read
     * @param f A valid file
     */
    public void setFile(File f) {
        this.inFile = f;
    }

    /**
     * Specifies the file to read
     * @param name path to a valid file
     */
    public void setFileName(String name) {
        this.inFile = new File(name);
    }

    /**
     * Get method for the file 
     * @return The java.io.File specified for reading
     */
    public File getFile() {
        return this.inFile;
    }

    /**
     * This function reads in the Real and imaginary traces and sets the 
     * members of the class.
     * @param f
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @throws javax.xml.xpath.XPathExpressionException
     */
    protected void readTraces(File f) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        String realpart = null, imagpart = null;
        int traces, elements;
        byte[] realout, imagout;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document doc = builder.parse(f);
        XPathFactory xpfactory = XPathFactory.newInstance();
        XPath xpath = xpfactory.newXPath();
        traces = Integer.parseInt((String) xpath.evaluate("/mrstk-spectrum-array/header/data-point-rows/text()", doc, XPathConstants.STRING));
        elements = Integer.parseInt((String) xpath.evaluate("/mrstk-spectrum-array/header/data-point-columns/text()", doc, XPathConstants.STRING));
        this.realArray = new double[traces][elements];
        this.imagArray = new double[traces][elements];
        for (int i = 0; i < traces; i++) {
            realpart = (String) xpath.evaluate("/mrstk-spectrum-array/data/row[" + i + "+1]/real/text()", doc, XPathConstants.STRING);
            imagpart = (String) xpath.evaluate("/mrstk-spectrum-array/data/row[" + i + "+1]/imag/text()", doc, XPathConstants.STRING);
            realout = Base64.decode(realpart);
            imagout = Base64.decode(imagpart);
            ByteArrayInputStream realbyte_in = new ByteArrayInputStream(realout);
            DataInputStream realdata_in = new DataInputStream(realbyte_in);
            ByteArrayInputStream imagbyte_in = new ByteArrayInputStream(imagout);
            DataInputStream imagdata_in = new DataInputStream(imagbyte_in);
            int j = 0;
            while ((realdata_in.available() > 0) && (imagdata_in.available() > 0)) {
                this.realArray[i][j] = realdata_in.readDouble();
                this.imagArray[i][j] = imagdata_in.readDouble();
                j++;
            }
        }
    }

    /**
     * Utility that reads a double-valued parameter string from the specified file
     * @param f
     * @param key Case-sensitive parameter to be read
     * @return the associated double value, or 0 if it is not found
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @throws javax.xml.xpath.XPathExpressionException
     */
    protected double readParameter(File f, String key) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        double retVal = 0;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document doc = builder.parse(f);
        XPathFactory xpfactory = XPathFactory.newInstance();
        XPath xpath = xpfactory.newXPath();
        StringBuffer evalBuff = new StringBuffer();
        String evalStr = null;
        StringBuffer e = evalBuff.append("/mrstk-spectrum-array/header/").append(key).append("/text()");
        evalStr = e.toString();
        retVal = Double.parseDouble((String) xpath.evaluate(evalStr.toString(), doc, XPathConstants.STRING));
        return retVal;
    }

    /**
     * This function reads in the real and imaginary fid traces from the 
     * XML file and assigns the members of the spectrumarray object. Then it 
     * sets the pipeline for the reader.
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @throws javax.xml.xpath.XPathExpressionException
     */
    public void read() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        super.executeAlgorithm();
        if (!this.inFile.exists()) {
            System.out.println("File does not Exist.");
        }
        if (!this.inFile.getName().endsWith(".xml")) {
            System.out.println("Please select a valid XML file");
        } else {
            try {
                this.readTraces(inFile);
            } catch (Exception ex) {
                Logger.getLogger(MrstkXmlFileReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        SpectrumArray sp = new SpectrumArray();
        sp.setNominalFrequencyHz(1000000 * this.readParameter(this.inFile, "transmitter-frequency"));
        sp.setCenterFrequencyPPM(this.readParameter(this.inFile, "chemical-shift-reference"));
        sp.setSpectralWidthHz(this.readParameter(this.inFile, "spectral-width"));
        sp.setDwellTime(1 / sp.getSpectralWidthHz());
        ComplexArray[] fidArr = new ComplexArray[this.realArray.length];
        for (int i = 0; i < realArray.length; i++) {
            fidArr[i] = new ComplexArray(realArray[i], imagArray[i]);
        }
        sp.setFidArray(fidArr);
        this.myOutputs[0] = sp;
    }
}
