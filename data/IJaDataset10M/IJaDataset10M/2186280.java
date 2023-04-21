package org.openbroad.shared;

import java.util.*;
import java.io.Serializable;

/**
 * The File class represent an record in the DB.
 * @version 0.3.1
 *
 * @jboss-net.xml-schema urn="OpenBroad:File"
 */
public class File implements Serializable {

    /**
    *
    * @param id
    * @param title
    * @param length
    * @param startDate
    * @param endDate
    * @param description
    * @param created
    * @param userName
    * @param typeId
    */
    public File(int id, String title, int length, Calendar startDate, Calendar endDate, String description, Calendar created, String userName, int typeId) {
        this.id = id;
        this.title = title;
        this.length = length;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.created = created;
        this.userName = userName;
        this.typeId = typeId;
        fileinfos = new Hashtable();
        infoChangeListeners = new Vector();
    }

    public File() {
        fileinfos = new Hashtable();
        infoChangeListeners = new Vector();
    }

    public File(int id, String title) {
        this.id = id;
        this.title = title;
        fileinfos = new Hashtable();
        infoChangeListeners = new Vector();
    }

    /**
    *
    * @return
    */
    public int getId() {
        return id;
    }

    /**
    *
    * @return
    */
    public String getTitle() {
        return title;
    }

    /**
    *
    * @return
    */
    public int getLength() {
        return length;
    }

    /**
    *
    * @return
    */
    public Calendar getStartDate() {
        return startDate;
    }

    /**
    *
    * @return
    */
    public Calendar getEndDate() {
        return endDate;
    }

    /**
    *
    * @return
    */
    public String getDescription() {
        return description;
    }

    /**
    *
    * @return
    */
    public Calendar getCreated() {
        return created;
    }

    /**
    *
    * @return
    */
    public String getUserName() {
        return userName;
    }

    /**
    *
    * @return
    */
    public int getTypeId() {
        return typeId;
    }

    /**
    * Add an info to the file. It then informs all listeners about the addition.
    * If the info is already added, it's not added and NO listerners is informed.
    *
    * @param i A reference to an already added to the Filelist.
    */
    public void addInfo(Info i) {
        Integer key = new Integer(i.getInfoType());
        if (fileinfos == null) {
            System.out.println("ØV");
        }
        List keytable = (List) fileinfos.get(key);
        if (keytable == null) {
            keytable = new ArrayList();
            fileinfos.put(key, keytable);
        }
        if (!keytable.contains(i)) {
            keytable.add(i);
            for (int l = 0; l < infoChangeListeners.size(); l++) {
                InfoChangeListener icl = (InfoChangeListener) infoChangeListeners.get(l);
                icl.infoAdded(new InfoChangeEvent(this, i));
            }
        }
    }

    public Hashtable getInfoList() {
        return fileinfos;
    }

    public void setInfoList(Hashtable ht) {
        fileinfos = ht;
    }

    public synchronized void addInfoChangeListener(InfoChangeListener icl) {
        infoChangeListeners.add(icl);
    }

    public synchronized void removeInfoChangeListener(InfoChangeListener icl) {
        infoChangeListeners.remove(icl);
    }

    public String toString() {
        return getTitle();
    }

    /**
    * Setter for property created.
    * @param created New value of property created.
    */
    public void setCreated(Calendar created) {
        this.created = created;
    }

    /**
    * Setter for property description.
    * @param description New value of property description.
    */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * Setter for property endDate.
    * @param endDate New value of property endDate.
    */
    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    /**
    * Setter for property id.
    * @param id New value of property id.
    */
    public void setId(int id) {
        this.id = id;
    }

    /**
    * Setter for property length.
    * @param length New value of property length.
    */
    public void setLength(int length) {
        this.length = length;
    }

    /**
    * Setter for property startDate.
    * @param startDate New value of property startDate.
    */
    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    /**
    * Setter for property title.
    * @param title New value of property title.
    */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
    * Setter for property typeId.
    * @param typeId New value of property typeId.
    */
    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    /**
    * Setter for property userName.
    * @param userName New value of property userName.
    */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    private int id;

    private String title;

    private String userName;

    private int length;

    private int typeId;

    private Calendar startDate;

    private Calendar endDate;

    private String description;

    private Calendar created;

    private Hashtable fileinfos;

    private Vector infoChangeListeners;
}
