package de.robowars.ui.config;

import de.robowars.ui.config.Layer;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.Dispatcher;
import javax.xml.bind.Element;
import javax.xml.bind.InvalidAttributeException;
import javax.xml.bind.InvalidContentObjectException;
import javax.xml.bind.LocalValidationException;
import javax.xml.bind.MarshallableObject;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PredicatedLists;
import javax.xml.bind.PredicatedLists.Predicate;
import javax.xml.bind.StructureValidationException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidatableObject;
import javax.xml.bind.Validator;
import javax.xml.marshal.XMLScanner;
import javax.xml.marshal.XMLWriter;

public class Layers extends MarshallableObject implements Element {

    private List _Layer = PredicatedLists.createInvalidating(this, new LayerPredicate(), new ArrayList());

    private PredicatedLists.Predicate pred_Layer = new LayerPredicate();

    public List getLayer() {
        return _Layer;
    }

    public void deleteLayer() {
        _Layer = null;
        invalidate();
    }

    public void emptyLayer() {
        _Layer = PredicatedLists.createInvalidating(this, pred_Layer, new ArrayList());
    }

    public void validateThis() throws LocalValidationException {
    }

    public void validate(Validator v) throws StructureValidationException {
        for (Iterator i = _Layer.iterator(); i.hasNext(); ) {
            v.validate(((ValidatableObject) i.next()));
        }
    }

    public void marshal(Marshaller m) throws IOException {
        XMLWriter w = m.writer();
        w.start("layers");
        if (_Layer.size() > 0) {
            for (Iterator i = _Layer.iterator(); i.hasNext(); ) {
                m.marshal(((MarshallableObject) i.next()));
            }
        }
        w.end("layers");
    }

    public void unmarshal(Unmarshaller u) throws UnmarshalException {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("layers");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            throw new InvalidAttributeException(an);
        }
        {
            List l = PredicatedLists.create(this, pred_Layer, new ArrayList());
            while (xs.atStart("layer")) {
                l.add(((Layer) u.unmarshal()));
            }
            _Layer = PredicatedLists.createInvalidating(this, pred_Layer, l);
        }
        xs.takeEnd("layers");
    }

    public static Layers unmarshal(InputStream in) throws UnmarshalException {
        return unmarshal(XMLScanner.open(in));
    }

    public static Layers unmarshal(XMLScanner xs) throws UnmarshalException {
        return unmarshal(xs, newDispatcher());
    }

    public static Layers unmarshal(XMLScanner xs, Dispatcher d) throws UnmarshalException {
        return ((Layers) d.unmarshal(xs, (Layers.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof Layers)) {
            return false;
        }
        Layers tob = ((Layers) ob);
        if (_Layer != null) {
            if (tob._Layer == null) {
                return false;
            }
            if (!_Layer.equals(tob._Layer)) {
                return false;
            }
        } else {
            if (tob._Layer != null) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int h = 0;
        h = ((127 * h) + ((_Layer != null) ? _Layer.hashCode() : 0));
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<layers");
        if (_Layer != null) {
            sb.append(" layer=");
            sb.append(_Layer.toString());
        }
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        return Client.newDispatcher();
    }

    private static class LayerPredicate implements PredicatedLists.Predicate {

        public void check(Object ob) {
            if (!(ob instanceof Layer)) {
                throw new InvalidContentObjectException(ob, (Layer.class));
            }
        }
    }
}
