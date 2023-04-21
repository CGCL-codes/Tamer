package uk.co.nimp.scard.terminalManager;

import com.atolsystems.atolutilities.CommandLine;
import com.atolsystems.atolutilities.CommandLine.Arg;
import com.atolsystems.atolutilities.ExternalRuntimeException;
import com.atolsystems.atolutilities.SimpleArgHandler;
import com.atolsystems.atolutilities.StopRequestFromUserException;
import java.io.File;
import java.util.List;
import uk.co.nimp.scard.GenericTerminal;
import uk.co.nimp.scard.ScardLogHandler;
import uk.co.nimp.scard.ScriptPlayer;
import uk.co.nimp.scard.StarScriptReader;

/**
 *
 * @author sebastien riou
 */
class StarScriptArgHandler extends SimpleArgHandler {

    static final String ARG_STAR_SCRIPT_FILE = "starScript:";

    final ScriptPlayer scriptPlayer;

    final GenericTerminal terminal;

    int nRun = 1;

    Throwable error = null;

    public StarScriptArgHandler(GenericTerminal terminal) {
        scriptPlayer = new ScriptPlayer();
        this.terminal = terminal;
    }

    public Throwable getError() {
        return error;
    }

    public int getnRun() {
        return nRun;
    }

    public void setnRun(int nRun) {
        this.nRun = nRun;
    }

    public long getDeadTime() {
        return scriptPlayer.getDeadTime();
    }

    public boolean processArg(Arg arg, CommandLine cl) {
        File in = arg.getFile(arg.value.substring(ARG_STAR_SCRIPT_FILE.length()));
        try {
            List<? extends Object> cmds;
            terminal.logLine(ScardLogHandler.LOG_INFO, "SCRIPT: " + in.getCanonicalPath());
            StarScriptReader script = new StarScriptReader(in);
            cmds = script.getCmds();
            scriptPlayer.setnRun(nRun);
            scriptPlayer.play(terminal, cmds);
            if (null == error) {
                if (scriptPlayer.isErrorOccured()) throw new ExternalRuntimeException("An error occured during execution of script:\n" + in.getCanonicalPath()); else if (false == scriptPlayer.isCheckOperation()) throw new ExternalRuntimeException("Response checking has been disabled during execution of script:\n" + in.getCanonicalPath());
            }
        } catch (StopRequestFromUserException e) {
            throw e;
        } catch (Throwable e) {
            error = e;
            throw new RuntimeException(e);
        }
        return true;
    }
}
