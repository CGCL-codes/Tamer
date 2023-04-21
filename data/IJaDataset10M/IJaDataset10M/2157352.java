package org.fudaa.dodico.crue.metier.emh;

public class OrdResBrancheNiveauxAssocies extends OrdRes {

    /** @pdOid 636f61f8-f6dd-4750-8c12-ca13b3fe7fd7 */
    private boolean ddeDz;

    /**
   * @return the ddeDz
   */
    public boolean getDdeDz() {
        return ddeDz;
    }

    /**
   * @param ddeDz the ddeDz to set
   */
    public void setDdeDz(final boolean ddeDz) {
        this.ddeDz = ddeDz;
    }

    @Override
    public String toString() {
        return "OrdResBrancheBarrageGenerique [ddeDz=" + ddeDz + "]";
    }
}
