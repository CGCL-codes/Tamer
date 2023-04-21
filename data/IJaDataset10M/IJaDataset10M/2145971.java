package freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.glossary.data.xmlBinding;

import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.glossary.data.xmlBinding.ViewElementLink;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.ConversionException;
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
import javax.xml.bind.NoValueException;
import javax.xml.bind.PredicatedLists;
import javax.xml.bind.PredicatedLists.Predicate;
import javax.xml.bind.StructureValidationException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidatableObject;
import javax.xml.bind.Validator;
import javax.xml.marshal.XMLScanner;
import javax.xml.marshal.XMLWriter;

public class ViewElement extends MarshallableObject implements Element, IdentifiableElement {

    private String _Id;

    private String _ParentId;

    private String _Title;

    private String _Type;

    private String _LastModificationDate;

    private String _HtmlFileName;

    private List _LearningUnitViewElementLinks = PredicatedLists.createInvalidating(this, new LearningUnitViewElementLinksPredicate(), new ArrayList());

    private PredicatedLists.Predicate pred_LearningUnitViewElementLinks = new LearningUnitViewElementLinksPredicate();

    private boolean _Folder;

    private boolean has_Folder;

    private boolean _MemoryUsable;

    private boolean has_MemoryUsable;

    private int _SuccessfulTries;

    private boolean has_SuccessfulTries;

    private int _Tries;

    private boolean has_Tries;

    private long _LastTry;

    private boolean has_LastTry;

    private int _Category;

    private boolean has_Category;

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
        return _Id.toString();
    }

    public String getParentId() {
        return _ParentId;
    }

    public void setParentId(String _ParentId) {
        this._ParentId = _ParentId;
        if (_ParentId == null) {
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

    public String getType() {
        return _Type;
    }

    public void setType(String _Type) {
        this._Type = _Type;
        if (_Type == null) {
            invalidate();
        }
    }

    public String getLastModificationDate() {
        return _LastModificationDate;
    }

    public void setLastModificationDate(String _LastModificationDate) {
        this._LastModificationDate = _LastModificationDate;
        if (_LastModificationDate == null) {
            invalidate();
        }
    }

    public String getHtmlFileName() {
        return _HtmlFileName;
    }

    public void setHtmlFileName(String _HtmlFileName) {
        this._HtmlFileName = _HtmlFileName;
        if (_HtmlFileName == null) {
            invalidate();
        }
    }

    public List getLearningUnitViewElementLinks() {
        return _LearningUnitViewElementLinks;
    }

    public void deleteLearningUnitViewElementLinks() {
        _LearningUnitViewElementLinks = null;
        invalidate();
    }

    public void emptyLearningUnitViewElementLinks() {
        _LearningUnitViewElementLinks = PredicatedLists.createInvalidating(this, pred_LearningUnitViewElementLinks, new ArrayList());
    }

    public boolean getFolder() {
        if (has_Folder) {
            return _Folder;
        }
        throw new NoValueException("folder");
    }

    public void setFolder(boolean _Folder) {
        this._Folder = _Folder;
        has_Folder = true;
        invalidate();
    }

    public boolean hasFolder() {
        return has_Folder;
    }

    public void deleteFolder() {
        has_Folder = false;
        invalidate();
    }

    public boolean getMemoryUsable() {
        if (has_MemoryUsable) {
            return _MemoryUsable;
        }
        throw new NoValueException("memoryUsable");
    }

    public void setMemoryUsable(boolean _MemoryUsable) {
        this._MemoryUsable = _MemoryUsable;
        has_MemoryUsable = true;
        invalidate();
    }

    public boolean hasMemoryUsable() {
        return has_MemoryUsable;
    }

    public void deleteMemoryUsable() {
        has_MemoryUsable = false;
        invalidate();
    }

    public int getSuccessfulTries() {
        if (has_SuccessfulTries) {
            return _SuccessfulTries;
        }
        throw new NoValueException("successfulTries");
    }

    public void setSuccessfulTries(int _SuccessfulTries) {
        this._SuccessfulTries = _SuccessfulTries;
        has_SuccessfulTries = true;
        invalidate();
    }

    public boolean hasSuccessfulTries() {
        return has_SuccessfulTries;
    }

    public void deleteSuccessfulTries() {
        has_SuccessfulTries = false;
        invalidate();
    }

    public int getTries() {
        if (has_Tries) {
            return _Tries;
        }
        throw new NoValueException("tries");
    }

    public void setTries(int _Tries) {
        this._Tries = _Tries;
        has_Tries = true;
        invalidate();
    }

    public boolean hasTries() {
        return has_Tries;
    }

    public void deleteTries() {
        has_Tries = false;
        invalidate();
    }

    public long getLastTry() {
        if (has_LastTry) {
            return _LastTry;
        }
        throw new NoValueException("lastTry");
    }

    public void setLastTry(long _LastTry) {
        this._LastTry = _LastTry;
        has_LastTry = true;
        invalidate();
    }

    public boolean hasLastTry() {
        return has_LastTry;
    }

    public void deleteLastTry() {
        has_LastTry = false;
        invalidate();
    }

    public int getCategory() {
        if (has_Category) {
            return _Category;
        }
        throw new NoValueException("category");
    }

    public void setCategory(int _Category) {
        this._Category = _Category;
        has_Category = true;
        invalidate();
    }

    public boolean hasCategory() {
        return has_Category;
    }

    public void deleteCategory() {
        has_Category = false;
        invalidate();
    }

    public void validateThis() throws LocalValidationException {
        if (_Id == null) {
            throw new MissingAttributeException("id");
        }
        if (_ParentId == null) {
            throw new MissingAttributeException("parentId");
        }
        if (_Title == null) {
            throw new MissingAttributeException("title");
        }
        if (_Type == null) {
            throw new MissingAttributeException("type");
        }
        if (!has_Folder) {
            throw new MissingAttributeException("folder");
        }
    }

    public void validate(Validator v) throws StructureValidationException {
        for (Iterator i = _LearningUnitViewElementLinks.iterator(); i.hasNext(); ) {
            v.validate(((ValidatableObject) i.next()));
        }
    }

    public void marshal(Marshaller m) throws IOException {
        XMLWriter w = m.writer();
        w.start("viewElement");
        w.attribute("id", _Id.toString());
        w.attribute("parentId", _ParentId.toString());
        w.attribute("title", _Title.toString());
        w.attribute("type", _Type.toString());
        if (_LastModificationDate != null) {
            w.attribute("lastModificationDate", _LastModificationDate.toString());
        }
        if (_HtmlFileName != null) {
            w.attribute("htmlFileName", _HtmlFileName.toString());
        }
        w.attribute("folder", printBoolean(getFolder()));
        if (has_MemoryUsable) {
            w.attribute("memoryUsable", printBoolean(getMemoryUsable()));
        }
        if (has_SuccessfulTries) {
            w.attribute("successfulTries", Integer.toString(getSuccessfulTries()));
        }
        if (has_Tries) {
            w.attribute("tries", Integer.toString(getTries()));
        }
        if (has_LastTry) {
            w.attribute("lastTry", Long.toString(getLastTry()));
        }
        if (has_Category) {
            w.attribute("category", Integer.toString(getCategory()));
        }
        if (_LearningUnitViewElementLinks.size() > 0) {
            for (Iterator i = _LearningUnitViewElementLinks.iterator(); i.hasNext(); ) {
                m.marshal(((MarshallableObject) i.next()));
            }
        }
        w.end("viewElement");
    }

    private static String printBoolean(boolean f) {
        return (f ? "true" : "false");
    }

    public void unmarshal(Unmarshaller u) throws UnmarshalException {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("viewElement");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            if (an.equals("id")) {
                if (_Id != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Id = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("parentId")) {
                if (_ParentId != null) {
                    throw new DuplicateAttributeException(an);
                }
                _ParentId = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("title")) {
                if (_Title != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Title = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("type")) {
                if (_Type != null) {
                    throw new DuplicateAttributeException(an);
                }
                _Type = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("lastModificationDate")) {
                if (_LastModificationDate != null) {
                    throw new DuplicateAttributeException(an);
                }
                _LastModificationDate = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("htmlFileName")) {
                if (_HtmlFileName != null) {
                    throw new DuplicateAttributeException(an);
                }
                _HtmlFileName = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("folder")) {
                if (has_Folder) {
                    throw new DuplicateAttributeException(an);
                }
                try {
                    _Folder = readBoolean(xs.takeAttributeValue());
                } catch (Exception x) {
                    throw new ConversionException(an, x);
                }
                has_Folder = true;
                continue;
            }
            if (an.equals("memoryUsable")) {
                if (has_MemoryUsable) {
                    throw new DuplicateAttributeException(an);
                }
                try {
                    _MemoryUsable = readBoolean(xs.takeAttributeValue());
                } catch (Exception x) {
                    throw new ConversionException(an, x);
                }
                has_MemoryUsable = true;
                continue;
            }
            if (an.equals("successfulTries")) {
                if (has_SuccessfulTries) {
                    throw new DuplicateAttributeException(an);
                }
                try {
                    _SuccessfulTries = Integer.parseInt(xs.takeAttributeValue());
                } catch (Exception x) {
                    throw new ConversionException(an, x);
                }
                has_SuccessfulTries = true;
                continue;
            }
            if (an.equals("tries")) {
                if (has_Tries) {
                    throw new DuplicateAttributeException(an);
                }
                try {
                    _Tries = Integer.parseInt(xs.takeAttributeValue());
                } catch (Exception x) {
                    throw new ConversionException(an, x);
                }
                has_Tries = true;
                continue;
            }
            if (an.equals("lastTry")) {
                if (has_LastTry) {
                    throw new DuplicateAttributeException(an);
                }
                try {
                    _LastTry = Long.parseLong(xs.takeAttributeValue());
                } catch (Exception x) {
                    throw new ConversionException(an, x);
                }
                has_LastTry = true;
                continue;
            }
            if (an.equals("category")) {
                if (has_Category) {
                    throw new DuplicateAttributeException(an);
                }
                try {
                    _Category = Integer.parseInt(xs.takeAttributeValue());
                } catch (Exception x) {
                    throw new ConversionException(an, x);
                }
                has_Category = true;
                continue;
            }
            throw new InvalidAttributeException(an);
        }
        {
            List l = PredicatedLists.create(this, pred_LearningUnitViewElementLinks, new ArrayList());
            while (xs.atStart("viewElementLink")) {
                l.add(((ViewElementLink) u.unmarshal()));
            }
            _LearningUnitViewElementLinks = PredicatedLists.createInvalidating(this, pred_LearningUnitViewElementLinks, l);
        }
        xs.takeEnd("viewElement");
    }

    private static boolean readBoolean(String s) throws ConversionException {
        if (s.equals("true")) {
            return true;
        }
        if (s.equals("false")) {
            return false;
        }
        throw new ConversionException(s);
    }

    public static ViewElement unmarshal(InputStream in) throws UnmarshalException {
        return unmarshal(XMLScanner.open(in));
    }

    public static ViewElement unmarshal(XMLScanner xs) throws UnmarshalException {
        return unmarshal(xs, newDispatcher());
    }

    public static ViewElement unmarshal(XMLScanner xs, Dispatcher d) throws UnmarshalException {
        return ((ViewElement) d.unmarshal(xs, (ViewElement.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof ViewElement)) {
            return false;
        }
        ViewElement tob = ((ViewElement) ob);
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
        if (_ParentId != null) {
            if (tob._ParentId == null) {
                return false;
            }
            if (!_ParentId.equals(tob._ParentId)) {
                return false;
            }
        } else {
            if (tob._ParentId != null) {
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
        if (_LastModificationDate != null) {
            if (tob._LastModificationDate == null) {
                return false;
            }
            if (!_LastModificationDate.equals(tob._LastModificationDate)) {
                return false;
            }
        } else {
            if (tob._LastModificationDate != null) {
                return false;
            }
        }
        if (_HtmlFileName != null) {
            if (tob._HtmlFileName == null) {
                return false;
            }
            if (!_HtmlFileName.equals(tob._HtmlFileName)) {
                return false;
            }
        } else {
            if (tob._HtmlFileName != null) {
                return false;
            }
        }
        if (_LearningUnitViewElementLinks != null) {
            if (tob._LearningUnitViewElementLinks == null) {
                return false;
            }
            if (!_LearningUnitViewElementLinks.equals(tob._LearningUnitViewElementLinks)) {
                return false;
            }
        } else {
            if (tob._LearningUnitViewElementLinks != null) {
                return false;
            }
        }
        if (has_Folder) {
            if (!tob.has_Folder) {
                return false;
            }
            if (_Folder != tob._Folder) {
                return false;
            }
        } else {
            if (tob.has_Folder) {
                return false;
            }
        }
        if (has_MemoryUsable) {
            if (!tob.has_MemoryUsable) {
                return false;
            }
            if (_MemoryUsable != tob._MemoryUsable) {
                return false;
            }
        } else {
            if (tob.has_MemoryUsable) {
                return false;
            }
        }
        if (has_SuccessfulTries) {
            if (!tob.has_SuccessfulTries) {
                return false;
            }
            if (_SuccessfulTries != tob._SuccessfulTries) {
                return false;
            }
        } else {
            if (tob.has_SuccessfulTries) {
                return false;
            }
        }
        if (has_Tries) {
            if (!tob.has_Tries) {
                return false;
            }
            if (_Tries != tob._Tries) {
                return false;
            }
        } else {
            if (tob.has_Tries) {
                return false;
            }
        }
        if (has_LastTry) {
            if (!tob.has_LastTry) {
                return false;
            }
            if (_LastTry != tob._LastTry) {
                return false;
            }
        } else {
            if (tob.has_LastTry) {
                return false;
            }
        }
        if (has_Category) {
            if (!tob.has_Category) {
                return false;
            }
            if (_Category != tob._Category) {
                return false;
            }
        } else {
            if (tob.has_Category) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int h = 0;
        h = ((127 * h) + ((_Id != null) ? _Id.hashCode() : 0));
        h = ((127 * h) + ((_ParentId != null) ? _ParentId.hashCode() : 0));
        h = ((127 * h) + ((_Title != null) ? _Title.hashCode() : 0));
        h = ((127 * h) + ((_Type != null) ? _Type.hashCode() : 0));
        h = ((127 * h) + ((_LastModificationDate != null) ? _LastModificationDate.hashCode() : 0));
        h = ((127 * h) + ((_HtmlFileName != null) ? _HtmlFileName.hashCode() : 0));
        h = ((127 * h) + ((_LearningUnitViewElementLinks != null) ? _LearningUnitViewElementLinks.hashCode() : 0));
        h = ((31 * h) + (_Folder ? 137 : 139));
        h = ((31 * h) + (_MemoryUsable ? 137 : 139));
        h = ((31 * h) + _SuccessfulTries);
        h = ((31 * h) + _Tries);
        h = ((31 * h) + ((int) (_LastTry ^ (_LastTry >> 32))));
        h = ((31 * h) + _Category);
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<viewElement");
        if (_Id != null) {
            sb.append(" id=");
            sb.append(_Id.toString());
        }
        if (_ParentId != null) {
            sb.append(" parentId=");
            sb.append(_ParentId.toString());
        }
        if (_Title != null) {
            sb.append(" title=");
            sb.append(_Title.toString());
        }
        if (_Type != null) {
            sb.append(" type=");
            sb.append(_Type.toString());
        }
        if (_LastModificationDate != null) {
            sb.append(" lastModificationDate=");
            sb.append(_LastModificationDate.toString());
        }
        if (_HtmlFileName != null) {
            sb.append(" htmlFileName=");
            sb.append(_HtmlFileName.toString());
        }
        if (_LearningUnitViewElementLinks != null) {
            sb.append(" viewElementLink=");
            sb.append(_LearningUnitViewElementLinks.toString());
        }
        if (has_Folder) {
            sb.append(" folder=");
            sb.append(printBoolean(_Folder));
        }
        if (has_MemoryUsable) {
            sb.append(" memoryUsable=");
            sb.append(printBoolean(_MemoryUsable));
        }
        if (has_SuccessfulTries) {
            sb.append(" successfulTries=");
            sb.append(Integer.toString(_SuccessfulTries));
        }
        if (has_Tries) {
            sb.append(" tries=");
            sb.append(Integer.toString(_Tries));
        }
        if (has_LastTry) {
            sb.append(" lastTry=");
            sb.append(Long.toString(_LastTry));
        }
        if (has_Category) {
            sb.append(" category=");
            sb.append(Integer.toString(_Category));
        }
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        return GlossaryDescriptor.newDispatcher();
    }

    private static class LearningUnitViewElementLinksPredicate implements PredicatedLists.Predicate {

        public void check(Object ob) {
            if (!(ob instanceof ViewElementLink)) {
                throw new InvalidContentObjectException(ob, (ViewElementLink.class));
            }
        }
    }
}
