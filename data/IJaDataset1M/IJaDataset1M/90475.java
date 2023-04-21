package org.eclipse.mylyn.internal.java.ui;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IParent;
import org.eclipse.jdt.core.ISourceReference;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JarEntryFile;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.internal.ui.packageview.ClassPathContainer;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jdt.internal.ui.util.ExceptionHandler;
import org.eclipse.mylyn.context.core.AbstractContextStructureBridge;
import org.eclipse.mylyn.context.core.AbstractRelationProvider;
import org.eclipse.mylyn.context.core.ContextCorePlugin;
import org.eclipse.mylyn.context.core.IInteractionElement;
import org.eclipse.mylyn.internal.java.ui.search.JUnitReferencesProvider;
import org.eclipse.mylyn.internal.java.ui.search.JavaImplementorsProvider;
import org.eclipse.mylyn.internal.java.ui.search.JavaReadAccessProvider;
import org.eclipse.mylyn.internal.java.ui.search.JavaReferencesProvider;
import org.eclipse.mylyn.internal.java.ui.search.JavaWriteAccessProvider;
import org.eclipse.mylyn.internal.resources.ui.ResourceStructureBridge;
import org.eclipse.mylyn.monitor.core.StatusHandler;
import org.eclipse.ui.internal.WorkingSet;
import org.eclipse.ui.views.markers.internal.ConcreteMarker;

/**
 * @author Mik Kersten
 */
public class JavaStructureBridge extends AbstractContextStructureBridge {

    public static final String CONTENT_TYPE = "java";

    public List<AbstractRelationProvider> providers;

    public JavaStructureBridge() {
        providers = new ArrayList<AbstractRelationProvider>();
        providers.add(new JavaReferencesProvider());
        providers.add(new JavaImplementorsProvider());
        providers.add(new JavaReadAccessProvider());
        providers.add(new JavaWriteAccessProvider());
        providers.add(new JUnitReferencesProvider());
    }

    @Override
    public String getContentType() {
        return CONTENT_TYPE;
    }

    @Override
    public Object getAdaptedParent(Object object) {
        if (object instanceof IFile) {
            IFile file = (IFile) object;
            return JavaCore.create(file.getParent());
        } else {
            return super.getAdaptedParent(object);
        }
    }

    @Override
    public String getParentHandle(String handle) {
        IJavaElement javaElement = (IJavaElement) getObjectForHandle(handle);
        if (javaElement != null && javaElement.getParent() != null) {
            return getHandleIdentifier(javaElement.getParent());
        } else {
            return null;
        }
    }

    @Override
    public List<String> getChildHandles(String handle) {
        Object object = getObjectForHandle(handle);
        if (object instanceof IJavaElement) {
            IJavaElement element = (IJavaElement) object;
            if (element instanceof IParent) {
                IParent parent = (IParent) element;
                IJavaElement[] children;
                try {
                    children = parent.getChildren();
                    List<String> childHandles = new ArrayList<String>();
                    for (int i = 0; i < children.length; i++) {
                        String childHandle = getHandleIdentifier(children[i]);
                        if (childHandle != null) childHandles.add(childHandle);
                    }
                    AbstractContextStructureBridge parentBridge = ContextCorePlugin.getDefault().getStructureBridge(parentContentType);
                    if (parentBridge != null && parentBridge instanceof ResourceStructureBridge) {
                        if (element.getElementType() < IJavaElement.TYPE) {
                            List<String> resourceChildren = parentBridge.getChildHandles(handle);
                            if (!resourceChildren.isEmpty()) childHandles.addAll(resourceChildren);
                        }
                    }
                    return childHandles;
                } catch (JavaModelException e) {
                } catch (Exception e) {
                    StatusHandler.fail(e, "could not get child", false);
                }
            }
        }
        return Collections.emptyList();
    }

    @Override
    public Object getObjectForHandle(String handle) {
        try {
            return JavaCore.create(handle);
        } catch (Throwable t) {
            StatusHandler.log("Could not create java element for handle: " + handle, this);
            return null;
        }
    }

    /**
	 * Uses resource-compatible path for projects
	 */
    @Override
    public String getHandleIdentifier(Object object) {
        if (object instanceof IJavaElement) {
            return ((IJavaElement) object).getHandleIdentifier();
        } else {
            if (object instanceof IAdaptable) {
                Object adapter = ((IAdaptable) object).getAdapter(IJavaElement.class);
                if (adapter instanceof IJavaElement) {
                    return ((IJavaElement) adapter).getHandleIdentifier();
                }
            } else if (isWtpClass(object)) {
                return getWtpElementHandle(object);
            }
        }
        return null;
    }

    /**
	 * TODO: remove after WTP 1.5.1 is generally available
	 */
    private String getWtpElementHandle(Object object) {
        Class<?> objectClass = object.getClass();
        try {
            Method getProjectMethod = objectClass.getMethod("getProject", new Class[0]);
            Object javaProject = getProjectMethod.invoke(object, new Object[0]);
            if (javaProject instanceof IJavaProject) {
                return ((IJavaElement) javaProject).getHandleIdentifier();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isWtpClass(Object object) {
        return object != null && object.getClass().getSimpleName().equals("CompressedJavaProject");
    }

    @Override
    public String getLabel(Object object) {
        if (object instanceof IJavaElement) {
            return ((IJavaElement) object).getElementName();
        } else {
            return "";
        }
    }

    @Override
    public boolean canBeLandmark(String handle) {
        IJavaElement element = (IJavaElement) getObjectForHandle(handle);
        if ((element instanceof IMember || element instanceof IType) && element.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * TODO: figure out if the non IJavaElement stuff is needed
	 */
    @Override
    public boolean acceptsObject(Object object) {
        if (object instanceof IResource) {
            Object adapter = ((IResource) object).getAdapter(IJavaElement.class);
            return adapter instanceof IJavaElement;
        }
        boolean accepts = object instanceof IJavaElement || object instanceof PackageFragmentRootContainer || object instanceof ClassPathContainer.RequiredProjectWrapper || object instanceof JarEntryFile || object instanceof IPackageFragment || object instanceof WorkingSet || isWtpClass(object);
        return accepts;
    }

    /**
	 * Uses special rules for classpath containers since these do not have an associated interest, i.e. they're not
	 * IJavaElement(s).
	 */
    @Override
    public boolean canFilter(Object object) {
        if (object instanceof ClassPathContainer.RequiredProjectWrapper) {
            return true;
        } else if (object instanceof PackageFragmentRootContainer) {
            PackageFragmentRootContainer container = (PackageFragmentRootContainer) object;
            Object[] children = container.getChildren();
            for (int i = 0; i < children.length; i++) {
                if (children[i] instanceof JarPackageFragmentRoot) {
                    JarPackageFragmentRoot element = (JarPackageFragmentRoot) children[i];
                    IInteractionElement node = ContextCorePlugin.getContextManager().getElement(element.getHandleIdentifier());
                    if (node != null && node.getInterest().isInteresting()) {
                        return false;
                    }
                }
            }
        } else if (object instanceof WorkingSet) {
            try {
                WorkingSet workingSet = (WorkingSet) object;
                IAdaptable[] elements = workingSet.getElements();
                for (int i = 0; i < elements.length; i++) {
                    IAdaptable adaptable = elements[i];
                    IInteractionElement element = ContextCorePlugin.getContextManager().getElement(getHandleIdentifier(adaptable));
                    if (element.getInterest().isInteresting()) return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isDocument(String handle) {
        IJavaElement element = (IJavaElement) getObjectForHandle(handle);
        return element instanceof ICompilationUnit || element instanceof IClassFile;
    }

    @Override
    public String getHandleForOffsetInObject(Object resource, int offset) {
        if (resource == null || !(resource instanceof ConcreteMarker)) return null;
        ConcreteMarker marker = (ConcreteMarker) resource;
        try {
            IResource res = marker.getResource();
            ICompilationUnit compilationUnit = null;
            if (res instanceof IFile) {
                IFile file = (IFile) res;
                if (file.getFileExtension().equals("java")) {
                    compilationUnit = JavaCore.createCompilationUnitFrom(file);
                } else {
                    return null;
                }
            }
            if (compilationUnit != null) {
                IJavaElement javaElement = compilationUnit.getElementAt(marker.getMarker().getAttribute(IMarker.CHAR_START, 0));
                if (javaElement != null) {
                    if (javaElement instanceof IImportDeclaration) javaElement = javaElement.getParent().getParent();
                    return javaElement.getHandleIdentifier();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (JavaModelException ex) {
            if (!ex.isDoesNotExist()) ExceptionHandler.handle(ex, "error", "could not find java element");
            return null;
        } catch (Throwable t) {
            StatusHandler.fail(t, "Could not find element for: " + marker, false);
            return null;
        }
    }

    @Override
    public String getContentType(String elementHandle) {
        return getContentType();
    }

    /**
	 * Some copying from:
	 * 
	 * @see org.eclipse.jdt.ui.ProblemsLabelDecorator
	 */
    public boolean containsProblem(IInteractionElement node) {
        try {
            IJavaElement element = (IJavaElement) getObjectForHandle(node.getHandleIdentifier());
            switch(element.getElementType()) {
                case IJavaElement.JAVA_PROJECT:
                case IJavaElement.PACKAGE_FRAGMENT_ROOT:
                    return getErrorTicksFromMarkers(element.getResource(), IResource.DEPTH_INFINITE, null);
                case IJavaElement.PACKAGE_FRAGMENT:
                case IJavaElement.COMPILATION_UNIT:
                case IJavaElement.CLASS_FILE:
                    return getErrorTicksFromMarkers(element.getResource(), IResource.DEPTH_ONE, null);
                case IJavaElement.PACKAGE_DECLARATION:
                case IJavaElement.IMPORT_DECLARATION:
                case IJavaElement.IMPORT_CONTAINER:
                case IJavaElement.TYPE:
                case IJavaElement.INITIALIZER:
                case IJavaElement.METHOD:
                case IJavaElement.FIELD:
                case IJavaElement.LOCAL_VARIABLE:
                    ICompilationUnit cu = (ICompilationUnit) element.getAncestor(IJavaElement.COMPILATION_UNIT);
                    if (cu != null) return getErrorTicksFromMarkers(element.getResource(), IResource.DEPTH_ONE, null);
            }
        } catch (CoreException e) {
        }
        return false;
    }

    private boolean getErrorTicksFromMarkers(IResource res, int depth, ISourceReference sourceElement) throws CoreException {
        if (res == null || !res.isAccessible()) return false;
        IMarker[] markers = res.findMarkers(IMarker.PROBLEM, true, depth);
        if (markers != null) {
            for (int i = 0; i < markers.length; i++) {
                IMarker curr = markers[i];
                if (sourceElement == null) {
                    int priority = curr.getAttribute(IMarker.SEVERITY, -1);
                    if (priority == IMarker.SEVERITY_ERROR) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
