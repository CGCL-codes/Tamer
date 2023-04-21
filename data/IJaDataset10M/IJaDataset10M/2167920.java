package de.unikoeln.genetik.popgen.jfms.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import de.unikoeln.genetik.popgen.jfms.model.DiscreteSelectionCoefficientDistribution;
import de.unikoeln.genetik.popgen.jfms.model.NonEpistaticEnvironment;
import de.unikoeln.genetik.popgen.jfms.model.Parameter;
import de.unikoeln.genetik.popgen.jfms.model.SelectionCoefficientDistribution;

@SuppressWarnings("serial")
public class SelectionDialog extends JDialog {

    private final int MAXDIGITS = 9;

    private final double rounding = Math.pow(10.0, MAXDIGITS);

    class RowPanel extends JPanel {

        private JTextField selectionCoefficientField = new JTextField(5);

        private JTextField probabilityField = new JTextField(5);

        private JButton deleteButton = new JButton("Delete");

        private JButton addButton = new JButton("  Add  ");

        RowPanel(boolean editable) {
            selectionCoefficientField.setEditable(editable);
            probabilityField.setEditable(editable);
            selectionCoefficientField.setHorizontalAlignment(SwingConstants.CENTER);
            probabilityField.setHorizontalAlignment(SwingConstants.CENTER);
            setLayout(new FlowLayout());
            add(new JLabel("Selection coefficient: "));
            add(selectionCoefficientField);
            add(new JLabel(" Probability: "));
            add(probabilityField);
            if (editable) {
                add(deleteButton);
                deleteButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        gridPanel.remove(RowPanel.this);
                        updateDistribution();
                        gridPanel.updateUI();
                    }
                });
            } else {
                add(addButton);
                addButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        if (updateDistribution()) {
                            gridPanel.add(new RowPanel(true));
                            gridPanel.updateUI();
                        }
                    }
                });
            }
        }

        double getProbability() throws NumberFormatException {
            String s = probabilityField.getText().trim();
            return s.isEmpty() ? 0 : Double.parseDouble(s);
        }

        double getSelectionCoefficient() throws NumberFormatException {
            String s = selectionCoefficientField.getText().trim();
            return s.isEmpty() ? 0 : Double.parseDouble(s);
        }

        void setProbability(double d) {
            probabilityField.setText("" + d);
        }

        void setSelectionCoefficient(double d) {
            selectionCoefficientField.setText("" + d);
        }
    }

    private JButton okButton = new JButton("OK");

    private JButton cancelButton = new JButton("Cancel");

    private GridLayout gridLayout = new GridLayout(0, 1);

    private JPanel gridPanel = new JPanel(gridLayout);

    private final Parameter parameter;

    SelectionDialog(Frame frame, Parameter parameter) {
        super(frame, true);
        setTitle("Probability distribution of selection coefficients");
        this.parameter = parameter;
        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(gridPanel, BorderLayout.CENTER);
        JPanel panel = new JPanel();
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (updateDistribution()) {
                    readParameter();
                    SelectionDialog.this.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(SelectionDialog.this, "No proper probability distribution!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SelectionDialog.this.setVisible(false);
            }
        });
        writeParameter();
        JPanel buttonPanel = new JPanel(new GridLayout());
        buttonPanel.add(cancelButton);
        buttonPanel.add(okButton);
        panel = new JPanel();
        panel.add(inputPanel);
        add(panel, BorderLayout.CENTER);
        panel = new JPanel();
        panel.add(buttonPanel);
        add(panel, BorderLayout.SOUTH);
        setLocation(200, 200);
        pack();
        setSize(500, 200);
    }

    private boolean updateDistribution() {
        double p = 1.0;
        for (int i = 1; i < gridPanel.getComponentCount(); i++) {
            RowPanel rowPanel = (RowPanel) gridPanel.getComponent(i);
            double probability = rowPanel.getProbability();
            if (probability < 0 || probability > 1) return false;
            p = p - probability;
        }
        p = round(p);
        if (0 <= p && p <= 1.0) {
            ((RowPanel) gridPanel.getComponent(0)).setProbability(p);
            return true;
        }
        return false;
    }

    private double round(double d) {
        return Math.round(d * rounding) / rounding;
    }

    private void readParameter() throws NumberFormatException {
        Map<Double, Double> selectionCoefficients = new TreeMap<Double, Double>();
        for (Object rowPanel : gridPanel.getComponents()) {
            double probability = ((RowPanel) rowPanel).getProbability();
            double selectionCoefficient = ((RowPanel) rowPanel).getSelectionCoefficient();
            if (selectionCoefficients.containsKey(selectionCoefficient)) {
                probability += selectionCoefficients.get(selectionCoefficient);
            }
            if (probability > 0) {
                selectionCoefficients.put(selectionCoefficient, round(probability));
            }
        }
        SelectionCoefficientDistribution sl = new DiscreteSelectionCoefficientDistribution(selectionCoefficients);
        NonEpistaticEnvironment environment = new NonEpistaticEnvironment(sl, new TreeMap<Double, Double>());
        parameter.setEnvironment(environment);
    }

    private void reset() {
        gridPanel.removeAll();
        gridPanel.add(new RowPanel(false));
    }

    @Override
    public void setVisible(boolean b) {
        if (b) writeParameter();
        super.setVisible(b);
    }

    private void writeParameter() {
        reset();
        Map<Double, Double> map;
        if (parameter.getEnvironment() != null && parameter.getEnvironment() instanceof NonEpistaticEnvironment) {
            map = ((NonEpistaticEnvironment) parameter.getEnvironment()).getSelectionCoefficientDistribution().getDistribution();
        } else {
            map = new HashMap<Double, Double>();
            map.put(1.0, 1.0);
        }
        RowPanel rowPanel;
        for (Double selectionCoefficient : map.keySet()) {
            if (selectionCoefficient == 1.0) {
                rowPanel = (RowPanel) gridPanel.getComponent(0);
            } else {
                rowPanel = new RowPanel(true);
                gridPanel.add(rowPanel);
            }
            rowPanel.setSelectionCoefficient(selectionCoefficient);
            rowPanel.setProbability(map.get(selectionCoefficient));
        }
    }
}
