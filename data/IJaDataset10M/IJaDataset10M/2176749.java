package cn.ekuma.epos.datalogic;

import com.openbravo.data.basic.BasicException;
import com.openbravo.data.loader.I_Session;
import cn.ekuma.data.dao.I_DataLogic;

public interface I_DataLogicSymmetricds extends I_DataLogic {

    void init(I_Session s);

    void reloadNode(String nodeId) throws BasicException;
}
