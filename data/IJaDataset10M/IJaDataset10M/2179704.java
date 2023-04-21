package se.sics.cooja.motes;

import java.awt.Container;
import org.apache.log4j.Logger;
import se.sics.cooja.AbstractionLevelDescription;
import se.sics.cooja.COOJARadioPacket;
import se.sics.cooja.ClassDescription;
import se.sics.cooja.Mote;
import se.sics.cooja.MoteTimeEvent;
import se.sics.cooja.MoteType;
import se.sics.cooja.RadioPacket;
import se.sics.cooja.Simulation;
import se.sics.cooja.MoteType.MoteTypeCreationException;
import se.sics.cooja.interfaces.ApplicationRadio;
import se.sics.cooja.interfaces.Radio.RadioEvent;

/**
 * Simple application-level mote that periodically transmits dummy radio packets
 * on all radio channels (-1), interfering all surrounding radio communication.
 * 
 * This mote type also implements the mote functionality ("mote software"),
 * and can be used as an example of implementing application-level mote.
 *
 * @see DisturberMote
 * @author Fredrik Osterlind, Thiemo Voigt
 */
@ClassDescription("Disturber Mote Type")
@AbstractionLevelDescription("Application level")
public class DisturberMoteType extends AbstractApplicationMoteType {

    private static Logger logger = Logger.getLogger(DisturberMoteType.class);

    public DisturberMoteType() {
        super();
    }

    public DisturberMoteType(String identifier) {
        super(identifier);
        setDescription("Disturber Mote Type #" + identifier);
    }

    public boolean configureAndInit(Container parentContainer, Simulation simulation, boolean visAvailable) throws MoteTypeCreationException {
        if (!super.configureAndInit(parentContainer, simulation, visAvailable)) {
            return false;
        }
        setDescription("Disturber Mote Type #" + getIdentifier());
        return true;
    }

    public Mote generateMote(Simulation simulation) {
        return new DisturberMote(this, simulation);
    }

    public static class DisturberMote extends AbstractApplicationMote {

        private ApplicationRadio radio = null;

        private final RadioPacket radioPacket = new COOJARadioPacket(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 });

        private static final long DELAY = Simulation.MILLISECOND / 5;

        private static final long DURATION = 10 * Simulation.MILLISECOND;

        public DisturberMote() {
            super();
        }

        public DisturberMote(MoteType moteType, Simulation simulation) {
            super(moteType, simulation);
        }

        public void execute(long time) {
            if (radio == null) {
                radio = (ApplicationRadio) getInterfaces().getRadio();
            }
            radio.startTransmittingPacket(radioPacket, DURATION);
        }

        public void receivedPacket(RadioPacket p) {
        }

        public void sentPacket(RadioPacket p) {
            getSimulation().scheduleEvent(new MoteTimeEvent(this, 0) {

                public void execute(long t) {
                    radio.startTransmittingPacket(radioPacket, DURATION);
                }
            }, getSimulation().getSimulationTime() + DELAY);
        }

        public String toString() {
            return "Disturber " + getID();
        }
    }
}
