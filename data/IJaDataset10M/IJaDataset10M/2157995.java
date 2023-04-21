package com.foursoft.fourever.vmodell.regular.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import com.foursoft.component.exception.ComponentInternalException;
import com.foursoft.fourever.objectmodel.ComplexInstance;
import com.foursoft.fourever.objectmodel.Link;
import com.foursoft.fourever.objectmodel.SimpleInstance;
import com.foursoft.fourever.objectmodel.exception.TypeMismatchException;
import com.foursoft.fourever.vmodell.regular.Aktivitaet;
import com.foursoft.fourever.vmodell.regular.Aktivitaetsknoten;
import com.foursoft.fourever.vmodell.regular.Textbaustein;
import com.foursoft.fourever.vmodell.regular.Unteraktivitaetsknoten;

public class UnteraktivitaetsknotenImpl extends ModelElementImpl implements Unteraktivitaetsknoten {

    public UnteraktivitaetsknotenImpl(ComplexInstance ci) {
        super(ci);
    }

    public Aktivitaet getAktivitaet() {
        ComplexInstance ci = null;
        try {
            ci = (ComplexInstance) instance.getOutgoingLink("Aktivit�tRef").getFirstTarget();
        } catch (TypeMismatchException ex) {
            throw new ComponentInternalException("Unexpected schema - regenerate sources.", ex);
        }
        return (ci == null) ? null : new AktivitaetImpl(ci);
    }

    public String getBeschreibung() {
        SimpleInstance si = null;
        try {
            si = (SimpleInstance) instance.getOutgoingLink("Beschreibung").getFirstTarget();
        } catch (TypeMismatchException ex) {
            throw new ComponentInternalException("Unexpected schema - regenerate sources.", ex);
        }
        return (si == null) ? null : si.getValueAsString();
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

    public Aktivitaet getEingebundeneAktivitaet() {
        ComplexInstance ci = null;
        try {
            ci = (ComplexInstance) instance.getOutgoingLink("EingebundeneAktivit�tRef").getFirstTarget();
        } catch (TypeMismatchException ex) {
            throw new ComponentInternalException("Unexpected schema - regenerate sources.", ex);
        }
        return (ci == null) ? null : new AktivitaetImpl(ci);
    }

    public Aktivitaetsknoten getParentAktivitaetsknoten() {
        Set<Aktivitaetsknoten> resultSet = new HashSet<Aktivitaetsknoten>();
        for (Iterator<Link> lit = instance.getIncomingLinks("Unteraktivit�tsknoten"); lit.hasNext(); ) {
            ComplexInstance ci = lit.next().getSource();
            if (ci != null) {
                resultSet.add(new AktivitaetsknotenImpl(ci));
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

    public Textbaustein getTextbaustein() {
        ComplexInstance ci = null;
        try {
            ci = (ComplexInstance) instance.getOutgoingLink("TextbausteinRef").getFirstTarget();
        } catch (TypeMismatchException ex) {
            throw new ComponentInternalException("Unexpected schema - regenerate sources.", ex);
        }
        return (ci == null) ? null : new TextbausteinImpl(ci);
    }
}
