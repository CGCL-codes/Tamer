package com.pbxworkbench.campaign.ui.netbeans.welcome;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import org.openide.ErrorManager;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import com.pbxworkbench.campaign.ui.actions.NewCampaignAction;

/**
 * Top component which displays something.
 */
final class Welcome2TopComponent extends TopComponent {

    private static final long serialVersionUID = 1L;

    private static Welcome2TopComponent instance;

    private static final String PREFERRED_ID = "Welcome2TopComponent";

    private Welcome2TopComponent() {
        initComponents();
        setName(NbBundle.getMessage(Welcome2TopComponent.class, "CTL_Welcome2TopComponent"));
        setToolTipText(NbBundle.getMessage(Welcome2TopComponent.class, "HINT_Welcome2TopComponent"));
        setBackground(Color.white);
        setOpaque(true);
        try {
            URL htmlFile = Welcome2TopComponent.class.getResource("/com/pbxworkbench/campaign/ui/netbeans/javahelp/intro.html");
            JEditorPane htmlPane = new JEditorPane(htmlFile);
            htmlPane.setEditable(false);
            add(htmlPane, BorderLayout.NORTH);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton campBtn = new JButton("Create Campaign");
        campBtn.addActionListener(new NewCampaignAction());
        btnPanel.add(campBtn);
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setOpaque(true);
        add(btnPanel, BorderLayout.CENTER);
    }

    private void initComponents() {
        setLayout(new BorderLayout());
    }

    /**
	 * Gets default instance. Do not use directly: reserved for *.settings files
	 * only, i.e. deserialization routines; otherwise you could get a
	 * non-deserialized instance. To obtain the singleton instance, use
	 * {@link findInstance}.
	 */
    public static synchronized Welcome2TopComponent getDefault() {
        if (instance == null) {
            instance = new Welcome2TopComponent();
        }
        return instance;
    }

    /**
	 * Obtain the Welcome2TopComponent instance. Never call {@link #getDefault}
	 * directly!
	 */
    public static synchronized Welcome2TopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            ErrorManager.getDefault().log(ErrorManager.WARNING, "Cannot find Welcome2 component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof Welcome2TopComponent) {
            return (Welcome2TopComponent) win;
        }
        ErrorManager.getDefault().log(ErrorManager.WARNING, "There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    public void componentOpened() {
    }

    public void componentClosed() {
    }

    /** replaces this in object stream */
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    protected String preferredID() {
        return PREFERRED_ID;
    }

    static final class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return Welcome2TopComponent.getDefault();
        }
    }
}
