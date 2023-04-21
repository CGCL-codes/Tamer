package it.southdown.avana.ui.util;

import it.southdown.avana.util.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.util.logging.*;

public abstract class TextOutputDialog extends OkDialog {

    public static final String LOGGER_NAME = TextOutputDialog.class.getCanonicalName();

    private static Logger logger = Logger.getLogger(LOGGER_NAME);

    protected JTextArea textArea;

    protected FileChooserButton saveButton;

    public TextOutputDialog(JFrame parent, String title) {
        super(parent, title);
        pack();
    }

    protected Container getMainPanel() {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        textArea = new JTextArea();
        textArea.setLineWrap(false);
        JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        int fontSize = textArea.getFont().getSize();
        Font f = new Font("Courier New", Font.PLAIN, fontSize);
        textArea.setFont(f);
        saveButton = new FileChooserButton("Save") {

            protected void prepareForDisplay() {
                this.setSelectedFile(getOutputFile());
            }
        };
        saveButton.addFileChooserListener(new FileChooserListener() {

            public void fileChosen(File chosenFile, FileFilter filter) {
                String content = textArea.getText();
                try {
                    FileUtilities.saveContent(chosenFile, content);
                } catch (IOException e) {
                    logger.severe("Error writing out result file: " + e.getMessage());
                }
            }
        });
        addButton(saveButton);
        textPanel.add(scrollPane, BorderLayout.CENTER);
        textPanel.setPreferredSize(new Dimension(600, 300));
        return textPanel;
    }

    public void refresh() {
        textArea.setText(getContent());
        textArea.setCaretPosition(0);
        saveButton.setEnabled(isSavingEnabled());
    }

    public abstract String getContent();

    public abstract File getOutputFile();

    public abstract boolean isSavingEnabled();
}
