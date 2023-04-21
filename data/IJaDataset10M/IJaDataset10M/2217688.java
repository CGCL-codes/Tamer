package edu.asu.vogon.quadriga.update;

import edu.asu.vogon.digitalHPS.IElement;

public class ActorIDU extends AIDUpdater {

    public ActorIDU(IDUpdater updater) {
        super(updater);
    }

    @Override
    protected void processSubelements(IElement element, edu.asu.quadriga.interfaces.elements.IElement elementWithId) {
    }
}
