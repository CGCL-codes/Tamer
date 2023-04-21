package org.fudaa.fudaa.sig;

import java.io.File;
import com.memoire.bu.BuFileFilter;
import org.fudaa.ctulu.CtuluAnalyze;
import org.fudaa.ctulu.CtuluIOOperationSynthese;
import org.fudaa.ctulu.ProgressionInterface;
import org.fudaa.ctulu.gis.GISAttributeInterface;
import org.fudaa.dodico.h2d.type.H2dVariableType;
import org.fudaa.dodico.telemac.io.SerafinFileFormat;
import org.fudaa.dodico.telemac.io.SerafinGisAdapter;
import org.fudaa.dodico.telemac.io.SerafinInterface;
import org.fudaa.dodico.telemac.io.TelemacVariableMapper;

/**
 * @author Fred Deniger
 * @version $Id: FSigFileLoaderSerafin.java,v 1.6 2006-10-16 07:59:06 deniger Exp $
 */
public class FSigFileLoaderSerafin implements FSigFileLoaderI {

    final BuFileFilter filter_;

    private FSigFileLoaderSerafin(final BuFileFilter _ft) {
        filter_ = _ft;
    }

    /**
   * 
   */
    public FSigFileLoaderSerafin() {
        filter_ = SerafinFileFormat.getInstance().createFileFilter();
    }

    public FSigFileLoaderI createNew() {
        return new FSigFileLoaderSerafin(filter_);
    }

    public BuFileFilter getFileFilter() {
        return filter_;
    }

    transient SerafinInterface result_;

    public void setInResult(final FSigFileLoadResult _r, final File _f, final ProgressionInterface _prog, final CtuluAnalyze _analyze) {
        if (result_ == null) {
            final CtuluIOOperationSynthese op = SerafinFileFormat.getInstance().readLast(_f, _prog);
            if (!op.containsFatalError()) {
                result_ = (SerafinInterface) op.getSource();
                result_.getGrid().computeBord(_prog, _analyze);
            }
        }
        final TelemacVariableMapper mapper = new TelemacVariableMapper();
        final int nbVar = result_.getValueNb();
        final GISAttributeInterface[] attributes = new GISAttributeInterface[nbVar];
        for (int i = 0; i < nbVar; i++) {
            final H2dVariableType t = mapper.getUsedKnownVar(result_.getValueId(i));
            attributes[i] = t == null ? _r.findOrCreateAttribute(result_.getValueId(i), Double.class, true) : _r.findOrCreateAttribute(t, true);
        }
        _r.addUsedAttributes(attributes);
        _r.pointModel_.add(new SerafinGisAdapter(result_, attributes));
        _r.nbPoint_ += result_.getGrid().getPtsNb();
        _r.nbPointTotal_ += result_.getGrid().getPtsNb();
    }
}
