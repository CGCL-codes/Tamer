package net.sf.xpontus.plugins.indentation.xml;

import net.sf.xpontus.configuration.XPontusConfig;
import net.sf.xpontus.modules.gui.components.preferences.IPreferencesPanel;
import net.sf.xpontus.plugins.preferences.PreferencesPluginIF;
import net.sf.xpontus.utils.PropertiesConfigurationLoader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class XMLIndentationPreferences implements PreferencesPluginIF {

    private IPreferencesPanel panel;

    private String preserveSpaceOptionP;

    private String omitCommentsOptionP;

    private String omitDoctypeOptionP;

    private String omitXmlDeclarationP;

    private JCheckBox omitCommentsOption;

    private JCheckBox omitDocTypeOption;

    private JCheckBox omitXMLDeclarationOption;

    private JCheckBox preserveSpaceOption;

    public XMLIndentationPreferences() {
        panel = new XMLIndentationPreferencesPanel();
    }

    public String getPluginCategory() {
        return "Indentation";
    }

    public IPreferencesPanel getPreferencesPanelComponent() {
        return panel;
    }

    public void saveSettings() {
        Properties props = new Properties();
        props.put(XMLIndentationPreferencesConstantsIF.class.getName() + "$" + XMLIndentationPreferencesConstantsIF.OMIT_COMMENTS_OPTION, omitCommentsOptionP);
        props.put(XMLIndentationPreferencesConstantsIF.class.getName() + "$" + XMLIndentationPreferencesConstantsIF.OMIT_DOCTYPE_OPTION, omitDoctypeOptionP);
        props.put(XMLIndentationPreferencesConstantsIF.class.getName() + "$" + XMLIndentationPreferencesConstantsIF.OMIT_XML_DECLARATION_OPTION, omitXmlDeclarationP);
        props.put(XMLIndentationPreferencesConstantsIF.class.getName() + "$" + XMLIndentationPreferencesConstantsIF.PRESERVE_SPACE_OPTION, preserveSpaceOptionP);
        Iterator it = props.keySet().iterator();
        while (it.hasNext()) {
            System.out.println("XP:" + it.next());
        }
        PropertiesConfigurationLoader.save(XMLIndentationPlugin.configfile, props);
    }

    public void loadSettings() {
        System.out.println("Loading XML settings");
        omitCommentsOptionP = (String) XPontusConfig.getValue(XMLIndentationPreferencesConstantsIF.class.getName() + "$" + XMLIndentationPreferencesConstantsIF.OMIT_COMMENTS_OPTION);
        System.out.println("option:" + omitCommentsOptionP);
        omitDoctypeOptionP = (String) XPontusConfig.getValue(XMLIndentationPreferencesConstantsIF.class.getName() + "$" + XMLIndentationPreferencesConstantsIF.OMIT_DOCTYPE_OPTION);
        omitXmlDeclarationP = (String) XPontusConfig.getValue(XMLIndentationPreferencesConstantsIF.class.getName() + "$" + XMLIndentationPreferencesConstantsIF.OMIT_XML_DECLARATION_OPTION);
        preserveSpaceOptionP = (String) XPontusConfig.getValue(XMLIndentationPreferencesConstantsIF.class.getName() + "$" + XMLIndentationPreferencesConstantsIF.PRESERVE_SPACE_OPTION);
        omitCommentsOption.setSelected(Boolean.valueOf(omitCommentsOptionP).booleanValue());
        omitDocTypeOption.setSelected(Boolean.valueOf(omitDoctypeOptionP).booleanValue());
        omitXMLDeclarationOption.setSelected(Boolean.valueOf(omitXmlDeclarationP).booleanValue());
        preserveSpaceOption.setSelected(Boolean.valueOf(preserveSpaceOptionP).booleanValue());
    }

    public class XMLIndentationPreferencesPanel extends JComponent implements IPreferencesPanel {

        public XMLIndentationPreferencesPanel() {
            setLayout(new BorderLayout());
            JLabel top = new JLabel(getTitle());
            top.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            top.setFont(top.getFont().deriveFont(Font.BOLD));
            top.setOpaque(true);
            top.setBackground(Color.white);
            add("North", top);
            JPanel p = new JPanel(new GridLayout(4, 1, 4, 4));
            omitCommentsOption = new JCheckBox("Omit comments", true);
            omitDocTypeOption = new JCheckBox("Omit doctype", true);
            omitXMLDeclarationOption = new JCheckBox("Omit XML declaration", true);
            preserveSpaceOption = new JCheckBox("Preserve space", true);
            p.add(omitCommentsOption);
            p.add(omitDocTypeOption);
            p.add(omitXMLDeclarationOption);
            p.add(preserveSpaceOption);
            System.out.println("xml prop:" + omitCommentsOptionP + "," + Boolean.valueOf(omitCommentsOptionP));
            omitCommentsOption.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    omitCommentsOptionP = Boolean.valueOf(omitCommentsOption.isSelected()).toString();
                }
            });
            omitDocTypeOption.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    omitDoctypeOptionP = Boolean.valueOf(omitDocTypeOption.isSelected()).toString();
                }
            });
            omitXMLDeclarationOption.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    omitXmlDeclarationP = Boolean.valueOf(omitXMLDeclarationOption.isSelected()).toString();
                }
            });
            preserveSpaceOption.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    preserveSpaceOptionP = Boolean.valueOf(preserveSpaceOption.isSelected()).toString();
                }
            });
            add(p, BorderLayout.CENTER);
        }

        public String getTitle() {
            return "XML indentation";
        }

        public Component getJComponent() {
            return this;
        }

        public String getId() {
            return getClass().getName();
        }
    }
}
