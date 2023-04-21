package com.ibm.wala.refactoring.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import com.ibm.wala.cast.ir.ssa.AstIRFactory.AstIR;
import com.ibm.wala.cast.java.client.JDTJavaSourceAnalysisEngine;
import com.ibm.wala.cast.java.client.JavaSourceAnalysisEngine;
import com.ibm.wala.cast.java.ipa.callgraph.JavaSourceAnalysisScope;
import com.ibm.wala.cast.java.loader.JavaSourceLoaderImpl.ConcreteJavaMethod;
import com.ibm.wala.cast.java.translator.jdt.JDTJava2CAstTranslator.JdtPosition;
import com.ibm.wala.classLoader.BinaryDirectoryTreeModule;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.classLoader.JarFileModule;
import com.ibm.wala.classLoader.Module;
import com.ibm.wala.classLoader.SourceDirectoryTreeModule;
import com.ibm.wala.classLoader.SourceFileModule;
import com.ibm.wala.ide.classloader.EclipseSourceFileModule;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.Entrypoint;
import com.ibm.wala.ipa.callgraph.impl.DefaultEntrypoint;
import com.ibm.wala.ipa.callgraph.propagation.ArrayContentsKey;
import com.ibm.wala.ipa.callgraph.propagation.InstanceFieldKey;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.callgraph.propagation.PointerKey;
import com.ibm.wala.ipa.callgraph.propagation.StaticFieldKey;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.ipa.modref.ArrayLengthKey;
import com.ibm.wala.ipa.slicer.HeapStatement;
import com.ibm.wala.ipa.slicer.HeapStatement.HeapReturnCallee;
import com.ibm.wala.ipa.slicer.HeapStatement.HeapReturnCaller;
import com.ibm.wala.ipa.slicer.NormalReturnCallee;
import com.ibm.wala.ipa.slicer.NormalStatement;
import com.ibm.wala.ipa.slicer.PDG;
import com.ibm.wala.ipa.slicer.Statement;
import com.ibm.wala.ipa.slicer.Statement.Kind;
import com.ibm.wala.ipa.slicer.StatementWithInstructionIndex;
import com.ibm.wala.refactoring.Activator;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAArrayStoreInstruction;
import com.ibm.wala.ssa.SSAFieldAccessInstruction;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.types.FieldReference;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeName;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.collections.Pair;
import com.ibm.wala.util.debug.Assertions;
import com.ibm.wala.util.intset.OrdinalSet;
import com.ibm.wala.util.io.FileProvider;

/**
 * Uility methods for creating the WALA engine 
 * @author Alexander Libov
 *
 */
public abstract class WALAUtils {

    /**
	 * please use the method with class name as an argument
	 * this method assumes filename = classname
	 * @param fileName
	 * @param project
	 * @param methodName
	 * @return
	 * @throws Exception
	 */
    @Deprecated
    public static JDTJavaSourceAnalysisEngine buildEngine(final String fileName, IJavaProject project, final String methodName) throws Exception {
        IPath path = new Path(fileName);
        String className = path.lastSegment();
        className = className.substring(0, className.indexOf('.'));
        return buildEngine(fileName, project, methodName, className);
    }

    public static String REGRESSION_EXCLUSIONS = "Java60RegressionExclusions.txt";

    public static JDTJavaSourceAnalysisEngine buildEngine(final String fileName, IJavaProject project, final String methodName, String className) throws Exception {
        IPath path = new Path(fileName);
        String entrypoint = "L" + path.removeFirstSegments(1).removeLastSegments(1) + "/" + className;
        final TypeReference T = TypeReference.findOrCreate(JavaSourceAnalysisScope.SOURCE, TypeName.string2TypeName(entrypoint));
        JDTJavaSourceAnalysisEngine engine = new JDTJavaSourceAnalysisEngine(project) {

            protected Iterable<Entrypoint> makeDefaultEntrypoints(AnalysisScope scope, IClassHierarchy cha) {
                final IClassHierarchy fcha = cha;
                IClass requredClass = null;
                requredClass = fcha.lookupClass(T);
                final LinkedList<com.ibm.wala.classLoader.IMethod> allMethods = new LinkedList<com.ibm.wala.classLoader.IMethod>(requredClass.getAllMethods());
                final LinkedList<com.ibm.wala.classLoader.IMethod> methods = new LinkedList<com.ibm.wala.classLoader.IMethod>(allMethods);
                if (methodName != null) {
                    for (IMethod m : allMethods) {
                        if (!m.getName().toString().equals(methodName)) {
                            methods.remove(m);
                        }
                    }
                }
                if (methods.isEmpty()) {
                    throw new RuntimeException("couldn't find the method " + methodName + " in " + fileName);
                }
                return new Iterable<Entrypoint>() {

                    @Override
                    public Iterator<Entrypoint> iterator() {
                        return new Iterator<Entrypoint>() {

                            private int index = 0;

                            @Override
                            public boolean hasNext() {
                                return index < methods.size();
                            }

                            @Override
                            public Entrypoint next() {
                                return new DefaultEntrypoint(methods.get(index++), fcha);
                            }

                            @Override
                            public void remove() {
                                Assertions.UNREACHABLE();
                            }
                        };
                    }
                };
            }
        };
        String exclusionsFileAbsolutePath = FileProvider.getFileFromPlugin(Activator.getDefault(), REGRESSION_EXCLUSIONS).getAbsolutePath();
        engine.setExclusionsFile(exclusionsFileAbsolutePath);
        populateEngineScope(engine, project);
        return engine;
    }

    /**
	 * please use the method with method name and class name as an argument
	 * this method assumes filename = classname and generated all possible entrypoints
	 * @param fileName
	 * @param project
	 * @param methodName
	 * @return
	 * @throws Exception
	 */
    @Deprecated
    public static JDTJavaSourceAnalysisEngine buildEngine(String fileName, IJavaProject project) throws Exception {
        return buildEngine(fileName, project, null);
    }

    /**
	 * This function adds the sources and the libraries to the engine. 
	 * @param engine - the analysis engine
	 * @param project - the java project.
	 * @throws IOException
	 */
    private static void populateEngineScope(JavaSourceAnalysisEngine engine, IJavaProject project) throws IOException {
        Set<String> libs = new HashSet<String>();
        Set<String> sources = new HashSet<String>();
        IWorkspace w = null;
        try {
            if (project != null) {
                w = ResourcesPlugin.getWorkspace();
                for (IPackageFragment mypackage : project.getPackageFragments()) {
                    if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
                        for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
                            sources.add(unit.getPath().removeFirstSegments(1).toString());
                        }
                    } else if (mypackage.getKind() == IPackageFragmentRoot.K_BINARY) {
                        libs.add(mypackage.getParent().getPath().toString());
                    } else {
                        System.err.println("unsupported package kind");
                    }
                }
            }
        } catch (IllegalStateException e) {
        } catch (JavaModelException e) {
            e.printStackTrace();
        }
        boolean foundLib = false;
        for (String lib : libs) {
            File libFile = new File(lib);
            if (!libFile.exists()) {
                continue;
            }
            foundLib = true;
            Module m;
            if (libFile.isFile()) {
                m = new JarFileModule(new JarFile(libFile));
            } else {
                m = new BinaryDirectoryTreeModule(libFile);
            }
            engine.addSystemModule(m);
        }
        Assert.isTrue(foundLib, "couldn't find library file from " + libs);
        for (String srcFilePath : sources) {
            if (w != null) {
                IFile file = project.getProject().getFile(srcFilePath);
                try {
                    engine.addSourceModule(EclipseSourceFileModule.createEclipseSourceFileModule(file));
                } catch (IllegalArgumentException e) {
                    Assert.isTrue(false, e.getMessage());
                }
            } else {
                String srcFileName = srcFilePath.substring(srcFilePath.lastIndexOf(File.separator) + 1);
                File f = new File(srcFilePath);
                Assert.isTrue(f.exists(), "couldn't find " + srcFilePath);
                if (f.isDirectory()) {
                    engine.addSourceModule(new SourceDirectoryTreeModule(f));
                } else {
                    engine.addSourceModule(new SourceFileModule(f, srcFileName));
                }
            }
        }
    }

    public static IJavaProject getNamedProject(String projectName) {
        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        IJavaModel javaModel = JavaCore.create(workspaceRoot);
        IJavaProject helloWorldProject = javaModel.getJavaProject(projectName);
        return helloWorldProject;
    }

    /**
	 * please use the method with method name and class name as an argument
	 * this method assumes filename = classname and generated all possible entrypoints
	 * @param fileName
	 * @param project
	 * @param methodName
	 * @return
	 * @throws Exception
	 */
    @Deprecated
    public static JDTJavaSourceAnalysisEngine buildEngine(String fileName, String projName) throws Exception {
        return buildEngine(fileName, getNamedProject(projName));
    }

    /**
	 * Search the CallGraph for a concrete method specific name.
	 * @param cg - a CallGraph to look in. 
	 * @param methodName - String contains the name we will look for.
	 * @return a CGNode contains the method with that name, or null if there is no such CGNode.
	 *
    @Deprecated
	public static CGNode findCGNode(CallGraph cg, String methodName) {
		for (int i=0; i< cg.getNumberOfNodes(); i++) {
			CGNode cgn = cg.getNode(i);
			if (!(cgn.getMethod() instanceof ConcreteJavaMethod)) continue;
			ConcreteJavaMethod cjm = (ConcreteJavaMethod) cgn.getMethod();
			if (cjm.getName().toString().equals(methodName))
				return cgn;
		}
		return null;
	}
    */
    public static CGNode findCGNode(CallGraph cg, MethodReference reference) {
        for (int i = 0; i < cg.getNumberOfNodes(); i++) {
            CGNode cgn = cg.getNode(i);
            if (!(cgn.getMethod() instanceof ConcreteJavaMethod)) continue;
            ConcreteJavaMethod cjm = (ConcreteJavaMethod) cgn.getMethod();
            if (cjm.getDescriptor().equals(reference.getDescriptor())) return cgn;
        }
        return null;
    }

    /**
	 * Search the CallGraph for a concrete method in line.
	 * @param cg - a CallGraph to look in. 
	 * @param methodFirstLine - number of first line of the method
	 * @return a CGNode contains the method with in that line, or null if there is no such CGNode.
	 */
    public static CGNode findCGNode(CallGraph cg, int methodStartPosition) {
        for (int i = 0; i < cg.getNumberOfNodes(); i++) {
            CGNode cgn = cg.getNode(i);
            if (!(cgn.getMethod() instanceof ConcreteJavaMethod)) continue;
            ConcreteJavaMethod cjm = (ConcreteJavaMethod) cgn.getMethod();
            if (cjm.getSourcePosition().getFirstOffset() == methodStartPosition) {
                return cgn;
            }
        }
        return null;
    }

    /**
	 * please use the method with class name as an argument
	 * this method assumes filename = classname 
	 * @param fileName
	 * @param project
	 * @param methodName
	 * @return
	 * @throws Exception
	 */
    @Deprecated
    public static JDTJavaSourceAnalysisEngine buildEngine(String fileName, String projName, String functionName) throws Exception {
        return buildEngine(fileName, getNamedProject(projName), functionName);
    }

    /**
	 * finds the node that has the line selectionline2
	 * @param cg callgraph to search in
	 * @param selectionLine2 line to look for
	 * @return the node that contains the line
	 */
    public static CGNode findNode(CallGraph cg, int selectionLine2) {
        int nodesNum = cg.getNumberOfNodes();
        CGNode retVal = null;
        for (int i = 0; i < nodesNum; i++) {
            CGNode n = cg.getNode(i);
            if (!(n.getMethod() instanceof ConcreteJavaMethod)) continue;
            ConcreteJavaMethod m = ((ConcreteJavaMethod) n.getMethod());
            IR ir = n.getIR();
            SSAInstruction[] insts = ir.getInstructions();
            for (int j = insts.length - 1; j >= 0; j--) {
                if (insts[j] != null) {
                    int ln = m.getLineNumber(j);
                    if (ln == selectionLine2) {
                        retVal = n;
                    }
                }
            }
        }
        return retVal;
    }

    public static JDTJavaSourceAnalysisEngine buildEngine(String compUnitName, String projectName, String methodName, String className) throws Exception {
        return buildEngine(compUnitName, getNamedProject(projectName), methodName, className);
    }

    /**
	 * gets the statement corresponding to the selection line number and
	 * the selected variable.
	 * used as base of the slice
	 * @param n the node in which to look for
	 * @return the statement 
	 * @throws InvalidClassFileException
	 */
    public static Statement getStatement(CGNode n, int selectedLine, String selectedName, Map<CGNode, OrdinalSet<PointerKey>> mod) throws InvalidClassFileException {
        AstIR ir = (AstIR) n.getIR();
        ConcreteJavaMethod m = ((ConcreteJavaMethod) n.getMethod());
        SSAInstruction[] insts = ir.getInstructions();
        for (int i = insts.length - 1; i >= 0; i--) {
            if (insts[i] != null) {
                if (m.getLineNumber(i) == selectedLine) {
                    if (insts[i] instanceof com.ibm.wala.ssa.SSAAbstractInvokeInstruction) {
                        PointerKey loc = null;
                        Iterator<PointerKey> it = mod.get(n).iterator();
                        while (it.hasNext()) {
                            PointerKey pk = it.next();
                            if (pk instanceof InstanceFieldKey && ((InstanceFieldKey) pk).getField().getName().toString().equals(selectedName)) {
                                loc = pk;
                                break;
                            }
                            if (pk instanceof InstanceFieldKey) {
                                continue;
                            }
                            if (pk instanceof StaticFieldKey && ((StaticFieldKey) pk).getField().getName().toString().equals(selectedName)) {
                                loc = pk;
                                break;
                            }
                            if (pk instanceof StaticFieldKey) {
                                continue;
                            }
                            if (pk instanceof ArrayContentsKey) {
                                continue;
                            }
                        }
                        if (loc == null) {
                            continue;
                        }
                        return new com.ibm.wala.ipa.slicer.HeapStatement.HeapReturnCaller(n, i, loc);
                    }
                    if (insts[i] instanceof SSAFieldAccessInstruction && ((SSAFieldAccessInstruction) insts[i]).getDeclaredField().getName().toString().equals(selectedName)) {
                        return new com.ibm.wala.ipa.slicer.NormalStatement(n, i);
                    }
                    for (int j = 0; j < insts[i].getNumberOfDefs(); j++) {
                        int def = insts[i].getDef(j);
                        String[] names = ir.getLocalNames(i, def);
                        if (names != null && Arrays.asList(names).contains(selectedName)) return new com.ibm.wala.ipa.slicer.NormalStatement(n, i);
                    }
                    for (int j = 0; j < insts[i].getNumberOfUses(); j++) {
                        int def = insts[i].getUse(j);
                        String[] names = ir.getLocalNames(i, def);
                        if (names != null && Arrays.asList(names).contains(selectedName)) return new com.ibm.wala.ipa.slicer.NormalStatement(n, i);
                    }
                }
            }
        }
        return null;
    }

    public static Set<Statement> getAllReturnStatements(CGNode node) {
        Set<Statement> retVal = new HashSet<Statement>();
        PDG pdg = WALAwrapper.getSDG().getPDG(node);
        Iterator<Statement> nodeIter = pdg.iterator();
        while (nodeIter.hasNext()) {
            Statement stmt = nodeIter.next();
            if (!(stmt instanceof NormalReturnCallee)) {
                continue;
            }
            retVal.add(stmt);
        }
        return retVal;
    }

    public static Set<Statement> getAllHeapReturnCaleeStatements(CGNode node) {
        Set<Statement> retVal = new HashSet<Statement>();
        PDG pdg = WALAwrapper.getSDG().getPDG(node);
        Iterator<Statement> nodeIter = pdg.iterator();
        while (nodeIter.hasNext()) {
            Statement stmt = nodeIter.next();
            int instrIndex = getStatmentInstructionIndex(stmt);
            if (instrIndex != -1) {
            }
            if (!(stmt instanceof HeapReturnCallee)) {
                continue;
            }
            HeapStatement hpc = (HeapStatement) stmt;
            PointerKey pk = hpc.getLocation();
            if (pk instanceof StaticFieldKey || pk instanceof InstanceFieldKey) {
                retVal.add(stmt);
            } else if (pk instanceof ArrayLengthKey) {
                InstanceKey ik = ((ArrayLengthKey) pk).getInstanceKey();
                IClass ic = ik.getConcreteType();
                System.out.println(ic.getName().getClassName().toString());
                continue;
            } else if (pk instanceof ArrayContentsKey) {
                InstanceKey ik = ((ArrayContentsKey) pk).getInstanceKey();
                IClass ic = ik.getConcreteType();
                System.out.println(ic.getName().getClassName().toString());
                continue;
            } else {
                throw new RuntimeException("non StaticFieldKey or InstanceFieldKey in getAllHeapReturnStatements");
            }
        }
        return retVal;
    }

    public static void removeFinalUseEgdes(CGNode node, Set<Statement> heapReturnCalleeStatments) {
        PDG pdg = WALAwrapper.getSDG().getPDG(node);
        Map<Statement, Set<FieldReference>> finalUseMap = VarsUtils.finalUseKeys.get(node);
        Set<Pair<Statement, Statement>> finalUseEdges = new HashSet<Pair<Statement, Statement>>();
        for (Statement heapReturnCalleeStatement : heapReturnCalleeStatments) {
            if (!(heapReturnCalleeStatement.getKind() == Kind.HEAP_RET_CALLEE)) continue;
            PointerKey pk = ((HeapStatement.HeapReturnCallee) heapReturnCalleeStatement).getLocation();
            FieldReference fieldRef = WALAUtils.pointerKeyToFieldReference(pk);
            Iterator<Statement> defIterator = pdg.getPredNodes(heapReturnCalleeStatement);
            while (defIterator.hasNext()) {
                Statement finalDefStatement = defIterator.next();
                Iterator<Statement> useIterator = pdg.getSuccNodes(finalDefStatement);
                while (useIterator.hasNext()) {
                    Statement useStatement = useIterator.next();
                    if (finalUseMap.get(useStatement).contains(fieldRef)) {
                        finalUseEdges.add(Pair.make(finalDefStatement, useStatement));
                    }
                }
            }
        }
        for (Pair<Statement, Statement> finalUseEdge : finalUseEdges) {
            Statement finalDefStatement = finalUseEdge.fst;
            Statement finalUseStatement = finalUseEdge.snd;
            pdg.removeEdge(finalDefStatement, finalUseStatement);
            System.out.println("removing final use egde: " + WALAUtils.getStatementInfo(finalDefStatement) + " - " + WALAUtils.getStatementInfo(finalUseStatement));
        }
    }

    public static Set<Statement> getAllHeapReturnStatements(CGNode node, Set<String> varNames) {
        HashSet<Statement> result = new HashSet<Statement>();
        PDG pdg = WALAwrapper.getSDG().getPDG(node);
        Iterator<Statement> nodeIter = pdg.iterator();
        while (nodeIter.hasNext()) {
            Statement stmt = nodeIter.next();
            if (stmt instanceof HeapStatement) {
                HeapStatement hpc = (HeapStatement) stmt;
                PointerKey pk = hpc.getLocation();
                String varName = null;
                if (pk instanceof StaticFieldKey) {
                    varName = ((StaticFieldKey) pk).getField().getName().toString();
                } else if (pk instanceof InstanceFieldKey) {
                    varName = ((InstanceFieldKey) pk).getField().getName().toString();
                } else if (pk instanceof ArrayLengthKey) {
                    InstanceKey ik = ((ArrayLengthKey) pk).getInstanceKey();
                    IClass ic = ik.getConcreteType();
                    System.out.println(ic.getName().getClassName().toString());
                    continue;
                } else if (pk instanceof ArrayContentsKey) {
                    InstanceKey ik = ((ArrayContentsKey) pk).getInstanceKey();
                    IClass ic = ik.getConcreteType();
                    System.out.println(ic.getName().getClassName().toString());
                    continue;
                } else {
                    throw new RuntimeException("non StaticFieldKey or InstanceFieldKey in getAllHeapReturnStatements");
                }
                if (varNames.contains(varName)) {
                    result.add(stmt);
                    continue;
                }
            }
        }
        return result;
    }

    /**
	 * Check if the method modify any non-local variables.
	 * @param functionName - String contains the method name we want to examine.
	 * @return whether or not the method changes outside variables. 
	 * @throws Exception if there is no such method, or one of the analysis steps failed.
	 */
    public static boolean isFunctionModify(int position) throws Exception {
        try {
            Map<CGNode, OrdinalSet<PointerKey>> mod = WALAwrapper.getMod();
            CGNode cgn = findCGNode(WALAwrapper.getCallGraph(), position);
            if (cgn == null) throw new RuntimeException("function in position: " + position + " doesn't exist");
            return !mod.get(cgn).isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Can't perform the isFunctionModify check!");
        }
    }

    public static FieldReference pointerKeyToFieldReference(PointerKey pk) {
        if (pk instanceof InstanceFieldKey) {
            return ((InstanceFieldKey) pk).getField().getReference();
        }
        if (pk instanceof StaticFieldKey) {
            return ((StaticFieldKey) pk).getField().getReference();
        }
        if (pk instanceof ArrayLengthKey || pk instanceof ArrayContentsKey) {
            return null;
        }
        throw new RuntimeException("unexpected pointer key: " + pk.toString());
    }

    public static Set<String> getDefinedLocalVars(int methodStartPosition, int firstLine, int lastLine) {
        CGNode node = findCGNode(WALAwrapper.getCallGraph(), methodStartPosition);
        if (node == null) {
            throw new RuntimeException("couldn't find CGNode");
        }
        ConcreteJavaMethod jm = ((ConcreteJavaMethod) node.getMethod());
        HashSet<String> result = new HashSet<String>();
        PDG pdg = WALAwrapper.getSDG().getPDG(node);
        Iterator<Statement> nodeIter = pdg.iterator();
        while (nodeIter.hasNext()) {
            Statement stmt = nodeIter.next();
            int instrIndex = getStatmentInstructionIndex(stmt);
            if (instrIndex == -1) {
                continue;
            }
            int lineNum = jm.getLineNumber(instrIndex);
            if (lineNum < firstLine || lineNum > lastLine) {
                continue;
            }
            if (stmt instanceof NormalStatement) {
                populateLocalDefs(result, (NormalStatement) stmt);
            }
        }
        return result;
    }

    public static int getLastDefLineNumber(int methodStartPosition, int firstLine, int lastLine, String varName) {
        CGNode node = findCGNode(WALAwrapper.getCallGraph(), methodStartPosition);
        if (node == null) {
            throw new RuntimeException("couldn't find CGNode");
        }
        ConcreteJavaMethod jm = ((ConcreteJavaMethod) node.getMethod());
        HashSet<String> result = new HashSet<String>();
        PDG pdg = WALAwrapper.getSDG().getPDG(node);
        Iterator<Statement> nodeIter = pdg.iterator();
        int lastDefLine = -1;
        while (nodeIter.hasNext()) {
            Statement stmt = nodeIter.next();
            int instrIndex = getStatmentInstructionIndex(stmt);
            if (instrIndex == -1) {
                continue;
            }
            int lineNum = jm.getLineNumber(instrIndex);
            if (lineNum < firstLine || lineNum > lastLine) {
                continue;
            }
            if (stmt instanceof NormalStatement) {
                result.clear();
                populateLocalDefs(result, (NormalStatement) stmt);
                if (result.contains(varName)) {
                    lastDefLine = lineNum;
                }
            }
        }
        return lastDefLine;
    }

    public static void populateLocalDefs(Set<String> retVal, NormalStatement s) {
        int instructionIndex = s.getInstructionIndex();
        if (s.getNode().getMethod() instanceof ConcreteJavaMethod) {
            AstIR ir = (AstIR) s.getNode().getIR();
            SSAInstruction[] insts = ir.getInstructions();
            for (int j = 0; j < insts[instructionIndex].getNumberOfDefs(); j++) {
                int def = insts[instructionIndex].getDef(j);
                String[] names = ir.getLocalNames(instructionIndex, def);
                if (names != null) {
                    for (String name : names) {
                        retVal.add(name);
                    }
                }
            }
            if (insts[instructionIndex] instanceof SSAArrayStoreInstruction) {
                String[] names = ir.getLocalNames(instructionIndex, ((SSAArrayStoreInstruction) insts[instructionIndex]).getArrayRef());
                if (names != null) {
                    for (String name : names) {
                        retVal.add(name);
                    }
                }
            }
        }
    }

    public static int getStatmentInstructionIndex(Statement s) {
        int instructionIndex = -1;
        if (s instanceof StatementWithInstructionIndex) instructionIndex = ((StatementWithInstructionIndex) s).getInstructionIndex(); else if (s.getKind() == Statement.Kind.HEAP_RET_CALLER) instructionIndex = ((HeapReturnCaller) s).getCallIndex();
        return instructionIndex;
    }

    public static String getStatementInfo(Statement s) {
        int instructionIndex = WALAUtils.getStatmentInstructionIndex(s);
        if (instructionIndex == -1) {
            return "no index";
        }
        if (!(s.getNode().getMethod() instanceof ConcreteJavaMethod)) {
            return "not a java method";
        }
        ConcreteJavaMethod jm = ((ConcreteJavaMethod) s.getNode().getMethod());
        int lineNum = jm.getLineNumber(instructionIndex);
        JdtPosition sp = (JdtPosition) jm.getSourcePosition(instructionIndex);
        return "line: " + Integer.toString(lineNum) + " pos: " + Integer.toString(sp.getFirstOffset()) + ":" + Integer.toString(sp.getLastOffset());
    }
}
