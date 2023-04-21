package org.xmi.repository;

import java.util.Map;

/**
 * The model statistic element
 */
public interface ModelStatistic {

    /**
	 * the total count of all nodes
	 * @return
	 */
    public int getNodeCount();

    /**
	 * the number of element types
	 * @return
	 */
    public int getElementTypeCount();

    /**
	 * get a map with element types and their counter<p>
	 * the key is the type name and the integer the counter
	 * @return
	 */
    public Map<String, Integer> getElementTypes();
}
