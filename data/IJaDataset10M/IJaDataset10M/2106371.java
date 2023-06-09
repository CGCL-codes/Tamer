package net.sourceforge.texlipse.bibparser.node;

import net.sourceforge.texlipse.bibparser.analysis.*;

@SuppressWarnings("nls")
public final class AEntryDef extends PEntryDef {

    private TEntryName _entryName_;

    public AEntryDef() {
    }

    public AEntryDef(@SuppressWarnings("hiding") TEntryName _entryName_) {
        setEntryName(_entryName_);
    }

    @Override
    public Object clone() {
        return new AEntryDef(cloneNode(this._entryName_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAEntryDef(this);
    }

    public TEntryName getEntryName() {
        return this._entryName_;
    }

    public void setEntryName(TEntryName node) {
        if (this._entryName_ != null) {
            this._entryName_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._entryName_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._entryName_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._entryName_ == child) {
            this._entryName_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._entryName_ == oldChild) {
            setEntryName((TEntryName) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
