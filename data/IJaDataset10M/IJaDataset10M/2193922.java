package org.fudaa.dodico.objet;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;
import org.fudaa.dodico.corba.objet.IActivateur;
import org.fudaa.dodico.corba.objet.IActivateurOperations;

/**
 * Classe implementant un activateur.
 * 
 * @version $Revision: 1.15 $ $Date: 2006-09-19 14:44:21 $ by $Author: deniger $
 * @author Guillaume Desnoix
 */
public final class DActivateur extends DService implements IActivateur, IActivateurOperations {

    private static DActivateur INSTANCE;

    private static boolean interactive_;

    /**
   * Mode classe.
   */
    public static final Integer MODE_CLASS = new Integer(2);

    /**
   * Mode execution.
   */
    public static final Integer MODE_EXEC = new Integer(1);

    /**
   * Mode objet.
   */
    public static final Integer MODE_OBJECT = new Integer(3);

    private static DActivateur instance() {
        if (INSTANCE == null) {
            INSTANCE = new DActivateur();
            CDodico.rebind(CDodico.generateName("::objet::IActivateur"), UsineLib.buildStubFromDObject(INSTANCE));
        }
        return INSTANCE;
    }

    private static void outNewLine() {
        System.out.println("");
    }

    /**
   * Liste sur la sortie standard les objet,classes,exec qui peuvent etre activees.
   */
    public static void list() {
        instance().internalList();
    }

    /**
   * Lance l'activateur de maniere interactive.
   * 
   * @param _args non utilise
   */
    public static void main(final String[] _args) {
        outNewLine();
        System.out.println("ACTIVATOR ************************************************************");
        outNewLine();
        interactive_ = true;
        instance();
        System.out.println("Usage: class, exec, file, help, kill, list, object, process");
        System.out.println("       quit, request, toggle");
        try {
            final LineNumberReader rin = new LineNumberReader(new InputStreamReader(System.in));
            while (true) {
                System.out.print(">>> ");
                System.out.flush();
                final String cmd = rin.readLine();
                if (cmd == null) {
                    break;
                }
                if ("".equals(cmd)) {
                    continue;
                }
                if ("e".equals(cmd) || "exec".equals(cmd)) {
                    doExe(rin);
                } else if ("c".equals(cmd) || "class".equals(cmd)) {
                    doClass(rin);
                } else if ("o".equals(cmd) || "object".equals(cmd)) {
                    doObject(rin);
                } else if ("f".equals(cmd) || "file".equals(cmd)) {
                    instance().internalRead();
                } else if ("h".equals(cmd) || "?".equals(cmd) || "help".equals(cmd)) {
                    usage();
                } else if ("k".equals(cmd) || "kill".equals(cmd)) {
                    doKill(rin);
                } else if ("l".equals(cmd) || "list".equals(cmd)) {
                    list();
                } else if ("p".equals(cmd) || "process".equals(cmd)) {
                    instance().internalProcess();
                } else if ("q".equals(cmd) || "quit".equals(cmd)) {
                    doQuit();
                } else if ("r".equals(cmd) || "request".equals(cmd)) {
                    doRequest(rin);
                } else if ("t".equals(cmd) || "toggle".equals(cmd)) {
                    doToggle();
                } else {
                    outNewLine();
                    System.out.println("    command '" + cmd + "' not reconized");
                    outNewLine();
                }
            }
        } catch (final Exception ex) {
        }
    }

    private static void doToggle() {
        interactive_ = !interactive_;
        outNewLine();
        System.out.println("Log mode is " + (interactive_ ? "ON" : "OFF"));
        outNewLine();
    }

    private static void doRequest(final LineNumberReader _rin) throws IOException {
        System.out.print(getInterfaceEntete());
        System.out.flush();
        final String intf = _rin.readLine();
        if (!"".equals(intf)) {
            request(intf);
        }
    }

    private static void doQuit() {
        for (final Iterator e = instance().prcs_.keySet().iterator(); e.hasNext(); ) {
            final Process k = (Process) e.next();
            k.destroy();
        }
        System.exit(0);
    }

    private static String getInterfaceEntete() {
        return "--> Interface : ";
    }

    private static void doKill(final LineNumberReader _rin) throws IOException {
        System.out.print("--> Process : ");
        System.out.flush();
        String proc = _rin.readLine();
        final org.omg.CORBA.Object o = (org.omg.CORBA.Object) instance().srvs_.get(proc);
        if (o != null) {
            CDodico.unbind(proc, o);
            instance().srvs_.remove(proc);
        } else if (!"".equals(proc)) {
            if ("all".equals(proc)) {
                proc = "";
            }
            for (final Iterator e = instance().prcs_.keySet().iterator(); e.hasNext(); ) {
                final Process k = (Process) e.next();
                if (k.toString().indexOf(proc) >= 0) {
                    k.destroy();
                }
            }
        }
    }

    private static void doObject(final LineNumberReader _rin) throws IOException {
        System.out.print(getInterfaceEntete());
        System.out.flush();
        final String intf = _rin.readLine();
        if (!"".equals(intf)) {
            System.out.print("--> Name      : ");
            System.out.flush();
            final String name = _rin.readLine();
            if (!"".equals(name)) {
                final org.omg.CORBA.Object o = CDodico.findServerByName(name, 5000);
                if (o != null) {
                    instance().internalAddObject(intf, o);
                }
            }
        }
    }

    private static void doClass(final LineNumberReader _rin) throws IOException {
        System.out.print(getInterfaceEntete());
        System.out.flush();
        final String intf = _rin.readLine();
        if (!"".equals(intf)) {
            System.out.print("--> Class     : ");
            System.out.flush();
            final String clss = _rin.readLine();
            if (!"".equals(clss)) {
                instance().internalAddClass(intf, clss);
            }
        }
    }

    private static void doExe(final LineNumberReader _rin) throws IOException {
        System.out.print(getInterfaceEntete());
        System.out.flush();
        final String intf = _rin.readLine();
        if (!"".equals(intf)) {
            System.out.print("--> Exec      : ");
            System.out.flush();
            final String exec = _rin.readLine();
            if (!"".equals(exec)) {
                instance().internalAddExec(intf, exec);
            }
        }
    }

    /**
   * @param _f le fichier a lire contenant les operations de l'activateur.
   */
    public static void read(final File _f) {
        if (_f == null) {
            instance().internalRead();
        }
        instance().internalRead(_f);
    }

    /**
   * @param _interface l'interface a interroger
   * @return l'objet active ou null si aucune
   */
    public static org.omg.CORBA.Object request(final String _interface) {
        return instance().internalRequest(_interface);
    }

    /**
   * Cree une instance.
   */
    public static void run() {
        instance();
    }

    /**
   * L'aide.
   */
    public static void usage() {
        outNewLine();
        System.out.println("class   : define a class");
        System.out.println("exec    : define an executable");
        System.out.println("file    : reread the file activateur.lst");
        System.out.println("help    : display this screen");
        System.out.println("kill    : kill a process");
        System.out.println("list    : list the lines");
        System.out.println("object  : define an object");
        System.out.println("process : list the process");
        System.out.println("request : request a server");
        System.out.println("quit    : quit this software");
        System.out.println("toggle  : toggle log mode");
        outNewLine();
    }

    private Map mods_;

    private Map objs_;

    private Map prcs_;

    private Map srvs_;

    private DActivateur() {
        super();
        mods_ = new Hashtable(11);
        objs_ = new Hashtable(11);
        prcs_ = new Hashtable(11);
        srvs_ = new Hashtable(11);
        internalRead();
    }

    private synchronized void internalAddClass(final String _interface, final Class _class) {
        mods_.put(_interface, MODE_CLASS);
        objs_.put(_interface, _class);
    }

    private synchronized void internalAddClass(final String _interface, final String _class) {
        try {
            final Class c = Class.forName(_class);
            internalAddClass(_interface, c);
        } catch (final Exception ex) {
        }
    }

    private synchronized void internalAddExec(final String _interface, final String _cmd) {
        mods_.put(_interface, MODE_EXEC);
        objs_.put(_interface, _cmd);
    }

    private synchronized void internalAddObject(final String _interface, final org.omg.CORBA.Object _object) {
        mods_.put(_interface, MODE_OBJECT);
        objs_.put(_interface, _object);
    }

    private synchronized String[] internalFindInterfaceNames() {
        final String[] r = new String[mods_.size()];
        int i = 0;
        for (final Iterator e = mods_.keySet().iterator(); e.hasNext(); i++) {
            final String name = (String) e.next();
            r[i] = name;
        }
        return r;
    }

    private synchronized void internalList() {
        outNewLine();
        System.out.println("interface ******************** mode *** informations *****************");
        for (final Iterator e = mods_.keySet().iterator(); e.hasNext(); ) {
            final String k = (String) e.next();
            String m = (k + "                            ").substring(0, 28);
            String s = "";
            final Integer v = (Integer) mods_.get(k);
            if (v == MODE_EXEC) {
                m += " * exec   * ";
                s = "" + objs_.get(k);
                final int i = s.indexOf("org.fudaa.dodico.");
                if (i >= 0) {
                    s = s.substring(0, i) + "..." + s.substring(i + 18);
                }
            } else if (v == MODE_CLASS) {
                m += " * class  * ";
                s = ((Class) objs_.get(k)).getName();
            } else if (v == MODE_OBJECT) {
                m += " * object * ";
                s = "corba";
            }
            if (s.startsWith("org.fudaa.dodico.")) {
                s = s.substring(18);
            }
            if (s.length() > 35) {
                s = s.substring(0, 35);
            }
            m += s;
            System.out.println(m);
        }
        System.out.println("**********************************************************************");
        outNewLine();
    }

    private synchronized void internalProcess() {
        outNewLine();
        System.out.println("process ********************** state ********** command **************");
        for (final Iterator e = prcs_.keySet().iterator(); e.hasNext(); ) {
            final Process k = (Process) e.next();
            String m = (k + "                       ").substring(10, 33).toLowerCase() + "     ";
            String s;
            try {
                final String ev = "       " + k.exitValue();
                m += " * killed " + ev.substring(ev.length() - 7, ev.length()) + " * ";
            } catch (final Exception ex) {
                m += " * running        * ";
            }
            s = "" + prcs_.get(k);
            final int i = s.indexOf("org.fudaa.dodico.");
            if (i >= 0) {
                s = s.substring(0, i) + "..." + s.substring(i + 18);
            }
            if (s.length() > 22) {
                s = s.substring(0, 22);
            }
            m += s;
            System.out.println(m);
        }
        for (final Iterator e = srvs_.keySet().iterator(); e.hasNext(); ) {
            final String k = (String) e.next();
            String m = (k + "                            ").substring(0, 28);
            String s;
            m += " * running        * ";
            s = "" + srvs_.get(k);
            if (s.length() > 22) {
                s = s.substring(0, 22);
            }
            m += s;
            System.out.println(m);
        }
        System.out.println("**********************************************************************");
        outNewLine();
    }

    private void internalRead() {
        final String f = System.getProperty("user.home") + System.getProperty("file.separator") + "activateur.lst";
        System.out.println("Reading " + f);
        internalRead(new File(f));
    }

    private void internalRead(final File _f) {
        LineNumberReader rin = null;
        try {
            rin = new LineNumberReader(new FileReader(_f));
            String cmd = rin.readLine();
            while (cmd != null) {
                if ("end".equals(cmd)) {
                    break;
                }
                if ("".equals(cmd)) {
                    continue;
                }
                if (cmd.startsWith("#")) {
                    continue;
                }
                if ("exec".equals(cmd)) {
                    final String intf = rin.readLine();
                    final String cmdl = rin.readLine();
                    internalAddExec(intf, cmdl);
                } else if ("class".equals(cmd)) {
                    final String intf = rin.readLine();
                    final String clss = rin.readLine();
                    internalAddClass(intf, clss);
                } else if ("request".equals(cmd)) {
                    final String intf = rin.readLine();
                    internalRequest(intf);
                }
                cmd = rin.readLine();
            }
            rin.close();
            System.out.println("Succeed");
            System.out.println();
        } catch (final Exception ex) {
            System.err.println("Read: " + ex.getClass());
        } finally {
            try {
                if (rin != null) {
                    rin.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
   * Essai de creer un objet d'interface demande (par reflexion si necessaire).
   * 
   * @param _interface l'interface a creer
   * @return null si echec.
   */
    private synchronized org.omg.CORBA.Object internalRequest(final String _interface) {
        if (interactive_) {
            outNewLine();
            System.out.println("Request " + _interface);
            outNewLine();
        }
        org.omg.CORBA.Object r = null;
        final Integer m = (Integer) mods_.get(_interface);
        if (m == MODE_OBJECT) {
            r = (org.omg.CORBA.Object) objs_.get(_interface);
        } else if (m == MODE_CLASS) {
            final Class c = (Class) objs_.get(_interface);
            try {
                r = (org.omg.CORBA.Object) c.newInstance();
            } catch (final Exception ex) {
            }
            if (r != null) {
                final String name = CDodico.generateName(_interface);
                CDodico.rebind(name, r);
                srvs_.put(name, r);
            }
        } else if (m == MODE_EXEC) {
            try {
                final String cmd = (String) objs_.get(_interface);
                final StringTokenizer st = new StringTokenizer(cmd);
                final List v = new Vector(15);
                String t;
                try {
                    while ((t = st.nextToken()) != null) {
                        v.add(t);
                    }
                } catch (final NoSuchElementException nseex) {
                }
                final String[] l = new String[v.size()];
                v.toArray(l);
                final Process p = Runtime.getRuntime().exec(l);
                prcs_.put(p, cmd);
            } catch (final Exception ex) {
                System.err.println("MODE_EXEC: " + ex.getClass());
            }
        }
        return r;
    }

    public org.omg.CORBA.Object active(final String _interface) {
        return instance().internalRequest(_interface);
    }

    public String description() {
        return "Activateur, serveur d'activation d'objets (CORBA)" + super.description();
    }

    public String[] nomsInterface() {
        return instance().internalFindInterfaceNames();
    }

    public String toString() {
        return "DActivateur(" + mods_.size() + " lines)";
    }
}
