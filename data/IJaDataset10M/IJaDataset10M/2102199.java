package org.mitre.dm;

import java.util.*;
import org.mitre.midiki.state.*;
import org.mitre.midiki.logic.*;
import org.mitre.dm.*;

/**
 * Tokenizes a string, breaking on whitespace and punctuation.
 * Does not attempt to construct a lattice, just a list.
 *
 * @author <a href="mailto:cburke@mitre.org">Carl Burke</a>
 * @version 1.0
 * @since 1.0
 * @see MethodHandler
 */
public class WhitespaceTokenizingInterpreter {

    public WhitespaceTokenizingInterpreter() {
        super();
    }

    public CellHandlers initializeHandlers() {
        CellHandlers interpreterCell = new CellHandlers(ContractDatabase.find("interpreter"));
        MethodHandler transducer = new MethodHandler() {

            public boolean invoke(Collection arguments, Bindings bindings) {
                Object intfid = null;
                Object string = null;
                Object wordlist = null;
                Iterator argIt = arguments.iterator();
                if (argIt.hasNext()) intfid = argIt.next();
                if (argIt.hasNext()) string = argIt.next();
                if (argIt.hasNext()) wordlist = argIt.next();
                if (string == null) {
                    return false;
                }
                String str = string.toString();
                StringTokenizer st = new StringTokenizer(str);
                LinkedList wl = new LinkedList();
                while (st.hasMoreTokens()) {
                    String token = st.nextToken();
                    wl.add(token);
                }
                if (Unify.getInstance().matchTerms(wordlist, wl, bindings)) {
                    return true;
                }
                return false;
            }
        };
        interpreterCell.addMethodHandler("string2wordlist", transducer);
        return interpreterCell;
    }
}
