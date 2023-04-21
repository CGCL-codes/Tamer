package pdp.scrabble.ia;

import pdp.scrabble.game.GameEnvironment;
import pdp.scrabble.game.Player;

/** An abstract Class for Move generating types.
 *  Handles interfacing between {@link AbstractAlgoStep} and {@link MoveGenerator}
 * @author alexandre
 *
 */
public abstract class AbstractMoveGenStep extends AbstractAlgoStep implements MoveGenerator {

    private MoveAccumulator moveStorage;

    public AbstractMoveGenStep(GameEnvironment env, Player p, MoveAccumulator accu) {
        super(env, p);
        this.moveStorage = accu;
    }

    @Override
    public void process() {
        this.moves.clear();
        if (previousStep != null) previousStep.process();
        generate(this.environment.board(), this.usedPlayer.getRack());
        moveStorage.noMoreMoves();
    }

    protected void registerMove(MoveModel move) {
        moveStorage.register(move);
    }
}
