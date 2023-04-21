package joelib2.process;

/**
 * Molecule process exception.
 *
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.3 $, $Date: 2005/02/17 16:48:38 $
 */
public class MoleculeProcessException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     *  Constructor for the DescriptorException object
     */
    public MoleculeProcessException() {
        super();
    }

    /**
     *  Constructor for the DescriptorException object
     *
     * @param  s  Description of the Parameter
     */
    public MoleculeProcessException(String s) {
        super(s);
    }
}
