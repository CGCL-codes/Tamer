package net.sourceforge.omov.app.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import net.sourceforge.jpotpourri.jpotface.IPtEscapeDisposeReceiver;
import net.sourceforge.jpotpourri.jpotface.PtEscapeDisposer;
import net.sourceforge.jpotpourri.jpotface.util.PtGuiUtil;
import net.sourceforge.omov.app.util.AppImageFactory;
import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.action.OpenBrowserAction;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class AboutDialog extends JDialog implements IPtEscapeDisposeReceiver {

    private static final Log LOG = LogFactory.getLog(AboutDialog.class);

    private static final long serialVersionUID = -6058616320816195022L;

    public AboutDialog(JFrame owner) {
        super(owner, "About", true);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(final WindowEvent event) {
                doClose();
            }
        });
        PtEscapeDisposer.enableEscapeOnDialogWithoutFocusableComponents(this, this);
        this.getContentPane().add(this.initComponents());
        this.pack();
        this.setResizable(false);
        PtGuiUtil.setCenterLocation(this);
    }

    private JPanel initComponents() {
        final JPanel panel = new JPanel();
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(panel, c);
        panel.setLayout(layout);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));
        final JLabel logo = new JLabel(AppImageFactory.getInstance().getAboutLogo(), SwingConstants.CENTER);
        try {
            final OpenBrowserAction openBrowser = new OpenBrowserAction(Constants.getWebUrl());
            logo.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    openBrowser.actionPerformed(null);
                }
            });
            PtGuiUtil.enableHandCursor(logo);
        } catch (MalformedURLException e) {
            LOG.error("OpenBrowserAction failed.", e);
        }
        final JLabel title = new JLabel("OurMovies", SwingConstants.CENTER);
        title.setFont(new Font("default", Font.BOLD, 14));
        final JLabel versionLabel = new JLabel("Version " + BeanFactory.getInstance().getCurrentApplicationVersion().getVersionString(), SwingConstants.CENTER);
        versionLabel.setFont(new Font("default", Font.PLAIN, 11));
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.insets = new Insets(0, 0, 2, 0);
        c.gridy = 0;
        panel.add(title, c);
        c.insets = new Insets(0, 0, 8, 0);
        c.gridy++;
        panel.add(versionLabel, c);
        c.insets = new Insets(0, 0, 10, 0);
        c.gridy++;
        panel.add(logo, c);
        c.insets = new Insets(0, 0, 0, 0);
        c.gridy++;
        panel.add(new JLabel("<html>http://omov.sourceforge.net &copy; GPL v2</html>"), c);
        return panel;
    }

    public static void main(String[] args) {
        new AboutDialog(null).setVisible(true);
    }

    private void doClose() {
        this.dispose();
    }

    public void doEscape() {
        this.doClose();
    }
}
