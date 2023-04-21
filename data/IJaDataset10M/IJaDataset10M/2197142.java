package com.foursoft.fourever.vmodell.enhanced.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import com.foursoft.component.exception.ComponentInternalException;
import com.foursoft.fourever.objectmodel.ComplexInstance;
import com.foursoft.fourever.objectmodel.Instance;
import com.foursoft.fourever.objectmodel.Link;
import com.foursoft.fourever.objectmodel.SimpleInstance;
import com.foursoft.fourever.objectmodel.exception.TypeMismatchException;
import com.foursoft.fourever.vmodell.enhanced.Begriffsabbildung;
import com.foursoft.fourever.vmodell.enhanced.Kapitel;
import com.foursoft.fourever.vmodell.enhanced.KapitelEinordnen;
import com.foursoft.fourever.vmodell.enhanced.V_Modell_Teil;
import com.foursoft.fourever.vmodell.enhanced.V_Modellvariante;

public class V_Modell_TeilImpl extends ModelElementImpl implements V_Modell_Teil {

    public V_Modell_TeilImpl(ComplexInstance ci) {
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

    public List<Kapitel> getKapitels() {
        List<Kapitel> resultList = new Vector<Kapitel>();
        try {
            Link link = instance.getOutgoingLink("Kapitel");
            if (link != null) {
                for (Iterator<Instance> it = link.getTargets(); it.hasNext(); ) {
                    ComplexInstance ci = (ComplexInstance) it.next();
                    resultList.add(new KapitelImpl(ci));
                }
            }
        } catch (TypeMismatchException ex) {
            throw new ComponentInternalException("Unexpected schema - regenerate sources.", ex);
        }
        return resultList;
    }

    public String getNummer() {
        SimpleInstance si = null;
        try {
            si = (SimpleInstance) instance.getOutgoingLink("Nummer").getFirstTarget();
        } catch (TypeMismatchException ex) {
            throw new ComponentInternalException("Unexpected schema - regenerate sources.", ex);
        }
        return (si == null) ? null : si.getValueAsString();
    }

    public V_Modellvariante getParentV_Modellvariante() {
        Set<V_Modellvariante> resultSet = new HashSet<V_Modellvariante>();
        for (Iterator<Link> lit = instance.getIncomingLinks("V-Modell-Teil"); lit.hasNext(); ) {
            ComplexInstance ci = lit.next().getSource();
            if (ci != null) {
                for (Iterator<Link> lit2 = ci.getIncomingLinks("V-Modell-Struktur"); lit2.hasNext(); ) {
                    ComplexInstance ci2 = lit2.next().getSource();
                    if (ci2 != null) {
                        resultSet.add(new V_ModellvarianteImpl(ci2));
                    }
                }
            }
        }
        return (resultSet.size() > 0) ? resultSet.iterator().next() : null;
    }

    public Set<Begriffsabbildung> getReferringBegriffsabbildungs() {
        Set<Begriffsabbildung> resultSet = new HashSet<Begriffsabbildung>();
        for (Iterator<Link> lit = instance.getIncomingLinks("wird_abgebildet_durchTeilRef"); lit.hasNext(); ) {
            ComplexInstance ci = lit.next().getSource();
            if (ci != null) {
                resultSet.add(new BegriffsabbildungImpl(ci));
            }
        }
        return resultSet;
    }

    public Set<KapitelEinordnen> getReferringKapitelEinordnens() {
        Set<KapitelEinordnen> resultSet = new HashSet<KapitelEinordnen>();
        for (Iterator<Link> lit = instance.getIncomingLinks("TeilRef"); lit.hasNext(); ) {
            ComplexInstance ci = lit.next().getSource();
            if (ci != null) {
                resultSet.add(new KapitelEinordnenImpl(ci));
            }
        }
        return resultSet;
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
