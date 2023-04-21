package event.notifyTexture;

import gui.jtab.JTab;

public class NotifyTexture {

    String ID;

    NotifyTextureListenerInterface miReceptor;

    public void addEventNotifyTextureListener(JTab receptor) {
        if (miReceptor == null) miReceptor = receptor;
    }

    public void launchEvent() {
        miReceptor.catchNotifyTextureListener(new EventNotifyTexture(this, ID));
    }
}
