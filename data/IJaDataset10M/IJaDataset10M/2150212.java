package nl.gridshore.samples.raffle.dao.jpa;

import nl.gridshore.samples.raffle.dao.WinnerDao;
import nl.gridshore.samples.raffle.domain.Winner;

/**
 * Created by IntelliJ IDEA.
 * User: jettro
 * Date: Jan 12, 2008
 * Time: 9:02:42 AM
 * Test class for winner data access
 */
public class WinnerDaoJpaTest extends BaseTestDaoJpa {

    private WinnerDao winnerDao;

    private static final long WINNER_ID = 1L;

    public void setWinnerDao(WinnerDao winnerDao) {
        this.winnerDao = winnerDao;
    }

    public void testDeleteWinner() {
        int countBefore = winnerDao.loadAll().size();
        Winner winner = winnerDao.loadById(WINNER_ID);
        winnerDao.delete(winner);
    }
}
