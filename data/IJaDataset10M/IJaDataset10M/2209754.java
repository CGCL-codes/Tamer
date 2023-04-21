package com.foursoft.fourever.vmodell.regular.impl;

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
import com.foursoft.fourever.vmodell.regular.Begriffsabbildung;
import com.foursoft.fourever.vmodell.regular.Methodenreferenz;
import com.foursoft.fourever.vmodell.regular.ModelElement;
import com.foursoft.fourever.vmodell.regular.Quelle;
import com.foursoft.fourever.vmodell.regular.V_Modellvariante;

public class MethodenreferenzImpl extends ModelElementImpl implements Methodenreferenz {

    public MethodenreferenzImpl(ComplexInstance ci) {
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

    public V_Modellvariante getParentV_Modellvariante() {
        Set<V_Modellvariante> resultSet = new HashSet<V_Modellvariante>();
        for (Iterator<Link> lit = instance.getIncomingLinks("Methodenreferenz"); lit.hasNext(); ) {
            ComplexInstance ci = lit.next().getSource();
            if (ci != null) {
                for (Iterator<Link> lit2 = ci.getIncomingLinks("Methodenreferenzen"); lit2.hasNext(); ) {
                    ComplexInstance ci2 = lit2.next().getSource();
                    if (ci2 != null) {
                        resultSet.add(new V_ModellvarianteImpl(ci2));
                    }
                }
            }
        }
        return (resultSet.size() > 0) ? resultSet.iterator().next() : null;
    }

    public List<Quelle> getQuelles() {
        List<Quelle> resultList = new Vector<Quelle>();
        try {
            Link link = instance.getOutgoingLink("QuelleRef");
            if (link != null) {
                for (Iterator<Instance> it = link.getTargets(); it.hasNext(); ) {
                    ComplexInstance ci = (ComplexInstance) it.next();
                    resultList.add(new QuelleImpl(ci));
                }
            }
        } catch (TypeMismatchException ex) {
            throw new ComponentInternalException("Unexpected schema - regenerate sources.", ex);
        }
        return resultList;
    }

    public Set<Begriffsabbildung> getReferringBegriffsabbildungs() {
        Set<Begriffsabbildung> resultSet = new HashSet<Begriffsabbildung>();
        for (Iterator<Link> lit = instance.getIncomingLinks("wird_abgebildet_durchMethodenreferenzRef"); lit.hasNext(); ) {
            ComplexInstance ci = lit.next().getSource();
            if (ci != null) {
                resultSet.add(new BegriffsabbildungImpl(ci));
            }
        }
        return resultSet;
    }

    public Set<ModelElement> getReferringModelElementsViaMethodenreferenz() {
        Set<ModelElement> resultSet = new HashSet<ModelElement>();
        for (Iterator<Link> lit = instance.getIncomingLinks("MethodenreferenzRef"); lit.hasNext(); ) {
            ComplexInstance ci = lit.next().getSource();
            if (ci != null) {
                resultSet.add(new ModelElementImpl(ci));
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

    public String getSinn_und_Zweck() {
        SimpleInstance si = null;
        try {
            si = (SimpleInstance) instance.getOutgoingLink("Sinn_und_Zweck").getFirstTarget();
        } catch (TypeMismatchException ex) {
            throw new ComponentInternalException("Unexpected schema - regenerate sources.", ex);
        }
        return (si == null) ? null : si.getValueAsString();
    }
}
