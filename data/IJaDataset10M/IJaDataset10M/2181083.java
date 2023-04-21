package com.rapidminer.tools.math.matrix;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.rapidminer.tools.IterationArrayList;

/**
 * This class visualizes matrices by two combo boxes that allow to specify 
 * a certain entry in the matrix.
 * 
 * @author Michael Wurst, Ingo Mierswa
 * @version $Id: MatrixComboBoxVisualizer.java,v 1.2 2009-03-14 08:48:56 ingomierswa Exp $
 * 
 */
public class MatrixComboBoxVisualizer<Ex, Ey> extends JPanel implements ItemListener {

    private static final long serialVersionUID = 1890397148444056696L;

    private List<Ex> xLabels;

    private List<Ey> yLabels;

    private JComboBox chooseBox1;

    private JComboBox chooseBox2;

    private JLabel label;

    private Matrix<Ex, Ey> matrix;

    public MatrixComboBoxVisualizer(Matrix<Ex, Ey> matrix) {
        this.matrix = matrix;
        this.xLabels = new IterationArrayList<Ex>(matrix.getXLabels());
        this.yLabels = new IterationArrayList<Ey>(matrix.getYLabels());
        JPanel panel = new JPanel(new FlowLayout());
        chooseBox1 = new JComboBox(new Vector<Ex>(xLabels));
        chooseBox2 = new JComboBox(new Vector<Ey>(yLabels));
        label = new JLabel("value: 0.0");
        chooseBox1.addItemListener(this);
        chooseBox2.addItemListener(this);
        panel.add(new JLabel("x: "));
        panel.add(chooseBox1);
        panel.add(new JLabel(" y: "));
        panel.add(chooseBox2);
        panel.add(label);
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
    }

    public void itemStateChanged(ItemEvent e) {
        Ex o1 = xLabels.get(chooseBox1.getSelectedIndex());
        Ey o2 = yLabels.get(chooseBox2.getSelectedIndex());
        label.setText("value: " + matrix.getEntry(o1, o2));
    }
}
