package com.sjakkforum;

import java.util.ArrayList;
import java.util.List;
import com.sjakkforum.xml.TournamentXMLTransformer;
import junit.framework.TestCase;

public class TournamentEntryTest extends TestCase {

    public void testTournamentEnties() {
        TournamentXMLTransformer ttf = new TournamentXMLTransformer();
        List<Player> players = ttf.readPlayers("Tournament_players.xml");
        List<TournamentEntry> entries = new ArrayList<TournamentEntry>();
        List<TournamentEntry> entries2 = new ArrayList<TournamentEntry>();
        int i = 1;
        for (Player p : players) {
            TournamentEntry e = new TournamentEntry();
            e.setPlayer(p);
            e.setStartnumber(i);
            entries.add(e);
            i++;
        }
        i = 1;
        for (Player p : players) {
            TournamentEntry e = new TournamentEntry();
            e.setPlayer(p);
            e.setStartnumber(i);
            entries2.add(e);
            i++;
        }
        for (Player p : players) {
            assertTrue(getEntryForPlayer(entries, p).equals(getEntryForPlayer(entries, p)));
            assertTrue(getEntryForPlayer(entries2, p).equals(getEntryForPlayer(entries, p)));
            assertTrue(getEntryForPlayer(entries, p).equals(getEntryForPlayer(entries2, p)));
            assertTrue(getEntryForPlayer(entries, p).hashCode() == getEntryForPlayer(entries, p).hashCode());
            assertTrue(getEntryForPlayer(entries, p).hashCode() == getEntryForPlayer(entries2, p).hashCode());
        }
        Round round1 = new Round();
        round1.setRoundNumber(1);
        Game game1_1 = new Game();
        Game game1_2 = new Game();
        game1_1.setWhite(entries.get(0));
        game1_1.setBlack(entries.get(1));
        game1_2.setWhite(entries.get(2));
        game1_2.setBlack(entries.get(3));
        assertTrue(game1_1.getRoundNumber() == 0);
        assertTrue(game1_2.getRoundNumber() == 0);
        round1.addGame(game1_1);
        round1.addGame(game1_2);
        assertTrue(game1_1.getRoundNumber() == 1);
        assertTrue(game1_2.getRoundNumber() == 1);
        entries.get(0).addGame(game1_1);
        entries.get(1).addGame(game1_1);
        entries.get(2).addGame(game1_2);
        entries.get(3).addGame(game1_2);
        assertTrue(entries.get(0).getScore(new DefaultScoreComputer()) == 0.0);
        assertTrue(entries.get(1).getScore(new DefaultScoreComputer()) == 0.0);
        assertTrue(entries.get(2).getScore(new DefaultScoreComputer()) == 0.0);
        assertTrue(entries.get(3).getScore(new DefaultScoreComputer()) == 0.0);
        assertTrue(entries.get(0).getNumBlack() == 0);
        assertTrue(entries.get(1).getNumBlack() == 1);
        assertTrue(entries.get(2).getNumBlack() == 0);
        assertTrue(entries.get(3).getNumBlack() == 1);
        assertTrue(entries.get(0).getNumWhite() == 1);
        assertTrue(entries.get(1).getNumWhite() == 0);
        assertTrue(entries.get(2).getNumWhite() == 1);
        assertTrue(entries.get(3).getNumWhite() == 0);
    }

    private TournamentEntry getEntryForPlayer(List<TournamentEntry> entries, Player p) {
        TournamentEntry entry = null;
        for (TournamentEntry e : entries) {
            if (e.getPlayer().equals(p)) {
                entry = e;
                break;
            }
        }
        return entry;
    }
}
