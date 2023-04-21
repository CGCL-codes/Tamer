package client.view.table;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ButtonPanel extends JPanel {

    private static final long serialVersionUID = -1168046524344809198L;

    private JButton foldButton;

    private JButton checkButton;

    private JButton callButton;

    private JButton raiseButton;

    private JButton allInButton;

    private ActionListener listener;

    public ButtonPanel(ActionListener listener) {
        this.listener = listener;
        setUpButtons();
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setPreferredSize(new Dimension(getWidth(), 28));
        this.setOpaque(false);
    }

    private void setUpButtons() {
        foldButton = new JButton("Fold");
        checkButton = new JButton("Check");
        callButton = new JButton("Call");
        raiseButton = new JButton("Raise");
        allInButton = new JButton("All-In");
        foldButton.setOpaque(false);
        checkButton.setOpaque(false);
        callButton.setOpaque(false);
        raiseButton.setOpaque(false);
        allInButton.setOpaque(false);
        Dimension preferredSize = new Dimension(80, 18);
        foldButton.setPreferredSize(preferredSize);
        checkButton.setPreferredSize(preferredSize);
        callButton.setPreferredSize(preferredSize);
        raiseButton.setPreferredSize(preferredSize);
        allInButton.setPreferredSize(preferredSize);
        foldButton.addActionListener(listener);
        checkButton.addActionListener(listener);
        callButton.addActionListener(listener);
        raiseButton.addActionListener(listener);
        allInButton.addActionListener(listener);
        hideAll();
        this.add(foldButton);
        this.add(checkButton);
        this.add(callButton);
        this.add(raiseButton);
        this.add(allInButton);
    }

    public void hideAll() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                hideFoldButton();
                hideCheckButton();
                hideCallButton();
                hideRaiseButton();
                hideAllInButton();
            }
        });
    }

    public void enableAllInButton() {
        allInButton.setEnabled(true);
    }

    public void enableFoldButton() {
        foldButton.setEnabled(true);
    }

    public void enableCheckButton() {
        checkButton.setEnabled(true);
    }

    public void enableCallButton() {
        callButton.setEnabled(true);
    }

    public void enableRaiseButton() {
        raiseButton.setEnabled(true);
    }

    public void disableAllInButton() {
        allInButton.setEnabled(false);
    }

    public void disableFoldButton() {
        foldButton.setEnabled(false);
    }

    public void disableCheckButton() {
        checkButton.setEnabled(false);
    }

    public void disableCallButton() {
        callButton.setEnabled(false);
    }

    public void disableRaiseButton() {
        raiseButton.setEnabled(false);
    }

    public void showAllInButton() {
        allInButton.setVisible(true);
    }

    public void showFoldButton() {
        foldButton.setVisible(true);
    }

    public void showCheckButton() {
        checkButton.setVisible(true);
    }

    public void showCallButton() {
        callButton.setVisible(true);
    }

    public void showRaiseButton() {
        raiseButton.setVisible(true);
    }

    public void hideAllInButton() {
        allInButton.setVisible(false);
    }

    public void hideFoldButton() {
        foldButton.setVisible(false);
    }

    public void hideCheckButton() {
        checkButton.setVisible(false);
    }

    public void hideCallButton() {
        callButton.setVisible(false);
    }

    public void hideRaiseButton() {
        raiseButton.setVisible(false);
    }

    public void switchCallToCall() {
        callButton.setText("Call");
    }

    public void switchFoldToFold() {
        foldButton.setText("Fold");
    }

    public void switchRaiseToRaise() {
        raiseButton.setText("Raise");
    }

    public void switchCallToShow() {
        callButton.setText("Show");
    }

    public void switchFoldToMuck() {
        foldButton.setText("Muck");
    }

    public void switchRaiseToBet() {
        raiseButton.setText("Bet");
    }
}
