package com.qspin.qtaste.kernel.campaign;

import java.util.ArrayList;

/**
 * A campaign is composed of a name and a set of Campaign runs
 * @author lvboque
 */
public class Campaign {

    String name;

    ArrayList<CampaignRun> runs;

    public Campaign() {
        runs = new ArrayList<CampaignRun>();
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<CampaignRun> getRuns() {
        return runs;
    }
}
