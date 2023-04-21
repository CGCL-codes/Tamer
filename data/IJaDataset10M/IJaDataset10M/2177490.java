package gate.jape;

import gate.AnnotationSet;
import gate.Document;
import gate.Gate;
import gate.creole.ontology.Ontology;
import gate.util.Err;
import gate.util.GateRuntimeException;
import gate.util.Strings;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
  * The RHS of a CPSL rule. The action part. Contains an inner class
  * created from the code in the grammar RHS.
  */
public class RightHandSide implements JapeConstants, java.io.Serializable {

    private static final long serialVersionUID = -4359589687308736378L;

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

    private transient SourceInfo sourceInfo;

    /** Cardinality of the action class set. Used for ensuring class name
    * uniqueness.
    */
    private static AtomicInteger actionClassNumber = new AtomicInteger();

    /** The set of block names.
    * Used to ensure we only get their annotations once in the action class.
    */
    private Set<String> blockNames;

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

    private static Set<StackTraceElement> warnings = new HashSet<StackTraceElement>();

    /** Construction from the transducer name, rule name and the LHS. */
    public RightHandSide(String transducerName, String ruleName, LeftHandSide lhs, String importblock) {
        this.lhs = lhs;
        this.phaseName = transducerName;
        this.ruleName = ruleName;
        actionClassName = new String(transducerName + ruleName + "ActionClass" + actionClassNumber.getAndIncrement());
        blockNames = new HashSet<String>();
        actionClassString = new StringBuffer("// " + actionClassName + nl + "package " + actionsDirName + "; " + nl + importblock + nl + "public class " + actionClassName + nl + "implements java.io.Serializable, RhsAction { " + nl + "  private ActionContext ctx;" + nl + "  public String ruleName() { return \"" + ruleName + "\"; }" + nl + "  public String phaseName() { return \"" + phaseName + "\"; }" + nl + "  public void setActionContext(ActionContext ac) { ctx = ac; }" + nl + "  public ActionContext getActionContext() { return ctx; }" + nl + "  public void doit(gate.Document doc, " + nl + "                   java.util.Map<java.lang.String, gate.AnnotationSet> bindings, " + nl + "                   gate.AnnotationSet annotations, " + nl + "                   gate.AnnotationSet inputAS, gate.AnnotationSet outputAS, " + nl + "                   gate.creole.ontology.Ontology ontology) throws gate.jape.JapeException {" + nl);
        actionClassJavaFileName = actionsDirName + File.separator + actionClassName.replace('.', File.separatorChar) + ".java";
        actionClassQualifiedName = actionsDirName.replace(File.separatorChar, '.').replace('/', '.').replace('\\', '.') + "." + actionClassName;
        actionClassClassFileName = actionClassQualifiedName.replace('.', File.separatorChar) + ".class";
        sourceInfo = new SourceInfo(actionClassQualifiedName, phaseName, ruleName);
    }

    /** Add an anonymous block to the action class */
    public void addBlock(String anonymousBlock) {
        actionClassString.append(nl);
        actionClassString.append("if (true) {");
        actionClassString.append(nl);
        actionClassString.append(sourceInfo.addBlock(actionClassString.toString(), anonymousBlock));
        actionClassString.append(nl);
        actionClassString.append("}");
        actionClassString.append(nl);
    }

    /** Add a named block to the action class */
    public void addBlock(String name, String namedBlock) {
        if (name == null) {
            addBlock(namedBlock);
            return;
        }
        if (blockNames.add(name)) actionClassString.append("    gate.AnnotationSet " + name + "Annots = bindings.get(\"" + name + "\"); " + nl);
        actionClassString.append("    if(" + name + "Annots != null && " + name + "Annots.size() != 0) { " + nl);
        actionClassString.append(sourceInfo.addBlock(actionClassString.toString(), namedBlock));
        actionClassString.append(nl + "    }" + nl);
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

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        try {
            Class class1 = Gate.getClassLoader().loadClass(actionClassQualifiedName);
            out.writeObject(class1);
        } catch (ClassNotFoundException cnfe) {
            throw new GateRuntimeException(cnfe);
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        String className = getActionClassName();
        if (Gate.getClassLoader().findExistingClass(className) == null) {
            try {
                Map<String, String> actionClasses = new HashMap<String, String>();
                actionClasses.put(className, getActionClassString());
                gate.util.Javac.loadClasses(actionClasses, Gate.getClassLoader().getDisposableClassLoader(in.toString()));
            } catch (Exception e1) {
                throw new GateRuntimeException(e1);
            }
        }
    }

    /** Makes changes to the document, using LHS bindings. */
    public void transduce(Document doc, java.util.Map<String, AnnotationSet> bindings, AnnotationSet inputAS, final AnnotationSet outputAS, Ontology ontology, ActionContext actionContext) throws JapeException {
        if (theActionObject == null) {
            instantiateActionClass();
        }
        AnnotationSet annotations = (AnnotationSet) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { AnnotationSet.class }, new InvocationHandler() {

            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                StackTraceElement japeSTE = null;
                int lineNumber = -1;
                for (StackTraceElement ste : (new Throwable()).getStackTrace()) {
                    if (ste.getClassName().equals(actionClassQualifiedName)) {
                        if (ste.getLineNumber() >= 0 && sourceInfo != null) {
                            japeSTE = sourceInfo.getStackTraceElement(ste.getLineNumber());
                            lineNumber = ste.getLineNumber();
                        } else {
                            japeSTE = new StackTraceElement(getPhaseName(), getRuleName(), null, -1);
                        }
                        break;
                    }
                }
                if (!warnings.contains(japeSTE)) {
                    Err.println(nl + "WARNING: the JAPE 'annotations' parameter has been deprecated. Please use 'inputAS' or 'outputAS' instead.");
                    Err.println(japeSTE);
                    if (lineNumber >= 0) Err.println("\t" + sourceInfo.getSource(getActionClassString(), lineNumber).trim());
                    warnings.add(japeSTE);
                }
                return method.invoke(outputAS, args);
            }
        });
        try {
            ((RhsAction) theActionObject).setActionContext(actionContext);
            ((RhsAction) theActionObject).doit(doc, bindings, annotations, inputAS, outputAS, ontology);
        } catch (NonFatalJapeException e) {
            Throwable t = e.getCause();
            Err.println("A non-fatal JAPE exception occurred while processing document '" + doc.getName() + "'.");
            Err.println("The issue occurred during execution of rule '" + getRuleName() + "' in phase '" + getPhaseName() + "':");
            if (t != null) {
                sourceInfo.enhanceTheThrowable(t);
                t.printStackTrace(Err.getPrintWriter());
            } else {
                Err.println("Line number and exception details are not available!");
            }
        } catch (Throwable e) {
            if (sourceInfo != null) sourceInfo.enhanceTheThrowable(e);
            if (e instanceof Error) {
                throw (Error) e;
            }
            if (e instanceof JapeException) {
                throw (JapeException) e;
            }
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new JapeException("Couldn't run RHS action", e);
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
