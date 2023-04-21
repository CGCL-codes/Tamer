package fr.esrf.tangoatk.core;

import fr.esrf.tangoatk.core.command.ACommand;
import fr.esrf.tangoatk.core.command.CommandFactory;
import fr.esrf.tangoatk.core.command.VoidVoidCommand;
import java.util.List;

/**
 * @deprecated  As of release ATKCore-4.3.1 and higher, please use instead fr.esrf.tangoatk.core.command.VoidVoidCommandGroup
 */
@Deprecated
public class CommandGroup extends CommandList implements ICommandGroup {

    protected EventSupport propChanges;

    /**
     * Creates a new <code>CommandGroup</code> instance, and 
     * instanciates its command factory.
     */
    public CommandGroup() {
        factory = CommandFactory.getInstance();
        filter = new IEntityFilter() {

            public boolean keep(IEntity entity) {
                if (entity instanceof VoidVoidCommand) return true; else return false;
            }
        };
        propChanges = new EventSupport();
    }

    public void setFilter(IEntityFilter filter) {
        return;
    }

    public void addEndGroupExecutionListener(IEndGroupExecutionListener l) {
        propChanges.addEndGroupExecutionListener(l);
    }

    public void removeEndGroupExecutionListener(IEndGroupExecutionListener l) {
        propChanges.removeEndGroupExecutionListener(l);
    }

    protected void publishEndExecution(List result) {
        propChanges.fireEndGroupExecutionEvent(this, result);
    }

    public void execute() {
        long t0 = System.currentTimeMillis();
        IEntity ie = null;
        ICommand ic = null;
        DeviceFactory.getInstance().trace(DeviceFactory.TRACE_COMMAND, "CommandGroup.execute()  ", t0);
        for (int i = 0; i < size(); i++) {
            ie = (IEntity) get(i);
            if (!(ie instanceof VoidVoidCommand)) continue;
            ic = (ICommand) ie;
            ic.execute();
        }
        DeviceFactory.getInstance().trace(DeviceFactory.TRACE_COMMAND, "CommandGroup.execute()  end of loop", t0);
        publishEndExecution(null);
    }

    public String getCmdName() {
        String cmd = null;
        if (this.getSize() > 0) {
            ACommand acmd = (ACommand) (this.get(0));
            if (acmd != null) cmd = acmd.getNameSansDevice();
        }
        return cmd;
    }

    public String getVersion() {
        return "$Id $";
    }
}
