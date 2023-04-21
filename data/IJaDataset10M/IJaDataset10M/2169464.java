package sereneTest;

import java.util.Stack;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.ValidatorHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.XMLReaderFactory;
import serene.validation.jaxp.RNGSchemaFactory;
import sereneWrite.WriteErrorHandler;
import sereneWrite.MessageWriter;
import sereneWrite.WriteHandler;
import sereneWrite.ConsoleHandler;
import sereneWrite.FileHandler;
import serene.Constants;

class Tester {

    SchemaFactory schemaFactory;

    MessageWriter debugWriter;

    WriteErrorHandler debugErrorHandler;

    String resultsDestinationFileName;

    String destinationDirName;

    Stack<String> sourceDirNames;

    int count;

    XMLReader xmlReader;

    public Tester() {
        count = 0;
        debugWriter = new MessageWriter();
        debugWriter.setWriteHandler(new FileHandler());
        debugErrorHandler = new WriteErrorHandler();
        debugErrorHandler.setWriteHandler(new FileHandler());
        try {
            debugErrorHandler.setFeature("http://example.com/countMissingLibraryExceptions", false);
        } catch (SAXNotRecognizedException e) {
            e.printStackTrace();
        }
        sourceDirNames = new Stack<String>();
        try {
            xmlReader = XMLReaderFactory.createXMLReader();
            try {
                xmlReader.setFeature("http://xml.org/sax/features/namespaces", true);
                xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
                xmlReader.setFeature("http://xml.org/sax/features/validation", false);
            } catch (SAXNotRecognizedException e) {
                e.printStackTrace();
            }
            xmlReader.setErrorHandler(debugErrorHandler);
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    int getTestCount() {
        return count;
    }

    /**
	* Naming convention for test files:
	* - one schema file per directory called schema.rng
	* in the case of multiple documents schemas, the root document must bear 
	* the name, the other names are free of constraints
	* - zero or more documents called document[n].xml where [n] stands for a natural
	* number greater than 0.
	*/
    public void test(String sourceDirName, String xmlFileName, String destinationDirName, String resultsDestinationFileName) {
        File sourceDir = new File(sourceDirName);
        if (!sourceDir.isDirectory()) {
            System.out.println("NO DIRECTORY " + sourceDirName);
            return;
        }
        this.destinationDirName = destinationDirName;
        this.resultsDestinationFileName = resultsDestinationFileName;
        debugWriter.init(destinationDirName);
        debugErrorHandler.init(destinationDirName);
        if (schemaFactory == null) {
            schemaFactory = new RNGSchemaFactory();
            try {
                schemaFactory.setFeature(Constants.LEVEL1_ATTRIBUTE_DEFAULT_VALUE_FEATURE, true);
                schemaFactory.setFeature(Constants.LEVEL2_ATTRIBUTE_DEFAULT_VALUE_FEATURE, true);
                schemaFactory.setFeature(Constants.LEVEL1_ATTRIBUTE_ID_TYPE_FEATURE, true);
                schemaFactory.setFeature(Constants.LEVEL2_ATTRIBUTE_ID_TYPE_FEATURE, true);
                schemaFactory.setFeature(Constants.LEVEL1_DOCUMENTATION_ELEMENT_FEATURE, true);
            } catch (SAXNotRecognizedException e) {
                e.printStackTrace();
            } catch (SAXNotSupportedException e) {
                e.printStackTrace();
            }
            schemaFactory.setErrorHandler(debugErrorHandler);
        }
        sourceDirNames.clear();
        System.out.println("TESTED DIRECTORY " + sourceDir);
        File[] content = sourceDir.listFiles();
        if (content.length == 0) {
            System.out.println("empty directory ");
            return;
        }
        File rngFile = null;
        File xmlFile = null;
        for (int i = 0; i < content.length; i++) {
            String fileName = content[i].getName();
            if (fileName.equals("schema.rng")) {
                rngFile = content[i];
            } else if (fileName.equals(xmlFileName)) {
                xmlFile = content[i];
            }
        }
        if (rngFile != null) {
            initOutput();
            debugErrorHandler.start(resultsDestinationFileName);
            debugErrorHandler.print("TESTED SCHEMA " + rngFile);
            System.out.println("TESTED SCHEMA " + rngFile);
            if (!initReader(rngFile)) {
                debugErrorHandler.close();
                return;
            }
            String xmlFilePath = xmlFile.getAbsolutePath();
            System.out.println("TESTED DOCUMENT " + xmlFilePath);
            validateXMLFile(xmlFilePath);
            debugErrorHandler.close();
        }
    }

    /**
	* Naming convention for test files:
	* - one schema file per directory called schema.rng
	* in the case of multiple documents schemas, the root document must bear 
	* the name, the other names are free of constraints
	* - zero or more documents called document[n].xml where [n] stands for a natural
	* number greater than 0.
	*/
    public void test(String sourceDirName, String destinationDirName, String resultsDestinationFileName) {
        File sourceDir = new File(sourceDirName);
        if (!sourceDir.isDirectory()) {
            System.out.println("NO DIRECTORY " + sourceDirName);
            return;
        }
        this.destinationDirName = destinationDirName;
        this.resultsDestinationFileName = resultsDestinationFileName;
        debugWriter.init(destinationDirName);
        debugErrorHandler.init(destinationDirName);
        if (schemaFactory == null) {
            schemaFactory = new RNGSchemaFactory();
            try {
                schemaFactory.setFeature(Constants.LEVEL1_ATTRIBUTE_DEFAULT_VALUE_FEATURE, true);
                schemaFactory.setFeature(Constants.LEVEL2_ATTRIBUTE_DEFAULT_VALUE_FEATURE, true);
                schemaFactory.setFeature(Constants.LEVEL1_ATTRIBUTE_ID_TYPE_FEATURE, true);
                schemaFactory.setFeature(Constants.LEVEL2_ATTRIBUTE_ID_TYPE_FEATURE, true);
                schemaFactory.setFeature(Constants.LEVEL1_DOCUMENTATION_ELEMENT_FEATURE, true);
            } catch (SAXNotRecognizedException e) {
                e.printStackTrace();
            } catch (SAXNotSupportedException e) {
                e.printStackTrace();
            }
            schemaFactory.setErrorHandler(debugErrorHandler);
        }
        sourceDirNames.clear();
        System.out.println("TESTED DIRECTORY " + sourceDir);
        File[] content = sourceDir.listFiles();
        if (content.length == 0) {
            System.out.println("empty directory ");
            return;
        }
        String[] xmlFiles = new String[content.length];
        File rngFile = null;
        for (int i = 0; i < content.length; i++) {
            String fileName = content[i].getName();
            if (content[i].isDirectory()) {
                if (!fileName.equals("acceptedResults") && !fileName.startsWith(".")) test(content[i]);
            } else {
                if (fileName.equals("schema.rng")) {
                    rngFile = content[i];
                } else if (fileName.endsWith(".xml") && fileName.startsWith("document")) {
                    xmlFiles[getXMLTestNumber(fileName) - 1] = content[i].getAbsolutePath();
                }
            }
        }
        if (rngFile != null) {
            initOutput();
            debugErrorHandler.start(resultsDestinationFileName);
            debugErrorHandler.print("TESTED SCHEMA " + rngFile);
            System.out.println("TESTED SCHEMA " + rngFile);
            if (!initReader(rngFile)) {
                debugErrorHandler.close();
                return;
            }
            for (int i = 0; i < xmlFiles.length; i++) {
                if (xmlFiles[i] != null) {
                    System.out.println("TESTED DOCUMENT " + xmlFiles[i]);
                    validateXMLFile(xmlFiles[i]);
                }
            }
            debugErrorHandler.close();
        } else if (xmlFiles != null) {
            for (int i = 0; i < xmlFiles.length; i++) {
                if (xmlFiles[i] != null) {
                    System.out.println("no schema file ");
                    debugErrorHandler.close();
                    return;
                }
            }
        }
    }

    private void test(File sourceDir) {
        System.out.println("TESTED DIRECTORY " + sourceDir);
        File[] content = sourceDir.listFiles();
        if (content.length == 0) {
            System.out.println("empty directory ");
            return;
        }
        sourceDirNames.push(sourceDir.getName());
        String[] xmlFiles = new String[content.length];
        File rngFile = null;
        for (int i = 0; i < content.length; i++) {
            String fileName = content[i].getName();
            if (content[i].isDirectory()) {
                if (!fileName.startsWith(".")) test(content[i]);
            } else {
                if (fileName.equals("schema.rng")) {
                    rngFile = content[i];
                } else if (fileName.endsWith(".xml") && fileName.startsWith("document")) {
                    xmlFiles[getXMLTestNumber(fileName) - 1] = content[i].getAbsolutePath();
                }
            }
        }
        if (rngFile != null) {
            initOutput();
            debugErrorHandler.start(resultsDestinationFileName);
            debugErrorHandler.print("TESTED SCHEMA " + rngFile);
            System.out.println("TESTED SCHEMA " + rngFile);
            if (!initReader(rngFile)) {
                sourceDirNames.pop();
                debugErrorHandler.close();
                return;
            }
            for (int i = 0; i < xmlFiles.length; i++) {
                if (xmlFiles[i] != null) {
                    System.out.println("TESTED DOCUMENT " + xmlFiles[i]);
                    validateXMLFile(xmlFiles[i]);
                }
            }
            debugErrorHandler.close();
        } else if (xmlFiles != null) {
            for (int i = 0; i < xmlFiles.length; i++) {
                if (xmlFiles[i] != null) {
                    System.out.println("no schema file ");
                    sourceDirNames.pop();
                    debugErrorHandler.close();
                    return;
                }
            }
        }
        sourceDirNames.pop();
    }

    int getXMLTestNumber(String name) {
        int start = name.lastIndexOf("document") + 8;
        int end = name.length() - 4;
        int testNumber = Integer.parseInt(name.substring(start, end));
        return testNumber;
    }

    boolean initReader(File schemaFile) {
        Schema schema;
        debugErrorHandler.init();
        try {
            count++;
            schema = schemaFactory.newSchema(schemaFile);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (debugErrorHandler.hasError()) return false;
        ValidatorHandler vh = schema.newValidatorHandler();
        vh.setErrorHandler(debugErrorHandler);
        xmlReader.setContentHandler(vh);
        return true;
    }

    void initOutput() {
        String s = destinationDirName;
        for (int i = 0; i < sourceDirNames.size(); i++) {
            s = s + File.separator + sourceDirNames.get(i);
        }
        debugErrorHandler.init(s);
        debugWriter.init(s);
    }

    void validateXMLFile(String filePath) {
        String name = filePath.substring(filePath.lastIndexOf(File.separator) + 1, filePath.indexOf(".xml"));
        debugErrorHandler.print("");
        debugErrorHandler.print("TESTED DOCUMENT " + name + ".xml");
        try {
            count++;
            xmlReader.parse(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXParseException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
