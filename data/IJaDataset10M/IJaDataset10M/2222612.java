package to.ax.games.card;

public interface RandomIntegerGenerator {

    /** Return a card selection from 0..maxCard - 1, often a random one.*/
    int randomInteger(int max);
}
