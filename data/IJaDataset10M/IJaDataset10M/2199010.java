package org.project.trunks.data;

import java.util.Comparator;
import org.project.trunks.utilities.*;
import org.project.trunks.data.Field;

/**
  * Cette classe permet la gestion du tri d'un vecteur d'objet Field
  * @author  : MFR
  * @version : 24/02/2004
*/
public class FieldComparator implements Comparator {

    /**
     * contains criterium value
     */
    private FieldType critere;

    /**
		 * Base fields, contains LOV, etc...
		 */
    private Field[] baseFields;

    /**
		 * Fields, contains AUTR...
		 */
    private Field[] fields1;

    /**
		 * Fields, contains AUTR...
		 */
    private Field[] fields2;

    /**
		 * Sort ascending or descending
		 */
    protected boolean ascending;

    /**
     * Constructor
     * @param critere comparison criteria
     */
    public FieldComparator(FieldType critere) {
        this.critere = critere;
        this.ascending = true;
    }

    /**
		 * Constructor
		 * @param critere comparison criteria
		 */
    public FieldComparator(FieldType critere, boolean asc) {
        this.critere = critere;
        this.ascending = asc;
    }

    /**
		 * Constructor
		 * @param critere comparison criteria
		 * @param baseFields baseFields
		 * @param fields fields
		 */
    public FieldComparator(FieldType critere, boolean asc, Field[] baseFields, Field[] fields1, Field[] fields2) {
        this.critere = critere;
        this.ascending = asc;
        this.baseFields = baseFields;
        this.fields1 = fields1;
        this.fields2 = fields2;
    }

    /**
     * Compare two actors according to the criteria
     * @param a1 one actor
     * @param a2 the other actor
     * @return result of the comparison
     */
    protected int compareCritere(Field field1, Field field2) {
        String val1 = "", val2 = "";
        if (fields1 != null && fields1.length > 0 && fields2 != null && fields2.length > 0 && baseFields != null && baseFields.length > 0 && (field1.getKind().equals(FieldKind.PICK_UP) || field1.getKind().equals(FieldKind.SELECT))) {
            val1 = field1.getFieldValue(baseFields, fields1);
            val2 = field2.getFieldValue(baseFields, fields2);
            critere = FieldType.VARCHAR;
        } else {
            val1 = field1.getValue();
            val2 = field2.getValue();
        }
        if (val1 == null && val2 == null) return 0; else if (val1 == null) return 1; else if (val2 == null) return -1;
        if (critere.equals(FieldType.INTEGER)) return StringUtilities.compareIntFromString(val1, val2); else if (critere.equals(FieldType.NUMBER)) return StringUtilities.compareNumberFromString(val1, val2); else if (critere.equals(FieldType.UNKNOWN)) return val1.compareTo(val2); else if (critere.equals(FieldType.VARCHAR)) return val1.compareTo(val2); else if (critere.equals(FieldType.DATE)) return StringUtilities.compareDate(val1, val2, field1.getFormat()); else if (critere.equals(FieldType.BOOLEAN)) return StringUtilities.compareBooleanFromString(val1, val2); else return 0;
    }

    /**
     * Compare two objects according to the list of criteria
     * @param o1 first object to compare
     * @param o2 second object to compare
     * @return result of the comparison
     */
    public int compare(Object o1, Object o2) {
        if ((o1 instanceof Field && o2 instanceof Field) || (o1 instanceof FieldDB && o2 instanceof FieldDB)) {
            Field f1 = (Field) o1;
            Field f2 = (Field) o2;
            int res = compareCritere(f1, f2);
            if (ascending) return res; else return res * (-1);
        }
        return 0;
    }

    /**
   * Redefinition of "equals" method - not implemented
   * @param obj the object to compare
   * @return boolean
   */
    public boolean equals(Object obj) {
        throw new java.lang.UnsupportedOperationException("Method equals() not yet implemented.");
    }
}
