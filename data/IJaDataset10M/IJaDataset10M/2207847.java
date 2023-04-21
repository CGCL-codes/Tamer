package net.sourceforge.svnmonitor.model;

import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.PlatformObject;

/**
 * represents a folder in the repository structure
 */
public class RepoFolder extends PlatformObject implements IRepositoryItem, IAdaptable {

    private Collection<IRepositoryItem> children = new ArrayList<IRepositoryItem>();

    private IRepositoryItem parent;

    private String name;

    @Override
    public Collection<IRepositoryItem> getChildren() {
        return children;
    }

    public void setChildren(Collection<IRepositoryItem> children) {
        this.children = children;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IRepositoryItem getParent() {
        return parent;
    }

    public void setParent(IRepositoryItem parent) {
        this.parent = parent;
    }

    public void setName(String name) {
        this.name = name;
    }
}
