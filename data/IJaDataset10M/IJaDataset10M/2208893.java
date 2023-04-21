package com.uglygreencar.games.cribbage;

import java.applet.Applet;
import java.awt.BorderLayout;
import com.uglygreencar.games.cribbage.gui.GameGui;
import com.uglygreencar.games.cribbage.gui.title.TitleGui;

/**********************************************************************
* Main driver class for Applet invocation of Cribbage.
*
*
*	@author Nathaniel G. Auvil
**********************************************************************/
public final class Cribbage extends Applet {

    private GameGui gameGui;

    /**************************************************************
	 * init gets called first
	 *
	 *
	 *************************************************************/
    public void init() {
        super.setLayout(new BorderLayout());
        Globals.initialize(this);
        this.gameGui = new GameGui();
        super.add(this.gameGui, BorderLayout.CENTER);
    }
}
