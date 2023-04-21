package org.vtt.stylebase.model;

import java.util.Iterator;
import java.util.LinkedList;

public class Pattern {

    public static final int TYPE_NONE = 0;

    public static final int TYPE_DESIGN = 1;

    public static final int TYPE_ARCHITECTURAL = 2;

    private int id = 0;

    private int type = 0;

    private String lockedby = null;

    private String name = null;

    private String description = null;

    private LinkedList attributeList = null;

    Pattern(int id, String name) {
        this.id = id;
        this.name = name;
        this.description = new String("");
        this.attributeList = new LinkedList();
    }

    Pattern(int id, String name, String description, int type, String lockedby) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.lockedby = lockedby;
        this.attributeList = new LinkedList();
    }

    public boolean isLocked() {
        if (this.lockedby != null) {
            if (this.lockedby.length() > 0) return true;
        }
        return false;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Object[] getQualityAttributeArray() {
        return this.attributeList.toArray(new QualityAttribute[this.attributeList.size()]);
    }

    public LinkedList getQualityAttributeList() {
        return this.attributeList;
    }

    public Object[] getQualityAttributesWithDefinitions(AttributeDefinitions definitions) {
        LinkedList newList = new LinkedList();
        definitions.copyList(newList);
        QualityAttribute newItem = null;
        QualityAttribute attribute = null;
        Iterator newListIter = newList.iterator();
        while (newListIter.hasNext()) {
            newItem = (QualityAttribute) newListIter.next();
            Iterator attributeIter = this.attributeList.iterator();
            while (attributeIter.hasNext()) {
                attribute = (QualityAttribute) attributeIter.next();
                if (newItem.getId() == attribute.getId()) {
                    newItem.setRationale(attribute.getRationale());
                    attributeIter = null;
                    break;
                }
            }
        }
        return newList.toArray(new QualityAttribute[newList.size()]);
    }

    public int getId() {
        return this.id;
    }

    public int getType() {
        return this.type;
    }

    public String getLockedBy() {
        return this.lockedby;
    }

    public boolean equals(Object object) {
        if (object == null) return false;
        if (!(object instanceof Pattern)) return false;
        int newId = ((Pattern) object).getId();
        if (this.id == newId) return true; else return false;
    }

    public int hashCode() {
        return this.id;
    }

    void clearAttributeList() {
        this.attributeList.clear();
    }

    void setLockedBy(String lockedby) {
        this.lockedby = lockedby;
    }

    void addQualityAttribute(QualityAttribute attribute) {
        this.attributeList.add(attribute);
    }

    void setAttributes(LinkedList attributes) {
        if (attributes != null) this.attributeList = attributes;
    }

    void setValues(String name, String description, int type, String lockedby) {
        if (name != null) this.name = name;
        if (description != null) this.description = description;
        this.type = type;
        if (lockedby != null) this.lockedby = lockedby;
    }

    public static String[] getPatternTypes() {
        String[] types = { "None", "Concrete", "Conceptual" };
        return types;
    }
}
