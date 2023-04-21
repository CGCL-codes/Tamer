package net.sourceforge.ondex.transformer.optimalpaths;

import net.sourceforge.ondex.event.type.EventType;

/**
 * Error thrown when the pathway specified is invalid
 * @author hindlem
 *
 */
public class InvalidPathwayErrorEvent extends EventType {

    /**
	 * 
	 * @param message error that occured
	 * @param extension calling class and method
	 */
    public InvalidPathwayErrorEvent(String message, String extension) {
        super(message, extension);
        super.desc = "The pathway specified is invalid: ";
    }
}
