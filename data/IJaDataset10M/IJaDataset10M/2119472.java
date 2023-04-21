package fr.fg.server.data.base;

import fr.fg.server.dao.PersistentData;

public class ContractRewardBase extends PersistentData {

    public static final String TYPE_XP = "xp", TYPE_FLEET_XP = "fleet_xp", TYPE_RESOURCE = "resource", TYPE_SHIP = "ship";

    private long id;

    private long idContract;

    private String type;

    private int keyName;

    private long value;

    public long getIdContract() {
        return idContract;
    }

    public void setIdContract(long idContract) {
        if (!isEditable()) throwDataUneditableException();
        this.idContract = idContract;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (!isEditable()) throwDataUneditableException();
        if (type.equals(TYPE_XP)) this.type = TYPE_XP; else if (type.equals(TYPE_FLEET_XP)) this.type = TYPE_FLEET_XP; else if (type.equals(TYPE_RESOURCE)) this.type = TYPE_RESOURCE; else if (type.equals(TYPE_SHIP)) this.type = TYPE_SHIP; else throw new IllegalArgumentException("Invalid value: '" + type + "'.");
    }

    public int getKeyName() {
        return keyName;
    }

    public void setKeyName(int keyName) {
        if (!isEditable()) throwDataUneditableException();
        this.keyName = keyName;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        if (!isEditable()) throwDataUneditableException();
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        if (!isEditable()) throwDataUneditableException();
        this.id = id;
    }
}
