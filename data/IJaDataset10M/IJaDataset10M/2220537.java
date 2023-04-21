package org.javasock.jssniff;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.javasock.jssniff.handlerdata.HandlerData;
import org.javasock.jssniff.handlerpanel.HandlerPanel;

public class HandlerDataDialog extends JDialog implements ActionListener {

    /**
     * 
     * <p>
     *
     * @author John P. Wilson
     *
     * @version 03/00/2007
     */
    private static final long serialVersionUID = 1L;

    /**
     * OK button
     * <p>
     *
     * @author John P. Wilson
     *
     * @version 03/16/2007
     */
    private JButton okButton = null;

    /**
     * Cancel button
     * <p>
     *
     * @author John P. Wilson
     *
     * @version 03/16/2007
     */
    private JButton cancelButton = null;

    /**
     * The HandlerData model which we will be viewing using this class.
     * <p>
     *
     * @author John P. Wilson
     *
     * @version 03/00/2007
     */
    private HandlerData handlerData = null;

    /**
     * The HandlerPanel to view the model
     * <p>
     *
     * @author John P. Wilson
     *
     * @version 03/00/2007
     */
    private HandlerPanel handlerPanel = null;

    /**
     * Parent dialog.
     * <p>
     *
     * @author John P. Wilson
     *
     * @version 03/17/2007
     */
    private JDialog parent = null;

    /**
     * The class this contains the handler/filter tree
     * <p>
     *
     * @author John P. Wilson
     *
     * @version 03/21/2007
     */
    HandlerTreePanel treeOwner = null;

    public HandlerDataDialog(JDialog parentI, HandlerTreePanel treeOwnerI, boolean modalI, HandlerData handlerDataI) {
        super(parentI, "Filter Specification", modalI);
        parent = parentI;
        treeOwner = treeOwnerI;
        handlerData = handlerDataI;
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createGUI();
            }
        });
    }

    public void actionPerformed(ActionEvent eventI) {
        Object source = eventI.getSource();
        if (source == okButton) {
            if (handlerPanel.okAction()) {
                cleanup();
            }
        } else if (source == cancelButton) {
            cleanup();
        }
    }

    private void cleanup() {
        setVisible(false);
        treeOwner.refreshTree();
    }

    protected void createGUI() {
        setBackground(Color.lightGray);
        getContentPane().setLayout(new GridBagLayout());
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 100, 100, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        getContentPane().add(topPanel, gbc);
        handlerPanel = HandlerPanel.createHandlerPanel(handlerData);
        if (handlerPanel == null) {
            JOptionPane.showMessageDialog(getParent(), "Error: an appropriate handler was not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        gbc = new GridBagConstraints(0, 0, 1, 1, 100, 100, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        topPanel.add(handlerPanel, gbc);
        okButton = new JButton("OK");
        okButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        gbc = new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 10, 10), 0, 0);
        topPanel.add(buttonPanel, gbc);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new CloseClass());
        setTitle(handlerData.getHandlerName() + " Filter Specification");
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    class CloseClass extends WindowAdapter {

        public void windowClosing(WindowEvent e) {
            cleanup();
        }
    }
}
