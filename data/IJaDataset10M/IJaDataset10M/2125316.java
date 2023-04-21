package org.eyrene.jgames.core;

import java.util.HashMap;

/**
 * <p>Title: PieceFactory.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: eyrene</p>
 * @author Francesco Vadicamo
 * @version 1.0
 */
public class PieceFactory {

    private static PieceFactory instance = null;

    private static HashMap singletons = new HashMap(256, 0.51f);

    public static PieceFactory getInstance() {
        if (instance == null) instance = new PieceFactory();
        return instance;
    }

    private PieceFactory() {
    }

    public static Piece createPiece(int id, String name) {
        return new Piece(id, name);
    }

    public static Piece singletonPiece(Object key, int id, String name) {
        Object value = singletons.get(key);
        if (value == null) {
            value = new Piece(id, name);
            singletons.put(key, value);
        }
        return (Piece) value;
    }

    public static Piece getSingletonPiece(Object key) {
        return (Piece) singletons.get(key);
    }
}
