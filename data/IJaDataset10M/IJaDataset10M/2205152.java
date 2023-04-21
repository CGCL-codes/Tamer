package cx.ath.contribs.internal.xerces.impl.xs;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * A class used to hold the internal schema grammar set for the current instance
 * 
 * @xerces.internal  
 * 
 * @author Sandy Gao, IBM
 * @version $Id: XSGrammarBucket.java,v 1.2 2007/07/13 07:23:28 paul Exp $
 */
public class XSGrammarBucket {

    /**
     * Hashtable that maps between Namespace and a Grammar
     */
    Hashtable fGrammarRegistry = new Hashtable();

    SchemaGrammar fNoNSGrammar = null;

    /**
     * Get the schema grammar for the specified namespace
     *
     * @param namespace
     * @return SchemaGrammar associated with the namespace
     */
    public SchemaGrammar getGrammar(String namespace) {
        if (namespace == null) return fNoNSGrammar;
        return (SchemaGrammar) fGrammarRegistry.get(namespace);
    }

    /**
     * Put a schema grammar into the registry
     * This method is for internal use only: it assumes that a grammar with
     * the same target namespace is not already in the bucket.
     *
     * @param grammar   the grammar to put in the registry
     */
    public void putGrammar(SchemaGrammar grammar) {
        if (grammar.getTargetNamespace() == null) fNoNSGrammar = grammar; else fGrammarRegistry.put(grammar.getTargetNamespace(), grammar);
    }

    /**
     * put a schema grammar and any grammars imported by it (directly or
     * inderectly) into the registry. when a grammar with the same target
     * namespace is already in the bucket, and different from the one being
     * added, it's an error, and no grammar will be added into the bucket.
     *
     * @param grammar   the grammar to put in the registry
     * @param deep      whether to add imported grammars
     * @return          whether the process succeeded
     */
    public boolean putGrammar(SchemaGrammar grammar, boolean deep) {
        SchemaGrammar sg = getGrammar(grammar.fTargetNamespace);
        if (sg != null) {
            return sg == grammar;
        }
        if (!deep) {
            putGrammar(grammar);
            return true;
        }
        Vector currGrammars = (Vector) grammar.getImportedGrammars();
        if (currGrammars == null) {
            putGrammar(grammar);
            return true;
        }
        Vector grammars = ((Vector) currGrammars.clone());
        SchemaGrammar sg1, sg2;
        Vector gs;
        for (int i = 0; i < grammars.size(); i++) {
            sg1 = (SchemaGrammar) grammars.elementAt(i);
            sg2 = getGrammar(sg1.fTargetNamespace);
            if (sg2 == null) {
                gs = sg1.getImportedGrammars();
                if (gs == null) continue;
                for (int j = gs.size() - 1; j >= 0; j--) {
                    sg2 = (SchemaGrammar) gs.elementAt(j);
                    if (!grammars.contains(sg2)) grammars.addElement(sg2);
                }
            } else if (sg2 != sg1) {
                return false;
            }
        }
        putGrammar(grammar);
        for (int i = grammars.size() - 1; i >= 0; i--) putGrammar((SchemaGrammar) grammars.elementAt(i));
        return true;
    }

    /**
     * get all grammars in the registry
     *
     * @return an array of SchemaGrammars.
     */
    public SchemaGrammar[] getGrammars() {
        int count = fGrammarRegistry.size() + (fNoNSGrammar == null ? 0 : 1);
        SchemaGrammar[] grammars = new SchemaGrammar[count];
        Enumeration schemas = fGrammarRegistry.elements();
        int i = 0;
        while (schemas.hasMoreElements()) grammars[i++] = (SchemaGrammar) schemas.nextElement();
        if (fNoNSGrammar != null) grammars[count - 1] = fNoNSGrammar;
        return grammars;
    }

    /**
     * Clear the registry.
     * REVISIT: update to use another XSGrammarBucket
     */
    public void reset() {
        fNoNSGrammar = null;
        fGrammarRegistry.clear();
    }
}
