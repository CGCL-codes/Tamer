package bluffinmuffin.poker;

import bluffinmuffin.poker.entities.TableInfoTraining;
import bluffinmuffin.poker.entities.dealer.AbstractDealer;

public class PokerGameTraining extends PokerGame implements IPokerGame {

    public PokerGameTraining() {
        super();
    }

    public PokerGameTraining(TableInfoTraining table, int wtaPlayerAction, int wtaBoardDealed, int wtaPotWon) {
        super(table, wtaPlayerAction, wtaBoardDealed, wtaPotWon);
    }

    public PokerGameTraining(AbstractDealer dealer) {
        super(dealer);
    }

    public PokerGameTraining(AbstractDealer dealer, TableInfoTraining table, int wtaPlayerAction, int wtaBoardDealed, int wtaPotWon) {
        super(dealer, table, wtaPlayerAction, wtaBoardDealed, wtaPotWon);
    }

    public TableInfoTraining getTrainingTable() {
        return (TableInfoTraining) m_table;
    }
}
