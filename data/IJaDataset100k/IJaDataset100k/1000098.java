package onto.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ProgressMonitor;
import javax.swing.SpringLayout;
import javax.swing.Timer;
import javax.swing.UIManager;
import org.apache.uima.UIMAException;
import org.apache.uima.UIMAFramework;
import org.apache.uima.UIMARuntimeException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.TypeOrFeature;
import org.apache.uima.analysis_engine.metadata.FixedFlow;
import org.apache.uima.analysis_engine.metadata.SofaMapping;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.collection.CasConsumerDescription;
import org.apache.uima.collection.CollectionProcessingManager;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.collection.EntityProcessStatus;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.metadata.Capability;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.tools.components.FileSystemCollectionReader;
import org.apache.uima.tools.components.XmiWriterCasConsumer;
import org.apache.uima.tools.components.XmlDetagger;
import org.apache.uima.tools.docanalyzer.*;
import org.apache.uima.util.AnalysisEnginePerformanceReports;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.FileUtils;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;

/**
 * A simple GUI for the RunTextAnalysis application library. Note that currently this will only run
 * under Windows since it relies on Windows-specific commands for invoking a web browser to view the
 * annotated documents.
 * 
 * 
 * 
 */
public class DocumentAnalyzerGA {

    private static final long serialVersionUID = 8795969283257780425L;

    private static final String HELP_MESSAGE = "Instructions for using UIMA Document Analyzer:\n\n" + "1) In the \"Input Directory\" field, either type or use the browse\n" + "button to select a directory containing the documents that you want\n" + "to analyze.\n\n" + "2) In the \"Output Directory\" field, either type or use the browse\n" + "button to select a directory where you would like the analyzed\n" + "documents to be placed.\n\n" + "3) In the \"Location of Analysis Engine XML Descriptor\" field, either type or use\n" + "the browse button to select the XML Descriptor file for the Analysis Engine you\n" + "want to use.\n\n" + "4) Optionally, if your input documents are XML files and you only\n" + "want to analyze the contents of a particular tag within those files,\n" + "you may enter in the \"XML Tag Containing Text\" field the name of\n" + "the XML tag that contains the text to be analyzed.\n\n " + "5) In the \"Language\" field, you may select or type the language \n" + "of your input documents.  Some Analysis Engines may require this.\n\n" + "6) Click the \"Run\" button at the buttom of the window.\n\n\n" + "When processing is complete, a list of the analyzed documents will\n" + "be displayed.  Select the view format (Java Viewer is recommended),\n" + "and double-click on a document to view it.\n\n";

    private FileSelector inputFileSelector;

    protected FileSelector outputFileSelector;

    protected FileSelector xmlFileSelector;

    protected String outputFileSelected = null;

    private JTextField runParametersField;

    private JComboBox languageComboBox;

    private JComboBox encodingComboBox;

    private ProgressMonitor progressMonitor;

    protected TypeSystem currentTypeSystem;

    protected String[] currentTaeOutputTypes;

    private File userStyleMapFile;

    protected boolean useGeneratedStyleMap = false;

    private FileSystemCollectionReader collectionReader;

    private CollectionProcessingManager mCPM;

    protected String interactiveTempFN = "__DAtemp__.txt";

    private JDialog aboutDialog;

    private int numDocs;

    private int numDocsProcessed = 0;

    /** Directory in which this program will write its output files. */
    private File outputDirectory;

    private JButton runButton;

    private JButton interButton;

    protected boolean interactive = false;

    private final JRadioButton javaViewerRB = new JRadioButton("Java Viewer");

    private final JRadioButton javaViewerUCRB = new JRadioButton("<html><font color=maroon>JV user colors</font></html>");

    private final JRadioButton htmlRB = new JRadioButton("HTML");

    protected final JRadioButton xmlRB = new JRadioButton("XML");

    protected boolean javaViewerRBisSelected = false;

    protected boolean javaViewerUCRBisSelected = false;

    protected PrefsMediator prefsMed;

    protected String statsString;

    protected File taeDescFile;

    protected String taeDescFileName;

    private File aeSpecifierFile;

    protected CAS cas;

    private Timer progressTimer;

    private boolean usingXmlDetagger;

    /**
   * Constructor. Sets up the GUI.
   */
    public DocumentAnalyzerGA() {
        this(null, false, false);
    }

    public DocumentAnalyzerGA(String outputFileSelected, boolean interactiveDA, boolean jvucrbis) {
        prefsMed = new PrefsMediator();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set look and feel: " + e.getMessage());
        }
    }

    /**
   * Invokes the <code>RunTextAnalysis</code> application library that actually analyzes the
   * documents and generates the output. Displays a progress bar while processing is occuring. When
   * processing is complete, allows the user to view the results. JMP added arg for input text to
   * analyze.
   */
    public void analyzeDocuments(String analysisText) {
        outputFileSelected = outputFileSelector.getSelected();
        File inputDir = new File(inputFileSelector.getSelected());
        if (outputFileSelector.getSelected().length() > 0) outputDirectory = new File(outputFileSelector.getSelected()); else outputDirectory = null;
        String tempFileDir = null;
        if ((analysisText != null) && (outputDirectory != null)) {
            tempFileDir = outputFileSelector.getSelected() + "/interactive_temp";
            inputDir = new File(tempFileDir);
            if (!inputDir.exists()) inputDir.mkdirs();
            outputFileSelected = outputFileSelector.getSelected() + "/interactive_out";
            prefsMed.setOutputDirForInteractiveMode(outputFileSelected, outputFileSelector.getSelected());
            outputDirectory = new File(outputFileSelected);
        } else {
            analysisText = null;
        }
        aeSpecifierFile = new File(xmlFileSelector.getSelected());
        String xmlTag = runParametersField.getText();
        if ("".equals(xmlTag)) {
            xmlTag = null;
        }
        String language = (String) languageComboBox.getSelectedItem();
        String encoding = (String) encodingComboBox.getSelectedItem();
        if (aeSpecifierFile.getName().equals("")) {
            displayError("An Analysis Engine Descriptor is Required");
        } else if (!aeSpecifierFile.exists()) {
            displayError("Analysis Engine Descriptor \"" + xmlFileSelector.getSelected() + "\" does not exist.");
        } else if (aeSpecifierFile.isDirectory()) {
            displayError("The Analysis Engine Descriptor (" + xmlFileSelector.getSelected() + ") must be a file, not a directory.");
        } else if (inputDir.getName().equals("")) {
            displayError("An Input Directory is Required");
        } else if (!inputDir.exists()) {
            displayError("The input directory \"" + inputFileSelector.getSelected() + "\" does not exist.");
        } else if (!inputDir.isDirectory()) {
            displayError("The input directory (" + inputFileSelector.getSelected() + ") must be a directory, not a file.");
        } else if (outputDirectory != null && (!outputDirectory.exists() && !outputDirectory.mkdirs()) || !outputDirectory.isDirectory()) {
            displayError("The output directory \"" + outputFileSelector.getSelected() + "\" does not exist and could not be created.");
        } else if (inputDir.equals(outputDirectory)) {
            displayError("The input directory and the output directory must be different.");
        } else {
            if (analysisText != null) {
                File[] filesInOutDir = inputDir.listFiles();
                for (int i = 0; i < filesInOutDir.length; i++) {
                    if (!filesInOutDir[i].isDirectory()) {
                        filesInOutDir[i].delete();
                    }
                }
                File tempFile = new File(tempFileDir + "/" + interactiveTempFN);
                PrintWriter writer;
                try {
                    writer = new PrintWriter(new BufferedWriter(new FileWriter(tempFile)));
                    writer.println(analysisText);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            File[] filesInOutDir = outputDirectory.listFiles();
            for (int i = 0; i < filesInOutDir.length; i++) {
                if (!filesInOutDir[i].isDirectory()) {
                    String filename = filesInOutDir[i].getName();
                    if (filename.endsWith(".xmi")) {
                        filename = filename.substring(0, filename.length() - 4);
                    }
                    if (!new File(inputDir, filename).exists()) {
                    }
                }
            }
            for (int i = 0; i < filesInOutDir.length; i++) {
                if (!filesInOutDir[i].isDirectory()) {
                    filesInOutDir[i].delete();
                }
            }
            ProcessingThread thread = new ProcessingThread(inputDir, outputDirectory, aeSpecifierFile, xmlTag, language, encoding);
            thread.start();
        }
    }

    private void checkProgressMonitor() {
        if (progressMonitor.isCanceled()) {
            progressMonitor.setNote("Cancelling...");
            mCPM.stop();
            aborted();
        }
    }

    /**
   * @see org.apache.uima.collection.StatusCallbackListener#entityProcessComplete(org.apache.uima.cas.CAS,
   *      org.apache.uima.collection.EntityProcessStatus)
   */
    public void entityProcessComplete(CAS aCas, EntityProcessStatus aStatus) {
        if (aStatus.isException()) {
            displayError((Throwable) aStatus.getExceptions().get(0));
        }
        numDocsProcessed++;
        progressMonitor.setProgress(numDocsProcessed + 2);
        progressMonitor.setNote("Processed " + numDocsProcessed + " of " + numDocs + " documents.");
    }

    /**
   * @see org.apache.uima.collection.base_cpm.BaseStatusCallbackListener#aborted()
   */
    public void aborted() {
    }

    /**
   * @see org.apache.uima.collection.base_cpm.BaseStatusCallbackListener#batchProcessComplete()
   */
    public void batchProcessComplete() {
    }

    /**
   * @see org.apache.uima.collection.base_cpm.BaseStatusCallbackListener#collectionProcessComplete()
   */
    public void collectionProcessComplete() {
        if (!progressMonitor.isCanceled()) {
            progressMonitor.close();
            progressTimer.stop();
        }
    }

    /**
   * @see org.apache.uima.collection.base_cpm.BaseStatusCallbackListener#initializationComplete()
   */
    public void initializationComplete() {
    }

    /**
   * @see org.apache.uima.collection.base_cpm.BaseStatusCallbackListener#paused()
   */
    public void paused() {
    }

    /**
   * @see org.apache.uima.collection.base_cpm.BaseStatusCallbackListener#resumed()
   */
    public void resumed() {
    }

    public void showAnalysisResults(File aOutputDir) {
        try {
            cas = createCasFromDescriptor(prefsMed.getTAEfile());
            currentTypeSystem = cas.getTypeSystem();
        } catch (Exception e) {
            displayError(e);
        }
        statsString = null;
        if (prefsMed.getStylemapFile().exists()) {
        }
        show_analysis(aOutputDir);
    }

    /**
   * Creates a CAS from an descriptor. Supports both local AE descriptors and remote service
   * specifiers. In the latter case the service is contacted to obtain its type system.
   * 
   * @throws ResourceInitializationException
   * @throws InvalidXMLException
   * @throws IOException
   */
    protected CAS createCasFromDescriptor(String aDescriptorFile) throws ResourceInitializationException, InvalidXMLException, IOException {
        ResourceSpecifier spec = UIMAFramework.getXMLParser().parseResourceSpecifier(new XMLInputSource(aDescriptorFile));
        if (spec instanceof AnalysisEngineDescription) {
            return CasCreationUtils.createCas((AnalysisEngineDescription) spec);
        } else {
            AnalysisEngine currentAe = UIMAFramework.produceAnalysisEngine(spec);
            return CasCreationUtils.createCas(currentAe.getAnalysisEngineMetaData());
        }
    }

    protected String readStylemapFile(File smapFile) {
        String styleMapXml = "";
        if (smapFile.exists()) {
            try {
                return FileUtils.file2String(smapFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return styleMapXml;
    }

    private void show_analysis(File outputDir) {
        File styleMapFile = getStyleMapFile();
        if (styleMapFile == null) {
            styleMapFile = prefsMed.getStylemapFile();
        }
        if (interactive) {
        } else {
            if (usingXmlDetagger) {
            }
        }
    }

    /**
   * Save user's preferences using Java's Preference API.
   */
    public void savePreferences() {
        prefsMed.setInputDir(inputFileSelector.getSelected());
        prefsMed.setOutputDir(outputFileSelector.getSelected());
        prefsMed.setTAEfile(xmlFileSelector.getSelected());
        prefsMed.setXmlTag(runParametersField.getText());
        prefsMed.setLanguage((String) languageComboBox.getSelectedItem());
        prefsMed.setEncoding((String) encodingComboBox.getSelectedItem());
    }

    /**
   * Reset GUI to preferences last saved via {@link #savePreferences}.
   */
    public void restorePreferences() {
        prefsMed.restorePreferences();
    }

    /**
   * Displays an error message to the user.
   * 
   * @param aErrorString
   *          error message to display
   */
    public void displayError(String aErrorString) {
        StringBuffer buf = new StringBuffer(aErrorString.length());
        final int CHARS_PER_LINE = 80;
        int charCount = 0;
        StringTokenizer tokenizer = new StringTokenizer(aErrorString, " \n", true);
        while (tokenizer.hasMoreTokens()) {
            String tok = tokenizer.nextToken();
            if (tok.equals("\n")) {
                buf.append("\n");
                charCount = 0;
            } else if ((charCount > 0) && ((charCount + tok.length()) > CHARS_PER_LINE)) {
                buf.append("\n").append(tok);
                charCount = tok.length();
            } else {
                buf.append(tok);
                charCount += tok.length();
            }
        }
    }

    /**
   * Displays an error message to the user.
   * 
   * @param aThrowable
   *          Throwable whose message is to be displayed.
   */
    public void displayError(Throwable aThrowable) {
        aThrowable.printStackTrace();
        String message = aThrowable.toString();
        while ((aThrowable instanceof UIMAException) || (aThrowable instanceof UIMARuntimeException)) {
            if (aThrowable instanceof UIMAException) {
                aThrowable = ((UIMAException) aThrowable).getCause();
            } else if (aThrowable instanceof UIMARuntimeException) {
                aThrowable = ((UIMARuntimeException) aThrowable).getCause();
            }
            if (aThrowable != null) {
                message += ("\nCausedBy: " + aThrowable.toString());
            }
        }
        displayError(message);
    }

    /**
   * Runs the application.
   */
    public static void main(String[] args) {
        final DocumentAnalyzerGA frame = new DocumentAnalyzerGA();
        File inputDir = new File("/Users/ahamed/DLink/");
        File outputDir = new File("/Users/ahamed/ontotatorOutput/");
        File taeSpecifierFile = new File("/Users/ahamed/Projects/workspace/ontotator-model-plugin/src/onto/resources/descriptors/analysis_engine/aggregate/USER_ASSEMBLED_AGGREGATE_AE.xml");
        System.out.println("|==> input, output, desc stuff \n" + inputDir + "\n" + outputDir + "\n" + taeSpecifierFile);
        frame.runProcessingThread(inputDir, outputDir, taeSpecifierFile, "", "english", "");
    }

    public void runProcessingThread(File inputDir, File outputDir, File aeSpecifierFile, String xmlTag, String language, String encoding) {
        try {
            System.out.println("1");
            CollectionReaderDescription collectionReaderDesc = FileSystemCollectionReader.getDescription();
            ConfigurationParameterSettings paramSettings = collectionReaderDesc.getMetaData().getConfigurationParameterSettings();
            paramSettings.setParameterValue(FileSystemCollectionReader.PARAM_INPUTDIR, inputDir.getAbsolutePath());
            paramSettings.setParameterValue(FileSystemCollectionReader.PARAM_LANGUAGE, language);
            paramSettings.setParameterValue(FileSystemCollectionReader.PARAM_ENCODING, encoding);
            collectionReader = (FileSystemCollectionReader) UIMAFramework.produceCollectionReader(collectionReaderDesc);
            String progressMsg = "  Processing " + collectionReader.getNumberOfDocuments() + " Documents.";
            mCPM = UIMAFramework.newCollectionProcessingManager();
            XMLInputSource in = new XMLInputSource(aeSpecifierFile);
            ResourceSpecifier aeSpecifier = UIMAFramework.getXMLParser().parseResourceSpecifier(in);
            CasConsumerDescription casConsumerDesc = XmiWriterCasConsumer.getDescription();
            ConfigurationParameterSettings consumerParamSettings = casConsumerDesc.getMetaData().getConfigurationParameterSettings();
            consumerParamSettings.setParameterValue(XmiWriterCasConsumer.PARAM_OUTPUTDIR, outputDir.getAbsolutePath());
            casConsumerDesc.getCasConsumerMetaData().getCapabilities()[0].addInputType("uima.cas.TOP", true);
            System.out.println("2");
            AnalysisEngineDescription xmlDetaggerDesc = null;
            if (xmlTag != null && xmlTag.length() > 0) {
                xmlDetaggerDesc = XmlDetagger.getDescription();
                ConfigurationParameterSettings xmlDetaggerParamSettings = xmlDetaggerDesc.getMetaData().getConfigurationParameterSettings();
                xmlDetaggerParamSettings.setParameterValue(XmlDetagger.PARAM_TEXT_TAG, xmlTag);
                usingXmlDetagger = true;
                System.out.println("3");
            } else {
                usingXmlDetagger = false;
            }
            AnalysisEngineDescription aggDesc = UIMAFramework.getResourceSpecifierFactory().createAnalysisEngineDescription();
            aggDesc.setPrimitive(false);
            aggDesc.getDelegateAnalysisEngineSpecifiersWithImports().put("UserAE", aeSpecifier);
            aggDesc.getDelegateAnalysisEngineSpecifiersWithImports().put("XmiWriter", casConsumerDesc);
            FixedFlow flow = UIMAFramework.getResourceSpecifierFactory().createFixedFlow();
            if (xmlDetaggerDesc != null) {
                System.out.println("4");
                aggDesc.getDelegateAnalysisEngineSpecifiersWithImports().put("XmlDetagger", xmlDetaggerDesc);
                flow.setFixedFlow(new String[] { "XmlDetagger", "UserAE", "XmiWriter" });
                SofaMapping sofaMapping1 = UIMAFramework.getResourceSpecifierFactory().createSofaMapping();
                sofaMapping1.setComponentKey("XmlDetagger");
                sofaMapping1.setComponentSofaName("xmlDocument");
                sofaMapping1.setAggregateSofaName(CAS.NAME_DEFAULT_SOFA);
                SofaMapping sofaMapping2 = UIMAFramework.getResourceSpecifierFactory().createSofaMapping();
                sofaMapping2.setComponentKey("UserAE");
                sofaMapping2.setAggregateSofaName("plainTextDocument");
                SofaMapping sofaMapping3 = UIMAFramework.getResourceSpecifierFactory().createSofaMapping();
                sofaMapping3.setComponentKey("XmiWriter");
                sofaMapping3.setAggregateSofaName("plainTextDocument");
                aggDesc.setSofaMappings(new SofaMapping[] { sofaMapping1, sofaMapping2, sofaMapping3 });
            } else {
                flow.setFixedFlow(new String[] { "UserAE", "XmiWriter" });
            }
            aggDesc.getAnalysisEngineMetaData().setName("DocumentAnalyzerAE");
            aggDesc.getAnalysisEngineMetaData().setFlowConstraints(flow);
            aggDesc.getAnalysisEngineMetaData().getOperationalProperties().setMultipleDeploymentAllowed(false);
            AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(aggDesc);
            mCPM.setAnalysisEngine(ae);
            List descriptorList = new ArrayList();
            descriptorList.add(collectionReaderDesc);
            descriptorList.add(ae.getMetaData());
            descriptorList.add(casConsumerDesc);
            cas = CasCreationUtils.createCas(descriptorList);
            currentTypeSystem = cas.getTypeSystem();
            if (aeSpecifier instanceof AnalysisEngineDescription) {
                ArrayList outputTypeList = new ArrayList();
                Capability[] capabilities = ((AnalysisEngineDescription) aeSpecifier).getAnalysisEngineMetaData().getCapabilities();
                for (int i = 0; i < capabilities.length; i++) {
                    TypeOrFeature[] outputs = capabilities[i].getOutputs();
                    for (int j = 0; j < outputs.length; j++) {
                        if (outputs[j].isType()) {
                            outputTypeList.add(outputs[j].getName());
                            Type t = currentTypeSystem.getType(outputs[j].getName());
                            if (t != null) {
                                List subsumedTypes = currentTypeSystem.getProperlySubsumedTypes(t);
                                Iterator it = subsumedTypes.iterator();
                                while (it.hasNext()) {
                                    outputTypeList.add(((Type) it.next()).getName());
                                }
                            }
                        }
                    }
                }
                outputTypeList.add("uima.tcas.DocumentAnnotation");
                currentTaeOutputTypes = new String[outputTypeList.size()];
                outputTypeList.toArray(currentTaeOutputTypes);
            } else {
                currentTaeOutputTypes = null;
            }
            System.out.println("5");
            mCPM.process(collectionReader);
            System.out.println("6");
        } catch (Throwable t) {
            if (usingXmlDetagger && (t instanceof UIMAException) && ((UIMAException) t).hasMessageKey(ResourceInitializationException.SOFA_MAPPING_NOT_SUPPORTED_FOR_REMOTE)) {
                displayError("The XML detagging feature is not supported for remote Analysis Engines or for Aggregates containing remotes.  " + "If you are running a remote Analysis Engine the \"XML Tag Containing Text\" field must be left blank.");
            } else {
                displayError(t);
            }
            aborted();
        }
    }

    class ProcessingThread extends Thread {

        File inputDir;

        File outputDir;

        File taeSpecifierFile;

        String xmlTag;

        String language;

        String encoding;

        ProcessingThread(File inputDir, File outputDir, File taeSpecifierFile, String xmlTag, String language, String encoding) {
            this.inputDir = inputDir;
            this.outputDir = outputDir;
            this.taeSpecifierFile = taeSpecifierFile;
            this.xmlTag = xmlTag;
            this.language = language;
            this.encoding = encoding;
            System.out.println("9");
        }

        public void run() {
            inputDir = new File("/Users/ahamed/DLink/");
            outputDir = new File("/Users/ahamed/ontotatorOutput/");
            taeSpecifierFile = new File("/Users/ahamed/Projects/workspace/ontotator-model-plugin/src/onto/resources/descriptors/analysis_engine/aggregate/USER_ASSEMBLED_AGGREGATE_AE.xml");
            System.out.println("7");
            runProcessingThread(inputDir, outputDir, taeSpecifierFile, xmlTag, language, encoding);
            System.out.println("8");
        }
    }

    /**
   * @return Returns the styleMapFile.
   */
    public File getStyleMapFile() {
        if (userStyleMapFile == null && prefsMed != null) {
            return prefsMed.getStylemapFile();
        }
        return userStyleMapFile;
    }

    /**
   * @param styleMapFile
   *          The styleMapFile to set.
   */
    public void setStyleMapFile(File styleMapFile) {
        this.userStyleMapFile = styleMapFile;
    }
}
