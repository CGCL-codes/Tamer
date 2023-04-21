package iwork.patchpanel.manager.script.node;

import java.util.*;
import iwork.patchpanel.manager.script.analysis.*;

public final class AHeadLine extends PHeadLine {

    private TGroup _group_;

    private PGroupname _groupname_;

    private TSemicolon _semicolon_;

    public AHeadLine() {
    }

    public AHeadLine(TGroup _group_, PGroupname _groupname_, TSemicolon _semicolon_) {
        setGroup(_group_);
        setGroupname(_groupname_);
        setSemicolon(_semicolon_);
    }

    public Object clone() {
        return new AHeadLine((TGroup) cloneNode(_group_), (PGroupname) cloneNode(_groupname_), (TSemicolon) cloneNode(_semicolon_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAHeadLine(this);
    }

    public TGroup getGroup() {
        return _group_;
    }

    public void setGroup(TGroup node) {
        if (_group_ != null) {
            _group_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _group_ = node;
    }

    public PGroupname getGroupname() {
        return _groupname_;
    }

    public void setGroupname(PGroupname node) {
        if (_groupname_ != null) {
            _groupname_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _groupname_ = node;
    }

    public TSemicolon getSemicolon() {
        return _semicolon_;
    }

    public void setSemicolon(TSemicolon node) {
        if (_semicolon_ != null) {
            _semicolon_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _semicolon_ = node;
    }

    public String toString() {
        return "" + toString(_group_) + toString(_groupname_) + toString(_semicolon_);
    }

    void removeChild(Node child) {
        if (_group_ == child) {
            _group_ = null;
            return;
        }
        if (_groupname_ == child) {
            _groupname_ = null;
            return;
        }
        if (_semicolon_ == child) {
            _semicolon_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_group_ == oldChild) {
            setGroup((TGroup) newChild);
            return;
        }
        if (_groupname_ == oldChild) {
            setGroupname((PGroupname) newChild);
            return;
        }
        if (_semicolon_ == oldChild) {
            setSemicolon((TSemicolon) newChild);
            return;
        }
    }
}
