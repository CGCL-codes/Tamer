package fr.esrf.tangoatk.core.util;

import java.util.EventListener;

/**
 *
 * @author  OUNSY
 */
public interface INonAttrSpectrumListener extends EventListener {

    public void spectrumChange(NonAttrNumberSpectrumEvent e);
}
