package org.simpleframework.tool.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SourceProject {

    private Map<String, List<SourceDetails>> packages = new HashMap<String, List<SourceDetails>>();

    private Map<String, SourceDetails> names = new HashMap<String, SourceDetails>();

    private List<SourceDetails> all = new ArrayList<SourceDetails>();

    public List<SourceDetails> getDetails() {
        return all;
    }

    public void addSource(SourceDetails details) {
        String packageName = details.getPackage();
        String name = details.getName();
        List<SourceDetails> packageFiles = packages.get(packageName);
        if (packageFiles == null) {
            packageFiles = new ArrayList<SourceDetails>();
            packages.put(packageName, packageFiles);
        }
        packageFiles.add(details);
        all.add(details);
        names.put(name, details);
    }

    public SourceDetails getDetails(String name) {
        return names.get(name);
    }
}
