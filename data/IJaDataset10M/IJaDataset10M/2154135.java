package net.taylor.lang;

import java.util.NoSuchElementException;
import javax.transaction.Synchronization;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.jboss.seam.transaction.Transaction;
import org.jboss.seam.transaction.UserTransaction;

public abstract class ClassloaderSwitcher {

    protected static final Log logger = Logging.getLog(ClassloaderSwitcher.class);

    private ClassLoader previousContextClassLoader;

    private ClassLoader currentContextClassLoader;

    public void execute() {
        try {
            switchClassLoader();
            doWork();
        } finally {
            registerSynchronization();
        }
    }

    protected abstract void doWork();

    protected void switchClassLoader() {
        if (this.previousContextClassLoader == null) {
            Thread currentThread = Thread.currentThread();
            this.previousContextClassLoader = currentThread.getContextClassLoader();
            this.currentContextClassLoader = getClass().getClassLoader();
            currentThread.setContextClassLoader(this.currentContextClassLoader);
            logger.debug("switchClassLoader: {0}, previous: {1}, current: {2}", this, this.previousContextClassLoader, this.currentContextClassLoader);
        }
    }

    protected void switchClassLoaderBack() {
        if (this.previousContextClassLoader != null) {
            logger.debug("switchClassLoaderBack: {0}, previous: {1}, current: {2}", this, this.previousContextClassLoader, this.currentContextClassLoader);
            Thread.currentThread().setContextClassLoader(this.previousContextClassLoader);
            this.previousContextClassLoader = null;
        }
    }

    protected void registerSynchronization() {
        try {
            UserTransaction transaction = Transaction.instance();
            if (transaction.isActiveOrMarkedRollback()) {
                try {
                    transaction.registerSynchronization(new Synchronization() {

                        public void beforeCompletion() {
                        }

                        public void afterCompletion(int status) {
                            switchClassLoaderBack();
                        }
                    });
                } catch (NoSuchElementException e) {
                    logger.debug(e);
                    switchClassLoaderBack();
                }
            } else {
                switchClassLoaderBack();
            }
        } catch (Exception e) {
            logger.warn(e);
            switchClassLoaderBack();
        }
    }
}
