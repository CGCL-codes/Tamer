package tgreiner.amy.chess.tablebases;

/**
 * Prober for endgames with three chess men.
 *
 * @author <a href = "mailto:thorsten.greiner@googlemail.com">Thorsten Greiner</a>
 */
public interface ThreeMenProber extends Prober {

    /**
     * Get the winner's piece position.
     *
     * @return the winner's piece position
     */
    int getPiece();
}
