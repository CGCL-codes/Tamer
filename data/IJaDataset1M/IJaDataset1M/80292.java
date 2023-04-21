package eu.pouvesle.nicolas.dpp.ms.filter;

import java.awt.List;
import eu.pouvesle.nicolas.dpp.ms.Data;
import eu.pouvesle.nicolas.dpp.ms.Scan;

/**
 * Abstract - Parent class for all filter function or methode.
 *
 * @author Nicolas Pouvesle
 * @version 1.00
 * @since dpp 1.07.00
 */
public abstract class FilterFunction {

    protected String name;

    /**
	 * Default Constructor.
	 *                                                               
	 * @param name Filter name. 
	 */
    public FilterFunction(String name) {
        this.name = name;
    }

    /**
	 * @return Returns the type name.
	 */
    public final String getName() {
        return name;
    }

    /**
	 * Set the filter.
	 *
	 * @param list List of parameters.
	 */
    public abstract void setFilter(List list);

    /**
	 * Get filtered scan.
	 *
	 * @param scan Scan to filter.
	 * @param id   New scan id.
	 * @return Filtered scan.
	 */
    public abstract Scan get(Scan scan, String id);

    /**
	 * Get filtered data set.
	 *
	 * @param data Data to filter.
	 * @param id   New scan id.
	 * @return Filtered scan.
	 */
    public abstract Data get(Data data, String id);
}
