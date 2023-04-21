package net.sf.mzmine.modules.peaklistmethods.identification.mascot.data;

public class ModificationPeptide {

    private String name;

    private double mass;

    private boolean fixed;

    private char site;

    /**
	 * This class represents a modification for any amino acid in the sequence of a peptide.
	 * 
	 * @param name
	 * @param mass
	 * @param fixed
	 */
    public ModificationPeptide(String name, double mass, char amino, boolean fixed) {
        this.name = name;
        this.mass = mass;
        this.fixed = fixed;
    }

    /**
	 * Returns the name of the modification
	 * 
	 * @return name
	 */
    public String getName() {
        return name;
    }

    /**
	 * Returns true if the modification is fixed (according with Mascot definition)
	 * 
	 * @return boolean
	 */
    public boolean isFixed() {
        return fixed;
    }

    /**
	 * Return the mass value of this modification
	 * 
	 * @return mass
	 */
    public double getMass() {
        return mass;
    }

    /**
	 * Returns the site (amino acid) where this modification could happen.
	 * 
	 * @return char
	 */
    public char getSite() {
        return site;
    }
}
