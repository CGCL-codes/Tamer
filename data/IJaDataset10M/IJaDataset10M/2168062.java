package com.rapidminer.gui.new_plotter.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.rapidminer.gui.new_plotter.configuration.AggregationWindowing;
import com.rapidminer.gui.new_plotter.configuration.ValueSource;
import com.rapidminer.gui.new_plotter.data.PlotInstance;
import com.rapidminer.gui.new_plotter.listener.events.PlotConfigurationChangeEvent;
import com.rapidminer.gui.tools.ResourceLabel;

/**
 * @author Nils Woehler
 * 
 */
public class WindowingConfigurationContainer extends AbstractTreeSelectionDependentPanel {

    private static final long serialVersionUID = 1L;

    private JLabel grabLeftLabel;

    private JSpinner grabLeftSpinner;

    private JLabel grabRightLabel;

    private JSpinner grabRightSpinner;

    private JLabel includeIncompleteGroupsLabel;

    private JCheckBox includeIncompleteGroupsCheckBox;

    public WindowingConfigurationContainer(JTree plotConfigurationTree, PlotInstance plotInstance) {
        super(plotConfigurationTree, plotInstance);
        createComponents();
        registerAsPlotConfigurationListener();
    }

    private void createComponents() {
        {
            grabLeftLabel = new ResourceLabel("plotter.configuration_dialog.grab_left");
            grabLeftSpinner = new JSpinner(new SpinnerNumberModel(0, -1, null, 1));
            grabLeftLabel.setLabelFor(grabLeftSpinner);
            grabLeftSpinner.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    ValueSource selectedValueSource = getSelectedValueSource();
                    if (selectedValueSource != null) {
                        AggregationWindowing aggregationWindowing = selectedValueSource.getAggregationWindowing();
                        if (aggregationWindowing != null) {
                            int oldGrabLeft = aggregationWindowing.getGrabLeft();
                            int newGrabLeft = (Integer) grabLeftSpinner.getValue();
                            if (oldGrabLeft != newGrabLeft) {
                                aggregationWindowing.setGrabLeft(newGrabLeft);
                            }
                        }
                    }
                }
            });
            addTwoComponentRow(this, grabLeftLabel, grabLeftSpinner);
        }
        {
            grabRightLabel = new ResourceLabel("plotter.configuration_dialog.grab_right");
            grabRightSpinner = new JSpinner(new SpinnerNumberModel(0, -1, null, 1));
            grabRightLabel.setLabelFor(grabRightSpinner);
            grabRightSpinner.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    ValueSource selectedValueSource = getSelectedValueSource();
                    if (selectedValueSource != null) {
                        AggregationWindowing aggregationWindowing = selectedValueSource.getAggregationWindowing();
                        if (aggregationWindowing != null) {
                            int oldGrabRight = aggregationWindowing.getGrabRight();
                            int newGrabRight = (Integer) grabRightSpinner.getValue();
                            if (oldGrabRight != newGrabRight) {
                                aggregationWindowing.setGrabRight(newGrabRight);
                            }
                        }
                    }
                }
            });
            addTwoComponentRow(this, grabRightLabel, grabRightSpinner);
        }
        {
            includeIncompleteGroupsLabel = new ResourceLabel("plotter.configuration_dialog.incomplete_groups");
            includeIncompleteGroupsCheckBox = new JCheckBox();
            includeIncompleteGroupsLabel.setLabelFor(includeIncompleteGroupsCheckBox);
            includeIncompleteGroupsCheckBox.setSelected(true);
            includeIncompleteGroupsCheckBox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    ValueSource selectedValueSource = getSelectedValueSource();
                    if (selectedValueSource != null) {
                        AggregationWindowing aggregationWindowing = selectedValueSource.getAggregationWindowing();
                        if (aggregationWindowing != null) {
                            aggregationWindowing.setIncludeIncompleteGroups(includeIncompleteGroupsCheckBox.isSelected());
                        }
                    }
                }
            });
            addTwoComponentRow(this, includeIncompleteGroupsLabel, includeIncompleteGroupsCheckBox);
        }
    }

    private void grabLeftChanged(Integer value) {
        grabLeftSpinner.setValue(value);
    }

    private void grabRightChanged(Integer value) {
        grabRightSpinner.setValue(value);
    }

    private void incompleteGroupsChanged(boolean incompleteGroups) {
        includeIncompleteGroupsCheckBox.setSelected(incompleteGroups);
    }

    @Override
    public boolean plotConfigurationChanged(PlotConfigurationChangeEvent change) {
        adaptGUI();
        return true;
    }

    @Override
    protected void adaptGUI() {
        ValueSource selectedValueSource = getSelectedValueSource();
        if (selectedValueSource != null) {
            AggregationWindowing windowing = selectedValueSource.getAggregationWindowing();
            if (windowing != null) {
                grabLeftChanged(windowing.getGrabLeft());
                grabRightChanged(windowing.getGrabRight());
                incompleteGroupsChanged(windowing.isIncludingIncompleteGroups());
            }
        }
    }
}
