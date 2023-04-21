package org.matsim.ptproject.qsim.qnetsimengine;

public abstract class LinkActivator {

    protected abstract void activateLink(final QLinkInternalI link);

    abstract int getNumberOfSimulatedLinks();
}
