package org.jmlspecs.eclipse.jmlchecker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IProblemRequestor;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblem;
import org.eclipse.jdt.internal.compiler.problem.ProblemSeverities;
import org.jmlspecs.eclipse.util.Env;
import org.jmlspecs.eclipse.util.Log;

/**
 * This is a class that implements the eclipse IProblem interface
 * tailored for the JmlParser.  It is mostly present to provide
 * an interface between the plugin and the internal class that it
 * extends, but it also provides some utility functions to produce 
 * and report errors and warnings.  It also adds some additional
 * information to its parent class to hold information relevant to
 * line and print oriented problem reporting.
 */
public class CompilerProblem extends DefaultProblem {

    /** This holds a reference to the source text as known
   *  at the time the problem was created, or null if
   *  the source text is not available.
   */
    public String sourceText;

    /**
   * Holds the character position of the beginning of the line indicated
   * by the line argument (and on which startPosition resides); the
   * value is -1 if the lineStart information is not available.
   */
    public int lineStart;

    /**
   * Holds the character position of the end of the line indicated
   * by the line argument (and on which startPosition resides); this is
   * the character position of the line termination or end of file;
   * the value is -1 if the line end information is not available.
   */
    public int lineEnd;

    /**
   * Constructor for a new problem.
   * @param ifile    The workspace IFile in which the offending source text is located, if any (otherwise null)
   * @param message  The problem message
   * @param id       A numerical id for the problem
   * @param severity The severity of the problem, using one of the constants listed above (e.g. Error, Warning)
   * @param startPosition The character position within the line that begins the offending region
   * @param endPosition   The character position within the line that ends (not one after) the offending region
   * @param line     The line within the source text containing the offending source text
   * @param source   A reference to the source text, or null
   * @param lineStart The character position of the beginning of the stated line in the IFile or source text, or -1 if not known
   * @param lineEnd   The character position of the end of the stated line (that is, where the line termination character is) in the IFile or source text, or -1 if not known
   */
    public CompilerProblem(IFile ifile, String message, int id, int severity, int startPosition, int endPosition, int line, String source, int lineStart, int lineEnd) {
        super(ifile == null ? null : ifile.getFullPath().toString().toCharArray(), message, id, null, severity, startPosition, endPosition, line);
        sourceText = source;
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
    }

    /**
   * A utility method to obtain a reference to the source text for
   * an ICompilationUnit, or null if it could not be obtained.
   * @param icu The ICompilationUnit
   * @return The source text for the ICompilationUnit
   */
    private static String getSource(ICompilationUnit icu) {
        if (icu == null) return null;
        try {
            return icu.getSource();
        } catch (Exception e) {
            return null;
        }
    }

    public CompilerProblem(IProblem p, ICompilationUnit icu) {
        this((IFile) icu.getResource(), p.getMessage(), p.getID(), p.isError() ? ProblemSeverities.Error : p.isWarning() ? ProblemSeverities.Warning : -1, p.getSourceStart(), p.getSourceEnd(), p.getSourceLineNumber(), getSource(icu), -1, -1);
    }

    /**
   * Constructs and reports a CompilerProblem 
   * @param req  The problem requestor to which to report the error
   * @param scanner The instance of the Java scanner that is scanning the source text
   * @param message  The error message to produce
   * @param id       A numerical id for the problem
   * @param severity One of the severity constants above, e.g. Error
   * @param startPosition The character position within the file of source text to mark
   * @param endPosition The character position within the file of source text of the end of the region to mark (not one beyond it)
   */
    private static void report(IProblemRequestor req, JavaScanner scanner, String message, int id, int severity, int startPosition, int endPosition) {
        if (severity == Problems.IGNORE) return;
        int line = scanner.getLineNumber(startPosition);
        int lineStart = scanner.getLineStart(line);
        if (line == 1 && lineStart < 0) lineStart = 0;
        int lineEnd = scanner.getLineEnd(line);
        if (lineEnd < lineStart) lineEnd = scanner.eofPosition;
        req.acceptProblem(new CompilerProblem(scanner.ifile, message, id, severity, startPosition, endPosition, line, scanner.getSourceString(), lineStart, lineEnd));
    }

    /**
   * Constructs and reports an IProblem 
   * @param req  The problem requestor to which to report the error
   * @param cu The CompilationUnit from which to get line ending information
   * @param message  The error message to produce
   * @param id       A numerical id for the problem
   * @param severity One of the severity constants above, e.g. Error, Warning
   * @param startPosition The character position within the file of source text to mark
   * @param endPosition The character position within the file of source text of the end of the region to mark (not one beyond it)
   */
    private static void report(IProblemRequestor req, org.eclipse.jdt.core.dom.CompilationUnit cu, String message, int id, int severity, int startPosition, int endPosition) {
        if (severity == Problems.IGNORE) return;
        if (cu != null) {
            int line = cu.getLineNumber(startPosition);
            if (line < 1) line = 1;
            ICompilationUnit icu = (ICompilationUnit) cu.getJavaElement();
            IFile r = (IFile) icu.getResource();
            req.acceptProblem(new CompilerProblem(r, message, id, severity, startPosition, endPosition, line, null, -1, -1));
        } else {
            int line = -1;
            req.acceptProblem(new CompilerProblem(null, message, id, severity, startPosition, endPosition, line, null, -1, -1));
        }
    }

    /**
   * Constructs and reports an IProblem 
   * @param req  The problem requestor to which to report the error
   * @param cu The CompilationUnit from which to get line ending information
   * @param startPosition The character position within the file of source text to mark
   * @param endPosition The character position within the file of source text of the end of the region to mark (not one beyond it)
   * @param problem  The instance of IProblems giving the id/text of this error
   * @param args The arguments to substitute in the problem text
   */
    public static void report(IProblemRequestor req, org.eclipse.jdt.core.dom.CompilationUnit cu, int startPosition, int endPosition, IProblems problem, Object... args) {
        report(req, cu, problem.toString(args), problem.id(), problem.severity(), startPosition, endPosition);
    }

    /**
   * Constructs and reports an IProblem 
   * @param req  The problem requestor to which to report the error
   * @param cu The CompilationUnit from which to get line ending information
   * @param message  The error message to produce
   * @param id       A numerical id for the problem
   * @param severity One of the severity constants above, e.g. Error, Warning
   * @param startPosition The character position within the file of source text to mark
   * @param endPosition The character position within the file of source text of the end of the region to mark (not one beyond it)
   */
    private static void report(IProblemRequestor req, org.jmlspecs.eclipse.jmldom.CompilationUnit cu, String message, int id, int severity, int startPosition, int endPosition) {
        if (severity == Problems.IGNORE) return;
        org.eclipse.jdt.core.dom.CompilationUnit ecu = (org.eclipse.jdt.core.dom.CompilationUnit) cu.oldNode;
        int line = cu.getLineNumber(startPosition);
        if (line < 1) line = 1;
        int lineStart = cu.getLineStart(line);
        if (line == 1 && lineStart < 0) lineStart = 0;
        if (ecu != null) {
            req.acceptProblem(new CompilerProblem((IFile) ((ICompilationUnit) ecu.getJavaElement()).getResource(), message, id, severity, startPosition, endPosition, line, cu.getOriginalSource(), lineStart, cu.getLineEnd(line)));
        } else {
            req.acceptProblem(new CompilerProblem(null, message, id, severity, startPosition, endPosition, line, cu.getOriginalSource(), lineStart, cu.getLineEnd(line)));
        }
    }

    /**
   * Constructs and reports an IProblem 
   * @param req  The problem requestor to which to report the error
   * @param cu The CompilationUnit from which to get line ending information
   * @param startPosition The character position within the file of source text to mark
   * @param endPosition The character position within the file of source text of the end of the region to mark (not one beyond it)
   * @param problem  The instance of IProblems giving the id/text of this error
   * @param args The arguments to substitute in the problem text
   */
    public static void report(IProblemRequestor req, org.jmlspecs.eclipse.jmldom.CompilationUnit cu, int startPosition, int endPosition, IProblems problem, Object... args) {
        report(req, cu, problem.toString(args), problem.id(), problem.severity(), startPosition, endPosition);
    }

    /**
   * Constructs and reports a problem that is not associated with a source location
   * @param req  The problem requestor to which to report the error
   * @param problem  The instance of IProblems giving the id/text of this error
   * @param args The arguments to substitute in the problem text
   * @return a String version of the problem message
   */
    public static String report(IProblemRequestor req, IProblems problem, Object... args) {
        String s;
        req.acceptProblem(new CompilerProblem(null, s = problem.toString(args), problem.id(), problem.severity(), -1, -1, -1, null, -1, -1));
        return s;
    }

    /**
   * Constructs and reports a problem to be reported to the user 
   * @param req  The problem requestor to which to report the error
   * @param scanner The instance of the Java scanner that is scanning the source text
   * @param startPosition The character position within the file of source text to mark
   * @param endPosition The character position within the file of source text of the end of the region to mark (not one beyond it)
   * @param problem  A Problems instance providing the template for the message
   * @param args     A sequence of arguments for the problem template message
   */
    public static void report(IProblemRequestor req, JavaScanner scanner, int startPosition, int endPosition, IProblems problem, Object... args) {
        report(req, scanner, problem.toString(args), problem.id(), problem.severity(), startPosition, endPosition);
    }

    /**
   * A utility method that prints the given problem on the given PrintStream
   * @param out the stream on which to write the problem
   * @param p   The IProblem to print
   */
    public static void printProblem(PrintStream out, IProblem p) {
        char[] filenameAsChars = p.getOriginatingFileName();
        String filename = null;
        IResource resource = null;
        if (filenameAsChars != null) {
            filename = (String.valueOf(p.getOriginatingFileName()));
            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            IWorkspaceRoot root = workspace.getRoot();
            resource = root.findMember(filename);
            if (resource != null) {
                filename = resource.getLocation().toOSString();
            }
        }
        int line = p.getSourceLineNumber();
        String lineString = line <= 0 ? "" : String.valueOf(line);
        if (Env.testingMode) {
            out.println((filename == null ? "" : "FILE") + " " + lineString + ": [" + (p.getID() & IProblem.IgnoreCategoriesMask) + "] " + p.getMessage());
        } else {
            String ff = filename == null ? null : filename.replaceAll("\\\\", "/");
            out.println((filename == null ? ("" + lineString) : ("(" + ff + ":" + lineString + ")")) + " [" + (p.getID() & IProblem.IgnoreCategoriesMask) + "] " + p.getMessage());
        }
        if (p.getSourceStart() < 0) return;
        String sline = null;
        int lineStart = -1;
        if (p instanceof CompilerProblem) {
            CompilerProblem cp = (CompilerProblem) p;
            String stext = cp.sourceText;
            lineStart = cp.lineStart;
            if (lineStart >= 0) {
                int k = cp.lineEnd + 1;
                sline = stext.substring(lineStart, k > 0 && k <= stext.length() ? k : stext.length());
            }
        }
        BufferedReader r = null;
        try {
            if (sline == null) try {
                if (p instanceof CompilerProblem) {
                    String stext = ((CompilerProblem) p).sourceText;
                    if (stext != null) r = new BufferedReader(new StringReader(stext));
                }
                if (r == null && resource != null && (resource instanceof IFile)) {
                    r = new BufferedReader(new InputStreamReader(((IFile) resource).getContents()));
                }
                if (r == null) return;
                int count = 0;
                int lastr = -2;
                int c = 0;
                --line;
                if (line > 0) {
                    while (true) {
                        c = r.read();
                        count++;
                        if (c == -1) return;
                        if (c == '\r') {
                            lastr = count;
                            --line;
                            if (line == 0) {
                                c = r.read();
                                count++;
                                if (c == -1) return;
                                break;
                            }
                        } else if (c == '\n') {
                            if (lastr + 1 != count) {
                                --line;
                                c = 0;
                                if (line == 0) break;
                            }
                        }
                    }
                }
                if (c == '\r') {
                    sline = Env.eol;
                } else if (c == 0 || c == '\n') {
                    sline = r.readLine() + Env.eol;
                } else {
                    sline = (char) c + r.readLine() + Env.eol;
                }
                if (lineStart == -1) lineStart = count;
                r.close();
            } catch (Exception e) {
                Log.errorlog("INTERNAL ERROR - Exception occurred while printing a Problem: " + e, e);
                return;
            }
            if (sline != null) {
                out.print(sline);
                {
                    int last = sline.length() - 1;
                    int c = sline.charAt(last);
                    if (!(c == '\n' || c == '\r')) out.println();
                }
                int st = p.getSourceStart();
                int length = p.getSourceEnd() - st + 1;
                st -= lineStart;
                int extra = 0;
                if (st >= sline.length()) {
                    extra = st - sline.length();
                    st = sline.length();
                }
                boolean ellipsis = false;
                if (st + extra + length > sline.length() + 2) {
                    length = sline.length() + 2 - st - extra;
                    if (length <= 0) length = 1;
                    ellipsis = true;
                }
                int i = 0;
                while (--st >= 0) out.print(sline.charAt(i++) == '\t' ? "\t" : " ");
                while (--extra >= 0) out.print(" ");
                while (--length >= 0) out.print("^");
                if (ellipsis) out.print("...");
                out.println();
            }
        } finally {
            if (r != null) {
                try {
                    r.close();
                } catch (java.io.IOException e) {
                    Log.errorlog("Failed to close a BufferedReader", e);
                }
            }
        }
    }

    /**
   * Implementations of this interface can be used to store state
   * that is then used when a problem report is issued.
   */
    public static interface Context {

        /**
     * A simplified problem report call.  An implementation of this
     * method will combine the information from the call parameters
     * with information stored in the instance of the class implementing
     * the Context interface in order to create a full problem 
     * report.
     * @param start the starting character position of the problem
     * @param end the ending character position of the problem
     * @param p the Problems instance
     * @param args the arguments of the Problems instance
     */
        public void report(int start, int end, Problems p, Object... args);
    }
}
