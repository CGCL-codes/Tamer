package org.argouml.uml;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;
import org.tigris.gef.util.ChildGenerator;
import ru.novosoft.uml.foundation.core.MGeneralizableElement;
import ru.novosoft.uml.foundation.core.MGeneralization;

/** Utility class to generate the base classes of a class. It
 *  recursively moves up the class hierarchy.  But id does that in a
 *  safe way that will nothang in case of cyclic inheritance. */
public class GenAncestorClasses implements ChildGenerator {

    public static GenAncestorClasses TheInstance = new GenAncestorClasses();

    public Enumeration gen(Object o) {
        Vector res = new Vector();
        if (!(o instanceof MGeneralizableElement)) return res.elements();
        MGeneralizableElement cls = (MGeneralizableElement) o;
        Collection gens = cls.getGeneralizations();
        if (gens == null) return res.elements();
        accumulateAncestors(cls, res);
        return res.elements();
    }

    public void accumulateAncestors(MGeneralizableElement cls, Vector accum) {
        Vector gens = new Vector(cls.getGeneralizations());
        if (gens == null) return;
        int size = gens.size();
        for (int i = 0; i < size; i++) {
            MGeneralization g = (MGeneralization) (gens).elementAt(i);
            MGeneralizableElement ge = g.getParent();
            if (!accum.contains(ge)) {
                accum.add(ge);
                accumulateAncestors(cls, accum);
            }
        }
    }
}
