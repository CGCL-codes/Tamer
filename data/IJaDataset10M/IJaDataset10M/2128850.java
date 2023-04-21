package com.foursoft.fourever.vmodell.enhanced.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import com.foursoft.component.exception.ComponentInternalException;
import com.foursoft.fourever.objectmodel.ComplexInstance;
import com.foursoft.fourever.objectmodel.Link;
import com.foursoft.fourever.objectmodel.SimpleInstance;
import com.foursoft.fourever.objectmodel.exception.TypeMismatchException;
import com.foursoft.fourever.vmodell.enhanced.Beschreibungstextverschiebungen;
import com.foursoft.fourever.vmodell.enhanced.Kapitel;
import com.foursoft.fourever.vmodell.enhanced.KapitelEinordnen;
import com.foursoft.fourever.vmodell.enhanced.V_Modell_Teil;

public class KapitelEinordnenImpl extends ModelElementImpl implements KapitelEinordnen {

    public KapitelEinordnenImpl(ComplexInstance ci) {
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

    public Beschreibungstextverschiebungen getParentBeschreibungstextverschiebungen() {
        Set<Beschreibungstextverschiebungen> resultSet = new HashSet<Beschreibungstextverschiebungen>();
        for (Iterator<Link> lit = instance.getIncomingLinks("KapitelEinordnen"); lit.hasNext(); ) {
            ComplexInstance ci = lit.next().getSource();
            if (ci != null) {
                resultSet.add(new BeschreibungstextverschiebungenImpl(ci));
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

    public V_Modell_Teil getTeil() {
        ComplexInstance ci = null;
        try {
            ci = (ComplexInstance) instance.getOutgoingLink("TeilRef").getFirstTarget();
        } catch (TypeMismatchException ex) {
            throw new ComponentInternalException("Unexpected schema - regenerate sources.", ex);
        }
        return (ci == null) ? null : new V_Modell_TeilImpl(ci);
    }
}
