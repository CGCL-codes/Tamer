package com.c4j.composition;

import com.c4j.linker.ILinker;
import com.c4j.linker.IReference;
import com.c4j.workspace.IFragment;
import com.c4j.workspace.IReceptaclePort;

public class PublicReceptacleReference extends PublicPortReference implements IPublicReceptacleReference {

    private final IReference<IReceptaclePort> portReference;

    PublicReceptacleReference(final ILinker linker, final PublicReceptaclePort parent, final String instanceref, final String portref, final int cardinality) {
        super(linker, parent, instanceref, cardinality);
        portReference = linker.createReference(this);
        portReference.setReferenceString(portref);
    }

    @Override
    public IReference<? extends IReceptaclePort> getPortReference() {
        return portReference;
    }

    @Override
    public IReceptaclePort getPort() {
        return portReference.getReferee();
    }

    @Override
    void foundFragment(final IFragment fragment) {
        fragment.getReceptacleResolver().addReference(portReference);
    }

    @Override
    void loosingFragment(final IFragment fragment) {
        fragment.getReceptacleResolver().removeReference(portReference);
    }
}
