package gameEngine.baseElement;

import java.io.*;

/**
 * A Base Class that is used by all classes that contains element of the game: - Cards - Maps - Pawns - Dices...
 * @author  Nicola
 */
public abstract class GameItem extends GameComposite implements Serializable {

    private String partType;

    private String name;

    private String image;

    private boolean hidden;

    public GameItem(String partType, String name, String image, boolean hidden) {
        super(null);
        this.partType = partType;
        this.name = name;
        this.image = image;
        this.hidden = hidden;
    }

    /**
	 * @return  The image name for this game part.
	 * @uml.property  name="image"
	 */
    public String getImage() {
        return image;
    }

    /**
	 * @return  The name of this game part.
	 * @uml.property  name="name"
	 */
    public String getName() {
        return name;
    }

    /**
	 * @return  The name of the part
	 * @uml.property  name="partType"
	 */
    public String getPartType() {
        return partType;
    }

    public void setPartType(String partType) {
        this.partType = partType;
    }

    /**
	 * @return  The visibility state of the part
	 * @uml.property  name="hidden"
	 */
    public boolean isHidden() {
        return hidden;
    }

    public String toString() {
        return getName();
    }
}
