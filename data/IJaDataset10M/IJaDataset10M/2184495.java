package playground.thibautd.tsplanoptimizer.timemodechooser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import playground.thibautd.tsplanoptimizer.framework.Move;
import playground.thibautd.tsplanoptimizer.framework.MoveGenerator;
import playground.thibautd.tsplanoptimizer.framework.Solution;
import playground.thibautd.tsplanoptimizer.framework.Value;

/**
 * @author thibautd
 */
public class AllPossibleModesMovesGenerator implements MoveGenerator {

    private final List<Move> moves = new ArrayList<Move>();

    public AllPossibleModesMovesGenerator(final Solution initialSolution, final Collection<String> possibleModes) {
        int i = 0;
        for (Value value : initialSolution.getRepresentation()) {
            if (value.getValue() instanceof String) {
                for (String mode : possibleModes) {
                    moves.add(new ModeMove(i, mode));
                }
            }
            i++;
        }
    }

    @Override
    public Collection<Move> generateMoves() {
        return moves;
    }
}
