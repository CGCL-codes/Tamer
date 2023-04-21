package edu.mit.wi.omnigene.jaxb.das.style;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.ConversionException;
import javax.xml.bind.Dispatcher;
import javax.xml.bind.Element;
import javax.xml.bind.InvalidAttributeException;
import javax.xml.bind.LocalValidationException;
import javax.xml.bind.MarshallableObject;
import javax.xml.bind.Marshaller;
import javax.xml.bind.StructureValidationException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Validator;
import javax.xml.marshal.XMLScanner;
import javax.xml.marshal.XMLWriter;

public class LINE extends MarshallableObject implements Element {

    private String _WIDTH;

    private String _COLOR;

    public String getWIDTH() {
        return _WIDTH;
    }

    public void setWIDTH(String _WIDTH) {
        this._WIDTH = _WIDTH;
        if (_WIDTH == null) {
            invalidate();
        }
    }

    public String getCOLOR() {
        return _COLOR;
    }

    public void setCOLOR(String _COLOR) {
        this._COLOR = _COLOR;
        if (_COLOR == null) {
            invalidate();
        }
    }

    public void validateThis() throws LocalValidationException {
    }

    public void validate(Validator v) throws StructureValidationException {
    }

    public void marshal(Marshaller m) throws IOException {
        XMLWriter w = m.writer();
        w.start("LINE");
        if (_WIDTH != null) {
            w.leaf("WIDTH", _WIDTH.toString());
        }
        if (_COLOR != null) {
            w.leaf("COLOR", _COLOR.toString());
        }
        w.end("LINE");
    }

    public void unmarshal(Unmarshaller u) throws UnmarshalException {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("LINE");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            throw new InvalidAttributeException(an);
        }
        if (xs.atStart("WIDTH")) {
            xs.takeStart("WIDTH");
            String s;
            if (xs.atChars(XMLScanner.WS_COLLAPSE)) {
                s = xs.takeChars(XMLScanner.WS_COLLAPSE);
            } else {
                s = "";
            }
            try {
                _WIDTH = String.valueOf(s);
            } catch (Exception x) {
                throw new ConversionException("WIDTH", x);
            }
            xs.takeEnd("WIDTH");
        }
        if (xs.atStart("COLOR")) {
            xs.takeStart("COLOR");
            String s;
            if (xs.atChars(XMLScanner.WS_COLLAPSE)) {
                s = xs.takeChars(XMLScanner.WS_COLLAPSE);
            } else {
                s = "";
            }
            try {
                _COLOR = String.valueOf(s);
            } catch (Exception x) {
                throw new ConversionException("COLOR", x);
            }
            xs.takeEnd("COLOR");
        }
        xs.takeEnd("LINE");
    }

    public static LINE unmarshal(InputStream in) throws UnmarshalException {
        return unmarshal(XMLScanner.open(in));
    }

    public static LINE unmarshal(XMLScanner xs) throws UnmarshalException {
        return unmarshal(xs, newDispatcher());
    }

    public static LINE unmarshal(XMLScanner xs, Dispatcher d) throws UnmarshalException {
        return ((LINE) d.unmarshal(xs, (LINE.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof LINE)) {
            return false;
        }
        LINE tob = ((LINE) ob);
        if (_WIDTH != null) {
            if (tob._WIDTH == null) {
                return false;
            }
            if (!_WIDTH.equals(tob._WIDTH)) {
                return false;
            }
        } else {
            if (tob._WIDTH != null) {
                return false;
            }
        }
        if (_COLOR != null) {
            if (tob._COLOR == null) {
                return false;
            }
            if (!_COLOR.equals(tob._COLOR)) {
                return false;
            }
        } else {
            if (tob._COLOR != null) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int h = 0;
        h = ((127 * h) + ((_WIDTH != null) ? _WIDTH.hashCode() : 0));
        h = ((127 * h) + ((_COLOR != null) ? _COLOR.hashCode() : 0));
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<LINE");
        if (_WIDTH != null) {
            sb.append(" WIDTH=");
            sb.append(_WIDTH.toString());
        }
        if (_COLOR != null) {
            sb.append(" COLOR=");
            sb.append(_COLOR.toString());
        }
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        return ARROW.newDispatcher();
    }
}
