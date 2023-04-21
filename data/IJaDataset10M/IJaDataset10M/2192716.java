package no.uio.ifi.kjetilos.javatraits.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import no.uio.ifi.kjetilos.javatraits.CompilationUnitController;
import no.uio.ifi.kjetilos.javatraits.JavaClassController;
import no.uio.ifi.kjetilos.javatraits.Options;
import no.uio.ifi.kjetilos.javatraits.exception.TraitWeaverException;
import no.uio.ifi.kjetilos.javatraits.parser.ASTAnnotation;
import no.uio.ifi.kjetilos.javatraits.parser.ASTClassOrInterfaceBody;
import no.uio.ifi.kjetilos.javatraits.parser.ASTClassOrInterfaceBodyDeclaration;
import no.uio.ifi.kjetilos.javatraits.parser.ASTClassOrInterfaceDeclaration;
import no.uio.ifi.kjetilos.javatraits.parser.ASTClassOrInterfaceType;
import no.uio.ifi.kjetilos.javatraits.parser.ASTImplementsList;
import no.uio.ifi.kjetilos.javatraits.parser.ASTMethodDeclaration;
import no.uio.ifi.kjetilos.javatraits.parser.ASTWithDeclaration;
import no.uio.ifi.kjetilos.javatraits.parser.Node;
import no.uio.ifi.kjetilos.javatraits.parser.JavaTParser.ModifierSet;
import no.uio.ifi.kjetilos.javatraits.util.NodeCreator;

/**
 * This class represents a Java class in the preprocessor system. It contains
 * operations for flattening itself and operations for extracting methods, with
 * blocks, extends fields, implements fields. A JavaClass can be either a class
 * or an interface it is checked by the <code>isInterface</code> method.
 * 
 * @author Kjetil �ster�s
 */
public class JavaClass extends FileEntity {

    private MethodExtractor methodExtractor;

    private WithExtractor withExtractor;

    private ExtendsExtractor extendsExtractor;

    private ImplementsExtractor implementsExtractor;

    private boolean isInterface = false;

    private ASTClassOrInterfaceDeclaration declaration;

    /**
	 * This constructor creates a <code>JavaClass</code> out of an abstract
	 * syntax tree node which represents a classOrInterfaceDeclaration.
	 * 
	 * @param classOrInterfaceDeclaration
	 * @see ASTClassOrInterfaceDeclaration
	 */
    public JavaClass(ASTClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        this.declaration = classOrInterfaceDeclaration;
        this.isInterface = classOrInterfaceDeclaration.isInterface();
        this.methodExtractor = new MethodExtractor(classOrInterfaceDeclaration);
        this.withExtractor = new WithExtractor(classOrInterfaceDeclaration);
        this.extendsExtractor = new ExtendsExtractor(classOrInterfaceDeclaration);
        this.implementsExtractor = new ImplementsExtractor(classOrInterfaceDeclaration);
    }

    /**
	 * Checks if a required signature is compatible with a provided signature
	 * and check if they are return type substitutable, throws substitutable and
	 * static compatible.
	 * 
	 * @param requiredSignature
	 *            the method signature of a certain requirement.
	 * @param providedSignature
	 *            the method signature of a certain provided method.
	 * @return <code>true</code> if the two methods are compatible,
	 *         <code>false</code> if they are not compatible.
	 * @throws TraitWeaverException
	 *             if a compatible signature is found but is is not either:
	 *             return type substitutable, throws substitutable or static
	 *             compatible.
	 */
    private boolean isCompatible(MethodSignature requiredSignature, MethodSignature providedSignature) {
        if (providedSignature.isCompatibleWith(requiredSignature)) {
            if (!providedSignature.isReturnTypeSubstitutableFor(requiredSignature)) {
                throw new TraitWeaverException("An override equivalent signature " + providedSignature + " is not return type substitutable " + "for " + requiredSignature);
            }
            if (!providedSignature.isThrowsSubstitutableFor(requiredSignature)) {
                throw new TraitWeaverException("An override equivalent signature " + providedSignature + " is not throws substitutable " + "for " + requiredSignature);
            }
            if (!isStaticCompatible(requiredSignature, providedSignature)) {
                throw new TraitWeaverException("An override equivalent signature " + providedSignature + " is not modifier compatible " + "with " + requiredSignature);
            }
            return true;
        } else {
            return false;
        }
    }

    private void checkNotNull(JavaClass interfaceUsed, String message) {
        if (interfaceUsed == null) {
            throw new TraitWeaverException(message);
        }
    }

    /**
	 * This is the important check on whether a required method is present or
	 * not. This method is a good starting point for optimalization.
	 * 
	 * @param requiredMethods
	 *            a set of {@link RequiredMethod} objects which are gathered
	 *            from all the flattened traits.
	 * @param traitMethods
	 *            this is a set of methods which are provided by the flattened
	 *            traits.
	 * @throws TraitWeaverException
	 *             if a required method is not present.
	 */
    private void checkRequiredMethods(Set<RequiredMethod> requiredMethods, Set<OverridableMethodWrapper> traitMethods) {
        for (RequiredMethod requiredMethod : requiredMethods) {
            if (isRequiredMethodPresentRecursive(requiredMethod, true)) {
                continue;
            }
            if (isRequiredMethodInsideTraits(requiredMethod, traitMethods)) {
                continue;
            } else {
                throw new TraitWeaverException("Required method " + requiredMethod.getName() + " with signature " + requiredMethod.getMethodSignature() + " not present or not visible in class " + getName());
            }
        }
    }

    /**
	 * Creates a body declaration. This method is used when methods are
	 * inserted.
	 * 
	 * @return the body declaration created.
	 * @see NodeCreator
	 */
    private ASTClassOrInterfaceBodyDeclaration createBodyDeclaration() {
        ASTClassOrInterfaceBody body = findClassOrInterfaceBody();
        ASTClassOrInterfaceBodyDeclaration decl = NodeCreator.newASTClassOrInterfaceBodyDeclaration(body);
        body.jjtAddChild(decl, body.jjtGetNumChildren());
        return decl;
    }

    /**
	 * Extracts a list of methods which are declared within this java class.
	 * 
	 * @return a list of {@link ASTMethodDeclaration} objects that this java
	 *         class contains.
	 */
    public List<ASTMethodDeclaration> extractMethods() {
        return methodExtractor.extractMethods();
    }

    /**
	 * Finds the node representing the body of this class or interface.
	 * 
	 * @return the body node.
	 */
    private ASTClassOrInterfaceBody findClassOrInterfaceBody() {
        return declaration.findFirstChildOfType(ASTClassOrInterfaceBody.class);
    }

    /**
	 * Tries to locate a method which is compatible to the requireMethod inside
	 * the current class.
	 * 
	 * @param requiredMethod
	 *            the method which we try to find a compatible method to.
	 * @return null if a compatible method to requiredMethod is not found and
	 *         returns a reference to the compatible method if it is found.
	 */
    private ProvidedMethod findCompatibleMethod(RequiredMethod requiredMethod) {
        Collection<ProvidedMethod> providedMethods = getProvidedMethods();
        MethodSignature requiredSignature = requiredMethod.getMethodSignature();
        for (ProvidedMethod p : providedMethods) {
            MethodSignature providedSignature = p.getMethodSignature();
            if (isCompatible(requiredSignature, providedSignature)) {
                return p;
            }
        }
        return null;
    }

    /**
	 * This method flattens the JavaClass and is the most important method in
	 * this class. It contains the algorithm which collects all the trait
	 * methods checks and inserts them into the class body, it also checks if
	 * all the requirements are present within this java class so the traits can
	 * be successfully flattened.
	 * 
	 */
    public void flattenJavaClass() {
        Set<OverridableMethodWrapper> traitMethods = new HashSet<OverridableMethodWrapper>();
        Set<RequiredMethod> requiredMethods = new HashSet<RequiredMethod>();
        Set<ASTClassOrInterfaceType> implementSet = new HashSet<ASTClassOrInterfaceType>();
        for (TraitUsage tu : withExtractor.getUsedTraits()) {
            Trait trait = tu.getTrait();
            Set<OverridableMethodWrapper> flattenedMethods = trait.flattenTrait(requiredMethods, implementSet);
            List<Operation> operations = tu.getOperations();
            for (Operation op : operations) {
                op.execute(flattenedMethods);
            }
            for (OverridableMethodWrapper m : flattenedMethods) {
                if (traitMethods.contains(m)) {
                    traitMethods.remove(m);
                    traitMethods.add(new OverridableMethodWrapper(new ConflictMethod(m.getMethod())));
                } else {
                    traitMethods.add(m);
                }
            }
        }
        checkRequiredMethods(requiredMethods, traitMethods);
        insertMethods(traitMethods);
        insertImplementSet(implementSet);
    }

    /**
	 * Gets the name of the superclass of this java class.
	 * 
	 * @return the name of the class this java class extends
	 */
    public String getExtends() {
        return extendsExtractor.extractExtends();
    }

    /**
	 * Gets all the implemented interfaces of this java class.
	 * 
	 * @return a list of interface names.
	 */
    public String[] getImplements() {
        return implementsExtractor.extractImplements();
    }

    /**
	 * Gets all the provided methods of this java class.
	 * 
	 * @return a collection of {@link ProvidedMethod} objects which this java
	 *         class provides.
	 */
    private Collection<ProvidedMethod> getProvidedMethods() {
        return methodExtractor.getLocalProvidedMethods();
    }

    /**
	 * Gets a set of wrapped methods. Wrapped methods are used to help
	 * comparison operations.
	 * 
	 * @return a set of wrapped methods. The {@link OverridableMethodWrapper} is
	 *         used as a wrapper class.
	 */
    private Set<OverridableMethodWrapper> getProvidedMethodSet() {
        Collection<ProvidedMethod> providedMethods = getProvidedMethods();
        Set<OverridableMethodWrapper> providedMethodSet = new HashSet<OverridableMethodWrapper>();
        for (ProvidedMethod p : providedMethods) {
            OverridableMethodWrapper adapter = new OverridableMethodWrapper(p);
            if (providedMethodSet.contains(adapter)) {
                throw new TraitWeaverException("Two override equivalent signatures found in the java class");
            } else {
                providedMethodSet.add(new OverridableMethodWrapper(p));
            }
        }
        return providedMethodSet;
    }

    /**
	 * Finds and returns the superclass of this class.
	 * 
	 * @return the superclass of this java class or <code>null</code> if this
	 *         class has no explicit superclass.
	 * @throws TraitWeaverException
	 *             if an extends name is found but the class is not found in the
	 *             {@link JavaClassController}.
	 */
    public JavaClass getSuperClass() {
        String superClassName = getExtends();
        if (superClassName == null) {
            return null;
        } else {
            JavaClassController jcController = CompilationUnitController.getInstance().getJavaClassController();
            JavaClass superClass = jcController.getJavaClass(superClassName);
            if (superClass == null) {
                throw new TraitWeaverException("Super class " + superClassName + " not found!");
            } else {
                return superClass;
            }
        }
    }

    /**
	 * Extracts the list of with declarations this java class contains.
	 * 
	 * @return a list of with declarations inside this java class.
	 */
    public List<ASTWithDeclaration> getWithDeclarations() {
        return withExtractor.getTraitUsed();
    }

    /**
	 * Inserts method annotation on methods inserted from traits.
	 * 
	 * @param decl
	 *            the class declaration to insert the annotation on.
	 * @param method
	 *            the method which comes from a trait.
	 */
    private void insertAnnotation(ASTClassOrInterfaceBodyDeclaration decl, Method method) {
        ASTAnnotation annotation = NodeCreator.newASTAnnotation(decl, method);
        decl.jjtAddChild(annotation, 0);
        annotation.jjtSetParent(decl);
    }

    /**
	 * Insert implements nodes from the traits used. This is done by adding the
	 * ASTClassOrInterfaceType nodes to the existing ASTImplementationList, if
	 * the ASTImplementationList does not exist then it is created.
	 * 
	 * @param implementSet
	 *            a set of implemented interfaces. This set is from the trait
	 *            declaration.
	 */
    private void insertImplementSet(Set<ASTClassOrInterfaceType> implementSet) {
        if (implementSet.isEmpty()) {
            return;
        }
        ASTImplementsList implementsList = declaration.findFirstChildOfType(ASTImplementsList.class);
        if (implementsList == null) {
            implementsList = NodeCreator.newASTImpementsList(declaration);
        }
        for (ASTClassOrInterfaceType astImplement : implementSet) {
            implementsList.jjtAddChild(astImplement, implementsList.jjtGetNumChildren());
        }
    }

    /**
	 * Inserts a node into a body...
	 * 
	 * @param node
	 *            the node to insert
	 * @param decl
	 *            the declaration in which to insert the <code>node</code>
	 */
    private void insertIntoBody(Node node, ASTClassOrInterfaceBodyDeclaration decl) {
        decl.jjtAddChild(node, node.jjtGetNumChildren());
    }

    /**
	 * Inserts the given methods into a block. Checks if the method already
	 * exists in the class, in which case the method is not added. This is due
	 * to class methods overriding trait methods.
	 * 
	 * @param methods
	 *            a set of method wrappers
	 */
    private void insertMethods(Set<OverridableMethodWrapper> methods) {
        Set<OverridableMethodWrapper> providedMethodSet = getProvidedMethodSet();
        for (OverridableMethodWrapper methodAdapter : methods) {
            if (providedMethodSet.contains(methodAdapter)) {
                continue;
            }
            if (methodAdapter.getMethod() instanceof ConflictMethod) {
                throw new TraitWeaverException("Conflict on method " + methodAdapter.getMethod());
            }
            ASTClassOrInterfaceBodyDeclaration decl = createBodyDeclaration();
            Options opts = Options.getInstance();
            if (opts.isAnnotate()) {
                insertAnnotation(decl, methodAdapter.getMethod());
            }
            insertIntoBody(methodAdapter.getMethod().getMethodDeclaration(), decl);
        }
    }

    /**
	 * Grabs with blocks from the implemented interfaces and inserts them into
	 * this class body.
	 */
    public void insertTraitsFromInterface() {
        String[] interfaces = getImplements();
        ASTClassOrInterfaceBody body = findClassOrInterfaceBody();
        JavaClassController jc = CompilationUnitController.getInstance().getJavaClassController();
        for (String className : interfaces) {
            JavaClass interfaceUsed = jc.getJavaClass(className);
            checkNotNull(interfaceUsed, "Interface " + className + " not found ");
            List<ASTWithDeclaration> withDecls = interfaceUsed.getWithDeclarations();
            if (withDecls.size() > 0) {
                for (ASTWithDeclaration withDecl : withDecls) {
                    body.jjtAddChild(withDecl, body.jjtGetNumChildren());
                }
            }
        }
    }

    /**
	 * Finds out if this java class represents an interface or a class.
	 * 
	 * @return <code>true</code> if this java class is an interface,
	 *         <code>false</code> if this java class is a class.
	 */
    public boolean isInterface() {
        return isInterface;
    }

    /**
	 * Finds out if the method has a private modifier.
	 * 
	 * @param method
	 *            the method to test.
	 * @return <code>true</code> if the method has a private modifier,
	 *         <code>false</code> otherwise.
	 */
    private boolean isPrivate(Method method) {
        ModifierSet modifierSet = new ModifierSet();
        MethodSignature requiredSignature = method.getMethodSignature();
        return modifierSet.isPrivate(requiredSignature.getModifiers());
    }

    /**
	 * Finds out if some compatible method of the required method is present in
	 * the list of <code>traitMethods</code>.
	 * 
	 * @param requiredMethod
	 *            the required method to look for.
	 * @param traitMethods
	 *            the list of methods to serch through.
	 * @return <code>true</code> if a method is found inside the
	 *         <code>traitMethods</code> which is compatible with the
	 *         <code>requiredMethod</code>, <code>false</code> if it is not
	 *         found.
	 */
    private boolean isRequiredMethodInsideTraits(RequiredMethod requiredMethod, Set<OverridableMethodWrapper> traitMethods) {
        MethodSignature requiredSignature = requiredMethod.getMethodSignature();
        MethodSignature providedSignature;
        for (OverridableMethodWrapper methodWrapper : traitMethods) {
            providedSignature = methodWrapper.getMethod().getMethodSignature();
            if (isCompatible(requiredSignature, providedSignature)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Search this JavaClass and its superclasses for the required method.
	 * 
	 * @param requiredMethod
	 * @param first
	 *            true if its the first time this method is called, false
	 *            otherwise
	 * @return <code>true</code> if the method is found somewhere in this
	 *         class or any of the superclasses, <code>false</code> otherwise.
	 * @throws TraitWeaverException
	 *             if a requirement specifies a private method and it is not
	 *             found within the first class.
	 */
    private boolean isRequiredMethodPresentRecursive(RequiredMethod requiredMethod, boolean first) {
        ProvidedMethod providedMethod = findCompatibleMethod(requiredMethod);
        if (providedMethod != null) {
            return first || !isPrivate(providedMethod);
        } else {
            if (first && isPrivate(requiredMethod)) {
                MethodSignature requiredSignature = requiredMethod.getMethodSignature();
                throw new TraitWeaverException("The private requirement " + requiredSignature + " from a trait was not found directly in the class " + getName());
            }
            JavaClass superClass = getSuperClass();
            if (superClass == null) {
                return false;
            } else {
                return superClass.isRequiredMethodPresentRecursive(requiredMethod, false);
            }
        }
    }

    /**
	 * Checks if the modifiers of the required signature is compatible with the
	 * modifiers of the provided signature with respect to the static modifier.
	 * 
	 * @param requiredSignature
	 *            the signature of the required method
	 * @param providedSignature
	 *            the signature of the provided method
	 * @return <code>true</code> if either both signatures are static or both
	 *         are non-static, <code>false</code> if one is static and the
	 *         other is non-static.
	 */
    private boolean isStaticCompatible(MethodSignature requiredSignature, MethodSignature providedSignature) {
        ModifierSet modifierSet = new ModifierSet();
        int requiredModifier = requiredSignature.getModifiers();
        int providedModifier = providedSignature.getModifiers();
        boolean isRequiredStatic = modifierSet.isStatic(requiredModifier);
        boolean isProvidedStatic = modifierSet.isStatic(providedModifier);
        return (isRequiredStatic == isProvidedStatic);
    }

    public String toString() {
        return "Class " + getName() + ", " + getFile();
    }
}
