package org.bs.mdi.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.bs.mdi.core.*;
import org.bs.mdi.gui.*;

public class ToolToggleButton extends JToggleButton implements ActionListener, MouseListener, CommandTrigger, GuiPreferenceChangeListener {

    String helpText = null;

    Command command = null;

    GuiItem guiItem = null;

    public ToolToggleButton(GuiItem item) {
        initGui(item);
        Application.getGuiPreferences().addPreferenceChangeListener(this);
        addMouseListener(this);
    }

    public ToolToggleButton(Command command) {
        this.command = command;
        initGui(command);
        Application.getGuiPreferences().addPreferenceChangeListener(this);
        addMouseListener(this);
        command.addTrigger(this);
        addActionListener(this);
        setEnabled(command.isEnabled());
    }

    protected void initGui(GuiItem item) {
        IconSet iconSet = null;
        int iconSize = 0;
        this.guiItem = item;
        switch(Application.getGuiPreferences().getToolbarDisplayMode()) {
            case GuiPreferences.TOOLBARMODE_ONLYICONS:
                iconSize = Application.getGuiPreferences().getToolbarIconSize();
                iconSet = Application.getResources().getScaledIconSet(item.getIconKey(), iconSize);
                setIcon(iconSet.getNormalIcon());
                setDisabledIcon(iconSet.getDisabledIcon());
                setDisabledSelectedIcon(iconSet.getDisabledIcon());
                setPressedIcon(iconSet.getSelectedIcon());
                setSelectedIcon(iconSet.getSelectedIcon());
                setRolloverIcon(iconSet.getSelectedIcon());
                setRolloverSelectedIcon(iconSet.getSelectedIcon());
                setRolloverEnabled(true);
                setText(null);
                setPreferredSize(new Dimension(iconSize + 6, iconSize + 6));
                setMinimumSize(getPreferredSize());
                setMaximumSize(getPreferredSize());
                setSize(getPreferredSize());
                setToolTipText(item.isIndirect() ? item.getLabel() + I18n.tr("general.threedots") : item.getLabel());
                if (item.getMnemonic() != 0) setMnemonic(item.getMnemonic());
                break;
            case GuiPreferences.TOOLBARMODE_ONLYTEXT:
                setIcon(null);
                setText(item.isIndirect() ? item.getLabel() + I18n.tr("general.threedots") : item.getLabel());
                setPreferredSize(null);
                setMinimumSize(null);
                setMaximumSize(null);
                setToolTipText(null);
                if (item.getMnemonic() != 0) setMnemonic(item.getMnemonic());
                break;
            case GuiPreferences.TOOLBARMODE_ICONSWITHTEXT:
                iconSize = Application.getGuiPreferences().getToolbarIconSize();
                iconSet = Application.getResources().getScaledIconSet(item.getIconKey(), iconSize);
                setIcon(iconSet.getNormalIcon());
                setDisabledIcon(iconSet.getDisabledIcon());
                setDisabledSelectedIcon(iconSet.getDisabledIcon());
                setPressedIcon(iconSet.getSelectedIcon());
                setSelectedIcon(iconSet.getSelectedIcon());
                setRolloverIcon(iconSet.getSelectedIcon());
                setRolloverSelectedIcon(iconSet.getSelectedIcon());
                setRolloverEnabled(true);
                setText(item.isIndirect() ? item.getLabel() + I18n.tr("general.threedots") : item.getLabel());
                if (item.getMnemonic() != 0) setMnemonic(item.getMnemonic());
                setVerticalTextPosition(SwingConstants.BOTTOM);
                setHorizontalTextPosition(SwingConstants.CENTER);
                setPreferredSize(null);
                setMinimumSize(null);
                setMaximumSize(null);
                setToolTipText(null);
                break;
        }
        helpText = item.getHelp();
        setActionCommand(item.getName());
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
        if (trigger != this) {
            AbstractButton button = (AbstractButton) trigger;
            if (button instanceof JToggleButton) setSelected(button.isSelected());
        }
    }

    public void commandUpdated() {
        initGui(command);
    }

    public void guiPreferencesChanged(GuiPreferences newPreferences) {
        initGui(guiItem);
    }

    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
            getParent().dispatchEvent(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
