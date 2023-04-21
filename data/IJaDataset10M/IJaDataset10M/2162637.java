package com.lemu.music.transform;

import ren.io.Factory;
import ren.util.PO;

/**
 * if indymedia becomes big, would corporates put in releases the way they put in press releases
 * 
 * @author wooller
 *
 */
public class TFactory implements Factory {

    private static TFactory fact = new TFactory();

    /**
	 * 
	 */
    public TFactory() {
        super();
    }

    private static final Transformer[] tr = new Transformer[] { new Scope(), new Transpose(), new Rate(), new Quantise(), new Shuffle() };

    public static Transformer create(String type) {
        for (int i = 0; i < tr.length; i++) {
            if (tr[i].getType().equals(type)) {
                try {
                    return ((Transformer) (tr[i].getClass()).newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        PO.p("couldn't create the transformer type : " + type);
        return null;
    }

    public Object createObj(String type) {
        return create(type);
    }

    public static String[] getTypes() {
        String[] toRet = new String[tr.length];
        for (int i = 0; i < tr.length; i++) {
            toRet[i] = tr[i].getType();
        }
        return toRet;
    }

    public static Factory getInstance() {
        return fact;
    }
}
