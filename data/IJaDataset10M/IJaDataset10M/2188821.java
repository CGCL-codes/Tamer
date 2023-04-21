package com.wikiup.romulan.gom.imp.producer;

import com.wikiup.romulan.gom.Model;
import com.wikiup.romulan.gom.Player;
import com.wikiup.romulan.gom.imp.rc.ExponentRankResourceContainer;
import com.wikiup.romulan.gom.inf.NamedModelInf;
import com.wikiup.romulan.gom.inf.ProducerInf;

public class DynamicProducer extends ExponentRankResourceContainer implements ProducerInf, NamedModelInf {

    public DynamicProducer(Player player, Model model) {
        super(player, model);
    }

    @Override
    protected boolean multiplyRank() {
        return true;
    }

    public String getName() {
        return "producer";
    }
}
