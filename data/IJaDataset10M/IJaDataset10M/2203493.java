package org.junithelper.core.config.extension;

import java.util.ArrayList;
import java.util.List;
import org.junithelper.core.util.Assertion;

public class ExtReturn {

    public ExtReturn(String canonicalClassName) {
        Assertion.on("canonicalClassName").mustNotBeNull(canonicalClassName);
        this.canonicalClassName = canonicalClassName;
    }

    public String canonicalClassName;

    public List<String> importList = new ArrayList<String>();

    public List<String> asserts = new ArrayList<String>();
}
