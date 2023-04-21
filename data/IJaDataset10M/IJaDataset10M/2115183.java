package model;

import model.co.COId;
import vue.IIGGameInterface;

/**
 * A new player in the game description
 * 
 * @29 juil. 2010
 * @author Gronowski St�phane stephane.gronowski@gmail.com
 */
public class NewPlayerDescription {

    private COId co;

    private Color color;

    private IIGGameInterface gameInterface;

    private String pseudo;

    public NewPlayerDescription(COId co, Color color, IIGGameInterface gameInterface, String pseudo) {
        this.co = co;
        this.color = color;
        this.gameInterface = gameInterface;
        this.pseudo = pseudo;
    }

    /**
     * Return the {@link COId}.
     * 
     * @return the {@link COId}
     */
    public COId getCo() {
        return co;
    }

    /**
     * Return the color.
     * 
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Return the {@link IIGGameInterface} if the player.
     * 
     * @return the {@link IIGGameInterface} if the player
     */
    public IIGGameInterface getInterface() {
        return gameInterface;
    }

    /**
     * Return the pseudo.
     * 
     * @return the pseudo
     */
    public String getPseudo() {
        return pseudo;
    }
}
