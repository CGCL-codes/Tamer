package cz.cas.ujf.falcon.solver.p2p;

import cz.cas.ujf.falcon.demand.FileDemand;
import cz.cas.ujf.falcon.network.Site;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Random;

/**
 * ----- Class comment -------------
 *
 * @author: <a href="mailto:zerola@matfyz.cz">Michal Zerola</a>
 * @version: $Id: LinkManager.java,v 1.1.1.1 2009/01/04 18:55:12 zer0 Exp $
 * @URL: $HeadURL$
 */
class LinkManager {

    private ArrayList<FileDemand> filesAtSite = new ArrayList<FileDemand>();

    private Map<FileDemand, Integer> filesStatus;

    private Site mySite;

    private int fileIndex = 0;

    private int busyTill = 0;

    private int mySpeed;

    public LinkManager(Site startSite, int mySpeed, List<FileDemand> files, Map<FileDemand, Integer> filesStatus) {
        this.mySite = startSite;
        this.mySpeed = mySpeed;
        this.filesStatus = filesStatus;
        for (FileDemand file : files) {
            if (file.getFileOrigins().contains(mySite)) filesAtSite.add(file);
        }
    }

    public void printFiles() {
        System.out.println("Site: " + mySite.getName());
        for (FileDemand file : filesAtSite) {
            System.out.println(file + " " + filesStatus.get(file));
        }
    }

    public int wakeup(int timeCounter) {
        if (timeCounter < busyTill) return busyTill;
        int cardinality = Integer.MAX_VALUE;
        for (FileDemand file : filesAtSite) {
            if ((filesStatus.get(file) == 0) && (file.getFileOrigins().size() < cardinality)) cardinality = file.getFileOrigins().size();
        }
        ArrayList<FileDemand> candidates = new ArrayList<FileDemand>();
        for (FileDemand file : filesAtSite) {
            if ((file.getFileOrigins().size() == cardinality) && (filesStatus.get(file) == 0)) candidates.add(file);
        }
        if (candidates.size() > 0) {
            Random generator = new Random();
            int fileIndex = generator.nextInt(candidates.size());
            busyTill = timeCounter + mySpeed;
            filesStatus.put(candidates.get(fileIndex), 1);
            filesAtSite.remove(candidates.get(fileIndex));
        }
        return busyTill;
    }
}
