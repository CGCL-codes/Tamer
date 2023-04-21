package de.ios.kontor.sv.order.co;

import java.util.*;
import java.text.*;
import de.ios.framework.basic.*;
import de.ios.framework.gui.Table;
import de.ios.kontor.sv.basic.co.*;
import de.ios.kontor.sv.main.co.*;

/**
 * Datacarrier for the main Infos about a DeliveryNote 
 *
 * @author fw (Frank Wiesen).
 * @version $Id: DeliveryNoteDC.java,v 1.1.1.1 2004/03/24 23:02:56 nanneb Exp $.
 */
public class DeliveryNoteDC extends BasicDC {

    /** */
    public String custNo = null;

    public String custName = null;

    public Long delNo = null;

    public String delDesc = null;

    public IoSDate delDate = null;

    public IoSDate delFinD = null;

    public String delAddr = null;

    public String comment = null;

    /**
   * Build the RowVector.
   * Defined rowDispVer: 3
   *
   * @return the Vector containing the ListRow.
   */
    public Vector getRowVector() {
        Vector v = new Vector(20);
        if (rowDispVer < 2) v.addElement(toString(custNo));
        if (rowDispVer < 1) v.addElement(toString(custName));
        v.addElement(delNo);
        v.addElement(toString(delDesc));
        v.addElement(delDate);
        v.addElement(delFinD);
        if (rowDispVer < 3) {
            v.addElement(toString(delAddr));
        }
        v.addElement(toString(comment));
        return v;
    }

    /**
   * Return the number of elemnts(columns) returned by @see getRowVector.
   * @param _rowDispVer the same argument as given to getRowVector.
   * @return the number of elements returned by @see getRowVector.
   */
    public static int getColumnCount(int rowDispVer) {
        switch(rowDispVer) {
            case 0:
                return 8;
            case 1:
                return 7;
            case 2:
                return 6;
            case 3:
                return 5;
            default:
                return 0;
        }
    }

    /** Table-Titles (auto-translated). */
    public static String I18N_TTIT_custNo = "Kd-Nr.:";

    public static String I18N_TTIT_custName = "Kunde:";

    public static String I18N_TTIT_delNo = "Nummer:";

    public static String I18N_TTIT_delDesc = "Kurzbez.:";

    public static String I18N_TTIT_delDate = "gedruckt:";

    public static String I18N_TTIT_delFinD = "abgeschlossen:";

    public static String I18N_TTIT_delAddr = "Lieferadresse:";

    public static String I18N_TTIT_comment = "Kommentar:";

    /**
   * Prepares the table for a certain display-version (sets the title an defines the column-modes).
   * @param _t the table to prepare.
   * @param _rowDispVer the same argument as given to getRowVector.
   */
    public static Table prepareTable(Table _t, int rowDispVer) {
        initFormaters();
        switch(rowDispVer) {
            case 0:
                _t.setColumnModes(new int[] { Table.COLUMN_TEXT, Table.COLUMN_TEXT, Table.COLUMN_NUMERIC, Table.COLUMN_TEXT, Table.COLUMN_IOSDATE, Table.COLUMN_IOSDATE, Table.COLUMN_TEXT, Table.COLUMN_TEXT }).setColumnModes(new Format[] { null, null, decFormN, null, null, null, null, null }).setColumnTitles(" " + I18N_TTIT_custNo + " | " + I18N_TTIT_custName + " | " + I18N_TTIT_delNo + " | " + I18N_TTIT_delDesc + " | " + I18N_TTIT_delDate + " | " + I18N_TTIT_delFinD + " | " + I18N_TTIT_delAddr + " | " + I18N_TTIT_comment + " ");
                break;
            case 1:
                _t.setColumnModes(new int[] { Table.COLUMN_TEXT, Table.COLUMN_NUMERIC, Table.COLUMN_TEXT, Table.COLUMN_IOSDATE, Table.COLUMN_IOSDATE, Table.COLUMN_TEXT, Table.COLUMN_TEXT }).setColumnModes(new Format[] { null, decFormN, null, null, null, null, null }).setColumnTitles(" " + I18N_TTIT_custNo + " | " + I18N_TTIT_delNo + " | " + I18N_TTIT_delDesc + " | " + I18N_TTIT_delDate + " | " + I18N_TTIT_delFinD + " | " + I18N_TTIT_delAddr + " | " + I18N_TTIT_comment + " ");
                break;
            case 2:
                _t.setColumnModes(new int[] { Table.COLUMN_NUMERIC, Table.COLUMN_TEXT, Table.COLUMN_IOSDATE, Table.COLUMN_IOSDATE, Table.COLUMN_TEXT, Table.COLUMN_TEXT }).setColumnModes(new Format[] { decFormN, null, null, null, null, null }).setColumnTitles(" " + I18N_TTIT_delNo + " | " + I18N_TTIT_delDesc + " | " + I18N_TTIT_delDate + " | " + I18N_TTIT_delFinD + " | " + I18N_TTIT_delAddr + " | " + I18N_TTIT_comment + " ");
            case 3:
                _t.setColumnModes(new int[] { Table.COLUMN_NUMERIC, Table.COLUMN_TEXT, Table.COLUMN_IOSDATE, Table.COLUMN_IOSDATE, Table.COLUMN_TEXT }).setColumnModes(new Format[] { decFormN, null, null, null, null }).setColumnTitles(" " + I18N_TTIT_delNo + " | " + I18N_TTIT_delDesc + " | " + I18N_TTIT_delDate + " | " + I18N_TTIT_delFinD + " | " + I18N_TTIT_comment + " ");
        }
        return _t;
    }
}

;
