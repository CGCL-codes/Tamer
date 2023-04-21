package hypercast;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * This class is timer ID 
 
 * @author HyperCast Team
 * @author Guimin Zhang
 * @version 2.0, May. 23, 2001
 */
public class Timer_ID {

    /**the finite state machine . */
    private I_MessageStoreFSM fsm;

    /** Identifier of the timeout. */
    private int id;

    private Object obj = null;

    /**
	 * Constructs a Time_ID object with time id and time.
	 * @param   FSM      finite state machine
	 * @param	ID       time id
	 */
    public Timer_ID(I_MessageStoreFSM FSM, int ID) {
        id = ID;
        fsm = FSM;
    }

    /**
     * Construct a timer object with another object attached to it
     */
    public Timer_ID(I_MessageStoreFSM FSM, Object obj) {
        this.id = -1;
        this.obj = obj;
        fsm = FSM;
    }

    /**
	 * Constructs a new Timer_ID object with a Timer_ID object
	 * @param	te	a Timer_ID object
	 */
    public Timer_ID(Timer_ID te) {
        id = te.getID();
        this.obj = te.obj;
        fsm = te.getFSM();
    }

    /**
	 * Returns the timeout id 
	 * @return	id
	 */
    public int getID() {
        return id;
    }

    /**
     * Returns the object associated with the timer id object, or null if there is no object associated.
     */
    public Object getObject() {
        return obj;
    }

    /**
	 * Returns the finite state machine
	 * @return	fsm
	 */
    public I_MessageStoreFSM getFSM() {
        return fsm;
    }

    /**
	 * compare two Timer_ID
	 * @return	true/false
	 */
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Timer_ID)) return false;
        Timer_ID td = (Timer_ID) o;
        if (obj != null && td.obj != null) return fsm == td.fsm && obj.equals(td.obj); else return fsm == td.fsm && id == td.id;
    }
}
