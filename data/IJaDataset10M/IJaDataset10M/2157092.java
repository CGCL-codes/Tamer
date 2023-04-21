package edu.mit.wi.omnigene.jaxb.bsml2_2;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.Dispatcher;
import javax.xml.bind.DuplicateAttributeException;
import javax.xml.bind.Element;
import javax.xml.bind.IdentifiableElement;
import javax.xml.bind.InvalidAttributeException;
import javax.xml.bind.InvalidContentObjectException;
import javax.xml.bind.LocalValidationException;
import javax.xml.bind.MarshallableObject;
import javax.xml.bind.Marshaller;
import javax.xml.bind.MissingAttributeException;
import javax.xml.bind.PredicatedLists;
import javax.xml.bind.PredicatedLists.Predicate;
import javax.xml.bind.StructureValidationException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidatableObject;
import javax.xml.bind.Validator;
import javax.xml.bind.Validator.Patcher;
import javax.xml.marshal.XMLScanner;
import javax.xml.marshal.XMLWriter;

public class SeqPairRun extends MarshallableObject implements Element {

    private IdentifiableElement _Id;

    private String _Translated;

    private String _Runlength;

    private String _Comprunlength;

    private String _Refpos;

    private String _Refcomplement;

    private String _Comppos;

    private String _Compcomplement;

    private String _Runscore;

    private String _Runprob;

    private String _Alignment;

    private List _Refs = null;

    private PredicatedLists.Predicate pred_Refs = new RefsPredicate();

    private List _Attribute = PredicatedLists.createInvalidating(this, new AttributePredicate(), new ArrayList());

    private PredicatedLists.Predicate pred_Attribute = new AttributePredicate();

    public IdentifiableElement getId() {
        return _Id;
    }

    public void setId(IdentifiableElement _Id) {
        this._Id = _Id;
        if (_Id == null) {
            invalidate();
        }
    }

    public String getTranslated() {
        return _Translated;
    }

    public void setTranslated(String _Translated) {
        this._Translated = _Translated;
        if (_Translated == null) {
            invalidate();
        }
    }

    public String getRunlength() {
        return _Runlength;
    }

    public void setRunlength(String _Runlength) {
        this._Runlength = _Runlength;
        if (_Runlength == null) {
            invalidate();
        }
    }

    public String getComprunlength() {
        return _Comprunlength;
    }

    public void setComprunlength(String _Comprunlength) {
        this._Comprunlength = _Comprunlength;
        if (_Comprunlength == null) {
            invalidate();
        }
    }

    public String getRefpos() {
        return _Refpos;
    }

    public void setRefpos(String _Refpos) {
        this._Refpos = _Refpos;
        if (_Refpos == null) {
            invalidate();
        }
    }

    public String getRefcomplement() {
        return _Refcomplement;
    }

    public void setRefcomplement(String _Refcomplement) {
        this._Refcomplement = _Refcomplement;
        if (_Refcomplement == null) {
            invalidate();
        }
    }

    public String getComppos() {
        return _Comppos;
    }

    public void setComppos(String _Comppos) {
        this._Comppos = _Comppos;
        if (_Comppos == null) {
            invalidate();
        }
    }

    public String getCompcomplement() {
        return _Compcomplement;
    }

    public void setCompcomplement(String _Compcomplement) {
        this._Compcomplement = _Compcomplement;
        if (_Compcomplement == null) {
            invalidate();
        }
    }

    public String getRunscore() {
        return _Runscore;
    }

    public void setRunscore(String _Runscore) {
        this._Runscore = _Runscore;
        if (_Runscore == null) {
            invalidate();
        }
    }

    public String getRunprob() {
        return _Runprob;
    }

    public void setRunprob(String _Runprob) {
        this._Runprob = _Runprob;
        if (_Runprob == null) {
            invalidate();
        }
    }

    public String getAlignment() {
        return _Alignment;
    }

    public void setAlignment(String _Alignment) {
        this._Alignment = _Alignment;
        if (_Alignment == null) {
            invalidate();
        }
    }

    public List getRefs() {
        return _Refs;
    }

    public void deleteRefs() {
        _Refs = null;
        invalidate();
    }

    public void emptyRefs() {
        _Refs = PredicatedLists.createInvalidating(this, pred_Refs, new ArrayList());
    }

    public List getAttribute() {
        return _Attribute;
    }

    public void deleteAttribute() {
        _Attribute = null;
        invalidate();
    }

    public void emptyAttribute() {
        _Attribute = PredicatedLists.createInvalidating(this, pred_Attribute, new ArrayList());
    }

    public void validateThis() throws LocalValidationException {
        if (_Runlength == null) {
            throw new MissingAttributeException("runlength");
        }
        if (_Refpos == null) {
            throw new MissingAttributeException("refpos");
        }
        if (_Comppos == null) {
            throw new MissingAttributeException("comppos");
        }
    }

    public void validate(Validator v) throws StructureValidationException {
        if (_Id != null) v.reference(_Id);
        if (_Refs != null) {
            for (Iterator i = _Refs.iterator(); i.hasNext(); ) {
                v.reference(((IdentifiableElement) i.next()));
            }
        }
        if (_Attribute != null) {
            for (Iterator i = _Attribute.iterator(); i.hasNext(); ) {
                v.validate(((ValidatableObject) i.next()));
            }
        }
    }

    public void marshal(Marshaller m) throws IOException {
        XMLWriter w = m.writer();
        w.start("Seq-pair-run");
        if (_Id != null) {
            w.attribute("id", _Id.id());
        }
        if (_Translated != null) {
            w.attribute("translated", _Translated.toString());
        }
        w.attribute("runlength", _Runlength.toString());
        if (_Comprunlength != null) {
            w.attribute("comprunlength", _Comprunlength.toString());
        }
        w.attribute("refpos", _Refpos.toString());
        if (_Refcomplement != null) {
            w.attribute("refcomplement", _Refcomplement.toString());
        }
        w.attribute("comppos", _Comppos.toString());
        if (_Compcomplement != null) {
            w.attribute("compcomplement", _Compcomplement.toString());
        }
        if (_Runscore != null) {
            w.attribute("runscore", _Runscore.toString());
        }
        if (_Runprob != null) {
            w.attribute("runprob", _Runprob.toString());
        }
        if (_Alignment != null) {
            w.attribute("alignment", _Alignment.toString());
        }
        if (_Refs != null) {
            w.attributeName("refs");
            for (Iterator i = _Refs.iterator(); i.hasNext(); ) {
                w.attributeValueToken(((IdentifiableElement) i.next()).id());
            }
        }
        if (_Attribute.size() > 0) {
            for (Iterator i = _Attribute.iterator(); i.hasNext(); ) {
                m.marshal(((MarshallableObject) i.next()));
            }
        }
        w.end("Seq-pair-run");
    }

    public void unmarshal(Unmarshaller u) throws UnmarshalException {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("Seq-pair-run");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            if (an.equals("id")) {
                if (_Id != null) {
                    throw new DuplicateAttributeException(an);
                }
                v.reference(xs.takeAttributeValue(), new IdVPatcher());
                continue;
            }
            if (an.equals("translated")) {
                if (_Translated != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Translated = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("runlength")) {
                if (_Runlength != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Runlength = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("comprunlength")) {
                if (_Comprunlength != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Comprunlength = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("refpos")) {
                if (_Refpos != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Refpos = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("refcomplement")) {
                if (_Refcomplement != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Refcomplement = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("comppos")) {
                if (_Comppos != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Comppos = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("compcomplement")) {
                if (_Compcomplement != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Compcomplement = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("runscore")) {
                if (_Runscore != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Runscore = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("runprob")) {
                if (_Runprob != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Runprob = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("alignment")) {
                if (_Alignment != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Alignment = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("refs")) {
                if (_Refs != null) {
                    throw new DuplicateAttributeException(an);
                }
                ArrayList l = new ArrayList();
                xs.tokenizeAttributeValue();
                while (xs.atAttributeValueToken()) {
                    l.add(String.valueOf(xs.takeAttributeValueToken()));
                }
                _Refs = PredicatedLists.createInvalidating(this, pred_Refs, l);
                continue;
            }
            throw new InvalidAttributeException(an);
        }
        {
            List l = PredicatedLists.create(this, pred_Attribute, new ArrayList());
            while (xs.atStart("Attribute")) {
                l.add(((Attribute) u.unmarshal()));
            }
            _Attribute = PredicatedLists.createInvalidating(this, pred_Attribute, l);
        }
        xs.takeEnd("Seq-pair-run");
    }

    public static SeqPairRun unmarshal(InputStream in) throws UnmarshalException {
        return unmarshal(XMLScanner.open(in));
    }

    public static SeqPairRun unmarshal(XMLScanner xs) throws UnmarshalException {
        return unmarshal(xs, newDispatcher());
    }

    public static SeqPairRun unmarshal(XMLScanner xs, Dispatcher d) throws UnmarshalException {
        return ((SeqPairRun) d.unmarshal(xs, (SeqPairRun.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof SeqPairRun)) {
            return false;
        }
        SeqPairRun tob = ((SeqPairRun) ob);
        if (_Id != null) {
            if (tob._Id == null) {
                return false;
            }
            if (!_Id.equals(tob._Id)) {
                return false;
            }
        } else {
            if (tob._Id != null) {
                return false;
            }
        }
        if (_Translated != null) {
            if (tob._Translated == null) {
                return false;
            }
            if (!_Translated.equals(tob._Translated)) {
                return false;
            }
        } else {
            if (tob._Translated != null) {
                return false;
            }
        }
        if (_Runlength != null) {
            if (tob._Runlength == null) {
                return false;
            }
            if (!_Runlength.equals(tob._Runlength)) {
                return false;
            }
        } else {
            if (tob._Runlength != null) {
                return false;
            }
        }
        if (_Comprunlength != null) {
            if (tob._Comprunlength == null) {
                return false;
            }
            if (!_Comprunlength.equals(tob._Comprunlength)) {
                return false;
            }
        } else {
            if (tob._Comprunlength != null) {
                return false;
            }
        }
        if (_Refpos != null) {
            if (tob._Refpos == null) {
                return false;
            }
            if (!_Refpos.equals(tob._Refpos)) {
                return false;
            }
        } else {
            if (tob._Refpos != null) {
                return false;
            }
        }
        if (_Refcomplement != null) {
            if (tob._Refcomplement == null) {
                return false;
            }
            if (!_Refcomplement.equals(tob._Refcomplement)) {
                return false;
            }
        } else {
            if (tob._Refcomplement != null) {
                return false;
            }
        }
        if (_Comppos != null) {
            if (tob._Comppos == null) {
                return false;
            }
            if (!_Comppos.equals(tob._Comppos)) {
                return false;
            }
        } else {
            if (tob._Comppos != null) {
                return false;
            }
        }
        if (_Compcomplement != null) {
            if (tob._Compcomplement == null) {
                return false;
            }
            if (!_Compcomplement.equals(tob._Compcomplement)) {
                return false;
            }
        } else {
            if (tob._Compcomplement != null) {
                return false;
            }
        }
        if (_Runscore != null) {
            if (tob._Runscore == null) {
                return false;
            }
            if (!_Runscore.equals(tob._Runscore)) {
                return false;
            }
        } else {
            if (tob._Runscore != null) {
                return false;
            }
        }
        if (_Runprob != null) {
            if (tob._Runprob == null) {
                return false;
            }
            if (!_Runprob.equals(tob._Runprob)) {
                return false;
            }
        } else {
            if (tob._Runprob != null) {
                return false;
            }
        }
        if (_Alignment != null) {
            if (tob._Alignment == null) {
                return false;
            }
            if (!_Alignment.equals(tob._Alignment)) {
                return false;
            }
        } else {
            if (tob._Alignment != null) {
                return false;
            }
        }
        if (_Refs != null) {
            if (tob._Refs == null) {
                return false;
            }
            if (!_Refs.equals(tob._Refs)) {
                return false;
            }
        } else {
            if (tob._Refs != null) {
                return false;
            }
        }
        if (_Attribute != null) {
            if (tob._Attribute == null) {
                return false;
            }
            if (!_Attribute.equals(tob._Attribute)) {
                return false;
            }
        } else {
            if (tob._Attribute != null) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int h = 0;
        h = ((179 * h) + ((_Id != null) ? _Id.id().hashCode() : 0));
        h = ((127 * h) + ((_Translated != null) ? _Translated.hashCode() : 0));
        h = ((127 * h) + ((_Runlength != null) ? _Runlength.hashCode() : 0));
        h = ((127 * h) + ((_Comprunlength != null) ? _Comprunlength.hashCode() : 0));
        h = ((127 * h) + ((_Refpos != null) ? _Refpos.hashCode() : 0));
        h = ((127 * h) + ((_Refcomplement != null) ? _Refcomplement.hashCode() : 0));
        h = ((127 * h) + ((_Comppos != null) ? _Comppos.hashCode() : 0));
        h = ((127 * h) + ((_Compcomplement != null) ? _Compcomplement.hashCode() : 0));
        h = ((127 * h) + ((_Runscore != null) ? _Runscore.hashCode() : 0));
        h = ((127 * h) + ((_Runprob != null) ? _Runprob.hashCode() : 0));
        h = ((127 * h) + ((_Alignment != null) ? _Alignment.hashCode() : 0));
        h = ((127 * h) + ((_Refs != null) ? _Refs.hashCode() : 0));
        h = ((127 * h) + ((_Attribute != null) ? _Attribute.hashCode() : 0));
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<Seq-pair-run");
        if (_Id != null) {
            sb.append(" id=");
            sb.append(_Id.id());
        }
        if (_Translated != null) {
            sb.append(" translated=");
            sb.append(_Translated.toString());
        }
        if (_Runlength != null) {
            sb.append(" runlength=");
            sb.append(_Runlength.toString());
        }
        if (_Comprunlength != null) {
            sb.append(" comprunlength=");
            sb.append(_Comprunlength.toString());
        }
        if (_Refpos != null) {
            sb.append(" refpos=");
            sb.append(_Refpos.toString());
        }
        if (_Refcomplement != null) {
            sb.append(" refcomplement=");
            sb.append(_Refcomplement.toString());
        }
        if (_Comppos != null) {
            sb.append(" comppos=");
            sb.append(_Comppos.toString());
        }
        if (_Compcomplement != null) {
            sb.append(" compcomplement=");
            sb.append(_Compcomplement.toString());
        }
        if (_Runscore != null) {
            sb.append(" runscore=");
            sb.append(_Runscore.toString());
        }
        if (_Runprob != null) {
            sb.append(" runprob=");
            sb.append(_Runprob.toString());
        }
        if (_Alignment != null) {
            sb.append(" alignment=");
            sb.append(_Alignment.toString());
        }
        if ((_Refs != null) && (_Refs.size() > 0)) {
            sb.append(" refs=");
            sb.append(((IdentifiableElement) _Refs.get(0)).id());
        }
        if (_Attribute != null) {
            sb.append(" Attribute=");
            sb.append(_Attribute.toString());
        }
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        return AlignmentPointSet.newDispatcher();
    }

    private static class RefsPredicate implements PredicatedLists.Predicate {

        public void check(Object ob) {
            if (!(ob instanceof IdentifiableElement)) {
                throw new InvalidContentObjectException(ob, (IdentifiableElement.class));
            }
        }
    }

    private static class AttributePredicate implements PredicatedLists.Predicate {

        public void check(Object ob) {
            if (!(ob instanceof Attribute)) {
                throw new InvalidContentObjectException(ob, (Attribute.class));
            }
        }
    }

    private class IdVPatcher extends Validator.Patcher {

        public void patch(IdentifiableElement target) {
            _Id = target;
        }
    }
}
