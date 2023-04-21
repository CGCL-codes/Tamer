package cz.cas.ujf.falcon.feeder;

import cz.cas.ujf.falcon.demand.FileDemand;
import cz.cas.ujf.falcon.network.Site;
import cz.cas.ujf.falcon.network.Network;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

/**
 * Class for generating file demands, which origins are distinct single sites.
 *
 * @author: <a href="mailto:zerola@matfyz.cz">Michal Zerola</a>
 * @version: $Id: DistinctFileDemandsFeeder.java,v 1.1.1.1 2009/01/04 18:55:11 zer0 Exp $
 * @URL: $HeadURL$
 */
public class DistinctFileDemandsFeeder implements FileDemandsFeeder {

    private Network network;

    private String[] siteNames;

    /**
     * Constructor.
     *
     * @param network   network configuration
     * @param siteNames array of site names
     */
    public DistinctFileDemandsFeeder(Network network, String[] siteNames) {
        this.network = network;
        this.siteNames = siteNames;
    }

    /**
     * Generates list of file demands, where each file will be available only at one site.
     *
     * @param numberOfFiles number of files to generate
     * @return list of file demands, each file available at one site only
     */
    public List<FileDemand> generateDemands(int numberOfFiles) {
        List<FileDemand> demands = new ArrayList<FileDemand>();
        for (int d = 0; d < numberOfFiles; d++) {
            Set<Site> origins = new HashSet<Site>();
            try {
                origins.add(network.getSiteByName(siteNames[d % siteNames.length]));
                demands.add(new FileDemand("file_" + d, origins));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return demands;
    }
}
