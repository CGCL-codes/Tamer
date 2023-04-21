package Diff;

import java.io.Serializable;
import java.util.Vector;

public abstract class Operation implements Serializable {

    public int pos;

    public int len;

    public Operation(Integer position) {
        this.pos = position;
    }

    public abstract String exec(String in);

    /**
	 * 
	 * @param OPs are the Operations which are analysed to manipulate {@code this}.
	 * @param ops_are_incoming specifies whether the Operations {@code OPs} are incoming and therefore the Operation {@code this} is outgoing - has already been applied to he text.
	 * @return a new Operation that is ready to be applied to the final text
	 */
    public abstract Operation include(Vector<Operation> OPs, boolean ops_are_incoming);
}
