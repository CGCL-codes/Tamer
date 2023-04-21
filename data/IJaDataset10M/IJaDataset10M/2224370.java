package org.aigebi.analytics;

import java.util.HashMap;

/**Crosstab node.
 * @author Ligong Xu
 * @version $Id: Categorical.java 1 2007-09-22 18:10:03Z ligongx $
 */
public interface Categorical {

    public String getKey();

    public String getDisplayName();

    public Categorical getParentCategorical();

    public HashMap<String, Categorical> getChildCategoricalMap();

    public void setChildCategoricalMap(HashMap<String, Categorical> childCats);
}
