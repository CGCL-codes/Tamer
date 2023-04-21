package org.ujac.util.table;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Name: GroupRow<br>
 * Description: A table row for group rows.
 * 
 * @author lauerc
 */
public class GroupRow extends DataRow {

    /** The serial version UID. */
    static final long serialVersionUID = 5453737701006063660L;

    /** The group name. */
    private String groupName = null;

    /**
   * Constructs a GroupRow instance with specific attributes.
   * Sets all fields invisible by default.
   * @param table The table to create the row for.
   * @param type The row type.
   * @param groupName The group name.
   */
    public GroupRow(Table table, String type, String groupName) {
        super(table, -1);
        this.setType(type);
        this.groupName = groupName;
        try {
            int columnCount = table.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                setVisible(i, false);
            }
        } catch (ColumnNotDefinedException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
   * Getter method for the the property groupName.
   * @return The current value of property groupName.
   */
    public String getGroupName() {
        return groupName;
    }

    /**
   * Setter method for the the property groupName.
   * @param groupName The value to set for the property groupName.
   */
    protected void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
   * Writes the object's data to the given stream.
   * @param s The stream to write to
   * @exception IOException In case the data output failed.
   */
    protected void writeData(ObjectOutputStream s) throws IOException {
        super.writeData(s);
        s.writeObject(groupName);
    }

    /**
   * Reads the object's data from the given stream.
   * @param s The stream to read from.
   * @exception IOException In case the data reading failed.
   * @exception ClassNotFoundException In case the class to deserialize could not be found
   */
    protected void readData(ObjectInputStream s) throws IOException, ClassNotFoundException {
        super.readData(s);
        groupName = (String) s.readObject();
    }

    /**
   * Writes the object's data to the given stream.
   * @param s The stream to write to
   * @exception IOException In case the data output failed.
   */
    private void writeObject(ObjectOutputStream s) throws IOException {
        writeData(s);
    }

    /**
   * Reads the object's data from the given stream.
   * @param s The stream to read from.
   * @exception IOException In case the data reading failed.
   * @exception ClassNotFoundException In case the class to deserialize could not be found
   */
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        readData(s);
    }
}
