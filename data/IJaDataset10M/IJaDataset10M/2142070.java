package hotgammon.domain;

public class HotgammonFactoryDeltamon extends HotgammonFactory {

    public void init() {
        super.dice = new DiceAlphamon();
        super.move = new MoveAlphamon(super.board);
        super.winner = new WinnerDeltamon(super.dice, super.board);
    }
}
