package be.vds.jtbdive.client.view.core.about;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.Border;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.swing.component.ExceptionDialog;
import be.vds.jtbdive.client.util.ResourceManager;
import be.vds.jtbdive.client.view.components.LicenceDialog;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.jtbdive.core.util.FileUtilities;

public class TechnologiesPanel extends JPanel {

    protected static final Syslog LOGGER = Syslog.getLogger(TechnologiesPanel.class);

    public TechnologiesPanel() {
        init();
    }

    private void init() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(3, 5, 3, 5);
        int y = 0;
        for (Technologies t : Technologies.values()) {
            Component c = createIconComponent(t);
            GridBagLayoutManager.addComponent(this, c, gc, 0, y, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
            GridBagLayoutManager.addComponent(this, new JLabel(t.getName()), gc, 1, y, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
            c = createLicenceComponent(t);
            GridBagLayoutManager.addComponent(this, c, gc, 2, y, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.EAST);
            y++;
            GridBagLayoutManager.addComponent(this, new JSeparator(), gc, 0, y, 3, 1, 1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
            y++;
        }
        GridBagLayoutManager.addComponent(this, Box.createVerticalGlue(), gc, 1, y, 1, 1, 0, 1, GridBagConstraints.VERTICAL, GridBagConstraints.CENTER);
    }

    private Component createLicenceComponent(final Technologies t) {
        final String licencePath = t.getLicencePath();
        if (licencePath != null) {
            JButton infoBtn = new JButton(new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    URI uri;
                    try {
                        uri = ResourceManager.getInstance().getLicenceURL(licencePath).toURI();
                        String content = FileUtilities.readFileContent(uri);
                        LicenceDialog ld = new LicenceDialog(t.getName(), content);
                        ld.setLocationRelativeTo(null);
                        ld.setVisible(true);
                    } catch (URISyntaxException e1) {
                        LOGGER.error(e1);
                        ExceptionDialog.showDialog(e1, TechnologiesPanel.this);
                    } catch (IOException ex) {
                        LOGGER.error(ex);
                        ExceptionDialog.showDialog(ex, TechnologiesPanel.this);
                    }
                }
            });
            infoBtn.setIcon(UIAgent.getInstance().getIcon(UIAgent.ICON_INFO_16));
            return infoBtn;
        }
        return Box.createGlue();
    }

    private Component createIconComponent(Technologies t) {
        String iconName = t.getIconName();
        if (iconName != null) {
            return new JLabel(ResourceManager.getInstance().getImageIcon("logos/" + iconName));
        }
        return Box.createVerticalStrut(24);
    }
}

enum Technologies {

    JAVA(1, "Java", "java_24.png", null), DOCKING_FRAMES(2, "Docking frames", null, null), SWINGX(3, "SwingX", null, null), JASPER_REPORTS(4, "JasperReports", "japserreports_24.png", "jasperreport.txt"), JFREECHART(5, "JFreeChart", null, null), LOG4J(6, "Log4j", "log4j_24.png", null), RXTX(7, "rxtx", null, null), ITEXT(8, "iText", null, null);

    private int index;

    private String name, iconName, licencePath;

    public String getIconName() {
        return iconName;
    }

    public String getLicencePath() {
        return licencePath;
    }

    Technologies(int index, String name, String iconName, String licencePath) {
        this.index = index;
        this.name = name;
        this.iconName = iconName;
        this.licencePath = licencePath;
    }

    public String getName() {
        return name;
    }

    public static Technologies getTechnologyForIndex(int index) {
        for (Technologies t : values()) {
            if (t.getIndex() == index) return t;
        }
        return null;
    }

    private int getIndex() {
        return index;
    }
}
