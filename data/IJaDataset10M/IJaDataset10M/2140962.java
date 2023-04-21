package com.simpledata.bc.reports.base;

import java.util.ArrayList;

/**
 * TableFields is a collection of fields that a JasperReport
 * table contains as seen from the Application. There are 
 * basically two kinds of fields: 
 * <ul>
 *  <li> <i>Fields that contain the normal Jasper types.</i> </li>
 *  <li> <i>Special fields that contain Subreports.</i> These will 
 *       be expanded with the three postfixes 'Show', 'Report' and
 *       'Datasource' on the Jasper level. </li>
 * </ul>
 * Fields will be layed out in the table in the order of addition. 
 * 
 * Once the fields are defined, the definition can be frozen. There
 * is no way to thaw it - but the error raised will only be an 
 * assertion error. 
 */
class TableFields {

    /** Fields storage. */
    private ArrayList m_fields;

    /** Is the field list under construction ? */
    private boolean m_frozen;

    /**
	 * Constructor. 
	 */
    public TableFields() {
        m_fields = new ArrayList();
        m_frozen = false;
    }

    /** 
	 * Add a normal Jasper type field. Jasper types include: 
	 * String, Object, Boolean, Byte, Date, Timestamp, Time, 
	 * Double, Float, Integer, InputStream, Long, Short, 
	 * BigDecimal.
	 */
    public void addJasperField(String name) {
        assert !m_frozen : "Field list must not be frozen yet.";
        if (m_frozen) return;
        m_fields.add(new FieldDefinition(name, FieldDefinition.JASPERTYPE));
    }

    /**
	 * Add a report field. These will 
	 * be expanded with the four postfixes 'Show', 'Report',
	 * 'Datasource' and 'Fields' on the Jasper level. The only value that 
	 * you can store in a field like this is an implementor of 
	 * SubreportTreeItem. 
	 */
    public void addReportField(String name) {
        assert !m_frozen : "Field list must not be frozen yet.";
        if (m_frozen) return;
        m_fields.add(new FieldDefinition(name, FieldDefinition.REPORTTYPE));
    }

    /**
	 * Call this once your field definition is stable 
	 * (meaning finished). This will forbid addition 
	 * of further fields. 
	 */
    public void freeze() {
        m_frozen = true;
    }

    /**
	 * Retrieve the freeze status of this definition. 
	 */
    public boolean isFrozen() {
        return m_frozen;
    }

    /**
	 * Retrieve number of fields defined here. 
	 */
    public int size() {
        assert m_fields != null : "Fields array must be initialized";
        return m_fields.size();
    }

    /**
	 * @return true if the field is a subreport
	 * field. 
	 */
    public boolean isSubreportField(int i) {
        assert m_fields != null : "Fields array must be initialized.";
        assert i < m_fields.size() && i >= 0 : "Index must be within bounds";
        FieldDefinition def = (FieldDefinition) m_fields.get(i);
        return def.type == FieldDefinition.REPORTTYPE;
    }

    /**
	 * Get base name of this field. If the field is a subreport
	 * field, you need to modify the name as described in addReportField
	 * to get your real Jasper Fields.
	 * @param i Index of field to retrieve name for. 
	 */
    public String getBaseName(int i) {
        assert m_fields != null : "Fields array must be initialized.";
        assert i < this.size() && i >= 0 : "i must be in range of the fields array.";
        FieldDefinition def = (FieldDefinition) m_fields.get(i);
        return def.name;
    }

    class FieldDefinition {

        public static final int JASPERTYPE = 1;

        public static final int REPORTTYPE = 2;

        public String name;

        public int type;

        /**
		 * Create a field definition. 
		 * @param name Name for the field. 
		 * @param type Type for the field, one of JASPERTYPE, REPORTTYPE. 
		 */
        public FieldDefinition(String name, int type) {
            this.name = name;
            this.type = type;
        }
    }
}
