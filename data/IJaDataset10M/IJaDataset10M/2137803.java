package jartest;

import java.awt.Dimension;
import javax.swing.JFrame;
import saheba.util.conf.GlobalConfigurationManager;
import saheba.util.properties.PropertyReader;

public class RunnableJFrame {

    public static void main(String[] args) {
        GlobalConfigurationManager.loadCustomConfigurationManager(new PropertyReader(RunnableJFrame.class, "/config", new String[] { "general.properties" }, new String[] { "coded.properties" }));
        JFrame frame = new JFrame(GlobalConfigurationManager.getProperty("saheba.test") + " coded: " + GlobalConfigurationManager.getProperty("saheba.coded.test"));
        frame.setPreferredSize(new Dimension(300, 100));
        frame.setSize(new Dimension(300, 100));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
