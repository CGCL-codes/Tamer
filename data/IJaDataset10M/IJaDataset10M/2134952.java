package org.fudaa.fudaa.tr.rubar;

import java.io.File;
import com.memoire.fu.FuLog;
import org.fudaa.ctulu.CtuluIOOperationSynthese;
import org.fudaa.ctulu.CtuluLibMessage;
import org.fudaa.ctulu.ProgressionInterface;
import org.fudaa.dodico.h2d.rubar.H2DRubarFrictionListener;
import org.fudaa.dodico.h2d.rubar.H2dRubarParameters;
import org.fudaa.dodico.rubar.io.RubarFRTFileFormat;

/**
 * @author Fred Deniger
 * @version $Id: TrRubarFileStateFRT.java,v 1.10 2007-05-04 14:01:53 deniger Exp $
 */
public class TrRubarFileStateFRT extends TrRubarFileState implements H2DRubarFrictionListener {

    public void frictionChanged() {
        setModified(true);
    }

    /**
   * @param _l le listener
   */
    public TrRubarFileStateFRT(final TrRubarFileStateListener _l, final H2dRubarParameters _p) {
        super(RubarFRTFileFormat.getInstance(), _l);
        _p.getFriction().addListener(this);
    }

    @Override
    public CtuluIOOperationSynthese save(final File _dir, final String _projectName, final ProgressionInterface _progression, final TrRubarProject _projet) {
        return RubarFRTFileFormat.getInstance().write(RubarFRTFileFormat.getInstance().getFileFor(_dir, _projectName), _projet.getH2dRubarParameters().getFriction(), _progression);
    }

    @Override
    protected CtuluIOOperationSynthese loadIfNeeded(final File _initFile, final ProgressionInterface _prog) {
        FuLog.warning(new Throwable());
        return null;
    }

    protected CtuluIOOperationSynthese loadIfNeeded(final File _initFile, final ProgressionInterface _prog, final int _nbElt) {
        if ((initFile_ != null) && (!initFile_.equals(_initFile))) {
            new Throwable(initFile_.getName() + " loaded " + _initFile.getName()).printStackTrace();
            return null;
        }
        if (initFile_ == null) {
            initFile_ = _initFile;
        }
        if (isLoadedFileUpToDate()) {
            CtuluLibMessage.info("RELOAD VF2M: " + _initFile.getName() + " uptodate");
            return null;
        }
        CtuluLibMessage.info("RELOAD VF2M: " + _initFile.getName() + " will be reloaded");
        lastModifiedTime_ = _initFile.lastModified();
        return ((RubarFRTFileFormat) fmt_).read(_initFile, _prog, _nbElt);
    }
}
