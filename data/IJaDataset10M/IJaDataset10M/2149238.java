package net.sf.mzmine.modules.batchmode;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.sf.mzmine.main.MZmineCore;
import net.sf.mzmine.modules.MZmineModule;
import net.sf.mzmine.modules.MZmineModuleCategory;
import net.sf.mzmine.modules.MZmineProcessingModule;
import net.sf.mzmine.modules.MZmineProcessingStep;
import net.sf.mzmine.modules.impl.MZmineModuleWrapper;
import net.sf.mzmine.modules.impl.MZmineProcessingStepImpl;
import net.sf.mzmine.parameters.ParameterSet;
import net.sf.mzmine.util.ExitCode;
import net.sf.mzmine.util.GUIUtils;
import net.sf.mzmine.util.components.DragOrderedJList;
import net.sf.mzmine.util.dialogs.LoadSaveFileChooser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import de.schlichtherle.io.FileOutputStream;

public class BatchSetupComponent extends JPanel implements ActionListener {

    private static final Logger LOG = Logger.getLogger(BatchSetupComponent.class.getName());

    private static final String XML_EXTENSION = "xml";

    private enum QueueOperations {

        Replace, Prepend, Insert, Append
    }

    private BatchQueue batchQueue;

    private final JComboBox methodsCombo;

    private final JList currentStepsList;

    private final JButton btnAdd;

    private final JButton btnConfig;

    private final JButton btnRemove;

    private final JButton btnClear;

    private final JButton btnLoad;

    private final JButton btnSave;

    private LoadSaveFileChooser chooser;

    /**
     * Create the component.
     */
    public BatchSetupComponent() {
        super(new BorderLayout());
        batchQueue = new BatchQueue();
        chooser = new LoadSaveFileChooser("Select Batch Queue File");
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("XML files", XML_EXTENSION));
        currentStepsList = new DragOrderedJList();
        currentStepsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        methodsCombo = new JComboBox();
        final Collection<MZmineModule> allModules = MZmineCore.getAllModules();
        for (final MZmineModuleCategory category : MZmineModuleCategory.values()) {
            boolean categoryItemAdded = false;
            for (final MZmineModule module : allModules) {
                if (!module.getClass().equals(BatchModeModule.class) && module instanceof MZmineProcessingModule) {
                    final MZmineProcessingModule step = (MZmineProcessingModule) module;
                    if (step.getModuleCategory() == category) {
                        if (!categoryItemAdded) {
                            methodsCombo.addItem("--- " + category + " ---");
                            categoryItemAdded = true;
                        }
                        MZmineModuleWrapper wrappedModule = new MZmineModuleWrapper(step);
                        methodsCombo.addItem(wrappedModule);
                    }
                }
            }
        }
        final JPanel panelTop = new JPanel();
        panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.X_AXIS));
        btnLoad = GUIUtils.addButton(panelTop, "Load...", null, this, "LOAD", "Loads a batch queue from a file");
        btnSave = GUIUtils.addButton(panelTop, "Save...", null, this, "SAVE", "Saves a batch queue to a file");
        final JPanel pnlRight = new JPanel();
        pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.Y_AXIS));
        btnConfig = GUIUtils.addButton(pnlRight, "Configure", null, this, "CONFIG", "Configure the selected batch step");
        btnRemove = GUIUtils.addButton(pnlRight, "Remove", null, this, "REMOVE", "Remove the selected batch step");
        btnClear = GUIUtils.addButton(pnlRight, "Clear", null, this, "CLEAR", "Removes all batch steps");
        final JPanel pnlBottom = new JPanel(new BorderLayout());
        btnAdd = GUIUtils.addButton(pnlBottom, "Add", null, this, "ADD", "Adds the selected method to the batch queue");
        pnlBottom.add(btnAdd, BorderLayout.EAST);
        pnlBottom.add(methodsCombo, BorderLayout.CENTER);
        add(panelTop, BorderLayout.NORTH);
        add(new JScrollPane(currentStepsList), BorderLayout.CENTER);
        add(pnlBottom, BorderLayout.SOUTH);
        add(pnlRight, BorderLayout.EAST);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final Object src = e.getSource();
        if (btnAdd.equals(src)) {
            final Object selectedItem = methodsCombo.getSelectedItem();
            if (selectedItem instanceof MZmineModuleWrapper) {
                final MZmineModuleWrapper wrappedModule = (MZmineModuleWrapper) selectedItem;
                final MZmineProcessingModule selectedMethod = (MZmineProcessingModule) wrappedModule.getModule();
                final ParameterSet methodParams = MZmineCore.getConfiguration().getModuleParameters(selectedMethod.getClass());
                if (methodParams.getParameters().length > 0) {
                    ExitCode exitCode = methodParams.showSetupDialog();
                    if (exitCode != ExitCode.OK) return;
                }
                final ParameterSet stepParams = methodParams.cloneParameter();
                final MZmineProcessingStep<MZmineProcessingModule> step = new MZmineProcessingStepImpl<MZmineProcessingModule>(selectedMethod, stepParams);
                batchQueue.add(step);
                currentStepsList.setListData(batchQueue);
                currentStepsList.setSelectedIndex(currentStepsList.getModel().getSize() - 1);
            }
        }
        if (btnRemove.equals(src)) {
            final MZmineProcessingStep selected = (MZmineProcessingStep) currentStepsList.getSelectedValue();
            if (selected != null) {
                final int index = currentStepsList.getSelectedIndex();
                batchQueue.remove(selected);
                currentStepsList.setListData(batchQueue);
                selectStep(index);
            }
        }
        if (btnClear.equals(src)) {
            batchQueue.clear();
            currentStepsList.setListData(batchQueue);
        }
        if (btnConfig.equals(src)) {
            final MZmineProcessingStep selected = (MZmineProcessingStep) currentStepsList.getSelectedValue();
            final ParameterSet parameters = selected == null ? null : selected.getParameterSet();
            if (parameters != null) {
                parameters.showSetupDialog();
            }
        }
        if (btnSave.equals(src)) {
            try {
                final File file = chooser.getSaveFile(this, XML_EXTENSION);
                if (file != null) {
                    saveBatchSteps(file);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "A problem occurred saving the file.\n" + ex.getMessage(), "Saving Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (btnLoad.equals(src)) {
            try {
                final File file = chooser.getLoadFile(this);
                if (file != null) {
                    loadBatchSteps(file);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "A problem occurred loading the file.\n" + ex.getMessage(), "Loading Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Get the queue.
     *
     * @return the queue.
     */
    public BatchQueue getValue() {
        return batchQueue;
    }

    /**
     * Sets the queue.
     *
     * @param newValue
     *            the new queue.
     */
    public void setValue(final BatchQueue newValue) {
        batchQueue = newValue;
        currentStepsList.setListData(batchQueue);
        selectStep(0);
    }

    /**
     * Select a step of the batch queue.
     *
     * @param step
     *            the step's index in the queue.
     */
    private void selectStep(final int step) {
        final int size = currentStepsList.getModel().getSize();
        if (size > 0 && step >= 0) {
            final int index = Math.min(step, size - 1);
            currentStepsList.setSelectedIndex(index);
            currentStepsList.ensureIndexIsVisible(index);
        }
    }

    /**
     * Save the batch queue to a file.
     *
     * @param file
     *            the file to save in.
     * @throws ParserConfigurationException
     *             if there is a parser problem.
     * @throws TransformerException
     *             if there is a transformation problem.
     * @throws FileNotFoundException
     *             if the file can't be found.
     */
    private void saveBatchSteps(final File file) throws ParserConfigurationException, TransformerException, FileNotFoundException {
        final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        final Element element = document.createElement("batch");
        document.appendChild(element);
        batchQueue.saveToXml(element);
        final Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(document), new StreamResult(new FileOutputStream(file)));
        LOG.info("Saved " + batchQueue.size() + " batch step(s) to " + file.getName());
    }

    /**
     * Load a batch queue from a file.
     *
     * @param file
     *            the file to read.
     * @throws ParserConfigurationException
     *             if there is a parser problem.
     * @throws SAXException
     *             if there is a SAX problem.
     * @throws IOException
     *             if there is an i/o problem.
     */
    public void loadBatchSteps(final File file) throws ParserConfigurationException, IOException, SAXException {
        final BatchQueue queue = BatchQueue.loadFromXml(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file).getDocumentElement());
        LOG.info("Loaded " + queue.size() + " batch step(s) from " + file.getName());
        final int option = JOptionPane.showOptionDialog(this, "How should the loaded batch steps be added to the queue?", "Add Batch Steps", JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, QueueOperations.values(), QueueOperations.Replace);
        int index = currentStepsList.getSelectedIndex();
        if (option >= 0) {
            switch(QueueOperations.values()[option]) {
                case Replace:
                    index = 0;
                    batchQueue = queue;
                    break;
                case Prepend:
                    index = 0;
                    batchQueue.addAll(0, queue);
                    break;
                case Insert:
                    index = index < 0 ? 0 : index;
                    batchQueue.addAll(index, queue);
                    break;
                case Append:
                    index = batchQueue.size();
                    batchQueue.addAll(queue);
                    break;
            }
        }
        currentStepsList.setListData(batchQueue);
        selectStep(index);
    }
}
