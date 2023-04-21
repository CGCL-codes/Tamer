package us.ga.atlanta.jell;

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

public class Warning extends MarshallableObject implements Element {

    private String _Type;

    private String _Number;

    private ClassRef _ClassRef;

    public String getType() {
        return _Type;
    }

    public void setType(String _Type) {
        this._Type = _Type;
        if (_Type == null) {
            invalidate();
        }
    }

    public String getNumber() {
        return _Number;
    }

    public void setNumber(String _Number) {
        this._Number = _Number;
        if (_Number == null) {
            invalidate();
        }
    }

    public ClassRef getClassRef() {
        return _ClassRef;
    }

    public void setClassRef(ClassRef _ClassRef) {
        this._ClassRef = _ClassRef;
        if (_ClassRef == null) {
            invalidate();
        }
    }

    public void validateThis() throws LocalValidationException {
        if (_Type == null) {
            throw new MissingAttributeException("Type");
        }
        if (_Number == null) {
            throw new MissingAttributeException("Number");
        }
    }

    public void validate(Validator v) throws StructureValidationException {
        v.validate(_ClassRef);
    }

    public void marshal(Marshaller m) throws IOException {
        XMLWriter w = m.writer();
        w.start("Warning");
        w.attribute("Type", _Type.toString());
        w.attribute("Number", _Number.toString());
        if (_ClassRef != null) {
            m.marshal(_ClassRef);
        }
        w.end("Warning");
    }

    public void unmarshal(Unmarshaller u) throws UnmarshalException {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("Warning");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            if (an.equals("Type")) {
                if (_Type != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Type = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("Number")) {
                if (_Number != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Number = xs.takeAttributeValue();
                continue;
            }
            throw new InvalidAttributeException(an);
        }
        if (xs.atStart("ClassRef")) {
            _ClassRef = ((ClassRef) u.unmarshal());
        }
        xs.takeEnd("Warning");
    }

    public static Warning unmarshal(InputStream in) throws UnmarshalException {
        return unmarshal(XMLScanner.open(in));
    }

    public static Warning unmarshal(XMLScanner xs) throws UnmarshalException {
        return unmarshal(xs, newDispatcher());
    }

    public static Warning unmarshal(XMLScanner xs, Dispatcher d) throws UnmarshalException {
        return ((Warning) d.unmarshal(xs, (Warning.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof Warning)) {
            return false;
        }
        Warning tob = ((Warning) ob);
        if (_Type != null) {
            if (tob._Type == null) {
                return false;
            }
            if (!_Type.equals(tob._Type)) {
                return false;
            }
        } else {
            if (tob._Type != null) {
                return false;
            }
        }
        if (_Number != null) {
            if (tob._Number == null) {
                return false;
            }
            if (!_Number.equals(tob._Number)) {
                return false;
            }
        } else {
            if (tob._Number != null) {
                return false;
            }
        }
        if (_ClassRef != null) {
            if (tob._ClassRef == null) {
                return false;
            }
            if (!_ClassRef.equals(tob._ClassRef)) {
                return false;
            }
        } else {
            if (tob._ClassRef != null) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int h = 0;
        h = ((127 * h) + ((_Type != null) ? _Type.hashCode() : 0));
        h = ((127 * h) + ((_Number != null) ? _Number.hashCode() : 0));
        h = ((127 * h) + ((_ClassRef != null) ? _ClassRef.hashCode() : 0));
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<Warning");
        if (_Type != null) {
            sb.append(" Type=");
            sb.append(_Type.toString());
        }
        if (_Number != null) {
            sb.append(" Number=");
            sb.append(_Number.toString());
        }
        if (_ClassRef != null) {
            sb.append(" ClassRef=");
            sb.append(_ClassRef.toString());
        }
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        return ClassRef.newDispatcher();
    }
}
