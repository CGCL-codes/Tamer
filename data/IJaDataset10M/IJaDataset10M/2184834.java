package org.nomicron.suber.model.test;

import org.nomicron.suber.enums.VoteCode;
import org.nomicron.suber.model.factory.MetaFactory;
import org.nomicron.suber.model.factory.VoteFactory;
import org.nomicron.suber.model.object.BallotItem;
import org.nomicron.suber.model.object.Element;
import org.nomicron.suber.model.object.Player;
import org.nomicron.suber.model.object.Turn;
import org.nomicron.suber.model.object.Vote;
import java.util.List;

/**
 * Tests for the Vote.
 */
public class VoteTest extends SuberTest {

    /**
     * Contstruct the test.
     *
     * @param name test name
     */
    public VoteTest(String name) {
        super(name);
    }

    public void testObject() throws Exception {
        VoteCode voteCode = VoteCode.FOR;
        Turn turn = new Turn();
        turn.save();
        Element element = new Element();
        element.save();
        Player player = new Player();
        player.save();
        BallotItem ballotItem = new BallotItem();
        ballotItem.save();
        Vote vote = new Vote();
        vote.setPlayer(player);
        vote.setVoteCode(voteCode);
        vote.setTurn(turn);
        vote.setBallotItem(ballotItem);
        vote.addElement(element);
        vote.save();
        assertEquals(player, vote.getPlayer());
        assertEquals(voteCode, vote.getVoteCode());
        assertEquals(turn, vote.getTurn());
        assertEquals(ballotItem, vote.getBallotItem());
        assertTrue(vote.getElementList().contains(element));
    }

    public void testVotesByPlayerAndTurn() throws Exception {
        VoteFactory voteFactory = MetaFactory.getInstance().getVoteFactory();
        Turn turn = new Turn();
        turn.save();
        Turn turn2 = new Turn();
        turn2.save();
        Player player = new Player();
        player.save();
        Player player2 = new Player();
        player2.save();
        Vote vote1 = new Vote();
        vote1.setPlayer(player);
        vote1.setTurn(turn);
        vote1.save();
        Vote vote2 = new Vote();
        vote2.save();
        Vote vote3 = new Vote();
        vote3.setPlayer(player);
        vote3.save();
        Vote vote4 = new Vote();
        vote4.setTurn(turn);
        vote4.save();
        Vote vote5 = new Vote();
        vote5.setPlayer(player2);
        vote5.setTurn(turn);
        vote5.save();
        Vote vote6 = new Vote();
        vote6.setPlayer(player);
        vote6.setTurn(turn2);
        vote6.save();
        resetSession();
        List voteList = voteFactory.getVotesByPlayerAndTurn(player, turn);
        assertEquals(1, voteList.size());
        assertTrue(voteList.contains(vote1));
        assertFalse(voteList.contains(vote2));
        assertFalse(voteList.contains(vote3));
        assertFalse(voteList.contains(vote4));
        assertFalse(voteList.contains(vote5));
        assertFalse(voteList.contains(vote6));
    }
}
