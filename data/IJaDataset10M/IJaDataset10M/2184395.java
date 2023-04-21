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
import javax.xml.bind.Validator.Patcher;
import javax.xml.marshal.XMLScanner;
import javax.xml.marshal.XMLWriter;

public class SequenceMotif extends MarshallableObject implements Element, IdentifiableElement {

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

    private String _DisplayAuto;

    private IdentifiableElement _AutoView;

    private IdentifiableElement _Seqref;

    private String _Alignment;

    private String _Startpos;

    private String _Endpos;

    private String _Refs;

    private List _IntervalLoc = PredicatedLists.createInvalidating(this, new IntervalLocPredicate(), new ArrayList());

    private PredicatedLists.Predicate pred_IntervalLoc = new IntervalLocPredicate();

    private List _MotifData = PredicatedLists.createInvalidating(this, new MotifDataPredicate(), new ArrayList());

    private PredicatedLists.Predicate pred_MotifData = new MotifDataPredicate();

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

    public String getDisplayAuto() {
        return _DisplayAuto;
    }

    public void setDisplayAuto(String _DisplayAuto) {
        this._DisplayAuto = _DisplayAuto;
        if (_DisplayAuto == null) {
            invalidate();
        }
    }

    public IdentifiableElement getAutoView() {
        return _AutoView;
    }

    public void setAutoView(IdentifiableElement _AutoView) {
        this._AutoView = _AutoView;
        if (_AutoView == null) {
            invalidate();
        }
    }

    public IdentifiableElement getSeqref() {
        return _Seqref;
    }

    public void setSeqref(IdentifiableElement _Seqref) {
        this._Seqref = _Seqref;
        if (_Seqref == null) {
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

    public String getStartpos() {
        return _Startpos;
    }

    public void setStartpos(String _Startpos) {
        this._Startpos = _Startpos;
        if (_Startpos == null) {
            invalidate();
        }
    }

    public String getEndpos() {
        return _Endpos;
    }

    public void setEndpos(String _Endpos) {
        this._Endpos = _Endpos;
        if (_Endpos == null) {
            invalidate();
        }
    }

    public String getRefs() {
        return _Refs;
    }

    public void setRefs(String _Refs) {
        this._Refs = _Refs;
        if (_Refs == null) {
            invalidate();
        }
    }

    public List getIntervalLoc() {
        return _IntervalLoc;
    }

    public void deleteIntervalLoc() {
        _IntervalLoc = null;
        invalidate();
    }

    public void emptyIntervalLoc() {
        _IntervalLoc = PredicatedLists.createInvalidating(this, pred_IntervalLoc, new ArrayList());
    }

    public List getMotifData() {
        return _MotifData;
    }

    public void deleteMotifData() {
        _MotifData = null;
        invalidate();
    }

    public void emptyMotifData() {
        _MotifData = PredicatedLists.createInvalidating(this, pred_MotifData, new ArrayList());
    }

    public void validateThis() throws LocalValidationException {
    }

    public void validate(Validator v) throws StructureValidationException {
        if (_AutoView != null) v.reference(_AutoView);
        if (_Seqref != null) v.reference(_Seqref);
        if (_IntervalLoc != null) {
            for (Iterator i = _IntervalLoc.iterator(); i.hasNext(); ) {
                v.validate(((ValidatableObject) i.next()));
            }
        }
        if (_MotifData != null) {
            for (Iterator i = _MotifData.iterator(); i.hasNext(); ) {
                v.validate(((ValidatableObject) i.next()));
            }
        }
    }

    public void marshal(Marshaller m) throws IOException {
        XMLWriter w = m.writer();
        w.start("Sequence-motif");
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
        if (_DisplayAuto != null) {
            w.attribute("display-auto", _DisplayAuto.toString());
        }
        if (_AutoView != null) {
            w.attribute("auto-view", _AutoView.id());
        }
        w.attribute("seqref", _Seqref.id());
        if (_Alignment != null) {
            w.attribute("alignment", _Alignment.toString());
        }
        if (_Startpos != null) {
            w.attribute("startpos", _Startpos.toString());
        }
        if (_Endpos != null) {
            w.attribute("endpos", _Endpos.toString());
        }
        if (_Refs != null) {
            w.attribute("refs", _Refs.toString());
        }
        if (_IntervalLoc.size() > 0) {
            for (Iterator i = _IntervalLoc.iterator(); i.hasNext(); ) {
                m.marshal(((MarshallableObject) i.next()));
            }
        }
        if (_MotifData.size() > 0) {
            for (Iterator i = _MotifData.iterator(); i.hasNext(); ) {
                m.marshal(((MarshallableObject) i.next()));
            }
        }
        w.end("Sequence-motif");
    }

    public void unmarshal(Unmarshaller u) throws UnmarshalException {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("Sequence-motif");
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
            if (an.equals("display-auto")) {
                if (_DisplayAuto != null) {
                    throw new DuplicateAttributeException(an);
                }
                _DisplayAuto = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("auto-view")) {
                if (_AutoView != null) {
                    throw new DuplicateAttributeException(an);
                }
                v.reference(xs.takeAttributeValue(), new AutoViewVPatcher());
                continue;
            }
            if (an.equals("seqref")) {
                if (_Seqref != null) {
                    throw new DuplicateAttributeException(an);
                }
                v.reference(xs.takeAttributeValue(), new SeqrefVPatcher());
                continue;
            }
            if (an.equals("alignment")) {
                if (_Alignment != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Alignment = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("startpos")) {
                if (_Startpos != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Startpos = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("endpos")) {
                if (_Endpos != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Endpos = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("refs")) {
                if (_Refs != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Refs = xs.takeAttributeValue();
                continue;
            }
            throw new InvalidAttributeException(an);
        }
        {
            List l = PredicatedLists.create(this, pred_IntervalLoc, new ArrayList());
            while (xs.atStart("Interval-loc")) {
                l.add(((IntervalLoc) u.unmarshal()));
            }
            _IntervalLoc = PredicatedLists.createInvalidating(this, pred_IntervalLoc, l);
        }
        {
            List l = PredicatedLists.create(this, pred_MotifData, new ArrayList());
            while (xs.atStart("Motif-data")) {
                l.add(((MotifData) u.unmarshal()));
            }
            _MotifData = PredicatedLists.createInvalidating(this, pred_MotifData, l);
        }
        xs.takeEnd("Sequence-motif");
    }

    public static SequenceMotif unmarshal(InputStream in) throws UnmarshalException {
        return unmarshal(XMLScanner.open(in));
    }

    public static SequenceMotif unmarshal(XMLScanner xs) throws UnmarshalException {
        return unmarshal(xs, newDispatcher());
    }

    public static SequenceMotif unmarshal(XMLScanner xs, Dispatcher d) throws UnmarshalException {
        return ((SequenceMotif) d.unmarshal(xs, (SequenceMotif.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof SequenceMotif)) {
            return false;
        }
        SequenceMotif tob = ((SequenceMotif) ob);
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
        if (_DisplayAuto != null) {
            if (tob._DisplayAuto == null) {
                return false;
            }
            if (!_DisplayAuto.equals(tob._DisplayAuto)) {
                return false;
            }
        } else {
            if (tob._DisplayAuto != null) {
                return false;
            }
        }
        if (_AutoView != null) {
            if (tob._AutoView == null) {
                return false;
            }
            if (!_AutoView.equals(tob._AutoView)) {
                return false;
            }
        } else {
            if (tob._AutoView != null) {
                return false;
            }
        }
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
        if (_Startpos != null) {
            if (tob._Startpos == null) {
                return false;
            }
            if (!_Startpos.equals(tob._Startpos)) {
                return false;
            }
        } else {
            if (tob._Startpos != null) {
                return false;
            }
        }
        if (_Endpos != null) {
            if (tob._Endpos == null) {
                return false;
            }
            if (!_Endpos.equals(tob._Endpos)) {
                return false;
            }
        } else {
            if (tob._Endpos != null) {
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
        if (_IntervalLoc != null) {
            if (tob._IntervalLoc == null) {
                return false;
            }
            if (!_IntervalLoc.equals(tob._IntervalLoc)) {
                return false;
            }
        } else {
            if (tob._IntervalLoc != null) {
                return false;
            }
        }
        if (_MotifData != null) {
            if (tob._MotifData == null) {
                return false;
            }
            if (!_MotifData.equals(tob._MotifData)) {
                return false;
            }
        } else {
            if (tob._MotifData != null) {
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
        h = ((127 * h) + ((_DisplayAuto != null) ? _DisplayAuto.hashCode() : 0));
        h = ((179 * h) + ((_AutoView != null) ? _AutoView.id().hashCode() : 0));
        h = ((179 * h) + ((_Seqref != null) ? _Seqref.id().hashCode() : 0));
        h = ((127 * h) + ((_Alignment != null) ? _Alignment.hashCode() : 0));
        h = ((127 * h) + ((_Startpos != null) ? _Startpos.hashCode() : 0));
        h = ((127 * h) + ((_Endpos != null) ? _Endpos.hashCode() : 0));
        h = ((127 * h) + ((_Refs != null) ? _Refs.hashCode() : 0));
        h = ((127 * h) + ((_IntervalLoc != null) ? _IntervalLoc.hashCode() : 0));
        h = ((127 * h) + ((_MotifData != null) ? _MotifData.hashCode() : 0));
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<Sequence-motif");
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
        if (_DisplayAuto != null) {
            sb.append(" display-auto=");
            sb.append(_DisplayAuto.toString());
        }
        if (_AutoView != null) {
            sb.append(" auto-view=");
            sb.append(_AutoView.id());
        }
        if (_Seqref != null) {
            sb.append(" seqref=");
            sb.append(_Seqref.id());
        }
        if (_Alignment != null) {
            sb.append(" alignment=");
            sb.append(_Alignment.toString());
        }
        if (_Startpos != null) {
            sb.append(" startpos=");
            sb.append(_Startpos.toString());
        }
        if (_Endpos != null) {
            sb.append(" endpos=");
            sb.append(_Endpos.toString());
        }
        if (_Refs != null) {
            sb.append(" refs=");
            sb.append(_Refs.toString());
        }
        if (_IntervalLoc != null) {
            sb.append(" Interval-loc=");
            sb.append(_IntervalLoc.toString());
        }
        if (_MotifData != null) {
            sb.append(" Motif-data=");
            sb.append(_MotifData.toString());
        }
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        return AlignmentPointSet.newDispatcher();
    }

    private static class IntervalLocPredicate implements PredicatedLists.Predicate {

        public void check(Object ob) {
            if (!(ob instanceof IntervalLoc)) {
                throw new InvalidContentObjectException(ob, (IntervalLoc.class));
            }
        }
    }

    private static class MotifDataPredicate implements PredicatedLists.Predicate {

        public void check(Object ob) {
            if (!(ob instanceof MotifData)) {
                throw new InvalidContentObjectException(ob, (MotifData.class));
            }
        }
    }

    private class AutoViewVPatcher extends Validator.Patcher {

        public void patch(IdentifiableElement target) {
            _AutoView = target;
        }
    }

    private class SeqrefVPatcher extends Validator.Patcher {

        public void patch(IdentifiableElement target) {
            _Seqref = target;
        }
    }
}
