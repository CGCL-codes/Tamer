package net.sf.jnclib.tp.ssh2.term;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.LinkedList;

/**
* This is a list of the available terminal classes
*/
public class Terminals {

    private static final String PROP_DEFAULT = "org.maverickdbms.basic.term.default";

    private static final String PROP_LOAD_LOCAL = "org.maverickdbms.basic.term.load_local";

    private static final String DEFAULT_DEFAULT = "org.maverickdbms.basic.term.VT100";

    private static final boolean DEFAULT_LOAD_LOCAL = true;

    private static final Terminal[] LIST = { new LINUX(), new VT100() };

    private TermInfo terminfo;

    private LinkedList terminals;

    private String defaultTerm;

    Terminals(Properties properties) {
        terminfo = new TermInfo(properties);
        terminals = new LinkedList();
        defaultTerm = properties.getProperty(PROP_DEFAULT, DEFAULT_DEFAULT);
        String loadLocal = properties.getProperty(PROP_LOAD_LOCAL);
        if ((loadLocal != null) ? loadLocal.equals("true") : DEFAULT_LOAD_LOCAL) {
            load();
        }
    }

    public static Terminals getInstance(Properties properties) {
        return new Terminals(properties);
    }

    public Terminal getTerminal(String name) throws IOException {
        Terminal query = new QueryTerminal(name);
        if (terminals.contains(query)) {
            return (Terminal) terminals.get(terminals.indexOf(query));
        }
        Terminal term = terminfo.getTerminal(name);
        if (term != null) {
            return (Terminal) terminals.get(terminals.indexOf(term));
        }
        try {
            Class c = Class.forName(defaultTerm);
            return (Terminal) c.newInstance();
        } catch (ClassNotFoundException cnfe) {
            throw new IOException(cnfe.toString());
        } catch (IllegalAccessException iae) {
            throw new IOException(iae.toString());
        } catch (InstantiationException ie) {
            throw new IOException(ie.toString());
        }
    }

    private void load() {
        for (int i = 0; i < LIST.length; i++) {
            terminals.add(LIST[i]);
        }
    }
}
