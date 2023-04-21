package org.argouml.language.java.reveng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.kernel.ProjectManager;
import org.argouml.language.java.reveng.classfile.ParserUtils;
import org.argouml.model.CoreFactory;
import org.argouml.model.Facade;
import org.argouml.model.Model;
import org.argouml.ocl.OCLUtil;
import org.argouml.profile.Profile;
import org.argouml.uml.reveng.ImportCommon;
import org.argouml.uml.reveng.ImportInterface;

/**
 * Modeller maps Java source code(parsed/recognised by ANTLR) to UML model
 * elements, it applies some of the semantics in JSR-26. Note: JSR-26 was
 * withdrawn in March, 2004, so it obviously provides no guidance for more
 * recent language features such as Java 5.
 * 
 * TODO: This really needs a more sophisticated symbol table facility. It
 * currently uses the model repository as its symbol table which makes it easy
 * to merge into an existing model, but it also sometimes requires guessing
 * about what a symbol represents (e.g. interface, class, or package) so that
 * the name can instantiated in a concrete form. - tfm 20070911
 * 
 * @author Marcus Andersson, Thomas Neustupny
 */
public class Modeller {

    private static final Logger LOG = Logger.getLogger(Modeller.class);

    private static final String JAVA_PACKAGE = "java.lang";

    private static final List<String> EMPTY_STRING_LIST = Collections.emptyList();

    /**
     * Current working model.
     */
    private Object model;

    /**
     * Java profile model.
     */
    private Profile javaProfile;

    /**
     * Current import settings.
     */
    private ImportCommon importSession;

    /**
     * The package which the currentClassifier belongs to.
     */
    private Object currentPackage;

    /**
     * Keeps the data that varies during parsing.
     */
    private ParseState parseState;

    /**
     * Stack up the state when descending inner classes.
     */
    private Stack<ParseState> parseStateStack;

    /**
     * Only attributes will be generated.
     */
    private boolean noAssociations;

    /**
     * Arrays will be modelled as unique datatypes.
     */
    private boolean arraysAsDatatype;

    /**
     * The name of the file being parsed.
     */
    private String fileName;

    /**
     * Arbitrary attributes.
     */
    private Hashtable<String, Object> attributes = new Hashtable<String, Object>();

    /**
     * List of the names of parsed method calls.
     */
    private List<String> methodCalls = new ArrayList<String>();

    /**
     * HashMap of parsed local variables. Indexed by variable name with string
     * representation of the type stored as the value.
     */
    private Hashtable<String, String> localVariables = new Hashtable<String, String>();

    /**
     * New model elements that were created during this reverse engineering
     * session. TODO: We want a stronger type here, but ArgoUML treats all
     * elements as just simple Objects.
     */
    private Collection<Object> newElements;

    /**
     * Flag to control generation of artificial names for associations. If true,
     * generate names of form "From->To". If false, set name to null.
     */
    private boolean generateNames = true;

    /**
     * Create a new modeller.
     * 
     * @param theModel The model to work with.
     * @param attributeSelected true if associations should be modeled as
     *            attributes
     * @param datatypeSelected true if arrays should be modeled as datatypes
     *            instead of instead of using UML multiplicities
     * @param theFileName the current file name
     * @deprecated for 0.27.2 by thn. Use the other constructor.
     */
    public Modeller(Object theModel, boolean attributeSelected, boolean datatypeSelected, String theFileName) {
        this(theModel, null, attributeSelected, datatypeSelected, theFileName);
    }

    /**
     * Create a new modeller.
     * 
     * @param theModel The model to work with.
     * @param theJavaProfile The Java profile.
     * @param attributeSelected true if associations should be modeled as
     *            attributes
     * @param datatypeSelected true if arrays should be modeled as datatypes
     *            instead of instead of using UML multiplicities
     * @param theFileName the current file name
     */
    public Modeller(Object theModel, Profile theJavaProfile, boolean attributeSelected, boolean datatypeSelected, String theFileName) {
        model = theModel;
        javaProfile = theJavaProfile;
        noAssociations = attributeSelected;
        arraysAsDatatype = datatypeSelected;
        currentPackage = this.model;
        newElements = new HashSet<Object>();
        parseState = new ParseState(this.model, getPackage(JAVA_PACKAGE, true));
        parseStateStack = new Stack<ParseState>();
        fileName = theFileName;
        if (javaProfile == null) {
            LOG.warn("No Java profile activated for Java source import. Why?");
        }
    }

    /**
     * @param key the key of the attribute to get
     * @return the value of the attribute
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * @param key the key of the attribute
     * @param value the value for the attribute
     */
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    /**
     * This is a mapping from a Java compilation Unit -> a UML artifact.
     * Classes are resident in a component in UML1, and realizing classifiers
     * in UML2. Imports are relationships between artifacts and other
     * classes / packages.
     * <p>
     * 
     * See JSR 26 (for UML1?).
     * <p>
     * 
     * Adding artifacts is a little messy since there are 2 cases:
     * 
     * <ol>
     * <li>source file has package statement, will be added several times since
     * lookup in addComponent() only looks in the model since the package
     * namespace is not yet known.
     * 
     * <li>source file has not package statement: artifact is added to the
     * model namespace. There is no package statement so the lookup will
     * always work.
     * 
     * </ol>
     * Therefore in the case of (1), we need to delete duplicate artifacts in
     * the addPackage() method.
     * <p>
     * 
     * In either case we need to add a package since we don't know in advance if
     * there will be a package statement.
     * <p>
     */
    public void addComponent() {
        Object artifact = Model.getFacade().lookupIn(currentPackage, fileName);
        if (artifact == null) {
            if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
                artifact = Model.getCoreFactory().createComponent();
            } else {
                artifact = Model.getCoreFactory().createArtifact();
            }
            Model.getCoreHelper().setName(artifact, fileName);
            newElements.add(artifact);
        }
        parseState.setArtifact(artifact);
        Model.getCoreHelper().setNamespace(parseState.getArtifact(), model);
    }

    /**
     * Called from the parser when a package clause is found.
     * 
     * @param name The name of the package.
     */
    public void addPackage(String name) {
        String ownerPackageName, currentName = name;
        ownerPackageName = getPackageName(currentName);
        while (!"".equals(ownerPackageName)) {
            currentName = ownerPackageName;
            ownerPackageName = getPackageName(currentName);
        }
        Object mPackage = getPackage(currentName, false);
        if (importSession != null && importSession.getSrcPath() != null && Model.getFacade().getTaggedValue(mPackage, ImportInterface.SOURCE_PATH_TAG) == null) {
            String[] srcPaths = { importSession.getSrcPath() };
            buildTaggedValue(mPackage, ImportInterface.SOURCE_PATH_TAG, srcPaths);
        }
        mPackage = getPackage(name, false);
        currentPackage = mPackage;
        parseState.addPackageContext(mPackage);
        Object artifact = Model.getFacade().lookupIn(currentPackage, fileName);
        if (artifact == null) {
            Model.getCoreHelper().setNamespace(parseState.getArtifact(), currentPackage);
        } else {
            Object oldArtifact = parseState.getArtifact();
            Model.getUmlFactory().delete(oldArtifact);
            newElements.remove(oldArtifact);
            parseState.setArtifact(artifact);
        }
    }

    /**
     * Called from the parser when an import clause is found.
     * 
     * @param name The name of the import. Can end with a '*'.
     */
    public void addImport(String name) {
        addImport(name, false);
    }

    public void addClassSignature(String signature) {
        addTypeParameters(parseState.getClassifier(), ParserUtils.extractTypeParameters(signature));
    }

    /**
     * Called from the parser when an import clause is found.
     * 
     * @param name The name of the import. Can end with a '*'.
     * @param forceIt Force addition by creating all that's missing.
     */
    void addImport(String name, boolean forceIt) {
        if (getLevel() == 0) {
            return;
        }
        String packageName = getPackageName(name);
        if (packageName == null || "".equals(packageName)) {
            LOG.warn("Import skipped - unable to get package name for " + name);
            return;
        }
        String classifierName = getClassifierName(name);
        Object mPackage = getPackage(packageName, true);
        if (classifierName.equals("*")) {
            parseState.addPackageContext(mPackage);
            Object srcFile = parseState.getArtifact();
            buildImport(mPackage, srcFile);
        } else {
            Object mClassifier = null;
            try {
                mClassifier = (new PackageContext(null, mPackage)).get(classifierName, false, javaProfile);
            } catch (ClassifierNotFoundException e) {
                if (forceIt && classifierName != null && mPackage != null) {
                    mPackage = (packageName.length() > 0) ? getPackage(packageName, false) : model;
                    mClassifier = Model.getFacade().lookupIn(mPackage, classifierName);
                    if (mClassifier == null) {
                        LOG.info("Modeller.java: " + "forced creation of unknown classifier " + classifierName);
                        mClassifier = Model.getCoreFactory().buildClass(classifierName, mPackage);
                        newElements.add(mClassifier);
                    }
                } else {
                    warnClassifierNotFound(classifierName, "an imported classifier");
                }
            }
            if (mClassifier != null) {
                parseState.addClassifierContext(mClassifier);
                Object srcFile = parseState.getArtifact();
                buildImport(mClassifier, srcFile);
            }
        }
    }

    private Object buildImport(Object element, Object srcFile) {
        Collection dependencies = Model.getCoreHelper().getDependencies(element, srcFile);
        for (Object dep : dependencies) {
            for (Object stereotype : Model.getFacade().getStereotypes(dep)) {
                if ("javaImport".equals(Model.getFacade().getName(stereotype))) {
                    return dep;
                }
            }
        }
        Object pkgImport = Model.getCoreFactory().buildDependency(srcFile, element);
        if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
            Model.getCoreHelper().addStereotype(pkgImport, getUML1Stereotype("javaImport"));
            ProjectManager.getManager().updateRoots();
        }
        String newName = makeDependencyName(srcFile, element);
        Model.getCoreHelper().setName(pkgImport, newName);
        newElements.add(pkgImport);
        return pkgImport;
    }

    private String makeAbstractionName(Object child, Object parent) {
        return makeFromToName(child, parent);
    }

    private String makeAssociationName(Object from, Object to) {
        return makeFromToName(from, to);
    }

    private String makeDependencyName(Object from, Object to) {
        return makeFromToName(from, to);
    }

    private String makeFromToName(Object from, Object to) {
        if (!generateNames) {
            return null;
        } else {
            return makeFromToName(Model.getFacade().getName(from), Model.getFacade().getName(to));
        }
    }

    private String makeFromToName(String from, String to) {
        if (!generateNames) {
            return null;
        } else {
            return from + " -> " + to;
        }
    }

    /**
     * Called from the parser when a class declaration is found.
     * 
     * @param name The name of the class.
     * @param modifiers A sequence of class modifiers.
     * @param superclassName Zero or one string with the name of the superclass.
     *            Can be fully qualified or just a simple class name.
     * @param interfaces Zero or more strings with the names of implemented
     *            interfaces. Can be fully qualified or just a simple interface
     *            name.
     * @param javadoc The javadoc comment. null or "" if no comment available.
     */
    public void addClass(String name, short modifiers, String superclassName, List<String> interfaces, String javadoc) {
        addClass(name, modifiers, EMPTY_STRING_LIST, superclassName, interfaces, javadoc, false);
    }

    /**
     * Called from the parser when a class declaration is found.
     * 
     * @param name The name of the class.
     * @param modifiers A bitmask of class modifiers.
     * @param typeParameters List of strings containing names of types for
     *            parameters
     * @param superclassName Zero or one string with the name of the superclass.
     *            Can be fully qualified or just a simple class name.
     * @param interfaces Zero or more strings with the names of implemented
     *            interfaces. Can be fully qualified or just a simple interface
     *            name.
     * @param javadoc The javadoc comment. null or "" if no comment available.
     * @param forceIt Force addition by creating all that's missing.
     */
    void addClass(String name, short modifiers, List<String> typeParameters, String superclassName, List<String> interfaces, String javadoc, boolean forceIt) {
        if (typeParameters != null && typeParameters.size() > 0) {
            logError("type parameters not supported on Class", name);
            for (String s : typeParameters) {
                logError("type parameter ", s);
            }
        }
        Object mClass = addClassifier(Model.getCoreFactory().createClass(), name, modifiers, javadoc, typeParameters);
        Model.getCoreHelper().setAbstract(mClass, (modifiers & JavaParser.ACC_ABSTRACT) > 0);
        Model.getCoreHelper().setLeaf(mClass, (modifiers & JavaParser.ACC_FINAL) > 0);
        if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
            Model.getCoreHelper().setRoot(mClass, false);
        }
        newElements.add(mClass);
        if (getLevel() == 0) {
            return;
        }
        if (superclassName != null) {
            Object parentClass = null;
            try {
                parentClass = getContext(superclassName).get(getClassifierName(superclassName), false, javaProfile);
                getGeneralization(currentPackage, parentClass, mClass);
            } catch (ClassifierNotFoundException e) {
                if (forceIt && superclassName != null && model != null) {
                    LOG.info("Modeller.java: forced creation of unknown class " + superclassName);
                    String packageName = getPackageName(superclassName);
                    String classifierName = getClassifierName(superclassName);
                    Object mPackage = (packageName.length() > 0) ? getPackage(packageName, false) : model;
                    parentClass = Model.getFacade().lookupIn(mPackage, classifierName);
                    if (parentClass == null) {
                        parentClass = Model.getCoreFactory().buildClass(classifierName, mPackage);
                        newElements.add(parentClass);
                    }
                    getGeneralization(currentPackage, parentClass, mClass);
                } else {
                    warnClassifierNotFound(superclassName, "a generalization");
                }
            }
        }
        if (interfaces != null) {
            addInterfaces(mClass, interfaces, forceIt);
        }
    }

    /**
     * Called from the parser when an anonymous inner class is found.
     * 
     * @param type The type of this anonymous class.
     */
    public void addAnonymousClass(String type) {
        addAnonymousClass(type, false);
    }

    /**
     * Called from the parser when an anonymous inner class is found.
     * 
     * @param type The type of this anonymous class.
     * @param forceIt Force addition by creating all that's missing.
     */
    void addAnonymousClass(String type, boolean forceIt) {
        String name = parseState.anonymousClass();
        try {
            Object mClassifier = getContext(type).get(getClassifierName(type), false, javaProfile);
            List<String> interfaces = new ArrayList<String>();
            if (Model.getFacade().isAInterface(mClassifier)) {
                interfaces.add(type);
            }
            addClass(name, (short) 0, EMPTY_STRING_LIST, Model.getFacade().isAClass(mClassifier) ? type : null, interfaces, "", forceIt);
        } catch (ClassifierNotFoundException e) {
            addClass(name, (short) 0, EMPTY_STRING_LIST, null, EMPTY_STRING_LIST, "", forceIt);
            LOG.info("Modeller.java: an anonymous class was created " + "although it could not be found in the classpath.");
        }
    }

    /**
     * Add an Interface to the model.
     * 
     * TODO: This method preserves the historical public API which is used by
     * other reverse engineering modules such as the Classfile module. This
     * really needs to be decoupled.
     * 
     * @param name The name of the interface.
     * @param modifiers A sequence of interface modifiers.
     * @param interfaces Zero or more strings with the names of extended
     *            interfaces. Can be fully qualified or just a simple interface
     *            name.
     * @param javadoc The javadoc comment. "" if no comment available.
     */
    public void addInterface(String name, short modifiers, List<String> interfaces, String javadoc) {
        addInterface(name, modifiers, EMPTY_STRING_LIST, interfaces, javadoc, false);
    }

    /**
     * Called from the parser when an interface declaration is found.
     * 
     * @param name The name of the interface.
     * @param modifiers A sequence of interface modifiers.
     * @param interfaces Zero or more strings with the names of extended
     *            interfaces. Can be fully qualified or just a simple interface
     *            name.
     * @param javadoc The javadoc comment. "" if no comment available.
     * @param forceIt Force addition by creating all that's missing.
     */
    void addInterface(String name, short modifiers, List<String> typeParameters, List<String> interfaces, String javadoc, boolean forceIt) {
        if (typeParameters != null && typeParameters.size() > 0) {
            logError("type parameters not supported on Interface", name);
        }
        Object mInterface = addClassifier(Model.getCoreFactory().createInterface(), name, modifiers, javadoc, typeParameters);
        if (getLevel() == 0) {
            return;
        }
        for (String interfaceName : interfaces) {
            Object parentInterface = null;
            try {
                parentInterface = getContext(interfaceName).get(getClassifierName(interfaceName), true, javaProfile);
                getGeneralization(currentPackage, parentInterface, mInterface);
            } catch (ClassifierNotFoundException e) {
                if (forceIt && interfaceName != null && model != null) {
                    LOG.info("Modeller.java: " + "forced creation of unknown interface " + interfaceName);
                    String packageName = getPackageName(interfaceName);
                    String classifierName = getClassifierName(interfaceName);
                    Object mPackage = (packageName.length() > 0) ? getPackage(packageName, false) : model;
                    parentInterface = Model.getFacade().lookupIn(mPackage, classifierName);
                    if (parentInterface == null) {
                        parentInterface = Model.getCoreFactory().buildInterface(classifierName, mPackage);
                        newElements.add(parentInterface);
                    }
                    getGeneralization(currentPackage, parentInterface, mInterface);
                } else {
                    warnClassifierNotFound(interfaceName, "a generalization");
                }
            }
        }
    }

    /**
     * Called from the parser when an enumeration declaration is found.
     * 
     * @param name The name of the class.
     * @param modifiers A sequence of class modifiers.
     * @param interfaces Zero or more strings with the names of implemented
     *            interfaces. Can be fully qualified or just a simple interface
     *            name.
     * @param javadoc The javadoc comment. null or "" if no comment available.
     * @param forceIt Force addition by creating all that's missing.
     */
    void addEnumeration(String name, short modifiers, List<String> interfaces, String javadoc, boolean forceIt) {
        Object mEnum = null;
        if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
            mEnum = addClassifier(Model.getCoreFactory().createClass(), name, modifiers, javadoc, EMPTY_STRING_LIST);
            Model.getCoreHelper().addStereotype(mEnum, getUML1Stereotype("enumeration"));
            ProjectManager.getManager().updateRoots();
        } else {
            mEnum = Model.getCoreFactory().createEnumeration();
            Object mNamespace;
            if (parseState.getClassifier() != null) {
                mNamespace = parseState.getClassifier();
            } else {
                parseState.outerClassifier();
                mNamespace = currentPackage;
            }
            if (LOG.isInfoEnabled()) {
                LOG.info("Created new enumeration for " + name);
            }
            Model.getCoreHelper().setName(mEnum, name);
            Model.getCoreHelper().setNamespace(mEnum, mNamespace);
            newElements.add(mEnum);
            parseState.innerClassifier(mEnum);
            parseStateStack.push(parseState);
            parseState = new ParseState(parseState, mEnum, currentPackage);
            setVisibility(mEnum, modifiers);
            if (getLevel() <= 0) {
                addDocumentationTag(mEnum, javadoc);
            }
        }
        if ((modifiers & JavaParser.ACC_ABSTRACT) > 0) {
            logError("Illegal \"abstract\" modifier on enum ", name);
        } else {
            Model.getCoreHelper().setAbstract(mEnum, false);
        }
        if ((modifiers & JavaParser.ACC_FINAL) > 0) {
            logError("Illegal \"final\" modifier on enum ", name);
        } else {
            Model.getCoreHelper().setLeaf(mEnum, true);
        }
        if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
            Model.getCoreHelper().setRoot(mEnum, false);
        }
        if (getLevel() == 0) {
            return;
        }
        if (interfaces != null) {
            addInterfaces(mEnum, interfaces, forceIt);
        }
    }

    /**
     * @param mClass
     * @param interfaces
     * @param forceIt
     */
    private void addInterfaces(Object mClass, List<String> interfaces, boolean forceIt) {
        for (String interfaceName : interfaces) {
            Object mInterface = null;
            try {
                mInterface = getContext(interfaceName).get(getClassifierName(interfaceName), true, javaProfile);
            } catch (ClassifierNotFoundException e) {
                if (forceIt && interfaceName != null && model != null) {
                    LOG.info("Modeller.java: " + "forced creation of unknown interface " + interfaceName);
                    String packageName = getPackageName(interfaceName);
                    String classifierName = getClassifierName(interfaceName);
                    Object mPackage = (packageName.length() > 0) ? getPackage(packageName, false) : model;
                    mInterface = Model.getFacade().lookupIn(mPackage, classifierName);
                    if (mInterface == null) {
                        mInterface = Model.getCoreFactory().buildInterface(classifierName, mPackage);
                        newElements.add(mInterface);
                    }
                } else {
                    warnClassifierNotFound(interfaceName, "an abstraction");
                }
            }
            if (mInterface != null && mInterface != mClass) {
                Object mAbstraction = getAbstraction(mInterface, mClass);
                if (Model.getFacade().getSuppliers(mAbstraction).size() == 0) {
                    Model.getCoreHelper().addSupplier(mAbstraction, mInterface);
                    Model.getCoreHelper().addClient(mAbstraction, mClass);
                }
                if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
                    Model.getCoreHelper().setNamespace(mAbstraction, currentPackage);
                    Model.getCoreHelper().addStereotype(mAbstraction, getUML1Stereotype(CoreFactory.REALIZE_STEREOTYPE));
                    ProjectManager.getManager().updateRoots();
                }
                newElements.add(mAbstraction);
            }
        }
    }

    /**
     * Called from the parser when an enumeration literal is found.
     * 
     * @param name The name of the enumerationLiteral.
     */
    void addEnumerationLiteral(String name) {
        Object enumeration = parseState.getClassifier();
        if (!isAEnumeration(enumeration)) {
            throw new ParseStateException("not an Enumeration");
        }
        short mod = JavaParser.ACC_PUBLIC | JavaParser.ACC_FINAL | JavaParser.ACC_STATIC;
        if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
            addAttribute(mod, null, name, null, null, true);
        } else {
            Model.getCoreFactory().buildEnumerationLiteral(name, enumeration);
        }
    }

    private boolean isAEnumeration(Object element) {
        if (Model.getFacade().isAEnumeration(element)) {
            return true;
        }
        if (!Model.getFacade().isAClass(element)) {
            return false;
        }
        return Model.getExtensionMechanismsHelper().hasStereotype(element, "enumeration");
    }

    /**
     * Add an annotation declaration
     * 
     * @param name identifier for annotation definition.
     * @param modifiers A sequence of modifiers.
     * @param javadoc The javadoc comment. null or "" if no comment available.
     * @param forceIt Force addition by creating all that's missing.
     */
    void addAnnotationDefinition(String name, short modifiers, String javadoc, boolean forceIt) {
        logError("Java 5 annotation definitions not supported", "@" + name);
    }

    /**
     * Called from the parser when an annotation declaration is found.
     * 
     * @param name identifier for annotation.
     */
    void addAnnotation(String name) {
        logError("Java 5 annotations not supported", "@" + name);
    }

    /**
     * Done adding an annotation.
     */
    void endAnnotation() {
    }

    void addTypeParameters(Object modelElement, List<String> typeParameters) {
        if (modelElement == null || typeParameters == null) {
            return;
        }
        if (Model.getFacade().getTemplateParameters(modelElement).size() == 0) {
            for (String parameter : typeParameters) {
                Pattern p = Pattern.compile("([^ ]*)( super | extends )?((.*))");
                Matcher m = p.matcher(parameter);
                if (m.matches()) {
                    String templateParameterName = m.group(1);
                    Object param = Model.getCoreFactory().createParameter();
                    Model.getCoreHelper().setName(param, templateParameterName);
                    Object templateParameter = Model.getCoreFactory().buildTemplateParameter(modelElement, param, null);
                    if (m.group(2) != null) {
                        buildTaggedValue(param, m.group(2).trim(), new String[] { m.group(3) });
                    }
                    Model.getCoreHelper().addTemplateParameter(modelElement, templateParameter);
                }
            }
        }
    }

    /**
     * Common code used by addClass and addInterface.
     * 
     * @param newClassifier Supply one if none is found in the model.
     * @param name Name of the classifier.
     * @param modifiers String of modifiers.
     * @param javadoc The javadoc comment. null or "" if no comment available.
     * @param typeParameters List of types for parameters (not implemented)
     * @return The newly created/found classifier.
     */
    private Object addClassifier(Object newClassifier, String name, short modifiers, String javadoc, List<String> typeParameters) {
        Object mClassifier;
        Object mNamespace;
        if (parseState.getClassifier() != null) {
            mClassifier = Model.getFacade().lookupIn(parseState.getClassifier(), name);
            mNamespace = parseState.getClassifier();
        } else {
            parseState.outerClassifier();
            mClassifier = Model.getFacade().lookupIn(currentPackage, name);
            mNamespace = currentPackage;
        }
        if (mClassifier == null) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Created new classifier for " + name);
            }
            mClassifier = newClassifier;
            Model.getCoreHelper().setName(mClassifier, name);
            Model.getCoreHelper().setNamespace(mClassifier, mNamespace);
            newElements.add(mClassifier);
        } else {
            if (LOG.isInfoEnabled()) {
                LOG.info("Found existing classifier for " + name);
            }
            cleanModelElement(mClassifier);
        }
        parseState.innerClassifier(mClassifier);
        if (parseState.getClassifier() == null) {
            if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
                if (Model.getFacade().getElementResidences(mClassifier).isEmpty()) {
                    Object resident = Model.getCoreFactory().createElementResidence();
                    Model.getCoreHelper().setResident(resident, mClassifier);
                    Model.getCoreHelper().setContainer(resident, parseState.getArtifact());
                }
            } else {
                Object artifact = parseState.getArtifact();
                Collection c = Model.getCoreHelper().getUtilizedElements(artifact);
                if (!c.contains(mClassifier)) {
                    Object manifestation = Model.getCoreFactory().buildManifestation(mClassifier);
                    Model.getCoreHelper().addManifestation(artifact, manifestation);
                }
            }
        }
        parseStateStack.push(parseState);
        parseState = new ParseState(parseState, mClassifier, currentPackage);
        setVisibility(mClassifier, modifiers);
        if (getLevel() <= 0) {
            addDocumentationTag(mClassifier, javadoc);
        }
        addTypeParameters(mClassifier, typeParameters);
        return mClassifier;
    }

    /**
     * Return the current import pass/level.
     * 
     * @return 0, 1, or 2 depending on current import level and pass of
     *         processing. Returns -1 if level isn't defined.
     */
    private int getLevel() {
        Object level = this.getAttribute("level");
        if (level != null) {
            return ((Integer) level).intValue();
        }
        return -1;
    }

    /**
     * Called from the parser when a classifier is completely parsed.
     */
    public void popClassifier() {
        parseState.removeObsoleteFeatures();
        parseState.removeObsoleteInnerClasses();
        parseState = parseStateStack.pop();
    }

    /**
     * Add an Operation to the current model
     * 
     * @param modifiers A sequence of operation modifiers.
     * @param returnType The return type of the operation.
     * @param name The name of the operation as a string
     * @param parameters A List of parameter declarations containing types and
     *            names.
     * @param javadoc The javadoc comment. null or "" if no comment available.
     * @return The operation.
     */
    public Object addOperation(short modifiers, String returnType, String name, List<ParameterDeclaration> parameters, String javadoc) {
        return addOperation(modifiers, EMPTY_STRING_LIST, returnType, name, parameters, javadoc, false);
    }

    /**
     * Called from the parser when an operation is found.
     * 
     * @param modifiers A sequence of operation modifiers.
     * @param returnType The return type of the operation.
     * @param name The name of the operation as a string
     * @param parameters A number of lists, each representing a parameter.
     * @param javadoc The javadoc comment. null or "" if no comment available.
     * @param forceIt Force addition by creating all that's missing.
     * @return The operation.
     */
    Object addOperation(short modifiers, List<String> typeParameters, String returnType, String name, List<ParameterDeclaration> parameters, String javadoc, boolean forceIt) {
        if (typeParameters != null && typeParameters.size() > 0) {
            logError("type parameters not supported on operation return type", name);
        }
        Object mOperation = getOperation(name);
        parseState.feature(mOperation);
        Model.getCoreHelper().setAbstract(mOperation, (modifiers & JavaParser.ACC_ABSTRACT) > 0);
        Model.getCoreHelper().setLeaf(mOperation, (modifiers & JavaParser.ACC_FINAL) > 0);
        if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
            Model.getCoreHelper().setRoot(mOperation, false);
        }
        setOwnerScope(mOperation, modifiers);
        setVisibility(mOperation, modifiers);
        if ((modifiers & JavaParser.ACC_SYNCHRONIZED) > 0) {
            Model.getCoreHelper().setConcurrency(mOperation, Model.getConcurrencyKind().getGuarded());
        } else if (Model.getFacade().getConcurrency(mOperation) == Model.getConcurrencyKind().getGuarded()) {
            Model.getCoreHelper().setConcurrency(mOperation, Model.getConcurrencyKind().getSequential());
        }
        Object[] c = Model.getFacade().getParameters(mOperation).toArray();
        for (Object parameter : c) {
            Model.getCoreHelper().removeParameter(mOperation, parameter);
        }
        Object mParameter;
        String typeName;
        Object mClassifier = null;
        if (returnType == null || ("void".equals(returnType) && name.equals(Model.getFacade().getName(parseState.getClassifier())))) {
            Model.getCoreHelper().addStereotype(mOperation, getStereotype(mOperation, "create", "BehavioralFeature"));
            ProjectManager.getManager().updateRoots();
        } else {
            try {
                mClassifier = getContext(returnType).get(getClassifierName(returnType), false, javaProfile);
            } catch (ClassifierNotFoundException e) {
                if (forceIt && returnType != null && model != null) {
                    LOG.info("Modeller.java: " + "forced creation of unknown classifier " + returnType);
                    String packageName = getPackageName(returnType);
                    String classifierName = getClassifierName(returnType);
                    Object mPackage = (packageName.length() > 0) ? getPackage(packageName, false) : model;
                    mClassifier = Model.getFacade().lookupIn(mPackage, classifierName);
                    if (mClassifier == null) {
                        mClassifier = Model.getCoreFactory().buildClass(classifierName, mPackage);
                        newElements.add(mClassifier);
                    }
                } else {
                    warnClassifierNotFound(returnType, "operation return type");
                }
            }
            if (mClassifier != null) {
                mParameter = buildReturnParameter(mOperation, mClassifier);
            }
        }
        for (ParameterDeclaration parameter : parameters) {
            typeName = parameter.getType();
            if (typeName.endsWith("...")) {
                logError("Unsupported variable length parameter list notation", parameter.getName());
            }
            mClassifier = null;
            try {
                mClassifier = getContext(typeName).get(getClassifierName(typeName), false, javaProfile);
            } catch (ClassifierNotFoundException e) {
                if (forceIt && typeName != null && model != null) {
                    LOG.info("Modeller.java: " + "forced creation of unknown classifier " + typeName);
                    String packageName = getPackageName(typeName);
                    String classifierName = getClassifierName(typeName);
                    Object mPackage = (packageName.length() > 0) ? getPackage(packageName, false) : model;
                    mClassifier = Model.getFacade().lookupIn(mPackage, classifierName);
                    if (mClassifier == null) {
                        mClassifier = Model.getCoreFactory().buildClass(classifierName, mPackage);
                        newElements.add(mClassifier);
                    }
                } else {
                    warnClassifierNotFound(typeName, "operation params");
                }
            }
            if (mClassifier != null) {
                mParameter = buildInParameter(mOperation, mClassifier, parameter.getName());
                if (!Model.getFacade().isAClassifier(mClassifier)) {
                    logError("Modeller.java: a valid type for a parameter " + "could not be resolved:\n " + "In file: " + fileName + ", for operation: " + Model.getFacade().getName(mOperation) + ", for parameter: ", Model.getFacade().getName(mParameter));
                }
            }
        }
        addDocumentationTag(mOperation, javadoc);
        return mOperation;
    }

    private Object buildInParameter(Object operation, Object classifier, String name) {
        Object parameter = buildParameter(operation, classifier, name);
        Model.getCoreHelper().setKind(parameter, Model.getDirectionKind().getInParameter());
        return parameter;
    }

    private Object buildReturnParameter(Object operation, Object classifier) {
        Object parameter = buildParameter(operation, classifier, "return");
        Model.getCoreHelper().setKind(parameter, Model.getDirectionKind().getReturnParameter());
        return parameter;
    }

    private Object buildParameter(Object operation, Object classifier, String name) {
        Object parameter = Model.getCoreFactory().buildParameter(operation, classifier);
        Model.getCoreHelper().setName(parameter, name);
        return parameter;
    }

    /**
     * Warn user that information available in input source will not be
     * reflected accurately in the model.
     * 
     * @param name name of the classifier which wasn't found
     * @param operation - a string indicating what type of operation was being
     *            attempted
     */
    private void warnClassifierNotFound(String name, String operation) {
        logError("Modeller.java: a classifier (" + name + ") that was in the source " + "file could not be generated in the model ", operation);
    }

    /**
     * Add an error message to the log to be shown to the user.
     * <p>
     * TODO: This currently just writes to the error log. It needs to return
     * errors some place that the user can see them and deal with them. We also
     * need a way to get the line and column numbers to help the user track the
     * problem down.
     */
    private void logError(String message, String identifier) {
        LOG.warn(message + " : " + identifier);
    }

    /**
     * Called from the parser when a class field is parsed. This can occur in a
     * class block where it indicates a method body to to be added to an
     * operation (An operation will have exactly one Java body) OR it can occur
     * in the enum declaration (not currently supported).
     * 
     * TODO: Support use in an enum declaration
     * 
     * @param op An operation.
     * @param body A method body.
     */
    public void addBodyToOperation(Object op, String body) {
        if (op == null || !Model.getFacade().isAOperation(op)) {
            throw new ParseStateException("Found class body in context other than a class");
        }
        if (body == null || body.length() == 0) {
            return;
        }
        Object method = getMethod(Model.getFacade().getName(op));
        parseState.feature(method);
        if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
            Model.getCoreHelper().setBody(method, Model.getDataTypesFactory().createProcedureExpression("Java", body));
        } else {
            Model.getDataTypesHelper().setBody(method, body);
            Model.getDataTypesHelper().setLanguage(method, "Java");
        }
        Model.getCoreHelper().addMethod(op, method);
        if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
            Model.getCoreHelper().addFeature(Model.getFacade().getOwner(op), method);
        } else {
            Model.getCoreHelper().addOwnedElement(Model.getFacade().getOwner(op), method);
        }
    }

    /**
     * Called from the parser when an attribute is found.
     * 
     * @param modifiers A sequence of attribute modifiers.
     * @param typeSpec The attribute's type.
     * @param name The name of the attribute.
     * @param initializer The initial value of the attribute.
     * @param javadoc The javadoc comment. null or "" if no comment available.
     */
    public void addAttribute(short modifiers, String typeSpec, String name, String initializer, String javadoc) {
        addAttribute(modifiers, typeSpec, name, initializer, javadoc, false);
    }

    /**
     * Called from the parser when an attribute is found.
     * 
     * @param modifiers A sequence of attribute modifiers.
     * @param typeSpec The attribute's type.
     * @param name The name of the attribute.
     * @param initializer The initial value of the attribute.
     * @param javadoc The javadoc comment. null or "" if no comment available.
     * @param forceIt Force addition by creating all that's missing.
     */
    void addAttribute(short modifiers, String typeSpec, String name, String initializer, String javadoc, boolean forceIt) {
        String multiplicity = "1_1";
        Object mClassifier = null;
        if (typeSpec != null) {
            if (!arraysAsDatatype && typeSpec.indexOf('[') != -1) {
                typeSpec = typeSpec.substring(0, typeSpec.indexOf('['));
                multiplicity = "1_N";
            }
            try {
                mClassifier = getContext(typeSpec).get(getClassifierName(typeSpec), false, javaProfile);
            } catch (ClassifierNotFoundException e) {
                if (forceIt && typeSpec != null && model != null) {
                    LOG.info("Modeller.java: forced creation of" + " unknown classifier " + typeSpec);
                    String packageName = getPackageName(typeSpec);
                    String classifierName = getClassifierName(typeSpec);
                    Object mPackage = (packageName.length() > 0) ? getPackage(packageName, false) : model;
                    mClassifier = Model.getFacade().lookupIn(mPackage, classifierName);
                    if (mClassifier == null) {
                        mClassifier = Model.getCoreFactory().buildClass(classifierName, mPackage);
                        newElements.add(mClassifier);
                    }
                } else {
                    warnClassifierNotFound(typeSpec, "an attribute");
                }
            }
            if (mClassifier == null) {
                logError("failed to find or create type", typeSpec);
                return;
            }
        }
        if (mClassifier == null || noAssociations || Model.getFacade().isADataType(mClassifier) || (Model.getFacade().getNamespace(mClassifier) == getPackage(JAVA_PACKAGE, true))) {
            Object mAttribute = parseState.getAttribute(name);
            if (mAttribute == null) {
                mAttribute = buildAttribute(parseState.getClassifier(), mClassifier, name);
            }
            parseState.feature(mAttribute);
            setOwnerScope(mAttribute, modifiers);
            setVisibility(mAttribute, modifiers);
            Model.getCoreHelper().setMultiplicity(mAttribute, multiplicity);
            if (Model.getFacade().isAClassifier(mClassifier)) {
                Model.getCoreHelper().setType(mAttribute, mClassifier);
            } else {
                logError("Modeller.java: a valid type for a parameter " + "could not be resolved:\n " + "In file: " + fileName + ", for attribute: ", Model.getFacade().getName(mAttribute));
            }
            if (initializer != null) {
                initializer = initializer.replace('\n', ' ');
                initializer = initializer.replace('\t', ' ');
                Object newInitialValue = Model.getDataTypesFactory().createExpression("Java", initializer);
                Model.getCoreHelper().setInitialValue(mAttribute, newInitialValue);
            }
            if ((modifiers & JavaParser.ACC_FINAL) > 0) {
                Model.getCoreHelper().setReadOnly(mAttribute, true);
            } else if (Model.getFacade().isReadOnly(mAttribute)) {
                Model.getCoreHelper().setReadOnly(mAttribute, true);
            }
            addDocumentationTag(mAttribute, javadoc);
        } else {
            Object mAssociationEnd = getAssociationEnd(name, mClassifier);
            Model.getCoreHelper().setStatic(mAssociationEnd, (modifiers & JavaParser.ACC_STATIC) > 0);
            setVisibility(mAssociationEnd, modifiers);
            Model.getCoreHelper().setMultiplicity(mAssociationEnd, multiplicity);
            Model.getCoreHelper().setType(mAssociationEnd, mClassifier);
            Model.getCoreHelper().setName(mAssociationEnd, name);
            if ((modifiers & JavaParser.ACC_FINAL) > 0) {
                Model.getCoreHelper().setReadOnly(mAssociationEnd, true);
            }
            if (!mClassifier.equals(parseState.getClassifier())) {
                Model.getCoreHelper().setNavigable(mAssociationEnd, true);
            }
            addDocumentationTag(mAssociationEnd, javadoc);
        }
    }

    /**
     * Find a generalization in the model. If it does not exist, a new
     * generalization is created.
     * 
     * @param mPackage Look in this package.
     * @param parent The superclass.
     * @param child The subclass.
     * @return The generalization found or created.
     */
    private Object getGeneralization(Object mPackage, Object parent, Object child) {
        Object mGeneralization = Model.getFacade().getGeneralization(child, parent);
        if (mGeneralization == null) {
            mGeneralization = Model.getCoreFactory().buildGeneralization(child, parent);
            newElements.add(mGeneralization);
        }
        if (mGeneralization != null && Model.getFacade().getUmlVersion().charAt(0) == '1') {
            Model.getCoreHelper().setNamespace(mGeneralization, mPackage);
        }
        return mGeneralization;
    }

    /**
     * Find an abstraction<<realize>> in the model. If it does not exist, a new
     * abstraction is created.
     * 
     * @param parent The superclass.
     * @param child The subclass.
     * @return The abstraction found or created.
     */
    private Object getAbstraction(Object parent, Object child) {
        Object mAbstraction = null;
        for (Iterator i = Model.getFacade().getClientDependencies(child).iterator(); i.hasNext(); ) {
            mAbstraction = i.next();
            Collection c = Model.getFacade().getSuppliers(mAbstraction);
            if (c == null || c.size() == 0) {
                Model.getCoreHelper().removeClientDependency(child, mAbstraction);
            } else {
                if (parent != c.toArray()[0]) {
                    mAbstraction = null;
                } else {
                    break;
                }
            }
        }
        if (mAbstraction == null) {
            mAbstraction = Model.getCoreFactory().buildAbstraction(makeAbstractionName(child, parent), parent, child);
            newElements.add(mAbstraction);
        }
        return mAbstraction;
    }

    /**
     * Find a class in a package. If it does not exist, a new class is
     * created.
     * 
     * @param mPackage Look in this package.
     * @param name The name of the class.
     * @return The class found or created.
     */
    private Object getClass(Object mPackage, String name) {
        Object mClass = null;
        for (Object c : Model.getCoreHelper().getAllClasses(mPackage)) {
            if (name.equals(Model.getFacade().getName(c))) {
                mClass = c;
                break;
            }
        }
        if (mClass == null) {
            mClass = Model.getCoreFactory().buildClass(name, mPackage);
            newElements.add(mClass);
        }
        return mClass;
    }

    /**
     * Find a package in the project. If it does not exist, a new package is
     * created in the user model.
     * 
     * @param name The name of the package.
     * @param useProfile also look in the Java profile if true
     * @return The package found or created.
     */
    private Object getPackage(String name, boolean useProfile) {
        Object mPackage = searchPackageInModel(name, useProfile);
        if (mPackage == null) {
            Object currentNs = model;
            StringTokenizer st = new StringTokenizer(name, ".");
            while (st.hasMoreTokens()) {
                String rname = st.nextToken();
                mPackage = Model.getFacade().lookupIn(currentNs, rname);
                if (mPackage == null || !Model.getFacade().isAPackage(mPackage)) {
                    mPackage = Model.getModelManagementFactory().buildPackage(getRelativePackageName(rname));
                    Model.getCoreHelper().addOwnedElement(currentNs, mPackage);
                    newElements.add(mPackage);
                }
                currentNs = mPackage;
            }
        }
        return mPackage;
    }

    /**
     * Search recursively for nested packages in the user model. So if you pass
     * a package org.argouml.kernel , this method searches for a package kernel,
     * that is owned by a package argouml, which is owned by a package org. This
     * method is required to nest the parsed packages. It optionally first
     * searches in the Java profile.
     * 
     * @param name The fully qualified package name of the package we are
     *            searching for.
     * @param useProfile first have a look in the Java profile if true
     * @return The found package or null, if it is not in the model.
     */
    private Object searchPackageInModel(String name, boolean useProfile) {
        Object ret = null;
        if ("".equals(getPackageName(name))) {
            if (useProfile && javaProfile != null) {
                try {
                    Object m = javaProfile.getProfilePackages().iterator().next();
                    ret = Model.getFacade().lookupIn(m, name);
                } catch (Exception e) {
                    ret = null;
                }
            }
            if (ret == null) {
                ret = Model.getFacade().lookupIn(model, name);
            }
            return ret;
        }
        Object owner = searchPackageInModel(getPackageName(name), useProfile);
        return owner == null ? null : Model.getFacade().lookupIn(owner, getRelativePackageName(name));
    }

    /**
     * Find an operation in the currentClassifier. If the operation is not
     * found, a new is created.
     * 
     * @param name The name of the operation.
     * @return The operation found or created.
     */
    private Object getOperation(String name) {
        Object mOperation = parseState.getOperation(name);
        if (mOperation != null) {
            LOG.info("Getting the existing operation " + name);
        } else {
            LOG.info("Creating a new operation " + name);
            Object cls = parseState.getClassifier();
            Object returnType = ProjectManager.getManager().getCurrentProject().getDefaultReturnType();
            mOperation = Model.getCoreFactory().buildOperation2(cls, returnType, name);
            newElements.add(mOperation);
        }
        return mOperation;
    }

    /**
     * Find an operation in the currentClassifier. If the operation is not
     * found, a new is created.
     * 
     * @param name The name of the method.
     * @return The method found or created.
     */
    private Object getMethod(String name) {
        Object method = parseState.getMethod(name);
        if (method != null) {
            LOG.info("Getting the existing method " + name);
        } else {
            LOG.info("Creating a new method " + name);
            method = Model.getCoreFactory().buildMethod(name);
            newElements.add(method);
            if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
                Model.getCoreHelper().addFeature(parseState.getClassifier(), method);
            }
        }
        return method;
    }

    /**
     * Build a new attribute in the current classifier.
     * 
     * @param classifier the model were are reverse engineering into
     * @param type the the type of the new attribute
     * @param name The name of the attribute.
     * @return The attribute found or created.
     */
    private Object buildAttribute(Object classifier, Object type, String name) {
        Object mAttribute = Model.getCoreFactory().buildAttribute2(classifier, type);
        newElements.add(mAttribute);
        Model.getCoreHelper().setName(mAttribute, name);
        return mAttribute;
    }

    /**
     * Find an associationEnd for a binary Association from the
     * currentClassifier to the type specified. If not found, a new is created.
     * 
     * @param name The name of the attribute.
     * @param mClassifier Where the association ends.
     * @return The attribute found or created.
     */
    private Object getAssociationEnd(String name, Object mClassifier) {
        Object mAssociationEnd = null;
        for (Iterator i = Model.getFacade().getAssociationEnds(mClassifier).iterator(); i.hasNext(); ) {
            Object ae = i.next();
            Object assoc = Model.getFacade().getAssociation(ae);
            if (name.equals(Model.getFacade().getName(ae)) && Model.getFacade().getConnections(assoc).size() == 2 && Model.getFacade().getType(Model.getFacade().getNextEnd(ae)) == parseState.getClassifier()) {
                mAssociationEnd = ae;
            }
        }
        if (mAssociationEnd == null && !noAssociations) {
            String newName = makeAssociationName(parseState.getClassifier(), mClassifier);
            Object mAssociation = buildDirectedAssociation(newName, parseState.getClassifier(), mClassifier);
            mAssociationEnd = Model.getFacade().getAssociationEnd(mClassifier, mAssociation);
        }
        return mAssociationEnd;
    }

    /**
     * Build a unidirectional association between two Classifiers.
     * 
     * @param name name of the association
     * @param sourceClassifier source classifier (end which is non-navigable)
     * @param destClassifier destination classifier (end which is navigable)
     * @return newly created Association
     */
    public static Object buildDirectedAssociation(String name, Object sourceClassifier, Object destClassifier) {
        return Model.getCoreFactory().buildAssociation(destClassifier, true, sourceClassifier, false, name);
    }

    /**
     * Get the stereotype with a specific name. UML 1.x only.
     * 
     * @param name The name of the stereotype.
     * @return The stereotype.
     */
    private Object getUML1Stereotype(String name) {
        LOG.debug("Trying to find a stereotype of name <<" + name + ">>");
        Object stereotype = Model.getFacade().lookupIn(model, name);
        if (stereotype == null) {
            LOG.debug("Couldn't find so creating it");
            return Model.getExtensionMechanismsFactory().buildStereotype(name, model);
        }
        if (!Model.getFacade().isAStereotype(stereotype)) {
            LOG.debug("Found something that isn't a stereotype so creating it");
            return Model.getExtensionMechanismsFactory().buildStereotype(name, model);
        }
        LOG.debug("Found it");
        return stereotype;
    }

    /**
     * Find the first suitable stereotype with baseclass for a given object.
     * 
     * @param me
     * @param name
     * @param baseClass
     * @return the stereotype if found
     * 
     * @throws IllegalArgumentException if the desired stereotypes for the
     *             modelelement and baseclass was not found and could not be
     *             created. No stereotype is created.
     */
    private Object getStereotype(Object me, String name, String baseClass) {
        Collection models = ProjectManager.getManager().getCurrentProject().getModels();
        Collection stereos = Model.getExtensionMechanismsHelper().getAllPossibleStereotypes(models, me);
        Object stereotype = null;
        if (stereos != null && stereos.size() > 0) {
            Iterator iter = stereos.iterator();
            while (iter.hasNext()) {
                stereotype = iter.next();
                if (Model.getExtensionMechanismsHelper().isStereotypeInh(stereotype, name, baseClass)) {
                    LOG.info("Returning the existing stereotype of <<" + Model.getFacade().getName(stereotype) + ">>");
                    return stereotype;
                }
            }
        }
        if (Model.getFacade().getUmlVersion().charAt(0) != '1') {
            throw new IllegalArgumentException("Could not find " + "a suitable stereotype for " + Model.getFacade().getName(me) + " -  stereotype: <<" + name + ">> base: " + baseClass + ".\n" + "Check if environment variable eUML.resources " + "is correctly set.");
        }
        stereotype = getUML1Stereotype(name);
        if (stereotype != null) {
            Model.getExtensionMechanismsHelper().addBaseClass(stereotype, me);
            return stereotype;
        }
        throw new IllegalArgumentException("Could not find " + "a suitable stereotype for " + Model.getFacade().getName(me) + " -  stereotype: <<" + name + ">> base: " + baseClass);
    }

    /**
     * Return the tagged value with a specific tag.
     * 
     * @param element The tagged value belongs to this.
     * @param name The tag.
     * @return The found tag. A new is created if not found.
     */
    private Object getTaggedValue(Object element, String name) {
        Object tv = Model.getFacade().getTaggedValue(element, name);
        if (tv == null) {
            String[] empties = { "" };
            buildTaggedValue(element, name, empties);
            tv = Model.getFacade().getTaggedValue(element, name);
        }
        return tv;
    }

    /**
     * This classifier was earlier generated by reference but now it is its time
     * to be parsed so we clean out remnants.
     * 
     * @param element that they are removed from
     */
    private void cleanModelElement(Object element) {
        Object tv = Model.getFacade().getTaggedValue(element, Facade.GENERATED_TAG);
        while (tv != null) {
            Model.getUmlFactory().delete(tv);
            tv = Model.getFacade().getTaggedValue(element, Facade.GENERATED_TAG);
        }
    }

    /**
     * Get the package name from a fully specified classifier name.
     * 
     * @param name A fully specified classifier name.
     * @return The package name.
     */
    private String getPackageName(String name) {
        name = stripVarargAndGenerics(name);
        int lastDot = name.lastIndexOf('.');
        if (lastDot == -1) {
            return "";
        }
        String pkgName = name.substring(0, lastDot);
        if (Character.isUpperCase(getRelativePackageName(pkgName).charAt(0))) {
            return getPackageName(pkgName);
        } else {
            return pkgName;
        }
    }

    /**
     * Get the relative package name from a fully qualified package name. So if
     * the parameter is 'org.argouml.kernel' the method is supposed to return
     * 'kernel' (the package kernel is in package 'org.argouml').
     * 
     * @param packageName A fully qualified package name.
     * @return The relative package name.
     */
    private String getRelativePackageName(String packageName) {
        return getClassifierName(packageName);
    }

    /**
     * Get the classifier name from a fully specified classifier name.
     * <p>
     * FIXME: Most uses of this method are wrong. We should be adding context
     * such as package names or outer classifier names, not removing it, before
     * doing lookup so that the search methods have the fully qualified name to
     * work with.
     * 
     * @param name A fully specified classifier name.
     * @return The classifier name.
     */
    private String getClassifierName(String name) {
        name = stripVarargAndGenerics(name);
        int lastDot = name.lastIndexOf('.');
        if (lastDot == -1) {
            return name;
        }
        return name.substring(lastDot + 1);
    }

    /**
     * Set the visibility for a model element.
     * 
     * @param element The model element.
     * @param modifiers A sequence of modifiers which may contain 'private',
     *            'protected' or 'public'.
     */
    private void setVisibility(Object element, short modifiers) {
        if ((modifiers & JavaParser.ACC_PRIVATE) > 0) {
            Model.getCoreHelper().setVisibility(element, Model.getVisibilityKind().getPrivate());
        } else if ((modifiers & JavaParser.ACC_PROTECTED) > 0) {
            Model.getCoreHelper().setVisibility(element, Model.getVisibilityKind().getProtected());
        } else if ((modifiers & JavaParser.ACC_PUBLIC) > 0) {
            Model.getCoreHelper().setVisibility(element, Model.getVisibilityKind().getPublic());
        } else {
            Model.getCoreHelper().setVisibility(element, Model.getVisibilityKind().getPackage());
        }
    }

    /**
     * Set the owner scope for a feature.
     * 
     * @param feature The feature.
     * @param modifiers A sequence of modifiers which may contain 'static'.
     */
    private void setOwnerScope(Object feature, short modifiers) {
        Model.getCoreHelper().setStatic(feature, (modifiers & JavaParser.ACC_STATIC) > 0);
    }

    /**
     * Get the context for a classifier name that may or may not be fully
     * qualified. The context contains either the user model, or a package
     * or class inside the user model, or a package or class in the Java
     * profile.
     * 
     * @param name the classifier name
     * @return the context
     */
    private Context getContext(String name) {
        Context context = parseState.getContext();
        String packageName = getPackageName(name);
        Object pkg = model;
        if (!"".equals(packageName)) {
            pkg = getPackage(packageName, true);
        }
        String classifierName = name.substring(packageName.length());
        if (classifierName.charAt(0) == '.') {
            classifierName = classifierName.substring(1);
        }
        classifierName = stripVarargAndGenerics(classifierName);
        int lastDot = classifierName.lastIndexOf('.');
        if (lastDot != -1) {
            String clsName = classifierName.substring(0, lastDot);
            Object cls = getClass(pkg, clsName);
            context = new OuterClassifierContext(context.getContext(), cls, pkg, clsName + '$');
        } else if (!"".equals(packageName)) {
            context = new PackageContext(context, pkg);
        }
        return context;
    }

    /**
     * Add the contents of a single standard javadoc tag to the model element.
     * Usually this will be added as a tagged value.
     * 
     * This is called from {@link #addDocumentationTag} only.
     * 
     * @param me the model element to add to
     * @param sTagName the name of the javadoc tag
     * @param sTagData the contents of the javadoc tag
     */
    private void addJavadocTagContents(Object me, String sTagName, String[] sTagData) {
        if (sTagData.length == 0 || sTagData[0] == null) {
            LOG.debug("Called addJavadocTagContents with no tag data!");
            return;
        }
        int colonPos = (sTagData != null) ? sTagData[0].indexOf(':') : -1;
        if (colonPos != -1 && (("invariant".equals(sTagName)) || ("pre-condition".equals(sTagName)) || ("post-condition".equals(sTagName)))) {
            String sContext = OCLUtil.getContextString(me);
            String name = sTagData[0].substring(0, colonPos);
            String body = null;
            if (sTagName.equals("invariant")) {
                body = sContext + " inv " + sTagData;
            } else if (sTagName.equals("pre-condition")) {
                body = sContext + " pre " + sTagData;
            } else {
                body = sContext + " post " + sTagData;
            }
            Object bexpr = Model.getDataTypesFactory().createBooleanExpression("OCL", body);
            Object mc = Model.getCoreFactory().buildConstraint(name, bexpr);
            Model.getCoreHelper().addConstraint(me, mc);
            if (Model.getFacade().getNamespace(me) != null) {
                Model.getCoreHelper().addOwnedElement(Model.getFacade().getNamespace(me), mc);
            }
        } else {
            if ("stereotype".equals(sTagName)) {
                Object tv = getTaggedValue(me, sTagName);
                if (tv != null) {
                    String sStereotype = Model.getFacade().getValueOfTag(tv);
                    if (sStereotype != null && sStereotype.length() > 0) {
                        sTagData[0] = sStereotype + ',' + sTagData[0];
                    }
                }
                HashSet<String> stSet = new HashSet<String>();
                StringTokenizer st = new StringTokenizer(sTagData[0], ", ");
                while (st.hasMoreTokens()) {
                    stSet.add(st.nextToken().trim());
                }
                StringBuffer sb = new StringBuffer();
                Iterator<String> iter = stSet.iterator();
                while (iter.hasNext()) {
                    if (sb.length() > 0) {
                        sb.append(',');
                    }
                    sb.append(iter.next());
                }
                sTagData[0] = sb.toString();
            }
            buildTaggedValue(me, sTagName, sTagData);
        }
    }

    private void buildTaggedValue(Object me, String sTagName, String[] sTagData) {
        Object tv = Model.getFacade().getTaggedValue(me, sTagName);
        if (tv == null) {
            Model.getExtensionMechanismsHelper().addTaggedValue(me, Model.getExtensionMechanismsFactory().buildTaggedValue(sTagName, sTagData[0]));
        } else {
            Model.getExtensionMechanismsHelper().setDataValues(tv, sTagData);
        }
    }

    /**
     * Add the javadocs as a tagged value 'documentation' to the model element.
     * All comment delimiters are removed prior to adding the comment.
     * 
     * Added 2001-10-05 STEFFEN ZSCHALER.
     * 
     * @param modelElement the model element to which to add the documentation
     * @param sJavaDocs the documentation comments to add ("" or null if no java
     *            docs)
     */
    private void addDocumentationTag(Object modelElement, String sJavaDocs) {
        if ((sJavaDocs != null) && (sJavaDocs.trim().length() >= 5)) {
            StringBuffer sbPureDocs = new StringBuffer(80);
            String sCurrentTagName = null;
            String[] sCurrentTagData = { null };
            int nStartPos = 3;
            boolean fHadAsterisk = true;
            while (nStartPos < sJavaDocs.length()) {
                switch(sJavaDocs.charAt(nStartPos)) {
                    case '*':
                        fHadAsterisk = true;
                        nStartPos++;
                        break;
                    case ' ':
                    case '\t':
                        if (!fHadAsterisk) {
                            nStartPos++;
                            break;
                        }
                    default:
                        int j = nStartPos;
                        while ((j < sJavaDocs.length()) && ((sJavaDocs.charAt(j) == ' ') || (sJavaDocs.charAt(j) == '\t'))) {
                            j++;
                        }
                        if (j < sJavaDocs.length()) {
                            if (sJavaDocs.charAt(j) == '@') {
                                int lineEndPos = 0;
                                if (sJavaDocs.indexOf('\n', j) < 0) {
                                    lineEndPos = sJavaDocs.length() - 2;
                                } else {
                                    lineEndPos = sJavaDocs.indexOf('\n', j) + 1;
                                }
                                sbPureDocs.append(sJavaDocs.substring(j, lineEndPos));
                                if (sCurrentTagName != null) {
                                    addJavadocTagContents(modelElement, sCurrentTagName, sCurrentTagData);
                                }
                                int nTemp = sJavaDocs.indexOf(' ', j + 1);
                                if (nTemp == -1) {
                                    nTemp = sJavaDocs.length() - 1;
                                }
                                sCurrentTagName = sJavaDocs.substring(j + 1, nTemp);
                                int nTemp1 = sJavaDocs.indexOf('\n', ++nTemp);
                                if (nTemp1 == -1) {
                                    nTemp1 = sJavaDocs.length();
                                } else {
                                    nTemp1++;
                                }
                                sCurrentTagData[0] = sJavaDocs.substring(nTemp, nTemp1);
                                nStartPos = nTemp1;
                            } else {
                                int nTemp = sJavaDocs.indexOf('\n', nStartPos);
                                if (nTemp == -1) {
                                    nTemp = sJavaDocs.length();
                                } else {
                                    nTemp++;
                                }
                                if (sCurrentTagName != null) {
                                    sbPureDocs.append(sJavaDocs.substring(nStartPos, nTemp));
                                    sCurrentTagData[0] += " " + sJavaDocs.substring(nStartPos, nTemp);
                                } else {
                                    sbPureDocs.append(sJavaDocs.substring(nStartPos, nTemp));
                                }
                                nStartPos = nTemp;
                            }
                        }
                        fHadAsterisk = false;
                }
            }
            sJavaDocs = sbPureDocs.toString();
            sJavaDocs = removeTrailingSlash(sJavaDocs);
            if (sCurrentTagName != null) {
                sCurrentTagData[0] = removeTrailingSlash(sCurrentTagData[0]);
                addJavadocTagContents(modelElement, sCurrentTagName, sCurrentTagData);
            }
            String[] javadocs = { sJavaDocs };
            buildTaggedValue(modelElement, Argo.DOCUMENTATION_TAG, javadocs);
            addStereotypes(modelElement);
        }
    }

    private String removeTrailingSlash(String s) {
        if (s.endsWith("\n/")) {
            return s.substring(0, s.length() - 2);
        } else if (s.endsWith("*/")) {
            return s.substring(0, s.length() - 2);
        } else if (s.endsWith("/")) {
            return s.substring(0, s.length() - 1);
        } else {
            return s;
        }
    }

    private String stripVarargAndGenerics(String name) {
        if (name != null) {
            if (name.endsWith("...")) {
                name = name.substring(0, name.length() - 3);
            }
            if (name.endsWith(">")) {
                int i = name.length() - 2;
                int cnt = 1;
                while (i >= 0 && cnt > 0) {
                    if (name.charAt(i) == '<') {
                        cnt--;
                    } else if (name.charAt(i) == '>') {
                        cnt++;
                    }
                    i--;
                }
                name = name.substring(0, i + 1);
            }
        }
        return name;
    }

    private void addStereotypes(Object modelElement) {
        if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
            Object tv = Model.getFacade().getTaggedValue(modelElement, "stereotype");
            if (tv != null) {
                String stereo = Model.getFacade().getValueOfTag(tv);
                if (stereo != null && stereo.length() > 0) {
                    StringTokenizer st = new StringTokenizer(stereo, ", ");
                    while (st.hasMoreTokens()) {
                        Model.getCoreHelper().addStereotype(modelElement, getUML1Stereotype(st.nextToken().trim()));
                    }
                    ProjectManager.getManager().updateRoots();
                }
                Model.getUmlFactory().delete(tv);
            }
        }
    }

    /**
     * Add a parsed method call to the collection of method calls.
     * 
     * @param methodName The method name called.
     */
    public void addCall(String methodName) {
        methodCalls.add(methodName);
    }

    /**
     * Get collection of method calls.
     * 
     * @return list containing collected method calls
     */
    public synchronized List<String> getMethodCalls() {
        return methodCalls;
    }

    /**
     * Clear collected method calls.
     */
    public void clearMethodCalls() {
        methodCalls.clear();
    }

    /**
     * Add a local variable declaration to the list of variables.
     * 
     * @param type type of declared variable
     * @param name name of declared variable
     */
    public void addLocalVariableDeclaration(String type, String name) {
        localVariables.put(name, type);
    }

    /**
     * Return the collected set of local variable declarations.
     * 
     * @return hash table containing all local variable declarations.
     */
    public Hashtable<String, String> getLocalVariableDeclarations() {
        return localVariables;
    }

    /**
     * Clear the set of local variable declarations.
     */
    public void clearLocalVariableDeclarations() {
        localVariables.clear();
    }

    /**
     * Get the elements which were created while reverse engineering this file.
     * 
     * @return the collection of elements
     */
    public Collection<Object> getNewElements() {
        return newElements;
    }

    /**
     * Set flag that controls name generation. Artificial names are generated by
     * default for historical reasons, but in most cases they are just clutter.
     * 
     * @param generateNamesFlag true to generate artificial names of the form
     *            "From->To" for Associations, Dependencies, etc.
     */
    public void setGenerateNames(boolean generateNamesFlag) {
        generateNames = generateNamesFlag;
    }
}
