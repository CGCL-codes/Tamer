package ghm.followgui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
@author <a href="mailto:greghmerrill@yahoo.com">Greg Merrill</a>
*/
class DisableableConfirm extends JDialog {

    DisableableConfirm(final Frame parent, final String title, final String message, final String confirmButtonText, final String doNotConfirmButtonText, final String disableText) {
        super(parent, title, true);
        final JPanel messagePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        final StringTokenizer stknzr = new StringTokenizer(message, "\n\r");
        gbc.gridy = 0;
        while (stknzr.hasMoreTokens()) {
            messagePanel.add(new JLabel(stknzr.nextToken()), gbc);
            gbc.gridy++;
        }
        final JPanel controlPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        final JPanel buttonPanel = new JPanel();
        final JButton confirmButton = new JButton(confirmButtonText);
        confirmButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                confirmed_ = true;
                DisableableConfirm.this.dispose();
            }
        });
        buttonPanel.add(confirmButton);
        final JButton doNotConfirmButton = new JButton(doNotConfirmButtonText);
        doNotConfirmButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                confirmed_ = false;
                DisableableConfirm.this.dispose();
            }
        });
        buttonPanel.add(doNotConfirmButton);
        controlPanel.add(buttonPanel, gbc);
        disabledCheckBox_ = new JCheckBox(disableText);
        gbc.gridy = 1;
        controlPanel.add(disabledCheckBox_, gbc);
        final JPanel contentPane = new JPanel(new BorderLayout(0, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));
        contentPane.add(messagePanel, BorderLayout.CENTER);
        contentPane.add(controlPanel, BorderLayout.SOUTH);
        this.setContentPane(contentPane);
        FollowApp.centerWindowInScreen(this);
    }

    private JCheckBox disabledCheckBox_;

    boolean confirmed_;

    boolean markedDisabled() {
        return disabledCheckBox_.isSelected();
    }

    boolean markedConfirmed() {
        return confirmed_;
    }
}
