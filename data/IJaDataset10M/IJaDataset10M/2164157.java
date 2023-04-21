package org.impalaframework.module.source;

import java.net.URL;
import java.util.List;
import org.impalaframework.classloader.CustomClassLoader;
import org.impalaframework.classloader.ModuleClassLoader;
import org.impalaframework.classloader.NonDelegatingResourceClassLoader;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

public class ModuleResourceUtils {

    public static URL loadModuleResource(ModuleLocationResolver moduleLocationResolver, String moduleName, String resourceName) {
        List<Resource> locations = moduleLocationResolver.getApplicationModuleClassLocations(moduleName);
        CustomClassLoader cl = new ModuleClassLoader(ClassUtils.getDefaultClassLoader(), ResourceUtils.getFiles(locations));
        NonDelegatingResourceClassLoader ndl = new NonDelegatingResourceClassLoader(cl);
        URL resource = ndl.getResource(resourceName);
        return resource;
    }
}
