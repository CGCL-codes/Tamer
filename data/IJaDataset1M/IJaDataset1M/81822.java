package de.robowars.comm.transport;

import de.robowars.comm.transport.RobotEnumeration;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.ConversionException;
import javax.xml.bind.Dispatcher;
import javax.xml.bind.DuplicateAttributeException;
import javax.xml.bind.Element;
import javax.xml.bind.InvalidAttributeException;
import javax.xml.bind.LocalValidationException;
import javax.xml.bind.MarshallableObject;
import javax.xml.bind.Marshaller;
import javax.xml.bind.MissingAttributeException;
import javax.xml.bind.MissingContentException;
import javax.xml.bind.StructureValidationException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Validator;
import javax.xml.marshal.XMLScanner;
import javax.xml.marshal.XMLWriter;

public class Application extends MarshallableObject implements Element {

    private RobotEnumeration _RobotType;

    private String _PlayerName;

    public RobotEnumeration getRobotType() {
        return _RobotType;
    }

    public void setRobotType(RobotEnumeration _RobotType) {
        this._RobotType = _RobotType;
        if (_RobotType == null) {
            invalidate();
        }
    }

    public String getPlayerName() {
        return _PlayerName;
    }

    public void setPlayerName(String _PlayerName) {
        this._PlayerName = _PlayerName;
        if (_PlayerName == null) {
            invalidate();
        }
    }

    public void validateThis() throws LocalValidationException {
        if (_RobotType == null) {
            throw new MissingAttributeException("robot-type");
        }
        if (_PlayerName == null) {
            throw new MissingContentException("player-name");
        }
    }

    public void validate(Validator v) throws StructureValidationException {
    }

    public void marshal(Marshaller m) throws IOException {
        XMLWriter w = m.writer();
        w.start("application");
        w.attribute("robot-type", _RobotType.toString());
        w.leaf("player-name", _PlayerName.toString());
        w.end("application");
    }

    public void unmarshal(Unmarshaller u) throws UnmarshalException {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("application");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            if (an.equals("robot-type")) {
                if (_RobotType != null) {
                    throw new DuplicateAttributeException(an);
                }
                try {
                    _RobotType = RobotEnumeration.parse(xs.takeAttributeValue());
                } catch (Exception x) {
                    throw new ConversionException(an, x);
                }
                continue;
            }
            throw new InvalidAttributeException(an);
        }
        if (xs.atStart("player-name")) {
            xs.takeStart("player-name");
            String s;
            if (xs.atChars(XMLScanner.WS_COLLAPSE)) {
                s = xs.takeChars(XMLScanner.WS_COLLAPSE);
            } else {
                s = "";
            }
            try {
                _PlayerName = String.valueOf(s);
            } catch (Exception x) {
                throw new ConversionException("player-name", x);
            }
            xs.takeEnd("player-name");
        }
        xs.takeEnd("application");
    }

    public static Application unmarshal(InputStream in) throws UnmarshalException {
        return unmarshal(XMLScanner.open(in));
    }

    public static Application unmarshal(XMLScanner xs) throws UnmarshalException {
        return unmarshal(xs, newDispatcher());
    }

    public static Application unmarshal(XMLScanner xs, Dispatcher d) throws UnmarshalException {
        return ((Application) d.unmarshal(xs, (Application.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof Application)) {
            return false;
        }
        Application tob = ((Application) ob);
        if (_RobotType != null) {
            if (tob._RobotType == null) {
                return false;
            }
            if (!_RobotType.equals(tob._RobotType)) {
                return false;
            }
        } else {
            if (tob._RobotType != null) {
                return false;
            }
        }
        if (_PlayerName != null) {
            if (tob._PlayerName == null) {
                return false;
            }
            if (!_PlayerName.equals(tob._PlayerName)) {
                return false;
            }
        } else {
            if (tob._PlayerName != null) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int h = 0;
        h = ((127 * h) + ((_RobotType != null) ? _RobotType.hashCode() : 0));
        h = ((127 * h) + ((_PlayerName != null) ? _PlayerName.hashCode() : 0));
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<application");
        if (_RobotType != null) {
            sb.append(" robot-type=");
            sb.append(_RobotType.toString());
        }
        if (_PlayerName != null) {
            sb.append(" player-name=");
            sb.append(_PlayerName.toString());
        }
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        return Acceptance.newDispatcher();
    }
}
