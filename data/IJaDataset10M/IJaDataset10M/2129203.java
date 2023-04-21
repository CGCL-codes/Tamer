package ecskernel;

import rescuecore.RescueComponent;
import rescuecore.RescueConstants;
import rescuecore.RescueMessage;
import rescuecore.RescueObject;
import rescuecore.commands.Command;
import rescuecore.commands.Commands;
import rescuecore.commands.KVConnectError;
import rescuecore.commands.KVConnectOK;
import rescuecore.commands.Update;
import rescuecore.commands.VKAcknowledge;
import rescuecore.commands.VKConnect;

public abstract class ARescuecoreViewerComponent extends RescueComponent {

    protected boolean isRunning;

    /**
	 * Generates the Command used by this class to connect to the kernel.
	 *
	 * @return VKConnect, a command used to connect on the port used to
	 *         communicate with viewers.
	 */
    public final Command generateConnectCommand() {
        return new VKConnect();
    }

    /**
	 * Returns the type of the RescueComponent held in this class.
	 *
	 * @return A constant representing the type Viewer.
	 */
    public final int getComponentType() {
        return RescueConstants.COMPONENT_TYPE_VIEWER;
    }

    /**
	 * What the class is to do if the connect fails.
	 *
	 * @param c the KV_CONNECT_ERROR command
	 * @return a String containing the reason why the connect failed
	 */
    public final String handleConnectError(Command c) {
        KVConnectError err = (KVConnectError) c;
        System.out.println("Error: " + getName() + " cannot connect to kernel");
        return err.getReason();
    }

    /**
	 * How the class responds to a KV_CONNECT_OK command.
	 *
	 * @param c the KV_CONNNECT_OK Command.
	 */
    public boolean handleConnectOK(Command c) {
        System.out.println(getName() + " Connected");
        KVConnectOK ok = (KVConnectOK) c;
        RescueMessage ack = new RescueMessage();
        ack.append(new VKAcknowledge());
        sendMessage(ack);
        RescueObject[] knowledge = ok.getKnowledge();
        doConnectOKTasks(knowledge);
        isRunning = true;
        return true;
    }

    /**
	 * Decides what to do with messages received from the kernel.
	 *
	 * @param msg the Command received from the kernel
	 */
    public final void handleMessage(Command msg) {
        switch(msg.getType()) {
            case RescueConstants.UPDATE:
                handleUpdate((Update) msg);
                break;
            case RescueConstants.COMMANDS:
                handleCommands((Commands) msg);
                break;
        }
    }

    protected abstract void handleUpdate(Update msg);

    protected abstract void handleCommands(Commands msg);

    /**
	 * Check to see if this class currently running
	 *
	 * @return true iff the class is running
	 */
    public final boolean isRunning() {
        return isRunning;
    }

    /**
	 * Shuts down the EcsKernel class.
	 */
    public final void shutdown() {
        doShutdownTasks();
        isRunning = false;
    }

    public abstract String getName();

    protected abstract void doShutdownTasks();

    protected abstract void doConnectOKTasks(RescueObject[] knowledge);
}
