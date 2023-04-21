package clubmixer.client.handler.progressbar;

import java.util.EventListener;

/**
 *
 * @author Alexander Schindler
 */
public interface ProgBarListener extends EventListener {

    void progBarStateChanged(ProgBarEvent event);
}
