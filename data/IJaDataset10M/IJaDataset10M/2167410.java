package com.rapidminer.gui.new_plotter.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.jfree.chart.plot.PlotOrientation;
import com.rapidminer.gui.new_plotter.StaticDebug;
import com.rapidminer.gui.new_plotter.data.PlotInstance;
import com.rapidminer.gui.new_plotter.gui.cellrenderer.ColorSchemeComboBoxRenderer;
import com.rapidminer.gui.new_plotter.gui.cellrenderer.EnumComboBoxCellRenderer;
import com.rapidminer.gui.new_plotter.gui.popup.PopupAction;
import com.rapidminer.gui.new_plotter.gui.popup.PopupAction.PopupPosition;
import com.rapidminer.gui.new_plotter.listener.events.PlotConfigurationChangeEvent;
import com.rapidminer.gui.new_plotter.listener.events.PlotConfigurationChangeEvent.PlotConfigurationChangeType;
import com.rapidminer.gui.new_plotter.templates.style.ColorScheme;
import com.rapidminer.gui.tools.ResourceAction;
import com.rapidminer.gui.tools.ResourceLabel;
import com.rapidminer.tools.I18N;

/**
 * @author Nils Woehler
 * 
 */
public class GlobalConfigurationPanel extends AbstractConfigurationPanel {

    private static final long serialVersionUID = 1L;

    private JTextField titleTextField;

    private JComboBox plotOrientationComboBox;

    private final int fontSize = 12;

    private JToggleButton titleConfigButton;

    private AxisConfigurationContainer axisConfigurationContainer;

    private LegendConfigurationPanel legendConfigContainer;

    private JButton plotBackgroundColorChooserButton;

    private JButton frameBackgroundColorChooserButton;

    private JLabel plotBackGroundColorLabel;

    private JLabel frameBackGroundColorLabel;

    private DefaultComboBoxModel colorsSchemesComboBoxModel;

    private JComboBox colorSchemesComboBox;

    private ChartTitleConfigurationContainer chartTitleConfigurationContainer;

    public GlobalConfigurationPanel(PlotInstance plotInstance) {
        super(plotInstance);
        axisConfigurationContainer = new AxisConfigurationContainer(plotInstance);
        addPlotInstanceChangeListener(axisConfigurationContainer);
        legendConfigContainer = new LegendConfigurationPanel(plotInstance);
        addPlotInstanceChangeListener(legendConfigContainer);
        chartTitleConfigurationContainer = new ChartTitleConfigurationContainer(getCurrentPlotInstance());
        addPlotInstanceChangeListener(chartTitleConfigurationContainer);
        createComponents();
        registerAsPlotConfigurationListener();
        adaptGUI();
    }

    private void createComponents() {
        {
            JLabel titleLabel = new ResourceLabel("plotter.configuration_dialog.chart_title");
            String title = getPlotConfiguration().getTitleText();
            if (title == null) {
                title = "";
            }
            titleTextField = new JTextField(title);
            titleLabel.setLabelFor(titleTextField);
            titleTextField.addKeyListener(new KeyListener() {

                @Override
                public void keyTyped(KeyEvent e) {
                    return;
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    String newTitle = titleTextField.getText();
                    String titleText = getCurrentPlotInstance().getMasterPlotConfiguration().getTitleText();
                    if (titleText != null) {
                        if (!titleText.equals(newTitle) || titleText == null && newTitle.length() > 0) {
                            if (newTitle.length() > 0) {
                                getPlotConfiguration().setTitleText(newTitle);
                            } else {
                                getPlotConfiguration().setTitleText(null);
                            }
                        }
                    } else {
                        if (newTitle.length() > 0) {
                            getPlotConfiguration().setTitleText(newTitle);
                        } else {
                            getPlotConfiguration().setTitleText(null);
                        }
                    }
                    if (newTitle.equals("Iris") && e.isControlDown() && e.getKeyCode() == KeyEvent.VK_D) {
                        startAnimation();
                    }
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    return;
                }
            });
            titleTextField.setPreferredSize(new Dimension(115, 23));
            titleConfigButton = new JToggleButton(new PopupAction(true, "plotter.configuration_dialog.open_popup", chartTitleConfigurationContainer, PopupPosition.HORIZONTAL));
            addThreeComponentRow(this, titleLabel, titleTextField, titleConfigButton);
        }
        {
            JLabel plotOrientationLabel = new ResourceLabel("plotter.configuration_dialog.global_config_panel.plot_orientation");
            PlotOrientation[] orientations = { PlotOrientation.HORIZONTAL, PlotOrientation.VERTICAL };
            plotOrientationComboBox = new JComboBox(orientations);
            plotOrientationLabel.setLabelFor(plotOrientationComboBox);
            plotOrientationComboBox.setRenderer(new EnumComboBoxCellRenderer("plotter"));
            plotOrientationComboBox.setSelectedIndex(0);
            plotOrientationComboBox.addPopupMenuListener(new PopupMenuListener() {

                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    return;
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    getPlotConfiguration().setOrientation((PlotOrientation) plotOrientationComboBox.getSelectedItem());
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent e) {
                    return;
                }
            });
            addTwoComponentRow(this, plotOrientationLabel, plotOrientationComboBox);
        }
        {
            JLabel legendStyleConfigureLabel = new ResourceLabel("plotter.configuration_dialog.global_config_panel.legend_style");
            JToggleButton legendStyleConfigButton = new JToggleButton(new PopupAction(true, "plotter.configuration_dialog.open_popup", legendConfigContainer, PopupAction.PopupPosition.HORIZONTAL));
            legendStyleConfigureLabel.setLabelFor(legendStyleConfigButton);
            addTwoComponentRow(this, legendStyleConfigureLabel, legendStyleConfigButton);
        }
        {
            JLabel axisStyleConfigureLabel = new ResourceLabel("plotter.configuration_dialog.global_config_panel.axis_style");
            JToggleButton axisStyleConfigureButton = new JToggleButton(new PopupAction(true, "plotter.configuration_dialog.open_popup", axisConfigurationContainer, PopupAction.PopupPosition.HORIZONTAL));
            axisStyleConfigureLabel.setLabelFor(axisStyleConfigureButton);
            addTwoComponentRow(this, axisStyleConfigureLabel, axisStyleConfigureButton);
        }
        {
            JLabel colorConfigureLabel = new ResourceLabel("plotter.configuration_dialog.global_config_panel.color_scheme");
            colorsSchemesComboBoxModel = new DefaultComboBoxModel();
            colorSchemesComboBox = new JComboBox(colorsSchemesComboBoxModel);
            colorConfigureLabel.setLabelFor(colorSchemesComboBox);
            colorSchemesComboBox.setRenderer(new ColorSchemeComboBoxRenderer());
            colorSchemesComboBox.addPopupMenuListener(new PopupMenuListener() {

                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    return;
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    ColorScheme colorScheme = (ColorScheme) colorSchemesComboBox.getSelectedItem();
                    if (colorScheme != null) {
                        getPlotConfiguration().setActiveColorScheme(colorScheme.getName());
                    }
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent e) {
                    return;
                }
            });
            JButton colorConfigButton = new JButton(new ResourceAction(true, "plotter.configuration_dialog.open_color_scheme_dialog") {

                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent e) {
                    createColorSchemeDialog();
                }
            });
            addThreeComponentRow(this, colorConfigureLabel, colorSchemesComboBox, colorConfigButton);
        }
        {
            plotBackGroundColorLabel = new ResourceLabel("plotter.configuration_dialog.global_config_panel.select_plot_background_color");
            plotBackgroundColorChooserButton = new JButton(new ResourceAction(true, "plotter.configuration_dialog.select_plot_color") {

                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent e) {
                    createPlotBackgroundColorDialog();
                }
            });
            plotBackGroundColorLabel.setLabelFor(plotBackgroundColorChooserButton);
            addTwoComponentRow(this, plotBackGroundColorLabel, plotBackgroundColorChooserButton);
        }
        {
            frameBackGroundColorLabel = new ResourceLabel("plotter.configuration_dialog.global_config_panel.select_frame_background_color");
            frameBackgroundColorChooserButton = new JButton(new ResourceAction(true, "plotter.configuration_dialog.select_frame_color") {

                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent e) {
                    createFrameBackgroundColorDialog();
                }
            });
            frameBackGroundColorLabel.setLabelFor(frameBackgroundColorChooserButton);
            addTwoComponentRow(this, frameBackGroundColorLabel, frameBackgroundColorChooserButton);
        }
        {
            JPanel spacerPanel = new JPanel();
            GridBagConstraints itemConstraint = new GridBagConstraints();
            itemConstraint.fill = GridBagConstraints.BOTH;
            itemConstraint.weightx = 1;
            itemConstraint.weighty = 1;
            itemConstraint.gridwidth = GridBagConstraints.REMAINDER;
            this.add(spacerPanel, itemConstraint);
        }
    }

    private void startAnimation() {
        StaticDebug.debug("Starting animation");
    }

    @Override
    protected void adaptGUI() {
        String title = getPlotConfiguration().getTitleText();
        if (title == null) {
            title = "";
        }
        if (!title.equals(titleTextField.getText())) {
            titleTextField.setText(title);
        }
        Font titleFont = getPlotConfiguration().getTitleFont();
        if (titleFont != null) {
            titleTextField.setFont(new Font(titleFont.getFamily(), titleFont.getStyle(), fontSize));
        }
        plotOrientationChanged(getPlotConfiguration().getOrientation());
        Color backgroundColor = getPlotConfiguration().getPlotBackgroundColor();
        if (backgroundColor == null) {
            backgroundColor = Color.white;
        }
        Color chartBackgroundColor = getPlotConfiguration().getChartBackgroundColor();
        if (chartBackgroundColor == null) {
            chartBackgroundColor = Color.white;
        }
        Map<String, ColorScheme> colorSchemes = getPlotConfiguration().getColorSchemes();
        colorsSchemesComboBoxModel.removeAllElements();
        for (ColorScheme scheme : colorSchemes.values()) {
            colorsSchemesComboBoxModel.addElement(scheme);
        }
        colorsSchemesComboBoxModel.setSelectedItem(getPlotConfiguration().getActiveColorScheme());
    }

    private void plotOrientationChanged(PlotOrientation orientation) {
        plotOrientationComboBox.setSelectedItem(orientation);
    }

    private void createPlotBackgroundColorDialog() {
        Color oldColor = getPlotConfiguration().getPlotBackgroundColor();
        if (oldColor == null) {
            oldColor = Color.white;
        }
        Color newBackgroundColor = JColorChooser.showDialog(this, I18N.getGUILabel("plotter.configuration_dialog.global_config_panel.plot_background_color_title.label"), oldColor);
        if (newBackgroundColor != null && !(newBackgroundColor.equals(oldColor))) {
            getPlotConfiguration().setPlotBackgroundColor(newBackgroundColor);
        }
    }

    private void createFrameBackgroundColorDialog() {
        Color oldColor = getPlotConfiguration().getChartBackgroundColor();
        if (oldColor == null) {
            oldColor = Color.white;
        }
        Color newBackgroundColor = JColorChooser.showDialog(this, I18N.getGUILabel("plotter.configuration_dialog.global_config_panel.chart_background_color_title.label"), oldColor);
        if (newBackgroundColor != null && !(newBackgroundColor.equals(oldColor))) {
            getPlotConfiguration().setFrameBackgroundColor(newBackgroundColor);
        }
    }

    private void createColorSchemeDialog() {
        ColorSchemeDialog colorSchemeDialog = new ColorSchemeDialog(this, "plotter.configuration_dialog.color_scheme_dialog", getPlotConfiguration());
        colorSchemeDialog.setVisible(true);
    }

    @Override
    public boolean plotConfigurationChanged(PlotConfigurationChangeEvent change) {
        PlotConfigurationChangeType type = change.getType();
        switch(type) {
            case CHART_TITLE:
                adaptGUI();
                break;
            case PLOT_ORIENTATION:
                adaptGUI();
                break;
            case PLOT_BACKGROUND_COLOR:
                adaptGUI();
                break;
            case FRAME_BACKGROUND_COLOR:
                adaptGUI();
                break;
            case COLOR_SCHEME:
                adaptGUI();
                break;
            case META_CHANGE:
                adaptGUI();
                break;
        }
        return true;
    }
}
