package jxl.jxta;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;
import jxl.util.TimeUtils;
import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.Message;
import net.jxta.exception.PeerGroupException;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupFactory;
import net.jxta.pipe.OutputPipe;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.rendezvous.RendezVousStatus;
import net.jxta.util.JxtaBiDiPipe;

/**
 * Utillite rutines for jxta
 * @author Alex Lynch
 */
public final class JxtaUtils {

    public static void waitForRendezVous(PeerGroup group) {
        if (!group.getRendezVousService().isConnectedToRendezVous()) {
            System.out.print("Waiting for " + group.getPeerGroupName() + " RendezVous");
            while (!group.getRendezVousService().isConnectedToRendezVous()) {
                System.out.print('.');
                TimeUtils.quietSleep(500);
            }
            System.out.println("\nConnected.");
        }
    }

    public static void printRdvStatus(PeerGroup group) {
        RendezVousStatus status = group.getRendezVousService().getRendezVousStatus();
        if (status == status.ADHOC) System.out.println("adhoc");
        if (status == status.AUTO_EDGE) System.out.println("auto edge");
        if (status == status.AUTO_RENDEZVOUS) System.out.println("auto rdv");
        if (status == status.EDGE) System.out.println("edge");
        if (status == status.NONE) System.out.println("none");
        if (status == status.RENDEZVOUS) System.out.println("redezvous");
        if (status == status.UNKNOWN) System.out.println("unknown");
    }

    public static void forceMessage(OutputPipe out, Message m) throws IOException {
        while (!out.send(m)) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    public static void forceMessage(JxtaBiDiPipe out, Message m) throws IOException {
        while (!out.sendMessage(m)) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    private static PeerGroup net;

    /**
     * Gets the jxta NetPeerGroup and saves it for later invocations
     */
    public static PeerGroup getNetGroup() throws PeerGroupException {
        if (net == null) net = PeerGroupFactory.newNetPeerGroup();
        return net;
    }

    private JxtaUtils() {
    }
}
