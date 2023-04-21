package tudresden.ocl20.pivot.ocl2parser.testcasegenerator.gen.testcasegenerator.node;

import java.util.*;
import tudresden.ocl20.pivot.ocl2parser.testcasegenerator.gen.testcasegenerator.analysis.*;

public final class AAbstractModel extends PAbstractModel {

    private TAbstractmodel _abstractmodel_;

    private TBraceOpen _braceOpen_;

    private TFirstName _firstName_;

    private TAssign _assign_;

    private PModelExpression _modelExpression_;

    private final LinkedList _assignment_ = new TypedLinkedList(new Assignment_Cast());

    private TBraceClose _braceClose_;

    public AAbstractModel() {
    }

    public AAbstractModel(TAbstractmodel _abstractmodel_, TBraceOpen _braceOpen_, TFirstName _firstName_, TAssign _assign_, PModelExpression _modelExpression_, List _assignment_, TBraceClose _braceClose_) {
        setAbstractmodel(_abstractmodel_);
        setBraceOpen(_braceOpen_);
        setFirstName(_firstName_);
        setAssign(_assign_);
        setModelExpression(_modelExpression_);
        {
            this._assignment_.clear();
            this._assignment_.addAll(_assignment_);
        }
        setBraceClose(_braceClose_);
    }

    public AAbstractModel(TAbstractmodel _abstractmodel_, TBraceOpen _braceOpen_, TFirstName _firstName_, TAssign _assign_, PModelExpression _modelExpression_, XPAssignment _assignment_, TBraceClose _braceClose_) {
        setAbstractmodel(_abstractmodel_);
        setBraceOpen(_braceOpen_);
        setFirstName(_firstName_);
        setAssign(_assign_);
        setModelExpression(_modelExpression_);
        if (_assignment_ != null) {
            while (_assignment_ instanceof X1PAssignment) {
                this._assignment_.addFirst(((X1PAssignment) _assignment_).getPAssignment());
                _assignment_ = ((X1PAssignment) _assignment_).getXPAssignment();
            }
            this._assignment_.addFirst(((X2PAssignment) _assignment_).getPAssignment());
        }
        setBraceClose(_braceClose_);
    }

    public Object clone() {
        return new AAbstractModel((TAbstractmodel) cloneNode(_abstractmodel_), (TBraceOpen) cloneNode(_braceOpen_), (TFirstName) cloneNode(_firstName_), (TAssign) cloneNode(_assign_), (PModelExpression) cloneNode(_modelExpression_), cloneList(_assignment_), (TBraceClose) cloneNode(_braceClose_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAAbstractModel(this);
    }

    public Object apply(SwitchWithReturn sw, Object param) throws AttrEvalException {
        return ((AnalysisWithReturn) sw).caseAAbstractModel(this, param);
    }

    public TAbstractmodel getAbstractmodel() {
        return _abstractmodel_;
    }

    public void setAbstractmodel(TAbstractmodel node) {
        if (_abstractmodel_ != null) {
            _abstractmodel_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _abstractmodel_ = node;
    }

    public TBraceOpen getBraceOpen() {
        return _braceOpen_;
    }

    public void setBraceOpen(TBraceOpen node) {
        if (_braceOpen_ != null) {
            _braceOpen_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _braceOpen_ = node;
    }

    public TFirstName getFirstName() {
        return _firstName_;
    }

    public void setFirstName(TFirstName node) {
        if (_firstName_ != null) {
            _firstName_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _firstName_ = node;
    }

    public TAssign getAssign() {
        return _assign_;
    }

    public void setAssign(TAssign node) {
        if (_assign_ != null) {
            _assign_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _assign_ = node;
    }

    public PModelExpression getModelExpression() {
        return _modelExpression_;
    }

    public void setModelExpression(PModelExpression node) {
        if (_modelExpression_ != null) {
            _modelExpression_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _modelExpression_ = node;
    }

    public LinkedList getAssignment() {
        return _assignment_;
    }

    public void setAssignment(List list) {
        _assignment_.clear();
        _assignment_.addAll(list);
    }

    public TBraceClose getBraceClose() {
        return _braceClose_;
    }

    public void setBraceClose(TBraceClose node) {
        if (_braceClose_ != null) {
            _braceClose_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _braceClose_ = node;
    }

    public String toString() {
        return "" + toString(_abstractmodel_) + toString(_braceOpen_) + toString(_firstName_) + toString(_assign_) + toString(_modelExpression_) + toString(_assignment_) + toString(_braceClose_);
    }

    void removeChild(Node child) {
        if (_abstractmodel_ == child) {
            _abstractmodel_ = null;
            return;
        }
        if (_braceOpen_ == child) {
            _braceOpen_ = null;
            return;
        }
        if (_firstName_ == child) {
            _firstName_ = null;
            return;
        }
        if (_assign_ == child) {
            _assign_ = null;
            return;
        }
        if (_modelExpression_ == child) {
            _modelExpression_ = null;
            return;
        }
        if (_assignment_.remove(child)) {
            return;
        }
        if (_braceClose_ == child) {
            _braceClose_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_abstractmodel_ == oldChild) {
            setAbstractmodel((TAbstractmodel) newChild);
            return;
        }
        if (_braceOpen_ == oldChild) {
            setBraceOpen((TBraceOpen) newChild);
            return;
        }
        if (_firstName_ == oldChild) {
            setFirstName((TFirstName) newChild);
            return;
        }
        if (_assign_ == oldChild) {
            setAssign((TAssign) newChild);
            return;
        }
        if (_modelExpression_ == oldChild) {
            setModelExpression((PModelExpression) newChild);
            return;
        }
        for (ListIterator i = _assignment_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set(newChild);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        if (_braceClose_ == oldChild) {
            setBraceClose((TBraceClose) newChild);
            return;
        }
    }

    private class Assignment_Cast implements Cast {

        public Object cast(Object o) {
            PAssignment node = (PAssignment) o;
            if ((node.parent() != null) && (node.parent() != AAbstractModel.this)) {
                node.parent().removeChild(node);
            }
            if ((node.parent() == null) || (node.parent() != AAbstractModel.this)) {
                node.parent(AAbstractModel.this);
            }
            return node;
        }
    }
}
