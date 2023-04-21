package jebl.gui.trees.treeviewer_dev.treelayouts;

import org.virion.jam.controlpalettes.Controller;
import org.virion.jam.controlpalettes.ControllerSettings;
import org.virion.jam.panels.OptionsPanel;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Andrew Rambaut
 * @version $Id: RectilinearTreeLayoutController.java 294 2006-04-14 10:28:11Z rambaut $
 */
public class RectilinearTreeLayoutController implements Controller {

    public RectilinearTreeLayoutController(final RectilinearTreeLayout treeLayout) {
        this.treeLayout = treeLayout;
        titleLabel = new JLabel("Rectangular Layout");
        optionsPanel = new OptionsPanel();
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
        curvatureSlider = new JSlider(SwingConstants.HORIZONTAL, 0, sliderMax, 0);
        curvatureSlider.setValue((int) (treeLayout.getCurvature() * sliderMax));
        curvatureSlider.setMajorTickSpacing(curvatureSlider.getMaximum() / 5);
        curvatureSlider.setPaintTicks(true);
        curvatureSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent changeEvent) {
                double value = curvatureSlider.getValue();
                treeLayout.setCurvature(value / sliderMax);
            }
        });
        optionsPanel.addComponentWithLabel("Curvature:", curvatureSlider, true);
        alignTipLabelsCheck = new JCheckBox("Align Tip Labels");
        alignTipLabelsCheck.setSelected(treeLayout.isAlignTipLabels());
        alignTipLabelsCheck.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent changeEvent) {
                treeLayout.setAlignTipLabels(alignTipLabelsCheck.isSelected());
            }
        });
        optionsPanel.addComponent(alignTipLabelsCheck);
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

    private final JSlider rootLengthSlider;

    private final JSlider curvatureSlider;

    private final JCheckBox alignTipLabelsCheck;

    private final RectilinearTreeLayout treeLayout;
}
