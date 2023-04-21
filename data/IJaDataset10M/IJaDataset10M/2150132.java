package joelib.process.filter;

import org.apache.log4j.Category;
import joelib.molecule.JOEMol;

/**
 * Molecule process filter NOT condition.
 *
 * @author     wegnerj
 * @license GPL
 * @cvsversion    $Revision: 1.7 $, $Date: 2004/02/20 13:12:01 $
 */
public class NOTFilter implements Filter {

    private static Category logger = Category.getInstance("joelib.process.filter.NOTFilter");

    private Filter filter;

    private FilterInfo info;

    /**
     *  Constructor for the DescriptorFilter object
     */
    public NOTFilter() {
    }

    /**
     *  Constructor for the DescriptorFilter object
     *
     * @param  descNamesURL  Description of the Parameter
     */
    public NOTFilter(Filter _filter) {
        init(_filter);
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
        if (filter == null) {
            logger.warn("Filter not defined in " + this.getClass().getName() + ".");
            return false;
        }
        return (!filter.accept(mol));
    }

    /**
     *  Description of the Method
     *
     * @param  _descNames  Description of the Parameter
     */
    public void init(Filter _filter) {
        filter = _filter;
    }
}
