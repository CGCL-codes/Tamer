package david.util;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class DonateButton extends JButton {

    private static final String paypal = "https://www.paypal.com/cgi-bin/webscr?cmd=_xclick&business=david%40depowell%2ecom&item_name=Lottery%20Checker%20Donation";

    public DonateButton() {
        setText("Make A Donation");
        setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        setVerticalAlignment(javax.swing.SwingConstants.TOP);
        setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                BrowserControl.displayURL(paypal);
            }
        });
    }

    public DonateButton(int width, int height) {
        this();
        setSize(width, height);
        setMargin(new Insets(0, 4, 0, 4));
    }
}
