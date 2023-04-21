package com.newisys.dv.ifgen.ant;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.tools.ant.types.DataType;

/**
 * DataType for nested shell elements.
 * 
 * @author Jon Nall
 */
public class ShellType extends DataType {

    private String testbench;

    private String shellname;

    private List<ArgType> parameters = new LinkedList<ArgType>();

    public void setTestbench(String testbench) {
        this.testbench = testbench;
    }

    public void setShellname(String shellname) {
        this.shellname = shellname;
    }

    public void addConfiguredArg(ArgType parameter) {
        parameters.add(parameter);
    }

    public String getTestbench() {
        return this.testbench;
    }

    public String getShellname() {
        return this.shellname;
    }

    public List<? extends ArgType> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(64);
        buf.append(testbench);
        buf.append(" <");
        Iterator<ArgType> iter = parameters.iterator();
        while (iter.hasNext()) {
            final ArgType def = iter.next();
            buf.append(def.getName());
            buf.append(" = ");
            buf.append(def.getValue());
            if (iter.hasNext()) {
                buf.append(", ");
            }
        }
        buf.append("> ");
        buf.append(shellname);
        return buf.toString();
    }
}
