package cz.razor.dzemuj.decisiontree;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.SimpleAttributes;
import com.rapidminer.example.set.SimpleExampleSet;
import com.rapidminer.example.table.DataRowFactory;
import com.rapidminer.example.table.ExampleTable;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.example.table.NumericalAttribute;
import com.rapidminer.example.table.PolynominalAttribute;
import com.rapidminer.example.table.SimpleArrayData;
import com.rapidminer.example.table.SimpleArrayDataRowReader;
import com.rapidminer.gui.graphs.GraphViewer;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.learner.tree.DecisionTreeLearner;
import com.rapidminer.operator.learner.tree.TreeModel;
import com.rapidminer.operator.preprocessing.discretization.LabelDiscretizer;
import cz.razor.dzemuj.Texts;

public class DecisionTreeApplet extends JApplet {

    private static final long serialVersionUID = 1L;

    /**
	 * Applet parameter. 
	 * Specifies column index in data loaded from fileAnswersCount
	 * parameter that should be used as a class attribute by default.
	 * Starts from zero.
	 */
    private int CLASS_DEFAULT_INDEX;

    private static Logger logger = Logger.getAnonymousLogger();

    private JPanel contentPane;

    private String errors;

    private JPanel visualizationPanel;

    private JPanel settingsPanel;

    private DecisionTreeLearner learner;

    private JSpinner confidence;

    private JSpinner maxDepth;

    private JSpinner minLeafSize;

    private JComboBox classAttributeComboBox;

    private JSpinner equivalenceClassesNumberSpinner;

    private URL url;

    private IOObject inputDataObject;

    private JComponent rangeComponent;

    public void init() {
        Boolean inputError = new Boolean(Boolean.FALSE);
        final String language = getParameter("language");
        if (language == null) {
            logger.log(Level.INFO, Texts.getString("PARAM_missing") + "language");
            inputError = Boolean.TRUE;
        }
        Texts.init(new Locale(language));
        if (contentPane == null) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        contentPane = new JPanel(new BorderLayout());
                        setContentPane(contentPane);
                    }
                });
            } catch (Exception e) {
                System.err.println(Texts.getString("GUI_contentPane_init_error"));
                e.printStackTrace();
            }
        }
        final String fileQuestions = getParameter("fileQuestions");
        if (fileQuestions == null) {
            logger.log(Level.INFO, Texts.getString("PARAM_missing") + "fileQuestions");
            inputError = Boolean.TRUE;
        }
        final String fileAnswersCount = getParameter("fileAnswersCount");
        if (fileAnswersCount == null) {
            logger.log(Level.INFO, Texts.getString("PARAM_missing") + "fileAnswersCount");
            inputError = Boolean.TRUE;
        }
        final String fileMaxMinPts = getParameter("fileMaxMinPts");
        if (fileMaxMinPts == null) {
            logger.log(Level.INFO, Texts.getString("PARAM_missing") + "fileMaxMinPts");
            inputError = Boolean.TRUE;
        }
        CLASS_DEFAULT_INDEX = Integer.parseInt(getParameter("defaultClassIndex"));
        if (fileQuestions == null) {
            logger.log(Level.INFO, Texts.getString("PARAM_missing") + "fileMaxMinPts");
            inputError = Boolean.TRUE;
        }
        try {
            url = new URL(this.getCodeBase(), fileAnswersCount);
            inputDataObject = loadData2IOObject(url);
            learner = getLearner();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (!inputError) {
                javax.swing.SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        createGUI();
                    }
                });
            } else {
                javax.swing.SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        createErrorGUI();
                    }
                });
            }
        } catch (Exception e) {
            System.err.println(Texts.getString("GUI_createError"));
            e.printStackTrace();
        }
    }

    /**
	 * 
	 * @param url
	 * @return
	 */
    private IOObject loadData2IOObject(URL url) {
        ExampleTable table = null;
        ExampleSet es = null;
        SimpleAttributes attributes = new SimpleAttributes();
        Attribute idAtt = new PolynominalAttribute("Hash");
        attributes.addRegular(idAtt);
        attributes.setId(idAtt);
        attributes.addRegular(new NumericalAttribute("Špatně"));
        attributes.addRegular(new NumericalAttribute("Bez odpovědi"));
        attributes.addRegular(new NumericalAttribute("Správně"));
        attributes.addRegular(new NumericalAttribute("průměr"));
        Attribute labelAtt = new PolynominalAttribute("label");
        attributes.addRegular(labelAtt);
        List<Attribute> attibuteList = new ArrayList<Attribute>();
        attibuteList.add(attributes.getId());
        for (Attribute attribute : attributes) {
            attibuteList.add(attribute);
        }
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            in.readLine();
            Attribute[] attributeArray = new Attribute[5];
            attributeArray = attibuteList.toArray(attributeArray);
            DataRowFactory dataRowFactory = new DataRowFactory(DataRowFactory.TYPE_DOUBLE_ARRAY, '.');
            Iterator<SimpleArrayData> urlArrayDataIterator = new UrlArrayDataIterator(in).iterator();
            SimpleArrayDataRowReader dataRowReader = new SimpleArrayDataRowReader(dataRowFactory, attributeArray, urlArrayDataIterator);
            table = new MemoryExampleTable(attibuteList, dataRowReader);
            in.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<Attribute, String> specialAttributes = new HashMap<Attribute, String>();
        specialAttributes.put(attributes.getId(), "id");
        List<Attribute> regularAttributes = new ArrayList<Attribute>();
        for (Attribute attribute : attributes) {
            regularAttributes.add(attribute);
        }
        es = new SimpleExampleSet(table, regularAttributes, specialAttributes);
        return es;
    }

    /**
	 * Creates the learner used to create tree. Sets its initial parameters
	 * 
	 * @return initialized learner
	 * @throws ClassNotFoundException
	 */
    private DecisionTreeLearner getLearner() throws ClassNotFoundException {
        OperatorDescription operatorDescription = new OperatorDescription("DecisionTreeLearner", "com.rapidminer.operator.learner.tree.DecisionTreeLearner", "description can be written here", null, null, null);
        DecisionTreeLearner l = new DecisionTreeLearner(operatorDescription);
        l.setParameter("confidence", "0.25");
        l.setParameter("no_pruning", "false");
        l.setParameter("minimal_leaf_size", "2");
        l.setParameter("maximal_depth", "10");
        return l;
    }

    /**
	 * 
	 */
    private void createGUI() {
        if (settingsPanel == null) {
            settingsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            ComboBoxModel model = new DefaultComboBoxModel();
            classAttributeComboBox = new JComboBox(model);
            classAttributeComboBox.setRenderer(new ClassSelectorListCellRenderer());
            SimpleExampleSet es = (SimpleExampleSet) inputDataObject;
            Attributes atts = es.getAttributes();
            for (Attribute attribute : atts) {
                classAttributeComboBox.addItem(attribute);
            }
            classAttributeComboBox.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    updateTree();
                }
            });
            settingsPanel.add(new JLabel(Texts.getString("DT_classSelectorTitle")));
            settingsPanel.add(classAttributeComboBox);
            SpinnerModel spinnerModel;
            spinnerModel = new SpinnerNumberModel(4, 2, null, 1);
            equivalenceClassesNumberSpinner = new JSpinner(spinnerModel);
            settingsPanel.add(new JLabel(Texts.getString("DT_nuberOfBins")));
            settingsPanel.add(equivalenceClassesNumberSpinner);
            spinnerModel = new SpinnerNumberModel(0.25, 0, 0.5, 0.1);
            confidence = new JSpinner(spinnerModel);
            confidence.setPreferredSize(new Dimension(50, 20));
            settingsPanel.add(new JLabel(Texts.getString("DT_confidence")));
            settingsPanel.add(confidence);
            spinnerModel = new SpinnerNumberModel(10, 1, null, 1);
            maxDepth = new JSpinner(spinnerModel);
            settingsPanel.add(new JLabel(Texts.getString("DT_maxDepth")));
            settingsPanel.add(maxDepth);
            spinnerModel = new SpinnerNumberModel(2, 1, null, 1);
            minLeafSize = new JSpinner(spinnerModel);
            settingsPanel.add(new JLabel(Texts.getString("DT_minLeafSize")));
            settingsPanel.add(minLeafSize);
            JButton updateButton = new JButton(Texts.getString("DT_updateTreeButton"));
            updateButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    double confidenceValues = Double.parseDouble(confidence.getModel().getValue().toString());
                    if (confidenceValues == 0) {
                        learner.setParameter("no_pruning", "true");
                    }
                    if (confidenceValues > 0 && confidenceValues < 0.5) {
                        learner.setParameter("no_pruning", "false");
                        learner.setParameter("confidence", confidence.getModel().getValue().toString());
                    }
                    learner.setParameter("criterion", "gain_ratio");
                    learner.setParameter("minimal_leaf_size", minLeafSize.getModel().getValue().toString());
                    learner.setParameter("maximal_depth", maxDepth.getModel().getValue().toString());
                    updateTree();
                    contentPane.updateUI();
                }
            });
            settingsPanel.add(updateButton);
            contentPane.add(settingsPanel, BorderLayout.NORTH);
        }
        updateTree();
        contentPane.updateUI();
    }

    /**
	 * 
	 * @param input
	 * 
	 * contentPane has to be already initialized when calling this method
	 */
    private void updateTree() {
        Attribute wannaBeClass = (Attribute) classAttributeComboBox.getSelectedItem();
        int tableIndex;
        if (wannaBeClass != null) {
            tableIndex = wannaBeClass.getTableIndex();
        } else tableIndex = CLASS_DEFAULT_INDEX;
        try {
            inputDataObject = loadData2IOObject(url);
            ExampleSet discretized = discretize((ExampleSet) inputDataObject, tableIndex);
            TreeModel tm = (TreeModel) learner.learn((ExampleSet) discretized);
            if (visualizationPanel != null) {
                contentPane.remove(visualizationPanel);
            }
            TreeModelTreeCreator treeCreator = new TreeModelTreeCreator(tm);
            JComponent compo = rangeComponent;
            compo.setBorder(BorderFactory.createTitledBorder(Texts.getString("DT_rangeDetailsTitle")));
            treeCreator.addOptionComponent(rangeComponent);
            visualizationPanel = (JPanel) new GraphViewer<String, String>(treeCreator);
            contentPane.add(visualizationPanel, BorderLayout.CENTER);
            SimpleExampleSet es = (SimpleExampleSet) discretized;
            Attributes atts = es.getAttributes();
            classAttributeComboBox.removeAllItems();
            for (Attribute attribute : atts) {
                classAttributeComboBox.addItem(attribute);
            }
            classAttributeComboBox.addItem(atts.getLabel());
            classAttributeComboBox.getModel().setSelectedItem(atts.getLabel());
        } catch (OperatorException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        visualizationPanel.updateUI();
        visualizationPanel.validate();
    }

    /**
	 * 
	 * @param input
	 * @return discretized clone of input
	 * @throws ClassNotFoundException
	 */
    private ExampleSet discretize(ExampleSet inputOriginal, int labelTableIndex) throws ClassNotFoundException {
        OperatorDescription operatorDescription = new OperatorDescription("SimpleBinDiscretization", "com.rapidminer.operator.preprocessing.discretization.SimpleBinDiscretization", "... ", null, null, null);
        LabelDiscretizer discretizer = new LabelDiscretizer(operatorDescription);
        SimpleExampleSet es = (SimpleExampleSet) inputOriginal.clone();
        es.recalculateAllAttributeStatistics();
        if (equivalenceClassesNumberSpinner != null) {
            String numberOfBins = equivalenceClassesNumberSpinner.getModel().getValue().toString();
            if (numberOfBins != null && Integer.parseInt(numberOfBins) > 0) {
                try {
                    discretizer.setParameter("number_of_bins", "" + numberOfBins);
                    es = (SimpleExampleSet) discretizer.apply(es, labelTableIndex);
                    rangeComponent = discretizer.getRangeComponent();
                } catch (OperatorException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return es;
    }

    /**
	 * 
	 */
    private void createErrorGUI() {
        JTextPane text = new JTextPane();
        text.setText(errors);
        text.setEditable(Boolean.FALSE);
        JScrollPane sp = new JScrollPane(text);
        contentPane.add(sp, BorderLayout.CENTER);
    }
}
