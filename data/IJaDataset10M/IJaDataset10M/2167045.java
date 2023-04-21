package edu.mit.wi.omnigene.jaxb.bsml;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.Dispatcher;
import javax.xml.bind.DuplicateAttributeException;
import javax.xml.bind.Element;
import javax.xml.bind.IdentifiableElement;
import javax.xml.bind.InvalidAttributeException;
import javax.xml.bind.LocalValidationException;
import javax.xml.bind.MarshallableObject;
import javax.xml.bind.Marshaller;
import javax.xml.bind.MissingAttributeException;
import javax.xml.bind.StructureValidationException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Validator;
import javax.xml.bind.Validator.Patcher;
import javax.xml.marshal.XMLScanner;
import javax.xml.marshal.XMLWriter;

public class AlignedSequence extends MarshallableObject implements Element {

    private IdentifiableElement _Seqref;

    private String _Start;

    private String _OnComplement;

    private String _Translated;

    private String _Frame;

    private String _TransTable;

    private String _Seqnum;

    private String _Name;

    private String _Length;

    public IdentifiableElement getSeqref() {
        return _Seqref;
    }

    public void setSeqref(IdentifiableElement _Seqref) {
        this._Seqref = _Seqref;
        if (_Seqref == null) {
            invalidate();
        }
    }

    public String getStart() {
        return _Start;
    }

    public void setStart(String _Start) {
        this._Start = _Start;
        if (_Start == null) {
            invalidate();
        }
    }

    public String getOnComplement() {
        return _OnComplement;
    }

    public void setOnComplement(String _OnComplement) {
        this._OnComplement = _OnComplement;
        if (_OnComplement == null) {
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

    public String getFrame() {
        return _Frame;
    }

    public void setFrame(String _Frame) {
        this._Frame = _Frame;
        if (_Frame == null) {
            invalidate();
        }
    }

    public String getTransTable() {
        return _TransTable;
    }

    public void setTransTable(String _TransTable) {
        this._TransTable = _TransTable;
        if (_TransTable == null) {
            invalidate();
        }
    }

    public String getSeqnum() {
        return _Seqnum;
    }

    public void setSeqnum(String _Seqnum) {
        this._Seqnum = _Seqnum;
        if (_Seqnum == null) {
            invalidate();
        }
    }

    public String getName() {
        return _Name;
    }

    public void setName(String _Name) {
        this._Name = _Name;
        if (_Name == null) {
            invalidate();
        }
    }

    public String getLength() {
        return _Length;
    }

    public void setLength(String _Length) {
        this._Length = _Length;
        if (_Length == null) {
            invalidate();
        }
    }

    public void validateThis() throws LocalValidationException {
        if (_Seqnum == null) {
            throw new MissingAttributeException("seqnum");
        }
        if (_Name == null) {
            throw new MissingAttributeException("name");
        }
        if (_Length == null) {
            throw new MissingAttributeException("length");
        }
    }

    public void validate(Validator v) throws StructureValidationException {
        if (_Seqref != null) v.reference(_Seqref);
    }

    public void marshal(Marshaller m) throws IOException {
        XMLWriter w = m.writer();
        w.start("Aligned-sequence");
        if (_Seqref != null) {
            w.attribute("seqref", _Seqref.id());
        }
        if (_Start != null) {
            w.attribute("start", _Start.toString());
        }
        if (_OnComplement != null) {
            w.attribute("on-complement", _OnComplement.toString());
        }
        if (_Translated != null) {
            w.attribute("translated", _Translated.toString());
        }
        if (_Frame != null) {
            w.attribute("frame", _Frame.toString());
        }
        if (_TransTable != null) {
            w.attribute("trans-table", _TransTable.toString());
        }
        w.attribute("seqnum", _Seqnum.toString());
        w.attribute("name", _Name.toString());
        w.attribute("length", _Length.toString());
        w.end("Aligned-sequence");
    }

    public void unmarshal(Unmarshaller u) throws UnmarshalException {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("Aligned-sequence");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            if (an.equals("seqref")) {
                if (_Seqref != null) {
                    throw new DuplicateAttributeException(an);
                }
                v.reference(xs.takeAttributeValue(), new SeqrefVPatcher());
                continue;
            }
            if (an.equals("start")) {
                if (_Start != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Start = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("on-complement")) {
                if (_OnComplement != null) {
                    throw new DuplicateAttributeException(an);
                }
                _OnComplement = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("translated")) {
                if (_Translated != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Translated = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("frame")) {
                if (_Frame != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Frame = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("trans-table")) {
                if (_TransTable != null) {
                    throw new DuplicateAttributeException(an);
                }
                _TransTable = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("seqnum")) {
                if (_Seqnum != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Seqnum = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("name")) {
                if (_Name != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Name = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("length")) {
                if (_Length != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Length = xs.takeAttributeValue();
                continue;
            }
            throw new InvalidAttributeException(an);
        }
        xs.takeEnd("Aligned-sequence");
    }

    public static AlignedSequence unmarshal(InputStream in) throws UnmarshalException {
        return unmarshal(XMLScanner.open(in));
    }

    public static AlignedSequence unmarshal(XMLScanner xs) throws UnmarshalException {
        return unmarshal(xs, newDispatcher());
    }

    public static AlignedSequence unmarshal(XMLScanner xs, Dispatcher d) throws UnmarshalException {
        return ((AlignedSequence) d.unmarshal(xs, (AlignedSequence.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof AlignedSequence)) {
            return false;
        }
        AlignedSequence tob = ((AlignedSequence) ob);
        if (_Seqref != null) {
            if (tob._Seqref == null) {
                return false;
            }
            if (!_Seqref.equals(tob._Seqref)) {
                return false;
            }
        } else {
            if (tob._Seqref != null) {
                return false;
            }
        }
        if (_Start != null) {
            if (tob._Start == null) {
                return false;
            }
            if (!_Start.equals(tob._Start)) {
                return false;
            }
        } else {
            if (tob._Start != null) {
                return false;
            }
        }
        if (_OnComplement != null) {
            if (tob._OnComplement == null) {
                return false;
            }
            if (!_OnComplement.equals(tob._OnComplement)) {
                return false;
            }
        } else {
            if (tob._OnComplement != null) {
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
        if (_Frame != null) {
            if (tob._Frame == null) {
                return false;
            }
            if (!_Frame.equals(tob._Frame)) {
                return false;
            }
        } else {
            if (tob._Frame != null) {
                return false;
            }
        }
        if (_TransTable != null) {
            if (tob._TransTable == null) {
                return false;
            }
            if (!_TransTable.equals(tob._TransTable)) {
                return false;
            }
        } else {
            if (tob._TransTable != null) {
                return false;
            }
        }
        if (_Seqnum != null) {
            if (tob._Seqnum == null) {
                return false;
            }
            if (!_Seqnum.equals(tob._Seqnum)) {
                return false;
            }
        } else {
            if (tob._Seqnum != null) {
                return false;
            }
        }
        if (_Name != null) {
            if (tob._Name == null) {
                return false;
            }
            if (!_Name.equals(tob._Name)) {
                return false;
            }
        } else {
            if (tob._Name != null) {
                return false;
            }
        }
        if (_Length != null) {
            if (tob._Length == null) {
                return false;
            }
            if (!_Length.equals(tob._Length)) {
                return false;
            }
        } else {
            if (tob._Length != null) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int h = 0;
        h = ((179 * h) + ((_Seqref != null) ? _Seqref.id().hashCode() : 0));
        h = ((127 * h) + ((_Start != null) ? _Start.hashCode() : 0));
        h = ((127 * h) + ((_OnComplement != null) ? _OnComplement.hashCode() : 0));
        h = ((127 * h) + ((_Translated != null) ? _Translated.hashCode() : 0));
        h = ((127 * h) + ((_Frame != null) ? _Frame.hashCode() : 0));
        h = ((127 * h) + ((_TransTable != null) ? _TransTable.hashCode() : 0));
        h = ((127 * h) + ((_Seqnum != null) ? _Seqnum.hashCode() : 0));
        h = ((127 * h) + ((_Name != null) ? _Name.hashCode() : 0));
        h = ((127 * h) + ((_Length != null) ? _Length.hashCode() : 0));
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<Aligned-sequence");
        if (_Seqref != null) {
            sb.append(" seqref=");
            sb.append(_Seqref.id());
        }
        if (_Start != null) {
            sb.append(" start=");
            sb.append(_Start.toString());
        }
        if (_OnComplement != null) {
            sb.append(" on-complement=");
            sb.append(_OnComplement.toString());
        }
        if (_Translated != null) {
            sb.append(" translated=");
            sb.append(_Translated.toString());
        }
        if (_Frame != null) {
            sb.append(" frame=");
            sb.append(_Frame.toString());
        }
        if (_TransTable != null) {
            sb.append(" trans-table=");
            sb.append(_TransTable.toString());
        }
        if (_Seqnum != null) {
            sb.append(" seqnum=");
            sb.append(_Seqnum.toString());
        }
        if (_Name != null) {
            sb.append(" name=");
            sb.append(_Name.toString());
        }
        if (_Length != null) {
            sb.append(" length=");
            sb.append(_Length.toString());
        }
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        return Agent.newDispatcher();
    }

    private class SeqrefVPatcher extends Validator.Patcher {

        public void patch(IdentifiableElement target) {
            _Seqref = target;
        }
    }
}
