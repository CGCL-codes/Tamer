package agents;

import algorithms.ISelector;
import environment.IEnvironmentSingle;

/** The basic behavior of an Agent is : 
 <ul>
<li> According to the current state of the environment, choose the action</li>
<li> Apply this action, get the reward</li>
</ul>

Every Agent can call its underlying <i>algorithm</i>, and ask it to choose the action. 

 @author Francesco De Comite (decomite at lifl.fr)
 @version $Revision: 1.0 $ 
*/
public class LoneAgent extends AbstractAgent {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /** Place the agent  in the environment.*/
    public LoneAgent(IEnvironmentSingle s, ISelector al) {
        super(s, al);
        if (s != null) this.currentState = s.defaultInitialState();
    }
}
