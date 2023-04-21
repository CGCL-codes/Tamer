package com.uglygreencar.games.cribbage.gui.countHands;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import com.uglygreencar.games.cribbage.gui.GameGui;

public final class CountHandsGui extends JPanel {

    private CountHandsPanel countHandsPanel;

    private CountHandsBar countHandsBar;

    /*************************************************************
	 *
	 *************************************************************/
    public CountHandsGui(GameGui gameGui) {
        super.setLayout(new BorderLayout());
        this.countHandsBar = new CountHandsBar(gameGui);
        super.add(countHandsBar, BorderLayout.SOUTH);
        this.countHandsPanel = new CountHandsPanel();
        super.add(this.countHandsPanel, BorderLayout.CENTER);
    }

    public void countHands() {
        this.countHandsPanel.countHands();
    }
}
