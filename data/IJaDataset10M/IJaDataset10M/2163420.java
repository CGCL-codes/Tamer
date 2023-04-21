package org.xtoto.dao;

import org.xtoto.model.Game;
import java.util.Date;
import java.util.List;

public interface GameDao extends DaoBase<Game> {

    List<Game> findGamesOrdered();

    Date findLastGameDate();

    boolean isLastGameOver();
}
