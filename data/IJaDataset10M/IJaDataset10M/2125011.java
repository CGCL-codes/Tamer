package affichage;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class MapHalloween extends MapPattern {

    private Image imageHalloween;

    private Image citrouilleMap;

    /** Constructeur de la map. Attention, n'initialise pas la map. 
	 * @throws SlickException */
    public MapHalloween() throws SlickException {
        super(30, 30);
        this.imageHalloween = new Image("affichage/Map/Herbe.png");
        this.citrouilleMap = new Image("affichage/Map/citrouilleMap.png");
    }

    /** Initialise la map. */
    public void init() {
        super.init(0, 0, super.getSizeMapX() - 1, super.getSizeMapY() - 1, imageHalloween);
        super.init(0, 0, 20, 20, citrouilleMap);
    }
}
