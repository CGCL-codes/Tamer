package com.jvito.compile;

import java.util.LinkedList;
import com.jvito.exception.CompilerException;
import com.jvito.util.Resources;

/**
 * Compiler written for JViTo.
 * 
 * @author Daniel Hakenjos
 * @version $Id: JViToCompiler.java,v 1.3 2008/04/12 14:28:13 djhacker Exp $
 */
public class JViToCompiler implements Compiler, Runnable {

    private Compileable comp;

    private Exception exception;

    private Compileable current;

    private boolean iscompiling = true;

    private int task;

    public static final int COMPILE = 0;

    public static final int COMPILE_ALL = 1;

    public static final int COMPILE_ALL_CHILDS = 2;

    public static final int COMPILE_ALL_PARENTS = 3;

    /**
	 * Defines the names of the tasks. For example get the name of the simple compile-task:<br>
	 * <code>
	 * System.out.println(JViToCompiler.COMPILE_TAKS_NAMES[JViToCompiler.COMPILE]);
	 * </code>
	 * 
	 * @see #COMPILE
	 * @see #COMPILE_ALL
	 * @see #COMPILE_ALL_CHILDS
	 * @see #COMPILE_ALL_PARENTS
	 */
    public static final String[] COMPILE_TASK_NAMES = { Resources.getString("COMILE"), Resources.getString("COMILE_ALL"), Resources.getString("COMILE_ALL_CHILDS"), Resources.getString("COMILE_ALL_PARENTS") };

    /**
	 * Constructs JViToCompiler with the Compileable-object and a task.
	 * 
	 * @see #COMPILE
	 * @see #COMPILE_ALL
	 * @see #COMPILE_ALL_CHILDS
	 * @see #COMPILE_ALL_PARENTS
	 */
    public JViToCompiler(Compileable comp, int task) {
        this.comp = comp;
        this.task = task;
        this.current = null;
        this.exception = null;
    }

    /**
	 * Gets the task.
	 */
    public int getTask() {
        return task;
    }

    /**
	 * Gets the name of the actual task.
	 */
    public String getTaskName() {
        return COMPILE_TASK_NAMES[task];
    }

    public void compile(Compileable comp) throws CompilerException {
        Compileable parent = comp.getParentCompileableObject();
        if ((parent != null) && (!parent.isCompiled())) {
            throw new CompilerException(Resources.getString("EXCEPTION_PARENT_NOT_COMPILED"));
        }
        current = comp;
        comp.compile();
        comp.fireIsCompiled();
    }

    public void compileAll(Compileable comp) throws CompilerException {
        current = comp;
        comp.compile();
        comp.fireIsCompiled();
        compileAllChilds(comp);
    }

    public void compileAllChilds(Compileable comp) throws CompilerException {
        LinkedList<Compileable> complist = new LinkedList<Compileable>();
        int childs = comp.getCompileableChildCount();
        for (int i = 0; i < childs; i++) {
            complist.add(comp.getCompileableChild(i));
        }
        Compileable c;
        while (!complist.isEmpty()) {
            c = complist.removeFirst();
            compile(c);
            int count = c.getCompileableChildCount();
            for (int i = 0; i < count; i++) {
                complist.add(c.getCompileableChild(i));
            }
        }
    }

    /**
	 * Compile all Objects which are Parents from comp. Start at root.
	 * 
	 * @see com.jvito.compile.Compiler#compileAllParents(com.jvito.compile.Compileable)
	 */
    public void compileAllParents(Compileable comp) throws CompilerException {
        LinkedList<Compileable> complist = new LinkedList<Compileable>();
        Compileable parent = comp.getParentCompileableObject();
        while (parent != null) {
            complist.addFirst(parent);
        }
        Compileable c;
        while (!complist.isEmpty()) {
            c = complist.removeFirst();
            compile(c);
        }
    }

    public void run() {
        try {
            runthis();
        } catch (CompilerException e) {
            exception = e;
            e.printStackTrace();
        } catch (Exception e) {
            this.exception = e;
            e.printStackTrace();
        }
    }

    public void runthis() throws CompilerException {
        iscompiling = true;
        if (task == COMPILE) {
            compile(comp);
        } else if (task == COMPILE_ALL) {
            compileAll(comp);
        } else if (task == COMPILE_ALL_CHILDS) {
            compileAllChilds(comp);
        } else if (task == COMPILE_ALL_PARENTS) {
            compileAllParents(comp);
        } else {
        }
        iscompiling = false;
        current = null;
    }

    public Exception getException() {
        return exception;
    }

    public Compileable getCurrentCompileableObject() {
        return current;
    }

    public boolean isCompiling() {
        return iscompiling;
    }
}
