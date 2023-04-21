package fr.univartois.cril.xtext2.alloyplugin.core;

import java.io.Serializable;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import edu.mit.csail.sdg.alloy4.ErrorWarning;
import edu.mit.csail.sdg.alloy4compiler.ast.Command;
import fr.univartois.cril.xtext2.alloyplugin.api.IALSCommand;
import fr.univartois.cril.xtext2.alloyplugin.api.IReporter;
import fr.univartois.cril.xtext2.alloyplugin.api.Util;
import fr.univartois.cril.xtext2.alloyplugin.console.AlloyMessageConsole;
import fr.univartois.cril.xtext2.alloyplugin.console.Console;
import fr.univartois.cril.xtext2.ui.activator.AlsActivator;

/**
 * Allow to display within the Eclipse framework events reported by Alloy4 compiler.
 * It implements the Map interface in order to get notified of included AlloySpecification file
 * through the method put(String key,String value) where key is the absolute filename of the included file.
 * 
 * @author leberre
 *
 */
public final class Reporter extends IReporter implements Serializable {

    /**	 * 	 */
    private static final long serialVersionUID = 1L;

    private int warningCount = 0;

    private String filename;

    private transient IResource resource;

    private transient IALSCommand execCommand;

    public int getWarningCount() {
        return warningCount;
    }

    public void setWarningCount(int warningCount) {
        this.warningCount = warningCount;
    }

    public long getStartTranslation() {
        return startTranslation;
    }

    public void setStartTranslation(long startTranslation) {
        this.startTranslation = startTranslation;
    }

    public long getStartSolving() {
        return startSolving;
    }

    public void setStartSolving(long startSolving) {
        this.startSolving = startSolving;
    }

    public String getFilename() {
        return filename;
    }

    public IResource getResource() {
        return resource;
    }

    public IALSCommand getExecCommand() {
        return execCommand;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setResource(IResource resource) {
        this.resource = resource;
    }

    private long startTranslation;

    private long startSolving;

    /**
	 * Create a reporter which is associated with one resource. 
	 */
    public Reporter(IResource res) {
        this.filename = res.getLocation().toOSString();
        this.resource = res;
    }

    @Override
    public void warning(final ErrorWarning e) {
        warningCount++;
        printInfo("Warning #" + warningCount);
        printInfo(e.msg.trim());
        AlloyLaunching.displayWarningInProblemView(resource, e);
    }

    @Override
    public void translate(String solver, int bitwidth, int maxseq, int skolemDepth, int symmetry) {
        printCommand("Launched Command : " + execCommand.toString() + "\n");
        print("Solver=" + solver + " Bitwidth=" + bitwidth + " MaxSeq=" + maxseq + (skolemDepth == 0 ? "" : " SkolemDepth=" + skolemDepth) + " Symmetry=" + (symmetry > 0 ? ("" + symmetry) : "OFF") + "\nGenerating CNF...\n");
        startTranslation = System.currentTimeMillis();
    }

    @Override
    public void solve(int primaryVars, int totalVars, int clauses) {
        startSolving = System.currentTimeMillis();
        print("CNF translation time:" + (startSolving - startTranslation) + " ms");
        print("Var=" + totalVars + " PrimaryVars=" + primaryVars + " Clauses=" + clauses + "\nSolving...\n");
    }

    @Override
    public void resultCNF(final String filename) {
        printInfo("CNF file written to " + filename + "\n");
    }

    @Override
    public void resultSAT(Object command, long solvingTime, Object solution) {
        Command cmd = (Command) command;
        resultSAT(cmd.expects, cmd.check, solvingTime);
    }

    public void resultSAT(int expects, boolean check, long solvingTime) {
        StringBuilder sb = new StringBuilder("");
        if (check) {
            sb.append("Counterexample found. \nAssertion is invalid");
        } else {
            sb.append("Instance found. \nPredicate is consistent");
        }
        if (expects == 0) sb.append(", contrary to expectation"); else if (expects == 1) sb.append(", as expected.");
        sb.append(" " + (System.currentTimeMillis() - startSolving) + " ms.");
        print(sb.toString());
        updateExecCommand(true, sb.toString());
    }

    @Override
    public void resultUNSAT(Object command, long solvingTime, Object solution) {
        Command cmd = (Command) command;
        resultUNSAT(cmd.expects, cmd.check, solvingTime);
    }

    public void resultUNSAT(int expects, boolean check, long solvingTime) {
        StringBuilder sb = new StringBuilder("");
        if (check) {
            sb.append("No counterexample found.\nAssertion may be valid");
        } else {
            sb.append("No instance found.\nPredicate may be inconsistent");
        }
        if (expects == 1) sb.append(", contrary to expectation"); else if (expects == 0) sb.append(", as expected.");
        sb.append(" " + (System.currentTimeMillis() - startSolving) + " ms.");
        print(sb.toString());
        updateExecCommand(false, sb.toString());
    }

    /**
	 * update associated command if there is one.  
	 */
    private void updateExecCommand(boolean sat, String resultMessage) {
        if (execCommand != null) {
            execCommand.setSat(sat);
            execCommand.setStringResult(resultMessage);
        }
    }

    private void printInfo(String string) {
        AlloyMessageConsole console = Console.findAlloyInfoConsole("");
        console.print(string);
    }

    private void print(String string) {
        AlloyMessageConsole console = Console.findAlloyConsole(filename);
        console.print(string);
    }

    /** This method is called by the parser to report parser events. */
    public void parse(String msg) {
        printInfo(msg);
    }

    /** This method is called by the typechecker to report the type for each field/function/predicate/assertion, etc. */
    public void typecheck(String msg) {
        printInfo(msg);
    }

    /** This method is called by the ScopeComputer to report the scope chosen for each sig. */
    public void scope(String msg) {
    }

    /** This method is called by the BoundsComputer to report the bounds chosen for each sig and each field. */
    public void bound(String msg) {
    }

    /**
	 * Set the ExecutableCommand to the reporter.
	 * */
    public void setExecCommand(IALSCommand cmd) {
        this.execCommand = cmd;
    }

    /**
     * Method called when a new file 
     */
    @Override
    public String put(String key, String value) {
        String result = super.put(key, value);
        IFile res = Util.getFileForLocation(key);
        if (res != null && res.exists()) try {
            res.deleteMarkers(Util.ALLOYPROBLEM, false, 0);
        } catch (CoreException e) {
            AlsActivator.getDefault().log(e);
        }
        return result;
    }

    /**	 * Method called when a command is launched to write the full command	 * 	 */
    private void printCommand(String string) {
        AlloyMessageConsole console = Console.findAlloyConsole(filename);
        console.printCommand(string);
    }
}
