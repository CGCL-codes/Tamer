package sifter.ui;

import sifter.*;
import sifter.rcfile.*;
import sifter.translation.*;
import sifter.ui.*;
import sifter.messages.*;
import net.sourceforge.jmisc.Debug;
import java.io.IOException;
import java.util.Vector;
import java.util.Date;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

/** This class prints out comments and choices for the user.  It's subclasses
    will then get responses from the user.

    The different sub-classes of this are
    <ul>
    <li> GUI.java:  the graphical user interface (GUI), using swing.
    <li> LineReader.java: the command line user interface (CLUI).  Output
    is sent to stdout, and user input is read from stdin.
    <li> Buffer.java: the command line until anything unusual happens and
    then it turns graphical user interface (CLUAUHATITGUI).
    Output is sent to stdout until input is required from the user, then a GUI is
    opend up.  If no user input is needed, no GUI is every opened.
    </ul>

    If this class itself is used, it is a NUI (no user interface), and will
    choose the default for any ChoiceSet it is given.  If any ChoiceSet has a
    level that is higher than acceptLevel, it will throw an exception.

    @author Fred Gylys-Colwell
    @version $Name:  $, $Revision: 1.14 $
*/
public class Chooser extends UnicastRemoteObject implements Verify {

    /** Above this level, don't choose automatically.
	It is set from the value in Argument.verifyFlag during startup. 
     */
    int acceptLevel = COMMENT;

    /** Below this level, don't print choices.
	It is set from the value in Argument.verifyQuiet during startup. 
     */
    int quietLevel = COMMENT;

    Parent currentDir = null;

    static String bar = "------------------";

    static String space = "                  ";

    public Chooser() throws RemoteException {
        pickArt();
    }

    public void increaseAcceptLevel(int i) {
        acceptLevel = Math.min(i, acceptLevel);
    }

    /** This sets the automatic acceptance level.  
	Any choice whose severity level below the automatic acceptance level will
	automatically be marked as 'O.K.' and will automatically be run when its
	turn comes up.
    */
    public void setAcceptLevel(int i) {
        acceptLevel = i;
    }

    public int getAcceptLevel() {
        return acceptLevel;
    }

    /** This sets the automatic acceptance level, by name instead of by number. */
    public void setAcceptLevel(String s) {
        for (int i = 0; i < levelNames.length; i++) {
            if (s.equalsIgnoreCase(levelNames[i])) {
                acceptLevel = i;
                return;
            }
        }
        System.out.println("No acceptLevel level '" + s + "'.  Try: ");
        for (int i = 0; i < levelNames.length; i++) {
            System.out.print(levelNames[i] + "  ");
        }
        System.out.println("");
    }

    /** This sets the quiet level. Any warning or choice whose severity level
	is below this will not be mentioned in the log file. */
    public void setQuietLevel(int i) {
        quietLevel = i;
        if (quietLevel > acceptLevel) setAcceptLevel(quietLevel);
    }

    /** This sets the quiet level, by name instead of by number. */
    public void setQuietLevel(String s) {
        for (int i = 0; i < levelNames.length; i++) {
            if (s.equalsIgnoreCase(levelNames[i])) {
                setQuietLevel(i);
                return;
            }
        }
    }

    public synchronized void warn(String s, int level) throws IOException {
        ChoiceSet c = new ChoiceSet(s, level);
        choose(c);
        if (level == COMMENT) {
            c.status = ChoiceSet.DONE;
        }
    }

    public void cd(Parent p) throws IOException {
        choose(p);
        currentDir = p;
    }

    public void cdup() throws IOException {
        if ((currentDir != null) && currentDir.printed) {
            System.out.println(indentS[3] + "--------------------");
        }
        if (currentDir != null) {
            if (ignoredDir == currentDir.dir) {
                Debug.println(Bug.ACTIONS, "No longer ignoring dir. " + ignoredDir);
                ignoredDir = null;
            }
            currentDir = currentDir.parent;
        } else {
            Debug.println(Bug.ACTIONS, "Warning: currentDir = null, ignoredDir = " + ignoredDir);
            ignoredDir = null;
        }
        if (currentDir != null) indent = currentDir.indent + 1; else indent = 0;
        pickArt();
    }

    void cdup(int i) {
        while (indent > i) {
            System.out.println(indentS[3] + "--------------------");
            indent--;
            pickArt();
        }
    }

    Directory ignoredDir = null;

    public void ignoreChildren(Directory dir) {
        if (ignoredDir == null) {
            ignoredDir = dir;
            Debug.println(Bug.ACTIONS, "Starting to ignore dir. " + ignoredDir);
        } else {
            Debug.println(Bug.ACTIONS, "Already ignoreing dir. " + ignoredDir);
        }
    }

    public void oneDate(String name, Date d) throws IOException {
        if (d.getTime() < 0) {
            warn("No date for " + name + ".", VERBOSE);
        } else {
            warn("The " + name + " was on " + d + ".", VERBOSE);
        }
    }

    /** Make a choice, or print a warning. */
    public void choose(ChoiceSet c) throws IOException {
        c.setIndent(currentDir);
        if (ignoredDir != null) {
            if (c.trySetSelectedIndex(ChoiceSet.SC_IGNORE)) {
                c.choose();
                return;
            }
        }
        if (c.level >= quietLevel) {
            print(c);
            if (c.level > acceptLevel) {
                System.err.println("The accept level: '" + levelNames[acceptLevel] + "' is lower " + "than this action requires: '" + levelNames[c.level] + "'.");
                throw new IOException(c.toString());
            }
            OneChoice[] s = c.getChoices();
            if (s.length > 0) {
                System.out.println(indentS[2] + "* " + s[c.getSelectedIndex()]);
            }
        }
        c.choose();
        MemoryCheck.check(this);
    }

    int indent = 0;

    static final String[][] asciiArt = { { "*****", "-----", "     ", "......" }, { "/----", "|----", "|    ", "\\----" }, { "|/---", "||---", "||   ", "|\\---" }, { "||/--", "|||--", "|||  ", "||\\--" }, { "/--", "|--", "|  ", "\\--" } };

    String[] indentS = new String[4];

    void pickArt() {
        if (indent > 3) {
            String count = "" + (indent - 1);
            if (indent < 11) count = " " + (indent - 1);
            for (int i = 0; i < 4; i++) indentS[i] = count + asciiArt[4][i];
        } else {
            for (int i = 0; i < 4; i++) indentS[i] = asciiArt[indent][i];
        }
    }

    public void print(ChoiceSet c) {
        if ((c.parent != null) && (!c.parent.printed)) {
            print(c.parent);
        }
        cdup(c.indent);
        String prefix = indentS[1];
        if (c instanceof Parent) {
            ((Parent) c).printed = true;
            indent = c.indent + 1;
            pickArt();
            prefix = indentS[0];
        }
        if (c.lines != null) {
            StringBuffer line = new StringBuffer(prefix);
            line.append(bar);
            line.replace(line.length() - c.prefix.length(), line.length(), c.prefix);
            for (int i = 0; i < c.lines.length; i++) {
                line.append(" ");
                line.append(c.lines[i].local);
            }
            System.out.println(line.toString());
            prefix = indentS[2];
            line = new StringBuffer(prefix);
            line.append(space);
            line.replace(line.length() - c.hints.length(), line.length(), c.hints);
            for (int i = 0; i < c.lines.length; i++) {
                line.append(" ");
                line.append(c.lines[i].remote);
            }
            System.out.println(line.toString());
            prefix = indentS[2];
        }
        String[] s = c.getDescription();
        for (int i = 0; i < s.length; i++) {
            System.out.println(prefix + s[i]);
            prefix = indentS[2];
        }
    }

    public void pause() throws IOException {
        return;
    }

    public void done() throws IOException {
        return;
    }
}
