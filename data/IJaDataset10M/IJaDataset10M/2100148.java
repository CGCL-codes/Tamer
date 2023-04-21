package motejx.extensions.classic;

import java.util.EventListener;

/**
 *  
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public interface ClassicControllerButtonListener extends EventListener {

    public void buttonPressed(ClassicControllerButtonEvent evt);
}
