package org.flexharmony.eclipse.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

public class PatternMarshaller {

    public static final String DELIMITTER = ",";

    public static String marshall(List<String> patterns) {
        String value = "";
        for (int i = 0; i < patterns.size(); i++) {
            if (i != 0) value += DELIMITTER;
            value += patterns.get(i);
        }
        return value;
    }

    public static List<String> getPatterns(IProject project) throws CoreException {
        return unmarshall(project.getPersistentProperty(PropertiesConstants.CLASS_NAME_PATTERNS_QNAME));
    }

    public static List<String> unmarshall(String value) {
        if ((value == null) || (value.length() == 0)) return new ArrayList<String>();
        String[] patterns = value.split(DELIMITTER);
        return Arrays.asList(patterns);
    }
}
