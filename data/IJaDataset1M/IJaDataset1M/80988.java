package org.fudaa.dodico.crue.metier.emh;

/** @pdOid cc43d51b-7432-4985-a983-fe9a92163aa8 */
public class EMHModeleEnchainement extends EMH {

    /** @pdOid f682a954-9130-4dfd-8f82-c5c52e516540 */
    @Override
    public EnumTypeEMH getCatType() {
        return EnumTypeEMH.MODELE_ENCHAINEMENT;
    }

    @Override
    public boolean getUserActive() {
        return true;
    }
}
