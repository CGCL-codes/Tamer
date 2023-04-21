package jebl.gui.trees.treeviewer_dev.treelayouts;

import org.virion.jam.panels.OptionsPanel;
import org.virion.jam.controlpalettes.ControllerSettings;
import org.virion.jam.controlpalettes.Controller;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author Andrew Rambaut
 * @version $Id: PolarTreeLayoutController.java 294 2006-04-14 10:28:11Z rambaut $
 */
public class PolarTreeLayoutController implements Controller {

    public PolarTreeLayoutController(final PolarTreeLayout treeLayout) {
        this.treeLayout = treeLayout;
        titleLabel = new JLabel("Polar Layout");
        optionsPanel = new OptionsPanel();
        rootAngleSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 3600, 0);
        rootAngleSlider.setValue((int) (180.0 - (treeLayout.getRootAngle() * 10)));
        rootAngleSlider.setMajorTickSpacing(rootAngleSlider.getMaximum() / 5);
        rootAngleSlider.setPaintTicks(true);
        rootAngleSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent changeEvent) {
                double value = 180 + (rootAngleSlider.getValue() / 10.0);
                treeLayout.setRootAngle(value % 360);
            }
        });
        optionsPanel.addComponentWithLabel("Root Angle:", rootAngleSlider, true);
        final int sliderMax = 10000;
        rootLengthSlider = new JSlider(SwingConstants.HORIZONTAL, 0, sliderMax, 0);
        rootLengthSlider.setValue((int) (treeLayout.getRootLength() * sliderMax));
        rootLengthSlider.setMajorTickSpacing(rootLengthSlider.getMaximum() / 5);
        rootLengthSlider.setPaintTicks(true);
        rootLengthSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent changeEvent) {
                double value = rootLengthSlider.getValue();
                treeLayout.setRootLength(value / sliderMax);
            }
        });
        optionsPanel.addComponentWithLabel("Root Length:", rootLengthSlider, true);
        angularRangeSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 3600, 0);
        angularRangeSlider.setValue((int) (360.0 - (treeLayout.getAngularRange() * 10)));
        angularRangeSlider.setMajorTickSpacing(angularRangeSlider.getMaximum() / 5);
        angularRangeSlider.setPaintTicks(true);
        angularRangeSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent changeEvent) {
                double value = 360.0 - (angularRangeSlider.getValue() / 10.0);
                treeLayout.setAngularRange(value);
            }
        });
        optionsPanel.addComponentWithLabel("Angle Range:", angularRangeSlider, true);
        labelPositionCombo = new JComboBox();
        for (PolarTreeLayout.TipLabelPosition position : PolarTreeLayout.TipLabelPosition.values()) {
            if (position != PolarTreeLayout.TipLabelPosition.HORIZONTAL) labelPositionCombo.addItem(position);
        }
        labelPositionCombo.setSelectedItem(treeLayout.getTipLabelPosition());
        labelPositionCombo.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent itemEvent) {
                treeLayout.setTipLabelPosition((PolarTreeLayout.TipLabelPosition) labelPositionCombo.getSelectedItem());
            }
        });
        optionsPanel.addComponentWithLabel("Label Position:", labelPositionCombo);
    }

    public JComponent getTitleComponent() {
        return titleLabel;
    }

    public JPanel getPanel() {
        return optionsPanel;
    }

    public boolean isInitiallyVisible() {
        return false;
    }

    public void setSettings(ControllerSettings settings) {
    }

    public void getSettings(ControllerSettings settings) {
    }

    private final JLabel titleLabel;

    private final OptionsPanel optionsPanel;

    private final JSlider rootAngleSlider;

    private final JSlider rootLengthSlider;

    private final JSlider angularRangeSlider;

    private final JComboBox labelPositionCombo;

    private final PolarTreeLayout treeLayout;
}
