package hotgammon.domain;

public class HotgammonFactoryGammamon extends HotgammonFactory {

    public void init() {
        super.dice = new DiceGammamon();
        super.winner = new WinnerAlphamon(super.dice);
        super.move = new MoveAlphamon(super.board);
    }
}
