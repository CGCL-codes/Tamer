package DAO;

import BD.AccesSQL;
import Bus.Ad;
import Bus.Keyword;
import Bus.Tag;
import utils.Utils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author Raimon Bosch
 */
public class DAOKeyword extends DAO {

    public SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    /** Creates a new instance of DAOKeyword */
    public DAOKeyword(AccesSQL acces) {
        super(acces);
    }

    public Vector<Keyword> getCompleteExtraKeywordsOfAd(String id, String type_tag) {
        return getKeywordsOfAd(id, true, true, type_tag);
    }

    public Vector<Keyword> getPartialExtraKeywordsOfAd(String id, String type_tag) {
        return getKeywordsOfAd(id, false, true, type_tag);
    }

    public Vector<Keyword> getCompleteKeywordsOfAd(String id, String type_tag) {
        return getKeywordsOfAd(id, true, false, type_tag);
    }

    public Vector<Keyword> getPartialKeywordsOfAd(String id, String type_tag) {
        return getKeywordsOfAd(id, false, false, type_tag);
    }

    public Vector<Keyword> getKeywordsOfAd(String id, boolean foreign_keys, boolean extra_kw, String type_tag) {
        Keyword k = null;
        Vector keywords = new Vector<Keyword>();
        String select, tables, where, group;
        String str_extra_kw_cond = "";
        if (extra_kw) str_extra_kw_cond = "k.extra_kw=1 AND";
        if (!foreign_keys) {
            select = "t.s_name as s_name";
            tables = "tbl_tags t, tbl_ads a, tbl_keywords k";
            where = "k.fk_i_id_tbl_tags = t.i_id AND k.fk_i_id_tbl_ads = a.i_id AND t.fks_i_id_tbl_types_tag=" + type_tag + " AND t.f_rank > 0 AND " + str_extra_kw_cond + " k.fk_i_id_tbl_ads=" + id;
            group = "t.f_rank desc";
        } else {
            select = "t.s_name as s_name,a.i_id as id_ad,t.i_id as id_tag,k.extra_kw as extra_kw";
            tables = "tbl_tags t, tbl_ads a, tbl_keywords k";
            where = "k.fk_i_id_tbl_tags = t.i_id AND k.fk_i_id_tbl_ads = a.i_id AND t.fks_i_id_tbl_types_tag=" + type_tag + " AND t.f_rank > 0 AND " + str_extra_kw_cond + " k.fk_i_id_tbl_ads=" + id;
            group = "t.f_rank desc";
        }
        List<Map> list = acces.Selecciona(select, tables, where, group);
        for (int i = 0; i < list.size(); i++) {
            Map row = list.get(i);
            if (!foreign_keys) k = new Keyword((String) row.get("s_name")); else k = new Keyword((String) row.get("s_name"), (String) row.get("id_ad"), (String) row.get("id_tag"), (String) row.get("extra_kw"));
            keywords.addElement(k);
        }
        return keywords;
    }

    public Vector<String> getTagsType() {
        Vector<String> type_tags = null;
        List<Map> list = acces.Selecciona("i_id", "tbl_types_tag", "", "");
        if (list.size() > 0) {
            type_tags = new Vector<String>();
            for (int i = 0; i < list.size(); i++) {
                Map row = list.get(i);
                type_tags.add((String) row.get("i_id"));
            }
        }
        return type_tags;
    }

    public Vector<Ad> getAdsFromTag(String name) {
        Tag t = new Tag(name, "1");
        List<Map> list = acces.Selecciona("k.fk_i_id_tbl_ads as id_ad", "tbl_tags t, tbl_keywords k, tbl_ads a", "a.dt_date > '" + date_format.format(new Date()) + "' AND t.s_name like '" + t.getName() + "' AND k.fk_i_id_tbl_tags = t.i_id AND k.fk_i_id_tbl_ads = a.i_id", "");
        if (list.size() > 0) {
            DAOAd da = new DAOAd(this.acces);
            String ids[] = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                ids[i] = (String) list.get(i).get("id_ad");
            }
            return da.getAdsByListIds(ids);
        } else return null;
    }

    public String getIdTagOfKeyword(String name) {
        String idtag = "0";
        List<Map> list = acces.Selecciona("i_id as id_tag", "tbl_tags", "s_name like '" + name + "'", "");
        if (list.size() > 0) idtag = (String) list.get(0).get("id_tag");
        return idtag;
    }

    public boolean insertDateAndTitleKeywords(Ad a, String id_ad) {
        DAOTag dt = new DAOTag(this.acces);
        String date = a.getRealDateExternal(false, false).trim();
        int code = dt.insertTag(new Tag(date, "2"));
        if (code > 0) {
            insertKeyword(id_ad, "" + code, false);
        } else if (code == 0) {
            insertKeyword(id_ad, dt.getIdLastInsertedTag(), false);
        } else {
            return false;
        }
        String title = a.getTitle().replace("-", " ").trim();
        code = dt.insertTag(new Tag(title, "4"));
        if (code > 0) {
            insertKeyword(id_ad, "" + code, false);
        } else if (code == 0) {
            insertKeyword(id_ad, dt.getIdLastInsertedTag(), false);
        } else {
            return false;
        }
        return true;
    }

    public int insertContentKeywords(String content, String id_ad) {
        Object keywords[] = Utils.getStrongKeywords(content).toArray();
        HashSet<String> keys = new HashSet<String>();
        DAOTag dt = null;
        for (int i = 0; i < keywords.length; i++) {
            String kw = (String) keywords[i];
            String id_tag = getIdTagOfKeyword(kw);
            if (id_tag.equals("0")) {
                if (dt == null) dt = new DAOTag(this.acces);
                dt.insertTag(new Tag(kw, "1"));
                insertKeyword(id_ad, dt.getIdLastInsertedTag(), false);
            } else {
                insertKeyword(id_ad, id_tag, false);
            }
        }
        return 1;
    }

    public int insertKeyword(String id_ad, String id_tag, boolean extra_kw) {
        String str_extra_kw = "0";
        if (extra_kw) str_extra_kw = "1";
        List<Map> list = acces.Selecciona("fk_i_id_tbl_tags", "tbl_keywords", "fk_i_id_tbl_ads=" + id_ad + " AND fk_i_id_tbl_tags=" + id_tag, "");
        if (list.size() == 0) {
            int before = acces.getNumRows("tbl_keywords");
            acces.Afegeix("tbl_keywords", "fk_i_id_tbl_ads,fk_i_id_tbl_tags,extra_kw,dt_date", id_ad + "," + id_tag + "," + str_extra_kw + ",'" + date_format.format(new Date()) + "'");
            if (before < acces.getNumRows("tbl_keywords")) return 1; else return -1;
        }
        return -2;
    }
}
