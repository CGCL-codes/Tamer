package net.frede.gui.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import net.frede.gui.Avatar;
import net.frede.gui.program.Argument;
import net.frede.gui.program.Command;
import net.frede.gui.program.Option;
import net.frede.gui.program.OptionGroup;
import net.frede.gui.program.Program;
import net.frede.gui.user.HistoryManager;
import net.frede.gui.user.Selection;
import net.frede.toolbox.Invoker;
import net.frede.toolbox.XML.XMLElement;

/**
 * defines an net.frede.gui.action related on commands of a
 * net.frede.gui.program
 */
public class ActionCommandHistory extends ActionCommand {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public static final String SEPARATOR = "/";

    /**
	 * default constructor
	 */
    public ActionCommandHistory() {
    }

    /**
	 * collect the parameters of this ActionCommand. asks the net.frede.gui.user
	 * for command customization
	 * 
	 * @return true if the collect was successfull, otherwise false
	 */
    protected boolean collectParameters() {
        loadHistory();
        boolean back = super.collectParameters();
        return back;
    }

    /**
	 * DOCUMENT ME!
	 */
    protected void loadHistory() {
        Collection opts = new ArrayList();
        Command c = getCommand();
        opts.addAll(c.getOptions());
        opts.addAll(c.getGlobalOptions());
        Program prog = c.getProgram();
        StringBuffer sb = new StringBuffer(prog.getClassName());
        sb.append(SEPARATOR);
        sb.append(prog.getName());
        String progPath = sb.toString();
        Iterator it = opts.iterator();
        while (it.hasNext()) {
            Option o = (Option) it.next();
            if (o instanceof OptionGroup) {
                opts = ((OptionGroup) o).subElements();
                Iterator itgroup = opts.iterator();
                while (itgroup.hasNext()) {
                    Option opt = (Option) itgroup.next();
                    loadInner(progPath, prog, opt);
                }
            }
            loadInner(progPath, prog, o);
        }
        Collection args = new ArrayList();
        args.addAll(c.getArguments());
        it = args.iterator();
        HistoryManager hist = getHistoryManager();
        if (hist != null) {
            while (it.hasNext()) {
                Argument a = (Argument) it.next();
                if (getLogger().isDebugEnabled()) {
                    getLogger().warn("loading history " + progPath + " of argument " + a.getName());
                }
                String path = getPath(a, progPath, prog);
                loadInner(hist, path, prog, a);
            }
        } else {
            getLogger().warn("no history manager for " + getName());
        }
    }

    /**
	 * DOCUMENT ME!
	 */
    protected void process() {
        saveHistory();
        super.process();
    }

    /**
	 * DOCUMENT ME!
	 */
    protected void saveHistory() {
        Collection opts = new ArrayList();
        Command c = getCommand();
        opts.addAll(c.getOptions());
        opts.addAll(c.getGlobalOptions());
        Program prog = c.getProgram();
        StringBuffer sb = new StringBuffer(prog.getClassName());
        sb.append(SEPARATOR);
        sb.append(prog.getName());
        String progPath = sb.toString();
        Iterator it = opts.iterator();
        while (it.hasNext()) {
            Option o = (Option) it.next();
            if (o instanceof OptionGroup) {
                if (o.isSelected()) {
                    opts = ((OptionGroup) o).subElements();
                    Iterator itgroup = opts.iterator();
                    while (itgroup.hasNext()) {
                        Option opt = (Option) itgroup.next();
                        saveInner(progPath, prog, opt);
                    }
                }
            } else {
                saveInner(progPath, prog, o);
            }
        }
        Collection args = new ArrayList();
        args.addAll(c.getArguments());
        it = args.iterator();
        HistoryManager hist = getHistoryManager();
        if (hist != null) {
            while (it.hasNext()) {
                Argument a = (Argument) it.next();
                if (getLogger().isDebugEnabled()) {
                    getLogger().warn("saving history " + progPath + " of argument " + a.getName());
                }
                saveInner(hist, progPath, prog, a);
            }
        } else {
            getLogger().warn("no history manager for " + getName());
        }
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param x
	 *            DOCUMENT ME!
	 * @param progPath
	 *            DOCUMENT ME!
	 * @param prog
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
    private String getPath(XMLElement x, String progPath, Program prog) {
        StringBuffer sb = new StringBuffer(progPath);
        ArrayList al = new ArrayList();
        while (!prog.equals(x) && (x != null)) {
            al.add(0, x);
            x = x.getXMLParent();
        }
        for (int i = 0; i < al.size(); i++) {
            sb.append(SEPARATOR);
            Avatar av = (Avatar) al.get(i);
            sb.append(av.getClassName());
            sb.append(SEPARATOR);
            sb.append(av.getName());
        }
        return sb.toString();
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param progPath
	 *            DOCUMENT ME!
	 * @param prog
	 *            DOCUMENT ME!
	 * @param o
	 *            DOCUMENT ME!
	 */
    private void loadInner(String progPath, Program prog, Option o) {
        HistoryManager hist = getHistoryManager();
        if (hist != null) {
            Collection c = o.getArguments();
            Iterator it = c.iterator();
            while (it.hasNext()) {
                Argument a = (Argument) it.next();
                if (a != null) {
                    if (a.getName() != null) {
                        String path = getPath(a, progPath, prog);
                        loadInner(hist, path, prog, a);
                    }
                }
            }
            String val = null;
            val = hist.get(getPath(o, progPath, prog), "selected");
            if ("true".equals(val)) {
                o.setSelected(true);
            }
        }
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param hist
	 *            DOCUMENT ME!
	 * @param path
	 *            DOCUMENT ME!
	 * @param prog
	 *            DOCUMENT ME!
	 * @param a
	 *            DOCUMENT ME!
	 */
    private void loadInner(HistoryManager hist, String path, Program prog, Argument a) {
        List values = hist.getHistory(path);
        List objs = new ArrayList();
        for (int i = 0; i < values.size(); i++) {
            String val = (String) values.get(i);
            try {
                StringTokenizer st = new StringTokenizer(val, "[,]");
                List vals = new ArrayList();
                Selection s = null;
                if (getGui() != null) {
                    s = getGui().getSelection();
                }
                while (st.hasMoreTokens()) {
                    String className = st.nextToken().trim();
                    String v = st.nextToken().trim();
                    Object obj = null;
                    if (s == null) {
                        obj = Invoker.invokeConstructor(className, new String[] { v });
                    } else {
                        obj = s.objectValue(className, v);
                    }
                    if (obj != null) {
                        vals.add(obj);
                    }
                }
                objs.add(vals);
            } catch (NoSuchElementException e_nse) {
                getLogger().error("Badly formed history value : " + val);
            }
        }
        if (getLogger().isDebugEnabled()) {
            getLogger().warn("loaded history " + path + " : " + objs + " to " + a.getName());
        }
        a.setHistory(objs);
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param hist
	 *            DOCUMENT ME!
	 * @param progPath
	 *            DOCUMENT ME!
	 * @param prog
	 *            DOCUMENT ME!
	 * @param a
	 *            DOCUMENT ME!
	 */
    private void saveInner(HistoryManager hist, String progPath, Program prog, Argument a) {
        if (a != null) {
            if (a.getName() != null) {
                List vals = new ArrayList();
                List values = a.getValues();
                Selection s = null;
                if (getGui() != null) {
                    s = getGui().getSelection();
                }
                for (int i = 0; i < values.size(); i++) {
                    Object obj = values.get(i);
                    vals.add(obj.getClass().getName());
                    if (s == null) {
                        vals.add(obj.toString());
                    } else {
                        vals.add(s.stringValue(obj));
                    }
                }
                String value = vals.toString();
                hist.putHistory(getPath(a, progPath, prog), value);
                if (getLogger().isDebugEnabled()) {
                    getLogger().warn("saved history of " + getPath(a, progPath, prog) + " : " + value);
                }
            }
        }
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param progPath
	 *            DOCUMENT ME!
	 * @param prog
	 *            DOCUMENT ME!
	 * @param o
	 *            DOCUMENT ME!
	 */
    private void saveInner(String progPath, Program prog, Option o) {
        if (o.isSelected()) {
            if (o.isValid()) {
                HistoryManager hist = getHistoryManager();
                if (hist != null) {
                    if (getLogger().isDebugEnabled()) {
                        getLogger().debug("saving history of " + getPath(o, progPath, prog) + " : selected");
                    }
                    hist.put(getPath(o, progPath, prog), "selected", "true");
                    Collection c = o.getArguments();
                    Iterator it = c.iterator();
                    while (it.hasNext()) {
                        Argument a = (Argument) it.next();
                        saveInner(hist, progPath, prog, a);
                    }
                }
            }
        } else {
            HistoryManager hist = getHistoryManager();
            if (hist != null) {
                if (getLogger().isDebugEnabled()) {
                    getLogger().debug("saving history of " + getPath(o, progPath, prog) + " : not selected");
                }
                hist.put(getPath(o, progPath, prog), "selected", "false");
            }
        }
    }
}
