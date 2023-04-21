package ca.umontreal.iro.rali.gate.jape;

import java.util.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import gate.annotation.*;
import gate.util.*;
import gate.creole.ontology.Ontology;
import gate.*;

/**
  * The RHS of a CPSL rule. The action part. Contains an inner class
  * created from the code in the grammar RHS.
  */
public class RightHandSide implements JapeConstants, java.io.Serializable {

    /** Debug flag */
    private static final boolean DEBUG = false;

    /** An instance of theActionClass. */
    private transient Object theActionObject;

    /** The string we use to create the action class. */
    private StringBuffer actionClassString;

    /** The bytes of the compiled action class. */
    private byte[] actionClassBytes;

    /** The name of the action class. */
    private String actionClassName;

    /** Package name for action classes. It's called a "dir name" because
    * we used to dump the action classes to disk and compile them there.
    */
    private static String actionsDirName = "japeactionclasses";

    /** The qualified name of the action class. */
    private String actionClassQualifiedName;

    /** Name of the .java file for the action class. */
    private String actionClassJavaFileName;

    /** Name of the .class file for the action class. */
    private String actionClassClassFileName;

    /** Cardinality of the action class set. Used for ensuring class name
    * uniqueness.
    */
    private static int actionClassNumber = 0;

    /** Allow setting of the initial action class number. Used for ensuring
    * class name uniqueness when running more than one transducer. The
    * long-term solution is to have separate class loaders for each
    * transducer.
    */
    public static void setActionClassNumber(int n) {
        actionClassNumber = n;
    }

    /** The set of block names.
    * Used to ensure we only get their annotations once in the action class.
    */
    private HashSet blockNames;

    /** Returns the string for the java code */
    public String getActionClassString() {
        return actionClassString.toString();
    }

    public String getActionClassName() {
        return actionClassQualifiedName;
    }

    /** The LHS of our rule, where we get bindings from. */
    private LeftHandSide lhs;

    /** A list of the files and directories we create. */
    private static ArrayList tempFiles = new ArrayList();

    /** Local fashion for newlines. */
    private final String nl = Strings.getNl();

    /** Debug flag. */
    static final boolean debug = false;

    private String phaseName;

    private String ruleName;

    /** Construction from the transducer name, rule name and the LHS. */
    public RightHandSide(String transducerName, String ruleName, LeftHandSide lhs) {
        this.lhs = lhs;
        this.phaseName = transducerName;
        this.ruleName = ruleName;
        actionClassName = new String(transducerName + ruleName + "ActionClass" + actionClassNumber++);
        blockNames = new HashSet();
        actionClassString = new StringBuffer("// " + actionClassName + nl + "package " + actionsDirName + "; " + nl + "import java.io.*;" + nl + "import java.util.*;" + nl + "import gate.*;" + nl + "import ca.umontreal.iro.rali.gate.jape.*;" + nl + "import gate.creole.ontology.Ontology;" + nl + "import gate.annotation.*;" + nl + "import gate.util.*;" + nl + nl + "public class " + actionClassName + nl + "implements java.io.Serializable, RhsAction { " + nl + "  public void doit(Document doc, java.util.Map bindings, " + nl + "                   AnnotationSet annotations, " + nl + "                   AnnotationSet inputAS, AnnotationSet outputAS, " + nl + "                   Ontology ontology) {" + nl);
        actionClassJavaFileName = actionsDirName + File.separator + actionClassName.replace('.', File.separatorChar) + ".java";
        actionClassQualifiedName = actionsDirName.replace(File.separatorChar, '.').replace('/', '.').replace('\\', '.') + "." + actionClassName;
        actionClassClassFileName = actionClassQualifiedName.replace('.', File.separatorChar) + ".class";
    }

    /** Add an anonymous block to the action class */
    public void addBlock(String anonymousBlock) {
        actionClassString.append(anonymousBlock);
        actionClassString.append(nl);
    }

    /** Add a named block to the action class */
    public void addBlock(String name, String namedBlock) {
        if (name == null) {
            addBlock(namedBlock);
            return;
        }
        if (blockNames.add(name)) actionClassString.append("    AnnotationSet " + name + "Annots = (AnnotationSet)bindings.get(\"" + name + "\"); " + nl);
        actionClassString.append("    if(" + name + "Annots != null && " + name + "Annots.size() != 0) { " + nl + "      " + namedBlock + nl + "    }" + nl);
    }

    /** Create the action class and an instance of it. */
    public void createActionClass() throws JapeException {
        actionClassString.append("  }" + nl + "}" + nl);
    }

    /** Create an instance of the action class. */
    public void instantiateActionClass() throws JapeException {
        try {
            theActionObject = Gate.getClassLoader().loadClass(actionClassQualifiedName).newInstance();
        } catch (Exception e) {
            throw new JapeException("couldn't create instance of action class " + actionClassName + ": " + e.getMessage());
        }
    }

    /** Remove class files created for actions. */
    public static void cleanUp() {
        if (tempFiles.size() == 0) return;
        for (ListIterator i = tempFiles.listIterator(tempFiles.size() - 1); i.hasPrevious(); ) {
            File tempFile = (File) i.previous();
            tempFile.delete();
        }
        tempFiles.clear();
    }

    /** Makes changes to the document, using LHS bindings. */
    public void transduce(Document doc, java.util.Map bindings, AnnotationSet inputAS, AnnotationSet outputAS, Ontology ontology) throws JapeException {
        if (theActionObject == null) {
            instantiateActionClass();
        }
        try {
            ((RhsAction) theActionObject).doit(doc, bindings, outputAS, inputAS, outputAS, ontology);
        } catch (Exception e) {
            StringWriter stackTraceWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTraceWriter));
            throw new JapeException("Couldn't run RHS action: " + Strings.getNl() + stackTraceWriter.getBuffer().toString() + Strings.addLineNumbers(actionClassString.toString()));
        }
    }

    /** Create a string representation of the object. */
    public String toString() {
        return toString("");
    }

    /** Create a string representation of the object. */
    public String toString(String pad) {
        String nl = Strings.getNl();
        StringBuffer buf = new StringBuffer(pad + "RHS: actionClassName(" + actionClassName + "); ");
        buf.append("actionClassClassFileName(" + nl + actionClassClassFileName + nl);
        buf.append("actionClassJavaFileName(" + nl + actionClassJavaFileName + nl);
        buf.append("actionClassQualifiedName(" + nl + actionClassQualifiedName + nl);
        buf.append("blockNames(" + blockNames.toString() + "); ");
        buf.append(nl + pad + ") RHS." + nl);
        return buf.toString();
    }

    /** Create a string representation of the object. */
    public String shortDesc() {
        String res = "" + actionClassName;
        return res;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleName() {
        return ruleName;
    }
}
