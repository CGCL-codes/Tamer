package org.compiere.minigrid;

/**
 *  Info Column Details
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ColumnInfo.java,v 1.3 2006/07/30 00:51:28 jjanke Exp $
 */
public class ColumnInfo {

    /**
	 *  Create Info Column (r/o and not color column)
	 *
	 *  @param colHeader Column Header
	 *  @param colSQL    SQL select code for column
	 *  @param colClass  class of column - determines display
	 */
    public ColumnInfo(String colHeader, String colSQL, Class colClass) {
        this(colHeader, colSQL, colClass, true, false, null);
    }

    /**
	 *  Create Info Column (r/o and not color column)
	 *
	 *  @param colHeader Column Header
	 *  @param colSQL    SQL select code for column
	 *  @param colClass  class of column - determines display
	 *  @param keyPairColSQL  SQL select for the ID of the for the displayed column
	 */
    public ColumnInfo(String colHeader, String colSQL, Class colClass, String keyPairColSQL) {
        this(colHeader, colSQL, colClass, true, false, keyPairColSQL);
    }

    /**
	 *  Create Info Column
	 *
	 *  @param colHeader Column Header
	 *  @param colSQL    SQL select code for column
	 *  @param colClass  class of column - determines display
	 *  @param readOnly  column is read only
	 *  @param colorColumn   if true, value of column determines foreground color
	 *  @param keyPairColSQL  SQL select for the ID of the for the displayed column
	 */
    public ColumnInfo(String colHeader, String colSQL, Class colClass, boolean readOnly, boolean colorColumn, String keyPairColSQL) {
        setColHeader(colHeader);
        setColSQL(colSQL);
        setColClass(colClass);
        setReadOnly(readOnly);
        setColorColumn(colorColumn);
        setKeyPairColSQL(keyPairColSQL);
    }

    private String m_colHeader;

    private String m_colSQL;

    private Class m_colClass;

    private boolean m_readOnly;

    private boolean m_colorColumn;

    private String m_keyPairColSQL = "";

    /**
	 * 	Get Col Class
	 *	@return class
	 */
    public Class getColClass() {
        return m_colClass;
    }

    /**
	 * 	Get Col Header
	 *	@return header
	 */
    public String getColHeader() {
        return m_colHeader;
    }

    /**
	 * 	Get Col SQL
	 *	@return sql
	 */
    public String getColSQL() {
        return m_colSQL;
    }

    /**
	 * 	ReadOnly
	 *	@return r/o
	 */
    public boolean isReadOnly() {
        return m_readOnly;
    }

    /**
	 * 	Set ColClass
	 *	@param colClass class
	 */
    public void setColClass(Class colClass) {
        m_colClass = colClass;
    }

    /**
	 * 	Set Col Header
	 *	@param colHeader header
	 */
    public void setColHeader(String colHeader) {
        m_colHeader = colHeader;
    }

    /**
	 * 	Set Col SQL
	 *	@param colSQL sql
	 */
    public void setColSQL(String colSQL) {
        m_colSQL = colSQL;
    }

    /**
	 * 	Set Read Only
	 *	@param readOnly r/o
	 */
    public void setReadOnly(boolean readOnly) {
        m_readOnly = readOnly;
    }

    /**
	 * 	Set Color Column
	 *	@param colorColumn color
	 */
    public void setColorColumn(boolean colorColumn) {
        m_colorColumn = colorColumn;
    }

    /**
	 * 	ColorColumn
	 *	@return true if color column
	 */
    public boolean isColorColumn() {
        return m_colorColumn;
    }

    /**
	 *  Add ID column SQL for the displayed column
	 *  The Class for this should be KeyNamePair
	 *  @param keyPairColSQL
	 */
    public void setKeyPairColSQL(String keyPairColSQL) {
        m_keyPairColSQL = keyPairColSQL;
        if (m_keyPairColSQL == null) m_keyPairColSQL = "";
    }

    /**
	 * 	Get Key Pair Col SQL
	 *	@return sql
	 */
    public String getKeyPairColSQL() {
        return m_keyPairColSQL;
    }

    /**
	 * 	Key Pair Col
	 *	@return column
	 */
    public boolean isKeyPairCol() {
        return m_keyPairColSQL.length() > 0;
    }
}
