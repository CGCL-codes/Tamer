package org.frameworkset.util.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.frameworkset.util.SystemPropertyUtils;
import com.frameworkset.util.EditorInf;

/**
 * <p>Title: ResourceArrayPropertyEditor.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-14
 * @author biaoping.yin
 * @version 1.0
 */
public class ResourceArrayPropertyEditor implements EditorInf<Resource[]> {

    private final ResourcePatternResolver resourcePatternResolver;

    /**
	 * Create a new ResourceArrayPropertyEditor with a default
	 * PathMatchingResourcePatternResolver.
	 * @see PathMatchingResourcePatternResolver
	 */
    public ResourceArrayPropertyEditor() {
        this.resourcePatternResolver = new PathMatchingResourcePatternResolver();
    }

    /**
	 * Create a new ResourceArrayPropertyEditor with the given ResourcePatternResolver.
	 * @param resourcePatternResolver the ResourcePatternResolver to use
	 */
    public ResourceArrayPropertyEditor(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    /**
	 * Resolve the given path, replacing placeholders with
	 * corresponding system property values if necessary.
	 * @param path the original file path
	 * @return the resolved file path

	 */
    protected String resolvePath(String path) {
        return SystemPropertyUtils.resolvePlaceholders(path);
    }

    public Resource[] getValueFromObject(Object value) {
        if (value instanceof Collection || (value instanceof Object[] && !(value instanceof Resource[]))) {
            Collection input = (value instanceof Collection ? (Collection) value : Arrays.asList((Object[]) value));
            List merged = new ArrayList();
            for (Iterator it = input.iterator(); it.hasNext(); ) {
                Object element = it.next();
                if (element instanceof String) {
                    String pattern = resolvePath((String) element).trim();
                    try {
                        Resource[] resources = this.resourcePatternResolver.getResources(pattern);
                        for (int i = 0; i < resources.length; i++) {
                            Resource resource = resources[i];
                            if (!merged.contains(resource)) {
                                merged.add(resource);
                            }
                        }
                    } catch (IOException ex) {
                        throw new IllegalArgumentException("Could not resolve resource location pattern [" + pattern + "]: " + ex.getMessage());
                    }
                } else if (element instanceof Resource) {
                    if (!merged.contains(element)) {
                        merged.add(element);
                    }
                } else {
                    throw new IllegalArgumentException("Cannot convert element [" + element + "] to [" + Resource.class.getName() + "]: only location String and Resource object supported");
                }
            }
            return (Resource[]) (merged.toArray(new Resource[merged.size()]));
        } else {
            if (value instanceof Resource[]) return (Resource[]) (value); else return getValueFromString(String.valueOf(value));
        }
    }

    public Resource[] getValueFromString(String text) {
        String pattern = resolvePath(text).trim();
        try {
            return (this.resourcePatternResolver.getResources(pattern));
        } catch (IOException ex) {
            throw new IllegalArgumentException("Could not resolve resource location pattern [" + pattern + "]: " + ex.getMessage());
        }
    }
}
