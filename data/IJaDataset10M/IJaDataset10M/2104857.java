package com.metanology.mde.core.ui.common;

import java.io.File;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import com.metanology.mde.core.codeFactory.MetaMachine;
import com.metanology.mde.core.common.IResourceUtility;

/**
 * @author wwang
 */
public class ResourceUtility implements IResourceUtility {

    private IProject root;

    private IProgressMonitor monitor;

    /**
	 * Default constructor
	 */
    public ResourceUtility(IProject root, IProgressMonitor monitor) {
        this.root = root;
        this.monitor = monitor;
    }

    public boolean move(File src, File dest) {
        try {
            IResource srcRes = getResource(src);
            if (srcRes != null) {
                IPath destPath = root.getFullPath();
                destPath = destPath.append(getRootRelativePath(dest));
                srcRes.move(destPath, true, monitor);
                return true;
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
        return false;
    }

    public boolean delete(File resource) {
        try {
            IResource res = getResource(resource);
            if (res != null) {
                res.delete(true, monitor);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private IResource getResource(File resource) {
        try {
            IPath resourcePath = getRootRelativePath(resource);
            if (resourcePath == null) throw new RuntimeException("Invalid path:" + resource.getPath());
            IResource res = root.findMember(resourcePath);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private IPath getRootRelativePath(File resource) {
        try {
            IPath resourcePath = new Path(resource.getCanonicalPath());
            resourcePath = resourcePath.setDevice(null);
            IPath rootPath = root.getLocation();
            rootPath = rootPath.setDevice(null);
            String p1 = resourcePath.toString();
            String p2 = rootPath.toString();
            String rootPkgPath = p2 + "/" + MetaMachine.ROOT_PKG;
            String rootSubsysPath = p2 + "/" + MetaMachine.ROOT_SUB;
            String lower_p1 = p1.toLowerCase();
            int i = lower_p1.indexOf(rootPkgPath.toLowerCase());
            if (i < 0) {
                i = lower_p1.indexOf(rootSubsysPath.toLowerCase());
            }
            if (i < 0) {
                i = lower_p1.indexOf(p2.toLowerCase());
            }
            if (i >= 0) {
                p1 = p1.substring(i + p2.length());
                resourcePath = new Path(p1);
                return resourcePath;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * @return
	 */
    public IProgressMonitor getMonitor() {
        return monitor;
    }

    /**
	 * @param monitor
	 */
    public void setMonitor(IProgressMonitor monitor) {
        this.monitor = monitor;
    }

    /**
	 * @return
	 */
    public IProject getRoot() {
        return root;
    }

    /**
	 * @param root
	 */
    public void setRoot(IProject root) {
        this.root = root;
    }
}
