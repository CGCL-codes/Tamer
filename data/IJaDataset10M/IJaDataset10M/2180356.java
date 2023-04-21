package hypercast.DT;

import hypercast.I_AddressPair;
import hypercast.NeighborhoodStats;
import hypercast.StatsElement;
import hypercast.StatsProcessor;

/**
 * The stats table for DT neighborhood table
 * 
 * @author HyperCast Team
 * @author Guangyu Dong
 * @version 2005 (version 3.0)
 */
class DT_NeighborhoodStats extends NeighborhoodStats {

    /**
	 * I_Stats instance which maintains statistics CW in this entry.
	 */
    NeighborhoodStats cw;

    /**
	 * I_Stats instance which maintains statistics CW in this entry.
	 */
    NeighborhoodStats ccw;

    /**
	 * Constructor.
	 */
    public DT_NeighborhoodStats(I_AddressPair ap, I_AddressPair cw_ap, I_AddressPair ccw_ap) {
        super(ap, "Neighbor");
        cw = new NeighborhoodStats(cw_ap);
        ccw = new NeighborhoodStats(ccw_ap);
        this.InitMyStatisticsStructure();
    }

    public void set(NeighborhoodStats entry) {
        super.set(entry);
        DT_NeighborhoodStats dt_entry = (DT_NeighborhoodStats) entry;
        cw.addressPair = dt_entry.cw.addressPair;
        ccw.addressPair = dt_entry.ccw.addressPair;
    }

    /**
	 * Initialize the statistics processor instance contained in this object.
	 */
    protected void InitMyStatisticsStructure() {
        statsPro.addStatsElement("CW", cw, 1, 1);
        statsPro.addStatsElement("CCW", ccw, 1, 1);
    }
}
