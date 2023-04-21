package edu.mit.wi.omnigene.jaxb.bsml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.Dispatcher;
import javax.xml.bind.DuplicateAttributeException;
import javax.xml.bind.Element;
import javax.xml.bind.InvalidAttributeException;
import javax.xml.bind.InvalidContentObjectException;
import javax.xml.bind.LocalValidationException;
import javax.xml.bind.MarshallableObject;
import javax.xml.bind.Marshaller;
import javax.xml.bind.MissingAttributeException;
import javax.xml.bind.MissingContentException;
import javax.xml.bind.PredicatedLists;
import javax.xml.bind.PredicatedLists.Predicate;
import javax.xml.bind.StructureValidationException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidatableObject;
import javax.xml.bind.Validator;
import javax.xml.marshal.XMLScanner;
import javax.xml.marshal.XMLWriter;

public class AlignedGroups extends MarshallableObject implements Element {

    private String _Groups;

    private List _AlignedGroup = PredicatedLists.createInvalidating(this, new AlignedGroupPredicate(), new ArrayList());

    private PredicatedLists.Predicate pred_AlignedGroup = new AlignedGroupPredicate();

    private TotalAlignment _TotalAlignment;

    public String getGroups() {
        return _Groups;
    }

    public void setGroups(String _Groups) {
        this._Groups = _Groups;
        if (_Groups == null) {
            invalidate();
        }
    }

    public List getAlignedGroup() {
        return _AlignedGroup;
    }

    public void deleteAlignedGroup() {
        _AlignedGroup = null;
        invalidate();
    }

    public void emptyAlignedGroup() {
        _AlignedGroup = PredicatedLists.createInvalidating(this, pred_AlignedGroup, new ArrayList());
    }

    public TotalAlignment getTotalAlignment() {
        return _TotalAlignment;
    }

    public void setTotalAlignment(TotalAlignment _TotalAlignment) {
        this._TotalAlignment = _TotalAlignment;
        if (_TotalAlignment == null) {
            invalidate();
        }
    }

    public void validateThis() throws LocalValidationException {
        if (_Groups == null) {
            throw new MissingAttributeException("groups");
        }
        if (_TotalAlignment == null) {
            throw new MissingContentException("Total-alignment");
        }
    }

    public void validate(Validator v) throws StructureValidationException {
        if (_AlignedGroup != null) {
            for (Iterator i = _AlignedGroup.iterator(); i.hasNext(); ) {
                v.validate(((ValidatableObject) i.next()));
            }
        }
        if (_TotalAlignment != null) v.validate(_TotalAlignment);
    }

    public void marshal(Marshaller m) throws IOException {
        XMLWriter w = m.writer();
        w.start("Aligned-groups");
        w.attribute("groups", _Groups.toString());
        if (_AlignedGroup.size() > 0) {
            for (Iterator i = _AlignedGroup.iterator(); i.hasNext(); ) {
                m.marshal(((MarshallableObject) i.next()));
            }
        }
        m.marshal(_TotalAlignment);
        w.end("Aligned-groups");
    }

    public void unmarshal(Unmarshaller u) throws UnmarshalException {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("Aligned-groups");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            if (an.equals("groups")) {
                if (_Groups != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Groups = xs.takeAttributeValue();
                continue;
            }
            throw new InvalidAttributeException(an);
        }
        {
            List l = PredicatedLists.create(this, pred_AlignedGroup, new ArrayList());
            while (xs.atStart("Aligned-group")) {
                l.add(((AlignedGroup) u.unmarshal()));
            }
            _AlignedGroup = PredicatedLists.createInvalidating(this, pred_AlignedGroup, l);
        }
        _TotalAlignment = ((TotalAlignment) u.unmarshal());
        xs.takeEnd("Aligned-groups");
    }

    public static AlignedGroups unmarshal(InputStream in) throws UnmarshalException {
        return unmarshal(XMLScanner.open(in));
    }

    public static AlignedGroups unmarshal(XMLScanner xs) throws UnmarshalException {
        return unmarshal(xs, newDispatcher());
    }

    public static AlignedGroups unmarshal(XMLScanner xs, Dispatcher d) throws UnmarshalException {
        return ((AlignedGroups) d.unmarshal(xs, (AlignedGroups.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof AlignedGroups)) {
            return false;
        }
        AlignedGroups tob = ((AlignedGroups) ob);
        if (_Groups != null) {
            if (tob._Groups == null) {
                return false;
            }
            if (!_Groups.equals(tob._Groups)) {
                return false;
            }
        } else {
            if (tob._Groups != null) {
                return false;
            }
        }
        if (_AlignedGroup != null) {
            if (tob._AlignedGroup == null) {
                return false;
            }
            if (!_AlignedGroup.equals(tob._AlignedGroup)) {
                return false;
            }
        } else {
            if (tob._AlignedGroup != null) {
                return false;
            }
        }
        if (_TotalAlignment != null) {
            if (tob._TotalAlignment == null) {
                return false;
            }
            if (!_TotalAlignment.equals(tob._TotalAlignment)) {
                return false;
            }
        } else {
            if (tob._TotalAlignment != null) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int h = 0;
        h = ((127 * h) + ((_Groups != null) ? _Groups.hashCode() : 0));
        h = ((127 * h) + ((_AlignedGroup != null) ? _AlignedGroup.hashCode() : 0));
        h = ((127 * h) + ((_TotalAlignment != null) ? _TotalAlignment.hashCode() : 0));
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<Aligned-groups");
        if (_Groups != null) {
            sb.append(" groups=");
            sb.append(_Groups.toString());
        }
        if (_AlignedGroup != null) {
            sb.append(" Aligned-group=");
            sb.append(_AlignedGroup.toString());
        }
        if (_TotalAlignment != null) {
            sb.append(" Total-alignment=");
            sb.append(_TotalAlignment.toString());
        }
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        return Agent.newDispatcher();
    }

    private static class AlignedGroupPredicate implements PredicatedLists.Predicate {

        public void check(Object ob) {
            if (!(ob instanceof AlignedGroup)) {
                throw new InvalidContentObjectException(ob, (AlignedGroup.class));
            }
        }
    }
}
