package console;

import java.util.Observer;

/**
 *
 * @author Michael Hanns
 *
 */
public interface IOBuffer {

    public String getText();

    public void writeTo(String input);

    public void addObserver(Observer o);
}
