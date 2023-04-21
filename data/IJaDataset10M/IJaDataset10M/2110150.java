package poker.plans.croupier;

import java.util.List;
import poker.beliefs.*;
import jadex.adapter.fipa.AgentIdentifier;
import jadex.adapter.fipa.SFipa;
import jadex.runtime.IGoal;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

/**
 *  Plan to deal cards to players
 *  No reply from the player is necessary.
 */
public class DealCardsPlan extends Plan {

    public void body() {
        System.out.println("CROUPIER - PREFLOP setup");
        Table t = (Table) getBeliefbase().getBelief("table").getFact();
        AgentIdentifier[] players = t.getPlayers();
        Pack p = (Pack) getBeliefbase().getBelief("pack").getFact();
        if (t.getNumHand() == 0) p.newPack();
        p.shuffle();
        IMessageEvent msg;
        int incr = t.getNumHand() * 5;
        t.setBigBlind(t.getBigBlind() + incr);
        t.setSmallBlind(t.getSmallBlind() + incr);
        AgentIdentifier small = (AgentIdentifier) players[(t.getNumHand()) % players.length];
        AgentIdentifier big = (AgentIdentifier) players[(t.getNumHand() + 1) % players.length];
        System.out.println("CROUPIER: The Big Blind is " + big.getName() + " and the Small blind is " + small.getName());
        AssignBlind sblind = new AssignBlind();
        AssignBlind bblind = new AssignBlind();
        sblind.setAmount(t.getSmallBlind());
        bblind.setAmount(t.getBigBlind());
        msg = createMessageEvent("assign_blind");
        msg.setContent(sblind);
        msg.getParameterSet(SFipa.RECEIVERS).addValue(small);
        sendMessage(msg);
        msg = createMessageEvent("assign_blind");
        msg.setContent(bblind);
        msg.getParameterSet(SFipa.RECEIVERS).addValue(big);
        sendMessage(msg);
        System.out.println("CROUPIER: Blinds assigned to players");
        Card card;
        DealCard[] dc = new DealCard[players.length];
        System.out.println("CROUPIER: Begin dealing cards");
        for (int i = 0; i < players.length; i++) {
            card = p.dealCard();
            dc[i] = new DealCard();
            dc[i].addCard(card);
            System.out.println("CROUPIER: Player " + players[i].getName() + " Card: " + card.toString());
        }
        for (int i = 0; i < players.length; i++) {
            card = p.dealCard();
            dc[i].addCard(card);
            System.out.println("CROUPIER: Player " + players[i].getName() + " Card: " + card.toString());
            msg = createMessageEvent("deal_card");
            msg.setContent(dc[i]);
            msg.getParameterSet(SFipa.RECEIVERS).addValue(players[i]);
            sendMessage(msg);
        }
        getBeliefbase().getBelief("table").setFact(t);
        getBeliefbase().getBelief("pack").setFact(p);
        IGoal goal = createGoal("playhand");
        getGoalbase().dispatchTopLevelGoal(goal);
    }
}
