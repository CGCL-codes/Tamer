package edu.mit.wi.omnigene.jaxb.bsml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.Dispatcher;
import javax.xml.bind.DuplicateAttributeException;
import javax.xml.bind.Element;
import javax.xml.bind.FixedValueException;
import javax.xml.bind.IdentifiableElement;
import javax.xml.bind.InvalidAttributeException;
import javax.xml.bind.InvalidContentObjectException;
import javax.xml.bind.InvalidFixedValueException;
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

public class GroupLink extends MarshallableObject implements Element, IdentifiableElement {

    private String _Id;

    private String _XmlLink;

    private static final String FIXED_XMLLINK = String.valueOf("GROUP");

    private String _Steps;

    private String _Role;

    private String _Href;

    private String _Title;

    private String _Rel;

    private String _Rev;

    private String _Show;

    private boolean isDefaulted_Show = true;

    private static final String DEFAULT_SHOW = String.valueOf("embed");

    private String _Actuate;

    private boolean isDefaulted_Actuate = true;

    private static final String DEFAULT_ACTUATE = String.valueOf("user");

    private String _Behavior;

    private List _DocumentLink = PredicatedLists.createInvalidating(this, new DocumentLinkPredicate(), new ArrayList());

    private PredicatedLists.Predicate pred_DocumentLink = new DocumentLinkPredicate();

    public String getId() {
        return _Id;
    }

    public void setId(String _Id) {
        this._Id = _Id;
        if (_Id == null) {
            invalidate();
        }
    }

    public String id() {
        if (_Id == null) _Id = IDGenerator.getInstance().getID();
        return _Id.toString();
    }

    public String getXmlLink() {
        return _XmlLink;
    }

    public final void setXmlLink(String _XmlLink) {
        if ((_XmlLink != null) && (!_XmlLink.equals(FIXED_XMLLINK))) {
            throw new FixedValueException(_XmlLink.toString(), FIXED_XMLLINK.toString());
        }
        this._XmlLink = _XmlLink;
        if (_XmlLink == null) {
            invalidate();
        }
    }

    public String getSteps() {
        return _Steps;
    }

    public void setSteps(String _Steps) {
        this._Steps = _Steps;
        if (_Steps == null) {
            invalidate();
        }
    }

    public String getRole() {
        return _Role;
    }

    public void setRole(String _Role) {
        this._Role = _Role;
        if (_Role == null) {
            invalidate();
        }
    }

    public String getHref() {
        return _Href;
    }

    public void setHref(String _Href) {
        this._Href = _Href;
        if (_Href == null) {
            invalidate();
        }
    }

    public String getTitle() {
        return _Title;
    }

    public void setTitle(String _Title) {
        this._Title = _Title;
        if (_Title == null) {
            invalidate();
        }
    }

    public String getRel() {
        return _Rel;
    }

    public void setRel(String _Rel) {
        this._Rel = _Rel;
        if (_Rel == null) {
            invalidate();
        }
    }

    public String getRev() {
        return _Rev;
    }

    public void setRev(String _Rev) {
        this._Rev = _Rev;
        if (_Rev == null) {
            invalidate();
        }
    }

    public boolean defaultedShow() {
        return (_Show != null);
    }

    public String getShow() {
        if (_Show == null) {
            return DEFAULT_SHOW;
        }
        return _Show;
    }

    public void setShow(String _Show) {
        this._Show = _Show;
        if (_Show == null) {
            invalidate();
        }
    }

    public boolean defaultedActuate() {
        return (_Actuate != null);
    }

    public String getActuate() {
        if (_Actuate == null) {
            return DEFAULT_ACTUATE;
        }
        return _Actuate;
    }

    public void setActuate(String _Actuate) {
        this._Actuate = _Actuate;
        if (_Actuate == null) {
            invalidate();
        }
    }

    public String getBehavior() {
        return _Behavior;
    }

    public void setBehavior(String _Behavior) {
        this._Behavior = _Behavior;
        if (_Behavior == null) {
            invalidate();
        }
    }

    public List getDocumentLink() {
        return _DocumentLink;
    }

    public void deleteDocumentLink() {
        _DocumentLink = null;
        invalidate();
    }

    public void emptyDocumentLink() {
        _DocumentLink = PredicatedLists.createInvalidating(this, pred_DocumentLink, new ArrayList());
    }

    public void validateThis() throws LocalValidationException {
    }

    public void validate(Validator v) throws StructureValidationException {
        if (_DocumentLink != null) {
            for (Iterator i = _DocumentLink.iterator(); i.hasNext(); ) {
                v.validate(((ValidatableObject) i.next()));
            }
        }
    }

    public void marshal(Marshaller m) throws IOException {
        XMLWriter w = m.writer();
        w.start("Group-link");
        if (_Id != null) {
            w.attribute("id", _Id.toString());
        }
        if (_XmlLink != null) {
            w.attribute("xml-link", _XmlLink.toString());
        }
        if (_Steps != null) {
            w.attribute("steps", _Steps.toString());
        }
        if (_Role != null) {
            w.attribute("role", _Role.toString());
        }
        if (_Href != null) {
            w.attribute("href", _Href.toString());
        }
        if (_Title != null) {
            w.attribute("title", _Title.toString());
        }
        if (_Rel != null) {
            w.attribute("rel", _Rel.toString());
        }
        if (_Rev != null) {
            w.attribute("rev", _Rev.toString());
        }
        if (_Show != null) {
            w.attribute("show", _Show.toString());
        }
        if (_Actuate != null) {
            w.attribute("actuate", _Actuate.toString());
        }
        if (_Behavior != null) {
            w.attribute("behavior", _Behavior.toString());
        }
        if (_DocumentLink.size() > 0) {
            for (Iterator i = _DocumentLink.iterator(); i.hasNext(); ) {
                m.marshal(((MarshallableObject) i.next()));
            }
        }
        w.end("Group-link");
    }

    public void unmarshal(Unmarshaller u) throws UnmarshalException {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("Group-link");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            if (an.equals("id")) {
                if (_Id != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Id = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("xml-link")) {
                if (_XmlLink != null) {
                    throw new DuplicateAttributeException(an);
                }
                String s = xs.takeAttributeValue();
                if (!s.equals(FIXED_XMLLINK)) {
                    throw new InvalidFixedValueException(s, FIXED_XMLLINK);
                }
                _XmlLink = s;
                continue;
            }
            if (an.equals("steps")) {
                if (_Steps != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Steps = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("role")) {
                if (_Role != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Role = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("href")) {
                if (_Href != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Href = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("title")) {
                if (_Title != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Title = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("rel")) {
                if (_Rel != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Rel = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("rev")) {
                if (_Rev != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Rev = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("show")) {
                if (_Show != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Show = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("actuate")) {
                if (_Actuate != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Actuate = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("behavior")) {
                if (_Behavior != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Behavior = xs.takeAttributeValue();
                continue;
            }
            throw new InvalidAttributeException(an);
        }
        {
            List l = PredicatedLists.create(this, pred_DocumentLink, new ArrayList());
            while (xs.atStart("Document-link")) {
                l.add(((DocumentLink) u.unmarshal()));
            }
            _DocumentLink = PredicatedLists.createInvalidating(this, pred_DocumentLink, l);
        }
        xs.takeEnd("Group-link");
    }

    public static GroupLink unmarshal(InputStream in) throws UnmarshalException {
        return unmarshal(XMLScanner.open(in));
    }

    public static GroupLink unmarshal(XMLScanner xs) throws UnmarshalException {
        return unmarshal(xs, newDispatcher());
    }

    public static GroupLink unmarshal(XMLScanner xs, Dispatcher d) throws UnmarshalException {
        return ((GroupLink) d.unmarshal(xs, (GroupLink.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof GroupLink)) {
            return false;
        }
        GroupLink tob = ((GroupLink) ob);
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
        if (_XmlLink != null) {
            if (tob._XmlLink == null) {
                return false;
            }
            if (!_XmlLink.equals(tob._XmlLink)) {
                return false;
            }
        } else {
            if (tob._XmlLink != null) {
                return false;
            }
        }
        if (_Steps != null) {
            if (tob._Steps == null) {
                return false;
            }
            if (!_Steps.equals(tob._Steps)) {
                return false;
            }
        } else {
            if (tob._Steps != null) {
                return false;
            }
        }
        if (_Role != null) {
            if (tob._Role == null) {
                return false;
            }
            if (!_Role.equals(tob._Role)) {
                return false;
            }
        } else {
            if (tob._Role != null) {
                return false;
            }
        }
        if (_Href != null) {
            if (tob._Href == null) {
                return false;
            }
            if (!_Href.equals(tob._Href)) {
                return false;
            }
        } else {
            if (tob._Href != null) {
                return false;
            }
        }
        if (_Title != null) {
            if (tob._Title == null) {
                return false;
            }
            if (!_Title.equals(tob._Title)) {
                return false;
            }
        } else {
            if (tob._Title != null) {
                return false;
            }
        }
        if (_Rel != null) {
            if (tob._Rel == null) {
                return false;
            }
            if (!_Rel.equals(tob._Rel)) {
                return false;
            }
        } else {
            if (tob._Rel != null) {
                return false;
            }
        }
        if (_Rev != null) {
            if (tob._Rev == null) {
                return false;
            }
            if (!_Rev.equals(tob._Rev)) {
                return false;
            }
        } else {
            if (tob._Rev != null) {
                return false;
            }
        }
        if (_Show != null) {
            if (tob._Show == null) {
                return false;
            }
            if (!_Show.equals(tob._Show)) {
                return false;
            }
        } else {
            if (tob._Show != null) {
                return false;
            }
        }
        if (_Actuate != null) {
            if (tob._Actuate == null) {
                return false;
            }
            if (!_Actuate.equals(tob._Actuate)) {
                return false;
            }
        } else {
            if (tob._Actuate != null) {
                return false;
            }
        }
        if (_Behavior != null) {
            if (tob._Behavior == null) {
                return false;
            }
            if (!_Behavior.equals(tob._Behavior)) {
                return false;
            }
        } else {
            if (tob._Behavior != null) {
                return false;
            }
        }
        if (_DocumentLink != null) {
            if (tob._DocumentLink == null) {
                return false;
            }
            if (!_DocumentLink.equals(tob._DocumentLink)) {
                return false;
            }
        } else {
            if (tob._DocumentLink != null) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int h = 0;
        h = ((127 * h) + ((_Id != null) ? _Id.hashCode() : 0));
        h = ((127 * h) + ((_XmlLink != null) ? _XmlLink.hashCode() : 0));
        h = ((127 * h) + ((_Steps != null) ? _Steps.hashCode() : 0));
        h = ((127 * h) + ((_Role != null) ? _Role.hashCode() : 0));
        h = ((127 * h) + ((_Href != null) ? _Href.hashCode() : 0));
        h = ((127 * h) + ((_Title != null) ? _Title.hashCode() : 0));
        h = ((127 * h) + ((_Rel != null) ? _Rel.hashCode() : 0));
        h = ((127 * h) + ((_Rev != null) ? _Rev.hashCode() : 0));
        h = ((127 * h) + ((_Show != null) ? _Show.hashCode() : 0));
        h = ((127 * h) + ((_Actuate != null) ? _Actuate.hashCode() : 0));
        h = ((127 * h) + ((_Behavior != null) ? _Behavior.hashCode() : 0));
        h = ((127 * h) + ((_DocumentLink != null) ? _DocumentLink.hashCode() : 0));
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<Group-link");
        if (_Id != null) {
            sb.append(" id=");
            sb.append(_Id.toString());
        }
        if (_XmlLink != null) {
            sb.append(" xml-link=");
            sb.append(_XmlLink.toString());
        }
        if (_Steps != null) {
            sb.append(" steps=");
            sb.append(_Steps.toString());
        }
        if (_Role != null) {
            sb.append(" role=");
            sb.append(_Role.toString());
        }
        if (_Href != null) {
            sb.append(" href=");
            sb.append(_Href.toString());
        }
        if (_Title != null) {
            sb.append(" title=");
            sb.append(_Title.toString());
        }
        if (_Rel != null) {
            sb.append(" rel=");
            sb.append(_Rel.toString());
        }
        if (_Rev != null) {
            sb.append(" rev=");
            sb.append(_Rev.toString());
        }
        sb.append(" show=");
        sb.append(getShow().toString());
        sb.append(" actuate=");
        sb.append(getActuate().toString());
        if (_Behavior != null) {
            sb.append(" behavior=");
            sb.append(_Behavior.toString());
        }
        if (_DocumentLink != null) {
            sb.append(" Document-link=");
            sb.append(_DocumentLink.toString());
        }
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        return Agent.newDispatcher();
    }

    private static class DocumentLinkPredicate implements PredicatedLists.Predicate {

        public void check(Object ob) {
            if (!(ob instanceof DocumentLink)) {
                throw new InvalidContentObjectException(ob, (DocumentLink.class));
            }
        }
    }
}
