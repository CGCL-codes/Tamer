package motej;

import java.util.EventListener;

/**
 * 
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public interface MoteFinderListener extends EventListener {

    public void moteFound(Mote mote);
}
