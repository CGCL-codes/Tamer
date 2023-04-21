package twentetimo.org.visualizationEnvironment;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class VisualizationOptionsPanel extends JPanel {

    private static VisualizationOptionsPanel instance = null;

    public static VisualizationOptionsPanel getInstance() {
        if (instance == null) instance = new VisualizationOptionsPanel();
        return instance;
    }

    private VisualizationOptionsPanel() {
        gridPanel = new JPanel();
        gridPanel.setLayout(new BoxLayout(gridPanel, BoxLayout.Y_AXIS));
        addOrbitCheckBox();
        addCoolingCheckBox();
        addScale();
        addThreshold();
        contourDisplay = new ButtonPanel("Contour", new String[] { NONE, TEMP, ROLL, THRE });
        gridPanel.add(contourDisplay);
        textDisplay = new ButtonPanel("Text", new String[] { NONE, TEMP, ROLL });
        gridPanel.add(textDisplay);
        addMachinePathToggles();
        add(gridPanel);
    }

    private void addThreshold() {
        min = new JFormattedTextField(NumberFormat.getNumberInstance());
        min.setInputVerifier(new FormattedTextFieldVerifier());
        min.setValue(130.0f);
        max = new JFormattedTextField(NumberFormat.getNumberInstance());
        max.setInputVerifier(new FormattedTextFieldVerifier());
        max.setValue(180.0f);
        JPanel thresholdPanel = new JPanel();
        thresholdPanel.setLayout(new GridLayout(2, 2));
        gridPanel.add(thresholdPanel);
        thresholdPanel.add(new JLabel("min. Temp."));
        thresholdPanel.add(new JLabel("max. Temp."));
        thresholdPanel.add(min);
        thresholdPanel.add(max);
        thresholdPanel.setAlignmentX(LEFT_ALIGNMENT);
    }

    private void addScale() {
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
        Border titled = BorderFactory.createTitledBorder("Scale Factor");
        sliderPanel.setBorder(titled);
        gridPanel.add(sliderPanel);
        ySlider = new JSlider(1, 10);
        ySlider.setValue(1);
        sliderPanel.add(ySlider);
        ySlider.setPreferredSize(new Dimension(100, 20));
        ySlider.setMaximumSize(new Dimension(100, 20));
        ySlider.addChangeListener(new scaleListener());
        sliderPanel.setAlignmentX(LEFT_ALIGNMENT);
    }

    private void addCoolingCheckBox() {
        JCheckBox coolingCB = new JCheckBox("Display Cooling");
        coolingCB.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showCooling = showCooling ? false : true;
                VisualizationProject.getInstance().drawProject();
            }
        });
        gridPanel.add(coolingCB);
    }

    private void addOrbitCheckBox() {
        JCheckBox orbitCB = new JCheckBox("Orbit");
        orbitCB.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                isOrbiting = isOrbiting ? false : true;
                if (isOrbiting) {
                    OrbitBehavior orbit = new OrbitBehavior(MainCanvasPanel.getInstance().canvas(), OrbitBehavior.REVERSE_ALL);
                    orbit.setSchedulingBounds(VisualizationProject.getInstance().streetShape().getBounds());
                    ViewingPlatform vp = MainCanvasPanel.getInstance().universe().getViewingPlatform();
                    vp.setViewPlatformBehavior(orbit);
                } else {
                    ViewingPlatform vp = MainCanvasPanel.getInstance().universe().getViewingPlatform();
                    vp.setNominalViewingTransform();
                }
            }
        });
        gridPanel.add(orbitCB);
    }

    private void addMachinePathToggles() {
        JCheckBox showModelChk = new JCheckBox("Show Machine Model");
        gridPanel.add(showModelChk);
        showModelChk.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showModel = showModel ? false : true;
                AnimatedMachine.updateModelDisplay();
            }
        });
        JPanel machinePanel = new JPanel();
        machinePanel.setLayout(new BoxLayout(machinePanel, BoxLayout.Y_AXIS));
        Border titled = BorderFactory.createTitledBorder("Machine Passes");
        machinePanel.setBorder(titled);
        TreeMap<Integer, MachineType> types = MachineType.types();
        Collection c = types.values();
        Iterator itr = c.iterator();
        while (itr.hasNext()) {
            MachineType type = (MachineType) itr.next();
            final MachineButton btn = new MachineButton("Display " + type.name());
            btn.setType(type.id());
            machinePanel.add(btn);
            machineBtns.add(btn);
        }
        gridPanel.add(machinePanel);
    }

    public boolean isMachineToggled(int type) {
        for (int i = 0; i < machineBtns.size(); i++) {
            MachineButton mb = machineBtns.get(i);
            if (mb.type() == type) {
                return mb.isSelected();
            }
        }
        return false;
    }

    public float getMaxThreshold() {
        try {
            max.commitEdit();
            return Float.parseFloat(max.getText());
        } catch (Exception ex) {
            return 0.0f;
        }
    }

    public float getMinThreshold() {
        try {
            min.commitEdit();
            return Float.parseFloat(min.getText());
        } catch (Exception ex) {
            return 0.0f;
        }
    }

    public static void reset() {
        instance = new VisualizationOptionsPanel();
        MainCanvasPanel.getInstance().addDisplayToolbar();
    }

    public boolean showCooling() {
        return showCooling;
    }

    private JPanel gridPanel = null;

    private ButtonPanel contourDisplay;

    private ButtonPanel textDisplay;

    private ArrayList<MachineButton> machineBtns = new ArrayList<MachineButton>();

    public int contourDisplay() {
        String sel = contourDisplay.getSelection();
        return radioSelection(sel);
    }

    public int textDisplay() {
        String sel = textDisplay.getSelection();
        return radioSelection(sel);
    }

    private int radioSelection(String sel) {
        try {
            if (sel == NONE) return NO_SELECTION; else if (sel == TEMP) return TEMP_SELECTION; else if (sel == ROLL) return ROLL_SELECTION; else if (sel == THRE) return THRE_SELECTION; else {
                throw new Exception("Wrong radio box selection!");
            }
        } catch (Exception ex) {
            ErrorHandler.getInstance().ErrorHandling(ex);
            return -1;
        }
    }

    public int scaleFactor() {
        return scaleFactor;
    }

    public boolean showModel() {
        return showModel;
    }

    private boolean showCooling = true;

    private boolean isOrbiting = false;

    private int scaleFactor = 0;

    private boolean showModel = false;

    public class MachineButton extends JCheckBox implements ActionListener {

        public MachineButton(String name) {
            super(name);
            addActionListener(this);
        }

        public void setPrefferedSize(int i, int j) {
        }

        public int type() {
            return type;
        }

        public void setType(int t) {
            type = t;
        }

        int type;

        public void actionPerformed(ActionEvent e) {
            VisualizationProject.getInstance().drawMachinePaths();
        }
    }

    public class scaleListener implements ChangeListener {

        public void stateChanged(ChangeEvent arg0) {
            VisualizationProject.getInstance().scaleProject(1, ySlider.getValue());
        }
    }

    private class FormattedTextFieldVerifier extends InputVerifier {

        public boolean verify(JComponent component) {
            JFormattedTextField textField = (JFormattedTextField) component;
            return textField.isValid();
        }
    }

    private JSlider ySlider = null;

    private JFormattedTextField min = null;

    private JFormattedTextField max = null;

    private static final String NONE = "None";

    public static final int NO_SELECTION = 0;

    private static final String TEMP = "Temperature";

    public static final int TEMP_SELECTION = 1;

    private static final String ROLL = "Roller Passes";

    public static final int ROLL_SELECTION = 2;

    private static final String THRE = "Threshold";

    public static final int THRE_SELECTION = 3;
}
