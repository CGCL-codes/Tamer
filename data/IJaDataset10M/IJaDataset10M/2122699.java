package com.guig.maths;

import com.guig.maths.language.Binaire;
import com.guig.maths.language.Constante;
import com.guig.maths.language.Division;
import com.guig.maths.language.IOperande;
import com.guig.maths.language.Moins;
import com.guig.maths.language.MoinsUnaire;
import com.guig.maths.language.Multiplication;
import com.guig.maths.language.Plus;
import com.guig.maths.language.Unaire;
import com.guig.maths.language.X;

public class Maths {

    private IOperande arbre;

    public IOperande getArbre() {
        return arbre;
    }

    public Maths(String maths) {
        maths = maths.toLowerCase();
        arbre = lecture(maths);
    }

    public void simplification() {
        IOperande o = simplification(arbre);
        while (o != null) {
            arbre = o;
            o = simplification(arbre);
        }
    }

    public void trie() {
        while (trie(arbre)) ;
    }

    private boolean trie(IOperande operande) {
        boolean trie = false;
        if (operande instanceof Plus) {
            if (((Binaire) operande).getRight() instanceof X && !(((Binaire) operande).getLeft() instanceof X)) {
                IOperande o = ((Binaire) operande).getRight();
                ((Binaire) operande).setRight(((Binaire) operande).getLeft());
                ((Binaire) operande).setLeft(o);
                trie |= true;
            }
            if (((Binaire) operande).getRight() instanceof Plus && ((Plus) ((Binaire) operande).getRight()).getLeft() instanceof X && !(((Binaire) operande).getLeft() instanceof X)) {
                IOperande o = ((Plus) ((Binaire) operande).getRight()).getLeft();
                ((Plus) ((Binaire) operande).getRight()).setLeft(((Binaire) operande).getLeft());
                ((Binaire) operande).setLeft(o);
                trie |= true;
            }
            if (((Binaire) operande).getLeft() instanceof Plus && !(((Plus) ((Binaire) operande).getLeft()).getRight() instanceof X) && ((Binaire) operande).getRight() instanceof X) {
                IOperande o = ((Plus) ((Binaire) operande).getLeft()).getRight();
                ((Plus) ((Binaire) operande).getLeft()).setRight(((Binaire) operande).getRight());
                ((Binaire) operande).setRight(o);
                trie |= true;
            }
            if (((Binaire) operande).getLeft() instanceof Multiplication && (((Binaire) operande).getRight() instanceof Multiplication && ((Multiplication) ((Binaire) operande).getRight()).getRight() instanceof X && !(((Multiplication) ((Binaire) operande).getLeft()).getRight() instanceof X)) || (((Binaire) operande).getRight() instanceof Multiplication && ((Multiplication) ((Binaire) operande).getRight()).getRight() instanceof X && !(((Binaire) operande).getLeft() instanceof X))) {
                IOperande o = ((Binaire) operande).getRight();
                ((Binaire) operande).setRight(((Binaire) operande).getLeft());
                ((Binaire) operande).setLeft(o);
                trie |= true;
            }
            if (operande instanceof Unaire) trie |= trie(((Unaire) operande).getUnique());
            if (operande instanceof Binaire) trie |= trie(((Binaire) operande).getLeft());
            trie |= trie(((Binaire) operande).getRight());
        }
        if (operande instanceof Multiplication) {
            if (!(((Binaire) operande).getRight() instanceof X) && ((Binaire) operande).getLeft() instanceof X) {
                IOperande o = ((Binaire) operande).getRight();
                ((Binaire) operande).setRight(((Binaire) operande).getLeft());
                ((Binaire) operande).setLeft(o);
                trie |= true;
            }
        }
        if (operande instanceof Binaire) {
            trie |= trie(((Binaire) operande).getLeft());
            trie |= trie(((Binaire) operande).getRight());
        } else if (operande instanceof Unaire) trie |= trie(((Unaire) operande).getUnique());
        return trie;
    }

    private IOperande simplification(IOperande operande) {
        if (operande instanceof Division) if (((Binaire) operande).getLeft() instanceof Constante) if (((Constante) ((Binaire) operande).getLeft()).getValue().equals(new Double(0.0))) {
            Constante c = new Constante();
            c.setValue(0.0);
            return c;
        }
        if (operande instanceof Multiplication) if (((Binaire) operande).getLeft() instanceof Constante) if (((Constante) ((Binaire) operande).getLeft()).getValue().equals(new Double(0.0))) {
            Constante c = new Constante();
            c.setValue(0.0);
            return c;
        }
        if (operande instanceof Multiplication) if (((Binaire) operande).getRight() instanceof Constante) if (((Constante) ((Binaire) operande).getRight()).getValue().equals(new Double(0.0))) {
            Constante c = new Constante();
            c.setValue(0.0);
            return c;
        }
        if (operande instanceof Moins) if (((Binaire) operande).getRight() instanceof Constante) if (((Constante) ((Binaire) operande).getRight()).getValue().equals(new Double(0.0))) {
            IOperande c = ((Binaire) operande).getLeft();
            return c;
        }
        if (operande instanceof Moins) if (((Binaire) operande).getLeft() instanceof Constante) if (((Constante) ((Binaire) operande).getLeft()).getValue().equals(new Double(0.0))) {
            MoinsUnaire m = new MoinsUnaire();
            m.setUnique(((Binaire) operande).getRight());
            return m;
        }
        if (operande instanceof Plus) if (((Binaire) operande).getLeft() instanceof Constante) if (((Constante) ((Binaire) operande).getLeft()).getValue().equals(new Double(0.0))) {
            IOperande c = ((Binaire) operande).getRight();
            return c;
        }
        if (operande instanceof Plus) if (((Binaire) operande).getRight() instanceof Constante) if (((Constante) ((Binaire) operande).getRight()).getValue().equals(new Double(0.0))) {
            IOperande c = ((Binaire) operande).getLeft();
            return c;
        }
        if (operande instanceof Binaire) if (((Binaire) operande).getLeft() instanceof Constante) if (((Binaire) operande).getRight() instanceof Constante) {
            Constante c = new Constante();
            c.setValue(operande.compute());
            if (c.isInteger()) return c;
        }
        if (operande instanceof Unaire) if (((Unaire) operande).getUnique() instanceof Constante) {
            Constante c = new Constante();
            c.setValue(operande.compute());
            if (c.isInteger()) return c;
        }
        if (operande instanceof Plus) if (((Binaire) operande).getLeft() instanceof Moins) if (((Binaire) operande).getRight() instanceof Constante) if (((Binaire) ((Binaire) operande).getLeft()).getRight() instanceof Constante) {
            Constante a = (Constante) ((Binaire) operande).getRight();
            Constante b = (Constante) ((Binaire) ((Binaire) operande).getLeft()).getRight();
            IOperande o = ((Binaire) ((Binaire) operande).getLeft()).getLeft();
            Constante c = new Constante();
            c.setValue(a.getValue() - b.getValue());
            Plus p = new Plus();
            p.setLeft(o);
            p.setRight(c);
            if (c.isInteger()) {
                return p;
            }
        }
        if (operande instanceof Plus) if (((Binaire) operande).getLeft() instanceof Plus) if (((Binaire) operande).getRight() instanceof Constante) if (((Binaire) ((Binaire) operande).getLeft()).getRight() instanceof Constante) {
            Constante a = (Constante) ((Binaire) operande).getRight();
            Constante b = (Constante) ((Binaire) ((Binaire) operande).getLeft()).getRight();
            IOperande o = ((Binaire) ((Binaire) operande).getLeft()).getLeft();
            Constante c = new Constante();
            c.setValue(a.getValue() + b.getValue());
            Plus p = new Plus();
            p.setLeft(o);
            p.setRight(c);
            if (c.isInteger()) {
                return p;
            }
        }
        if (operande instanceof Moins) if (((Binaire) operande).getLeft() instanceof Plus) if (((Binaire) operande).getRight() instanceof Constante) if (((Binaire) ((Binaire) operande).getLeft()).getRight() instanceof Constante) {
            Constante a = (Constante) ((Binaire) operande).getRight();
            Constante b = (Constante) ((Binaire) ((Binaire) operande).getLeft()).getRight();
            IOperande o = ((Binaire) ((Binaire) operande).getLeft()).getLeft();
            Constante c = new Constante();
            c.setValue(-a.getValue() + b.getValue());
            Plus p = new Plus();
            p.setLeft(o);
            p.setRight(c);
            if (c.isInteger()) {
                return p;
            }
        }
        if (operande instanceof Binaire) {
            IOperande o = simplification(((Binaire) operande).getRight());
            if (o != null) {
                ((Binaire) operande).setRight(o);
                return operande;
            }
            o = simplification(((Binaire) operande).getLeft());
            if (o != null) {
                ((Binaire) operande).setLeft(o);
                return operande;
            }
        } else if (operande instanceof Unaire) {
            IOperande o = simplification(((Unaire) operande).getUnique());
            if (o != null) {
                ((Unaire) operande).setUnique(o);
                return operande;
            }
        }
        return null;
    }

    public IOperande lecture(String maths) {
        maths = maths.trim();
        IOperande cc = null;
        cc = Plus.scan(mask(maths), maths, this);
        return cc;
    }

    private String mask(String maths) {
        StringBuffer mask = new StringBuffer();
        int rr = 0;
        for (int j = 0; j < maths.length(); j++) {
            char cc = maths.charAt(j);
            if (cc == '(') rr++;
            if (rr != 0) mask.append("_"); else mask.append(cc);
            if (cc == ')') rr--;
        }
        return mask.toString();
    }

    public IOperande parenthese(String maths) {
        int r = 0, memo = 0;
        for (int i = 0; i < maths.length(); i++) {
            char c = maths.charAt(i);
            if (c == '(') r++; else if (c == ')') r--;
            if (memo != r && r == 0) {
                IOperande o = lecture(maths.substring(1, i));
                return o;
            }
            memo = r;
        }
        return null;
    }
}
