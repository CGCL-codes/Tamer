package org.ofsm.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Marshaller;
import org.ofsm.IState;
import org.ofsm.impl.schema.Transition;
import org.ofsm.impl.schema.Transitions;

public class FileFiniteStateMachineModel extends FileFiniteStateMachineBaseModel {

    private org.ofsm.impl.schema.ObjectFactory objFactory = new org.ofsm.impl.schema.ObjectFactory();

    private boolean safeUnmarshallingMode = false;

    public FileFiniteStateMachineModel() {
    }

    public void unmarshal(InputStream stream) throws Exception {
        getEventInfos().clear();
        getStateInfos().clear();
        getTransitionInfos().clear();
        setInitialState(null);
        JAXBContext jc = JAXBContext.newInstance("org.ofsm.impl.schema");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        org.ofsm.impl.schema.FiniteStateMachine defMachine = (org.ofsm.impl.schema.FiniteStateMachine) unmarshaller.unmarshal(stream);
        org.ofsm.impl.schema.Definitions defs = defMachine.getDefinitions();
        unmarshalEvents(defs.getEvents());
        unmarshalStates(defs.getStates());
        unmarshalInitialState(defMachine.getInitialState());
        unmarshalTransitions(defMachine.getTransitions());
    }

    public void marshal(OutputStream stream) throws Exception {
        JAXBContext jc = JAXBContext.newInstance("org.ofsm.impl.schema");
        Marshaller marshaller = jc.createMarshaller();
        org.ofsm.impl.schema.FiniteStateMachine defMachine = objFactory.createFiniteStateMachine();
        defMachine.setDefinitions(marshalDefinitions());
        defMachine.setInitialState(marshalInitialState());
        defMachine.setTransitions(marshalTransitions());
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(defMachine, stream);
    }

    public void setSafeUnmarshallingMode(boolean mode) {
        safeUnmarshallingMode = mode;
    }

    protected void unmarshalEvents(org.ofsm.impl.schema.Events events) throws Exception {
        for (org.ofsm.impl.schema.EventType item : events.getEvent()) {
            addEventInfo(unmarshalEvent(item));
        }
    }

    protected EventInfo unmarshalEvent(org.ofsm.impl.schema.EventType eventDef) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String className = eventDef.getClassName();
        if (className == null) {
            className = "org.ofsm.impl.Event";
        }
        Event event = null;
        if (!safeUnmarshallingMode) {
            Class<Event> evClass = (Class<Event>) Class.forName(className);
            event = evClass.newInstance();
        } else event = new Event();
        event.setName(eventDef.getName());
        EventInfo result = new EventInfo();
        result.setEvent(event);
        result.setClassName(className);
        return result;
    }

    private void unmarshalStates(org.ofsm.impl.schema.States states) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        for (org.ofsm.impl.schema.StateType item : states.getState()) {
            addStateInfo(unmarshalState(item));
        }
    }

    protected StateInfo unmarshalState(org.ofsm.impl.schema.StateType stateDef) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String className = stateDef.getClassName();
        if (className == null) {
            className = "org.ofsm.impl.State";
        }
        State state = null;
        if (!safeUnmarshallingMode) {
            Class<State> stClass = (Class<State>) Class.forName(className);
            state = stClass.newInstance();
        } else state = new State();
        state.setName(stateDef.getName());
        StateInfo result = new StateInfo();
        result.setState(state);
        result.setClassName(className);
        return result;
    }

    protected void unmarshalInitialState(String stateName) throws ClassNotFoundException {
        StateInfo info = getStateInfo(stateName);
        if (info != null) setInitialState(info.getState());
    }

    protected void unmarshalTransitions(org.ofsm.impl.schema.Transitions defTransitions) throws ClassNotFoundException {
        for (org.ofsm.impl.schema.Transition item : defTransitions.getTransition()) {
            unmarshalTransition(item);
        }
    }

    protected void unmarshalTransition(org.ofsm.impl.schema.Transition defTransition) throws ClassNotFoundException {
        EventInfo eventInfo = getEventInfo(defTransition.getWhenEvent());
        if (eventInfo != null) {
            StateInfo fromStateInfo = getStateInfo(defTransition.getFromState());
            StateInfo toStateInfo = getStateInfo(defTransition.getToState());
            if (fromStateInfo != null && toStateInfo != null) {
                StateTransition transition = new StateTransition();
                transition.setEvent(eventInfo.getEvent());
                transition.setFromState(fromStateInfo.getState());
                transition.setToState(toStateInfo.getState());
                addTransitionInfo(transition);
            }
        }
    }

    private org.ofsm.impl.schema.Definitions marshalDefinitions() {
        org.ofsm.impl.schema.Definitions defs = objFactory.createDefinitions();
        defs.setEvents(marshalEvents());
        defs.setStates(marshalStates());
        return defs;
    }

    private org.ofsm.impl.schema.Events marshalEvents() {
        Collection<EventInfo> events = getEventInfos();
        org.ofsm.impl.schema.Events eventsDef = objFactory.createEvents();
        for (EventInfo item : events) {
            eventsDef.getEvent().add(marshalEvent(item));
        }
        return eventsDef;
    }

    private org.ofsm.impl.schema.EventType marshalEvent(EventInfo eventInfo) {
        org.ofsm.impl.schema.EventType eventDef = objFactory.createEventType();
        eventDef.setName(eventInfo.getEvent().getName());
        if (eventInfo.getClassName() != null && eventInfo.getClassName().length() > 0) eventDef.setClassName(eventInfo.getClassName());
        return eventDef;
    }

    private org.ofsm.impl.schema.States marshalStates() {
        Collection<StateInfo> states = getStateInfos();
        org.ofsm.impl.schema.States statesDef = objFactory.createStates();
        for (StateInfo item : states) {
            statesDef.getState().add(marshalState(item));
        }
        return statesDef;
    }

    private org.ofsm.impl.schema.StateType marshalState(StateInfo stateInfo) {
        org.ofsm.impl.schema.StateType stateDef = objFactory.createStateType();
        stateDef.setName(stateInfo.getState().getName());
        if (stateInfo.getClassName() != null && stateInfo.getClassName().length() > 0) stateDef.setClassName(stateInfo.getClassName());
        return stateDef;
    }

    private String marshalInitialState() {
        IState state = getInitialState();
        if (state != null) {
            return state.getName();
        }
        return null;
    }

    private Transitions marshalTransitions() {
        Transitions transDefs = objFactory.createTransitions();
        for (Map.Entry<IState, List<StateTransition>> lstItem : getTransitionInfos().entrySet()) {
            for (StateTransition item : lstItem.getValue()) {
                Transition transDef = objFactory.createTransition();
                marshalTransition(item, transDef);
                transDefs.getTransition().add(transDef);
            }
        }
        return transDefs;
    }

    private void marshalTransition(StateTransition trans, Transition transDef) {
        if (trans.getFromState() != null) transDef.setFromState(trans.getFromState().getName());
        if (trans.getToState() != null) transDef.setToState(trans.getToState().getName());
        if (trans.getEvent() != null) transDef.setWhenEvent(trans.getEvent().getName());
    }
}
