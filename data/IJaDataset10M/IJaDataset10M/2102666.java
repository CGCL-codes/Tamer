package jonkoshare.event;

/**
 * Listener to get notified, that the
 * panel changed state. Normally this causes
 * the midi panel to change state, too
 * 
 * @since 0.0.2
 * @author methke01
 *
 */
public interface SynthesizerPanelListener {

    void synthesizerPanelEnabled(boolean state);
}
