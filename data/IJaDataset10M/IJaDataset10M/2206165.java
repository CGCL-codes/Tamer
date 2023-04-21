package feup.aiad.agents.macroscopic.globalsync;

import java.awt.Label;
import trasmapi.genAPI.Lane;
import trasmapi.sumo.SumoLane;

public class Adjacent {

    Lane lane_out[];

    String idTL;

    public Adjacent(String ln_id, String tl_id) {
        lane_out = new Lane[3];
        lane_out[0] = new SumoLane(ln_id + "_0");
        lane_out[1] = new SumoLane(ln_id + "_1");
        lane_out[2] = new SumoLane(ln_id + "_2");
        idTL = tl_id;
    }
}
