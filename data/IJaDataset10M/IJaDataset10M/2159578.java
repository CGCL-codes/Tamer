package net.sourceforge.simplemerge.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import net.sourceforge.simplemerge.Difference;
import net.sourceforge.simplemerge.Range;

public class FileDiffView extends JPanel {

    private static final String ADD_IMAGE = "addImage";

    private static final String DELETE_IMAGE = "deleteImage";

    private static final String CHANGE_IMAGE = "changeImage";

    private static final String ADD_STYLE = "addedLine";

    private static final String CHANGE_STYLE = "changedLine";

    private static final String DELETE_STYLE = "deletedLine";

    private static final String BUTTON_STYLE = "buttonAtEndOfLine";

    private JTextPane area = null;

    private JTextField fileField = null;

    private DefaultListModel model = new DefaultListModel();

    private JButton fileChooserBtn = null;

    private File selectedFile = null;

    private MarkLine markLine = null;

    public FileDiffView() {
        super();
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        fileField = new JTextField();
        fileField.setPreferredSize(new Dimension(300, 20));
        JPanel up = new JPanel(new FlowLayout());
        up.add(fileField);
        fileChooserBtn = new JButton("...");
        fileChooserBtn.addActionListener(new FileChooserAction());
        up.add(fileChooserBtn);
        add(up, BorderLayout.NORTH);
        area = new JTextPane();
        area.setDocument(new DefaultStyledDocument());
        area.setHighlighter(new DefaultHighlighter());
        markLine = new MarkLine(area);
        JScrollPane scroll = new JScrollPane();
        scroll.setRowHeaderView(markLine);
        scroll.setViewportView(area);
        add(scroll, BorderLayout.CENTER);
        addStyles();
    }

    private void addStyles() {
        StyledDocument doc = area.getStyledDocument();
        Style style = doc.addStyle(ADD_STYLE, null);
        StyleConstants.setBackground(style, Color.GREEN);
        StyleConstants.setItalic(style, true);
        style = doc.addStyle(CHANGE_STYLE, null);
        StyleConstants.setUnderline(style, true);
        StyleConstants.setBackground(style, Color.LIGHT_GRAY);
        style = doc.addStyle(DELETE_STYLE, null);
        StyleConstants.setBackground(style, Color.RED);
        style = doc.addStyle(BUTTON_STYLE, null);
        JButton btn = new JButton(">>");
        StyleConstants.setComponent(style, btn);
        style = doc.addStyle(ADD_IMAGE, null);
        ImageIcon icon = new ImageIcon(getClass().getResource("/add.png"));
        StyleConstants.setIcon(style, icon);
        style = doc.addStyle(DELETE_IMAGE, null);
        icon = new ImageIcon(getClass().getResource("/delete.png"));
        StyleConstants.setIcon(style, icon);
        style = doc.addStyle(CHANGE_IMAGE, null);
        icon = new ImageIcon(getClass().getResource("/change.png"));
        StyleConstants.setIcon(style, icon);
    }

    private class FileChooserAction implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser("/Users/mic/Projects/simplemerge/simplemerge");
            int ret = fileChooser.showOpenDialog(null);
            if (ret == JFileChooser.APPROVE_OPTION) {
                File openFile = fileChooser.getSelectedFile();
                setFile(openFile);
            }
        }
    }

    public void setFile(File file) {
        setSelectedFile(file);
        fileField.setText(file.getAbsolutePath());
        area.setText(readFile(file));
    }

    /**
     * Read File into String. String is empty if an exception occurs
     * 
     * @param file
     *            java.io.File
     * @return Content of File as String
     */
    private String readFile(File file) {
        StringBuffer sb = new StringBuffer();
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            int c;
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
            reader.close();
            reader = new FileReader(file);
            LineNumberReader lnr = new LineNumberReader(reader);
            String line;
            while ((line = lnr.readLine()) != null) {
                model.addElement(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public void highlightLines(Range range, int method) {
        Element section = area.getDocument().getDefaultRootElement();
        Element startline = section.getElement(range.getStartLine() - 1);
        Element endline = section.getElement(range.getEndLine() - 1);
        int startOffset = startline.getStartOffset();
        int endOffset = endline.getEndOffset();
        StyledDocument doc = area.getStyledDocument();
        Style style = null;
        ImageIcon img = null;
        switch(method) {
            case Difference.ADD:
                style = doc.getStyle(ADD_STYLE);
                img = new ImageIcon(getClass().getResource("/add.png"));
                break;
            case Difference.CHANGE:
                style = doc.getStyle(CHANGE_STYLE);
                img = new ImageIcon(getClass().getResource("/change.png"));
                break;
            case Difference.DELETE:
                style = doc.getStyle(DELETE_STYLE);
                img = new ImageIcon(getClass().getResource("/delete.png"));
                break;
            default:
                break;
        }
        doc.setCharacterAttributes(startOffset, endOffset - startOffset, style, false);
        for (int i = range.getStartLine(); i < range.getEndLine() + 1; i++) {
            markLine.addMarkToLine(new Integer(i), img.getImage());
        }
    }

    /**
     * Getter for File
     * 
     * @return java.io.File
     */
    public File getSelectedFile() {
        return selectedFile;
    }

    /**
     * Setter for File
     * 
     * @param selectedFile
     */
    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }
}
