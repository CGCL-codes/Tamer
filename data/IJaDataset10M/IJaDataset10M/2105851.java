package wtanaka.praya;

/**
 * Thrown if an attempted message send does not complete for some reason.
 *
 * <p>
 * Return to <A href="http://sourceforge.net/projects/praya/">
 * <IMG src="http://sourceforge.net/sflogo.php?group_id=2302&type=1"
 *   alt="Sourceforge" width="88" height="31" border="0"></A>
 * or the <a href="http://praya.sourceforge.net/">Praya Homepage</a>
 *
 * @author $Author: wtanaka $
 * @version $Name:  $ $Date: 2001/10/14 18:07:12 $
 **/
public class NotSentException extends Exception {

    public NotSentException(String msg) {
        super(msg);
    }
}
