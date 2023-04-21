package org.mobicents.media.server.resource;

import org.mobicents.media.server.spi.Valve;

/**
 * Used as a factory class for creating inner pipes dynamic upon request
 * using assigned inlet's and outlet's factrories;
 * 
 * @author kulikov
 */
public class PipeFactory {

    private String inlet;

    private String outlet;

    private Valve valve;

    public String getInlet() {
        return inlet;
    }

    public String getOutlet() {
        return outlet;
    }

    public void setInlet(String inlet) {
        this.inlet = inlet;
    }

    public void setOutlet(String outlet) {
        this.outlet = outlet;
    }

    public Valve getValve() {
        return valve;
    }

    public void setValve(Valve valve) {
        this.valve = valve;
    }

    public void openPipe(Channel channel) throws UnknownComponentException {
        Pipe pipe = new Pipe(channel);
        pipe.setValve(this.valve);
        channel.openPipe(pipe, inlet, outlet);
    }
}
