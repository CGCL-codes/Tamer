package preprocessing.automatic.GUI.GraphExporters;

import org.jfree.chart.JFreeChart;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: lagon
 * Date: Nov 12, 2009
 * Time: 12:26:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GraphExporter {

    public void exportGraph(JFreeChart chart, String path, String filename) throws IOException;

    public String getFileExtension();
}
