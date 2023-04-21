package org.fudaa.ebli.animation;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import com.memoire.fu.FuLib;
import com.memoire.fu.FuLog;
import org.fudaa.ctulu.CtuluLib;
import org.fudaa.ctulu.CtuluLibFile;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.ctulu.gui.CtuluDialogPanel;
import org.fudaa.ctulu.gui.CtuluLibDialog;
import org.fudaa.ctulu.image.CtuluImageExport;
import org.fudaa.ctulu.image.CtuluImageProducer;
import org.fudaa.ctulu.video.CtuluMencoderFinder;
import org.fudaa.ebli.commun.EbliLib;

/**
 * @author Fred Deniger
 * @version $Id: EbliAnimationOutputAviMencoder.java,v 1.4 2007-05-04 13:49:44 deniger Exp $
 */
public class EbliAnimationOutputAviMencoder extends EbliAnimationOutputAbstract {

    /**
   * Le coefficient a utiliser pour multiplier le nombre d'image par seconde.
   */
    int coefFrameRate_;

    NumberFormat fmt_;

    int idx_;

    File listFile_;

    BufferedWriter out_;

    String mencoder_;

    File tmpDir_;

    public EbliAnimationOutputAviMencoder() {
        super(".avi");
    }

    public CtuluDialogPanel createConfigurePanel(final int _nbFrame) {
        return new EbliAnimationVideoEditPanel.Mencoder(_nbFrame, this);
    }

    public void appendFrame(final BufferedImage _im, final boolean _last) {
        idx_++;
        final String string = fmt_.format(idx_) + ".png";
        CtuluImageExport.export(_im, new File(tmpDir_, string), "png", null);
        try {
            for (int i = 0; i < coefFrameRate_; i++) {
                out_.write(string);
                out_.write(CtuluLibString.LINE_SEP);
            }
        } catch (final IOException _evt) {
            FuLog.error(_evt);
        }
    }

    public void finish() {
        FuLib.safeClose(out_);
        final String[] cmd = new String[] { mencoder_, "mf://@" + listFile_.getName(), "-mf", "fps=" + getGoodFPSForAvi() + ":type=png", "-vf", "scale=" + width_ + ':' + height_, "-ovc", "lavc", "-lavcopts", "vcodec=msmpeg4", "-oac", "copy", "-o", destFile_.getAbsolutePath() };
        try {
            FuLib.runProgram(cmd, tmpDir_);
        } catch (final IOException _evt) {
            FuLog.error(_evt);
        }
        CtuluLibFile.deleteDir(tmpDir_);
        warnIfFileExists_ = true;
    }

    public String getName() {
        return "video avi (divx)";
    }

    public String getShortName() {
        return "divx";
    }

    public boolean init(final CtuluImageProducer _p, final Component _parent, final int _nbImg) {
        idx_ = 0;
        if (isActivated()) {
            coefFrameRate_ = getNbRepeatForAvi();
            fmt_ = CtuluLibString.getFormatForIndexingInteger(_nbImg);
            mencoder_ = new CtuluMencoderFinder().getMencoderPath();
            if (mencoder_ == null) {
                CtuluLibDialog.showError(_parent, "Mencoder", EbliLib.getS("L'outil 'mencoder' n'a pas �t� trouv�"));
            }
            if (warnIfFileExists_ && !canOverwrittenFile(_parent)) {
                return false;
            }
            destFile_.delete();
            tmpDir_ = null;
            try {
                tmpDir_ = CtuluLibFile.createTempDir("img", destFile_.getParentFile());
            } catch (final IOException _evt) {
                CtuluLibDialog.showError(_parent, EbliLib.getS("Vid�o"), EbliLib.getS("Impossible de cr�er un r�pertoire temporaire"));
                FuLog.error(_evt);
            }
            if (tmpDir_ == null) {
                return false;
            }
            listFile_ = new File(tmpDir_, "list.txt");
            try {
                out_ = new BufferedWriter(new FileWriter(listFile_));
            } catch (final IOException _evt) {
                FuLog.error(_evt);
                FuLib.safeClose(out_);
                out_ = null;
            }
        }
        return true;
    }

    public String isValid() {
        if (destFile_ == null) {
            return EbliLib.getS("Le fichier de sortie n'est pas pr�cis�");
        }
        if (destFile_.isDirectory()) {
            return EbliLib.getS("Le fichier de sortie est un r�pertoire");
        }
        return CtuluLibFile.canWrite(destFile_);
    }

    public String isValid(final boolean _testDim) {
        if (mencoder_ == null) {
            mencoder_ = new CtuluMencoderFinder().getMencoderPath();
            return CtuluLib.getS("L'outil 'mencoder' n'a pas �t� trouv�");
        }
        return super.isValid(_testDim);
    }
}
