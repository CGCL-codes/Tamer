package lk.apiit.friends.persistence.tools;

/**
 * Utility Routine to Generate Database Schema.
 * 
 * @author Yohan Liyanage
 * @version 12-Sep-2008
 * @since 12-Sep-2008
 */
public class SchemaGen {

    public static void main(String[] args) throws Exception {
        SchemaSupport.buildExport().create(false, true);
    }
}
