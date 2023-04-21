package panda.metainfo;

import static java.sql.Types.VARCHAR;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import panda.query.struct.Attribute;
import panda.query.struct.SemAttribute;
import panda.record.FieldInfo;
import panda.record.RecordFile;
import panda.record.Schema;
import panda.record.TableToken;
import panda.server.Panda;
import panda.transaction.Transaction;

public class TableManager {

    public static final int MAXNAMESIZE = 16;

    private Map<String, TokenCache> cache;

    private TableToken tblInfo, rdInfo;

    /**
	 * 
	 * @param isNew
	 *             If the datebase is a new database
	 * @param tx
	 *             The transaction of the system 
	 */
    TableManager(boolean isNew, Transaction tx) {
        Schema tblSch = new Schema();
        tblSch.addIntAttr(new SemAttribute("recordlen"));
        tblSch.addAttribute(new SemAttribute("tablename"), VARCHAR, MAXNAMESIZE);
        tblInfo = new TableToken("tblInfo", tblSch);
        Schema rdSch = new Schema();
        rdSch.addAttribute(new SemAttribute("tablename"), VARCHAR, MAXNAMESIZE);
        rdSch.addAttribute(new SemAttribute("fieldname"), VARCHAR, MAXNAMESIZE);
        rdSch.addIntAttr(new SemAttribute("type"));
        rdSch.addIntAttr(new SemAttribute("length"));
        rdSch.addIntAttr(new SemAttribute("offset"));
        rdSch.addIntAttr(new SemAttribute("id"));
        rdInfo = new TableToken("rdInfo", rdSch);
        if (isNew) {
            createTable("tblInfo", tblSch, tx);
            createTable("rdInfo", rdSch, tx);
        }
        updateCache(tx);
    }

    public static long readTableTime = 0;

    public void dropTable(String tbl, Transaction tx) {
        if (!tbl.equals("tblInfo") && !tbl.equals("rdInfo")) return;
        Panda.getFileManager().delete(tbl);
        RecordFile tblRf = new RecordFile(tblInfo, tx);
        tblRf.insert();
        while (tblRf.next()) if (tblRf.getString("tablename").equals(tbl)) tblRf.delete();
        tblRf.close();
        RecordFile rdRf = new RecordFile(rdInfo, tx);
        while (rdRf.next()) if (rdRf.getString("tablename").equals(tbl)) rdRf.delete();
        rdRf.close();
        updateCache(tx);
    }

    public void createTable(String tbl, Schema sch, Transaction tx) {
        TableToken tt = new TableToken(tbl, sch);
        RecordFile tblRf = new RecordFile(tblInfo, tx);
        tblRf.insert();
        tblRf.setString("tablename", tbl);
        tblRf.setInt("recordlen", tt.getTupleLength());
        tblRf.close();
        RecordFile rdRf = new RecordFile(rdInfo, tx);
        for (Attribute fldname : sch.getAllAttributes()) {
            rdRf.insert();
            rdRf.setString("tablename", tbl);
            rdRf.setString("fieldname", fldname.getAttributeName());
            rdRf.setInt("type", sch.getType(fldname));
            rdRf.setInt("length", sch.getLength(fldname));
            rdRf.setInt("offset", tt.offsetOf(fldname.getAttributeName()));
            rdRf.setInt("id", sch.IDof(fldname.getAttributeName()));
        }
        rdRf.close();
        if (!tbl.equals("tblInfo") && !tbl.equals("rdInfo")) updateCache(tx);
    }

    public TableToken getTableToken(String tbl, Transaction tx) {
        readTableTime -= System.currentTimeMillis();
        TokenCache tc = cache.get(tbl);
        Map<String, Attribute> amap = new HashMap<String, Attribute>();
        Map<Attribute, String> nmap = new HashMap<Attribute, String>();
        Map<Attribute, FieldInfo> smap = new HashMap<Attribute, FieldInfo>();
        Map<Attribute, Integer> fieldID = new HashMap<Attribute, Integer>();
        for (String fn : tc.fields) {
            SemAttribute sa = new SemAttribute(fn);
            amap.put(fn, sa);
            smap.put(sa, tc.info.get(fn));
            nmap.put(sa, fn);
            fieldID.put(sa, tc.id.get(fn));
        }
        Schema sch = new Schema(nmap, amap, fieldID, smap);
        readTableTime += System.currentTimeMillis();
        return new TableToken(tbl, sch, tc.offset, tc.reclen);
    }

    public void updateCache(Transaction tx) {
        cache = new HashMap<String, TokenCache>();
        RecordFile tblRf = new RecordFile(tblInfo, tx);
        while (tblRf.next()) {
            TokenCache tc = new TokenCache();
            tc.reclen = tblRf.getInt("recordlen");
            tc.fields = new ArrayList<String>();
            tc.id = new HashMap<String, Integer>();
            tc.offset = new HashMap<String, Integer>();
            tc.info = new HashMap<String, FieldInfo>();
            cache.put(tblRf.getString("tablename"), tc);
        }
        tblRf.close();
        System.out.println();
        RecordFile rdRf = new RecordFile(rdInfo, tx);
        while (rdRf.next()) {
            TokenCache tc = cache.get(rdRf.getString("tablename"));
            String field = rdRf.getString("fieldname");
            int ty = rdRf.getInt("type");
            int len = rdRf.getInt("length");
            FieldInfo fi = new FieldInfo(ty, len);
            int id = rdRf.getInt("id");
            int offset = rdRf.getInt("offset");
            tc.fields.add(field);
            tc.id.put(field, id);
            tc.offset.put(field, offset);
            tc.info.put(field, fi);
        }
        rdRf.close();
    }

    Collection<String> getAllTables(Transaction tx) {
        if (cache.keySet() == null) {
            updateCache(tx);
        }
        return cache.keySet();
    }

    public class TokenCache {

        public Map<String, Integer> offset;

        public Map<String, Integer> id;

        public Map<String, FieldInfo> info;

        public List<String> fields;

        public int reclen;
    }
}
