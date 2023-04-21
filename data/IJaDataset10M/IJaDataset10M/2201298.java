package com.nogoodatcoding.cip.converter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *
 * The Java object for a CIP Image Document XML file
 * 
 * @author no.good.at.coding
 */
public class CIPImageDocument {

    private static Logger log_ = Logger.getLogger(CIPImageDocument.class);

    private static ResourceBundle messages_ = ResourceBundle.getBundle("com.nogoodatcoding.cip.converter.messages.Messages_CIPImageDocument");

    private int locationX = -1;

    private int locationY = -1;

    private int width = 0;

    private int height = 0;

    private int depth = 2;

    private String data = null;

    private String title = null;

    private String prompt = null;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLocationX() {
        return locationX;
    }

    public void setLocationX(int locationX) {
        this.locationX = locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public void setLocationY(int locationY) {
        this.locationY = locationY;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    /**
     *
     * Reads the file specified with {code filename} and makes a
     * {code CIPImageDocument} from the values read.
     *
     * @param filename The name (and path) of the file to be read
     *
     * @return The equivalent {code CIPImageDocument}
     *
     * @throws java.io.IOException If there is a problem reading the file or
     *                             parsing the XML
     */
    public static CIPImageDocument readFromFile(String filename) throws IOException {
        CIPImageDocument.log_.debug(CIPImageDocument.messages_.getString("cipImageDocument.log.debug.readingCIP") + filename);
        CIPImageDocument cip = new CIPImageDocument();
        SAXBuilder builder = new SAXBuilder();
        try {
            Document doc = builder.build(new File(filename));
            Element cipRoot = doc.getRootElement();
            cip.setLocationX(Integer.parseInt(cipRoot.getChildText("LocationX")));
            cip.setLocationY(Integer.parseInt(cipRoot.getChildText("LocationY")));
            cip.setWidth(Integer.parseInt(cipRoot.getChildText("Width")));
            cip.setHeight(Integer.parseInt(cipRoot.getChildText("Height")));
            cip.setDepth(Integer.parseInt(cipRoot.getChildText("Depth")));
            cip.setData(cipRoot.getChildText("Data"));
            cip.setTitle(cipRoot.getChildText("Title"));
            cip.setPrompt(cipRoot.getChildText("Prompt"));
        } catch (JDOMException e) {
            CIPImageDocument.log_.error(CIPImageDocument.messages_.getString("cipImageDocument.log.error.cipReadException"), e);
            throw new IOException(CIPImageDocument.messages_.getString("cipImageDocument.log.error.cipReadException"), e);
        }
        CIPImageDocument.log_.debug(CIPImageDocument.messages_.getString("cipImageDocument.log.debug.doneReadingCIP"));
        return cip;
    }

    /**
     *
     * Writes {@code this CIPImageDocument} object to file with the given
     * {code filename}
     *
     * @param filename The file to be written to
     *
     * @throws java.io.IOException In case there is a problem writing the file
     */
    public void writeToFile(String filename) throws IOException {
        CIPImageDocument.writeCIPFile(filename, data, width, height, title, prompt, locationX, locationY);
    }

    /**
     *
     * Writes a file with {@code filename} and the passed {@code CIP Image
     * Document} attributes as the nodes
     *
     * @param filename The name of the file to be written to
     *
     * @param data The encoded image data
     *
     * @param width The width of the image that is encoded in {@code data}
     *
     * @param height The height of the image that is encoded in {@code data}
     *
     * @param title The title for the image
     *
     * @param prompt The prompt for the image
     *
     * @param locationX The horizontal location of the image when displayed
     *
     * @param locationY The vertical location of the image when displayed
     *
     * @throws java.io.IOException In case there is a problem writing the file
     */
    public static void writeCIPFile(String filename, String data, int width, int height, String title, String prompt, int locationX, int locationY) throws IOException {
        CIPImageDocument.log_.debug(CIPImageDocument.messages_.getString("cipImageDocument.log.debug.writingCIP") + filename);
        BufferedWriter out = new BufferedWriter(new FileWriter(filename, false));
        out.write("<CiscoIPPhoneImage>\n");
        out.write("    <LocationX>");
        out.write(locationX + "");
        out.write("</LocationX>\n");
        out.write("    <LocationY>");
        out.write(locationY + "");
        out.write("</LocationY>\n");
        out.write("    <Width>");
        out.write(width + "");
        out.write("</Width>\n");
        out.write("    <Height>");
        out.write(height + "");
        out.write("</Height>\n");
        out.write("    <Depth>");
        out.write(2 + "");
        out.write("</Depth>\n");
        out.write("    <Data>");
        out.write(data);
        out.write("</Data>\n");
        out.write("    <Title>");
        out.write(title);
        out.write("</Title>\n");
        out.write("    <Prompt>");
        out.write(prompt);
        out.write("</Prompt>\n");
        out.write("</CiscoIPPhoneImage>");
        out.flush();
        out.close();
        CIPImageDocument.log_.debug(CIPImageDocument.messages_.getString("cipImageDocument.log.debug.doneWritingCIP"));
    }
}
