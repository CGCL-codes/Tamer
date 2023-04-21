package algorithms;

import java.util.Iterator;
import util.KeyBoard;
import dataset.Dataset;
import environment.ActionList;
import environment.IAction;
import environment.IState;
import environment.NullableAction;

/**
 * A Generic (i.e. available for any problem) Human Player.<br>
 * This HCI is minimal : you are asked to choose in a list of all possible
 * moves.
 * 
 * @author Francesco De Comite (decomite at lifl.fr)
 * @version $Revision: 1.0 $
 * 
 */
public class HumanSelector implements ISelector {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /** The learning phase is left to the human... */
    public void learn(IState s1, IState s2, IAction a, double reward) {
    }

    /** Lists the legal moves, ask human to choose */
    public IAction getChoice(ActionList l) {
        Iterator<IAction> iterator = l.iterator();
        IAction firstAction = iterator.next();
        if (((NullableAction) firstAction).isNullAction()) {
            System.out.println("You must Pass");
            return firstAction;
        }
        while (true) {
            System.out.println("Choose in this list : ");
            int i = 0;
            Iterator<IAction> actions = l.iterator();
            while (actions.hasNext()) {
                IAction action = actions.next();
                System.out.println(i + " " + action);
                i++;
            }
            int j = KeyBoard.readInt();
            if ((j >= 0) && (j < l.size())) return l.get(j);
        }
    }

    /** No data collected... */
    public Dataset extractDataset() {
        return null;
    }

    /** Nothing to reset */
    public void newEpisode() {
    }

    ;
}
