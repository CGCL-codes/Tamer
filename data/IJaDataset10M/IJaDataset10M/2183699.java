package memory;

import environment.IAction;
import environment.IState;
import environment.ITwoPlayerState;

/** 
The version studied by  <a href="http://www.cs.tau.ac.il/%7Ezwick/papers/memory-game.ps.gz">Zwick and Patterson</a>,where they try to maximize the number of cards. 

Compared to <code>MemoryBoard</code>, only the <code>getReward</code> method changes.


@see MemoryBoard

 @author Francesco De Comite (decomite at lifl.fr)
 @version $Revision: 1.0 $ 

 */
public class MemoryBoardZP extends MemoryBoard {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public MemoryBoardZP() {
    }

    ;

    public MemoryBoardZP(int i, int j) {
        super(i, j);
    }

    public double getReward(IState s1, IState s2, IAction a) {
        ITwoPlayerState ss2 = (ITwoPlayerState) s2;
        if (ss2.getTurn()) return -nbCartes / 4 + ((MemoryState) s2).firstFound(); else return -nbCartes / 4 + ((MemoryState) s2).secondFound();
    }
}
