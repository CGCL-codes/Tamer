package com.knowgate.hipergate;

import com.knowgate.jdc.JDCConnection;
import com.knowgate.dataobjs.DB;
import com.knowgate.dataobjs.DBPersist;
import java.sql.SQLException;

/**
 * Category Translated Labels
 * @author Sergio Montoro
 * @version 1.0
 */
public class CategoryLabel extends DBPersist {

    /**
   * Create empty label
   */
    public CategoryLabel() {
        super(DB.k_cat_labels, "CategoryLabel");
    }

    /**
   * Load label from database
   * @param sCatId Category GUID
   * @param sLanguage 2 characters language code (see k_lu_languages table)
   */
    public CategoryLabel(String sCatId, String sLanguage) {
        super(DB.k_cat_labels, "CategoryLabel");
        put(DB.gu_category, sCatId);
        put(DB.id_language, sLanguage);
    }

    /**
   * Single Step label Create and Store
   * @param oConn Database Conenction
   * @param Values An Array with values { (String) gu_category, (String) id_language,
   * (String) tr_category, (String) url_category }
   * @throws SQLException
   */
    public static void create(JDCConnection oConn, Object[] Values) throws SQLException {
        CategoryLabel oLbl = new CategoryLabel();
        oLbl.put(DB.gu_category, Values[0]);
        oLbl.put(DB.id_language, Values[1]);
        oLbl.put(DB.tr_category, Values[2]);
        oLbl.put(DB.url_category, Values[3]);
        if (Values.length > 4) oLbl.put(DB.de_category, Values[4]);
        oLbl.store(oConn);
    }

    public static final short ClassId = 11;
}
