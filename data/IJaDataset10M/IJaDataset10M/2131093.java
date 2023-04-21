package gov.usda.gdpc;

/**
 * This is a taxon filter.
 *
 *
 * @author  terryc
 */
public class TaxonFilter extends AbstractFilter {

    private static final long serialVersionUID = 813L;

    /**
     * TaxonFilter constructor.
     */
    public TaxonFilter() {
    }

    /**
     * Adds a value for a particular taxon property for this filter.
     * Only filter values for taxon properties can be added.
     *
     * @param value value
     */
    public void addValue(FilterValue value) {
        if (value.getProperty() instanceof TaxonProperty) {
            super.addValue(value);
        } else {
            throw new IllegalStateException("TaxonFilter: addValue: filter value property must be instance of TaxonProperty");
        }
    }
}
