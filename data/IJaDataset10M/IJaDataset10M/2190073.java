package net.sf.xdc.processing;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.io.File;
import net.sf.xdc.util.PathDescriptor;

/**
 * The <code>PatternSelector</code> class is an extension to the
 * {@link FileSelector} class which allows to additionally restrict the set
 * of selected files by specifying one or more {@link PathDescriptor}s
 * (wildcard expressions).
 *
 * @author Jens Vo�
 * @since 0.5
 * @version 0.5
 */
public class PatternSelector extends FileSelector {

    private PathDescriptor[] descriptors;

    private Map fileMap;

    /**
   * Public constructor.
   *
   * @param dir This <code>PatternSelector</code>'s directory
   * @param descriptors An array of wildcard expressions for this
   *         <code>PatternSelector</code>
   */
    PatternSelector(File dir, PathDescriptor[] descriptors) {
        super(dir, null, false);
        this.descriptors = descriptors;
    }

    /**
   * This method retrieves the collection of all selected files within this
   * <code>PatternSelector</code>'s directory which match the wildcard
   * expression of one or more of its <code>PathDescriptor</code> objects.
   *
   * @return An array of files and directories in this
   *          <code>PatternSelector</code>'s directory which match the wildcard
   *          pattern of at least one of its path descriptors
   */
    protected File[] selectFiles() {
        if (fileMap == null) {
            fileMap = new HashMap();
            for (int i = 0; i < descriptors.length; i++) {
                PathDescriptor descriptor = descriptors[i];
                File[] files = getDir().listFiles(descriptor);
                for (int j = 0; j < files.length; j++) {
                    File file = files[j];
                    Set childDescriptors = (Set) fileMap.get(file);
                    if (childDescriptors == null) {
                        childDescriptors = new HashSet();
                        fileMap.put(file, childDescriptors);
                    }
                    childDescriptors.add(descriptor.getChildDescriptor());
                    if (descriptor.matchesDirWithWildcard(file)) {
                        childDescriptors.add(descriptor);
                    }
                }
            }
        }
        Set files = fileMap.keySet();
        return (File[]) files.toArray(new File[files.size()]);
    }

    /**
   * This method constructs a <code>PatternSelector</code> for a subdirectory of
   * the current directory.
   *
   * @param subdirName The name of the subdirectory for which a new
   *         <code>FileSelector</code> is to be constructed
   * @return A new <code>PatternSelector</code> whose directory is the
   *          subdirectory with the specified name
   */
    protected FileSelector moveToSubdir(String subdirName) {
        File subdir = new File(getDir(), subdirName);
        Set children = (Set) fileMap.get(subdir);
        PathDescriptor[] childDescriptors = (PathDescriptor[]) children.toArray(new PathDescriptor[children.size()]);
        return new PatternSelector(subdir, childDescriptors);
    }
}
