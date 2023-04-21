package org.simpleframework.chart;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

/**
<definition>
  <define>
    <variable name="path">c:\</variable>
    <variable name="type">image</variable>    
  </define>  
  <chart title="Graph Title" xAxis="X" yAxis="Y" height="200" width="200" graphFile="c:\\file.png">
    <plot source="c:\\file.csv" x="Sample" y="Throughput"/>
    <plot source="c:\\file2.csv" x="Sample" y="Throughput"/>    
  </chart>
  <chart title="Graph Title" xAxis="X" yAxis="Y" height="200" width="200" graphFile="c:\\file.png">
    <plot source="c:\\file.csv" x="Sample" y="Throughput"/>
    <plot source="c:\\file2.csv" x="Sample" y="Throughput"/>    
  </chart>
</definition>
 */
@Root(name = "charts")
public class PlotXmlSourceList implements PlotSourceList {

    @ElementList(inline = true, type = PlotXmlSource.class)
    List<PlotSource> list;

    /**
   * Ensure the same output is not used for two different plots.
   * This would overwrite the file which is not desired.
   */
    @Validate
    private void validate() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        for (PlotSource source : list) {
            File graphFile = source.getGraphFile();
            String path = graphFile.getCanonicalPath();
            String title = source.getTitle();
            if (map.containsKey(path)) {
                throw new IllegalStateException("Graph file " + path + " has already been specified for graph " + map.get(path));
            }
            map.put(path, title);
        }
    }

    /**
   * Get all the plots that are required.
   */
    public List<PlotSource> getPlotSources() {
        return list;
    }
}
