package galoot.node;

import java.util.*;
import galoot.analysis.*;

@SuppressWarnings("nls")
public final class AWithBlock extends PWithBlock {

    private PVarExpression _expression_;

    private TId _var_;

    private final LinkedList<PEntity> _entities_ = new LinkedList<PEntity>();

    public AWithBlock() {
    }

    public AWithBlock(@SuppressWarnings("hiding") PVarExpression _expression_, @SuppressWarnings("hiding") TId _var_, @SuppressWarnings("hiding") List<PEntity> _entities_) {
        setExpression(_expression_);
        setVar(_var_);
        setEntities(_entities_);
    }

    @Override
    public Object clone() {
        return new AWithBlock(cloneNode(this._expression_), cloneNode(this._var_), cloneList(this._entities_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAWithBlock(this);
    }

    public PVarExpression getExpression() {
        return this._expression_;
    }

    public void setExpression(PVarExpression node) {
        if (this._expression_ != null) {
            this._expression_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._expression_ = node;
    }

    public TId getVar() {
        return this._var_;
    }

    public void setVar(TId node) {
        if (this._var_ != null) {
            this._var_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._var_ = node;
    }

    public LinkedList<PEntity> getEntities() {
        return this._entities_;
    }

    public void setEntities(List<PEntity> list) {
        this._entities_.clear();
        this._entities_.addAll(list);
        for (PEntity e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    @Override
    public String toString() {
        return "" + toString(this._expression_) + toString(this._var_) + toString(this._entities_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._expression_ == child) {
            this._expression_ = null;
            return;
        }
        if (this._var_ == child) {
            this._var_ = null;
            return;
        }
        if (this._entities_.remove(child)) {
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._expression_ == oldChild) {
            setExpression((PVarExpression) newChild);
            return;
        }
        if (this._var_ == oldChild) {
            setVar((TId) newChild);
            return;
        }
        for (ListIterator<PEntity> i = this._entities_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PEntity) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        throw new RuntimeException("Not a child.");
    }
}
