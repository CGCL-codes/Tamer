package net.stevechaloner.intellijad.decompilers;

import com.intellij.openapi.vfs.VirtualFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.stevechaloner.intellijad.IntelliJadConstants;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for creating {@link FileSystemDecompilationDescriptor}s based on a virtual file representing the target class.
 *
 * @author Steve Chaloner
 */
class FileSystemDecompilationDescriptorFactory extends DecompilationDescriptorFactory {

    /**
     * The pattern for a package declaration within a class.
     */
    private static final Pattern PACKAGE_PATTERN = Pattern.compile("package [\\w|\\.]*;");

    /** {@inheritDoc} */
    @NotNull
    public DecompilationDescriptor create(@NotNull VirtualFile target) {
        return new FileSystemDecompilationDescriptor(target);
    }

    /** {@inheritDoc} */
    public void update(@NotNull DecompilationDescriptor dd, @NotNull String classContent) {
        Matcher packageNameMatcher = PACKAGE_PATTERN.matcher(classContent);
        if (packageNameMatcher.find()) {
            String packageName = classContent.substring("package ".length() + packageNameMatcher.start(), packageNameMatcher.end() - 1);
            dd.setPackageName(packageName);
            String asPath = packageName.replaceAll("\\.", "/");
            if (!asPath.endsWith("/")) {
                asPath = asPath + '/';
            }
            dd.setPackageNameAsPath(asPath);
            dd.setFqName(packageName + '.' + dd.getClassName());
            dd.setFqNameAsPath(asPath + dd.getClassName() + IntelliJadConstants.DOT_JAVA_EXTENSION);
        } else {
            dd.setPackageName("");
            dd.setPackageNameAsPath("");
            dd.setFqName(dd.getClassName());
            dd.setFqNameAsPath(dd.getClassName() + IntelliJadConstants.DOT_JAVA_EXTENSION);
        }
    }
}
