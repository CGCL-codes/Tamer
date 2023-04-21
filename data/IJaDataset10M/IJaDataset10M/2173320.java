package joolets;

import java.lang.reflect.*;
import pipedobjects.*;
import console.*;
import exceptions.*;

/**
 * @author Acri Emanuele
 *
 */
public class DumpObject {

    final String command = "dumpobject";

    final String info = "Name:\n" + "\tDumpObject\n" + "Type:\n" + "\tJooLet\n" + "Description:\n" + "\tDump fields and methods of an object\n" + "Options:\n" + "\t +a    = all\n" + "\t +n -n = name\n" + "\t +f -f = fields\n" + "\t +m -m = methods\n";

    final String usage = "Usage:\n\t" + command + " <options> <object name>\n";

    /**
	 * Return the command managed by the class
	 */
    public String command() {
        return command;
    }

    /**
	 * Return command info and help
	 */
    public String help() {
        String info = this.info + this.usage;
        return info;
    }

    /**
	 * Entry point...
	 */
    public PipedObject main(String[] args, PipedObject pobj) throws CmdExecException {
        return dumpobject(args, pobj);
    }

    /**
	 * Dump java objects
	 * @param class
	 */
    public PipedObject dumpobject(String args[], PipedObject pobj) throws CmdExecException {
        Class c;
        Method[] m;
        Field[] f;
        Boolean name = true;
        Boolean fields = true;
        Boolean methods = true;
        if (args.length < 2) {
            ShellConsole.println(usage);
            throw new CmdExecException();
        }
        args[0] = "";
        for (String curr : args) {
            if (curr.isEmpty()) continue;
            if (curr.equals("+a")) {
                name = true;
                fields = true;
                methods = true;
            } else if (curr.equals("+n")) {
                name = true;
            } else if (curr.equals("-n")) {
                name = false;
            } else if (curr.equals("+f")) {
                fields = true;
            } else if (curr.equals("-f")) {
                fields = false;
            } else if (curr.equals("+m")) {
                methods = true;
            } else if (curr.equals("-m")) {
                methods = false;
            }
            try {
                c = Class.forName(curr);
                if (pobj.pipe) {
                    pobj = new PipedObject();
                    if (name) {
                        pobj.addValue(c);
                    }
                    if (fields) {
                        f = c.getFields();
                        for (int i = 0; i < f.length; i++) pobj.addValue(f[i]);
                    }
                    if (methods) {
                        m = c.getDeclaredMethods();
                        for (int i = 0; i < m.length; i++) pobj.addValue(m[i]);
                    }
                } else {
                    if (name) {
                        ShellConsole.println(c.toString());
                    }
                    if (fields) {
                        f = c.getFields();
                        for (int i = 0; i < f.length; i++) ShellConsole.println(f[i].toString());
                    }
                    if (methods) {
                        m = c.getDeclaredMethods();
                        for (int i = 0; i < m.length; i++) ShellConsole.println(m[i].toString());
                    }
                }
            } catch (ClassNotFoundException e) {
                throw new CmdExecException("Class not found \"" + curr + "\" :");
            }
        }
        return pobj;
    }
}
