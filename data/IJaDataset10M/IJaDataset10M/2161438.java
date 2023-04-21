package scheme4j.state;

/**
 * Questa classe implementa il design pattern STATE per la gestione degli stati della gui in relazione all'esecuzione
 * dei programmi scheme.
 */
public class InterpreterState {

    public static void init() {
    }

    static {
        setState(InterpreterStateConstants.EMPTY_NORMAL);
    }

    private static IInterpreterState currentState = InterpreterStateConstants.EMPTY_NORMAL;

    /**
	 * Modifica lo stato dell'interprete.
	 */
    private static void setState(IInterpreterState newState) {
        if (currentState != newState) {
            currentState = newState;
            newState.updateProgram();
        } else newState = currentState;
    }

    public static synchronized void play() {
        setState(currentState.play());
    }

    public static synchronized void stopOrKill() {
        setState(currentState.stopOrKill());
    }

    public static synchronized void debugSwitch() {
        setState(currentState.debugSwitch());
    }

    public static synchronized void programTerminated() {
        setState(currentState.programTerminated());
    }
}
