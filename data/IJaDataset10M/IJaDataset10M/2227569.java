package galoot.node;

import java.util.*;
import galoot.analysis.*;

@SuppressWarnings("nls")
public final class AFirstOfEntity extends PEntity {

    private final LinkedList<PArgument> _args_ = new LinkedList<PArgument>();

    public AFirstOfEntity() {
    }

    public AFirstOfEntity(@SuppressWarnings("hiding") List<PArgument> _args_) {
        setArgs(_args_);
    }

    @Override
    public Object clone() {
        return new AFirstOfEntity(cloneList(this._args_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAFirstOfEntity(this);
    }

    public LinkedList<PArgument> getArgs() {
        return this._args_;
    }

    public void setArgs(List<PArgument> list) {
        this._args_.clear();
        this._args_.addAll(list);
        for (PArgument e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    @Override
    public String toString() {
        return "" + toString(this._args_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._args_.remove(child)) {
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        for (ListIterator<PArgument> i = this._args_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PArgument) newChild);
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
