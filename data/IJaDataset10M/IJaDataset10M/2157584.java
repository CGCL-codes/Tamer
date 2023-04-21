package org.rdv.action;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLDecoder;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * @author msoltani
 *
 */
public class OpenSeesDataImportAction extends DataImportAction {

    /** serialization version identifier */
    private static final long serialVersionUID = -6446885350157840581L;

    String outputDataFileName = "data.dat";

    /** Reader for inpout file  */
    BufferedReader buffReader;

    /** writer for output file */
    BufferedWriter buffWriter;

    /** Constructor to show text/description
   * 
   */
    public OpenSeesDataImportAction() {
        super("Import OpenSees data file", "Import local data to RBNB server");
    }

    /** Helper class to delete a temporary folder created on file system */
    private static DirectoryDeleter deleterThread;

    /** Thread to delete folder on Exit */
    static {
        deleterThread = new DirectoryDeleter();
        Runtime.getRuntime().addShutdownHook(deleterThread);
    }

    /**
   * Prompts the user for the input OpesSees data file and uploads the data to the RBNB server.
   */
    public void actionPerformed(ActionEvent ae) {
        boolean error = false;
        try {
            File inputDataFile = getFile();
            if (inputDataFile != null) {
                File openSeesDataFile = createOSDataFile(inputDataFile);
                importData(openSeesDataFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
            error = true;
        }
        if (error) {
            JOptionPane.showMessageDialog(null, "There was an error importing the data file.", "Import failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
   * Method to create flat OpenSees output data file from XML input
   * @param input XML dataFile
   * @return flat output file
   * @throws FileNotFoundException
   * @throws IOException
   */
    private File createOSDataFile(File inputDataFile) throws FileNotFoundException, IOException {
        File tmpFolder = createTmpFolder();
        File outputDataFile = createOutputDataFile(tmpFolder);
        openReadWriteBuffers(inputDataFile, outputDataFile);
        writeOutputHeader(inputDataFile);
        writeOutputData(inputDataFile);
        closeReadWriteBuffers();
        return outputDataFile;
    }

    /**
   * Writes all data between <Data></Data> tags to output
   * @param input XML file
   * @throws IOException
   */
    private void writeOutputData(File input) throws IOException {
        String line;
        buffReader = new BufferedReader(new FileReader(input));
        while ((line = buffReader.readLine()) != null) {
            if (!line.contains("<Data>")) {
                continue;
            } else {
                while ((line = buffReader.readLine()) != null) {
                    if (!line.contains("</Data>")) {
                        line = line.trim().replaceAll(" ", "\t");
                        writeLine(line, buffWriter);
                    } else {
                        break;
                    }
                }
            }
        }
    }

    /**
   * Write Header extracted from XML input
   * @param inputFile OpesSees input File
   * @throws IOException
   */
    private void writeOutputHeader(File inputFile) throws IOException {
        String header = getHeaderString(inputFile);
        if (header != null) {
            writeLine(header, buffWriter);
        }
    }

    /**
   * Create OpenSees output data file in temporary folder
   * @param tmpFolder
   * @return Output file created
   */
    private File createOutputDataFile(File tmpFolder) {
        File output = new File(tmpFolder + "/" + outputDataFileName);
        return output;
    }

    /** Transformation of input header xml file using the stylesheet
   * @param input OpenSees XML file
   * @return Parsed string of the header
   */
    private String getHeaderString(File input) {
        try {
            Source xmlSource = new StreamSource(input);
            URL stylesheetUrl = getClass().getClassLoader().getResource("xslt/header.xslt");
            if (stylesheetUrl == null) return null;
            String stylesheetLocation = URLDecoder.decode(stylesheetUrl.getFile(), "UTF-8");
            File stylesheet = new File(stylesheetLocation);
            Source xsltSource = new StreamSource(stylesheet);
            StringWriter writer = new StringWriter();
            Result result = new StreamResult(writer);
            TransformerFactory transFact = TransformerFactory.newInstance();
            Transformer trans = transFact.newTransformer(xsltSource);
            trans.transform(xmlSource, result);
            return writer.toString();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
            return null;
        } catch (TransformerException te) {
            te.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
   * @param line to write
   * @param writer target writer
   * @throws IOException
   */
    private static void writeLine(String line, BufferedWriter writer) throws IOException {
        writer.write(line + "\n");
    }

    /**
   * open buffers
   * @param input File for reading
   * @param output File for writing
   * @throws FileNotFoundException
   * @throws IOException
   */
    private void openReadWriteBuffers(File input, File output) throws FileNotFoundException, IOException {
        buffReader = new BufferedReader(new FileReader(input));
        buffWriter = new BufferedWriter(new FileWriter(output));
    }

    /**
   * close buffers
   * @throws IOException
   */
    private void closeReadWriteBuffers() throws IOException {
        if (buffReader != null) {
            buffReader.close();
        }
        if (buffWriter != null) {
            buffWriter.close();
        }
    }

    /**
   * Create a temporary folder for output files
   * @return
   */
    private File createTmpFolder() {
        String folderName = Integer.toString((int) (Math.random() * (999 - 99)) + 99);
        File dir = new File(System.getProperty("java.io.tmpdir"), folderName);
        dir.deleteOnExit();
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }
        deleterThread.add(dir);
        return dir;
    }

    /**
   * Prompts the user for the OpesSees XML input file to import data from.
   * 
   * @return  the data file, or null if none is selected
   */
    private static File getFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {

            public boolean accept(File f) {
                return (f.isDirectory() || f.getName().toLowerCase().endsWith(".xml"));
            }

            public String getDescription() {
                return "OpenSees XML Files";
            }
        });
        int returnVal = fileChooser.showDialog(null, "Import");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }
}
