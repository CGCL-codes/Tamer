package org.jtomtom.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import org.apache.log4j.Logger;
import org.jtomtom.Application;
import org.jtomtom.gui.utilities.JTTabPanel;
import org.jtomtom.gui.utilities.SpringUtilities;

/**
 * @author Frédéric Combes
 *
 * About tab
 */
public class TabAbout extends JTTabPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(TabAbout.class);

    public static final String LICENSE_URL = "http://www.gnu.org/licenses/gpl.html";

    public static final String WEBSITE_URL = "http://jtomtom.sourceforge.net/";

    public static final String DEVELOPER_URL = "belz12@yahoo.fr";

    private JButton openWebsite;

    private JButton sendMeaMail;

    private JButton viewLicense;

    /**
	 * Display initialisation
	 */
    public TabAbout() {
        super(getTabTranslations().getString("org.jtomtom.tab.about.title"));
        setPanelLeftImage(getClass().getResource("resources/apropos.png"));
    }

    /**
	 * Building UI
	 */
    public JPanel build() {
        if (isBuild()) return this;
        super.build();
        LOGGER.debug("Building TabAbout ...");
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(buildInformationsPanel());
        add(buildTranslatorsPanel());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        viewLicense = new JButton(getTabTranslations().getString("org.jtomtom.tab.about.license") + " GPLv3");
        viewLicense.addActionListener(this);
        buttonPanel.add(viewLicense);
        add(buttonPanel);
        return this;
    }

    private JComponent buildInformationsPanel() {
        JPanel informationsPanel = new JPanel(new SpringLayout());
        informationsPanel.add(new JLabel("<html><strong>" + getTabTranslations().getString("org.jtomtom.tab.about.version") + "</strong></html>"));
        informationsPanel.add(new JLabel(Application.getInstance().getVersionNumber()));
        informationsPanel.add(new JLabel("<html><strong>" + getTabTranslations().getString("org.jtomtom.tab.about.date") + "</strong></html>"));
        informationsPanel.add(new JLabel(Application.getInstance().getVersionDate()));
        informationsPanel.add(new JLabel("<html><strong>" + getTabTranslations().getString("org.jtomtom.tab.about.developer") + "</strong></html>"));
        sendMeaMail = new JButton("<html>Frédéric Combes @ <a href=\"mailto:" + DEVELOPER_URL + "\">" + DEVELOPER_URL + "</a></html>");
        sendMeaMail.setHorizontalAlignment(JButton.LEFT);
        sendMeaMail.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        sendMeaMail.addActionListener(this);
        informationsPanel.add(sendMeaMail);
        informationsPanel.add(new JLabel("<html><strong>" + getTabTranslations().getString("org.jtomtom.tab.about.website") + "</strong></html>"));
        openWebsite = new JButton("<html><a href=\"" + WEBSITE_URL + "\">" + WEBSITE_URL + "</a></html>");
        openWebsite.setHorizontalAlignment(JButton.LEFT);
        openWebsite.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        openWebsite.addActionListener(this);
        informationsPanel.add(openWebsite);
        SpringUtilities.makeCompactGrid(informationsPanel, 4, 2, 2, 2, 2, 2);
        return informationsPanel;
    }

    private JComponent buildTranslatorsPanel() {
        JPanel translatorsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        StringBuffer infos = new StringBuffer();
        infos.append("<html><table>");
        infos.append("<tr><td>Català (ca_ES) :</td><td><strong>Oriol Gonzalez Llobet</strong></td></tr>");
        infos.append("<tr><td>Dutch (nl_NL) :</td><td><strong>Charly Preis</strong></td></tr>");
        infos.append("<tr><td>Español (es_ES) :</td><td><strong>Francisco Luque Contreras</strong></td></tr>");
        infos.append("<tr><td>German (de_DE) :</td><td><strong>Olivier Brügger</strong></td></tr>");
        infos.append("</table>");
        infos.append("</html>");
        JLabel label = new JLabel(infos.toString());
        translatorsPanel.add(label);
        JScrollPane scroll = new JScrollPane(translatorsPanel);
        scroll.setBorder(BorderFactory.createTitledBorder(getTabTranslations().getString("org.jtomtom.tab.about.translation")));
        return scroll;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            if (event.getSource() == viewLicense) {
                java.awt.Desktop.getDesktop().browse(java.net.URI.create(LICENSE_URL));
            } else if (event.getSource() == openWebsite) {
                java.awt.Desktop.getDesktop().browse(java.net.URI.create(WEBSITE_URL));
            } else if (event.getSource() == sendMeaMail) {
                java.awt.Desktop.getDesktop().browse(java.net.URI.create("mailto:" + DEVELOPER_URL));
            }
        } catch (IOException e) {
            LOGGER.error(e.getLocalizedMessage());
            if (LOGGER.isDebugEnabled()) LOGGER.debug(e);
        }
    }
}
