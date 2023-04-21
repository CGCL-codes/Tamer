package com.karma.shared;

/**
 * 
 * @author rajarshi
 *
 * The pieces class to extend more versatility like rotating blocks found in Coloroid v2.0 
 */
public class Pieces {

    /**
	 * The colorMutable field is used to mark pieces that has already been transformed into one color and thus mutates into
	 * colors selected by the player  
	 */
    public boolean colorMutable = false;

    public Object colorType;

    public Pieces(Object cType) {
        this.colorType = cType;
    }
}
