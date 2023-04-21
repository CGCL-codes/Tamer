package com.foursoft.fourever.vmodell.regular.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import com.foursoft.component.exception.ComponentInternalException;
import com.foursoft.fourever.objectmodel.ComplexInstance;
import com.foursoft.fourever.objectmodel.Link;
import com.foursoft.fourever.objectmodel.SimpleInstance;
import com.foursoft.fourever.objectmodel.exception.TypeMismatchException;
import com.foursoft.fourever.vmodell.regular.Beschreibungstextersetzungen;
import com.foursoft.fourever.vmodell.regular.Kapitel;
import com.foursoft.fourever.vmodell.regular.KapiteltextErsetzen;

public class KapiteltextErsetzenImpl extends ModelElementImpl implements KapiteltextErsetzen {

    public KapiteltextErsetzenImpl(ComplexInstance ci) {
        super(ci);
    }

    public String getConsistent_to_version() {
        SimpleInstance si = null;
        try {
            si = (SimpleInstance) instance.getOutgoingLink("consistent_to_version").getFirstTarget();
        } catch (TypeMismatchException ex) {
            throw new ComponentInternalException("Unexpected schema - regenerate sources.", ex);
        }
        return (si == null) ? null : si.getValueAsString();
    }

    public Kapitel getKapitel() {
        ComplexInstance ci = null;
        try {
            ci = (ComplexInstance) instance.getOutgoingLink("KapitelRef").getFirstTarget();
        } catch (TypeMismatchException ex) {
            throw new ComponentInternalException("Unexpected schema - regenerate sources.", ex);
        }
        return (ci == null) ? null : new KapitelImpl(ci);
    }

    public String getNeuerText() {
        SimpleInstance si = null;
        try {
            si = (SimpleInstance) instance.getOutgoingLink("NeuerText").getFirstTarget();
        } catch (TypeMismatchException ex) {
            throw new ComponentInternalException("Unexpected schema - regenerate sources.", ex);
        }
        return (si == null) ? null : si.getValueAsString();
    }

    public Beschreibungstextersetzungen getParentBeschreibungstextersetzungen() {
        Set<Beschreibungstextersetzungen> resultSet = new HashSet<Beschreibungstextersetzungen>();
        for (Iterator<Link> lit = instance.getIncomingLinks("KapiteltextErsetzen"); lit.hasNext(); ) {
            ComplexInstance ci = lit.next().getSource();
            if (ci != null) {
                resultSet.add(new BeschreibungstextersetzungenImpl(ci));
            }
        }
        return (resultSet.size() > 0) ? resultSet.iterator().next() : null;
    }

    public String getRefers_to_id() {
        SimpleInstance si = null;
        try {
            si = (SimpleInstance) instance.getOutgoingLink("refers_to_id").getFirstTarget();
        } catch (TypeMismatchException ex) {
            throw new ComponentInternalException("Unexpected schema - regenerate sources.", ex);
        }
        return (si == null) ? null : si.getValueAsString();
    }
}
