package org.fudaa.ctulu.gis;

import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntIntIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.fudaa.ctulu.collection.CtuluListObject;

/**
 * @author Fred Deniger
 * @version $Id: GISAttributeModelObjectList.java,v 1.11 2007-05-04 13:43:25 deniger Exp $
 */
public class GISAttributeModelObjectList extends CtuluListObject implements GISAttributeModelObservable, GISAttributeModelObjectInterface {

    protected transient GISAttributeListener listener_;

    GISAttributeInterface attribute_;

    protected GISAttributeModelObjectList() {
        super(0);
    }

    /**
   * @param _nb le nombre de valeurs
   * @param _attr l'attribut
   */
    public GISAttributeModelObjectList(final int _nb, final GISAttributeInterface _attr) {
        super(_nb);
        attribute_ = _attr;
        if (attribute_ == null) {
            throw new IllegalArgumentException("attribute is null");
        }
    }

    public Object getAverage() {
        if (list_.size() == 0) {
            return null;
        }
        return list_.get(0);
    }

    public GISAttributeModel createUpperModel(final int _numObject, final TIntIntHashMap _newIdxOldIdx) {
        if (_numObject < getSize()) {
            throw new IllegalArgumentException("bad num objects");
        }
        if (_numObject == getSize()) {
            return this;
        }
        final ArrayList newList = new ArrayList(_numObject);
        Collections.fill(newList, getAttribute().getDefaultValue());
        if (_newIdxOldIdx != null) {
            final TIntIntIterator it = _newIdxOldIdx.iterator();
            for (int i = _newIdxOldIdx.size(); i-- > 0; ) {
                it.advance();
                final int newIdx = it.key();
                newList.set(newIdx, list_.get(it.value()));
            }
        }
        return new GISAttributeModelObjectList(newList, this);
    }

    public GISAttributeModel deriveNewModel(final int _numObject, final GISReprojectInterpolateurI _interpol) {
        if (!(_interpol instanceof GISReprojectInterpolateurI.ObjectTarget)) {
            throw new IllegalArgumentException("bad interface");
        }
        return deriveNewModel(_numObject, (GISReprojectInterpolateurI.ObjectTarget) _interpol);
    }

    public Object getValue(final int _idx) {
        return getValueAt(_idx);
    }

    public GISAttributeModelObjectInterface deriveNewModel(final int _numObject, final GISReprojectInterpolateurI.ObjectTarget _interpol) {
        final ArrayList newList = new ArrayList(_numObject);
        for (int i = 0; i < _numObject; i++) {
            newList.add(_interpol.interpol(i));
        }
        return new GISAttributeModelObjectList(newList, this);
    }

    /**
   * @param _list la liste a copier
   * @param _attr l'attribut
   */
    public GISAttributeModelObjectList(final Collection _list, final GISAttributeInterface _attr) {
        super(_list);
        attribute_ = _attr;
        if (attribute_ == null) {
            throw new IllegalArgumentException("attribute is null");
        }
    }

    private GISAttributeModelObjectList(final List _l, final GISAttributeModelObjectList _model) {
        super(0);
        list_ = _l;
        attribute_ = _model.getAttribute();
        listener_ = _model.getListener();
    }

    public GISAttributeModel createSubModel(final int[] _idxToRemove) {
        final ArrayList newList = new ArrayList(getSize());
        final int nb = getSize();
        for (int i = 0; i < nb; i++) {
            if (Arrays.binarySearch(_idxToRemove, i) < 0) {
                newList.add(list_.get(i));
            }
        }
        return new GISAttributeModelObjectList(newList, this);
    }

    protected void fireObjectChanged(int _indexGeom, Object _newValue) {
        if (listener_ != null) {
            listener_.gisDataChanged(attribute_, _indexGeom, _newValue);
        }
    }

    protected final GISAttributeListener getListener() {
        return listener_;
    }

    public final void setListener(final GISAttributeListener _listener) {
        listener_ = _listener;
    }

    /**
   * @return l'attribut associe
   */
    public final GISAttributeInterface getAttribute() {
        return attribute_;
    }
}
