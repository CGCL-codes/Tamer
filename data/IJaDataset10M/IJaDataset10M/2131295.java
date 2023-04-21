package com.enerjy.index.java;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.enerjy.index.IMetricsFile;

/**
 * An in-memory implementation of a Java metrics data  store.
 */
public class HashMapDataStore implements IJavaDataStore, Serializable {

    private static final long serialVersionUID = 1L;

    private Map<IMetricsFile, Set<String>> filesToDeclaredBindings = new HashMap<IMetricsFile, Set<String>>();

    private Map<String, Set<IMetricsFile>> refBindingsToFile = new HashMap<String, Set<IMetricsFile>>();

    private Set<IMetricsFile> failed = new HashSet<IMetricsFile>();

    public void clearData(IMetricsFile file) {
        filesToDeclaredBindings.remove(file);
        failed.remove(file);
    }

    public void clear() {
        filesToDeclaredBindings.clear();
        refBindingsToFile.clear();
        failed.clear();
    }

    public void addReferenceToType(String type, IMetricsFile file) {
        Set<IMetricsFile> refs = refBindingsToFile.get(type);
        if (null == refs) {
            refs = new HashSet<IMetricsFile>();
            refBindingsToFile.put(type, refs);
        }
        refs.add(file);
    }

    public void addTypeDeclaration(IMetricsFile file, String type) {
        Set<String> types = filesToDeclaredBindings.get(file);
        if (null == types) {
            types = new HashSet<String>();
            filesToDeclaredBindings.put(file, types);
        }
        types.add(type);
    }

    public String[] getDeclaredTypesFrom(IMetricsFile file) {
        Set<String> types = filesToDeclaredBindings.get(file);
        if (null == types) {
            return new String[0];
        }
        return types.toArray(new String[types.size()]);
    }

    public IMetricsFile[] getFilesUsing(String type) {
        Set<IMetricsFile> files = refBindingsToFile.get(type);
        if (null == files) {
            return new IMetricsFile[0];
        }
        return files.toArray(new IMetricsFile[files.size()]);
    }

    public void addFailure(IMetricsFile file) {
        failed.add(file);
    }

    public IMetricsFile[] getAllFailedFiles() {
        return failed.toArray(new IMetricsFile[failed.size()]);
    }
}
