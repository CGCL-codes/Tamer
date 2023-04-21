package joelib2.util.ghemical;

/**
 * JOELib property exception.
 *
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.6 $, $Date: 2005/02/17 16:48:41 $
 */
public class GhemicalException extends Exception {

    /**
     *  Constructor for the DescriptorException object
     */
    public GhemicalException() {
        super();
    }

    /**
     *  Constructor for the DescriptorException object
     *
     * @param  s  Description of the Parameter
     */
    public GhemicalException(String s) {
        super(s);
    }
}
