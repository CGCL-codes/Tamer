package org.fest.swing.junit.ant;

import org.apache.tools.ant.taskdefs.optional.junit.AggregateTransformer;
import org.apache.tools.ant.taskdefs.optional.junit.XMLResultAggregator;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

/**
 * Aggregates all &lt;junit&gt; XML formatter testsuite data under a specific directory and transforms the results via
 * XSLT.
 * 
 * @author Alex Ruiz
 */
public class JUnitReportTask extends XMLResultAggregator {

    private Path classpath;

    /**
   * Generate a report based on the document created by the merge.
   * @return the generated report.
   */
    @SuppressWarnings("unchecked")
    @Override
    public AggregateTransformer createReport() {
        ReportTransformer transformer = new ReportTransformer(this);
        transformer.setClasspath(classpath);
        transformers.addElement(transformer);
        return transformer;
    }

    /**
   * Sets an additional classpath.
   * @param classpath the additional classpath to append to the current one.
   */
    public void setClasspath(Path classpath) {
        createClasspath().append(classpath);
    }

    /**
   * Sets a reference to a classpath.
   * @param r the reference to set.
   */
    public void setClasspathRef(Reference r) {
        createClasspath().setRefid(r);
    }

    /**
   * Creates the current classpath.
   * @return the created classpath.
   */
    public Path createClasspath() {
        if (classpath == null) classpath = new Path(getProject());
        return classpath.createPath();
    }
}
