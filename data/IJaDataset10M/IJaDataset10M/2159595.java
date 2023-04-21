package de.sicari.starter.gui.components;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import de.sicari.starter.util.Status;
import de.sicari.starter.util.StringIcon;
import de.sicari.starter.util.Utils;

/**
 * This is a convenience class for creating a panel that contains a logo on
 * the left side, a couple of user input panels on the right. Instances of this
 * class are not intended to be created manually. Instead,
 * {@link #enroll(String,URL,ConverterPanel[],JTabbedPane,ChangeListener)
 * enrolling} automatically creates and registers a <code>LogoPanel</code> with
 * a parent {@link JTabbedPane} and returns a {@link StringIcon} object which
 * might be used for further event processing.
 *
 * @author Matthias Pressfreund
 * @version "$Id: LogoPanel.java 335 2007-09-06 19:20:10Z jpeters $"
 */
public class LogoPanel extends JPanel implements ChangeListener {

    private static final long serialVersionUID = -1762223026088597744L;

    /**
     * References to the embedded components
     */
    protected ConverterPanel[] components_;

    /**
     * The associated icon
     */
    protected StringIcon icon_;

    /**
     * The parent tabbed panel
     */
    protected JTabbedPane parent_;

    /**
     * Hidden construction. Usage of
     * {@link #enroll(String,URL,ConverterPanel[],JTabbedPane,ChangeListener)}
     * is intended to be used for creating and registering objects.
     */
    protected LogoPanel() {
        super(new GridBagLayout());
    }

    /**
     * Create and register a <code>LogoPanel</code> at a parent tabbed panel.
     *
     * @param title The tab title
     * @param logopath The system resource path to the logo
     * @param components The components to be placed into this
     *   <code>LogoPanel</code>
     * @param parent The parent tabbed panel
     * @param startupListener The listener that checks if the <i>SicAri</i>
     *   server is ready for startup
     *
     * @return The icon associated with the created <code>LogoPanel</code>
     */
    public static StringIcon enroll(String title, URL logopath, ConverterPanel[] components, JTabbedPane parent, ChangeListener startupListener) {
        LogoPanel panel;
        panel = new LogoPanel();
        panel.init(title, logopath, components, parent, startupListener);
        return panel.icon_;
    }

    /**
     * This method will be called by
     * {@link #enroll(String,URL,ConverterPanel[],JTabbedPane,ChangeListener)}
     * after creating an instance.
     */
    protected void init(String title, URL logopath, ConverterPanel[] components, JTabbedPane parent, ChangeListener startupListener) {
        JPanel cp;
        int i;
        components_ = components;
        Utils.addConstrained(this, new JLabel(new ImageIcon(logopath)), 0, 0, 1, components.length, GridBagConstraints.NONE, GridBagConstraints.EAST, 1, 1, new Insets(15, 15, 15, 5));
        cp = new JPanel(new GridBagLayout());
        for (i = 0; i < components.length; i++) {
            Utils.addConstrained(cp, (Component) components[i], 0, i, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, 1, 1, new Insets(5, 5, 5, 10));
            components[i].addChangeListener(this);
        }
        Utils.addConstrained(this, cp, 1, 0, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST, 1, 1, new Insets(10, 0, 10, 0));
        parent_ = parent;
        icon_ = new StringIcon(title, parent_, findLowestStatus());
        icon_.addChangeListener(startupListener);
        parent_.addTab(null, icon_, this);
        parent_.setDisabledIconAt(parent_.indexOfComponent(this), icon_);
    }

    /**
     * Find the lowestStatus of all {@link #components_ components}.
     *
     * @return The lowest status of all contained components
     */
    protected Status findLowestStatus() {
        Status lowest;
        Status status;
        lowest = Status.OK;
        for (ConverterPanel cp : components_) {
            status = cp.getStatus();
            if (status.isLowerThan(lowest)) {
                lowest = status;
            }
            if (lowest.isEqualTo(Status.ERROR)) {
                break;
            }
        }
        return lowest;
    }

    public void stateChanged(ChangeEvent e) {
        boolean enabled;
        enabled = false;
        for (ConverterPanel cp : components_) {
            if (cp.isEnabled()) {
                enabled = true;
                break;
            }
        }
        parent_.setEnabledAt(parent_.indexOfComponent(this), enabled);
        icon_.setStatus(findLowestStatus());
    }
}
