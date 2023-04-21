package com.google.gwt.dev.javac;

import com.google.gwt.dev.javac.JavaSourceFile;
import com.google.gwt.dev.javac.JavaSourceOracle;
import junit.framework.Assert;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link ResourceOracle} for testing.
 */
public class MockJavaSourceOracle implements JavaSourceOracle {

    private Map<String, JavaSourceFile> exportedMap = Collections.emptyMap();

    private Set<JavaSourceFile> exportedValues = Collections.emptySet();

    public MockJavaSourceOracle(JavaSourceFile... sourceFiles) {
        add(sourceFiles);
    }

    public Set<String> getClassNames() {
        return exportedMap.keySet();
    }

    public Set<JavaSourceFile> getSourceFiles() {
        return exportedValues;
    }

    public Map<String, JavaSourceFile> getSourceMap() {
        return exportedMap;
    }

    public void add(JavaSourceFile... sourceFiles) {
        Map<String, JavaSourceFile> newMap = new HashMap<String, JavaSourceFile>(exportedMap);
        for (JavaSourceFile sourceFile : sourceFiles) {
            String className = sourceFile.getTypeName();
            Assert.assertFalse(newMap.containsKey(className));
            newMap.put(className, sourceFile);
        }
        export(newMap);
    }

    void remove(String... classNames) {
        Map<String, JavaSourceFile> newMap = new HashMap<String, JavaSourceFile>(exportedMap);
        for (String className : classNames) {
            JavaSourceFile oldValue = newMap.remove(className);
            Assert.assertNotNull(oldValue);
        }
        export(newMap);
    }

    void replace(JavaSourceFile... sourceFiles) {
        Map<String, JavaSourceFile> newMap = new HashMap<String, JavaSourceFile>(exportedMap);
        for (JavaSourceFile sourceFile : sourceFiles) {
            String className = sourceFile.getTypeName();
            Assert.assertTrue(newMap.containsKey(className));
            newMap.put(className, sourceFile);
        }
        export(newMap);
    }

    private void export(Map<String, JavaSourceFile> newMap) {
        exportedMap = Collections.unmodifiableMap(newMap);
        exportedValues = Collections.unmodifiableSet(new HashSet<JavaSourceFile>(exportedMap.values()));
    }
}
