package com.alesj.blueberry.spring.games.impl;

import com.alesj.blueberry.par.basic.GameObject;
import com.alesj.blueberry.par.lotto.LottoTicket;
import com.alesj.blueberry.spring.games.Game;

/**
 * @author <a href="mailto:ales.justin@genera-lynx.com">Ales Justin</a>
 */
public class LottoStrategy extends AbstractStrategy {

    public Game getGame() {
        return Game.LOTTO;
    }

    public boolean useStrategy(String number, String message) {
        return true;
    }

    public GameObject createGameObject() {
        return new LottoTicket();
    }
}
