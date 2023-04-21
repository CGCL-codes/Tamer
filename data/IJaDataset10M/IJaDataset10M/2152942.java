package com.foursoft.fourever.vmodell.regular.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import com.foursoft.component.exception.ComponentInternalException;
import com.foursoft.fourever.objectmodel.ComplexInstance;
import com.foursoft.fourever.objectmodel.Link;
import com.foursoft.fourever.objectmodel.SimpleInstance;
import com.foursoft.fourever.objectmodel.exception.TypeMismatchException;
import com.foursoft.fourever.vmodell.regular.Ablaufbausteinspezifikation;
import com.foursoft.fourever.vmodell.regular.PDS_Spezifikation;
import com.foursoft.fourever.vmodell.regular.V_Modellvariante;

public class PDS_SpezifikationImpl extends ModelElementImpl implements PDS_Spezifikation {

    public PDS_SpezifikationImpl(ComplexInstance ci) {
        super(ci);
    }

    public Ablaufbausteinspezifikation getAblaufbausteinspezifikation() {
        ComplexInstance ci = null;
        try {
            ci = (ComplexInstance) instance.getOutgoingLink("AblaufbausteinspezifikationRef").getFirstTarget();
        } catch (TypeMismatchException ex) {
            throw new ComponentInternalException("Unexpected schema - regenerate sources.", ex);
        }
        return (ci == null) ? null : new AblaufbausteinspezifikationImpl(ci);
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
        for (Iterator<Link> lit = instance.getIncomingLinks("PDS-Spezifikation"); lit.hasNext(); ) {
            ComplexInstance ci = lit.next().getSource();
            if (ci != null) {
                for (Iterator<Link> lit2 = ci.getIncomingLinks("PDS-Spezifikationen"); lit2.hasNext(); ) {
                    ComplexInstance ci2 = lit2.next().getSource();
                    if (ci2 != null) {
                        resultSet.add(new V_ModellvarianteImpl(ci2));
                    }
                }
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
