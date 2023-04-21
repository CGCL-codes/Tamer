package org.mcisb.ui.jws.calculator;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import org.mcisb.jws.*;
import org.mcisb.ui.util.*;
import org.mcisb.ui.util.list.*;
import org.mcisb.util.*;

/**
 * @author Neil Swainston
 */
public abstract class ValueCalculatorPanel extends CalculatorPanel implements ActionListener, ChangeListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("org.mcisb.ui.jws.calculator.messages");

    /**
	 * 
	 */
    private static final int DEFAULT_NUM_STEPS = 20;

    /**
	 * 
	 */
    private static final double MIN = 0.0;

    /**
	 * 
	 */
    private static final double MAX = 1000.0;

    /**
	 * 
	 */
    private static final double STEP = 1.0;

    /**
	 * 
	 */
    private static final String RATES = resourceBundle.getString("ValueCalculatorPanel.rates");

    /**
	 * 
	 */
    private static final String METABOLITES = resourceBundle.getString("ValueCalculatorPanel.metabolites");

    /**
	 * 
	 */
    protected boolean showRates = true;

    /**
	 * 
	 */
    protected final JList ratesList;

    /**
	 * 
	 */
    protected final JList metaboliteList;

    /**
	 * 
	 */
    protected final JSpinner low;

    /**
	 * 
	 */
    protected final JSpinner high;

    /**
	 * 
	 */
    protected final JSpinner steps;

    /**
	 * 
	 */
    private final JScrollPane scrollPane = new JScrollPane();

    /**
	 * 
	 * @param factory
	 * @param name
	 * @param selection
	 * @param rates
	 * @param metabolites
	 * @param showRates
	 * @param lowLabel
	 * @param highLabel
	 * @param defaultLow
	 * @param defaultHigh
	 * @param hasNumberOfSteps
	 */
    protected ValueCalculatorPanel(final JwsCalculatorFactory factory, final String name, final String selection, final Map<Object, Boolean> rates, final Map<Object, Boolean> metabolites, final boolean showRates, final String lowLabel, final String highLabel, final double defaultLow, final double defaultHigh, final boolean hasNumberOfSteps) {
        super(factory, name, selection);
        ratesList = getList(rates);
        metaboliteList = getList(metabolites);
        final JRadioButton ratesButton = new JRadioButton();
        ratesButton.setMnemonic(KeyEvent.VK_R);
        ratesButton.setActionCommand(RATES);
        ratesButton.setSelected(showRates);
        ratesButton.addActionListener(this);
        final JRadioButton metabolitesButton = new JRadioButton();
        metabolitesButton.setMnemonic(KeyEvent.VK_M);
        metabolitesButton.setActionCommand(METABOLITES);
        metabolitesButton.setSelected(!showRates);
        metabolitesButton.addActionListener(this);
        final ButtonGroup group = new ButtonGroup();
        group.add(ratesButton);
        group.add(metabolitesButton);
        final SpinnerModel startModel = new SpinnerNumberModel(defaultLow, MIN, MAX, STEP);
        final SpinnerModel endModel = new SpinnerNumberModel(defaultHigh, MIN, MAX, STEP);
        final SpinnerModel stepsModel = new SpinnerNumberModel(DEFAULT_NUM_STEPS, (int) MIN, (int) MAX, (int) STEP);
        low = new JSpinner(startModel);
        high = new JSpinner(endModel);
        steps = new JSpinner(stepsModel);
        low.addChangeListener(this);
        high.addChangeListener(this);
        final GridBagPanel parameterPanel = new GridBagPanel();
        int row = 0;
        parameterPanel.add(new JLabel(lowLabel), 0, row, false, false, false, false, GridBagConstraints.HORIZONTAL, 1);
        parameterPanel.add(low, 1, row++, true, true, false, false, GridBagConstraints.HORIZONTAL, 1);
        parameterPanel.add(new JLabel(highLabel), 0, row, false, false, false, false, GridBagConstraints.HORIZONTAL, 1);
        parameterPanel.add(high, 1, row++, true, true, false, false, GridBagConstraints.HORIZONTAL, 1);
        if (hasNumberOfSteps) {
            parameterPanel.add(new JLabel(resourceBundle.getString("ValueCalculatorPanel.steps")), 0, row, false, false, false, false, GridBagConstraints.HORIZONTAL, 1);
            parameterPanel.add(steps, 1, row++, true, true, false, false, GridBagConstraints.HORIZONTAL, 1);
        }
        parameterPanel.add(new JLabel(RATES), 0, row, false, false, false, false, GridBagConstraints.HORIZONTAL, 1);
        parameterPanel.add(ratesButton, 1, row++, true, true, false, false, GridBagConstraints.HORIZONTAL, 1);
        parameterPanel.add(new JLabel(METABOLITES), 0, row, false, false, false, true, GridBagConstraints.HORIZONTAL, 1);
        parameterPanel.add(metabolitesButton, 1, row++, true, true, false, true, GridBagConstraints.HORIZONTAL, 1);
        final JPanel innerPanel = new JPanel(new BorderLayout());
        final Border border = BorderFactory.createTitledBorder(resourceBundle.getString("ValueCalculatorPanel.values"));
        innerPanel.setBorder(border);
        innerPanel.add(scrollPane, BorderLayout.CENTER);
        add(parameterPanel, BorderLayout.NORTH);
        add(innerPanel, BorderLayout.CENTER);
        ratesButton.setSelected(showRates);
        setShowRates(showRates);
    }

    public void actionPerformed(final ActionEvent e) {
        final String actionCommand = e.getActionCommand();
        if (actionCommand.equals(RATES)) {
            setShowRates(true);
        } else if (actionCommand.equals(METABOLITES)) {
            setShowRates(false);
        }
    }

    public void stateChanged(final ChangeEvent e) {
        if (e.getSource() == low) {
            final Object value = low.getValue();
            final SpinnerModel model = high.getModel();
            if (value instanceof Comparable && model instanceof SpinnerNumberModel) {
                ((SpinnerNumberModel) model).setMinimum((Comparable<?>) value);
            }
        } else if (e.getSource() == high) {
            final Object value = high.getValue();
            final SpinnerModel model = low.getModel();
            if (value instanceof Comparable && model instanceof SpinnerNumberModel) {
                ((SpinnerNumberModel) model).setMaximum((Comparable<?>) value);
            }
        }
    }

    /**
	 * 
	 * @param showRates
	 */
    private void setShowRates(final boolean showRates) {
        this.showRates = showRates;
        scrollPane.setViewportView((showRates) ? ratesList : metaboliteList);
        repaint();
    }

    /**
	 * 
	 * @param values
	 * @return JList
	 */
    private JList getList(final Map<Object, Boolean> values) {
        final DefaultListModel listModel = new DefaultListModel();
        ListModelUtils.add(listModel, values.keySet());
        final JList list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        final Collection<Integer> selectedIndices = new ArrayList<Integer>();
        for (Iterator<Map.Entry<Object, Boolean>> iterator = values.entrySet().iterator(); iterator.hasNext(); ) {
            final Map.Entry<Object, Boolean> entry = iterator.next();
            if (entry.getValue().booleanValue()) {
                selectedIndices.add(Integer.valueOf(listModel.indexOf(entry.getKey())));
            }
        }
        list.setSelectedIndices(CollectionUtils.toIntArray(selectedIndices));
        return list;
    }
}
