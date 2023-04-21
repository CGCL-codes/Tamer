package org.twdata.kokua.tw.model;

import java.util.Collection;
import java.util.Date;

public interface Owner {

    public boolean isCorporate();

    public boolean isFriendly(Player p);
}
