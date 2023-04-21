package fr.esrf.tangoatk.core;

import java.util.EventListener;

public interface ISetErrorListener extends EventListener, java.io.Serializable {

    public void setErrorOccured(ErrorEvent evt);
}
