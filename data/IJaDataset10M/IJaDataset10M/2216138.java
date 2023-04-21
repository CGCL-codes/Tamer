package ces.coffice.docmanage.vo;

import ces.coffice.common.base.BaseVo;

/**
 *
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class DocConsult extends BaseVo {

    private int id;

    private int docId;

    private String docType;

    private int consultorId;

    private String consultor;

    private long consultDeptId;

    private String consultDept;

    private String consultDate;

    public DocConsult() {
    }

    public DocConsult(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getDocId() {
        return docId;
    }

    public String getDocType() {
        return docType;
    }

    public int getConsultorId() {
        return consultorId;
    }

    public String getConsultor() {
        return consultor;
    }

    public long getConsultDeptId() {
        return consultDeptId;
    }

    public String getConsultDept() {
        return consultDept;
    }

    public String getConsultDate() {
        return consultDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public void setConsultorId(int consultorId) {
        this.consultorId = consultorId;
    }

    public void setConsultor(String consultor) {
        this.consultor = consultor;
    }

    public void setConsultDeptId(long consultDeptId) {
        this.consultDeptId = consultDeptId;
    }

    public void setConsultDept(String consultDept) {
        this.consultDept = consultDept;
    }

    public void setConsultDate(String consultDate) {
        this.consultDate = consultDate;
    }
}
