package playground.benjamin.dataprepare;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.network.LinkImpl;
import org.matsim.core.network.MatsimNetworkReader;
import org.matsim.core.network.NetworkImpl;
import org.matsim.core.network.NetworkWriter;
import org.matsim.core.network.algorithms.NetworkCalcTopoType;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.misc.ConfigUtils;

/**
 * Simplifies a given network, by merging links.
 *
 * @author benjamin after aneumann
 *
 */
public class NetworkSimplifier {

    private static final Logger log = Logger.getLogger(NetworkSimplifier.class);

    private boolean mergeLinkStats = false;

    private Set<Integer> nodeTopoToMerge = new TreeSet<Integer>();

    public static void main(String[] args) {
        Set<Integer> nodeTypesToMerge = new TreeSet<Integer>();
        nodeTypesToMerge.add(new Integer(4));
        nodeTypesToMerge.add(new Integer(5));
        Scenario scenario = (ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig());
        final Network network = scenario.getNetwork();
        new MatsimNetworkReader(scenario).readFile("../../detailedEval/Net/network-86-85-87-84_withLanes.xml");
        NetworkSimplifier nsimply = new NetworkSimplifier();
        nsimply.setNodesToMerge(nodeTypesToMerge);
        nsimply.setMergeLinkStats(true);
        nsimply.run(network);
        new NetworkWriter(network).write("../../detailedEval/Net/network-86-85-87-84_simplifiedWithStrongLinkMerge---withLanes.xml");
    }

    public void run(final Network network) {
        if (this.nodeTopoToMerge.size() == 0) {
            Gbl.errorMsg("No types of node specified. Please use setNodesToMerge to specify which nodes should be merged");
        }
        log.info("running " + this.getClass().getName() + " algorithm...");
        log.info("  checking " + network.getNodes().size() + " nodes and " + network.getLinks().size() + " links for dead-ends...");
        NetworkCalcTopoType nodeTopo = new NetworkCalcTopoType();
        nodeTopo.run(network);
        for (Node node : network.getNodes().values()) {
            if (this.nodeTopoToMerge.contains(Integer.valueOf(nodeTopo.getTopoType(node)))) {
                List<Link> iLinks = new ArrayList<Link>(node.getInLinks().values());
                for (Link iL : iLinks) {
                    LinkImpl inLink = (LinkImpl) iL;
                    List<Link> oLinks = new ArrayList<Link>(node.getOutLinks().values());
                    for (Link oL : oLinks) {
                        LinkImpl outLink = (LinkImpl) oL;
                        if (inLink != null && outLink != null) {
                            if (!outLink.getToNode().equals(inLink.getFromNode())) {
                                if (this.mergeLinkStats) {
                                    LinkImpl link = (LinkImpl) network.getFactory().createLink(new IdImpl(inLink.getId() + "-" + outLink.getId()), inLink.getFromNode().getId(), outLink.getToNode().getId());
                                    link.setLength(inLink.getLength() + outLink.getLength());
                                    link.setFreespeed((inLink.getLength() + outLink.getLength()) / (inLink.getFreespeedTravelTime() + outLink.getFreespeedTravelTime()));
                                    link.setCapacity(Math.min(inLink.getCapacity(), outLink.getCapacity()));
                                    link.setNumberOfLanes((inLink.getLength() * inLink.getNumberOfLanes() + outLink.getLength() * outLink.getNumberOfLanes()) / (inLink.getLength() + outLink.getLength()));
                                    link.setOrigId(null);
                                    link.setType(outLink.getType());
                                    network.addLink(link);
                                    network.removeLink(inLink.getId());
                                    network.removeLink(outLink.getId());
                                } else {
                                    if (bothLinksHaveSameLinkStats(inLink, outLink)) {
                                        LinkImpl newLink = ((NetworkImpl) network).createAndAddLink(new IdImpl(inLink.getId() + "-" + outLink.getId()), inLink.getFromNode(), outLink.getToNode(), inLink.getLength() + outLink.getLength(), inLink.getFreespeed(), inLink.getCapacity(), outLink.getNumberOfLanes(), null, outLink.getType());
                                        newLink.setAllowedModes(inLink.getAllowedModes());
                                        network.removeLink(inLink.getId());
                                        network.removeLink(outLink.getId());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        org.matsim.core.network.algorithms.NetworkCleaner nc = new org.matsim.core.network.algorithms.NetworkCleaner();
        nc.run(network);
        nodeTopo = new NetworkCalcTopoType();
        nodeTopo.run(network);
        log.info("  resulting network contains " + network.getNodes().size() + " nodes and " + network.getLinks().size() + " links.");
        log.info("done.");
    }

    /**
		 * Compare link attributes. Return whether they are the same or not.
		 */
    private boolean bothLinksHaveSameLinkStats(LinkImpl linkA, LinkImpl linkB) {
        boolean bothLinksHaveSameLinkStats = true;
        if (linkA.getFreespeed() != linkB.getFreespeed()) {
            bothLinksHaveSameLinkStats = false;
        }
        if (linkA.getCapacity() != linkB.getCapacity()) {
            bothLinksHaveSameLinkStats = false;
        }
        return bothLinksHaveSameLinkStats;
    }

    /**
	 * Specify the types of node which should be merged.
	 *
	 * @param nodeTypesToMerge A Set of integer indicating the node types as specified by {@link NetworkCalcTopoType}
	 * @see NetworkCalcTopoType NetworkCalcTopoType for a list of available classifications.
	 */
    public void setNodesToMerge(Set<Integer> nodeTypesToMerge) {
        this.nodeTopoToMerge.addAll(nodeTypesToMerge);
    }

    /**
	 *
	 * @param mergeLinkStats If set true, links will be merged despite their different attributes.
	 *  If set false, only links with the same attributes will be merged, thus preserving as much information as possible.
	 *  Default is set false.
	 */
    public void setMergeLinkStats(boolean mergeLinkStats) {
        this.mergeLinkStats = mergeLinkStats;
    }
}
