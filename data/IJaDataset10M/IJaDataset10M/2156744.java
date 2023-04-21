package org.bs.mdi.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.bs.mdi.core.*;
import org.bs.mdi.gui.*;

public class PushButton extends JButton implements ActionListener, CommandTrigger {

    String helpText = null;

    Command command = null;

    int fixedHeight = 0;

    public PushButton(GuiItem item) {
        initGui(item);
    }

    public PushButton(Command command) {
        this.command = command;
        initGui(command);
        command.addTrigger(this);
        addActionListener(this);
        setEnabled(command.isEnabled());
    }

    protected void initGui(GuiItem item) {
        setText(item.isIndirect() ? item.getLabel() + I18n.tr("general.threedots") : item.getLabel());
        if (item.getMnemonic() != 0) setMnemonic(item.getMnemonic());
        if (item.getIconKey() != null) {
            IconSet iconSet = Application.getResources().getScaledIconSet(item.getIconKey(), Application.getGuiPreferences().getButtonIconSize());
            setIcon(iconSet.getNormalIcon());
            setDisabledIcon(iconSet.getDisabledIcon());
            setDisabledSelectedIcon(iconSet.getDisabledIcon());
            setPressedIcon(iconSet.getSelectedIcon());
            setSelectedIcon(iconSet.getSelectedIcon());
            setRolloverIcon(iconSet.getSelectedIcon());
            setRolloverSelectedIcon(iconSet.getSelectedIcon());
            setRolloverEnabled(true);
        }
        setToolTipText(item.getDescription());
        helpText = item.getHelp();
        setActionCommand(item.getName());
        fixedHeight = Application.getGuiPreferences().getButtonIconSize() + 6;
    }

    public void actionPerformed(ActionEvent e) {
        if (command == null) return;
        execute();
    }

    public Command getCommand() {
        return command;
    }

    public void execute() {
        command.execute(this);
    }

    public void commandExecuted(CommandTrigger trigger) {
    }

    public void commandUpdated() {
        initGui(command);
    }

    public Dimension getMinimumSize() {
        Dimension d = super.getMinimumSize();
        d.height = fixedHeight;
        return d;
    }

    public Dimension getMaximumSize() {
        Dimension d = super.getMaximumSize();
        d.height = fixedHeight;
        return d;
    }

    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        d.height = fixedHeight;
        return d;
    }
}
