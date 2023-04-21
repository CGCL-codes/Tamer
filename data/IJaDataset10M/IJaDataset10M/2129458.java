package net.stevechaloner.intellijad.decompilers;

import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;

/**
 * Decompilation descriptor for jar-based classes.
 * 
 * @author Steve Chaloner
 */
public class JarDecompilationDescriptor extends DecompilationDescriptor {

    @NotNull
    private String pathToJarFile;

    @NotNull
    private VirtualFile jarFile;

    /**
     * Initialises a new instance of this class.
     *
     * @param classFile the class to decompile
     * @param fqName the fully-qualified name of the class
     * @param fqNameAsPath the fully-qualified name of the class as a path
     * @param packageName the package name
     * @param packageNameAsPath the package name as a path, i.e. with / instead of .
     * @param jarFile the jar file the class is in
     */
    JarDecompilationDescriptor(@NotNull VirtualFile classFile, @NotNull String fqName, @NotNull String fqNameAsPath, @NotNull String packageName, @NotNull String packageNameAsPath, @NotNull VirtualFile jarFile) {
        super(classFile, fqName, fqNameAsPath, packageName, packageNameAsPath);
        this.jarFile = jarFile;
        this.pathToJarFile = jarFile.getPath();
    }

    @Nullable
    public VirtualFile getJarFile() {
        return jarFile;
    }

    @Nullable
    public String getPathToJarFile() {
        return pathToJarFile;
    }

    @Nullable
    public ClassPathType getClassPathType() {
        return ClassPathType.JAR;
    }

    /** {@inheritDoc} */
    @NotNull
    public File getSourceFile(@NotNull File availableDirectory) {
        return new File(availableDirectory, getClassName() + '.' + getExtension());
    }
}
