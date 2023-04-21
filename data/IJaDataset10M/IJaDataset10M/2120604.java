package org.jzentest.dependency;

import java.util.Set;

public class TestDependencies {

    private String className;

    private Set<String> dependencies;

    public TestDependencies(String className, Set<String> dependencies) {
        this.className = className;
        this.dependencies = dependencies;
    }

    public String getClassName() {
        return className;
    }

    public Set<String> getDependencies() {
        return dependencies;
    }
}
