package com.novocode.naf.persist;

import java.util.ArrayList;
import java.util.StringTokenizer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.novocode.naf.app.NAFException;
import com.novocode.naf.model.IObjectModel;

/**
 * Persister for int[] models.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Oct 21, 2004
 * @version $Id: IntArrayModelPersister.java 271 2004-12-16 14:52:05 +0000 (Thu, 16 Dec 2004) szeiger $
 */
public class IntArrayModelPersister<T extends IObjectModel<int[]>> implements IPersister<T> {

    public void toModel(Element e, T model) throws NAFException {
        if ("null".equals(e.getLocalName())) model.setValue(null); else {
            ArrayList<String> l = new ArrayList<String>();
            StringTokenizer tok = new StringTokenizer(e.getTextContent(), ",");
            while (tok.hasMoreTokens()) l.add(tok.nextToken().trim());
            int[] a = new int[l.size()];
            for (int i = 0; i < a.length; i++) a[i] = Integer.parseInt(l.get(i));
            model.setValue(a);
        }
    }

    public Element toDOM(Document doc, T model) throws NAFException {
        int[] val = model.getValue();
        if (val == null) return doc.createElement("null");
        Element e = doc.createElement("int-array");
        StringBuilder b = new StringBuilder();
        for (int v : val) {
            if (b.length() != 0) b.append(',');
            b.append(v);
        }
        e.setTextContent(b.toString());
        return e;
    }
}
