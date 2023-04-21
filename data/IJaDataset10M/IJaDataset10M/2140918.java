package org.fudaa.dodico.crue.metier.emh;

/** @pdOid df067a22-9c98-463e-823f-860b251124f9 */
public class OrdResBrancheStrickler extends OrdRes {

    /** @pdOid e8323bb6-a01b-4ad8-b6a0-5639b287519f */
    private boolean ddeSplan;

    /** @pdOid 89033f60-e9fc-423b-8624-4251f3087217 */
    private boolean ddeVol;

    /** @pdOid d2a1a6ef-b95b-40db-8281-8e8b6037f1a0 */
    public final boolean getDdeSplan() {
        return ddeSplan;
    }

    /** @pdOid 3d542414-7c1a-44f2-8a22-d7dda270d11e */
    public final boolean getDdeVol() {
        return ddeVol;
    }

    /**
   * @param newDdeSplan
   * @pdOid e37d025a-69f4-4b78-9faf-3c612ffcb642
   */
    public final void setDdeSplan(boolean newDdeSplan) {
        ddeSplan = newDdeSplan;
    }

    /**
   * @param newDdeVol
   * @pdOid 8b2123a3-4ea6-481f-8b3b-afb1a8f0c149
   */
    public final void setDdeVol(boolean newDdeVol) {
        ddeVol = newDdeVol;
    }

    @Override
    public String toString() {
        return "OrdResBrancheStrickler [ddeSplan=" + ddeSplan + ", ddeVol=" + ddeVol + "]";
    }
}
