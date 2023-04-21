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
import javax.xml.bind.PredicatedLists;
import javax.xml.bind.PredicatedLists.Predicate;
import javax.xml.bind.StructureValidationException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidatableObject;
import javax.xml.bind.Validator;
import javax.xml.marshal.XMLScanner;
import javax.xml.marshal.XMLWriter;

public class Sets extends MarshallableObject implements Element, IdentifiableElement {

    private String _Id;

    private String _Class;

    private String _Style;

    private String _Title;

    private String _Comment;

    private String _ValueType;

    private String _Value;

    private String _Selectable;

    private String _Selected;

    private String _Display;

    private String _Readonly;

    private String _Onclick;

    private String _Ondblclick;

    private String _Onmousedown;

    private String _Onmouseup;

    private String _Onmouseover;

    private String _Onmousemove;

    private String _Onmouseout;

    private String _Onkeypress;

    private String _Onkeydown;

    private String _Onkeyup;

    private List _Content = PredicatedLists.createInvalidating(this, new ContentPredicate(), new ArrayList());

    private PredicatedLists.Predicate pred_Content = new ContentPredicate();

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

    public String get_class() {
        return _Class;
    }

    public void setClass(String _Class) {
        this._Class = _Class;
        if (_Class == null) {
            invalidate();
        }
    }

    public String getStyle() {
        return _Style;
    }

    public void setStyle(String _Style) {
        this._Style = _Style;
        if (_Style == null) {
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

    public String getComment() {
        return _Comment;
    }

    public void setComment(String _Comment) {
        this._Comment = _Comment;
        if (_Comment == null) {
            invalidate();
        }
    }

    public String getValueType() {
        return _ValueType;
    }

    public void setValueType(String _ValueType) {
        this._ValueType = _ValueType;
        if (_ValueType == null) {
            invalidate();
        }
    }

    public String getValue() {
        return _Value;
    }

    public void setValue(String _Value) {
        this._Value = _Value;
        if (_Value == null) {
            invalidate();
        }
    }

    public String getSelectable() {
        return _Selectable;
    }

    public void setSelectable(String _Selectable) {
        this._Selectable = _Selectable;
        if (_Selectable == null) {
            invalidate();
        }
    }

    public String getSelected() {
        return _Selected;
    }

    public void setSelected(String _Selected) {
        this._Selected = _Selected;
        if (_Selected == null) {
            invalidate();
        }
    }

    public String getDisplay() {
        return _Display;
    }

    public void setDisplay(String _Display) {
        this._Display = _Display;
        if (_Display == null) {
            invalidate();
        }
    }

    public String getReadonly() {
        return _Readonly;
    }

    public void setReadonly(String _Readonly) {
        this._Readonly = _Readonly;
        if (_Readonly == null) {
            invalidate();
        }
    }

    public String getOnclick() {
        return _Onclick;
    }

    public void setOnclick(String _Onclick) {
        this._Onclick = _Onclick;
        if (_Onclick == null) {
            invalidate();
        }
    }

    public String getOndblclick() {
        return _Ondblclick;
    }

    public void setOndblclick(String _Ondblclick) {
        this._Ondblclick = _Ondblclick;
        if (_Ondblclick == null) {
            invalidate();
        }
    }

    public String getOnmousedown() {
        return _Onmousedown;
    }

    public void setOnmousedown(String _Onmousedown) {
        this._Onmousedown = _Onmousedown;
        if (_Onmousedown == null) {
            invalidate();
        }
    }

    public String getOnmouseup() {
        return _Onmouseup;
    }

    public void setOnmouseup(String _Onmouseup) {
        this._Onmouseup = _Onmouseup;
        if (_Onmouseup == null) {
            invalidate();
        }
    }

    public String getOnmouseover() {
        return _Onmouseover;
    }

    public void setOnmouseover(String _Onmouseover) {
        this._Onmouseover = _Onmouseover;
        if (_Onmouseover == null) {
            invalidate();
        }
    }

    public String getOnmousemove() {
        return _Onmousemove;
    }

    public void setOnmousemove(String _Onmousemove) {
        this._Onmousemove = _Onmousemove;
        if (_Onmousemove == null) {
            invalidate();
        }
    }

    public String getOnmouseout() {
        return _Onmouseout;
    }

    public void setOnmouseout(String _Onmouseout) {
        this._Onmouseout = _Onmouseout;
        if (_Onmouseout == null) {
            invalidate();
        }
    }

    public String getOnkeypress() {
        return _Onkeypress;
    }

    public void setOnkeypress(String _Onkeypress) {
        this._Onkeypress = _Onkeypress;
        if (_Onkeypress == null) {
            invalidate();
        }
    }

    public String getOnkeydown() {
        return _Onkeydown;
    }

    public void setOnkeydown(String _Onkeydown) {
        this._Onkeydown = _Onkeydown;
        if (_Onkeydown == null) {
            invalidate();
        }
    }

    public String getOnkeyup() {
        return _Onkeyup;
    }

    public void setOnkeyup(String _Onkeyup) {
        this._Onkeyup = _Onkeyup;
        if (_Onkeyup == null) {
            invalidate();
        }
    }

    public List getContent() {
        return _Content;
    }

    public void deleteContent() {
        _Content = null;
        invalidate();
    }

    public void emptyContent() {
        _Content = PredicatedLists.createInvalidating(this, pred_Content, new ArrayList());
    }

    public void validateThis() throws LocalValidationException {
    }

    public void validate(Validator v) throws StructureValidationException {
        if (_Content != null) {
            for (Iterator i = _Content.iterator(); i.hasNext(); ) {
                v.validate(((ValidatableObject) i.next()));
            }
        }
    }

    public void marshal(Marshaller m) throws IOException {
        XMLWriter w = m.writer();
        w.start("Sets");
        if (_Id != null) {
            w.attribute("id", _Id.toString());
        }
        if (_Class != null) {
            w.attribute("class", _Class.toString());
        }
        if (_Style != null) {
            w.attribute("style", _Style.toString());
        }
        if (_Title != null) {
            w.attribute("title", _Title.toString());
        }
        if (_Comment != null) {
            w.attribute("comment", _Comment.toString());
        }
        if (_ValueType != null) {
            w.attribute("value-type", _ValueType.toString());
        }
        if (_Value != null) {
            w.attribute("value", _Value.toString());
        }
        if (_Selectable != null) {
            w.attribute("selectable", _Selectable.toString());
        }
        if (_Selected != null) {
            w.attribute("selected", _Selected.toString());
        }
        if (_Display != null) {
            w.attribute("display", _Display.toString());
        }
        if (_Readonly != null) {
            w.attribute("readonly", _Readonly.toString());
        }
        if (_Onclick != null) {
            w.attribute("onclick", _Onclick.toString());
        }
        if (_Ondblclick != null) {
            w.attribute("ondblclick", _Ondblclick.toString());
        }
        if (_Onmousedown != null) {
            w.attribute("onmousedown", _Onmousedown.toString());
        }
        if (_Onmouseup != null) {
            w.attribute("onmouseup", _Onmouseup.toString());
        }
        if (_Onmouseover != null) {
            w.attribute("onmouseover", _Onmouseover.toString());
        }
        if (_Onmousemove != null) {
            w.attribute("onmousemove", _Onmousemove.toString());
        }
        if (_Onmouseout != null) {
            w.attribute("onmouseout", _Onmouseout.toString());
        }
        if (_Onkeypress != null) {
            w.attribute("onkeypress", _Onkeypress.toString());
        }
        if (_Onkeydown != null) {
            w.attribute("onkeydown", _Onkeydown.toString());
        }
        if (_Onkeyup != null) {
            w.attribute("onkeyup", _Onkeyup.toString());
        }
        if (_Content.size() > 0) {
            for (Iterator i = _Content.iterator(); i.hasNext(); ) {
                m.marshal(((MarshallableObject) i.next()));
            }
        }
        w.end("Sets");
    }

    public void unmarshal(Unmarshaller u) throws UnmarshalException {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("Sets");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            if (an.equals("id")) {
                if (_Id != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Id = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("class")) {
                if (_Class != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Class = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("style")) {
                if (_Style != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Style = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("title")) {
                if (_Title != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Title = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("comment")) {
                if (_Comment != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Comment = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("value-type")) {
                if (_ValueType != null) {
                    throw new DuplicateAttributeException(an);
                }
                _ValueType = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("value")) {
                if (_Value != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Value = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("selectable")) {
                if (_Selectable != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Selectable = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("selected")) {
                if (_Selected != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Selected = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("display")) {
                if (_Display != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Display = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("readonly")) {
                if (_Readonly != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Readonly = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("onclick")) {
                if (_Onclick != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Onclick = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("ondblclick")) {
                if (_Ondblclick != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Ondblclick = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("onmousedown")) {
                if (_Onmousedown != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Onmousedown = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("onmouseup")) {
                if (_Onmouseup != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Onmouseup = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("onmouseover")) {
                if (_Onmouseover != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Onmouseover = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("onmousemove")) {
                if (_Onmousemove != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Onmousemove = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("onmouseout")) {
                if (_Onmouseout != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Onmouseout = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("onkeypress")) {
                if (_Onkeypress != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Onkeypress = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("onkeydown")) {
                if (_Onkeydown != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Onkeydown = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("onkeyup")) {
                if (_Onkeyup != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Onkeyup = xs.takeAttributeValue();
                continue;
            }
            throw new InvalidAttributeException(an);
        }
        {
            List l = PredicatedLists.create(this, pred_Content, new ArrayList());
            while (xs.atStart("Attribute") || xs.atStart("Set")) {
                l.add(((MarshallableObject) u.unmarshal()));
            }
            _Content = PredicatedLists.createInvalidating(this, pred_Content, l);
        }
        xs.takeEnd("Sets");
    }

    public static Sets unmarshal(InputStream in) throws UnmarshalException {
        return unmarshal(XMLScanner.open(in));
    }

    public static Sets unmarshal(XMLScanner xs) throws UnmarshalException {
        return unmarshal(xs, newDispatcher());
    }

    public static Sets unmarshal(XMLScanner xs, Dispatcher d) throws UnmarshalException {
        return ((Sets) d.unmarshal(xs, (Sets.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof Sets)) {
            return false;
        }
        Sets tob = ((Sets) ob);
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
        if (_Class != null) {
            if (tob._Class == null) {
                return false;
            }
            if (!_Class.equals(tob._Class)) {
                return false;
            }
        } else {
            if (tob._Class != null) {
                return false;
            }
        }
        if (_Style != null) {
            if (tob._Style == null) {
                return false;
            }
            if (!_Style.equals(tob._Style)) {
                return false;
            }
        } else {
            if (tob._Style != null) {
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
        if (_Comment != null) {
            if (tob._Comment == null) {
                return false;
            }
            if (!_Comment.equals(tob._Comment)) {
                return false;
            }
        } else {
            if (tob._Comment != null) {
                return false;
            }
        }
        if (_ValueType != null) {
            if (tob._ValueType == null) {
                return false;
            }
            if (!_ValueType.equals(tob._ValueType)) {
                return false;
            }
        } else {
            if (tob._ValueType != null) {
                return false;
            }
        }
        if (_Value != null) {
            if (tob._Value == null) {
                return false;
            }
            if (!_Value.equals(tob._Value)) {
                return false;
            }
        } else {
            if (tob._Value != null) {
                return false;
            }
        }
        if (_Selectable != null) {
            if (tob._Selectable == null) {
                return false;
            }
            if (!_Selectable.equals(tob._Selectable)) {
                return false;
            }
        } else {
            if (tob._Selectable != null) {
                return false;
            }
        }
        if (_Selected != null) {
            if (tob._Selected == null) {
                return false;
            }
            if (!_Selected.equals(tob._Selected)) {
                return false;
            }
        } else {
            if (tob._Selected != null) {
                return false;
            }
        }
        if (_Display != null) {
            if (tob._Display == null) {
                return false;
            }
            if (!_Display.equals(tob._Display)) {
                return false;
            }
        } else {
            if (tob._Display != null) {
                return false;
            }
        }
        if (_Readonly != null) {
            if (tob._Readonly == null) {
                return false;
            }
            if (!_Readonly.equals(tob._Readonly)) {
                return false;
            }
        } else {
            if (tob._Readonly != null) {
                return false;
            }
        }
        if (_Onclick != null) {
            if (tob._Onclick == null) {
                return false;
            }
            if (!_Onclick.equals(tob._Onclick)) {
                return false;
            }
        } else {
            if (tob._Onclick != null) {
                return false;
            }
        }
        if (_Ondblclick != null) {
            if (tob._Ondblclick == null) {
                return false;
            }
            if (!_Ondblclick.equals(tob._Ondblclick)) {
                return false;
            }
        } else {
            if (tob._Ondblclick != null) {
                return false;
            }
        }
        if (_Onmousedown != null) {
            if (tob._Onmousedown == null) {
                return false;
            }
            if (!_Onmousedown.equals(tob._Onmousedown)) {
                return false;
            }
        } else {
            if (tob._Onmousedown != null) {
                return false;
            }
        }
        if (_Onmouseup != null) {
            if (tob._Onmouseup == null) {
                return false;
            }
            if (!_Onmouseup.equals(tob._Onmouseup)) {
                return false;
            }
        } else {
            if (tob._Onmouseup != null) {
                return false;
            }
        }
        if (_Onmouseover != null) {
            if (tob._Onmouseover == null) {
                return false;
            }
            if (!_Onmouseover.equals(tob._Onmouseover)) {
                return false;
            }
        } else {
            if (tob._Onmouseover != null) {
                return false;
            }
        }
        if (_Onmousemove != null) {
            if (tob._Onmousemove == null) {
                return false;
            }
            if (!_Onmousemove.equals(tob._Onmousemove)) {
                return false;
            }
        } else {
            if (tob._Onmousemove != null) {
                return false;
            }
        }
        if (_Onmouseout != null) {
            if (tob._Onmouseout == null) {
                return false;
            }
            if (!_Onmouseout.equals(tob._Onmouseout)) {
                return false;
            }
        } else {
            if (tob._Onmouseout != null) {
                return false;
            }
        }
        if (_Onkeypress != null) {
            if (tob._Onkeypress == null) {
                return false;
            }
            if (!_Onkeypress.equals(tob._Onkeypress)) {
                return false;
            }
        } else {
            if (tob._Onkeypress != null) {
                return false;
            }
        }
        if (_Onkeydown != null) {
            if (tob._Onkeydown == null) {
                return false;
            }
            if (!_Onkeydown.equals(tob._Onkeydown)) {
                return false;
            }
        } else {
            if (tob._Onkeydown != null) {
                return false;
            }
        }
        if (_Onkeyup != null) {
            if (tob._Onkeyup == null) {
                return false;
            }
            if (!_Onkeyup.equals(tob._Onkeyup)) {
                return false;
            }
        } else {
            if (tob._Onkeyup != null) {
                return false;
            }
        }
        if (_Content != null) {
            if (tob._Content == null) {
                return false;
            }
            if (!_Content.equals(tob._Content)) {
                return false;
            }
        } else {
            if (tob._Content != null) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int h = 0;
        h = ((127 * h) + ((_Id != null) ? _Id.hashCode() : 0));
        h = ((127 * h) + ((_Class != null) ? _Class.hashCode() : 0));
        h = ((127 * h) + ((_Style != null) ? _Style.hashCode() : 0));
        h = ((127 * h) + ((_Title != null) ? _Title.hashCode() : 0));
        h = ((127 * h) + ((_Comment != null) ? _Comment.hashCode() : 0));
        h = ((127 * h) + ((_ValueType != null) ? _ValueType.hashCode() : 0));
        h = ((127 * h) + ((_Value != null) ? _Value.hashCode() : 0));
        h = ((127 * h) + ((_Selectable != null) ? _Selectable.hashCode() : 0));
        h = ((127 * h) + ((_Selected != null) ? _Selected.hashCode() : 0));
        h = ((127 * h) + ((_Display != null) ? _Display.hashCode() : 0));
        h = ((127 * h) + ((_Readonly != null) ? _Readonly.hashCode() : 0));
        h = ((127 * h) + ((_Onclick != null) ? _Onclick.hashCode() : 0));
        h = ((127 * h) + ((_Ondblclick != null) ? _Ondblclick.hashCode() : 0));
        h = ((127 * h) + ((_Onmousedown != null) ? _Onmousedown.hashCode() : 0));
        h = ((127 * h) + ((_Onmouseup != null) ? _Onmouseup.hashCode() : 0));
        h = ((127 * h) + ((_Onmouseover != null) ? _Onmouseover.hashCode() : 0));
        h = ((127 * h) + ((_Onmousemove != null) ? _Onmousemove.hashCode() : 0));
        h = ((127 * h) + ((_Onmouseout != null) ? _Onmouseout.hashCode() : 0));
        h = ((127 * h) + ((_Onkeypress != null) ? _Onkeypress.hashCode() : 0));
        h = ((127 * h) + ((_Onkeydown != null) ? _Onkeydown.hashCode() : 0));
        h = ((127 * h) + ((_Onkeyup != null) ? _Onkeyup.hashCode() : 0));
        h = ((127 * h) + ((_Content != null) ? _Content.hashCode() : 0));
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<Sets");
        if (_Id != null) {
            sb.append(" id=");
            sb.append(_Id.toString());
        }
        if (_Class != null) {
            sb.append(" class=");
            sb.append(_Class.toString());
        }
        if (_Style != null) {
            sb.append(" style=");
            sb.append(_Style.toString());
        }
        if (_Title != null) {
            sb.append(" title=");
            sb.append(_Title.toString());
        }
        if (_Comment != null) {
            sb.append(" comment=");
            sb.append(_Comment.toString());
        }
        if (_ValueType != null) {
            sb.append(" value-type=");
            sb.append(_ValueType.toString());
        }
        if (_Value != null) {
            sb.append(" value=");
            sb.append(_Value.toString());
        }
        if (_Selectable != null) {
            sb.append(" selectable=");
            sb.append(_Selectable.toString());
        }
        if (_Selected != null) {
            sb.append(" selected=");
            sb.append(_Selected.toString());
        }
        if (_Display != null) {
            sb.append(" display=");
            sb.append(_Display.toString());
        }
        if (_Readonly != null) {
            sb.append(" readonly=");
            sb.append(_Readonly.toString());
        }
        if (_Onclick != null) {
            sb.append(" onclick=");
            sb.append(_Onclick.toString());
        }
        if (_Ondblclick != null) {
            sb.append(" ondblclick=");
            sb.append(_Ondblclick.toString());
        }
        if (_Onmousedown != null) {
            sb.append(" onmousedown=");
            sb.append(_Onmousedown.toString());
        }
        if (_Onmouseup != null) {
            sb.append(" onmouseup=");
            sb.append(_Onmouseup.toString());
        }
        if (_Onmouseover != null) {
            sb.append(" onmouseover=");
            sb.append(_Onmouseover.toString());
        }
        if (_Onmousemove != null) {
            sb.append(" onmousemove=");
            sb.append(_Onmousemove.toString());
        }
        if (_Onmouseout != null) {
            sb.append(" onmouseout=");
            sb.append(_Onmouseout.toString());
        }
        if (_Onkeypress != null) {
            sb.append(" onkeypress=");
            sb.append(_Onkeypress.toString());
        }
        if (_Onkeydown != null) {
            sb.append(" onkeydown=");
            sb.append(_Onkeydown.toString());
        }
        if (_Onkeyup != null) {
            sb.append(" onkeyup=");
            sb.append(_Onkeyup.toString());
        }
        if (_Content != null) {
            sb.append(" content=");
            sb.append(_Content.toString());
        }
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        return AlignmentPointSet.newDispatcher();
    }

    private static class ContentPredicate implements PredicatedLists.Predicate {

        public void check(Object ob) {
            if (!(ob instanceof MarshallableObject)) {
                throw new InvalidContentObjectException(ob, (MarshallableObject.class));
            }
        }
    }
}
