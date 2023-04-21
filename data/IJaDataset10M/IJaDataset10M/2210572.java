package de.ibis.permoto.gui.solver.panels.parametric;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import de.ibis.permoto.gui.solver.parametric.ArrivalRateParametricAnalysis;
import de.ibis.permoto.gui.solver.parametric.NumberOfCustomerParametricAnalysis;
import de.ibis.permoto.gui.solver.parametric.ParametricAnalysisChecker;
import de.ibis.permoto.gui.solver.parametric.ParametricAnalysisModelFactory;
import de.ibis.permoto.gui.solver.parametric.PopulationMixParametricAnalysis;
import de.ibis.permoto.gui.solver.parametric.SeedParametricAnalysis;
import de.ibis.permoto.gui.solver.parametric.ServiceTimesParametricAnalysis;
import de.ibis.permoto.model.definitions.IClassSection;
import de.ibis.permoto.model.definitions.IStationSection;
import de.ibis.permoto.solver.sim.mgt.definitions.ISimulationDefinition;
import de.ibis.permoto.solver.sim.mgt.definitions.impl.PerMoToParametricAnalysisDefinition;
import de.ibis.permoto.util.Constants;

/**
 * <p>
 * Title: ParametricAnalysisPanel
 * </p>
 * <p>
 * Description: with this panel user can select the type of parametric analysis .
 * This panel contains a <code>ParameterOptionPanel</code> that changes each
 * time user selects a different parametric analysis type.
 * </p>
 * 
 * @author Francesco D'Aquino Date: 7-mar-2006 Time: 13.12.42
 */
public class ParametricAnalysisPanel extends JPanel implements Constants {

    private static final long serialVersionUID = -1390945728259848653L;

    private static final Color BACK_COLOR = Color.WHITE;

    private Color DEFAULT_TITLE_COLOR = new TitledBorder("").getTitleColor();

    private String[] parameters = { "                   " };

    private ParameterOptionPanel parameterOptionPanel;

    private JCheckBox enabler;

    private JPanel upperPanel;

    private JPanel chooserPanel;

    private JPanel enablerPanel;

    private JComboBox chooser;

    private TitledBorder tb;

    IClassSection cd;

    IStationSection sd;

    ISimulationDefinition simd;

    Frame parent;

    public ParametricAnalysisPanel(final IClassSection classDef, final IStationSection stationDef, final ISimulationDefinition simDef, final Frame owner) {
        this.cd = classDef;
        this.sd = stationDef;
        this.simd = simDef;
        this.parent = owner;
        initGui();
        setListeners();
    }

    /**
	 * Creates the choosen parameter option panel.
	 * @param pad the instance of <code>ParameterAnalysisDefinition</code>
	 *            model.
	 * @return the <code>ParameterOptionPanel</code> corresponding to the
	 *         <code>ParameterAnalysisDefinition</code> passed as parameter
	 */
    protected ParameterOptionPanel createPanel(PerMoToParametricAnalysisDefinition pad) {
        ParameterOptionPanel pop;
        if (pad == null) {
            pop = new EmptyPanel();
        } else {
            if (pad instanceof NumberOfCustomerParametricAnalysis) {
                pop = new NumberOfCustomersPanel((NumberOfCustomerParametricAnalysis) pad, cd, sd, simd);
            } else if (pad instanceof PopulationMixParametricAnalysis) {
                pop = new PopulationMixPanel((PopulationMixParametricAnalysis) pad, cd, sd, simd);
            } else if (pad instanceof ServiceTimesParametricAnalysis) {
                pop = new ServiceTimesPanel((ServiceTimesParametricAnalysis) pad, cd, sd, simd);
            } else if (pad instanceof ArrivalRateParametricAnalysis) {
                pop = new ArrivalRatesPanel((ArrivalRateParametricAnalysis) pad, cd, sd, simd);
            } else if (pad instanceof SeedParametricAnalysis) {
                pop = new SeedPanel((SeedParametricAnalysis) pad, cd, sd, simd);
            } else {
                pop = null;
            }
        }
        return pop;
    }

    /**
	 * @return the panel's name
	 */
    public String getName() {
        return "What-if analysis";
    }

    /**
	 * Called when the What-if analysis panel is selected.
	 */
    public void gotFocus() {
        removeAll();
        initGui();
        setListeners();
    }

    public void initGui() {
        enabler = new JCheckBox("Enable what-if analysis");
        enabler.setBackground(BACK_COLOR);
        enabler.setToolTipText("Enable or disable what-if analysis");
        enablerPanel = new JPanel(new BorderLayout());
        enablerPanel.setBackground(BACK_COLOR);
        enablerPanel.add(enabler, BorderLayout.WEST);
        upperPanel = new JPanel(new BorderLayout());
        upperPanel.setBackground(BACK_COLOR);
        ;
        chooserPanel = new JPanel();
        chooserPanel.setBackground(BACK_COLOR);
        tb = new TitledBorder("Parameter selection for the control of repeated executions");
        chooserPanel.setBorder(tb);
        chooser = new JComboBox(parameters);
        chooser.setPreferredSize(DIM_BUTTON_L);
        chooser.setToolTipText("Choose the what-if analysis to be performed");
        chooserPanel.add(chooser, BorderLayout.NORTH);
        final JLabel whatIfEnablerDescription = new JLabel(PARAMETRIC_ANALYSIS_DESCRIPTION);
        final JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(BACK_COLOR);
        northPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        northPanel.add(whatIfEnablerDescription, BorderLayout.CENTER);
        northPanel.add(enablerPanel, BorderLayout.SOUTH);
        upperPanel.add(northPanel, BorderLayout.NORTH);
        upperPanel.add(chooserPanel, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        setBorder(new EtchedBorder(EtchedBorder.RAISED));
        chooser.removeAllItems();
        final ParametricAnalysisChecker pac = new ParametricAnalysisChecker(cd, sd);
        if (!pac.canBeEnabled()) {
            enabler.setEnabled(false);
            parameterOptionPanel = createPanel(null);
            simd.setParametricAnalysisModel(null);
            simd.setParametricAnalysisEnabled(false);
        } else {
            enabler.setEnabled(true);
            enabler.setSelected(simd.isParametricAnalysisEnabled());
            PerMoToParametricAnalysisDefinition pad = simd.getParametricAnalysisModel();
            parameters = pac.getRunnableParametricAnalysis();
            for (int k = 0; k < parameters.length; k++) {
                chooser.addItem(parameters[k]);
            }
            final String temp = parameters[0];
            if (pad == null) {
                pad = ParametricAnalysisModelFactory.createParametricAnalysisModel(temp, cd, sd, simd);
                simd.setParametricAnalysisModel(pad);
            } else {
                final int code = pad.checkCorrectness(true);
                if (code != 2) {
                    chooser.setSelectedItem(pad.getType());
                } else {
                    pad = ParametricAnalysisModelFactory.createParametricAnalysisModel(temp, cd, sd, simd);
                    simd.setParametricAnalysisModel(pad);
                }
            }
            parameterOptionPanel = createPanel(pad);
        }
        parameterOptionPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        parameterOptionPanel.setBackground(BACK_COLOR);
        add(upperPanel, BorderLayout.NORTH);
        add(parameterOptionPanel, BorderLayout.CENTER);
        setEnabled(enabler.isSelected());
    }

    public void setData(final IClassSection cd, final IStationSection sd, final ISimulationDefinition simd) {
        this.cd = cd;
        this.sd = sd;
        this.simd = simd;
        removeAll();
        initGui();
        setListeners();
        doLayout();
        validate();
        repaint();
    }

    public void setEnabled(boolean enabled) {
        if (!enabled) {
            chooser.setEnabled(false);
            enablerPanel.setEnabled(false);
            chooserPanel.setEnabled(false);
            upperPanel.setEnabled(false);
            parameterOptionPanel.setEnabled(false);
            tb.setTitleColor(Color.LIGHT_GRAY);
            parameterOptionPanel.repaint();
        } else {
            chooser.setEnabled(true);
            enablerPanel.setEnabled(true);
            chooserPanel.setEnabled(true);
            upperPanel.setEnabled(true);
            parameterOptionPanel.setEnabled(true);
            tb.setTitleColor(DEFAULT_TITLE_COLOR);
            parameterOptionPanel.repaint();
        }
    }

    /**
	 * Sets the listeners to enabler and chooser.
	 */
    private void setListeners() {
        enabler.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    setEnabled(true);
                    simd.setParametricAnalysisEnabled(true);
                } else {
                    setEnabled(false);
                    simd.setParametricAnalysisEnabled(false);
                }
            }
        });
        chooser.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                final String param = (String) chooser.getSelectedItem();
                if (parameterOptionPanel != null) {
                    remove(parameterOptionPanel);
                    final PerMoToParametricAnalysisDefinition temp = ParametricAnalysisModelFactory.createParametricAnalysisModel(param, cd, sd, simd);
                    simd.setParametricAnalysisModel(temp);
                    simd.setSaveChanged();
                    parameterOptionPanel = createPanel(temp);
                    add(parameterOptionPanel, BorderLayout.CENTER);
                    doLayout();
                    parameterOptionPanel.validate();
                }
            }
        });
    }
}
