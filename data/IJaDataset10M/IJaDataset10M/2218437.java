package net.sf.JRecord.Details;

import java.util.List;
import net.sf.JRecord.Common.FieldDetail;

public interface AbstractLayoutDetails<FieldDtls extends FieldDetail, RecordDtls extends AbstractRecordDetail<FieldDtls>> {

    /**
	 * get a specified field
	 *
	 * @param layoutIdx the specific record layout to be used
	 * @param fieldIdx field index required
	 * @return the required field
	 */
    public abstract FieldDtls getField(final int layoutIdx, final int fieldIdx);

    /**
	 * Get the field Description array
	 *
	 * @param layoutIdx layout that we want the description for
	 * @return all the descriptions
	 */
    public abstract String[] getFieldDescriptions(final int layoutIdx, int columnsToSkip);

    /**
	 * Get the records Description.
	 *
	 * @return The description
	 */
    public abstract String getDescription();

    /**
	 * Get the record-layout name
	 *
	 * @return record layout name
	 */
    public abstract String getLayoutName();

    /**
	 * Add a record to the layout
	 * @param record new record
	 */
    public abstract void addRecord(RecordDtls record);

    /**
	 * get a specific field number
	 *
	 * @param recordNum record number to retrieve
	 *
	 * @return a specific record layout
	 */
    public abstract RecordDtls getRecord(int recordNum);

    /**
	 * get number of records in the layout
	 *
	 *
	 * @return the number of records in the layout
	 */
    public abstract int getRecordCount();

    /**
	 * get record type
	 *
	 * @return the Record Type
	 */
    public abstract int getLayoutType();

    /**
	 * Get the record Seperator bytes
	 *
	 * @return Record Seperator
	 */
    public abstract byte[] getRecordSep();

    /**
	 * wether it is a binary record
	 *
	 * @return wether it is a binary record
	 */
    public abstract boolean isBinary();

    /**
	 * Get the Canonical Name (ie Font name)
	 *
	 * @return Canonical Name (ie Font name)
	 */
    public abstract String getFontName();

    /**
	 * Get the seperator String
	 *
	 * @return end of line string
	 */
    public abstract String getEolString();

    /**
	 * Get the maximum length of the Layout
	 *
	 * @return the maximum length
	 */
    public abstract int getMaximumRecordLength();

    /**
	 * Return the file structure
	 *
	 * @return file structure
	 */
    public abstract int getFileStructure();

    /**
	 * Get the Index of a specific record (base on name)
	 *
	 * @param recordName record name being searched for
	 *
	 * @return index of the record
	 */
    public abstract int getRecordIndex(String recordName);

    /**
	 * Get the Record Decider class (if present)
	 * @return Returns the record layout decider.
	 */
    public abstract RecordDecider getDecider();

    /**
	 * Get a fields value
	 *
	 * @param record record containg the field
	 * @param type type to use when getting the field
	 * @param field field to retrieve
	 *
	 * @return fields Value
	 */
    public abstract Object getField(final byte[] record, int type, FieldDtls field);

    /**
	 * Get a field for a supplied field-name
	 *
	 * @param fieldName name of the field being requested
	 *
	 * @return field definition for the supplied name
	 */
    public abstract FieldDtls getFieldFromName(String fieldName);

    /**
	 * get the field delimiter
	 * @return the field delimeter
	 */
    public abstract String getDelimiter();

    /**
	 * get the field delimiter
	 * @return the field delimeter
	 */
    public abstract byte[] getDelimiterBytes();

    /**
	 /**
	 * This is a function used by the RecordEditor to get a field using
	 * recalculated columns. Basically for XML copybooks,
	 * the End and FollowingText columns
	 * are moved from from 2 and 3 to  the end of the record.
	 * Function probably does not belong here but as good as any spot.
	 *
	 * @param layoutIdx  record index
	 * @param fieldIdx field index
	 * @return requested field
	 */
    public abstract FieldDtls getAdjField(int layoutIdx, int fieldIdx);

    /**
	 * This is a function used by the RecordEditor to recalculate
	 * columns. Basically for XML copybooks, the End and FollowingText
	 * columns are moved from from 2 and 3 to  the end of the record.
	 * Function probably does not belong here but as good as any spot.
	 *
	 * @param recordIndex record index
	 * @param inColumn input column
	 * @return adjusted column
	 */
    public abstract int getAdjFieldNumber(int recordIndex, int inColumn);

    public abstract int getUnAdjFieldNumber(int recordIndex, int inColumn);

    /**
	 * Wether this is an XML Layout
	 * @return is it an XML layout
	 */
    public abstract boolean isXml();

    /**
	 * Wether it is ok to add Attributes to this layout
	 * @return Wether it is ok to add Attributes to this layout
	 */
    public abstract boolean isOkToAddAttributes();

    /**
	 * determine wether the layout is built or not
	 * @return determine wether the layout is built or not
	 */
    public abstract boolean isBuildLayout();

    /**
	 * check if a Tree Structure has been defined for the layout
	 * i.e. is there a hierarchy between the various layouts
	 * @return wether there is a Tree Structure defined
	 */
    public abstract boolean hasTreeStructure();

    public abstract boolean isBinCSV();

    /**
	 * @return the allowChildren
	 */
    public abstract boolean hasChildren();

    /**
	 * Whether layout has Maps (Key / Data sets);
	 * @return Whether layout has Maps (Key / Data sets);
	 */
    public abstract boolean isMapPresent();

    /**
	 * @param allowChildren the allowChildren to set
	 */
    public abstract void setAllowChildren(boolean allowChildren);

    /**
	 * Get Filtered Layout
	 * @param filter layout filter
	 * @return Filtered (Cut down) Layout
	 */
    public AbstractLayoutDetails<FieldDtls, RecordDtls> getFilteredLayout(List<RecordFilter> filter);
}
