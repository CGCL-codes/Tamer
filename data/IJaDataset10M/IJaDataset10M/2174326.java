package gui.GLJPanels;

import gui.GLListeners.GLListenerPistilo;

public class GLJPanelPistilo extends GLJPanelCurvas {

    private static final long serialVersionUID = 1L;

    public GLJPanelPistilo(GLListenerPistilo GLL) {
        super();
        this.gllistener = (GLListenerPistilo) GLL;
    }
}
