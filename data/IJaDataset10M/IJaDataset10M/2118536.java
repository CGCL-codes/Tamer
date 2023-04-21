package net.sf.gridarta.model.io;

import java.io.File;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link CacheFiles} implementation that stores all files in sub-directories
 * right next to the original files.
 * @author Andreas Kirschbaum
 */
public class SubDirectoryCacheFiles implements CacheFiles {

    /**
     * The name of the sub-directories.
     */
    @NotNull
    private final String directory;

    /**
     * Creates a new instance.
     * @param directory the name of the sub-directories
     */
    public SubDirectoryCacheFiles(@NotNull final String directory) {
        this.directory = directory;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public File getCacheFile(@NotNull final File file, @Nullable final String prefix) {
        final String name = file.getName();
        return new File(new File(file.getParent(), directory), prefix == null ? name : name + "." + prefix);
    }
}
