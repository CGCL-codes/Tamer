package org.fudaa.dodico.crue.metier.emh;

/**
 * type 2 Crue9
 * 
 * @pdOid ab08a8c4-9702-4b7c-93f4-0315d2ab1782
 */
public class DonCalcSansPrtBrancheSeuilTransversal extends DonCalcSansPrtBrancheSeuil {

    /**
   * @pdRoleInfo migr=no name=ElemSeuilAvecPdc assc=association38 coll=java.util.List impl=java.util.ArrayList mult=1..*
   *             type=Composition
   */
    private java.util.List<ElemSeuilAvecPdc> elemSeuilAvecPdc;

    /** @pdGenerated default getter */
    public java.util.List<ElemSeuilAvecPdc> getElemSeuilAvecPdc() {
        if (elemSeuilAvecPdc == null) {
            elemSeuilAvecPdc = new java.util.ArrayList<ElemSeuilAvecPdc>();
        }
        return elemSeuilAvecPdc;
    }

    /** @pdGenerated default iterator getter */
    public java.util.Iterator getIteratorElemSeuilAvecPdc() {
        if (elemSeuilAvecPdc == null) {
            elemSeuilAvecPdc = new java.util.ArrayList<ElemSeuilAvecPdc>();
        }
        return elemSeuilAvecPdc.iterator();
    }

    /**
   * @pdGenerated default setter
   * @param newElemSeuilAvecPdc
   */
    public void setElemSeuilAvecPdc(final java.util.List<ElemSeuilAvecPdc> newElemSeuilAvecPdc) {
        removeAllElemSeuilAvecPdc();
        for (final java.util.Iterator iter = newElemSeuilAvecPdc.iterator(); iter.hasNext(); ) {
            addElemSeuilAvecPdc((ElemSeuilAvecPdc) iter.next());
        }
    }

    /**
   * @pdGenerated default add
   * @param newElemSeuilAvecPdc
   */
    public void addElemSeuilAvecPdc(final ElemSeuilAvecPdc newElemSeuilAvecPdc) {
        if (newElemSeuilAvecPdc == null) {
            return;
        }
        if (this.elemSeuilAvecPdc == null) {
            this.elemSeuilAvecPdc = new java.util.ArrayList<ElemSeuilAvecPdc>();
        }
        if (!this.elemSeuilAvecPdc.contains(newElemSeuilAvecPdc)) {
            this.elemSeuilAvecPdc.add(newElemSeuilAvecPdc);
        }
    }

    /**
   * @pdGenerated default remove
   * @param oldElemSeuilAvecPdc
   */
    public void removeElemSeuilAvecPdc(final ElemSeuilAvecPdc oldElemSeuilAvecPdc) {
        if (oldElemSeuilAvecPdc == null) {
            return;
        }
        if (this.elemSeuilAvecPdc != null) {
            if (this.elemSeuilAvecPdc.contains(oldElemSeuilAvecPdc)) {
                this.elemSeuilAvecPdc.remove(oldElemSeuilAvecPdc);
            }
        }
    }

    /** @pdGenerated default removeAll */
    public void removeAllElemSeuilAvecPdc() {
        if (elemSeuilAvecPdc != null) {
            elemSeuilAvecPdc.clear();
        }
    }
}
