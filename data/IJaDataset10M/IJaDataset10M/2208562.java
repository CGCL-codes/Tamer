package com.jrandrews.statediagram.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import com.jrandrews.statediagram.Main;

public class StateGray implements ActionListener {

    private static final Logger logger = Logger.getLogger(StateGray.class.getName());

    public void actionPerformed(ActionEvent e) {
        logger.fine("menu selection: " + e.getActionCommand());
        if ("gray".equals(e.getActionCommand())) {
            Main.instance().graySelected(true);
        } else if ("ungray".equals(e.getActionCommand())) {
            Main.instance().graySelected(false);
        }
    }
}
