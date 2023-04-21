package org.fudaa.ctulu;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import com.memoire.bu.BuLib;
import com.memoire.bu.BuResource;
import com.memoire.fu.FuLog;
import org.fudaa.ctulu.gui.CtuluImageFileChooser;

/**
 * @author Fred Deniger
 * @version $Id: CtuluImageExport.java,v 1.11 2006-09-19 14:36:54 deniger Exp $
 */
public final class CtuluImageExport {

    /**
   * La liste des formats images supportes.
   */
    public static final List FORMAT_LIST = new CtuluPermanentList(getAvailableFormat());

    private CtuluImageExport() {
        super();
    }

    public static void exportImageFor(final CtuluUI _ui, final CtuluImageProducer _producer) {
        final CtuluImageFileChooser fileC = new CtuluImageFileChooser();
        final String name = CtuluLib.getS("Enregistrement image");
        fileC.setDialogTitle(name);
        if (fileC.showOpenDialog(CtuluLib.getFrameAncestorHelper(_ui.getParentComponent())) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File f = fileC.getSelectedFile();
        if (f != null) {
            final String fName = f.getName();
            final String ext = CtuluLibFile.getExtension(fName);
            String fmt = fileC.getSelectedFormat();
            if (ext != null && !ext.equals(fmt) && FORMAT_LIST.contains(ext)) {
                fmt = ext;
            }
            if (!fName.toLowerCase().endsWith(CtuluLibString.DOT + fmt)) {
                f = new File(f.getAbsolutePath() + CtuluLibString.DOT + fmt);
            }
            if (f.exists()) {
                final boolean b = _ui.question(CtuluLib.getS("Confirmation"), CtuluLib.getS("Le fichier {0} existe d�j�. Voulez-vous l'�craser?", f.getName()));
                if (!b) {
                    return;
                }
            }
            final File fToSave = f;
            final CtuluTaskDelegate delegate = _ui.createTask(name);
            BuLib.invokeLater(new Runnable() {

                public void run() {
                    final RenderedImage i = _producer.produceImage();
                    delegate.start(new Runnable() {

                        public void run() {
                            if (CtuluImageExport.export(i, fToSave, fileC.getSelectedFormat(), _ui)) {
                                BuLib.invokeLater(new Runnable() {

                                    public void run() {
                                        _ui.message(name, CtuluLib.getS("Enregistrement image r�ussi"), true);
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
    }

    public static BufferedImage transform(final BufferedImage _init, final int _w, final int _h) {
        final double sx = (double) _w / ((double) _init.getWidth());
        final double sy = (double) _h / ((double) _init.getHeight());
        final AffineTransformOp res = new AffineTransformOp(AffineTransform.getScaleInstance(sx, sy), AffineTransformOp.TYPE_BILINEAR);
        final BufferedImage r = new BufferedImage(_w, _h, BufferedImage.TYPE_3BYTE_BGR);
        res.filter(_init, r);
        return r;
    }

    private static String[] getAvailableFormat() {
        String[] w = ImageIO.getWriterFormatNames();
        final Set r = new HashSet(w.length);
        for (int i = w.length - 1; i >= 0; i--) {
            final String s = w[i].toLowerCase();
            if (!"jpeg".equals(s)) {
                r.add(s);
            }
        }
        w = new String[r.size()];
        r.toArray(w);
        Arrays.sort(w);
        return w;
    }

    /**
   * @param _image l'image a exporter
   * @param _targetFile le fichier cible
   * @param _format le format voulu ( doit appartenir a la liste FORMAT_LIST
   * @param _ui l'interface utilisateur
   * @return true si ok
   * @see #FORMAT_LIST
   */
    public static boolean export(final RenderedImage _image, final File _targetFile, final String _format, final CtuluUI _ui) {
        try {
            ImageIO.write(_image, _format, _targetFile);
        } catch (final IllegalArgumentException e) {
            return false;
        } catch (final IOException _e) {
            if (_ui == null) {
                FuLog.error("save image", _e);
            } else {
                _ui.error(BuResource.BU.getString("Enregistrement image..."), _e.getLocalizedMessage(), false);
            }
            return false;
        }
        return true;
    }
}
