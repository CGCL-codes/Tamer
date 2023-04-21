package net.sourceforge.metrics.core.sources;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sourceforge.metrics.core.ICalculator;
import net.sourceforge.metrics.core.Log;
import net.sourceforge.metrics.core.MetricsPlugin;
import net.sourceforge.metrics.internal.xml.IXMLExporter;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;

/**
 * initialize package fragment root metrics (my children) and collect all results from the calculators for the project root level
 * 
 * @author Frank Sauer
 */
public class ProjectMetrics extends AbstractMetricSource implements IGraphContributor {

    static final long serialVersionUID = 7999821409641083884L;

    private Map<String, Set<String>> efferent;

    public ProjectMetrics() {
        super();
    }

    @Override
    protected void initializeChildren(AbstractMetricSource parentMetric) {
        IJavaProject pack = (IJavaProject) getJavaElement();
        try {
            IJavaElement[] children = pack.getChildren();
            for (int i = 0; i < children.length; i++) {
                if (children[i] instanceof IPackageFragmentRoot) {
                    if (!((IPackageFragmentRoot) children[i]).isArchive()) {
                        AbstractMetricSource next = Dispatcher.getAbstractMetricSource(children[i]);
                        if (next != null) {
                            addChild(next);
                        } else {
                            Log.logMessage("Package " + children[i].getHandleIdentifier() + " not found.");
                        }
                    }
                }
            }
        } catch (JavaModelException e) {
        }
    }

    @Override
    public void recurse(AbstractMetricSource parentMetric) {
        initializeChildren(parentMetric);
        calculate();
        save();
    }

    /**
	 * @see net.sourceforge.metrics.core.sources.AbstractMetricSource#getCalculators()
	 */
    @Override
    protected List<ICalculator> getCalculators() {
        return MetricsPlugin.getDefault().getCalculators("project");
    }

    /**
	 * @see net.sourceforge.metrics.core.sources.AbstractMetricSource#getLevel()
	 */
    @Override
    public int getLevel() {
        return PROJECT;
    }

    @Override
    public IXMLExporter getExporter() {
        return IXMLExporter.PROJECT_EXPORTER;
    }

    @Override
    public void calculate() {
        super.calculate();
        mergeEfferentCouplings();
    }

    /**
	 * merge the efferent dependency graphs of my child sourcefolders into one
	 */
    private void mergeEfferentCouplings() {
        efferent = new HashMap<String, Set<String>>();
        for (Object element : getChildren()) {
            PackageFragmentRootMetrics next = (PackageFragmentRootMetrics) element;
            Map<String, Set<String>> eff = next.getEfferent();
            addEfferent(eff);
        }
    }

    /**
	 * @param eff
	 *            dependencies of a single source folder
	 */
    private void addEfferent(Map<String, Set<String>> eff) {
        for (String key : eff.keySet()) {
            Set<String> deps = eff.get(key);
            if (deps != null) {
                Set<String> total = efferent.get(key);
                if (total == null) {
                    total = new HashSet<String>();
                    efferent.put(key, total);
                }
                total.addAll(deps);
            }
        }
    }

    public Map<String, Set<String>> getEfferent() {
        return efferent;
    }

    @Override
    public ASTNode getASTNode() {
        return null;
    }
}
