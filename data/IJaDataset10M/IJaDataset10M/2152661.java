package de.fhg.igd.semoa.shell;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 *
 *
 * @author Roger Hartmann
 * @version "$Id: ASTCommandStatement.java 1722 2006-04-13 14:57:28Z jpeters $"
 */
public class ASTCommandStatement extends SimpleNode {

    protected int type = TYPE_CMD;

    public static String[] TYPES = { "Command", "BackTick" };

    public static int TYPE_CMD = 0;

    public static int TYPE_BTK = 1;

    protected String tailToken = "";

    public ASTCommandStatement(int id) {
        super(id);
    }

    public Object eval(ShellParser p) {
        StringBuffer strbuf;
        ArrayList list;
        String[] aliasArgs;
        String[] children;
        String[] args;
        String command;
        Object obj;
        int start;
        int n;
        int i;
        list = new ArrayList();
        strbuf = new StringBuffer((String) jjtGetChild(0).eval(p));
        for (i = 1; i < jjtGetNumChildren(); i++) {
            if (!jjtGetChild(i - 1).seperated()) {
                strbuf.append((String) jjtGetChild(i).eval(p));
            } else {
                list.add(strbuf.toString());
                strbuf = new StringBuffer((String) jjtGetChild(i).eval(p));
            }
        }
        list.add(strbuf.toString());
        children = (String[]) list.toArray(new String[0]);
        command = children[0].trim();
        start = 1;
        if (command.equals("enable")) {
            command = children[1].trim();
            start = 2;
        }
        if (command.equals("disable")) {
            return null;
        }
        list = new ArrayList();
        obj = p.run_.aliases_.get(command);
        if (obj != null) {
            aliasArgs = (String[]) obj;
            if (aliasArgs[0] != null) {
                command = aliasArgs[0].trim();
                for (i = 1; i < aliasArgs.length; i++) {
                    list.add(aliasArgs[i]);
                }
            }
        }
        if (p.run_.label_ != null) {
            if (command.equals(p.run_.label_)) {
                p.run_.label_ = null;
            }
            return null;
        }
        if (command.startsWith(":")) {
            return null;
        }
        for (i = start; i < children.length; i++) {
            list.add(children[i]);
        }
        args = (String[]) list.toArray(new String[0]);
        try {
            if (type != TYPE_BTK) {
                obj = p.run_.exec(command, args);
            } else {
                obj = ((String) p.run_.execBTK(command, args));
            }
            p.run_.setResult(obj);
        } catch (NoSuchMethodException e) {
            System.err.println("Unknown command: " + command);
            return null;
        } catch (InvocationTargetException e) {
            p.run_.addExceptionToHistory(command, args, e.getTargetException());
            System.err.println(" [Command caused an exception!]");
            return null;
        } catch (Throwable e) {
            p.run_.addExceptionToHistory(command, args, e);
            System.err.println(" [Command caused an exception!]");
            return null;
        }
        return obj;
    }

    public String prettyPrint() {
        StringBuffer str;
        int n;
        int i;
        str = new StringBuffer();
        n = jjtGetNumChildren();
        if (type == TYPE_BTK) {
            str.append("`");
        }
        for (i = 0; i < n; i++) {
            str.append(jjtGetChild(i).prettyPrint());
            if (jjtGetChild(i).seperated()) {
                str.append(" ");
            }
        }
        if (type == TYPE_BTK) {
            str.append("`");
            if (seperated()) {
                str.append(" ");
            }
        }
        return str.toString();
    }

    public boolean seperated() {
        if (space_.indexOf(tailToken.charAt(tailToken.length() - 1)) != -1) {
            return true;
        } else {
            return false;
        }
    }
}
