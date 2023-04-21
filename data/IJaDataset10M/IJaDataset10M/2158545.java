package wtanaka.praya.gale;

import java.io.IOException;
import wtanaka.praya.Pushable;
import wtanaka.praya.obj.Message;

/**
 * New World Order AKD responder.
 *
 * <p>
 * Return to <A href="http://sourceforge.net/projects/praya/">
 * <IMG src="http://sourceforge.net/sflogo.php?group_id=2302&type=1"
 *   alt="Sourceforge" width="88" height="31" border="0"></A>
 * or the <a href="http://praya.sourceforge.net/">Praya Homepage</a>
 *
 * @see OWOAKDResponder
 * @see PublicKeyFetchOperation
 *
 * @author $Author: wtanaka $
 * @version $Name:  $ $Date: 2003/12/17 01:25:17 $
 **/
public class AKDResponder implements Pushable {

    private GaleClient m_parent;

    public AKDResponder(GaleClient parent) {
        m_parent = parent;
    }

    public void receiveMessage(Message m) {
        if (!(m instanceof KeyRequestMessage)) return;
        Location keyLocation = ((KeyRequestMessage) m).getKeyLocation();
        GalePublicKey key = KeyCache.getInstance().getPublic(keyLocation);
        if (key != null) {
            StringBuffer category = new StringBuffer("@");
            category.append(keyLocation.getDomainPart());
            category.append("/user/_gale/key/");
            for (int i = 0; i < keyLocation.getNumLocalPieces(); ++i) {
                category.append(keyLocation.getLocalPieceAt(i));
                category.append("/");
            }
            PuffOperation puff = new PuffOperation(new Category(category.toString()), null);
            try {
                puff = puff.withNewFragment(new KeyFragment(FragmentInterface.NWO_KEY, key));
                puff = puff.withNewFragment(new InMemoryTimeFragment(FragmentInterface.TIME));
                puff = puff.withNewFragment(new InMemoryTextFragment(FragmentInterface.INSTANCE, m_parent.getIdInstance()));
                puff = puff.withNewFragment(new InMemoryTextFragment(FragmentInterface.CLASS, m_parent.getIdClass()));
                puff = m_parent.signPuff(puff);
                puff.write(m_parent.getConnection().getOutput());
            } catch (NotEnoughInfoException e) {
            } catch (IOException e) {
            }
        }
    }
}
