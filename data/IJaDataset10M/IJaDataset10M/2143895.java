package iconSwingStates.Transitions.TransitionOnTag;

import iconSwingStates.StateMachine.SMIconStateMachine;

/**
 * TPress represents the transition fired by a pressed button over a given tag
 * @author uribe-gaget
 *
 */
public class TPressOnTag extends TMoveOnTag {

    /**
	 * Default constructor
	 * @param nomTransition name of the transition
	 * @param pointer Name of the pointer
	 * @param obligatory True if the slot must be connected
	 * @param tag The tag
	 */
    public TPressOnTag(String nomTransition, String pointer, boolean obligatory, String tag) {
        super(nomTransition, pointer, SMIconStateMachine.BOOLEAN, obligatory, tag);
    }

    /**
	 * Default constructor
	 * @param nomTransition name of the transition
	 * @param pointer Name of the pointer
	 * @param obligatory True if the slot must be connected
	 * @param tag The tag
	 * @param outputState The output state
	 */
    public TPressOnTag(String nomTransition, String pointer, boolean obligatory, String tag, String outputState) {
        super(nomTransition, pointer, SMIconStateMachine.BOOLEAN, obligatory, tag, outputState);
    }

    /**
	 * Constructor with more than one slot
	 * @param nomTransition name of the transition
	 * @param pointer Name of the pointer
	 * @param type Type of the slot
	 * @param obligatory True if the slot must be connected
	 * @param synchro True if the slots activity must be synchronized
	 * @param tag The tag
	 */
    public TPressOnTag(String nomTransition, String pointer, int[] type, boolean obligatory, boolean synchro, String tag) {
        super(nomTransition, pointer, type, obligatory, synchro, tag);
    }

    /**
	 * Constructor with more than one slot
	 * @param nomTransition name of the transition
	 * @param pointer Name of the pointer
	 * @param type Type of the slot
	 * @param obligatory True if the slot must be connected
	 * @param synchro True if the slots activity must be synchronized
	 * @param tag The tag
	 * @param outputState The output state
	 */
    public TPressOnTag(String nomTransition, String pointer, int[] type, boolean obligatory, boolean synchro, String tag, String outputState) {
        super(nomTransition, pointer, type, obligatory, synchro, tag, outputState);
    }
}
