package vizz3d_data.layouts.ccvisu;

import java.io.BufferedReader;

/*****************************************************************
 * Reader for input data.
 * @version  $Revision: 1.2 $; $Date: 2005/08/22 12:30:55 $
 * @author   Dirk Beyer
 *****************************************************************/
public abstract class ReaderData {

    /** Input stream reader object. */
    protected BufferedReader in;

    /**
   * Constructor.
   * @param in  Stream reader object.
   */
    public ReaderData(BufferedReader in) {
        this.in = in;
    }

    /*****************************************************************
   * Reads the graph or layout data from stream reader <code>in</code>.
   * @return   Graph or layout data, stored in a <code>GraphData</code> object.
   *****************************************************************/
    public abstract GraphData read();
}

;
