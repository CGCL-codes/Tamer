package com.rpgeeframework;

/**
 * @author allen
 *
 */
public abstract class DescriptionChooser {

    /**
	 * this is the game object for which this chooser will dictate the behaviour of lookups for.
	 */
    private GameObject gameObject;

    public DescriptionChooser(GameObject g) {
        setGameObject(g);
    }

    public String[] getShortDescriptions() {
        return getGameObject().getShortDesc();
    }

    public abstract String getShortDesc();

    /**
	 * @param gameObject the gameObject to set
	 */
    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    /**
	 * @return the gameObject
	 */
    public GameObject getGameObject() {
        return gameObject;
    }
}
