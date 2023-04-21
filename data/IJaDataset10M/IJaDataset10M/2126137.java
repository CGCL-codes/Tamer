package frost.util.gui.textpane;

import javax.swing.JEditorPane;

/**
 * Base class for Decoder
 * @author ET
 */
public abstract class Decoder {

    /**
	 * Maybe override by subclass for decode String message.
	 * Decode message and append to JEditorPane document.
	 */
    public void decode(String message, JEditorPane parent) {
    }
}
