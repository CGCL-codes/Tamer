package ge.modules;

public interface Module {

    /**
	 * 	This function indicates what you have to do in every update call for the game
	 */
    public void update();

    /**
	 * 	This function indicates what you have to draw in every update call for the game
	 */
    public void draw();

    /**
	 * This function indicates what you have to disable when you are deleted
	 */
    public void delete();
}
