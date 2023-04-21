package libsecondlife.inventorysystem;

import libsecondlife.*;
import java.util.Vector;
import java.util.Hashtable;

public class InventoryFolder extends InventoryBase {

    public String getName() {
        return _Name;
    }

    public void setName(String value) throws Exception {
        _Name = value;
        iManager.FolderRename(this);
    }

    private LLUUID _FolderID;

    public LLUUID getFolderID() {
        return _FolderID;
    }

    private LLUUID _ParentID;

    public LLUUID getParentID() {
        return _ParentID;
    }

    public void setParentID(LLUUID value) throws Exception {
        InventoryFolder ifParent = iManager.getFolder(this.getParentID());
        ifParent.alContents.remove(this);
        ifParent = iManager.getFolder(value);
        ifParent.alContents.addElement(this);
        this._ParentID = value;
        iManager.FolderMove(this, value);
    }

    byte _Type;

    public byte getType() {
        return _Type;
    }

    public Vector alContents = new Vector();

    InventoryFolder(InventoryManager manager) throws Exception {
        super(manager);
        _Name = "";
        _FolderID = new LLUUID();
        _ParentID = new LLUUID();
        _Type = -1;
    }

    InventoryFolder(InventoryManager manager, String name, LLUUID folderID, LLUUID parentID) throws Exception {
        super(manager);
        this._Name = name;
        this._FolderID = folderID;
        this._ParentID = parentID;
        this._Type = 0;
    }

    InventoryFolder(InventoryManager manager, String name, LLUUID folderID, LLUUID parentID, byte Type) throws Exception {
        super(manager);
        this._Name = name;
        this._FolderID = folderID;
        this._ParentID = parentID;
        this._Type = Type;
    }

    InventoryFolder(InventoryManager manager, Hashtable htData) throws Exception {
        super(manager);
        this._Name = (String) htData.get("name");
        this._FolderID = new LLUUID((String) htData.get("folder_id"));
        this._ParentID = new LLUUID((String) htData.get("parent_id"));
        this._Type = Byte.parseByte(htData.get("type_default").toString());
    }

    public InventoryFolder CreateFolder(String name) throws Exception {
        return iManager.FolderCreate(name, getFolderID());
    }

    public void Delete() throws Exception {
        iManager.getFolder(this.getParentID()).alContents.remove(this);
        iManager.FolderRemove(this);
    }

    public void MoveTo(InventoryFolder newParent) throws Exception {
        MoveTo(newParent.getFolderID());
    }

    public void MoveTo(LLUUID newParentID) throws Exception {
        this.setParentID(newParentID);
    }

    public InventoryNotecard NewNotecard(String name, String description, String body) throws Exception {
        return iManager.NewNotecard(name, description, body, this.getFolderID());
    }

    public InventoryImage NewImage(String name, String description, byte[] j2cdata) throws Exception {
        return iManager.NewImage(name, description, j2cdata, this.getFolderID());
    }

    public Vector GetItemByName(String name) {
        Vector items = new Vector();
        for (int i = 0; i < alContents.size(); i++) {
            InventoryBase ib = (InventoryBase) alContents.elementAt(i);
            if (ib instanceof InventoryFolder) {
                items.addElement(((InventoryFolder) ib).GetItemByName(name));
            } else if (ib instanceof InventoryItem) {
                if (((InventoryItem) ib).getName().equals(name)) {
                    items.addElement(ib);
                }
            }
        }
        return items;
    }

    public String toXML(boolean outputAssets) throws Exception {
        String output = "<folder ";
        output += "name = '" + xmlSafe(getName()) + "' ";
        output += "uuid = '" + getFolderID() + "' ";
        output += "parent = '" + getParentID() + "' ";
        output += "Type = '" + getType() + "' ";
        output += ">\n";
        for (int i = 0; i < alContents.size(); i++) {
            Object oContent = alContents.elementAt(i);
            output += ((InventoryBase) oContent).toXML(outputAssets);
        }
        output += "</folder>\n";
        return output;
    }
}
