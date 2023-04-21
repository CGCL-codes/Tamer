package drm.test.simulator;

import java.util.Map;

public class RobustnessTester implements NodeFollower {

    private Net net = null;

    private int cycles = 0;

    private GraphAlgorithms gra = new GraphAlgorithms();

    private final int STAT_CYCLE;

    public RobustnessTester(Net net) {
        this.net = net;
        STAT_CYCLE = Integer.parseInt(net.getProperty("follower.stat_cycle", "49"));
    }

    public void otherCache(int peer) {
    }

    public boolean nextCycle() {
        if (++cycles <= STAT_CYCLE) return true;
        int i;
        final int DELETE_RATE = net.size() / 200;
        Graph ug = new ConstUndirGraph(net);
        PrefixSubGraph sg = new PrefixSubGraph(ug);
        for (i = 35 * net.size() / 100; i > 0; i -= DELETE_RATE) {
            sg.setSize(i);
            Map m = gra.weaklyConnectedClusters(sg);
            System.out.println(m.size() + " " + m);
        }
        return false;
    }
}
