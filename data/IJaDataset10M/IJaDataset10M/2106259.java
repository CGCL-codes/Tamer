package net.sf.kmoviecataloger.core.listeners;

import java.util.EventObject;
import net.sf.kmoviecataloger.core.Movie;

/**
 *
 * @author knitter
 */
public class MovieEvent extends EventObject {

    public MovieEvent(Movie source) {
        super(source);
    }
}
