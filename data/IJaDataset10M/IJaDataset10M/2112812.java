package issrg.utils.gui.repository;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class RepositoryMultiBrowserGUI extends JTabbedPane {

    RepositoryMultiBrowser[] rbList;

    /**
     * Default repository constructor
     *
     */
    public RepositoryMultiBrowserGUI() {
    }

    /**
     * Custom repository constructor
     * 
     * @param rbList - a list of repository components to be added on the 
     * the tabs
     */
    public RepositoryMultiBrowserGUI(RepositoryMultiBrowser[] rbList) {
        this.rbList = rbList;
        addBrowserList(rbList);
    }

    public void setList(RepositoryMultiBrowser[] rbList) {
        this.rbList = rbList;
    }

    public RepositoryMultiBrowser[] getList() {
        return this.rbList;
    }

    public void addBrowser(RepositoryMultiBrowser browser) {
        addTab(browser.getBrowserName(), browser.getBrowserLogo(), (Component) browser);
    }

    public void addBrowser(JPanel panel, String name, ImageIcon logo) {
        addTab(name, logo, panel);
    }

    public void addBrowserList(RepositoryMultiBrowser[] rbList) {
        RepositoryMultiBrowser browser;
        for (int i = 0; i < rbList.length; i++) {
            browser = rbList[i];
            addTab(browser.getBrowserName(), browser.getBrowserLogo(), (Component) browser);
        }
    }
}
