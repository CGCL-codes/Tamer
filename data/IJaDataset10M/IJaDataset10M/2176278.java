package luz.dsexplorer.datastructures;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.tree.TreeNode;
import luz.dsexplorer.exceptions.NoProcessException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;

@Root
public class Result implements TreeNode, DSListener, Cloneable {

    private ResultList resultList;

    @Attribute(required = false)
    private Long address;

    @Element
    private Datastructure datastructure;

    private int fieldIndex;

    private Object valueCache = null;

    private boolean valueCacheOK = false;

    private Long pointerCache = null;

    private boolean pointerCacheOK = false;

    private String staticAddr = null;

    private boolean staticAddrCacheOK = false;

    protected Result() {
    }

    public Result(ResultList resultList, Datastructure datastructure, long address, Object value) {
        this.resultList = resultList;
        this.address = address;
        this.valueCache = value;
        if (value != null) this.valueCacheOK = true;
        this.datastructure = datastructure;
        this.datastructure.addListener(this);
    }

    public Result(ResultList resultList, Result parent, Datastructure datastructure, int fieldIndex) {
        this.resultList = resultList;
        this.parent = parent;
        this.fieldIndex = fieldIndex;
        this.datastructure = datastructure;
        this.datastructure.addListener(this);
    }

    public Result(Datastructure datastructure) {
        this.datastructure = datastructure;
        this.datastructure.addListener(this);
    }

    public void setDatastructure(Datastructure newDS) {
        Datastructure oldDS = this.datastructure;
        if (isSimpleResult()) {
            log.debug("change ds");
            oldDS.removeListener(this);
            removeAllChilds();
            valueCacheOK = false;
            this.datastructure = newDS;
            newDS.addListener(this);
            if (!newDS.isContainer()) newDS.setName(oldDS.getName());
            getResultList().reload(this);
        } else {
            log.debug("change field");
            Container dsParent = (Container) ((Result) parent).getDatastructure();
            dsParent.replaceField(oldDS, newDS, fieldIndex);
        }
    }

    public Datastructure getDatastructure() {
        return datastructure;
    }

    public void setResultList(ResultList resultList) {
        log.debug("set resultList " + resultList + " on " + this.hashCode());
        this.resultList = resultList;
    }

    public ResultList getResultList() {
        return resultList;
    }

    public void setAddress(Long address) {
        if (isSimpleResult()) {
            this.address = address;
            invalidateParentAndChilds();
        }
    }

    public String getStatic() {
        if (!staticAddrCacheOK) {
            Long addr = getAddress();
            if (addr != null) staticAddr = getResultList().getStatic(addr);
            staticAddrCacheOK = true;
        }
        return staticAddr;
    }

    public Long getAddress() {
        if (isSimpleResult()) {
            return address;
        } else {
            Container dsParent = (Container) ((Result) parent).getDatastructure();
            Long address;
            if (dsParent.isPointer()) address = ((Result) parent).getPointer(); else address = ((Result) parent).getAddress();
            if (address != null) {
                address += dsParent.getOffset(fieldIndex);
            }
            return address;
        }
    }

    public String getAddressString() {
        Long p = getAddress();
        return p == null ? null : String.format("%1$08X", p);
    }

    private Long getPointer() {
        if (datastructure.isContainer()) {
            Container c = (Container) datastructure;
            if (c.isPointer()) {
                if (pointerCacheOK) return pointerCache;
                Memory buffer = new Memory(4);
                try {
                    log.trace("Pointer: " + getAddressString());
                    getResultList().ReadProcessMemory(Pointer.createConstant(getAddress()), buffer, (int) buffer.getSize(), null);
                    pointerCache = (long) buffer.getInt(0);
                } catch (NoProcessException e) {
                    pointerCache = null;
                } catch (Exception e) {
                    log.warn(e);
                    pointerCache = null;
                }
                pointerCacheOK = true;
                return pointerCache;
            }
        }
        return null;
    }

    public String getPointerString() {
        Long p = getPointer();
        return p == null ? null : String.format("%1$08X", p);
    }

    public Object getValue() {
        if (datastructure.isContainer()) return null;
        if (valueCacheOK) return valueCache;
        Memory buffer = new Memory(datastructure.getByteCount());
        try {
            Long address = getAddress();
            if (address != null && address != 0) {
                log.trace("Read: " + getAddressString());
                getResultList().ReadProcessMemory(Pointer.createConstant(address), buffer, (int) buffer.getSize(), null);
                valueCache = datastructure.eval(buffer);
            } else {
                valueCache = null;
            }
        } catch (NoProcessException e) {
            valueCache = null;
        } catch (Exception e) {
            log.warn("Cannot Read: " + getAddressString());
            valueCache = null;
        }
        valueCacheOK = true;
        return valueCache;
    }

    public String getValueString() {
        Object v = getValue();
        return v == null ? null : getDatastructure().valueToString(v);
    }

    public void delete() {
        if (isSimpleResult()) {
            getResultList().remove(this);
        } else {
            Container dsParent = (Container) ((Result) parent).getDatastructure();
            dsParent.removeField(fieldIndex);
        }
    }

    public byte[] getMemoryBytes(long low, long high) {
        Memory buffer = new Memory(high - low);
        byte[] value = null;
        try {
            getResultList().ReadProcessMemory(Pointer.createConstant(low), buffer, (int) buffer.getSize(), null);
            value = buffer.getByteArray(0, (int) buffer.getSize());
        } catch (NoProcessException e) {
        } catch (Exception e) {
        }
        return value;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public void invalidateParentAndChilds() {
        valueCacheOK = false;
        pointerCacheOK = false;
        staticAddrCacheOK = false;
        getResultList().nodeChanged(this);
        if (childs != null) for (Result r : childs) r.invalidateParentAndChilds();
    }

    private void invalidateFollowingSiblings() {
        if (parent instanceof Result) {
            List<Result> siblings = ((Result) parent).childs;
            if (siblings != null) for (int i = siblings.indexOf(this) + 1; i < siblings.size(); i++) siblings.get(i).invalidateParentAndChilds();
        }
    }

    private void invalidateFollowingChilds(int index) {
        if (childs != null) for (int i = index + 1; i < childs.size(); i++) childs.get(i).invalidateParentAndChilds();
    }

    @Override
    public void hasChanged() {
        valueCacheOK = false;
        staticAddrCacheOK = false;
        getResultList().nodeChanged(this);
    }

    @Override
    public void pointerChanged(boolean pointer) {
        invalidateParentAndChilds();
        invalidateFollowingSiblings();
        getResultList().nodeChanged(this);
    }

    @Override
    public void addedField(Datastructure field, int fieldIndex) {
        if (childs != null) {
            Result r = new Result(resultList, this, field, fieldIndex);
            childs.add(fieldIndex, r);
            for (int i = fieldIndex + 1; i < childs.size(); i++) childs.get(i).fieldIndex++;
            getResultList().nodeInserted(r);
        }
        invalidateFollowingSiblings();
    }

    @Override
    public void removedField(int fieldIndex) {
        if (childs != null) {
            log.debug("field removed");
            Result child = childs.get(fieldIndex);
            if (child.fieldIndex == fieldIndex) {
                childs.remove(fieldIndex);
                for (int i = fieldIndex; i < childs.size(); i++) childs.get(i).fieldIndex--;
                getResultList().nodeRemoved(child, fieldIndex);
                invalidateFollowingChilds(fieldIndex - 1);
            }
        }
    }

    @Override
    public void replacedField(Datastructure oldField, Datastructure newField, int fieldIndex) {
        if (childs != null) {
            log.debug("replaced Field");
            Result child = childs.get(fieldIndex);
            if (child.fieldIndex == fieldIndex) {
                oldField.removeListener(child);
                child.removeAllChilds();
                child.valueCacheOK = false;
                child.datastructure = newField;
                newField.addListener(child);
                if (!newField.isContainer()) newField.setName(oldField.getName());
                getResultList().reload(child);
            }
        }
        invalidateFollowingChilds(fieldIndex);
    }

    private static final Log log = LogFactory.getLog(Result.class);

    private void defineChildNodes() {
        if (!isLeaf() && childs == null) {
            childs = new LinkedList<Result>();
            if (datastructure.isContainer()) {
                List<Datastructure> fields = ((Container) datastructure).getFields();
                for (int i = 0; i < fields.size(); i++) {
                    Result r = new Result(resultList, this, fields.get(i), i);
                    childs.add(r);
                }
            }
        }
    }

    private void removeAllChilds() {
        childs = null;
    }

    public boolean isSimpleResult() {
        return parent.equals(resultList);
    }

    private TreeNode parent;

    private List<Result> childs;

    @Override
    public boolean isLeaf() {
        return !datastructure.isContainer();
    }

    @Override
    public int getChildCount() {
        defineChildNodes();
        return childs.size();
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public boolean getAllowsChildren() {
        return !datastructure.isContainer();
    }

    @Override
    public Enumeration<Result> children() {
        return new Enumeration<Result>() {

            Iterator<Result> iter = childs.iterator();

            public boolean hasMoreElements() {
                return iter.hasNext();
            }

            public Result nextElement() {
                return iter.next();
            }
        };
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        defineChildNodes();
        return childs.get(childIndex);
    }

    @Override
    public int getIndex(TreeNode node) {
        defineChildNodes();
        return childs.indexOf(node);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getAddressString()).append(' ');
        sb.append('[').append(getDatastructure().getName()).append("] ");
        if (!datastructure.isContainer()) {
            sb.append(datastructure.valueToString(getValue()));
        } else {
            if (((Container) datastructure).isPointer()) sb.append(getPointerString());
        }
        return sb.toString();
    }

    @Override
    public Result clone() {
        try {
            Result clone = (Result) super.clone();
            this.getDatastructure().removeListener(this);
            this.getDatastructure().addListener(clone);
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
