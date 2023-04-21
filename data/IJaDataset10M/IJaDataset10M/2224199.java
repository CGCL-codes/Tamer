package org.eclipse.pde.nls.internal.ui.model;

public class ResourceBundleKeyList {

    private final ResourceBundleKey[] keys;

    public ResourceBundleKeyList(ResourceBundleKey[] keys) {
        this.keys = keys;
    }

    public ResourceBundleKey getKey(int index) {
        return keys[index];
    }

    public int getSize() {
        return keys.length;
    }
}
