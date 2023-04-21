package de.ibk.ods.core;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.asam.ods.DataType;
import de.ibk.ods.basemodel.BaseModel;
import de.ibk.ods.core.sql.SqlHelper;

/**
 * @author Reinhard Kessler, Ingenieurbüro Kessler
 * @version 5.0.0
 */
public class SvcAttr {

    /**
	 * 
	 */
    private Logger log = LogManager.getLogger("de.ibk.ods.openaos");

    /**
	 * 
	 */
    private Kernel kernel;

    /**
	 * @return Returns the kernel.
	 */
    public Kernel getKernel() {
        return kernel;
    }

    /**
	 * @param kernel The kernel to set.
	 */
    public void setKernel(Kernel kernel) {
        this.kernel = kernel;
    }

    /**
	 * 
	 */
    private SvcEnt svcEnt;

    /**
	 * @return Returns the svcent.
	 */
    public SvcEnt getSvcEnt() {
        return svcEnt;
    }

    /**
	 * 
	 */
    private int aid;

    /**
	 * @return Returns the aid.
	 */
    public int getAid() {
        return aid;
    }

    /**
	 * @param aid The aid to set.
	 */
    public void setAid(int aid) {
        if (aid != this.aid) {
            String sql = SqlHelper.format("update SVCATTR set AID=%s where AID=%s and ATTRNR=%s", new String[] { Integer.toString(aid), Integer.toString(this.aid), Integer.toString(attrnr) });
            this.update(sql);
            this.aid = aid;
        }
    }

    /**
	 * 
	 */
    private int attrnr;

    /**
	 * @return Returns the attrnr.
	 */
    public int getAttrnr() {
        return attrnr;
    }

    /**
	 * 
	 */
    private String aaname;

    /**
	 * @return Returns the aaname.
	 */
    public String getAaname() {
        return aaname;
    }

    /**
	 * @param aaname The aaname to set.
	 */
    public void setAaname(String aaname) {
        if (!aaname.equals(this.aaname)) {
            String sql = SqlHelper.format("update SVCATTR set AANAME='%s' where AID=%s and ATTRNR=%s", new String[] { aaname, Integer.toString(aid), Integer.toString(attrnr) });
            this.update(sql);
            this.aaname = aaname;
        }
    }

    /**
	 * 
	 */
    private String baname;

    /**
	 * @return Returns the baname.
	 */
    public String getBaname() {
        return baname;
    }

    /**
	 * @param baname The baname to set.
	 */
    public void setBaname(String baname) {
        if (!baname.equals(this.baname)) {
            String sql = SqlHelper.format("update SVCATTR set BANAME='%s' where AID=%s and ATTRNR=%s", new String[] { baname, Integer.toString(aid), Integer.toString(attrnr) });
            this.update(sql);
            ApplElem applElem = this.kernel.getApplicationStructureValue().getApplElem(this.aid);
            if ("AoLocalColumn".equals(applElem.getBename())) {
                if ("flags".equals(baname) || "values".equals(baname)) {
                    sql = SqlHelper.format("alter table %s drop column %s", new String[] { applElem.getSvcent().getDbtname(), this.dbcname });
                    this.update(sql);
                    sql = SqlHelper.format("update SVCATTR set DBCNAME='NULL' where AID=%s and ATTRNR=%s", new String[] { Integer.toString(aid), Integer.toString(attrnr) });
                    this.update(sql);
                }
            }
            this.baname = baname;
        }
    }

    /**
	 * 
	 */
    private int faid;

    /**
	 * @return Returns the faid.
	 */
    public int getFaid() {
        return faid;
    }

    /**
	 * @param faid The faid to set.
	 */
    public void setFaid(int faid) {
        if (faid != this.faid) {
            String sql = SqlHelper.format("update SVCATTR set FAID=%s where AID=%s and ATTRNR=%s", new String[] { Integer.toString(faid), Integer.toString(aid), Integer.toString(attrnr) });
            this.update(sql);
            this.faid = faid;
        }
    }

    /**
	 * 
	 */
    private int funit;

    /**
	 * @return Returns the funit.
	 */
    public int getFunit() {
        return funit;
    }

    /**
	 * @param funit The funit to set.
	 */
    public void setFunit(int funit) {
        if (funit != this.funit) {
            String sql = SqlHelper.format("update SVCATTR set FUNIT=%s where AID=%s and ATTRNR=%s", new String[] { Integer.toString(funit), Integer.toString(aid), Integer.toString(attrnr) });
            this.update(sql);
            this.funit = funit;
        }
    }

    /**
	 * 
	 */
    private int adtype;

    /**
	 * @return Returns the adtype.
	 */
    public int getAdtype() {
        return adtype;
    }

    /**
	 * @param adtype The adtype to set.
	 */
    public void setAdtype(int adtype) {
        if (adtype != this.adtype) {
            try {
                String dbtname = kernel.getApplicationStructureValue().getApplElem(this.aid).getSvcent().getDbtname();
                DatabaseMetaData dmd = kernel.getConnection().getMetaData();
                ResultSet rs = dmd.getColumns(null, null, dbtname, this.dbcname);
                boolean colExists = rs.next();
                rs.close();
                if (!colExists) {
                    rs = dmd.getColumns(null, null, dbtname + "_ARRAY", this.dbcname);
                    if (rs.next()) {
                        colExists = true;
                        dbtname += "_ARRAY";
                    }
                    rs.close();
                }
                HashMap typeMap = (HashMap) kernel.getTypeMap();
                String type = (String) typeMap.get(BaseModel.getBaseEnum("DataType").getItem(adtype).name);
                if (type != null) {
                    int index = type.indexOf("%d");
                    if (index != -1) {
                        type = type.substring(0, index) + Integer.toString(this.aflen) + ")";
                    }
                    String sql = null;
                    if (colExists) {
                        HashMap sqlMap = (HashMap) kernel.getSqlMap();
                        String s = (String) sqlMap.get("ALTER_TABLE_MODIFY_COLUMN");
                        if (s != null) {
                            sql = SqlHelper.format(s, new String[] { dbtname, dbcname, type });
                        }
                    } else {
                        switch(adtype) {
                            case DataType._DT_STRING:
                            case DataType._DT_SHORT:
                            case DataType._DT_FLOAT:
                            case DataType._DT_BOOLEAN:
                            case DataType._DT_BYTE:
                            case DataType._DT_LONG:
                            case DataType._DT_DOUBLE:
                            case DataType._DT_LONGLONG:
                            case DataType._DT_ID:
                            case DataType._DT_DATE:
                            case DataType._DT_BYTESTR:
                                break;
                            case DataType._DT_BLOB:
                                break;
                            case DataType._DT_COMPLEX:
                            case DataType._DT_DCOMPLEX:
                            case DataType._DS_STRING:
                            case DataType._DS_SHORT:
                            case DataType._DS_FLOAT:
                            case DataType._DS_BOOLEAN:
                            case DataType._DS_BYTE:
                            case DataType._DS_LONG:
                            case DataType._DS_DOUBLE:
                            case DataType._DS_LONGLONG:
                            case DataType._DS_COMPLEX:
                            case DataType._DS_DCOMPLEX:
                            case DataType._DS_ID:
                            case DataType._DS_DATE:
                            case DataType._DS_BYTESTR:
                            case DataType._DT_EXTERNALREFERENCE:
                            case DataType._DS_EXTERNALREFERENCE:
                                dbtname += "_ARRAY";
                                break;
                            case DataType._DT_ENUM:
                                break;
                            case DataType._DS_ENUM:
                                dbtname += "_ARRAY";
                                break;
                            default:
                                break;
                        }
                        String altertableaddcol = (String) kernel.getSqlMap().get("ALTER_TABLE_ADD_COLUMN");
                        if (altertableaddcol == null) {
                            sql = SqlHelper.format("alter table %s add (%s %s)", new String[] { dbtname, dbcname, type });
                        } else {
                            sql = SqlHelper.format(altertableaddcol, new String[] { dbtname, dbcname, type });
                        }
                    }
                    if (sql != null) {
                        kernel.getQueryHandler().executeUpdate(sql);
                    }
                    sql = SqlHelper.format("update SVCATTR set ADTYPE=%s where AID=%s and ATTRNR=%s", new String[] { Integer.toString(adtype), Integer.toString(this.aid), Integer.toString(this.attrnr) });
                    kernel.getQueryHandler().executeUpdate(sql);
                    this.adtype = adtype;
                }
            } catch (SQLException e) {
                log.fatal(e.getMessage());
            }
        }
    }

    /**
	 * 
	 */
    private int aflen;

    /**
	 * @return Returns the aflen.
	 */
    public int getAflen() {
        return aflen;
    }

    /**
	 * @param aflen The aflen to set.
	 */
    public void setAflen(int aflen) {
        if (aflen != this.aflen) {
            String sql = SqlHelper.format("update SVCATTR set AFLEN=%s where AID=%s and ATTRNR=%s", new String[] { Integer.toString(aflen), Integer.toString(aid), Integer.toString(attrnr) });
            this.update(sql);
            switch(this.adtype) {
                case DataType._DS_DATE:
                case DataType._DS_STRING:
                case DataType._DT_DATE:
                case DataType._DT_STRING:
                    String dbtname = kernel.getApplicationStructureValue().getApplElem(this.aid).getSvcent().getDbtname();
                    try {
                        DatabaseMetaData dmd = kernel.getConnection().getMetaData();
                        ResultSet rs = dmd.getColumns(null, null, dbtname, this.dbcname);
                        boolean colExists = rs.next();
                        rs.close();
                        if (!colExists) {
                            rs = dmd.getColumns(null, null, dbtname + "_ARRAY", this.dbcname);
                            if (rs.next()) {
                                colExists = true;
                                dbtname += "_ARRAY";
                            }
                            rs.close();
                        }
                        if (colExists) {
                            HashMap typeMap = (HashMap) kernel.getTypeMap();
                            String type = (String) typeMap.get(BaseModel.getBaseEnum("DataType").getItem(adtype).name);
                            if (type != null) {
                                int index = type.indexOf("%d");
                                if (index != -1) {
                                    type = type.substring(0, index) + Integer.toString(aflen) + ")";
                                }
                                HashMap sqlMap = (HashMap) kernel.getSqlMap();
                                String s = (String) sqlMap.get("ALTER_TABLE_MODIFY_COLUMN");
                                if (s != null) {
                                    sql = SqlHelper.format(s, new String[] { dbtname, dbcname, type });
                                    kernel.getQueryHandler().executeUpdate(sql);
                                }
                            }
                        }
                    } catch (SQLException e) {
                        log.fatal(e.getMessage());
                    }
                    break;
                default:
                    break;
            }
            this.aflen = aflen;
        }
    }

    /**
	 * 
	 */
    private String dbcname;

    /**
	 * @return Returns the dbcname.
	 */
    public String getDbcname() {
        return dbcname;
    }

    /**
	 * 
	 */
    private int aclref;

    /**
	 * @return Returns the aclref.
	 */
    public int getAclref() {
        return aclref;
    }

    /**
	 * @param aclref The aclref to set.
	 */
    public void setAclref(int aclref) {
        if (aclref != this.aclref) {
            String sql = SqlHelper.format("update SVCATTR set ACLREF=%s where AID=%s and ATTRNR=%s", new String[] { Integer.toString(aclref), Integer.toString(aid), Integer.toString(attrnr) });
            this.update(sql);
            this.aclref = aclref;
        }
    }

    /**
	 * 
	 */
    private String invname;

    /**
	 * @return Returns the invname.
	 */
    public String getInvname() {
        return invname;
    }

    /**
	 * @param invname The invname to set.
	 */
    public void setInvname(String invname) {
        if (!invname.equals(this.invname)) {
            String sql = SqlHelper.format("update SVCATTR set INVNAME='%s' where AID=%s and ATTRNR=%s", new String[] { invname, Integer.toString(aid), Integer.toString(attrnr) });
            this.update(sql);
            this.invname = invname;
        }
    }

    /**
	 * 
	 */
    private int flag;

    /**
	 * @return Returns the flag.
	 */
    public int getFlag() {
        return flag;
    }

    /**
	 * @param flag The flag to set.
	 */
    public void setFlag(int flag) {
        if (flag != this.flag) {
            String sql = SqlHelper.format("update SVCATTR set FLAG=%s where AID=%s and ATTRNR=%s", new String[] { Integer.toString(flag), Integer.toString(aid), Integer.toString(attrnr) });
            this.update(sql);
            this.flag = flag;
        }
    }

    /**
	 * 
	 */
    private String enumname;

    /**
	 * @return Returns the enumname.
	 */
    public String getEnumname() {
        return enumname;
    }

    /**
	 * @param enumname The enumname to set.
	 */
    public void setEnumname(String enumname) {
        if (!enumname.equals(this.enumname)) {
            String sql = SqlHelper.format("update SVCATTR set ENUMNAME='%s' where AID=%s and ATTRNR=%s", new String[] { enumname, Integer.toString(aid), Integer.toString(attrnr) });
            this.update(sql);
            this.enumname = enumname;
        }
    }

    /**
	 * @param aid
	 * @param attrnr
	 * @param aaname
	 * @param baname
	 * @param faid
	 * @param funit
	 * @param adtype
	 * @param aflen
	 * @param dbcname
	 * @param aclref
	 * @param invname
	 * @param flag
	 * @param enumname
	 */
    public SvcAttr(int aid, int attrnr, String aaname, String baname, int faid, int funit, int adtype, int aflen, String dbcname, int aclref, String invname, int flag, String enumname) {
        super();
        this.aid = aid;
        this.attrnr = attrnr;
        this.aaname = aaname;
        this.baname = baname;
        this.faid = faid;
        this.funit = funit;
        this.adtype = adtype;
        this.aflen = aflen;
        this.dbcname = dbcname;
        this.aclref = aclref;
        this.invname = invname;
        this.flag = flag;
        this.enumname = enumname;
    }

    /**
	 * 
	 * @param sql
	 */
    private void update(String sql) {
        try {
            Statement stmt = kernel.getConnection().createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            log.fatal(e.getMessage());
        }
    }
}
