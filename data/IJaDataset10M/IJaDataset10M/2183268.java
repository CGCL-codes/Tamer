package freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.textStudy.data.xmlBinding;

import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.textStudy.data.xmlBinding.ViewElementLinkTarget;
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
import javax.xml.bind.PredicatedLists;
import javax.xml.bind.PredicatedLists.Predicate;
import javax.xml.bind.StructureValidationException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidatableObject;
import javax.xml.bind.Validator;
import javax.xml.marshal.XMLScanner;
import javax.xml.marshal.XMLWriter;

public class ViewElementLink extends MarshallableObject implements Element {

    private String _Id;

    private List _LearningUnitViewElementLinkTargets = PredicatedLists.createInvalidating(this, new LearningUnitViewElementLinkTargetsPredicate(), new ArrayList());

    private PredicatedLists.Predicate pred_LearningUnitViewElementLinkTargets = new LearningUnitViewElementLinkTargetsPredicate();

    public String getId() {
        return _Id;
    }

    public void setId(String _Id) {
        this._Id = _Id;
        if (_Id == null) {
            invalidate();
        }
    }

    public List getLearningUnitViewElementLinkTargets() {
        return _LearningUnitViewElementLinkTargets;
    }

    public void deleteLearningUnitViewElementLinkTargets() {
        _LearningUnitViewElementLinkTargets = null;
        invalidate();
    }

    public void emptyLearningUnitViewElementLinkTargets() {
        _LearningUnitViewElementLinkTargets = PredicatedLists.createInvalidating(this, pred_LearningUnitViewElementLinkTargets, new ArrayList());
    }

    public void validateThis() throws LocalValidationException {
        if (_Id == null) {
            throw new MissingAttributeException("id");
        }
    }

    public void validate(Validator v) throws StructureValidationException {
        for (Iterator i = _LearningUnitViewElementLinkTargets.iterator(); i.hasNext(); ) {
            v.validate(((ValidatableObject) i.next()));
        }
    }

    public void marshal(Marshaller m) throws IOException {
        XMLWriter w = m.writer();
        w.start("viewElementLink");
        w.attribute("id", _Id.toString());
        if (_LearningUnitViewElementLinkTargets.size() > 0) {
            for (Iterator i = _LearningUnitViewElementLinkTargets.iterator(); i.hasNext(); ) {
                m.marshal(((MarshallableObject) i.next()));
            }
        }
        w.end("viewElementLink");
    }

    public void unmarshal(Unmarshaller u) throws UnmarshalException {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("viewElementLink");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            if (an.equals("id")) {
                if (_Id != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Id = xs.takeAttributeValue();
                continue;
            }
            throw new InvalidAttributeException(an);
        }
        {
            List l = PredicatedLists.create(this, pred_LearningUnitViewElementLinkTargets, new ArrayList());
            while (xs.atStart("viewElementLinkTarget")) {
                l.add(((ViewElementLinkTarget) u.unmarshal()));
            }
            _LearningUnitViewElementLinkTargets = PredicatedLists.createInvalidating(this, pred_LearningUnitViewElementLinkTargets, l);
        }
        xs.takeEnd("viewElementLink");
    }

    public static ViewElementLink unmarshal(InputStream in) throws UnmarshalException {
        return unmarshal(XMLScanner.open(in));
    }

    public static ViewElementLink unmarshal(XMLScanner xs) throws UnmarshalException {
        return unmarshal(xs, newDispatcher());
    }

    public static ViewElementLink unmarshal(XMLScanner xs, Dispatcher d) throws UnmarshalException {
        return ((ViewElementLink) d.unmarshal(xs, (ViewElementLink.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof ViewElementLink)) {
            return false;
        }
        ViewElementLink tob = ((ViewElementLink) ob);
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
        if (_LearningUnitViewElementLinkTargets != null) {
            if (tob._LearningUnitViewElementLinkTargets == null) {
                return false;
            }
            if (!_LearningUnitViewElementLinkTargets.equals(tob._LearningUnitViewElementLinkTargets)) {
                return false;
            }
        } else {
            if (tob._LearningUnitViewElementLinkTargets != null) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int h = 0;
        h = ((127 * h) + ((_Id != null) ? _Id.hashCode() : 0));
        h = ((127 * h) + ((_LearningUnitViewElementLinkTargets != null) ? _LearningUnitViewElementLinkTargets.hashCode() : 0));
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<viewElementLink");
        if (_Id != null) {
            sb.append(" id=");
            sb.append(_Id.toString());
        }
        if (_LearningUnitViewElementLinkTargets != null) {
            sb.append(" viewElementLinkTarget=");
            sb.append(_LearningUnitViewElementLinkTargets.toString());
        }
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        return TextStudyDescriptor.newDispatcher();
    }

    private static class LearningUnitViewElementLinkTargetsPredicate implements PredicatedLists.Predicate {

        public void check(Object ob) {
            if (!(ob instanceof ViewElementLinkTarget)) {
                throw new InvalidContentObjectException(ob, (ViewElementLinkTarget.class));
            }
        }
    }
}
