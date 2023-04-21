package com.showdown.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.swt.widgets.Widget;
import com.showdown.api.impl.ShowDownManager;
import com.showdown.resource.Messages;
import com.showdown.settings.ISettingsItem;
import com.showdown.settings.ISettingsListener;
import com.showdown.settings.SettingsItemEnum;
import com.showdown.ui.ImageRegistry.ShowDownImage;

/**
 * System Tray support for ShowDown
 * 
 * @author Mat DeLong
 */
public class ShowDownTray implements ISettingsListener {

    private Shell shell;

    private TrayItem trayItem;

    private Menu trayMenu;

    /**
    * Constructor which specifies the {@link ShowDown} shell to show/hide
    * 
    * @param shell
    *           the shell the system tray item is for
    */
    public ShowDownTray(Shell shell) {
        this.shell = shell;
        addListeners();
        updateTrayState();
    }

    private void addListeners() {
        shell.addShellListener(new ShellAdapter() {

            public void shellClosed(ShellEvent e) {
                if (useTray()) {
                    e.doit = false;
                    shell.setVisible(false);
                }
            }
        });
        ShowDownManager.INSTANCE.getSettings().addSettingsListener(this);
    }

    private boolean useTray() {
        Boolean useTray = ShowDownManager.INSTANCE.getSettings().getBoolean(SettingsItemEnum.SYSTEM_TRAY);
        return useTray != null && useTray.booleanValue();
    }

    /**
    * Updates the state of the tray based on the current settings.
    */
    public void updateTrayState() {
        updateTrayState(useTray());
    }

    /**
    * Updates the state of the tray based on if the user wants to use it or not
    * @param useTray true to use the tray, false otherwise
    */
    public void updateTrayState(boolean useTray) {
        if (useTray) {
            addTrayItem();
        } else {
            removeTrayItem();
        }
    }

    private void addTrayItem() {
        if (!isDisposed()) {
            return;
        }
        Tray tray = shell.getDisplay().getSystemTray();
        if (tray == null) {
            return;
        }
        Image image = ImageRegistry.INSTANCE.getImage(ShowDownImage.IMG_SHOWDOWN);
        if (image == null) {
            return;
        }
        trayItem = new TrayItem(tray, SWT.NONE);
        trayItem.setToolTipText("ShowDown");
        trayMenu = new Menu(shell, SWT.POP_UP);
        MenuItem show = new MenuItem(trayMenu, SWT.PUSH);
        show.setText(Messages.Show);
        show.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                shell.setVisible(true);
                shell.forceActive();
            }
        });
        MenuItem hide = new MenuItem(trayMenu, SWT.PUSH);
        hide.setText(Messages.Hide);
        hide.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                shell.setVisible(false);
            }
        });
        new MenuItem(trayMenu, SWT.SEPARATOR);
        MenuItem exit = new MenuItem(trayMenu, SWT.PUSH);
        exit.setText(Messages.Exit);
        exit.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                shell.dispose();
            }
        });
        trayMenu.setDefaultItem(show);
        trayItem.addListener(SWT.MenuDetect, new Listener() {

            public void handleEvent(Event event) {
                trayMenu.setVisible(true);
                shell.forceActive();
            }
        });
        trayItem.addListener(SWT.DefaultSelection, new Listener() {

            public void handleEvent(Event event) {
                shell.setVisible(!shell.getVisible());
                if (shell.isVisible()) {
                    shell.forceActive();
                }
            }
        });
        trayItem.setImage(image);
    }

    private void removeTrayItem() {
        if (shell != null && !shell.isDisposed()) {
            shell.setVisible(true);
            shell.forceActive();
        }
        disposeWidget(trayMenu);
        disposeWidget(trayItem);
    }

    private boolean isDisposed() {
        return trayItem == null || trayMenu == null || trayItem.isDisposed() || trayMenu.isDisposed();
    }

    private void disposeWidget(Widget widget) {
        if (widget != null && !widget.isDisposed()) {
            widget.dispose();
        }
    }

    /**
    * {@inheritDoc}
    */
    public void settingChanged(ISettingsItem item) {
        if (item == SettingsItemEnum.SYSTEM_TRAY) {
            Display.getDefault().syncExec(new Runnable() {

                public void run() {
                    updateTrayState();
                }
            });
        }
    }
}
