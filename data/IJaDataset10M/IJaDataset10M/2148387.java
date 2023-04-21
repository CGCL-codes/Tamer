package fr.esrf.tangoatk.core;

import java.beans.*;

public interface ISpectrumListener extends IAttributeStateListener {

    public void spectrumChange(NumberSpectrumEvent e);
}
