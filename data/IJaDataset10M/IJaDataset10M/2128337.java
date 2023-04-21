package lt.baltic_amadeus.jqbridge.cmdproc;

/**
 * 
 * @author Baltic Amadeus, JSC
 * @author Antanas Kompanas
 *
 */
public class ShutdownServerCommand extends Command {

    public ShutdownServerCommand() {
    }

    public void execute(CommandProcessor cmdProc) {
        cmdProc.setExitNeeded(true);
    }
}
