package hotgammon.domain;

public class HotgammonFactoryBetamon extends HotgammonFactory {

    public void init() {
        super.dice = new DiceAlphamon();
        super.winner = new WinnerAlphamon(super.dice);
        super.move = new MoveBetamon(super.board);
    }
}
