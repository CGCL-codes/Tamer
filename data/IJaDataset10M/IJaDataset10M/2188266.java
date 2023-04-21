package net.sf.brightside.golfmaster.service;

import java.util.Collection;
import net.sf.brightside.golfmaster.beans.Golfer;

public interface Golfers {

    Golfer save(Golfer golfEvent);

    Collection<Golfer> retrieve();

    Golfer authenticate(String userName, String password);
}
