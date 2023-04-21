package seedpod.model;

import java.util.Collection;
import java.util.Iterator;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Slot;

public class RdbCls {

    private Cls _cls;

    private KnowledgeBase _kb;

    private Cls _clsMetaCls;

    private boolean _isRdbCls;

    private SeedpodSystemFrames _systemFrames;

    public RdbCls(Cls cls, KnowledgeBase kb) {
        _cls = cls;
        _kb = kb;
        _systemFrames = new SeedpodSystemFrames(_kb);
        _clsMetaCls = cls.getDirectType();
        _isRdbCls = _cls.hasType(_kb.getCls(SeedpodModel.DEFAULT_META_RDB_CLASS));
    }

    public String getName() {
        return _cls.getName();
    }

    public Cls getCls() {
        return _cls;
    }

    public boolean isInline() {
        Slot inlineSlot = _systemFrames.getIsInlineSlot();
        Boolean isInline = (Boolean) _cls.getDirectOwnSlotValue(inlineSlot);
        return (isInline == null) ? false : isInline.booleanValue();
    }

    public Instance getDataSource() {
        Slot dataSourceSlot = _systemFrames.getDataSourceSlot();
        Instance sourceIns = (Instance) _cls.getDirectOwnSlotValue(dataSourceSlot);
        return sourceIns;
    }

    public String getUserDefinedName() {
        Slot userNameSlot = _systemFrames.getUserAssignedNameSlot();
        return (String) _cls.getDirectOwnSlotValue(userNameSlot);
    }

    public boolean isRdbCls() {
        return _isRdbCls;
    }

    public static boolean isRdbCls(Cls cls) {
        KnowledgeBase kb = cls.getKnowledgeBase();
        return (cls.hasType(kb.getCls(SeedpodModel.DEFAULT_META_RDB_CLASS)));
    }

    public Cls getClsMetaCls() {
        return _clsMetaCls;
    }

    @SuppressWarnings("unchecked")
    public Collection getTemplateSlots() {
        return _cls.getTemplateSlots();
    }

    public Cls getParentCls() {
        return (Cls) getFirst(_cls.getSuperclasses());
    }

    public void setUserDefinedName(String name) {
        Slot userNameSlot = _systemFrames.getUserAssignedNameSlot();
        _cls.setDirectOwnSlotValue(userNameSlot, name);
    }

    @SuppressWarnings("unchecked")
    private static Object getFirst(Collection collection) {
        Iterator iter = collection.iterator();
        if (iter.hasNext()) return iter.next(); else return null;
    }

    public boolean equals(Object other) {
        RdbCls otherCls = (RdbCls) other;
        return (this.getCls().getName().compareTo(otherCls.getCls().getName()) == 0);
    }

    public int hashCode() {
        return (31 + _cls.getName().hashCode());
    }
}
