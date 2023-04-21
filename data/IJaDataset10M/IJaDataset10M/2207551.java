package be.hoornaert.tom.games.golddigger.domain.entities.tools;

import be.hoornaert.tom.games.golddigger.infrastructure.common.Range;

public class PickAxe extends Tool {

    public PickAxe() {
        super("PickAxe", 15, new Range(15, 30), 1, 1);
    }
}
