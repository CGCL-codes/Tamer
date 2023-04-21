package org.fudaa.fudaa.sinavi2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import com.memoire.bu.BuCommonImplementation;
import com.memoire.bu.BuDialogMessage;
import com.memoire.bu.BuFileChooser;
import com.memoire.bu.BuFileFilter;
import com.memoire.bu.BuInformationsDocument;
import com.memoire.fu.FuLog;
import org.fudaa.ebli.graphe.BGraphe;
import org.fudaa.ebli.graphe.EbliFilleGraphe;

/**
 * Permet de lancer un graphe
 * 
 * @version $Revision: 1.9 $ $Date: 2007-02-07 09:56:22 $ by $Author: deniger $
 * @author Beno�t Maneuvrier, Fatimatou Ka
 */
public class Sinavi2FilleGraphe extends EbliFilleGraphe implements InternalFrameListener, MouseMotionListener, MouseListener {

    public Sinavi2Implementation imp_ = null;

    /**
   * Constructeur de la fentre graphe : repr�sente le graphe et clic droit permet l'impression
   * 
   * @param _appli : implementation
   * @param _graphe : BGraphe contenant les donn�es du graphe
   */
    public Sinavi2FilleGraphe(final BuCommonImplementation _appli, final BuInformationsDocument _id, final BGraphe _graphe) {
        super("Graphe", true, true, true, true, _appli, _id);
        setBackground(Color.white);
        graphe_ = _graphe;
        imp_ = (Sinavi2Implementation) _appli.getImplementation();
        addMouseMotionListener(this);
        addMouseListener(this);
        setPreferredSize(new Dimension(640, 480));
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(graphe_, BorderLayout.CENTER);
        setVisible(true);
        addInternalFrameListener(this);
    }

    public BGraphe getGraphe() {
        return graphe_;
    }

    public void setGraphe(final BGraphe _graphe) {
        getContentPane().removeAll();
        graphe_ = _graphe;
        getContentPane().add(graphe_, BorderLayout.CENTER);
        _graphe.repaint();
    }

    public void actionPerformed(final ActionEvent _e) {
        if (_e.getSource() != null) {
            cmdEnregistrerSous();
        }
    }

    /**
   * imprime dans un fichier png
   */
    public void cmdEnregistrerSous() {
        final BuFileChooser chooser = new BuFileChooser();
        chooser.setFileFilter(new BuFileFilter("png", "Fichiers : " + ".png"));
        final int returnVal = chooser.showSaveDialog((JFrame) imp_.getApp());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filename = chooser.getSelectedFile().getAbsolutePath();
            if (!filename.substring(filename.length() - 4).equalsIgnoreCase(".png")) {
                filename = filename + ".png";
            }
            final File f = new File(filename);
            try {
                final boolean reussi = ImageIO.write(graphe_.produceImage(null), "png", f);
                if (reussi) {
                    affMessage("Le graphique a �t� enregistr�");
                } else {
                    affMessage("Erreur, veuillez r�essayer. Si le probl�me persiste n'h�sitez pas � faire appel � l'�quipe Technique.");
                }
            } catch (final Exception e) {
                FuLog.debug("erreur d'enregistrement d'image de graphe !");
                e.printStackTrace();
            }
            FuLog.debug("exportation de graphe sous forme d'image");
        }
    }

    public void mouseClicked(final MouseEvent _e) {
        if (_e.getButton() == 3) {
            cmdEnregistrerSous();
        }
    }

    public void affMessage(final String _t) {
        final BuDialogMessage dialog_mess = new BuDialogMessage(imp_.getApp(), imp_.getInformationsSoftware(), "" + _t);
        dialog_mess.activate();
    }

    public void internalFrameClosed(final InternalFrameEvent e) {
        setVisible(false);
    }

    /**
   * Non implante
   */
    public void internalFrameActivated(final InternalFrameEvent e) {
    }

    /**
   * Non implante
   */
    public void internalFrameDeactivated(final InternalFrameEvent e) {
    }

    /**
   * Non implante
   */
    public void internalFrameOpened(final InternalFrameEvent e) {
    }

    /**
   * Non implante
   */
    public void internalFrameClosing(final InternalFrameEvent e) {
    }

    /**
   * Non implante
   */
    public void internalFrameIconified(final InternalFrameEvent e) {
    }

    /**
   * Non implante
   */
    public void internalFrameDeiconified(final InternalFrameEvent e) {
    }

    public void mouseDragged(final MouseEvent e) {
    }

    public void mouseMoved(final MouseEvent e) {
    }

    public void mouseEntered(final MouseEvent e) {
    }

    public void mouseExited(final MouseEvent e) {
    }

    public void mousePressed(final MouseEvent e) {
    }

    public void mouseReleased(final MouseEvent e) {
    }
}
