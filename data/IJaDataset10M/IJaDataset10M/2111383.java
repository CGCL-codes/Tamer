package org.proclos.etlcore.config.source;

import java.util.List;
import org.jdom.Element;
import org.proclos.etlcore.config.ConfigurationException;
import org.proclos.etlcore.source.filter.DimensionFilter;

/**
 * 
 * @author Christian Schwarzinger. Mail: christian.schwarzinger@proclos.com
 *
 */
public class OLAPDimensionSourceConfigurator extends TreeSourceConfigurator {

    private String dimName;

    public void setQuery(DimensionFilter dimensionFilter) {
        Element query = getXML().getChild("query");
        Element dimension = query.getChild("dimension");
        if (dimension != null) {
            String leafs = dimension.getAttributeValue("leafs");
            List<?> filters = dimension.getChildren();
            for (int j = 0; j < filters.size(); j++) {
                Element dimFilter = (Element) filters.get(j);
                String parent = resolve(dimFilter.getAttributeValue("parent"));
                String regexp = resolve(dimFilter.getAttributeValue("element", "."));
                dimensionFilter.filter(dimFilter.getName(), parent, regexp);
            }
            if (leafs != null) {
                if (leafs.equals("restrict")) dimensionFilter.restrictToLeafs();
                if (leafs.equals("include")) dimensionFilter.includeLeafs();
                if (leafs.equals("exclude")) dimensionFilter.excludeLeafs();
            }
        }
    }

    protected void setDimension() {
        Element dimension = null;
        Element query = getXML().getChild("query");
        if (query != null) dimension = query.getChild("dimension");
        dimName = getName();
        if (dimension != null) dimName = dimension.getAttributeValue("name");
    }

    public String getDimensionName() {
        return resolve(dimName);
    }

    public void configure() throws ConfigurationException {
        super.configure();
        setDimension();
    }
}
