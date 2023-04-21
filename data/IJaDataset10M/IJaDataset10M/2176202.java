package joelib.process.filter;

import org.apache.log4j.Category;
import joelib.molecule.JOEMol;

/**
 * Molecule process filter AND condition.
 *
 * @author     wegnerj
 * @license GPL
 * @cvsversion    $Revision: 1.9 $, $Date: 2004/07/25 20:43:24 $
 */
public class ANDFilter implements Filter {

    private static Category logger = Category.getInstance("joelib.process.filter.ANDFilter");

    private Filter filter1;

    private Filter filter2;

    private FilterInfo info;

    /**
     *  Constructor for the DescriptorFilter object
     */
    public ANDFilter() {
    }

    /**
     *  Constructor for the DescriptorFilter object
     *
     * @param  descNamesURL  Description of the Parameter
     */
    public ANDFilter(Filter _filter1, Filter _filter2) {
        init(_filter1, _filter2);
    }

    /**
     *  Sets the filterInfo attribute of the DescriptorFilter object
     *
     * @param  _info  The new filterInfo value
     */
    public void setFilterInfo(FilterInfo _info) {
        info = _info;
    }

    /**
     *  Gets the processInfo attribute of the DescriptorFilter object
     *
     * @return    The processInfo value
     */
    public FilterInfo getFilterInfo() {
        return info;
    }

    /**
     *  Description of the Method
     *
     * @param  mol  Description of the Parameter
     * @return      Description of the Return Value
     */
    public boolean accept(JOEMol mol) {
        if ((filter1 == null) || (filter2 == null)) {
            logger.warn("Filters not defined in " + this.getClass().getName() + ".");
            return false;
        }
        return (filter1.accept(mol) && filter2.accept(mol));
    }

    /**
     *  Description of the Method
     *
     * @param  _descNames  Description of the Parameter
     */
    public void init(Filter _filter1, Filter _filter2) {
        filter1 = _filter1;
        filter2 = _filter2;
    }
}
