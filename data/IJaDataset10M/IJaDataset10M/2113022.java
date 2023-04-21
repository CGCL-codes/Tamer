package com.netime.commons.standard.gui.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import org.apache.commons.lang.reflect.ConstructorUtils;
import com.netime.commons.standard.gui.AutofficeInternalFrame;
import com.netime.commons.standard.gui.AutofficeMenuBar;
import com.netime.commons.standard.gui.AutofficeSuiteDesktopPane;
import com.netime.commons.standard.gui.Navigator;
import com.netime.commons.standard.gui.AutofficeSuiteTaskBar.IconOnBar;
import com.netime.commons.standard.log.LogManager;
import com.netime.commons.standard.log.LogMessage;
import com.netime.commons.standard.config.ConfigManager;
import com.netime.commons.standard.exceptions.AOSystemException;

public class ApplicationLauchListener implements ActionListener {

    static final String CLASS_NAME = ApplicationLauchListener.class.getName();

    AutofficeSuiteDesktopPane desktop = null;

    AutofficeMenuBar menuBar = null;

    Locale locale = null;

    public ApplicationLauchListener(AutofficeSuiteDesktopPane desktop, AutofficeMenuBar menuBar, Locale locale) {
        this.desktop = desktop;
        this.menuBar = menuBar;
        this.locale = locale;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final String METHOD_NAME = "actionPerformed(ActionEvent e)";
        String activedAppName = null;
        if (e.getSource() instanceof IconOnBar) activedAppName = ((IconOnBar) e.getSource()).getLabel(); else activedAppName = (String) e.getSource();
        LogManager.log(new LogMessage(LogManager.INFO, CLASS_NAME, METHOD_NAME, "Application lauching..." + activedAppName));
        try {
            lauchApplication(activedAppName);
        } catch (AOSystemException e1) {
            e1.printStackTrace();
        }
    }

    private void lauchApplication(String appName) throws AOSystemException {
        Object[] args = new Object[2];
        args[0] = menuBar;
        args[1] = locale;
        try {
            AutofficeInternalFrame app = (AutofficeInternalFrame) ConstructorUtils.invokeConstructor(Navigator.getApplicationByName(appName), args);
            desktop.add(app);
            app.setSize(ConfigManager.getIntProperty("display.size.frame.width.preferred"), ConfigManager.getIntProperty("display.size.frame.height.preferred"));
            app.setPreferredSize(new java.awt.Dimension(ConfigManager.getIntProperty("display.size.frame.width.preferred"), ConfigManager.getIntProperty("display.size.frame.height.preferred")));
            app.setSelected(true);
            app.setVisible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
