package org.goniolab.uc;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.Locale;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import org.almondframework.commons.AConstantsHtml;
import org.almondframework.l10n.ALexicon;
import org.almondframework.prefs.APrefsStorageHandler;
import org.almondframework.ui.AApplicationPanel;

/**
 *
 * @author Patrik Karlsson
 */
public class ConfigPanel extends AApplicationPanel implements APrefsStorageHandler {

    public ConfigPanel() {
        init();
    }

    @Override
    public void loadPreferences() {
    }

    @Override
    public void setLocale(Locale locale) {
        super.setLocale(locale);
        int storeIndex = angleUnitComboBox.getSelectedIndex();
        angleUnitComboBox.setModel(new DefaultComboBoxModel(new String[] { lexicon._("AngleUnitsDegLong"), lexicon._("AngleUnitsGraLong"), lexicon._("AngleUnitsRadLong") }));
        angleUnitComboBox.setSelectedIndex(storeIndex);
    }

    @Override
    public void savePreferences() {
    }

    private void init() {
        getAApplication().getLexiconNotificationList().add(this);
        getAApplication().getLexiconNotificationList().add(lexicon);
        initLexicons();
        initLayout();
        orientationComboBox.setModel(new DefaultComboBoxModel(new String[] { "X", "Y" }));
        bgColorButton.setIcon(new ImageIcon(getClass().getResource("/org/goniolab/uc/bw.png")));
        bgColorButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
            }
        });
        angleUnitComboBox.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                angleUnitComboBoxActionPerformed(evt);
            }
        });
    }

    private void initLayout() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);
        GridBagConstraints gridBagConstraints;
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.ipady = 5;
        gridBagLayout.setConstraints(angleUnitLabel, gridBagConstraints);
        add(angleUnitLabel);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.ipady = 5;
        gridBagLayout.setConstraints(angleUnitComboBox, gridBagConstraints);
        add(angleUnitComboBox);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.ipady = 5;
        gridBagLayout.setConstraints(intervalLabel, gridBagConstraints);
        add(intervalLabel);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.ipady = 5;
        gridBagLayout.setConstraints(intervalComboBox, gridBagConstraints);
        add(intervalComboBox);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 5;
        gridBagLayout.setConstraints(orientationLabel, gridBagConstraints);
        add(orientationLabel);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.ipady = 5;
        gridBagLayout.setConstraints(orientationComboBox, gridBagConstraints);
        add(orientationComboBox);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.ipady = 5;
        gridBagLayout.setConstraints(backgroundColorLabel, gridBagConstraints);
        add(backgroundColorLabel);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.ipady = 5;
        gridBagLayout.setConstraints(bgColorButton, gridBagConstraints);
        add(bgColorButton);
    }

    private void initLexicons() {
        lexicon.setPair(orientationLabel, "Orientation");
        lexicon.setPair(angleUnitLabel, "AngleUnit");
        lexicon.setPair(intervalLabel, "Interval");
        lexicon.setPair(backgroundColorLabel, "BackgroundColor");
    }

    private void angleUnitComboBoxActionPerformed(ActionEvent evt) {
        String sFirst = "";
        String sLast = "";
        int currentStartIndex = intervalComboBox.getSelectedIndex();
        switch(angleUnitComboBox.getSelectedIndex()) {
            case 0:
                sFirst = "-180" + AConstantsHtml.SPACE + AConstantsHtml.ARRORW_R + AConstantsHtml.SPACE + "180";
                sLast = "0" + AConstantsHtml.SPACE + AConstantsHtml.ARRORW_R + AConstantsHtml.SPACE + "360";
                break;
            case 1:
                sFirst = "-200" + AConstantsHtml.SPACE + AConstantsHtml.ARRORW_R + AConstantsHtml.SPACE + "200";
                sLast = "0" + AConstantsHtml.SPACE + AConstantsHtml.ARRORW_R + AConstantsHtml.SPACE + "400";
                break;
            case 2:
                sFirst = "-" + AConstantsHtml.PI + AConstantsHtml.SPACE + AConstantsHtml.ARRORW_R + AConstantsHtml.SPACE + AConstantsHtml.PI;
                sLast = "0" + AConstantsHtml.SPACE + AConstantsHtml.ARRORW_R + AConstantsHtml.SPACE + "2" + AConstantsHtml.PI;
                break;
        }
        intervalComboBox.setModel(new DefaultComboBoxModel(new String[] { AConstantsHtml.BEG + sFirst, AConstantsHtml.BEG + sLast }));
        intervalComboBox.setSelectedIndex(currentStartIndex);
    }

    private JLabel backgroundColorLabel = new JLabel();

    private JLabel angleUnitLabel = new JLabel();

    private JLabel intervalLabel = new JLabel();

    private JLabel orientationLabel = new JLabel();

    private JComboBox angleUnitComboBox = new JComboBox();

    private JComboBox intervalComboBox = new JComboBox();

    private JComboBox orientationComboBox = new JComboBox();

    private JButton bgColorButton = new JButton();

    private ALexicon lexicon = new ALexicon(UnitCircleConstants.L10N_RESOURCE);
}
