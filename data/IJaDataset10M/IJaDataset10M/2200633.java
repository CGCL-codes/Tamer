package eu.billewicz.notiary.dao.remote.stub;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import eu.billewicz.notiary.dao.db.AuctionStubDao;
import eu.billewicz.notiary.dao.db.UniversalDao;
import eu.billewicz.notiary.dao.remote.FinansowoAuctionDao;
import eu.billewicz.notiary.dao.remote.FinansowoPersonDao;
import eu.billewicz.notiary.model.Auction;
import eu.billewicz.notiary.model.AuctionStub;

public class FinansowoAuctionDaoStub implements FinansowoAuctionDao {

    private Log log = LogFactory.getLog(getClass());

    private UniversalDao universalDao;

    private FinansowoPersonDao finansowoPersonDao;

    private AuctionStubDao auctionStubDao;

    public Auction getAuction(Integer auctionId) {
        log.info("Getting auction: " + auctionId);
        AuctionStub auctionStub = (AuctionStub) universalDao.getById(auctionId, AuctionStub.class);
        Auction auction = convertStubToAuction(auctionStub);
        return auction;
    }

    public List<Auction> getNewAuctions() {
        log.info("START: Getting new auctions");
        List<AuctionStub> auctionStubs = auctionStubDao.getAll();
        List<Auction> auctions = new ArrayList<Auction>();
        for (AuctionStub auctionStub : auctionStubs) {
            auctions.add(convertStubToAuction(auctionStub));
        }
        log.info("END: Getting new auctions");
        return auctions;
    }

    @Override
    public List<Auction> getArchiveAuctions() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public List<Auction> getOverdueAuctions() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    private Auction convertStubToAuction(AuctionStub auctionStub) {
        Auction auction = new Auction();
        auction.setId(auctionStub.getId());
        auction.setComment(auctionStub.getComment());
        auction.setAmount(auctionStub.getAmount());
        auction.setInterest(auctionStub.getInterest());
        auction.setTerm(auctionStub.getTerm());
        auction.setBorrower(finansowoPersonDao.getPerson(auctionStub.getBorrower().getId()));
        auction.setClearForBid(auctionStub.isClearForBid());
        return auction;
    }

    public UniversalDao getUniversalDao() {
        return universalDao;
    }

    public void setUniversalDao(UniversalDao universalDao) {
        this.universalDao = universalDao;
    }

    public FinansowoPersonDao getFinansowoPersonDao() {
        return finansowoPersonDao;
    }

    public void setFinansowoPersonDao(FinansowoPersonDao finansowoPersonDao) {
        this.finansowoPersonDao = finansowoPersonDao;
    }

    public AuctionStubDao getAuctionStubDao() {
        return auctionStubDao;
    }

    public void setAuctionStubDao(AuctionStubDao auctionStubDao) {
        this.auctionStubDao = auctionStubDao;
    }
}
