package jhomenet.server.boot;

import jhomenet.commons.boot.AbstractVoidBootTask;
import jhomenet.commons.boot.BootException;
import jhomenet.commons.boot.BootTaskManager;
import jhomenet.server.plugin.AbstractPluginLoader;

/**
 * TODO: Class description.
 *
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public class LoadPluginsBootTask extends AbstractVoidBootTask {

    /**
	 * 
	 * @param bootManager
	 */
    public LoadPluginsBootTask(BootTaskManager bootManager) {
        super(bootManager);
    }

    /**
	 * @see jhomenet.commons.boot.BootTask#execute()
	 */
    public void execute() throws BootException {
        if (!AbstractPluginLoader.getInitialized()) AbstractPluginLoader.initialize();
    }

    /**
	 * @see jhomenet.commons.boot.BootTask#getTaskIncrement()
	 */
    public Integer getTaskIncrement() {
        return 200;
    }

    /**
	 * @see jhomenet.commons.boot.BootTask#getTaskName()
	 */
    public String getTaskName() {
        return this.getClass().getName();
    }

    /**
	 * @see jhomenet.commons.boot.BootTask#getTaskString()
	 */
    public String getTaskString() {
        return "Loading plugins";
    }

    /**
	 * @see jhomenet.commons.boot.BootTask#systemFailureOnException()
	 */
    public Boolean systemFailureOnException() {
        return false;
    }
}
