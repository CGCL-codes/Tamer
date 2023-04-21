package org.qsardb.model;

class CompoundCargo extends Cargo<Compound> {

    CompoundCargo(Compound compound) {
        super(ID, compound);
    }

    public static final String ID = "compoundcargo";
}
