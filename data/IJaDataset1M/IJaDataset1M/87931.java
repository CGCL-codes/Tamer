package net.sf.kpex.gui;

import java.awt.Button;
import net.sf.kpex.prolog.Prog;
import net.sf.kpex.prolog.Term;

/**
 * Button with attached Jinni action. Runs action on new thread, when Button
 * pushed.
 */
@Deprecated
public class JinniButton extends Button implements Runnable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2357137325209170073L;

    private Term action;

    private String name;

    private Prog prog;

    public JinniButton(String name, Term action) {
        super(name);
        this.name = name;
        this.action = action;
        prog = null;
    }

    /**
	 * Passes action to Jinni when Button is pushed
	 */
    public void run() {
        if (prog != null) {
            prog.stop();
        }
        prog = Prog.new_engine(action, action);
        prog.getElement();
        prog.stop();
    }
}
