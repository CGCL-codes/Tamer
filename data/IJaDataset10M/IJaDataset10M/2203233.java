package net.sf.asyncobjects.jca.impl;

import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.UnavailableException;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.xa.XAResource;
import net.sf.asyncobjects.AsyncAction;
import net.sf.asyncobjects.AsyncObject;
import net.sf.asyncobjects.Promise;
import net.sf.asyncobjects.j2ee.AServiceRegistry;
import net.sf.asyncobjects.j2ee.JCARunner;
import net.sf.asyncobjects.j2ee.ServiceRegistry;
import net.sf.asyncobjects.util.timer.ATimer;
import net.sf.asyncobjects.util.timer.TimerWrapper;
import net.sf.asyncobjects.vats.Vat;
import net.sf.asyncobjects.vats.VatRunner;

/**
 * Main class of the JCA VatRunner connector.
 **/
public class JCARunnerResourceAdapter implements ResourceAdapter {

    /** Logger. */
    private static final Logger log = Logger.getLogger(JCARunnerResourceAdapter.class.getName());

    /**
     * JCA Bootstrap context. 
     */
    private BootstrapContext bootstrapContext;

    /**
     * Single instance of {@link JCARunner}.
     */
    private JCARunner runner;

    /**
     * Timer from bottstrap context (i.e. runs in special
     * thread from app server). By default is <code>null</code>
     * and created only when really needed. 
     */
    private TimerWrapper timer;

    /**
     * Registry for {@link AsyncObject}.
     */
    private AServiceRegistry registry;

    /**
     * Internal lock object.
     * Used for:
     * 1. {@link #bootstrapContext} field access synchronization;
     * 2. {@link #runner} field access synchronization;
     * 3. {@link #timerVat} field access synchronization;
     */
    private final Object lock = new Object();

    /**
     * A vat timer runs in.
     */
    private Vat timerVat;

    /**
     * A registry timer runs in.
     */
    private Vat registryVat;

    /**
     * Lock for {@link #timer} field.
     */
    private Object timerLock = new Object();

    ;

    /**
     * {@inheritDoc}
     */
    public void start(BootstrapContext ctx) throws ResourceAdapterInternalException {
        synchronized (lock) {
            this.bootstrapContext = ctx;
            runner = new JCARunner(ctx.getWorkManager());
            timerVat = runner.newVat("TimerVat");
            registryVat = runner.newVat("RegistryVat");
            registryVat.enqueue(new Runnable() {

                public void run() {
                    registry = new ServiceRegistry().export();
                }
            });
        }
        if (log.isLoggable(Level.INFO)) {
            log.info("JCA VatRunner Connector started.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void stop() {
        if (log.isLoggable(Level.INFO)) {
            log.info("JCA VatRunner Connector stopping...");
        }
        synchronized (timerLock) {
            if (timer != null) {
                timer.forceClose();
                timer = null;
            }
        }
        JCARunner tempRunner = null;
        synchronized (lock) {
            this.bootstrapContext = null;
            tempRunner = runner;
            runner = null;
        }
        try {
            tempRunner.stop(true);
        } catch (InterruptedException ex) {
            if (log.isLoggable(Level.SEVERE)) {
                log.log(Level.SEVERE, "JCA VatRunner stop failed.", ex);
            }
        }
        if (log.isLoggable(Level.INFO)) {
            log.info("JCA VatRunner Connector stopped.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public XAResource[] getXAResources(ActivationSpec[] arg0) throws ResourceException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void endpointActivation(MessageEndpointFactory factory, ActivationSpec spec) throws ResourceException {
    }

    /**
     * {@inheritDoc}
     */
    public void endpointDeactivation(MessageEndpointFactory factory, ActivationSpec spec) {
    }

    /**
     * Returns {@link VatRunner}.
     * 
     * @return {@link VatRunner}.
     */
    VatRunner getRunner() {
        synchronized (lock) {
            return runner;
        }
    }

    /**
     * Returns {@link Promise} for {@link ATimer}.
     * 
     * @return {@link Promise} for {@link ATimer}.
     */
    Promise<ATimer> getTimer() {
        Vat tempTimerVat;
        synchronized (lock) {
            tempTimerVat = this.timerVat;
        }
        return new AsyncAction<ATimer>() {

            @Override
            public Promise<ATimer> run() throws Throwable {
                TimerWrapper temp;
                synchronized (timerLock) {
                    if (timer == null) {
                        timer = new TimerWrapper(getTimerInternal());
                    }
                    temp = timer;
                }
                return temp.promise();
            }
        }.doInOtherVat(tempTimerVat);
    }

    /**
     * Registry for {@link AsyncObject}s.
     * 
     * @return registry for {@link AsyncObject}s.
     */
    Promise<AServiceRegistry> getServiceRegistry() {
        return new AsyncAction<AServiceRegistry>() {

            @Override
            public Promise<? extends AServiceRegistry> run() throws Throwable {
                return Promise.with(registry);
            }
        }.doInOtherVat(registryVat);
    }

    /**
     * Returns {@link Timer} from bootstrap context.
     * 
     * @return {@link Timer} from bootstrap context.
     * @throws UnavailableException if timer cannot be created.
     */
    private Timer getTimerInternal() throws UnavailableException {
        synchronized (lock) {
            return this.bootstrapContext.createTimer();
        }
    }
}
