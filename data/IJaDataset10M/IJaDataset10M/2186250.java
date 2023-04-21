package wtanaka.praya.gui;

import wtanaka.praya.Protocol;

/**
 * Protocol List Widget.
 *
 * <p>
 * Return to <A href="http://sourceforge.net/projects/praya/">
 * <IMG src="http://sourceforge.net/sflogo.php?group_id=2302&type=1"
 *   alt="Sourceforge" width="88" height="31" border="0"></A>
 * or the <a href="http://praya.sourceforge.net/">Praya Homepage</a>
 *
 * @author $Author: wtanaka $
 * @version $Name:  $ $Date: 2001/10/07 19:44:23 $
 **/
public interface ProtocolList extends Widget {

    void addProtocol(Protocol p);

    void removeProtocol(Protocol p);
}
