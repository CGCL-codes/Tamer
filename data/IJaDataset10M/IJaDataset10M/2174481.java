package org.cgsuite.help;

import java.awt.BorderLayout;
import java.util.Enumeration;
import javax.help.HelpSet;
import javax.help.JHelp;
import javax.help.JHelpIndexNavigator;
import javax.help.JHelpNavigator;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Lookup;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//org.cgsuite.help//CgsuiteHelp//EN", autostore = false)
@TopComponent.Description(preferredID = "CgsuiteHelpTopComponent", persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "help", openAtStartup = false)
@ActionID(category = "Window", id = "org.cgsuite.help.CgsuiteHelpTopComponent")
@ActionReference(path = "Menu/Window", position = 1750)
@TopComponent.OpenActionRegistration(displayName = "#CTL_CgsuiteHelpAction", preferredID = "CgsuiteHelpTopComponent")
public final class CgsuiteHelpTopComponent extends TopComponent {

    private JHelp helpViewer;

    public CgsuiteHelpTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(CgsuiteHelpTopComponent.class, "CTL_CgsuiteHelpTopComponent"));
        setToolTipText(NbBundle.getMessage(CgsuiteHelpTopComponent.class, "HINT_CgsuiteHelpTopComponent"));
        HelpSet help = Lookup.getDefault().lookup(HelpSet.class);
        helpViewer = new JHelp(help);
        Enumeration e = helpViewer.getHelpNavigators();
        while (e.hasMoreElements()) {
            JHelpNavigator navigator = (JHelpNavigator) e.nextElement();
            if (navigator instanceof JHelpIndexNavigator) {
                helpViewer.removeHelpNavigator(navigator);
                break;
            }
        }
        add(helpViewer, BorderLayout.CENTER);
    }

    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    public JHelp getHelpViewer() {
        return helpViewer;
    }

    void writeProperties(java.util.Properties p) {
        p.setProperty("version", "1.0");
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
    }
}
