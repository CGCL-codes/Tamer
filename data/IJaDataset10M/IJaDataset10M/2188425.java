package com.sparkit.extracta.builder.ui;

import java.awt.event.*;
import javax.swing.*;
import com.sparkit.extracta.builder.*;
import com.sparkit.extracta.builder.tools.*;

/**
 * The BuilderAction class is a base for the Command pattern
 * used in VisualBuilder. Every BuilderAction is responsible
 * for triggering a specific action inside a program. Action
 * contains a command (ICommand interface) which knows how to
 * execute itself without some special context. Command is executed
 * in "error-safe" way (every Throwable a command could generate
 * is handled by the application).
 * When an assembled action is needed (a find-and-refresh i.e.) the
 * command's execute method should call a getCommand().execute() method
 * on the RefreshAction not an actionPerformed method on a RefreshAction
 * (so the exceptions are handled correctly).
 *
 * @version 1.0
 * @author Bostjan Vester
 * @author Dejan Pazin
 * @author Dominik Roblek
 */
public class BuilderAction extends AbstractAction {

    ICommand m_command;

    /**
   * Creates a new action.
   *
   * @param sName A name of this action (as it should appear on a button i.e.)
   * @param icon An icon for this action.
   * @param command A command to be executed when action is triggered.
   */
    public BuilderAction(String sName, Icon icon, ICommand command) {
        super(sName, icon);
        m_command = command;
    }

    /**
   * This is called by the registered component. It executes the specified command.
   */
    public void actionPerformed(ActionEvent e) {
        try {
            m_command.execute();
        } catch (Throwable t) {
            UITools.handleThrowable(t);
        }
    }

    /**
   * Returns an assigned command - for creating assembled actions.
   */
    public ICommand getCommand() {
        return m_command;
    }
}
