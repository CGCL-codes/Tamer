package checker.gui.filter;

import checker.license.License;

/**
 * License criterion
 * Used to filter results based on a specific license
 *  
 * @author Veli-Jussi Raitila
 * 
 */
public class LicenseCriterion extends Criterion {

    License license;

    public LicenseCriterion(License l) {
        license = l;
    }

    @Override
    public Object getValue() {
        return license;
    }

    @Override
    public String toString() {
        return license.getId();
    }
}
