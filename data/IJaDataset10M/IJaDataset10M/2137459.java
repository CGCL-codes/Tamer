package jugglinglab.notation;

import java.awt.*;
import java.util.*;
import jugglinglab.core.*;
import jugglinglab.jml.*;
import jugglinglab.util.*;

public abstract class Notation {

    static ResourceBundle errorstrings;

    static {
        errorstrings = JLLocale.getBundle("ErrorStrings");
    }

    static Hashtable hash = null;

    public static final String[] builtinNotations = { "Siteswap" };

    public static final int NOTATION_NONE = 0;

    public static final int NOTATION_SITESWAP = 1;

    public static Notation getNotation(String name) throws JuggleExceptionUser, JuggleExceptionInternal {
        if (hash == null) hash = new Hashtable();
        Notation not = (Notation) hash.get(name.toLowerCase());
        if (not == null) {
            Notation newnot = null;
            try {
                Object obj = Class.forName("jugglinglab.notation." + name.toLowerCase() + "Notation").newInstance();
                if (!(obj instanceof Notation)) throw new JuggleExceptionInternal(errorstrings.getString("Error_notation_bad") + ": '" + name + "'");
                newnot = (Notation) obj;
            } catch (ClassNotFoundException cnfe) {
                throw new JuggleExceptionUser(errorstrings.getString("Error_notation_notfound") + ": '" + name + "'");
            } catch (IllegalAccessException iae) {
                throw new JuggleExceptionUser(errorstrings.getString("Error_notation_cantaccess") + ": '" + name + "'");
            } catch (InstantiationException ie) {
                throw new JuggleExceptionInternal(errorstrings.getString("Error_notation_cantcreate") + ": '" + name + "'");
            }
            hash.put(name.toLowerCase(), newnot);
            return newnot;
        }
        return not;
    }

    public abstract String getName();

    public abstract JMLPattern getJMLPattern(String pattern) throws JuggleExceptionUser, JuggleExceptionInternal;
}
