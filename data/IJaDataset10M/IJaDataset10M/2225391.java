package org.fudaa.fudaa.tr;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import com.memoire.bu.*;
import org.fudaa.ctulu.CtuluIOOperationSynthese;
import org.fudaa.ctulu.CtuluLibDialog;
import org.fudaa.ctulu.CtuluProgressionBarAdapter;
import org.fudaa.ctulu.ProgressionInterface;
import org.fudaa.ctulu.gis.GISZone;
import org.fudaa.ctulu.gui.CtuluDialogPanel;
import org.fudaa.dodico.commun.DodicoLib;
import org.fudaa.dodico.fichiers.NativeBinarySystem;
import org.fudaa.dodico.telemac.io.MatisseReader;
import org.fudaa.dodico.telemac.io.SinusxFileFormat;
import org.fudaa.fudaa.tr.common.TrPreferences;
import org.fudaa.fudaa.tr.common.TrResource;

/**
 * @author Fred Deniger
 * @version $Id: TestMatisseConvertGUI.java,v 1.12 2006-10-27 10:24:42 deniger Exp $
 */
public class TestMatisseConvertGUI extends CtuluDialogPanel implements ActionListener {

    boolean containsData_;

    BuButton btCharger_;

    BuButton btOut_;

    JTextField in_;

    JTextField out_;

    BuComboBox cbFmt_;

    BuTextField txtFichier_;

    BuTextField txtZones_;

    BuTextField txtPoints_;

    BuTextField txtpolylignes_;

    BuTextField txtpolygones_;

    ProgressionInterface progress_;

    GISZone mnt_;

    /**
   *
   */
    public TestMatisseConvertGUI() {
        super();
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        final BuPanel pnCharger = new BuPanel();
        pnCharger.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY), DodicoLib.getS("Entr�e")));
        pnCharger.setLayout(new BuGridLayout(2, 5, 5));
        in_ = addLabelFileChooserPanel(pnCharger, TrResource.getS("Fichier") + ": ", null, false);
        addLabel(pnCharger, TrResource.getS("Architecture") + ": ");
        cbFmt_ = new BuComboBox(new String[] { NativeBinarySystem.SPARC_NAME, NativeBinarySystem.X86_NAME });
        cbFmt_.setSelectedItem(NativeBinarySystem.getLocalMachineName());
        pnCharger.add(cbFmt_);
        btCharger_ = new BuButton(DodicoLib.getS("Charger"));
        btCharger_.setIcon(BuResource.BU.loadMenuCommandIcon("analyser"));
        btCharger_.addActionListener(this);
        pnCharger.add(new BuLabel(""));
        pnCharger.add(btCharger_);
        final BuPanel info = new BuPanel();
        info.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY), DodicoLib.getS("Informations")));
        info.setLayout(new BuGridLayout(2, 5, 5));
        txtFichier_ = addLabelStringText(info, TrResource.getS("Fichier analys�"));
        txtFichier_.setEditable(false);
        txtZones_ = addLabelDoubleText(info, TrResource.getS("Nombre de zones"));
        txtZones_.setEditable(false);
        txtPoints_ = addLabelDoubleText(info, TrResource.getS("Nombre total de points"));
        txtPoints_.setEditable(false);
        txtpolygones_ = addLabelDoubleText(info, TrResource.getS("Nombre total de polygones"));
        txtpolygones_.setEditable(false);
        txtpolylignes_ = addLabelDoubleText(info, TrResource.getS("Nombre total de polylignes"));
        txtpolylignes_.setEditable(false);
        final BuPanel pnOut = new BuPanel();
        pnOut.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY), DodicoLib.getS("Sortie")));
        pnOut.setLayout(new BuGridLayout(2, 5, 5));
        out_ = addLabelFileChooserPanel(pnOut, TrResource.getS("Fichier de sortie") + ": ", null, false);
        pnOut.add(new BuLabel());
        btOut_ = new BuButton(DodicoLib.getS("Sauvegarger"));
        btOut_.setIcon(BuResource.BU.loadMenuCommandIcon("enregistrer"));
        btOut_.addActionListener(this);
        btOut_.setEnabled(false);
        pnOut.add(btOut_);
        setLayout(new BuBorderLayout(10, 10));
        final BuPanel main = new BuPanel();
        main.setLayout(new BuBorderLayout(10, 10));
        main.add(pnCharger, BuBorderLayout.NORTH);
        main.add(info, BuBorderLayout.CENTER);
        main.add(pnOut, BuBorderLayout.SOUTH);
        add(main, BuBorderLayout.CENTER);
        final BuProgressBar progress = new BuProgressBar();
        progress_ = new CtuluProgressionBarAdapter(progress);
        add(progress, BuBorderLayout.SOUTH);
    }

    /**
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
    public void actionPerformed(final ActionEvent _e) {
        final Object s = _e.getSource();
        if (s == btCharger_) {
            charger();
        } else if (s == btOut_) {
            save();
        }
    }

    private void charger() {
        final String file = in_.getText().trim();
        if (file.length() == 0) {
            return;
        }
        final File f = new File(file);
        if (!f.exists()) {
            CtuluLibDialog.showError(this, "Fichier non trouv�", "Le fichier " + f + " est introuvable");
        }
        new Thread() {

            public void run() {
                final MatisseReader r = new MatisseReader();
                r.setProgressReceiver(progress_);
                r.setMachineId(NativeBinarySystem.getIdFromName((String) cbFmt_.getSelectedItem()));
                r.setFile(f);
                final CtuluIOOperationSynthese s = r.read();
                if (s.containsFatalError()) {
                    CtuluLibDialog.showError(TestMatisseConvertGUI.this, "Erreur de lecture", s.getAnalyze().getFatalError().getMessage());
                }
                mnt_ = (GISZone) s.getSource();
                if (mnt_ == null) {
                    return;
                }
                cbFmt_.setSelectedItem(r.getMachineId());
                txtFichier_.setText(f.getAbsolutePath());
                txtFichier_.setToolTipText(f.getAbsolutePath());
                txtPoints_.setText(Double.toString(mnt_.getNumPoints()));
                txtpolygones_.setText(Double.toString(mnt_.getNumPolyligones()));
                txtpolylignes_.setText(Double.toString(mnt_.getNumPolylignes()));
                txtZones_.setText("");
                btOut_.setEnabled(true);
                progress_.setProgression(0);
            }
        }.start();
    }

    private void save() {
        final String file = out_.getText().trim();
        System.out.println("sauvegarde dans " + file);
        if (file.length() == 0) {
            return;
        }
        final File f = new File(file);
        if (f.exists() && !f.canWrite()) {
            CtuluLibDialog.showError(this, "Impossible d'�crire", "Le fichier " + f + " est prot�g�");
        }
        if (mnt_ == null) {
            CtuluLibDialog.showError(this, "Pas de donn�es", "Avant de sauvegarder un projet, vous devez analyser un fichier matisse");
        }
        new Thread() {

            public void run() {
                btCharger_.setEnabled(false);
                SinusxFileFormat.getInstance().getLastVersionInstance().write(f, mnt_, progress_);
                btCharger_.setEnabled(true);
                CtuluLibDialog.showMessage(btCharger_, "OK", "La sauvegarde qui va bien est finie");
                progress_.setProgression(0);
            }
        }.start();
    }

    /**
   * @param _args non utilisee
   */
    public static void main(final String[] _args) {
        final JFrame f = new JFrame(TrResource.getS("Convertisseur Matisse"));
        f.setContentPane(new TestMatisseConvertGUI());
        f.addWindowListener(new WindowListener() {

            public void windowActivated(final WindowEvent _e) {
            }

            public void windowClosed(final WindowEvent _e) {
                BuPreferences.BU.writeIniFile();
                TrPreferences.TR.writeIniFile();
                System.exit(0);
            }

            public void windowClosing(final WindowEvent _e) {
            }

            public void windowDeactivated(final WindowEvent _e) {
            }

            public void windowDeiconified(final WindowEvent _e) {
            }

            public void windowIconified(final WindowEvent _e) {
            }

            public void windowOpened(final WindowEvent _e) {
            }
        });
        f.pack();
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        f.show();
    }
}
