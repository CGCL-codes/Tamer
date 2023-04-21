package org.jbudget.gui.budget;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

/**
 *
 * @author petrov
 */
public class BudgetSelectorDialog extends JDialog {

    private final BudgetSelectorPanel panel;

    private final JButton okButton = new JButton("OK");

    private final JButton cancelButton = new JButton("Cancel");

    /** A guard variable. It is set to true when either "OK" or "Cancel"
   * buttons are pressed.
   */
    private boolean selectionMade = false;

    /** The id of the selected budget */
    private int selectedBudgetId = -1;

    /** Creates a new instance of BudgetSelectorDialog.
   * @param showUnused if true only unused budgets are shown.
   */
    public BudgetSelectorDialog(JFrame parent, boolean showUnused) {
        super(parent, "Choose a budget", true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                selectedBudgetId = panel.getSelectedBudgetID();
                selectionMade = true;
                close();
            }
        });
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                selectedBudgetId = -1;
                selectionMade = true;
                close();
            }
        });
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent ev) {
                if (!selectionMade) {
                    selectedBudgetId = -1;
                    selectionMade = true;
                    close();
                }
            }
        });
        setTitle("Select a budget");
        panel = new BudgetSelectorPanel(showUnused);
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BorderLayout());
        innerPanel.add(panel, BorderLayout.CENTER);
        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(0, 40));
        controlPanel.setMinimumSize(new Dimension(100, 30));
        innerPanel.add(controlPanel, BorderLayout.SOUTH);
        SpringLayout layout = new SpringLayout();
        layout.putConstraint(SpringLayout.WEST, cancelButton, 10, SpringLayout.WEST, controlPanel);
        layout.putConstraint(SpringLayout.NORTH, cancelButton, 5, SpringLayout.NORTH, controlPanel);
        layout.putConstraint(SpringLayout.EAST, okButton, -10, SpringLayout.EAST, controlPanel);
        layout.putConstraint(SpringLayout.NORTH, okButton, 5, SpringLayout.NORTH, controlPanel);
        controlPanel.setLayout(layout);
        controlPanel.add(cancelButton);
        controlPanel.add(okButton);
        setContentPane(innerPanel);
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    /** Frees resources */
    public void dispose() {
        super.dispose();
        panel.dispose();
    }

    /** Displays a dialog and waits for the selection. Returns the ID of 
   *  the selected budget or -1 if the dialog was closed without making a
   *  selection. 
   */
    public static int showDialog(JFrame parent) {
        BudgetSelectorDialog dialog = new BudgetSelectorDialog(parent, false);
        dialog.dispose();
        return dialog.selectedBudgetId;
    }

    /** Displays a dialog and waits for the selection. Returns the ID of 
   *  the selected budget or -1 if the dialog was closed without making a
   *  selection. 
   * @param showUnused if true only unused budgets are shown.
   */
    public static int showDialog(JFrame parent, boolean showUnused) {
        BudgetSelectorDialog dialog = new BudgetSelectorDialog(parent, showUnused);
        dialog.dispose();
        return dialog.selectedBudgetId;
    }

    /** Close the dialog */
    private void close() {
        setVisible(false);
        dispose();
    }
}
