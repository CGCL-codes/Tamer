package joelib2.molecule.types;

/**
 * Virtual bond.
 *
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.3 $, $Date: 2005/02/17 16:48:37 $
 */
public class BasicVirtualBond implements VirtualBond {

    /**
     *  Description of the Field
     */
    protected int beginAtom;

    /**
     *  Description of the Field
     */
    protected int endAtom;

    /**
     *  Description of the Field
     */
    protected int order;

    /**
     *  Description of the Field
     */
    protected int stereo;

    /**
     *  Constructor for the JOEVirtualBond object
     */
    public BasicVirtualBond() {
        this(0, 0, 0, 0);
    }

    /**
     *  Constructor for the JOEVirtualBond object
     *
     * @param  bgn  Description of the Parameter
     * @param  end  Description of the Parameter
     * @param  ord  Description of the Parameter
     */
    public BasicVirtualBond(int bgn, int end, int ord) {
        this(bgn, end, ord, 0);
    }

    /**
     *  Constructor for the JOEVirtualBond object
     *
     * @param  bgn     Description of the Parameter
     * @param  end     Description of the Parameter
     * @param  ord     Description of the Parameter
     * @param  stereo  Description of the Parameter
     */
    public BasicVirtualBond(int bgn, int end, int ord, int stereo) {
        beginAtom = bgn;
        endAtom = end;
        order = ord;
        this.stereo = stereo;
    }

    /**
     *  Gets the bgn attribute of the JOEVirtualBond object
     *
     * @return    The bgn value
     */
    public int getBeginAtom() {
        return beginAtom;
    }

    /**
     *  Gets the end attribute of the JOEVirtualBond object
     *
     * @return    The end value
     */
    public int getEndAtom() {
        return endAtom;
    }

    /**
     *  Gets the order attribute of the JOEVirtualBond object
     *
     * @return    The order value
     */
    public int getOrder() {
        return order;
    }

    /**
     *  Gets the stereo attribute of the JOEVirtualBond object
     *
     * @return    The stereo value
     */
    public int getStereo() {
        return stereo;
    }

    /**
     * @param beginAtom The beginAtom to set.
     */
    public void setBeginAtom(int beginAtom) {
        this.beginAtom = beginAtom;
    }

    /**
     * @param endAtom The endAtom to set.
     */
    public void setEndAtom(int endAtom) {
        this.endAtom = endAtom;
    }

    /**
     * @param order The order to set.
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * @param stereo The stereo to set.
     */
    public void setStereo(int stereo) {
        this.stereo = stereo;
    }
}
