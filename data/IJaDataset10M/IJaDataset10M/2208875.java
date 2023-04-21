package org.openconcerto.openoffice;

import org.openconcerto.utils.text.SimpleDocumentListener;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;

public class GrepFrame extends javax.swing.JFrame {

    private JTextField patternTF;

    private JButton fileBtn;

    private JPanel emptyPanel;

    private JButton grepBtn;

    private JLabel fileTF;

    private JLabel fileL;

    private JLabel patternL;

    private final Preferences pref;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                GrepFrame inst = new GrepFrame();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public GrepFrame() {
        this(null, null);
    }

    public GrepFrame(final String pattern, final String path) {
        super();
        initGUI();
        this.pref = Preferences.userRoot().node("/ilm/openoffice/grep");
        this.patternTF.setText(pattern == null ? this.pref.get("pattern", "pattern") : pattern);
        this.fileTF.setText(path == null ? this.pref.get("path", ".") : path);
    }

    private void initGUI() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        GridLayout thisLayout = new GridLayout(3, 3);
        thisLayout.setColumns(3);
        thisLayout.setHgap(5);
        thisLayout.setVgap(5);
        thisLayout.setRows(3);
        getContentPane().setLayout(thisLayout);
        this.setPreferredSize(new java.awt.Dimension(800, 254));
        {
            this.patternTF = new JTextField();
            this.patternTF.setText("regexp");
            this.patternTF.setBounds(162, 12, 218, 21);
            this.patternTF.getDocument().addDocumentListener(new SimpleDocumentListener() {

                @Override
                public void update(DocumentEvent e) {
                    GrepFrame.this.pref.put("pattern", GrepFrame.this.patternTF.getText());
                    prefChanged();
                }
            });
        }
        {
            this.patternL = new JLabel();
            getContentPane().add(this.patternL);
            this.patternL.setText("Pattern");
            this.patternL.setBounds(30, 15, 84, 14);
            getContentPane().add(this.patternTF);
        }
        {
            this.emptyPanel = new JPanel();
            getContentPane().add(this.emptyPanel);
        }
        {
            this.fileL = new JLabel();
            this.fileL.setText("File");
            this.fileL.setBounds(30, 41, 23, 14);
            getContentPane().add(this.fileL);
        }
        {
            this.fileTF = new JLabel();
            this.fileTF.setBounds(162, 41, 217, 18);
            getContentPane().add(this.fileTF);
        }
        {
            this.fileBtn = new JButton(new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    final JFileChooser fc = new JFileChooser();
                    fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    final File file = new File(GrepFrame.this.fileTF.getText());
                    if (file.exists()) fc.setSelectedFile(file);
                    if (fc.showOpenDialog(GrepFrame.this) == JFileChooser.APPROVE_OPTION) {
                        GrepFrame.this.fileTF.setText(fc.getSelectedFile().getPath());
                        GrepFrame.this.pref.put("path", fc.getSelectedFile().getPath());
                        prefChanged();
                    }
                }
            });
            this.fileBtn.setText("Choisir");
            this.fileBtn.setBounds(392, 38, 120, 21);
            getContentPane().add(this.fileBtn);
        }
        {
            this.grepBtn = new JButton(new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    new Grep(GrepFrame.this.patternTF.getText()).grep(new File(GrepFrame.this.fileTF.getText()));
                }
            });
            this.grepBtn.setText("Grep");
            this.grepBtn.setBounds(30, 79, 120, 21);
            getContentPane().add(this.grepBtn);
        }
        pack();
        this.setSize(1000, 254);
    }

    protected void prefChanged() {
        try {
            this.pref.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }
}
