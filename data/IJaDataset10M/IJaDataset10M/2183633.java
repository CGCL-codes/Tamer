package csillag.attribute;

import csillag.framework.BitmapCharacter;

/**
 * v�zszintes vonalakkal metszi a k�pet, �s a k�rt vonal metsz�spontjaival t�r vissza
 * 
 * @author Rocciax
 */
public class HorizontalMidlineAttribute extends AbstractAttribute {

    private int db;

    private int actual;

    public HorizontalMidlineAttribute() {
        this(1, 1);
    }

    public HorizontalMidlineAttribute(int db, int actual) {
        super();
        this.db = db;
        this.actual = actual;
    }

    @Override
    public Double getValue() {
        if (character == null) {
            return null;
        }
        if (db < 1 || actual < 1 || db < actual) {
            return null;
        }
        double value = 0.0;
        int linerange = character.getHeight() / (db + 1);
        boolean black = false;
        for (int i = 0; i < character.getWidth(); i++) {
            if (character.get(linerange * actual, i) == BitmapCharacter.BLACK_PIXEL) {
                if (!black) {
                    black = true;
                    value++;
                }
            } else {
                black = false;
            }
        }
        return value;
    }
}
