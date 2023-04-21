package zildo.monde.sprites.persos;

import zildo.monde.sprites.desc.ElementDescription;
import zildo.monde.sprites.elements.Element;

/**
 * @author Tchegito
 * 
 */
public class PersoShadowed extends PersoNJ {

    int addY;

    public PersoShadowed() {
        super();
        shadow = new Element();
        shadow.setSprModel(ElementDescription.SHADOW);
        addPersoSprites(shadow);
    }

    public PersoShadowed(ElementDescription p_shadowType, int p_addY) {
        this();
        shadow.setSprModel(p_shadowType);
        addY = p_addY;
    }

    @Override
    public void finaliseComportement(int compteur_animation) {
        if (persoSprites.size() > 0) {
            Element ombre = persoSprites.get(0);
            ombre.setX(x);
            ombre.setY(y - 1);
            ombre.setZ(-7 + addY);
            ombre.setVisible(z >= 0);
        }
        super.finaliseComportement(compteur_animation);
    }
}
