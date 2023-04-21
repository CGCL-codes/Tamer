package fildiv.jremcntl.server.core.env;

import java.net.URL;
import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JMenuItem;
import fildiv.jremcntl.common.core.JRemRuntimeException;
import fildiv.jremcntl.server.core.ext.Environment;

public class HelpTool {

    private Environment env;

    private HelpSet hs;

    private HelpBroker hb;

    public HelpTool(Environment env) {
        hs = getHelpSet("jh/jremcntl.hs");
        hb = hs.createHelpBroker();
    }

    public void assign(JMenuItem menuItem, String topic) {
        CSH.setHelpIDString(menuItem, topic);
        menuItem.addActionListener(new CSH.DisplayHelpFromSource(hb));
    }

    public HelpSet getHelpSet(String helpsetfile) {
        HelpSet hs = null;
        ClassLoader cl = this.getClass().getClassLoader();
        try {
            URL hsURL = HelpSet.findHelpSet(cl, helpsetfile);
            hs = new HelpSet(null, hsURL);
        } catch (Exception e) {
            throw new JRemRuntimeException(e);
        }
        return hs;
    }
}
