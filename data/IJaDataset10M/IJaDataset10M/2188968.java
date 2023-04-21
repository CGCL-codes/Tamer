package com.foursoft.fourever.vmodell.enhanced.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import com.foursoft.component.exception.ComponentInternalException;
import com.foursoft.fourever.objectmodel.ComplexInstance;
import com.foursoft.fourever.objectmodel.Link;
import com.foursoft.fourever.objectmodel.SimpleInstance;
import com.foursoft.fourever.objectmodel.exception.TypeMismatchException;
import com.foursoft.fourever.vmodell.enhanced.Produkt;
import com.foursoft.fourever.vmodell.enhanced.Produktaenderungen;
import com.foursoft.fourever.vmodell.enhanced.ProdukttextVoranstellen;

public class ProdukttextVoranstellenImpl extends ModelElementImpl implements ProdukttextVoranstellen {

    public ProdukttextVoranstellenImpl(ComplexInstance ci) {
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

    public Produktaenderungen getParentProduktaenderungen() {
        Set<Produktaenderungen> resultSet = new HashSet<Produktaenderungen>();
        for (Iterator<Link> lit = instance.getIncomingLinks("ProdukttextVoranstellen"); lit.hasNext(); ) {
            ComplexInstance ci = lit.next().getSource();
            if (ci != null) {
                resultSet.add(new ProduktaenderungenImpl(ci));
            }
        }
        return (resultSet.size() > 0) ? resultSet.iterator().next() : null;
    }

    public Produkt getProdukt() {
        ComplexInstance ci = null;
        try {
            ci = (ComplexInstance) instance.getOutgoingLink("ProduktRef").getFirstTarget();
        } catch (TypeMismatchException ex) {
            throw new ComponentInternalException("Unexpected schema - regenerate sources.", ex);
        }
        return (ci == null) ? null : new ProduktImpl(ci);
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

    public String getZuErgaenzenderText() {
        SimpleInstance si = null;
        try {
            si = (SimpleInstance) instance.getOutgoingLink("ZuErg�nzenderText").getFirstTarget();
        } catch (TypeMismatchException ex) {
            throw new ComponentInternalException("Unexpected schema - regenerate sources.", ex);
        }
        return (si == null) ? null : si.getValueAsString();
    }
}
