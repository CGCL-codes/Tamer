package org.jaffa.applications.mylife.admin.components.userrolelookup.dto;

import java.util.*;
import org.jaffa.components.finder.*;

/** The output for the UserRoleLookup.
 */
public class UserRoleLookupOutDto extends FinderOutDto {

    /** Holds an array of rows to be returned. */
    private List rows;

    /** Default Constructor.*/
    public UserRoleLookupOutDto() {
        rows = new ArrayList();
    }

    /** Add rows.
     * @param rows Rows.
     */
    public void addRows(UserRoleLookupOutRowDto rows) {
        this.rows.add(rows);
    }

    /** Add rows at the specified position.
     * @param rows Rows.
     * @param index The position at which the rows is to be added.
     */
    public void setRows(UserRoleLookupOutRowDto rows, int index) {
        if ((index < 0) || (index > this.rows.size())) throw new IndexOutOfBoundsException();
        this.rows.set(index, rows);
    }

    /** Add an array of rows. This will overwrite existing rows.
     * @param rows An array of rows.
     */
    public void setRows(UserRoleLookupOutRowDto[] rows) {
        this.rows = Arrays.asList(rows);
    }

    /** Clear existing rows.
     */
    public void clearRows() {
        rows.clear();
    }

    /** Remove rows.
     * @param rows Rows.
     * @return A true indicates a rows was removed. A false indicates, the rows was not found.
     */
    public boolean removeRows(UserRoleLookupOutRowDto rows) {
        return this.rows.remove(rows);
    }

    /** Returns a rows from the specified position.
     * @param index The position index.
     * @return Rows.
     */
    public UserRoleLookupOutRowDto getRows(int index) {
        if ((index < 0) || (index > rows.size())) throw new IndexOutOfBoundsException();
        return (UserRoleLookupOutRowDto) rows.get(index);
    }

    /** Returns an array of rows.
     * @return An array of rows.
     */
    public UserRoleLookupOutRowDto[] getRows() {
        return (UserRoleLookupOutRowDto[]) rows.toArray(new UserRoleLookupOutRowDto[0]);
    }

    /** Returns a count of rows.
     * @return The count of rows.
     */
    public int getRowsCount() {
        return rows.size();
    }

    /** Returns the debug information
     * @return The debug information
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("<UserRoleLookupOutDto>");
        buf.append("<moreRecordsExist>");
        if (getMoreRecordsExist() != null) buf.append(getMoreRecordsExist());
        buf.append("</moreRecordsExist>");
        buf.append("<rows>");
        UserRoleLookupOutRowDto[] rows = getRows();
        for (int i = 0; i < rows.length; i++) {
            buf.append(rows[i].toString());
        }
        buf.append("</rows>");
        buf.append("</UserRoleLookupOutDto>");
        return buf.toString();
    }
}
