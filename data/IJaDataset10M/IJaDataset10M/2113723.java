package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;

@SuppressWarnings("nls")
public final class ASingleStaticImportDeclaration extends PSingleStaticImportDeclaration {

    private TImport _import_;

    private TStatic _static_;

    private PName _name_;

    private TSemicolon _semicolon_;

    public ASingleStaticImportDeclaration() {
    }

    public ASingleStaticImportDeclaration(@SuppressWarnings("hiding") TImport _import_, @SuppressWarnings("hiding") TStatic _static_, @SuppressWarnings("hiding") PName _name_, @SuppressWarnings("hiding") TSemicolon _semicolon_) {
        setImport(_import_);
        setStatic(_static_);
        setName(_name_);
        setSemicolon(_semicolon_);
    }

    @Override
    public Object clone() {
        return new ASingleStaticImportDeclaration(cloneNode(this._import_), cloneNode(this._static_), cloneNode(this._name_), cloneNode(this._semicolon_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseASingleStaticImportDeclaration(this);
    }

    public TImport getImport() {
        return this._import_;
    }

    public void setImport(TImport node) {
        if (this._import_ != null) {
            this._import_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._import_ = node;
    }

    public TStatic getStatic() {
        return this._static_;
    }

    public void setStatic(TStatic node) {
        if (this._static_ != null) {
            this._static_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._static_ = node;
    }

    public PName getName() {
        return this._name_;
    }

    public void setName(PName node) {
        if (this._name_ != null) {
            this._name_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._name_ = node;
    }

    public TSemicolon getSemicolon() {
        return this._semicolon_;
    }

    public void setSemicolon(TSemicolon node) {
        if (this._semicolon_ != null) {
            this._semicolon_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._semicolon_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._import_) + toString(this._static_) + toString(this._name_) + toString(this._semicolon_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._import_ == child) {
            this._import_ = null;
            return;
        }
        if (this._static_ == child) {
            this._static_ = null;
            return;
        }
        if (this._name_ == child) {
            this._name_ = null;
            return;
        }
        if (this._semicolon_ == child) {
            this._semicolon_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._import_ == oldChild) {
            setImport((TImport) newChild);
            return;
        }
        if (this._static_ == oldChild) {
            setStatic((TStatic) newChild);
            return;
        }
        if (this._name_ == oldChild) {
            setName((PName) newChild);
            return;
        }
        if (this._semicolon_ == oldChild) {
            setSemicolon((TSemicolon) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
