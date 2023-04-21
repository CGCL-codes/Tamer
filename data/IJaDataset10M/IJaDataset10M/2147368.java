package com.golemgame.mvc.golems;

import java.util.Collection;
import com.golemgame.mvc.CollectionType;
import com.golemgame.mvc.DataType;
import com.golemgame.mvc.PropertyStore;
import com.golemgame.mvc.Reference;
import com.golemgame.mvc.ReferenceType;
import com.jme.renderer.ColorRGBA;

public class GroupInterpreter extends StoreInterpreter {

    public static final String MEMBERS = "members";

    public static final String COLOR = "color";

    public static final String GROUP_NAME = "group.name";

    public static final String NUMBER = "number";

    public GroupInterpreter() {
        this(new PropertyStore());
    }

    public GroupInterpreter(PropertyStore store) {
        super(store);
        getStore().setClassName(GolemsClassRepository.GROUP_CLASS);
    }

    @Override
    public Collection<String> enumerateKeys(Collection<String> keys) {
        keys.add(MEMBERS);
        keys.add(COLOR);
        keys.add(GROUP_NAME);
        keys.add(NUMBER);
        return super.enumerateKeys(keys);
    }

    @Override
    public DataType getDefaultValue(String key) {
        if (key.equals(MEMBERS)) return defaultCollection;
        if (key.equals(COLOR)) return defaultColor;
        if (key.equals(GROUP_NAME)) return defaultString;
        if (key.equals(NUMBER)) return defaultInt;
        return super.getDefaultValue(key);
    }

    public int getNumber() {
        return getStore().getInt(NUMBER, 0);
    }

    public void setNumber(int number) {
        getStore().setProperty(NUMBER, number);
    }

    public ColorRGBA getColor() {
        return getStore().getColor(COLOR, new ColorRGBA());
    }

    public void setColor(ColorRGBA color) {
        getStore().setProperty(COLOR, color);
    }

    public String getGroupName() {
        return getStore().getString(GROUP_NAME);
    }

    public void setGroupName(String name) {
        getStore().setProperty(GROUP_NAME, name);
    }

    public CollectionType getMembers() {
        return getStore().getCollectionType(MEMBERS);
    }

    public void addMember(Reference member) {
        getMembers().addElement(new ReferenceType(member));
    }

    public void removeMember(Reference member) {
        DataType toRemove = null;
        for (DataType data : getMembers().getValues()) {
            if (data.getType() == DataType.Type.REFERENCE) {
                if (((ReferenceType) data).getID().equals(member)) {
                    toRemove = data;
                    break;
                }
            }
        }
        getMembers().removeElement(toRemove);
    }
}
