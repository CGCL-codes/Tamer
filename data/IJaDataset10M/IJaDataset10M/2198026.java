package de.blitzcoder.collide.gui.debugger;

import java.io.File;
import java.util.LinkedList;

/**
 *
 * @author blitzcoder
 */
public class DebugTraceFunction {

    private String name;

    private File file;

    private int line;

    private int column;

    private LinkedList<DebugVariable> variables;

    public DebugTraceFunction(String name, String file, int line, int col) {
        this.name = name;
        this.file = new File(file);
        this.line = line;
        this.column = col;
        variables = new LinkedList<DebugVariable>();
    }

    public void addVariable(DebugVariable var) {
        variables.addLast(var);
    }

    public LinkedList<DebugVariable> getVariables() {
        return variables;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return name;
    }
}
