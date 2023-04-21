package ru.adv.repository.dump;

import java.util.*;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Заголовок объекта для {@link DumpWriter} и {@link DumpReader}
 * User: vic
 * Date: 03.10.2003
 * Time: 15:31:32
 */
public class DumpObjectHeader {

    private String objectName;

    private LinkedList<TypeAttr> typeAttrs;

    private boolean sequence = false;

    private transient HashMap tmpIndexAttrs = null;

    /**
     * Конструктор для объекта типа таблица
     * @param objectName имя объекта
     */
    public DumpObjectHeader(String objectName) {
        this(objectName, false);
    }

    /**
     * Конструктор
     * @param objectName имя объекта
     * @param isSequence либо sequence либо таблица
     */
    public DumpObjectHeader(String objectName, boolean isSequence) {
        this.objectName = objectName;
        this.sequence = isSequence;
        typeAttrs = new LinkedList<TypeAttr>();
    }

    /**
     * объект является sequence, иначе простая таблица
     * @return
     */
    public boolean isSequence() {
        return sequence;
    }

    /**
     * Имя объекта
     * @return
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * Дабавляет атрибут в заголок объекта
     */
    public void addAttr(TypeAttr tAttr) {
        typeAttrs.add(tAttr);
        tmpIndexAttrs = null;
    }

    /**
     * Список {@link TypeAttr}
     * @return
     */
    public List<TypeAttr> getTypeAttrs() {
        return Collections.unmodifiableList(typeAttrs);
    }

    /**
     * Возвращает индекс {@link TypeAttr} в списке {@link #getTypeAttrs()} по имени атрибута
     * @param attrName имя атрибута
     * @return - -1 если не существует {@link TypeAttr} с таким именем
     */
    public int indexOfTypeAttr(String attrName) {
        initIndex();
        Integer i = (Integer) tmpIndexAttrs.get(attrName);
        if (i != null) {
            return i.intValue();
        }
        return -1;
    }

    private void initIndex() {
        if (tmpIndexAttrs == null) {
            tmpIndexAttrs = new HashMap();
            int index = 0;
            for (Iterator i = getTypeAttrs().iterator(); i.hasNext(); ) {
                TypeAttr attr = (TypeAttr) i.next();
                tmpIndexAttrs.put(attr.getAttrName(), new Integer(index++));
            }
        }
    }

    public String toString() {
        StringBuffer buff = new StringBuffer(20);
        buff.append("object=");
        buff.append(getObjectName());
        buff.append(", isSequence=");
        buff.append(isSequence());
        buff.append(", attrs=");
        buff.append(getTypeAttrs());
        return buff.toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DumpObjectHeader)) return false;
        final DumpObjectHeader dumpObjectHeader = (DumpObjectHeader) o;
        if (!objectName.equals(dumpObjectHeader.objectName)) return false;
        if (!typeAttrs.equals(dumpObjectHeader.typeAttrs)) return false;
        return true;
    }

    public int hashCode() {
        int result;
        result = objectName.hashCode();
        result = 29 * result + typeAttrs.hashCode();
        return result;
    }

    public void write(ObjectOutputStream stream) throws IOException {
        stream.writeByte(DumpHeader.SOH);
        stream.writeUTF(this.getObjectName());
        stream.writeBoolean(this.isSequence());
        List list = this.getTypeAttrs();
        stream.writeInt(list.size());
        for (Iterator i = list.iterator(); i.hasNext(); ) {
            TypeAttr attr = (TypeAttr) i.next();
            stream.writeUTF(attr.getAttrName());
            stream.writeInt(attr.getType());
        }
        stream.writeByte(DumpHeader.EOH);
    }

    public static DumpObjectHeader read(ObjectInputStream stream) throws IOException {
        if (stream.readUnsignedByte() != DumpHeader.SOH) {
            throw new IOException("Invalid start of dump object header marker");
        }
        String objectName = stream.readUTF();
        boolean sequence = stream.readBoolean();
        DumpObjectHeader result = new DumpObjectHeader(objectName, sequence);
        int length = stream.readInt();
        for (int i = 0; i < length; ++i) {
            String attrName = stream.readUTF();
            int type = stream.readInt();
            result.addAttr(new TypeAttr(attrName, type));
        }
        if (stream.readUnsignedByte() != DumpHeader.EOH) {
            throw new IOException("Invalid end of dump object header marker");
        }
        return result;
    }
}
