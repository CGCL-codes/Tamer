package maplab.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import maplab.core.LabelGenerator;
import maplab.core.LabelGeneratorImpl;
import maplab.core.LabelGeneratorNew;
import maplab.core.Settings;
import maplab.core.Workspace;

/**
 * Window to change various parameters used by label generation.
 */
class SettingsWindow extends JFrame {

    private static final long serialVersionUID = -4434557115794176902L;

    /** Mapview displaying the routes. */
    MapView view;

    /** Creates a settings window. The window is not visible by default.
     *
     * @param view View displaying the routes and labels
     */
    SettingsWindow(MapView view) {
        this.view = view;
        createGUI();
        pack();
    }

    /** Constructs the settings window. */
    private void createGUI() {
        Setting[] settings = { new LabelSizeSlider(), new ComplexitySlider("Complexity grid size", Settings.COMPLEXITY_GRID_SIZE, 100, 3000), new ComplexitySlider("Complexity grids amount treshold", Settings.COMPLEX_GRIDS_AMOUNT_TRESHOLD, 1, 4, 3), new ComplexitySlider("Complexity length multiplier", Settings.COMPLEXITY_LENGTH_MULTIPLIER, 0, 3), new ComplexitySlider("Complexity route count multiplier", Settings.COMPLEXITY_ROUTECOUNT_MULTIPLIER, 0, 5), new DoubleSlider("Intersection distance factor", Settings.INTERSECTION_DISTANCE_FACTOR, 0.5, 5), new DoubleSlider("Maximum distance factor", Settings.MAX_DIST_FACTOR, 0, 10), new DoubleSlider("Disk cost factor", Settings.DISK_COST_FACTOR, 0, 5), new DoubleSlider("Label interval factor", Settings.LABEL_INTERVAL_FACTOR, 1, 80), new DoubleSlider("Road distance factor", Settings.LABEL_ROAD_DISTANCE_FACTOR, 0.01, 1), new DoubleSlider("Show Debug", Settings.SHOW_DEBUG, 0, 1, 1), new DoubleSlider("Show Quadtree", Settings.SHOW_QUADTREE, 0, 1, 1), new DoubleSlider("Quadtree max depth", Settings.QUADTREE_MAX_DEPTH, 0, 10, 10), new DoubleSlider("Show Vertices", Settings.SHOW_VERTICES, 0, 1, 1), new AlgorithmSelectionMenu("Algorithm", Settings.ALGORITHM) };
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        setLayout(layout);
        for (Setting s : settings) {
            ++c.gridy;
            addBag(layout, c, 0, new JLabel(s.name));
            addBag(layout, c, 1, s.createWidget());
            addBag(layout, c, 2, s.label);
        }
    }

    /** Adds a new component to some place of GridBagLayout.
     *
     * @param l Layout of this window
     * @param c Constraints for the placement of the component to add
     * @param x X-coordinate of the grid cell to use for the new component
     * @param component The component to add
     */
    private void addBag(GridBagLayout l, GridBagConstraints c, int x, JComponent component) {
        c.gridx = x;
        l.setConstraints(component, c);
        add(component);
    }

    /** Base class for all settings. */
    private abstract class Setting {

        /** Name of the setting. */
        String name;

        /** Index of the setting in <code>Settings</code> class. */
        int index;

        /** Label displaying value of the setting. */
        JLabel label = new JLabel();

        /** Creates a component to change setting value.
         *
         * @return Component where the value can be changed
         */
        abstract JComponent createWidget();

        /** Creates a setting.
         * 
         * @param name Name of the setting
         * @param index Index of the setting in <code>Settings</code> class
         */
        Setting(String name, int index) {
            this.name = name;
            this.index = index;
        }
    }

    /** Setting where the values is selected using a slider. */
    private class DoubleSlider extends Setting implements ChangeListener {

        /** Min and max values of the slider. */
        double min, max;

        /** Amount of different values the slider can take. */
        int values = 100;

        /** Creates a new slider.
         *
         * @param name Name of the setting
         * @param index Index of the setting
         * @param min Minimum value of the setting
         * @param max Maximum value of the setting
         */
        DoubleSlider(String name, int index, double min, double max) {
            super(name, index);
            this.min = min;
            this.max = max;
        }

        /** Creates a new slider.
         *
         * @param name Name of the setting
         * @param index Index of the setting
         * @param min Minimum value of the setting
         * @param max Maximum value of the setting
         * @param values Amount of different values the slider can take
         */
        DoubleSlider(String name, int index, double min, double max, int values) {
            super(name, index);
            this.min = min;
            this.max = max;
            this.values = values;
        }

        JComponent createWidget() {
            double start = getValue();
            label.setText(toStr(start));
            JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, values, (int) (values * (start - min) / (max - min)));
            slider.addChangeListener(this);
            return slider;
        }

        /** Returns the current value of the setting. */
        double getValue() {
            return Settings.INSTANCE.getValue(index);
        }

        /** Sets a new value for the setting. */
        void setValue(double value) {
            Settings.INSTANCE.setValue(index, value);
            label.setText(toStr(value));
        }

        /** Updates changed slider state to <code>Settings</code>-class.
         *
         * @param e Event created by the JSlider object
         */
        public void stateChanged(ChangeEvent e) {
            JSlider slider = (JSlider) e.getSource();
            double value = min + (double) slider.getValue() / values * (max - min);
            setValue(value);
        }
    }

    /** Slider to choose complexity grid parameters. */
    private class ComplexitySlider extends DoubleSlider {

        ComplexitySlider(String name, int index, double min, double max, int values) {
            super(name, index, min, max, values);
        }

        ComplexitySlider(String name, int index, double min, double max) {
            super(name, index, min, max);
        }

        public void stateChanged(ChangeEvent e) {
            JSlider s = (JSlider) e.getSource();
            if (!s.getValueIsAdjusting()) {
                Workspace.INSTANCE.generateMetadata();
                view.repaint();
            }
            super.stateChanged(e);
        }
    }

    /** Slider to choose label size. */
    private class LabelSizeSlider extends DoubleSlider {

        static final float START_SIZE = MapView.DEFAULT_LABEL_SIZE;

        LabelSizeSlider() {
            super("Label size", -1, START_SIZE / 5, START_SIZE * 2);
        }

        double getValue() {
            Font fnt = getFont();
            if (fnt == null) return START_SIZE;
            return fnt.getSize2D();
        }

        void setValue(double value) {
            float size = (float) value;
            view.setFont(getFont().deriveFont(size));
            label.setText(toStr(value));
        }
    }

    /** Writes a number as string displaying only few decimals. */
    private static String toStr(double x) {
        return String.format("%.2f", x);
    }

    private enum Algorithm {

        DEFAULT(new LabelGeneratorImpl()), MIDPOINT_ONLY(new LabelGeneratorNew());

        private LabelGenerator labelGenerator;

        private Algorithm(LabelGenerator labelGenerator) {
            this.labelGenerator = labelGenerator;
        }

        public LabelGenerator getLabelGenerator() {
            return labelGenerator;
        }
    }

    /** Class to view a dropdown menu */
    private class AlgorithmSelectionMenu extends Setting implements ActionListener {

        private JComboBox box;

        public AlgorithmSelectionMenu(String name, int index) {
            super(name, index);
        }

        @Override
        JComponent createWidget() {
            box = new JComboBox(Algorithm.values());
            box.addActionListener(this);
            return box;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Algorithm selected = (Algorithm) ((JComboBox) e.getSource()).getSelectedItem();
            Workspace.INSTANCE.setLabelGenerator(selected.getLabelGenerator());
        }
    }
}
