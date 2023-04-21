package bluffinmuffin.protocol;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author Hocus
 *         This class represents a network table.
 */
public class TuplePlayerInfo {

    public int m_noSeat;

    public boolean m_isEmpty;

    public String m_playerName;

    public int m_money;

    public final ArrayList<Integer> m_holeCardIDs;

    public boolean m_isDealer;

    public boolean m_isSmallBlind;

    public boolean m_isBigBlind;

    public boolean m_isCurrentPlayer;

    public int m_timeRemaining;

    public int m_bet;

    public boolean m_isPlaying;

    /**
     * Create a new table
     * 
     * @param p_noPort
     *            Port number of the table
     * @param p_tableName
     *            Table name
     * @param p_gameType
     *            Game type
     * @param p_bigBlind
     *            Number of chips of the big blind
     * @param p_nbPlayers
     *            Number of players
     * @param p_nbSeats
     *            Number of seat
     */
    public TuplePlayerInfo(int noSeat, boolean isEmpty, String playerName, int money, ArrayList<Integer> hole, boolean isDealer, boolean isSmallBlind, boolean isBigBlind, boolean isCurrentPlayer, int timeRemaining, int bet, boolean isPlaying) {
        m_noSeat = noSeat;
        m_isEmpty = isEmpty;
        m_playerName = playerName;
        m_money = money;
        m_holeCardIDs = hole;
        m_isDealer = isDealer;
        m_isSmallBlind = isSmallBlind;
        m_isBigBlind = isBigBlind;
        m_isCurrentPlayer = isCurrentPlayer;
        m_timeRemaining = timeRemaining;
        m_bet = bet;
        m_isPlaying = isPlaying;
    }

    public TuplePlayerInfo(int noSeat) {
        m_holeCardIDs = new ArrayList<Integer>();
        m_noSeat = noSeat;
        m_isEmpty = true;
    }

    public TuplePlayerInfo(StringTokenizer argsToken) {
        m_holeCardIDs = new ArrayList<Integer>();
        m_noSeat = Integer.parseInt(argsToken.nextToken());
        m_isEmpty = Boolean.parseBoolean(argsToken.nextToken());
        if (!m_isEmpty) {
            m_playerName = argsToken.nextToken();
            m_money = Integer.parseInt(argsToken.nextToken());
            m_holeCardIDs.add(Integer.parseInt(argsToken.nextToken()));
            m_holeCardIDs.add(Integer.parseInt(argsToken.nextToken()));
            m_isDealer = Boolean.parseBoolean(argsToken.nextToken());
            m_isSmallBlind = Boolean.parseBoolean(argsToken.nextToken());
            m_isBigBlind = Boolean.parseBoolean(argsToken.nextToken());
            m_isCurrentPlayer = Boolean.parseBoolean(argsToken.nextToken());
            m_timeRemaining = Integer.parseInt(argsToken.nextToken());
            m_bet = Integer.parseInt(argsToken.nextToken());
            m_isPlaying = Boolean.parseBoolean(argsToken.nextToken());
        }
    }

    /**
     * Return a string representing the table.
     * This string is used to be sent through the network
     * 
     * @param p_delimiter
     *            Delimiter of the fields
     * @return
     *         A string representing the table
     */
    public String toString(String p_delimiter) {
        final StringBuilder sb = new StringBuilder();
        sb.append(m_noSeat);
        sb.append(p_delimiter);
        sb.append(m_isEmpty);
        sb.append(p_delimiter);
        if (!m_isEmpty) {
            sb.append(m_playerName);
            sb.append(p_delimiter);
            sb.append(m_money);
            sb.append(p_delimiter);
            sb.append(m_holeCardIDs.get(0));
            sb.append(p_delimiter);
            sb.append(m_holeCardIDs.get(1));
            sb.append(p_delimiter);
            sb.append(m_isDealer);
            sb.append(p_delimiter);
            sb.append(m_isSmallBlind);
            sb.append(p_delimiter);
            sb.append(m_isBigBlind);
            sb.append(p_delimiter);
            sb.append(m_isCurrentPlayer);
            sb.append(p_delimiter);
            sb.append(m_timeRemaining);
            sb.append(p_delimiter);
            sb.append(m_bet);
            sb.append(p_delimiter);
            sb.append(m_isPlaying);
            sb.append(p_delimiter);
        }
        return sb.toString();
    }
}
