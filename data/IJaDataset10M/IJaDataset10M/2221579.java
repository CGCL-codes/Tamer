package scheme4j.state;

/**
 * Interfaccia contenente gli stati della GUI dell'interprete in relazione all'esecuzione dei programmi.
 */
public interface InterpreterStateConstants {

    public static final IInterpreterState EMPTY_DEBUG = new EmptyDebugState();

    public static final IInterpreterState EMPTY_NORMAL = new EmptyNormalState();

    public static final IInterpreterState EXECUTING_NORMAL = new ExecutingNormalState();

    public static final IInterpreterState STOP_DEBUG = new StopDebugState();

    public static final IInterpreterState STOP_NORMAL = new StopNormalState();
}
