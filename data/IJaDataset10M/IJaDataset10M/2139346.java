package net.sf.doolin.gui.display;

/**
 * Abstract adapter that manages the read-only state
 * 
 * @author Damien Coraboeuf
 * 
 * @param <S>
 *            Source type
 */
public abstract class AbstractStateAdapter<S> implements StateAdapter<S> {

    /**
	 * Read-only, no adaptation
	 */
    @Override
    public S convertTargetToSubject(DisplayState targetValue) {
        return null;
    }
}
