package desmoj.extensions.space3D;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;

/**
 * The RotatingDoneEvent class represents a internal event which notifies
 * a MovableSpatialObject the end of its rotation. And it should only be used by
 * a MovableSpatialObject. If some other entities call the constructor a
 * DESMOJException will be thrown.
 * @author Fred Sun
 *
 */
public class RotatingDoneEvent extends ExternalEvent {

    private MovableSpatialObject _targetObject;

    /**
	 * Constructs a RotatingDoneEvent and checks whether the caller is a
	 * instance of the MovableSpatialObject. If not, a DESMOJException will be
	 * thrown.
	 * @param owner The model this event is associated to
	 * @param name The name of this event
	 * @param showInTrace Flag for showing event in trace-files. Set it to true if event should show up in trace. Set it to false if event should not be shown in trace.
	 * @param targetObject A reference to the object which should be the target
	 * of this event.
	 */
    public RotatingDoneEvent(Model owner, String name, boolean showInTrace, MovableSpatialObject targetObject) {
        super(owner, name, showInTrace);
        _targetObject = targetObject;
    }

    @Override
    public void eventRoutine() {
        _targetObject.notifyRotatingDone();
    }
}
