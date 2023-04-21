package nlp.lang.he.morph.erel.awt;

import java.applet.Applet;
import java.awt.Event;
import java.awt.Frame;

class AppletViewingFrame extends Frame {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5419838533653733061L;

    protected boolean m_fStandAlone = false;

    public AppletViewingFrame(String str, boolean fStandAlone) {
        super(str);
        m_fStandAlone = fStandAlone;
    }

    @Override
    public boolean handleEvent(Event evt) {
        switch(evt.id) {
            case Event.WINDOW_DESTROY:
                dispose();
                if (m_fStandAlone) System.exit(0);
                return true;
            default:
                return super.handleEvent(evt);
        }
    }
}

/**
 * an applet that can also run standalone
 */
public class AmphibianApplet extends Applet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5636549126960170811L;

    protected boolean m_fStandAlone = false;

    protected Frame parent = null;

    protected AmphibianApplet standAlone() {
        m_fStandAlone = true;
        init();
        start();
        return this;
    }

    /**
	 * creates this applet's parent. If you override this method, you should
	 * call super.init() in the beginning of the overriding routine.
	 */
    public void init() {
        parent = new AppletViewingFrame(getClass().toString(), m_fStandAlone);
        parent.add("Center", this);
    }

    public void start() {
        parent.pack();
        parent.show();
        parent.pack();
        if (m_fStandAlone) parent.resize(parent.preferredSize()); else parent.resize(800, 400);
    }

    public void stop() {
        parent.hide();
    }
}
