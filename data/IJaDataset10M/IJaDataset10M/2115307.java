package com.loribel.commons.business.impl.data.generated;

import com.loribel.commons.abstraction.GB_Prototype;
import com.loribel.commons.business.abstraction.GB_BOData;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.commons.business.impl.bo.GB_BOLinkIdBO;
import com.loribel.commons.business.impl.data.GB_BOLinkIdData;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.GB_StringTools;

/**
 * Generated class (simple bean) for BOLinkId.
 */
public abstract class GB_BOLinkIdDataGen implements GB_BOData, java.io.Serializable {

    public static final String BO_NAME = "BOLinkId";

    private String id;

    private String fkIdStr;

    public String boName() {
        return BO_NAME;
    }

    public static final GB_Prototype newPrototype() {
        GB_Prototype retour = new GB_Prototype() {

            public Object newInstance() {
                return new GB_BOLinkIdData();
            }
        };
        return retour;
    }

    public String getId() {
        return id;
    }

    public void setId(String a_id) {
        id = a_id;
    }

    public String getFkIdStr() {
        return fkIdStr;
    }

    public void setFkIdStr(String a_fkIdStr) {
        fkIdStr = a_fkIdStr;
    }

    public void updateFromBO(GB_SimpleBusinessObject a_bo) {
        GB_BOLinkIdBO l_bo = (GB_BOLinkIdBO) a_bo;
        setId(l_bo.getId());
        String[] l_fkId = l_bo.getFkIdArray();
        String l_fkIdStr = GB_StringTools.toString(l_fkId, "|");
        setFkIdStr(l_fkIdStr);
    }

    public void updateBO(GB_SimpleBusinessObject a_bo) {
        GB_BOLinkIdBO l_bo = (GB_BOLinkIdBO) a_bo;
        l_bo.setId(this.getId());
        int len;
        String l_fkId = this.getFkIdStr();
        l_bo.removeAllFkId();
        String[] l_fkIdArray = GB_StringTools.toStringArray(l_fkId, "|");
        len = CTools.getSize(l_fkIdArray);
        for (int i = 0; i < len; i++) {
            l_bo.addFkId(l_fkIdArray[i]);
        }
    }
}
