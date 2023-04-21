package com.c4j.workbench.dialog;

import static java.lang.Short.MAX_VALUE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.SwingConstants.RIGHT;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import com.c4j.composition.IConnection;
import com.c4j.composition.IPublicPortReference;
import com.c4j.workbench.IconSet;
import com.c4j.workbench.WorkbenchFrame;

@SuppressWarnings("serial")
public class CardinalityDialog extends javax.swing.JDialog {

    private static final int DIALOG_WIDTH = 350;

    private final JFrame frame;

    private final IConnection connection;

    private final IPublicPortReference portRef;

    private DialogHeadPanel headPanel;

    private DialogButtonPanel buttonPanel;

    private JPanel panel;

    private JSeparator separator1;

    private JLabel cardinalityLabel;

    private JTextField cardinalityTextField;

    /**
     * Method to display this JDialog.
     *
     * @param args
     *         The argument list.
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                final JFrame jFrame = new JFrame();
                final CardinalityDialog inst = new CardinalityDialog(jFrame, (IConnection) null);
                inst.setVisible(true);
            }
        });
    }

    public CardinalityDialog(final JFrame frame, final IConnection connection) {
        super(frame);
        this.frame = frame;
        this.connection = connection;
        this.portRef = null;
        setTitle("Connection cardinality");
        initGUI();
        if (connection != null) cardinalityTextField.setText(Integer.toString(connection.getCardinality()));
        headPanel.setTitel("Connection cardinality");
        headPanel.setDescription("Change cardinality of the connection.");
    }

    public CardinalityDialog(final JFrame frame, final IPublicPortReference portRef) {
        super(frame);
        this.frame = frame;
        this.connection = null;
        this.portRef = portRef;
        setTitle("Port reference cardinality");
        initGUI();
        cardinalityTextField.setText(Integer.toString(portRef.getCardinality()));
        headPanel.setTitel("Port reference cardinality");
        headPanel.setDescription("Change cardinality of the port reference.");
    }

    private void initPanel() {
        panel = new JPanel();
        final GroupLayout panelLayout = new GroupLayout(panel);
        panel.setLayout(panelLayout);
        cardinalityLabel = new JLabel("Cardinality:");
        cardinalityTextField = new JTextField();
        cardinalityTextField.setHorizontalAlignment(RIGHT);
        panelLayout.setHorizontalGroup(panelLayout.createSequentialGroup().addContainerGap().addGroup(panelLayout.createParallelGroup().addComponent(cardinalityLabel, PREFERRED_SIZE, PREFERRED_SIZE, PREFERRED_SIZE)).addGap(5).addGroup(panelLayout.createParallelGroup().addComponent(cardinalityTextField, 0, 100, MAX_VALUE)).addContainerGap());
        panelLayout.setVerticalGroup(panelLayout.createSequentialGroup().addContainerGap().addGroup(panelLayout.createParallelGroup(BASELINE).addComponent(cardinalityLabel, PREFERRED_SIZE, 24, PREFERRED_SIZE).addComponent(cardinalityTextField, PREFERRED_SIZE, 24, PREFERRED_SIZE)).addContainerGap());
    }

    private void initGUI() {
        try {
            final GroupLayout thisLayout = new GroupLayout(getContentPane());
            getContentPane().setLayout(thisLayout);
            headPanel = new DialogHeadPanel();
            try {
                headPanel.setDialogIcon(IconSet.C4J_LOGO_SMALL.getIcon());
            } catch (final Throwable e) {
                System.err.println("Could not find small logo.");
            }
            initPanel();
            separator1 = new JSeparator();
            buttonPanel = new DialogButtonPanel();
            thisLayout.setVerticalGroup(thisLayout.createSequentialGroup().addComponent(headPanel, PREFERRED_SIZE, PREFERRED_SIZE, PREFERRED_SIZE).addComponent(panel, PREFERRED_SIZE, PREFERRED_SIZE, MAX_VALUE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(separator1, PREFERRED_SIZE, PREFERRED_SIZE, PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(buttonPanel, PREFERRED_SIZE, PREFERRED_SIZE, PREFERRED_SIZE).addContainerGap());
            thisLayout.setHorizontalGroup(thisLayout.createParallelGroup().addComponent(headPanel, GroupLayout.Alignment.LEADING, 0, DIALOG_WIDTH, MAX_VALUE).addComponent(panel, GroupLayout.Alignment.LEADING, 0, DIALOG_WIDTH, MAX_VALUE).addComponent(separator1, GroupLayout.Alignment.LEADING, 0, DIALOG_WIDTH, MAX_VALUE).addComponent(buttonPanel, GroupLayout.Alignment.LEADING, 0, DIALOG_WIDTH, MAX_VALUE));
            pack();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        buttonPanel.setOKAction(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                if (connection != null) {
                    try {
                        connection.setCardinality(Integer.valueOf(cardinalityTextField.getText()));
                    } catch (final NumberFormatException e) {
                        WorkbenchFrame.showError(frame, e);
                    }
                } else if (portRef != null) {
                    try {
                        portRef.setCardinality(Integer.valueOf(cardinalityTextField.getText()));
                    } catch (final NumberFormatException e) {
                        WorkbenchFrame.showError(frame, e);
                    }
                }
                CardinalityDialog.this.dispose();
            }
        });
        buttonPanel.setCancelAction(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                CardinalityDialog.this.dispose();
            }
        });
    }
}
