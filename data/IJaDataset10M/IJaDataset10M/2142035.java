package ppine.logicmodel.controllers;

import ppine.viewmodel.controllers.CytoNetworkConverter;
import java.util.Collection;
import ppine.viewmodel.structs.CytoPPINetwork;
import ppine.logicmodel.structs.SpeciesTreeNode;

public class NetworksConverter {

    public static void convertNetworks(Collection<SpeciesTreeNode> networks) {
        for (SpeciesTreeNode network : networks) {
            CytoPPINetwork cytoNetwork = NetworkConverter.convertPPINetwork(network);
            CytoNetworkConverter.convertCytoNetwork(cytoNetwork);
        }
    }
}
