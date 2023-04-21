package org.tranche.gui.add;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import org.tranche.gui.DisplayTextArea;
import org.tranche.gui.GenericRoundedButton;
import org.tranche.gui.Styles;

/**
 *
 * @author James "Augie" Hill - augman85@gmail.com
 */
public class AddFileToolReportPanel extends JPanel {

    private DisplayTextArea trancheHash = new DisplayTextArea();

    private GenericRoundedButton emailReceiptButton = new GenericRoundedButton("Email Receipt"), saveReceiptButton = new GenericRoundedButton("Save Receipt");

    public AddFileToolReportPanel(final UploadSummary us) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 10, 0, 10);
        gbc.weightx = 1;
        gbc.weighty = 0;
        add(new DisplayTextArea("Your upload is complete. A receipt for your upload has been emailed to you. The following is the Tranche hash that represents your upload:"), gbc);
        gbc.insets = new Insets(10, 10, 10, 10);
        trancheHash.setFont(Styles.FONT_12PT_BOLD);
        trancheHash.setText(us.getReport().getHash().toString());
        add(trancheHash, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 5, 3, 5);
        add(saveReceiptButton, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(emailReceiptButton, gbc);
        saveReceiptButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                us.saveReceipt(AddFileToolReportPanel.this);
            }
        });
        emailReceiptButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Thread t = new Thread() {

                    @Override()
                    public void run() {
                        us.showEmailFrame(AddFileToolReportPanel.this);
                    }
                };
                t.setDaemon(true);
                t.start();
            }
        });
    }
}
