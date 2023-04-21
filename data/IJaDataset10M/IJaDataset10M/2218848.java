package net.sf.brightside.stockswatcher.server.service.api.hibernate;

import java.util.List;
import net.sf.brightside.stockswatcher.server.metamodel.Share;

public interface ITickerUpdater {

    List<Share> update();
}
