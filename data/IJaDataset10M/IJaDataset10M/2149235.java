package bluffinmuffin.poker;

import bluffinmuffin.poker.entities.TableInfoCareer;
import bluffinmuffin.poker.entities.dealer.AbstractDealer;

public class PokerGameCareer extends PokerGame implements IPokerGame {

    public PokerGameCareer() {
        super();
    }

    public PokerGameCareer(TableInfoCareer table, int wtaPlayerAction, int wtaBoardDealed, int wtaPotWon) {
        super(table, wtaPlayerAction, wtaBoardDealed, wtaPotWon);
    }

    public PokerGameCareer(AbstractDealer dealer) {
        super(dealer);
    }

    public PokerGameCareer(AbstractDealer dealer, TableInfoCareer table, int wtaPlayerAction, int wtaBoardDealed, int wtaPotWon) {
        super(dealer, table, wtaPlayerAction, wtaBoardDealed, wtaPotWon);
    }

    public TableInfoCareer getTrainingTable() {
        return (TableInfoCareer) m_table;
    }
}
