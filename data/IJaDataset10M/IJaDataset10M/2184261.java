package com.nokia.ats4.appmodel.model.domain.impl;

import com.nokia.ats4.appmodel.event.EventQueue;
import com.nokia.ats4.appmodel.model.KendoModel;
import com.nokia.ats4.appmodel.model.KendoProject;
import com.nokia.ats4.appmodel.model.domain.Port;
import com.nokia.ats4.appmodel.model.domain.FunctionalModel;
import com.nokia.ats4.appmodel.model.domain.SystemResponse;
import com.nokia.ats4.appmodel.model.domain.SystemState;
import com.nokia.ats4.appmodel.model.domain.Transition;
import com.nokia.ats4.appmodel.model.domain.event.ModelObjectPropertyChangedEvent;
import com.nokia.ats4.appmodel.model.domain.statemachine.GuardCondition;
import com.nokia.ats4.appmodel.util.image.ImageData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * SystemStateImpl
 *
 * @author Timo Sillanp&auml;&auml;
 * @version $Revision: 2 $
 */
class SystemStateImpl extends StateImpl implements SystemState {

    /** Image cache and data container with support for language variants */
    private ImageData imageData = new ImageData();

    /** The system response expected in this state. */
    private SystemResponse systemResponse = new SystemResponseImpl();

    /** An entry port for this state. */
    private Port entryPort = new Port(this, Port.PortType.ENTRY);

    /** An exit port for this state. */
    private Port exitPort = new Port(this, Port.PortType.EXIT);

    /** List of guardconditions (a.k.a. Flags). */
    private List<GuardCondition> guards = new ArrayList<GuardCondition>();

    /**
     * Creates a new instance of SystemStateImpl
     */
    SystemStateImpl(FunctionalModel model, Long id) {
        super(model, id);
        KendoModel containingModel = model.getContainingModel();
        if (containingModel != null) {
            KendoProject prj = containingModel.getContainingKendoProject();
            if (prj != null) {
                imageData.setImageGallery(prj.getImageGallery());
            }
        }
    }

    @Override
    public SystemResponse getSystemResponse() {
        return systemResponse;
    }

    @Override
    public Port getDefaultEntryPort() {
        return entryPort;
    }

    @Override
    public Port getDefaultExitPort() {
        return exitPort;
    }

    @Override
    public Iterator<Port> getEntryPorts() {
        return Collections.singletonList(entryPort).iterator();
    }

    @Override
    public Iterator<Port> getExitPorts() {
        return Collections.singletonList(exitPort).iterator();
    }

    @Override
    public void setImageData(ImageData img) {
        this.imageData = img;
        EventQueue.dispatchEvent(new ModelObjectPropertyChangedEvent(getContainingModel(), this, "image"));
    }

    @Override
    public ImageData getImageData() {
        return this.imageData;
    }

    @Override
    public String getEventIdPrefix() {
        return "S";
    }

    @Override
    public List<GuardCondition> getGuardConditions() {
        return this.guards;
    }

    /**
     * Creates an outbound transition which represents a user action.
     */
    @Override
    protected Transition createOutgoingTransition(Port sourcePort, Port targetPort, Long transitionId) {
        return new TransitionImpl(sourcePort, targetPort, transitionId);
    }

    @Override
    public boolean acceptsIncomingTransition(Transition transition) {
        boolean parent = super.acceptsIncomingTransition(transition);
        return parent;
    }

    @Override
    public boolean acceptsOutgoingTransition(Transition transition) {
        return !(transition instanceof ImplicitTransitionImpl);
    }

    @Override
    public void addGuardCondition(GuardCondition condition) {
        this.getGuardConditions().add(condition);
    }
}
