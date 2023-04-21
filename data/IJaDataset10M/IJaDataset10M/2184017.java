package gov.sns.xal.tools.orbit;

import gov.sns.tools.correlator.*;
import gov.sns.tools.messaging.MessageCenter;
import java.util.*;

/**
 * OrbitBuffer maintains statistics on the orbit based on several measurements.
 * An instance of OrbitBuffer maintains a circular buffer of orbit measurements. 
 * The size of the buffer is fixed for an instance of OrbitBuffer.
 *
 * @author  tap
 */
public class OrbitBuffer implements CorrelationNotice {

    protected int bufferSize;

    protected LinkedList<Correlation> orbitBuffer;

    protected MutableOrbitRecord orbit;

    protected MessageCenter messageCenter;

    protected OrbitListener orbitProxy;

    /** Creates a new instance of OrbitBuffer */
    public OrbitBuffer(OrbitCorrelator correlator, int size) {
        bufferSize = size;
        messageCenter = new MessageCenter();
        orbitProxy = messageCenter.registerSource(this, OrbitListener.class);
        orbitBuffer = new LinkedList<Correlation>();
        orbit = new MutableOrbitRecord();
        correlator.addListener(this);
    }

    /** size of the circular buffer of measurements */
    public int bufferSize() {
        return bufferSize;
    }

    /** set the buffer size to a new size */
    public synchronized void setBufferSize(int newBufferSize) {
        bufferSize = newBufferSize;
    }

    /** number of buffer samples */
    public int numSamples() {
        return orbitBuffer.size();
    }

    /** add a listener to notify when the orbit statistics change */
    public void addOrbitListener(OrbitListener listener) {
        messageCenter.registerTarget(listener, this, OrbitListener.class);
    }

    /** remove a listener of orbit statistics changes */
    public void removeOrbitListener(OrbitListener listener) {
        messageCenter.removeTarget(listener, this, OrbitListener.class);
    }

    /** clear the buffer */
    public synchronized void reset() {
        orbitBuffer.clear();
        orbit.reset();
    }

    /** implement CorrelationNotice interface */
    public synchronized void newCorrelation(Object sender, Correlation newCorrelation) {
        orbitBuffer.addLast(newCorrelation);
        orbit.addSample(newCorrelation);
        while (orbitBuffer.size() > bufferSize) {
            Correlation oldCorrelation = orbitBuffer.removeFirst();
            orbit.removeSample(oldCorrelation);
        }
        orbitProxy.newOrbit(this, new OrbitRecord(orbit));
    }

    /** implement CorrelationNotice interface */
    public void noCorrelationCaught(Object sender) {
    }
}
