package net.sf.hippopotam.framework.tree;

/**
 * <br>Author: Dmitry Ermakov dim_er@mail.ru
 * <br>Date: 15.05.2007
 */
public class UnloadedNodes {

    private static UnloadedNodes instance;

    public static UnloadedNodes getInstance() {
        if (instance == null) {
            instance = new UnloadedNodes();
        }
        return instance;
    }

    private UnloadedNodes() {
        super();
    }

    public String toString() {
        return "Unloaded nodes";
    }
}
