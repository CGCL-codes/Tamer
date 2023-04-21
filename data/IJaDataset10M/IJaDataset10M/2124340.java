package org.fudaa.fudaa.tr.rubar;

import java.io.File;
import org.fudaa.ctulu.CtuluIOOperationSynthese;
import org.fudaa.ctulu.ProgressionInterface;
import org.fudaa.ctulu.fileformat.FileFormatUnique;
import org.fudaa.dodico.dico.DicoEntite;
import org.fudaa.dodico.dico.DicoParams;
import org.fudaa.dodico.dico.DicoParamsListener;
import org.fudaa.dodico.h2d.rubar.H2dRubarDicoModel;
import org.fudaa.dodico.h2d.rubar.H2dRubarParameters;
import org.fudaa.dodico.h2d.rubar.H2dRubarSedimentListener;
import org.fudaa.dodico.h2d.rubar.H2dRubarSedimentMng;
import org.fudaa.dodico.h2d.type.H2dRubarProjetType;
import org.fudaa.dodico.rubar.io.RubarSEDFileFormat;

public class TrRubarFileStateSED extends TrRubarFileState implements H2dRubarSedimentListener, DicoParamsListener {

    private final H2dRubarParameters parameters;

    public TrRubarFileStateSED(TrRubarFileStateListener _l, H2dRubarParameters parameters) {
        super(new RubarSEDFileFormat(), _l);
        parameters.getSedimentMng().addSedimentListener(this);
        parameters.getDicoParams().addModelListener(this);
        this.parameters = parameters;
        updateIsPresent();
    }

    @Override
    public void dicoParamsEntiteAdded(DicoParams _cas, DicoEntite _ent) {
        updateModifyState(_ent);
    }

    private void updateModifyState(DicoEntite _ent) {
        DicoEntite initalTime = H2dRubarDicoModel.getInitalTime(parameters.getDicoParams().getDicoFileFormatVersion());
        if (_ent == initalTime) {
            setModified(true);
        }
    }

    @Override
    public void dicoParamsEntiteRemoved(DicoParams _cas, DicoEntite _ent, String _oldValue) {
        updateModifyState(_ent);
    }

    @Override
    public void dicoParamsEntiteUpdated(DicoParams _cas, DicoEntite _ent, String _oldValue) {
        updateModifyState(_ent);
    }

    @Override
    public void dicoParamsEntiteCommentUpdated(DicoParams _cas, DicoEntite _ent) {
    }

    @Override
    public void dicoParamsValidStateEntiteUpdated(DicoParams _cas, DicoEntite _ent) {
    }

    @Override
    public void dicoParamsVersionChanged(DicoParams _cas) {
    }

    @Override
    public void sedimentChanged(H2dRubarSedimentMng _mng) {
        setModified(true);
        updateIsPresent();
    }

    private void updateIsPresent() {
        if (parameters.getProjetType() == H2dRubarProjetType.TRANSPORT) {
            if (((TrRubarFileStateMng) l_).contains(fmt_) == null) {
                ((TrRubarFileStateMng) l_).add(this);
            }
        } else {
            if (((TrRubarFileStateMng) l_).contains(fmt_) != null) {
                ((TrRubarFileStateMng) l_).remove(this);
            }
        }
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public CtuluIOOperationSynthese save(File _dir, String _projectName, ProgressionInterface _progression, TrRubarProject _projet) {
        final File f = fmt_.getFileFor(_dir, _projectName);
        return ((FileFormatUnique) fmt_).write(f, _projet.getH2dRubarParameters().getSedimentMng().getSavedInterface(), _progression);
    }
}
