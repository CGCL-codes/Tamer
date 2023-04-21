package de.volkerraum.pokerbot.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import de.volkerraum.pokerbot.tournamentlog.model.GameLog;

public class PotHandler {

    Random rand = new Random(System.currentTimeMillis());

    HashMap<Integer, Long> playersBets = new HashMap<Integer, Long>();

    long sum = 0;

    public void playerDidBet(int playerIndex, long money) {
        Long playersBet = playersBets.get(playerIndex);
        if (playersBet == null) playersBets.put(playerIndex, money); else playersBets.put(playerIndex, money + playersBet);
        sum += money;
    }

    public long getAllBets() {
        return sum;
    }

    public long getMaximumBet() {
        long max = 0;
        for (Long currBet : playersBets.values()) {
            if (currBet > max) max = currBet;
        }
        return max;
    }

    public long getBetOfPlayer(int playerIndex) {
        long bet = 0;
        Long playersBet = playersBets.get(playerIndex);
        if (playersBet != null) bet = playersBet;
        return bet;
    }

    /**
    * 
    * @param cardValuePerPlayer
    *           all values for all players who are alive and didn't fold
    * @return
    */
    public Map<Integer, Long> distributeMoney(Map<Integer, Long> cardValuePerPlayer, Set<Integer> winners, GameLog gameLog) {
        HashMap<Integer, Long> moneyPerPlayer = new HashMap<Integer, Long>();
        List<Long> sortedPlayersBets = new ArrayList<Long>();
        for (Long currPlayersBet : playersBets.values()) {
            if (!sortedPlayersBets.contains(currPlayersBet)) sortedPlayersBets.add(currPlayersBet);
        }
        Collections.sort(sortedPlayersBets);
        List<Pot> pots = new ArrayList<Pot>();
        long potsum = 0;
        for (int i = 0; i < sortedPlayersBets.size(); ++i) {
            long newPotValue = sortedPlayersBets.get(i) - potsum;
            potsum += newPotValue;
            Pot newPot = new Pot(newPotValue);
            pots.add(newPot);
        }
        for (Integer currPlayer : playersBets.keySet()) {
            long playersBet = playersBets.get(currPlayer);
            int potCount = 0;
            while (playersBet > 0) {
                pots.get(potCount).addPlayerIndex(currPlayer);
                playersBet -= pots.get(potCount).getMoneyPerPlayer();
                potCount++;
            }
        }
        for (Pot currPot : pots) {
            List<Integer> winnersInThisPot = new ArrayList<Integer>();
            long max = 0;
            for (Integer playerInPot : currPot.playersInPot) {
                Long playersCardValue = cardValuePerPlayer.get(playerInPot);
                if (playersCardValue != null) {
                    if (playersCardValue > max) max = playersCardValue;
                }
            }
            for (Integer playerInPot : currPot.playersInPot) {
                Long playersCardValue = cardValuePerPlayer.get(playerInPot);
                if (playersCardValue != null && playersCardValue == max) {
                    winnersInThisPot.add(playerInPot);
                    winners.add(playerInPot);
                }
            }
            if (gameLog != null) gameLog.addPot(currPot, winnersInThisPot);
            if (winnersInThisPot.size() == 0) {
                for (Integer currIndex : currPot.getPlayersInPot()) {
                    addMoneyForPlayer(moneyPerPlayer, currPot.getMoneyPerPlayer(), currIndex);
                }
            } else {
                long moneyForEachPlayer = currPot.getTotalSumInPot() / winnersInThisPot.size();
                long fraction = currPot.getTotalSumInPot() % winnersInThisPot.size();
                for (Integer winnerId : winnersInThisPot) {
                    addMoneyForPlayer(moneyPerPlayer, moneyForEachPlayer, winnerId);
                }
                if (winnersInThisPot.size() > 1) {
                    int fractionreceiverIndex = rand.nextInt(winnersInThisPot.size());
                    addMoneyForPlayer(moneyPerPlayer, fraction, fractionreceiverIndex);
                }
            }
        }
        return moneyPerPlayer;
    }

    private void addMoneyForPlayer(HashMap<Integer, Long> playersMoney, long money, int playerIndex) {
        Long sum = playersMoney.get(playerIndex);
        if (sum == null) sum = 0L;
        sum += money;
        playersMoney.put(playerIndex, sum);
    }
}
