package org.coinjema.context.source;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.Set;

class ClasspathResource implements Resource {

    String resourceName;

    String baseName;

    ClassLoader loader;

    Set<MetaType> types;

    String format;

    ClasspathResource(String fullName, String name) {
        resourceName = fullName;
        baseName = name;
        types = MetaType.getIncludedTypes(fullName);
    }

    public String getFormat() {
        if (format == null) {
            format = resourceName.substring(baseName.length());
            format = MetaType.stripMetaTypes(format);
            if (format.startsWith(".")) format = format.substring(1);
            if (format.endsWith(".")) format = format.substring(0, format.length() - 1);
        }
        return format;
    }

    public InputStream getInputStream() {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
    }

    public Collection<MetaType> getMetaTypes() {
        return types;
    }

    public String getName() {
        return baseName;
    }

    public Reader getReader() {
        return new InputStreamReader(getInputStream());
    }
}
