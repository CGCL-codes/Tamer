package com.interworldtransport.clados;

/**
 * All math objects within the clados package have a few things in common.  They
 * are all named objects from algebras that have names and foot points of their
 * own.  This class encapsulates the common areas, but it not instantiable on 
 * its own.
 * 
 * @version 0.80, $Date: 2005/09/29 08:36:20 $
 * @author Dr Alfred W Differ
 */
public abstract class CladosObject {

    /**
 * All objects of this class have a name independent of all other features.
 */
    private String Name;

    /**
 * All clados objects are elements of some algebra.  That algebra has a name.
 */
    private String AlgebraName;

    /** 
 * This String is the name the Foot of the Reference Frame of the Monad
 */
    private String FootName;

    public String getName() {
        return this.Name;
    }

    public void setName(String pName) {
        this.Name = new String(pName);
    }

    public String getAlgebraName() {
        return this.AlgebraName;
    }

    public void setAlgebraName(String pName) {
        this.AlgebraName = new String(pName);
    }

    public String getFootName() {
        return this.FootName;
    }

    public void setFootName(String pName) {
        this.FootName = new String(pName);
    }
}
