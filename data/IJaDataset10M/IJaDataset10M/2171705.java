package it.haefelinger.flaka;

import it.haefelinger.flaka.util.Static;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;

/**
 * 
 * @author merzedes
 * @since 1.0
 */
public class For extends it.haefelinger.flaka.Task implements TaskContainer {

    protected String expr;

    protected String var;

    /** Optional Vector holding the nested tasks */
    protected Vector tasks = new Vector();

    protected Object saved = null;

    public For() {
        super();
    }

    /**
   * The argument list to be iterated over.
   */
    public void setIn(String expr) {
        this.expr = Static.trim3(getProject(), expr, this.expr);
    }

    /**
   * Set the var attribute. This is the name of the macrodef attribute that gets
   * set for each iterator of the sequential element.
   */
    public void setVar(String var) {
        this.var = Static.trim3(getProject(), var, this.var);
    }

    public void addTask(Task nestedTask) {
        this.tasks.add(nestedTask);
    }

    protected void rescue() {
        this.saved = getProject().getReference(this.var);
    }

    protected void restore() {
        Static.assign(getProject(), this.var, this.saved, Static.VARREF);
    }

    protected void exectasks(Object val) throws BuildException {
        Iterator iter;
        Task task;
        Static.assign(getProject(), this.var, val, Static.VARREF);
        iter = this.tasks.iterator();
        while (iter.hasNext()) {
            task = (Task) iter.next();
            task.perform();
        }
    }

    protected Iterator iterator() {
        Iterator iter = null;
        Project project;
        Object obj;
        project = getProject();
        obj = Static.el2obj(project, this.expr);
        if (obj == null) return null;
        if (obj instanceof Iterable) {
            iter = ((Iterable) obj).iterator();
            return iter;
        }
        if (obj instanceof Map) {
            Set keys = ((Map) obj).keySet();
            iter = Arrays.asList(keys.toArray()).iterator();
            return iter;
        }
        iter = Arrays.asList(obj).iterator();
        return iter;
    }

    public void execute() throws BuildException {
        Iterator iter;
        if (this.expr == null || this.var == null) {
            return;
        }
        try {
            rescue();
            iter = iterator();
            while (iter != null && iter.hasNext()) {
                try {
                    exectasks(iter.next());
                } catch (BuildException bx) {
                    String s;
                    s = bx.getMessage();
                    if (s == null) throw bx;
                    if (s.endsWith(Break.TOKEN)) break;
                    if (s.endsWith(Continue.TOKEN)) continue;
                    throw bx;
                }
            }
        } finally {
            restore();
        }
    }
}
