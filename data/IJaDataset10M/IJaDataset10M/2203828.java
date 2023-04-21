package org.tide.gui.action;

import org.tide.gui.*;
import org.tidelaget.gui.*;
import org.tidelaget.gui.action.*;
import org.tidelaget.gui.panel.*;
import org.tidelaget.gui.tree.*;
import java.awt.event.*;
import javax.swing.*;

public class StopTomcatProcessAction extends TAction {

    protected static ImageIcon m_icon = new ImageIcon(ClassLoader.getSystemResource("icons/icon_stopprocess.gif"));

    public StopTomcatProcessAction(String actionMapping) {
        super(actionMapping, "Stop tomcat", m_icon);
        setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
        TIDEController.get().getTomcatProcessPanel().stop();
        TIDEActionMapper.getAction(TIDEActionMapper.ACTION_TIDE_STARTTOMCATPROCESS).setEnabled(true);
        TIDEActionMapper.getAction(TIDEActionMapper.ACTION_TIDE_STOPTOMCATPROCESS).setEnabled(false);
    }
}
