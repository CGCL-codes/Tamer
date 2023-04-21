package net.kortsoft.gameportlet.service;

import java.util.List;
import net.kortsoft.gameportlet.model.GameType;
import net.kortsoft.gameportlet.model.GameTypeId;

public interface GameTypeService {

    List<GameType> listAll();

    GameType storeGameType(GameType gameType);

    GameType load(GameTypeId gameTypeId);

    GameType byName(String name);
}
