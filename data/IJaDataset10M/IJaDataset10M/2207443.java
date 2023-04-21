package playground.mrieser;

import org.apache.log4j.Logger;

/**
 * @author mrieser
 */
public class MyRuns {

    private static final Logger log = Logger.getLogger(MyRuns.class);

    public static void main(final String[] args) {
        log.info("start");
        String networkFile = "/Volumes/Data/vis/ch25pct_kti/network.c.xml.gz";
        String inPlansFile = "/Volumes/Data/talks/20120322_UsrMtg_Via/data_basic/0.plans.xml.gz";
        String outPlansFile = "/Volumes/Data/talks/20120322_UsrMtg_Via/data_basic/0.plans.selected.xml.gz";
        int size = 11;
        int nOfParts = 3;
        for (int i = 0; i < nOfParts; i++) {
            System.out.println(size * (i + 1) / nOfParts);
        }
    }
}
