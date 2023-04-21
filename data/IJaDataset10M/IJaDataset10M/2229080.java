package com.incendiaryblue.cmslite;

import javax.persistence.MappedSuperclass;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;
import java.io.Serializable;

/**
 * Varchar data entity backing
 * @author guy
 */
@MappedSuperclass
public class DataVarchar implements Serializable, ContentData {

    public static String GET_DATA_VARCHAR = "getDataVarchar";

    @EmbeddedId
    private DataPK dataPK;

    @Column(name = "DATA")
    private String data;

    public DataVarchar() {
    }

    public DataVarchar(Integer contentId, Integer structureItemId, String data) {
        this.dataPK = new DataPK(contentId, structureItemId);
        this.data = data;
    }

    public Integer getContentId() {
        return dataPK.contentId;
    }

    public Integer getStructureItemId() {
        return dataPK.structureItemId;
    }

    public String getData() {
        return data;
    }
}
