package jmud.engine.commands;

import jmud.engine.job.JobManager;
import jmud.engine.job.definitions.AbstractClientJob;
import jmud.engine.netio.JMudClient;
import java.util.ArrayList;

/**
 * @author david.h.loman
 */
public abstract class AbstractCommand extends AbstractClientJob {

    /**
	 * Aliases for the command.
	 */
    protected ArrayList<String> aliases = new ArrayList<String>();

    /**
	 * The string command received from the client, only broken down into an
	 * array by .split(" ")
	 */
    private String[] cmdArray;

    public AbstractCommand(JMudClient c, String[] cmdArray) {
        super(c);
        this.cmdArray = cmdArray;
        this.setAliases();
    }

    public AbstractCommand(JobManager jm, JMudClient c, String[] cmdArray) {
        super(jm, c);
        this.cmdArray = cmdArray;
        this.setAliases();
    }

    /**
	 * @return an ArrayList of aliases for the command
	 */
    public final ArrayList<String> getAliases() {
        return aliases;
    }

    public String[] getCmdArray() {
        return cmdArray;
    }

    @Override
    public boolean doJob() {
        boolean retval = true;
        if (this.c == null || this.cmdArray == null) {
            retval = false;
        } else {
            this.c.sendCRLF();
            this.c.sendTextLn("Processing a " + this.getClass().getSimpleName());
            retval = this.doCmd();
        }
        return retval;
    }

    protected abstract boolean doCmd();

    public abstract AbstractCommand getNewInstance(JMudClient c, JobManager jm, String[] cmdArray);

    protected abstract void setAliases();
}
