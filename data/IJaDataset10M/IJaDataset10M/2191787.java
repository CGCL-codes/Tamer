package org.neuroph.netbeans.ide.wizards;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.ChangeListener;
import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.netbeans.ide.CurrentProject;
import org.neuroph.netbeans.ide.project.NeurophProject;
import org.neuroph.netbeans.ide.project.NeurophProjectFilesFactory;
import org.neuroph.netbeans.main.easyneurons.TrainingSetTableModel;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;

public final class NewTrainingSetWizardIterator implements WizardDescriptor.InstantiatingIterator {

    private int index;

    private WizardDescriptor wizard;

    private WizardDescriptor.Panel[] panels;

    /**
     * Initialize panels representing individual wizard's steps and sets
     * various properties for them influencing wizard appearance.
     */
    private WizardDescriptor.Panel[] getPanels() {
        if (panels == null) {
            panels = new WizardDescriptor.Panel[] { new NewTrainingSetWizardPanel1(), new NewTrainingSetWizardPanel2() };
            String[] steps = createSteps();
            for (int i = 0; i < panels.length; i++) {
                Component c = panels[i].getComponent();
                if (steps[i] == null) {
                    steps[i] = c.getName();
                }
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    jc.putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i));
                    jc.putClientProperty("WizardPanel_contentData", steps);
                    jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE);
                    jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
                    jc.putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE);
                }
            }
        }
        return panels;
    }

    public Set instantiate() throws IOException {
        boolean cancelled = wizard.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {
            NeurophProject project = (NeurophProject) wizard.getProperty("project");
            CurrentProject.getInstance().setCurrentProject(project);
            JTable trainingSetTable = ((NewTrainingSetVisualPanel2) panels[1].getComponent()).getTrainingSetTable();
            if (trainingSetTable.isEditing()) {
                trainingSetTable.getCellEditor().stopCellEditing();
            }
            TrainingSet trainingSet = null;
            TrainingSetTableModel tableModel = (TrainingSetTableModel) trainingSetTable.getModel();
            ArrayList<ArrayList> dataVector = tableModel.getDataVector();
            Iterator<ArrayList> iterator = dataVector.iterator();
            String trainingSetType = (String) wizard.getProperty("type");
            if (trainingSetType.equals("Unsupervised")) {
                int inputsNumber = Integer.parseInt((String) wizard.getProperty("inputsNumber"));
                trainingSet = new TrainingSet(inputsNumber);
                trainingSet.setLabel((String) wizard.getProperty("name"));
                while (iterator.hasNext()) {
                    ArrayList rowVector = iterator.next();
                    ArrayList<Double> doubleRowVector = new ArrayList<Double>();
                    try {
                        for (int i = 0; i < inputsNumber; i++) {
                            double doubleVal = Double.parseDouble(rowVector.get(i).toString());
                            doubleRowVector.add(new Double(doubleVal));
                        }
                    } catch (Exception ex) {
                        continue;
                    }
                    TrainingElement trainingElement = new TrainingElement(doubleRowVector);
                    trainingSet.addElement(trainingElement);
                }
            } else if (trainingSetType.equals("Supervised")) {
                int inputsNumber = Integer.parseInt((String) wizard.getProperty("inputsNumber"));
                int outputsNumber = Integer.parseInt((String) wizard.getProperty("outputsNumber"));
                trainingSet = new TrainingSet(inputsNumber, outputsNumber);
                trainingSet.setLabel((String) wizard.getProperty("name"));
                while (iterator.hasNext()) {
                    ArrayList rowVector = iterator.next();
                    ArrayList<Double> inputVector = new ArrayList<Double>();
                    ArrayList<Double> outputVector = new ArrayList<Double>();
                    try {
                        for (int i = 0; i < inputsNumber; i++) {
                            double doubleVal = Double.parseDouble(rowVector.get(i).toString());
                            inputVector.add(new Double(doubleVal));
                        }
                        for (int i = 0; i < outputsNumber; i++) {
                            double doubleVal = Double.parseDouble(rowVector.get(inputsNumber + i).toString());
                            outputVector.add(new Double(doubleVal));
                        }
                    } catch (Exception ex) {
                        continue;
                    }
                    SupervisedTrainingElement trainingElement = new SupervisedTrainingElement(inputVector, outputVector);
                    trainingSet.addElement(trainingElement);
                }
            }
            NeurophProjectFilesFactory fileFactory = NeurophProjectFilesFactory.getDefault();
            fileFactory.createTrainingSetFile(trainingSet);
            String createdFilePath = fileFactory.getCreatedFilePath();
            FileObject fao = FileUtil.toFileObject(new File(createdFilePath));
            DataObject dao = DataObject.find(fao);
            return dao != null ? Collections.singleton(dao) : Collections.EMPTY_SET;
        }
        return Collections.EMPTY_SET;
    }

    public void initialize(WizardDescriptor wizard) {
        this.wizard = wizard;
    }

    public void uninitialize(WizardDescriptor wizard) {
        panels = null;
    }

    public WizardDescriptor.Panel current() {
        return getPanels()[index];
    }

    public String name() {
        return index + 1 + ". from " + getPanels().length;
    }

    public boolean hasNext() {
        return index < getPanels().length - 1;
    }

    public boolean hasPrevious() {
        return index > 0;
    }

    public void nextPanel() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        index++;
    }

    public void previousPanel() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        index--;
    }

    public void addChangeListener(ChangeListener l) {
    }

    public void removeChangeListener(ChangeListener l) {
    }

    private String[] createSteps() {
        String[] beforeSteps = null;
        Object prop = wizard.getProperty("WizardPanel_contentData");
        if (prop != null && prop instanceof String[]) {
            beforeSteps = (String[]) prop;
        }
        if (beforeSteps == null) {
            beforeSteps = new String[0];
        }
        String[] res = new String[(beforeSteps.length - 1) + panels.length];
        for (int i = 0; i < res.length; i++) {
            if (i < (beforeSteps.length - 1)) {
                res[i] = beforeSteps[i];
            } else {
                res[i] = panels[i - beforeSteps.length + 1].getComponent().getName();
            }
        }
        return res;
    }
}
