package org.osmorc.frameworkintegration;

import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import java.util.List;

/**
 * Author: Robert F. Beeger (robert@beeger.net)
 */
public interface FrameworkInstanceLibrarySourceFinder {

    List<VirtualFile> getSourceForLibraryClasses(@NotNull VirtualFile libraryClasses);

    /**
   * Is the file actually a source only file that does not contain any compiled classes?
   * @param libraryClassesCondidate The candidate file that may contain compiled classes
   * @return true if the file only contains sources and no compiled classes.
   */
    boolean containsOnlySources(@NotNull VirtualFile libraryClassesCondidate);
}
