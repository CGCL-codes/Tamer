package org.dwgsoftware.raistlin.composition.data;

import java.io.Serializable;

/**
 * <p>A fileset directive is a scoped defintion of a set of files.  A fileset
 * a structurally defined as a base directory and a set of relative filenames
 * represented as include directives and/or exclude directives.</p>
 *
 * <p><b>XML</b></p>
 * <pre>
 *   &lt;fileset dir="<font color="darkred">lib</font>"&gt;
 *     &lt;include name="<font color="darkred">avalon-framework.jar</font>"/&gt;
 *     &lt;include name="<font color="darkred">logkit.jar</font>"/&gt;
 *     &lt;exclude name="<font color="darkred">servlet.jar</font>"/&gt;
 *   &lt;/dirset&gt;
 * </pre>
 *
 * @see IncludeDirective
 * @see ExcludeDirective
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 * @version $Revision: 1.1 $ $Date: 2005/09/06 00:58:17 $
 */
public class FilesetDirective implements Serializable {

    /**
     * The base directory from which include directives will be resolved.
     */
    private final String m_base;

    /**
     * The set of include directives.
     */
    private final IncludeDirective[] m_includes;

    /**
     * The set of exclude directives.
     */
    private final ExcludeDirective[] m_excludes;

    /**
     * Create a FilesetDirective instance.
     *
     * @param base the base directory path against which includes are evaluated
     * @param includes the set of includes to include in the fileset
     */
    public FilesetDirective(final String base, final IncludeDirective[] includes) {
        this(base, includes, null);
    }

    /**
     * Create a FilesetDirective instance.
     *
     * @param base the base directory path against which includes are evaluated
     * @param includes the set of includes to include in the fileset
     */
    public FilesetDirective(final String base, final IncludeDirective[] includes, final ExcludeDirective[] excludes) {
        m_base = base;
        m_includes = includes;
        m_excludes = excludes;
    }

    /**
     * Return the base directory.
     *
     * @return the directory
     */
    public String getBaseDirectory() {
        return m_base;
    }

    /**
     * Return the set of include directives.
     *
     * @return the include set
     */
    public IncludeDirective[] getIncludes() {
        return m_includes;
    }

    /**
     * Return the set of exclude directives.
     *
     * @return the exclude set
     */
    public ExcludeDirective[] getExcludes() {
        return m_excludes;
    }
}
