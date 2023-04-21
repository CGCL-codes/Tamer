package org.vrforcad.installer;

import javax.swing.JPanel;

/**
 * Installer Interface. 
 * 
 * @version 1.0 
 * @author Daniel Cioi <dan.cioi@vrforcad.org>
 */
public interface InstallerInterface extends StepsControllInterface {

    int OS_WINDOWS = 0;

    int OS_LINUX = 1;

    int OS_MAC = 2;

    int OS_OTHER = 3;

    void addPanel(JPanel panel);

    void removePreviousPanels();

    String getInstallPath();

    void setInstallPath(String installPath);

    void installationFailed();
}
