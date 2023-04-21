package de.grogra.ext.pdb.model;

/**
 * @author fdill, mdube
 * This class models the pdb ATOM entry.
 * atomName corresponds to the Atom name entry in the pdb file
 * element is an identifier to which element the atom belongs to
 * xCoord, yCoord, zCoord corresponds
 */
public class Atom {

    /**
	 * 
	 * @uml.property name="atomName"
	 */
    String atomName;

    /**
	 * 
	 * @uml.property name="xCoord"
	 */
    float xCoord;

    /**
	 * 
	 * @uml.property name="yCoord"
	 */
    float yCoord;

    /**
	 * 
	 * @uml.property name="zCoord"
	 */
    float zCoord;

    /**
	 * a counter for statistics. Is incremented with every creation.
	 */
    public static int numberOfAtoms = 0;

    /**
	 * Creates an atom
	 * @param name - the name of the atom taken from the PDB entry
	 * @param x - the x coordinate
	 * @param y - the y coordinate
	 * @param z - the z coordinate
	 */
    public Atom(String name, float x, float y, float z) {
        numberOfAtoms++;
        atomName = name;
        xCoord = x;
        yCoord = y;
        zCoord = z;
    }

    /**
	 * @return the name of the atom
	 * 
	 * @uml.property name="atomName"
	 */
    public String getAtomName() {
        return atomName;
    }

    /**
	 * The first character of the atom name indicates the element this atom belongs to
	 * @return the basic element
	 */
    public String getElement() {
        return Character.toString(atomName.charAt(0));
    }

    /**
	 * @return - the x coordinate
	 * 
	 * @uml.property name="xCoord"
	 */
    public float getXCoord() {
        return xCoord;
    }

    /**
	 * @return - the y coordinate
	 * 
	 * @uml.property name="yCoord"
	 */
    public float getYCoord() {
        return yCoord;
    }

    /**
	 * @return - the z coordinate
	 * 
	 * @uml.property name="zCoord"
	 */
    public float getZCoord() {
        return zCoord;
    }

    /**
	 * @param string
	 * 
	 * @uml.property name="atomName"
	 */
    public void setAtomName(String string) {
        atomName = string;
    }

    /**
	 * @param float1
	 * 
	 * @uml.property name="xCoord"
	 */
    public void setXCoord(float float1) {
        xCoord = float1;
    }

    /**
	 * @param float1
	 * 
	 * @uml.property name="yCoord"
	 */
    public void setYCoord(float float1) {
        yCoord = float1;
    }

    /**
	 * @param float1
	 * 
	 * @uml.property name="zCoord"
	 */
    public void setZCoord(float float1) {
        zCoord = float1;
    }

    @Override
    public String toString() {
        return "name: " + this.atomName + " x:" + this.getXCoord() + " y:" + this.getYCoord() + " z:" + this.getZCoord();
    }
}
