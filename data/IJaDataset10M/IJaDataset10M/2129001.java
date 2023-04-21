package ch.sahits.model.java;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.eclipse.jdt.core.IJavaProject;
import ch.sahits.model.GeneratedClass;

/**
 * This class is the Java specific extension of {@link GeneratedClass}.
 * The data class model is extended with Java language specific attributes.
 * @author Andi Hotz
 * @since 0.9.3
 *
 */
public class GeneratedJavaClass extends GeneratedClass implements IGeneratedJavaClass2 {

    /**
	 * List with all implemented interfaces
	 */
    @SuppressWarnings("unchecked")
    private List<Class> interfaces = new Vector<Class>();

    /** visibility of the class */
    private EVisibility visibility = null;

    /** Flag indicating if the class is final */
    private boolean _final = false;

    /** Flag indicating if the class is abstract */
    private boolean _abstract = false;

    /**
	 * The project the class belongs to
	 * @uml.property  name="jProject"
	 */
    private IJavaProject jProject = null;

    /**
	 * Local (workspace) path to the src directory of the project
	 * @uml.property  name="srcPath"
	 */
    private String srcPath = null;

    /**
	 * Name of the package
	 * @uml.property  name="packageName"
	 */
    private String packageName = null;

    /**
	 * Flag to indicate if the class is generated with a jetemplate
	 * @uml.property  name="jetFileUse"
	 */
    private boolean jetFileUse = false;

    /**
	 * Flag to indicate if the class is generated with a generator
	 * @uml.property  name="generatorUse"
	 */
    private boolean generatorUse = false;

    /**
	 * Flag to indicate if a jetemplate is used
	 * @uml.property  name="jetTemplateUse"
	 */
    private boolean jetTemplateUse = false;

    /**
	 * Flag to indicate if abstract syntax tree generation is invoked
	 * @uml.property  name="astTemplateUse"
	 */
    private boolean astTemplateUse = false;

    /**
	 * Absolute path to the input file
	 * @uml.property  name="inputFilePath"
	 */
    private String inputFilePath = null;

    /**
	 * Path to the jet template
	 * @uml.property  name="jetTemplatePath"
	 */
    private String jetTemplatePath = null;

    /**
	 * Flag indicates if the <code>jetTemplatePath</code> is part of the plugin
	 * @uml.property  name="jetTemplateInPlugin"
	 */
    private boolean jetTemplateInPlugin = false;

    /**
	 * Path to the generator class
	 * @uml.property  name="generatorClassPath"
	 */
    private String generatorClassPath = null;

    /**
	 * Flag indicates if the <code>generatorClassPath</code> is part of the plugin
	 * @uml.property  name="generatorClassPathInPlugin"
	 */
    private boolean generatorClassPathInPlugin = false;

    /**
	 * Absolute path to the reference implementation
	 * @uml.property  name="referenceImplementationPath"
	 */
    private String referenceImplementationPath = null;

    /**
	 * @param _packageName Name of the package
	 * @param className Name of the class
	 */
    public GeneratedJavaClass(String _packageName, String className) {
        super(className);
        setPackageName(_packageName);
    }

    /**
	 * Constructor initializing the class name
	 * @param name
	 */
    public GeneratedJavaClass(String name) {
        super(name);
    }

    /**
	 * @return the interfaces
	 */
    @SuppressWarnings("unchecked")
    public List<Class> interfaces() {
        return interfaces;
    }

    /**
	 * Convenient method to retrieve an array of all interfaces
	 * @return Array with all interfaces
	 */
    @SuppressWarnings("unchecked")
    public Class[] getInterfaces() {
        Class[] list = new Class[interfaces.size()];
        int i = 0;
        for (Iterator iterator = interfaces.iterator(); iterator.hasNext(); ) {
            Class c = (Class) iterator.next();
            list[i++] = c;
        }
        return list;
    }

    /**
	 * @return the visibility
	 */
    public EVisibility getVisibility() {
        return visibility;
    }

    /**
	 * @param _visibility the visibility to set
	 */
    public void setVisibility(EVisibility _visibility) {
        this.visibility = _visibility;
    }

    /**
	 * @return the _final
	 */
    public boolean isFinal() {
        return _final;
    }

    /**
	 * @param __final the _final to set
	 */
    public void setFinal(boolean __final) {
        this._final = __final;
    }

    /**
	 * @return the _abstract
	 */
    public boolean isAbstract() {
        return _abstract;
    }

    /**
	 * @param __abstract the _abstract to set
	 */
    public void setAbstract(boolean __abstract) {
        this._abstract = __abstract;
    }

    /**
	 * Check if the visibility is public
	 * @return true if the visibility is public
	 */
    public boolean isPublic() {
        return visibility.equals(EVisibility.PUBLIC);
    }

    /**
	 * Check if the visibility is private
	 * @return true if the visibility is private
	 */
    public boolean isPrivate() {
        return visibility.equals(EVisibility.PRIVATE);
    }

    /**
	 * Check if the visibility is package default
	 * @return true if the visibility is package default
	 */
    public boolean isPackageDefault() {
        return visibility.equals(EVisibility.DEFAULT);
    }

    /**
	 * Check if the visibility is protected
	 * @return true if the visibility is protected
	 */
    public boolean isProtected() {
        return visibility.equals(EVisibility.PROTECTED);
    }

    /**
	 * @return  the jProject
	 * @uml.property  name="jProject"
	 */
    public IJavaProject getJProject() {
        return jProject;
    }

    /**
	 * @param project  the jProject to set
	 * @uml.property  name="jProject"
	 */
    public void setJProject(IJavaProject project) {
        jProject = project;
    }

    /**
	 * @return  the srcPath
	 * @uml.property  name="srcPath"
	 */
    public String getSrcPath() {
        return srcPath;
    }

    /**
	 * @param _srcPath  the srcPath to set
	 * @uml.property  name="srcPath"
	 */
    public void setSrcPath(String _srcPath) {
        this.srcPath = _srcPath;
    }

    /**
	 * Get the package name if it exists
	 * @return  the packageName name of the package or empty string
	 * @uml.property  name="packageName"
	 */
    public String getPackageName() {
        if (packageName != null) return packageName; else return "";
    }

    /**
	 * @param _packageName  the packageName to set
	 * @uml.property  name="packageName"
	 */
    public void setPackageName(String _packageName) {
        this.packageName = _packageName;
    }

    /**
	 * correspondence with RADIO Button jetemplate
	 * @return  the jetFileUse
	 * @uml.property  name="jetFileUse"
	 */
    public boolean isJetFileUse() {
        return jetFileUse;
    }

    /**
	 * correspondence with RADIO Button jetemplate
	 * @param _jetFileUse  the jetFileUse to set
	 * @uml.property  name="jetFileUse"
	 */
    public void setJetFileUse(boolean _jetFileUse) {
        this.jetFileUse = _jetFileUse;
    }

    /**
	 * correspondence with RADIO Button generator
	 * @return  the generatorUse
	 * @uml.property  name="generatorUse"
	 */
    public boolean isGeneratorUse() {
        return generatorUse;
    }

    /**
	 * correspondence with RADIO Button generator
	 * @param _generatorUse  the generatorUse to set
	 * @uml.property  name="generatorUse"
	 */
    public void setGeneratorUse(boolean _generatorUse) {
        this.generatorUse = _generatorUse;
    }

    /**
	 * correspondence with CHECKBOX jetemplate
	 * @return  the jetTemplateUse
	 * @uml.property  name="jetTemplateUse"
	 */
    public boolean isJetTemplateUse() {
        return jetTemplateUse;
    }

    /**
	 * correspondence with CHECKBOX jetemplate
	 * @param _jetTemplateUse  the jetTemplateUse to set
	 * @uml.property  name="jetTemplateUse"
	 */
    public void setJetTemplateUse(boolean _jetTemplateUse) {
        this.jetTemplateUse = _jetTemplateUse;
    }

    /**
	 * correspondence with CHECKBOX AST generation
	 * @return  the astTemplateUse
	 * @uml.property  name="astTemplateUse"
	 */
    public boolean isAstTemplateUse() {
        return astTemplateUse;
    }

    /**
	 * correspondence with CHECKBOX AST generation
	 * @param _astTemplateUse  the astTemplateUse to set
	 * @uml.property  name="astTemplateUse"
	 */
    public void setAstTemplateUse(boolean _astTemplateUse) {
        this.astTemplateUse = _astTemplateUse;
    }

    /**
	 * @return  the inputFilePath
	 * @uml.property  name="inputFilePath"
	 */
    public String getInputFilePath() {
        return inputFilePath;
    }

    /**
	 * @param _inputFilePath  the inputFilePath to set
	 * @uml.property  name="inputFilePath"
	 */
    public void setInputFilePath(String _inputFilePath) {
        this.inputFilePath = _inputFilePath;
    }

    /**
	 * @return  the jetTemplatePath
	 * @uml.property  name="jetTemplatePath"
	 */
    public String getJetTemplatePath() {
        return jetTemplatePath;
    }

    /**
	 * @param _jetTemplatePath  the jetTemplatePath to set
	 * @uml.property  name="jetTemplatePath"
	 */
    public void setJetTemplatePath(String _jetTemplatePath) {
        this.jetTemplatePath = _jetTemplatePath;
    }

    /**
	 * @return  the jetTemplateInPlugin
	 * @uml.property  name="jetTemplateInPlugin"
	 */
    public boolean isJetTemplateInPlugin() {
        return jetTemplateInPlugin;
    }

    /**
	 * @param _jetTemplateInPlugin  the jetTemplateInPlugin to set
	 * @uml.property  name="jetTemplateInPlugin"
	 */
    public void setJetTemplateInPlugin(boolean _jetTemplateInPlugin) {
        this.jetTemplateInPlugin = _jetTemplateInPlugin;
    }

    /**
	 * @return  the generatorClassPath
	 * @uml.property  name="generatorClassPath"
	 */
    public String getGeneratorClassPath() {
        return generatorClassPath;
    }

    /**
	 * @param _generatorClassPath  the generatorClassPath to set
	 * @uml.property  name="generatorClassPath"
	 */
    public void setGeneratorClassPath(String _generatorClassPath) {
        this.generatorClassPath = _generatorClassPath;
    }

    /**
	 * @return  the generatorClassPathInPlugin
	 * @uml.property  name="generatorClassPathInPlugin"
	 */
    public boolean isGeneratorClassPathInPlugin() {
        return generatorClassPathInPlugin;
    }

    /**
	 * @param _generatorClassPathInPlugin  the generatorClassPathInPlugin to set
	 * @uml.property  name="generatorClassPathInPlugin"
	 */
    public void setGeneratorClassPathInPlugin(boolean _generatorClassPathInPlugin) {
        this.generatorClassPathInPlugin = _generatorClassPathInPlugin;
    }

    /**
	 * @return  the referenceImplementationPath
	 * @uml.property  name="referenceImplementationPath"
	 */
    public String getReferenceImplementationPath() {
        return referenceImplementationPath;
    }

    /**
	 * @param _referenceImplementationPath  the referenceImplementationPath to set
	 * @uml.property  name="referenceImplementationPath"
	 */
    public void setReferenceImplementationPath(String _referenceImplementationPath) {
        this.referenceImplementationPath = _referenceImplementationPath;
    }

    /**
	 * Check if the class is of a simple type, ie a primitive type or a type from
	 * package 'java.lang.*'
	 * @param c Class to be checked
	 * @return true if the class is a simple type
	 */
    @SuppressWarnings("unchecked")
    public boolean isSympleType(Class c) {
        if (c.equals(int.class) || c.equals(boolean.class) || c.equals(double.class) || c.equals(float.class) || c.equals(byte.class) || c.equals(long.class)) {
            return true;
        }
        if (c.getName().startsWith("java.lang.")) return true; else return false;
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((interfaces == null) ? 0 : interfaces.hashCode());
        result = prime * result + ((packageName == null) ? 0 : packageName.hashCode());
        return result;
    }

    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        IGeneratedJavaClass2 gen = (IGeneratedJavaClass2) obj;
        if (gen.getPackageName().equals(getPackageName())) {
            Class<?>[] _interfaces = gen.getInterfaces();
            if (_interfaces.length != getInterfaces().length) {
                return false;
            }
            for (int i = 0; i < _interfaces.length; i++) {
                if (!interfaces().contains(_interfaces[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
	 * Set the superclass as the first element in the superclasses list
	 * @see ch.sahits.model.GeneratedClass#addSuperClass(java.lang.Class)
	 */
    @SuppressWarnings("unchecked")
    @Override
    public void addSuperClass(Class superclass) {
        superclasses().add(0, superclass);
    }
}
