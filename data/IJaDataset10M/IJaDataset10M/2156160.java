package org.grapheditor.properties;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.grapheditor.editor.GraphEditorPane;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class GlobalGraphGridProperties extends JPanel implements PropertiesModule {

    private GraphEditorPane graphPane;

    private JPanel jPanel = null;

    private JCheckBox jCheckBox = null;

    private JComboBox jComboBox = null;

    private JPanel jPanel2;

    private JSpinner spinner;

    private JPanel mainPanel;

    private JCheckBox showGridCheckBox;

    public GlobalGraphGridProperties(GraphEditorPane graphPane) {
        this.graphPane = graphPane;
        initialize();
    }

    /**
	 * This method initializes this
	 * 			

	 */
    private void initialize() {
        this.setLayout(new BorderLayout());
        this.add(getMainPanel(), BorderLayout.NORTH);
        getMainPanel().setLayout(new BoxLayout(getMainPanel(), BoxLayout.Y_AXIS));
        GlobalProperties prop = GlobalProperties.getInstance();
        jCheckBox.setSelected(prop.isGridEnabled());
        spinner.setValue(prop.getGridSize());
        showGridCheckBox.setSelected(prop.isGridVisable());
        switch(prop.getGridMode()) {
            case GraphEditorPane.CROSS_GRID_MODE:
                jComboBox.setSelectedIndex(0);
                break;
            case GraphEditorPane.DOT_GRID_MODE:
                jComboBox.setSelectedIndex(1);
                break;
            case GraphEditorPane.LINE_GRID_MODE:
                jComboBox.setSelectedIndex(2);
                break;
        }
        jCheckBox.getActionListeners()[0].actionPerformed(new ActionEvent("custom", 0, "action.."));
    }

    private JPanel getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new JPanel();
            mainPanel.add(getJCheckBox(), null);
            mainPanel.add(getJPanel(), null);
            mainPanel.add(getJPanel2(), null);
            mainPanel.add(getShowGridCheckBox());
        }
        return mainPanel;
    }

    private JCheckBox getShowGridCheckBox() {
        if (showGridCheckBox == null) {
            showGridCheckBox = new JCheckBox("Paint grid");
        }
        return showGridCheckBox;
    }

    public String getPropertiesDescription() {
        return "Properties for the grid in the graph editor";
    }

    public Component getPropertiesEditor() {
        return this;
    }

    public String getPropertiesName() {
        return "Grid";
    }

    public void save() {
        GlobalProperties prop = GlobalProperties.getInstance();
        prop.setGridEnabled(jCheckBox.isSelected());
        prop.setGridSize((Double) spinner.getValue());
        switch(jComboBox.getSelectedIndex()) {
            case 0:
                prop.setGridMode(GraphEditorPane.CROSS_GRID_MODE);
                break;
            case 1:
                prop.setGridMode(GraphEditorPane.DOT_GRID_MODE);
                break;
            case 2:
                prop.setGridMode(GraphEditorPane.LINE_GRID_MODE);
                break;
        }
        prop.setGridVisable(showGridCheckBox.isSelected());
        graphPane.setGridProps();
    }

    /**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));
            jPanel.setBorder(new TitledBorder("Grid type:"));
            jPanel.add(getJComboBox(), null);
            jPanel.add(Box.createHorizontalGlue());
        }
        return jPanel;
    }

    /**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel2() {
        if (jPanel2 == null) {
            jPanel2 = new JPanel();
            jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.X_AXIS));
            jPanel2.setBorder(new TitledBorder("Grid size:"));
            double min = 0.0;
            double max = 100.0;
            double step = 5.0;
            double initValue = 10.0;
            SpinnerModel model = new SpinnerNumberModel(initValue, min, max, step);
            spinner = new JSpinner(model);
            jPanel2.add(spinner, null);
            jPanel2.add(Box.createHorizontalGlue());
        }
        return jPanel2;
    }

    /**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
    private JCheckBox getJCheckBox() {
        if (jCheckBox == null) {
            jCheckBox = new JCheckBox();
            jCheckBox.setText("Enable grid");
            jCheckBox.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (!jCheckBox.isSelected()) {
                        jPanel.setEnabled(false);
                        jPanel2.setEnabled(false);
                        spinner.setEnabled(false);
                        jComboBox.setEnabled(false);
                        showGridCheckBox.setEnabled(false);
                    } else {
                        jPanel.setEnabled(true);
                        jPanel2.setEnabled(true);
                        spinner.setEnabled(true);
                        jComboBox.setEnabled(true);
                        showGridCheckBox.setEnabled(true);
                    }
                }
            });
        }
        return jCheckBox;
    }

    /**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getJComboBox() {
        if (jComboBox == null) {
            String[] gridTypes = { "Cross", "Dot", "Line" };
            jComboBox = new JComboBox(gridTypes);
        }
        return jComboBox;
    }

    public String toString() {
        return getPropertiesName();
    }
}
