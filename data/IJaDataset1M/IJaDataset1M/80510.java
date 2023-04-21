package org.fudaa.fudaa.tr.rubar;

import java.io.File;
import org.fudaa.ctulu.CtuluIOOperationSynthese;
import org.fudaa.ctulu.ProgressionInterface;
import org.fudaa.dodico.rubar.io.RubarMAIFileFormat;

/**
 * @author Fred Deniger
 * @version $Id: TrRubarFileStateMAI.java,v 1.6 2006-09-08 16:53:09 deniger Exp $
 */
public class TrRubarFileStateMAI extends TrRubarFileState {

    /**
   * @param _l le listener
   */
    public TrRubarFileStateMAI(final TrRubarFileStateListener _l) {
        super(RubarMAIFileFormat.getInstance(), _l);
    }

    @Override
    public CtuluIOOperationSynthese save(final File _dir, final String _projectName, final ProgressionInterface _progression, final TrRubarProject _projet) {
        return null;
    }

    @Override
    public boolean isMarkRemoved() {
        return false;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void setMarkRemoved(final boolean _isMarkRemoved) {
    }

    @Override
    public void setModified(final boolean _isModified) {
    }
}
