package prodoc;

import java.util.Vector;

/**
 *
 * @author jhierrot
 */
public class Conditions {

    /**
 *
 */
    private boolean OperatorAnd = true;

    /**
 *
 */
    private Vector CondList = new Vector();

    /**
 *
 * @param Cond
 */
    public void addCondition(Condition Cond) {
        CondList.add(Cond);
    }

    /**
 *
 * @param ListCond
 */
    public void addCondition(Conditions ListCond) {
        CondList.add(ListCond);
    }

    /**
 *
 * @return
 */
    public int NumCond() {
        return (CondList.size());
    }

    /**
 * 
 * @param n
 * @return
 */
    public Object Cond(int n) {
        return (CondList.elementAt(n));
    }

    /**
* @return the OperatorAnd
*/
    public boolean isOperatorAnd() {
        return OperatorAnd;
    }

    /**
* @param OperatorAnd the OperatorAnd to set
*/
    public void setOperatorAnd(boolean OperatorAnd) {
        this.OperatorAnd = OperatorAnd;
    }
}
