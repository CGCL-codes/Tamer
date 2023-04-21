package mou.gui.colonyscreen;

import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import mou.Main;
import mou.gui.GUI;

/**
 * @author pb
 */
public class DiscardBuildingDialog extends JDialog {

    protected JSlider slider = new JSlider(0, 0);

    private JLabel labelName = new JLabel();

    private JLabel labelMenge = new JLabel();

    private JButton buttonDiscard = new JButton("Abrei�en");

    private JButton buttonCancel = new JButton("Abbrechen");

    private boolean ok = false;

    /**
	 * @throws java.awt.HeadlessException
	 */
    public DiscardBuildingDialog() throws HeadlessException {
        super(Main.instance().getGUI().getMainFrame(), "Kaufen", true);
        Box boxY = Box.createVerticalBox();
        getContentPane().add(boxY);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        boxY.add(panel);
        panel.add(new JLabel("Abrei�en: "));
        panel.add(labelName);
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        boxY.add(panel);
        panel.add(new JLabel("Anzahl: "));
        panel.add(labelMenge);
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        boxY.add(panel);
        panel.add(slider);
        panel = new JPanel();
        boxY.add(panel);
        panel.add(buttonDiscard);
        buttonDiscard.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ok = true;
                setVisible(false);
            }
        });
        panel.add(buttonCancel);
        buttonCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                slider.setValue(0);
                setVisible(false);
            }
        });
        slider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                labelMenge.setText(GUI.formatLong(slider.getValue()));
            }
        });
        slider.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Escape");
        slider.getActionMap().put("Escape", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
    }

    /**
	 * @param maxQuantity
	 * @param itemPrice
	 * @param itemName
	 * @return ausgew�hlte Anzahl der Objekte
	 */
    public int showDialog(int maxQuantity, String itemName) {
        slider.setMaximum(maxQuantity);
        slider.setValue(0);
        labelName.setText(itemName);
        Hashtable<Integer, JComponent> labels = new Hashtable<Integer, JComponent>();
        labels.put(new Integer(0), new JLabel("0"));
        labels.put(new Integer(maxQuantity), new JLabel(GUI.formatLong(maxQuantity)));
        slider.setLabelTable(labels);
        slider.setPaintLabels(true);
        pack();
        GUI.centreWindow(Main.instance().getGUI().getMainFrame(), this);
        setVisible(true);
        if (ok) {
            ok = false;
            return slider.getValue();
        } else return 0;
    }
}
