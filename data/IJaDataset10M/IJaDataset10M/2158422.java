package com.ee.bruscar.generator.model.mock.service.model.game;

import java.io.Serializable;
import java.util.ArrayList;

public class Campaigns implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6047842847446079088L;

    private ArrayList<Campaign> campaigns;

    private int layout;

    /**
	 * java type: ArrayList<Campaign>
	 */
    public ArrayList<Campaign> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(ArrayList<Campaign> campaigns) {
        this.campaigns = campaigns;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }
}
