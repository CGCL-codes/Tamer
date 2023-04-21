package fr.ign.cogit.geoxygene.spatial.topoprim;

import java.util.ArrayList;
import java.util.List;

public class TP_Expression {

    /** Liste des termes. */
    protected List<TP_DirectedTopo> term;

    /** Renvoie le terme de rang i. */
    public TP_DirectedTopo getTerm(int i) {
        return this.term.get(i);
    }

    /** Renvoie la liste des termes. */
    public List<TP_DirectedTopo> getTermList() {
        return this.term;
    }

    /** Affecte une valeur au rang i. */
    public void setTerm(int i, TP_DirectedTopo value) {
        this.term.set(i, value);
    }

    /** Ajoute un terme en fin de liste. */
    public void addTerm(TP_DirectedTopo value) {
        this.term.add(value);
    }

    /** Ajoute un terme au rang i. */
    public void addTerm(int i, TP_DirectedTopo value) {
        this.term.add(i, value);
    }

    /** Efface le terme de valeur "value". */
    public void removeTerm(TP_DirectedTopo value) {
        this.term.remove(value);
    }

    /** Efface le terme de rang i. */
    public void removeTerm(int i) {
        this.term.remove(i);
    }

    /** Renvoie le nombre de termes. */
    public int sizeTerm() {
        return this.term.size();
    }

    /** Constructeur par défaut. */
    public TP_Expression() {
        this.term = new ArrayList<TP_DirectedTopo>();
    }

    /** Constructeur à partir d'un TP_DirectedTopo. */
    public TP_Expression(TP_DirectedTopo dt) {
        this.term = new ArrayList<TP_DirectedTopo>();
        this.term.add(dt);
    }

    /** Constructeur à partir de plusieurs TP_DirectedTopo. */
    public TP_Expression(List<TP_DirectedNode> sdt) {
        this.term = new ArrayList<TP_DirectedTopo>();
        for (int i = 0; i < sdt.size(); i++) {
            TP_DirectedTopo dt = sdt.get(i);
            this.term.add(dt);
        }
        this.simplify();
    }

    /** Addition de 2 TP_Expression (self et le TP_Expression passé en paramètre). */
    public TP_Expression plus(TP_Expression s) {
        TP_Expression result = new TP_Expression();
        result.term.addAll(this.term);
        result.term.addAll(s.term);
        result.simplify();
        return result;
    }

    /**
   * Soustraction de 2 TP_Expression (self et le TP_Expression passé en
   * paramètre).
   */
    public TP_Expression minus(TP_Expression s) {
        TP_Expression result = new TP_Expression();
        result.term.addAll(this.term);
        for (int i = 0; i < s.sizeTerm(); i++) {
            TP_DirectedTopo dt = s.getTerm(i);
            TP_DirectedTopo dtnegate = null;
            if (dt instanceof TP_DirectedNode) {
                dtnegate = ((TP_DirectedNode) dt).negate();
            } else if (dt instanceof TP_DirectedEdge) {
                dtnegate = ((TP_DirectedEdge) dt).negate();
            } else if (dt instanceof TP_DirectedFace) {
                dtnegate = ((TP_DirectedFace) dt).negate();
            } else if (dt instanceof TP_DirectedSolid) {
                dtnegate = ((TP_DirectedSolid) dt).negate();
            } else if (dt instanceof TP_Node) {
                dtnegate = ((TP_Node) dt).negate();
            } else if (dt instanceof TP_Edge) {
                dtnegate = ((TP_Edge) dt).negate();
            } else if (dt instanceof TP_Face) {
                dtnegate = ((TP_Face) dt).negate();
            } else if (dt instanceof TP_Solid) {
                dtnegate = ((TP_Solid) dt).negate();
            }
            result.term.add(dtnegate);
        }
        result.simplify();
        return result;
    }

    /** Renvoie l'opposé de self. */
    public TP_Expression negate() {
        TP_Expression result = new TP_Expression();
        int n = this.sizeTerm();
        for (int i = 0; i < n; i++) {
            TP_DirectedTopo dt = this.getTerm(n - i - 1);
            TP_DirectedTopo dtnegate = null;
            if (dt instanceof TP_DirectedNode) {
                dtnegate = ((TP_DirectedNode) dt).negate();
            } else if (dt instanceof TP_DirectedEdge) {
                dtnegate = ((TP_DirectedEdge) dt).negate();
            } else if (dt instanceof TP_DirectedFace) {
                dtnegate = ((TP_DirectedFace) dt).negate();
            } else if (dt instanceof TP_DirectedSolid) {
                dtnegate = ((TP_DirectedSolid) dt).negate();
            } else if (dt instanceof TP_Node) {
                dtnegate = ((TP_Node) dt).negate();
            } else if (dt instanceof TP_Edge) {
                dtnegate = ((TP_Edge) dt).negate();
            } else if (dt instanceof TP_Face) {
                dtnegate = ((TP_Face) dt).negate();
            } else if (dt instanceof TP_Solid) {
                dtnegate = ((TP_Solid) dt).negate();
            }
            result.term.add(dtnegate);
        }
        return result;
    }

    /** TRUE si self est un zéro polynomial. */
    public boolean isZero() {
        this.simplify();
        if (this.sizeTerm() == 0) {
            return true;
        }
        return false;
    }

    /** TRUE si la frontière est zéro. */
    public boolean isCycle() {
        return (this.boundary().isZero());
    }

    /**
   * Remplace chaque boundary de chaque TP_Primitive de chaque TP_DirectedTopo,
   * et simplifie le résultat.
   */
    public TP_Expression boundary() {
        TP_Expression result = new TP_Expression();
        for (int i = 0; i < this.sizeTerm(); i++) {
            TP_DirectedTopo dt = this.getTerm(i);
            TP_Boundary dtbdy = null;
            if (dt instanceof TP_DirectedNode) {
                dtbdy = ((TP_DirectedNode) dt).boundary();
            } else if (dt instanceof TP_DirectedEdge) {
                dtbdy = ((TP_DirectedEdge) dt).boundary();
            } else if (dt instanceof TP_DirectedFace) {
                dtbdy = ((TP_DirectedFace) dt).boundary();
            } else if (dt instanceof TP_DirectedSolid) {
                dtbdy = ((TP_DirectedSolid) dt).boundary();
            } else if (dt instanceof TP_Node) {
                dtbdy = ((TP_Node) dt).boundary();
            } else if (dt instanceof TP_Edge) {
                dtbdy = ((TP_Edge) dt).boundary();
            } else if (dt instanceof TP_Face) {
                dtbdy = ((TP_Face) dt).boundary();
            } else if (dt instanceof TP_Solid) {
                dtbdy = ((TP_Solid) dt).boundary();
            }
            result = result.plus(dtbdy);
        }
        return result;
    }

    /** TRUE s'il y a égalité polynomiale. */
    public boolean equals(TP_Expression s) {
        TP_Expression thisBis = new TP_Expression();
        for (int i = 0; i < this.sizeTerm(); i++) {
            thisBis.addTerm(this.getTerm(i));
        }
        thisBis.simplify();
        TP_Expression sBis = new TP_Expression();
        for (int i = 0; i < s.sizeTerm(); i++) {
            sBis.addTerm(s.getTerm(i));
        }
        sBis.simplify();
        if (thisBis.sizeTerm() != sBis.sizeTerm()) {
            return false;
        }
        for (int i = 0; i < thisBis.sizeTerm(); i++) {
            TP_DirectedTopo dt1 = thisBis.getTerm(i);
            boolean trouve = false;
            for (int j = 0; j < sBis.sizeTerm(); j++) {
                TP_DirectedTopo dt2 = sBis.getTerm(j);
                if (dt1.getId() == dt2.getId()) {
                    trouve = true;
                }
            }
            if (!trouve) {
                return false;
            }
        }
        return true;
    }

    /** Cast en liste de TP_Primitive. Dans la norme, on convertit en set. */
    public List<TP_Primitive> support() {
        List<TP_Primitive> result = new ArrayList<TP_Primitive>();
        for (int i = 0; i < this.sizeTerm(); i++) {
            TP_DirectedTopo dt = this.getTerm(i);
            TP_Primitive prim = null;
            if (dt instanceof TP_DirectedNode) {
                prim = ((TP_DirectedNode) dt).topo();
            } else if (dt instanceof TP_DirectedEdge) {
                prim = ((TP_DirectedEdge) dt).topo();
            } else if (dt instanceof TP_DirectedFace) {
                prim = ((TP_DirectedFace) dt).topo();
            } else if (dt instanceof TP_DirectedSolid) {
                prim = ((TP_DirectedSolid) dt).topo();
            } else if (dt instanceof TP_Node) {
                prim = ((TP_Node) dt).topo();
            } else if (dt instanceof TP_Edge) {
                prim = ((TP_Edge) dt).topo();
            } else if (dt instanceof TP_Face) {
                prim = ((TP_Face) dt).topo();
            } else if (dt instanceof TP_Solid) {
                prim = ((TP_Solid) dt).topo();
            }
            result.add(prim);
        }
        return result;
    }

    /**
   * Cast chaque coBoundary de chaque TP_Primitive de chaque TP_DirectedTopo en
   * TP_Expression, et simplifie le résultat.
   */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public TP_Expression coBoundary() {
        TP_Expression result = new TP_Expression();
        for (int i = 0; i < this.sizeTerm(); i++) {
            TP_DirectedTopo dt = this.getTerm(i);
            List theCoBoundary = null;
            if (dt instanceof TP_DirectedNode) {
                theCoBoundary = ((TP_DirectedNode) dt).coBoundary();
            } else if (dt instanceof TP_DirectedEdge) {
                theCoBoundary = ((TP_DirectedEdge) dt).coBoundary();
            } else if (dt instanceof TP_DirectedFace) {
                theCoBoundary = ((TP_DirectedFace) dt).coBoundary();
            } else if (dt instanceof TP_DirectedSolid) {
                theCoBoundary = ((TP_DirectedSolid) dt).coBoundary();
            } else if (dt instanceof TP_Node) {
                theCoBoundary = ((TP_Node) dt).coBoundary();
            } else if (dt instanceof TP_Edge) {
                theCoBoundary = ((TP_Edge) dt).coBoundary();
            } else if (dt instanceof TP_Face) {
                theCoBoundary = ((TP_Face) dt).coBoundary();
            } else if (dt instanceof TP_Solid) {
                theCoBoundary = ((TP_Solid) dt).coBoundary();
            }
            TP_Expression tpe = new TP_Expression(theCoBoundary);
            result = result.plus(tpe);
        }
        return result;
    }

    /** Cast en liste de TP_DirectedTopo. Dans la norme, on convertit en set. */
    public List<TP_DirectedTopo> asSet() {
        List<TP_DirectedTopo> result = new ArrayList<TP_DirectedTopo>();
        for (int i = 0; i < this.sizeTerm(); i++) {
            TP_DirectedTopo dt = this.getTerm(i);
            result.add(dt);
        }
        return result;
    }

    /**
   * = Usage interne. Simplifie le TP_Expression en annulant les termes opposés.
   */
    private void simplify() {
        int n = this.sizeTerm();
        if (n > 1) {
            for (int i = 0; i < n - 1; i++) {
                TP_DirectedTopo dt1 = this.getTerm(i);
                for (int j = i + 1; j < n; j++) {
                    TP_DirectedTopo dt2 = this.getTerm(j);
                    if (dt1.getId() == -dt2.getId()) {
                        this.removeTerm(j);
                        this.removeTerm(i);
                        n = n - 2;
                        i = i - 1;
                        break;
                    }
                }
            }
        }
    }
}
