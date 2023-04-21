package ac.at.tuwien.swa04.ebay.notification;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ac.at.tuwien.swa04.ebay.test.util.TestHelper;
import at.ac.tuwien.swa04.ebay.dao.AuctionDAO;
import at.ac.tuwien.swa04.ebay.dao.UserDAO;
import at.ac.tuwien.swa04.ebay.model.Auction;
import at.ac.tuwien.swa04.ebay.model.User;
import at.ac.tuwien.swa04.ebay.notification.NotificationRemote;
import at.ac.tuwien.swa04.ebay.notification.dto.MessageDTO;

@SuppressWarnings("unused")
public class NotificationTest {

    private UserDAO userDAO;

    private NotificationRemote notification;

    @Before
    public void setUp() throws Exception {
        this.userDAO = (UserDAO) TestHelper.getJNDIContext().lookup("swa04_gen_ebay-ear/UserDAOImpl/remote");
        this.notification = (NotificationRemote) TestHelper.getJNDIContext().lookup("swa04_ebay-ear/NotificationImpl/remote");
        TestHelper.createTestData();
    }

    @After
    public void tearDown() throws Exception {
        TestHelper.deleteTestData();
    }

    @Test
    public void notifySellerOnBid() {
        User seller = userDAO.findUser("martin");
        User buyer = userDAO.findUser("robert");
        notification.notifiyBid(seller.getId(), buyer.getId(), new Long(0), (double) 200);
    }

    @Test
    public void notifyBuyerWhenOverbidden() {
        User seller = userDAO.findUser("martin");
        User buyer = userDAO.findUser("robert");
        notification.notifyOverbidden(seller.getId(), buyer.getId(), new Long(0), (double) 199, (double) 200);
    }

    @Test
    public void notifyBuyerWon() {
        User seller = userDAO.findUser("martin");
        User buyer = userDAO.findUser("robert");
        notification.notifyAuctionWon(seller.getId(), buyer.getId(), new Long(0), (double) 200);
    }
}
