package jpdl.patterns.parser.node;

import jpdl.patterns.parser.analysis.*;

@SuppressWarnings("nls")
public final class AFieldPattern extends PPattern {

    private TPathPattern _type_;

    private TPathPattern _path_;

    public AFieldPattern() {
    }

    public AFieldPattern(@SuppressWarnings("hiding") TPathPattern _type_, @SuppressWarnings("hiding") TPathPattern _path_) {
        setType(_type_);
        setPath(_path_);
    }

    @Override
    public Object clone() {
        return new AFieldPattern(cloneNode(this._type_), cloneNode(this._path_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAFieldPattern(this);
    }

    public TPathPattern getType() {
        return this._type_;
    }

    public void setType(TPathPattern node) {
        if (this._type_ != null) {
            this._type_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._type_ = node;
    }

    public TPathPattern getPath() {
        return this._path_;
    }

    public void setPath(TPathPattern node) {
        if (this._path_ != null) {
            this._path_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._path_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._type_) + toString(this._path_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._type_ == child) {
            this._type_ = null;
            return;
        }
        if (this._path_ == child) {
            this._path_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._type_ == oldChild) {
            setType((TPathPattern) newChild);
            return;
        }
        if (this._path_ == oldChild) {
            setPath((TPathPattern) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
