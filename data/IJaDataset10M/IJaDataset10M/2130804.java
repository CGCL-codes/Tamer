package org.proclos.etlcore.config.source;

import java.util.List;
import org.jdom.Element;
import org.proclos.etlcore.config.ConfigurationException;
import org.proclos.etlcore.source.filter.PaloCubeFilter;
import org.proclos.etlcore.source.filter.PaloDimensionFilter;

/**
 * 
 * @author Christian Schwarzinger. Mail: christian.schwarzinger@proclos.com
 *
 */
public class CubeSourceConfigurator extends TableSourceConfigurator {

    private String cubeName;

    private String valueName = "Value";

    public void setQuery(PaloCubeFilter paloCubeFilter) {
        Element query = getXML().getChild("query");
        if (query != null) {
            List<?> dimensions = query.getChildren("dimension");
            for (int i = 0; i < dimensions.size(); i++) {
                Element dimension = (Element) dimensions.get(i);
                String name = resolve(dimension.getAttributeValue("name"));
                PaloDimensionFilter paloDimensionFilter = paloCubeFilter.addDimensionFilter(name);
                if (paloDimensionFilter != null) {
                    String leafs = resolve(dimension.getAttributeValue("leafs"));
                    List<?> filters = dimension.getChildren();
                    for (int j = 0; j < filters.size(); j++) {
                        Element dimFilter = (Element) filters.get(j);
                        String parent = resolve(dimFilter.getAttributeValue("parent"));
                        String regexp = resolve(dimFilter.getAttributeValue("element", "."));
                        paloDimensionFilter.filterRegexp(dimFilter.getName(), parent, regexp);
                    }
                    if (leafs != null) {
                        if (leafs.equals("restrict")) paloDimensionFilter.restrictToLeafs();
                        if (leafs.equals("include")) paloDimensionFilter.includeLeafs();
                        if (leafs.equals("exclude")) paloDimensionFilter.excludeLeafs();
                    }
                }
            }
        }
    }

    protected void setCube() {
        Element cube = null;
        Element query = getXML().getChild("query");
        if (query != null) cube = query.getChild("cube");
        cubeName = getName();
        if (cube != null) {
            cubeName = cube.getAttributeValue("name");
            valueName = cube.getAttributeValue("value", valueName);
        }
    }

    public String getCubeName() {
        return resolve(cubeName);
    }

    public String getValueName() {
        return resolve(valueName);
    }

    public void configure() throws ConfigurationException {
        super.configure();
        setCube();
    }
}
