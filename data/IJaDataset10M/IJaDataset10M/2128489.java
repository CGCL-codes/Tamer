package gui.matchMii.persoUI.mp3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import model.data.Album;
import model.data.Artiste;
import model.data.TitrePerso;
import javazoom.spi.PropertiesContainer;
import gui.matchMii.event.QuitListener;
import gui.matchMii.ui.InfiniteProgressPanel;
import gui.matchMii.ui.SalesManagerFrame;
import gui.matchMii.ui.UIHelper;

/**
 *
 * @author  Ced
 */
public class PlayMP3Panel extends javax.swing.JPanel implements QuitListener, ObserverPlayer {

    private SalesManagerFrame parent;

    private InfiniteProgressPanel glassPane;

    /** Creates new form PlayMP3 */
    public PlayMP3Panel(SalesManagerFrame parent) {
        this.parent = parent;
        this.parent.addQuitListener(this);
        this.glassPane = new InfiniteProgressPanel();
        this.parent.setGlassPane(glassPane);
        initComponents();
        MP3Player.getInstance().addObserver(this);
    }

    private void addMP3() {
        Artiste artiste = new Artiste("Chris Brown");
        Album album = new Album("Exclusive", 2006);
        album.addArtiste(artiste.getNom());
        TitrePerso titre = new TitrePerso(45, "Kiss Kiss (Feat.T-Pain)", 23, 34, new File("ressources/02-Chris Brown - Kiss Kiss (Feat.T-Pain)-RGF.mp3"));
        TitrePerso titre2 = new TitrePerso(45, "Relax (Take It Easy)", 23, 34, new File("ressources/MIKA - Relax (Take It Easy).mp3"));
        TitrePerso titre3 = new TitrePerso(45, "Young Folks", 23, 34, new File("ressources/Peter_Bjorn_and_John-Young_Folks.mp3"));
        TitrePerso titre4 = new TitrePerso(45, "Tell Me Why", 23, 34, new File("ressources/Supermode - Tell Me Why.mp3"));
        titre.addAlbum(album.getNom());
        titre.addArtiste(artiste.getNom());
        album.addTitre(titre);
        titre2.addAlbum(album.getNom());
        titre2.addArtiste(artiste.getNom());
        album.addTitre(titre2);
        titre3.addAlbum(album.getNom());
        titre3.addArtiste(artiste.getNom());
        album.addTitre(titre3);
        titre4.addAlbum(album.getNom());
        titre4.addArtiste(artiste.getNom());
        album.addTitre(titre4);
        MP3Player.getInstance().addTitre(titre);
        MP3Player.getInstance().addTitre(titre2);
        MP3Player.getInstance().addTitre(titre3);
        MP3Player.getInstance().addTitre(titre4);
    }

    private void initComponents() {
        setOpaque(false);
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = UIHelper.createButton("PLAY");
        jButton4 = UIHelper.createButton("STOP");
        jButton1.addActionListener(new PerformPlayAction());
        jButton4.addActionListener(new PerformStopAction());
        jButton2 = UIHelper.createButton(">");
        jButton3 = UIHelper.createButton("<");
        jButton2.addActionListener(new PerformNextAction());
        jButton3.addActionListener(new PerformPreviousAction());
        jTable1.setModel(MP3Player.getInstance().getModel());
        jScrollPane1.setViewportView(jTable1);
        jScrollPane1.setOpaque(false);
        jScrollPane1.getViewport().setOpaque(false);
        jScrollPane1.setViewportBorder(null);
        addMP3();
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                System.out.println("TOTO");
            }
        });
        jTable1.addMouseListener(new PerformPlayAction());
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(jButton3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton4).addGap(28, 28, 28)))));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton3).addComponent(jButton2).addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JButton jButton3;

    private javax.swing.JButton jButton4;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTable jTable1;

    @Override
    public void quit() {
        glassPane.setVisible(false);
        glassPane.interrupt();
    }

    /**
	 * Action pour l'ex�cution d'une chanson
	 * 
	 * @author MORA
	 */
    class PerformPlayAction extends MouseAdapter implements ActionListener {

        private void play() {
            new Thread() {

                @Override
                public void run() {
                    int row = jTable1.getSelectedRow();
                    MP3Player.getInstance().play(row);
                }
            }.start();
        }

        public void actionPerformed(ActionEvent e) {
            play();
        }

        public void mouseClicked(java.awt.event.MouseEvent e) {
            if (e.getClickCount() >= 2) {
                play();
            }
        }
    }

    class PerformStopAction implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            new Thread() {

                @Override
                public void run() {
                    MP3Player.getInstance().stop();
                }
            }.start();
        }
    }

    class PerformNextAction implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            new Thread() {

                @Override
                public void run() {
                    if (MP3Player.getInstance().next()) {
                    }
                }
            }.start();
        }
    }

    class PerformPreviousAction implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            new Thread() {

                @Override
                public void run() {
                    if (MP3Player.getInstance().previous()) {
                    }
                }
            }.start();
        }
    }

    @Override
    public void updatePlayer(int row) {
        jTable1.getSelectionModel().setSelectionInterval(row, row);
    }
}
