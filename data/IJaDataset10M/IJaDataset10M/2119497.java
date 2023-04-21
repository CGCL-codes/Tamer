package org.omwg.mediation.parser.hrsyntax.node;

import java.util.*;
import org.omwg.mediation.parser.hrsyntax.analysis.*;

public final class ADocumentid extends PDocumentid {

    private PIri _iri_;

    public ADocumentid() {
    }

    public ADocumentid(PIri _iri_) {
        setIri(_iri_);
    }

    public Object clone() {
        return new ADocumentid((PIri) cloneNode(_iri_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseADocumentid(this);
    }

    public PIri getIri() {
        return _iri_;
    }

    public void setIri(PIri node) {
        if (_iri_ != null) {
            _iri_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _iri_ = node;
    }

    public String toString() {
        return "" + toString(_iri_);
    }

    void removeChild(Node child) {
        if (_iri_ == child) {
            _iri_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_iri_ == oldChild) {
            setIri((PIri) newChild);
            return;
        }
    }
}
