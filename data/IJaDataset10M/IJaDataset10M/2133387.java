package jdice.app.commands;

import jdice.app.Command;
import jdice.app.JDiceApp;

/**
 * \@evalint command
 * @author phatonin
 *
 */
public class EvaluateInt implements Command {

    private final Boolean value;

    /**
	 * Constructs an int evaluation command.
	 * @param value
	 */
    public EvaluateInt(Boolean value) {
        super();
        this.value = value;
    }

    public EvaluateInt() {
        this(null);
    }

    @Override
    public void execute(JDiceApp app) {
        boolean value = this.value == null ? !app.isEvaluateInt() : this.value;
        app.setEvaluateInt(value);
    }
}
