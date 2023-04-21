package org.tockit.cass.javaexport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IParent;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

public class SourceExportJob extends Job {

    private static final int ERROR_CODE_JAVA_MODEL_EXCEPTION = 1;

    private static final int ERROR_CODE_FILE_NOT_FOUND_EXCEPTION = 2;

    private static final String PLUGIN_NAME = "org.tockit.cass.sourceexport";

    private IProgressMonitor progressMonitor;

    private final IJavaProject javaProject;

    private final File targetFile;

    private final String fileFormat;

    private final Pattern[] ignorePattern;

    public SourceExportJob(IJavaProject javaProject, File targetFile, String fileFormat, String[] ignorePattern) {
        super("Export Java source as graph");
        this.javaProject = javaProject;
        this.targetFile = targetFile;
        this.fileFormat = fileFormat;
        this.ignorePattern = new Pattern[ignorePattern.length];
        for (int i = 0; i < ignorePattern.length; i++) {
            this.ignorePattern[i] = Pattern.compile(ignorePattern[i]);
        }
        this.setUser(true);
        this.setRule(javaProject.getSchedulingRule());
    }

    public boolean exportSource() throws JavaModelException, FileNotFoundException {
        progressMonitor.beginTask("Exporting Java source graph", 3);
        Model model = ModelFactory.createDefaultModel();
        boolean completed = extractAssertions(javaProject, model);
        if (!completed) {
            return false;
        }
        progressMonitor.worked(1);
        completed = addExtraAssertions(model);
        if (!completed) {
            return false;
        }
        progressMonitor.worked(1);
        progressMonitor.subTask("Writing output file");
        model.setNsPrefixes(Namespaces.PREFIX_MAPPING);
        model.write(new FileOutputStream(targetFile), fileFormat);
        progressMonitor.done();
        return true;
    }

    @SuppressWarnings("unchecked")
    private boolean addExtraAssertions(Model model) {
        progressMonitor.subTask("Inferring extra relations: extended callgraph");
        boolean uninterupted = addClosureAlongContainsProperty(model, Properties.CALLS_CLOSURE, Properties.CALLS_EXTENDED);
        if (!uninterupted) {
            return false;
        }
        progressMonitor.subTask("Inferring extra relations: combined type hierarchy");
        List<Resource[]> newPairs = new ArrayList<Resource[]>();
        Iterator<Statement> it = model.listStatements(null, Properties.EXTENDS_CLOSURE, (RDFNode) null);
        while (it.hasNext()) {
            Statement stmt = it.next();
            Resource subject = stmt.getSubject();
            Resource object = (Resource) stmt.getObject();
            newPairs.add(new Resource[] { subject, object });
            Iterator<Statement> it2 = model.listStatements(object, Properties.IMPLEMENTS_CLOSURE, (RDFNode) null);
            while (it2.hasNext()) {
                Statement implStmt = it2.next();
                newPairs.add(new Resource[] { subject, (Resource) implStmt.getObject() });
            }
            if (progressMonitor.isCanceled()) {
                return false;
            }
        }
        for (Iterator<Resource[]> npIter = newPairs.iterator(); npIter.hasNext(); ) {
            Resource[] resources = npIter.next();
            resources[0].addProperty(Properties.DERIVED_FROM_CLOSURE, resources[1]);
        }
        it = model.listStatements(null, Properties.IMPLEMENTS_CLOSURE, (RDFNode) null);
        while (it.hasNext()) {
            Statement stmt = it.next();
            stmt.getSubject().addProperty(Properties.DERIVED_FROM_CLOSURE, stmt.getObject());
        }
        progressMonitor.subTask("Inferring extra relations: generic dependencies from callgraph");
        it = model.listStatements(null, Properties.CALLS_EXTENDED, (RDFNode) null);
        while (it.hasNext()) {
            Statement stmt = it.next();
            stmt.getSubject().addProperty(Properties.DEPENDS_TRANSITIVELY, stmt.getObject());
        }
        progressMonitor.subTask("Inferring extra relations: generic dependencies from parameter usage");
        uninterupted = addClosureAlongContainsProperty(model, Properties.HAS_PARAMETER_EXTENDED, Properties.DEPENDS_TRANSITIVELY);
        if (!uninterupted) {
            return false;
        }
        progressMonitor.subTask("Inferring extra relations: generic dependencies from return type usage");
        uninterupted = addClosureAlongContainsProperty(model, Properties.HAS_RETURN_TYPE_EXTENDED, Properties.DEPENDS_TRANSITIVELY);
        if (!uninterupted) {
            return false;
        }
        progressMonitor.subTask("Inferring extra relations: generic dependencies from variable usage");
        uninterupted = addClosureAlongContainsProperty(model, Properties.HAS_VARIABLE_TYPE_EXTENDED, Properties.DEPENDS_TRANSITIVELY);
        if (!uninterupted) {
            return false;
        }
        progressMonitor.subTask("Inferring extra relations: generic dependencies from inheritance");
        uninterupted = addClosureAlongContainsProperty(model, Properties.DERIVED_FROM_CLOSURE, Properties.DEPENDS_TRANSITIVELY);
        if (!uninterupted) {
            return false;
        }
        return true;
    }

    /**
	 * Creates a new relation by extending an existing one along the containment hierarchy.
	 * 
	 * All pairs (S startProperty O) are found, and new properties are added for all X in the
	 * upset of S regarding the {@link Properties#CONTAINS_CLOSURE} relation, using targetProperty
	 * as property and all Y in the upset of O regarding the same relation as values.
	 * 
	 * @param model The model to modify. Must not be null.
	 * @param startProperty The property to search for. Must not be null.
	 * @param targetProperty The property to set. Must not be null.
	 * @return true iff the process was not interrupted by the user.
	 */
    @SuppressWarnings("unchecked")
    private boolean addClosureAlongContainsProperty(Model model, Property startProperty, Property targetProperty) {
        Iterator<Statement> it = model.listStatements(null, startProperty, (RDFNode) null);
        List<Resource[]> newPairs = new ArrayList<Resource[]>();
        while (it.hasNext()) {
            Statement stmt = it.next();
            Resource subject = stmt.getSubject();
            Resource object = (Resource) stmt.getObject();
            newPairs.add(new Resource[] { subject, object });
            Iterator<Statement> it2 = model.listStatements(null, Properties.CONTAINS_CLOSURE, subject);
            while (it2.hasNext()) {
                Statement contSubjStmt = it2.next();
                Iterator<Statement> it3 = model.listStatements(null, Properties.CONTAINS_CLOSURE, object);
                while (it3.hasNext()) {
                    Statement contObjStmt = it3.next();
                    newPairs.add(new Resource[] { contSubjStmt.getSubject(), contObjStmt.getSubject() });
                }
            }
            if (progressMonitor.isCanceled()) {
                return false;
            }
        }
        for (Iterator<Resource[]> npIter = newPairs.iterator(); npIter.hasNext(); ) {
            Resource[] resources = npIter.next();
            resources[0].addProperty(targetProperty, resources[1]);
        }
        return true;
    }

    private boolean extractAssertions(IParent parent, final Model model) throws JavaModelException {
        if (progressMonitor.isCanceled()) {
            return false;
        }
        Resource packageResource = null;
        if (parent instanceof IPackageFragment) {
            IPackageFragment packageFragment = (IPackageFragment) parent;
            progressMonitor.subTask("Extract base data from Java code: " + packageFragment.getElementName());
            packageResource = createResource(model, packageFragment);
        }
        mainLoop: for (int i = 0; i < parent.getChildren().length; i++) {
            IJavaElement element = parent.getChildren()[i];
            for (int j = 0; j < ignorePattern.length; j++) {
                Pattern pattern = ignorePattern[j];
                if (pattern.matcher(element.getElementName()).matches()) {
                    continue mainLoop;
                }
            }
            if (element instanceof ICompilationUnit) {
                ICompilationUnit compilationUnit = (ICompilationUnit) element;
                ASTParser parser = ASTParser.newParser(AST.JLS3);
                parser.setKind(ASTParser.K_COMPILATION_UNIT);
                parser.setSource(compilationUnit);
                parser.setResolveBindings(true);
                ASTNode result = parser.createAST(null);
                final ArrayList<Resource> startStack = new ArrayList<Resource>();
                if (packageResource != null) {
                    startStack.add(packageResource);
                }
                result.accept(new ASTVisitor() {

                    List<Resource> resources = startStack;

                    private Resource getTop() {
                        return resources.get(resources.size() - 1);
                    }

                    private void pushOnStack(Resource currentRes) {
                        addPropertyWithTransitiveClosure(model, getTop(), currentRes, Properties.CONTAINS, Properties.CONTAINS_CLOSURE);
                        resources.add(currentRes);
                    }

                    private void popStack() {
                        resources.remove(resources.size() - 1);
                    }

                    public boolean visit(TypeDeclaration node) {
                        ITypeBinding typeBinding = node.resolveBinding();
                        Resource currentRes = createResource(model, typeBinding);
                        pushOnStack(currentRes);
                        return true;
                    }

                    public void endVisit(TypeDeclaration node) {
                        popStack();
                    }

                    public boolean visit(MethodDeclaration node) {
                        Resource currentRes = createResource(model, node.resolveBinding());
                        pushOnStack(currentRes);
                        return true;
                    }

                    public void endVisit(MethodDeclaration node) {
                        popStack();
                    }

                    public boolean visit(MethodInvocation node) {
                        addPropertyWithTransitiveClosure(model, getTop(), createResource(model, node.resolveMethodBinding()), Properties.CALLS, Properties.CALLS_CLOSURE);
                        return true;
                    }

                    public boolean visit(ConstructorInvocation node) {
                        addPropertyWithTransitiveClosure(model, getTop(), createResource(model, node.resolveConstructorBinding()), Properties.CALLS, Properties.CALLS_CLOSURE);
                        return true;
                    }

                    public boolean visit(VariableDeclarationFragment node) {
                        ITypeBinding typeBinding = node.resolveBinding().getType();
                        Resource typeResource = createResource(model, typeBinding);
                        getTop().addProperty(Properties.HAS_VARIABLE_TYPE, typeResource);
                        getTop().addProperty(Properties.HAS_VARIABLE_TYPE_EXTENDED, typeResource);
                        if (typeBinding.isArray()) {
                            getTop().addProperty(Properties.HAS_VARIABLE_TYPE_EXTENDED, createResource(model, typeBinding.getElementType()));
                        }
                        return true;
                    }
                });
            }
            if (element instanceof IParent) {
                extractAssertions((IParent) element, model);
            }
        }
        return true;
    }

    private static String encodeForURI(String unescapedString) {
        try {
            return URLEncoder.encode(unescapedString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding 'UTF-8' not available, but should be");
        }
    }

    private static Resource createResource(Model model, IPackageBinding packageBinding) {
        return createPackageResource(model, packageBinding.getName());
    }

    private static Resource createPackageResource(Model model, String packageName) {
        Resource packageResource = model.createResource(Namespaces.PACKAGES + encodeForURI(packageName));
        if (!model.containsResource(packageResource)) {
            packageResource.addProperty(Properties.TYPE, Types.PACKAGE);
            packageResource.addProperty(Properties.CONTAINS_CLOSURE, packageResource);
            int lastDot = packageName.lastIndexOf('.');
            if (lastDot != -1) {
                Resource parentPackageResource = createPackageResource(model, packageName.substring(0, lastDot));
                addPropertyWithTransitiveClosure(model, parentPackageResource, packageResource, Properties.CONTAINS, Properties.CONTAINS_CLOSURE);
            }
        }
        return packageResource;
    }

    private static Resource createResource(final Model model, IPackageFragment packageFragment) {
        return createPackageResource(model, packageFragment.getElementName());
    }

    @SuppressWarnings("unchecked")
    private static void addPropertyWithTransitiveClosure(Model model, Resource from, Resource to, Property coveringRelation, Property closureRelation) {
        from.addProperty(coveringRelation, to);
        from.addProperty(closureRelation, to);
        Iterator<Resource> it = model.listObjectsOfProperty(to, closureRelation);
        while (it.hasNext()) {
            from.addProperty(closureRelation, it.next());
        }
        Iterator<Statement> it2 = model.listStatements(null, closureRelation, from);
        while (it2.hasNext()) {
            Statement stmt = it2.next();
            stmt.getSubject().addProperty(closureRelation, to);
        }
    }

    private static Resource createResource(final Model model, ITypeBinding typeBinding) {
        ITypeBinding erasureTypeBinding = typeBinding.getErasure();
        Resource typeRes = model.createResource(Namespaces.TYPES + encodeForURI(erasureTypeBinding.getKey()));
        if (!model.containsResource(typeRes)) {
            typeRes.addProperty(Properties.CONTAINS_CLOSURE, typeRes);
            typeRes.addProperty(Properties.EXTENDS_CLOSURE, typeRes);
            if (erasureTypeBinding.getPackage() != null) {
                Resource packageRes = createResource(model, erasureTypeBinding.getPackage());
                addPropertyWithTransitiveClosure(model, packageRes, typeRes, Properties.CONTAINS, Properties.CONTAINS_CLOSURE);
            }
            typeRes.addProperty(Properties.TYPE, Types.TYPE);
            if (erasureTypeBinding.isInterface()) {
                typeRes.addProperty(Properties.TYPE, Types.INTERFACE);
            } else {
                typeRes.addProperty(Properties.TYPE, Types.CLASS);
            }
            ITypeBinding superClass = erasureTypeBinding.getSuperclass();
            if (superClass != null) {
                addPropertyWithTransitiveClosure(model, typeRes, createResource(model, superClass), Properties.EXTENDS, Properties.EXTENDS_CLOSURE);
            }
            ITypeBinding[] interfaces = erasureTypeBinding.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                ITypeBinding superInterface = interfaces[i];
                addPropertyWithTransitiveClosure(model, typeRes, createResource(model, superInterface), Properties.IMPLEMENTS, Properties.IMPLEMENTS_CLOSURE);
            }
            IVariableBinding[] fields = erasureTypeBinding.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                IVariableBinding field = fields[i];
                Resource fieldTypeResource = createResource(model, field.getType());
                typeRes.addProperty(Properties.HAS_FIELD_TYPE, fieldTypeResource);
                typeRes.addProperty(Properties.HAS_FIELD_TYPE_EXTENDED, fieldTypeResource);
                if (field.getType().isArray()) {
                    typeRes.addProperty(Properties.HAS_FIELD_TYPE_EXTENDED, createResource(model, field.getType().getElementType()));
                }
            }
        }
        return typeRes;
    }

    private static Resource createResource(final Model model, IMethodBinding methodBinding) {
        Resource methodResource = model.createResource(Namespaces.METHODS + encodeForURI(methodBinding.getKey()));
        if (!model.containsResource(methodResource)) {
            methodResource.addProperty(Properties.CALLS_CLOSURE, methodResource);
            methodResource.addProperty(Properties.CONTAINS_CLOSURE, methodResource);
            ITypeBinding[] formalParams = methodBinding.getParameterTypes();
            for (int i = 0; i < formalParams.length; i++) {
                ITypeBinding param = formalParams[i];
                Resource paramResource = createResource(model, param);
                methodResource.addProperty(Properties.HAS_PARAMETER, paramResource);
                methodResource.addProperty(Properties.HAS_PARAMETER_EXTENDED, paramResource);
                if (param.isArray()) {
                    methodResource.addProperty(Properties.HAS_PARAMETER_EXTENDED, createResource(model, param.getElementType()));
                }
            }
            ITypeBinding retVal = methodBinding.getReturnType();
            Resource retValResource = createResource(model, retVal);
            methodResource.addProperty(Properties.HAS_RETURN_TYPE, retValResource);
            methodResource.addProperty(Properties.HAS_RETURN_TYPE_EXTENDED, retValResource);
            if (retVal.isArray()) {
                methodResource.addProperty(Properties.HAS_RETURN_TYPE_EXTENDED, createResource(model, retVal.getElementType()));
            }
        }
        return methodResource;
    }

    protected IStatus run(IProgressMonitor monitor) {
        this.progressMonitor = monitor;
        try {
            boolean exportCompleted = exportSource();
            if (exportCompleted) {
                return Status.OK_STATUS;
            } else {
                return Status.CANCEL_STATUS;
            }
        } catch (JavaModelException e) {
            return new Status(Status.ERROR, PLUGIN_NAME, ERROR_CODE_JAVA_MODEL_EXCEPTION, e.getLocalizedMessage(), e);
        } catch (FileNotFoundException e) {
            return new Status(Status.ERROR, PLUGIN_NAME, ERROR_CODE_FILE_NOT_FOUND_EXCEPTION, e.getLocalizedMessage(), e);
        }
    }
}
