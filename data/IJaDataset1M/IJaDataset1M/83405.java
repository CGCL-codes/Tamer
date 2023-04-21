package org.verus.ngl.sl.bprocess.administration;

import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author verus
 */
public interface Adm_Form_Letter {

    public Integer getMaxFormIdAdm_Form_letter(Integer library_id, String databaseId);

    public void saveAdm_Form_letter(Integer form_id, Integer entry_library_id, String form_letter_no, String form_letter_title, java.util.Date entry_date, String to_id, Integer to_library_id, String print_status, String email_status, String instant_message_status, String to_email_id, Integer library_id, String jve_path, String to_type, int format_id, String entry_id, String DBID);

    public Vector fetchAdm_Form_letter(String databaseId);

    public Vector getByDateAdm_Form_letter(String databaseId, java.sql.Timestamp fromDate, java.sql.Timestamp toDate);

    public Vector getByDateNTypeAdm_Form_letter(String databaseId, java.sql.Timestamp fromDate, java.sql.Timestamp toDate, int format_id);

    public Vector getByTypeAdm_Form_letter(String databaseId, int format_id);
}
