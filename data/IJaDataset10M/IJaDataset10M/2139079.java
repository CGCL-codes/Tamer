package net.sf.dz3.device.actuator.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.sf.dz3.device.actuator.Damper;
import net.sf.jukebox.jmx.JmxDescriptor;
import net.sf.jukebox.sem.ACT;
import net.sf.jukebox.sem.SemaphoreGroup;
import net.sf.jukebox.service.Messenger;
import org.apache.log4j.NDC;

/**
 * Damper multiplexer.
 * 
 * Allows to control several physical dampers via one logical one. Each of controlled dampers
 * can be calibrated individually.
 * 
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org"> Vadim Tkachenko</a> 2001-2009
 */
public class DamperMultiplexer extends AbstractDamper {

    /**
     * Dampers to control.
     */
    private final Set<Damper> dampers = new HashSet<Damper>();

    /**
     * Create an instance.
     * 
     * @param name Name to use.
     * @param dampers Set of dampers to control.
     */
    public DamperMultiplexer(String name, Set<Damper> dampers) {
        super(name);
        this.dampers.addAll(dampers);
    }

    @Override
    protected synchronized void moveDamper(double position) throws IOException {
        for (Iterator<Damper> i = dampers.iterator(); i.hasNext(); ) {
            Damper d = i.next();
            try {
                d.set(position);
            } catch (IOException ex) {
                throw new IOException("One of controlled dampers failed", ex);
            }
        }
    }

    @Override
    public double getPosition() throws IOException {
        return dampers.iterator().next().getPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JmxDescriptor getJmxDescriptor() {
        return new JmxDescriptor("dz", "Damper multiplexer", Integer.toHexString(hashCode()), "Controls " + dampers.size() + " dampers");
    }

    /**
     * {@inheritDoc}
     */
    public ACT park() {
        logger.info(getName() + ": parking at " + getParkPosition());
        return new ParkingAssistant().start();
    }

    /**
     * Commands the {@link ServoDamper#servo} to move to {@link ServoDamper#getParkPosition
     * parked position} and waits until the servo has done so.
     */
    private class ParkingAssistant extends Messenger {

        /**
         * Move the {@link ServoDamper#servo} and wait until it gets there.
         */
        @Override
        protected final Object execute() throws Throwable {
            NDC.push("execute");
            try {
                SemaphoreGroup parked = new SemaphoreGroup();
                for (Iterator<Damper> i = dampers.iterator(); i.hasNext(); ) {
                    Damper d = i.next();
                    parked.add(d.park());
                }
                parked.waitForAll();
                logger.info(getName() + ": parked at " + getParkPosition());
            } catch (Throwable t) {
                logger.error(getName() + ": failed to park at " + getParkPosition(), t);
            } finally {
                NDC.pop();
            }
            return null;
        }
    }
}
