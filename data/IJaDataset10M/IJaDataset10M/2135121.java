package nl.tudelft.simulation.mfssm.animation;

import java.io.Serializable;

/**
 * WorkerIntrospector
 * <p>
 * (c) copyright 2007 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> 
 * or <a href=" or https://sourceforge.net/projects/mfssm/">sourceforge.net/projects/mfssm/</a><br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @version 1.0 <br>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/royc/index.htm">Roy Chin
 *         </a>
 */
public class WorkerIntrospector implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** name of the worker */
    private String name = null;

    /**
	 * Constructor.
	 * 
	 * @param name
	 *            of the worker to introspect
	 */
    public WorkerIntrospector(final String name) {
        super();
        this.name = name;
    }

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return this.name;
    }
}
