package battleTank;

/**
 * This simple class simulates the speed on which the tanks will move if on
 * grass. If the tanks are on grass, the movement speed will be 5.
 * 
 * @author Team Exception
 * 
 * @see Terrain, Ice, Sand
 * 
 * @serial
 */
public class Grass implements Terrain {

    private static final long serialVersionUID = 1L;

    /**
	 * This method returns the speed at which the tank moves on grass
	 * 
	 * @return speed of 5 for on grass
	 */
    @Override
    public int setSpeed() {
        return 5;
    }
}
