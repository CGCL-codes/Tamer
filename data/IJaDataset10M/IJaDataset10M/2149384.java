package org.gs.game.gostop.play;

import java.util.List;
import org.gs.game.gostop.*;
import org.gs.game.gostop.action.AutoPlayAction;
import org.gs.game.gostop.action.GameAction;
import org.gs.game.gostop.action.ShowGoAction;
import org.gs.game.gostop.config.BonusCards;
import org.gs.game.gostop.event.GameEvent;
import org.gs.game.gostop.event.GameEventManager;
import org.gs.game.gostop.event.GameEventResult;
import org.gs.game.gostop.event.GameEventType;
import org.gs.game.gostop.item.CardItem;
import org.gs.game.gostop.play.GameRule.GoStopRule;

public class AutoPlayHandler implements IPlayHandler {

    private GamePlayer gamePlayer;

    private GameTable gameTable;

    protected AutoPlayHandler(GamePlayer gamePlayer, GameTable gameTable) {
        this.gamePlayer = gamePlayer;
        this.gameTable = gameTable;
    }

    public void pickCard() {
        TableCardPoint tcp;
        GameEvent event;
        CardItem cardItem = pickTakeableCard();
        tcp = gameTable.getTableCardPoint(cardItem.getMajorCode(), false);
        if (tcp == null && cardItem.isBonusCard() == false && gamePlayer.getHoldCardCount(cardItem.getMajorCode()) >= 3) {
            event = new GameEvent(gamePlayer, GameEventType.SWING_DECIDED, new GameEventResult(true, cardItem));
        } else if (tcp != null && tcp.needToQueryForTaking()) {
            int index = selectTableCard(tcp);
            event = new GameEvent(gamePlayer, GameEventType.SELECTED_CARD_ON_TABLE, new GameEventResult(cardItem, index));
        } else event = new GameEvent(cardItem, GameEventType.ITEM_CLICKED);
        GameEventManager.fireGameEvent(event, false);
    }

    public void onPostActive() {
        gamePlayer.arrangeHoldCards();
    }

    public GameAction getSelectTableCardAction(TableCardPoint flipTcp) {
        return new AutoPlayAction(this, flipTcp);
    }

    public int selectTableCard(TableCardPoint flipTcp) {
        CardItem ci0 = flipTcp.getCardItems().get(0);
        CardItem ci1 = flipTcp.getCardItems().get(1);
        return getCardPriority(ci0, false) > getCardPriority(ci1, false) ? 0 : 1;
    }

    public void decideGo() {
        boolean canGo = checkSwampedCards() && checkOtherPoints();
        if (canGo) {
            ShowGoAction sga = new ShowGoAction(gamePlayer, 20, GameEventType.GO_DECIDED, false);
            gamePlayer.getGamePanel().getActionManager().addItem(sga);
        } else {
            GameEvent event = new GameEvent(gamePlayer, GameEventType.GO_DECIDED, new GameEventResult(false));
            GameEventManager.fireGameEvent(event, false);
        }
    }

    public void decideNine() {
        GameEvent event = new GameEvent(gamePlayer, GameEventType.NINE_DECIDED, new GameEventResult(true));
        GameEventManager.fireGameEvent(event, false);
    }

    public void decideGoOnFourCards() {
        ShowGoAction sga = new ShowGoAction(gamePlayer, 20, GameEventType.FOUR_CARDS_DECIDED, true);
        gamePlayer.getGamePanel().getActionManager().addItem(sga);
    }

    private CardItem pickTakeableCard() {
        CardItem cardItem = null;
        List<CardItem> holdCards = gamePlayer.getHoldCards();
        int topPriority = Integer.MIN_VALUE;
        if (holdCards.size() > 0) {
            PlayerStatus playerStatus = gamePlayer.getPlayerStatus();
            CardItem bonusCard = null;
            for (CardItem ci : holdCards) {
                if (ci.isBonusCard()) {
                    if (bonusCard == null || ci.getCardClass() != CardClass.LEAF) bonusCard = ci;
                } else {
                    int priority = getCardPriority(ci, true);
                    if (topPriority < priority) {
                        topPriority = priority;
                        cardItem = ci;
                    }
                }
            }
            if (bonusCard != null && (bonusCard.getCardClass() != CardClass.LEAF || playerStatus.getTakenCards(CardClass.LEAF).size() > 0)) cardItem = bonusCard; else if (topPriority < 0) {
                if (bonusCard != null) cardItem = bonusCard; else if (gamePlayer.getFlipCount() > 0) cardItem = gameTable.getTopDeckCard(false);
            }
        }
        if (cardItem == null) cardItem = gameTable.getTopDeckCard(false);
        return cardItem;
    }

    private int getCardPriority(CardItem cardItem, boolean checkTable) {
        int priority = 0;
        int goPriority = 0;
        TableCardPoint tcp = null;
        if (checkTable) tcp = gameTable.getTableCardPoint(cardItem.getMajorCode(), false);
        List<GamePlayer> goPlayers = gameTable.getGoPlayers();
        if (gameTable.getGameType().getPlayers() == 3 && goPlayers != null && goPlayers.size() == 1 && goPlayers.get(0) != gamePlayer) goPriority = getPriorityOnOtherGo(cardItem, tcp, checkTable, goPlayers.get(0));
        if (tcp != null && (tcp.getCardCount(true) == 3 || gamePlayer.getHoldCardCount(cardItem.getMajorCode()) == 3) && gameTable.canGetOtherLeaves(gamePlayer)) priority = 2000; else if (gameTable.isMissionAvailable() && (cardItem.isMissionCard() || (tcp != null && tcp.hasMissionCard()))) priority = 1000; else if (tcp != null && tcp.hasMultiLeaves()) priority = 900; else if ((cardItem.getCardClass() == CardClass.LEAF && GameRule.getLeafPoints(cardItem.getCardCode()) > 1) || cardItem.getCardClass() == CardClass.TEN_LEAF) priority = 650; else if (tcp != null && tcp.hasKingCard()) priority = 600; else if (cardItem.getCardClass() == CardClass.KING) priority = 550; else {
            GoStopRule rule;
            priority = cardItem.getCardClass() == CardClass.LEAF ? 150 : 100;
            if (tcp != null) {
                for (CardItem ci : tcp.getCardItems()) {
                    rule = GameRule.getGoStopRule(ci.getCardCode());
                    if (rule != null && gameTable.isRuleAvailable(rule)) {
                        int curPriority = rule.getRulePoint() * 100 + 40;
                        if (getTakenCount(rule) > 1) curPriority += rule.getRulePoint() * 100;
                        if (priority < curPriority) priority = curPriority;
                    }
                }
            } else if (gameTable.isCardTaken(cardItem.getMajorCode(), true) && gamePlayer.getHoldCardCount(cardItem.getMajorCode()) > 1) priority = 10;
            boolean missionAvail = gameTable.isMissionAvailable();
            int[] untakenCards;
            if (checkTable) untakenCards = gameTable.getUntakenCardCodes(cardItem.getCardCode()); else {
                untakenCards = new int[1];
                untakenCards[0] = cardItem.getCardCode();
            }
            for (int untaken : untakenCards) {
                CardClass cardClass = GameRule.getClardClass(CardItem.getCardIndex(untaken));
                if (cardClass == CardClass.KING) {
                    if (priority < 300) priority = gamePlayer.isHoldingCard(untaken) ? 340 : 300;
                } else if ((cardClass == CardClass.LEAF && GameRule.getLeafPoints(untaken) > 1) || cardClass == CardClass.TEN_LEAF) {
                    if (priority < 400) priority = gamePlayer.isHoldingCard(untaken) ? 440 : 400;
                } else {
                    rule = GameRule.getGoStopRule(untaken);
                    if (rule != null && gameTable.isRuleAvailable(rule)) {
                        int curPriority = rule.getRulePoint() * 100;
                        int takenCount = getTakenCount(rule);
                        if (takenCount > 1) curPriority += rule.getRulePoint() * 100;
                        if (untaken == cardItem.getCardCode()) curPriority += 40;
                        if (priority < curPriority) priority = curPriority;
                    }
                }
                if (missionAvail && gameTable.isMissionCard(untaken) && priority < 1000) priority = untaken == cardItem.getCardCode() ? 1020 : 1000;
            }
        }
        if (goPriority > priority) priority = goPriority;
        if (checkTable) {
            if (goPriority != 0) priority = goPriority; else if (tcp == null && priority > 0) priority = -priority;
        }
        return priority;
    }

    private int getPriorityOnOtherGo(CardItem cardItem, TableCardPoint tcp, boolean checkTable, GamePlayer goPlayer) {
        int goPriority = 0;
        PlayerStatus otherStatus = gameTable.getOtherPlayerStatus(gamePlayer, goPlayer).get(0);
        PlayerStatus goStatus = gameTable.getPlayerStatus(goPlayer);
        if (gameTable.isKingAvailable(cardItem.getMajorCode()) && tcp == null) {
            if (goStatus.getTakenCards(CardClass.KING).size() > 1) goPriority = -2200; else if (otherStatus.getTakenCards(CardClass.KING).size() > 1) goPriority = 2200; else if (otherStatus.getTakenCards(CardClass.KING).size() > 0) goPriority = 2100;
        }
        int cardStatus = checkCardStatus(cardItem, otherStatus);
        int majorCode = cardItem.getMajorCode();
        boolean takenHalf = gameTable.isCardTaken(majorCode, true);
        int holdCount = gamePlayer.getHoldCardCount(majorCode);
        if (cardStatus == 2) {
            goPriority = tcp == null ? (takenHalf && holdCount > 1 ? 0 : 2400) : takenHalf ? 0 : -2400;
        } else if (cardStatus == 1) {
            goPriority = tcp == null ? (takenHalf && holdCount > 1 ? 0 : 2300) : takenHalf ? 0 : -2300;
        } else {
            cardStatus = checkCardStatus(cardItem, goStatus);
            if (cardStatus == 2) {
                goPriority = tcp == null ? (takenHalf && holdCount > 1 ? 0 : -2400) : takenHalf ? 300 : 500;
            } else if (cardStatus == 1) {
                goPriority = tcp == null ? (takenHalf && holdCount > 1 ? 0 : -2300) : takenHalf ? 300 : 500;
            } else if (tcp != null && tcp.getCardCount(true) > 2) goPriority = goStatus.getCardPoints(CardClass.LEAF) > 0 ? -2300 : -2150;
        }
        if (goPriority == 0 && tcp == null && otherStatus.getCardPoints(CardClass.LEAF) >= 8 && goStatus.getCardPoints(CardClass.LEAF) < 8 && gameTable.isDoubleLeafAvailable(cardItem.getMajorCode())) goPriority = 2100;
        if (checkTable && cardStatus == 0 && goPriority > 0 && (tcp == null || tcp.getCardCount(true) < 3)) goPriority = -goPriority;
        return goPriority;
    }

    /**
     * Checks the card status from the player status
     * 
     * @return 0: not found
     *         1: found
     *         2: rule card
     */
    private int checkCardStatus(CardItem cardItem, PlayerStatus pStatus) {
        int status = 0;
        CardClass[] cc = { CardClass.TEN, CardClass.FIVE };
        int majorCode = cardItem.getMajorCode();
        for (int k = 0; k < cc.length && status == 0; k++) {
            List<CardItem> cards = pStatus.getTakenCards(cc[k]);
            for (int l = 0; l < cards.size() && status == 0; l++) {
                GoStopRule rule = GameRule.getGoStopRule(cards.get(l).getCardCode());
                if (rule != null && gameTable.isRuleAvailable(rule)) {
                    int[] ruleCards = rule.getRuleCards();
                    for (int i = 0; i < ruleCards.length && status == 0; i++) {
                        if (majorCode == CardItem.getMajorCode(ruleCards[i])) status = cardItem.getCardCode() == ruleCards[i] ? 2 : 1;
                    }
                }
            }
        }
        return status;
    }

    private boolean checkSwampedCards() {
        boolean canGo = true;
        Integer[] swampedCards = gameTable.getSwampedCardsOnTable();
        if (swampedCards != null) {
            for (int i = 0; i < swampedCards.length && canGo; i++) canGo = gamePlayer.getHoldCardCount(swampedCards[i]) > 0;
        }
        return canGo;
    }

    private boolean checkOtherPoints() {
        boolean canGo = true;
        List<PlayerStatus> pStatus = gameTable.getOtherPlayerStatus(gamePlayer);
        int winPoints = gameTable.getGameType().getWinPoints();
        for (int i = 0; i < pStatus.size() && canGo; i++) {
            PlayerStatus ps = pStatus.get(i);
            int points = ps.getPlayerPoints();
            int leafCount = GameRule.getLeafCount(ps.getTakenCards(CardClass.LEAF));
            if (ps.isTakenNineUndecided()) leafCount += 2;
            canGo = points == 0 || points < winPoints - 4;
            if (canGo) {
                int kingCount = ps.getTakenCards(CardClass.KING).size();
                int kingForOthers = getMaxKingCountForOthers();
                if ((ps.getCardPoints(CardClass.KING) == (winPoints - 1) && kingForOthers == 0) || (kingCount == (winPoints - 1) && kingForOthers > 0 && (ps.isCardTaken(GameRule.RAIN_KING, false) || (kingForOthers == 1 && kingCount <= 2 && gameTable.isCardTaken(GameRule.RAIN_KING, false) == false)))) {
                    if (ps.getTakenCards(CardClass.TEN).size() >= 4 || ps.getTakenCards(CardClass.FIVE).size() >= 4 || leafCount - 9 + getMaxLeafCountForOthers() >= 1) canGo = false;
                } else if (kingCount > 1) canGo = kingCount + kingForOthers < winPoints;
            }
            if (canGo) {
                CardClass[] cc = { CardClass.TEN, CardClass.FIVE };
                for (int k = 0; k < cc.length && canGo; k++) {
                    List<CardItem> cards = ps.getTakenCards(cc[k]);
                    for (int l = 0; l < cards.size() && canGo; l++) {
                        GoStopRule rule = GameRule.getGoStopRule(cards.get(l).getCardCode());
                        if (rule != null && gameTable.isRuleAvailable(rule)) {
                            int[] cardCodes = rule.getRuleCards();
                            int untaken = 0;
                            boolean canTake = false;
                            for (int m = 0; m < cardCodes.length && canTake == false; m++) {
                                if (gameTable.isCardTaken(cardCodes[m], false) == false) {
                                    untaken++;
                                    canTake = canTake(cardCodes[m]);
                                }
                            }
                            canGo = canTake || untaken > 1;
                        }
                    }
                }
            }
            if (canGo) canGo = leafCount - 9 + getMaxLeafCountForOthers() < winPoints;
        }
        return canGo;
    }

    private int getMaxKingCountForOthers() {
        int remaining = 0;
        boolean hasBonus = gameTable.getBonusCards().getKingCount() > 0;
        for (int kingCode : GameRule.getKingCodes()) {
            if (gameTable.isCardTaken(kingCode, false) == false && (CardItem.isBonusCard(kingCode) == false || hasBonus) && canTake(kingCode) == false) remaining++;
        }
        return remaining;
    }

    private int getMaxLeafCountForOthers() {
        int maxLeaf = 0;
        BonusCards bonusCards = gameTable.getBonusCards();
        if (bonusCards.getTripleCount() > 0) {
            for (int tripleCode : GameRule.getTripleLeafCodes()) {
                if (gameTable.isCardTaken(tripleCode, false) == false && canTake(tripleCode) == false) maxLeaf += 3;
            }
        }
        int doubleBonus = bonusCards.getDoubleCount();
        int doubleStart = CardItem.getCardIndex(CardItem.getCardCode(13, 'b'));
        int remainDouble = 0;
        for (int doubleCode : GameRule.getDoubleLeafCodes()) {
            if (gameTable.isCardTaken(doubleCode, false) == false && (CardItem.isBonusCard(doubleCode) == false || CardItem.getCardIndex(doubleCode) - doubleStart < doubleBonus) && canTake(doubleCode) == false) {
                if (CardItem.isBonusCard(doubleCode)) maxLeaf += 2; else remainDouble++;
            }
        }
        if (remainDouble == 0) maxLeaf += 4; else if (remainDouble == 1) maxLeaf += 5; else maxLeaf += 6;
        maxLeaf = (int) (maxLeaf * 2 / 3.0f);
        return maxLeaf;
    }

    private boolean canTake(int cardCode) {
        boolean canTake;
        if (CardItem.isBonusCard(cardCode)) canTake = gamePlayer.isHoldingCard(cardCode); else {
            int majorCode = CardItem.getMajorCode(cardCode);
            boolean takenHalf = gameTable.isCardTaken(majorCode, true);
            if (takenHalf) canTake = gamePlayer.getHoldCardCount(majorCode) > 0; else {
                canTake = gamePlayer.isHoldingCard(cardCode);
                if (canTake == false && gamePlayer.getHoldCardCount(majorCode) > 0) {
                    TableCardPoint tcp = gameTable.getTableCardPoint(majorCode, false);
                    canTake = tcp != null && tcp.getCardCount(true) == 3;
                }
            }
        }
        return canTake;
    }

    private int getTakenCount(GoStopRule rule) {
        int taken = 0;
        for (int cardCode : rule.getRuleCards()) {
            if (gameTable.isCardTaken(cardCode, false)) taken++;
        }
        return taken;
    }
}
