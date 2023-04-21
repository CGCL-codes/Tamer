package fr.esrf.tangoatk.core;

import java.util.EventListener;

public interface IAttributeStateListener extends IErrorListener {

    public void stateChange(AttributeStateEvent e);
}
