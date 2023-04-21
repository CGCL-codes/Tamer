package org.parallelj.designer.typeselector.processor.annotation;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.TypeNameRequestor;
import org.eclipselabs.resourceselector.core.processor.ResourceProcessor;
import org.eclipselabs.resourceselector.processor.java.JavaTypeInfo.JavaTypeVisibility;

/**
 * @Program Annotation Processor for Java Management
 * 
 */
public class AnnotationProcessor extends ResourceProcessor {

    private IJavaProject javaProject;

    private String classFQN;

    private static final String ANNOTATION_PROGRAM_KEY = "Program";

    private static final String ANNOTATED_IMAGE_PATH = "/icons/obj16/pjann-16.png";

    private static final String PACKAGED_PARALLELJ_PROGRAM = "org.parallelj.Program";

    /**
	 * constructor AnnotationProcessor
	 * 
	 * @param classFQN
	 * @param javaProject
	 */
    public AnnotationProcessor(String classFQN, IJavaProject javaProject) {
        this.classFQN = classFQN;
        this.javaProject = javaProject;
    }

    public void process() {
        this.searchResults.clear();
        List<JavaResourceInfo> searchedResults = getPathForElementInClasspath();
        if (searchedResults == null) {
            return;
        }
        JavaResourceMatcher matcher = new JavaResourceMatcher(classFQN);
        for (JavaResourceInfo typeInfo : searchedResults) {
            if (matcher.match(typeInfo.getPackageName(), typeInfo.getElementName())) {
                this.searchResults.add(typeInfo);
            }
        }
    }

    /**
	 * Retrieves the Element for the path.
	 * 
	 * @return List<JavaResourceInfo> containing @Program annotation
	 */
    private List<JavaResourceInfo> getPathForElementInClasspath() {
        IJavaSearchScope javaSearchScope = SearchEngine.createJavaSearchScope(new IJavaElement[] { this.javaProject });
        SearchEngine searchEngine = new SearchEngine();
        final List<JavaResourceInfo> searchedResults = new ArrayList<JavaResourceInfo>();
        TypeNameRequestor typeNameRequestor = new TypeNameRequestor() {

            @Override
            public void acceptType(int modifiers, char[] packageName, char[] simpleTypeName, char[][] enclosingTypeNames, String path) {
                String packageNameAsString = new String(packageName);
                String simpleTypeNameAsString = new String(simpleTypeName);
                String[] enclosingNames = new String[enclosingTypeNames.length];
                for (int i = 0; i < enclosingTypeNames.length; i++) enclosingNames[i] = new String(enclosingTypeNames[i]);
                if (path.contains(javaProject.getPath().toString())) {
                    JavaResourceInfo javaTypeInfo = new JavaResourceInfo(simpleTypeNameAsString, packageNameAsString, path, enclosingNames, modifiers);
                    if (!(javaTypeInfo.isInnerElement() && JavaTypeVisibility.PRIVATE.equals(javaTypeInfo.getTypeVisibility()))) {
                        if (isProgram(getTypeFromPath(path))) {
                            javaTypeInfo.setImage(AnnotationActivator.getDefault().getImage(ANNOTATED_IMAGE_PATH));
                            javaTypeInfo.setContainerImage(AnnotationActivator.getDefault().getImage(ANNOTATED_IMAGE_PATH));
                            searchedResults.add(javaTypeInfo);
                        }
                    }
                }
            }
        };
        String packagePattern = null;
        String typePattern = this.classFQN;
        if (!typePattern.endsWith("*")) typePattern += "*";
        if (typePattern.contains(ResourceProcessor.DOT)) {
            packagePattern = typePattern.substring(0, typePattern.lastIndexOf(ResourceProcessor.DOT));
            typePattern = typePattern.substring(typePattern.lastIndexOf(ResourceProcessor.DOT) + 1);
        }
        try {
            searchEngine.searchAllTypeNames(packagePattern == null ? null : packagePattern.toCharArray(), SearchPattern.R_EXACT_MATCH | SearchPattern.R_PATTERN_MATCH, typePattern.toCharArray(), SearchPattern.R_EXACT_MATCH | SearchPattern.R_PATTERN_MATCH, IJavaSearchConstants.CLASS, javaSearchScope, typeNameRequestor, IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH, null);
        } catch (JavaModelException e) {
            AnnotationActivator.getDefault().logError("An Exception has been thrown while loading IResources in IContainer", e);
        }
        return searchedResults.size() > 0 ? searchedResults : null;
    }

    /**
	 * Retrieves IType in JavaProject, for the TypeNameDescriptor
	 * 
	 * @param javaProject
	 * @param typeNameDescriptor
	 * @return Type
	 */
    private IType getTypeFromPath(String path) {
        String[] pathChunks = path.split("\\|");
        IJavaElement myJavaElement = null;
        if (pathChunks.length == 2) {
            String pathToJar = pathChunks[0];
            String pathToClass = pathChunks[1];
            IPackageFragmentRoot myJarPackageFragmentRoot = null;
            try {
                for (int i = 0; i < javaProject.getAllPackageFragmentRoots().length && myJarPackageFragmentRoot == null; i++) {
                    IPackageFragmentRoot packageFragmentRoot = javaProject.getAllPackageFragmentRoots()[i];
                    String path1 = pathToJar.replace("\\", "/");
                    String path2 = packageFragmentRoot.getPath().toString().replace("\\", "/");
                    if (path1.equals(path2)) myJarPackageFragmentRoot = packageFragmentRoot;
                }
            } catch (JavaModelException e) {
                AnnotationActivator.getDefault().logError("An Exception has been thrown while loading IResources in IContainer", e);
            }
            if (myJarPackageFragmentRoot == null) return null;
            pathToClass = pathToClass.replace("/", ".");
            try {
                for (int i = 0; i < myJarPackageFragmentRoot.getChildren().length && myJavaElement == null; i++) {
                    IJavaElement javaElement = myJarPackageFragmentRoot.getChildren()[i];
                    if (javaElement instanceof IPackageFragment) {
                        IPackageFragment packageFragment = (IPackageFragment) javaElement;
                        for (int j = 0; j < packageFragment.getClassFiles().length && myJavaElement == null; j++) {
                            IClassFile classFile = packageFragment.getClassFiles()[j];
                            String fileName = packageFragment.getElementName() + "." + classFile.getElementName();
                            if (pathToClass.equals(fileName)) myJavaElement = classFile;
                        }
                        for (int j = 0; j < packageFragment.getCompilationUnits().length && myJavaElement == null; j++) {
                            ICompilationUnit compilationUnit = packageFragment.getCompilationUnits()[j];
                            String fileName = packageFragment.getElementName() + "." + compilationUnit.getElementName();
                            if (pathToClass.equals(fileName)) myJavaElement = compilationUnit;
                        }
                    } else if (javaElement instanceof IClassFile) {
                        IClassFile classFile = (IClassFile) javaElement;
                        String fileName = classFile.getElementName();
                        if (pathToClass.equals(fileName)) myJavaElement = classFile;
                    } else if (javaElement instanceof ICompilationUnit) {
                        ICompilationUnit compilationUnit = (ICompilationUnit) javaElement;
                        String fileName = compilationUnit.getElementName();
                        if (pathToClass.equals(fileName)) myJavaElement = compilationUnit;
                    }
                }
            } catch (JavaModelException e) {
                AnnotationActivator.getDefault().logError("An Exception has been thrown while loading IResources in IContainer", e);
            }
        } else if (pathChunks.length == 1) {
            IFile file2 = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(pathChunks[0]));
            if (file2 != null && file2.exists()) myJavaElement = JavaCore.create(file2);
        }
        if (myJavaElement instanceof ICompilationUnit) {
            ICompilationUnit compilationUnit = (ICompilationUnit) myJavaElement;
            return compilationUnit.getType(myJavaElement.getElementName().replace(".java", ""));
        } else if (myJavaElement instanceof IClassFile) {
            IClassFile classFile = (IClassFile) myJavaElement;
            return classFile.getType();
        }
        return null;
    }

    /**
	 * Checks if IType passed as parameter has the Entity annotation
	 * 
	 * @param type
	 * @return boolean
	 * @throws JavaModelException
	 */
    private boolean isProgram(IType type) {
        try {
            if (type.isClass() && type.getAnnotations() != null) {
                IAnnotation[] annotations = type.getAnnotations();
                for (IAnnotation annotation : annotations) {
                    String name = annotation.getElementName();
                    if (name.equals(ANNOTATION_PROGRAM_KEY) || name.equals(PACKAGED_PARALLELJ_PROGRAM)) {
                        return true;
                    }
                }
            }
        } catch (JavaModelException e) {
            AnnotationActivator.getDefault().logError("An Exception has been thrown while loading IResources in IContainer", e);
        }
        return false;
    }
}
