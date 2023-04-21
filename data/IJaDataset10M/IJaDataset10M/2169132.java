package nashQLearning;

import environment.ComposedActionHashMap;
import environment.IEnvironmentSingle;

/**
 * @author Francesco De Comit�
 *
 * 22 juin 07
 */
public class SwarmHashMapNashQMatrix2x2 extends AbstractSwarmNashQMatrix2x2 {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * @param s
	 */
    public SwarmHashMapNashQMatrix2x2(IEnvironmentSingle s) {
        super(s);
        ca = new ComposedActionHashMap();
    }
}
