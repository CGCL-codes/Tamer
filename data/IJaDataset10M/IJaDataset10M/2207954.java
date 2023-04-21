package net.saim.gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import net.saim.algorithms.SimpleLearner;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * 
 * @author Konrad Höffner
 *
 */
public class MainFrame extends JFrame {

    private static final long serialVersionUID = 5210615961678522226L;

    static List<String> actionButtonTexts = new ArrayList<String>();

    private JMenuBar menuBar = new JMenuBar();

    private JMenu fileMenu = new JMenu("File");

    JMenuItem loadXMLItem = new JMenuItem("Load XML");

    JMenuItem saveXMLItem = new JMenuItem("Save XML");

    private JMenu infoMenu = new JMenu("Info");

    JMenuItem openDevelopementPlanItem = new JMenuItem("Open development plan");

    private JTabbedPane tabbedPane = new JTabbedPane();

    JPanel mainPanel = new JPanel();

    Map<String, DataSourcePanel> dataSourcePanels = new HashMap<String, DataSourcePanel>();

    PrefixPanelTextArea prefixPanel = new PrefixPanelTextArea();

    LearnPanel learnPanel = new LearnPanel();

    JButton previewButton = new JButton("Preview");

    JButton matchButton = new JButton("Match");

    InterlinkPanel interlinkPanel = new InterlinkPanel();

    JProgressBar progressBar = new JProgressBar();

    private ActionListener listener = new MainFrameActionListener(this);

    class MainFrameWindowListener extends JFrame implements WindowListener {

        private static final long serialVersionUID = -7357063880876236479L;

        @Override
        public void windowClosing(WindowEvent arg0) {
            File tempFolder = new File("temp");
            if (!tempFolder.exists()) tempFolder.mkdir();
            saveXML(new File("temp/autosave.xml"));
            try {
                learnPanel.saveState(new File("temp/learning.txt"));
                System.out.println("Successfully autosaved learning state in file \"temp/learning.txt\"");
            } catch (FileNotFoundException e) {
                System.out.println("Problem autosaving learning state: " + e);
            }
        }

        @Override
        public void windowActivated(WindowEvent arg0) {
        }

        @Override
        public void windowClosed(WindowEvent arg0) {
        }

        @Override
        public void windowDeactivated(WindowEvent arg0) {
        }

        @Override
        public void windowDeiconified(WindowEvent arg0) {
        }

        @Override
        public void windowIconified(WindowEvent arg0) {
        }

        @Override
        public void windowOpened(WindowEvent e) {
        }
    }

    public MainFrame() {
        super("SAIM");
        this.setLocation(100, 100);
        this.addWindowListener(new MainFrameWindowListener());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(800, 700));
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        fileMenu.add(loadXMLItem);
        loadXMLItem.addActionListener(listener);
        fileMenu.add(saveXMLItem);
        saveXMLItem.addActionListener(listener);
        menuBar.add(fileMenu);
        openDevelopementPlanItem.addActionListener(listener);
        infoMenu.add(openDevelopementPlanItem);
        menuBar.add(infoMenu);
        this.setJMenuBar(menuBar);
        this.getContentPane().add(tabbedPane);
        for (int i = 0; i <= 1; i++) {
            DataSourcePanel dataSourcePanel = new DataSourcePanel();
            dataSourcePanels.put(dataSourcePanel.idField.getText(), dataSourcePanel);
            mainPanel.add(dataSourcePanel);
        }
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        updateMainPanel();
        tabbedPane.addTab("main panel", mainPanel);
        previewButton.addActionListener(listener);
        this.getContentPane().add(previewButton);
        matchButton.addActionListener(listener);
        this.getContentPane().add(matchButton);
        progressBar.setIndeterminate(false);
        progressBar.setMaximum(100);
        progressBar.setValue(0);
        this.getContentPane().add(progressBar);
        this.pack();
        try {
            loadXML(new File("temp/autosave.xml"));
            System.out.println("Successfully loaded autosave file \"temp/autosave.xml\"");
        } catch (FileNotFoundException e) {
            System.out.println("Autosave file not found.");
        }
    }

    void match() {
        this.saveXML(new File("temp.xml"));
        new Thread(new Runnable() {

            @Override
            public void run() {
                progressBar.setIndeterminate(true);
                matchButton.setEnabled(false);
                System.out.println("starting matching thread...");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("stopping matching thread...");
                progressBar.setIndeterminate(false);
                progressBar.setValue(100);
                matchButton.setEnabled(true);
            }
        }).start();
    }

    public void preview() {
        DataSourcePanel sourcePanel = dataSourcePanels.get(interlinkPanel.sourceDatasetTextField.getText());
        DataSourcePanel targetPanel = dataSourcePanels.get(interlinkPanel.targetDatasetTextField.getText());
        String sourceEndpoint = sourcePanel.endpointField.getText();
        String sourceGraph = sourcePanel.graphField.getText();
        String sourceRestriction = interlinkPanel.sourceRestrictionTextField.getText();
        String targetEndpoint = targetPanel.endpointField.getText();
        String targetGraph = targetPanel.graphField.getText();
        String targetRestriction = interlinkPanel.targetRestrictionTextField.getText();
        try {
            URI uri1 = new java.net.URI(sourceEndpoint + "?query=" + URLEncoder.encode(prefixPanel.getPrefixSPARQLString() + "select * where {" + sourceRestriction + "} limit 10", "UTF8") + "&default-graph-uri=" + sourceGraph);
            URI uri2 = new java.net.URI(targetEndpoint + "?query=" + URLEncoder.encode(prefixPanel.getPrefixSPARQLString() + "select * where {" + targetRestriction + "} limit 10", "UTF8") + "&default-graph-uri=" + targetGraph);
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            desktop.browse(uri1);
            desktop.browse(uri2);
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }
    }

    public void loadState(File file) {
        try {
            ObjectInputStream d = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
            prefixPanel.loadState(d);
            d.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Loading state not successful (" + e + ").");
        }
    }

    public void saveState(File selectedFile) {
        try {
            ObjectOutputStream e = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(selectedFile)));
            prefixPanel.saveState(e);
            e.close();
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(this, "Saving state not successful (" + e1 + ").");
        }
    }

    public Element paramElement(String name, String value) {
        Element p = new Element("Param");
        p.setAttribute("name", name);
        p.setAttribute("value", value);
        return p;
    }

    public Element inputElement(String path) {
        Element e = new Element("Input");
        e.setAttribute("path", path);
        return e;
    }

    /**
	 * Creates an XML LSL (Link Specification Language?) file out of the various
	 * form input values and saves it to the given file.
	 */
    public void saveXML(File file) {
        Element rootElement = new Element("Silk");
        Document doc = new Document(rootElement);
        Element prefixesElement = new Element("Prefixes");
        rootElement.addContent(prefixesElement);
        Map<String, String> prefixes = prefixPanel.getPrefixes();
        for (String id : prefixes.keySet()) {
            Element prefixElement = new Element("Prefix");
            prefixesElement.addContent(prefixElement);
            Attribute idAttribute = new Attribute("id", id);
            Attribute namespaceAttribute = new Attribute("namespace", prefixes.get(id));
            prefixElement.setAttribute(idAttribute);
            prefixElement.setAttribute(namespaceAttribute);
        }
        Element dataSourcesElement = new Element("DataSources");
        rootElement.addContent(dataSourcesElement);
        for (DataSourcePanel dsPanel : dataSourcePanels.values()) {
            Element dataSourceElement = new Element("DataSource");
            dataSourcesElement.addContent(dataSourceElement);
            dataSourceElement.setAttribute("id", dsPanel.idField.getText().trim());
            dataSourceElement.setAttribute("type", "sparqlEndpoint");
            Scanner previewInstances = new Scanner(dsPanel.previewInstancesTextArea.getText());
            StringBuffer previewInstancesStringBuffer = new StringBuffer();
            while (previewInstances.hasNext()) {
                String previewInstance = previewInstances.next().trim();
                if (previewInstances.equals("")) continue;
                if (previewInstancesStringBuffer.length() > 0) previewInstancesStringBuffer.append(" ");
                previewInstancesStringBuffer.append(previewInstance);
            }
            if (previewInstancesStringBuffer.length() > 0) dataSourceElement.setAttribute("instanceList", previewInstancesStringBuffer.toString());
            dataSourceElement.addContent(paramElement("endpointURI", dsPanel.endpointField.getText().trim()));
            dataSourceElement.addContent(paramElement("graph", dsPanel.graphField.getText().trim()));
        }
        Element interlinksElement = new Element("Interlinks");
        rootElement.addContent(interlinksElement);
        Element interlinkElement = new Element("Interlink");
        interlinksElement.addContent(interlinkElement);
        interlinkElement.setAttribute("id", interlinkPanel.interlinkIDTextField.getText());
        Element linkTypeElement = new Element("LinkType");
        interlinkElement.addContent(linkTypeElement);
        linkTypeElement.addContent(interlinkPanel.linkTypeTextField.getText());
        String[] datasetIDs = { interlinkPanel.sourceDatasetTextField.getText(), interlinkPanel.targetDatasetTextField.getText() };
        for (int i = 0; i <= 1; i++) {
            Element datasetElement = new Element(i == 0 ? "SourceDataset" : "TargetDataset");
            interlinkElement.addContent(datasetElement);
            datasetElement.setAttribute("dataSource", datasetIDs[i]);
            datasetElement.setAttribute("var", (i == 0 ? interlinkPanel.sourceVarTextField.getText() : interlinkPanel.targetVarTextField.getText()).trim());
            Element restrictToElement = new Element("RestrictTo");
            datasetElement.addContent(restrictToElement);
            restrictToElement.addContent(i == 0 ? interlinkPanel.sourceRestrictionTextField.getText() : interlinkPanel.targetRestrictionTextField.getText().trim());
        }
        Element blockingElement = new Element("Blocking");
        interlinkElement.addContent(blockingElement);
        blockingElement.setAttribute("blocks", interlinkPanel.blockingNumberTextField.getText());
        blockingElement.setAttribute("function", interlinkPanel.blockingFunctionTextField.getText());
        Element sourceInputElement = new Element("Input");
        sourceInputElement.setAttribute("path", interlinkPanel.sourceBlockingPropertyTextField.getText());
        blockingElement.addContent(sourceInputElement);
        Element targetInputElement = new Element("Input");
        targetInputElement.setAttribute("path", interlinkPanel.targetBlockingPropertyTextField.getText());
        blockingElement.addContent(targetInputElement);
        Element linkConditionElement = new Element("LinkCondition");
        interlinkElement.addContent(linkConditionElement);
        Element aggregateElement = new Element("Aggregate");
        linkConditionElement.addContent(aggregateElement);
        aggregateElement.setAttribute("type", "average");
        Element compareElement = new Element("Compare");
        aggregateElement.addContent(compareElement);
        compareElement.setAttribute("metric", "jaro");
        for (DataSourcePanel dsPanel : dataSourcePanels.values()) {
            compareElement.addContent(inputElement("?" + dsPanel.idField.getText() + "/rdfs:label"));
        }
        Element filterElement = new Element("Filter");
        interlinkElement.addContent(filterElement);
        filterElement.setAttribute("threshold", "0.9");
        Element outputsElement = new Element("Outputs");
        rootElement.addContent(outputsElement);
        Element outputElement = new Element("Output");
        outputsElement.addContent(outputElement);
        outputElement.setAttribute("type", "file");
        outputElement.setAttribute("minConfidence", "0.9");
        Element paramElement1 = new Element("Param");
        outputElement.addContent(paramElement1);
        paramElement1.setAttribute("name", "format");
        paramElement1.setAttribute("value", "ntriples");
        Element paramElement2 = new Element("Param");
        outputElement.addContent(paramElement2);
        paramElement2.setAttribute("name", "file");
        paramElement2.setAttribute("value", "accepted_links.nt");
        XMLOutputter out = new XMLOutputter();
        try {
            out.output(doc, new FileOutputStream(file));
        } catch (IOException e) {
            SwingHelper.showExceptionDialog(this, e, "Error saving image of the chart.");
        }
    }

    @SuppressWarnings("unchecked")
    public void loadXML(File file) throws FileNotFoundException {
        if (!file.exists()) throw new FileNotFoundException(file.toString());
        for (DataSourcePanel dsPanel : this.dataSourcePanels.values()) {
            mainPanel.remove(dsPanel);
            mainPanel.updateUI();
        }
        this.dataSourcePanels.clear();
        Document doc = null;
        try {
            doc = new SAXBuilder().build(file);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element rootElement = doc.getRootElement();
        Element prefixesElement = rootElement.getChild("Prefixes");
        Map<String, String> prefixes = new HashMap<String, String>();
        for (Object prefixElement : prefixesElement.getChildren("Prefix")) {
            prefixes.put(((Element) prefixElement).getAttribute("id").getValue(), ((Element) prefixElement).getAttribute("namespace").getValue());
        }
        prefixPanel.setPrefixes(prefixes);
        Element dataSourcesElement = rootElement.getChild("DataSources");
        List<Element> dataSourceElementObjects = dataSourcesElement.getChildren("DataSource");
        for (int dsIndex = 0; dsIndex < dataSourceElementObjects.size(); dsIndex++) {
            Element dataSourceElement = dataSourceElementObjects.get(dsIndex);
            DataSourcePanel dsPanel = new DataSourcePanel();
            dsPanel.idField.setText(dataSourceElement.getAttribute("id").getValue());
            String instanceListString = dataSourceElement.getAttributeValue("instanceList");
            if (instanceListString != null) {
                String[] instances = instanceListString.split(" ");
                StringBuffer text = new StringBuffer();
                for (String instance : instances) {
                    text.append(instance + '\n');
                }
                dsPanel.previewInstancesTextArea.setText(text.toString());
            } else dsPanel.previewInstancesTextArea.setText("");
            List<Element> params = dataSourceElement.getChildren("Param");
            for (Element param : params) {
                if (param.getAttributeValue("name").equals("endpointURI")) dsPanel.endpointField.setText(param.getAttributeValue("value")); else if (param.getAttributeValue("name").equals("graph")) dsPanel.graphField.setText(param.getAttributeValue("value")); else throw new RuntimeException("Invalid data source parameter in XML: " + param.getAttributeValue("name"));
            }
            dataSourcePanels.put(dsPanel.idField.getText(), dsPanel);
            updateMainPanel();
        }
        Element interlinksElement = rootElement.getChild("Interlinks");
        Element interlinkElement = interlinksElement.getChild("Interlink");
        String interlinkID = interlinkElement.getAttributeValue("id");
        interlinkPanel.interlinkIDTextField.setText(interlinkID);
        Element linkTypeElement = interlinkElement.getChild("LinkType");
        String linkType = linkTypeElement.getText().trim();
        interlinkPanel.linkTypeTextField.setText(linkType);
        Element sourceDatasetElement = interlinkElement.getChild("SourceDataset");
        interlinkPanel.sourceDatasetTextField.setText(sourceDatasetElement.getAttributeValue("dataSource"));
        interlinkPanel.sourceVarTextField.setText(sourceDatasetElement.getAttributeValue("var"));
        Element sourceRestrictToElement = sourceDatasetElement.getChild("RestrictTo");
        String sourceRestriction = sourceRestrictToElement.getText().trim();
        interlinkPanel.sourceRestrictionTextField.setText(sourceRestriction);
        Element targetDatasetElement = interlinkElement.getChild("TargetDataset");
        interlinkPanel.targetDatasetTextField.setText(targetDatasetElement.getAttributeValue("dataSource"));
        interlinkPanel.targetVarTextField.setText(targetDatasetElement.getAttributeValue("var"));
        Element targetRestrictToElement = targetDatasetElement.getChild("RestrictTo");
        String targetRestriction = targetRestrictToElement.getText().trim();
        interlinkPanel.targetRestrictionTextField.setText(targetRestriction);
        Element blockingElement = interlinkElement.getChild("Blocking");
        interlinkPanel.blockingNumberTextField.setText(blockingElement.getAttributeValue("blocks"));
        interlinkPanel.blockingFunctionTextField.setText(blockingElement.getAttributeValue("function"));
        interlinkPanel.sourceBlockingPropertyTextField.setText(((Element) blockingElement.getChildren("Input").get(0)).getAttributeValue("path"));
        interlinkPanel.targetBlockingPropertyTextField.setText(((Element) blockingElement.getChildren("Input").get(1)).getAttributeValue("path"));
    }

    private void updateMainPanel() {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                mainPanel.removeAll();
                for (JPanel dataSourcePanel : dataSourcePanels.values()) {
                    mainPanel.add(dataSourcePanel);
                }
                mainPanel.add(prefixPanel);
                mainPanel.updateUI();
            }
        });
    }
}
