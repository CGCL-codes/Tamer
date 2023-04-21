package com.rapidminer.operator;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.rapidminer.gui.RapidMinerGUI;
import com.rapidminer.gui.wizards.PreviewListener;
import com.rapidminer.operator.extraction.ExtractionException;
import com.rapidminer.operator.extraction.TextExtractionWrapper;
import com.rapidminer.operator.extraction.segmenter.DocumentSegmenter;
import com.rapidminer.operator.extraction.segmenter.DocumentSegmenterClass;
import com.rapidminer.parameter.UndefinedParameterError;

public class SegmenterPreviewer extends JDialog implements ListSelectionListener {

    private static final long serialVersionUID = 3938814199445624978L;

    public static final String ERROR_MESSAGE = "You must specify a directory containing at least one text in \"texts\"";

    private JTextArea textAreaOriginal;

    private JTextArea textAreaSegments;

    private JList selectorList = null;

    private String fileType = null;

    private DocumentSegmenter segmenter = null;

    public SegmenterPreviewer(PreviewListener previewListener) {
        super(RapidMinerGUI.getMainFrame(), "Segmenter Previewer", true);
        textAreaOriginal = new JTextArea();
        textAreaOriginal.setWrapStyleWord(true);
        textAreaOriginal.setLineWrap(true);
        textAreaOriginal.setMargin(new Insets(10, 10, 10, 10));
        textAreaSegments = new JTextArea();
        textAreaSegments.setWrapStyleWord(true);
        textAreaSegments.setLineWrap(true);
        textAreaSegments.setMargin(new Insets(10, 10, 10, 10));
        try {
            fileType = (String) previewListener.getParameters().getParameter("content_type");
        } catch (UndefinedParameterError e1) {
            fileType = null;
        }
        try {
            segmenter = DocumentSegmenterClass.getSegmenterFromParameters(previewListener.getParameters());
        } catch (UserError e1) {
            segmenter = null;
            textAreaOriginal.setText("The query expression is not well defined.");
        }
        File baseDir = null;
        try {
            baseDir = previewListener.getProcess().resolveFileName((String) previewListener.getParameters().getParameter("texts"));
        } catch (UndefinedParameterError e) {
        }
        File[] files = baseDir.listFiles(new FileFilter() {

            public boolean accept(File f) {
                return f.isFile() && !f.isHidden();
            }
        });
        if ((files != null) && (files.length > 0)) selectorList = new JList(files); else selectorList = new JList();
        selectorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectorList.addListSelectionListener(this);
        JButton okButton = new JButton("Close");
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }
        });
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.gridx = 0;
        gbcLabel.gridy = GridBagConstraints.RELATIVE;
        gbcLabel.fill = GridBagConstraints.HORIZONTAL;
        gbcLabel.weightx = 1;
        gbcLabel.weighty = 0;
        gbcLabel.insets = new Insets(10, 10, 10, 10);
        JLabel label = new JLabel("Select a document");
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        getContentPane().add(label, gbcLabel);
        getContentPane().add(new JScrollPane(selectorList), gbc);
        label = new JLabel("Original text");
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        getContentPane().add(label, gbcLabel);
        getContentPane().add(new JScrollPane(textAreaOriginal), gbc);
        label = new JLabel("Extracted segments");
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        getContentPane().add(label, gbcLabel);
        getContentPane().add(new JScrollPane(textAreaSegments), gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.insets = new Insets(7, 7, 7, 7);
        getContentPane().add(okButton, gbc);
        setSize(Math.max(640, (int) (0.66d * getOwner().getWidth())), Math.max(480, (int) (0.66d * getOwner().getHeight())));
        setLocationRelativeTo(getOwner());
        if ((files != null) && (files.length > 0)) {
            selectorList.setSelectedIndex(0);
        } else {
            textAreaOriginal.setText(ERROR_MESSAGE);
        }
    }

    private void showSegments(File f) {
        StringBuffer originalText = new StringBuffer();
        try {
            BufferedReader in = new BufferedReader(new FileReader(f));
            String buf = null;
            while ((buf = in.readLine()) != null) {
                originalText.append(buf);
                originalText.append('\n');
            }
            textAreaOriginal.setText(originalText.toString());
            textAreaOriginal.setCaretPosition(0);
            int type;
            if (fileType != null) type = TextExtractionWrapper.determineType(fileType); else type = TextExtractionWrapper.determineType(f);
            StringBuffer segmentsText = new StringBuffer();
            Iterator<String> segments = segmenter.getSegments(f, type);
            while (segments.hasNext()) {
                segmentsText.append("\n-----------------\n");
                segmentsText.append(segments.next());
            }
            textAreaSegments.setText(segmentsText.toString());
            textAreaSegments.setCaretPosition(0);
        } catch (FileNotFoundException e) {
            textAreaOriginal.setText("Could not load text from " + f);
        } catch (IOException e) {
            textAreaOriginal.setText("Could not load text from " + f);
        } catch (ExtractionException e) {
            textAreaSegments.setText("Could not extract any segments from original: " + e);
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        File f = (File) selectorList.getModel().getElementAt(selectorList.getSelectedIndex());
        if (f != null) {
            showSegments(f);
        } else {
            textAreaOriginal.setText(ERROR_MESSAGE);
        }
    }
}
