package wrapper.symore;

import symore.conflictsolving.ConflictSolvingAlgorithm;
import symore.events.Event;
import symore.sentinel.ISentinelManager;

/**
 * Der Konfliktl�sungslagorithmus f�r das Puzzlespiel
 * �hnelt dem 
 * 
 * @author Sebastian Martens, Manuel Scholz
 */
public class PuzzleConflictSolver extends ConflictSolvingAlgorithm {

    /**
	 * 
	 * 
	 * 
	 */
    public boolean solveConflict(ISentinelManager sm, Event from, Event until, boolean withReadSet) {
        return false;
    }
}
