package freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.textStudy.data.xmlBinding;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.Dispatcher;
import javax.xml.bind.DuplicateAttributeException;
import javax.xml.bind.Element;
import javax.xml.bind.InvalidAttributeException;
import javax.xml.bind.LocalValidationException;
import javax.xml.bind.MarshallableObject;
import javax.xml.bind.Marshaller;
import javax.xml.bind.MissingAttributeException;
import javax.xml.bind.StructureValidationException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Validator;
import javax.xml.marshal.XMLScanner;
import javax.xml.marshal.XMLWriter;

public class ViewElementLinkTarget extends MarshallableObject implements Element {

    private String _TargetLearningUnitViewManagerId;

    private String _TargetLearningUnitViewElementId;

    private String _TargetLearningUnitId;

    public String getTargetLearningUnitViewManagerId() {
        return _TargetLearningUnitViewManagerId;
    }

    public void setTargetLearningUnitViewManagerId(String _TargetLearningUnitViewManagerId) {
        this._TargetLearningUnitViewManagerId = _TargetLearningUnitViewManagerId;
        if (_TargetLearningUnitViewManagerId == null) {
            invalidate();
        }
    }

    public String getTargetLearningUnitViewElementId() {
        return _TargetLearningUnitViewElementId;
    }

    public void setTargetLearningUnitViewElementId(String _TargetLearningUnitViewElementId) {
        this._TargetLearningUnitViewElementId = _TargetLearningUnitViewElementId;
        if (_TargetLearningUnitViewElementId == null) {
            invalidate();
        }
    }

    public String getTargetLearningUnitId() {
        return _TargetLearningUnitId;
    }

    public void setTargetLearningUnitId(String _TargetLearningUnitId) {
        this._TargetLearningUnitId = _TargetLearningUnitId;
        if (_TargetLearningUnitId == null) {
            invalidate();
        }
    }

    public void validateThis() throws LocalValidationException {
        if (_TargetLearningUnitViewManagerId == null) {
            throw new MissingAttributeException("targetViewManagerId");
        }
        if (_TargetLearningUnitViewElementId == null) {
            throw new MissingAttributeException("targetViewElementId");
        }
        if (_TargetLearningUnitId == null) {
            throw new MissingAttributeException("targetLearningUnitId");
        }
    }

    public void validate(Validator v) throws StructureValidationException {
    }

    public void marshal(Marshaller m) throws IOException {
        XMLWriter w = m.writer();
        w.start("viewElementLinkTarget");
        w.attribute("targetViewManagerId", _TargetLearningUnitViewManagerId.toString());
        w.attribute("targetViewElementId", _TargetLearningUnitViewElementId.toString());
        w.attribute("targetLearningUnitId", _TargetLearningUnitId.toString());
        w.end("viewElementLinkTarget");
    }

    public void unmarshal(Unmarshaller u) throws UnmarshalException {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("viewElementLinkTarget");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            if (an.equals("targetViewManagerId")) {
                if (_TargetLearningUnitViewManagerId != null) {
                    throw new DuplicateAttributeException(an);
                }
                _TargetLearningUnitViewManagerId = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("targetViewElementId")) {
                if (_TargetLearningUnitViewElementId != null) {
                    throw new DuplicateAttributeException(an);
                }
                _TargetLearningUnitViewElementId = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("targetLearningUnitId")) {
                if (_TargetLearningUnitId != null) {
                    throw new DuplicateAttributeException(an);
                }
                _TargetLearningUnitId = xs.takeAttributeValue();
                continue;
            }
            throw new InvalidAttributeException(an);
        }
        xs.takeEnd("viewElementLinkTarget");
    }

    public static ViewElementLinkTarget unmarshal(InputStream in) throws UnmarshalException {
        return unmarshal(XMLScanner.open(in));
    }

    public static ViewElementLinkTarget unmarshal(XMLScanner xs) throws UnmarshalException {
        return unmarshal(xs, newDispatcher());
    }

    public static ViewElementLinkTarget unmarshal(XMLScanner xs, Dispatcher d) throws UnmarshalException {
        return ((ViewElementLinkTarget) d.unmarshal(xs, (ViewElementLinkTarget.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof ViewElementLinkTarget)) {
            return false;
        }
        ViewElementLinkTarget tob = ((ViewElementLinkTarget) ob);
        if (_TargetLearningUnitViewManagerId != null) {
            if (tob._TargetLearningUnitViewManagerId == null) {
                return false;
            }
            if (!_TargetLearningUnitViewManagerId.equals(tob._TargetLearningUnitViewManagerId)) {
                return false;
            }
        } else {
            if (tob._TargetLearningUnitViewManagerId != null) {
                return false;
            }
        }
        if (_TargetLearningUnitViewElementId != null) {
            if (tob._TargetLearningUnitViewElementId == null) {
                return false;
            }
            if (!_TargetLearningUnitViewElementId.equals(tob._TargetLearningUnitViewElementId)) {
                return false;
            }
        } else {
            if (tob._TargetLearningUnitViewElementId != null) {
                return false;
            }
        }
        if (_TargetLearningUnitId != null) {
            if (tob._TargetLearningUnitId == null) {
                return false;
            }
            if (!_TargetLearningUnitId.equals(tob._TargetLearningUnitId)) {
                return false;
            }
        } else {
            if (tob._TargetLearningUnitId != null) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int h = 0;
        h = ((127 * h) + ((_TargetLearningUnitViewManagerId != null) ? _TargetLearningUnitViewManagerId.hashCode() : 0));
        h = ((127 * h) + ((_TargetLearningUnitViewElementId != null) ? _TargetLearningUnitViewElementId.hashCode() : 0));
        h = ((127 * h) + ((_TargetLearningUnitId != null) ? _TargetLearningUnitId.hashCode() : 0));
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<viewElementLinkTarget");
        if (_TargetLearningUnitViewManagerId != null) {
            sb.append(" targetViewManagerId=");
            sb.append(_TargetLearningUnitViewManagerId.toString());
        }
        if (_TargetLearningUnitViewElementId != null) {
            sb.append(" targetViewElementId=");
            sb.append(_TargetLearningUnitViewElementId.toString());
        }
        if (_TargetLearningUnitId != null) {
            sb.append(" targetLearningUnitId=");
            sb.append(_TargetLearningUnitId.toString());
        }
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        return TextStudyDescriptor.newDispatcher();
    }
}
