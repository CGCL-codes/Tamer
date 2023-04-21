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
import com.foursoft.fourever.objectmodel.exception.TypeMismatchException;
import com.foursoft.fourever.vmodell.regular.Projekttyp;
import com.foursoft.fourever.vmodell.regular.SelektiverAusschluss;
import com.foursoft.fourever.vmodell.regular.Vortailoring;
import com.foursoft.fourever.vmodell.regular.Wert;

public class SelektiverAusschlussImpl extends ModelElementImpl implements SelektiverAusschluss {

    public SelektiverAusschlussImpl(ComplexInstance ci) {
        super(ci);
    }

    public List<Projekttyp> getAusschlusskontext() {
        List<Projekttyp> resultList = new Vector<Projekttyp>();
        try {
            Link link = instance.getOutgoingLink("Ausschlusskontext");
            if (link != null) {
                ComplexInstance interim = (ComplexInstance) link.getFirstTarget();
                if (interim != null) {
                    for (Iterator<Instance> it = interim.getOutgoingLink("ProjekttypRef").getTargets(); it.hasNext(); ) {
                        ComplexInstance ci = (ComplexInstance) it.next();
                        resultList.add(new ProjekttypImpl(ci));
                    }
                }
            }
        } catch (TypeMismatchException ex) {
            throw new ComponentInternalException("Unexpected schema - regenerate sources.", ex);
        }
        return resultList;
    }

    public Vortailoring getParentVortailoring() {
        Set<Vortailoring> resultSet = new HashSet<Vortailoring>();
        for (Iterator<Link> lit = instance.getIncomingLinks("SelektiverAusschluss"); lit.hasNext(); ) {
            ComplexInstance ci = lit.next().getSource();
            if (ci != null) {
                resultSet.add(new VortailoringImpl(ci));
            }
        }
        return (resultSet.size() > 0) ? resultSet.iterator().next() : null;
    }

    public List<Wert> getProjektmerkmalWerts() {
        List<Wert> resultList = new Vector<Wert>();
        try {
            Link link = instance.getOutgoingLink("ProjektmerkmalWertRef");
            if (link != null) {
                for (Iterator<Instance> it = link.getTargets(); it.hasNext(); ) {
                    ComplexInstance ci = (ComplexInstance) it.next();
                    resultList.add(new WertImpl(ci));
                }
            }
        } catch (TypeMismatchException ex) {
            throw new ComponentInternalException("Unexpected schema - regenerate sources.", ex);
        }
        return resultList;
    }
}
