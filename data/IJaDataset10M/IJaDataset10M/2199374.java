package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AP1ListOfAssignments extends PListOfAssignments {

    private PAssignment _assignment_;

    private TTComma _tComma_;

    private PListOfAssignments _listOfAssignments_;

    public AP1ListOfAssignments() {
    }

    public AP1ListOfAssignments(@SuppressWarnings("hiding") PAssignment _assignment_, @SuppressWarnings("hiding") TTComma _tComma_, @SuppressWarnings("hiding") PListOfAssignments _listOfAssignments_) {
        setAssignment(_assignment_);
        setTComma(_tComma_);
        setListOfAssignments(_listOfAssignments_);
    }

    @Override
    public Object clone() {
        return new AP1ListOfAssignments(cloneNode(this._assignment_), cloneNode(this._tComma_), cloneNode(this._listOfAssignments_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAP1ListOfAssignments(this);
    }

    public PAssignment getAssignment() {
        return this._assignment_;
    }

    public void setAssignment(PAssignment node) {
        if (this._assignment_ != null) {
            this._assignment_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._assignment_ = node;
    }

    public TTComma getTComma() {
        return this._tComma_;
    }

    public void setTComma(TTComma node) {
        if (this._tComma_ != null) {
            this._tComma_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._tComma_ = node;
    }

    public PListOfAssignments getListOfAssignments() {
        return this._listOfAssignments_;
    }

    public void setListOfAssignments(PListOfAssignments node) {
        if (this._listOfAssignments_ != null) {
            this._listOfAssignments_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._listOfAssignments_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._assignment_) + toString(this._tComma_) + toString(this._listOfAssignments_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._assignment_ == child) {
            this._assignment_ = null;
            return;
        }
        if (this._tComma_ == child) {
            this._tComma_ = null;
            return;
        }
        if (this._listOfAssignments_ == child) {
            this._listOfAssignments_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._assignment_ == oldChild) {
            setAssignment((PAssignment) newChild);
            return;
        }
        if (this._tComma_ == oldChild) {
            setTComma((TTComma) newChild);
            return;
        }
        if (this._listOfAssignments_ == oldChild) {
            setListOfAssignments((PListOfAssignments) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
