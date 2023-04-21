package net.sourceforge.metrics.internal.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.sourceforge.metrics.core.IExporter;
import net.sourceforge.metrics.core.MetricsPlugin;
import net.sourceforge.metrics.core.sources.AbstractMetricSource;
import net.sourceforge.metrics.core.sources.Cache;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;

/**
 * Create an XML report of the metrics
 * 
 * @author Frank Sauer
 */
public class XMLSourceTreeExporter implements IExporter {

    private String handle;

    public XMLSourceTreeExporter() {
    }

    /**
	 * @param monitor
	 */
    public void export(IJavaElement element, File outputFile, IProgressMonitor monitor) throws InvocationTargetException {
        try {
            this.handle = element.getHandleIdentifier();
            FileOutputStream out = new FileOutputStream(outputFile);
            XMLPrintStream pOut = new XMLPrintStream(out);
            pOut.printXMLHeader();
            pOut.println("<Metrics>");
            monitor.beginTask("Exporting metrics to XML...", calculateTotalWork(handle));
            printDescriptions(pOut, monitor);
            AbstractMetricSource root = Cache.singleton.get(element);
            IXMLExporter exporter = root.getExporter();
            exporter.export(root, pOut, 1, monitor);
            monitor.worked(1);
            pOut.println("</Metrics>");
            pOut.close();
        } catch (Throwable e) {
            throw new InvocationTargetException(e);
        }
    }

    /**
	 * @param pOut
	 */
    private void printDescriptions(XMLPrintStream pOut, IProgressMonitor monitor) {
        MetricsPlugin plugin = MetricsPlugin.getDefault();
        String[] names = plugin.getMetricIds();
        String[] descriptions = plugin.getMetricDescriptions();
        pOut.indent(1);
        pOut.println("<MetricDescriptions>");
        for (int i = 0; i < names.length; i++) {
            pOut.indent(2);
            StringBuffer b = new StringBuffer("<MetricDescription ");
            b.append("id = \"").append(names[i]).append("\" ");
            b.append("description =\"").append(descriptions[i]).append("\"/>");
            pOut.println(b.toString());
            monitor.worked(1);
        }
        pOut.indent(1);
        pOut.println("</MetricDescriptions>");
    }

    private int calculateTotalWork(String handle) {
        int result = 0;
        Set<String> handles = Cache.singleton.getKeysForHandle(handle);
        for (Iterator<String> i = handles.iterator(); i.hasNext(); ) {
            String next = i.next();
            if (next.startsWith(handle)) {
                result++;
            }
        }
        MetricsPlugin plugin = MetricsPlugin.getDefault();
        return result + plugin.getMetricIds().length;
    }

    protected static List<AbstractMetricSource> getChildren(String handle, Class<? extends AbstractMetricSource> filter) {
        Set<String> handles = Cache.singleton.getKeysForHandle(handle);
        List<AbstractMetricSource> result = new ArrayList<AbstractMetricSource>();
        for (Iterator<String> i = handles.iterator(); i.hasNext(); ) {
            String next = i.next();
            if (next.startsWith(handle) && (!next.equals(handle))) {
                AbstractMetricSource p = Cache.singleton.get(next);
                if (filter.isInstance(p)) {
                    result.add(p);
                }
            }
        }
        return result;
    }
}
