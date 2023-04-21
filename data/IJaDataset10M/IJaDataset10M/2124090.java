package core;

/**
 * Communication Exception Class that extends from the Simulation Exception
 * class
 * @author bevan_calliess
 * @author angela_brown
 * @param inExceptionMsg The Exception Message 
 * @version v0.20
 */
public class CommunicationException extends SimulationException {

    public CommunicationException(String inExceptionMsg) {
        super(inExceptionMsg);
    }

    /**
	 * @author angela_brown
	 * @author bevan_calliess
	 * @return exceptionMsg
	 * @version v0.20
	 */
    public String toString() {
        return exceptionMsg;
    }
}
