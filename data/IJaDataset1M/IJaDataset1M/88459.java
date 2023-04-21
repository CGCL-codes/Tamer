package javarequirementstracer;

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.DefaultResourceLoader;

/**
 * @author Ronald Koster
 */
abstract class AbstractScanner {

    private final TraceProperties properties;

    AbstractScanner(TraceProperties properties) {
        this.properties = properties;
    }

    TraceProperties getProperties() {
        return this.properties;
    }

    boolean isTestCode(final Class<?> cl) {
        final int index = cl.getName().lastIndexOf('.');
        final String name = cl.getName().substring(index + 1);
        return name.startsWith("Test") || name.endsWith("Test") || name.endsWith("TestCase") || name.contains("Test$") || name.contains("TestCase$");
    }

    boolean exclude(final Class<?> cl) {
        if (!this.properties.isIncludeTestCode() && isTestCode(cl)) {
            return true;
        }
        for (String excludePackageName : this.properties.getExcludePackageNames()) {
            if (cl.getName().startsWith(excludePackageName)) {
                return true;
            }
        }
        for (String excludeTypeName : this.properties.getExcludeTypeNames()) {
            if (cl.getName().equals(excludeTypeName)) {
                return true;
            }
        }
        return false;
    }

    void setResourceLoader(ClassPathScanningCandidateComponentProvider classPathScanner) {
        if (this.properties.getClassLoader() != null) {
            classPathScanner.setResourceLoader(new DefaultResourceLoader(properties.getClassLoader()));
        }
    }

    ClassLoader getClassLoader() {
        return this.properties.getClassLoader();
    }
}
