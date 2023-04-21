package org.zamia.verilog.node;

import java.util.*;
import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AP1PortExpression extends PPortExpression {

    private TTLbrace _tLbrace_;

    private PPortReference _portReference_;

    private final LinkedList<PPortExpressionRep> _portExpressionRep_ = new LinkedList<PPortExpressionRep>();

    private TTRbrace _tRbrace_;

    public AP1PortExpression() {
    }

    public AP1PortExpression(@SuppressWarnings("hiding") TTLbrace _tLbrace_, @SuppressWarnings("hiding") PPortReference _portReference_, @SuppressWarnings("hiding") List<PPortExpressionRep> _portExpressionRep_, @SuppressWarnings("hiding") TTRbrace _tRbrace_) {
        setTLbrace(_tLbrace_);
        setPortReference(_portReference_);
        setPortExpressionRep(_portExpressionRep_);
        setTRbrace(_tRbrace_);
    }

    @Override
    public Object clone() {
        return new AP1PortExpression(cloneNode(this._tLbrace_), cloneNode(this._portReference_), cloneList(this._portExpressionRep_), cloneNode(this._tRbrace_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAP1PortExpression(this);
    }

    public TTLbrace getTLbrace() {
        return this._tLbrace_;
    }

    public void setTLbrace(TTLbrace node) {
        if (this._tLbrace_ != null) {
            this._tLbrace_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._tLbrace_ = node;
    }

    public PPortReference getPortReference() {
        return this._portReference_;
    }

    public void setPortReference(PPortReference node) {
        if (this._portReference_ != null) {
            this._portReference_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._portReference_ = node;
    }

    public LinkedList<PPortExpressionRep> getPortExpressionRep() {
        return this._portExpressionRep_;
    }

    public void setPortExpressionRep(List<PPortExpressionRep> list) {
        this._portExpressionRep_.clear();
        this._portExpressionRep_.addAll(list);
        for (PPortExpressionRep e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    public TTRbrace getTRbrace() {
        return this._tRbrace_;
    }

    public void setTRbrace(TTRbrace node) {
        if (this._tRbrace_ != null) {
            this._tRbrace_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._tRbrace_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._tLbrace_) + toString(this._portReference_) + toString(this._portExpressionRep_) + toString(this._tRbrace_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._tLbrace_ == child) {
            this._tLbrace_ = null;
            return;
        }
        if (this._portReference_ == child) {
            this._portReference_ = null;
            return;
        }
        if (this._portExpressionRep_.remove(child)) {
            return;
        }
        if (this._tRbrace_ == child) {
            this._tRbrace_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._tLbrace_ == oldChild) {
            setTLbrace((TTLbrace) newChild);
            return;
        }
        if (this._portReference_ == oldChild) {
            setPortReference((PPortReference) newChild);
            return;
        }
        for (ListIterator<PPortExpressionRep> i = this._portExpressionRep_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PPortExpressionRep) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        if (this._tRbrace_ == oldChild) {
            setTRbrace((TTRbrace) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
