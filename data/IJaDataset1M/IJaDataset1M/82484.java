package org.gzigzag.impl;

import java.util.*;
import org.gzigzag.*;
import org.gzigzag.vob.CharRangeIter;

class VStreamDimProxy extends AbstractDim implements VStreamDim, CompoundSpaceDim {

    VStreamDim base;

    final CompoundSpace cs;

    public VStreamDimProxy(CompoundSpace cs, VStreamDim base) {
        super(cs);
        this.cs = cs;
        this.base = new UndoableVStreamDim(base, cs.undo);
    }

    public CopyableDim getBase() {
        return (CopyableDim) base;
    }

    protected VStreamDim getVS(Cell cell) {
        Space sp = cs.getSpace(cell);
        if (sp == null) return base; else return (VStreamDim) sp.getDim(Dims.d_vstream_id);
    }

    public Cell s(Cell c, int steps, Obs o) {
        return cs.translate(getVS(c).s(cs.detranslate(c), steps, o));
    }

    public Cell h(Cell c, int dir, Obs o) {
        return cs.translate(getVS(c).h(cs.detranslate(c), dir, o));
    }

    public void connect(Cell c, Cell d) {
        if (cs.getSpace(c) != cs.getSpace(d)) throw new ZZError("attempted inter-space conn on d.vstream");
        getVS(c).connect(cs.detranslate(c), cs.detranslate(d));
    }

    public void disconnect(Cell c, int dir) {
        getVS(c).disconnect(cs.detranslate(c), dir);
    }

    public boolean isCircularHead(Cell c, Obs o) {
        return getVS(c).isCircularHead(c, o);
    }

    public void insertAfterCell(Cell at, Cell rank) {
        if (cs.getSpace(at) != cs.getSpace(rank)) throw new ZZError("attempted inter-space conn on d.vstream");
        getVS(at).insertAfterCell(cs.detranslate(at), cs.detranslate(rank));
    }

    public void removeRange(Cell first, Cell last) {
        getVS(first).removeRange(cs.detranslate(first), cs.detranslate(last));
    }

    public void iterate(CharRangeIter i, Cell stream, Map extra) {
        getVS(stream).iterate(i, cs.detranslate(stream), extra);
    }

    public void notifyTransclusion(Cell start, int length) {
        getVS(start).notifyTransclusion(cs.detranslate(start), length);
    }

    public void addRealNegSides(Set to) {
        throw new UnsupportedOperationException("Not implemented. Use " + "getBase().addRealNegSides().");
    }

    public void dumpVStreamInfo(Cell c) {
        getVS(c).dumpVStreamInfo(cs.detranslate(c));
    }
}
