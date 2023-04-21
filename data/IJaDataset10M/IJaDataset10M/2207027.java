package org.aiotrade.platform.core;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/** 
 *
 * @author Caoyuan Deng
 */
public class StatusBar extends JPanel {

    private StatusPane marketPane = new StatusPane("SH");

    public StatusBar() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 3));
        setBackground(Color.lightGray);
        setBorder(BorderFactory.createLineBorder(Color.darkGray));
        add(marketPane);
    }

    public void setMarketPane(String market) {
    }

    class StatusPane extends JLabel {

        private Font paneFont = new Font("Serif", Font.PLAIN, 10);

        public StatusPane(String text) {
            setBackground(Color.lightGray);
            setFont(paneFont);
            setHorizontalAlignment(CENTER);
            setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            setText(text);
        }
    }
}
