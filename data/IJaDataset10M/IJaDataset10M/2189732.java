package cards.game.poker;

import cards.Game;
import cards.Hand;
import cards.HandRankFactory;

/**
 * @author Mathieu Carbou
 */
public final class PokerGame extends Game {

    @Override
    protected HandRankFactory handRankFactory(Hand hand) {
        return RankType.getHigherRank(hand);
    }
}
