package hoplugins.feedback.dao;

import hoplugins.Commons;
import hoplugins.feedback.model.transfer.PlayerTransfer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import plugins.ISpieler;

/**
 * DAO to retrieve transfers in the HO database.
 *
 * @author <a href=mailto:nethyperon@users.sourceforge.net>Boy van der Werf</a>
 * @author flattermann <flattermannHO@gmail.com>
 */
public final class TransfersDAO {

    /** Name of the table in the HO database */
    private static final String TABLE_NAME = "transfers_transfers";

    /**
     * Private default constuctor to prevent class instantiation.
     */
    private TransfersDAO() {
    }

    /**
     * Gets a list of all transfers for you team
     * Consider transfers after 'startingDate' and in the last 'maxAge' weeks, only
     *
     * @param startDate		starting timestamp
     * @param maxAge	max age in weeks
     * @return 			list of all transfers
     */
    public static List<PlayerTransfer> getAllTransfers(Timestamp startDate, int maxAge) {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.WEEK_OF_YEAR, -maxAge);
        Timestamp maxAgeTs = new Timestamp(cal.getTimeInMillis());
        final StringBuffer sqlStmt = new StringBuffer("SELECT * FROM " + TABLE_NAME);
        sqlStmt.append(" WHERE date>'" + startDate.toString() + "' AND date>'" + maxAgeTs.toString() + "'");
        sqlStmt.append(" ORDER BY date DESC");
        return loadTransfers(sqlStmt.toString());
    }

    /**
     * Loads a list of transfers from the HO database.
     *
     * @param sqlStmt SQL statement.
     *
     * @return List of transfers
     */
    private static List<PlayerTransfer> loadTransfers(String sqlStmt) {
        final double curr_rate = Commons.getModel().getXtraDaten().getCurrencyRate();
        final List<PlayerTransfer> results = new Vector<PlayerTransfer>();
        final ResultSet rs = Commons.getModel().getAdapter().executeQuery(sqlStmt.toString());
        if (rs == null) {
            return new Vector<PlayerTransfer>();
        }
        try {
            while (rs.next()) {
                PlayerTransfer transfer = new PlayerTransfer(rs.getInt("transferid"), rs.getInt("playerid"));
                transfer.setPlayerName(Commons.getModel().getHelper().decodeStringFromDatabase(rs.getString("playername")));
                transfer.setDate(rs.getTimestamp("date"));
                transfer.setWeek(rs.getInt("week"));
                transfer.setSeason(rs.getInt("season"));
                transfer.setBuyerid(rs.getInt("buyerid"));
                transfer.setBuyerName(Commons.getModel().getHelper().decodeStringFromDatabase(rs.getString("buyername")));
                transfer.setSellerid(rs.getInt("sellerid"));
                transfer.setSellerName(Commons.getModel().getHelper().decodeStringFromDatabase(rs.getString("sellername")));
                transfer.setPrice((int) (rs.getInt("price") / curr_rate));
                transfer.setMarketvalue((int) (rs.getInt("marketvalue") / curr_rate));
                transfer.setTsi(rs.getInt("tsi"));
                results.add(transfer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (Iterator<PlayerTransfer> iter = results.iterator(); iter.hasNext(); ) {
            PlayerTransfer transfer = iter.next();
            final ISpieler spieler = Commons.getModel().getSpielerAtDate(transfer.getPlayerId(), transfer.getDate());
            if (spieler != null) {
                Integer transferWeek = Commons.getModel().getHelper().getHTWeek(transfer.getDate());
                Integer transferSeason = Commons.getModel().getHelper().getHTSeason(transfer.getDate());
                Integer spielerWeek = Commons.getModel().getHelper().getHTWeek(spieler.getHrfDate());
                Integer spielerSeason = Commons.getModel().getHelper().getHTSeason(spieler.getHrfDate());
                if (((transferSeason * 16) + transferWeek) == ((spielerSeason * 16) + spielerWeek)) {
                    transfer.setPlayerInfo(spieler);
                }
            }
        }
        return results;
    }
}
