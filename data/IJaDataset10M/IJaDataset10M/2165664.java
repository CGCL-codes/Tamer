package org.jfge.api.fighter;

import java.util.List;
import org.jfge.api.engine.Updatable;

/**
 * The Interface InputBufferQueue.
 */
public interface InputQueue extends Updatable {

    /**
	 * Handle input.
	 *
	 * @param input the input
	 * @return the list
	 */
    public List<String> handleInput(String input);
}
