package net.sourceforge.piqle.agents;

import net.sourceforge.piqle.agents.impl.StatelessActorImpl;
import com.google.inject.ImplementedBy;

@ImplementedBy(StatelessActorImpl.class)
public interface StatelessActor<TState> {

    /** 
	 * Acts and modifies the state.
	 * @param state The current state
	 * @return The state resulting from acting 
	 **/
    IActionResult<TState> act(TState state);

    public static interface IActionResult<TState> {

        public TState getResultingState();
    }
}
