package lib.mylib.object;

/**
 * For thnings that should be updated
 * 
 * @author Tobse
 */
public interface Updateable {

    /**
	 * Performs all logic which should be updated
	 * 
	 * @param delta time in ms since the last time updateded was called
	 */
    public void update(int delta);
}
