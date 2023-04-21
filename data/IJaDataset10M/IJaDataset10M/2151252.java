package org.polepos.teams.prevayler.bahrain;

class ObjectCreationTransaction extends BahrainTransaction {

    ObjectCreationTransaction(int objectsToCreate) {
        super(objectsToCreate);
    }

    protected void executeOn(BahrainSystem bahrain, int objectsToCreate) {
        bahrain.write(objectsToCreate);
    }

    private static final long serialVersionUID = 1L;
}
