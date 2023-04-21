package org.customsoft.stateless4j;

import org.customsoft.stateless4j.delegates.Action;
import org.customsoft.stateless4j.delegates.Action1;
import org.customsoft.stateless4j.delegates.Action2;
import org.customsoft.stateless4j.delegates.Action3;
import org.customsoft.stateless4j.delegates.Action4;
import org.customsoft.stateless4j.delegates.Func;
import org.customsoft.stateless4j.delegates.Func2;
import org.customsoft.stateless4j.delegates.Func3;
import org.customsoft.stateless4j.delegates.Func4;
import org.customsoft.stateless4j.resources.StateConfigurationResources;
import org.customsoft.stateless4j.transitions.Transition;
import org.customsoft.stateless4j.transitions.TransitioningTriggerBehaviour;
import org.customsoft.stateless4j.triggers.DynamicTriggerBehaviour;
import org.customsoft.stateless4j.triggers.IgnoredTriggerBehaviour;
import org.customsoft.stateless4j.triggers.TriggerWithParameters1;
import org.customsoft.stateless4j.triggers.TriggerWithParameters2;
import org.customsoft.stateless4j.triggers.TriggerWithParameters3;
import org.customsoft.stateless4j.validation.Enforce;

public class StateConfiguration<TState, TTrigger> {

    final StateRepresentation<TState, TTrigger> _representation;

    final Func2<TState, StateRepresentation<TState, TTrigger>> _lookup;

    final Func<Boolean> NoGuard = new Func<Boolean>() {

        @Override
        public Boolean call() {
            return true;
        }
    };

    public StateConfiguration(StateRepresentation<TState, TTrigger> representation, Func2<TState, StateRepresentation<TState, TTrigger>> lookup) throws Exception {
        _representation = Enforce.ArgumentNotNull(representation, "representation");
        _lookup = Enforce.ArgumentNotNull(lookup, "lookup");
    }

    public StateConfiguration<TState, TTrigger> Permit(TTrigger trigger, TState destinationState) throws Exception {
        enforceNotIdentityTransition(destinationState);
        return publicPermit(trigger, destinationState);
    }

    public StateConfiguration<TState, TTrigger> PermitIf(TTrigger trigger, TState destinationState, Func<Boolean> guard) throws Exception {
        enforceNotIdentityTransition(destinationState);
        return publicPermitIf(trigger, destinationState, guard);
    }

    public StateConfiguration<TState, TTrigger> PermitReentry(TTrigger trigger) throws Exception {
        return publicPermit(trigger, _representation.getUnderlyingState());
    }

    public StateConfiguration<TState, TTrigger> PermitReentryIf(TTrigger trigger, Func<Boolean> guard) throws Exception {
        return publicPermitIf(trigger, _representation.getUnderlyingState(), guard);
    }

    public StateConfiguration<TState, TTrigger> Ignore(TTrigger trigger) throws Exception {
        return IgnoreIf(trigger, NoGuard);
    }

    public StateConfiguration<TState, TTrigger> IgnoreIf(TTrigger trigger, Func<Boolean> guard) throws Exception {
        Enforce.ArgumentNotNull(guard, "guard");
        _representation.AddTriggerBehaviour(new IgnoredTriggerBehaviour<TState, TTrigger>(trigger, guard));
        return this;
    }

    public StateConfiguration<TState, TTrigger> OnEntry(final Action entryAction) throws Exception {
        Enforce.ArgumentNotNull(entryAction, "entryAction");
        return OnEntry(new Action1<Transition<TState, TTrigger>>() {

            public void doIt(Transition<TState, TTrigger> t) {
                entryAction.doIt();
            }

            ;
        });
    }

    public StateConfiguration<TState, TTrigger> OnEntry(final Action1<Transition<TState, TTrigger>> entryAction) throws Exception {
        Enforce.ArgumentNotNull(entryAction, "entryAction");
        _representation.AddEntryAction(new Action2<Transition<TState, TTrigger>, Object[]>() {

            public void doIt(Transition<TState, TTrigger> arg1, Object[] arg2) {
                entryAction.doIt(arg1);
            }

            ;
        });
        return this;
    }

    public StateConfiguration<TState, TTrigger> OnEntryFrom(TTrigger trigger, final Action entryAction) throws Exception {
        Enforce.ArgumentNotNull(entryAction, "entryAction");
        return OnEntryFrom(trigger, new Action1<Transition<TState, TTrigger>>() {

            public void doIt(Transition<TState, TTrigger> arg1) {
                entryAction.doIt();
            }

            ;
        });
    }

    public StateConfiguration<TState, TTrigger> OnEntryFrom(TTrigger trigger, final Action1<Transition<TState, TTrigger>> entryAction) throws Exception {
        Enforce.ArgumentNotNull(entryAction, "entryAction");
        _representation.AddEntryAction(trigger, new Action2<Transition<TState, TTrigger>, Object[]>() {

            public void doIt(Transition<TState, TTrigger> arg1, Object[] arg2) {
                entryAction.doIt(arg1);
            }

            ;
        });
        return this;
    }

    public <TArg0> StateConfiguration<TState, TTrigger> OnEntryFrom(TriggerWithParameters1<TArg0, TState, TTrigger> trigger, final Action1<TArg0> entryAction, final Class<TArg0> classe0) throws Exception {
        Enforce.ArgumentNotNull(entryAction, "entryAction");
        return OnEntryFrom(trigger, new Action2<TArg0, Transition<TState, TTrigger>>() {

            public void doIt(TArg0 arg1, Transition<TState, TTrigger> arg2) {
                entryAction.doIt(arg1);
            }

            ;
        }, classe0);
    }

    public <TArg0> StateConfiguration<TState, TTrigger> OnEntryFrom(TriggerWithParameters1<TArg0, TState, TTrigger> trigger, final Action2<TArg0, Transition<TState, TTrigger>> entryAction, final Class<TArg0> classe0) throws Exception {
        Enforce.ArgumentNotNull(entryAction, "entryAction");
        Enforce.ArgumentNotNull(trigger, "trigger");
        _representation.AddEntryAction(trigger.getTrigger(), new Action2<Transition<TState, TTrigger>, Object[]>() {

            @SuppressWarnings("unchecked")
            public void doIt(Transition<TState, TTrigger> t, Object[] arg2) throws Exception {
                entryAction.doIt((TArg0) arg2[0], t);
            }

            ;
        });
        return this;
    }

    public <TArg0, TArg1> StateConfiguration<TState, TTrigger> OnEntryFrom(TriggerWithParameters2<TArg0, TArg1, TState, TTrigger> trigger, final Action2<TArg0, TArg1> entryAction, final Class<TArg0> classe0, final Class<TArg1> classe1) throws Exception {
        Enforce.ArgumentNotNull(entryAction, "entryAction");
        return OnEntryFrom(trigger, new Action3<TArg0, TArg1, Transition<TState, TTrigger>>() {

            public void doIt(TArg0 a0, TArg1 a1, Transition<TState, TTrigger> t) throws Exception {
                entryAction.doIt(a0, a1);
            }

            ;
        }, classe0, classe1);
    }

    public <TArg0, TArg1> StateConfiguration<TState, TTrigger> OnEntryFrom(TriggerWithParameters2<TArg0, TArg1, TState, TTrigger> trigger, final Action3<TArg0, TArg1, Transition<TState, TTrigger>> entryAction, final Class<TArg0> classe0, final Class<TArg1> classe1) throws Exception {
        Enforce.ArgumentNotNull(entryAction, "entryAction");
        Enforce.ArgumentNotNull(trigger, "trigger");
        _representation.AddEntryAction(trigger.getTrigger(), new Action2<Transition<TState, TTrigger>, Object[]>() {

            @SuppressWarnings("unchecked")
            public void doIt(Transition<TState, TTrigger> t, Object[] args) throws Exception {
                entryAction.doIt((TArg0) args[0], (TArg1) args[1], t);
            }

            ;
        });
        return this;
    }

    public <TArg0, TArg1, TArg2> StateConfiguration<TState, TTrigger> OnEntryFrom(TriggerWithParameters3<TArg0, TArg1, TArg2, TState, TTrigger> trigger, final Action3<TArg0, TArg1, TArg2> entryAction, final Class<TArg0> classe0, final Class<TArg1> classe1, final Class<TArg2> classe2) throws Exception {
        Enforce.ArgumentNotNull(entryAction, "entryAction");
        return OnEntryFrom(trigger, new Action4<TArg0, TArg1, TArg2, Transition<TState, TTrigger>>() {

            public void doIt(TArg0 a0, TArg1 a1, TArg2 a2, Transition<TState, TTrigger> t) throws Exception {
                entryAction.doIt(a0, a1, a2);
            }

            ;
        }, classe0, classe1, classe2);
    }

    public <TArg0, TArg1, TArg2> StateConfiguration<TState, TTrigger> OnEntryFrom(TriggerWithParameters3<TArg0, TArg1, TArg2, TState, TTrigger> trigger, final Action4<TArg0, TArg1, TArg2, Transition<TState, TTrigger>> entryAction, final Class<TArg0> classe0, final Class<TArg1> classe1, final Class<TArg2> classe2) throws Exception {
        Enforce.ArgumentNotNull(entryAction, "entryAction");
        Enforce.ArgumentNotNull(trigger, "trigger");
        _representation.AddEntryAction(trigger.getTrigger(), new Action2<Transition<TState, TTrigger>, Object[]>() {

            @SuppressWarnings("unchecked")
            public void doIt(Transition<TState, TTrigger> t, Object[] args) throws Exception {
                entryAction.doIt((TArg0) args[0], (TArg1) args[1], (TArg2) args[2], t);
            }

            ;
        });
        return this;
    }

    public StateConfiguration<TState, TTrigger> OnExit(final Action exitAction) throws Exception {
        Enforce.ArgumentNotNull(exitAction, "exitAction");
        return OnExit(new Action1<Transition<TState, TTrigger>>() {

            @Override
            public void doIt(Transition<TState, TTrigger> arg1) {
                exitAction.doIt();
            }
        });
    }

    public StateConfiguration<TState, TTrigger> OnExit(Action1<Transition<TState, TTrigger>> exitAction) throws Exception {
        Enforce.ArgumentNotNull(exitAction, "exitAction");
        _representation.AddExitAction(exitAction);
        return this;
    }

    public StateConfiguration<TState, TTrigger> SubstateOf(TState superstate) throws Exception {
        StateRepresentation<TState, TTrigger> superRepresentation = _lookup.call(superstate);
        _representation.setSuperstate(superRepresentation);
        superRepresentation.AddSubstate(_representation);
        return this;
    }

    public StateConfiguration<TState, TTrigger> PermitDynamic(TTrigger trigger, final Func<TState> destinationStateSelector) throws Exception {
        return PermitDynamicIf(trigger, destinationStateSelector, NoGuard);
    }

    public <TArg0> StateConfiguration<TState, TTrigger> PermitDynamic(TriggerWithParameters1<TArg0, TState, TTrigger> trigger, Func2<TArg0, TState> destinationStateSelector) throws Exception {
        return permitDynamicIf(trigger, destinationStateSelector, NoGuard);
    }

    public <TArg0, TArg1> StateConfiguration<TState, TTrigger> PermitDynamic(TriggerWithParameters2<TArg0, TArg1, TState, TTrigger> trigger, Func3<TArg0, TArg1, TState> destinationStateSelector) throws Exception {
        return permitDynamicIf(trigger, destinationStateSelector, NoGuard);
    }

    public <TArg0, TArg1, TArg2> StateConfiguration<TState, TTrigger> PermitDynamic(TriggerWithParameters3<TArg0, TArg1, TArg2, TState, TTrigger> trigger, final Func4<TArg0, TArg1, TArg2, TState> destinationStateSelector) throws Exception {
        return permitDynamicIf(trigger, destinationStateSelector, NoGuard);
    }

    public StateConfiguration<TState, TTrigger> PermitDynamicIf(TTrigger trigger, final Func<TState> destinationStateSelector, Func<Boolean> guard) throws Exception {
        Enforce.ArgumentNotNull(destinationStateSelector, "destinationStateSelector");
        return publicPermitDynamicIf(trigger, new Func2<Object[], TState>() {

            @Override
            public TState call(Object[] arg0) {
                return destinationStateSelector.call();
            }
        }, guard);
    }

    public <TArg0> StateConfiguration<TState, TTrigger> permitDynamicIf(TriggerWithParameters1<TArg0, TState, TTrigger> trigger, final Func2<TArg0, TState> destinationStateSelector, Func<Boolean> guard) throws Exception {
        Enforce.ArgumentNotNull(trigger, "trigger");
        Enforce.ArgumentNotNull(destinationStateSelector, "destinationStateSelector");
        return publicPermitDynamicIf(trigger.getTrigger(), new Func2<Object[], TState>() {

            @SuppressWarnings("unchecked")
            @Override
            public TState call(Object[] args) throws Exception {
                return destinationStateSelector.call((TArg0) args[0]);
            }
        }, guard);
    }

    public <TArg0, TArg1> StateConfiguration<TState, TTrigger> permitDynamicIf(TriggerWithParameters2<TArg0, TArg1, TState, TTrigger> trigger, final Func3<TArg0, TArg1, TState> destinationStateSelector, Func<Boolean> guard) throws Exception {
        Enforce.ArgumentNotNull(trigger, "trigger");
        Enforce.ArgumentNotNull(destinationStateSelector, "destinationStateSelector");
        return publicPermitDynamicIf(trigger.getTrigger(), new Func2<Object[], TState>() {

            @SuppressWarnings("unchecked")
            @Override
            public TState call(Object[] args) throws Exception {
                return destinationStateSelector.call((TArg0) args[0], (TArg1) args[1]);
            }
        }, guard);
    }

    public <TArg0, TArg1, TArg2> StateConfiguration<TState, TTrigger> permitDynamicIf(TriggerWithParameters3<TArg0, TArg1, TArg2, TState, TTrigger> trigger, final Func4<TArg0, TArg1, TArg2, TState> destinationStateSelector, Func<Boolean> guard) throws Exception {
        Enforce.ArgumentNotNull(trigger, "trigger");
        Enforce.ArgumentNotNull(destinationStateSelector, "destinationStateSelector");
        return publicPermitDynamicIf(trigger.getTrigger(), new Func2<Object[], TState>() {

            @SuppressWarnings("unchecked")
            @Override
            public TState call(Object[] args) throws Exception {
                return destinationStateSelector.call((TArg0) args[0], (TArg1) args[1], (TArg2) args[2]);
            }
        }, guard);
    }

    void enforceNotIdentityTransition(TState destination) throws Exception {
        if (destination.equals(_representation.getUnderlyingState())) {
            throw new Exception(StateConfigurationResources.SelfTransitionsEitherIgnoredOrReentrant);
        }
    }

    StateConfiguration<TState, TTrigger> publicPermit(TTrigger trigger, TState destinationState) throws Exception {
        return publicPermitIf(trigger, destinationState, new Func<Boolean>() {

            @Override
            public Boolean call() {
                return true;
            }
        });
    }

    StateConfiguration<TState, TTrigger> publicPermitIf(TTrigger trigger, TState destinationState, Func<Boolean> guard) throws Exception {
        Enforce.ArgumentNotNull(guard, "guard");
        _representation.AddTriggerBehaviour(new TransitioningTriggerBehaviour<TState, TTrigger>(trigger, destinationState, guard));
        return this;
    }

    StateConfiguration<TState, TTrigger> publicPermitDynamic(TTrigger trigger, Func2<Object[], TState> destinationStateSelector) throws Exception {
        return publicPermitDynamicIf(trigger, destinationStateSelector, NoGuard);
    }

    StateConfiguration<TState, TTrigger> publicPermitDynamicIf(TTrigger trigger, Func2<Object[], TState> destinationStateSelector, Func<Boolean> guard) throws Exception {
        Enforce.ArgumentNotNull(destinationStateSelector, "destinationStateSelector");
        Enforce.ArgumentNotNull(guard, "guard");
        _representation.AddTriggerBehaviour(new DynamicTriggerBehaviour<TState, TTrigger>(trigger, destinationStateSelector, guard));
        return this;
    }
}
