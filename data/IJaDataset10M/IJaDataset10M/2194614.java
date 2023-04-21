package org.fudaa.ctulu.gis;

import gnu.trove.TIntArrayList;
import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntIntIterator;
import gnu.trove.TLongArrayList;
import java.util.Arrays;
import org.fudaa.ctulu.CtuluLibArray;
import org.fudaa.ctulu.collection.CtuluListInteger;
import org.fudaa.ctulu.collection.CtuluListLong;

/**
 * @author Fred Deniger
 * @version $Id: GISAttributeModelIntegerList.java,v 1.9 2007-01-10 08:58:47 deniger Exp $
 */
public class GISAttributeModelLongList extends CtuluListLong implements GISAttributeModelLongInterface, GISAttributeModelObservable {

    /**
   *
   */
    protected GISAttributeModelLongList() {
        super();
    }

    private GISAttributeModelLongList(final TLongArrayList _l, final GISAttributeModelLongList _model) {
        super(0);
        list_ = _l;
        attribute_ = _model.getAttribute();
        listener_ = _model.getListener();
    }

    public Object getAverage() {
        if (list_.size() == 0) {
            return Long.valueOf(0);
        }
        return Integer.valueOf((int) CtuluLibArray.getMoyenne(list_));
    }

    public GISAttributeModel createSubModel(final int[] _idxToRemove) {
        final TLongArrayList newList = new TLongArrayList(getSize());
        final int nb = getSize();
        for (int i = 0; i < nb; i++) {
            if (Arrays.binarySearch(_idxToRemove, i) < 0) {
                newList.add(list_.get(i));
            }
        }
        return new GISAttributeModelLongList(newList, this);
    }

    /**
   * @param _init
   * @param _attr l'attribut
   */
    public GISAttributeModelLongList(final CtuluListLong _init, final GISAttributeInterface _attr) {
        super(_init);
        attribute_ = _attr;
        if (attribute_ == null) {
            throw new IllegalArgumentException("attribute is null");
        }
    }

    /**
   * @param _init
   * @param _attr l'attribut
   */
    public GISAttributeModelLongList(final long[] _init, final GISAttributeInterface _attr) {
        super(_init);
        attribute_ = _attr;
        if (attribute_ == null) {
            throw new IllegalArgumentException("attribute is null");
        }
    }

    /**
   * @param _initCapacity la capacit� initiale
   * @param _attr l'attribut
   */
    public GISAttributeModelLongList(final int _initCapacity, final GISAttributeInterface _attr) {
        super(_initCapacity);
        attribute_ = _attr;
    }

    public GISAttributeModel createUpperModel(final int _numObject, final TIntIntHashMap _newIdxOldIdx) {
        if (_numObject < getSize()) {
            throw new IllegalArgumentException("bad num objects");
        }
        if (_numObject == getSize()) {
            return this;
        }
        final TLongArrayList newList = new TLongArrayList(_numObject);
        newList.fill(((Integer) getAttribute().getDefaultValue()).intValue());
        if (_newIdxOldIdx != null) {
            final TIntIntIterator it = _newIdxOldIdx.iterator();
            for (int i = _newIdxOldIdx.size(); i-- > 0; ) {
                it.advance();
                final int newIdx = it.key();
                newList.set(newIdx, list_.get(it.value()));
            }
        }
        return new GISAttributeModelLongList(newList, this);
    }

    /**
   * @param _init
   */
    public GISAttributeModelLongList(final TLongArrayList _init) {
        super(_init);
    }

    GISAttributeInterface attribute_;

    protected transient GISAttributeListener listener_;

    protected void fireObjectChanged(int _indexGeom, Object _newValue) {
        if (listener_ != null) {
            listener_.gisDataChanged(attribute_, _indexGeom, _newValue);
        }
    }

    public GISAttributeModel deriveNewModel(final int _numObject, final GISReprojectInterpolateurI _interpol) {
        if (!(_interpol instanceof GISReprojectInterpolateurI.LongTarget)) {
            throw new IllegalArgumentException("bad interface");
        }
        return deriveNewModel(_numObject, (GISReprojectInterpolateurI.LongTarget) _interpol);
    }

    public GISAttributeModelLongInterface deriveNewModel(final int _numObject, final GISReprojectInterpolateurI.LongTarget _interpol) {
        final TLongArrayList newList = new TLongArrayList(_numObject);
        for (int i = 0; i < _numObject; i++) {
            newList.add(_interpol.interpol(i));
        }
        return new GISAttributeModelLongList(newList, this);
    }

    protected final GISAttributeListener getListener() {
        return listener_;
    }

    protected final void setAttribute(final GISAttributeInterface _attribute) {
        attribute_ = _attribute;
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
