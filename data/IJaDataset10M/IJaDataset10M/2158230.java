package com.shithead.game;

import java.util.List;
import com.shithead.bo.Card;
import com.shithead.bo.score.PlayerScore;
import com.shithead.enums.GameAction;
import com.shithead.enums.GameState;

public interface GameObserver {

    public void playersMove(Integer id, GameAction action, List<Card> cards);

    public void setGameState(GameState state, List<PlayerScore> scores);
}
