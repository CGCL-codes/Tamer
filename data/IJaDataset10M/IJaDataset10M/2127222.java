package proper.app;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Vector;
import proper.core.ArgumentObject;
import proper.core.CommandLineInterface;
import proper.engine.Engine;
import proper.io.CommandLine;
import proper.io.CommandLineParameter;
import proper.util.ProperVector;
import proper.util.Stopwatch;
import proper.util.Strings;
import proper.xml.Attribute;
import proper.xml.Element;
import proper.xml.XMLAccessInterface;

/**
* This class functions as a Ancestor for all classes that are called from
* commandline with parameters.
* <br>
* For a complete list of commandline parameters just run it with the option
* "-help".
*
*
* @author      FracPete
* @version $Revision: 1.3 $
*/
public abstract class Application extends ArgumentObject implements CommandLineInterface, XMLAccessInterface {

    /** for processing the commandline parameters */
    protected CommandLine cl;

    /** for stopping the runtime */
    protected Stopwatch watch;

    /** for the available command line parameters */
    protected int indention;

    protected Vector parameters;

    protected Vector notes;

    /**
   * initializes the object
   */
    public Application() {
        super();
        cl = new CommandLine();
        watch = new Stopwatch();
        parameters = new ProperVector();
        notes = new ProperVector();
        indention = 1;
        defaultParameters();
        defineParameters();
        setDefinitions(cl);
    }

    /**
   * creates a new instance from the given classname
   */
    public static Application createInstance(String classname) {
        Class cls;
        Application result;
        try {
            cls = Class.forName(classname);
            result = (Application) cls.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    /**
   * adds the definition of a parameter to the list of supported parameters
   * @param name              the name of the parameter
   * @param description       the description of the parameter
   * @param hasArgument       whether the parameter needs an argument
   * @param argDesc           the description of the argument, "&lt;arg&gt;"
   *                          if NULL or ""
   * @param optional          whether the parameter it optional
   */
    protected void addDefinition(String name, String description, boolean hasArgument, String argDesc, boolean optional) {
        CommandLineParameter p;
        p = new CommandLineParameter(name, description, hasArgument, argDesc, optional);
        p.setIndention(indention);
        parameters.add(p);
    }

    /**
   * adds the given text to the notes (notes are printed after the parameters)
   */
    protected void addNote(String note) {
        notes.add(note);
    }

    /**
   * here the initialization of member variables takes place, that are
   * displayed in the parameters
   */
    protected void defaultParameters() {
    }

    /**
   * here all the available command line parameters are defined
   */
    protected void defineParameters() {
        addDefinition("verbose", "sets the verbose flag of the specified class(es), 'all' matches them all", true, "class[,class[,..]]", true);
        addDefinition("log", "redirects the output into the specified file", true, "<filename>", true);
        addDefinition("append_log", "with this the file is not deleted at the beginning", true, "yes | no", true);
        addDefinition("help", "to print this help", false, "", true);
    }

    /**
   * transfers the parameter definitions from the application to the 
   * CommandLine object
   */
    protected void setDefinitions(CommandLine cl) {
        int i;
        cl.clearParameterDefinitions();
        for (i = 0; i < parameters.size(); i++) cl.addParameterDefinition((CommandLineParameter) parameters.get(i));
    }

    /**
   * checks whether all necessary parameters are provided
   */
    public void checkCommandLine() {
        if (cl.exists("help")) {
            printUsage();
            System.exit(0);
        }
        if (!cl.isComplete()) parameterError();
    }

    /**
   * prints the usage if not all necessary parameters were provided
   */
    public void parameterError() {
        int i;
        printUsage();
        if (cl.getMissing().size() > 0) {
            System.out.println("The following parameter(s) are missing:");
            for (i = 0; i < cl.getMissing().size(); i++) System.out.println(" -" + cl.getMissing().get(i));
            System.out.println();
        }
        if (cl.getErroneous().size() > 0) {
            System.out.println("The following parameter(s) are incorrect:");
            for (i = 0; i < cl.getErroneous().size(); i++) System.out.println(" -" + cl.getErroneous().get(i));
            System.out.println();
        }
        System.exit(1);
    }

    /**
   * prints a short description of this class
   */
    public void printDescription() {
    }

    /**
   * prints the usage of the application, with all necessary parameters
   */
    public void printUsage() {
        int i;
        int n;
        Vector lines;
        System.out.println("\n" + this.getClass().getName() + "\n" + this.getClass().getName().replaceAll(".", "=") + "\n");
        printDescription();
        System.out.println("Parameters:\n");
        for (i = 0; i < parameters.size(); i++) System.out.println(parameters.get(i) + "\n");
        if (notes.size() > 0) {
            System.out.println("Notes:\n");
            for (i = 0; i < notes.size(); i++) {
                lines = Strings.breakUpVector(notes.get(i).toString(), "\n");
                for (n = 0; n < lines.size(); n++) System.out.println(Strings.stringOf(" ", indention) + lines.get(n).toString());
                System.out.println();
            }
        }
    }

    /**
   * initializes the engine
   */
    protected void initEngine(Engine engine) {
        addListener(engine);
        if (!engine.getVerbose()) engine.setVerbose(getVerbose());
    }

    /**
   * performs the initialization
   */
    protected boolean initialize(String[] args) throws Exception {
        File file;
        setArgs(args);
        if (cl.exists("verbose")) setVerboseString(cl.getValue("verbose"));
        if (cl.exists("log") && !cl.getValue("log").equals("")) {
            System.out.println("Output is redirected to '" + cl.getValue("log") + "'...");
            addOutput(new PrintStream(new BufferedOutputStream(new FileOutputStream(cl.getValue("log"), cl.getValue("append_log").equals("yes")))));
        }
        return true;
    }

    /**
   * here the actuale processing takes place - must be filled in the derived
   * classes
   */
    protected boolean process() throws Exception {
        return true;
    }

    /**
   * sets the arguments to use with the <code>run()</code> method
   * @see                  #run()
   * @param args           the arguments to use
   */
    public void setArgs(String[] args) {
        if (args == null) cl.setArguments(new String[0]); else cl.setArguments(args);
        checkCommandLine();
    }

    /**
   * sets the arguments from the given CommandLine object
   * @param cl             the commandline object containing the parameters
    */
    public void setArgs(CommandLine cl) {
        setArgs(cl.toArray());
    }

    /**
   * runs the application with previously set arguments
   * @see                  #setArgs(String[])
   */
    public boolean run() throws Exception {
        return run(cl.toArray());
    }

    /**
   * processes the commandline parameters and runs the application
   * @return         returns TRUE if successful run
   */
    public boolean run(String[] args) throws Exception {
        boolean result;
        result = initialize(args);
        if (result) {
            println(this);
            watch.start();
            result = process();
            watch.stop();
            println("\nRuntime: " + watch.getSeconds() + "s");
        }
        return result;
    }

    /**
   * outputs the current parameters
   */
    public String toString() {
        String result;
        String param;
        Vector params;
        int i;
        result = this.getClass().getName() + ":";
        params = cl.getParameters();
        for (i = 0; i < params.size(); i++) {
            param = (String) params.get(i);
            result += " -" + param;
            if (!cl.getValue(param).equals("")) result += " \"" + cl.getValue(param) + "\"";
        }
        return result;
    }

    /**
   * returns the content in an XML structure, where the actual data is 
   * stored in the children of the returned Element, i.e. the returned
   * node acts only as a container. The contents of the container are the
   * command line parameters currently used and the outer tag is the Java-tag
   * used in ANT.
   * @see                  proper.io.CommandLine#toXML()
   * @return               the Element containing the data in its children
   */
    public Element toXML() {
        Element result;
        Element node;
        int i;
        result = new Element("java");
        result.addAttribute(new Attribute("classname", this.getClass().getName()));
        result.addAttribute(new Attribute("fork", "yes"));
        node = cl.toXML();
        for (i = 0; i < node.getChildCount(); i++) result.add((Element) node.getChildAt(i));
        return result;
    }

    /**
   * reads all the data stored in the children of the given node (= container).
   * The content of the container must be nodes like used in the 
   * <code>CommandLine.fromXML()</code> method
   * @see                  proper.io.CommandLine#fromXML(Element)
   * @param node           the Element node containing the data in its children
   */
    public void fromXML(Element node) {
        cl.fromXML(node);
    }
}
