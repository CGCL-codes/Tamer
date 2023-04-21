package galacticthrone.map.data.obs;

/**
 * <br>
 *
 * @author Jaco van der Westhuizen
 */
public class Ob {

    Container container;

    private float posX;

    private float posY;

    protected Ob(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
    }

    protected Ob(Container container) {
        this.container = container;
    }

    /**
     * @return x position
     */
    public float getPosX() {
        if (container != null) posX = container.getPosX();
        return posX;
    }

    /**
     * @return y position
     */
    public float getPosY() {
        if (container != null) posY = container.getPosY();
        return posY;
    }
}
