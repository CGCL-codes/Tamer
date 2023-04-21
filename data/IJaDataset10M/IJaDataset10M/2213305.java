package org.mcisb.ui.kinetics;

import java.awt.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.mcisb.kinetics.*;
import org.mcisb.ontology.sbo.*;
import org.mcisb.ui.kinetics.data.*;
import org.mcisb.ui.util.*;
import org.mcisb.util.*;
import org.sbml.jsbml.*;

/**
 *
 * @author Neil Swainston
 */
public class KineticsResultsPanel extends GridBagPanel implements Disposable, ListSelectionListener, PropertyChangeListener, TableModelListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("org.mcisb.ui.kinetics.messages");

    /**
	 * 
	 */
    private final Map<String, KineticsCalculator> modelNameToKineticsCalculator;

    /**
	 * 
	 */
    private final Map<String, Component> modelNameToComponent = new HashMap<String, Component>();

    /**
	 * 
	 */
    private final JPanel imagePanel = new JPanel(new BorderLayout());

    /**
	 * 
	 */
    private final JCheckBox archiveCheckBox = new JCheckBox(resourceBundle.getString("KineticsResultsPanel.archiveCheckBox"));

    /**
	 * 
	 */
    private final transient KineticsExperimentSet experimentSet;

    /**
	 * 
	 */
    private final transient Archiver archiver;

    /**
	 * 
	 */
    private final JTable table;

    /**
	 * 
	 *
	 * @param experimentSet
	 * @param modelNameToKineticsCalculator
	 * @param archiver
	 * @throws Exception
	 */
    public KineticsResultsPanel(final KineticsExperimentSet experimentSet, final Map<String, KineticsCalculator> modelNameToKineticsCalculator, final Archiver archiver) throws Exception {
        this.experimentSet = experimentSet;
        this.modelNameToKineticsCalculator = modelNameToKineticsCalculator;
        this.archiver = archiver;
        final int ZERO = 0;
        final KineticsTableModel tableModel = new KineticsTableModel(experimentSet);
        table = new KineticsTable(tableModel);
        table.setRowSorter(new TableRowSorter<KineticsTableModel>(tableModel));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(this);
        table.getModel().addTableModelListener(new HillCoefficientListener(modelNameToKineticsCalculator));
        table.getModel().addTableModelListener(this);
        if (table.getRowCount() > ZERO) {
            table.getSelectionModel().setSelectionInterval(ZERO, ZERO);
        }
        imagePanel.setOpaque(false);
        final JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        final JScrollPane imageScrollPane = new JScrollPane(imagePanel);
        imageScrollPane.getViewport().setBackground(Color.WHITE);
        final int GRIDWIDTH = 1;
        add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScrollPane, imageScrollPane), 0, 0, true, true, true, archiver == null, GridBagConstraints.BOTH, GRIDWIDTH);
        if (archiver != null) {
            add(archiveCheckBox, 0, 1, true, true, false, true, GridBagConstraints.HORIZONTAL, GRIDWIDTH);
        }
    }

    @Override
    public void dispose() throws Exception {
        table.getSelectionModel().removeListSelectionListener(this);
        table.getModel().removeTableModelListener(this);
        for (Iterator<KineticsCalculator> iterator = modelNameToKineticsCalculator.values().iterator(); iterator.hasNext(); ) {
            iterator.next().removePropertyChangeListener(this);
        }
        close();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        final int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            final String modelName = (String) table.getValueAt(selectedRow, table.convertColumnIndexToView(KineticsTableModel.MODEL));
            if (modelName != null) {
                try {
                    final KineticsCalculator kineticsCalculator = modelNameToKineticsCalculator.get(modelName);
                    kineticsCalculator.addPropertyChangeListener(this);
                    Component component = modelNameToComponent.get(modelName);
                    if (component == null) {
                        component = new KineticsDataDisplayComponent(true, kineticsCalculator);
                        modelNameToComponent.put(modelName, component);
                    }
                    imagePanel.removeAll();
                    imagePanel.add(component, BorderLayout.CENTER);
                    revalidate();
                    repaint();
                } catch (Exception ex) {
                    final JDialog errorDialog = new ExceptionComponentFactory(true).getExceptionDialog(getParent(), ExceptionUtils.toString(ex), ex);
                    ComponentUtils.setLocationCentral(errorDialog);
                    errorDialog.setVisible(true);
                }
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(KineticsCalculator.UPDATE) && evt.getSource() instanceof KineticsCalculator) {
            final KineticsCalculator calculator = (KineticsCalculator) evt.getSource();
            final double kCat = calculator.getValue(SboUtils.CATALYTIC_RATE_CONSTANT);
            final double kM = calculator.getValue(SboUtils.MICHAELIS_CONSTANT);
            final double hillCoefficient = calculator.getValue(SboUtils.HILL_COEFFICIENT);
            final double kCatStandardError = calculator.getError(SboUtils.CATALYTIC_RATE_CONSTANT);
            final double kMStandardError = calculator.getError(SboUtils.MICHAELIS_CONSTANT);
            final double hillCoefficientError = calculator.getError(SboUtils.HILL_COEFFICIENT);
            final TableModel model = table.getModel();
            final int rowIndex = table.getSelectedRow();
            model.setValueAt(Double.valueOf(kCat), rowIndex, KineticsTableModel.KCAT);
            model.setValueAt(Double.valueOf(kCatStandardError), rowIndex, KineticsTableModel.KCAT_STANDARD_ERROR);
            model.setValueAt(Double.valueOf(kCatStandardError / kCat * 100.0f), rowIndex, KineticsTableModel.KCAT_PERCENTAGE_ERROR);
            model.setValueAt(Double.valueOf(kM), rowIndex, KineticsTableModel.KM);
            model.setValueAt(Double.valueOf(kMStandardError), rowIndex, KineticsTableModel.KM_STANDARD_ERROR);
            model.setValueAt(Double.valueOf(kMStandardError / kM * 100.0f), rowIndex, KineticsTableModel.KM_PERCENTAGE_ERROR);
            if (model.getColumnCount() > KineticsTableModel.HILL_COEFFICIENT) {
                model.setValueAt(Double.valueOf(hillCoefficient), rowIndex, KineticsTableModel.HILL_COEFFICIENT);
                model.setValueAt(Double.valueOf(hillCoefficientError), rowIndex, KineticsTableModel.HILL_COEFFICIENT_STANDARD_ERROR);
                model.setValueAt(Double.valueOf(hillCoefficientError / hillCoefficient * 100.0f), rowIndex, KineticsTableModel.HILL_COEFFICIENT_PERCENTAGE_ERROR);
            }
        }
    }

    @Override
    public void tableChanged(final TableModelEvent e) {
        final TableModel tableModel = table.getModel();
        final Object modelName = tableModel.getValueAt(e.getFirstRow(), KineticsTableModel.MODEL);
        for (Iterator<SBMLDocument> iterator = experimentSet.getDocuments().iterator(); iterator.hasNext(); ) {
            final Model model = iterator.next().getModel();
            if (model.getName().equals(modelName)) {
                final Object value = tableModel.getValueAt(e.getFirstRow(), e.getColumn());
                for (int l = 0; l < model.getNumReactions(); l++) {
                    final Reaction reaction = model.getReaction(l);
                    final KineticLaw kineticLaw = reaction.getKineticLaw();
                    LocalParameter kcat = null;
                    LocalParameter km = null;
                    LocalParameter hillCoefficient = null;
                    for (int m = 0; m < kineticLaw.getLocalParameterCount(); m++) {
                        final LocalParameter parameter = kineticLaw.getLocalParameter(m);
                        if (parameter != null && parameter.getSBOTerm() == SboUtils.CATALYTIC_RATE_CONSTANT) {
                            kcat = parameter;
                        } else if (parameter != null && parameter.getSBOTerm() == SboUtils.MICHAELIS_CONSTANT) {
                            km = parameter;
                        } else if (parameter != null && parameter.getSBOTerm() == SboUtils.HILL_COEFFICIENT) {
                            hillCoefficient = parameter;
                        }
                    }
                    switch(e.getColumn()) {
                        case KineticsTableModel.KCAT:
                            {
                                if (kcat != null) {
                                    kcat.setValue(((Double) value).doubleValue());
                                }
                                break;
                            }
                        case KineticsTableModel.KCAT_STANDARD_ERROR:
                            {
                                if (kcat != null) {
                                    experimentSet.addCondition(kcat, org.mcisb.util.PropertyNames.ERROR, value.toString());
                                }
                                break;
                            }
                        case KineticsTableModel.KM:
                            {
                                if (km != null) {
                                    km.setValue(((Double) value).doubleValue());
                                }
                                break;
                            }
                        case KineticsTableModel.KM_STANDARD_ERROR:
                            {
                                if (km != null) {
                                    experimentSet.addCondition(km, org.mcisb.util.PropertyNames.ERROR, value.toString());
                                }
                                break;
                            }
                        case KineticsTableModel.HILL_COEFFICIENT:
                            {
                                if (hillCoefficient != null) {
                                    hillCoefficient.setValue(((Double) value).doubleValue());
                                }
                                break;
                            }
                        case KineticsTableModel.HILL_COEFFICIENT_STANDARD_ERROR:
                            {
                                if (hillCoefficient != null) {
                                    experimentSet.addCondition(hillCoefficient, org.mcisb.util.PropertyNames.ERROR, value.toString());
                                }
                                break;
                            }
                        default:
                            {
                                continue;
                            }
                    }
                }
            }
        }
    }

    /**
	 * 
	 * @throws Exception
	 */
    private void close() throws Exception {
        if (archiveCheckBox.isSelected() && archiver != null) {
            final Map<String, double[]> modelNameToInitialRates = new HashMap<String, double[]>();
            for (Iterator<Map.Entry<String, KineticsCalculator>> iterator = this.modelNameToKineticsCalculator.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, KineticsCalculator> entry = iterator.next();
                modelNameToInitialRates.put(entry.getKey(), entry.getValue().getInitialRates());
            }
            archiver.archive(experimentSet, modelNameToInitialRates);
        }
    }
}
