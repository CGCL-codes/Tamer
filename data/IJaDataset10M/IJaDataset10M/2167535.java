package org.crap4j.crap4jeclipse.actions;

import com.agitar.lib.junit.AgitarTestCase;
import com.agitar.lib.mockingbird.Mockingbird;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.crap4j.CrapProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;

public class EclipseCrapProjectBuilderAgitarTest extends AgitarTestCase {

    public Class getTargetClass() {
        return EclipseCrapProjectBuilder.class;
    }

    public void testConstructorWithAggressiveMocks() throws Throwable {
        IJavaProject iJavaProject = (IJavaProject) Mockingbird.getProxyObject(IJavaProject.class);
        CoreException coreException = (CoreException) Mockingbird.getProxyObject(CoreException.class);
        Iterator iterator = (Iterator) Mockingbird.getProxyObject(Iterator.class);
        IRuntimeClasspathEntry iRuntimeClasspathEntry = (IRuntimeClasspathEntry) Mockingbird.getProxyObject(IRuntimeClasspathEntry.class);
        IClasspathEntry iClasspathEntry = (IClasspathEntry) Mockingbird.getProxyObject(IClasspathEntry.class);
        IRuntimeClasspathEntry iRuntimeClasspathEntry2 = (IRuntimeClasspathEntry) Mockingbird.getProxyObject(IRuntimeClasspathEntry.class);
        IClasspathEntry iClasspathEntry2 = (IClasspathEntry) Mockingbird.getProxyObject(IClasspathEntry.class);
        IRuntimeClasspathEntry iRuntimeClasspathEntry3 = (IRuntimeClasspathEntry) Mockingbird.getProxyObject(IRuntimeClasspathEntry.class);
        IClasspathEntry iClasspathEntry3 = (IClasspathEntry) Mockingbird.getProxyObject(IClasspathEntry.class);
        IRuntimeClasspathEntry iRuntimeClasspathEntry4 = (IRuntimeClasspathEntry) Mockingbird.getProxyObject(IRuntimeClasspathEntry.class);
        IClasspathEntry iClasspathEntry4 = (IClasspathEntry) Mockingbird.getProxyObject(IClasspathEntry.class);
        IPath iPath = (IPath) Mockingbird.getProxyObject(IPath.class);
        IRuntimeClasspathEntry iRuntimeClasspathEntry5 = (IRuntimeClasspathEntry) Mockingbird.getProxyObject(IRuntimeClasspathEntry.class);
        IClasspathEntry iClasspathEntry5 = (IClasspathEntry) Mockingbird.getProxyObject(IClasspathEntry.class);
        IPath iPath2 = (IPath) Mockingbird.getProxyObject(IPath.class);
        File file = (File) Mockingbird.getProxyObject(File.class);
        IRuntimeClasspathEntry iRuntimeClasspathEntry6 = (IRuntimeClasspathEntry) Mockingbird.getProxyObject(IRuntimeClasspathEntry.class);
        IClasspathEntry iClasspathEntry6 = (IClasspathEntry) Mockingbird.getProxyObject(IClasspathEntry.class);
        JavaModelException javaModelException = (JavaModelException) Mockingbird.getProxyObject(JavaModelException.class);
        Iterator iterator2 = (Iterator) Mockingbird.getProxyObject(Iterator.class);
        JavaModelException javaModelException2 = (JavaModelException) Mockingbird.getProxyObject(JavaModelException.class);
        Mockingbird.enterRecordingMode();
        ArrayList arrayList = (ArrayList) Mockingbird.getProxyObject(ArrayList.class);
        Mockingbird.replaceObjectForRecording(ArrayList.class, "<init>()", arrayList);
        Mockingbird.setException(JavaRuntime.computeUnresolvedRuntimeClasspath(iJavaProject), coreException);
        coreException.printStackTrace();
        Mockingbird.setNormalReturnForVoid();
        ArrayList arrayList2 = (ArrayList) Mockingbird.getProxyObject(ArrayList.class);
        Mockingbird.replaceObjectForRecording(ArrayList.class, "<init>()", arrayList2);
        Mockingbird.setReturnValue(arrayList.iterator(), iterator);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), iRuntimeClasspathEntry);
        Mockingbird.setReturnValue(iRuntimeClasspathEntry.getClasspathEntry(), iClasspathEntry);
        Mockingbird.setReturnValue(iClasspathEntry.getEntryKind(), 5);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), iRuntimeClasspathEntry2);
        Mockingbird.setReturnValue(iRuntimeClasspathEntry2.getClasspathEntry(), iClasspathEntry2);
        Mockingbird.setReturnValue(iClasspathEntry2.getEntryKind(), 5);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), iRuntimeClasspathEntry3);
        Mockingbird.setReturnValue(iRuntimeClasspathEntry3.getClasspathEntry(), iClasspathEntry3);
        Mockingbird.setReturnValue(iClasspathEntry3.getEntryKind(), 3);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), iRuntimeClasspathEntry4);
        Mockingbird.setReturnValue(iRuntimeClasspathEntry4.getClasspathEntry(), iClasspathEntry4);
        Mockingbird.setReturnValue(iClasspathEntry4.getEntryKind(), 0);
        Mockingbird.setReturnValue(iRuntimeClasspathEntry4.getPath(), iPath);
        Mockingbird.setReturnValue(iPath.isAbsolute(), false);
        Mockingbird.setReturnValue(EclipseCrapProjectBuilder.class, "getAbsoluteFilePath", "(org.eclipse.core.runtime.IPath)java.lang.String", "", 1);
        Boolean boolean2 = Boolean.FALSE;
        Mockingbird.setReturnValue(false, arrayList2, "add", "(java.lang.Object)boolean", boolean2, 1);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), iRuntimeClasspathEntry5);
        Mockingbird.setReturnValue(iRuntimeClasspathEntry5.getClasspathEntry(), iClasspathEntry5);
        Mockingbird.setReturnValue(iClasspathEntry5.getEntryKind(), 0);
        Mockingbird.setReturnValue(iRuntimeClasspathEntry5.getPath(), iPath2);
        Mockingbird.setReturnValue(iPath2.isAbsolute(), true);
        Mockingbird.setReturnValue(iPath2.toFile(), file);
        Mockingbird.setReturnValue(false, file, "getAbsolutePath", "()java.lang.String", new Object[] {}, "", 1);
        Mockingbird.setReturnValue(false, arrayList2, "add", "(java.lang.Object)boolean", boolean2, 1);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), iRuntimeClasspathEntry6);
        Mockingbird.setReturnValue(iRuntimeClasspathEntry6.getClasspathEntry(), iClasspathEntry6);
        Mockingbird.setReturnValue(iClasspathEntry6.getEntryKind(), 3);
        Mockingbird.setReturnValue(iterator.hasNext(), false);
        ArrayList arrayList3 = (ArrayList) Mockingbird.getProxyObject(ArrayList.class);
        Mockingbird.replaceObjectForRecording(ArrayList.class, "<init>()", arrayList3);
        Mockingbird.setException(iJavaProject.getResolvedClasspath(true), javaModelException);
        javaModelException.printStackTrace();
        Mockingbird.setNormalReturnForVoid();
        Mockingbird.replaceObjectForRecording(ArrayList.class, "<init>()", Mockingbird.getProxyObject(ArrayList.class));
        Mockingbird.setReturnValue(arrayList3.iterator(), iterator2);
        Mockingbird.setReturnValue(iterator2.hasNext(), true);
        Mockingbird.setReturnValue(iterator2.next(), "");
        Boolean boolean3 = Boolean.TRUE;
        Mockingbird.setReturnValue(EclipseCrapProjectBuilder.class, "isTestFolder", "(java.lang.String)boolean", boolean3, 1);
        Mockingbird.setReturnValue(iterator2.hasNext(), true);
        Mockingbird.setReturnValue(iterator2.next(), "");
        Mockingbird.setReturnValue(EclipseCrapProjectBuilder.class, "isTestFolder", "(java.lang.String)boolean", boolean2, 1);
        Mockingbird.setReturnValue(EclipseCrapProjectBuilder.class, "addToList", "(java.util.ArrayList,java.lang.String)void", null, 1);
        Mockingbird.setReturnValue(iterator2.hasNext(), true);
        Mockingbird.setReturnValue(iterator2.next(), "");
        Mockingbird.setReturnValue(EclipseCrapProjectBuilder.class, "isTestFolder", "(java.lang.String)boolean", boolean2, 1);
        Mockingbird.setReturnValue(EclipseCrapProjectBuilder.class, "addToList", "(java.util.ArrayList,java.lang.String)void", null, 1);
        Mockingbird.setReturnValue(iterator2.hasNext(), true);
        Mockingbird.setReturnValue(iterator2.next(), "");
        Mockingbird.setReturnValue(EclipseCrapProjectBuilder.class, "isTestFolder", "(java.lang.String)boolean", boolean3, 1);
        Mockingbird.setReturnValue(iterator2.hasNext(), false);
        Mockingbird.replaceObjectForRecording(ArrayList.class, "<init>(java.util.Collection)", Mockingbird.getProxyObject(ArrayList.class));
        Mockingbird.setReturnValue(EclipseCrapProjectBuilder.class, "filterItems", "(java.util.List,java.util.List)void", null, 1);
        Mockingbird.setReturnValue(EclipseCrapProjectBuilder.class, "filterItems", "(java.util.List,java.util.List)void", null, 1);
        Mockingbird.setReturnValue(iJavaProject.getResource(), null);
        Mockingbird.setReturnValue(EclipseCrapProjectBuilder.class, "makeResourceAbsolute", "(org.eclipse.core.resources.IResource)java.lang.String", "", 1);
        Mockingbird.replaceObjectForRecording(ArrayList.class, "<init>()", Mockingbird.getProxyObject(ArrayList.class));
        Mockingbird.setException(iJavaProject.getPackageFragmentRoots(), javaModelException2);
        Mockingbird.setReturnValue(false, javaModelException2, "printStackTrace", "()void", new Object[] {}, null, 1);
        Mockingbird.replaceObjectForRecording(CrapProject.class, "<init>(java.lang.String,java.util.List,java.util.List,java.util.List,java.util.List,java.lang.String)", Mockingbird.getProxyObject(CrapProject.class));
        Mockingbird.enterTestMode();
        EclipseCrapProjectBuilder eclipseCrapProjectBuilder = new EclipseCrapProjectBuilder(iJavaProject, "");
        assertNull("eclipseCrapProjectBuilder.getCrapProject().outputDir()", eclipseCrapProjectBuilder.getCrapProject().outputDir());
    }

    public void testAddToListWithAggressiveMocks() throws Throwable {
        EclipseCrapProjectBuilder eclipseCrapProjectBuilder = (EclipseCrapProjectBuilder) Mockingbird.getProxyObject(EclipseCrapProjectBuilder.class, true);
        ArrayList arrayList = (ArrayList) Mockingbird.getProxyObject(ArrayList.class);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(false, arrayList, "contains", "(java.lang.Object)boolean", Boolean.TRUE, 1);
        Mockingbird.enterTestMode(EclipseCrapProjectBuilder.class);
        callPrivateMethod("org.crap4j.crap4jeclipse.actions.EclipseCrapProjectBuilder", "addToList", new Class[] { ArrayList.class, String.class }, eclipseCrapProjectBuilder, new Object[] { arrayList, "" });
        assertNull("eclipseCrapProjectBuilder.getCrapProject()", eclipseCrapProjectBuilder.getCrapProject());
    }

    public void testFilterItemsWithAggressiveMocks() throws Throwable {
        EclipseCrapProjectBuilder eclipseCrapProjectBuilder = (EclipseCrapProjectBuilder) Mockingbird.getProxyObject(EclipseCrapProjectBuilder.class, true);
        List list = (List) Mockingbird.getProxyObject(List.class);
        List list2 = (List) Mockingbird.getProxyObject(List.class);
        Iterator iterator = (Iterator) Mockingbird.getProxyObject(Iterator.class);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(list.iterator(), iterator);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), "");
        Boolean boolean2 = Boolean.FALSE;
        Mockingbird.setReturnValue(false, list2, "contains", "(java.lang.Object)boolean", boolean2, 1);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), "");
        Boolean boolean3 = Boolean.TRUE;
        Mockingbird.setReturnValue(false, list2, "contains", "(java.lang.Object)boolean", boolean3, 1);
        Mockingbird.setReturnValue(false, list2, "remove", "(java.lang.Object)boolean", boolean2, 1);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), "");
        Mockingbird.setReturnValue(false, list2, "contains", "(java.lang.Object)boolean", boolean3, 1);
        Mockingbird.setReturnValue(false, list2, "remove", "(java.lang.Object)boolean", boolean2, 1);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), "");
        Mockingbird.setReturnValue(false, list2, "contains", "(java.lang.Object)boolean", boolean2, 1);
        Mockingbird.setReturnValue(iterator.hasNext(), false);
        Mockingbird.enterTestMode(EclipseCrapProjectBuilder.class);
        callPrivateMethod("org.crap4j.crap4jeclipse.actions.EclipseCrapProjectBuilder", "filterItems", new Class[] { List.class, List.class }, eclipseCrapProjectBuilder, new Object[] { list, list2 });
        assertNull("eclipseCrapProjectBuilder.getCrapProject()", eclipseCrapProjectBuilder.getCrapProject());
    }

    public void testGetAllClassDirs_FullyResolvedWithAggressiveMocks() throws Throwable {
        EclipseCrapProjectBuilder eclipseCrapProjectBuilder = (EclipseCrapProjectBuilder) Mockingbird.getProxyObject(EclipseCrapProjectBuilder.class, true);
        IJavaProject iJavaProject = (IJavaProject) Mockingbird.getProxyObject(IJavaProject.class);
        Mockingbird.enterRecordingMode();
        ArrayList arrayList = (ArrayList) Mockingbird.getProxyObject(ArrayList.class);
        Mockingbird.replaceObjectForRecording(ArrayList.class, "<init>()", arrayList);
        IClasspathEntry[] iClasspathEntrys = new IClasspathEntry[2];
        IClasspathEntry iClasspathEntry = (IClasspathEntry) Mockingbird.getProxyObject(IClasspathEntry.class);
        IClasspathEntry iClasspathEntry2 = (IClasspathEntry) Mockingbird.getProxyObject(IClasspathEntry.class);
        IPath iPath = (IPath) Mockingbird.getProxyObject(IPath.class);
        IWorkspace iWorkspace = (IWorkspace) Mockingbird.getProxyObject(IWorkspace.class);
        IWorkspaceRoot iWorkspaceRoot = (IWorkspaceRoot) Mockingbird.getProxyObject(IWorkspaceRoot.class);
        IResource iResource = (IResource) Mockingbird.getProxyObject(IResource.class);
        IPath iPath2 = (IPath) Mockingbird.getProxyObject(IPath.class);
        IWorkspace iWorkspace2 = (IWorkspace) Mockingbird.getProxyObject(IWorkspace.class);
        IWorkspaceRoot iWorkspaceRoot2 = (IWorkspaceRoot) Mockingbird.getProxyObject(IWorkspaceRoot.class);
        Mockingbird.setReturnValue(iJavaProject.getResolvedClasspath(true), iClasspathEntrys);
        iClasspathEntrys[0] = iClasspathEntry;
        iClasspathEntrys[1] = iClasspathEntry2;
        Mockingbird.setReturnValue(false, iClasspathEntry, "getEntryKind", "()int", new Integer(3), 1);
        Mockingbird.setReturnValue(iJavaProject.getOutputLocation(), null);
        Mockingbird.setReturnValue(false, iClasspathEntry, "getOutputLocation", "()org.eclipse.core.runtime.IPath", iPath, 1);
        Mockingbird.setReturnValue(ResourcesPlugin.getWorkspace(), iWorkspace);
        Mockingbird.setReturnValue(iWorkspace.getRoot(), iWorkspaceRoot);
        Mockingbird.setReturnValue(iWorkspaceRoot.findMember(iPath), iResource);
        Mockingbird.setReturnValue(false, eclipseCrapProjectBuilder, "makeResourceAbsolute", "(org.eclipse.core.resources.IResource)java.lang.String", new Object[] { iResource }, "", 1);
        Boolean boolean2 = Boolean.FALSE;
        Mockingbird.setReturnValue(false, arrayList, "contains", "(java.lang.Object)boolean", boolean2, 1);
        Mockingbird.setReturnValue(false, arrayList, "add", "(java.lang.Object)boolean", boolean2, 1);
        Mockingbird.setReturnValue(false, iClasspathEntry2, "getEntryKind", "()int", new Integer(3), 1);
        Mockingbird.setReturnValue(iJavaProject.getOutputLocation(), null);
        Mockingbird.setReturnValue(false, iClasspathEntry2, "getOutputLocation", "()org.eclipse.core.runtime.IPath", iPath2, 1);
        Mockingbird.setReturnValue(ResourcesPlugin.getWorkspace(), iWorkspace2);
        Mockingbird.setReturnValue(iWorkspace2.getRoot(), iWorkspaceRoot2);
        Mockingbird.setReturnValue(iWorkspaceRoot2.findMember(iPath2), null);
        Mockingbird.enterTestMode(EclipseCrapProjectBuilder.class);
        ArrayList result = (ArrayList) callPrivateMethod("org.crap4j.crap4jeclipse.actions.EclipseCrapProjectBuilder", "getAllClassDirs_FullyResolved", new Class[] { IJavaProject.class }, eclipseCrapProjectBuilder, new Object[] { iJavaProject });
        assertEquals("result.size()", 0, result.size());
        assertInvocationCount(iClasspathEntry2, "getEntryKind", 1);
        assertInvocationCount(iClasspathEntry2, "getOutputLocation", 1);
        assertInvocationCount(iClasspathEntry, "getEntryKind", 1);
        assertInvocationCount(iClasspathEntry, "getOutputLocation", 1);
    }

    public void testGetAllClassDirs_FullyResolvedWithAggressiveMocks1() throws Throwable {
        EclipseCrapProjectBuilder eclipseCrapProjectBuilder = (EclipseCrapProjectBuilder) Mockingbird.getProxyObject(EclipseCrapProjectBuilder.class, true);
        IJavaProject iJavaProject = (IJavaProject) Mockingbird.getProxyObject(IJavaProject.class);
        Mockingbird.enterRecordingMode();
        Mockingbird.replaceObjectForRecording(ArrayList.class, "<init>()", Mockingbird.getProxyObject(ArrayList.class));
        IClasspathEntry[] iClasspathEntrys = new IClasspathEntry[2];
        IClasspathEntry iClasspathEntry = (IClasspathEntry) Mockingbird.getProxyObject(IClasspathEntry.class);
        IClasspathEntry iClasspathEntry2 = (IClasspathEntry) Mockingbird.getProxyObject(IClasspathEntry.class);
        IPath iPath = (IPath) Mockingbird.getProxyObject(IPath.class);
        IWorkspace iWorkspace = (IWorkspace) Mockingbird.getProxyObject(IWorkspace.class);
        IWorkspaceRoot iWorkspaceRoot = (IWorkspaceRoot) Mockingbird.getProxyObject(IWorkspaceRoot.class);
        Mockingbird.setReturnValue(iJavaProject.getResolvedClasspath(true), iClasspathEntrys);
        iClasspathEntrys[0] = iClasspathEntry;
        iClasspathEntrys[1] = iClasspathEntry2;
        Mockingbird.setReturnValue(false, iClasspathEntry, "getEntryKind", "()int", new Integer(0), 1);
        Mockingbird.setReturnValue(false, iClasspathEntry2, "getEntryKind", "()int", new Integer(3), 1);
        Mockingbird.setReturnValue(iJavaProject.getOutputLocation(), null);
        Mockingbird.setReturnValue(false, iClasspathEntry2, "getOutputLocation", "()org.eclipse.core.runtime.IPath", iPath, 1);
        Mockingbird.setReturnValue(ResourcesPlugin.getWorkspace(), iWorkspace);
        Mockingbird.setReturnValue(iWorkspace.getRoot(), iWorkspaceRoot);
        Mockingbird.setReturnValue(iWorkspaceRoot.findMember(iPath), null);
        Mockingbird.enterTestMode(EclipseCrapProjectBuilder.class);
        ArrayList result = (ArrayList) callPrivateMethod("org.crap4j.crap4jeclipse.actions.EclipseCrapProjectBuilder", "getAllClassDirs_FullyResolved", new Class[] { IJavaProject.class }, eclipseCrapProjectBuilder, new Object[] { iJavaProject });
        assertEquals("result.size()", 0, result.size());
    }

    public void testGetCrapProjectWithAggressiveMocks() throws Throwable {
        EclipseCrapProjectBuilder eclipseCrapProjectBuilder = (EclipseCrapProjectBuilder) Mockingbird.getProxyObject(EclipseCrapProjectBuilder.class, true);
        setPrivateField(eclipseCrapProjectBuilder, "crapProject", null);
        Mockingbird.enterTestMode(EclipseCrapProjectBuilder.class);
        CrapProject result = eclipseCrapProjectBuilder.getCrapProject();
        assertNull("result", result);
    }

    public void testGetOutputLocationWithAggressiveMocks() throws Throwable {
        EclipseCrapProjectBuilder eclipseCrapProjectBuilder = (EclipseCrapProjectBuilder) Mockingbird.getProxyObject(EclipseCrapProjectBuilder.class, true);
        IClasspathEntry iClasspathEntry = (IClasspathEntry) Mockingbird.getProxyObject(IClasspathEntry.class);
        IPath iPath = (IPath) Mockingbird.getProxyObject(IPath.class);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(iClasspathEntry.getOutputLocation(), iPath);
        Mockingbird.enterTestMode(EclipseCrapProjectBuilder.class);
        IPath result = (IPath) callPrivateMethod("org.crap4j.crap4jeclipse.actions.EclipseCrapProjectBuilder", "getOutputLocation", new Class[] { IClasspathEntry.class, IPath.class }, eclipseCrapProjectBuilder, new Object[] { iClasspathEntry, null });
        assertNotNull("result", result);
    }

    public void testGetOutputLocationWithAggressiveMocks1() throws Throwable {
        EclipseCrapProjectBuilder eclipseCrapProjectBuilder = (EclipseCrapProjectBuilder) Mockingbird.getProxyObject(EclipseCrapProjectBuilder.class, true);
        IClasspathEntry iClasspathEntry = (IClasspathEntry) Mockingbird.getProxyObject(IClasspathEntry.class);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(iClasspathEntry.getOutputLocation(), null);
        Mockingbird.enterTestMode(EclipseCrapProjectBuilder.class);
        IPath result = (IPath) callPrivateMethod("org.crap4j.crap4jeclipse.actions.EclipseCrapProjectBuilder", "getOutputLocation", new Class[] { IClasspathEntry.class, IPath.class }, eclipseCrapProjectBuilder, new Object[] { iClasspathEntry, null });
        assertNull("result", result);
    }

    public void testGetResolvedClasspathEntriesWithAggressiveMocks() throws Throwable {
        EclipseCrapProjectBuilder eclipseCrapProjectBuilder = (EclipseCrapProjectBuilder) Mockingbird.getProxyObject(EclipseCrapProjectBuilder.class, true);
        Mockingbird.enterRecordingMode();
        ArrayList arrayList = (ArrayList) Mockingbird.getProxyObject(ArrayList.class);
        Mockingbird.replaceObjectForRecording(ArrayList.class, "<init>()", arrayList);
        IRuntimeClasspathEntry[] iRuntimeClasspathEntrys = new IRuntimeClasspathEntry[2];
        IRuntimeClasspathEntry iRuntimeClasspathEntry = (IRuntimeClasspathEntry) Mockingbird.getProxyObject(IRuntimeClasspathEntry.class);
        IRuntimeClasspathEntry iRuntimeClasspathEntry2 = (IRuntimeClasspathEntry) Mockingbird.getProxyObject(IRuntimeClasspathEntry.class);
        IRuntimeClasspathEntry[] iRuntimeClasspathEntrys2 = new IRuntimeClasspathEntry[2];
        IRuntimeClasspathEntry iRuntimeClasspathEntry3 = (IRuntimeClasspathEntry) Mockingbird.getProxyObject(IRuntimeClasspathEntry.class);
        Mockingbird.setReturnValue(JavaRuntime.computeUnresolvedRuntimeClasspath((IJavaProject) null), iRuntimeClasspathEntrys);
        iRuntimeClasspathEntrys[0] = iRuntimeClasspathEntry;
        iRuntimeClasspathEntrys[1] = iRuntimeClasspathEntry2;
        iRuntimeClasspathEntrys2[0] = iRuntimeClasspathEntry3;
        iRuntimeClasspathEntrys2[1] = (IRuntimeClasspathEntry) Mockingbird.getProxyObject(IRuntimeClasspathEntry.class);
        Mockingbird.setReturnValue(false, iRuntimeClasspathEntry, "getClasspathProperty", "()int", new Integer(1), 1);
        Mockingbird.setReturnValue(false, iRuntimeClasspathEntry2, "getClasspathProperty", "()int", new Integer(0), 1);
        Mockingbird.setReturnValue(true, JavaRuntime.class, "resolveRuntimeClasspathEntry", "(org.eclipse.jdt.launching.IRuntimeClasspathEntry,org.eclipse.jdt.core.IJavaProject)org.eclipse.jdt.launching.IRuntimeClasspathEntry[]", iRuntimeClasspathEntrys2, 1);
        Boolean boolean2 = Boolean.FALSE;
        Mockingbird.setReturnValue(false, arrayList, "add", "(java.lang.Object)boolean", boolean2, 1);
        Mockingbird.setReturnValue(false, arrayList, "add", "(java.lang.Object)boolean", boolean2, 1);
        Mockingbird.enterTestMode(EclipseCrapProjectBuilder.class);
        List result = (List) callPrivateMethod("org.crap4j.crap4jeclipse.actions.EclipseCrapProjectBuilder", "getResolvedClasspathEntries", new Class[] { IJavaProject.class }, eclipseCrapProjectBuilder, new Object[] { null });
        assertNotNull("result", result);
    }

    public void testGetSrcDirsWithAggressiveMocks() throws Throwable {
        EclipseCrapProjectBuilder eclipseCrapProjectBuilder = (EclipseCrapProjectBuilder) Mockingbird.getProxyObject(EclipseCrapProjectBuilder.class, true);
        IJavaProject iJavaProject = (IJavaProject) Mockingbird.getProxyObject(IJavaProject.class);
        Mockingbird.enterRecordingMode();
        ArrayList arrayList = (ArrayList) Mockingbird.getProxyObject(ArrayList.class);
        Mockingbird.replaceObjectForRecording(ArrayList.class, "<init>()", arrayList);
        IPackageFragmentRoot[] iPackageFragmentRoots = new IPackageFragmentRoot[2];
        IPackageFragmentRoot iPackageFragmentRoot = (IPackageFragmentRoot) Mockingbird.getProxyObject(IPackageFragmentRoot.class);
        IPackageFragmentRoot iPackageFragmentRoot2 = (IPackageFragmentRoot) Mockingbird.getProxyObject(IPackageFragmentRoot.class);
        IResource iResource = (IResource) Mockingbird.getProxyObject(IResource.class);
        IPath iPath = (IPath) Mockingbird.getProxyObject(IPath.class);
        File file = (File) Mockingbird.getProxyObject(File.class);
        IResource iResource2 = (IResource) Mockingbird.getProxyObject(IResource.class);
        IPath iPath2 = (IPath) Mockingbird.getProxyObject(IPath.class);
        File file2 = (File) Mockingbird.getProxyObject(File.class);
        Mockingbird.setReturnValue(iJavaProject.getPackageFragmentRoots(), iPackageFragmentRoots);
        iPackageFragmentRoots[0] = iPackageFragmentRoot;
        iPackageFragmentRoots[1] = iPackageFragmentRoot2;
        Mockingbird.setReturnValue(false, iPackageFragmentRoot, "getKind", "()int", new Integer(1), 1);
        Mockingbird.setReturnValue(false, iPackageFragmentRoot, "getResource", "()org.eclipse.core.resources.IResource", iResource, 1);
        Mockingbird.setReturnValue(iResource.getLocation(), iPath);
        Mockingbird.setReturnValue(iPath.toFile(), file);
        Mockingbird.setReturnValue(false, file, "getAbsolutePath", "()java.lang.String", new Object[] {}, "", 1);
        Boolean boolean2 = Boolean.FALSE;
        Mockingbird.setReturnValue(false, arrayList, "add", "(java.lang.Object)boolean", boolean2, 1);
        Mockingbird.setReturnValue(false, iPackageFragmentRoot2, "getKind", "()int", new Integer(1), 1);
        Mockingbird.setReturnValue(false, iPackageFragmentRoot2, "getResource", "()org.eclipse.core.resources.IResource", iResource2, 1);
        Mockingbird.setReturnValue(iResource2.getLocation(), iPath2);
        Mockingbird.setReturnValue(iPath2.toFile(), file2);
        Mockingbird.setReturnValue(false, file2, "getAbsolutePath", "()java.lang.String", new Object[] {}, "", 1);
        Mockingbird.setReturnValue(false, arrayList, "add", "(java.lang.Object)boolean", boolean2, 1);
        Mockingbird.enterTestMode(EclipseCrapProjectBuilder.class);
        List result = (List) callPrivateMethod("org.crap4j.crap4jeclipse.actions.EclipseCrapProjectBuilder", "getSrcDirs", new Class[] { IJavaProject.class }, eclipseCrapProjectBuilder, new Object[] { iJavaProject });
        assertNotNull("result", result);
    }

    public void testGetSrcDirsWithAggressiveMocks1() throws Throwable {
        EclipseCrapProjectBuilder eclipseCrapProjectBuilder = (EclipseCrapProjectBuilder) Mockingbird.getProxyObject(EclipseCrapProjectBuilder.class, true);
        IJavaProject iJavaProject = (IJavaProject) Mockingbird.getProxyObject(IJavaProject.class);
        Mockingbird.enterRecordingMode();
        ArrayList arrayList = (ArrayList) Mockingbird.getProxyObject(ArrayList.class);
        Mockingbird.replaceObjectForRecording(ArrayList.class, "<init>()", arrayList);
        IPackageFragmentRoot[] iPackageFragmentRoots = new IPackageFragmentRoot[2];
        IPackageFragmentRoot iPackageFragmentRoot = (IPackageFragmentRoot) Mockingbird.getProxyObject(IPackageFragmentRoot.class);
        IPackageFragmentRoot iPackageFragmentRoot2 = (IPackageFragmentRoot) Mockingbird.getProxyObject(IPackageFragmentRoot.class);
        IResource iResource = (IResource) Mockingbird.getProxyObject(IResource.class);
        IPath iPath = (IPath) Mockingbird.getProxyObject(IPath.class);
        File file = (File) Mockingbird.getProxyObject(File.class);
        Mockingbird.setReturnValue(iJavaProject.getPackageFragmentRoots(), iPackageFragmentRoots);
        iPackageFragmentRoots[0] = iPackageFragmentRoot;
        iPackageFragmentRoots[1] = iPackageFragmentRoot2;
        Mockingbird.setReturnValue(false, iPackageFragmentRoot, "getKind", "()int", new Integer(0), 1);
        Mockingbird.setReturnValue(false, iPackageFragmentRoot2, "getKind", "()int", new Integer(1), 1);
        Mockingbird.setReturnValue(false, iPackageFragmentRoot2, "getResource", "()org.eclipse.core.resources.IResource", iResource, 1);
        Mockingbird.setReturnValue(iResource.getLocation(), iPath);
        Mockingbird.setReturnValue(iPath.toFile(), file);
        Mockingbird.setReturnValue(false, file, "getAbsolutePath", "()java.lang.String", new Object[] {}, "", 1);
        Mockingbird.setReturnValue(false, arrayList, "add", "(java.lang.Object)boolean", Boolean.FALSE, 1);
        Mockingbird.enterTestMode(EclipseCrapProjectBuilder.class);
        List result = (List) callPrivateMethod("org.crap4j.crap4jeclipse.actions.EclipseCrapProjectBuilder", "getSrcDirs", new Class[] { IJavaProject.class }, eclipseCrapProjectBuilder, new Object[] { iJavaProject });
        assertNotNull("result", result);
    }

    public void testIsTestFolderWithAggressiveMocks() throws Throwable {
        EclipseCrapProjectBuilder eclipseCrapProjectBuilder = (EclipseCrapProjectBuilder) Mockingbird.getProxyObject(EclipseCrapProjectBuilder.class, true);
        Mockingbird.enterTestMode(EclipseCrapProjectBuilder.class);
        boolean result = ((Boolean) callPrivateMethod("org.crap4j.crap4jeclipse.actions.EclipseCrapProjectBuilder", "isTestFolder", new Class[] { String.class }, eclipseCrapProjectBuilder, new Object[] { "" })).booleanValue();
        assertFalse("result", result);
    }

    public void testMakeResourceAbsoluteWithAggressiveMocks() throws Throwable {
        EclipseCrapProjectBuilder eclipseCrapProjectBuilder = (EclipseCrapProjectBuilder) Mockingbird.getProxyObject(EclipseCrapProjectBuilder.class, true);
        IResource iResource = (IResource) Mockingbird.getProxyObject(IResource.class);
        IPath iPath = (IPath) Mockingbird.getProxyObject(IPath.class);
        File file = (File) Mockingbird.getProxyObject(File.class);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(iResource.getLocation(), iPath);
        Mockingbird.setReturnValue(iPath.toFile(), file);
        Mockingbird.setReturnValue(false, file, "getAbsolutePath", "()java.lang.String", new Object[] {}, "", 1);
        Mockingbird.enterTestMode(EclipseCrapProjectBuilder.class);
        String result = (String) callPrivateMethod("org.crap4j.crap4jeclipse.actions.EclipseCrapProjectBuilder", "makeResourceAbsolute", new Class[] { IResource.class }, eclipseCrapProjectBuilder, new Object[] { iResource });
        assertEquals("result", "", result);
    }

    public void testConstructorThrowsNullPointerException() throws Throwable {
        try {
            new EclipseCrapProjectBuilder(new JavaProject(), "testEclipseCrapProjectBuilderParam2");
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(JavaProject.class, ex);
        }
    }
}
