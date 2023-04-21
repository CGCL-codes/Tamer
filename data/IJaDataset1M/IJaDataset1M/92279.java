package icm.unicore.iademo.plugins.iademo;

import javax.swing.*;
import com.pallas.unicore.resourcemanager.ResourceManager;
import com.pallas.unicore.client.util.ExtensionPlugable;

public class IADemoPlugin extends ExtensionPlugable {

    public static final String PLUGIN_VERSION = "v1.10";

    /** Creates a new instance of IADemoPlugin */
    public IADemoPlugin() {
    }

    public String getPluginInfo() {
        return "IADemo Plugin " + PLUGIN_VERSION + "\nAuthor: Michal Wronski <wrona@mat.uni.torun.pl>\n" + " and Krzysztof Benedyczak <golbi@mat.uni.torun.pl>\n" + "(C) 2004 ICM Warsaw University";
    }

    public void startPlugin() {
    }

    public void stopPlugin() {
    }

    public JMenuItem getCustomMenu() {
        JMenuItem menuItem = new JMenuItem("IADemo Plugin");
        menuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new SelectVSiteDialog(ResourceManager.getCurrentInstance(), false).show();
            }
        });
        return menuItem;
    }
}
