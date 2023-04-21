package wingset;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.Icon;
import org.wings.*;
import org.wings.border.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision: 1759 $
 */
public class FileChooserExample extends WingSetPane {

    static final String TEMPLATE = "/wingset/templates/FileChooserExample.thtml";

    static final Color WARN_COLOR = new Color(255, 255, 127);

    /**
     * the file chooser that gets the files.
     */
    SFileChooser chooser;

    /**
     * three cards for different content to be previewd: images, text and
     * unknown.
     */
    SCardLayout contentSwitcher;

    /**
     * label that shows image content.
     */
    SLabel iconLabel;

    /**
     * text area to show text content.
     */
    STextArea textArea;

    /**
     * form, that contains the text-area
     */
    SForm textForm;

    /**
     * label for unknown content.
     */
    SLabel unknownLabel;

    /**
     * remember the previous file to remove it.
     */
    File previousFile;

    public SComponent createExample() {
        SPanel p = new SPanel();
        try {
            java.net.URL templateURL = getClass().getResource(TEMPLATE);
            p.setLayout(new STemplateLayout(templateURL));
        } catch (Exception e) {
        }
        p.add(createControlForm(), "controlForm");
        p.add(createUpload(), "uploadForm");
        p.add(createPreview(), "previewArea");
        return p;
    }

    private SForm createControlForm() {
        SForm controlForm = new SForm();
        controlForm.add(new SLabel("influence maximum accepted Content Length (in kB): "));
        Object[] values = { new Integer(1), new Integer(2), new Integer(4), new Integer(8), new Integer(16), new Integer(32), new Integer(64) };
        final SComboBox comboBox = new SComboBox(values);
        comboBox.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                getSession().setMaxContentLength(((Integer) comboBox.getSelectedItem()).intValue());
            }
        });
        comboBox.setSelectedItem(new Integer(getSession().getMaxContentLength()));
        controlForm.add(comboBox);
        SButton submit = new SButton("OK");
        controlForm.add(submit);
        controlForm.setBorder(new SEmptyBorder(10, 10, 0, 0));
        return controlForm;
    }

    protected String getText(File f) {
        try {
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new FileReader(f));
            String line = reader.readLine();
            while (line != null) {
                buffer.append(line).append("\n");
                line = reader.readLine();
            }
            return buffer.toString();
        } catch (Exception ex) {
            return "got exception " + ex.getMessage();
        }
    }

    protected void adaptPreview() {
        if (previousFile != null) {
            previousFile.delete();
            previousFile = null;
        }
        try {
            if (chooser.getFileType().startsWith("text/")) {
                textArea.setText(getText(chooser.getFile()));
                contentSwitcher.show(textForm);
            } else if (chooser.getFileType().startsWith("image/")) {
                iconLabel.setIcon(new SFileIcon(chooser.getFile(), null, chooser.getFileType()));
                contentSwitcher.show(iconLabel);
            } else {
                contentSwitcher.show(unknownLabel);
            }
            previousFile = chooser.getFile();
        } catch (Exception ex) {
            contentSwitcher.show(unknownLabel);
        }
    }

    protected SComponent createPreview() {
        SPanel p = new SPanel(new SFlowDownLayout());
        p.setVerticalAlignment(TOP);
        SLabel previewLabel = new SLabel("Preview");
        previewLabel.setBorder(new SEmptyBorder(0, 20, 0, 0));
        p.add(previewLabel);
        contentSwitcher = new SCardLayout();
        SPanel contentPane = new SPanel(contentSwitcher);
        iconLabel = new SLabel();
        textForm = new SForm();
        textArea = new STextArea();
        textArea.setColumns(50);
        textArea.setRows(20);
        textArea.setEditable(false);
        unknownLabel = new SLabel("Unknown Content");
        unknownLabel.setBackground(WARN_COLOR);
        contentPane.add(iconLabel, "ICON");
        textForm.add(textArea);
        contentPane.add(textForm, "TEXT");
        contentPane.add(unknownLabel, "UNKNOWN");
        contentSwitcher.show(unknownLabel);
        contentPane.setBorder(new SEmptyBorder(10, 20, 0, 0));
        p.add(contentPane);
        return p;
    }

    protected SComponent createUpload() {
        SForm form = new SForm(new SFlowDownLayout());
        form.setBorder(new SEmptyBorder(10, 5, 0, 0));
        form.setEncodingType("multipart/form-data");
        chooser = new SFileChooser();
        form.add(chooser);
        SButton submit = new SButton("upload");
        submit.setVerticalAlignment(RIGHT);
        form.add(submit);
        SPanel p = new SPanel(new SGridLayout(2));
        p.setBorder(new SEmptyBorder(10, 5, 0, 0));
        p.add(new SLabel("message:"));
        final SLabel message = new SLabel("");
        p.add(message);
        p.add(new SLabel("filename:"));
        final SLabel filename = new SLabel("");
        p.add(filename);
        p.add(new SLabel("fileid:"));
        final SLabel fileid = new SLabel("");
        p.add(fileid);
        p.add(new SLabel("filetype:"));
        final SLabel filetype = new SLabel("");
        p.add(filetype);
        p.add(new SLabel("size:"));
        final SLabel size = new SLabel("");
        p.add(size);
        form.add(p);
        form.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    if (chooser.getFile() != null) {
                        message.setText("OK");
                        message.setBackground(null);
                        filename.setText(chooser.getFileName());
                        fileid.setText(chooser.getFileId());
                        filetype.setText(chooser.getFileType());
                        size.setText("" + chooser.getFile().length());
                        adaptPreview();
                    } else {
                        message.setText("No file chosen");
                        message.setBackground(WARN_COLOR);
                    }
                } catch (IOException ex) {
                    message.setText(ex.getMessage());
                    message.setBackground(WARN_COLOR);
                    filename.setText("");
                    fileid.setText("");
                    filetype.setText("");
                    size.setText("");
                    contentSwitcher.show(unknownLabel);
                }
                chooser.reset();
            }
        });
        form.setVerticalAlignment(TOP);
        return form;
    }
}
