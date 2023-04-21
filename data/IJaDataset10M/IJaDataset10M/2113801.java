package org.sableccsupport.sccparser.node;

import java.util.*;
import org.sableccsupport.sccparser.analysis.*;

@SuppressWarnings("nls")
public final class AAst extends PAst {

    private TAbstract _abstract_;

    private TSyntax _syntax_;

    private TTree _tree_;

    private final LinkedList<PAstProd> _prods_ = new LinkedList<PAstProd>();

    public AAst() {
    }

    public AAst(@SuppressWarnings("hiding") TAbstract _abstract_, @SuppressWarnings("hiding") TSyntax _syntax_, @SuppressWarnings("hiding") TTree _tree_, @SuppressWarnings("hiding") List<PAstProd> _prods_) {
        setAbstract(_abstract_);
        setSyntax(_syntax_);
        setTree(_tree_);
        setProds(_prods_);
    }

    @Override
    public Object clone() {
        return new AAst(cloneNode(this._abstract_), cloneNode(this._syntax_), cloneNode(this._tree_), cloneList(this._prods_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAAst(this);
    }

    public TAbstract getAbstract() {
        return this._abstract_;
    }

    public void setAbstract(TAbstract node) {
        if (this._abstract_ != null) {
            this._abstract_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._abstract_ = node;
    }

    public TSyntax getSyntax() {
        return this._syntax_;
    }

    public void setSyntax(TSyntax node) {
        if (this._syntax_ != null) {
            this._syntax_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._syntax_ = node;
    }

    public TTree getTree() {
        return this._tree_;
    }

    public void setTree(TTree node) {
        if (this._tree_ != null) {
            this._tree_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._tree_ = node;
    }

    public LinkedList<PAstProd> getProds() {
        return this._prods_;
    }

    public void setProds(List<PAstProd> list) {
        this._prods_.clear();
        this._prods_.addAll(list);
        for (PAstProd e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    @Override
    public String toString() {
        return "" + toString(this._abstract_) + toString(this._syntax_) + toString(this._tree_) + toString(this._prods_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._abstract_ == child) {
            this._abstract_ = null;
            return;
        }
        if (this._syntax_ == child) {
            this._syntax_ = null;
            return;
        }
        if (this._tree_ == child) {
            this._tree_ = null;
            return;
        }
        if (this._prods_.remove(child)) {
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._abstract_ == oldChild) {
            setAbstract((TAbstract) newChild);
            return;
        }
        if (this._syntax_ == oldChild) {
            setSyntax((TSyntax) newChild);
            return;
        }
        if (this._tree_ == oldChild) {
            setTree((TTree) newChild);
            return;
        }
        for (ListIterator<PAstProd> i = this._prods_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PAstProd) newChild);
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
