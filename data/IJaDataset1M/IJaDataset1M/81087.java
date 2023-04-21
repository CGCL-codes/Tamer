package net.sourceforge.mazix.cli.utils.fileset;

import static net.sourceforge.mazix.cli.constants.log.ErrorConstants.COMMAND_LINE_ARGUMENTS_NULL_ERROR;
import static net.sourceforge.mazix.components.utils.check.ParameterCheckerUtils.checkNull;
import java.io.File;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;

/**
 * The class utilities simplifying some {@link FileSet} tasks.
 *
 * @author Benjamin Croizet (<a href="mailto:graffity2199@yahoo.fr>graffity2199@yahoo.fr</a>)
 *
 * @since 1.0
 * @version 1.0
 */
public abstract class FileSetUtils {

    /**
     * Creates a {@link FileSet} instance following passed parameters.
     *
     * @param baseDirectory
     *            the base directory, mustn't be <code>null</code>.
     * @param includePattern
     *            the include pattern, mustn't be <code>null</code>.
     * @return the {@link FileSet} instance following passed parameters.
     * @since 1.0
     */
    public static FileSet createFileSet(final File baseDirectory, final String includePattern) {
        checkNull(baseDirectory, COMMAND_LINE_ARGUMENTS_NULL_ERROR);
        checkNull(includePattern, COMMAND_LINE_ARGUMENTS_NULL_ERROR);
        final FileSet fileSet = new FileSet();
        final Project project = new Project();
        fileSet.setProject(project);
        fileSet.setDir(baseDirectory);
        fileSet.setIncludes(includePattern);
        return fileSet;
    }

    /**
     * Creates a {@link FileSet} instance following passed parameters.
     *
     * @param baseDirectory
     *            the base directory, mustn't be <code>null</code>.
     * @param includePattern
     *            the include pattern, mustn't be <code>null</code>.
     * @param excludePattern
     *            the exclude pattern, mustn't be <code>null</code>.
     * @return the {@link FileSet} instance following passed parameters.
     * @since 1.0
     */
    public static FileSet createFileSet(final File baseDirectory, final String includePattern, final String excludePattern) {
        checkNull(baseDirectory, COMMAND_LINE_ARGUMENTS_NULL_ERROR);
        checkNull(includePattern, COMMAND_LINE_ARGUMENTS_NULL_ERROR);
        checkNull(excludePattern, COMMAND_LINE_ARGUMENTS_NULL_ERROR);
        final FileSet fileSet = new FileSet();
        final Project project = new Project();
        fileSet.setProject(project);
        fileSet.setDir(baseDirectory);
        fileSet.setIncludes(includePattern);
        fileSet.setExcludes(excludePattern);
        return fileSet;
    }

    /**
     * Private constructor to prevent from instantiation.
     *
     * @since 1.0
     */
    private FileSetUtils() {
        super();
    }
}
