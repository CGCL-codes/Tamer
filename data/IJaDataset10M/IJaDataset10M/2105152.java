package cx.ath.contribs.internal.xerces.impl.xs.util;

import java.util.ArrayList;
import cx.ath.contribs.internal.xerces.impl.xs.SchemaGrammar;
import cx.ath.contribs.internal.xerces.impl.xs.XSModelImpl;
import cx.ath.contribs.internal.xerces.util.XMLGrammarPoolImpl;
import cx.ath.contribs.internal.xerces.xni.grammars.XMLGrammarDescription;
import cx.ath.contribs.internal.xerces.xs.XSModel;

/**
 * Add a method that return an <code>XSModel</code> that represents components in
 * the schema grammars in this pool implementation.
 * 
 * @xerces.internal  
 * 
 * @version $Id: XSGrammarPool.java,v 1.2 2007/07/13 07:23:30 paul Exp $
 */
public class XSGrammarPool extends XMLGrammarPoolImpl {

    /**
     * Return an <code>XSModel</code> that represents components in
     * the schema grammars in this pool implementation.
     *
     * @return  an <code>XSModel</code> representing this schema grammar
     */
    public XSModel toXSModel() {
        ArrayList list = new ArrayList();
        for (int i = 0; i < fGrammars.length; i++) {
            for (Entry entry = fGrammars[i]; entry != null; entry = entry.next) {
                if (entry.desc.getGrammarType().equals(XMLGrammarDescription.XML_SCHEMA)) list.add(entry.grammar);
            }
        }
        int size = list.size();
        if (size == 0) {
            return null;
        }
        SchemaGrammar[] gs = (SchemaGrammar[]) list.toArray(new SchemaGrammar[size]);
        return new XSModelImpl(gs);
    }
}
