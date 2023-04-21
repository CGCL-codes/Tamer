package org.fudaa.dodico.crue.metier.emh;

import org.fudaa.dodico.crue.metier.annotation.Visibility;

/**
 * CL de type 1 en Crue9
 * 
 * @pdOid 751797ca-e99e-4db1-82c1-b069eb1b22ea
 */
public class CalcTransNoeudNiveauContinuLimnigramme extends DonCLimMCommonItem implements CalcTransItem {

    /**
   * Loi Limnigramme : VarAbscisse = t VarOrdonnee = Z
   * 
   * @pdOid 54297aba-72c6-4ed4-9147-4f7a9af1b97f
   */
    private LoiDF limnigramme;

    /**
   * Loi Limnigramme : VarAbscisse = t VarOrdonnee = Z
   * 
   * @pdOid 31e8ec88-6501-474a-ad5f-c8ccfb3eed56
   */
    public LoiDF getLimnigramme() {
        return limnigramme;
    }

    /**
   * Loi Limnigramme : VarAbscisse = t VarOrdonnee = Z
   * 
   * @param newLimnigramme
   * @pdOid f40655c6-99ed-4687-9875-1c833ab85494
   */
    public void setLimnigramme(final LoiDF newLimnigramme) {
        if (this.limnigramme != null) {
            this.limnigramme.unregister(this);
        }
        limnigramme = newLimnigramme;
        this.limnigramme.register(this);
    }

    /** @pdOid 07ae3e3e-adb2-42c9-a534-ef303a8e0395 */
    @Visibility(ihm = false)
    public Loi getLoi() {
        return getLimnigramme();
    }

    /**
   * @param newLoi
   * @pdOid ae42bd86-2569-45ff-925b-8e31de194293
   */
    public void setLoi(final Loi newLoi) {
        setLimnigramme((LoiDF) newLoi);
    }
}
