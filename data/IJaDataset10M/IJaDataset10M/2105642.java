package com.limegroup.gnutella.gui.xml.editor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import com.limegroup.gnutella.FileDesc;
import com.limegroup.gnutella.gui.LimeTextField;
import com.limegroup.gnutella.xml.LimeXMLDocument;
import com.limegroup.gnutella.xml.LimeXMLNames;
import com.limegroup.gnutella.xml.LimeXMLSchema;

public class VideoEditor extends MetaEditorPanel {

    private JLabel titleLabel;

    private LimeTextField titleTextField;

    private JLabel typeLabel;

    private JComboBox typeComboBox;

    private JCheckBox typeCheckBox;

    private JLabel yearLabel;

    private LimeTextField yearTextField;

    private JCheckBox yearCheckBox;

    private JLabel ratingLabel;

    private JComboBox ratingComboBox;

    private JCheckBox ratingCheckBox;

    private JLabel directorLabel;

    private LimeTextField directorTextField;

    private JCheckBox directorCheckBox;

    private JLabel studioLabel;

    private LimeTextField studioTextField;

    private JCheckBox studioCheckBox;

    private JLabel commentsLabel;

    private JScrollPane commentsScrollPane;

    private JTextArea commentsTextArea;

    private JCheckBox commentsCheckBox;

    private JLabel languageLabel;

    private LimeTextField languageTextField;

    private JCheckBox languageCheckBox;

    private JLabel starsLabel;

    private LimeTextField starsTextField;

    private JCheckBox starsCheckBox;

    private JLabel producerLabel;

    private LimeTextField producerTextField;

    private JCheckBox producerCheckBox;

    private JLabel subtitlesLabel;

    private LimeTextField subtitlesTextField;

    private JCheckBox subtitlesCheckBox;

    private JLabel licenseLabel;

    private LimeTextField licenseTextField;

    private JCheckBox licenseCheckBox;

    private String title = null;

    private boolean titleEdited = false;

    public VideoEditor(FileDesc[] fds, LimeXMLSchema schema, LimeXMLDocument doc) {
        super(fds, schema, doc);
        super.setName(MetaEditorUtil.getStringResource(LimeXMLNames.VIDEO));
        init();
        initFields();
        setMultiEdit(fds.length > 1);
        if (licenseTextField.getText().equals("")) {
            licenseTextField.setVisible(false);
            licenseLabel.setVisible(false);
            licenseCheckBox.setVisible(false);
        }
        titleTextField.addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent evt) {
                titleEdited = true;
            }
        });
    }

    public void setMultiEdit(boolean multiEdit) {
        super.setMultiEdit(multiEdit);
        titleLabel.setVisible(!multiEdit);
        titleTextField.setVisible(!multiEdit);
    }

    public boolean hasChanged() {
        return (titleEdited || super.hasChanged());
    }

    protected void prepareSave() {
        if (title != null) {
            String text = titleTextField.getText().trim();
            if (text.equals("")) {
                titleTextField.setText(title);
            }
        }
    }

    protected void initFields() {
        addComponent(LimeXMLNames.VIDEO_TITLE, titleTextField);
        addComponent(LimeXMLNames.VIDEO_COMMENTS, commentsCheckBox, commentsTextArea);
        addComponent(LimeXMLNames.VIDEO_YEAR, yearCheckBox, yearTextField);
        addComponent(LimeXMLNames.VIDEO_TYPE, typeCheckBox, typeComboBox);
        addComponent(LimeXMLNames.VIDEO_DIRECTOR, directorCheckBox, directorTextField);
        addComponent(LimeXMLNames.VIDEO_STUDIO, studioCheckBox, studioTextField);
        addComponent(LimeXMLNames.VIDEO_RATING, ratingCheckBox, ratingComboBox);
        addComponent(LimeXMLNames.VIDEO_LICENSE, licenseCheckBox, licenseTextField);
        addComponent(LimeXMLNames.VIDEO_STARS, starsCheckBox, starsTextField);
        addComponent(LimeXMLNames.VIDEO_PRODUCER, producerCheckBox, producerTextField);
        addComponent(LimeXMLNames.VIDEO_LANGUAGE, languageCheckBox, languageTextField);
        addComponent(LimeXMLNames.VIDEO_SUBTITLES, subtitlesCheckBox, subtitlesTextField);
        super.initFields();
    }

    private void init() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 3, 0, 2);
        gbc.gridx = 1;
        gbc.gridy = 0;
        titleLabel = new JLabel(MetaEditorUtil.getStringResource(LimeXMLNames.VIDEO_TITLE));
        add(titleLabel, gbc);
        gbc.insets = new Insets(0, 3, 2, 2);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        titleTextField = new LimeTextField(35);
        add(titleTextField, gbc);
        gbc.insets = new Insets(5, 3, 0, 2);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        directorLabel = new JLabel(MetaEditorUtil.getStringResource(LimeXMLNames.VIDEO_DIRECTOR));
        add(directorLabel, gbc);
        gbc.insets = new Insets(0, 3, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        directorCheckBox = new JCheckBox();
        add(directorCheckBox, gbc);
        gbc.insets = new Insets(0, 3, 2, 2);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        directorTextField = new LimeTextField(24);
        add(directorTextField, gbc);
        gbc.insets = new Insets(5, 3, 0, 2);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        starsLabel = new JLabel(MetaEditorUtil.getStringResource(LimeXMLNames.VIDEO_STARS));
        add(starsLabel, gbc);
        gbc.insets = new Insets(0, 3, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        starsCheckBox = new JCheckBox();
        add(starsCheckBox, gbc);
        gbc.insets = new Insets(0, 3, 2, 2);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        starsTextField = new LimeTextField(24);
        add(starsTextField, gbc);
        gbc.insets = new Insets(5, 3, 0, 2);
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        producerLabel = new JLabel(MetaEditorUtil.getStringResource(LimeXMLNames.VIDEO_PRODUCER));
        add(producerLabel, gbc);
        gbc.insets = new Insets(0, 3, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        producerCheckBox = new JCheckBox();
        add(producerCheckBox, gbc);
        gbc.insets = new Insets(0, 3, 2, 2);
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        producerTextField = new LimeTextField(24);
        add(producerTextField, gbc);
        gbc.insets = new Insets(5, 3, 0, 2);
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        studioLabel = new JLabel(MetaEditorUtil.getStringResource(LimeXMLNames.VIDEO_STUDIO));
        add(studioLabel, gbc);
        gbc.insets = new Insets(0, 3, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        studioCheckBox = new JCheckBox();
        add(studioCheckBox, gbc);
        gbc.insets = new Insets(0, 3, 2, 2);
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        studioTextField = new LimeTextField(24);
        add(studioTextField, gbc);
        gbc.insets = new Insets(5, 3, 0, 2);
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        commentsLabel = new JLabel(MetaEditorUtil.getStringResource(LimeXMLNames.VIDEO_COMMENTS));
        add(commentsLabel, gbc);
        gbc.insets = new Insets(0, 3, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        commentsCheckBox = new JCheckBox();
        add(commentsCheckBox, gbc);
        gbc.insets = new Insets(0, 3, 2, 2);
        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        commentsTextArea = new JTextArea();
        commentsTextArea.setLineWrap(true);
        commentsTextArea.setWrapStyleWord(true);
        commentsScrollPane = new JScrollPane(commentsTextArea);
        commentsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        commentsScrollPane.setPreferredSize(new Dimension(22, 50));
        add(commentsScrollPane, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(5, 3, 0, 2);
        gbc.gridx = 1;
        gbc.gridy = 12;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        subtitlesLabel = new JLabel(MetaEditorUtil.getStringResource(LimeXMLNames.VIDEO_SUBTITLES));
        add(subtitlesLabel, gbc);
        gbc.insets = new Insets(0, 3, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        subtitlesCheckBox = new JCheckBox();
        add(subtitlesCheckBox, gbc);
        gbc.insets = new Insets(0, 3, 2, 2);
        gbc.gridx = 1;
        gbc.gridy = 13;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        subtitlesTextField = new LimeTextField(24);
        add(subtitlesTextField, gbc);
        gbc.insets = new Insets(5, 3, 0, 2);
        gbc.gridx = 1;
        gbc.gridy = 14;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        licenseLabel = new JLabel(MetaEditorUtil.getStringResource(LimeXMLNames.VIDEO_LICENSE));
        add(licenseLabel, gbc);
        gbc.insets = new Insets(0, 3, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = 15;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        licenseCheckBox = new JCheckBox();
        add(licenseCheckBox, gbc);
        gbc.insets = new Insets(0, 3, 2, 2);
        gbc.gridx = 1;
        gbc.gridy = 15;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        licenseTextField = new LimeTextField(24);
        licenseTextField.setEnabled(false);
        add(licenseTextField, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 3, 0, 2);
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        yearLabel = new JLabel(MetaEditorUtil.getStringResource(LimeXMLNames.VIDEO_YEAR));
        add(yearLabel, gbc);
        gbc.insets = new Insets(0, 3, 0, 0);
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        yearCheckBox = new JCheckBox();
        add(yearCheckBox, gbc);
        gbc.insets = new Insets(0, 3, 2, 2);
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        yearTextField = new LimeTextField(6);
        add(yearTextField, gbc);
        gbc.insets = new Insets(5, 3, 0, 2);
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        ratingLabel = new JLabel(MetaEditorUtil.getStringResource(LimeXMLNames.VIDEO_RATING));
        add(ratingLabel, gbc);
        gbc.insets = new Insets(0, 3, 0, 0);
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        ratingCheckBox = new JCheckBox();
        add(ratingCheckBox, gbc);
        gbc.insets = new Insets(0, 3, 2, 2);
        gbc.gridx = 3;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        ratingComboBox = new JComboBox();
        add(ratingComboBox, gbc);
        gbc.insets = new Insets(5, 3, 0, 2);
        gbc.gridx = 3;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        languageLabel = new JLabel(MetaEditorUtil.getStringResource(LimeXMLNames.VIDEO_LANGUAGE));
        add(languageLabel, gbc);
        gbc.insets = new Insets(0, 3, 0, 0);
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        languageCheckBox = new JCheckBox();
        add(languageCheckBox, gbc);
        gbc.insets = new Insets(0, 3, 2, 2);
        gbc.gridx = 3;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        languageTextField = new LimeTextField(6);
        add(languageTextField, gbc);
        gbc.insets = new Insets(5, 3, 0, 2);
        gbc.gridx = 3;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        typeLabel = new JLabel(MetaEditorUtil.getStringResource(LimeXMLNames.VIDEO_TYPE));
        add(typeLabel, gbc);
        gbc.insets = new Insets(0, 3, 0, 0);
        gbc.gridx = 2;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        typeCheckBox = new JCheckBox();
        add(typeCheckBox, gbc);
        gbc.insets = new Insets(0, 3, 2, 2);
        gbc.gridx = 3;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        typeComboBox = new JComboBox();
        add(typeComboBox, gbc);
    }
}
