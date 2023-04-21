package coda.plot2.window;

import coda.CoDaStats;
import coda.DataFrame;
import coda.plot.window.*;
import coda.gui.CoDaPackMain;
import coda.plot2.objects.Ternary2dGridObject;
import coda.plot2.TernaryPlot2dDisplay;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author marc
 */
public class TernaryPlot2dWindow extends CoDaPlot2dWindow {

    private double vx[] = new double[2];

    private double vy[] = new double[2];

    private final double sinA = 0.86602540378443864676372317075294;

    private final double cosA = 0.5;

    private TernaryPlot2dDisplay ternaryPlot;

    private JCheckBox checkBoxGridSelector = new JCheckBox();

    private JCheckBox checkBoxCenteredSelector = new JCheckBox();

    private double invCenter[] = null;

    private JButton rotate;

    private JButton inverted;

    protected JMenu menuEdit;

    private final String ITEM_EDIT = "Edit";

    private JMenuItem itemAddDataSet;

    private final String ITEM_DATA_SET = "Add data set";

    private JMenuItem itemAddCurve;

    private final String ITEM_CURVE = "Add curve";

    public TernaryPlot2dWindow(DataFrame dataframe, TernaryPlot2dDisplay display, String title) {
        super(dataframe, display, title);
        menuEdit = new JMenu();
        menuEdit.setText(ITEM_EDIT);
        itemAddDataSet = new JMenuItem();
        itemAddDataSet.setText(ITEM_DATA_SET);
        itemAddDataSet.addActionListener(new ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDataSet();
            }
        });
        menuEdit.add(itemAddDataSet);
        itemAddCurve = new JMenuItem();
        itemAddCurve.setText(ITEM_CURVE);
        itemAddCurve.addActionListener(new ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCurve();
            }
        });
        menuEdit.add(itemAddCurve);
        jMenuBar.add(menuEdit);
        ternaryPlot = display;
        checkBoxGridSelector.setText("Grid");
        checkBoxGridSelector.addActionListener(new ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxGridSelectorActionPerformed(evt);
            }
        });
        checkBoxCenteredSelector.setText("Centered");
        checkBoxCenteredSelector.addActionListener(new ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxCenteredSelectorActionPerformed(evt);
            }
        });
        inverted = new JButton(new ImageIcon(CoDaPackMain.RESOURCE_PATH + "ternary_xzy.png"));
        rotate = new JButton(new ImageIcon(CoDaPackMain.RESOURCE_PATH + "rotate.png"));
        inverted.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inverted_buttonEvent(evt);
            }
        });
        JPanel controlTernaryPlot = new JPanel();
        controlTernaryPlot.setSize(0, 40);
        controlTernaryPlot.add(inverted);
        rotate.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rotate_buttonEvent(evt);
            }
        });
        controlTernaryPlot.add(rotate);
        controlTernaryPlot.add(new JLabel(" "));
        controlTernaryPlot.add(checkBoxGridSelector);
        controlTernaryPlot.add(checkBoxCenteredSelector);
        particularControls1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        particularControls1.add(controlTernaryPlot);
        pack();
    }

    void addDataSet() {
        new TernaryPlot2dDialogDataSet(this).setVisible(true);
    }

    void addCurve() {
        new TernaryPlot2dDialogCurve(this).setVisible(true);
    }

    void inverted_buttonEvent(ActionEvent ev) {
        vx = ternaryPlot.getVX();
        vy = ternaryPlot.getVY();
        vx[0] = -vx[0];
        vy[0] = -vy[0];
        ternaryPlot.setVX(vx[0], vx[1]);
        ternaryPlot.setVY(vy[0], vy[1]);
        ternaryPlot.repaint();
    }

    void rotate_buttonEvent(ActionEvent ev) {
        vx = ternaryPlot.getVX();
        vy = ternaryPlot.getVY();
        double x = vx[0];
        double y = vx[1];
        vx[0] = cosA * x - sinA * y;
        vx[1] = sinA * x + cosA * y;
        ternaryPlot.setVX(vx[0], vx[1]);
        x = vy[0];
        y = vy[1];
        vy[0] = cosA * x - sinA * y;
        vy[1] = sinA * x + cosA * y;
        ternaryPlot.setVY(vy[0], vy[1]);
        ternaryPlot.repaint();
    }

    @Override
    public void repaint() {
        super.repaint();
    }

    public void setCenter(double center[]) {
        this.invCenter = CoDaStats.powering(center, -1);
    }

    public TernaryPlot2dDisplay getDisplay() {
        return ternaryPlot;
    }

    private void checkBoxGridSelectorActionPerformed(java.awt.event.ActionEvent evt) {
        ArrayList<Ternary2dGridObject> grids = ternaryPlot.getGrid();
        if (checkBoxGridSelector.isSelected()) {
            for (Ternary2dGridObject grid : grids) grid.setVisible(true);
        } else {
            for (Ternary2dGridObject grid : grids) grid.setVisible(false);
        }
        ternaryPlot.repaint();
    }

    private void checkBoxCenteredSelectorActionPerformed(java.awt.event.ActionEvent evt) {
        if (checkBoxCenteredSelector.isSelected()) {
            double[] center = ternaryPlot.getCenter();
            invCenter[0] = 1 / center[0];
            invCenter[1] = 1 / center[1];
            invCenter[2] = 1 / center[2];
            ternaryPlot.perturbate(invCenter);
        } else {
            double[] ones = { 1, 1, 1 };
            ternaryPlot.perturbate(ones);
        }
        ternaryPlot.repaint();
    }
}
