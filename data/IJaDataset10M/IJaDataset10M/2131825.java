package logic;

/**
 * @author   Fabrizio Pastore [ fabrizio.pastore@gmail.com ]
 */
public class RunMessage {

    /**
	 * @author   Fabrizio Pastore [ fabrizio.pastore@gmail.com ]
	 */
    public enum Type {

        RUN_STARTED, RUN_FINISHED, NEW_RUN, NEW_RUN_POP, NEW_THREAD_RUN, THREAD_RUN_FINISHED, THREAD_STARTED, LOAD_INSTANCE, NEW_POP, NEW_INSTANCE, SAVE_STATE, THREAD_RUN_STARTED
    }

    ;

    /**
	 * @uml.property   name="message"
	 */
    private String message;

    /**
	 * @uml.property   name="problId"
	 */
    private int problId;

    /**
	 * @uml.property   name="stateCurrent"
	 */
    private int stateCurrent;

    /**
	 * @uml.property   name="stateTotal"
	 */
    private int stateTotal;

    private Type type;

    public RunMessage(String mess, int problId, Type type, int stateCurrent, int stateTotal) {
        this.message = mess;
        this.stateCurrent = stateCurrent;
        this.stateTotal = stateTotal;
        this.type = type;
        this.problId = problId;
    }

    /**
	 * @return     Returns the message.
	 * @uml.property   name="message"
	 */
    public String getMessage() {
        return message;
    }

    /**
	 * @param message     The message to set.
	 * @uml.property   name="message"
	 */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
	 * @return     Returns the stateCurrent.
	 * @uml.property   name="stateCurrent"
	 */
    public int getStateCurrent() {
        return stateCurrent;
    }

    /**
	 * @param stateCurrent     The stateCurrent to set.
	 * @uml.property   name="stateCurrent"
	 */
    public void setStateCurrent(int stateCurrent) {
        this.stateCurrent = stateCurrent;
    }

    /**
	 * @return     Returns the stateTotal.
	 * @uml.property   name="stateTotal"
	 */
    public int getStateTotal() {
        return stateTotal;
    }

    /**
	 * @param stateTotal     The stateTotal to set.
	 * @uml.property   name="stateTotal"
	 */
    public void setStateTotal(int stateTotal) {
        this.stateTotal = stateTotal;
    }

    public RunMessage clone() {
        return new RunMessage(message, problId, type, stateCurrent, stateTotal);
    }

    /**
	 * @return     Returns the type.
	 * @uml.property   name="type"
	 */
    public Type getType() {
        return type;
    }

    /**
	 * @param type     The type to set.
	 * @uml.property   name="type"
	 */
    public void setType(Type type) {
        this.type = type;
    }

    /**
	 * @return     Returns the problId.
	 * @uml.property   name="problId"
	 */
    public int getProblId() {
        return problId;
    }

    /**
	 * @param problId     The problId to set.
	 * @uml.property   name="problId"
	 */
    public void setProblId(int problId) {
        this.problId = problId;
    }
}
