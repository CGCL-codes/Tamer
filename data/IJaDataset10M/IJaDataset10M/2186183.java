package org.fudaa.fudaa.tr.rubar;

import java.io.File;
import org.fudaa.ctulu.CtuluIOOperationSynthese;
import org.fudaa.ctulu.ProgressionInterface;
import org.fudaa.ctulu.fileformat.FileFormatUnique;
import org.fudaa.dodico.h2d.rubar.H2dRubarLimniListener;
import org.fudaa.dodico.h2d.rubar.H2dRubarLimniMng;
import org.fudaa.dodico.h2d.rubar.H2dRubarParameters;
import org.fudaa.dodico.rubar.io.RubarDTRFileFormat;

/**
 * @author Fred Deniger
 * @version $Id: TrRubarFileStateDTR.java,v 1.8 2007-05-04 14:01:53 deniger Exp $
 */
public class TrRubarFileStateDTR extends TrRubarFileState implements H2dRubarLimniListener {

    public void limniPointChanged(final H2dRubarLimniMng _mng) {
        setModified(true);
        setMarkRemoved(_mng.isEmpty());
        if (!_mng.isEmpty() && ((TrRubarFileStateMng) l_).contains(fmt_) == null) {
            ((TrRubarFileStateMng) l_).add(this);
        }
    }

    public void limniTimeStepChanged(final H2dRubarLimniMng _mng) {
        setModified(true);
    }

    /**
   * @param _l le listener
   * @param _p les parametres
   */
    public TrRubarFileStateDTR(final TrRubarFileStateMng _l, final H2dRubarParameters _p) {
        super(new RubarDTRFileFormat(), _l);
        _p.getLimniMng().addListener(this);
        if (!_p.getLimniMng().isEmpty()) {
            _l.add(this);
        }
    }

    @Override
    public CtuluIOOperationSynthese save(final File _dir, final String _projectName, final ProgressionInterface _progression, final TrRubarProject _projet) {
        final File f = fmt_.getFileFor(_dir, _projectName);
        return ((FileFormatUnique) fmt_).write(f, _projet.getH2dRubarParameters().getLimniMng().createResultAdapter(), _progression);
    }
}
