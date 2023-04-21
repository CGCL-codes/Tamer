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
import com.foursoft.fourever.vmodell.enhanced.Aenderungsoperationen;
import com.foursoft.fourever.vmodell.enhanced.MethodenreferenzEntfernen;
import com.foursoft.fourever.vmodell.enhanced.WerkzeugMethodenAenderungen;
import com.foursoft.fourever.vmodell.enhanced.WerkzeugreferenzEntfernen;

public class WerkzeugMethodenAenderungenImpl extends ModelElementImpl implements WerkzeugMethodenAenderungen {

    public WerkzeugMethodenAenderungenImpl(ComplexInstance ci) {
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

    public List<MethodenreferenzEntfernen> getMethodenreferenzEntfernens() {
        List<MethodenreferenzEntfernen> resultList = new Vector<MethodenreferenzEntfernen>();
        try {
            Link link = instance.getOutgoingLink("MethodenreferenzEntfernen");
            if (link != null) {
                for (Iterator<Instance> it = link.getTargets(); it.hasNext(); ) {
                    ComplexInstance ci = (ComplexInstance) it.next();
                    resultList.add(new MethodenreferenzEntfernenImpl(ci));
                }
            }
        } catch (TypeMismatchException ex) {
            throw new ComponentInternalException("Unexpected schema - regenerate sources.", ex);
        }
        return resultList;
    }

    public Aenderungsoperationen getParentAenderungsoperationen() {
        Set<Aenderungsoperationen> resultSet = new HashSet<Aenderungsoperationen>();
        for (Iterator<Link> lit = instance.getIncomingLinks("WerkzeugMethoden�nderungen"); lit.hasNext(); ) {
            ComplexInstance ci = lit.next().getSource();
            if (ci != null) {
                resultSet.add(new AenderungsoperationenImpl(ci));
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

    public List<WerkzeugreferenzEntfernen> getWerkzeugreferenzEntfernens() {
        List<WerkzeugreferenzEntfernen> resultList = new Vector<WerkzeugreferenzEntfernen>();
        try {
            Link link = instance.getOutgoingLink("WerkzeugreferenzEntfernen");
            if (link != null) {
                for (Iterator<Instance> it = link.getTargets(); it.hasNext(); ) {
                    ComplexInstance ci = (ComplexInstance) it.next();
                    resultList.add(new WerkzeugreferenzEntfernenImpl(ci));
                }
            }
        } catch (TypeMismatchException ex) {
            throw new ComponentInternalException("Unexpected schema - regenerate sources.", ex);
        }
        return resultList;
    }
}
