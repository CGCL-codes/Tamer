package PrologPlusCG;

import java.io.IOException;
import java.util.Vector;

public class Concept implements java.io.Serializable, TypesDonnees {

    CDonneePrlg ms_Type;

    CDonneePrlg ms_Ref;

    CDonneePrlg mc_Val;

    Vector mv_RelEntrs;

    Vector mv_RelSorts;

    Concept() {
        ms_Type = null;
        ms_Ref = null;
        mc_Val = null;
        mv_RelEntrs = new Vector(4, 2);
        mv_RelSorts = new Vector(4, 2);
    }

    Concept(CDonneePrlg Typ, CDonneePrlg Ref, CDonneePrlg Val) {
        ms_Type = Typ;
        ms_Ref = Ref;
        mc_Val = Val;
        mv_RelEntrs = new Vector(4, 2);
        mv_RelSorts = new Vector(4, 2);
    }

    public void finalize() {
        ms_Type = null;
        ms_Ref = null;
        mc_Val = null;
        mv_RelEntrs.removeAllElements();
        mv_RelSorts.removeAllElements();
    }

    Concept Copie() {
        Concept nouvConc = new Concept();
        nouvConc.ms_Type = ms_Type.Copie();
        if (ms_Ref != null) {
            nouvConc.ms_Ref = ms_Ref.Copie();
        }
        if (mc_Val != null) {
            nouvConc.mc_Val = mc_Val.Copie();
        }
        return nouvConc;
    }

    Concept CopieVal(int niv) {
        Concept nouvConc = new Concept();
        CContr ValType = Unification.valeur(ms_Type, niv);
        nouvConc.ms_Type = ValType.pDonnee.Copie();
        if (ms_Ref != null) {
            CContr ValRef = Unification.valeur(ms_Ref, niv);
            if (ValRef.pDonnee != null) {
                nouvConc.ms_Ref = ValRef.pDonnee.Copie();
            }
        }
        ;
        if (mc_Val != null) {
            CContr ValVal = Unification.valeur(mc_Val, niv);
            if (ValVal.pDonnee != null) {
                nouvConc.mc_Val = ValVal.pDonnee.Copie();
            }
        }
        ;
        return nouvConc;
    }

    /** Ajouter une relation en entr?e au concept **/
    void AddRelEntr(Relation rel) {
        mv_RelEntrs.addElement(rel);
    }

    /** Ajouter une relation en entr?e au concept **/
    void AddRelSort(Relation rel) {
        mv_RelSorts.addElement(rel);
    }

    /** Enlever une relation en sortie du concept **/
    void EnleveRelSort(Relation rel) {
        mv_RelSorts.removeElement(rel);
    }

    /** Enlever une relation en entr?e du concept **/
    void EnleveRelEntr(Relation rel) {
        mv_RelEntrs.removeElement(rel);
    }
}
