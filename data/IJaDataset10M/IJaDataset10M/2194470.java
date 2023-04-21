package org.tigr.microarray.mev.cluster.gui.impl.usc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import org.tigr.microarray.mev.r.ClassAssigner;
import org.tigr.microarray.mev.cluster.gui.impl.dialogs.AlgorithmDialog;
import org.tigr.microarray.mev.cluster.gui.impl.dialogs.DialogListener;
import org.tigr.microarray.mev.cluster.gui.impl.dialogs.dialogHelpUtil.HelpWindow;

/**
 * Dialog box for user to choose what type of analysis to run.  If training, user
 * enters the class labels here.
 * 
 * @author vu
 */
public class USCAssignLabel extends AlgorithmDialog {

    private int result;

    private String[] userLabelArray;

    private JPanel mainPanel;

    private JButton loadButton;

    private JButton saveButton;

    private ClassAssigner ca;

    /**
	 * 
	 * @param hybArray		[ String ] of hyb names
	 * @param labelArray	[ String ] of labels entered by user in ClassDialog
	 */
    public USCAssignLabel(String[] hybArray, String[] labelArray) {
        super(new JFrame(), "USCAssignLabel", true);
        this.userLabelArray = labelArray;
        this.setSize(555, 600);
        this.initGUI(hybArray, labelArray);
        Listener l = new Listener();
        super.addWindowListener(l);
        super.setActionListeners(l);
    }

    private void initGUI(String[] hybs, String[] labels) {
        Dimension dLabel = new Dimension(350, 20);
        Dimension dCombo = new Dimension(150, 20);
        int iHyb = hybs.length;
        int iLabel = labels.length;
        JPanel selectionPanel = new JPanel(new SpringLayout());
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Assign Labels"));
        this.ca = new ClassAssigner(hybs, labels, true, 3);
        this.mainPanel = new JPanel();
        this.mainPanel.add(this.ca.getScrollPane(), BorderLayout.NORTH);
        this.mainPanel.add(this.createButtonPanel(), BorderLayout.SOUTH);
        this.addContent(this.mainPanel);
    }

    private JPanel createButtonPanel() {
        JPanel toReturn = new JPanel();
        toReturn.setLayout(new BoxLayout(toReturn, BoxLayout.X_AXIS));
        Dimension dButton = new Dimension(150, 20);
        String title = "Assignments Files";
        Border greyLine = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
        Font font11 = new Font("Arial", Font.PLAIN, 11);
        TitledBorder border = BorderFactory.createTitledBorder(greyLine, title, TitledBorder.LEADING, TitledBorder.TOP, font11);
        toReturn.setBorder(border);
        this.loadButton = new JButton("Load Assignments");
        this.loadButton.setPreferredSize(dButton);
        this.saveButton = new JButton("Save Assignments");
        this.saveButton.setPreferredSize(dButton);
        AdvListener al = new AdvListener();
        this.loadButton.addActionListener(al);
        this.saveButton.addActionListener(al);
        toReturn.add(Box.createHorizontalGlue());
        toReturn.add(this.saveButton);
        toReturn.add(Box.createRigidArea(new Dimension(50, 20)));
        toReturn.add(this.loadButton);
        toReturn.add(Box.createHorizontalGlue());
        return toReturn;
    }

    /**
	 * Displays a dialog box with a message
	 * @param message
	 */
    private void error(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
	 * Shows this AlgorithmDialog
	 * @return	
	 */
    public int showModal() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - getSize().width) / 2, (screenSize.height - getSize().height) / 2);
        show();
        return result;
    }

    /**
	 * 
	 * @author iVu
	 */
    private class AdvListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == loadButton) {
                ca.onLoadAssignments();
            } else if (source == saveButton) {
                ca.onSaveAssignments();
            }
        }
    }

    /**
	 * The class to listen to the dialog and check boxes items events.
	 */
    private class Listener extends DialogListener implements ItemListener {

        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("ok-command")) {
                if (ca.verifyLabeling()) {
                    result = JOptionPane.OK_OPTION;
                    dispose();
                } else {
                }
            } else if (command.equals("cancel-command")) {
                result = JOptionPane.CANCEL_OPTION;
                dispose();
            } else if (command.equals("reset-command")) {
                result = JOptionPane.CANCEL_OPTION;
                return;
            } else if (command.equals("info-command")) {
                HelpWindow hw = new HelpWindow(USCAssignLabel.this, "USC Assign Label Dialog");
                result = JOptionPane.CANCEL_OPTION;
                if (hw.getWindowContent()) {
                    hw.setSize(450, 600);
                    hw.setLocation();
                    hw.show();
                    return;
                } else {
                    hw.setVisible(false);
                    hw.dispose();
                    return;
                }
            }
        }

        public void itemStateChanged(ItemEvent e) {
        }

        public void windowClosing(WindowEvent e) {
            result = JOptionPane.CLOSED_OPTION;
            dispose();
        }
    }

    public static void main(String[] args) {
        System.out.println("invoked by main()");
        String[] hybs = { "011101_16011521000000_S01_A01.txt", "011101_16011521000001_S01_A01.txt", "011101_16011521000002_S01_A01.txt", "011101_16011521000003_S01_A01.txt", "011101_16011521000004_S01_A01.txt", "011101_16011521000005_S01_A01.txt", "011101_16011521000006_S01_A01.txt", "011101_16011521000007_S01_A01.txt", "011101_16011521000008_S01_A01.txt", "011101_16011521000009_S01_A01.txt", "011101_16011521000010_S01_A01.txt" };
        String[] labels = { "tumor", "normal", "flu" };
        USCAssignLabel d = new USCAssignLabel(hybs, labels);
        d.showModal();
    }

    public String[] getHybLabels() {
        String[] toReturn = new String[this.ca.getVComboBox().size()];
        for (int i = 0; i < this.ca.getVComboBox().size(); i++) {
            toReturn[i] = this.ca.getSelectedString(i);
        }
        return toReturn;
    }
}
