package net.cblicher.mvxconnectorj.b.mm.dao;

import net.cblicher.mvxconnectorj.b.MovexSession;

/**
 * Data object class for the Movex table <code>Mitaun</code>.
 * 
 * @version 1.0 2010/12/5
 * @author Christian Blicher
 */
public class Mitaun extends DefaultMitaun {

    /**
	 * Creates a new <code>Mitaun</code> class type object.
	 * 
	 * @param movexSession
	 */
    public Mitaun(MovexSession movexSession) {
        super(movexSession);
    }
}
