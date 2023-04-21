package playground.gregor.evacuation;

import java.util.concurrent.ConcurrentLinkedQueue;
import org.matsim.config.Config;
import org.matsim.gbl.Gbl;
import org.matsim.network.Link;
import org.matsim.network.MatsimNetworkReader;
import org.matsim.network.NetworkLayer;
import org.matsim.network.NetworkWriter;

public class NetworkAdjuster {

    private NetworkLayer network;

    private double storageCap;

    private double flowCap;

    public NetworkAdjuster(NetworkLayer network) {
        this.network = network;
    }

    public void setStorageCap(double storageCap) {
        this.storageCap = storageCap;
    }

    public void setFlowCap(double flowCap) {
        this.flowCap = flowCap;
    }

    public NetworkLayer performChanges(double effectiveCellSize, double effectiveLaneWidth) {
        this.network.setEffectiveCellSize(effectiveCellSize);
        this.network.setEffectiveLaneWidth(effectiveLaneWidth);
        ConcurrentLinkedQueue<Link> links = new ConcurrentLinkedQueue<Link>();
        for (Link link : this.network.getLinks().values()) {
            links.add(link);
        }
        while (links.peek() != null) {
            Link link = links.poll();
            String id = link.getId().toString();
            String from = link.getFromNode().getId().toString();
            String to = link.getToNode().getId().toString();
            String length = Double.toString(link.getLength());
            String freespeed = Double.toString(link.getFreespeed(org.matsim.utils.misc.Time.UNDEFINED_TIME));
            String capacity = Double.toString(link.getCapacity(org.matsim.utils.misc.Time.UNDEFINED_TIME));
            String origid = link.getOrigId();
            String type = link.getType();
            double min_width = link.getCapacity(org.matsim.utils.misc.Time.UNDEFINED_TIME) / this.flowCap;
            double storage = min_width * link.getLength() * this.storageCap;
            double laneCap = this.network.getEffectiveLaneWidth() * link.getLength() * this.storageCap;
            double lanes = Math.max(storage / laneCap, 1.0);
            String permlanes = Double.toString(lanes);
            this.network.removeLink(link);
            this.network.createLink(id, from, to, length, freespeed, capacity, permlanes, origid, type);
        }
        return this.network;
    }

    public static void main(String[] args) {
        final double storageCap = 5.4;
        final double flowCap = 1.33;
        final double effCS = 0.26;
        final double effLW = 0.71;
        String configFile = "./configs/timeVariantEvac.xml";
        Config config = Gbl.createConfig(new String[] { configFile });
        System.out.println("  reading the network...");
        NetworkLayer network = null;
        network = (NetworkLayer) Gbl.getWorld().createLayer(NetworkLayer.LAYER_TYPE, null);
        new MatsimNetworkReader(network).readFile(config.network().getInputFile());
        System.out.println("  done.");
        NetworkAdjuster na = new NetworkAdjuster(network);
        na.setStorageCap(storageCap);
        na.setFlowCap(flowCap);
        network = na.performChanges(effCS, effLW);
        NetworkWriter writer = new NetworkWriter(network, "padang_net2.xml");
        writer.write();
    }
}
