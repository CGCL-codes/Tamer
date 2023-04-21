package petrieditor.model.viewinterfaces;

import petrieditor.util.Observer;
import petrieditor.model.event.NotifyEvent;
import petrieditor.model.event.PetriNetObject;
import petrieditor.model.PetriNet;

/**
 * @author wiktor
 */
public interface PetriNetView extends Observer<PetriNet, PetriNetView, NotifyEvent<PetriNetObject>> {
}
