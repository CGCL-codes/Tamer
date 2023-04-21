package com.memoire.fdcalc;

import java.lang.*;

public class Ln extends Liste {

    public final String retour() {
        String temp = new String("ln(");
        for (int j = 0; j < liste_.size(); j++) {
            if (estOp(j)) temp = temp.concat(op(j).retour()); else temp = temp.concat(expr(j).retour());
        }
        if (fin_) temp = temp.concat(")");
        return temp;
    }

    public Nombre evalue() throws ExceptionDeSyntaxe {
        if (taille() == 0) throw new ExceptionDeSyntaxe("ln sans argument");
        calcul();
        if (r_.valeur() > 0) r_.changePar(Math.log(r_.valeur())); else throw new ExceptionDeSyntaxe("ln(a) valide si a>O");
        return r_;
    }
}
