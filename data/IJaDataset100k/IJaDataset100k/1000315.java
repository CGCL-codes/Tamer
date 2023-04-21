package kshos.command;

import java.io.File;
import java.util.ArrayList;
import kshos.command.grammar.*;
import kshos.core.ProcessManager;
import kshos.core.objects.Process;
import kshos.ui.UserInterface;
import org.antlr.runtime.*;

/**
 * Shell.
 * @author <a href="mailto:novotny@students.zcu.cz">Jiri NOVOTNY A09N0032P</a>
 * @version 0.04 23/11/2009
 */
public class KSHell extends Process {

    private UserInterface userInterface;

    private int commandIndex;

    private ArrayList<String> commandHistory;

    /**
     * Get command history index.
     * Enables work with command history from console (UI).
     * @return index
     */
    public int getCommandIndex() {
        return commandIndex;
    }

    /**
     * Increase command history index.
     * Enables work with command history from console (UI).
     */
    public void incCommandIndex() {
        commandIndex++;
    }

    /**
     * Decrease command history index.
     * Enables work with command history from console (UI).
     */
    public void decCommandIndex() {
        commandIndex--;
    }

    /**
     * Gets full command history.
     * Enables work with command history from console (UI).
     * @return arraylist of strings with command history.
     */
    public ArrayList<String> getCommandHistory() {
        return commandHistory;
    }

    /**
     * Sets console (UI) for shell.
     * @param ui
     */
    public void setUserInterface(UserInterface ui) {
        this.userInterface = ui;
    }

    /**
     * Gets console (UI) for shell.
     * @param ui
     */
    public UserInterface getUserInterface() {
        return this.userInterface;
    }

    /**
     * Line processing using ANTLR generated lexer and parser.
     */
    public void processLine(String line) {
        if (!line.equals("")) {
            commandHistory.add(line);
            commandIndex = commandHistory.size();
        } else {
            return;
        }
        if (line.contains("kshell")) {
            if (line.equals("kshell")) ProcessManager.instance().createShell(getUserInterface(), this.getPID()); else this.getErr().stdWriteln("Error: kshell can not be piped!");
            return;
        }
        if (line.contains("exit")) {
            if (line.equals("exit")) processSignal(0); else this.getErr().stdWriteln("Error: exit can not be piped!");
            return;
        }
        OSVM_grammarLexer lex = new OSVM_grammarLexer(new ANTLRStringStream(line));
        CommonTokenStream tokens = new CommonTokenStream(lex);
        OSVM_grammarParser g = new OSVM_grammarParser(tokens);
        try {
            g.parse();
        } catch (RecognitionException e) {
            this.getErr().stdWriteln("Warning: Mismatched input!");
            this.getErr().stdWriteln("Usage: cmd args < in [ | cmd next]* > out");
            return;
        }
        if (g.containsInvalid()) {
            this.getErr().stdWriteln("Warning: Command contains invalid symbols!");
        }
        ProcessManager.instance().createProcess(this, userInterface, g);
    }

    /**
     * Shell init.
     * Sets IO, working directory and command history.
     */
    public void initShell() {
        this.setIn(userInterface);
        this.setOut(userInterface);
        this.setErr(userInterface);
        this.setWorkingDir(new File(""));
        commandIndex = -1;
        commandHistory = new ArrayList<String>();
    }

    @Override
    public void tick() {
        initShell();
    }

    /**
     * Signal processing.
     * @param type signal type
     */
    @Override
    public void processSignal(int type) {
        switch(type) {
            case 0:
                this.getOut().stdWriteln("Good bye :-)");
                if (this.getParent().getPID() == 1 && ProcessManager.instance().getLastShell(getUserInterface().getUser()).equals(this)) {
                    getUserInterface().close();
                } else {
                    while (this.getAllChilds().size() > 0) {
                        this.getChild(this.getAllChilds().firstKey()).setParent(this.getParent());
                        this.getParent().addChild(this.getChild(this.getAllChilds().firstKey()));
                        this.removeChild(this.getAllChilds().firstKey());
                    }
                    this.getIn().stdCloseIn();
                    this.getOut().stdCloseOut();
                    this.getParent().removeChild(this.getPID());
                    ProcessManager.instance().removeProcess(this.getPID());
                }
                break;
            default:
                break;
        }
    }
}
