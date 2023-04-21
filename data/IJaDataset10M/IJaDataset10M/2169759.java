package com.memoire.fdcalc;

public class Exp extends Liste {

    public final String retour() {
        String temp = new String("exp(");
        for (int j = 0; j < liste_.size(); j++) {
            if (estOp(j)) temp = temp.concat(op(j).retour()); else temp = temp.concat(expr(j).retour());
        }
        if (fin_) temp = temp.concat(")");
        return temp;
    }

    public Nombre evalue() throws ExceptionDeSyntaxe {
        if (taille() == 0) throw new ExceptionDeSyntaxe("exp sans argument");
        calcul();
        r_.changePar(Math.exp(r_.valeur()));
        return r_;
    }
}
