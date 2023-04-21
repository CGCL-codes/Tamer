package com.yawnefpark.scorekeeper.service.iface;

import java.util.List;
import com.yawnefpark.scorekeeper.domain.AtBat;

public interface AtBatServiceIface {

    public abstract AtBat getAtBatById(long atBatId);

    public abstract AtBat insertAtBat(AtBat atBat);

    public abstract boolean updateAtBat(AtBat atBat);

    public abstract boolean deleteAtBat(AtBat atBat);

    public abstract List<AtBat> getAllAtBatsForGameId(long gameId);
}
