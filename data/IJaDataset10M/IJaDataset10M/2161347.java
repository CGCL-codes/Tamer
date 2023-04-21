package stack.era.database;

import stack.era.domain.town.Town;

public class TownSaver extends Saver<Town> {

    public TownSaver() {
        super("towns");
    }
}
