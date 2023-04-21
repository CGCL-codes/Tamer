package org.fudaa.fudaa.tr.post.data;

import java.util.Map;
import org.fudaa.ctulu.CtuluUI;
import org.fudaa.dodico.h2d.type.H2dVariableType;
import org.fudaa.fudaa.tr.post.TrPostSource;

/**
 * @author fred deniger
 * @version $Id: TrPostDataCreatedCstTimeSaver.java,v 1.2 2007-04-30 14:22:38 deniger Exp $
 */
public class TrPostDataCreatedCstTimeSaver implements TrPostDataCreatedSaver {

    String shortName_;

    int tidx_;

    public TrPostDataCreatedCstTimeSaver() {
        super();
    }

    public TrPostDataCreatedCstTimeSaver(final TrPostDataCreatedCstTime _data) {
        shortName_ = _data.var_.getShortName();
        tidx_ = _data.getTimeIdx();
    }

    public TrPostDataCreated restore(H2dVariableType _newVar, final TrPostSource _src, final CtuluUI _ui, final Map _shortName) {
        return TrPostDataCreatedCstTime.createFrom(_newVar, _src, this, _shortName);
    }
}
