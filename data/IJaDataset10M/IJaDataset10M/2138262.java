package eu.billewicz.notiary.dao.remote;

import java.util.List;
import eu.billewicz.notiary.model.Auction;

public interface FinansowoAuctionDao {

    Auction getAuction(Integer auctionId);

    List<Auction> getNewAuctions();

    List<Auction> getArchiveAuctions();

    List<Auction> getOverdueAuctions();
}
