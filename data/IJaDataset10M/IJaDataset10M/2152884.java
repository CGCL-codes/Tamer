package com.bluemarsh.jswat.command;

import com.bluemarsh.jswat.Log;
import com.bluemarsh.jswat.Session;
import com.sun.jdi.ClassLoaderReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Defines the class that handles the 'classes' command.
 *
 * @author  Nathan Fiedler
 */
public class classesCommand extends JSwatCommand {

    /**
     * Perform the 'classes' command.
     *
     * @param  session  JSwat session on which to operate.
     * @param  args     Tokenized string of command arguments.
     * @param  out      Output to write messages to.
     */
    public void perform(Session session, CommandArguments args, Log out) {
        if (!session.isActive()) {
            throw new CommandException(Bundle.getString("noActiveSession"));
        }
        VirtualMachine vm = session.getVM();
        vm.suspend();
        List classes = vm.allClasses();
        vm.resume();
        if (args.hasMoreTokens()) {
            classes = new ArrayList(classes);
            String regex = args.nextToken();
            Pattern patt = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            ListIterator liter = classes.listIterator();
            while (liter.hasNext()) {
                ReferenceType clazz = (ReferenceType) liter.next();
                Matcher matcher = patt.matcher(clazz.name());
                if (!matcher.find()) {
                    liter.remove();
                }
            }
        }
        Iterator iter = classes.iterator();
        if (iter.hasNext()) {
            while (iter.hasNext()) {
                ReferenceType clazz = (ReferenceType) iter.next();
                out.write(clazz.name());
                out.write(" [");
                ClassLoaderReference clr = clazz.classLoader();
                if (clr != null) {
                    out.write(clr.referenceType().name());
                    out.write(" (");
                    out.write(String.valueOf(clr.uniqueID()));
                    out.write(")");
                } else {
                    out.write(Bundle.getString("classes.noClassLoader"));
                }
                out.writeln("]");
            }
        } else {
            throw new CommandException(Bundle.getString("classes.noneLoaded"));
        }
    }
}
