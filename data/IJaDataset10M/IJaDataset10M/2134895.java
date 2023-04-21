package org.impalaframework.resolver;

import java.util.List;
import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.util.PathUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * {@link ModuleLocationResolver} implementation whose {@link #workspaceRoots} property is directly wired in.
 * @author Phil Zoio
 */
public abstract class SimpleBaseModuleLocationResolver implements ModuleLocationResolver {

    private String[] workspaceRoots;

    public void init() {
        Assert.notNull(workspaceRoots, "workspaceRoots cannot be null");
    }

    protected String[] getWorkspaceRoots() {
        return workspaceRoots;
    }

    public void setWorkspaceRoot(String workspaceRoot) {
        Assert.notNull(workspaceRoot, "workspaceRoot cannot be null");
        String[] rootsArray = StringUtils.tokenizeToStringArray(workspaceRoot, ", ");
        for (int i = 0; i < rootsArray.length; i++) {
            rootsArray[i] = rootsArray[i].trim();
        }
        this.workspaceRoots = rootsArray;
    }

    protected void checkResources(List<Resource> resources, String moduleName, String moduleVersion, String rootDirectoryPath, String resourceType) {
        if (resources == null || resources.isEmpty()) {
            throw new InvalidStateException("Unable to find any " + resourceType + " resources in workspace root directory '" + PathUtils.getAbsolutePath(rootDirectoryPath) + "' for module named '" + moduleName + "' with version '" + moduleVersion + "'");
        }
    }
}
