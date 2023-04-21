package DE.FhG.IGD.semoa.demo;

import DE.FhG.IGD.semoa.server.*;
import DE.FhG.IGD.ui.*;

/**
 * A demo OutGate that plays an audicon whenever an agent is
 * transported.
 *
 * @author Volker Roth
 * @version "$Id: DemoOutGate.java 389 2001-07-03 18:36:01Z vroth $"
 */
public class DemoOutGate extends OutGate {

    /**
     * The default name of the audio file.
     */
    public static final String OUTGOING_AUDIO = "DE/FhG/IGD/semoa/demo/outgoing.au";

    /**
     * The name of the audio file.
     */
    private volatile String audio_ = OUTGOING_AUDIO;

    /**
     * Creates an instance of this service.
     */
    public DemoOutGate() {
        super();
    }

    /**
     * Sets the name of the audio file that is played whenever
     * an agent is accepted.
     *
     * @param name The path to the audio file relative to
     *   some resource in the class path. If the name is
     *   <code>null</code> or the empty string then sound
     *   playing is switched off.
     */
    public void setSound(String name) {
        if (name != null && name.length() > 0) {
            audio_ = name;
        } else {
            audio_ = null;
        }
    }

    /**
     * This callback is invoked whenever an agent is transported.
     * It plays an audicon.
     *
     * @param ctx The <code>AgentContext</code> of the agent
     *   that was just transported.
     */
    protected void onTransport(AgentContext ctx) {
        String name;
        name = audio_;
        if (name != null) {
            Multimedia.playSound(name);
        }
        return;
    }
}
