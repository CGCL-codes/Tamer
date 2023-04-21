package com.fh.auge.core;

import java.util.List;

public interface MarketDao {

    public void add(Market market);

    public void delete(Market market);

    public List<Market> getMarkets();

    public void update(Market market);

    public Market findById(String id);
}
