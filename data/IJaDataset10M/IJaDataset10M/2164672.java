package nashQLearning;

import environment.Filter;
import environment.IEnvironment;
import environment.IState;

/**
 * @author Francesco De Comit�
 *
 * 29 mars 07
 */
public class positionFilter extends Filter {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    protected int rank;

    /**
	 * 
	 */
    public positionFilter(int rank) {
        this.rank = rank;
    }

    @Override
    public IState filterState(IState s, IEnvironment c) {
        NashState ns = (NashState) s;
        positionState nps = new positionState(c);
        nps.setXcoord(ns.getPos(2 * this.rank));
        nps.setYcoord(ns.getPos(2 * this.rank + 1));
        nps.setXcoord2(ns.getPos(2 * (1 - this.rank)));
        nps.setYcoord2(ns.getPos(2 * (1 - this.rank) + 1));
        nps.setRank(this.rank);
        return nps;
    }
}
