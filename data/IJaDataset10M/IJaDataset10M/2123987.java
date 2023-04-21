package com.t_oster.notenschrank.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import com.t_oster.notenschrank.data.Archive;
import com.t_oster.notenschrank.data.OCR;
import com.t_oster.notenschrank.data.OCR.OCRResult;
import com.t_oster.notenschrank.data.SettingsManager;
import com.t_oster.notenschrank.data.Sheet;
import com.t_oster.notenschrank.data.Song;
import com.t_oster.notenschrank.data.Voice;

public class SortingDialog extends JDialog implements ActionListener {

    public enum Layout {

        VERTICAL, HORIZONTAL
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = -2699371667100327736L;

    private PreviewPanel previewpanel;

    private PreviewPanel current;

    private JPanel mainPanel;

    private List<SelectSongBox> cbSong;

    private List<SelectVoiceBox> cbVoice;

    private JButton bOk;

    private JButton bAddPage;

    private JButton bDeletePage;

    private JButton bAddVoice;

    private JButton bDeleteVoice;

    private Box songPanel;

    private Box voicePanel;

    private JButton bAddSong;

    private JButton bDeleteSong;

    private JButton bRotatePage;

    private JButton bGuessSong;

    private JButton bGuessVoice;

    private List<Sheet> stackSheets;

    public SortingDialog(JFrame parent, Layout layout) {
        super(parent, "Notenschrank " + SettingsManager.getInstance().getProgramVersion());
        this.setPreferredSize(new Dimension(800, 600));
        this.previewpanel = new PreviewPanel(this);
        this.current = new PreviewPanel(this);
        SelectSongBox cbSong = new SelectSongBox(true, true);
        cbSong.setMaximumSize(new Dimension(500, 35));
        this.cbSong = new LinkedList<SelectSongBox>();
        this.cbSong.add(cbSong);
        this.bAddSong = new JButton("+");
        this.bAddSong.addActionListener(this);
        this.bDeleteSong = new JButton("-");
        this.bDeleteSong.setEnabled(false);
        this.bDeleteSong.addActionListener(this);
        this.bAddVoice = new JButton("+");
        this.bAddVoice.addActionListener(this);
        this.bDeleteVoice = new JButton("-");
        this.bDeleteVoice.setEnabled(false);
        this.bDeleteVoice.addActionListener(this);
        SelectVoiceBox cbVoice = new SelectVoiceBox();
        cbVoice.setMaximumSize(new Dimension(500, 35));
        this.cbVoice = new LinkedList<SelectVoiceBox>();
        this.cbVoice.add(cbVoice);
        if (OCR.isAvailable()) {
            this.bGuessSong = new JButton("raten");
            this.bGuessSong.addActionListener(this);
            this.bGuessVoice = new JButton("raten");
            this.bGuessVoice.addActionListener(this);
        }
        this.bOk = new JButton("Speichern");
        this.bOk.addActionListener(this);
        this.bAddPage = new JButton("nächste Seite gehört dazu");
        this.bAddPage.addActionListener(this);
        this.bRotatePage = new JButton("Seite drehen");
        this.bRotatePage.addActionListener(this);
        this.bDeletePage = new JButton("Seite löschen");
        this.bDeletePage.addActionListener(this);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        bOk.setToolTipText("Speichert das aktuelle Blatt im Archiv unter dem angegeben Namen und der angegebenen Stimme");
        bAddPage.setToolTipText("Fügt das nächste Blatt (linke Seite) zum aktuellen Blatt (rechte Seite) hinzu. Sinnvoll bei mehrseitigen Stücken");
        bRotatePage.setToolTipText("Dreht das aktuelle Blatt (Sinnvoll wenn falschherum eingescannt)");
        cbVoice.setToolTipText("Stimme unter der das aktuelle Blatt archiviert werden soll (zB. 1. Klarinette in Bb)");
        cbSong.setToolTipText("Name unter dem das aktuelle Blatt archiviert werden soll. (zB. Martini)");
        previewpanel.setToolTipText("Blatt welches als nächstes archiviert wird (Nur angezeigt damit man erkennt ob das aktuelle Blatt mehrseitig ist)");
        current.setToolTipText("Zeigt die erste Seite des aktuellen Stücks. Zoom mit linker Maustaste in eine der 4 Ecken. Herauszoomen mit rechter Maustaste. Scrollen mit Scrollrad.");
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
        if (layout == Layout.HORIZONTAL) {
            Box b = Box.createHorizontalBox();
            b.add(Box.createHorizontalGlue());
            b.add(new JLabel("nächstes Blatt"));
            b.add(Box.createHorizontalGlue());
            b.add(new JLabel("aktuelles Blatt"));
            b.add(Box.createHorizontalGlue());
            this.mainPanel.add(b);
            JPanel previewContainer = new JPanel();
            previewContainer.setLayout(new GridLayout(1, 2));
            previewContainer.add(this.previewpanel);
            previewContainer.add(this.current);
            this.mainPanel.add(previewContainer);
        } else if (layout == Layout.VERTICAL) {
            mainPanel.add(new JLabel("aktuelles Blatt"));
            mainPanel.add(current);
            mainPanel.add(new JLabel("nächstes Blatt"));
            mainPanel.add(this.previewpanel);
        }
        Box b = Box.createHorizontalBox();
        b.add(Box.createHorizontalGlue());
        b.add(new JLabel("Stück:     "));
        songPanel = Box.createVerticalBox();
        songPanel.add(cbSong);
        b.add(songPanel);
        b.add(bAddSong);
        b.add(bDeleteSong);
        if (bGuessSong != null) {
            b.add(bGuessSong);
        }
        b.add(Box.createHorizontalGlue());
        this.mainPanel.add(b);
        b = Box.createHorizontalBox();
        b.add(Box.createHorizontalGlue());
        b.add(new JLabel("Stimme:"));
        voicePanel = Box.createVerticalBox();
        voicePanel.add(cbVoice);
        b.add(voicePanel);
        b.add(bAddVoice);
        b.add(bDeleteVoice);
        if (bGuessVoice != null) {
            b.add(bGuessVoice);
        }
        b.add(Box.createHorizontalGlue());
        this.mainPanel.add(b);
        Box box = Box.createHorizontalBox();
        box.add(bAddPage);
        box.add(bRotatePage);
        box.add(bDeletePage);
        box.add(bOk);
        cbSong.requestFocusInWindow();
        this.mainPanel.add(box);
        this.setContentPane(mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public void showDialog() {
        try {
            ProgressWindow pw = new ProgressWindow("Bitte warten", "Lese Daten", 0, 0);
            pw.setIntermediate(true);
            this.stackSheets = Archive.getInstance().getUnsortedSheets();
            pw.close();
            while (stackSheets.size() == 0) {
                if (JOptionPane.showConfirmDialog(null, "Keine Noten zum sortieren vorhanden\nBitte scannen Sie zuerst welche ein.\n" + "Die Noten müssen im PDF-Format vorliegen.\n\n" + "Möchten Sie ein anderes Verzeichnis angeben?", "Keine PDFs gefunden.", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                    this.dispose();
                    return;
                } else {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setCurrentDirectory(new java.io.File("."));
                    chooser.setDialogTitle("Bitte wählen Sie den Ordner mit den gescannten Daten aus");
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    chooser.setAcceptAllFileFilterUsed(false);
                    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                        SettingsManager.getInstance().setStackPath(chooser.getSelectedFile().getAbsolutePath());
                        this.stackSheets = Archive.getInstance().getUnsortedSheets();
                    } else {
                        this.dispose();
                        return;
                    }
                }
            }
            Sheet cur = stackSheets.remove(0);
            this.current.showSheet(cur);
            if (stackSheets.size() > 0) {
                Sheet nxt = stackSheets.remove(0);
                this.previewpanel.showSheet(nxt);
            } else {
                bAddPage.setEnabled(false);
            }
            this.setModal(true);
            this.setVisible(true);
        } catch (Exception e) {
        }
    }

    private void rotateClicked() {
        try {
            Sheet s = this.current.getSheet();
            s.rotateAllPages(1);
            this.current.refresh();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Fehler beim rotieren.", "Fehler", JOptionPane.OK_OPTION);
        }
    }

    private void addClicked() {
        try {
            Sheet current = this.current.getSheet();
            Sheet preview = this.previewpanel.getSheet();
            if (preview != null) {
                current.addPage(this.previewpanel.getSheet());
                this.current.refresh();
                preview.delete();
                if (this.stackSheets.size() > 0) {
                    this.previewpanel.showSheet(this.stackSheets.remove(0));
                } else {
                    this.previewpanel.showSheet(null);
                    this.bAddPage.setEnabled(false);
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(this, "Fehler beim hinzufügen der Seite");
        }
    }

    private void okClicked() {
        Sheet s = current.getSheet();
        for (SelectSongBox cbSong : this.cbSong) {
            for (SelectVoiceBox cbVoice : this.cbVoice) {
                Song song = cbSong.getSelectedSong();
                Voice v = cbVoice.getSelectedVoice();
                if (song == null || v == null) {
                    JOptionPane.showMessageDialog(this, "Sie müssen sowohl Titel als auch Stimme eingeben", "Fehler", JOptionPane.OK_OPTION);
                    return;
                }
                try {
                    if (Archive.getInstance().contains(song, v)) {
                        JPanel existingPreview = new JPanel();
                        existingPreview.setLayout(new BoxLayout(existingPreview, BoxLayout.Y_AXIS));
                        existingPreview.setPreferredSize(new Dimension(400, 400));
                        existingPreview.setMinimumSize(new Dimension(400, 400));
                        existingPreview.setMaximumSize(new Dimension(400, 400));
                        PreviewPanel pp = new PreviewPanel(existingPreview);
                        pp.setPreferredSize(new Dimension(380, 380));
                        pp.setSize(380, 380);
                        pp.showSheet(Archive.getInstance().getSheet(song, v));
                        existingPreview.add(pp);
                        existingPreview.add(new JLabel("Datei ist bereits im Archiv."));
                        existingPreview.add(new JLabel("Überschreiben?"));
                        if (JOptionPane.showConfirmDialog(this, existingPreview, this.getTitle(), JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                            return;
                        }
                    }
                    Archive.getInstance().addToArchive(s, song, v);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Fehler beim speichern der Seite");
                }
            }
        }
        s.delete();
        Sheet p = previewpanel.getSheet();
        if (p == null) {
            this.dispose();
        }
        this.current.showSheet(p);
        if (this.stackSheets.size() > 0) {
            this.previewpanel.showSheet(this.stackSheets.remove(0));
            this.cbVoice.get(0).requestFocus();
        } else {
            this.previewpanel.showSheet(null);
            this.bAddPage.setEnabled(false);
        }
    }

    private void deleteClicked() {
        Sheet s = current.getSheet();
        s.delete();
        Sheet p = previewpanel.getSheet();
        if (p == null) {
            this.dispose();
        }
        this.current.showSheet(p);
        if (this.stackSheets.size() > 0) {
            this.previewpanel.showSheet(this.stackSheets.remove(0));
        } else {
            this.previewpanel.showSheet(null);
            this.bAddPage.setEnabled(false);
        }
    }

    private void guessSongClicked() {
        bGuessSong.setEnabled(false);
        Sheet s = current.getSheet();
        SelectSongBox cbSong = this.cbSong.get(0);
        if (s != null) {
            try {
                String[] possible = new String[cbSong.getItemCount()];
                for (int i = 0; i < possible.length; i++) {
                    possible[i] = cbSong.getItemAt(i).toString();
                }
                OCRResult guess = OCR.findStringInImage(possible, s.getPreview(new Dimension(25, 0), new Dimension(50, 20), new Dimension(500, 200)));
                if (guess == null) {
                    return;
                }
                if (guess.matchquality > 70) {
                    cbSong.setSelectedIndex(guess.index);
                } else {
                    cbSong.setSelectedItem(guess.beautified);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        bGuessSong.setEnabled(true);
    }

    private void guessVoiceClicked() {
        bGuessVoice.setEnabled(false);
        Sheet s = current.getSheet();
        SelectVoiceBox cbVoice = this.cbVoice.get(0);
        if (s != null) {
            try {
                String[] possible = new String[cbVoice.getItemCount()];
                for (int i = 0; i < possible.length; i++) {
                    possible[i] = cbVoice.getItemAt(i).toString();
                }
                OCRResult guess1 = OCR.findStringInImage(possible, s.getPreview(new Dimension(0, 0), new Dimension(25, 20), new Dimension(250, 200)));
                OCRResult guess2 = OCR.findStringInImage(possible, s.getPreview(new Dimension(75, 0), new Dimension(25, 20), new Dimension(250, 200)));
                OCRResult guess = null;
                if (guess1 == null && guess2 != null) {
                    guess = guess2;
                } else if (guess2 == null && guess1 != null) {
                    guess = guess1;
                } else if (guess1 == null && guess2 == null) {
                    return;
                } else {
                    guess = guess1.matchquality > guess2.matchquality ? guess1 : guess2;
                }
                if (guess.matchquality > 50) {
                    cbVoice.setSelectedIndex(guess.index);
                } else {
                    cbVoice.setSelectedItem(guess.beautified);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        bGuessVoice.setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.bRotatePage)) {
            this.rotateClicked();
        } else if (e.getSource().equals(this.bAddPage)) {
            this.addClicked();
        } else if (e.getSource().equals(this.bDeletePage)) {
            this.deleteClicked();
        } else if (e.getSource().equals(this.bOk)) {
            this.okClicked();
        } else if (this.bGuessSong != null && e.getSource().equals(this.bGuessSong)) {
            this.guessSongClicked();
        } else if (this.bGuessVoice != null && e.getSource().equals(this.bGuessVoice)) {
            this.guessVoiceClicked();
        } else if (e.getSource().equals(this.bAddSong)) {
            SelectSongBox cbSong = new SelectSongBox(true, true);
            cbSong.setMaximumSize(new Dimension(500, 35));
            this.cbSong.add(cbSong);
            this.songPanel.add(cbSong);
            this.bDeleteSong.setEnabled(true);
        } else if (e.getSource().equals(this.bDeleteSong)) {
            this.songPanel.remove(this.cbSong.remove(cbSong.size() - 1));
            if (this.cbSong.size() == 1) {
                this.bDeleteSong.setEnabled(false);
            }
            this.validate();
        } else if (e.getSource().equals(this.bAddVoice)) {
            SelectVoiceBox cbVoice = new SelectVoiceBox();
            cbVoice.setMaximumSize(new Dimension(500, 35));
            this.cbVoice.add(cbVoice);
            this.voicePanel.add(cbVoice);
            this.bDeleteVoice.setEnabled(true);
        } else if (e.getSource().equals(this.bDeleteVoice)) {
            this.voicePanel.remove(this.cbVoice.remove(cbVoice.size() - 1));
            if (this.cbVoice.size() == 1) {
                this.bDeleteVoice.setEnabled(false);
            }
            this.validate();
        }
    }
}
