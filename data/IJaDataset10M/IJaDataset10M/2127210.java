package controller;

/**
 *
 * @author mathieuhausherr
 */
public class PlayerAction {

    public Integer id;

    public String nom;

    public Type type;

    public static enum Type {

        NEW, QUIT
    }

    ;

    public PlayerAction(Integer id, String nom, Type type) {
        this.id = id;
        this.nom = nom;
        this.type = type;
    }
}
