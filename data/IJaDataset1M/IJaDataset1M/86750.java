package com.umc.gui.content.config;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import com.umc.ConfigController;
import com.umc.collector.Publisher;
import com.umc.gui.GuiController;
import com.umc.helper.UMCLanguage;
import com.umc.plugins.moviedb.IPluginMovieDB;
import de.umcProject.xmlbeans.UmcConfigDocument.UmcConfig.Languages.Language;
import de.umcProject.xmlbeans.UmcConfigDocument.UmcConfig.Languages.Language.Plugin;

/**
	 *
	 * @author Administrator
	 */
public class OnlineInfoPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = -9222108198618300100L;

    public static final String ID = "ONLINEINFO";

    private JButton btnAddPlugin1;

    private JButton btnAddPlugin2;

    private JButton btnAddPlugin3;

    private JButton btnAddPlugin4;

    private JButton btnDown4;

    private JButton btnDown1;

    private JButton btnDown2;

    private JButton btnDown3;

    private JButton btnLeft1;

    private JButton btnLeft2;

    private JButton btnLeft3;

    private JButton btnLeft4;

    private JButton btnRemPlugin1;

    private JButton btnRemPlugin2;

    private JButton btnRemPlugin3;

    private JButton btnRemPlugin4;

    private JButton btnRight1;

    private JButton btnRight2;

    private JButton btnRight3;

    private JButton btnRight4;

    private JButton btnUp1;

    private JButton btnUp2;

    private JButton btnUp3;

    private JButton btnUp4;

    private JComboBox comboGenre1;

    private JComboBox comboGenre2;

    private JComboBox comboGenre3;

    private JComboBox comboGenre4;

    private JComboBox comboLanguage1;

    private JComboBox comboLanguage2;

    private JComboBox comboLanguage3;

    private JComboBox comboLanguage4;

    private JComboBox comboPlot1;

    private JComboBox comboPlot2;

    private JComboBox comboPlot3;

    private JComboBox comboPlot4;

    private JComboBox comboRating1;

    private JComboBox comboRating2;

    private JComboBox comboRating3;

    private JComboBox comboRating4;

    private JComboBox comboTitle1;

    private JComboBox comboTitle2;

    private JComboBox comboTitle3;

    private JComboBox comboTitle4;

    private JScrollPane jScrollPane1;

    private JScrollPane jScrollPane2;

    private JScrollPane jScrollPane3;

    private JScrollPane jScrollPane4;

    private JSeparator jSeparator1;

    private JSeparator jSeparator2;

    private JSeparator jSeparator3;

    private JSeparator jSeparator4;

    private JLabel labelGenre1;

    private JLabel labelGenre2;

    private JLabel labelGenre3;

    private JLabel labelGenre4;

    private JLabel labelInfo1;

    private JLabel labelInfo2;

    private JLabel labelInfo3;

    private JLabel labelInfo4;

    private JLabel labelLanguage1;

    private JLabel labelLanguage2;

    private JLabel labelLanguage3;

    private JLabel labelLanguage4;

    private JLabel labelListPlugins1;

    private JLabel labelListPlugins2;

    private JLabel labelListPlugins3;

    private JLabel labelListPlugins4;

    private JLabel labelPlot1;

    private JLabel labelPlot2;

    private JLabel labelPlot3;

    private JLabel labelPlot4;

    private JLabel labelRating1;

    private JLabel labelRating2;

    private JLabel labelRating3;

    private JLabel labelRating4;

    private JLabel labelTitle1;

    private JLabel labelTitle2;

    private JLabel labelTitle3;

    private JLabel labelTitle4;

    private JList listPlugins4;

    private JList listPlugins1;

    private JList listPlugins2;

    private JList listPlugins3;

    private JPanel panel1;

    private JPanel panel2;

    private JPanel panel3;

    private JPanel panel4;

    private List<String> tags;

    private DefaultComboBoxModel modelLanguage1;

    private DefaultComboBoxModel modelLanguage2;

    private DefaultComboBoxModel modelLanguage3;

    private DefaultComboBoxModel modelLanguage4;

    private DefaultListModel modelPlugin1;

    private DefaultListModel modelPlugin2;

    private DefaultListModel modelPlugin3;

    private DefaultListModel modelPlugin4;

    private DefaultComboBoxModel modelTitle1;

    private DefaultComboBoxModel modelTitle2;

    private DefaultComboBoxModel modelTitle3;

    private DefaultComboBoxModel modelTitle4;

    private DefaultComboBoxModel modelPlot1;

    private DefaultComboBoxModel modelPlot2;

    private DefaultComboBoxModel modelPlot3;

    private DefaultComboBoxModel modelPlot4;

    private DefaultComboBoxModel modelGenre1;

    private DefaultComboBoxModel modelGenre2;

    private DefaultComboBoxModel modelGenre3;

    private DefaultComboBoxModel modelGenre4;

    private DefaultComboBoxModel modelRating1;

    private DefaultComboBoxModel modelRating2;

    private DefaultComboBoxModel modelRating3;

    private DefaultComboBoxModel modelRating4;

    private String selectedLanguage1 = "";

    private String selectedLanguage2 = "";

    private String selectedLanguage3 = "";

    private String selectedLanguage4 = "";

    private de.umcProject.xmlbeans.UmcConfigDocument.UmcConfig.Languages languages = null;

    private Language languageConfig1 = null;

    private Language languageConfig2 = null;

    private Language languageConfig3 = null;

    private Language languageConfig4 = null;

    private FormListener formListener;

    Map<String, List<String>> pluginLanguageMap = null;

    private boolean update = false;

    /** Creates new form PluginMovieDBPanel */
    public OnlineInfoPanel() {
        initComponents();
        initContent();
    }

    private void updatePanel(Language[] languages) {
        update = true;
        if (languages.length >= 1) {
            resetLanguage1();
            Map<String, String> overideMap = new HashMap<String, String>();
            de.umcProject.xmlbeans.UmcConfigDocument.UmcConfig.Languages.Language language = languages[0];
            setLanguage1Enabled(true);
            languageConfig1 = language;
            selectedLanguage1 = language.getId();
            modelLanguage1.setSelectedItem(language.getId());
            for (Plugin plugin : language.getPluginArray()) {
                if (pluginLanguageMap.get(language.getId()).contains(plugin.getName())) {
                    modelPlugin1.addElement(plugin.getName());
                    modelTitle1.addElement(plugin.getName());
                    modelGenre1.addElement(plugin.getName());
                    modelRating1.addElement(plugin.getName());
                    modelPlot1.addElement(plugin.getName());
                    if (plugin.getStandardArray() != null) {
                        for (String std : plugin.getStandardArray()) {
                            overideMap.put(std.toLowerCase(), plugin.getName());
                        }
                    }
                }
            }
            if (overideMap.get("genre") != null) modelGenre1.setSelectedItem(overideMap.get("genre"));
            if (overideMap.get("title") != null) modelTitle1.setSelectedItem(overideMap.get("title"));
            if (overideMap.get("rating") != null) modelRating1.setSelectedItem(overideMap.get("rating"));
            if (overideMap.get("plot") != null) modelPlot1.setSelectedItem(overideMap.get("plot"));
        } else {
            modelLanguage1.setSelectedItem("");
            languageConfig1 = null;
            resetLanguage1();
            setLanguage1Enabled(false);
        }
        if (languages.length >= 2) {
            resetLanguage2();
            Map<String, String> overideMap = new HashMap<String, String>();
            de.umcProject.xmlbeans.UmcConfigDocument.UmcConfig.Languages.Language language = languages[1];
            setLanguage2Enabled(true);
            languageConfig2 = language;
            selectedLanguage2 = language.getId();
            modelLanguage2.setSelectedItem(language.getId());
            for (Plugin plugin : language.getPluginArray()) {
                if (pluginLanguageMap.get(language.getId()).contains(plugin.getName())) {
                    modelPlugin2.addElement(plugin.getName());
                    modelTitle2.addElement(plugin.getName());
                    modelGenre2.addElement(plugin.getName());
                    modelRating2.addElement(plugin.getName());
                    modelPlot2.addElement(plugin.getName());
                    if (plugin.getStandardArray() != null) {
                        for (String std : plugin.getStandardArray()) {
                            overideMap.put(std.toLowerCase(), plugin.getName());
                        }
                    }
                }
            }
            if (overideMap.get("genre") != null) modelGenre2.setSelectedItem(overideMap.get("genre"));
            if (overideMap.get("title") != null) modelTitle2.setSelectedItem(overideMap.get("title"));
            if (overideMap.get("rating") != null) modelRating2.setSelectedItem(overideMap.get("rating"));
            if (overideMap.get("plot") != null) modelPlot2.setSelectedItem(overideMap.get("plot"));
        } else {
            modelLanguage2.setSelectedItem("");
            languageConfig2 = null;
            resetLanguage2();
            setLanguage2Enabled(false);
        }
        if (languages.length >= 3) {
            resetLanguage3();
            Map<String, String> overideMap = new HashMap<String, String>();
            setLanguage3Enabled(true);
            de.umcProject.xmlbeans.UmcConfigDocument.UmcConfig.Languages.Language language = languages[2];
            languageConfig3 = language;
            selectedLanguage3 = language.getId();
            modelLanguage3.setSelectedItem(language.getId());
            for (Plugin plugin : language.getPluginArray()) {
                if (pluginLanguageMap.get(language.getId()).contains(plugin.getName())) {
                    modelPlugin3.addElement(plugin.getName());
                    modelTitle3.addElement(plugin.getName());
                    modelGenre3.addElement(plugin.getName());
                    modelRating3.addElement(plugin.getName());
                    modelPlot3.addElement(plugin.getName());
                    if (plugin.getStandardArray() != null) {
                        for (String std : plugin.getStandardArray()) {
                            overideMap.put(std.toLowerCase(), plugin.getName());
                        }
                    }
                }
            }
            if (overideMap.get("genre") != null) modelGenre3.setSelectedItem(overideMap.get("genre"));
            if (overideMap.get("title") != null) modelTitle3.setSelectedItem(overideMap.get("title"));
            if (overideMap.get("rating") != null) modelRating3.setSelectedItem(overideMap.get("rating"));
            if (overideMap.get("plot") != null) modelPlot3.setSelectedItem(overideMap.get("plot"));
        } else {
            modelLanguage3.setSelectedItem("");
            languageConfig3 = null;
            resetLanguage3();
            setLanguage3Enabled(false);
        }
        if (languages.length >= 4) {
            resetLanguage4();
            Map<String, String> overideMap = new HashMap<String, String>();
            setLanguage4Enabled(true);
            de.umcProject.xmlbeans.UmcConfigDocument.UmcConfig.Languages.Language language = languages[3];
            languageConfig4 = language;
            selectedLanguage4 = language.getId();
            modelLanguage4.setSelectedItem(language.getId());
            for (Plugin plugin : language.getPluginArray()) {
                if (pluginLanguageMap.get(language.getId()).contains(plugin.getName())) {
                    modelPlugin4.addElement(plugin.getName());
                    modelTitle4.addElement(plugin.getName());
                    modelGenre4.addElement(plugin.getName());
                    modelRating4.addElement(plugin.getName());
                    modelPlot4.addElement(plugin.getName());
                    if (plugin.getStandardArray() != null) {
                        for (String std : plugin.getStandardArray()) {
                            overideMap.put(std.toLowerCase(), plugin.getName());
                        }
                    }
                }
            }
            if (overideMap.get("genre") != null) modelGenre4.setSelectedItem(overideMap.get("genre"));
            if (overideMap.get("title") != null) modelTitle4.setSelectedItem(overideMap.get("title"));
            if (overideMap.get("rating") != null) modelRating4.setSelectedItem(overideMap.get("rating"));
            if (overideMap.get("plot") != null) modelPlot4.setSelectedItem(overideMap.get("plot"));
        } else {
            modelLanguage4.setSelectedItem("");
            languageConfig4 = null;
            resetLanguage4();
            setLanguage4Enabled(false);
        }
        update = false;
    }

    private void initContent() {
        tags = Arrays.asList(UMCLanguage.getText("OnlineInfoPanel.tags").split(","));
        comboLanguage2.setEnabled(false);
        comboLanguage3.setEnabled(false);
        comboLanguage4.setEnabled(false);
        setLanguage1Enabled(false);
        setLanguage2Enabled(false);
        setLanguage3Enabled(false);
        setLanguage4Enabled(false);
        modelLanguage1 = new DefaultComboBoxModel();
        modelLanguage1.addElement("");
        modelLanguage2 = new DefaultComboBoxModel();
        modelLanguage2.addElement("");
        modelLanguage3 = new DefaultComboBoxModel();
        modelLanguage3.addElement("");
        modelLanguage4 = new DefaultComboBoxModel();
        modelLanguage4.addElement("");
        pluginLanguageMap = Publisher.getInstance().getSupportedMovieDBLanguages();
        String[] lngList = pluginLanguageMap.keySet().toArray(new String[pluginLanguageMap.keySet().size()]);
        Arrays.sort(lngList);
        for (String s : lngList) {
            modelLanguage1.addElement(s);
            modelLanguage2.addElement(s);
            modelLanguage3.addElement(s);
            modelLanguage4.addElement(s);
        }
        modelPlugin1 = new DefaultListModel();
        modelPlugin2 = new DefaultListModel();
        modelPlugin3 = new DefaultListModel();
        modelPlugin4 = new DefaultListModel();
        modelTitle1 = new DefaultComboBoxModel();
        modelTitle1.addElement("");
        modelTitle2 = new DefaultComboBoxModel();
        modelTitle2.addElement("");
        modelTitle3 = new DefaultComboBoxModel();
        modelTitle3.addElement("");
        modelTitle4 = new DefaultComboBoxModel();
        modelTitle4.addElement("");
        modelGenre1 = new DefaultComboBoxModel();
        modelGenre1.addElement("");
        modelGenre2 = new DefaultComboBoxModel();
        modelGenre2.addElement("");
        modelGenre3 = new DefaultComboBoxModel();
        modelGenre3.addElement("");
        modelGenre4 = new DefaultComboBoxModel();
        modelGenre4.addElement("");
        modelPlot1 = new DefaultComboBoxModel();
        modelPlot1.addElement("");
        modelPlot2 = new DefaultComboBoxModel();
        modelPlot2.addElement("");
        modelPlot3 = new DefaultComboBoxModel();
        modelPlot3.addElement("");
        modelPlot4 = new DefaultComboBoxModel();
        modelPlot4.addElement("");
        modelRating1 = new DefaultComboBoxModel();
        modelRating1.addElement("");
        modelRating2 = new DefaultComboBoxModel();
        modelRating2.addElement("");
        modelRating3 = new DefaultComboBoxModel();
        modelRating3.addElement("");
        modelRating4 = new DefaultComboBoxModel();
        modelRating4.addElement("");
        if (ConfigController.getInstance().getUMCConfig() != null && ConfigController.getInstance().getUMCConfig().getLanguages() != null) {
            languages = ConfigController.getInstance().getUMCConfig().getLanguages();
            updatePanel(languages.getLanguageArray());
        } else {
        }
        comboLanguage1.setModel(modelLanguage1);
        comboLanguage2.setModel(modelLanguage2);
        comboLanguage3.setModel(modelLanguage3);
        comboLanguage4.setModel(modelLanguage4);
        listPlugins1.setModel(modelPlugin1);
        listPlugins2.setModel(modelPlugin2);
        listPlugins3.setModel(modelPlugin3);
        listPlugins4.setModel(modelPlugin4);
        comboTitle1.setModel(modelTitle1);
        comboTitle2.setModel(modelTitle2);
        comboTitle3.setModel(modelTitle3);
        comboTitle4.setModel(modelTitle4);
        comboRating1.setModel(modelRating1);
        comboRating2.setModel(modelRating2);
        comboRating3.setModel(modelRating3);
        comboRating4.setModel(modelRating4);
        comboGenre1.setModel(modelGenre1);
        comboGenre2.setModel(modelGenre2);
        comboGenre3.setModel(modelGenre3);
        comboGenre4.setModel(modelGenre4);
        comboPlot1.setModel(modelPlot1);
        comboPlot2.setModel(modelPlot2);
        comboPlot3.setModel(modelPlot3);
        comboPlot4.setModel(modelPlot4);
        comboPlot1.addActionListener(formListener);
        comboTitle1.addActionListener(formListener);
        comboRating1.addActionListener(formListener);
        comboGenre1.addActionListener(formListener);
        comboPlot2.addActionListener(formListener);
        comboTitle2.addActionListener(formListener);
        comboRating2.addActionListener(formListener);
        comboGenre2.addActionListener(formListener);
        comboPlot3.addActionListener(formListener);
        comboTitle3.addActionListener(formListener);
        comboRating3.addActionListener(formListener);
        comboGenre3.addActionListener(formListener);
        comboPlot4.addActionListener(formListener);
        comboTitle4.addActionListener(formListener);
        comboRating4.addActionListener(formListener);
        comboGenre4.addActionListener(formListener);
        comboLanguage1.addActionListener(formListener);
        comboLanguage2.addActionListener(formListener);
        comboLanguage3.addActionListener(formListener);
        comboLanguage4.addActionListener(formListener);
    }

    private void initComponents() {
        panel1 = new JPanel();
        labelListPlugins1 = new JLabel();
        jScrollPane2 = new JScrollPane();
        listPlugins1 = new JList();
        btnUp1 = new JButton();
        btnDown1 = new JButton();
        btnAddPlugin1 = new JButton();
        btnRemPlugin1 = new JButton();
        jSeparator2 = new JSeparator();
        labelInfo1 = new JLabel();
        labelTitle1 = new JLabel();
        labelPlot1 = new JLabel();
        labelRating1 = new JLabel();
        labelGenre1 = new JLabel();
        comboTitle1 = new JComboBox();
        comboPlot1 = new JComboBox();
        comboRating1 = new JComboBox();
        comboGenre1 = new JComboBox();
        comboLanguage1 = new JComboBox();
        btnLeft1 = new JButton();
        btnRight1 = new JButton();
        labelLanguage1 = new JLabel();
        panel2 = new JPanel();
        labelListPlugins2 = new JLabel();
        jScrollPane3 = new JScrollPane();
        listPlugins2 = new JList();
        btnUp2 = new JButton();
        btnDown2 = new JButton();
        btnAddPlugin2 = new JButton();
        btnRemPlugin2 = new JButton();
        jSeparator3 = new JSeparator();
        labelInfo2 = new JLabel();
        labelTitle2 = new JLabel();
        labelPlot2 = new JLabel();
        labelRating2 = new JLabel();
        labelGenre2 = new JLabel();
        comboTitle2 = new JComboBox();
        comboPlot2 = new JComboBox();
        comboRating2 = new JComboBox();
        comboGenre2 = new JComboBox();
        comboLanguage2 = new JComboBox();
        btnLeft2 = new JButton();
        btnRight2 = new JButton();
        labelLanguage2 = new JLabel();
        panel3 = new JPanel();
        labelListPlugins3 = new JLabel();
        jScrollPane4 = new JScrollPane();
        listPlugins3 = new JList();
        btnUp3 = new JButton();
        btnDown3 = new JButton();
        btnAddPlugin3 = new JButton();
        btnRemPlugin3 = new JButton();
        jSeparator4 = new JSeparator();
        labelInfo3 = new JLabel();
        labelTitle3 = new JLabel();
        labelPlot3 = new JLabel();
        labelRating3 = new JLabel();
        labelGenre3 = new JLabel();
        comboTitle3 = new JComboBox();
        comboPlot3 = new JComboBox();
        comboRating3 = new JComboBox();
        comboGenre3 = new JComboBox();
        comboLanguage3 = new JComboBox();
        btnLeft3 = new JButton();
        btnRight3 = new JButton();
        labelLanguage3 = new JLabel();
        panel4 = new JPanel();
        labelListPlugins4 = new JLabel();
        jScrollPane1 = new JScrollPane();
        listPlugins4 = new JList();
        btnUp4 = new JButton();
        btnDown4 = new JButton();
        btnAddPlugin4 = new JButton();
        btnRemPlugin4 = new JButton();
        jSeparator1 = new JSeparator();
        labelInfo4 = new JLabel();
        labelTitle4 = new JLabel();
        labelPlot4 = new JLabel();
        labelRating4 = new JLabel();
        labelGenre4 = new JLabel();
        comboTitle4 = new JComboBox();
        comboPlot4 = new JComboBox();
        comboRating4 = new JComboBox();
        comboGenre4 = new JComboBox();
        comboLanguage4 = new JComboBox();
        btnLeft4 = new JButton();
        btnRight4 = new JButton();
        labelLanguage4 = new JLabel();
        formListener = new FormListener();
        panel1.setBorder(BorderFactory.createEtchedBorder());
        labelListPlugins1.setText(UMCLanguage.getText("OnlineInfoPanel.labelListPlugins1.text"));
        listPlugins1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(listPlugins1);
        btnUp1.setIcon(new ImageIcon(getClass().getResource("/com/umc/gui/resources/up.png")));
        btnUp1.setMargin(new Insets(0, 0, 0, 0));
        btnUp1.addActionListener(formListener);
        btnDown1.setIcon(new ImageIcon(getClass().getResource("/com/umc/gui/resources/down.png")));
        btnDown1.setMargin(new Insets(0, 0, 0, 0));
        btnDown1.addActionListener(formListener);
        btnAddPlugin1.setIcon(new ImageIcon(getClass().getResource("/com/umc/gui/resources/add2.png")));
        btnAddPlugin1.setMargin(new Insets(0, 0, 0, 0));
        btnAddPlugin1.addActionListener(formListener);
        btnRemPlugin1.setIcon(new ImageIcon(getClass().getResource("/com/umc/gui/resources/remove2.png")));
        btnRemPlugin1.addActionListener(formListener);
        btnRemPlugin1.setMargin(new Insets(0, 0, 0, 0));
        labelInfo1.setText(UMCLanguage.getText("OnlineInfoPanel.labelInfo1.text"));
        labelTitle1.setText(UMCLanguage.getText("OnlineInfoPanel.labelTitle1.text"));
        labelPlot1.setText(UMCLanguage.getText("OnlineInfoPanel.labelPlot1.text"));
        labelRating1.setText(UMCLanguage.getText("OnlineInfoPanel.labelRating1.text"));
        labelGenre1.setText(UMCLanguage.getText("OnlineInfoPanel.labelGenre1.text"));
        btnLeft1.setIcon(new ImageIcon(getClass().getResource("/com/umc/gui/resources/left.png")));
        btnLeft1.setEnabled(false);
        btnLeft1.setMargin(new Insets(0, 0, 0, 0));
        btnRight1.setIcon(new ImageIcon(getClass().getResource("/com/umc/gui/resources/right.png")));
        btnRight1.setMargin(new Insets(0, 0, 0, 0));
        btnRight1.addActionListener(formListener);
        btnRight1.setEnabled(false);
        labelLanguage1.setFont(labelLanguage1.getFont().deriveFont(labelLanguage1.getFont().getStyle() | Font.BOLD));
        labelLanguage1.setHorizontalAlignment(SwingConstants.CENTER);
        labelLanguage1.setText(UMCLanguage.getText("OnlineInfoPanel.labelLanguage1.text"));
        GroupLayout panel1Layout = new GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(panel1Layout.createParallelGroup(GroupLayout.LEADING).add(panel1Layout.createSequentialGroup().addContainerGap().add(panel1Layout.createParallelGroup(GroupLayout.LEADING).add(labelListPlugins1).add(GroupLayout.TRAILING, panel1Layout.createSequentialGroup().add(jScrollPane2, GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE).addPreferredGap(LayoutStyle.RELATED).add(panel1Layout.createParallelGroup(GroupLayout.TRAILING).add(btnUp1).add(btnDown1).add(btnAddPlugin1).add(btnRemPlugin1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))).add(jSeparator2, GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE).add(labelInfo1).add(panel1Layout.createSequentialGroup().add(panel1Layout.createParallelGroup(GroupLayout.LEADING).add(labelTitle1).add(labelPlot1).add(labelRating1).add(labelGenre1)).addPreferredGap(LayoutStyle.RELATED).add(panel1Layout.createParallelGroup(GroupLayout.LEADING).add(comboTitle1, 0, 96, Short.MAX_VALUE).add(comboPlot1, 0, 96, Short.MAX_VALUE).add(comboRating1, 0, 96, Short.MAX_VALUE).add(comboGenre1, 0, 96, Short.MAX_VALUE))).add(comboLanguage1, 0, 131, Short.MAX_VALUE).add(panel1Layout.createSequentialGroup().add(btnLeft1).addPreferredGap(LayoutStyle.RELATED).add(labelLanguage1, GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE).addPreferredGap(LayoutStyle.RELATED).add(btnRight1))).addContainerGap()));
        panel1Layout.setVerticalGroup(panel1Layout.createParallelGroup(GroupLayout.LEADING).add(panel1Layout.createSequentialGroup().addContainerGap().add(panel1Layout.createParallelGroup(GroupLayout.TRAILING, false).add(labelLanguage1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(btnLeft1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(btnRight1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(LayoutStyle.RELATED).add(comboLanguage1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.RELATED).add(labelListPlugins1).addPreferredGap(LayoutStyle.RELATED).add(panel1Layout.createParallelGroup(GroupLayout.LEADING).add(panel1Layout.createSequentialGroup().add(btnUp1).addPreferredGap(LayoutStyle.RELATED).add(btnDown1).addPreferredGap(LayoutStyle.RELATED).add(btnAddPlugin1).addPreferredGap(LayoutStyle.RELATED).add(btnRemPlugin1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).add(jScrollPane2, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.UNRELATED).add(jSeparator2, GroupLayout.PREFERRED_SIZE, 8, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.RELATED).add(labelInfo1).addPreferredGap(LayoutStyle.UNRELATED).add(panel1Layout.createParallelGroup(GroupLayout.BASELINE).add(labelTitle1).add(comboTitle1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(panel1Layout.createParallelGroup(GroupLayout.BASELINE).add(labelPlot1).add(comboPlot1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(panel1Layout.createParallelGroup(GroupLayout.BASELINE).add(labelRating1).add(comboRating1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(panel1Layout.createParallelGroup(GroupLayout.BASELINE).add(labelGenre1).add(comboGenre1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addContainerGap(44, Short.MAX_VALUE)));
        panel2.setBorder(BorderFactory.createEtchedBorder());
        labelListPlugins2.setText(UMCLanguage.getText("OnlineInfoPanel.labelListPlugins2.text"));
        listPlugins2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(listPlugins2);
        btnUp2.setIcon(new ImageIcon(getClass().getResource("/com/umc/gui/resources/up.png")));
        btnUp2.setMargin(new Insets(0, 0, 0, 0));
        btnUp2.addActionListener(formListener);
        btnDown2.setIcon(new ImageIcon(getClass().getResource("/com/umc/gui/resources/down.png")));
        btnDown2.setMargin(new Insets(0, 0, 0, 0));
        btnDown2.addActionListener(formListener);
        btnAddPlugin2.setIcon(new ImageIcon(getClass().getResource("/com/umc/gui/resources/add2.png")));
        btnAddPlugin2.setMargin(new Insets(0, 0, 0, 0));
        btnAddPlugin2.addActionListener(formListener);
        btnRemPlugin2.setIcon(new ImageIcon(getClass().getResource("/com/umc/gui/resources/remove2.png")));
        btnRemPlugin2.setMargin(new Insets(0, 0, 0, 0));
        btnRemPlugin2.addActionListener(formListener);
        labelInfo2.setText(UMCLanguage.getText("OnlineInfoPanel.labelInfo2.text"));
        labelTitle2.setText(UMCLanguage.getText("OnlineInfoPanel.labelTitle2.text"));
        labelPlot2.setText(UMCLanguage.getText("OnlineInfoPanel.labelPlot2.text"));
        labelRating2.setText(UMCLanguage.getText("OnlineInfoPanel.labelRating2.text"));
        labelGenre2.setText(UMCLanguage.getText("OnlineInfoPanel.labelGenre2.text"));
        btnLeft2.setIcon(new ImageIcon(getClass().getResource("/com/umc/gui/resources/left.png")));
        btnLeft2.setMargin(new Insets(0, 0, 0, 0));
        btnLeft2.setEnabled(false);
        btnLeft2.addActionListener(formListener);
        btnRight2.setIcon(new ImageIcon(getClass().getResource("/com/umc/gui/resources/right.png")));
        btnRight2.setMargin(new Insets(0, 0, 0, 0));
        btnRight2.setEnabled(false);
        btnRight2.addActionListener(formListener);
        labelLanguage2.setFont(labelLanguage2.getFont().deriveFont(labelLanguage2.getFont().getStyle() | Font.BOLD));
        labelLanguage2.setHorizontalAlignment(SwingConstants.CENTER);
        labelLanguage2.setText(UMCLanguage.getText("OnlineInfoPanel.labelLanguage2.text"));
        GroupLayout panel2Layout = new GroupLayout(panel2);
        panel2.setLayout(panel2Layout);
        panel2Layout.setHorizontalGroup(panel2Layout.createParallelGroup(GroupLayout.LEADING).add(panel2Layout.createSequentialGroup().addContainerGap().add(panel2Layout.createParallelGroup(GroupLayout.LEADING).add(labelListPlugins2).add(GroupLayout.TRAILING, panel2Layout.createSequentialGroup().add(jScrollPane3, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE).addPreferredGap(LayoutStyle.RELATED).add(panel2Layout.createParallelGroup(GroupLayout.TRAILING).add(btnUp2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).add(btnDown2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).add(btnAddPlugin2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).add(btnRemPlugin2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))).add(jSeparator3, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE).add(labelInfo2).add(panel2Layout.createSequentialGroup().add(panel2Layout.createParallelGroup(GroupLayout.LEADING).add(labelTitle2).add(labelPlot2).add(labelRating2).add(labelGenre2)).addPreferredGap(LayoutStyle.RELATED).add(panel2Layout.createParallelGroup(GroupLayout.LEADING).add(comboTitle2, 0, 95, Short.MAX_VALUE).add(comboPlot2, 0, 95, Short.MAX_VALUE).add(comboRating2, 0, 95, Short.MAX_VALUE).add(comboGenre2, 0, 95, Short.MAX_VALUE))).add(comboLanguage2, 0, 130, Short.MAX_VALUE).add(panel2Layout.createSequentialGroup().add(btnLeft2).addPreferredGap(LayoutStyle.RELATED).add(labelLanguage2, GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE).addPreferredGap(LayoutStyle.RELATED).add(btnRight2))).addContainerGap()));
        panel2Layout.setVerticalGroup(panel2Layout.createParallelGroup(GroupLayout.LEADING).add(panel2Layout.createSequentialGroup().addContainerGap().add(panel2Layout.createParallelGroup(GroupLayout.TRAILING, false).add(labelLanguage2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(btnLeft2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(btnRight2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(LayoutStyle.RELATED).add(comboLanguage2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.RELATED).add(labelListPlugins2).addPreferredGap(LayoutStyle.RELATED).add(panel2Layout.createParallelGroup(GroupLayout.LEADING).add(panel2Layout.createSequentialGroup().add(btnUp2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.RELATED).add(btnDown2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.RELATED).add(btnAddPlugin2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.RELATED).add(btnRemPlugin2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).add(jScrollPane3, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.UNRELATED).add(jSeparator3, GroupLayout.PREFERRED_SIZE, 8, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.RELATED).add(labelInfo2).addPreferredGap(LayoutStyle.UNRELATED).add(panel2Layout.createParallelGroup(GroupLayout.BASELINE).add(labelTitle2).add(comboTitle2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(panel2Layout.createParallelGroup(GroupLayout.BASELINE).add(labelPlot2).add(comboPlot2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(panel2Layout.createParallelGroup(GroupLayout.BASELINE).add(labelRating2).add(comboRating2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(panel2Layout.createParallelGroup(GroupLayout.BASELINE).add(labelGenre2).add(comboGenre2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addContainerGap(44, Short.MAX_VALUE)));
        panel3.setBorder(BorderFactory.createEtchedBorder());
        labelListPlugins3.setText(UMCLanguage.getText("OnlineInfoPanel.labelListPlugins3.text"));
        listPlugins3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane4.setViewportView(listPlugins3);
        btnUp3.setIcon(new ImageIcon(getClass().getResource("/com/umc/gui/resources/up.png")));
        btnUp3.setMargin(new Insets(0, 0, 0, 0));
        btnUp3.addActionListener(formListener);
        btnDown3.setIcon(new ImageIcon(getClass().getResource("/com/umc/gui/resources/down.png")));
        btnDown3.setMargin(new Insets(0, 0, 0, 0));
        btnDown3.addActionListener(formListener);
        btnAddPlugin3.setIcon(new ImageIcon(getClass().getResource("/com/umc/gui/resources/add2.png")));
        btnAddPlugin3.setMargin(new Insets(0, 0, 0, 0));
        btnAddPlugin3.addActionListener(formListener);
        btnRemPlugin3.setIcon(new ImageIcon(getClass().getResource("/com/umc/gui/resources/remove2.png")));
        btnRemPlugin3.setMargin(new Insets(0, 0, 0, 0));
        btnRemPlugin3.addActionListener(formListener);
        labelInfo3.setText(UMCLanguage.getText("OnlineInfoPanel.labelInfo3.text"));
        labelTitle3.setText(UMCLanguage.getText("OnlineInfoPanel.labelTitle3.text"));
        labelPlot3.setText(UMCLanguage.getText("OnlineInfoPanel.labelPlot3.text"));
        labelRating3.setText(UMCLanguage.getText("OnlineInfoPanel.labelRating3.text"));
        labelGenre3.setText(UMCLanguage.getText("OnlineInfoPanel.labelGenre3.text"));
        btnLeft3.setIcon(new ImageIcon(getClass().getResource("/com/umc/gui/resources/left.png")));
        btnLeft3.setMargin(new Insets(0, 0, 0, 0));
        btnLeft3.setEnabled(false);
        btnLeft3.addActionListener(formListener);
        btnRight3.setIcon(new ImageIcon(getClass().getResource("/com/umc/gui/resources/right.png")));
        btnRight3.setMargin(new Insets(0, 0, 0, 0));
        btnRight3.setEnabled(false);
        btnRight3.addActionListener(formListener);
        labelLanguage3.setFont(labelLanguage3.getFont().deriveFont(labelLanguage3.getFont().getStyle() | Font.BOLD));
        labelLanguage3.setHorizontalAlignment(SwingConstants.CENTER);
        labelLanguage3.setText(UMCLanguage.getText("OnlineInfoPanel.labelLanguage3.text"));
        GroupLayout panel3Layout = new GroupLayout(panel3);
        panel3.setLayout(panel3Layout);
        panel3Layout.setHorizontalGroup(panel3Layout.createParallelGroup(GroupLayout.LEADING).add(panel3Layout.createSequentialGroup().addContainerGap().add(panel3Layout.createParallelGroup(GroupLayout.LEADING).add(labelListPlugins3).add(GroupLayout.TRAILING, panel3Layout.createSequentialGroup().add(jScrollPane4, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE).addPreferredGap(LayoutStyle.RELATED).add(panel3Layout.createParallelGroup(GroupLayout.TRAILING).add(btnUp3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).add(btnDown3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).add(btnAddPlugin3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).add(btnRemPlugin3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))).add(jSeparator4, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE).add(labelInfo3).add(panel3Layout.createSequentialGroup().add(panel3Layout.createParallelGroup(GroupLayout.LEADING).add(labelTitle3).add(labelPlot3).add(labelRating3).add(labelGenre3)).addPreferredGap(LayoutStyle.RELATED).add(panel3Layout.createParallelGroup(GroupLayout.LEADING).add(comboTitle3, 0, 94, Short.MAX_VALUE).add(comboPlot3, 0, 94, Short.MAX_VALUE).add(comboRating3, 0, 94, Short.MAX_VALUE).add(comboGenre3, 0, 94, Short.MAX_VALUE))).add(comboLanguage3, 0, 129, Short.MAX_VALUE).add(panel3Layout.createSequentialGroup().add(btnLeft3).addPreferredGap(LayoutStyle.RELATED).add(labelLanguage3, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE).addPreferredGap(LayoutStyle.RELATED).add(btnRight3))).addContainerGap()));
        panel3Layout.setVerticalGroup(panel3Layout.createParallelGroup(GroupLayout.LEADING).add(panel3Layout.createSequentialGroup().addContainerGap().add(panel3Layout.createParallelGroup(GroupLayout.TRAILING, false).add(labelLanguage3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(btnLeft3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(btnRight3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(LayoutStyle.RELATED).add(comboLanguage3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.RELATED).add(labelListPlugins3).addPreferredGap(LayoutStyle.RELATED).add(panel3Layout.createParallelGroup(GroupLayout.LEADING).add(panel3Layout.createSequentialGroup().add(btnUp3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.RELATED).add(btnDown3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.RELATED).add(btnAddPlugin3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.RELATED).add(btnRemPlugin3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).add(jScrollPane4, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.UNRELATED).add(jSeparator4, GroupLayout.PREFERRED_SIZE, 8, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.RELATED).add(labelInfo3).addPreferredGap(LayoutStyle.UNRELATED).add(panel3Layout.createParallelGroup(GroupLayout.BASELINE).add(labelTitle3).add(comboTitle3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(panel3Layout.createParallelGroup(GroupLayout.BASELINE).add(labelPlot3).add(comboPlot3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(panel3Layout.createParallelGroup(GroupLayout.BASELINE).add(labelRating3).add(comboRating3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(panel3Layout.createParallelGroup(GroupLayout.BASELINE).add(labelGenre3).add(comboGenre3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addContainerGap(44, Short.MAX_VALUE)));
        panel4.setBorder(BorderFactory.createEtchedBorder());
        labelListPlugins4.setText(UMCLanguage.getText("OnlineInfoPanel.labelListPlugins4.text"));
        listPlugins4.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(listPlugins4);
        btnUp4.setIcon(new ImageIcon(getClass().getResource("/com/umc/gui/resources/up.png")));
        btnUp4.setMargin(new Insets(0, 0, 0, 0));
        btnUp4.addActionListener(formListener);
        btnDown4.setIcon(new ImageIcon(getClass().getResource("/com/umc/gui/resources/down.png")));
        btnDown4.setMargin(new Insets(0, 0, 0, 0));
        btnDown4.addActionListener(formListener);
        btnAddPlugin4.setIcon(new ImageIcon(getClass().getResource("/com/umc/gui/resources/add2.png")));
        btnAddPlugin4.setMargin(new Insets(0, 0, 0, 0));
        btnAddPlugin4.addActionListener(formListener);
        btnRemPlugin4.setIcon(new ImageIcon(getClass().getResource("/com/umc/gui/resources/remove2.png")));
        btnRemPlugin4.setMargin(new Insets(0, 0, 0, 0));
        btnRemPlugin4.addActionListener(formListener);
        labelInfo4.setText(UMCLanguage.getText("OnlineInfoPanel.labelInfo4.text"));
        labelTitle4.setText(UMCLanguage.getText("OnlineInfoPanel.labelTitle4.text"));
        labelPlot4.setText(UMCLanguage.getText("OnlineInfoPanel.labelPlot4.text"));
        labelRating4.setText(UMCLanguage.getText("OnlineInfoPanel.labelRating4.text"));
        labelGenre4.setText(UMCLanguage.getText("OnlineInfoPanel.labelGenre4.text"));
        btnLeft4.setIcon(new ImageIcon(getClass().getResource("/com/umc/gui/resources/left.png")));
        btnLeft4.setMargin(new Insets(0, 0, 0, 0));
        btnLeft4.setEnabled(false);
        btnLeft4.addActionListener(formListener);
        btnRight4.setIcon(new ImageIcon(getClass().getResource("/com/umc/gui/resources/right.png")));
        btnRight4.setEnabled(false);
        btnRight4.setMargin(new Insets(0, 0, 0, 0));
        labelLanguage4.setFont(labelLanguage4.getFont().deriveFont(labelLanguage4.getFont().getStyle() | Font.BOLD));
        labelLanguage4.setHorizontalAlignment(SwingConstants.CENTER);
        labelLanguage4.setText(UMCLanguage.getText("OnlineInfoPanel.labelLanguage4.text"));
        GroupLayout panel4Layout = new GroupLayout(panel4);
        panel4.setLayout(panel4Layout);
        panel4Layout.setHorizontalGroup(panel4Layout.createParallelGroup(GroupLayout.LEADING).add(panel4Layout.createSequentialGroup().addContainerGap().add(panel4Layout.createParallelGroup(GroupLayout.LEADING).add(labelListPlugins4).add(GroupLayout.TRAILING, panel4Layout.createSequentialGroup().add(jScrollPane1, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE).addPreferredGap(LayoutStyle.RELATED).add(panel4Layout.createParallelGroup(GroupLayout.TRAILING).add(btnUp4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).add(btnDown4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).add(btnAddPlugin4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).add(btnRemPlugin4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))).add(jSeparator1, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE).add(labelInfo4).add(panel4Layout.createSequentialGroup().add(panel4Layout.createParallelGroup(GroupLayout.LEADING).add(labelTitle4).add(labelPlot4).add(labelRating4).add(labelGenre4)).addPreferredGap(LayoutStyle.RELATED).add(panel4Layout.createParallelGroup(GroupLayout.LEADING).add(comboTitle4, 0, 94, Short.MAX_VALUE).add(comboPlot4, 0, 94, Short.MAX_VALUE).add(comboRating4, 0, 94, Short.MAX_VALUE).add(comboGenre4, 0, 94, Short.MAX_VALUE))).add(comboLanguage4, 0, 129, Short.MAX_VALUE).add(panel4Layout.createSequentialGroup().add(btnLeft4).addPreferredGap(LayoutStyle.RELATED).add(labelLanguage4, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE).addPreferredGap(LayoutStyle.RELATED).add(btnRight4))).addContainerGap()));
        panel4Layout.setVerticalGroup(panel4Layout.createParallelGroup(GroupLayout.LEADING).add(panel4Layout.createSequentialGroup().addContainerGap().add(panel4Layout.createParallelGroup(GroupLayout.TRAILING, false).add(labelLanguage4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(btnLeft4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(btnRight4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(LayoutStyle.RELATED).add(comboLanguage4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.RELATED).add(labelListPlugins4).addPreferredGap(LayoutStyle.RELATED).add(panel4Layout.createParallelGroup(GroupLayout.LEADING).add(panel4Layout.createSequentialGroup().add(btnUp4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.RELATED).add(btnDown4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.RELATED).add(btnAddPlugin4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.RELATED).add(btnRemPlugin4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).add(jScrollPane1, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.UNRELATED).add(jSeparator1, GroupLayout.PREFERRED_SIZE, 8, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.RELATED).add(labelInfo4).addPreferredGap(LayoutStyle.UNRELATED).add(panel4Layout.createParallelGroup(GroupLayout.BASELINE).add(labelTitle4).add(comboTitle4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(panel4Layout.createParallelGroup(GroupLayout.BASELINE).add(labelPlot4).add(comboPlot4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(panel4Layout.createParallelGroup(GroupLayout.BASELINE).add(labelRating4).add(comboRating4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(panel4Layout.createParallelGroup(GroupLayout.BASELINE).add(labelGenre4).add(comboGenre4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addContainerGap(44, Short.MAX_VALUE)));
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(LayoutStyle.RELATED).add(panel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(LayoutStyle.RELATED).add(panel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(LayoutStyle.RELATED).add(panel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.LEADING).add(GroupLayout.TRAILING, layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(GroupLayout.TRAILING).add(GroupLayout.LEADING, panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(GroupLayout.LEADING, panel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(GroupLayout.LEADING, panel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(GroupLayout.LEADING, panel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
    }

    private class FormListener implements ActionListener {

        FormListener() {
        }

        public void actionPerformed(ActionEvent evt) {
            if (!update) {
                if (evt.getSource() == btnUp1) {
                    OnlineInfoPanel.this.btnUpActionPerformed(languageConfig1, listPlugins1);
                } else if (evt.getSource() == btnDown1) {
                    OnlineInfoPanel.this.btnDownActionPerformed(languageConfig1, listPlugins1);
                } else if (evt.getSource() == btnAddPlugin1) {
                    OnlineInfoPanel.this.btnAddPlugin1ActionPerformed(evt);
                } else if (evt.getSource() == btnRemPlugin1) {
                    OnlineInfoPanel.this.btnRemPlugin1ActionPerformed(evt);
                } else if (evt.getSource() == btnRight1 || evt.getSource() == btnLeft2) {
                    OnlineInfoPanel.this.swapLanguageActionPerformed(0);
                } else if (evt.getSource() == comboGenre1) {
                    OnlineInfoPanel.this.comboOverrideActionPerformed(languageConfig1, modelGenre1, "genre");
                } else if (evt.getSource() == comboTitle1) {
                    OnlineInfoPanel.this.comboOverrideActionPerformed(languageConfig1, modelTitle1, "title");
                } else if (evt.getSource() == comboPlot1) {
                    OnlineInfoPanel.this.comboOverrideActionPerformed(languageConfig1, modelPlot1, "plot");
                } else if (evt.getSource() == comboRating1) {
                    OnlineInfoPanel.this.comboOverrideActionPerformed(languageConfig1, modelRating1, "rating");
                } else if (evt.getSource() == comboLanguage1) {
                    OnlineInfoPanel.this.comboLanguage1ActionPerformed(evt);
                } else if (evt.getSource() == comboLanguage2) {
                    OnlineInfoPanel.this.comboLanguage2ActionPerformed(evt);
                } else if (evt.getSource() == btnUp2) {
                    OnlineInfoPanel.this.btnUpActionPerformed(languageConfig2, listPlugins2);
                } else if (evt.getSource() == btnDown2) {
                    OnlineInfoPanel.this.btnDownActionPerformed(languageConfig2, listPlugins2);
                } else if (evt.getSource() == btnAddPlugin2) {
                    OnlineInfoPanel.this.btnAddPlugin2ActionPerformed(evt);
                } else if (evt.getSource() == btnRemPlugin2) {
                    OnlineInfoPanel.this.btnRemPlugin2ActionPerformed(evt);
                } else if (evt.getSource() == comboGenre2) {
                    OnlineInfoPanel.this.comboOverrideActionPerformed(languageConfig2, modelGenre2, "genre");
                } else if (evt.getSource() == comboTitle2) {
                    OnlineInfoPanel.this.comboOverrideActionPerformed(languageConfig2, modelTitle2, "title");
                } else if (evt.getSource() == comboPlot2) {
                    OnlineInfoPanel.this.comboOverrideActionPerformed(languageConfig2, modelPlot2, "plot");
                } else if (evt.getSource() == comboRating2) {
                    OnlineInfoPanel.this.comboOverrideActionPerformed(languageConfig2, modelRating2, "rating");
                } else if (evt.getSource() == btnRight2 || evt.getSource() == btnLeft3) {
                    OnlineInfoPanel.this.swapLanguageActionPerformed(1);
                } else if (evt.getSource() == comboLanguage3) {
                    OnlineInfoPanel.this.comboLanguage3ActionPerformed(evt);
                } else if (evt.getSource() == btnUp3) {
                    OnlineInfoPanel.this.btnUpActionPerformed(languageConfig3, listPlugins3);
                    ;
                } else if (evt.getSource() == btnDown3) {
                    OnlineInfoPanel.this.btnDownActionPerformed(languageConfig3, listPlugins3);
                } else if (evt.getSource() == btnAddPlugin3) {
                    OnlineInfoPanel.this.btnAddPlugin3ActionPerformed(evt);
                } else if (evt.getSource() == btnRemPlugin3) {
                    OnlineInfoPanel.this.btnRemPlugin3ActionPerformed(evt);
                } else if (evt.getSource() == comboGenre3) {
                    OnlineInfoPanel.this.comboOverrideActionPerformed(languageConfig3, modelGenre3, "genre");
                } else if (evt.getSource() == comboTitle3) {
                    OnlineInfoPanel.this.comboOverrideActionPerformed(languageConfig3, modelTitle3, "title");
                } else if (evt.getSource() == comboPlot3) {
                    OnlineInfoPanel.this.comboOverrideActionPerformed(languageConfig3, modelPlot3, "plot");
                } else if (evt.getSource() == comboRating3) {
                    OnlineInfoPanel.this.comboOverrideActionPerformed(languageConfig3, modelRating3, "rating");
                } else if (evt.getSource() == btnRight3 || evt.getSource() == btnLeft4) {
                    OnlineInfoPanel.this.swapLanguageActionPerformed(2);
                } else if (evt.getSource() == comboLanguage4) {
                    OnlineInfoPanel.this.comboLanguage4ActionPerformed(evt);
                } else if (evt.getSource() == btnUp4) {
                    OnlineInfoPanel.this.btnUpActionPerformed(languageConfig4, listPlugins4);
                } else if (evt.getSource() == btnDown4) {
                    OnlineInfoPanel.this.btnDownActionPerformed(languageConfig4, listPlugins4);
                } else if (evt.getSource() == btnAddPlugin4) {
                    OnlineInfoPanel.this.btnAddPlugin4ActionPerformed(evt);
                } else if (evt.getSource() == btnRemPlugin4) {
                    OnlineInfoPanel.this.btnRemPlugin4ActionPerformed(evt);
                } else if (evt.getSource() == comboGenre4) {
                    OnlineInfoPanel.this.comboOverrideActionPerformed(languageConfig4, modelGenre4, "genre");
                } else if (evt.getSource() == comboTitle4) {
                    OnlineInfoPanel.this.comboOverrideActionPerformed(languageConfig4, modelTitle4, "title");
                } else if (evt.getSource() == comboPlot4) {
                    OnlineInfoPanel.this.comboOverrideActionPerformed(languageConfig4, modelPlot4, "plot");
                } else if (evt.getSource() == comboRating4) {
                    OnlineInfoPanel.this.comboOverrideActionPerformed(languageConfig4, modelRating4, "rating");
                }
            }
        }
    }

    /******* LANG 1 *******************/
    private void btnAddPlugin1ActionPerformed(ActionEvent evt) {
        DefaultListModel avalablePlugins = new DefaultListModel();
        for (String plugin : pluginLanguageMap.get(selectedLanguage1)) {
            if (!modelPlugin1.contains(plugin)) {
                avalablePlugins.addElement(plugin);
            }
        }
        OnlineInfoSelectionPanel frame = new OnlineInfoSelectionPanel(avalablePlugins, 1, this, GuiController.getInstance().getMainframe());
        frame.setVisible(true);
    }

    private void btnRemPlugin1ActionPerformed(ActionEvent evt) {
        Object[] items = listPlugins1.getSelectedValues();
        for (int i = 0; i < items.length; i++) {
            modelPlugin1.removeElement(items[i]);
            if (modelGenre1.getSelectedItem().equals(items[i])) modelGenre1.setSelectedItem("");
            modelGenre1.removeElement(items[i]);
            if (modelPlot1.getSelectedItem().equals(items[i])) modelPlot1.setSelectedItem("");
            modelPlot1.removeElement(items[i]);
            if (modelTitle1.getSelectedItem().equals(items[i])) modelTitle1.setSelectedItem("");
            modelTitle1.removeElement(items[i]);
            if (modelRating1.getSelectedItem().equals(items[i])) modelRating1.setSelectedItem("");
            modelRating1.removeElement(items[i]);
            for (int j = 0; j < languageConfig1.getPluginArray().length; j++) {
                if (languageConfig1.getPluginArray(j).getName().equals(items[i])) {
                    languageConfig1.removePlugin(j);
                    break;
                }
            }
        }
    }

    private void comboLanguage1ActionPerformed(ActionEvent evt) {
        String selectedItem = (String) comboLanguage1.getSelectedItem();
        if (!selectedLanguage1.equals(selectedItem)) {
            if (languageConfig1 == null && !selectedItem.equals("")) {
                Language newLang = languages.insertNewLanguage(0);
                newLang.setId(selectedItem);
                newLang.setIgnore(false);
                languageConfig1 = newLang;
                setLanguage1Enabled(true);
            } else if (languageConfig1 != null && ((String) comboLanguage1.getSelectedItem()).equals("")) {
                if (languageConfig2 != null) {
                    resetLanguage1();
                    languageConfig1 = null;
                    languages.removeLanguage(0);
                    setLanguage1Enabled(false);
                    updatePanel(languages.getLanguageArray());
                } else {
                    modelLanguage1.setSelectedItem(selectedLanguage1);
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            JOptionPane.showMessageDialog(GuiController.getInstance().getMainframe(), UMCLanguage.getText("OnlineInfoPanel.warningDelete"), "Info", JOptionPane.INFORMATION_MESSAGE);
                        }
                    });
                }
            } else {
                resetLanguage1();
                languages.removeLanguage(0);
                Language newLang = languages.insertNewLanguage(0);
                newLang.setId(selectedItem);
                newLang.setIgnore(false);
                languageConfig1 = newLang;
                setLanguage1Enabled(true);
            }
            selectedLanguage1 = (String) comboLanguage1.getSelectedItem();
        }
    }

    private void resetLanguage1() {
        modelPlugin1.removeAllElements();
        modelTitle1.removeAllElements();
        modelTitle1.addElement("");
        modelGenre1.removeAllElements();
        modelGenre1.addElement("");
        modelPlot1.removeAllElements();
        modelPlot1.addElement("");
        modelRating1.removeAllElements();
        modelRating1.addElement("");
    }

    public void updatePluginList1(List<String> pluginList) {
        for (String s : pluginList) {
            modelPlugin1.addElement(s);
            modelTitle1.addElement(s);
            modelPlot1.addElement(s);
            modelGenre1.addElement(s);
            modelRating1.addElement(s);
            Plugin plugin = languageConfig1.addNewPlugin();
            plugin.setName(s);
        }
    }

    /******* LANG 2 *******************/
    private void btnAddPlugin2ActionPerformed(ActionEvent evt) {
        DefaultListModel avalablePlugins = new DefaultListModel();
        for (String plugin : pluginLanguageMap.get(selectedLanguage2)) {
            if (!modelPlugin2.contains(plugin)) {
                avalablePlugins.addElement(plugin);
            }
        }
        OnlineInfoSelectionPanel frame = new OnlineInfoSelectionPanel(avalablePlugins, 2, this, GuiController.getInstance().getMainframe());
        frame.setVisible(true);
    }

    private void btnRemPlugin2ActionPerformed(ActionEvent evt) {
        Object[] items = listPlugins2.getSelectedValues();
        for (int i = 0; i < items.length; i++) {
            modelPlugin2.removeElement(items[i]);
            if (modelGenre2.getSelectedItem().equals(items[i])) modelGenre2.setSelectedItem("");
            modelGenre2.removeElement(items[i]);
            if (modelPlot2.getSelectedItem().equals(items[i])) modelPlot2.setSelectedItem("");
            modelPlot2.removeElement(items[i]);
            if (modelTitle2.getSelectedItem().equals(items[i])) modelTitle2.setSelectedItem("");
            modelTitle2.removeElement(items[i]);
            if (modelRating2.getSelectedItem().equals(items[i])) modelRating2.setSelectedItem("");
            modelRating2.removeElement(items[i]);
            for (int j = 0; j < languageConfig2.getPluginArray().length; j++) {
                if (languageConfig2.getPluginArray(j).getName().equals(items[i])) {
                    languageConfig2.removePlugin(j);
                    break;
                }
            }
        }
    }

    private void comboLanguage2ActionPerformed(ActionEvent evt) {
        String selectedItem = (String) comboLanguage2.getSelectedItem();
        if (!selectedLanguage2.equals(selectedItem)) {
            selectedLanguage2 = selectedItem;
            if (languageConfig2 == null && !selectedItem.equals("")) {
                Language newLang = languages.insertNewLanguage(1);
                newLang.setId(selectedItem);
                newLang.setIgnore(false);
                languageConfig2 = newLang;
                setLanguage2Enabled(true);
            } else if (languageConfig2 != null && ((String) comboLanguage2.getSelectedItem()).equals("")) {
                resetLanguage2();
                languageConfig2 = null;
                languages.removeLanguage(1);
                setLanguage2Enabled(false);
                updatePanel(languages.getLanguageArray());
            } else {
                resetLanguage2();
                languages.removeLanguage(1);
                Language newLang = languages.insertNewLanguage(1);
                newLang.setId(selectedItem);
                newLang.setIgnore(false);
                languageConfig2 = newLang;
                setLanguage2Enabled(true);
            }
        }
    }

    private void resetLanguage2() {
        modelPlugin2.removeAllElements();
        modelTitle2.removeAllElements();
        modelTitle2.addElement("");
        modelGenre2.removeAllElements();
        modelGenre2.addElement("");
        modelPlot2.removeAllElements();
        modelPlot2.addElement("");
        modelRating2.removeAllElements();
        modelRating2.addElement("");
    }

    public void updatePluginList2(List<String> pluginList) {
        for (String s : pluginList) {
            modelPlugin2.addElement(s);
            modelTitle2.addElement(s);
            modelPlot2.addElement(s);
            modelGenre2.addElement(s);
            modelRating2.addElement(s);
            Plugin plugin = languageConfig2.addNewPlugin();
            plugin.setName(s);
        }
    }

    /******* LANG 3 *******************/
    private void btnAddPlugin3ActionPerformed(ActionEvent evt) {
        DefaultListModel avalablePlugins = new DefaultListModel();
        for (String plugin : pluginLanguageMap.get(selectedLanguage3)) {
            if (!modelPlugin3.contains(plugin)) {
                avalablePlugins.addElement(plugin);
            }
        }
        OnlineInfoSelectionPanel frame = new OnlineInfoSelectionPanel(avalablePlugins, 3, this, GuiController.getInstance().getMainframe());
        frame.setVisible(true);
    }

    private void btnRemPlugin3ActionPerformed(ActionEvent evt) {
        Object[] items = listPlugins3.getSelectedValues();
        for (int i = 0; i < items.length; i++) {
            modelPlugin3.removeElement(items[i]);
            if (modelGenre3.getSelectedItem().equals(items[i])) modelGenre3.setSelectedItem("");
            modelGenre3.removeElement(items[i]);
            if (modelPlot3.getSelectedItem().equals(items[i])) modelPlot3.setSelectedItem("");
            modelPlot3.removeElement(items[i]);
            if (modelTitle3.getSelectedItem().equals(items[i])) modelTitle3.setSelectedItem("");
            modelTitle3.removeElement(items[i]);
            if (modelRating3.getSelectedItem().equals(items[i])) modelRating3.setSelectedItem("");
            modelRating3.removeElement(items[i]);
            for (int j = 0; j < languageConfig3.getPluginArray().length; j++) {
                if (languageConfig3.getPluginArray(j).getName().equals(items[i])) {
                    languageConfig3.removePlugin(j);
                    break;
                }
            }
        }
    }

    private void comboLanguage3ActionPerformed(ActionEvent evt) {
        String selectedItem = (String) comboLanguage3.getSelectedItem();
        if (!selectedLanguage3.equals(selectedItem)) {
            selectedLanguage3 = selectedItem;
            if (languageConfig3 == null && !selectedItem.equals("")) {
                Language newLang = languages.insertNewLanguage(2);
                newLang.setId(selectedItem);
                newLang.setIgnore(false);
                languageConfig3 = newLang;
                setLanguage3Enabled(true);
            } else if (languageConfig3 != null && selectedItem.equals("")) {
                resetLanguage3();
                languageConfig3 = null;
                languages.removeLanguage(2);
                setLanguage3Enabled(false);
                updatePanel(languages.getLanguageArray());
            } else {
                resetLanguage3();
                languages.removeLanguage(2);
                Language newLang = languages.insertNewLanguage(2);
                newLang.setId(selectedItem);
                newLang.setIgnore(false);
                languageConfig3 = newLang;
                setLanguage3Enabled(true);
            }
        }
    }

    private void resetLanguage3() {
        modelPlugin3.removeAllElements();
        modelTitle3.removeAllElements();
        modelTitle3.addElement("");
        modelGenre3.removeAllElements();
        modelGenre3.addElement("");
        modelPlot3.removeAllElements();
        modelPlot3.addElement("");
        modelRating3.removeAllElements();
        modelRating3.addElement("");
    }

    public void updatePluginList3(List<String> pluginList) {
        for (String s : pluginList) {
            modelPlugin3.addElement(s);
            modelTitle3.addElement(s);
            modelPlot3.addElement(s);
            modelGenre3.addElement(s);
            modelRating3.addElement(s);
            Plugin plugin = languageConfig3.addNewPlugin();
            plugin.setName(s);
        }
    }

    /******* LANG 4 *******************/
    private void btnAddPlugin4ActionPerformed(ActionEvent evt) {
        DefaultListModel avalablePlugins = new DefaultListModel();
        for (String plugin : pluginLanguageMap.get(selectedLanguage4)) {
            if (!modelPlugin4.contains(plugin)) {
                avalablePlugins.addElement(plugin);
            }
        }
        OnlineInfoSelectionPanel frame = new OnlineInfoSelectionPanel(avalablePlugins, 4, this, GuiController.getInstance().getMainframe());
        frame.setVisible(true);
    }

    private void btnRemPlugin4ActionPerformed(ActionEvent evt) {
        Object[] items = listPlugins4.getSelectedValues();
        for (int i = 0; i < items.length; i++) {
            modelPlugin4.removeElement(items[i]);
            if (modelGenre4.getSelectedItem().equals(items[i])) modelGenre4.setSelectedItem("");
            modelGenre4.removeElement(items[i]);
            if (modelPlot4.getSelectedItem().equals(items[i])) modelPlot4.setSelectedItem("");
            modelPlot4.removeElement(items[i]);
            if (modelTitle4.getSelectedItem().equals(items[i])) modelTitle4.setSelectedItem("");
            modelTitle4.removeElement(items[i]);
            if (modelRating4.getSelectedItem().equals(items[i])) modelRating4.setSelectedItem("");
            modelRating4.removeElement(items[i]);
            for (int j = 0; j < languageConfig4.getPluginArray().length; j++) {
                if (languageConfig4.getPluginArray(j).getName().equals(items[i])) {
                    languageConfig4.removePlugin(j);
                    break;
                }
            }
        }
    }

    private void btnUpActionPerformed(Language languageConfig, JList listPlugins) {
        DefaultListModel modelPlugin = (DefaultListModel) listPlugins.getModel();
        int index = listPlugins.getSelectedIndex();
        if (index != -1 && index > 0) {
            Plugin plugin = languageConfig.getPluginArray(index);
            Plugin newPlugin = languageConfig.insertNewPlugin(index - 1);
            newPlugin.setName(plugin.getName());
            newPlugin.setStandardArray(plugin.getStandardArray());
            languageConfig.removePlugin(index + 1);
            String tmp = (String) modelPlugin.remove(index);
            modelPlugin.add(index - 1, tmp);
            listPlugins.setSelectedIndex(index - 1);
        }
    }

    private void btnDownActionPerformed(Language languageConfig, JList listPlugins) {
        DefaultListModel modelPlugin = (DefaultListModel) listPlugins.getModel();
        int index = listPlugins.getSelectedIndex();
        if (index != -1 && index < modelPlugin.size() - 1) {
            Plugin plugin = languageConfig.getPluginArray(index);
            Plugin newPlugin = languageConfig.insertNewPlugin(index + 2);
            newPlugin.setName(plugin.getName());
            newPlugin.setStandardArray(plugin.getStandardArray());
            languageConfig.removePlugin(index);
            String tmp = (String) modelPlugin.remove(index);
            modelPlugin.add(index + 1, tmp);
            listPlugins.setSelectedIndex(index + 1);
        }
    }

    public void updatePluginList4(List<String> pluginList) {
        for (String s : pluginList) {
            modelPlugin4.addElement(s);
            modelTitle4.addElement(s);
            modelPlot4.addElement(s);
            modelGenre4.addElement(s);
            modelRating4.addElement(s);
            Plugin plugin = languageConfig4.addNewPlugin();
            plugin.setName(s);
        }
    }

    private void comboLanguage4ActionPerformed(ActionEvent evt) {
        String selectedItem = (String) comboLanguage4.getSelectedItem();
        if (!selectedLanguage4.equals(selectedItem)) {
            selectedLanguage4 = selectedItem;
            if (languageConfig4 == null && !selectedItem.equals("")) {
                Language newLang = languages.insertNewLanguage(3);
                newLang.setId(selectedItem);
                newLang.setIgnore(false);
                languageConfig4 = newLang;
                setLanguage4Enabled(true);
            } else if (languageConfig4 != null && selectedItem.equals("")) {
                resetLanguage4();
                languageConfig4 = null;
                languages.removeLanguage(3);
                setLanguage4Enabled(false);
                updatePanel(languages.getLanguageArray());
            } else {
                resetLanguage4();
                languages.removeLanguage(3);
                Language newLang = languages.insertNewLanguage(3);
                newLang.setId(selectedItem);
                newLang.setIgnore(false);
                languageConfig4 = newLang;
                setLanguage4Enabled(true);
            }
        }
    }

    private void resetLanguage4() {
        modelPlugin4.removeAllElements();
        modelTitle4.removeAllElements();
        modelTitle4.addElement("");
        modelGenre4.removeAllElements();
        modelGenre4.addElement("");
        modelPlot4.removeAllElements();
        modelPlot4.addElement("");
        modelRating4.removeAllElements();
        modelRating4.addElement("");
    }

    private void swapLanguageActionPerformed(int lng1) {
        Language newLang = languages.insertNewLanguage(lng1);
        newLang.setId(languages.getLanguageArray(lng1 + 2).getId());
        newLang.setIgnore(false);
        newLang.setPluginArray(languages.getLanguageArray(lng1 + 2).getPluginArray());
        languages.removeLanguage(lng1 + 2);
        updatePanel(languages.getLanguageArray());
    }

    private void comboOverrideActionPerformed(Language languageConfig, DefaultComboBoxModel model, String type) {
        for (Plugin plugin : languageConfig.getPluginArray()) {
            if (plugin.getStandardArray() != null) {
                for (int i = 0; i < plugin.getStandardArray().length; i++) {
                    if (plugin.getStandardArray(i).equals(type)) {
                        plugin.removeStandard(i);
                        break;
                    }
                }
            }
            if (plugin.getName().equals(model.getSelectedItem())) {
                plugin.addStandard(type);
            }
        }
    }

    private void setLanguage1Enabled(boolean aFlag) {
        comboLanguage2.setEnabled(aFlag);
        comboGenre1.setEnabled(aFlag);
        comboPlot1.setEnabled(aFlag);
        comboRating1.setEnabled(aFlag);
        comboTitle1.setEnabled(aFlag);
        listPlugins1.setEnabled(aFlag);
        btnUp1.setEnabled(aFlag);
        btnDown1.setEnabled(aFlag);
        btnAddPlugin1.setEnabled(aFlag);
        btnRemPlugin1.setEnabled(aFlag);
    }

    private void setLanguage2Enabled(boolean aFlag) {
        btnRight1.setEnabled(aFlag);
        btnLeft2.setEnabled(aFlag);
        comboLanguage3.setEnabled(aFlag);
        comboGenre2.setEnabled(aFlag);
        comboPlot2.setEnabled(aFlag);
        comboRating2.setEnabled(aFlag);
        comboTitle2.setEnabled(aFlag);
        listPlugins2.setEnabled(aFlag);
        btnUp2.setEnabled(aFlag);
        btnDown2.setEnabled(aFlag);
        btnAddPlugin2.setEnabled(aFlag);
        btnRemPlugin2.setEnabled(aFlag);
    }

    private void setLanguage3Enabled(boolean aFlag) {
        btnRight2.setEnabled(aFlag);
        btnLeft3.setEnabled(aFlag);
        comboLanguage4.setEnabled(aFlag);
        comboGenre3.setEnabled(aFlag);
        comboPlot3.setEnabled(aFlag);
        comboRating3.setEnabled(aFlag);
        comboTitle3.setEnabled(aFlag);
        btnUp3.setEnabled(aFlag);
        btnDown3.setEnabled(aFlag);
        btnAddPlugin3.setEnabled(aFlag);
        btnRemPlugin3.setEnabled(aFlag);
    }

    private void setLanguage4Enabled(boolean aFlag) {
        btnRight3.setEnabled(aFlag);
        btnLeft4.setEnabled(aFlag);
        comboGenre4.setEnabled(aFlag);
        comboPlot4.setEnabled(aFlag);
        comboRating4.setEnabled(aFlag);
        comboTitle4.setEnabled(aFlag);
        btnUp4.setEnabled(aFlag);
        btnDown4.setEnabled(aFlag);
        btnAddPlugin4.setEnabled(aFlag);
        btnRemPlugin4.setEnabled(aFlag);
    }

    public boolean matchFilter(String tag) {
        if (tags.contains(tag)) return true;
        return false;
    }
}
