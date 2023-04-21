package net.frede.gui.program;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.frede.gui.gui.output.OutputFormat;
import net.frede.gui.user.Selection;
import net.frede.toolbox.XML.XMLElement;

/**
 * a logical view of a net.frede.gui.program a net.frede.gui.program has:
 * 
 * <p>
 * a name used for console input (i.e. 'ls', 'cvs',...)
 * </p>
 * 
 * <p>
 * some global options used for all commands
 * </p>
 * 
 * <p>
 * some commands
 * </p>
 * 
 * <p>
 * some arguments and some options to are used as references within the commands
 * </p>
 * 
 * <p>
 * An example of such use may be done with a CVS example : cvs -q update -P -d
 * foo.c
 * </p>
 * 
 * <p>
 * This is referred as a 'Command' cvs is the name of the net.frede.gui.program
 * -q is one global option of the net.frede.gui.program update is the name of
 * the command -P -d are the specific options of the command foo.c is the
 * argument of the command
 * </p>
 * 
 * <p>
 * the commands are stored in the internal structure of the Avatar
 * </p>
 */
public class Program extends Notifier {

    /**
	 * the holder of the arguments of the net.frede.gui.program
	 */
    private Collection arguments = new ArrayList();

    /**
	 * The whole pack of output format that this net.frede.gui.program needs.
	 */
    private Collection formats;

    /**
	 * the holder of the options of the net.frede.gui.program
	 */
    private Collection options = new ArrayList();

    private Selection selection;

    /**
	 * default constructor
	 */
    public Program() {
        super();
        formats = new ArrayList();
    }

    /**
	 * Get the value of arguments.
	 * 
	 * @return value of arguments.
	 */
    public Collection getArguments() {
        return arguments;
    }

    /**
	 * gets the command with the given name
	 * 
	 * @param name
	 *            the name of the command to retrieve
	 * 
	 * @return the command with the name in input, or null if there is no
	 *         command with this name
	 */
    public Command getCommand(String name) {
        return (Command) getElementByName(Command.class, name);
    }

    /**
	 * Get the value of commands.
	 * 
	 * @return value of commands.
	 */
    public Collection getCommands() {
        return getElements();
    }

    /**
	 * Get the formats of this command.
	 * 
	 * @return a Collection view of the formats of this command.
	 */
    public Collection getFormats() {
        return formats;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
    public List getInnerStringValues() {
        List back = new ArrayList();
        Iterator it = getOptions().iterator();
        while (it.hasNext()) {
            Option o = (Option) it.next();
            Collection c = o.getStringValues();
            if (selection != null) {
                c = selection.sourceValues(c);
            }
            back.addAll(c);
        }
        it = getArguments().iterator();
        while (it.hasNext()) {
            Argument a = (Argument) it.next();
            if ((a != null) && a.isValid()) {
                Collection c = a.getStringValues();
                if (selection != null) {
                    c = selection.sourceValues(c);
                }
                back.addAll(c);
            }
        }
        return back;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
    public List getInnerValues() {
        List back = new ArrayList();
        Iterator it = getOptions().iterator();
        while (it.hasNext()) {
            Option o = (Option) it.next();
            Collection c = o.getValues();
            if (selection != null) {
                c = selection.sourceValues(c);
            }
            back.addAll(c);
        }
        it = getArguments().iterator();
        while (it.hasNext()) {
            Argument a = (Argument) it.next();
            if ((a != null) && a.isValid()) {
                Collection c = a.getValues();
                if (selection != null) {
                    c = selection.sourceValues(c);
                }
                back.addAll(c);
            }
        }
        return back;
    }

    /**
	 * Get the value of options.
	 * 
	 * @return value of options.
	 */
    public Collection getOptions() {
        return options;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param s
	 *            DOCUMENT ME!
	 */
    public void setSelection(Selection s) {
        selection = s;
        Iterator it = getArguments().iterator();
        while (it.hasNext()) {
            Argument a = (Argument) it.next();
            if (a != null) {
                a.setValues(s.getSelectedValues());
            }
        }
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
    public List getStringValues() {
        List back = new ArrayList();
        back.add(getName());
        back.addAll(getInnerStringValues());
        return back;
    }

    /**
	 * determines if this Option instance is valid , i.e. if its arguments and
	 * its options are valid
	 * 
	 * @return true if this instance is valid, false otherwise
	 */
    public boolean isValid() {
        boolean back = true;
        Collection c = getOptions();
        if (c != null) {
            Iterator it = c.iterator();
            while (it.hasNext()) {
                Option o = (Option) it.next();
                back &= o.isValid();
                if (!back) {
                    break;
                }
            }
        }
        Iterator it = getArguments().iterator();
        while (it.hasNext()) {
            Argument a = (Argument) it.next();
            back &= a.isValid();
            if (!back) {
                break;
            }
        }
        return back;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
    public List getValues() {
        List back = new ArrayList();
        back.add(getName());
        back.addAll(getInnerValues());
        return back;
    }

    /**
	 * adds a child element that was given from a XML description
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 */
    public void addXML(XMLElement e) {
        if (e instanceof Command) {
            Command c = (Command) e;
            getLogger().debug("adding command " + c + " to " + this);
            super.addXML(c);
            c.setProgram(this);
            c.setPrefix(getName());
        } else if (e instanceof Option) {
            getLogger().debug("adding " + e + " to " + this + " as an option");
            options.add(e);
            ((Option) e).setPrefix(getName());
        } else if (e instanceof Argument) {
            getLogger().debug("adding " + e + " to " + this + " as an argument");
            arguments.add(e);
            ((Argument) e).setPrefix(getName());
        } else if (e instanceof OutputFormat) {
            getLogger().debug("adding format " + e + " to " + this);
            formats.add(e);
        } else {
            getLogger().warn("trying to add " + e + "->" + e.getClass() + " not suitable for " + this);
        }
    }

    /**
	 * DOCUMENT ME!
	 */
    public void finalizeBuild() {
        long beg = System.currentTimeMillis();
        selfReference();
        long end = System.currentTimeMillis();
        getLogger().info("self referencing done in " + (end - beg) + " millis");
    }

    /**
	 * returns all children elements of this instance for an XML-point-of-view
	 * 
	 * @return a collection of the children elements of this instance
	 */
    public Collection subElements() {
        Collection c = new ArrayList();
        c.addAll(getArguments());
        c.addAll(getOptions());
        c.addAll(super.subElements());
        return c;
    }

    /**
	 * get a string description of this command
	 * 
	 * @return a string description of this command
	 */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        List l = getStringValues();
        Iterator it = l.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            sb.append(' ');
        }
        return sb.toString();
    }
}
