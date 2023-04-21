package jmovie;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileFilter;

public class jMovieActionListerner implements ActionListener {

    ActionType action;

    public jMovieActionListerner(ActionType action) {
        super();
        this.action = action;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(action) {
            case NEW_DB:
                JFileChooser fc111 = new JFileChooser();
                fc111.setDialogTitle("Seleziona il file Database (.xml) di jMovieDB su cui salvare");
                fc111.setFileFilter(new MyFileFilter(".xml"));
                fc111.setAcceptAllFileFilterUsed(false);
                int choose111 = fc111.showSaveDialog(GuiManager.getFrame());
                if (choose111 == JFileChooser.APPROVE_OPTION) {
                    File dbFile = fc111.getSelectedFile();
                    String filePath = dbFile.getAbsolutePath();
                    if (!filePath.endsWith(".xml")) dbFile = new File(filePath + ".xml");
                    IO.salvaSuFile(dbFile);
                    IO.writeCurrentDB(dbFile.getPath());
                    GuiManager.getFrame().aggiorna();
                }
                break;
            case IMPORT_DB:
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Seleziona il file Database (.mdb) di FilmUP da importare");
                fc.setFileFilter(new MyFileFilter(".mdb"));
                fc.setAcceptAllFileFilterUsed(false);
                int choose = fc.showOpenDialog(GuiManager.getFrame());
                if (choose == JFileChooser.APPROVE_OPTION) {
                    File dbFile = fc.getSelectedFile();
                    ArrayList<Film> filmImportati = IO.importFilmUpDB(dbFile);
                    for (Film film : filmImportati) {
                        if (!GuiManager.getFrame().listaFilm.contains(film)) GuiManager.getFrame().listaFilm.add(film); else System.out.println("'" + film + "� gi� in lista");
                    }
                    Collections.sort(GuiManager.getFrame().listaFilm);
                    GuiManager.getFrame().aggiorna();
                }
                break;
            case OPEN_DB:
                JFileChooser fc1 = new JFileChooser();
                fc1.setDialogTitle("Seleziona il file Database (.xml) di jMovieDB da aprire");
                fc1.setFileFilter(new MyFileFilter(".xml"));
                fc1.setAcceptAllFileFilterUsed(false);
                int choose1 = fc1.showOpenDialog(GuiManager.getFrame());
                if (choose1 == JFileChooser.APPROVE_OPTION) {
                    File dbFile = fc1.getSelectedFile();
                    GuiManager.getFrame().listaFilm = IO.caricaDaFile(dbFile);
                    IO.writeCurrentDB(dbFile.getPath());
                    GuiManager.getFrame().aggiorna();
                }
                break;
            case SAVE_DB:
                String filePath = IO.getCurrentDB();
                if (filePath == null) {
                    JFileChooser fc11 = new JFileChooser();
                    fc11.setDialogTitle("Seleziona il file Database (.xml) di jMovieDB su cui salvare");
                    fc11.setFileFilter(new MyFileFilter(".xml"));
                    fc11.setAcceptAllFileFilterUsed(false);
                    int choose11 = fc11.showSaveDialog(GuiManager.getFrame());
                    if (choose11 == JFileChooser.APPROVE_OPTION) {
                        File dbFile = fc11.getSelectedFile();
                        String filePath1 = dbFile.getAbsolutePath();
                        if (!filePath1.endsWith(".xml")) dbFile = new File(filePath1 + ".xml");
                        IO.salvaSuFile(dbFile);
                        IO.writeCurrentDB(dbFile.getPath());
                    }
                } else {
                    IO.salvaSuFile(new File(filePath));
                }
                break;
            case ABOUT:
                showInfo();
                break;
            case LICENSE:
                showLicence();
                break;
            case EXIT:
                GuiManager.getFrame().closeApplication();
                break;
        }
    }

    public void showInfo() {
        String credits = "<html><center><b><font size=6>jMovieDB</font></b></center><br>" + "Released under GNU General Public License version 3. " + "<a href=http://www.gnu.org/licenses/gpl-3.0-standalone.html>GPLv3</a><br><br>" + "Copyright � 2010 <i>Loria Salvatore</i><br>" + "Visit <a href=http://slash17.googlecode.com>http://slash17.googlecode.com</a></html>";
        JEditorPane editorPane = new JEditorPane("text/html", credits);
        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent evt) {
                if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    try {
                        Desktop.getDesktop().browse(new URI(evt.getDescription()));
                    } catch (Exception e) {
                    }
                }
            }
        });
        JOptionPane.showMessageDialog(GuiManager.getFrame(), editorPane, "About...", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("data/images/GNU.png"));
    }

    public void showLicence() {
        File f = new File("licence.txt");
        String text = "";
        try {
            BufferedReader r = new BufferedReader(new FileReader(f));
            String s;
            while ((s = r.readLine()) != null) {
                text = text + s + "\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JDialog d = new JDialog(GuiManager.getFrame());
        d.setTitle("Licence");
        d.setContentPane(new JPanel(new BorderLayout()));
        JLabel title = new JLabel("jMovieDB Licence", JLabel.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        title.setFont(new Font("Times New Roman", Font.BOLD, 24));
        d.add(title, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(new JTextArea(text));
        d.add(scrollPane);
        d.setSize(600, 600);
        d.setLocationRelativeTo(GuiManager.getFrame());
        d.setVisible(true);
    }

    public class MyFileFilter extends FileFilter {

        String acceptedExtension;

        public MyFileFilter(String string) {
            acceptedExtension = string;
        }

        @Override
        public String getDescription() {
            return "FilmUP Database file (" + acceptedExtension + ")";
        }

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) return true;
            if (f.getAbsolutePath().endsWith(acceptedExtension)) return true;
            return false;
        }
    }

    public enum ActionType {

        NEW_DB, IMPORT_DB, SAVE_DB, OPEN_DB, ABOUT, LICENSE, EXIT
    }
}
