package org.openejb.alt.assembler.modern.jar.ejb11;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Metadata for a describing a method or set of methods.  They must all be
 * from one bean, but may be from the home or remote interface.
 *
 * @author Aaron Mulder (ammulder@alumni.princeton.edu)
 * @version $Revision: 1.2 $
 */
public class MethodMetaData {

    private String ejbName;

    private String description;

    private Boolean remoteInterface;

    private String name;

    private LinkedList args = null;

    public MethodMetaData() {
    }

    public void setEJBName(String name) {
        ejbName = name;
    }

    public String getEJBName() {
        return ejbName;
    }

    public void setDescription(String desc) {
        description = desc;
    }

    public String getDescription() {
        return description;
    }

    public void setInterfaceRemote(Boolean isRemote) {
        remoteInterface = isRemote;
    }

    public Boolean isRemoteInterface() {
        return remoteInterface;
    }

    public void setMethodName(String name) {
        this.name = name;
    }

    public String getMethodName() {
        return name;
    }

    public void addArgument(String arg) {
        if (args == null) args = new LinkedList();
        args.add(arg);
    }

    public void removeArgument(String arg) {
        if (args != null) args.remove(arg);
    }

    public void setArguments(String[] argList) {
        if (args == null) args = new LinkedList(); else args.clear();
        args.addAll(Arrays.asList(argList));
    }

    public String[] getArguments() {
        if (args == null) return null;
        return (String[]) args.toArray(new String[args.size()]);
    }

    public void setArguments(Class[] argClasses) {
        if (args == null) args = new LinkedList(); else args.clear();
        for (int i = 0; i < argClasses.length; i++) args.add(argClasses[i].getName());
    }

    public String toString() {
        StringBuffer buf = new StringBuffer(ejbName);
        if (remoteInterface != null && !remoteInterface.booleanValue()) buf.append("Home");
        buf.append('.').append(name).append('(');
        if (args == null) {
            buf.append('*');
        } else {
            for (int i = 0; i < args.size(); i++) {
                if (i > 0) buf.append(", ");
                buf.append((String) args.get(i));
            }
        }
        buf.append(')');
        return buf.toString();
    }
}
