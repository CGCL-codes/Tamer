package ptolemy.plot;

/**
 Exception thrown by plot classes if there are format
 problems with the data to be plotted.

 @author Christopher Brooks
 @version $Id: CmdLineArgException.java,v 1.34 2005/07/28 18:49:20 cxh Exp $
 @since Ptolemy II 0.2
 @Pt.ProposedRating Yellow (cxh)
 @Pt.AcceptedRating Yellow (cxh)
 */
public class CmdLineArgException extends Exception {

    public CmdLineArgException() {
        super();
    }

    public CmdLineArgException(String s) {
        super(s);
    }
}
