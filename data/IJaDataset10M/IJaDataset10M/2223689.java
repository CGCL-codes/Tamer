package org.boblight4j.server.config;

import java.io.File;
import java.util.List;
import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.JNotifyListener;
import org.boblight4j.server.ClientsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigUpdater extends AbstractConfigUpdater implements Runnable {

    private final class NotifyListener implements JNotifyListener {

        @Override
        public void fileCreated(final int wd, final String rootPath, final String name) {
            if (name.equals(ConfigUpdater.this.watchFile.getName())) {
                synchronized (ConfigUpdater.this.monitor) {
                    ConfigUpdater.this.monitor.notifyAll();
                }
            }
        }

        @Override
        public void fileDeleted(final int wd, final String rootPath, final String name) {
        }

        @Override
        public void fileModified(final int wd, final String rootPath, final String name) {
            if (name.equals(ConfigUpdater.this.watchFile.getName())) {
                synchronized (ConfigUpdater.this.monitor) {
                    ConfigUpdater.this.monitor.notifyAll();
                }
            }
        }

        @Override
        public void fileRenamed(final int wd, final String rootPath, final String oldName, final String newName) {
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(ConfigUpdater.class);

    private final Object monitor = new Object();

    private boolean stop;

    public ConfigUpdater(final File file, ConfigCreator configCreator, final ClientsHandler<?> clients, final AbstractConfig config) {
        super(file, configCreator, clients, config);
    }

    @Override
    public void run() {
        try {
            JNotify.addWatch(this.watchFile.getParent(), JNotify.FILE_CREATED | JNotify.FILE_MODIFIED, false, new NotifyListener());
        } catch (final JNotifyException e) {
            LOG.error("", e);
            return;
        }
        while (!this.stop) {
            synchronized (this.monitor) {
                try {
                    this.monitor.wait();
                    this.updateConfig();
                } catch (final InterruptedException e) {
                    LOG.warn("", e);
                }
            }
            synchronized (this) {
                this.notifyAll();
            }
        }
    }

    public void startThread() {
        new Thread(this, "ConfigUpdater").start();
    }
}
