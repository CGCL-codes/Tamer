package org.fudaa.dodico.crue.metier.emh;

import org.apache.commons.lang.StringUtils;
import org.fudaa.dodico.crue.config.CrueConfigMetier;

/** @pdOid 644b4cd5-433b-4568-b043-3fefd4bca488 */
public class DonPrtGeoProfilCasier extends DonPrtGeoNomme {

    /** @pdOid ca711117-4b5e-4b0c-a66d-6b9174f265bb */
    private double longueur;

    public DonPrtGeoProfilCasier(CrueConfigMetier def) {
        longueur = def.getDefaultDoubleValue("longueur");
    }

    /**
   * @pdRoleInfo migr=no name=PtProfil assc=association37 coll=java.util.List impl=java.util.ArrayList mult=1..*
   *             type=Composition
   */
    private java.util.List<PtProfil> ptProfil;

    /** @pdRoleInfo migr=no name=LitUtile assc=association45 mult=1..1 type=Composition */
    private LitUtile litUtile;

    private String commentaire = StringUtils.EMPTY;

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = StringUtils.defaultString(commentaire);
    }

    /** @pdGenerated default getter */
    public java.util.List<PtProfil> getPtProfil() {
        if (ptProfil == null) {
            ptProfil = new java.util.ArrayList<PtProfil>();
        }
        return ptProfil;
    }

    /** @pdGenerated default iterator getter */
    public java.util.Iterator getIteratorPtProfil() {
        if (ptProfil == null) {
            ptProfil = new java.util.ArrayList<PtProfil>();
        }
        return ptProfil.iterator();
    }

    /**
   * @pdGenerated default setter
   * @param newPtProfil
   */
    public void setPtProfil(final java.util.List<PtProfil> newPtProfil) {
        removeAllPtProfil();
        for (final java.util.Iterator iter = newPtProfil.iterator(); iter.hasNext(); ) {
            addPtProfil((PtProfil) iter.next());
        }
    }

    /**
   * @pdGenerated default add
   * @param newPtProfil
   */
    public void addPtProfil(final PtProfil newPtProfil) {
        if (newPtProfil == null) {
            return;
        }
        if (this.ptProfil == null) {
            this.ptProfil = new java.util.ArrayList<PtProfil>();
        }
        if (!this.ptProfil.contains(newPtProfil)) {
            this.ptProfil.add(newPtProfil);
        }
    }

    /**
   * @pdGenerated default remove
   * @param oldPtProfil
   */
    public void removePtProfil(final PtProfil oldPtProfil) {
        if (oldPtProfil == null) {
            return;
        }
        if (this.ptProfil != null) {
            if (this.ptProfil.contains(oldPtProfil)) {
                this.ptProfil.remove(oldPtProfil);
            }
        }
    }

    /** @pdGenerated default removeAll */
    public void removeAllPtProfil() {
        if (ptProfil != null) {
            ptProfil.clear();
        }
    }

    /** @pdGenerated default parent getter */
    public LitUtile getLitUtile() {
        return litUtile;
    }

    /**
   * @pdGenerated default parent setter
   * @param newLitUtile
   */
    public void setLitUtile(final LitUtile newLitUtile) {
        this.litUtile = newLitUtile;
    }

    /** @pdOid cd852adc-d66e-471f-8535-f114020deb06 */
    public double getLongueur() {
        return longueur;
    }

    /**
   * @param newLongueur
   * @pdOid 65b749d9-88ed-4718-8189-0d7f9471643c
   */
    public void setLongueur(final double newLongueur) {
        longueur = newLongueur;
    }
}
